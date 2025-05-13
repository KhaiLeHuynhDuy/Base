package nro.services;

import nro.models.player.Player;
import nro.utils.Logger;

public class DotPhaService {

    private static DotPhaService instance;

    private DotPhaService() {
    }

    public static DotPhaService gI() {
        if (instance == null) {
            instance = new DotPhaService();
        }
        return instance;
    }

    public void thucHienDotPha(Player player, int select) {
        try {
            int maxDameg = 18_000_000;
            int maxHpMp = maxDameg * 3;

            // Kiểm tra giới hạn Trúc Cơ Cảnh
            boolean alreadyBeyond = player.nPoint.dameg >= maxDameg
                    || player.nPoint.hpg >= maxHpMp
                    || player.nPoint.mpg >= maxHpMp;

            if (!alreadyBeyond) {
                Service.gI().sendThongBaoOK(player, "Bạn chưa đạt giới hạn chỉ số của Trúc Cơ Cảnh để có thể đột phá!");
                return;
            }
            if (player.capTT != 3) {
                Service.gI().sendThongBaoOK(player, "Lại độ thiên kiếp rồi lại tới đột phá!");
                return;
            }
            if (player.dotpha != 0) {
                Service.gI().sendThongBaoOK(player, "Bạn đã đột phá rồi, không thể thực hiện lại!");
                return;
            }

            // Cho phép đột phá
            if (select == 0) {
                player.dotpha = 1; // Pháp Tu
                player.nPoint.dameg = player.nPoint.dameg + 6_000_000;
                player.nPoint.hpg = player.nPoint.hpg + 3_000_000;
                player.nPoint.mpg = player.nPoint.mpg + 3_000_000;
               // player.nPoint.tlNeDon += 105;
                Service.gI().sendThongBaoOK(player, "Bạn đã đột phá thành Pháp Tu!");
            } else if (select == 1) {
                player.dotpha = 2; // Thể Tu
                player.nPoint.dameg = player.nPoint.dameg + 1_000_000;
                player.nPoint.defg = player.nPoint.defg + 1_000_000;
                player.nPoint.hpg = player.nPoint.hpg + 18_000_000;
                player.nPoint.mpg = player.nPoint.mpg + 18_000_000;
               // player.nPoint.tlPST += 10;
                Service.gI().sendThongBaoOK(player, "Bạn đã đột phá thành Thể Tu!");
            } else {
                Service.gI().sendThongBaoOK(player, "Lựa chọn không hợp lệ.");
            }
        } catch (Exception e) {
            Logger.error("Lỗi khi thực hiện đột phá: " + e.getMessage());
            Service.gI().sendThongBaoOK(player, "Đã xảy ra lỗi khi đột phá, vui lòng thử lại.");
        }
    }

    public String getRealNameDotPha(int level) {
        switch (level) {
            case 0:
                return "Chưa Đột Phá";
            case 1:
                return "Pháp Tu";
            case 2:
                return "Thể Tu";
            default:
                return "Không xác định";
        }
    }
}
