package nro.models.mob;

//import nro.consts.ConstDataEvent;
import nro.consts.ConstMap;
import nro.consts.ConstMob;
import nro.consts.ConstTask;
//import nro.consts.SK20_10;
import nro.models.item.Item;
import nro.models.map.ItemMap;

import java.util.HashMap;
import java.util.List;

import nro.models.map.Zone;
import nro.models.player.Location;
import nro.models.player.NPoint;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.reward.ItemMobReward;
import nro.models.reward.MobReward;
import nro.models.skill.PlayerSkill;
import nro.models.skill.Skill;
import nro.network.io.Message;
import nro.server.Maintenance;
import nro.server.Manager;
import nro.services.*;
import nro.utils.Logger;
import nro.utils.Util;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;
import nro.consts.ConstItem;

import nro.jdbc.daos.PlayerDAO;
import nro.server.Client;
import nro.utils.AddOrIncrease;

//public final class Mob {
public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;
    public long idPlayerMaFuBa;
    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;
    public long lastTimeUpdate = System.currentTimeMillis();
    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public void setTiemNang() {
        this.maxTiemNang = (long) this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    public static void initMobBanDoKhoBau(Mob mob, byte level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void initMopbKhiGas(Mob mob, int level) {
        if (level <= 700) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = (level * 12472 * mob.level * 2 + level * 7263 * mob.tempId) * 5;
        }
        if (level > 700 && level <= 10000) {
            mob.point.dame = (level * 3250 * mob.level * 4) * 5;
            mob.point.maxHp = 2100000000;
        }
        if (level > 10000) {
            mob.point.dame = 2000000000;
            mob.point.maxHp = 2100000000;
        }
    }

    public static void initMobConDuongRanDoc(Mob mob, int level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public void randomSieuQuai() {
        if (this.tempId != 0 && MapService.gI().isMapKhongCoSieuQuai(this.zone.map.mapId) && Util.nextInt(0, 150) < 1) {
            this.lvMob = 1;

        }
    }

    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); //level mob
            msg.writeFix(Util.maxIntValue((mob.point.hp)));
            Service.gI().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }

    public synchronized void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }

            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if (this.tempId == 0 && damage > 10) {
                    damage = 10;
                }
            }
            if (this.tempId == 70) {
                long hp = this.point.hp;
                int tenPercent = (int) (hp * 0.1);
                if (damage > tenPercent) {
                    damage = tenPercent;
                }
                if (this.point.hp <= 10) {
                    damage = 1;
                }
                if (plAtt != null && !plAtt.isDie() && plAtt.nPoint.hp == 0) {
                    if (damage > tenPercent) {
                        damage = tenPercent;

                    }
                }

            }
            this.point.hp -= damage;
            if (this.isDie()) {
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, damage);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                this.lastTimeDie = System.currentTimeMillis();
            } else {
                if (plAtt == null || plAtt.nPoint == null) {
                    return;
                }
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);

                if (ItemMap.isInVeTinhRange(plAtt, ConstItem.VE_TINH_PHONG_THU, this.location.x, this.location.y)) {

                    if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {

                        this.mobAttackPlayer(plAtt);

                        this.lastTimeAttackPlayer = System.currentTimeMillis();
                    }
                }
            }
            if (plAtt != null) {
                Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
            }
        }
    }

    //    EMTI
//    public synchronized void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
//        if (!this.isDie()) {
//            if (damage >= this.point.hp) {
//                damage = this.point.hp;
//            }
//            if (!dieWhenHpFull && this.point.hp == this.point.maxHp) {
//                damage = Math.min(damage, this.point.hp - 1);
//            }
//            if (this.tempId == 0 && damage > 10) {
//                damage = 10;
//            }
//
//            this.point.hp -= damage;
//
//            if (this.isDie()) {
//                // Mob đã chết
//                this.status = 0;
//                this.sendMobDieAffterAttacked(plAtt, damage);
//                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
//                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
//                this.lastTimeDie = System.currentTimeMillis();
//            } else {
//                if (plAtt.nPoint != null) {
//                    this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);
//                }
//
//                if (plAtt != null) {
//                    Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
//                }
//            }
//        }
//    }
    public long getTiemNangForPlayer(Player pl, double dame) {
        if (pl == null) {
            return 1;
        }
        int levelPlayer = Service.gI().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = Util.TamkjllGH(dame) * 100 / point.getHpFull();
        long tiemNang = pDameHit * maxTiemNang / 100;
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        if (pl.nPoint == null) {
            return 1;
        }
        tiemNang = Util.TamkjllGH(pl.nPoint.calSucManhTiemNang(tiemNang));

        if (pl.zone != null && pl.zone.map != null) {
            if (pl.zone.map.mapId == 122 || pl.zone.map.mapId == 123 || pl.zone.map.mapId == 124) {
                tiemNang *= 2;
            } else if (pl.zone.map.mapId >= 53 || pl.zone.map.mapId <= 62) {
                tiemNang *= 5;
            } else if (pl.zone.map.mapId >= 135 || pl.zone.map.mapId <= 138) {
                tiemNang *= 4;
            }
            if (!pl.isDie() && ItemMap.isInVeTinhRange(pl, ConstItem.VE_TINH_TRI_TUE, this.location.x, this.location.y)) {
//                System.out.println("Cộng tiềm năng sm cho pl " + pl.name + " vì có vệ tinh trí tue");
                tiemNang += NPoint.calPercent(tiemNang, 20);
            }
        } else {
            return 1;
        }

        return tiemNang;
    }

    public boolean isSieuQuai() {
        return this.lvMob > 0;
    }

    public synchronized void injured(Player plAtt, long damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = this.point.hp;
            }
