package nro.services;

import java.util.ArrayList;
import java.util.List;
import nro.map.RanDoc.ConDuongRanDoc;

import nro.consts.ConstPlayer;
import nro.map.gas.Gas;
import nro.models.item.Item;
import nro.models.item.ItemTime;
import static nro.models.item.ItemTime.*;
import nro.models.map.BDKB.BanDoKhoBau;
import nro.models.map.doanhtrai.DoanhTrai;
import nro.models.player.Fusion;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.utils.Logger;
import nro.utils.Util;

public class ItemTimeService {

    private static ItemTimeService i;

    public static ItemTimeService gI() {
        if (i == null) {
            i = new ItemTimeService();
        }
        return i;
    }
    public List<ItemTime> vItemTime = new ArrayList<>();
    private String str = "Sự kiện trung thu và sự kiện hè đang diễn ra tại Làng Aru mời ae đến thẩm!!";

    //gửi cho client
    public void sendAllItemTime(Player player) {
        if (player.isPl()) {
            if (player.itemTime.isBienHinhMa) {
                int id = 29443;
////                if (Util.isTrue(80, 100)) {
//
////                    if (player.gender == 0) {
////                        id = 7426;
////                    }
//                if (player.gender == 1) {
//                    id = 29529;
//                }
//                if (player.gender == 2) {
//                    id = 29513;
//                }
////                }
                sendItemTime(player, id, (int) ((1800000 - (System.currentTimeMillis() - player.itemTime.lastTimeBienHinhMa)) / 1000));
            }
        }
        sendTextDoanhTrai(player);
        sendTextBanDoKhoBau(player);
        sendTextGas(player);
        sendTextConDuongRanDoc(player);
        if (player != null) {
            if (player.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                sendItemTime(player, player.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                        (int) ((Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
            }
            if (player.itemTime.isUseBoHuyet) {
                sendItemTime(player, 2755, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) / 1000));
            }
            if (player.itemTime.isUseBoKhi) {
                sendItemTime(player, 2756, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) / 1000));
            }
            if (player.itemTime.isUseGiapXen) {
                sendItemTime(player, 2757, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) / 1000));
            }
            if (player.itemTime.isUseCuongNo) {
                sendItemTime(player, 2754, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) / 1000));
            }

            if (player.itemTime.isUseAnDanh) {
                sendItemTime(player, 2760, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) / 1000));
            }
            if (player.itemTime.isUseBoHuyetSC) {
                sendItemTime(player, 10714, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyetSC)) / 1000));
            }
            if (player.itemTime.isUseBoKhiSC) {
                sendItemTime(player, 10715, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhiSC)) / 1000));
            }
            if (player.itemTime.isUseGiapXenSC) {
                sendItemTime(player, 10712, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXenSC)) / 1000));
            }
            if (player.itemTime.isUseCuongNoSC) {
                sendItemTime(player, 10716, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNoSC)) / 1000));
            }
            if (player.itemTime.isUseAnDanhSC) {
                sendItemTime(player, 10717, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanhSC)) / 1000));
            }
            if (player.itemTime.isUseBanhTet) {
                sendItemTime(player, 7079, (int) ((TIME_ITEM45P - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTet)) / 1000));
            }

            if (player.itemTime.isUseBanhChung) {
                sendItemTime(player, 7080, (int) ((TIME_ITEM45P - (System.currentTimeMillis() - player.itemTime.lastTimeBanhChung)) / 1000));
            }
            if (player.itemTime.isOpenPower) {
                sendItemTime(player, 3783, (int) ((TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) / 1000));
            }
            if (player.itemTime.isUseMayDo) {
                sendItemTime(player, 2758, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) / 1000));
            }
            if (player.itemTime.isUseBanhSau) {
                sendItemTime(player, 8247, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBanhSau)) / 1000));
            }
            if (player.itemTime.isUseBanhNhen) {
                sendItemTime(player, 8246, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBanhNhen)) / 1000));
            }
            if (player.itemTime.isUseSupBi) {
                sendItemTime(player, 8244, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeSupBi)) / 1000));
            }
            if (player.itemTime.isUseKeoMotMat) {
                sendItemTime(player, 8243, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeKeoMotMat)) / 1000));
            }
            if (player.itemTime.isUseBanhNgot) {
                sendItemTime(player, 11699, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBanhNgot)) / 1000));
            }
            if (player.itemTime.isUseKemOcQue) {
                sendItemTime(player, 11700, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeKemOcQue)) / 1000));
            }
            if (player.itemTime.isUseKeoDeo) {
                sendItemTime(player, 11701, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeKeoDeo)) / 1000));
            }
            if (player.itemTime.isUseKeoTrongGoi) {
                sendItemTime(player, 11704, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeKeoTrongGoi)) / 1000));
            }
            if (player.itemTime.isUseVooc) {
                sendItemTime(player, 8060, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseVooc)) / 1000));
            }
            if (player.itemTime.isUseConCua) {
                sendItemTime(player, 8061, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseConCua)) / 1000));
            }
            if (player.itemTime.isUseSaoBien) {
                sendItemTime(player, 8062, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseSaoBien)) / 1000));
            }
            if (player.itemTime.isUseMayDoSK) {
                sendItemTime(player, 28790, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDoSK)) / 1000));
            }
            if (player.itemTime.isUseGaQuay) {
                sendItemTime(player, 8132, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeGaQuay)) / 1000));
            }
            if (player.itemTime.isUseThapCap) {
                sendItemTime(player, 8131, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeThapCap)) / 1000));
            }
            if (player.itemTime.isBiNgo) {
                sendItemTime(player, 5138, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBiNgo)) / 1000));
            }
