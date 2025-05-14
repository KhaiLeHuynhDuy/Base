package nro.services;

import nro.models.item.Item;
import nro.models.player.Player;
import java.util.Random;
import nro.jdbc.daos.PlayerDAO;
import nro.utils.Logger;

public class BinhCanhService {

    private static BinhCanhService instance;
    private final Random rand;

    // 0:Sơ Kỳ → 1, 1:Trung Kỳ → 2, 2:Hậu Kỳ → 3(Đỉnh Phong)
    private static final int MAX_BinhCanh = 3;

    private static final int ITEM_ID_REQUIRED = 457;
    private static final int[] ITEM_REQUIRE = {1, 2, 3};
    private static final int[] SUCCESS_RATE = {90, 70, 50};

    // Mặc định hệ số power requirement khi capTT != 1,2
    private static final long[] DEFAULT_POWER_REQUIRE = {
        250_000_000_000L,
        450_000_000_000L,
        750_000_000_000L
    };

    // Yêu cầu power theo capTT: [0]=Luyện Khí (capTT=1), [1]=Trúc Cơ (capTT=2)
    private static final long[][] REALM_POWER_REQUIRE = {
        {250_000_000_000L, 450_000_000_000L, 750_000_000_000L}, // capTT=1
        {1_200_000_000_000L, 1_700_000_000_000L, 2_100_000_000_000L} // capTT=2
    };

    // Thêm bonus nếu Thiên Đạo Trúc Cơ
    private static final long TRUC_CODAN_BONUS = 500_000_000_000L;

    public static BinhCanhService gI() {
        if (instance == null) {
            instance = new BinhCanhService();
        }
        return instance;
    }

    private BinhCanhService() {
        rand = new Random();
    }

    public void process(Player player, int times) {

        if (player.capTT > 0) {
            try {
                if (player.capCS >= MAX_BinhCanh) {
                    Service.gI().sendThongBao(player,
                            "Bạn đã đạt Đỉnh Phong, hãy độ kiếp để lên cảnh giới mới");
                    return;
                }

                int initialLevel = player.capCS;
                int totalItemsUsed = 0;
                int successCount = 0;

                for (int i = 0; i < times; i++) {
                    int currLevel = player.capCS;
                    if (currLevel >= MAX_BinhCanh) {
                        return;
                    }

                    int targetLevel = currLevel + 1;
                    int needItem = ITEM_REQUIRE[currLevel];
                    long needPower = calculateRequiredPower(player.capTT, currLevel, player.isUseTrucCoDan);

                    // 1) Kiểm tra item
                    Item it = findItem(player);
                    if (it == null || it.quantity < needItem) {
                        Service.gI().sendThongBao(player,
                                "Cần " + needItem + " Thỏi vàng để đột phá bình cảnh");
                        return;
                    }

                    // 2) Kiểm tra power
                    if (player.nPoint.power < needPower) {
                        Service.gI().sendThongBao(player,
                                String.format("Tu vi tối thiểu: %,l (Hiện tại: %,l)",
                                        needPower, player.nPoint.power));
                        return;
                    }

                    // 3) Trừ item
                    InventoryServiceNew.gI().subQuantityItemsBag(player, it, needItem);
                    totalItemsUsed += needItem;

                    // 4) Quay tỉ lệ thành công
                    boolean success = rand.nextInt(100) < SUCCESS_RATE[currLevel];
                    if (success) {
                        player.capCS = (byte) targetLevel;
                        successCount++;
                        PlayerDAO.updatePlayer(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        break;

                    }
                    if (player.capCS == MAX_BinhCanh) {
                        Service.gI().sendThongBao(player,
                                "Chúc mừng! Bạn đã đạt Đỉnh Phong. Hãy độ kiếp để lên cảnh giới cao hơn.");
                        return;
                    }
                }

                Service.gI().sendThongBao(player,
                        String.format(
                                "Kết quả đột phá %d lần:\n"
                                + "- Cảnh giới: %s ➔ %s\n"
                                + "- Đã dùng: %,d Thỏi vàng\n"
                                + "- Lượt thành công: %d",
                                times,
                                getRealNameBinhCanh(initialLevel),
                                getRealNameBinhCanh(player.capCS),
                                totalItemsUsed,
                                successCount
                        )
                );

            } catch (Exception e) {
                Logger.error("Lỗi đột phá bình cảnh: " + e.getMessage());
                Service.gI().sendThongBao(player, "Có lỗi xảy ra, vui lòng thử lại.");
            } finally {
                InventoryServiceNew.gI().sendItemBags(player);
            }
        }

    }

    /**
     * Tính power yêu cầu theo cảnh giới bên độ kiếp (capTT)
     */
    private long calculateRequiredPower(int capTT, int level, boolean isTrucCoDan) {
        long base;
        if (capTT == 1) {
            base = REALM_POWER_REQUIRE[0][level];
        } else if (capTT == 2) {
            base = REALM_POWER_REQUIRE[1][level];
        } else {
            base = DEFAULT_POWER_REQUIRE[level];
        }
        if (capTT == 2 && isTrucCoDan) {
            base += TRUC_CODAN_BONUS;
        }
        return base;
    }

    private Item findItem(Player player) {
        for (Item it : player.inventory.itemsBag) {
            if (it != null && it.template.id == ITEM_ID_REQUIRED) {
                return it;
            }
        }
        return null;
    }

    public String getRealNameBinhCanh(int level) {
        switch (level) {
            case 0:
                return "Sơ Kỳ";
            case 1:
                return "Trung Kỳ";
            case 2:
                return "Hậu Kỳ";
            case 3:
                return "Đỉnh Phong";
            default:
                return "Không xác định";
        }
    }
}