//            if (this.zone.map.mapId >= 122 || this.zone.map.mapId <= 124) {
//                plAtt.event.addEventPointNHS(1);
//            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = this.point.hp - 1;
                }
                if (this.tempId == 0 && damage > 10) {
                    damage = 10;
                }
            }
            if (this.tempId == 70) {
                long hp = this.point.hp;
                int tenPercent = (int) (hp * 0.1);
                if (damage > tenPercent) {
                    damage = tenPercent;
                }

            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (plAtt.nPoint.multicationChuong > 0 && Util.canDoWithTime(plAtt.nPoint.lastTimeMultiChuong, PlayerSkill.TIME_MUTIL_CHUONG)) {
                            damage *= plAtt.nPoint.multicationChuong;
                            plAtt.nPoint.lastTimeMultiChuong = System.currentTimeMillis();
                        }

                }
            }
            this.point.hp -= damage;

            if (this.isDie()) {
                this.lvMob = 0;
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, damage);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                this.lastTimeDie = System.currentTimeMillis();
                if (this.id == 13) {
                    this.zone.isbulon13Alive = false;
                }
                if (this.id == 14) {
                    this.zone.isbulon14Alive = false;
                }
            } else {
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);
//                System.out.println("sendMobStillAliveAffterAttacked");
                if (ItemMap.isInVeTinhRange(plAtt, ConstItem.VE_TINH_PHONG_THU, this.location.x, this.location.y)) {
//                    System.out.println("isInVeTinhRange");
                    if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
                        if (plAtt != null) {
//                            System.out.println("mob attack me when using seatle");
                            this.mobAttackPlayer(plAtt);
                        } else {
                            System.out.println("player attack = null");
                        }
                        this.lastTimeAttackPlayer = System.currentTimeMillis();
                    }
                }
            }
            if (plAtt != null) {
                Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
            }
        }
    }

    public void update() {
        if (this.tempId == 71) {
            try {
                Message msg = new Message(102);
                msg.writer().writeByte(5);
                msg.writer().writeShort(this.zone.getPlayers().get(0).location.x);
                Service.gI().sendMessAllPlayerInMap(zone, msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }

        if (isDie() && (tempId == 71 || tempId == 72)) {
//
////            int trai = 0;
////            int phai = 1;
////            int next = 0;
////
////            for (int i = 0; i < 10; i++) {
////                int X = next == 0 ? -5 * trai : 5 * phai;
////                if (next == 0) {
////                    trai++;
////                } else {
////                    phai++;
////                }
////                next = next == 0 ? 1 : 0;
////                if (trai > 10) {
////                    trai = 0;
////                }
////                if (phai > 10) {
////                    phai = 1;
////                }
//                //Item roi
            if (Util.isTrue(1, 20)) {

                ItemMap itemMap = new ItemMap(zone, Util.nextInt(1099, 1102), 1, location.x, location.y, -1);
                Service.gI().dropItemMap(zone, itemMap);
            }
//                else {
////
////                    ItemMap itemMap = new ItemMap(zone, 861, 30, location.x + X, location.y, -1);
////                    Service.gI().dropItemMap(zone, itemMap);
////                }
////                if (Util.isTrue(1, 20)) {
////                    ItemMap itemMap = new ItemMap(zone, Util.nextInt(18, 20), 1, location.x + X, location.y, -1);
////                    Service.gI().dropItemMap(zone, itemMap);
////
////                } else {
////                    ItemMap itemMap = new ItemMap(zone, 457, 1, location.x + X, location.y, -1);
////                    Service.gI().dropItemMap(zone, itemMap);
////                }
//            }
            Service.gI().sendBigBoss(this.zone, tempId == 71 ? 7 : 6, 0, -1, -1);
        }

        if (isDie() && tempId == 70) {
//            System.out.println("Levle " + level);
            if (level == 0) {
                level = 1;
                action = 6;
                point.hp = this.point.maxHp;
            } else if (level == 1) {
                level = 2;
                action = 5;
                point.hp = this.point.maxHp;
            } else if (level == 2) {
                level = 3;
                action = 9;
            }
            int trai = 0;
            int phai = 1;
            int next = 0;

            for (int i = 0; i < 10; i++) {
                int X = next == 0 ? -5 * trai : 5 * phai;
                if (next == 0) {
                    trai++;
                } else {
                    phai++;
                }
                next = next == 0 ? 1 : 0;
                if (trai > 15) {
                    trai = 0;
                }
                if (phai > 15) {
                    phai = 1;
                }
                //Item roi
                if (Util.isTrue(1, 20)) {

                    ItemMap itemMap = new ItemMap(zone, Util.nextInt(1099, 1102), 1, location.x + X, location.y, -1);
                    Service.gI().dropItemMap(zone, itemMap);
                } else {

                    ItemMap itemMap = new ItemMap(zone, 861, 30, location.x + X, location.y, -1);
                    Service.gI().dropItemMap(zone, itemMap);
                }
                if (Util.isTrue(1, 20)) {
                    ItemMap itemMap = new ItemMap(zone, Util.nextInt(17, 20), 1, location.x + X, location.y, -1);
                    Service.gI().dropItemMap(zone, itemMap);

                }
                if (Util.isTrue(1, 200)) {
                    ItemMap itemMap = new ItemMap(zone, 457, 1, location.x + X, location.y, -1);
                    Service.gI().dropItemMap(zone, itemMap);
                }
            }
            Service.gI().sendBigBoss2(zone, action, this);
            if (level <= 2) {
                Message msg = null;
                try {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writeFix(Util.maxIntValue(this.point.gethp()));
                    msg.writer().writeInt(1);
                    Service.gI().sendMessAllPlayerInMap(zone, msg);
                } catch (Exception e) {

                } finally {
                    if (msg != null) {
                        msg.cleanup();
                        msg = null;
                    }
                }
            } else {
                location.x = -1000;
                location.y = -1000;
            }
        }

        if (this.isDie() && !Maintenance.isRuning) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    if (this.zone.isTrungUyTrangAlive && this.tempId == 22 && this.zone.map.mapId == 59) {
//                        if (this.id == 13) {
//                            this.zone.isbulon13Alive = false;
//                        }
//                        if (this.id == 14) {
//                            this.zone.isbulon14Alive = false;
//                        }
                        if (Util.canDoWithTime(lastTimeDie, 5000)) {
                            if (this.id == 13) {
                                this.zone.isbulon13Alive = true;
                            }
                            if (this.id == 14) {
                                this.zone.isbulon14Alive = true;
                            }
                            this.hoiSinh();
                            this.sendMobHoiSinh();
                        }

                    }
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    if (this.tempId == 72 || this.tempId == 71) {//robot bao ve
                        if (System.currentTimeMillis() - this.lastTimeDie > 3000) {
                            try {
                                Message t = new Message(102);
                                t.writer().writeByte((this.tempId == 71 ? 7 : 6));
                                Service.gI().sendMessAllPlayerInMap(this.zone, t);
                                t.cleanup();
                            } catch (IOException e) {
                            }
                        }
                    }
                    break;
                case ConstMap.MAP_KHI_GAS:
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 5000)) {
                        this.randomSieuQuai();
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    }
            }
        }
        effectSkill.update();
        attackPlayer();

        if (tempId >= 70 && tempId <= 72) {
            BigbossAttack();
        } else {
            attackPlayer();
        }
    }

    private boolean isHaveEffectSkill() {
        return effectSkill.isAnTroi || effectSkill.isBlindDCTT || effectSkill.isStun || effectSkill.isThoiMien || effectSkill.isSocola;
    }

    public long get_dame(Player pl) {
        long dameMob = this.point.getDameAttack();
        if (pl.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }

        return (int) dameMob;
    }

    private int action;

    private void BigbossAttack() {
        if (!isDie() && !isHaveEffectSkill() && Util.canDoWithTime(lastTimeAttackPlayer, 1000)) {
            Message msg = null;
            try {
                switch (tempId) {
                    case 70:
                        int[] idAction = new int[]{1, 2, 3, 7};
                        if (this.level >= 2) {
                            idAction = new int[]{1, 2};
                        }
                        action = action == 7 ? 0 : idAction[Util.nextInt(0, idAction.length - 1)];
                        int index = Util.nextInt(0, zone.getPlayers().size() - 1);

                        Player player = zone.getPlayers().get(index);
                        if (player == null || player.isDie()) {
                            return;
                        }
                        if (action == 1) {
                            location.x = (short) player.location.x;
                            Service.gI().sendBigBoss2(zone, 8, this);
                        }
                        msg = new Message(101);
                        msg.writer().writeByte(action);
//                        System.out.println("action" + action);
                        if (action >= 0 && action <= 4) {
                            if (action == 1) {
                                msg.writer().writeByte(1);
                                int dame = (int) player.injured(null, get_dame(player), false, true);

                                msg.writer().writeInt((int) player.id);
                                msg.writer().writeInt(dame);
                            } else if (action == 3) {
                                location.x = (short) player.location.x;
                                msg.writer().writeShort(location.x);
                                msg.writer().writeShort(location.y);
                            } else {
                                msg.writer().writeByte(zone.getHumanoids().size());
                                for (int i = 0; i < zone.getHumanoids().size(); i++) {
                                    Player pl = zone.getHumanoids().get(i);
                                    int dame = (int) player.injured(null, get_dame(player), false, true);

                                    msg.writer().writeInt((int) pl.id);
                                    msg.writer().writeInt(dame);
                                }
                            }
                        } else {
                            if (action == 6 || action == 8) {
                                location.x = (short) player.location.x;
                                msg.writer().writeShort(location.x);
                                msg.writer().writeShort(location.y);
                            }
                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();

                        break;
                    case 71:
                        int[] idAction2 = new int[]{3, 4, 5};
                        action = action == 7 ? 0 : idAction2[Util.nextInt(0, idAction2.length - 1)];
                        int index2 = Util.nextInt(0, zone.getPlayers().size() - 1);

                        Player player2 = zone.getPlayers().get(index2);
                        if (player2 == null || player2.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 5) {
                            if (action != 5) {
                                msg.writer().writeByte(1);
                                int dame = (int) player2.injured(null, get_dame(player2), false, true);
                                msg.writer().writeInt((int) player2.id);
                                msg.writer().writeInt(dame);
                            }
                            if (action == 5) {
                                location.x = (short) player2.location.x;
                                msg.writer().writeShort(location.x);
                            }
                        } else {

                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                    case 72:
                        int[] idAction3 = new int[]{0, 1, 2};
                        action = action == 7 ? 0 : idAction3[Util.nextInt(0, idAction3.length - 1)];
                        int index3 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player3 = zone.getPlayers().get(index3);
                        if (player3 == null || player3.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 2) {
                            msg.writer().writeByte(1);
                            int dame = (int) player3.injured(null, get_dame(player3), false, true);
                            msg.writer().writeInt((int) player3.id);
                            msg.writer().writeInt(dame);
                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                }
            } catch (Exception e) {

            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }
        }
    }

    private void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
                this.mobAttackPlayer(pl);
                this.lastTimeAttackPlayer = System.currentTimeMillis();
            }
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 100;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();

            for (Player pl : players) {
                if (!pl.isDie() && !pl.isNewPet && !pl.isMiniPet && !pl.name.equals("Jajirô") && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    if (!pl.isDie() && ItemMap.isInVeTinhRange(pl, ConstItem.VE_TINH_PHONG_THU, pl.location.x, pl.location.y)) {
                        continue;
                    }
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    //**************************************************************************
    private void mobAttackPlayer(Player player) {
        long dameMob = this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        if (player.charms.tdBatTu > System.currentTimeMillis() && player.itemTime != null
                && (player.itemTime.isLoNuocThanhx2 || player.itemTime.isLoNuocThanhx5
                || player.itemTime.isLoNuocThanhx7 || player.itemTime.isLoNuocThanhx10
                || player.itemTime.isLoNuocThanhx15)) {
            dameMob *= 1;
        }

        double dame = player.injured(null, dameMob, false, true);
        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, double dame) {
        if (!player.isPet && !player.isMiniPet && !player.isNewPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writeFix(Util.maxIntValue(dame)); //dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writeFix(Util.maxIntValue(player.nPoint.hp));
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writeFix(Util.maxIntValue(this.point.hp));
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //**************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, double dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writeFix(Util.maxIntValue(dameHit));
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (Exception e) {
        }
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet && !player.isNewPet && !player.isMiniPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    }
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
        List<ItemMap> itemReward = new ArrayList<>();

        try {

            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y));
            if (itemTask != null) {
                itemReward.add(itemTask);
            }

            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (Exception e) {

        }
        return itemReward;
    }
//    public List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
//        List<ItemMap> itemReward = new ArrayList<>();
//
//        try {
//            int x = this.location.x + Util.nextInt(-10, 10);
//            int y = this.location.y;
////            System.out.println("x: " + x);
////            System.out.println("y: " + y);
//
//            int yPhysic = this.zone.map.yPhysicInTop(x, y);
////            System.out.println("yPhysic: " + yPhysic);
//
//            // Kiểm tra giá trị trả về
//            if (yPhysic < 0 || yPhysic >= 2000) {
//                Service.gI().sendThongBao(player, " Có lỗi xảy ra khi up quái tại " + yPhysic);
//            }
//
//            itemReward = this.getItemMobReward(player, x, yPhysic);
//            if (itemTask != null) {
//                itemReward.add(itemTask);
//            }
//
//            msg.writer().writeByte(itemReward.size()); //sl item roi
//            for (ItemMap itemMap : itemReward) {
//                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
//                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
//                msg.writer().writeShort(itemMap.x); // xend item
//                msg.writer().writeShort(itemMap.y); // yend item
//                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
//            }
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.err.println("ArrayIndexOutOfBoundsException: " + e.getMessage());
//            e.printStackTrace();
//            // Xử lý ngoại lệ, có thể trả về một giá trị mặc định hoặc giá trị hợp lệ
//            return new ArrayList<>(); // Trả về danh sách trống hoặc xử lý phù hợp
//        } catch (Exception e) {
//            System.err.println("Exception: " + e.getMessage());
//            e.printStackTrace();
//            // Xử lý các ngoại lệ khác
//        }
//
//        return itemReward;
//    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) {
        List<ItemMap> list = new ArrayList<>();
        MobReward mobReward = Manager.MOB_REWARDS.get(this.tempId);
        player.event.addEventPointQuai(1);
//        if (player.itemTime.isUseMayDo && Util.isTrue(5, 300) && this.tempId > 57 && this.tempId < 66) {
//            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
//        }// vat phẩm rơi khi user maaáy dò adu hoa r o day ti code choa
//        if (player.itemTime.isUseMayDo2 && Util.isTrue(1, 400) && this.tempId > 1 && this.tempId < 81) {
//            list.add(new ItemMap(zone, 2120, 1, x, player.location.y, player.id));// cai nay sua sau nha
//        }
//        if (player.itemTime.isUseMayDo3 && Util.isTrue(1, 300) && this.tempId > 1 && this.tempId < 81) {
//            list.add(new ItemMap(zone, Util.nextInt(2162, 2165), 1, x, player.location.y, player.id));// cai nay sua sau nha
//        }
//        if (ConstDataEvent.isRunningSK16) {
//            try {
//                if (player.isPl() && player.itemTime.isUseMayDoSK) {
//                    if (Util.isTrue(1, 200)) {
////                        Service.gI().dropItemMap(this.zone, new ItemMap(zone, ConstDataEvent.getRandomFromList(), 1, this.location.x, this.location.y, player.id));
//                        ItemMap spl = Util.ItemRotTuQuai(zone, ConstDataEvent.getRandomFromList(), 1, this.location.x, this.location.y, player.id);
//                        spl.options.add(new Item.ItemOption(93, 5));
//                        list.add(spl);
//                    }
//
//                }
//            } catch (Exception e) {
//                Logger.error("Lỗi của Kiệt ở chỗ dánh quái rớt VPSK");
//            }
//        }
//        if (ConstDataEvent.isRunningSK16) {
//            try {
//                if (this.zone.map.mapId == 202) {
//                    if (Util.isTrue(2, 100)) {
//                        list.add(new ItemMap(zone, ConstDataEvent.ID_ITEM_6, 1, x, player.location.y, player.id));
//                    }
//                }
//            } catch (Exception e) {
//                Logger.error("Lỗi của Kiệt ở chỗ dánh quái rớt VPSK");
//            }
//        }
//
//        if (SK20_10.isRunningSK2010) {
//            try {
//
//                if (Util.isTrue(1, 150)) {
////                        Service.gI().dropItemMap(this.zone, new ItemMap(zone, ConstDataEvent.getRandomFromList(), 1, this.location.x, this.location.y, player.id));
//                    ItemMap spl = Util.ItemRotTuQuai(zone, SK20_10.getRandomFromList(), 1, this.location.x, this.location.y, player.id);
//                    spl.options.add(new Item.ItemOption(93, 5));
//                    list.add(spl);
//                }
//
//            } catch (Exception e) {
//                Logger.error("Lỗi của Kiệt ở chỗ dánh quái rớt VPSK");
//            }
//        }
//        if (player.isPl() && player.itemTime.isUseDuoiKhi) {
//
//            if (Util.isTrue(70, 100)) {
////                        Service.gI().dropItemMap(this.zone, new ItemMap(zone, ConstDataEvent.getRandomFromList(), 1, this.location.x, this.location.y, player.id));
//                ItemMap spl = Util.ItemRotTuQuai(zone, Util.getRandomFromListSKtrungthu(), 1, this.location.x, this.location.y, player.id);
//                list.add(spl);
//            }
//        }
        int mapid = this.zone.map.mapId;
        if (mapid == 156 || mapid == 157 || mapid == 159 || mapid == 158) {
            if (Util.isTrue(99, 100)) {
                list.add(new ItemMap(zone, 933, 1, x, player.location.y, player.id));
            }
        }
//        if (player.isPl() && player.getSession() != null) {
//            if (mapid == 165 || mapid == 167 || mapid == 168) {
//                boolean diemsk = false;
//
//                // Gán diemsk là true nếu Util.isTrue(30, 100) trả về true
//                if (Util.isTrue(30, 100)) {
//                    diemsk = true;
//                }
//
//                // Kiểm tra nếu cả Util.isTrue(30, 100) và diemsk đều là true
//                if (Util.isTrue(70, 100) && diemsk) {
//                    list.add(new ItemMap(zone, Util.getRandomFromListSKtrungthu(),
//                            1, x, player.location.y, player.id));
////                    if (Util.canDoWithTime(lastTimeUpdate, 1000)) {
////                        player.event.addEventPoint(1); // Cộng 1 điểm cho mỗi lần
////                        player.event.addEventPointQuyLao(1); // Cộng 1 điểm cho mỗi lần
////                        player.event.addEventPointMoc(1); // Cộng 1 điểm cho mỗi lần
////                        lastTimeUpdate = System.currentTimeMillis();
//////                                Logger.success(player.name + " up được 1 điểm\n");
////                    }
//                } else {
//                    list.add(new ItemMap(zone, ConstDataEvent.ID_ITEM_6, 1, x, player.location.y, player.id));
//                }
//            }
//        }

        if (mapid == 159) {
            if (Util.isTrue(1, 200)) {
                list.add(new ItemMap(zone, 2227, 1, x, player.location.y, player.id));
            }
        }
        if (mapid == 159 || mapid == 158) {
            if (Util.isTrue(1, 200)) {
                list.add(new ItemMap(zone, 934, 1, x, player.location.y, player.id));
            }
        }
        if (mapid == 159 || mapid == 158) {
            if (Util.isTrue(1, 200)) {
                list.add(new ItemMap(zone, 935, 1, x, player.location.y, player.id));
            }
        }
        if (mapid == 204) {
            if (Util.isTrue(50, 100)) {
                list.add(new ItemMap(zone, 1680, 1, x, player.location.y, player.id));
            }
        }
//        if (!player.isPet) {
//            if (Util.isTrue(1, 400)) {
//                list.add(new ItemMap(zone, 874, 1, x, player.location.y, player.id));
//            }
//        }
        if (!player.isPet) {
            if (Util.isTrue(1, 1000)) {
                list.add(new ItemMap(zone, Util.nextInt(220, 224), 1, x, player.location.y, player.id));
            }
        }
        if (mapid == 216) { // nhs 1
            if (!player.isPet && Util.isTrue(15, 100)) {
                int itemId = Util.nextInt(1698, 1706); // tan dan 1- 3
                Service.gI().dropItemMapForMe(player,
                        new ItemMap(zone, itemId, 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
            }

        }
        if (mapid == 217) { //nhs 2
            if (!player.isPet && Util.isTrue(45, 100)) {
                int itemId = Util.nextInt(1698, 1706); // tan dan 1- 3
                Service.gI().dropItemMapForMe(player,
                        new ItemMap(zone, itemId, 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
            }
            Service.gI().dropItemMapForMe(player,
                    new ItemMap(zone, 457, 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
        }
        if (mapid == 218) { //nhs 3
            Service.gI().dropItemMapForMe(player,
                    new ItemMap(zone, 457, 10, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
            Service.gI().dropItemMapForMe(player,
                    new ItemMap(zone, 1697, 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
            if (player.dotpha == 1) {
                Service.gI().dropItemMapForMe(player,
                        new ItemMap(zone, 1711, 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
            }
            if (player.dotpha == 2) {
                Service.gI().dropItemMapForMe(player,
                        new ItemMap(zone, 1712, 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), player.id));
            }

        }
        if (mapid == 155) {
            if (!player.isPet && Util.isTrue(30, 100)) {

                list.add(new ItemMap(zone, 2120, 1, x, player.location.y, player.id));
            }
        }
//        if (!player.isPet) {
//            if (Util.isTrue(1, 300)) {
//                list.add(new ItemMap(zone, Util.nextInt(828, 842), 1, x, player.location.y, player.id));
//            }
//        }
//        if (!player.isPet) {
//            if (Util.isTrue(1, 400)) {
//                list.add(new ItemMap(zone, 859, 1, x, player.location.y, player.id));
//            }
//        }
//        if (!player.isPet) {
//            if (Util.isTrue(1, 600)) {
//                list.add(new ItemMap(zone, 1142, 1, x, player.location.y, player.id));
//            }
//        }
//        if (!player.isPet) {
//            if (Util.isTrue(1, 30)) {
//                list.add(new ItemMap(zone, 225, 1, x, player.location.y, player.id));
//            }
//        }
//        if (!player.isPet) {
//            if (Util.isTrue(1, 300)) {
//                list.add(new ItemMap(zone, 226, 1, x, player.location.y, player.id));
//            }
//        }
//        if (player.isPet && player.getSession().actived && Util.isTrue(1, 300)) {
//            list.add(new ItemMap(zone, 874, 1, x, player.location.y, player.id));
//        }
        if (!player.isPet //                &&
                //                player.getSession() != null
                //                && player.getSession().actived
                //                && (player.inventory.itemsBody.stream()
                //                        .filter(item -> item.isNotNullItem() && item.isSKH())
                //                        .limit(5)
                //                        .count() == 5 )
                ) {

//                System.out.println("ok");
            byte randomDo = (byte) new Random().nextInt(Manager.spl.length);
            if (Util.isTrue(2, 200)) {
//                    System.out.println("ok2");
                ItemMap spl = Util.ItemRotTuQuai(zone, Manager.spl[randomDo], 1, this.location.x, this.location.y, player.id);
//                Service.gI().dropItemMap(this.zone, spl);
//                InventoryServiceNew.gI().sendItemBags(player);
                list.add(spl);

            }
        }
        if (!player.isPet
                && Manager.MapMocnhan.contains(mapid) //                player.getSession() != null
                //                && player.getSession().actived
                //                && (player.inventory.itemsBody.stream()
                //                        .filter(item -> item.isNotNullItem() && item.isSKH())
                //                        .limit(5)
                //                        .count() == 5)
                ) {

//                System.out.println("ok");
            byte randomDo = (byte) new Random().nextInt(Manager.spl.length);
            if (Util.isTrue(50, 100)) {
//                    System.out.println("ok2");
                ItemMap spl = Util.ItemRotTuQuai(zone, Manager.spl[randomDo], 1, this.location.x, this.location.y, player.id);
//                Service.gI().dropItemMap(this.zone, spl);
//                InventoryServiceNew.gI().sendItemBags(player);
                list.add(spl);

            }
        }
        if ((//!player.isPet&&
                player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isSKH())
                        .limit(5)
                        .count() == 5))) {

//                System.out.println("ok");
            byte randomDo = (byte) new Random().nextInt(Manager.nr4s.length);
            if (Util.isTrue(1, 400)
                    && Manager.MapTuongLai.contains(mapid)
                    && Manager.MapCold.contains(mapid)
                    && Manager.MapNgucTu.contains(mapid)
                    && Manager.MapLDBH.contains(mapid)) {
//                    System.out.println("ok2");
                ItemMap nr4s = Util.ItemRotTuQuai(zone, Manager.nr4s[randomDo], 1, this.location.x, this.location.y, player.id);
//                Service.gI().dropItemMap(this.zone, spl);
//                InventoryServiceNew.gI().sendItemBags(player);
                list.add(nr4s);

            }
        }
        if (!player.isPet
                && player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isSKH())
                        .limit(5)
                        .count() == 5)) {

//                System.out.println("ok");
            byte randomDo = (byte) new Random().nextInt(Manager.ca.length);
            if (Util.isTrue(1, 600)
                    && Manager.MapCold.contains(mapid)) {
//                    System.out.println("ok2");
                ItemMap spl = Util.ItemRotTuQuai(zone, Manager.ca[randomDo], 1, this.location.x, this.location.y, player.id);
//                Service.gI().dropItemMap(this.zone, spl);
//                InventoryServiceNew.gI().sendItemBags(player);
                list.add(spl);

            }
        }
        if (!player.isPet
                && player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isDHD())
                        .limit(5)
                        .count() == 5)
                && Manager.MapCold.contains(mapid)) {
            byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length);
            if (Util.isTrue(1, 500)) {
                ItemMap dotl = Util.ratiItemTL(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, player.id);
                list.add(dotl);
            }
        }
        if (!player.isPet
                && player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isSKH())
                        .limit(5)
                        .count() == 5)
                && Manager.MapCold.contains(mapid)) {
            byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length);
            if (Util.isTrue(1, 400)) {
                ItemMap dotl = Util.ratiItemTL(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, player.id);
                list.add(dotl);
            }
        }
        if (!player.isPet
                && player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isDTS())
                        .limit(5)
                        .count() == 5)
                && Manager.MapDiaNguc.contains(mapid)) {
            if (Util.isTrue(1, 200)) {
                ItemMap it = Util.ItemRotTuQuai(zone, 1314, 1, this.location.x, this.location.y, player.id);
                list.add(it);
            }
        }
        if (!player.isPet && player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isDTS())
                        .limit(5)
                        .count() == 5)
                && Manager.MapRungMo.contains(mapid)) {
            if (Util.isTrue(1, 200)) {
                ItemMap it = Util.ItemRotTuQuai(zone, 1316, 1, this.location.x, this.location.y, player.id);
                list.add(it);
            }
        }
        if (!player.isPet && player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isDTS())
                        .limit(5)
                        .count() == 5)
                && Manager.MapThungLung.contains(mapid)) {
            if (Util.isTrue(1, 200)) {
                ItemMap it = Util.ItemRotTuQuai(zone, Util.nextInt(1360, 1366), 1, this.location.x, this.location.y, player.id);
                list.add(it);
            }
        }
        if (!player.isPet && player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isDTS())
                        .limit(5)
                        .count() == 5)
                && Manager.MapCungTrang.contains(mapid)) {
            if (Util.isTrue(1, 200)) {
                ItemMap it = Util.ItemRotTuQuai(zone, 1315, 1, this.location.x, this.location.y, player.id);
                list.add(it);
            }
        }
        if (!player.isPet && player.getSession() != null
                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isDTL())
                        .limit(5)
                        .count() == 5)
                && Manager.MapCold.contains(mapid)) {
            byte randomDo = (byte) new Random().nextInt(Manager.thucan.length);
            if (Util.isTrue(1, 500)) {
                ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.thucan[randomDo], 1, this.location.x, this.location.y, player.id);
                list.add(thucan);
            }
        }
