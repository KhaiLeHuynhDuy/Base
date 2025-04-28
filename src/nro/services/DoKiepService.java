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
    private static final int MAX_CAPTT = 5; // Cảnh giới tối đa

    // Cấu hình vàng và tỷ lệ thành công theo từng cấp độ (capTT + 1)
    private static final long[] GOLD_REQUIRE = {
        100_000_000, // Cấp 1
        200_000_000, // Cấp 2 
        300_000_000, // Cấp 3
        400_000_000, // Cấp 4
        500_000_000 // Cấp 5
    };

    private static final int[] SUCCESS_RATE = {
        60, // 90% cho cấp 1
        50, // 70% cho cấp 2
        40, // 50% cho cấp 3
        30, // 30% cho cấp 4
        10 // 10% cho cấp 5
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

    public void process(Player player) {
        try {
            // Đã đạt cảnh giới tối đa
            if (player.capTT >= MAX_CAPTT) {
                sendError(player, "Bạn đã đạt cảnh giới tối đa");
                return;
            }

            // Tính toán yêu cầu cho cấp tiếp theo
            int targetLevel = player.capTT + 1;
            long requiredGold = GOLD_REQUIRE[targetLevel - 1];
            int successRate = SUCCESS_RATE[targetLevel - 1];

            // Kiểm tra vàng
            if (player.inventory.gold < requiredGold) {
                sendError(player, "Cần " + requiredGold + " vàng để độ kiếp");
                return;
            }

            // Trừ vàng
            player.inventory.gold -= requiredGold;

            // Tính toán thành công
            boolean success = rand.nextInt(100) < successRate;

            // Cập nhật cảnh giới
            if (success) {
                player.capTT = (byte) targetLevel;
            }

            // Lưu vào DB
            PlayerDAO.updatePlayer(player);

            // Gửi kết quả
            sendResult(player, success, requiredGold);

        } catch (Exception e) {
            Logger.error("Lỗi độ kiếp: " + e.getMessage());
            sendError(player, "Lỗi hệ thống");
        }
    }

    private void sendResult(Player player, boolean success, long goldUsed) {
        Message msg = new Message(71);
        try {
            msg.writer().writeBoolean(success);
            msg.writer().writeLong(player.inventory.gold);
            msg.writer().writeByte(player.capTT);
            player.sendMessage(msg);
        } catch (IOException e) {
            Logger.error("Lỗi gửi kết quả độ kiếp: " + e.getMessage());
        } finally {
            msg.cleanup(); // Đảm bảo dọn dẹp tài nguyên
        }
    }

    private void sendError(Player player, String message) {
        Message msg = new Message(71);
        try {
            msg.writer().writeBoolean(false);
            msg.writer().writeUTF(message);
            player.sendMessage(msg);
        } catch (IOException e) {
            Logger.error("Lỗi gửi thông báo lỗi: " + e.getMessage());
        } finally {
            msg.cleanup();
        }
    }
}
