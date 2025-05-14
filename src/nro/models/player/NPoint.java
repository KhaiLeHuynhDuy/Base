package nro.models.player;

//import com.arriety.card.Card;
import nro.models.card.OptionCard;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.consts.cn;
import nro.models.card.Card;
import nro.models.intrinsic.Intrinsic;
import nro.models.item.Item;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

public class NPoint {

    public static final byte MAX_LIMIT = 12;
    private Player player;
    private PointFusion ps;

    public NPoint(Player player) {

        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
    }

    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;

    private int percentDameIntrinsic;
    public int dameAfter;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;

    public long hp, hpMax, hpg;
    public long mp, mpMax, mpg;
    public long dame, dameg;
    public int def, defg;
    public int crit, critg, overflowcrit;
    public byte speed = 5;

    public boolean teleport;
    public boolean isSoHai;
    public boolean isDraburaFrost; //Cải trang Dracula Frost
    public boolean isDrabura; //Cải trang Dracula Frost
    public boolean isThoDaiCa; //Cải trang Dracula Frost
    public boolean khangTDHS;

    /**
     * Chỉ số cộng thêm
     */
    public long hpAdd, mpAdd, dameAdd, defAdd;
    public int critAdd, hpHoiAdd, mpHoiAdd;
    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public long hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlHutHp, tlHutMp, tlHutHpMob, tlHutMpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;

    public int voHieuChuong;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;

    public short tlHpGiamODo;
    public short test;
    public short tlHpFideBlack;

    public short multicationChuong;
    public long lastTimeMultiChuong;

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        if (this.player.pet != null) {
            this.player.pet.nPoint.setPointWhenWearClothes();
        }
        this.setPointWhenWearClothes();
    }

    public static long calPercent(long param, int percent) {
        return param * percent / 150;
    }

    private void setPointWhenWearClothes() {
        resetPoint();
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            dame += RewardBlackBall.R2S_1;

        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            hp += RewardBlackBall.R2S_1;
            mp += RewardBlackBall.R2S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            tlDameAttMob.add(RewardBlackBall.R3S_2);
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            tlPST += RewardBlackBall.R4S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {

            tlNeDon += RewardBlackBall.R5S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            tlHpHoi += RewardBlackBall.R6S_2;
            tlHutHp += RewardBlackBall.R6S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {
            tlMpHoi += RewardBlackBall.R7S_2;
            tlHutMp += RewardBlackBall.R7S_2;
        }
        for (Card card : player.Cards) {
            if (card != null && card.Used == 1) {
                for (OptionCard io : card.Options) {
                    if (io.active == card.Level || (card.Level == -1 && io.active == 0)) {
                        switch (io.id) {
                            case 0: // Tấn công +#
                                this.dameAdd += io.param;
                                break;
                            case 2: // HP, KI+#000
                                this.hpAdd += io.param * 1000d;
                                this.mpAdd += io.param * 1000d;
                                break;
                            case 3:// fake
                                this.voHieuChuong += io.param;
                                break;
                            case 5: // +#% sức đánh chí mạng
                                this.tlDameCrit.add(io.param);
                                break;
                            case 6: // HP+#
                                this.hpAdd += io.param;
                                break;
                            case 7: // KI+#
                                this.mpAdd += io.param;
                                break;
                            case 8: // Hút #% HP, KI xung quanh mỗi 5 giây
                                this.tlHutHpMpXQ += io.param;
                                break;
                            case 14: // Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 19: // Tấn công+#% khi đánh quái
                                this.tlDameAttMob.add(io.param);
                                break;
                            case 22: // HP+#K
                                this.hpAdd += io.param * 1000;
                                break;
                            case 23: // MP+#K
                                this.mpAdd += io.param * 1000;
                                break;
                            case 27: // +# HP/30s
                                this.hpHoiAdd += io.param;
                                break;
                            case 28: // +# KI/30s
                                this.mpHoiAdd += io.param;
                                break;
                            case 47: // Giáp+#
                                this.defAdd += io.param;
                                break;

                            case 48: // HP/KI+#
                                this.hpAdd += io.param;
                                this.mpAdd += io.param;
                                break;
                            case 49: // Tấn công+#%
                            case 50: // Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: // HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: // HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: // MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 88: // Cộng #% exp khi đánh quái
                                this.tlTNSM.add(io.param);
                                break;
                            case 94: // Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 95: // Biến #% tấn công thành HP
                                this.tlHutHp += io.param;
                                break;
                            case 96: // Biến #% tấn công thành MP
                                this.tlHutMp += io.param;
                                break;
                            case 97: // Phản #% sát thương
                                this.tlPST += io.param;
                                break;
                            case 100: // +#% vàng từ quái
                                this.tlGold += io.param;
                                break;
                            case 101: // +#% TN,SM
                                this.tlTNSM.add(io.param);
                                break;
                            case 103: // KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 104: // Biến #% tấn công quái thành HP
                                this.tlHutHpMob += io.param;
                                break;
                            case 147: // +#% sức đánh
                                this.tlDame.add(io.param);
                                break;

                        }
                    }
                }
            }
        }

//        if (this.player != null && this.player.setClothes != null) {  
//            this.player.setClothes.worldcup = 0;
//        }
//        0358124452
        for (Item item : this.player.inventory.itemsBody) {
            if (item != null && item.template != null) {
                if (item.template.id >= 592 && item.template.id <= 594) {
                    teleport = true;
                }

                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 232:
                        case 226:
                        case 0: //Tấn công +#
                            this.dameAdd += io.param;
                            break;
                        case 45: // tan cong K
                            this.dameAdd += io.param * 1000;
                            break;
                        case 2: //HP, KI+#000
                            this.hpAdd += io.param * 1000;
                            this.mpAdd += io.param * 1000;
                            break;
                        case 3:// fake
                            this.voHieuChuong += io.param;
                            break;
                        case 224:
                        case 246:
                        case 5: //+#% sức đánh chí mạng
                            this.tlDameCrit.add(io.param);
                            break;
                        case 233:
                        case 6: //HP+#
                        case 227:
                            this.hpAdd += io.param;
                            break;
                        case 234:
                        case 7: //KI+#
                        case 228:
                            this.mpAdd += io.param;
                            break;
                        case 230:
                        case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                            this.tlHutHpMpXQ += io.param;
                            break;
                        case 247:
                        case 14: //Chí mạng+#%
                            this.critAdd += io.param;
                            break;
                        case 19: //Tấn công+#% khi đánh quái
                            this.tlDameAttMob.add(io.param);
                            break;
                        case 22: //HP+#K
                            this.hpAdd += io.param * 1000;
                            break;
                        case 23: //MP+#K
                            this.mpAdd += io.param * 1000;
                            break;
                        case 27: //+# HP/30s
                            this.hpHoiAdd += io.param;
                            break;
                        case 28: //+# KI/30s
                            this.mpHoiAdd += io.param;
                            break;
                        case 33: //dịch chuyển tức thời
                            this.teleport = true;
                            break;
                        case 235:
                        case 47: //Giáp+#
                        case 229:
                            this.defAdd += io.param;
                            break;
                        case 46:
                            this.defAdd += io.param * 1000;
                            break;
//                        case 194:
//                            this.defAdd += io.param * 1000d;
//                            break;
                        case 48: //HP/KI+#
                            this.hpAdd += io.param;
                            this.mpAdd += io.param;
                            break;
                        case 221:
                        case 49: //Tấn công+#%
                        case 50: //Sức đánh+#%
                        case 242:
                            this.tlDame.add(io.param);
                            break;
                        case 222:

                        case 77: //HP+#%
                        case 244:
                            this.tlHp.add(io.param);
                            break;
                        case 80: //HP+#%/30s
                            this.tlHpHoi += io.param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += io.param;
                            break;
                        case 88: //Cộng #% exp khi đánh quái
                            this.tlTNSM.add(io.param);
                            break;
                        case 241:
                        case 94: //Giáp #%
                            this.tlDef.add(io.param);
                            break;
                        case 95: //Biến #% tấn công thành HP
                            this.tlHutHp += io.param;
                            break;
                        case 96: //Biến #% tấn công thành MP
                            this.tlHutMp += io.param;
                            break;
                        case 97: //Phản #% sát thương
                            this.tlPST += io.param;
                            break;
                        case 100: //+#% vàng từ quái
                            this.tlGold += io.param;
                            break;
                        case 101: //+#% TN,SM
                            this.tlTNSM.add(io.param);
                            break;
                        case 223:
                        case 103: //KI +#%
                        case 245:
                            this.tlMp.add(io.param);
                            break;
                        case 104: //Biến #% tấn công quái thành HP
                            this.tlHutHpMob += io.param;
                            break;
                        case 105: //Vô hình khi không đánh quái và boss
                            this.wearingVoHinh = true;
                            break;
                        case 106: //Không ảnh hưởng bởi cái lạnh
                            this.isKhongLanh = true;
                            break;
                        case 243:
                        case 108: //#% Né đòn
                            this.tlNeDon += io.param;// đối nghịch
                            break;
                        case 109: //Hôi, giảm #% HP
                            this.tlHpGiamODo += io.param;
                            break;
                        case 116: //Kháng thái dương hạ san
                            this.khangTDHS = true;
                            break;
                        case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                            this.tlSDDep.add(io.param);
                            break;
                        case 147: //+#% sức đánh
                            this.tlDame.add(io.param);
                            break;
                        case 75: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                            this.tlSubSD += 50;

                            this.tlTNSM.add(io.param);
                            this.tlGold += io.param;
                            break;
                        case 162: //Cute hồi #% KI/s bản thân và xung quanh
                            this.mpHoiCute += io.param;
                            break;
                        case 173: //Phục hồi #% HP và KI cho đồng đội
                            this.tlHpHoiBanThanVaDongDoi += io.param;
                            this.tlMpHoiBanThanVaDongDoi += io.param;
                            break;
                        case 115: //Thỏ Đại Ca
                            this.isThoDaiCa = true;
                            break;
                        case 26: //Dracula hóa đá
                            this.isDrabura = true;
                            break;
                        case 231:
                        case 218: // Dracula Frost
                            this.isDraburaFrost = true;
                            break;
                        case 219: // Blackfide
                            this.isSoHai = true;
                            break;
                        case 196:
                            this.hpAdd += io.param * 100000;
                            break;
                        case 197:
                            this.mpAdd += io.param * 100000;
                            break;
                        case 198:
                            this.dameAdd += io.param * 100000;
                            break;
                        case 199:
                            this.defAdd += io.param * 100000;
                            break;
                    }
                }
            }
        }
        for (Item item : this.player.inventory.itemsBag) {

            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//            for (Item item : this.player.inventory.itemsBag) {  
                if (item.isNotNullItem() && item.template.id == 921) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233:
                                this.hpAdd += io.param;
                                break;
                            case 234:
                                this.mpAdd += io.param;
                                break;
                            case 235:
                                this.defAdd += io.param;
                                break;
                            case 5: //+#% sức đánh chí mạng
                                this.tlDameCrit.add(io.param);
                                break;
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;

                        }
                    }
                    break;
                }
            }