//        if (!player.isPet && player.getSession() != null
//                && player.getSession().actived
//                && (player.inventory.itemsBody.stream()
//                        .filter(item -> item.isNotNullItem() && item.isSKH())
//                        .limit(5)
//                        .count() == 5)) {
//            byte randomDo = (byte) new Random().nextInt(Manager.itemhe2023.length);
//            if (Util.isTrue(1, 400)) {
//                ItemMap itemhe = Util.ItemRotTuQuai(zone, Manager.itemhe2023[randomDo], 1, this.location.x, this.location.y, player.id);
//                list.add(itemhe);
//            }
//        }

        if (!player.isPet && player.getSession() != null
                //                && player.getSession().actived
                && (player.inventory.itemsBody.stream()
                        .filter(item -> item.isNotNullItem() && item.isSKH())
                        .limit(5)
                        .count() == 5)) {
            byte randomDo = (byte) new Random().nextInt(Manager.nr.length);
            if (Util.isTrue(1, 400)) {
                ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.nr[randomDo], 1, this.location.x, this.location.y, player.id);
                list.add(thucan);
            }
        }
//        if (!player.isPet && player.getSession() != null
//                && player.getSession().actived
//                && (player.inventory.itemsBody.stream()
//                        .filter(item -> item.isNotNullItem() && item.isDHD())
//                        .limit(5)
//                        .count() == 5)
//                && Manager.MapThucVat.contains(mapid)
//                && Manager.MapNgucTu.contains(mapid)) {
//            byte randomDo = (byte) new Random().nextInt(Manager.manhts.length);
//            if (Util.isTrue(1, 1000)) {
//                ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.manhts[randomDo], 1, this.location.x, this.location.y, player.id);
//                list.add(thucan);
//            }
//        }
        if (!player.isPet
                && player.getSession() != null
                && player.getSession().actived
                && player.clan != null
                && player.clan.banDoKhoBau != null
                && Manager.MapBDKB.contains(mapid)) {

            int trai = 0;
            int phai = 1;
            int next = 0;

//            int tile = (BanDoKhoBau.level);
            for (int i = 0; i < (player.clan.banDoKhoBau.level + 16) / 8; i++) {
                int X = next == 0 ? -5 * trai : 5 * phai;
                if (next == 0) {
                    trai++;
                } else {
                    phai++;
                }
                next = next == 0 ? 1 : 0;
                if (trai > 18) {
                    trai = 0;
                }
                if (phai > 18) {
                    phai = 1;
                }
                byte randomDo = (byte) new Random().nextInt(Manager.nr.length);
                byte randomDo2 = (byte) new Random().nextInt(Manager.ibdkb.length);
                if (player.clan.banDoKhoBau.level >= 20) {
                    if (Util.isTrue(player.clan.banDoKhoBau.level, 2000)) {
                        ItemMap thucan = Util.ItemRotTuQuai(zone, 457, 1, this.location.x + X, this.location.y, -1);
                        list.add(thucan);
                    } else {
                        ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.nr[randomDo], 1, this.location.x + X, this.location.y, -1);
                        list.add(thucan);
                    }
                }
                if (player.clan.banDoKhoBau.level < 20) {
                    if (Util.isTrue(20, player.clan.banDoKhoBau.level)) {
                        ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.nr[randomDo], 1, this.location.x + X, this.location.y, -1);
                        list.add(thucan);
                    } else {
                        ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.ibdkb[randomDo2], 100, this.location.x + X, this.location.y, -1);
                        list.add(thucan);
                    }
                }
                if (Util.isTrue(player.clan.banDoKhoBau.level, 300)) {
                    ItemMap thucan = Util.ItemRotTuQuai(zone, 76, 30000, this.location.x + X, this.location.y, -1);
                    list.add(thucan);
                } else {
                    ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.ibdkb[randomDo2], 50, this.location.x + X, this.location.y, -1);
                    list.add(thucan);
                }

            }
        }
