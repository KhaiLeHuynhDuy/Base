package nro.models.player;

import nro.models.mob.Mob;
import nro.network.io.Message;
import nro.services.EffectSkillService;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.utils.Util;

public class EffectSkill {

    private Player player;
    //Cải trang Dracula Frost
    public boolean isBang;
    public long lastTimeHoaBang;
    public int timeBang;

    //Cải trang Dracula hóa đá
    public boolean isDa;
    public long lastTimeHoaDa;
    public int timeDa;
    public boolean isBinh;
    public long lastTimeHoaBinh;
    public int timeBinh;
    //Cải trang Thỏ Đại Ca
    public boolean isCarot;
    public long lastTimeHoaCarot;
    public int timeCarot;

    public boolean isSoHai;
    public long lastTimeSoHai;
    public int timeSoHai;
    //thái dương hạ san
    public boolean isStun;
    public long lastTimeStartStun;
    public int timeStun;

    //khiên năng lượng
    public boolean isShielding;
    public long lastTimeShieldUp;
    public int timeShield;

    //biến khỉ
    public boolean isMonkey;
    public byte levelMonkey;
    public long lastTimeUpMonkey;
    public int timeMonkey;

    //biến hình
//     public boolean isBienHinh;
//    public byte levelBienHinh;
//    public long lastTimeBienHinh;
//    public int timeBienHinh;
    //tái tạo năng lượng
    public boolean isCharging;
    public int countCharging;

    //huýt sáo
    public int tiLeHPHuytSao;
    public long lastTimeHuytSao;

    //thôi miên
    public boolean isThoiMien;
    public long lastTimeThoiMien;
    public int timeThoiMien;

    //trói
    public boolean useTroi;
    public boolean anTroi;
    public long lastTimeTroi;
    public long lastTimeAnTroi;
    public int timeTroi;
    public int timeAnTroi;
    public Player plTroi;
    public Player plAnTroi;
    public Mob mobAnTroi;

    //dịch chuyển tức thời
    public boolean isBlindDCTT;
    public long lastTimeBlindDCTT;
    public int timeBlindDCTT;

    //socola
    public boolean isSocola;
    public long lastTimeSocola;
    public int timeSocola;
    public int countPem1hp;

    // biến thành cái bình
    public boolean isCaiBinhChua;
    public long lastTimeCaiBinhChua;
    public int timeCaiBinhChua;

    // biến thành cái bình
    public boolean isHakai;
    public long lastTimeHakai;
    public int timeHakai;

    // Bien hinh
    public boolean isBienHinh;
    public long lastTimeBienHinh;
    public int timeBienHinh;
    public int levelBienHinh = 0;
    
    public EffectSkill(Player player) {
        this.player = player;
    }

    public void removeSkillEffectWhenDie() {
        if (isMonkey) {
            EffectSkillService.gI().monkeyDown(player);
        }
//        if (isBienHinh) {
//            EffectSkillService.gI().BienHinhDown(player);
//        }
        if (isShielding) {
            EffectSkillService.gI().removeShield(player);
            ItemTimeService.gI().removeItemTime(player, 3784);
        }
        if (useTroi) {
            EffectSkillService.gI().removeUseTroi(this.player);
            ItemTimeService.gI().removeItemTime(player, 3779);
        }
        if (isStun) {
            EffectSkillService.gI().removeStun(this.player);
        }
        if (isThoiMien) {
            EffectSkillService.gI().removeThoiMien(this.player);
        }
        if (isBlindDCTT) {
            EffectSkillService.gI().removeBlindDCTT(this.player);
        }
        if (isBang) {
            EffectSkillService.gI().removeBang(this.player);
        }
        if (isCaiBinhChua) {
            EffectSkillService.gI().removeMaPhongBa(this.player);
        }
        if (isSoHai) {
            EffectSkillService.gI().removeSoHai(this.player);
        }
        if (isHakai) {
            EffectSkillService.gI().removeHakai(this.player);
        }
    }

    public void update() {
        if (isBienHinh && (Util.canDoWithTime(lastTimeBienHinh, timeBienHinh))) {
            EffectSkillService.gI().downBienHinh(player);
        }
        if (isMonkey && (Util.canDoWithTime(lastTimeUpMonkey, timeMonkey))) {
            EffectSkillService.gI().monkeyDown(player);
        }
//         if (isBienHinh && (Util.canDoWithTime(lastTimeBienHinh, timeBienHinh))) {
//            EffectSkillService.gI().BienHinhDown(player);
//        }
        if (isShielding && (Util.canDoWithTime(lastTimeShieldUp, timeShield))) {
            EffectSkillService.gI().removeShield(player);
        }
        if (useTroi && Util.canDoWithTime(lastTimeTroi, timeTroi)
                || plAnTroi != null && plAnTroi.isDie()
                || useTroi && isHaveEffectSkill()) {
            EffectSkillService.gI().removeUseTroi(this.player);
        }
        if (anTroi && (Util.canDoWithTime(lastTimeAnTroi, timeAnTroi) || player.isDie())) {
            EffectSkillService.gI().removeAnTroi(this.player);
        }
        if (isStun && Util.canDoWithTime(lastTimeStartStun, timeStun)) {
            EffectSkillService.gI().removeStun(this.player);
        }
        if (isThoiMien && (Util.canDoWithTime(lastTimeThoiMien, timeThoiMien))) {
            EffectSkillService.gI().removeThoiMien(this.player);
        }
        if (isBlindDCTT && (Util.canDoWithTime(lastTimeBlindDCTT, timeBlindDCTT))) {
            EffectSkillService.gI().removeBlindDCTT(this.player);
        }
        if (isSocola && (Util.canDoWithTime(lastTimeSocola, timeSocola))) {
            EffectSkillService.gI().removeSocola(this.player);
        }
        if (isSoHai && (Util.canDoWithTime(lastTimeSoHai, timeSoHai))) {
            EffectSkillService.gI().removeSoHai(this.player);
        }
        if (isBang && (Util.canDoWithTime(lastTimeHoaBang, 5000))) {
            EffectSkillService.gI().removeBang(this.player);
        }
        if (isDa && (Util.canDoWithTime(lastTimeHoaDa, 5000))) {
            EffectSkillService.gI().removeDa(this.player);
        }
        if (isCarot && (Util.canDoWithTime(lastTimeHoaCarot, 30000))) {
            EffectSkillService.gI().removeCarot(this.player);
        }
        if (tiLeHPHuytSao != 0 && Util.canDoWithTime(lastTimeHuytSao, 30000)) {
            EffectSkillService.gI().removeHuytSao(this.player);
        }
        if (isCaiBinhChua && (Util.canDoWithTime(this.lastTimeCaiBinhChua, this.timeCaiBinhChua) || this.player.isDie())) {
//            isCaiBinhChua = false;
            Service.gI().Send_Caitrang(this.player);
        }
        if (isHakai && (Util.canDoWithTime(this.lastTimeHakai, this.timeHakai) || this.player.isDie())) {
//            isHakai = false;
//            Service.gI().Send_Caitrang(this.player);
            EffectSkillService.gI().removeHakai(this.player);
        }
    }

    public boolean isHaveEffectSkill() {
        return isStun || isBlindDCTT || anTroi || isThoiMien || isBang || isCaiBinhChua || isHakai;
    }

    

    public void dispose() {
        this.player = null;
    }
}
