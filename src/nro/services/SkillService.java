package nro.services;

import nro.consts.ConstPlayer;
import nro.models.Template.SkillTemplate;
import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossType;
import nro.models.intrinsic.Intrinsic;
import nro.models.mob.Mob;
import nro.models.mob.MobMe;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.player.SkillSpecial;
import nro.models.skill.PlayerSkill;
import nro.models.skill.Skill;
import nro.network.io.Message;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nro.models.card.RadarService;

public class SkillService {

    private static SkillService i;

    private SkillService() {

    }

    public static SkillService gI() {
        if (i == null) {
            i = new SkillService();
        }
        return i;
    }

    public boolean useSkill(Player player, Player plTarget, Mob mobTarget, Message message) {
        if (player.effectSkill.isHaveEffectSkill()) {
            return false;
        }
        if (player.playerSkill == null) {
            return false;
        }
        if (player.playerSkill.skillSelect.template.type == 2 && canUseSkillWithMana(player) && canUseSkillWithCooldown(player)) {
            useSkillBuffToPlayer(player, plTarget);
            return true;
        }
        if ((player.effectSkill.isHaveEffectSkill()
                && (player.playerSkill.skillSelect.template.id != Skill.TU_SAT
                && player.playerSkill.skillSelect.template.id != Skill.QUA_CAU_KENH_KHI
                && player.playerSkill.skillSelect.template.id != Skill.MAKANKOSAPPO))
                || (plTarget != null && !canAttackPlayer(player, plTarget))
                || (mobTarget != null && mobTarget.isDie())
                || !canUseSkillWithMana(player) || !canUseSkillWithCooldown(player)) {
            return false;
        }
        // Heroes Z
        if ((player.effectSkill.isHaveEffectSkill()
                && (player.playerSkill.skillSelect.template.id != Skill.SUPER_KAME
                && player.playerSkill.skillSelect.template.id != Skill.MA_PHONG_BA
                && player.playerSkill.skillSelect.template.id != Skill.LIEN_HOAN_CHUONG))
                || !canUseSkillWithMana(player) || !canUseSkillWithCooldown(player)) {
            return false;
        }

        if (player.effectSkill.useTroi) {
            EffectSkillService.gI().removeUseTroi(player);
        }
        if (player.effectSkill.isCharging) {
            EffectSkillService.gI().stopCharge(player);
        }

        byte st = -1;
        byte skillId = -1;
        Short dx = -1;
        Short dy = -1;
        byte dir = -1;
        Short x = -1;
        Short y = -1;

        try {
            st = message.reader().readByte();
            skillId = message.reader().readByte();
            dx = message.reader().readShort();
            dy = message.reader().readShort();
            dir = message.reader().readByte();
            x = message.reader().readShort();
            y = message.reader().readShort();

        } catch (Exception e) {
        }
        if (st == 20 && skillId != player.playerSkill.skillSelect.template.id) {
            selectSkill(player, skillId);
            return false;
        }

        switch (player.playerSkill.skillSelect.template.type) {
            case 1:
                useSkillAttack(player, plTarget, mobTarget);
                break;
            case 3:
                useSkillAlone(player);
                break;
            case 4:
                userSkillSpecial(player, st, skillId, dx, dy, dir, x, y);
                break;
            default:
                return false;
        }
        return true;
    }

