package nro.services;

import nro.models.item.Item;
import nro.models.player.Player;
import java.util.Random;
import nro.jdbc.daos.PlayerDAO;
import nro.utils.Logger;

public class DoKiepService {

    private static DoKiepService instance;
    private final Random rand;
    private static final int MAX_CapTuTien = 5;

    // Cấu hình mới
    private static final int ITEM_ID_REQUIRED = 457;
    private static final int[] ITEM_REQUIRE = {
        1, // Lv0 -> Lv1
        2, // Lv1 -> Lv2
        3, // Lv2 -> Lv3
        4, // Lv3 -> Lv4
        5 // Lv4 -> Lv5
    };

    private static final int[] DAME_REQUIRE = {
        25_000, // 1tr cho Lv0 -> Lv1
        3_600_000, // 3.6tr cho Lv1 -> Lv2
        5_000_000, // 5tr cho Lv2 -> Lv3
        7_500_000, // 7.5tr cho Lv3 -> Lv4
        10_000_000 // 10tr cho Lv4 -> Lv5
    };

    private static final int[] SUCCESS_RATE = {
        90, // Lv0 -> Lv1
        70, // Lv1 -> Lv2 
        50, // Lv2 -> Lv3
        30, // Lv3 -> Lv4
        10 // Lv4 -> Lv5
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
            if (player.capTT >= MAX_CapTuTien) {
                Service.gI().sendThongBao(player, "Bạn đã đạt cảnh giới tối đa");
                return;
            }

            int initialLevel = player.capTT;
            int totalItemsUsed = 0;
            int successCount = 0;

            for (int i = 0; i < times; i++) {
                int currentLevel = player.capTT;
                if (currentLevel >= MAX_CapTuTien) {
                    break;
                }

                // Kiểm tra điều kiện
                int targetLevel = currentLevel + 1;
                int requiredItems = ITEM_REQUIRE[currentLevel];
                int requiredDame = DAME_REQUIRE[currentLevel];

                // Kiểm tra vật phẩm
                Item requiredItem = findItem(player);
                if (requiredItem == null || requiredItem.quantity < requiredItems) {
                    Service.gI().sendThongBao(player, "Cần " + requiredItems + " Thỏi vàng để độ kiếp");
                    break;
                }

                // Kiểm tra sức đánh
                if (player.nPoint.dame < requiredDame) {
                    String dameMsg = String.format("Sức đánh tối thiểu: %,d (Hiện tại: %,d)",
                            requiredDame, player.nPoint.dame);
                    Service.gI().sendThongBao(player, dameMsg);
                    break;
                }

                // Trừ vật phẩm
                InventoryServiceNew.gI().subQuantityItemsBag(player, requiredItem, requiredItems);
                totalItemsUsed += requiredItems;

                // Tính toán thành công
                boolean success = rand.nextInt(100) < SUCCESS_RATE[currentLevel];

                if (success) {
                    player.capTT = (byte) targetLevel;
                    successCount++;
                    PlayerDAO.updatePlayer(player);
                    InventoryServiceNew.gI().sendItemBags(player);

                    break;
                }
            }

            // Thông báo tổng kết
            String resultMsg = String.format(
                    "Kết quả độ kiếp %d lần:\n"
                    + "- Cảnh giới: %s ➔ %s\n"
                    + "- Đã dùng: %,d Thỏi vàng\n"
                    + "- Tỷ lệ thành công: %d%%",
                    times,
                    getRealNameCanhGioi(player, initialLevel),
                    getRealNameCanhGioi(player, player.capTT),
                    totalItemsUsed,
                    SUCCESS_RATE[initialLevel]
            );

            Service.gI().sendThongBao(player, resultMsg);

        } catch (Exception e) {
            Logger.error("Lỗi độ kiếp: " + e.getMessage());
            Service.gI().sendThongBao(player, "Có lỗi xảy ra, vui lòng thử lại.");
        } finally {
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private Item findItem(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item != null && item.template.id == ITEM_ID_REQUIRED) {
                return item;
            }
        }
        return null;
    }

    public String getRealNameCanhGioi(Player player, int level) {
        switch (level) {
            case 0:
                return "Phàm Nhân";
            case 1:
                return "Luyện Khí";
            case 2:
                if (player.isUseTrucCoDan) {
                    return "Thiên Đạo Trúc Cơ";
                } else {
                    return "Trúc Cơ";
                }
            case 3:
                if (player.dotpha != 0) {
                    return "Kim Đan";
                } else {
                    return "Cụ Linh";
                }
            case 4:
                return "Nguyên Anh";
            case 5:
                return "Hóa Thần";
            default:
                return "Không xác định";
        }
    }

}