//        if (!player.isPet && player.getSession() != null
//                //                    && player.getSession().actived
//                && Manager.MapNguHanhSon.contains(mapid)) {
//            byte randomDo = (byte) new Random().nextInt(Manager.chu.length);
//            if (Util.isTrue(1, 100)) {
//                ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.chu[randomDo], 1, this.location.x, this.location.y, player.id);
//                list.add(thucan);
//
//            }
//            player.event.addEventPointNHS(1);
//        }
        if (//!player.isPet
                //                                        && player.getSession().actived
                Manager.MapCold.contains(mapid)
                || Manager.MapFide.contains(mapid)
                || Manager.MapLDBH.contains(mapid)
                || Manager.MapThucVat.contains(mapid)
                || Manager.MapTuongLai.contains(mapid)) {
            byte randomDo = (byte) new Random().nextInt(Manager.itembuff.length);
            if (Util.isTrue(1, 300)) {
                ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.itembuff[randomDo], 1, this.location.x, this.location.y, player.id);
                list.add(thucan);
            }

        } else if (//!player.isPet
                //                    && player.getSession().actived
                Manager.MapCold.contains(mapid)) {
            byte randomDo = (byte) new Random().nextInt(Manager.ca.length);
            if (Util.isTrue(1, 500)) {
                ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.ca[randomDo], 1, this.location.x, this.location.y, player.id);
                list.add(thucan);
            }
        }
