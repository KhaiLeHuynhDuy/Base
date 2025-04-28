package nro.services;

import nro.models.mob.Mob;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.network.io.Message;
import nro.utils.SkillUtil;
import static nro.utils.SkillUtil.getSkillbyId;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.models.card.RadarService;
import nro.server.Manager;

public class EffectSkillService {

    public static final byte TURN_ON_EFFECT = 1;
    public static final byte TURN_OFF_EFFECT = 0;
    public static final byte TURN_OFF_ALL_EFFECT = 2;

    public static final byte HOLD_EFFECT = 32;
    public static final byte SHIELD_EFFECT = 33;
    public static final byte HUYT_SAO_EFFECT = 39;
    public static final byte BLIND_EFFECT = 40;
    public static final byte SLEEP_EFFECT = 41;
    public static final byte STONE_EFFECT = 42;

    public static final byte HAKAI_EFFECT = 55;

    public static final int ICE_EFFECT = 202;
    public static final int THO_EFFECT = 206;
    private static EffectSkillService i;

    private EffectSkillService() {

    }

    public static EffectSkillService gI() {
        if (i == null) {
            i = new EffectSkillService();
        }
        return i;
    }

    //hiệu ứng player dùng skill
    public void sendEffectUseSkill(Player player, byte skillId) {
        Skill skill = SkillUtil.getSkillbyId(player, skillId);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(8);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    public void sendEffectPlayer(Player plUseSkill, Player plTarget, byte toggle, byte effect) {
        Message msg;
        try {
            msg = new Message(-124);
            msg.writer().writeByte(toggle); //0: hủy hiệu ứng, 1: bắt đầu hiệu ứng
            msg.writer().writeByte(0); //0: vào phần phayer, 1: vào phần mob
            if (toggle == TURN_OFF_ALL_EFFECT) {
                msg.writer().writeInt((int) plTarget.id);
            } else {
                msg.writer().writeByte(effect); //loại hiệu ứng
                msg.writer().writeInt((int) plTarget.id); //id player dính effect
                msg.writer().writeInt((int) plUseSkill.id); //id player dùng skill
            }
            Service.gI().sendMessAllPlayerInMap(plUseSkill.zone, msg);
            msg.cleanup();
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    public void sendEffectMob(Player plUseSkill, Mob mobTarget, byte toggle, byte effect) {
        Message msg;
        try {
            msg = new Message(-124);
            msg.writer().writeByte(toggle); //0: hủy hiệu ứng, 1: bắt đầu hiệu ứng
            msg.writer().writeByte(1); //0: vào phần phayer, 1: vào phần mob
            msg.writer().writeByte(effect); //loại hiệu ứng
            msg.writer().writeByte(mobTarget.id); //id mob dính effect
            msg.writer().writeInt((int) plUseSkill.id); //id player dùng skill
            Service.gI().sendMessAllPlayerInMap(mobTarget.zone, msg);
            msg.cleanup();
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    //Trói *********************************************************************
    //dừng sử dụng trói
    public void removeUseTroi(Player player) {
        if (player.effectSkill.mobAnTroi != null) {
            player.effectSkill.mobAnTroi.effectSkill.removeAnTroi();
        }
        if (player.effectSkill.plAnTroi != null) {
            removeAnTroi(player.effectSkill.plAnTroi);
        }
        player.effectSkill.useTroi = false;
        player.effectSkill.mobAnTroi = null;
        player.effectSkill.plAnTroi = null;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, HOLD_EFFECT);
    }

    //hết thời gian bị trói
    public void removeAnTroi(Player player) {
        if (player != null && player.effectSkill != null) {
            player.effectSkill.anTroi = false;
            player.effectSkill.plTroi = null;
            sendEffectPlayer(player, player, TURN_OFF_EFFECT, HOLD_EFFECT);
        }
    }

    public void setAnTroi(Player player, Player plTroi, long lastTimeAnTroi, int timeAnTroi) {
        player.effectSkill.anTroi = true;
        player.effectSkill.lastTimeAnTroi = lastTimeAnTroi;
        player.effectSkill.timeAnTroi = timeAnTroi;
        player.effectSkill.plTroi = plTroi;
    }

    public void setUseTroi(Player player, long lastTimeTroi, int timeTroi) {
        player.effectSkill.useTroi = true;
        player.effectSkill.lastTimeTroi = lastTimeTroi;
        player.effectSkill.timeTroi = timeTroi;
    }
    //**************************************************************************

    //Thôi miên ****************************************************************
    //thiết lập thời gian bắt đầu bị thôi miên
    public void setThoiMien(Player player, long lastTimeThoiMien, int timeThoiMien) {
        player.effectSkill.isThoiMien = true;
        player.effectSkill.lastTimeThoiMien = lastTimeThoiMien;
        player.effectSkill.timeThoiMien = timeThoiMien;
    }

    //hết hiệu ứng thôi miên
    public void removeThoiMien(Player player) {
        player.effectSkill.isThoiMien = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, SLEEP_EFFECT);
    }

    //**************************************************************************
    //Thái dương hạ san &&&&****************************************************
    //player ăn choáng thái dương hạ san
    public void startStun(Player player, long lastTimeStartBlind, int timeBlind) {
        player.effectSkill.lastTimeStartStun = lastTimeStartBlind;
        player.effectSkill.timeStun = timeBlind;
        player.effectSkill.isStun = true;
        sendEffectPlayer(player, player, TURN_ON_EFFECT, BLIND_EFFECT);
    }

    //kết thúc choáng thái dương hạ san
    public void removeStun(Player player) {
        player.effectSkill.isStun = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, BLIND_EFFECT);
    }

    //**************************************************************************
    //Cải trang Drabura Frost
    public void SetHoaBang(Player player, long lastTimeHoaBang, int timeHoaBang) {
        player.effectSkill.lastTimeHoaBang = lastTimeHoaBang;
        player.effectSkill.timeBang = timeHoaBang;
        player.effectSkill.isBang = true;
        sendEffectPlayer(player, player, TURN_ON_EFFECT, (byte) BLIND_EFFECT);

    }

    public void removeBang(Player player) {
        player.effectSkill.isBang = false;
        Service.gI().Send_Caitrang(player);
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, (byte) BLIND_EFFECT);
    }

    //**************************************************************************
    //Cải trang Drabura Hóa Đá
    public void SetHoaDa(Player player, long lastTimeHoaDa, int timeHoaDa) {
        player.effectSkill.lastTimeHoaDa = lastTimeHoaDa;
        player.effectSkill.timeDa = timeHoaDa;
        player.effectSkill.isDa = true;

    }

    public void removeDa(Player player) {
        player.effectSkill.isDa = false;
        Service.gI().Send_Caitrang(player);
    }

    //Hakai
    public void SetHakai(Player player, long lastTimeHakai, int timeHakai) {
        player.effectSkill.lastTimeHakai = lastTimeHakai;
        player.effectSkill.timeHakai = timeHakai;
        player.effectSkill.isHakai = true;
        sendEffectPlayer(player, player, TURN_ON_EFFECT, (byte) HAKAI_EFFECT);

    }

    public void removeHakai(Player player) {
        player.effectSkill.isHakai = false;
        Service.gI().Send_Caitrang(player);
//        Service.gI().removeEffectChar(player, 55);
//        sendEffectPlayer(player, player, TURN_OFF_EFFECT, (byte) HAKAI_EFFECT);
    }
    
    public void setMaPhongBa(Player player, long lastTimeMaPhongBa, int timeMaPhongBa, long dameMaFuBa) {
        player.effectSkill.lastTimeCaiBinhChua = lastTimeMaPhongBa;
        player.effectSkill.timeCaiBinhChua = timeMaPhongBa;
        player.dameMaFuBa = dameMaFuBa;
        player.effectSkill.isCaiBinhChua = true;
    }

    public void removeMaPhongBa(Player player) {
        player.effectSkill.lastTimeCaiBinhChua = 0;
        player.effectSkill.timeCaiBinhChua = 0;
        player.effectSkill.isCaiBinhChua = false;
        player.dameMaFuBa = 0;
        Service.gI().Send_Caitrang(player);
    }

    //**************************************************************************
    //Cải trang Thỏ Đại Ca
    public void SetHoaCarot(Player player, long lastTimeHoaCarot, int timeHoaCarot) {
        player.effectSkill.lastTimeHoaCarot = lastTimeHoaCarot;
        player.effectSkill.timeCarot = timeHoaCarot;
        player.effectSkill.isCarot = true;

    }

    public void removeCarot(Player player) {
        player.effectSkill.isCarot = false;
        Service.gI().Send_Caitrang(player);
    }

    public void SetHoaBinh(Player player, long lastTimeHoaBinh, int timeHoaBinh) {
        player.effectSkill.lastTimeHoaBinh = lastTimeHoaBinh;
        player.effectSkill.timeBinh = timeHoaBinh;
        player.effectSkill.isBinh = true;

    }

    public void removeBinh(Player player) {
        player.effectSkill.isBinh = false;
        Service.gI().Send_Caitrang(player);
    }

    public void sendPlayerToCaiBinh(Player pl, long lastTimeHoaBinh) {
        pl.effectSkill.lastTimeHoaBinh = lastTimeHoaBinh;
        //  pl.effectSkill.timeBinh = timeHoaBinh;
        pl.effectSkill.isBinh = true;

    }

    //Socola *******************************************************************
    //player biến thành socola
    public void setSocola(Player player, long lastTimeSocola, int timeSocola) {
        player.effectSkill.lastTimeSocola = lastTimeSocola;
        player.effectSkill.timeSocola = timeSocola;
        player.effectSkill.isSocola = true;
        player.effectSkill.countPem1hp = 0;
    }

    //player trở lại thành người
    public void removeSocola(Player player) {
        player.effectSkill.isSocola = false;
        Service.gI().Send_Caitrang(player);
    }

    public void setSoHai(Player player, long lastTimeSoHai, int timeSoHai) {
        player.effectSkill.lastTimeSoHai = lastTimeSoHai;
        player.effectSkill.timeSoHai = timeSoHai;
        player.effectSkill.isSoHai = true;

        sendEffectPlayer(player, player, TURN_ON_EFFECT, (byte) THO_EFFECT);
    }

    //player trở lại thành người
    public void removeSoHai(Player player) {
        player.effectSkill.isSoHai = false;
        Service.gI().Send_Caitrang(player);
        sendEffectPlayer(player, player, TURN_ON_EFFECT, (byte) ICE_EFFECT);
    }

    //quái biến thành socola
    public void sendMobToSocola(Player player, Mob mob, int timeSocola) {
        Message msg;
        try {
            msg = new Message(-112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(mob.id); //mob id
            msg.writer().writeShort(4133); //icon socola
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
            mob.effectSkill.setSocola(System.currentTimeMillis(), timeSocola);
        } catch (Exception e) { e.printStackTrace();
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }
    //**************************************************************************

    //Dịch chuyển tức thời *****************************************************
    public void setBlindDCTT(Player player, long lastTimeDCTT, int timeBlindDCTT) {
        player.effectSkill.isBlindDCTT = true;
        player.effectSkill.lastTimeBlindDCTT = lastTimeDCTT;
        player.effectSkill.timeBlindDCTT = timeBlindDCTT;
    }

    public void removeBlindDCTT(Player player) {
        player.effectSkill.isBlindDCTT = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, BLIND_EFFECT);
    }
    //**************************************************************************

    //Huýt sáo *****************************************************************
    //Hưởng huýt sáo
    public void setStartHuytSao(Player player, int tiLeHP) {
        if (player.effectSkill != null) {
            player.effectSkill.tiLeHPHuytSao = tiLeHP;

            player.effectSkill.lastTimeHuytSao = System.currentTimeMillis();
        }
    }

    //Hết hiệu ứng huýt sáo
    public void removeHuytSao(Player player) {
        player.effectSkill.tiLeHPHuytSao = 0;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, HUYT_SAO_EFFECT);
        Service.gI().point(player);
        Service.gI().Send_Info_NV(player);
    }

    //**************************************************************************
    //Biến khỉ *****************************************************************
    //Bắt đầu biến khỉ
    public void setIsMonkey(Player player) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) { ex.printStackTrace();
            Logger.getLogger(EffectSkillService.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (player.playerSkill != null) {//0358124452
            int timeMonkey = SkillUtil.getTimeMonkey(player.playerSkill.skillSelect.point);
            if (player.setClothes.cadic == 5) {
                timeMonkey *= 5;
            }
            player.effectSkill.isMonkey = true;
            player.effectSkill.timeMonkey = timeMonkey;
            player.effectSkill.lastTimeUpMonkey = System.currentTimeMillis();
            player.effectSkill.levelMonkey = (byte) player.playerSkill.skillSelect.point;
            player.nPoint.setHp(player.nPoint.hp * 2);
        }
    }

    public void monkeyDown(Player player) {
        player.effectSkill.isMonkey = false;
        player.effectSkill.levelMonkey = 0;
        if (player.nPoint.hp > player.nPoint.hpMax) {
            player.nPoint.setHp(player.nPoint.hpMax);
        }

        sendEffectEndCharge(player);
        sendEffectMonkey(player);
        Service.gI().setNotMonkey(player);
        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
        PlayerService.gI().sendInfoHpMp(player);
        Service.gI().Send_Info_NV(player);
        Service.gI().sendInfoPlayerEatPea(player);
    }

//    public void setIsBienHinh(Player player) {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(EffectSkillService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        int timeBienHinh = SkillUtil.getTimeMonkey(player.playerSkill.skillSelect.point);
////        if(player.setClothes.cadic == 5){
////            timeBienHinh *= 5;
////        }
//        player.effectSkill.isBienHinh = true;
//        player.effectSkill.timeBienHinh = timeBienHinh;
//        player.effectSkill.lastTimeBienHinh = System.currentTimeMillis();
//        player.effectSkill.levelBienHinh = (byte) player.playerSkill.skillSelect.point;
//        player.nPoint.setHp(player.nPoint.hp * 2);
//    }
//
//    public void BienHinhDown(Player player) {
//        player.effectSkill.isBienHinh = false;
//        player.effectSkill.levelBienHinh = 0;
//        if (player.nPoint.hp > player.nPoint.hpMax) {
//            player.nPoint.setHp(player.nPoint.hpMax);
//        }
//
//        sendEffectEndCharge(player);
//        sendEffectBienHinh(player);
//        Service.gI().setNotBienHinh(player);
//        Service.gI().Send_Caitrang(player);
//        Service.gI().point(player);
//        PlayerService.gI().sendInfoHpMp(player);
//        Service.gI().Send_Info_NV(player);
//        Service.gI().sendInfoPlayerEatPea(player);
//    }
    //**************************************************************************
    //Tái tạo năng lượng *******************************************************
    public void startCharge(Player player) {
        if (!player.effectSkill.isCharging) {
            player.effectSkill.isCharging = true;
            sendEffectCharge(player);
        }
    }

    public void stopCharge(Player player) {
        player.effectSkill.countCharging = 0;
        player.effectSkill.isCharging = false;
        sendEffectStopCharge(player);

    }

    //**************************************************************************
    //Khiên năng lượng *********************************************************
    public void setStartShield(Player player) {
        player.effectSkill.isShielding = true;
        player.effectSkill.lastTimeShieldUp = System.currentTimeMillis();
        player.effectSkill.timeShield = SkillUtil.getTimeShield(player.playerSkill.skillSelect.point);
    }

    public void removeShield(Player player) {
        player.effectSkill.isShielding = false;
        sendEffectPlayer(player, player, TURN_OFF_EFFECT, SHIELD_EFFECT);
    }

    public void breakShield(Player player) {
        removeShield(player);
        Service.gI().sendThongBao(player, "Khiên năng lượng đã bị vỡ!");
        ItemTimeService.gI().removeItemTime(player, 3784);
    }

    public void setStartHaoQuang(Player player) {
        player.effectSkill.isShielding = true;
        player.effectSkill.lastTimeShieldUp = System.currentTimeMillis();
        player.effectSkill.timeShield = SkillUtil.getTimeShield(player.playerSkill.skillSelect.point);
    }

    //**************************************************************************
    public void sendEffectBlindThaiDuongHaSan(Player plUseSkill, List<Player> players, List<Mob> mobs, int timeStun) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plUseSkill.id);
            msg.writer().writeShort(plUseSkill.playerSkill.skillSelect.skillId);
            msg.writer().writeByte(mobs.size());
            for (Mob mob : mobs) {
                msg.writer().writeByte(mob.id);
                msg.writer().writeByte(timeStun / 1000);
            }
            msg.writer().writeByte(players.size());
            for (Player pl : players) {
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeByte(timeStun / 1000);
            }
            Service.gI().sendMessAllPlayerInMap(plUseSkill.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    //hiệu ứng bắt đầu gồng
    public void sendEffectStartCharge(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_NANG_LUONG);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    //hiệu ứng đang gồng
    public void sendEffectCharge(Player player) {
        Skill skill = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_NANG_LUONG);
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(skill.skillId);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    //dừng gồng
    public void sendEffectStopCharge(Player player) {
        try {
            Message msg = new Message(-45);
            msg.writer().writeByte(3);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(-1);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    //hiệu ứng nổ kết thúc gồng
    public void sendEffectEndCharge(Player player) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(5);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            nro.utils.Logger.logException(EffectSkillService.class, e);
        }
    }

    public void setIsMonkeyTrungThu(Player player) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();

        }
        Skill khi = getSkillbyId(player, Skill.BIEN_KHI);
        int timeMonkey = 0;
//        System.out.println("Player: " + player.name + " bien monkey trung thu lv" + khi.point + "");
        if (khi.point == 0) {
            timeMonkey = (1 + 5) * 10000;
        } else {
            timeMonkey = SkillUtil.getTimeMonkey(khi.point);
        }