//        }

            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//            for (Item item : this.player.inventory.itemsBag) {  
                if (item.isNotNullItem() && item.template.id == 2064) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 5:
                                this.tlDameCrit.add(io.param);
                                break;
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233:
                                this.hpAdd += io.param;
                                break;
                            case 234:
                                this.mpAdd += io.param;
                                break;
                            case 235:
                                this.defAdd += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
//        }
            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//            for (Item item : this.player.inventory.itemsBag) {  
                if (item.isNotNullItem() && item.template.id == 2113) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 5:
                                this.tlDameCrit.add(io.param);
                                break;
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233:
                                this.hpAdd += io.param;
                                break;
                            case 234:
                                this.mpAdd += io.param;
                                break;
                            case 235:
                                this.defAdd += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
//        }
            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
//            for (Item item : this.player.inventory.itemsBag) {  
                if (item.isNotNullItem() && item.template.id == 1324) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 5:
                                this.tlDameCrit.add(io.param);
                                break;
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233:
                                this.hpAdd += io.param;
                                break;
                            case 234:
                                this.mpAdd += io.param;
                                break;
                            case 235:
                                this.defAdd += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {
//            for (Item item : this.player.inventory.itemsBag) {  
                if (item.isNotNullItem() && item.template.id == 1472) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 5:
                                this.tlDameCrit.add(io.param);
                                break;
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233:
                                this.hpAdd += io.param;
                                break;
                            case 234:
                                this.mpAdd += io.param;
                                break;
                            case 235:
                                this.defAdd += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (player.pet != null && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {

            if (player.getHead() == 1408) {
                tlPST += 50;
                tlHutHpMpXQ += 5;
                critAdd += 20;
                tlDame.add(20);
                tlHp.add(20);
                tlMp.add(20);
            }
        }
        if (player.pet != null && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {

            if (player.getHead() == 1414) {
                tlPST += 80;
                tlHutHpMpXQ += 10;
                critAdd += 30;
                tlDame.add(30);
                tlHp.add(30);
                tlMp.add(30);
            }
        }
        if (player.pet != null && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {

            if (player.getHead() == 1503 || player.getHead() == 1562) {
                tlPST += 100;
                tlHutHpMpXQ += 15;
                critAdd += 45;
                switch (player.gender) {
                    case 0:
                        tlDame.add(45);
                        dameAdd += calPercent(this.dame, 1 / 10);
                        tlHp.add(45);
                        tlMp.add(45);
                        break;
                    case 1:
                        tlDame.add(45);
                        tlHp.add(45);
                        tlMp.add(45);
                        mpAdd += calPercent(this.mp, 1);
                        break;
                    case 2:
                        tlDame.add(45);
                        tlHp.add(45);
                        hpAdd += calPercent(this.hp, 1);
                        tlMp.add(45);
                        break;
                    default:
                        tlDame.add(1);
                        tlHp.add(1);
                        tlMp.add(1);
                }
                isDraburaFrost = true;
            }

        }

        setDameTrainArmor();
        setBasePoint();
    }

    private void setDameTrainArmor() {
        if (!this.player.isPet && !this.player.isBoss) {
            if (this.player.inventory.itemsBody.size() < 7) {
                return;
            }
            try {
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem() && gtl.template.type != 35) {
                    this.wearingTrainArmor = true;
                    this.wornTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;

                    // Nếu có option 50, công dồn vào tlDame
                    for (Item.ItemOption io : gtl.itemOptions) {
                        if (io.optionTemplate.id == 50) {
                            this.tlSubSD += io.param;
                        }
                    }
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {
                    if (this.wornTrainArmor) {
                        this.wearingTrainArmor = false;

                        if (this.player.inventory.trainArmor.itemOptions != null) {
                            for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                                if (io.optionTemplate.id == 9 && io.param > 0) {
//                                    for (Item.ItemOption io2 : this.player.inventory.trainArmor.itemOptions) {
//                                        if (io.optionTemplate.id == 50) {
//                                            this.tlDame.add(io2.param);
//                                        }
//                                    }
                                    this.tlDame.add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                                    break;
                                }

                            }
                            // Tăng thêm nếu giáp có option id 50

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n");
            }
        }
    }

    public void setBasePoint() {
        setHpMax();
        setHp();
        setMpMax();
        setMp();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
        setPotara2();
        setPotara3();
        setPotara4();
        setPotara5();
        setPotara6();
    }

    private void setNeDon() {
        if (this.tlNeDon >= 100) {
            this.tlNeDon = 90;
        }
        if (player.setClothes.setVoCucTuTai == 6) {
            this.tlNeDon += 20;
        }
    }

    public void setPotara2() {
        if (!this.player.isPet && !this.player.isBoss) {
            Item btc2 = InventoryServiceNew.gI().findItemBag(this.player, 921);
            if (btc2 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                if (!btc2.itemOptions.isEmpty()) {
                    int param = btc2.itemOptions.get(0).param;
                    int option_id = btc2.itemOptions.get(0).optionTemplate.id;
                    switch (option_id) {
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);
                            break;
                        case 14:
                            this.crit += param;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;

                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;

                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233:
                            this.hpAdd += param;
                            break;
                        case 234:
                            this.mpAdd += param;
                            break;
                        case 235:
                            this.defAdd += param;
                            break;

                    }
                }
            }
        }

    }

    public void setPotara3() {
        if (!this.player.isPet && !this.player.isBoss) {
            Item btc3 = InventoryServiceNew.gI().findItemBag(this.player, 2064);

            if (btc3 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                if (!btc3.itemOptions.isEmpty()) {
                    int param = btc3.itemOptions.get(0).param;
                    int option_id = btc3.itemOptions.get(0).optionTemplate.id;
                    // System.out.println("Name " + player.name + "option id" + option_id + "Param " + param);
                    switch (option_id) {
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);

                            break;
                        case 14:

                            this.crit += param;
                            break;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;

                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;

                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233:
                            this.hpAdd += param;
                            break;
                        case 234:
                            this.mpAdd += param;
                            break;
                        case 235:
                            this.defAdd += param;
                            break;

                        default:
                            break;
                    }
                }
            }
        }

    }

    public void setPotara4() {
        if (!this.player.isPet && !this.player.isBoss) {
            Item btc4 = InventoryServiceNew.gI().findItemBag(this.player, 2113);

            if (btc4 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                if (!btc4.itemOptions.isEmpty()) {
                    int param = btc4.itemOptions.get(0).param;
                    int option_id = btc4.itemOptions.get(0).optionTemplate.id;
                    //    System.out.println("Name " + player.name + "option id" + option_id + "Param " + param);
                    switch (option_id) {
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);
                            break;
                        case 14:

                            this.crit += param;
                            break;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;

                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;

                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233:
                            this.hpAdd += param;
                            break;
                        case 234:
                            this.mpAdd += param;
                            break;
                        case 235:
                            this.defAdd += param;
                            break;

                        default:
                            break;
                    }
                }
            }
        }

    }

    public void setPotara5() {
        if (!this.player.isPet && !this.player.isBoss) {
            Item btc4 = InventoryServiceNew.gI().findItemBag(this.player, 1324);

            if (btc4 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                if (!btc4.itemOptions.isEmpty()) {
                    int param = btc4.itemOptions.get(0).param;
                    int option_id = btc4.itemOptions.get(0).optionTemplate.id;
                    //    System.out.println("Name " + player.name + "option id" + option_id + "Param " + param);
                    switch (option_id) {
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);
                            break;
                        case 14:

                            this.crit += param;
                            break;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;

                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;

                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233:
                            this.hpAdd += param;
                            break;
                        case 234:
                            this.mpAdd += param;
                            break;
                        case 235:
                            this.defAdd += param;
                            break;

                        default:
                            break;
                    }
                }
            }
        }

    }

    public void setPotara6() {
        if (!this.player.isPet && !this.player.isBoss) {
            Item btc4 = InventoryServiceNew.gI().findItemBag(this.player, 1472);

            if (btc4 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {
                if (!btc4.itemOptions.isEmpty()) {
                    int param = btc4.itemOptions.get(0).param;
                    int option_id = btc4.itemOptions.get(0).optionTemplate.id;
                    //    System.out.println("Name " + player.name + "option id" + option_id + "Param " + param);
                    switch (option_id) {
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);
                            break;
                        case 14:

                            this.crit += param;
                            break;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;

                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;

                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233:
                            this.hpAdd += param;
                            break;
                        case 234:
                            this.mpAdd += param;
                            break;
                        case 235:
                            this.defAdd += param;
                            break;

                        default:
                            break;
                    }
                }
            }
        }

    }

    private void setHpHoi() {
        this.hpHoi = Util.maxIntValue(this.hpMax / 100);
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += ((long) this.hpMax * this.tlHpHoi / 100);
        this.hpHoi += ((long) this.hpMax * this.tlHpHoiBanThanVaDongDoi / 100);
    }

    private void setMpHoi() {
        this.mpHoi = Util.maxIntValue(this.mpMax / 100);
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += ((long) this.mpMax * this.tlMpHoi / 100);
        this.mpHoi += ((long) this.mpMax * this.tlMpHoiBanThanVaDongDoi / 100);
    }

    // Assuming this.tlHp is an instance of ArrayList<Integer>
    private void setHpMax() {
        this.hpMax = this.hpg + this.hpAdd;

        for (Integer tl : this.tlHp) {
            if (tl != null) {
                this.hpMax += calPercent(this.hpMax, tl);
            }
        }

        // Set nappa
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.nappa
                == 5) {
            this.hpMax += calPercent(this.hpMax, 80);
        }
        // set thien ma
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setThienMa == 6) {

            this.hpMax += calPercent(this.hpMax, 350);

        }
        //set vo cuc tu tai
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setVoCucTuTai == 6) {

            this.hpMax += calPercent(this.hpMax, 750);

        }
        // Set worldcup
