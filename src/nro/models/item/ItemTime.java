package nro.models.item;

import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstPlayer;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.services.Service;
import nro.utils.Util;
import nro.services.ItemTimeService;

public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;
    public static final byte KHI_GASS = 2;
    public static final byte CON_DUONG_RAN_DOC = 3;
    public static final byte TEXT_NHAN_BUA_MIEN_PHI = 3;
    public static final int TIME_ITEM = 600000;
    public static final int TIME_ITEM45P = 2700000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_MAY_DO2 = 21600000;
    public static final int TIME_MAY_DO3 = 21600000;
    public static final int TIME_EAT_MEAL = 600000;

    public static final int TIME_KHITD = 1800000;
    public static final int TIME_KHINM = 7200000;

    public static final int TIME_MAX_TNSM = 600000;

    private Player player;

    public boolean isUseVooc;
    public long lastTimeUseVooc;

    public boolean isUseSaoBien;
    public long lastTimeUseSaoBien;

    public boolean isUseConCua;
    public long lastTimeUseConCua;

    public boolean isUseMayDoSK;
    public long lastTimeUseMayDoSK;

    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;
    public boolean isUseBanhSau;
    public boolean isUseBanhNhen;
    public boolean isUseSupBi;
    public boolean isUseKeoMotMat;
    public boolean isUseBoHuyetSC;
    public boolean isUseBoKhiSC;
    public boolean isUseGiapXenSC;
    public boolean isUseCuongNoSC;
    public boolean isUseAnDanhSC;
    public boolean isUseBanhTet;
    public boolean isUseBanhChung;
    public boolean isUseBanhNgot;
    public boolean isUseKeoTrongGoi;
    public boolean isUseKemOcQue;
    public boolean isUseKeoDeo;
    public boolean isUseGaQuay;
    public boolean isUseThapCap;

    public boolean isUseDuoiKhi;
    public long lastTimeDuoiKhi;
    public long lastTimeGaQuay;
    public long lastTimeThapCap;

    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;
    public long lastTimeBanhSau;
    public long lastTimeBanhNhen;
    public long lastTimeSupBi;
    public long lastTimeKeoMotMat;

    public long lastTimeBoHuyetSC;
    public long lastTimeBoKhiSC;
    public long lastTimeGiapXenSC;
    public long lastTimeCuongNoSC;
    public long lastTimeAnDanhSC;
    public long lastTimeBanhTet;
    public long lastTimeBanhChung;
    public long lastTimeBanhNgot;
    public long lastTimeKeoTrongGoi;
    public long lastTimeKemOcQue;
    public long lastTimeKeoDeo;

    public boolean isUseMayDo;
    public long lastTimeUseMayDo;//lastime de chung 1 cai neu time = nhau
    public boolean isUseMayDo2;
    public boolean isUseMayDo3;
    public long lastTimeUseMayDo2;
    public long lastTimeUseMayDo3;

    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;

    public boolean isBiNgo;
    public long lastTimeBiNgo;

    public boolean isLoNuocThanhx2;
    public boolean isLoNuocThanhx5;
    public boolean isLoNuocThanhx7;
    public boolean isLoNuocThanhx10;
    public boolean isLoNuocThanhx15;
    public long lastTimeLoNuocThanhx2;
    public long lastTimeLoNuocThanhx5;
    public long lastTimeLoNuocThanhx7;
    public long lastTimeLoNuocThanhx10;
    public long lastTimeLoNuocThanhx15;

    public ItemTime(Player player) {
        this.player = player;
    }

    public boolean isBienHinhMa;

    public long lastTimeBienHinhMa;

    public void update() {
        if (isLoNuocThanhx2) {
            if (Util.canDoWithTime(lastTimeLoNuocThanhx2, TIME_MAX_TNSM)) {
                isLoNuocThanhx2 = false;
                Service.gI().point(player);
            }
        }
        if (isLoNuocThanhx5) {
            if (Util.canDoWithTime(lastTimeLoNuocThanhx5, TIME_MAX_TNSM)) {
                isLoNuocThanhx5 = false;
                Service.gI().point(player);
            }
        }
        if (isLoNuocThanhx7) {
            if (Util.canDoWithTime(lastTimeLoNuocThanhx7, TIME_MAX_TNSM)) {
                isLoNuocThanhx7 = false;
                Service.gI().point(player);
            }
        }
        if (isLoNuocThanhx10) {
            if (Util.canDoWithTime(lastTimeLoNuocThanhx10, TIME_MAX_TNSM)) {
                isLoNuocThanhx10 = false;
                Service.gI().point(player);
            }
        }
        if (isLoNuocThanhx15) {
            if (Util.canDoWithTime(lastTimeLoNuocThanhx15, TIME_MAX_TNSM)) {
                isLoNuocThanhx15 = false;
                Service.gI().point(player);
            }
        }
        if (isBienHinhMa) {
            if (Util.canDoWithTime(lastTimeBienHinhMa, 1800000)) {
                isBienHinhMa = false;
                Service.gI().point(player);
            }
        }
        if (isBiNgo) {
            if (Util.canDoWithTime(lastTimeBiNgo, TIME_MAY_DO)) {
                isBiNgo = false;
                Service.gI().point(player);
            }
        }
        if (isUseDuoiKhi) {
            if (player.gender == ConstPlayer.TRAI_DAT) {
                if (Util.canDoWithTime(lastTimeDuoiKhi, TIME_KHITD)) {
                    isUseDuoiKhi = false;
                    Service.gI().point(player);
                }
            }
            if (player.gender == ConstPlayer.NAMEC) {
                if (Util.canDoWithTime(lastTimeDuoiKhi, TIME_KHINM)) {
                    isUseDuoiKhi = false;
                    Service.gI().point(player);
                }

            }
        }
        if (isUseMayDoSK) {
            if (Util.canDoWithTime(lastTimeUseMayDoSK, TIME_MAY_DO)) {
                isUseMayDoSK = false;
                Service.gI().point(player);
            }
        }
        if (isUseGaQuay) {
            if (Util.canDoWithTime(lastTimeGaQuay, TIME_EAT_MEAL)) {
                isUseGaQuay = false;
                Service.gI().point(player);
            }
        }

        if (isUseThapCap) {
            if (Util.canDoWithTime(lastTimeThapCap, TIME_EAT_MEAL)) {
                isUseThapCap = false;
                Service.gI().point(player);
            }
        }
        if (isUseVooc) {
            if (Util.canDoWithTime(lastTimeUseVooc, TIME_MAY_DO)) {
                isUseVooc = false;
                Service.gI().point(player);
            }
        }

        if (isUseSaoBien) {
            if (Util.canDoWithTime(lastTimeUseSaoBien, TIME_MAY_DO)) {
                isUseSaoBien = false;
                Service.gI().point(player);
            }
        }

        if (isUseConCua) {
            if (Util.canDoWithTime(lastTimeUseConCua, TIME_MAY_DO)) {
                isUseConCua = false;
                Service.gI().point(player);
            }
        }
        if (isEatMeal) {
            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
                isEatMeal = false;
                Service.gI().point(player);
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                Service.gI().point(player);
            }
        }

        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                Service.gI().point(player);
            }
        }

        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }

        if (isUseBoHuyetSC) {
            if (Util.canDoWithTime(lastTimeBoHuyetSC, TIME_ITEM)) {
                isUseBoHuyetSC = false;
                Service.gI().point(player);
//                Service.gI().Send_Info_NV(this.player);
            }
        }
        if (isUseBoKhiSC) {
            if (Util.canDoWithTime(lastTimeBoKhiSC, TIME_ITEM)) {
                isUseBoKhiSC = false;
                Service.gI().point(player);
            }
        }
        if (isUseGiapXenSC) {
            if (Util.canDoWithTime(lastTimeGiapXenSC, TIME_ITEM)) {
                isUseGiapXenSC = false;
            }
        }
        if (isUseCuongNoSC) {
            if (Util.canDoWithTime(lastTimeCuongNoSC, TIME_ITEM)) {
                isUseCuongNoSC = false;
                Service.gI().point(player);
            }
        }
        if (isUseAnDanhSC) {
            if (Util.canDoWithTime(lastTimeAnDanhSC, TIME_ITEM)) {
                isUseAnDanhSC = false;
            }
        }

        if (isUseBanhTet) {
            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM45P)) {
                isUseBanhTet = false;
                Service.gI().point(player);
            }
        }
        if (isUseBanhChung) {
            if (Util.canDoWithTime(lastTimeBanhChung, TIME_ITEM45P)) {
                isUseBanhChung = false;
                Service.gI().point(player);
            }
        }
        if (isUseBanhNhen) {
            if (Util.canDoWithTime(lastTimeBanhNhen, TIME_MAY_DO)) {
                isUseBanhNhen = false;
                Service.gI().point(player);
            }
        }

        if (isUseBanhSau) {
            if (Util.canDoWithTime(lastTimeBanhSau, TIME_MAY_DO)) {
                isUseBanhSau = false;
                Service.gI().point(player);
            }
        }
        if (isUseSupBi) {
            if (Util.canDoWithTime(lastTimeSupBi, TIME_MAY_DO)) {
                isUseSupBi = false;
                Service.gI().point(player);
            }
        }

        if (isUseKeoMotMat) {
            if (Util.canDoWithTime(lastTimeKeoMotMat, TIME_MAY_DO)) {
                isUseKeoMotMat = false;
                Service.gI().point(player);
            }
        }
        if (isUseBanhNgot) {
            if (Util.canDoWithTime(lastTimeBanhNgot, TIME_EAT_MEAL)) {
                isUseBanhNgot = false;
                Service.gI().point(player);
            }
        }

        if (isUseKeoDeo) {
            if (Util.canDoWithTime(lastTimeKeoDeo, TIME_EAT_MEAL)) {
                isUseKeoDeo = false;
                Service.gI().point(player);
            }
        }
        if (isUseKemOcQue) {
            if (Util.canDoWithTime(lastTimeKemOcQue, TIME_EAT_MEAL)) {
                isUseKemOcQue = false;
                Service.gI().point(player);
            }
        }

        if (isUseKeoTrongGoi) {
            if (Util.canDoWithTime(lastTimeKeoTrongGoi, TIME_EAT_MEAL)) {
                isUseKeoTrongGoi = false;
                Service.gI().point(player);
            }
        }
        if (isOpenPower) {
            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
                player.nPoint.limitPower++;
                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
                }
                Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
                isOpenPower = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseMayDo2) {
            if (Util.canDoWithTime(lastTimeUseMayDo2, TIME_MAY_DO2)) {
                isUseMayDo2 = false;
            }
        }
        if (isUseMayDo3) {
            if (Util.canDoWithTime(lastTimeUseMayDo3, TIME_MAY_DO3)) {
                isUseMayDo3 = false;
            }
        }
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