        if (player.setClothes.cadic == 5) {
            timeMonkey *= 5;
        }
        player.effectSkill.isMonkey = true;
        player.effectSkill.timeMonkey = timeMonkey;
        player.effectSkill.lastTimeUpMonkey = System.currentTimeMillis();
        if (khi.point == 0) {
            player.effectSkill.levelMonkey = 1;
        } else {
            player.effectSkill.levelMonkey = (byte) khi.point;
        }
        player.nPoint.setHp(player.nPoint.hp * 2);
    }

    //hiệu ứng biến khỉ
    public void sendEffectMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(6);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(97);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) { e.printStackTrace();
        }
    }

    public void sendMobToCaiBinhChua(Player player, Mob mob, int timeBinh) {
        Message message = null;
        try {
            message = new Message(-112);
            message.writer().writeByte(1);
            message.writer().writeByte(mob.id); //mob id
            message.writer().writeShort(13915); //icon cái bình
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
            mob.effectSkill.setCaiBinhChua(System.currentTimeMillis(), timeBinh);
        } catch (Exception e) { e.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
            }
        }
    }

    public void sendMobToHakai(Player player, Mob mob, int timeHakai) {
        Message message = null;
        try {
            message = new Message(-112);
            message.writer().writeByte(1);
            message.writer().writeByte(mob.id); //mob id
//            message.writer().writeShort(13915); //icon cái bình
            message.writer().writeShort(18221); //icon cái bình
            Service.gI().sendMessAllPlayerInMap(player.zone, message);
            message.cleanup();
            mob.effectSkill.setHakai(System.currentTimeMillis(), timeHakai);
        } catch (Exception e) { e.printStackTrace();
        } finally {
            if (message != null) {
                message.cleanup();
            }
        }
    }

    public void sendPlayerToCaiBinhChua(Player player, int time) {
        if (player.effectSkill != null) {
            player.effectSkill.isCaiBinhChua = true;
            player.effectSkill.timeCaiBinhChua = time;
            player.effectSkill.lastTimeCaiBinhChua = System.currentTimeMillis();
            Service.gI().Send_Caitrang(player);
        }
    }

    public void sendPlayerToHakai(Player player, int time) {
        if (player.effectSkill != null) {
            player.effectSkill.isHakai = true;
            player.effectSkill.timeHakai = time;
            player.effectSkill.lastTimeHakai = System.currentTimeMillis();
            Service.gI().Send_Caitrang(player);
        }
    }