    public void userSkillSpecial(Player player, byte st, byte skillId, Short dx, Short dy, byte dir, Short x, Short y) {
        try {
            switch (skillId) {
                case Skill.SUPER_KAME:
                    if (player.inventory.itemsBody.get(8).isNotNullItem()) {
                        for (int i = 1; i < player.inventory.itemsBody.get(8).itemOptions.size(); i++) {
                            if (player.inventory.itemsBody.get(8).itemOptions.get(i).optionTemplate.id == 236) {
                                if (player.inventory.itemsBody.get(8).itemOptions.get(i).param == 0) {
                                    Service.gI().sendThongBao(player, "Phục hồi sách hoặc tháo sách ra để dùng skill");
                                    return;
                                }
                            }
                        }
                        if (player.playerSkill.skillSelect.point == 7) {
                            Service.gI().SendImgSkill9(skillId, 3);
                            sendEffSkillSpecialID24(player, dir, 3);
                            break;
                        }
                        if (player.inventory.itemsBody.get(8).template.id == 1163) {
                            Service.gI().SendImgSkill9(skillId, 2);
                            sendEffSkillSpecialID24(player, dir, 2);
                            break;
                        } else {
                            sendEffSkillSpecialID24(player, dir, 0);
                            break;
                        }
                    } else {
                        sendEffSkillSpecialID24(player, dir, 0);
                    }
                    break;
                case Skill.LIEN_HOAN_CHUONG:
                    if (player.inventory.itemsBody.get(8).isNotNullItem()) {
                        for (int i = 1; i < player.inventory.itemsBody.get(8).itemOptions.size(); i++) {
                            if (player.inventory.itemsBody.get(8).itemOptions.get(i).optionTemplate.id == 236) {
                                if (player.inventory.itemsBody.get(8).itemOptions.get(i).param == 0) {
                                    Service.gI().sendThongBao(player, "Phục hồi sách hoặc tháo sách ra để dùng skill");
                                    return;
                                }
                            }
                        }
                        if (player.playerSkill.skillSelect.point == 7) {
                            Service.gI().SendImgSkill9(skillId, 3);
                            sendEffSkillSpecialID25(player, dir, 3);
                            break;
                        }
                        if (player.inventory.itemsBody.get(8).template.id == 1164) {
                            Service.gI().SendImgSkill9(skillId, 2);// gửi ảnh tới cilent
                            sendEffSkillSpecialID25(player, dir, 2);
                            break;
                        } else {
                            sendEffSkillSpecialID25(player, dir, 0);
                            break;
                        }
                    } else {
                        sendEffSkillSpecialID25(player, dir, 0);
                    }
                    break;
                case Skill.MA_PHONG_BA:
                    if (player.inventory.itemsBody.get(8).isNotNullItem()) {
                        for (int i = 1; i < player.inventory.itemsBody.get(8).itemOptions.size(); i++) {
                            if (player.inventory.itemsBody.get(8).itemOptions.get(i).optionTemplate.id == 236) {
                                if (player.inventory.itemsBody.get(8).itemOptions.get(i).param == 0) {
                                    Service.gI().sendThongBao(player, "Phục hồi sách hoặc tháo sách ra để dùng skill");
                                    return;
                                }
                            }
                        }
                        if (player.playerSkill.skillSelect.point == 7) {
                            Service.gI().SendImgSkill9(skillId, 3);
                            sendEffSkillSpecialID26(player, dir, 3);
                            break;
                        }
                        if (player.inventory.itemsBody.get(8).template.id == 1165) {
                            Service.gI().SendImgSkill9(skillId, 2);// gửi ảnh tới cilent
                            sendEffSkillSpecialID26(player, dir, 2);
                            break;
                        } else {
                            sendEffSkillSpecialID26(player, dir, 0);
                            break;
                        }
                    } else {
                        sendEffSkillSpecialID26(player, dir, 0);
                    }
                    break;
            }
            player.skillSpecial.setSkillSpecial(dir, dx, dy, x, y);
            affterUseSkill(player, player.playerSkill.skillSelect.template.id);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateSkillSpecial(Player player) {
        try {
            //  if (player.zone != null) {
//            player.zone.load_Another_To_Me(player);
//            player.zone.load_Me_To_Another(player);
            //}
            if (player.isDie() || player.effectSkill.isHaveEffectSkill()) {
                player.skillSpecial.closeSkillSpecial();
                return;
            }
            if (player.skillSpecial.skillSpecial.template.id == Skill.MA_PHONG_BA) {
                if (Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_GONG)) {
                    player.skillSpecial.lastTimeSkillSpecial = System.currentTimeMillis();
                    player.skillSpecial.closeSkillSpecial();
                    Skill curSkill = SkillUtil.getSkillbyId(player, Skill.MA_PHONG_BA);
                    int timeBinh = SkillUtil.getTimeBinh(curSkill.point);//thời gian biến thành bình

                    //hút người
                    for (Player playerMap : player.zone.getHumanoids()) {
                        if (playerMap != null && playerMap != player) {
                            if (player.skillSpecial.dir == -1 && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && this.canAttackPlayer(player, playerMap)) {
                                player.skillSpecial.playersTaget.add(playerMap);
                            } else if (player.skillSpecial.dir == 1 && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && this.canAttackPlayer(player, playerMap)) {
                                player.skillSpecial.playersTaget.add(playerMap);
                            }
                        }
                    }

                    //hút quái
                    for (Mob mobMap : player.zone.mobs) {
                        if (player.skillSpecial.dir == -1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                            player.skillSpecial.mobsTaget.add(mobMap);
                        } else if (player.skillSpecial.dir == 1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                            player.skillSpecial.mobsTaget.add(mobMap);
                        }
                        if (mobMap == null) {
                            continue;
                        }
                    }

                    //bắt đầu hút
                    if (player.inventory.itemsBody.size() > 8 && player.inventory.itemsBody.get(8).isNotNullItem()) {
                        for (int i = 1; i < player.inventory.itemsBody.get(8).itemOptions.size(); i++) {
                            if (player.inventory.itemsBody.get(8).itemOptions.get(i).optionTemplate.id == 236) {
                                player.inventory.itemsBody.get(8).itemOptions.get(i).param -= 1;
                                InventoryServiceNew.gI().sendItemBody(player);
                            }
                        }
                        if (player.playerSkill.skillSelect.point == 7) {
                            this.startSkillSpecialID26(player, 3, 2);
                        } else if (player.inventory.itemsBody.get(8).template.id == 1165) {
                            this.startSkillSpecialID26(player, 2, 1);
                        } else {
                            this.startSkillSpecialID26(player, 0, 0);
                        }
                    } else {
                        this.startSkillSpecialID26(player, 0, 0);
                    }
                    Thread.sleep(3000);//nghỉ 3s

                    //biến quái - bình
                    for (Mob mobMap : player.zone.mobs) {
                        if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                            if (player.skillSpecial.dir == -1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                                player.skillSpecial.mobsTaget.add(mobMap);
                            } else if (player.skillSpecial.dir == 1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                                player.skillSpecial.mobsTaget.add(mobMap);
                            }
                            if (mobMap == null) {
                                continue;
                            }
                            EffectSkillService.gI().sendMobToCaiBinhChua(player, mobMap, timeBinh);//biến mob thành bình
                            mobMap.idPlayerMaFuBa = player.id;
                        }
                    }

                    //biến người - bình
                    for (Player playerMap : player.zone.getHumanoids()) {
                        if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                            if (playerMap != null && playerMap != player) {
                                if (player.skillSpecial != null && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && this.canAttackPlayer(player, playerMap)) {
                                    player.skillSpecial.playersTaget.add(playerMap);

                                    if (playerMap != null && playerMap.id != player.id) {

                                        double ptdame = 0;

                                        switch (curSkill.point) {
                                            case 1:
                                            case 2:
                                                ptdame = 0.01;
                                                break;
                                            case 3:
                                            case 4:
                                                ptdame = 0.02;
                                                break;
                                            case 5:
                                            case 6:
                                                ptdame = 0.03;
                                                break;
                                            case 7:
                                            case 8:
                                                ptdame = 0.04;
                                                break;
                                            case 9:
                                                ptdame = 0.06;
                                                break;
                                            default:
                                                ptdame = 0.01;
                                                break;
                                        }

                                        int dameHit = (int) (player.nPoint.hpMax * ptdame);

                                        ItemTimeService.gI().sendItemTime(playerMap, 13915, timeBinh / 1000);
                                        EffectSkillService.gI().setMaPhongBa(playerMap, System.currentTimeMillis(), timeBinh, dameHit);
                                        Service.gI().Send_Caitrang(playerMap);
                                    }
                                }
                            }
                        } else {
                            if (playerMap != null && playerMap != player) {
                                if (player.skillSpecial != null && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && this.canAttackPlayer(player, playerMap)) {
                                    player.skillSpecial.playersTaget.add(playerMap);

                                    if (playerMap != null && playerMap.id != player.id) {

                                        double ptdame = 0;

                                        switch (curSkill.point) {
                                            case 1:
                                            case 2:
                                                ptdame = 0.01;
                                                break;
                                            case 3:
                                            case 4:
                                                ptdame = 0.02;
                                                break;
                                            case 5:
                                            case 6:
                                                ptdame = 0.03;
                                                break;
                                            case 7:
                                            case 8:
                                                ptdame = 0.04;
                                                break;
                                            case 9:
                                                ptdame = 0.06;
                                                break;
                                            default:
                                                ptdame = 0.01;
                                                break;
                                        }

                                        long dameHit = (long) (player.nPoint.hpMax * ptdame);

                                        ItemTimeService.gI().sendItemTime(playerMap, 13915, timeBinh / 1000);
                                        EffectSkillService.gI().setMaPhongBa(playerMap, System.currentTimeMillis(), timeBinh, dameHit);
                                        Service.gI().Send_Caitrang(playerMap);

                                    }
                                }
                            }
                        }
                    }
                    // Sau khi hoàn thành tất cả các tác vụ, hủy bỏ ScheduledExecutorService
//                    executorService.shutdown();
                }
            } else {
                // SUPER KAME
                if (player.skillSpecial.stepSkillSpecial == 0 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_GONG)) {
                    player.skillSpecial.lastTimeSkillSpecial = System.currentTimeMillis();
                    player.skillSpecial.stepSkillSpecial = 1;
                    if (player.skillSpecial.skillSpecial.template.id == Skill.SUPER_KAME) {
                        if (player.inventory.itemsBody.size() > 8 && player.inventory.itemsBody.get(8).isNotNullItem()) {
                            for (int i = 1; i < player.inventory.itemsBody.get(8).itemOptions.size(); i++) {
                                if (player.inventory.itemsBody.get(8).itemOptions.get(i).optionTemplate.id == 236) {
                                    player.inventory.itemsBody.get(8).itemOptions.get(i).param -= 1;
                                    InventoryServiceNew.gI().sendItemBody(player);
                                }
                            }
                            if (player.playerSkill.skillSelect.point == 7) {
                                this.startSkillSpecialID24(player, 3);
                            } else if (player.inventory.itemsBody.get(8).template.id == 1163) {
                                this.startSkillSpecialID24(player, 2);
                            } else {
                                this.startSkillSpecialID24(player, 0);
                            }
                        } else {
                            this.startSkillSpecialID24(player, 0);
                        }
                    } else {
                        // CA DIC LIEN HOAN CHUONG
                        if (player.inventory.itemsBody.size() > 8 && player.inventory.itemsBody.get(8).isNotNullItem()) {
                            for (int i = 1; i < player.inventory.itemsBody.get(8).itemOptions.size(); i++) {
                                if (player.inventory.itemsBody.get(8).itemOptions.get(i).optionTemplate.id == 236) {
                                    player.inventory.itemsBody.get(8).itemOptions.get(i).param -= 1;
                                    InventoryServiceNew.gI().sendItemBody(player);
                                }
                            }
                            if (player.playerSkill.skillSelect.point == 7) {
                                this.startSkillSpecialID25(player, 3);
                            } else if (player.inventory.itemsBody.get(8).template.id == 1164) {
                                this.startSkillSpecialID25(player, 2);
                            } else {
                                this.startSkillSpecialID25(player, 0);
                            }
                        } else {
                            this.startSkillSpecialID25(player, 0);
                        }
                    }
                } else if (player.skillSpecial.stepSkillSpecial == 1 && !Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, SkillSpecial.TIME_END_24_25)) {
                    if (MapService.gI().isMapOffline(player.zone.map.mapId)) {
                        for (Player playerMap : player.zone.getHumanoids()) {
                            if (playerMap != null) {
                                if (player.skillSpecial.dir == -1 && !playerMap.isDie()
                                        && playerMap.location.x <= player.location.x - 15
                                        && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                        && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                        && this.canAttackPlayer(player, playerMap)) {
                                    this.playerAttackPlayer(player, playerMap, false);
                                    PlayerService.gI().sendInfoHpMpMoney(playerMap);
                                }
                                if (player.skillSpecial.dir == 1 && !playerMap.isDie()
                                        && playerMap.location.x >= player.location.x + 15
                                        && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                        && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                        && this.canAttackPlayer(player, playerMap)) {
                                    this.playerAttackPlayer(player, playerMap, false);
                                    PlayerService.gI().sendInfoHpMpMoney(playerMap);
                                }
                            }
                        }
                        return;
                    }
                    for (Player playerMap : player.zone.getHumanoids()) {
                        if (player.skillSpecial.dir == -1 && !playerMap.isDie()
                                && playerMap.location.x <= player.location.x - 15
                                && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                && this.canAttackPlayer(player, playerMap)) {
                            this.playerAttackPlayer(player, playerMap, false);
                            PlayerService.gI().sendInfoHpMpMoney(playerMap);
                        }
                        if (player.skillSpecial.dir == 1 && !playerMap.isDie()
                                && playerMap.location.x >= player.location.x + 15
                                && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                                && this.canAttackPlayer(player, playerMap)) {
                            this.playerAttackPlayer(player, playerMap, false);
                            PlayerService.gI().sendInfoHpMpMoney(playerMap);
                        }
                        if (playerMap == null) {
                            continue;
                        }
                    }
                    for (Mob mobMap : player.zone.mobs) {
                        if (player.skillSpecial.dir == -1 && !mobMap.isDie()
                                && mobMap.location.x <= player.skillSpecial._xPlayer - 15
                                && Math.abs(mobMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(mobMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget) {
                            this.playerAttackMob(player, mobMap, false, false);
                        }
                        if (player.skillSpecial.dir == 1 && !mobMap.isDie()
                                && mobMap.location.x >= player.skillSpecial._xPlayer + 15
                                && Math.abs(mobMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                                && Math.abs(mobMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget) {
                            this.playerAttackMob(player, mobMap, false, false);
                        }
                        if (mobMap == null) {
                            continue;
                        }
                    }
                } else if (player.skillSpecial.stepSkillSpecial == 1) {
                    player.skillSpecial.closeSkillSpecial();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCurrLevelSpecial(Player player, Skill skill) {
        Message message = null;
        try {
            message = Service.gI().messageSubCommand((byte) 62);
            message.writer().writeShort(skill.skillId);
            message.writer().writeByte(0);
            message.writer().writeShort(skill.currLevel);
            player.sendMessage(message);
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }
    // Skill SuperKame

    public void sendEffSkillSpecialID24(Player player, byte dir, int TypePaintSkill) {
        Message message = null;
        try {
            message = new Message(-45);// passt code k dc vcb 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(24);
            message.writer().writeByte(1);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(2000);
            message.writer().writeByte(0);
            message.writer().writeByte(TypePaintSkill);// đoạn này là skill paint 
            message.writer().writeByte(0);
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // Skill liên hoàn chưởng
    public void sendEffSkillSpecialID25(Player player, byte dir, int typeskill) { //Tư thế gồng + hào quang
        Message message = null;
        try {
            message = new Message(-45);// passt code k dc vcb 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(25);
            message.writer().writeByte(2);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(2000);
            message.writer().writeByte(0);
            message.writer().writeByte(typeskill); // type skill : 0 = defaule, 1,2 = type mới
            message.writer().writeByte(0);
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // Skill Ma phong ba
    public void sendEffSkillSpecialID26(Player player, byte dir, int typeskill) {
        Message message = null;
        try {
            message = new Message(-45);// passt code k dc vcb 
            message.writer().writeByte(20);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(26); // id effect
            message.writer().writeByte(3);
            message.writer().writeByte(dir); // -1 trai | 1 phai
            message.writer().writeShort(SkillSpecial.TIME_GONG);
            message.writer().writeByte(0);
            message.writer().writeByte(typeskill);
            message.writer().writeByte(0);
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID24(Player player) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(player.skillSpecial.skillSpecial.template.id);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
            message.writer().writeShort(player.skillSpecial._xPlayer);
            message.writer().writeShort(3000); // thời gian skill chưởng chưởng nè
            message.writer().writeShort(player.skillSpecial._yObjTaget);
            message.writer().writeByte(player.gender);
            message.writer().writeByte(0);
            message.writer().writeByte(0);
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID25(Player player) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(player.skillSpecial.skillSpecial.template.id);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
            message.writer().writeShort(player.skillSpecial._yPlayer);
            message.writer().writeShort(3000); // thời gian skill chưởng chưởng nè
            message.writer().writeShort(25);
            message.writer().writeByte(player.gender);
            message.writer().writeByte(0);
            message.writer().writeByte(0);
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID26(Player player) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(26);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-75) : 75));
            message.writer().writeShort(player.skillSpecial._yPlayer);
            message.writer().writeShort(3000);
            message.writer().writeShort(player.skillSpecial._yObjTaget);
            message.writer().writeByte(player.gender);
            final byte size = (byte) (player.skillSpecial.playersTaget.size() + player.skillSpecial.mobsTaget.size());
            message.writer().writeByte(size);
            for (Player playerMap : player.skillSpecial.playersTaget) {
                message.writer().writeByte(1);
                message.writer().writeInt((int) playerMap.id);

            }
            for (Mob mobMap : player.skillSpecial.mobsTaget) {
                message.writer().writeByte(0);
                message.writer().writeByte(mobMap.id);
            }
            message.writer().writeByte(0);
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID24(Player player, int TypePaintSkill) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(player.skillSpecial.skillSpecial.template.id);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
            message.writer().writeShort(player.skillSpecial._xPlayer);
            message.writer().writeShort(3000); // thời gian skill chưởng chưởng nè
            message.writer().writeShort(player.skillSpecial._yObjTaget);
            message.writer().writeByte(TypePaintSkill);
            message.writer().writeByte(TypePaintSkill);
            message.writer().writeByte(TypePaintSkill);
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID25(Player player, int typeskill) { // bắt đầu sử dụng skill
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(player.skillSpecial.skillSpecial.template.id);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
            message.writer().writeShort(player.skillSpecial._yPlayer);
            message.writer().writeShort(3000); // thời gian skill chưởng chưởng nè
            message.writer().writeShort(25);
            message.writer().writeByte(typeskill); // skill tung ra : 0 = skill mặc định
            message.writer().writeByte(typeskill); // skill kết : 0 = skill mặc định
            message.writer().writeByte(typeskill); // skill kết : 0 = skill mặc định
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void startSkillSpecialID26(Player player, int typeskill, int imgBinh) {
        Message message = null;
        try {
            message = new Message(-45);
            message.writer().writeByte(21);
            message.writer().writeInt((int) player.id);
            message.writer().writeShort(26);
            message.writer().writeShort(player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-75) : 75));
            message.writer().writeShort(player.skillSpecial._yPlayer);
            message.writer().writeShort(3000);
            message.writer().writeShort(player.skillSpecial._yObjTaget);
            message.writer().writeByte(typeskill);
            final byte size = (byte) (player.skillSpecial.playersTaget.size() + player.skillSpecial.mobsTaget.size());
            message.writer().writeByte(size);
            for (Player playerMap : player.skillSpecial.playersTaget) {
                message.writer().writeByte(1);
                message.writer().writeInt((int) playerMap.id);
            }
            for (Mob mobMap : player.skillSpecial.mobsTaget) {
                message.writer().writeByte(0);
                message.writer().writeByte(mobMap.id);
            }
            message.writer().writeByte(imgBinh); // ảnh bình để hút vào : 0 = defaule ; 1 = ảnh cạnh ảnh 0; 2 = ảnh cạnh ảnh 1
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    // này hoc5 skill nha
    public void learSkillSpecial(Player player, byte skillID) {
        Message message = null;
        try {
            Skill curSkill = SkillUtil.createSkill(skillID, 1);
            SkillUtil.setSkill(player, curSkill);
            message = Service.gI().messageSubCommand((byte) 23);
            message.writer().writeShort(curSkill.skillId);
            player.sendMessage(message);
            message.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("88888");
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }

        }
    }

//    // này hoc5 skill nha
//    public void learSkillSpecial(Player player, byte skillID) {
//        Message message = null;
//        try {
//            Skill curSkill = SkillUtil.createSkill(skillID, 1);
//            SkillUtil.setSkill(player, curSkill);
//            message = Service.gI().messageSubCommand((byte) 23);
//            message.writer().writeShort(curSkill.skillId);
//            player.sendMessage(message);
//            message.cleanup();
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        } finally {
//            if (message != null) {
//                message.cleanup();
//                message = null;
//            }
//
//        }
//    }
    //=============================================================================
    private void useSkillAttack(Player player, Player plTarget, Mob mobTarget) {

        if (!player.isBoss) {
            if (player.isPet) {
                if (player.nPoint.stamina > 0) {
                    player.nPoint.numAttack++;
                    boolean haveCharmPet = ((Pet) player).master.charms.tdDeTu > System.currentTimeMillis();
                    if (haveCharmPet ? player.nPoint.numAttack >= 5 : player.nPoint.numAttack >= 2) {
                        player.nPoint.numAttack = 0;
                        player.nPoint.stamina--;
                    }
                } else {
                    ((Pet) player).askPea();
                    return;
                }
            } else {
                if (player.nPoint.stamina > 0) {
                    if (player.charms.tdDeoDai < System.currentTimeMillis()) {
                        player.nPoint.numAttack++;
                        if (player.nPoint.numAttack == 5) {
                            player.nPoint.numAttack = 0;
                            player.nPoint.stamina--;
                            PlayerService.gI().sendCurrentStamina(player);
                        }
                    }
                } else {
                    Service.gI().sendThongBao(player, "Thể lực đã cạn kiệt, hãy nghỉ ngơi để lấy lại sức");
                    return;
                }
            }
        }
        List<Mob> mobs;
        boolean miss = false;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.KAIOKEN: //kaioken
                long hpUse = player.nPoint.hpMax / 100 * 10;
                if (player.nPoint.hp <= hpUse) {
                    break;
                } else {
                    player.nPoint.setHp(player.nPoint.hp - hpUse);
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.gI().Send_Info_NV(player);
                }
            case Skill.DRAGON:
            case Skill.DEMON:
            case Skill.GALICK:
            case Skill.LIEN_HOAN:

                if (plTarget != null && Util.getDistance(player, plTarget) > Skill.RANGE_ATTACK_CHIEU_DAM) {
                    miss = true;
                }
                if (mobTarget != null && Util.getDistance(player, mobTarget) > Skill.RANGE_ATTACK_CHIEU_DAM) {
                    miss = true;
                }
            case Skill.KAMEJOKO:
            case Skill.MASENKO:
            case Skill.ANTOMIC:
                if (plTarget != null) {
                    playerAttackPlayer(player, plTarget, miss);
                }
                if (mobTarget != null) {
                    playerAttackMob(player, mobTarget, miss, false);
                }
                if (player.mobMe != null) {
                    player.mobMe.attack(plTarget, mobTarget);
                }

//                Mới sửa 0358124452
                if (player.playerSkill != null && player.playerSkill.skillSelect != null) {

                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                break;
            //******************************************************************
            case Skill.QUA_CAU_KENH_KHI:
                if (!player.playerSkill.prepareQCKK) {
                    //bắt đầu tụ quả cầu
                    player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
                    player.playerSkill.lastTimePrepareQCKK = System.currentTimeMillis();
                    sendPlayerPrepareSkill(player, 4000);
                } else {
                    //ném cầu
                    player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
                    mobs = new ArrayList<>();
                    List<Player> listPlayer = new ArrayList<>();
//                    if (plTarget != null) {
//                        playerAttackPlayer(player, plTarget, false);
//                        for (Mob mob : player.zone.mobs) {
//                            if (!mob.isDie()
//                                    && Util.getDistance(plTarget, mob) <= SkillUtil.getRangeQCKK(player.playerSkill.skillSelect.point)) {
//                                mobs.add(mob);
//                            }
//                        }
//                    }

                    if (plTarget != null) {
                        playerAttackPlayer(player, plTarget, false);
                        for (Player player1 : player.zone.getPlayers()) {
                            if (!player1.isDie()
                                    && canAttackPlayer(player, player1) && Util.getDistance(plTarget, player1) <= SkillUtil.getRangeQCKK(player.playerSkill.skillSelect.point)) {
                                listPlayer.add(player1);
                            }
                        }
                    }
                    if (mobTarget != null) {
                        playerAttackMob(player, mobTarget, false, true);
                        for (Mob mob : player.zone.mobs) {
                            if (!mob.equals(mobTarget) && !mob.isDie()
                                    && Util.getDistance(mob, mobTarget) <= SkillUtil.getRangeQCKK(player.playerSkill.skillSelect.point)) {
                                mobs.add(mob);
                            }
                        }
                    }
                    for (Mob mob : mobs) {
//                        mob.injured(player, player.point.getDameAttack(), true);
                        playerAttackMob(player, mob, false, true);
                    }

                    for (Player player1 : listPlayer) {
                        playerAttackPlayer(player, player1, false);
                    }
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (!player.playerSkill.prepareLaze) {
                    //bắt đầu nạp laze
                    player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
                    player.playerSkill.lastTimePrepareLaze = System.currentTimeMillis();
                    sendPlayerPrepareSkill(player, 3000);
                } else {
                    //bắn laze
                    player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
                    if (plTarget != null) {
                        playerAttackPlayer(player, plTarget, false);
                    }
                    if (mobTarget != null) {
                        playerAttackMob(player, mobTarget, false, true);
//                        mobTarget.attackMob(player, false, true);
                    }
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                break;
            case Skill.SOCOLA:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.SOCOLA);
                int timeSocola = SkillUtil.getTimeSocola();
                if (plTarget != null) {
                    EffectSkillService.gI().setSocola(plTarget, System.currentTimeMillis(), timeSocola);
                    Service.gI().Send_Caitrang(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3780, timeSocola / 1000);
                }
                if (mobTarget != null) {
                    EffectSkillService.gI().sendMobToSocola(player, mobTarget, timeSocola);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                int timeChoangDCTT = SkillUtil.getTimeDCTT(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    Service.gI().setPos(player, plTarget.location.x, plTarget.location.y);
                    playerAttackPlayer(player, plTarget, miss);
                    EffectSkillService.gI().setBlindDCTT(plTarget, System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                    PlayerService.gI().sendInfoHpMpMoney(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3779, timeChoangDCTT / 1000);
                }
                if (mobTarget != null) {
                    Service.gI().setPos(player, mobTarget.location.x, mobTarget.location.y);
//                    mobTarget.attackMob(player, false, false);
                    playerAttackMob(player, mobTarget, false, false);
                    mobTarget.effectSkill.setStartBlindDCTT(System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                }
                player.nPoint.isCrit100 = true;
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.THOI_MIEN:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.THOI_MIEN);
                int timeSleep = SkillUtil.getTimeThoiMien(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    EffectSkillService.gI().setThoiMien(plTarget, System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SLEEP_EFFECT);
                    ItemTimeService.gI().sendItemTime(plTarget, 3782, timeSleep / 1000);
                }
                if (mobTarget != null) {
                    mobTarget.effectSkill.setThoiMien(System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SLEEP_EFFECT);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TROI:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.TROI);
                int timeHold = SkillUtil.getTimeTroi(player.playerSkill.skillSelect.point);
                EffectSkillService.gI().setUseTroi(player, System.currentTimeMillis(), timeHold);
                if (plTarget != null && (!plTarget.playerSkill.prepareQCKK && !plTarget.playerSkill.prepareLaze && !plTarget.playerSkill.prepareTuSat)) {
                    player.effectSkill.plAnTroi = plTarget;
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HOLD_EFFECT);
                    EffectSkillService.gI().setAnTroi(plTarget, player, System.currentTimeMillis(), timeHold);
                }
                if (mobTarget != null) {
                    player.effectSkill.mobAnTroi = mobTarget;
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HOLD_EFFECT);
                    mobTarget.effectSkill.setTroi(System.currentTimeMillis(), timeHold);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                if (plTarget != null
                        && plTarget.isBoss
                        && MapService.gI().isMapHuyDiet(player.zone.map.mapId)
                        && Util.isTrue(40, 100)) {
                    EffectSkillService.gI().removeUseTroi(player);
                    EffectSkillService.gI().removeAnTroi(plTarget);
                    Service.gI().chat(plTarget, "Chiêu đó không có tác dụng đâu haha");
                }
                break;
        }
//        int c = 0;
//        int timeskills = 0;
//        if (c > 0 && timeskills > 0) {
//            player.playerSkill.skillSelect.lastTimeUseThisSkill = timeskills - (player.playerSkill.getCoolDown() - c);
//            Service.gI().HoiSkill(player, player.playerSkill.skillSelect.skillId, c);
//        } else {
//            player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis();
//        }

        if (!player.isBoss && player.effectSkin != null) {
            player.effectSkin.lastTimeAttack = System.currentTimeMillis();
        }

    }

    private void useSkillAlone(Player player) {
        List<Mob> mobs;
        List<Player> players;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.THAI_DUONG_HA_SAN:
                int timeStun = SkillUtil.getTimeStun(player.playerSkill.skillSelect.point);
                if (player.setClothes.thienXinHang == 5) {
                    timeStun *= 2;
                }
                mobs = new ArrayList<>();
                players = new ArrayList<>();
                if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                    List<Player> playersMap = player.zone.getHumanoids();
                    for (Player pl : playersMap) {
                        if (pl != null && pl.nPoint != null && !pl.nPoint.khangTDHS && player.playerSkill != null && player.playerSkill.skillSelect != null && !player.equals(pl)) {
                            if (Util.getDistance(player, pl) <= SkillUtil.getRangeStun(player.playerSkill.skillSelect.point)
                                    && canAttackPlayer(player, pl) //
                                    && (!pl.playerSkill.prepareQCKK && !pl.playerSkill.prepareLaze && !pl.playerSkill.prepareTuSat)) {
                                if (player.isPet && ((Pet) player).master.equals(pl)) {
                                    continue;
                                }
                                EffectSkillService.gI().startStun(pl, System.currentTimeMillis(), timeStun);
                                players.add(pl);
                            }
                        }
                    }
                }
                for (Mob mob : player.zone.mobs) {
                    if (Util.getDistance(player, mob) <= SkillUtil.getRangeStun(player.playerSkill.skillSelect.point)) {
                        mob.effectSkill.startStun(System.currentTimeMillis(), timeStun);
                        mobs.add(mob);
                    }
                }
                EffectSkillService.gI().sendEffectBlindThaiDuongHaSan(player, players, mobs, timeStun);

                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.DE_TRUNG:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.DE_TRUNG);
                if (player.mobMe != null) {
                    player.mobMe.mobMeDie();
                }
                player.mobMe = new MobMe(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.BIEN_KHI:
                //EffectSkillService.gI().sendEffectMonkey(player);
                EffectSkillService.gI().setIsMonkey(player);
                EffectSkillService.gI().sendEffectMonkey(player);

                Service.gI().sendSpeedPlayer(player, 0);
                Service.gI().Send_Caitrang(player);
                Service.gI().sendSpeedPlayer(player, -1);
                if (!player.isPet) {
                    PlayerService.gI().sendInfoHpMp(player);
                }
                Service.gI().point(player);
                Service.gI().Send_Info_NV(player);
                Service.gI().sendInfoPlayerEatPea(player);
                if (player.playerSkill != null && player.playerSkill.skillSelect != null) {
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                break;
            case Skill.KHIEN_NANG_LUONG:
                EffectSkillService.gI().setStartShield(player);
                EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.SHIELD_EFFECT);
                ItemTimeService.gI().sendItemTime(player, 3784, player.effectSkill.timeShield / 1000);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.HUYT_SAO:
                int tileHP = SkillUtil.getPercentHPHuytSao(player.playerSkill.skillSelect.point);
                if (player.zone != null) {
                    if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                        List<Player> playersMap = player.zone.getHumanoids();
                        for (Player pl : playersMap) {
                            if (pl.effectSkill != null && pl.effectSkill.useTroi) {
                                EffectSkillService.gI().removeUseTroi(pl);
                            }
                            if (!pl.isBoss && pl.gender != ConstPlayer.NAMEC
                                    && player.cFlag == pl.cFlag) {
                                EffectSkillService.gI().setStartHuytSao(pl, tileHP);
                                EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HUYT_SAO_EFFECT);
                                if (pl.nPoint != null) {
                                    pl.nPoint.calPoint();
                                    pl.nPoint.setHp(Util.maxIntValue(pl.nPoint.hp) + (Util.maxIntValue(pl.nPoint.hp) * tileHP / 100));
                                } else {
                                    System.err.print("SkillService-Skill.HUYT_SAO-pl.nPoint is null");
                                }

                                Service.gI().point(pl);
                                Service.gI().Send_Info_NV(pl);
                                ItemTimeService.gI().sendItemTime(pl, 3781, 30);
                                PlayerService.gI().sendInfoHpMp(pl);

                            }
                        }
                    } else {
                        EffectSkillService.gI().setStartHuytSao(player, tileHP);
                        EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.HUYT_SAO_EFFECT);
                        player.nPoint.calPoint();
                          player.nPoint.setHp(Util.maxIntValue(player.nPoint.hp) + (Util.maxIntValue(player.nPoint.hp) * tileHP / 100));
                        Service.gI().point(player);
                        Service.gI().Send_Info_NV(player);
                        ItemTimeService.gI().sendItemTime(player, 3781, 30);
                        PlayerService.gI().sendInfoHpMp(player);
                    }
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TAI_TAO_NANG_LUONG:
                EffectSkillService.gI().startCharge(player);

                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TU_SAT:
                if (!player.playerSkill.prepareTuSat) {
                    //gồng tự sát
                    player.playerSkill.prepareTuSat = !player.playerSkill.prepareTuSat;
                    player.playerSkill.lastTimePrepareTuSat = System.currentTimeMillis();
                    sendPlayerPrepareBom(player, 2000);
                } else {
                    if (!player.isBoss && !Util.canDoWithTime(player.playerSkill.lastTimePrepareTuSat, 1500)) {
                        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis();
                        player.playerSkill.prepareTuSat = false;
                        return;
                    }
                    //nổ
                    player.playerSkill.prepareTuSat = !player.playerSkill.prepareTuSat;
                    int rangeBom = SkillUtil.getRangeBom(player.playerSkill.skillSelect.point);
                    long dame = player.nPoint.hpMax;
                    for (Mob mob : player.zone.mobs) {
                        mob.injured(player, dame, true);
//                        if (Util.getDistance(player, mob) <= rangeBom) { //khoảng cách có tác dụng bom
//                            mob.injured(player, dame, true);
//                        }
                    }
                    List<Player> playersMap = null;
                    if (player.isBoss) {
                        playersMap = player.zone.getNotBosses();
                    } else {
                        playersMap = player.zone.getHumanoids();
                    }
                    if (!MapService.gI().isMapOffline(player.zone.map.mapId)) {
                        for (Player pl : playersMap) {
                            if (!player.equals(pl) && canAttackPlayer(player, pl)) {
                                pl.injured(player, pl.isBoss ? dame / 2 : dame, false, false);
                                PlayerService.gI().sendInfoHpMpMoney(pl);
                                Service.gI().Send_Info_NV(pl);
                            }
                        }
                    }
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                    player.injured(null, 2100000000, true, false);
                    if (player.effectSkill.tiLeHPHuytSao != 0) {
                        player.effectSkill.tiLeHPHuytSao = 0;
                        EffectSkillService.gI().removeHuytSao(player);
                    }
                }
                break;
            case Skill.HAKAI:
                EffectSkillService.gI().sendEffectMonkey(player);
//                EffectSkillService.gI().startCharge(player);
                if (player.effectSkill.levelBienHinh < 7) {
                    EffectSkillService.gI().setBienHinh(player);
                    EffectSkillService.gI().sendEffectMonkey(player);
                    EffectSkillService.gI().sendEffectEndCharge(player);

                    Service.gI().Send_Caitrang(player);

                    Service.gI().point(player);
                    player.nPoint.setFullHpMp();
                    PlayerService.gI().sendInfoHpMp(player);

                    RadarService.gI().RadarSetAura(player);

                    ItemTimeService.gI().sendItemTime(player, 22059, player.effectSkill.timeBienHinh / 1000);

                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                }
                break;
        }
    }

    private void useSkillBuffToPlayer(Player player, Player plTarget) {
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.TRI_THUONG:
                List<Player> players = new ArrayList();
                int percentTriThuong = SkillUtil.getPercentTriThuong(player.playerSkill.skillSelect.point);
                int point = player.playerSkill.skillSelect.point;
                if (canHsPlayer(player, plTarget)) {
                    players.add(plTarget);
                    List<Player> playersMap = player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!pl.equals(plTarget)) {
                            if (canHsPlayer(player, plTarget) && Util.getDistance(player, pl) <= 300) {
                                players.add(pl);
                            } else {
                                Service.gI().sendThongBao(player, "lỗi buff");
                            }
                        }
                    }
                    playerAttackPlayer(player, plTarget, false);
                    for (Player pl : players) {
                        boolean isDie = pl.isDie();
                        long hpHoi = pl.nPoint.hpMax * percentTriThuong / 100;
                        long mpHoi = pl.nPoint.mpMax * percentTriThuong / 100;
                        pl.nPoint.addHp(hpHoi);
                        pl.nPoint.addMp(mpHoi);
                        if (isDie) {
                            Service.gI().hsChar(pl, hpHoi, mpHoi);
                            PlayerService.gI().sendInfoHpMp(pl);
                        } else {
                            Service.gI().Send_Info_NV(pl);
                            PlayerService.gI().sendInfoHpMp(pl);
                        }
                    }
                    long hpHoiMe = player.nPoint.hp * percentTriThuong / 100;
                    player.nPoint.addHp(hpHoiMe);
                    PlayerService.gI().sendInfoHp(player);
                }

                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
        }
    }

    private void phanSatThuong(Player plAtt, Player plTarget, double dame) {

        int percentPST = plTarget.nPoint.tlPST;
        if (percentPST != 0) {
            double damePST = dame * percentPST / 100;
            Message msg;
            try {
                msg = new Message(56);
                msg.writer().writeInt((int) plAtt.id);
                if (damePST >= plAtt.nPoint.hp) {
                    damePST = Util.maxIntValue(plAtt.nPoint.hp - 1);
                }

                damePST = plAtt.injured(null, damePST, true, false);

                msg.writeFix(Util.maxIntValue(plAtt.nPoint.hp));
                msg.writeFix(Util.maxIntValue(damePST));
                msg.writer().writeBoolean(false);
                msg.writer().writeByte(36);
                Service.gI().sendMessAllPlayerInMap(plAtt.zone, msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.logException(SkillService.class, e);
            }
        }
    }

    private void hutHPMP(Player player, double dame, boolean attackMob) {
        int tiLeHutHp = player.nPoint.getTileHutHp(attackMob);
        int tiLeHutMp = player.nPoint.getTiLeHutMp();
        long hpHoi = Util.maxIntValue(dame * tiLeHutHp / 100);
        long mpHoi = Util.maxIntValue(dame * tiLeHutMp / 100);
        if (hpHoi > 0 || mpHoi > 0) {
            PlayerService.gI().hoiPhuc(player, hpHoi, mpHoi);
        }
    }

    private void playerAttackPlayer(Player plAtt, Player plInjure, boolean miss) {

        if (plInjure.effectSkill.anTroi) {
            plAtt.nPoint.isCrit100 = true;
        }
        double dameHit = plInjure.injured(plAtt, miss ? 0 : plAtt.nPoint.getDameAttack(false), false, false);
        phanSatThuong(plAtt, plInjure, dameHit);
        hutHPMP(plAtt, dameHit, false);
//        double a = dameHit * 100 / plInjure.nPoint.hpMax;
        double b = plInjure.nPoint.hp;
//        if (b > 20000000000L) {
//            a = 1;
//        } else {
//            a = dameHit;
//        }
        Message msg;
        try {
            msg = new Message(-60);
            msg.writer().writeInt((int) plAtt.id); //id pem
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); //skill pem
            msg.writer().writeByte(1); //số người pem
            msg.writer().writeInt((int) plInjure.id); //id ăn pem
            byte typeSkill = SkillUtil.getTyleSkillAttack(plAtt.playerSkill.skillSelect);
            msg.writer().writeByte(typeSkill == 2 ? 0 : 1); //read continue
            msg.writer().writeByte(typeSkill); //type skill
            msg.writeFix(Util.maxIntValue(dameHit)); //dame ăn
            msg.writer().writeBoolean(plInjure.isDie()); //is die
            msg.writer().writeBoolean(plAtt.nPoint.isCrit); //crit
            if (typeSkill != 1) {
                Service.gI().sendMessAllPlayerInMap(plAtt.zone, msg);
                msg.cleanup();
            } else {
                plInjure.sendMessage(msg);
                msg.cleanup();
                msg = new Message(-60);
                msg.writer().writeInt((int) plAtt.id); //id pem
                msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); //skill pem
                msg.writer().writeByte(1); //số người pem
                msg.writer().writeInt((int) plInjure.id); //id ăn pem
                msg.writer().writeByte(typeSkill == 2 ? 0 : 1); //read continue
                msg.writer().writeByte(0); //type skill
                msg.writeFix(Util.maxIntValue(dameHit)); //dame ăn
                msg.writer().writeBoolean(plInjure.isDie()); //is die
                msg.writer().writeBoolean(plAtt.nPoint.isCrit); //crit
                Service.gI().sendMessAnotherNotMeInMap(plInjure, msg);
                msg.cleanup();
            }
            if (plAtt.isPl()) {
                plAtt.Hppl = "\n|7|Name Boss: " + plInjure.name;
                plAtt.Hppl += "\n|3|Hp Boss: " + Util.powerToString(plInjure.nPoint.hp);
            }

            if (plAtt.isPl() && b > 20000000000L) {
                Service.gI().sendThongBao(plAtt, "##Thông tin đối thủ:\n"
                        + plAtt.Hppl);
            }
            if (plAtt.name.toLowerCase().contains("nhân bản") || plInjure.name.toLowerCase().contains("nhân bản")) {
                PlayerService.gI().sendTypePk(plAtt);
                PlayerService.gI().sendTypePk(plInjure);
                PlayerService.gI().sendInfoHpMpMoney(plAtt);
                PlayerService.gI().sendInfoHpMpMoney(plInjure);
                Service.gI().Send_Info_NV(plAtt);
                Service.gI().Send_Info_NV(plInjure);
                plAtt.zone.load_Another_To_Me(plAtt);
                plAtt.zone.load_Me_To_Another(plAtt);
            }
            Service.gI().addSMTN(plInjure, (byte) 2, 1, false);
            if (plInjure.isDie() && !plAtt.isBoss) {
                plAtt.fightMabu.changePoint((byte) 5);
            }
            try {
                msg = Service.gI().messageSubCommand((byte) 14);
                msg.writer().writeInt((int) plInjure.id);
                msg.writeFix(Util.maxIntValue(plInjure.nPoint.hp));
                msg.writer().writeByte(0);
                msg.writeFix(Util.maxIntValue(plInjure.nPoint.hpMax));
                Service.gI().sendMessAnotherNotMeInMap(plInjure, msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PlayerService.gI().sendTypePk(plAtt);
            PlayerService.gI().sendTypePk(plInjure);
            plAtt.tt = "|3|HP: " + Util.powerToString(plInjure.nPoint.hp) + "\n|1|MP: " + Util.powerToString(plInjure.nPoint.mp) + "\n|7|dame: " + Util.powerToString(dameHit);
            if (dameHit > 2000000000 || plInjure.nPoint.hp > Integer.MAX_VALUE || plInjure.nPoint.mp > Integer.MAX_VALUE) {

                if (plAtt.isPet) {
                    Service.gI().sendThongBao(((Pet) plAtt).master, plAtt.tt);
                } else {
                    Service.gI().sendThongBao(plAtt, plAtt.tt);
                }
            }

            if (!plAtt.isNewPet && !plInjure.isNewPet || !plAtt.isMiniPet && !plInjure.isMiniPet) {
                if (plInjure.pet != null) {
                    if (!plInjure.pet.enemies2.contains(plAtt)) {
                        Service.gI().chat(plInjure.pet,
                                "Mi làm ta nổi giận rồi " + plAtt.name);
                        plInjure.pet.enemies2.add(plAtt);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(SkillService.class, e);
        }
    }

    private void playerAttackMob(Player plAtt, Mob mob, boolean miss, boolean dieWhenHpFull) {
        if (!mob.isDie()) {
            double dameHit = plAtt.nPoint.getDameAttack(true);

            if (plAtt.charms.tdBatTu > System.currentTimeMillis() && plAtt.nPoint.hp == 1) {
                dameHit = 0;
            }
            if (plAtt.itemTime.isBienHinhMa && plAtt.nPoint.hp == 1) {
                dameHit = 0;
            }
//            if (plAtt.itemTime.isBienHinhMa) {
//                dameHit = 0;
//                
//            }
            if (plAtt.charms.tdManhMe > System.currentTimeMillis()) {
                dameHit += (dameHit * 150 / 100);
            }
            if (plAtt.isPet) {
                if (((Pet) plAtt).charms.tdDeTu > System.currentTimeMillis()) {
                    dameHit *= 2;
                }
            }
            if (miss) {
                dameHit = 0;
            }
            hutHPMP(plAtt, dameHit, true);
            sendPlayerAttackMob(plAtt, mob);
            mob.injured(plAtt, dameHit, dieWhenHpFull);
            plAtt.tt = "HP Đối Thủ: " + Util.powerToString(mob.point.hp) + "\nDAME gây ra: " + Util.powerToString(dameHit);
        }
    }

    private void sendPlayerPrepareSkill(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(4);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(affterMiliseconds);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPlayerPrepareBom(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(7);
            msg.writer().writeInt((int) player.id);
//            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(104);
            msg.writer().writeShort(affterMiliseconds);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean canUseSkillWithMana(Player player) {
        if (player.playerSkill.skillSelect != null) {
            if (player.playerSkill.skillSelect.template.id == Skill.KAIOKEN) {
                long hpUse = player.nPoint.hpMax / 100 * 10;
                if (player.nPoint.hp <= hpUse) {
                    return false;
                }
            }
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 1:
                    long mpUse = player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100;
                    if (player.nPoint.mp >= mpUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 2:
                    if (player.nPoint.mp > 0) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean canUseSkillWithCooldown(Player player) {
        if (player.playerSkill.skillSelect == null) {
            return false;
        }
        boolean canUseSkill = false;
        switch (player.playerSkill.skillSelect.template.manaUseType) {
            case 0:
                if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                    canUseSkill = true;
                }
                break;
            case 1:
                long mpUse = (Util.maxIntValue(player.nPoint.mpMax / 100 * player.playerSkill.skillSelect.manaUse));
                if (player.nPoint.mp >= mpUse) {
                    canUseSkill = true;
                }
                break;
            case 2:
                canUseSkill = true;
                break;
        }
        return canUseSkill && Util.canDoWithTime(player.playerSkill.skillSelect.lastTimeUseThisSkill, player.playerSkill.skillSelect.coolDown);
    }

    private void affterUseSkill(Player player, int skillId) {

        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        switch (skillId) {
            case Skill.DICH_CHUYEN_TUC_THOI:
                if (intrinsic.id == 6) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.THOI_MIEN:
                if (intrinsic.id == 7) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.SOCOLA:
                if (intrinsic.id == 14) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.TROI:
                if (intrinsic.id == 22) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
        }
        setMpAffterUseSkill(player);
        setLastTimeUseSkill(player, skillId);
    }

    private void setMpAffterUseSkill(Player player) {
        if (player.playerSkill.skillSelect != null) {
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        player.nPoint.setMp(player.nPoint.mp - player.playerSkill.skillSelect.manaUse);
                    }
                    break;
                case 1:
                    int mpUse = (int) (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100);
                    if (player.nPoint.mp >= mpUse) {
                        player.nPoint.setMp(player.nPoint.mp - mpUse);
                    }
                    break;
                case 2:
                    player.nPoint.setMp(0);
                    break;
            }
            PlayerService.gI().sendInfoHpMpMoney(player);
        }
    }

    private void setLastTimeUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        int subTimeParam = 0;
        switch (skillId) {
            case Skill.TRI_THUONG:
                if (intrinsic.id == 10) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.THAI_DUONG_HA_SAN:
                if (intrinsic.id == 3) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.QUA_CAU_KENH_KHI:
                if (intrinsic.id == 4) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.KHIEN_NANG_LUONG:
                if (intrinsic.id == 5 || intrinsic.id == 15 || intrinsic.id == 20) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (intrinsic.id == 11) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.DE_TRUNG:
                if (intrinsic.id == 12) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.TU_SAT:
                if (intrinsic.id == 19) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.HUYT_SAO:
                if (intrinsic.id == 21) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.HAKAI:
                subTimeParam = 1;
                break;
        }
        int coolDown = player.playerSkill.skillSelect.coolDown;

        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis() - (coolDown * subTimeParam / 100);

        if (subTimeParam != 0) {
            Service.gI().sendTimeSkill(player);
        }
    }

    private boolean canHsPlayer(Player player, Player plTarget) {
        if (plTarget == null) {
            return false;
        }
        if (plTarget.isBoss) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_ALL) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_PVP) {
            return false;
        }
        if (player.cFlag != 0) {
            if (plTarget.cFlag != 0 && plTarget.cFlag != player.cFlag) {
                return false;
            }
        } else if (plTarget.cFlag != 0) {
            return false;
        }
        return true;
    }

    public boolean canAttackPlayer(Player p1, Player p2) {
        if (p1.isDie() || p2.isDie()) {
            return false;
        }
        if (p1.zone.map.mapId == 129 && p1.typePk > 0 && p2.typePk > 0) {
            return true;
        }

        if (p1.typePk == ConstPlayer.PK_ALL || p2.typePk == ConstPlayer.PK_ALL) {
            return true;
        }
        if ((p1.cFlag != 0 && p2.cFlag != 0)
                && (p1.cFlag == 8 || p2.cFlag == 8 || p1.cFlag != p2.cFlag)) {
            return true;
        }
        if (p1.pvp == null || p2.pvp == null) {
            return false;
        }
        if (p1.pvp.isInPVP(p2) || p2.pvp.isInPVP(p1)) {
            return true;
        }
        return false;
    }

    private void sendPlayerAttackMob(Player plAtt, Mob mob) {
        Message msg;
        try {
            msg = new Message(54);
            msg.writer().writeInt((int) plAtt.id);
            if (plAtt.playerSkill != null) {
                msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId);
            }
            msg.writer().writeByte(mob.id);
            Service.gI().sendMessAllPlayerInMap(plAtt.zone, msg);
            msg.cleanup();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void selectSkill(Player player, int skillId) {
        Skill skillBefore = player.playerSkill.skillSelect;
        for (Skill skill : player.playerSkill.skills) {
            if (skill.skillId != -1 && skill.template.id == skillId) {
                player.playerSkill.skillSelect = skill;
                switch (skillBefore.template.id) {
                    case Skill.DRAGON:
                    case Skill.KAMEJOKO:
                    case Skill.DEMON:
                    case Skill.MASENKO:
                    case Skill.LIEN_HOAN:
                    case Skill.GALICK:
                    case Skill.ANTOMIC:
                        switch (skill.template.id) {
                            case Skill.KAMEJOKO:
//                                skill.lastTimeUseThisSkill = System.currentTimeMillis() + (5000 / 2);
//                                break;
                            case Skill.DRAGON:
                            case Skill.DEMON:
                            case Skill.MASENKO:
                            case Skill.LIEN_HOAN:
                            case Skill.GALICK:
                            case Skill.ANTOMIC:
//                                skill.lastTimeUseThisSkill = System.currentTimeMillis() + (skill.coolDown / 2);
                                break;
                        }
                        break;
                }
                break;
            }
        }
    }
}
