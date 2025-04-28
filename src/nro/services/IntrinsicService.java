package nro.services;

import nro.consts.ConstNpc;
import nro.models.intrinsic.Intrinsic;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.server.Manager;
import nro.utils.Util;
import java.util.List;

public class IntrinsicService {

    private static IntrinsicService I;
    private static final int[] COST_OPEN = {10, 20, 40, 80, 160, 320, 640, 1280};

    public static IntrinsicService gI() {
        if (IntrinsicService.I == null) {
            IntrinsicService.I = new IntrinsicService();
        }
        return IntrinsicService.I;
    }

    public List<Intrinsic> getIntrinsics(byte playerGender) {
        switch (playerGender) {
            case 0:
                return Manager.INTRINSIC_TD;
            case 1:
                return Manager.INTRINSIC_NM;
            default:
                return Manager.INTRINSIC_XD;
        }
    }

    public Intrinsic getIntrinsicById(int id) {
        for (Intrinsic intrinsic : Manager.INTRINSICS) {
            if (intrinsic.id == id) {
                return new Intrinsic(intrinsic);
            }
        }
        return null;
    }

    public void sendInfoIntrinsic(Player player) {
        Message msg;
        try {
            msg = new Message(112);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.playerIntrinsic.intrinsic.icon);
            msg.writer().writeUTF(player.playerIntrinsic.intrinsic.getName());
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAllIntrinsic(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        Message msg;
        try {
            msg = new Message(112);
            msg.writer().writeByte(1);
            msg.writer().writeByte(1); //count tab
            msg.writer().writeUTF("Nội tại");
            msg.writer().writeByte(listIntrinsic.size() - 1);
            for (int i = 1; i < listIntrinsic.size(); i++) {
                msg.writer().writeShort(listIntrinsic.get(i).icon);
                msg.writer().writeUTF(listIntrinsic.get(i).getDescription());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMenu(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.INTRINSIC, -1,
                "Nội tại là một kỹ năng bị động hỗ trợ đặc biệt\nBạn có muốn mở hoặc thay đổi nội tại không?",
                "Xem\ntất cả\nNội Tại", "Mở\nNội Tại", "Mở VIP", "Từ chối");
    }

    public void sattd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_TD, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\nTaiyoken", "Set\nGenki", "Set\nkamejoko", "Từ chối");

    }

    public void satnm(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_NM, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\ngod ki", "Set\ngod dame", "Set\nsummon", "Từ chối");

    }

    public void setxd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_XD, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\ngod galick", "Set\nmonkey", "Set\ngod hp", "Từ chối");

    }

    //Set đồ mới 2023
    public void settltd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_TLTD, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\nTaiyoken", "Set\nGenki", "Set\nkamejoko", "Từ chối");

    }

    public void settlnm(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_TLNM, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\ngod ki", "Set\ngod dame", "Set\nsummon", "Từ chối");

    }

    public void settlxd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_TLXD, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\ngod galick", "Set\nmonkey", "Set\ngod hp", "Từ chối");

    }

    public void sethdtd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_HDTD, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\nTaiyoken", "Set\nGenki", "Set\nkamejoko", "Từ chối");

    }
//set tan thu
    public void setTanThu(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_TANTHU, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Nhận Set\nTân Thủ", "Từ chối");

    }

    public void sethdnm(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_HDNM, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\ngod ki", "Set\ngod dame", "Set\nsummon", "Từ chối");

    }

    public void sethdxd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_HDXD, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\ngod galick", "Set\nmonkey", "Set\ngod hp", "Từ chối");

    }

    public void settstd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_TSTD, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\nTaiyoken", "Set\nGenki", "Set\nkamejoko", "Từ chối");

    }

    public void settsnm(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_TSNM, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\ngod ki", "Set\ngod dame", "Set\nsummon", "Từ chối");

    }

    public void settsxd(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.SET_TSXD, -1,
                "chọn lẹ đi để tau đi chơi với ny", "Set\ngod galick", "Set\nmonkey", "Set\ngod hp", "Từ chối");

    }

    public void showConfirmOpen(Player player) {
        if (player.playerIntrinsic.countOpen > COST_OPEN.length - 1) {
            Service.gI().sendThongBao(player, "Mở 1 lần VIP để tái lập giá");
            return;
        }
        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC, -1,
                "Bạn muốn đổi Nội Tại khác\nvới giá là "
                + COST_OPEN[player.playerIntrinsic.countOpen] + " ngọc  ?", "Mở\nNội Tại", "Từ chối");
    }

    public void showConfirmOpenVip(Player player) {

        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP, -1,
                "Bạn có muốn mở Nội Tại\nvới giá là 1000 ngọc hồng và\nTái lập giá ngọc quay lại ban đầu không?", "Mở\nNội VIP", "Từ chối");
    }

    private void changeIntrinsic(Player player) {
        List<Intrinsic> listIntrinsic = getIntrinsics(player.gender);
        player.playerIntrinsic.intrinsic = new Intrinsic(listIntrinsic.get(Util.nextInt(1, listIntrinsic.size() - 1)));
        player.playerIntrinsic.intrinsic.param1 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom1, player.playerIntrinsic.intrinsic.paramTo1);
        player.playerIntrinsic.intrinsic.param2 = (short) Util.nextInt(player.playerIntrinsic.intrinsic.paramFrom2, player.playerIntrinsic.intrinsic.paramTo2);
        Service.gI().sendThongBao(player, "Bạn nhận được Nội tại:\n" + player.playerIntrinsic.intrinsic.getName().substring(0, player.playerIntrinsic.intrinsic.getName().indexOf(" [")));
        sendInfoIntrinsic(player);
    }

    public void open(Player player) {
        if (player.nPoint.power >= 10000000000L) {
            int gemRequire = COST_OPEN[player.playerIntrinsic.countOpen] * 1000;
            if (player.inventory.gem >= gemRequire) {
                player.inventory.gem -= gemRequire;
                PlayerService.gI().sendInfoHpMpMoney(player);
                changeIntrinsic(player);
                player.playerIntrinsic.countOpen++;
            } else {
                Service.gI().sendThongBao(player, "Bạn không đủ ngọc, còn thiếu "
                        + Util.numberToMoney(gemRequire - player.inventory.gem) + " ngọc nữa");
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
        }
    }

    public void openVip(Player player) {
        if (player.nPoint.power >= 10000000000L) {
            int rubyRequire = 1000;
            if (player.inventory.ruby >= rubyRequire) {
                player.inventory.ruby -= rubyRequire;
                PlayerService.gI().sendInfoHpMpMoney(player);
                changeIntrinsic(player);
                player.playerIntrinsic.countOpen = 0;
            } else {
                Service.gI().sendThongBao(player, "Bạn không có đủ hồng ngọc, còn thiếu "
                        + (rubyRequire - player.inventory.ruby) + " hồng ngọc nữa");
            }
        } else {
            Service.gI().sendThongBao(player, "Yêu cầu sức mạnh tối thiểu 10 tỷ");
        }
    }

}