//    public void sendEffectBienHinh(Player player) {
//        Skill skill = SkillUtil.getSkillbyId(player, Skill.BIEN_HINH);
//        Message msg;
//        try {
//            msg = new Message(-45);
//            msg.writer().writeByte(5);
//            msg.writer().writeInt((int) player.id);
//            msg.writer().writeShort(skill.skillId);
//            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//            nro.utils.Logger.logException(EffectSkillService.class, e);
//        }
//    }
    public void setBienHinh(Player player) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
 e.printStackTrace();
        }

        Skill template = Manager.NCLASS.get(player.gender)
                .getSkillTemplate(player.playerSkill.skillSelect.template.id).skillss.stream().filter(s -> s.point == player.playerSkill.skillSelect.point)
                .findFirst().orElse(null);

        if (template == null) {
            return;
        }

        int skillLevel = 7;
        boolean lastLevel = player.effectSkill.levelBienHinh >= skillLevel - 1;

        player.effectSkill.isBienHinh = true;
        player.effectSkill.levelBienHinh = Math.min(skillLevel, player.effectSkill.levelBienHinh + 1);
        player.effectSkill.timeBienHinh = SkillUtil.getTimeBienHinh(lastLevel, template.coolDown);
        player.effectSkill.lastTimeBienHinh = System.currentTimeMillis();

        if (!lastLevel && template.coolDown > 0) {
            player.playerSkill.skillSelect.coolDown = template.coolDown * 40 / 100;
        } else {
            player.playerSkill.skillSelect.coolDown = template.coolDown;
        }
    }

    public void downBienHinh(Player player) {
        player.effectSkill.isBienHinh = false;
        player.effectSkill.levelBienHinh = 0;

        sendEffectMonkey(player);
        RadarService.gI().RadarSetAura(player);

        Service.gI().Send_Caitrang(player);
        Service.gI().point(player);
    }
}