//        if (player.itemTime.isUseMayDo2) {//2758 icon// cai nay time co cho bằng cái máy dò kia ko
//            sendItemTime(player, 16004, (int) ((TIME_MAY_DO2 - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2)) / 1000));
//        }
            if (player.itemTime.isUseMayDo2) {//2758 icon// cai nay time co cho bằng cái máy dò kia ko
                sendItemTime(player, 16004, (int) ((TIME_MAY_DO2 - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2)) / 1000));
            }
            if (player.itemTime.isUseMayDo3) {//2758 icon// cai nay time co cho bằng cái máy dò kia ko
                sendItemTime(player, 11705, (int) ((TIME_MAY_DO3 - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo3)) / 1000));
            }
            if (player.itemTime.isLoNuocThanhx2) {
                sendItemTime(player, 21881, (int) ((TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx2)) / 1000));
            }
            if (player.itemTime.isLoNuocThanhx5) {
                sendItemTime(player, 21878, (int) ((TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx5)) / 1000));
            }
            if (player.itemTime.isLoNuocThanhx7) {
                sendItemTime(player, 21879, (int) ((TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx7)) / 1000));
            }
            if (player.itemTime.isLoNuocThanhx10) {
                sendItemTime(player, 21880, (int) ((TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx10)) / 1000));
            }
            if (player.itemTime.isLoNuocThanhx15) {
                sendItemTime(player, 21876, (int) ((TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx15)) / 1000));
            }
            if (player.itemTime.isUseDuoiKhi) {
                if (player.gender == ConstPlayer.TRAI_DAT) {
                    sendItemTime(player, 5072, (int) ((TIME_KHITD - (System.currentTimeMillis() - player.itemTime.lastTimeDuoiKhi)) / 1000));
                }
                if (player.gender == ConstPlayer.NAMEC) {
                    sendItemTime(player, 5072, (int) ((TIME_KHINM - (System.currentTimeMillis() - player.itemTime.lastTimeDuoiKhi)) / 1000));
                }
                if (player.gender == ConstPlayer.XAYDA) {
                    Service.gI().sendThongBaoOK(player, "Người Xayda không thể dùng");
                }
            }
            if (player.itemTime.isEatMeal) {
                sendItemTime(player, player.itemTime.iconMeal, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) / 1000));
            }
            if (player.itemTime.isEatMeal) {
                sendItemTime(player, player.itemTime.iconMeal, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) / 1000));
            }
            if (player.itemTime.isUseTDLT) {
                sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000);
            }
        }
    }

    //bật tđlt
    public void turnOnTDLT(Player player, Item item) {
        int min = 0;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                min = io.param;
                io.param = 0;
                break;
            }
        }
        player.itemTime.isUseTDLT = true;
        player.itemTime.timeTDLT = min * 60 * 1000;
        player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();

        sendCanAutoPlay(player);
        sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    //tắt tđlt
    public void turnOffTDLT(Player player, Item item) {
        player.itemTime.isUseTDLT = false;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param += (short) ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000);
                break;
            }
        }

        sendCanAutoPlay(player);
        removeItemTime(player, 4387);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void sendCanAutoPlay(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(player.itemTime.isUseTDLT ? 1 : 0);
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(ItemTimeService.class, e);
        }
    }

    public void farmquaion(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(1);
            player.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(ItemTimeService.class, e);
        }
    }

    public void farmquaioff(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(ItemTimeService.class, e);
        }
    }

    public void sendTextDoanhTrai(Player player) {
        if (player.clan != null
                && player.clan.doanhTrai != null
                && !player.clan.doanhTrai_haveGone
                && player.clan.doanhTrai_lastTimeOpen != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.doanhTrai.getLastTimeOpen()) / 1000);
            int secondsLeft = (DoanhTrai.TIME_DOANH_TRAI / 1000) - secondPassed;
            sendTextTime(player, DOANH_TRAI, "Doanh trại độc nhãn", secondsLeft);

        }
    }

    public void sendTextGas(Player player) {
        if (player != null
                && player.clan != null
                && player.clan.timeOpenKhiGas != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.khiGas.getLastTimeOpen()) / 1000);
            int secondsLeft = (Gas.TIME_KHI_GAS / 1000) - secondPassed;
            sendTextTime(player, KHI_GASS, "Khí Gas Hủy Diệt: ", secondsLeft);
        }
    }

    public void sendTextConDuongRanDoc(Player player) {
        if (player != null
                && player.clan != null
                && player.clan.ConDuongRanDoc != null
                && !player.clan.ConDuongRanDoc_haveGone
                && player.clan.ConDuongRanDoc_lastTimeOpen != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.ConDuongRanDoc.getLastTimeOpen()) / 1000);
            int secondsLeft = (ConDuongRanDoc.TIME_CDRD / 1000) - secondPassed;
            sendTextTime(player, CON_DUONG_RAN_DOC, "Con đường rắn độc: ", secondsLeft);
        }
    }

    public void sendTextBanDoKhoBau(Player player) {
        if (player != null
                && player.clan != null
                && player.clan.banDoKhoBau != null
                && !player.clan.banDoKhoBau_haveGone
                && player.clan.banDoKhoBau_lastTimeOpen != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.banDoKhoBau.getLastTimeOpen()) / 1000);
            int secondsLeft = (BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            sendTextTime(player, BAN_DO_KHO_BAU, "Bản đồ kho báu", secondsLeft);
        }
    }

    public void removeTextDoanhTrai(Player player) {
        removeTextTime(player, DOANH_TRAI);
    }

    public void removeTextBDKB(Player player) {
        removeTextTime(player, BAN_DO_KHO_BAU);
    }

    public void removeTextConDuongRanDoc(Player player) {
        removeTextTime(player, CON_DUONG_RAN_DOC);
    }

    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, "", 0);
    }

//    public void send_text_time_nhan_bua_mien_phi(Player player) {
//        if (Util.canDoWithTime(player.itemTime.lastTimeSendTextTime, 60000)) {
//            if (player.event.getLuotNhanBuaMienPhi() == 1) {
//                ItemTimeService.gI().sendTextTime(player, TEXT_NHAN_BUA_MIEN_PHI, str, 30);
//            }
//            player.itemTime.lastTimeSendTextTime = System.currentTimeMillis();
//        }
//    }

    public void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendItemTime(Player player, int itemId, int time) {
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItemTime(Player player, int itemTime) {
        sendItemTime(player, itemTime, 0);
    }

}