//        if (//!player.isPet
//                //                    && player.getSession().actived
//                (this.zone.map.mapId == 0 || this.zone.map.mapId == 7 || this.zone.map.mapId == 14)) {
//            byte randomDo = (byte) new Random().nextInt(Manager.bktk.length);
//            if (Util.isTrue(1, 10)) {
//                ItemMap thucan = Util.ItemRotTuQuai(zone, Manager.bktk[randomDo], 1, this.location.x, this.location.y, player.id);
//                list.add(thucan);
//            }
//        }
        if (!player.isPet) {
            int soluongngocrong = Util.nextInt(20, 50);
            int ngochong = 861;
            if (Util.isTrue(1, 300)) {

                ItemMap thucan = Util.ItemRotTuQuai(zone, ngochong, soluongngocrong, this.location.x, this.location.y, player.id);
                list.add(thucan);
            }
        }
        if (!player.isPet) {
            int soluongngocrong = Util.nextInt(20, 200);
            int ngochong = 77;
            if (Util.isTrue(1, 300)) {

                ItemMap thucan = Util.ItemRotTuQuai(zone, ngochong, soluongngocrong, this.location.x, this.location.y, player.id);
                list.add(thucan);
            }
        }

        if (player.isPl()) {
            int mapId = player.zone.map.mapId;
            if (!player.isBoss && (mapId == 1 || mapId == 2 || mapId == 3 || mapId == 16 || mapId == 17 || mapId == 18 || mapId == 8 || mapId == 9 || mapId == 11)) {
                if (Util.isTrue(5, 100)) {

                    int[][] itemKH = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}}; //td, 
                    int skhId = ItemService.gI().randomSKHId(player.gender);
//                    items[player.gender][Util.nextInt(0, 4)], skhId
                    ItemMap it = ItemService.gI().itemMapSKH(zone, itemKH[player.gender][Util.nextInt(0, 4)], 1, this.location.x, this.location.y, player.id, skhId);
                    list.add(it);
                }
            }

        }

        if (!player.isBoss && Manager.MapNguHanhSon.contains(mapid)) {
            if (Util.isTrue(1, 1500)) {
                ItemMap map = new ItemMap(zone, Util.nextInt(1066, 1070), 1, this.location.x, this.location.y, player.id);
                list.add(map);
            }
        }
        if (mobReward == null) {
            return list;
        }
        List<ItemMobReward> items = mobReward.getItemReward();
        List<ItemMobReward> golds = mobReward.getGoldReward();
        if (!items.isEmpty()) {
            ItemMobReward item = items.get(Util.nextInt(0, items.size() - 1));
            ItemMap itemMap = item.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }
        if (!golds.isEmpty()) {
            ItemMobReward gold = golds.get(Util.nextInt(0, golds.size() - 1));
            ItemMap itemMap = gold.getItemMap(zone, player, x, yEnd);
            if (itemMap != null) {
                list.add(itemMap);
            }
        }

        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        switch (this.tempId) {
            case ConstMob.THAN_LAN_ME:
            case ConstMob.PHI_LONG_ME:
            case ConstMob.QUY_BAY_ME:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_6_1) {
                    itemMap = new ItemMap(this.zone, 20, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(double dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writeFix(Util.maxIntValue(this.point.gethp()));
            msg.writeFix(Util.maxIntValue(dameHit));
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

}