//        if (this.player != null && this.player.setClothes != null && this.player.setClothes.worldcup
//                == 2) {  
//            this.hpMax += calPercent(this.hpMax, 10);
//        }

        // Check if the "ngọc rồng đen" reward with 2 stars is still valid
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            this.hpMax += calPercent(this.hpMax, RewardBlackBall.R2S_1);
        }

        // Check if the "Monkey" skill effect is active and apply the bonus to hpMax
        if (this.player.effectSkill.isMonkey) {
            // Ensure that the player is not a pet or is a pet but not in fusion status
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                this.hpMax += calPercent(this.hpMax, percent);
            }
        }

        if (this.player.isPet// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6)) {
            if (((Pet) this.player).typePet == 1) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet1);
            }
            if (((Pet) this.player).typePet == 2) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet2);
            }
            if (((Pet) this.player).typePet == 3) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet3);
            }
            if ((((Pet) this.player).typePet == 4)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet4);
            }
            if ((((Pet) this.player).typePet == 5)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet5);
            }
            if ((((Pet) this.player).typePet == 6)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet6);
            }
            if ((((Pet) this.player).typePet == 7)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet7);
            }
            if ((((Pet) this.player).typePet == 8)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet8);
            }
            if ((((Pet) this.player).typePet == 9)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet9);
            }
            if (((Pet) this.player).typePet == 10) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet10);
            }
            if ((((Pet) this.player).typePet == 11)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet11);
            }
            if ((((Pet) this.player).typePet == 12)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet12);
            }
            if ((((Pet) this.player).typePet == 13)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet13);
            }
            if ((((Pet) this.player).typePet == 14)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet14);
            }
            if ((((Pet) this.player).typePet == 15)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet15);
            }
            if ((((Pet) this.player).typePet == 16)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet16);
            }
            if ((((Pet) this.player).typePet == 17)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet17);
            }
            if ((((Pet) this.player).typePet == 18)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet18);
            }
            if ((((Pet) this.player).typePet == 19)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet19);
            }
            if ((((Pet) this.player).typePet == 20)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet20);
            }
            if ((((Pet) this.player).typePet == 21)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet21);
            }
            if ((((Pet) this.player).typePet == 22)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet22);
            }
            if ((((Pet) this.player).typePet == 23)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet23);
            }
            if ((((Pet) this.player).typePet == 24)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet24);
            }
            if ((((Pet) this.player).typePet == 25)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet25);
            }
            if ((((Pet) this.player).typePet == 26)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet26);
            }
            if ((((Pet) this.player).typePet == 27)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet27);
            }
            if ((((Pet) this.player).typePet == 28)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet28);
            }
            if ((((Pet) this.player).typePet == 29)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet29);
            }
            if ((((Pet) this.player).typePet == 30)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet30);
            }
            if ((((Pet) this.player).typePet == 31)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet31);
            }
            if ((((Pet) this.player).typePet == 32)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet32);
            }
            if ((((Pet) this.player).typePet == 33)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet33);
            }
        }

        //phù
        if (this.player.zone
                != null && MapService.gI()
                        .isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.hpMax *= this.player.effectSkin.xHPKI;
        }
        //+hp đệ
        if (this.player.pet != null && this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if ((this.player.pet.typePet >= 34)) {
                this.hpMax += calPercent(this.player.pet.nPoint.hpMax, this.player.getPointfusion().getHpFusion());
            }
            this.hpMax += this.player.pet.nPoint.hpMax;
        }
        //huýt sáo
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill != null && this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hpMax += calPercent(this.hpMax, player.effectSkill.tiLeHPHuytSao);

            }
        }

        //bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
            this.hpMax += calPercent(this.hpMax, 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseSupBi) {
            this.hpMax += calPercent(this.hpMax, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyetSC) {
            this.hpMax += calPercent(this.hpMax, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {
            this.hpMax += calPercent(this.hpMax, 15);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {
            this.hpMax += calPercent(this.hpMax, 15);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhNgot) {
            this.hpMax += calPercent(this.hpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoTrongGoi) {
            this.hpMax += calPercent(this.hpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseVooc) {
            this.hpMax += calPercent(this.hpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseGaQuay) {
            this.hpMax += calPercent(this.hpMax, 50);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {
            this.hpMax += calPercent(this.hpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseVooc) {
            this.hpMax += calPercent(this.hpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx2) {
            this.hpMax -= calPercent(this.hpMax, 50);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx5) {
            this.hpMax -= calPercent(this.hpMax, 55);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx7) {
            this.hpMax -= calPercent(this.hpMax, 60);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx10) {
            this.hpMax -= calPercent(this.hpMax, 65);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx15) {
            this.hpMax -= calPercent(this.hpMax, 70);
        }
        if (this.player.zone
                != null && MapService.gI()
                        .isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.hpMax -= calPercent(this.hpMax, 50);
        }

        //mèo mun
        if (player.capCS
                > 0) {
            if (player.capCS <= 10) {
                hpMax += (10000) * player.capCS;
            }
            if (player.capCS <= 20 && player.capCS > 10) {
                hpMax += (15000) * (player.capCS);
            }
            if (player.capCS > 20) {
                hpMax += (17000) * (player.capCS);
            }
        }

        if (player.dotpha == 2) {

        }
        //mèo mun
        if (this.player.effectFlagBag.useMeoMun) {
            this.hpMax += calPercent(this.hpMax, 15);
        }
        if (this.player.lastTimeTitle1 > 0 && player.isTitleUse) {
            this.hpMax += calPercent(this.hpMax, 10);
        }
        if (this.player.lastTimeTitle2 > 0 && player.isTitleUse2) {
            this.hpMax += calPercent(this.hpMax, 10);
        }
        if (this.player.lastTimeTitle3 > 0 && player.isTitleUse3) {
            this.hpMax += calPercent(this.hpMax, 20);
        }

    }
    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)

    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    private void setMpMax() {

        this.mpMax = this.mpg + this.mpAdd;

        for (Integer tl : this.tlMp) {
            if (tl != null) {
                this.mpMax += calPercent(this.mpMax, tl);
            }
        }

        // Set picolo
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.picolo == 5) {
            this.mpMax += calPercent(this.mpMax, 100);
        }
        // set thiên ma
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setThienMa == 6) {
            this.mpMax += calPercent(this.mpMax, 350);
        }
        // set vô cực tự tại
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setVoCucTuTai == 6) {
            this.mpMax += calPercent(this.mpMax, 750);
        }
        //set tinh ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.tinhan == 5) {
            this.mpMax += calPercent(this.mpMax, 30);
        }
        // Check if the "ngọc rồng đen" reward with 2 stars is still valid
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {

            this.mpMax += calPercent(this.mpMax, RewardBlackBall.R2S_1);
        }
//        if (this.player.zone
//                != null && MapService.gI()
//                        .isMapCold(this.player.zone.map)
//                && !this.isKhongLanh) {  
//            this.mpMax -= calPercent(this.mpMax, 50);
//        }
        // Set worldcup
//        if (this.player != null && this.player.setClothes != null && this.player.setClothes.worldcup == 2) {  
//            this.mpMax += calPercent(this.mpMax, 10);
//        }
        if (this.player.isPet// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6)) {
            if (((Pet) this.player).typePet == 1) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet1);
            }
            if (((Pet) this.player).typePet == 2) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet2);
            }
            if (((Pet) this.player).typePet == 3) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet3);
            }
            if ((((Pet) this.player).typePet == 4)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet4);
            }
            if ((((Pet) this.player).typePet == 5)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet5);
            }
            if ((((Pet) this.player).typePet == 6)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet6);
            }
            if ((((Pet) this.player).typePet == 7)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet7);
            }
            if ((((Pet) this.player).typePet == 8)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet8);
            }
            if ((((Pet) this.player).typePet == 9)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet9);
            }
            if (((Pet) this.player).typePet == 10) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet10);
            }
            if ((((Pet) this.player).typePet == 11)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet11);
            }
            if ((((Pet) this.player).typePet == 12)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet12);
            }
            if ((((Pet) this.player).typePet == 13)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet13);
            }
            if ((((Pet) this.player).typePet == 14)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet14);
            }
            if ((((Pet) this.player).typePet == 15)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet15);
            }
            if ((((Pet) this.player).typePet == 16)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet16);
            }
            if ((((Pet) this.player).typePet == 17)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet17);
            }
            if ((((Pet) this.player).typePet == 18)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet18);
            }
            if ((((Pet) this.player).typePet == 19)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet19);
            }
            if ((((Pet) this.player).typePet == 20)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet20);
            }
            if ((((Pet) this.player).typePet == 21)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet21);
            }
            if ((((Pet) this.player).typePet == 22)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet22);
            }
            if ((((Pet) this.player).typePet == 23)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet23);
            }
            if ((((Pet) this.player).typePet == 24)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet24);
            }
            if ((((Pet) this.player).typePet == 25)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet25);
            }
            if ((((Pet) this.player).typePet == 26)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet26);
            }
            if ((((Pet) this.player).typePet == 27)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet27);
            }
            if ((((Pet) this.player).typePet == 28)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet28);
            }
            if ((((Pet) this.player).typePet == 29)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet29);
            }
            if ((((Pet) this.player).typePet == 30)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet30);
            }
            if ((((Pet) this.player).typePet == 31)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet31);
            }
            if ((((Pet) this.player).typePet == 32)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet32);
            }
            if ((((Pet) this.player).typePet == 33)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet33);
            }
        }
        //hợp thể

        if (this.player.pet != null && this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if ((this.player.pet.typePet >= 34)) {
                this.mpMax += calPercent(this.player.pet.nPoint.mpMax, this.player.getPointfusion().getMpFusion());
            }
            this.mpMax += this.player.pet.nPoint.mpMax;
        }
        //bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {
            this.mpMax += calPercent(this.mpMax, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
            this.mpMax += calPercent(this.mpMax, 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhiSC) {
            this.mpMax += calPercent(this.mpMax, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoMotMat) {
            this.mpMax += calPercent(this.mpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {
            this.mpMax += calPercent(this.mpMax, 30);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoDeo) {
            this.mpMax += calPercent(this.mpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoTrongGoi) {
            this.mpMax += calPercent(this.mpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseVooc) {
            this.mpMax += calPercent(this.mpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseGaQuay) {
            this.mpMax += calPercent(this.mpMax, 80);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {
            this.mpMax += calPercent(this.mpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx2) {
            this.mpMax -= calPercent(this.mpMax, 50);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx5) {
            this.mpMax -= calPercent(this.mpMax, 55);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx7) {
            this.mpMax -= calPercent(this.mpMax, 60);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx10) {
            this.mpMax -= calPercent(this.mpMax, 65);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx15) {
            this.mpMax -= calPercent(this.mpMax, 70);
        }

        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.mpMax *= this.player.effectSkin.xHPKI;
        }
        //xiên cá
        if (player.capCS > 0) {
            if (player.capCS <= 10) {
                mpMax += (13000) * player.capCS;
            }
            if (player.capCS <= 20 && player.capCS > 10) {
                mpMax += (15000) * (player.capCS);
            }
            if (player.capCS > 20) {
                mpMax += (17500) * (player.capCS);
            }
        }
        //set nhật ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.nhatan == 5) {
            this.mpMax += calPercent(this.mpMax, 30);
        }
        //xiên cá
        if (this.player.effectFlagBag.useXienCa) {
            this.mpMax += calPercent(this.mpMax, 15);
        }
        if (this.player.lastTimeTitle1 > 0 && player.isTitleUse) {
            this.mpMax += calPercent(this.mpMax, 10);
        }
        if (this.player.lastTimeTitle2 > 0 && player.isTitleUse2) {
            this.mpMax += calPercent(this.mpMax, 10);
        }
        if (this.player.lastTimeTitle3 > 0 && player.isTitleUse3) {
            this.mpMax += calPercent(this.mpMax, 20);
        }
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

//    private final Object lockObject = new Object();
    private void setDame() {
        this.dame = this.dameg;
        this.dame += this.dameAdd;

        for (Integer tl : this.tlDame) {
            if (tl != null) {
                this.dame += calPercent(this.dame, tl);
            }
        }

        for (Integer tl : this.tlSDDep) {
            if (tl != null) {
                this.dame += calPercent(this.dame, tl);
            }
        }
        // set thien ma
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setThienMa == 6) {
            this.dame += calPercent(this.dame, 350);
        }
        // set vo cuc tu tai
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setVoCucTuTai == 6) {
            this.dame += calPercent(this.dame, 750);
        }
        if (this.player.isPet// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6)) {
            if (((Pet) this.player).typePet == 1) {
                this.dame += calPercent(this.dame, cn.damePet1);
            }
            if (((Pet) this.player).typePet == 2) {
                this.dame += calPercent(this.dame, cn.damePet2);
            }
            if (((Pet) this.player).typePet == 3) {
                this.dame += calPercent(this.dame, cn.damePet3);
            }
            if ((((Pet) this.player).typePet == 4)) {
                this.dame += calPercent(this.dame, cn.damePet4);
            }
            if ((((Pet) this.player).typePet == 5)) {
                this.dame += calPercent(this.dame, cn.damePet5);
            }
            if ((((Pet) this.player).typePet == 6)) {
                this.dame += calPercent(this.dame, cn.damePet6);
            }
            if ((((Pet) this.player).typePet == 7)) {
                this.dame += calPercent(this.dame, cn.damePet7);
            }
            if ((((Pet) this.player).typePet == 8)) {
                this.dame += calPercent(this.dame, cn.damePet8);
            }
            if ((((Pet) this.player).typePet == 9)) {
                this.dame += calPercent(this.dame, cn.damePet9);
            }
            if (((Pet) this.player).typePet == 10) {
                this.dame += calPercent(this.dame, cn.damePet10);
            }
            if ((((Pet) this.player).typePet == 11)) {
                this.dame += calPercent(this.dame, cn.damePet11);
            }
            if ((((Pet) this.player).typePet == 12)) {
                this.dame += calPercent(this.dame, cn.damePet12);
            }
            if ((((Pet) this.player).typePet == 13)) {
                this.dame += calPercent(this.dame, cn.damePet13);
            }
            if ((((Pet) this.player).typePet == 14)) {
                this.dame += calPercent(this.dame, cn.damePet14);
            }
            if ((((Pet) this.player).typePet == 15)) {
                this.dame += calPercent(this.dame, cn.damePet15);
            }
            if ((((Pet) this.player).typePet == 16)) {
                this.dame += calPercent(this.dame, cn.damePet16);
            }
            if ((((Pet) this.player).typePet == 17)) {
                this.dame += calPercent(this.dame, cn.damePet17);
            }
            if ((((Pet) this.player).typePet == 18)) {
                this.dame += calPercent(this.dame, cn.damePet18);
            }
            if ((((Pet) this.player).typePet == 19)) {
                this.dame += calPercent(this.dame, cn.damePet19);
            }
            if ((((Pet) this.player).typePet == 20)) {
                this.dame += calPercent(this.dame, cn.damePet20);
            }
            if ((((Pet) this.player).typePet == 21)) {
                this.dame += calPercent(this.dame, cn.damePet21);
            }
            if ((((Pet) this.player).typePet == 22)) {
                this.dame += calPercent(this.dame, cn.damePet22);
            }
            if ((((Pet) this.player).typePet == 23)) {
                this.dame += calPercent(this.dame, cn.damePet23);
            }
            if ((((Pet) this.player).typePet == 24)) {
                this.dame += calPercent(this.dame, cn.damePet24);
            }
            if ((((Pet) this.player).typePet == 25)) {
                this.dame += calPercent(this.dame, cn.damePet25);
            }
            if ((((Pet) this.player).typePet == 26)) {
                this.dame += calPercent(this.dame, cn.damePet26);
            }
            if ((((Pet) this.player).typePet == 27)) {
                this.dame += calPercent(this.dame, cn.damePet27);
            }
            if ((((Pet) this.player).typePet == 28)) {
                this.dame += calPercent(this.dame, cn.damePet28);
            }
            if ((((Pet) this.player).typePet == 29)) {
                this.dame += calPercent(this.dame, cn.damePet29);
            }
            if ((((Pet) this.player).typePet == 30)) {
                this.dame += calPercent(this.dame, cn.damePet30);
            }
            if ((((Pet) this.player).typePet == 31)) {
                this.dame += calPercent(this.dame, cn.damePet31);
            }
            if ((((Pet) this.player).typePet == 32)) {
                this.dame += calPercent(this.dame, cn.damePet32);
            }
            if ((((Pet) this.player).typePet == 33)) {
                this.dame += calPercent(this.dame, cn.damePet33);
            }
        }

        if (this.player.pet != null && this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if ((this.player.pet.typePet >= 34)) {
                this.dame += calPercent(this.player.pet.nPoint.dame, this.player.getPointfusion().getDameFusion());
            }
            this.dame += this.player.pet.nPoint.dame;
        }
        //thức ăn
        if (!this.player.isPet && this.player.itemTime != null && this.player.itemTime.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += calPercent(this.dame, 10);
        }
        //cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
            this.dame += calPercent(this.dame, 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNoSC) {
            this.dame += calPercent(this.dame, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhSau) {
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {
            this.dame += calPercent(this.dame, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {
            this.dame += calPercent(this.dame, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKemOcQue) {
            this.dame += calPercent(this.dame, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoTrongGoi) {
            this.dame -= calPercent(this.dame, 90);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseSaoBien) {
            this.dame += calPercent(this.dame, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseThapCap) {
            this.dame += calPercent(this.dame, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx2) {
            this.dame -= calPercent(this.dame, 50);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx5) {
            this.dame -= calPercent(this.dame, 55);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx7) {
            this.dame -= calPercent(this.dame, 60);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx10) {
            this.dame -= calPercent(this.dame, 65);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx15) {
            this.dame -= calPercent(this.dame, 70);
        }

        //giảm dame
        this.dame -= calPercent(this.dame, tlSubSD);
        //map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.dame -= calPercent(this.dame, 60);
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {

            this.dame += calPercent(this.dame, RewardBlackBall.R2S_1);
        }

        //set nguyệt ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.nguyetan == 5) {
            this.dame += calPercent(this.dame, 20);
        }

        //phóng heo
        if (this.player.effectFlagBag.usePhongHeo) {
            this.dame += calPercent(this.dame, 15);
        }
//        if (player.capCS > 0) {
//            if (player.capCS <= 10) {
//                dame += (5000) * player.capCS;
//            }
//            if (player.capCS <= 20 && player.capCS > 10) {
//                dame += (7000) * (player.capCS);
//            }
//            if (player.capCS > 20) {
//                dame += (9000) * (player.capCS);
//            }
//        }
//        if (player.capTT > 0) {
//            if (player.capTT <= 10) {
//                dame += (9000) * player.capTT;
//            }
//            if (player.capTT <= 20 && player.capTT > 10) {
//                dame += (11000) * (player.capTT);
//            }
//            if (player.capTT > 20) {
//                dame += (15000) * (player.capTT);
//            }
//        }
        if (this.player.lastTimeTitle1 > 0 && player.isTitleUse) {
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.lastTimeTitle2 > 0 && player.isTitleUse2) {
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.lastTimeTitle3 > 0 && player.isTitleUse3) {
            this.dame += calPercent(this.dame, 20);
        }
        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
                this.dame += calPercent(this.dame, percent);
            }
        }

    }

    private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        //đồ
        for (Integer tl : this.tlDef) {
            this.def += calPercent(this.def, tl);
        }
//        if (this.player.isPet && (((Pet) this.player).typePet == 33)// chi so lam sao bac tu cho dj
//                ) {  
//            this.def += (this.def * cn.damePet33 / 100);//chi so hp
//        }
        //ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {

            this.def += calPercent(this.def, RewardBlackBall.R2S_2);
        }
    }

//    private void setCrit() {  
//        this.crit = this.critg;
//        this.crit += this.critAdd;
//        //ngọc rồng đen 3 sao
//        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {  
//            this.crit += RewardBlackBall.R3S_2;
//        }
//        //biến khỉ
//        if (this.player.effectSkill.isMonkey) {  
//            this.crit = 100;
//        }
//        if (this.crit > 100) {
//          
//            this.crit = 100;
//        }
//
//    }
    private void setCrit() {
        this.crit = this.critg + this.critAdd;
        this.overflowcrit = crit;
        // Ngọc Rồng Đen 3 Sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.crit += RewardBlackBall.R3S_2;
        }

        // Biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 100;
        }

        // Xử lý phần chí mạng dư
        if (this.crit > 100) {
            int critOverflow = this.crit - 100;  // Phần dư của chí mạng
            tlDameCrit.add(critOverflow / 5);   // Chuyển phần dư thành tlDameCrit
            this.crit = 100;                    // Giới hạn chí mạng về 100
        }
    }

    private void resetPoint() {
        this.voHieuChuong = 0;
        this.hpAdd = 0;
        this.isThoDaiCa = false; //Cải trang Thỏ Đại Ca
        this.isDrabura = false; //Cải trang Dracula
        this.isDraburaFrost = false; //Cải trang Dracula Frost
        this.isSoHai = false; //Cải trang Dracula Frost
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameCrit.clear();
        this.tlDameAttMob.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlSDDep.clear();
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.test = 0;
        this.teleport = false;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.khangTDHS = false;
    }

    public void addHp(long hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void addMp(long mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void setHp(long hp) {
        if (hp > this.hpMax) {
            this.hp = this.hpMax;
        } else {
            this.hp = hp;
        }
    }

    public void setMp(long mp) {
        if (mp > this.mpMax) {
            this.mp = this.mpMax;
        } else {
            this.mp = mp;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public double getDameAttack(boolean isAttackMob) {
        setIsCrit();
        double dameAttack = this.dame;
        double dameMP = this.mpMax;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        long percentDameSkill = 0;
        long percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.setTanThu == 6) {
                    percentXDame = 100;
                }
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                    percentXDame = 100;
                }
                if (this.player.isPet && (((Pet) this.player).typePet == 33)) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.setTanThu == 6) {
                    percentXDame = 100;
                }
                break;
            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.setTanThu == 6) {
                    percentXDame = 100;
                }
                break;
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.isPet && (((Pet) this.player).typePet == 33)) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.setTanThu == 6) {
                    percentXDame = 100;
                }
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.setTanThu == 6) {
                    percentXDame = 100;
                }
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.masenko == 5) {
                    percentXDame = 100;
                }
                if (this.player.isPet && (((Pet) this.player).typePet == 33)) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.setTanThu == 6) {
                    percentXDame = 100;
                }
                break;
            case Skill.KAIOKEN:
                if (intrinsic.id == 26) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kaioken == 5) {
                    percentXDame = 25;
                }
                if (this.player.setClothes.setTanThu == 6) {
                    percentXDame = 100;
                }
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                    percentXDame = 80;
                }
                if (this.player.setClothes.setTanThu == 6) {
                    percentXDame = 100;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 2;
                dameAttack = Util.nextInt((int) (dameAttack - (dameAttack * 5 / 100)),
                        (int) (dameAttack + (dameAttack * 5 / 100)));
                return (int) dameAttack;
            case Skill.MAKANKOSAPPO:

                percentDameSkill = skillSelect.damage;
                long dameSkill = (int) (dameMP * percentDameSkill / 100);
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long damekk = calPercent(this.dame, 2000);
                if (this.player.setClothes.kirin == 5) {
                    damekk *= 2;
                }
//                dame = dame + (Util.nextInt(-5, 5) * dame / 100);
                damekk = damekk + (5 * damekk / 100);
                return damekk;
        }
        if (intrinsic.id == 18
                && this.player.effectSkill.isMonkey //                && this.player.effectSkill.isBienHinh
                ) {
            percentDameIntrinsic = intrinsic.param1;
        }

        if (percentDameSkill != 0) {
            dameAttack = dameAttack * percentDameSkill / 100;
        }
        dameAttack += (dameAttack * percentDameIntrinsic / 100);
        dameAttack += (dameAttack * dameAfter / 100);

        if (isAttackMob) {
            for (Integer tl : this.tlDameAttMob) {
                dameAttack += (dameAttack * tl / 100);
            }
        }
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += (dameAttack * tl / 100);
            }
        }
        dameAttack += ((long) dameAttack * percentXDame / 100);
        dameAttack = Util.Tamkjllnext((dameAttack - (dameAttack * 5 / 100)), (dameAttack + (dameAttack * 5 / 100)));
        if (player.isPl()) {
            if (player.inventory.haveOption(player.inventory.itemsBody, 5, 159)) {
                if (Util.canDoWithTime(player.lastTimeUseOption, 60000) && (player.playerSkill.skillSelect.skillId == Skill.KAMEJOKO || player.playerSkill.skillSelect.skillId == Skill.ANTOMIC || player.playerSkill.skillSelect.skillId == Skill.MASENKO)) {
                    dameAttack *= player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
                    player.lastTimeUseOption = System.currentTimeMillis();
                }
            }
        }

        //check activation set
        return dameAttack;
    }

    public int getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100;
        }
        return (int) ((long) this.hp * 100 / this.hpMax);
    }

    public int getCurrPercentMP() {
        return (int) ((long) this.mp * 100 / this.mpMax);
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;
    }

    public void setFullHp() {
        this.hp = this.hpMax;
    }

    public void setFullMp() {
        this.mp = this.mpMax;
    }

    public void setFullStamina() {
        this.stamina = this.maxStamina;
    }

    public void setF20Stamina() {
        this.stamina += (short) (this.maxStamina * 20 / 100);
    }

    public void subHP(double sub) {
        this.hp -= sub;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subMP(double sub) {
        this.mp -= sub;
        if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public long calSucManhTiemNang(long tiemNang) {
        if (power < getPowerLimit()) {
            for (Integer tl : this.tlTNSM) {
                tiemNang += ((long) tiemNang * tl / 100);
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += ((long) tiemNang * 10 / 100);
                } else {
                    tiemNang += ((long) tiemNang * 5 / 100);
                }
            }
            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (this.player.itemTime != null && this.player.itemTime.isUseVooc) {
                tiemNang += ((long) tn * 20 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx2) {
                tiemNang += ((long) tn * 100 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx5) {
                tiemNang += ((long) tn * 250 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx7) {
                tiemNang += ((long) tn * 350 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx10) {
                tiemNang += ((long) tn * 500 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx15) {
                tiemNang += ((long) tn * 750 / 100);
            }

            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += ((long) tn * this.intrinsic.param1 / 100);
            }

//            if (this.power >= 900000000000L) {
//                tiemNang -= ((long) tn * 999 / 1000000);
//            } else if (this.power >= 800000000000L) {
//                tiemNang -= ((long) tn * 990 / 100000);
//            } else if (this.power >= 700000000000L) {
//                tiemNang -= ((long) tn * 889 / 100000);
//            } else if (this.power >= 600000000000L) {
//                tiemNang -= ((long) tn * 880 / 10000);
//            } else if (this.power >= 250000000000L) {
//                tiemNang -= ((long) tn * 779 / 10000);
//            } else if (this.power >= 200000000000L) {
//                tiemNang -= ((long) tn * 770 / 10000);
//            } else if (this.power >= 150000000000L) {
//                tiemNang -= ((long) tn * 669 / 1000);
//            } else if (this.power >= 60000000000L) {
//                tiemNang -= ((long) tn * 660 / 1000);
//            } else if (this.power >= 40000000000L) {
//                tiemNang -= ((long) tn * 559 / 1000);
//            } else if (this.power >= 20000000000L) {
//                tiemNang -= ((long) tn * 550 / 1000);
//            }
            if (this.player != null && this.player.zone != null) {
                if (MapService.gI().isMapDoanhTrai(player.zone.map.mapId)) {
                    tiemNang = tn * 5;
                }
                if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
                    tiemNang = tn * 4;
                }
                if (MapService.gI().isMapConDuongRanDoc(player.zone.map.mapId)) {
                    tiemNang = tn * 4;
                }
                if (MapService.gI().isNguHanhSon(player.zone.map.mapId)) {
                    tiemNang = tn * 4;
                }
            } else {

                System.err.print("Eror x tnsm");
            }
            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }

            }
            tiemNang *= Manager.RATE_EXP_SERVER;

            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }

        } else {
            tiemNang = 10;
        }
        return tiemNang;
    }

    public long calSubTNSM(long tiemNang) {
//        if (power >= 701_000_000_000L) {
//            tiemNang -= ((long) tiemNang * 999 / 1000);
//        } else if (power >= 501000000000L) {
//            tiemNang -= ((long) tiemNang * 980 / 1000);
//        } else if (power >= 301000000000L) {
//            tiemNang -= ((long) tiemNang * 98 / 100);
//        } else if (power >= 201000000000L) {
//            tiemNang -= ((long) tiemNang * 97 / 100);
//        } else if (power >= 150000000000L) {
//            tiemNang -= ((long) tiemNang * 95 / 100);
//        } else if (power >= 109000000000L) {
//            tiemNang -= ((long) tiemNang * 91 / 100);
//        } else if (power >= 180000000000L) {
//            tiemNang -= ((long) tiemNang * 90 / 100);
//        } else if (power >= 110000000000L) {
//            tiemNang -= ((long) tiemNang * 85 / 100);
//        } else if (power >= 100000000000L) {
//            tiemNang -= ((long) tiemNang * 80 / 100);
//        } else if (power >= 90000000000L) {
//            tiemNang -= ((long) tiemNang * 75 / 100);
//        } else if (power >= 80000000000L) {
//            tiemNang -= ((long) tiemNang * 70 / 100);
//        }

        return tiemNang;
    }

    public short getTileHutHp(boolean isMob) {
        if (isMob) {
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {
            return this.tlHutHp;
        }
    }

    public short getTiLeHutMp() {

        return this.tlHutMp;

    }

    public double subDameInjureWithDeff(double dame) {
        long def = this.def;
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {
        switch (limitPower) {
            case 0:
                return 9_999_999_999L;
            case 1:
                return 19_999_999_999L;
            case 2:
                return 29_999_999_999L;
            case 3:
                return 39_999_999_999L;
            case 4:
                return 49_999_999_999L;
            case 5:
                return 59_999_999_999L;
            case 6:
                return 69_999_999_999L;
            case 7:
                return 79_999_999_999L;
            case 8:
                return 89_999_999_999L;
            case 9:
                return 99_999_999_999L;
            case 10:
                return 109_999_999_999L;
            case 11:
                return 119_999_999_999L;
            case 12:
                return 129_999_999_999L;
            case 13:
                return 139_999_999_999L;
            case 14:
                return 149_999_999_999L;
            case 15:
                return 159_999_999_999L;
            case 16:
                return 169_999_999_999L;
            case 17:
                return 179_999_999_999L;
            case 18:
                return 189_999_999_999L;
            case 19:
                return 199_999_999_999L;
            case 20:
                return 209_999_999_999L;

            default:
                return 0;
        }
    }

    public long getPowerNextLimit() {
        switch (limitPower + 1) {
            case 0:
                return 9_999_999_999L;
            case 1:
                return 19_999_999_999L;
            case 2:
                return 29_999_999_999L;
            case 3:
                return 39_999_999_999L;
            case 4:
                return 49_999_999_999L;
            case 5:
                return 59_999_999_999L;
            case 6:
                return 69_999_999_999L;
            case 7:
                return 79_999_999_999L;
            case 8:
                return 89_999_999_999L;
            case 9:
                return 99_999_999_999L;
            case 10:
                return 109_999_999_999L;
            case 11:
                return 119_999_999_999L;
            case 12:
                return 129_999_999_999L;
            case 13:
                return 139_999_999_999L;
            case 14:
                return 149_999_999_999L;
            case 15:
                return 159_999_999_999L;
            case 16:
                return 169_999_999_999L;
            case 17:
                return 179_999_999_999L;
            case 18:
                return 189_999_999_999L;
            case 19:
                return 199_999_999_999L;
            case 20:
                return 209_999_999_999L;

            default:
                return 0;
        }
    }

    public int getHpMpLimit() {
        if (limitPower == 0) {
            return 220000;
        }
        if (limitPower == 1) {
            return 240000;
        }
        if (limitPower == 2) {
            return 260000;
        }
        if (limitPower == 3) {
            return 280000;
        }
        if (limitPower == 4) {
            return 300000;
        }
        if (limitPower == 5) {
            return 350000;
        }
        if (limitPower == 6) {
            return 370000;
        }
        if (limitPower == 7) {
            return 400000;
        }
        if (limitPower == 8) {
            return 450000;
        }
        if (limitPower == 9) {
            return 450000;
        }
        if (limitPower == 10) {
            return 450000;
        }
        if (limitPower == 11) {
            return 450000;
        }
        if (limitPower == 12) {
            return 450000;
        }
        if (limitPower == 13) {
            return 450000;
        }
        if (limitPower == 14) {
            return 450000;
        }
        if (limitPower == 15) {
            return 450000;
        }
        if (limitPower == 16) {
            return 450000;
        }
        if (limitPower == 17) {
            return 450000;
        }
        if (limitPower == 18) {
            return 450000;
        }
        if (limitPower == 19) {
            return 450000;
        }
        if (limitPower == 20) {
            return 450000;
        }
        return 0;
    }

    public int getDameLimit() {
        if (limitPower == 0) {
            return 11000;
        }
        if (limitPower == 1) {
            return 12000;
        }
        if (limitPower == 2) {
            return 13000;
        }
        if (limitPower == 3) {
            return 14000;
        }
        if (limitPower == 4) {
            return 14000;
        }
        if (limitPower == 5) {
            return 16000;
        }
        if (limitPower == 6) {
            return 17000;
        }
        if (limitPower == 7) {
            return 22000;
        }
        if (limitPower == 8) {
            return 25000;
        }
        if (limitPower == 9) {
            return 25000;
        }
        if (limitPower == 10) {
            return 25000;
        }
        if (limitPower == 11) {
            return 25000;
        }
        if (limitPower == 12) {
            return 25000;

        }
        if (limitPower == 13) {
            return 25000;
        }
        if (limitPower == 14) {
            return 25000;
        }
        if (limitPower == 15) {
            return 25000;
        }
        if (limitPower == 16) {
            return 25000;
        }
        if (limitPower == 17) {
            return 25000;
        }
        if (limitPower == 18) {
            return 25000;
        }
        if (limitPower == 19) {
            return 25000;
        }
        if (limitPower == 20) {
            return 25000;
        }

        return 0;
    }

    public int getDefLimit() {
        if (limitPower == 0) {
            return 5500;
        }
        if (limitPower == 1) {
            return 6000;
        }
        if (limitPower == 2) {
            return 7000;
        }
        if (limitPower == 3) {
            return 8000;
        }
        if (limitPower == 4) {
            return 10000;
        }
        if (limitPower == 5) {
            return 12000;
        }
        if (limitPower == 6) {
            return 14000;
        }
        if (limitPower == 7) {
            return 16000;
        }
        if (limitPower == 8) {
            return 17000;
        }
        if (limitPower == 9) {
            return 17000;
        }
        if (limitPower == 10) {
            return 17000;
        }
        if (limitPower == 11) {
            return 17000;
        }
        if (limitPower == 12) {
            return 17000;

        }
        if (limitPower == 13) {
            return 17000;
        }
        if (limitPower == 14) {
            return 17000;
        }
        if (limitPower == 15) {
            return 17000;
        }
        if (limitPower == 16) {
            return 17000;
        }
        if (limitPower == 17) {
            return 17000;
        }
        if (limitPower == 18) {
            return 17000;
        }
        if (limitPower == 19) {
            return 17000;
        }
        if (limitPower == 20) {
            return 17000;
        }

        return 0;
    }

    public byte getCritLimit() {
        if (limitPower == 0) {
            return 5;
        }
        if (limitPower == 1) {
            return 6;
        }
        if (limitPower == 2) {
            return 7;
        }
        if (limitPower == 3) {
            return 8;
        }
        if (limitPower == 4) {
            return 9;
        }
        if (limitPower == 5) {
            return 10;
        }
        if (limitPower == 6) {
            return 10;
        }
        if (limitPower == 7) {
            return 10;
        }
        if (limitPower == 8) {
            return 10;
        }
        if (limitPower == 9) {
            return 10;
        }
        if (limitPower == 10) {
            return 10;
        }
        if (limitPower == 11) {
            return 10;
        }
        if (limitPower == 12) {
            return 10;

        }
        if (limitPower == 13) {
            return 10;
        }
        if (limitPower == 14) {
            return 10;
        }
        if (limitPower == 15) {
            return 10;
        }
        if (limitPower == 16) {
            return 10;
        }
        if (limitPower == 17) {
            return 10;
        }
        if (limitPower == 18) {
            return 10;
        }
        if (limitPower == 19) {
            return 10;

        }
        if (limitPower == 20) {
            return 10;

        }
        return 0;
    }

    //**************************************************************************
    //POWER - TIEM NANG
    public void powerUp(long power) {
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(long tiemNang) {
        this.tiemNang += tiemNang;
    }

    public void increasePoint(byte type, short point, boolean manualForPet) {
        if (point <= 0 || point > 100) {
            return;
        }
        long tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (2L * (this.hpg + 1000) + pointHp - 20) / 2;
            if ((this.hpg + pointHp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (2L * (this.mpg + 1000) + pointMp - 20) / 2;
            if ((this.mpg + pointMp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2L * this.dameg + point - 1) / 2 * 100;
            if ((this.dameg + point) <= getDameLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
//        if (type == 3) {
//            int defOld = this.defg;
//            tiemNangUse = 2L * (defOld + 5) * point / 2 * 100000;
//            if ((defOld + point) <= getDefLimit()) {
//                if (doUseTiemNang(tiemNangUse)) {
//                    defg += point;
//                }
//            } else {
//                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
//                return;
//            }
//        }
//        if (type == 4) {
//            int critOld = this.critg;
//            tiemNangUse = 50000000L;
//            for (int i = 0; i < critOld; i++) {
//                tiemNangUse *= 5L;
//            }
//            if ((critOld + point) <= getCritLimit()) {
//                if (doUseTiemNang(tiemNangUse)) {
//                    critg += point;
//                }
//            } else {
//                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
//                return;
//            }
//        }
        Service.gI().point(player);
        if (manualForPet) {
            Service.gI().sendChiSoPetGoc(((Pet) player).master);
            Service.gI().showInfoPet(((Pet) player).master);
            Service.gI().point(((Pet) player).master);
        }
    }

    private boolean doUseTiemNang(long tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.gI().sendThongBao(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang && this.tiemNang - tiemNang >= 0) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                int tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    PlayerService.gI().hoiPhuc(player, hpMax / 100 * tiLeHoiPhuc,
                            mpMax / 100 * tiLeHoiPhuc);
                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.gI().chat(player, "Phục hồi năng lượng " + getCurrPercentHP() + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        //hồi phục 30s
        //hồi phục thể lực
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.tlTNSM = null;
    }
}
