package nro.models.boss.list_boss.BLACK;

import nro.consts.cn;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

import java.util.Random;
import nro.consts.ConstPlayer;
import nro.utils.Logger;

public class SuperBlack2 extends Boss {

    public SuperBlack2() throws Exception {
        super(Util.randomBossId(), BossesData.SUPER_BLACK_GOKU_2);
    }

//     @Override
//    public void reward(Player pl) {
//        pl.event.addEventPoint(1);
//        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm sự kiện");
//        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
//        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);
//        int trai = 0;
//        int phai = 1;
//        int next = 0;
//
//        for (int i = 0; i < 1; i++) {
//            int X = next == 0 ? -5 * trai : 5 * phai;
//            if (next == 0) {
//                trai++;
//            } else {
//                phai++;
//            }
//            next = next == 0 ? 1 : 0;
//            if (trai > 15) {
//                trai = 0;
//            }
//            if (phai > 15) {
//                phai = 1;
//            }
//            //Item roi
//            if (Util.isTrue(1, 10)) {
//                Service.gI().dropItemMap(zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x + X, this.location.y, pl.id));
//            } else {
//                Service.gI().dropItemMap(zone, Util.ratiItem(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x + X, this.location.y, pl.id));
//
//            }
//            if (Util.isTrue(9, 10)) {
//                Service.gI().dropItemMap(zone, Util.ratiItem(zone, 861, 200, this.location.x + X, this.location.y, pl.id));
//            } else {
//                Service.gI().dropItemMap(zone, Util.ratiItem(zone, 15, 1, this.location.x + X, this.location.y, pl.id));
//
//            }
//            if (Util.isTrue(1, 10)) {
//                Service.gI().dropItemMap(zone, Util.ratiItem(zone, 457, 100, this.location.x + X, this.location.y, pl.id));
//            } else {
//                Service.gI().dropItemMap(zone, Util.ratiItem(zone, 861, 1000, this.location.x + X, this.location.y, pl.id));
//
//            }
//            if (Util.isTrue(1, 10)) {
//                generalRewards(pl);
//            }
//        }
//
//    }
    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(2);
        Service.gI().sendThongBao(pl, "Bạn nhận được 2 điểm săn boss");
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);

        //Item roi
        if (Util.isTrue(9, 10)) {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x + 5, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        if (Util.isTrue(9, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(100, 500), this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 457, Util.nextInt(1, 50), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(100, 300), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (Util.isTrue(1, 10)) {
            generalRewards(pl);
        }

    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }
    private long st;

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        try {
        } catch (Exception ex) {
            Logger.logException(SuperBlack2.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }

damage = this.nPoint.subDameInjureWithDeff(damage);
            if (plAtt != null && !piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage * 0.5;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
//    @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.reward(plKill);
//    }
//
//    @Override
//    protected void notifyJoinMap() {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.notifyJoinMap();
//    }
}
