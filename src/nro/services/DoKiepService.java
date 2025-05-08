package nro.services;

import java.io.IOException;
import nro.models.player.Player;
import java.util.Random;
import nro.jdbc.daos.PlayerDAO;
import nro.network.io.Message;
import nro.utils.Logger;

public class DoKiepService {

    private static DoKiepService instance;
    private final Random rand;
    private static final int MAX_CAPTT = 5;

    private static final long[] GOLD_REQUIRE = {
        50_000_000,
        100_000_000, // Cấp 1
        200_000_000, // Cấp 2
        300_000_000, // Cấp 3
        400_000_000, // Cấp 4
        500_000_000 // Cấp 5
    };

    private static final int[] SUCCESS_RATE = {
        15,
        10, // 90% cho cấp 1
        5, // 70% cho cấp 2
        2, // 50% cho cấp 3
        5, // 30% cho cấp 4
        1 // 10% cho cấp 5
    };

    public static DoKiepService gI() {
        if (instance == null) {
            instance = new DoKiepService();
        }
        return instance;
    }

    private DoKiepService() {
        rand = new Random();
    }

    public void process(Player player, int times) {
        try {
            if (player.capTT >= MAX_CAPTT) {
                Service.gI().sendThongBao(player, "Bạn đã đạt cảnh giới tối đa");
                return;
            }

            int initialLevel = player.capTT;
            long totalGoldUsed = 0;
            int successCount = 0;

            for (int i = 0; i < times; i++) {
                // Kiểm tra điều kiện mỗi lần lặp
                if (player.capTT >= MAX_CAPTT) {
                    break;
                }

                int targetLevel = player.capTT + 1;
                long requiredGold = GOLD_REQUIRE[targetLevel - 1];

                // Kiểm tra vàng
                if (player.inventory.gold < requiredGold) {
                    Service.gI().sendThongBao(player, "Hết vàng sau " + i + " lần độ kiếp");
                    break;
                }

                // Trừ vàng trước khi check tỷ lệ
                player.inventory.gold -= requiredGold;
                totalGoldUsed += requiredGold;

                // Cập nhật vàng ngay lập tức
                InventoryServiceNew.gI().sendItemBags(player);

                // Tính toán thành công
                boolean success = rand.nextInt(100) < SUCCESS_RATE[targetLevel - 1];

                if (success) {
                    player.capTT = (byte) targetLevel;
                    successCount = i;
                    PlayerDAO.updatePlayer(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                }

                // Nếu đạt max level thì dừng
                if (player.capTT >= MAX_CAPTT) {
                    break;
                }
            }

            // Thông báo tổng kết
            String resultMsg = String.format("Kết quả độ kiếp %d lần:\n"
                    + "- Thăng cấp: %d ➔ %d\n"
                    + "- Tổng hao phí: %,d vàng\n",
                    times, initialLevel, player.capTT, totalGoldUsed);

            Service.gI().sendThongBao(player, resultMsg);

        } catch (Exception e) {
            Logger.error("Lỗi độ kiếp: " + e.getMessage());
            Service.gI().sendThongBao(player, "Có lỗi xảy ra, vui lòng thử lại.");
        } finally {
            // Đảm bảo cập nhật UI lần cuối
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
        }
    }
}
