package nro.services.func;

import nro.consts.ConstNpc;
import nro.consts.cn;
import nro.models.item.Item;
import nro.models.item.Item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.ServerNotify;
import nro.network.io.Message;
import nro.services.*;
import nro.utils.Logger;
import nro.utils.Util;

import java.util.*;
import java.util.stream.Collectors;
import nro.jdbc.daos.PlayerDAO;

public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;
    private static final int COST_THIEN_MA_THACH = 500000;
    private static final int COST_RUBY_DOI_SET_THIEN_MA = 1000000;

    private static final int COST_RUBY_DOI_SET_TU_TAI = 10000000;
    private static final int COST_LINH_KHI_DOI_SET_TU_TAI = 99999;
    private static final int COST_THOI_VANG_DOI_SET_TU_TAI = 999;
    private static final int COST_DAN_TRUC_CO_DOI_SET_TU_TAI = 99;
    private static final int COST_COIN_DOI_SET_TU_TAI = 200000;

    private static final int COST = 500000000;
    private static final int RUBY = 500;
    private static final int TIME_COMBINE = 1500;

    private static final byte MAX_STAR_ITEM = 12;
    private static final byte MAX_LEVEL_ITEM = 10;
    private static final byte MAX_LEVEL_ITEM2 = 10;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    private static final byte COMBINE_DA_VUN = 7;
    private static final byte COMBINE_LT = 8;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;
    public static final int DAP_DO_AO_HOA = 509;
    public static final int PS_HOA_TRANG_BI = 2150;
    public static final int TAY_PS_HOA_TRANG_BI = 2151;
    public static final int NANG_TL_LEN_HUY_DIET = 2512;
    public static final int NANG_HUY_DIET_LEN_SKH = 2513;
    public static final int NANG_HUY_DIET_LEN_SKH_VIP = 2514;
    public static final int MO_KHOA_ITEM = 2515;
    public static final int NANG_CAP_CHAN_MENH = 523;
    public static final int AN_TRANG_BI = 503;
    public static final int GIA_HAN_VAT_PHAM = 526;
//    public static final int DOI_VE_HUY_DIET = 503;
//    public static final int DAP_SET_KICH_HOAT = 504;
//    public static final int DOI_MANH_KICH_HOAT = 505;
//    public static final int DOI_CHUOI_KIEM = 506;
//    public static final int DOI_LUOI_KIEM = 507;
//    public static final int DOI_KIEM_THAN = 508;
    public static final int CHE_TAO_TRANG_BI_TS = 520;
    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int MO_CHI_SO_BONG_TAI = 517;
    public static final int LAM_PHEP_NHAP_DA = 512;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int PHAN_RA_DO_THAN_LINH = 514;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int NANG_CAP_SKH_VIP = 516;
    public static final int NANG_CAP_BONG_TAI2 = 518;
    public static final int MO_CHI_SO_BONG_TAI2 = 519;

    //khaile add
    public static final int CHE_TAO_AO_THIEN_MA = 521;
    public static final int CHE_TAO_QUAN_THIEN_MA = 522;
    public static final int CHE_TAO_GANG_THIEN_MA = 527;
    public static final int CHE_TAO_GIAY_THIEN_MA = 524;
    public static final int CHE_TAO_TRANG_SUC_THIEN_MA = 525;
    public static final int CHE_TAO_CAI_TRANG_THIEN_MA = 528;

    public static final int CHE_TAO_AO_VO_CUC_TU_TAI = 400;
    public static final int CHE_TAO_QUAN_VO_CUC_TU_TAI = 401;
    public static final int CHE_TAO_GANG_VO_CUC_TU_TAI = 402;
    public static final int CHE_TAO_GIAY_VO_CUC_TU_TAI = 403;
    public static final int CHE_TAO_TRANG_SUC_VO_CUC_TU_TAI = 404;
    // public static final int CHE_TAO_TRANG_SUC_VO_CUC_TU_TAI = 405;

    public static final int CHE_TAO_AO_LA_THIEN_TU_TAI = 406;
    public static final int CHE_TAO_QUAN_LA_THIEN_TU_TAI = 407;
    public static final int CHE_TAO_GANG_LA_THIEN_TU_TAI = 408;
    public static final int CHE_TAO_GIAY_LA_THIEN_TU_TAI = 409;
    public static final int CHE_TAO_TRANG_SUC_LA_THIEN_TU_TAI = 410;
    //  public static final int CHE_TAO_TRANG_SUC_VO_CUC_TU_TAI = 411;

    public static final int CHE_TAO_DAN_DUOC_LUYEN_KHI = 444;
    public static final int CHE_TAO_TRUC_CO_DAN = 445;

    public static final int MENU_CHE_TAO_DO_THIEN_MA = 530;

    private static final int GOLD_BONG_TAI = 500_000_000;
    private static final int GEM_BONG_TAI = 5_000;
    private static final int RUBY_BONG_TAI = 50_000;
    private static final int RATIO_BONG_TAI = 50;
    private static final int RATIO_NANG_CAP = 45;

    public static final int NANG_CAP_LINH_THU = 603;

    public static final int NANG_CAI_TRANG = 600;
    public static final int SIEU_HOA = 604;
    public static final int TINH_THACH_HOA = 605;
    public static final int NANG_GIAP_LUYEN_TAP = 606;
    private final Npc baHatMit;
    private final Npc npsthiensu64;
    //private final Npc HungVuong;
    private final Npc ThienMa;
    private final Npc TU_TAI_TIEN;

    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.npsthiensu64 = NpcManager.getNpc(ConstNpc.NPC_64);
        //this.HungVuong = NpcManager.getNpc(ConstNpc.HUNG_VUONG);
        this.ThienMa = NpcManager.getNpc(ConstNpc.THIEN_MA);
        this.TU_TAI_TIEN = NpcManager.getNpc(ConstNpc.TU_TAI_TIEN);

    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    private boolean SubThoiVang(Player pl, int quatity) {
        Iterator var3 = pl.inventory.itemsBag.iterator();

        Item item;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            item = (Item) var3.next();
        } while (!item.isNotNullItem() || item.template.id != 457 || item.quantity < quatity);

        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, quatity);
        return true;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void conditionThienMaItem(Player player) {
        // Kiểm tra nếu danh sách nguyên liệu trống
        if (player.combineNew.itemsCombine.isEmpty()) {
            this.ThienMa.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy đưa ta Thiên Mã Thạch, ta sẽ cho Đô Thiên Ma", "Đóng");
            return;
        }

        // Lọc ra danh sách Thiên Mã Thạch
        List<Item> thienMaThachList = player.combineNew.itemsCombine.stream()
                .filter(item -> item != null && item.isNotNullItem() && item.isThienMaThach())
                .toList();

        // Kiểm tra nếu danh sách trống
        if (thienMaThachList.isEmpty()) {
            this.ThienMa.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn chưa đặt Thiên Mã Thạch vào!", "Đóng");
            return;
        }

        // Lấy viên Thiên Mã Thạch đầu tiên
        Item thienMaThach = thienMaThachList.get(0);

        // Kiểm tra số lượng có đủ không
        if (thienMaThach.quantity < COST_THIEN_MA_THACH) {
            this.ThienMa.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Số lượng Thiên Mã Thạch của bạn chỉ có " + thienMaThach.quantity
                    + ", cần ít nhất " + COST_THIEN_MA_THACH + " để đổi!", "Đóng");
            return;
        }

        // Kiểm tra Ruby có đủ không
        if (player.inventory.ruby < COST_RUBY_DOI_SET_THIEN_MA) {
            this.ThienMa.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Kiếm đủ " + Util.numberToMoney(COST_RUBY_DOI_SET_THIEN_MA) + " hồng ngọc rồi quay lại đây", "Đóng");
            return;
        }

        // Kiểm tra túi đồ có đủ ô trống không
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Không đủ ô trống !");
            return;
        }

        // Hiển thị menu xác nhận
        String concac = "Bạn có chắc muốn chế tạo Đồ Thiên Ma?\nChi phí: \n"
                + "|2|- 1M Hồng Ngọc\n"
                + "|2|- 500k Thiên Ma Thạch";

        this.ThienMa.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, concac, "Chế tạo", "Từ chối");
    }

    private void createThienMaItem(Player player, short itemId, List<ItemOption> itemOptions) {
        // Kiểm tra nếu danh sách nguyên liệu trống
        if (player.combineNew.itemsCombine.isEmpty()) {
            this.ThienMa.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy đưa ta Thiên Mã Thạch, ta sẽ cho Đô Thiên Ma", "Đóng");
            return;
        }

        // Lọc ra danh sách Thiên Mã Thạch
        List<Item> thienMaThachList = player.combineNew.itemsCombine.stream()
                .filter(item -> item != null && item.isNotNullItem() && item.isThienMaThach())
                .toList();

        // Kiểm tra nếu danh sách trống
        if (thienMaThachList.isEmpty()) {
            this.ThienMa.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn chưa đặt Thiên Mã Thạch vào!", "Đóng");
            return;
        }

        // Lấy viên Thiên Mã Thạch đầu tiên
        Item thienMaThach = thienMaThachList.get(0);

        // Kiểm tra số lượng có đủ không
        if (thienMaThach.quantity < COST_THIEN_MA_THACH) {
            this.ThienMa.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Số lượng Thiên Mã Thạch của bạn chỉ có " + thienMaThach.quantity
                    + ", cần ít nhất " + COST_THIEN_MA_THACH + " để đổi!", "Đóng");
            return;
        }

        // Kiểm tra Ruby có đủ không
        if (player.inventory.ruby < COST_RUBY_DOI_SET_THIEN_MA) {
            this.ThienMa.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Kiếm đủ " + Util.numberToMoney(COST_RUBY_DOI_SET_THIEN_MA) + " hồng ngọc rồi quay lại đây", "Đóng");
            return;
        }

        // Kiểm tra túi đồ có đủ ô trống không
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Không đủ ô trống !");
            return;
        }

        // Tạo vật phẩm mới
        Item newItem = ItemService.gI().createNewItem(itemId);
        newItem.itemOptions.addAll(itemOptions); // Thêm danh sách ItemOption

        // Trừ Thiên Mã Thạch
        InventoryServiceNew.gI().subQuantityItemsBag(player, thienMaThach, COST_THIEN_MA_THACH);
        // Thêm vật phẩm vào túi
        InventoryServiceNew.gI().addItemBag(player, newItem);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private void conditionTuTaiItem(Player player) {
        if (player.combineNew.itemsCombine.isEmpty()) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy đặt đầy đủ nguyên liệu để chế tạo Vô Cực Tự Tại!", "Đóng");
            return;
        }

        int countDanDuoc = 0;
        int requiredDanDuoc = 4;
        List<Item> validItems = new ArrayList<>();
        Item linhKhi = null;
        Item thoiVang = null;

        for (Item item : player.combineNew.itemsCombine) {
            if (item != null && item.isNotNullItem()) {
                if (item.isTrucCoDan() && item.quantity >= COST_DAN_TRUC_CO_DOI_SET_TU_TAI) {
                    countDanDuoc++;
                    validItems.add(item);
                } else if (item.isLinhKhi()) {
                    linhKhi = item;
                    validItems.add(item);
                } else if (item.template.id == 457) { // Kiểm tra ID Thỏi Vàng
                    thoiVang = item;
                    validItems.add(item);
                }
            }
        }

        if (player.combineNew.itemsCombine.size() != validItems.size()) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Có vật phẩm không hợp lệ!", "Đóng");
            return;
        }

        if (countDanDuoc < requiredDanDuoc) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần đủ 4 loại Đan Dược Trúc Cơ x99 mỗi loại!", "Đóng");
            return;
        }

        if (linhKhi == null || linhKhi.quantity < COST_LINH_KHI_DOI_SET_TU_TAI) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần ít nhất 99,999 Linh Khí!", "Đóng");
            return;
        }

        if (thoiVang == null || thoiVang.quantity < COST_THOI_VANG_DOI_SET_TU_TAI) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần ít nhất 999 Thỏi Vàng!", "Đóng");
            return;
        }

        if (player.inventory.ruby < COST_RUBY_DOI_SET_TU_TAI) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần ít nhất 10 triệu Hồng Ngọc!", "Đóng");
            return;
        }

        if (player.getSession().vnd < COST_COIN_DOI_SET_TU_TAI) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần ít nhất 200,000 COIN!", "Đóng");
            return;
        }

        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Không đủ ô trống trong túi đồ!");
            return;
        }

        // Hiển thị menu xác nhận
        String concac = "Bạn có chắc muốn chế tạo Vô Cực Tự Tại?\nChi phí: \n"
                + "|2|- 4 Loại Đan Dược Trúc Cơ x99 mỗi loại\n"
                + "|2|- x99,999 Linh Khí\n"
                + "|2|- x999 Thỏi Vàng\n"
                + "|2|- 10M Hồng Ngọc\n"
                + "|2|- 200k COIN";

        this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, concac, "Chế tạo", "Từ chối");
    }

    private void createTuTaiItem(Player player, short itemId, List<ItemOption> itemOptions) {

        int countDanDuoc = 0;
        int requiredDanDuoc = 4;
        List<Item> validItems = new ArrayList<>();
        Item linhKhi = null;
        Item thoiVang = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item != null && item.isNotNullItem()) {
                if (item.isTrucCoDan() && item.quantity >= COST_DAN_TRUC_CO_DOI_SET_TU_TAI) {
                    countDanDuoc++;
                    validItems.add(item);
                } else if (item.isLinhKhi()) {
                    linhKhi = item;
                    validItems.add(item);
                } else if (item.template.id == 457) { // Kiểm tra ID Thỏi Vàng
                    thoiVang = item;
                    validItems.add(item);
                }
            }
        }
        // Trừ nguyên liệu
        thoiVang.quantity -= COST_THOI_VANG_DOI_SET_TU_TAI;
        InventoryServiceNew.gI().subQuantityItemsBag(player, linhKhi, COST_LINH_KHI_DOI_SET_TU_TAI);
        player.inventory.ruby -= COST_RUBY_DOI_SET_TU_TAI;
        PlayerDAO.subvnd(player, COST_COIN_DOI_SET_TU_TAI);

        // Trừ 4 loại đan dược
        int removedCount = 0;
        for (Item item : validItems) {
            if (item.isTrucCoDan() && item.quantity >= COST_DAN_TRUC_CO_DOI_SET_TU_TAI) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, item, COST_DAN_TRUC_CO_DOI_SET_TU_TAI);
                removedCount++;
                if (removedCount == 4) {
                    break;
                }
            }
        }

        // Tạo trang bị mới
        Item newItem = ItemService.gI().createNewItem(itemId);
        newItem.itemOptions.addAll(itemOptions);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        InventoryServiceNew.gI().sendItemBags(player);

        // Thông báo thành công
        Service.gI().sendThongBao(player, "Chúc mừng! Bạn đã chế tạo thành công Vô Cực Tự Tại!");

    }

    private void conditionDanDuocLuyenKhi(Player player) {
        if (player.combineNew.itemsCombine.isEmpty()) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy đặt đầy đủ nguyên liệu để chế tạo!", "Đóng");
            return;
        }

        Set<Short> uniqueTanDanIds = new HashSet<>();
        int countDanDuoc = 0;
        int requiredDanDuoc = 9;
        List<Item> validItems = new ArrayList<>();

        for (Item item : player.combineNew.itemsCombine) {
            if (item != null && item.isNotNullItem() && item.isTanDan() && item.quantity >= 50) {
                uniqueTanDanIds.add(item.template.id);
                countDanDuoc++;
                validItems.add(item);
            }
        }

        if (uniqueTanDanIds.size() < requiredDanDuoc) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần đủ 9 loại tàn đan x50 mỗi loại!", "Đóng");
            return;
        }

        if (validItems.size() != player.combineNew.itemsCombine.size()) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Có vật phẩm không hợp lệ hoặc số lượng không đủ!", "Đóng");
            return;
        }

        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Không đủ ô trống trong túi đồ!");
            return;
        }

        String concac = "Bạn có chắc muốn chế tạo đan dược Luyện Khí?\nChi phí: \n"
                + "|2|- 9 Loại tàn đan x50 mỗi loại\n";
        this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, concac, "Chế tạo", "Từ chối");
    }

    private void createDanDuocLuyenKhi(Player player, short itemId) {

        int countDanDuoc = 0;
        List<Item> validItems = new ArrayList<>();
        for (Item item : player.combineNew.itemsCombine) {
            if (item != null && item.isNotNullItem()) {
                if (item.isTanDan() && item.quantity >= 50) {
                    countDanDuoc++;
                    validItems.add(item);
                }

            }
        }

        // Trừ 4 loại đan dược
        int removedCount = 0;
        for (Item item : validItems) {
            if (item.isTanDan() && item.quantity >= 50) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 50);
                removedCount++;
                if (removedCount == 9) {
                    break;
                }
            }
        }

        // Tạo trang bị mới
        Item newItem = ItemService.gI().createNewItem(itemId);
        InventoryServiceNew.gI().addItemBag(player, newItem);
        InventoryServiceNew.gI().sendItemBags(player);

        // Thông báo thành công
        Service.gI().sendThongBao(player, "Chúc mừng! Bạn đã chế tạo đan dược thành công!");

    }

    private void conditionTrucCoDan(Player player) {
        if (player.combineNew.itemsCombine.isEmpty()) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Hãy đặt đầy đủ nguyên liệu để chế tạo!", "Đóng");
            return;
        }

        Set<Short> uniqueTanDanIds = new HashSet<>();
        boolean hasTrucCoDanPhuong = false;
        int countDanDuoc = 0;
        int requiredDanDuoc = 1;
        List<Item> validItems = new ArrayList<>();

        for (Item item : player.combineNew.itemsCombine) {
            if (item != null && item.isNotNullItem()) {
                // Kiểm tra Hoàng Cực Đan (id = 1664)
                if (item.template.id == 1664 && item.quantity >= 100) {
                    uniqueTanDanIds.add(item.template.id);
                    countDanDuoc++;
                    validItems.add(item);
                }
                // Kiểm tra Trúc Cơ Đan Phương (giả sử id = 2000)
                if (item.template.id == 1708) {
                    hasTrucCoDanPhuong = true;
                    validItems.add(item);
                }
            }
        }

        if (uniqueTanDanIds.size() < requiredDanDuoc) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần đủ Hoàng Cực Đan x100!", "Đóng");
            return;
        }

        if (!hasTrucCoDanPhuong) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Bạn cần có Trúc Cơ Đan Phương!", "Đóng");
            return;
        }

        if (validItems.size() != player.combineNew.itemsCombine.size()) {
            this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Có vật phẩm không hợp lệ hoặc số lượng không đủ!", "Đóng");
            return;
        }

        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Không đủ ô trống trong túi đồ!");
            return;
        }

        String concac = "Bạn có chắc muốn chế tạo Trúc Cơ Đan?\nChi phí: \n"
                + "|2|- Hoàng Cực Đan x100\n"
                + "|2|- Trúc Cơ Đan Phương\n"
                + "|2|- Tỉ lệ 25%\n";

        this.TU_TAI_TIEN.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, concac, "Chế tạo", "Từ chối");
    }

    private void createTrucCoDan(Player player, short itemId) {
        List<Item> hoangCucDanItems = new ArrayList<>();
        Item trucCoDanPhuongItem = null;

        // Đếm số lượng Hoàng Cực Đan và tìm Trúc Cơ Đan Phương
        int countHoangCucDan = 0;
        for (Item item : player.combineNew.itemsCombine) {
            if (item != null && item.isNotNullItem()) {
                if (item.template.id == 1664 && item.quantity >= 1) { // Hoàng Cực Đan
                    countHoangCucDan += item.quantity;
                    hoangCucDanItems.add(item);
                }
                if (item.template.id == 1708) { // Trúc Cơ Đan Phương
                    trucCoDanPhuongItem = item;
                }
            }
        }

        // Kiểm tra điều kiện đủ nguyên liệu
        if (countHoangCucDan < 100) {
            Service.gI().sendThongBao(player, "Bạn cần ít nhất 100 Hoàng Cực Đan!");
            return;
        }
        if (trucCoDanPhuongItem == null) {
            Service.gI().sendThongBao(player, "Bạn cần có Trúc Cơ Đan Phương!");
            return;
        }

        // Trừ Hoàng Cực Đan (100 viên)
        int removedCount = 0;
        for (Item item : hoangCucDanItems) {
            int toRemove = Math.min(item.quantity, 100 - removedCount);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, toRemove);
            removedCount += toRemove;
            if (removedCount >= 100) {
                break;
            }
        }

        // Trừ Trúc Cơ Đan Phương
        InventoryServiceNew.gI().subQuantityItemsBag(player, trucCoDanPhuongItem, 1);

        // **Cập nhật lại túi đồ trước khi xác định thành công/thất bại**
        InventoryServiceNew.gI().sendItemBags(player);

        // Xác suất thành công 25%
        if (new Random().nextInt(100) < 25) { // 25% tỷ lệ thành công
            // Tạo vật phẩm mới (Trúc Cơ Đan)
            sendEffectSuccessCombine(player);
            Item newItem = ItemService.gI().createNewItem(itemId);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Chúc mừng! Bạn đã chế tạo Trúc Cơ Đan thành công!");
        } else {
            sendEffectFailCombine(player);
            Service.gI().sendThongBao(player, "Thất bại! Bạn đã không chế tạo được Trúc Cơ Đan.");
        }
    }

    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }

        switch (player.combineNew.typeCombine) {
            case CHE_TAO_AO_THIEN_MA:
                conditionThienMaItem(player);
                break;
            case CHE_TAO_QUAN_THIEN_MA:
                conditionThienMaItem(player);
                break;
            case CHE_TAO_GANG_THIEN_MA:
                conditionThienMaItem(player);
                break;
            case CHE_TAO_GIAY_THIEN_MA:
                conditionThienMaItem(player);
                break;
            case CHE_TAO_TRANG_SUC_THIEN_MA:
                conditionThienMaItem(player);
                break;
            case CHE_TAO_CAI_TRANG_THIEN_MA:
                conditionThienMaItem(player);
                break;
            case CHE_TAO_AO_VO_CUC_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_QUAN_VO_CUC_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_GANG_VO_CUC_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_GIAY_VO_CUC_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_TRANG_SUC_VO_CUC_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_AO_LA_THIEN_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_QUAN_LA_THIEN_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_GANG_LA_THIEN_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_GIAY_LA_THIEN_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_TRANG_SUC_LA_THIEN_TU_TAI:
                conditionTuTaiItem(player);
                break;
            case CHE_TAO_DAN_DUOC_LUYEN_KHI:
                conditionDanDuocLuyenKhi(player);
                break;
            case CHE_TAO_TRUC_CO_DAN:
                conditionTrucCoDan(player);
                break;
            case CHE_TAO_TRANG_BI_TS:
                if (player.combineNew.itemsCombine.size()
                        == 0) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Tuandz", "Yes");
                    return;
                }

                if (player.combineNew.itemsCombine.size()
                        >= 2 && player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
//                        return;
//                    }
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
//                        return;
//                    }
                    Item mTS = null, daNC = null, daMM = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;
                    int tilenew = tilemacdinh;
//                    if (daNC != null) {
//                        tilenew += (daNC.template.id - 1073) * 10;                     
//                    }

                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " Thiên sứ "
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|7|Mảnh ghép " + mTS.quantity + "/999\n";
                    if (daNC != null) {
                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap()
                                + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 5000 hồng ngọc";
                    if (player.inventory.ruby < 5000) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n5000 hồng ngọc", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;

            case AN_TRANG_BI:
                if (InventoryServiceNew.gI()
                        .getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        Item dangusac = player.combineNew.itemsCombine.get(1);
                        if (isTrangBiAn(item)) {
                            if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && (dangusac.template.id == 1314 || dangusac.template.id == 1315 || dangusac.template.id == 1316) && dangusac.quantity >= 99) {
                                String npcSay = item.template.name + "\n|2|";
                                for (Item.ItemOption io : item.itemOptions) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                                npcSay += "|1|Con có muốn biến trang bị " + item.template.name + " thành\n"
                                        + "trang bị Ấn không?\b|4|Đục là lên\n"
                                        + "|7|Cần 99 " + dangusac.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa bỏ đủ vật phẩm !!!", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể hóa ấn", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case GIA_HAN_VAT_PHAM:
                if (player.combineNew.itemsCombine.size()
                        == 2) {
                    Item thegh = null;
                    Item itemGiahan = null;
                    for (Item item_ : player.combineNew.itemsCombine) {
                        if (item_.template.id == 1317) {
                            thegh = item_;
                        } else if (item_.isTrangBiHSD()) {
                            itemGiahan = item_;
                        }
                    }
                    if (thegh == null) {
                        Service.gI().sendThongBaoOK(player, "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                        return;
                    }
                    if (itemGiahan == null) {
                        Service.gI().sendThongBaoOK(player, "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                        return;
                    }
                    for (ItemOption itopt : itemGiahan.itemOptions) {
                        if (itopt.optionTemplate.id != 93) {
                            Service.gI().sendThongBaoOK(player, "Trang bị này không phải trang bị có Hạn Sử Dụng");
                            return;

                        }
                    }
                    String npcSay = "Trang bị được gia hạn \"" + itemGiahan.template.name + "\"";
                    npcSay += itemGiahan.template.name + "\n|2|";
                    for (Item.ItemOption io : itemGiahan.itemOptions) {
                        npcSay += io.getOptionString() + "\n";
                    }
                    npcSay += "\n|0|Sau khi gia hạn +7 ngày\n";

                    npcSay += "|0|Tỉ lệ thành công: 100%" + "\n";
                    if (player.inventory.ruby > 1000) {
                        npcSay += "|2|Cần 1000 hồng ngọc";
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Gia hạn", "Từ chối");

                    } else if (player.inventory.ruby < 1000) {
                        int SoVangThieu2 = (int) (1000 - player.inventory.ruby);
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu " + SoVangThieu2 + " Hong Ngoc");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống");
                }
                break;

            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size()
                        == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;

                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê

                    if (trangBi != null && daPhaLe != null) {

                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.rubyCombine = getGemEpSao(star);
                            player.combineNew.countThoiVang = getCountThoiVang(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Mất " + Util.numberToMoney(getGemEpSao(star)) + " Hồng Ngọc\n"
                                    + "Mất " + Util.numberToMoney(getCountThoiVang(star)) + " Thỏi Vàng\n";

                            if (player.inventory.ruby >= getGemEpSao(star)) {
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(getGemEpSao(star) - player.inventory.ruby) + "Hồng Ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào ", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào ", "Đóng");

                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size()
                        == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.rubyCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }

                            npcSay += "|1|Chọn đồ - Bấm vào đồ - Lấy sao số đẹp\n"
                                    + "Mất " + Util.numberToMoney(getGoldPhaLeHoa(star)) + " Vàng\n|1|"
                                    + "Mất " + Util.numberToMoney(getGemPhaLeHoa(star)) + "Hồng Ngọc\n"
                                    //                                    + "Mất" + Util.numberToMoney(getCountThoiVang(star)) + " Thỏi Vàng\n"
                                    + "|3|Tỉ lệ thành công: " + getRatioPhaLeHoa(star) + "%" + "\n";

                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng mỗi lần";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n" + player.combineNew.rubyCombine + " ngọc\n" + "1 lần",
                                        "Nâng cấp\n" + (player.combineNew.rubyCombine * 10) + " ngọc\n" + "10 Lần",
                                        "Nâng cấp\n" + (player.combineNew.rubyCombine * 100) + " ngọc\n" + "100 lần");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;

            case NANG_CAP_LINH_THU:
                if (InventoryServiceNew.gI()
                        .getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {

                        Item bnp = player.combineNew.itemsCombine.get(0);
                        Item item = player.combineNew.itemsCombine.get(1);
                        if (bnp != null && bnp.isNotNullItem() && (bnp.template.type == 72)) {
                            if (item != null && item.isNotNullItem() && (item.template.id == 674)) {
                                if (item.quantity >= 1 && bnp.quantity >= 1) {
                                    int sl = 1000;

                                    String npcSay = "|2|Con có muốn biến " + item.template.name + " thành\n"
                                            + "1 Linh thú cấp thần\n"
                                            + "\n|7|Cần x1 \n" + item.template.name
                                            + "\n|7|Làm phép 1 lần sẽ mất thêm " + sl + " thỏi vàng";

                                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");

//                                    }
                                } else {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần Linh Thú và Đá ngũ sắc", "Đóng");
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Đá ngũ sắc đâu con", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Linh thú đâu con", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần Linh Thú, Đá ngũ sắc", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CAP_CHAN_MENH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    int star = 0;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id >= 1185 && item.template.id <= 1193) {
                            bongTai = item;
                        } else if (item.template.id == 1318) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && bongTai.template.id == 1193) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chân Mệnh đã đạt cấp tối đa", "Đóng");
                        return;
                    }
                    player.combineNew.DiemNangcap = getDiemNangcapChanmenh(star);
                    player.combineNew.DaNangcap = getDaNangcapChanmenh(star);
                    player.combineNew.TileNangcap = getTiLeNangcapChanmenh(star);
                    if (bongTai != null && manhVo != null && (bongTai.template.id >= 1185 && bongTai.template.id <= 1193)) {
                        String npcSay = bongTai.template.name + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.TileNangcap + "%" + "\n";
                        if (player.combineNew.DiemNangcap <= player.event.getEventPointBoss()) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.DiemNangcap) + " Điểm Săn Boss";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.DaNangcap + " Đá Hoàng Kim");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.DiemNangcap - player.event.getEventPointBoss()) + " Điểm";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Chân Mệnh và Đá Hoàng Kim", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Chân Mệnh và Đá Hoàng Kim", "Đóng");
                }
                break;
            case LAM_PHEP_NHAP_DA:
                if (InventoryServiceNew.gI()
                        .getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {

                        Item item = player.combineNew.itemsCombine.get(0);
                        Item bnp = player.combineNew.itemsCombine.get(1);

                        if (bnp != null && bnp.isNotNullItem() && (bnp.template.id == 226)) {
                            if (item != null && item.isNotNullItem() && (item.template.id == 225)) {
                                if (item.quantity >= 99 && bnp.quantity >= 1) {
                                    int sl = 1;

                                    String npcSay = "|2|Con có muốn biến x99 " + item.template.name + " thành\n"
                                            + "1 đá nâng cấp ngẫu nhiên\n"
                                            + "\n|7|Cần x99 \n" + item.template.name
                                            + "\n|7|Làm phép 1 lần sẽ mất thêm " + sl + " thỏi vàng";

                                    baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");

                                } else {
                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ mảnh đá vụn rồi", "Đóng");
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Mảnh đá vụn đâu con", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bình nước phép đâu con", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bình nước phép và 99 mảnh đá vụn", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;

            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI()
                        .getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {

                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id > 16 && item.template.id <= 20)) {
                            int subthoiVang_ = (20 - item.template.id);
                            if (item.quantity >= 7) {
                                String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                        + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                        + "\n|7|Cần 7 \n" + item.template.name
                                        + "\n|7|Làm phép 1 lần sẽ mất thêm " + subthoiVang_ + " thỏi vàng";

                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");

                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Có " + (7 - item.quantity) + " viên mà cũng đòi ép", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Đặt Ngọc rồng vào mà ép con ", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần đặt ngọc rồng vô để nhập", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;

            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size()
                        >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }

                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        if (itemDo == null || itemDNC == null) {
                            Service.gI().sendThongBao(player, "Có lỗi xảy ra với đồ của bạn, vui lòng báo admin");
                            return;
                        }
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            int paramsub = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23
                                        || io.optionTemplate.id == 196
                                        || io.optionTemplate.id == 197
                                        || io.optionTemplate.id == 198
                                        || io.optionTemplate.id == 199) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    paramsub = io.param - (io.param * 10 / 100);
                                    break;
                                }
                            }
                            if (option == null || option.isEmpty()) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Không hỗ trợ item này");

                                return;
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6 || level == 8) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")\n|7|"
                                        + option.replaceAll("#", String.valueOf(paramsub));
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && itemDBV != null && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
            case DAP_DO_AO_HOA:
                if (player.combineNew.itemsCombine.size()
                        >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ ảo hóa (áo, quần, găng, giày, rada)", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá quý (pha lê đỏ, pha lê xanh, pha lê hồng, pha lê tím)", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (itemDo != null) {
                        boolean check1 = false;
                        boolean check2 = false;
                        if (InventoryServiceNew.gI().haveOption(itemDo, 102)) {
                            check1 = true;
                        }
                        if (InventoryServiceNew.gI().haveOption(itemDo, 107)) {
                            check2 = true;
                        }
                        if (!check1) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần đồ đã pha lê hóa 5 sao trở lên");
                            return;
                        }
                        if (!check2) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần đồ đã ép 5 sao pha lê trở lên");
                            return;
                        }
                        for (ItemOption iop : itemDo.itemOptions) {
                            if (iop != null) {
                                if (iop.optionTemplate.id == 102) {
                                    if (iop.param < 5) {
                                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Cần đồ đã pha lê hóa 5 sao trở lên");
                                        return;
                                    }
                                }
                                if (iop.optionTemplate.id == 107) {
                                    if (iop.param < 5) {
                                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Cần đồ đã ép 5 sao pha lê trở lên");
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck2(itemDo, itemDNC)) {
                        int level = 0;
                        if (itemDo == null || itemDNC == null) {
                            Service.gI().sendThongBao(player, "Có lỗi xảy ra với đồ của bạn, vui lòng báo admin");
                            return;
                        }
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 206) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM2) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaQuy = getCountDaQuy(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 206) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            int paramsub = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 5
                                        || io.optionTemplate.id == 101
                                        || io.optionTemplate.id == 50
                                        || io.optionTemplate.id == 77
                                        || io.optionTemplate.id == 103) {
                                    option = io.optionTemplate.name;
                                    param = io.param + Math.max(1, io.param * 10 / 100 - 2);
                                    paramsub = io.param - Math.max(1, io.param * 10 / 100);
                                    break;
                                }
                            }
                            if (option == null || option.isEmpty()) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Không hỗ trợ item này");

                                return;
                            }
                            npcSay += "|2|Sau khi ảo hóa trang bị (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaQuy > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaQuy + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6 || level == 8) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")\n|7|"
                                        + option.replaceAll("#", String.valueOf(paramsub));
                            }
                            if (player.combineNew.countDaQuy > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaQuy - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && itemDBV != null && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng quý phù hợp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng quý phù hợp", "Đóng");
                }
                break;
            case NANG_GIAP_LUYEN_TAP:
                if (player.combineNew.itemsCombine.size()
                        >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 32).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu giáp luyện tập", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá hổ phách", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá bảo vệ", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type == 32) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (itemDo != null) {
                        boolean check1 = false;
                        boolean check2 = false;
                        if (InventoryServiceNew.gI().haveOption(itemDo, 102)) {
                            check1 = true;
                        }
                        if (InventoryServiceNew.gI().haveOption(itemDo, 107)) {
                            check2 = true;
                        }
                        if (!check1) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần giáp luyện tập đã pha lê hóa 5 sao trở lên");
                            return;
                        }
                        if (!check2) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần giáp luyện tập đã ép 5 sao pha lê trở lên");
                            return;
                        }
                        for (ItemOption iop : itemDo.itemOptions) {
                            if (iop != null) {
                                if (iop.optionTemplate.id == 102) {
                                    if (iop.param < 5) {
                                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Cần giáp luyện tập đã pha lê hóa 5 sao trở lên");
                                        return;
                                    }
                                }
                                if (iop.optionTemplate.id == 107) {
                                    if (iop.param < 5) {
                                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Cần giáp luyện tập đã ép 5 sao pha lê trở lên");
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck3(itemDo, itemDNC)) {
                        int level = 0;
                        if (itemDo == null || itemDNC == null) {
                            Service.gI().sendThongBao(player, "Có lỗi xảy ra với đồ của bạn, vui lòng báo admin");
                            return;
                        }
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM2) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaQuy = getCountDaQuy(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            int paramsub = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 2
                                        || io.optionTemplate.id == 8
                                        || io.optionTemplate.id == 19
                                        || io.optionTemplate.id == 27
                                        || io.optionTemplate.id == 28
                                        || io.optionTemplate.id == 16
                                        || (io.optionTemplate.id >= 94
                                        && io.optionTemplate.id <= 101)
                                        || io.optionTemplate.id == 108
                                        || io.optionTemplate.id == 109
                                        || io.optionTemplate.id == 114
                                        || io.optionTemplate.id == 117
                                        || io.optionTemplate.id == 153
                                        || io.optionTemplate.id == 156
                                        || io.optionTemplate.id == 3) {
                                    option = io.optionTemplate.name;
                                    param = io.param + 1;
                                    paramsub = io.param - 1;
                                    break;
                                }
                            }

                            npcSay += "|2|Sau khi nâng giáp luyện tập (+" + (level + 1) + ")\n|7|"
                                    + (option != null ? option.replaceAll("#", String.valueOf(param)) : "Ngẫu nhiên nhận 1 chỉ số đặc biệt" + "\n")
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaQuy > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaQuy + " " + itemDNC.template.name + "\n"
                                    + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6 || level == 8) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")\n|7|"
                                        + (option != null ? option.replaceAll("#", String.valueOf(paramsub)) : "Chưa có chỉ số đặc biệt nào" + "\n");
                            }
                            if (player.combineNew.countDaQuy > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaQuy - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && itemDBV != null && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Giáp luyện tập của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 giáp luyện tập và đá hổ phách", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 giáp luyện tập và đá hổ phách", "Đóng");
                }
                break;
            case TAY_PS_HOA_TRANG_BI:
                if (InventoryServiceNew.gI()
                        .getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daHacHoa = null;
                        Item itemHacHoa = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.template.id == 2188) {
                                daHacHoa = item_;
                            } else if (item_.isTrangBiHacHoa()) {
                                itemHacHoa = item_;
                            }
                        }
                        if (daHacHoa == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu ngọc tẩy", "Đóng");
                            return;
                        }
                        if (itemHacHoa == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần trang bị có sao pha lê hoặc chỉ số đặc biệt\n[áo , quần , găng, giày, rada, pet, linh thú, vpdl, glt,....]", "Đóng");
                            return;
                        }

                        String npcSay = "|2|Hiện tại " + itemHacHoa.template.name + "\n|0|";
                        for (Item.ItemOption io : itemHacHoa.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }

                        npcSay += ((itemHacHoa.template.id > 1185 && itemHacHoa.template.id <= 1193) ? "|2|Tẩy hạ về chân mệnh cấp 1 và mất hết chỉ số\n|7|" : "|2|Sau khi tẩy sẽ xoá hết các chỉ số phụ và đặc biệt của đồ \n|7|")
                                + "\n|7|Tỉ lệ thành công: " + 100 + "%\n"
                                + "Cần " + Util.numberToMoney(20000) + " hồng ngọc";

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Tẩy trang bị\n" + Util.numberToMoney(20000) + " hồng ngọc", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có trang bị và ngọc tẩy", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;

            case MO_KHOA_ITEM:
                if (InventoryServiceNew.gI()
                        .getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item dahoangkim = null;
                        Item itemkhoagd = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            System.out.println("Item type: " + item_.template.type);
                            if (item_.template.id == 1318) {
                                dahoangkim = item_;
                            } else if (item_.isTrangBiKhoaGd()) {
                                itemkhoagd = item_;
                            }
                        }

                        if (dahoangkim == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có Đá Hoàng Kim", "Đóng");
                            return;
                        }
                        if (itemkhoagd == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có Item bị khóa giao dịch3", "Đóng");
                            return;
                        }

                        String npcSay = "|2|Hiện tại " + itemkhoagd.template.name + "\n|0|";
                        for (Item.ItemOption io : itemkhoagd.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|2|Sau khi mở khóa Item của bạn sẽ thành Item gd được \n|7|"
                                + "\n|7|Tỉ lệ thành công: " + 30 + "%\n"
                                + "Cần " + Util.numberToMoney(2000) + " hồng ngọc";

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Mở Khóa\n" + Util.numberToMoney(2000) + " hồng ngọc", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có Item bị khóa gd và Đá Hoàng Kim", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;
            case PS_HOA_TRANG_BI:
                if (InventoryServiceNew.gI()
                        .getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daHacHoa = null;
                        Item itemHacHoa = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.template.id == 2187) {
                                daHacHoa = item_;
                            } else if (item_.isTrangBiHacHoa()) {
                                itemHacHoa = item_;
                            }
                        }
                        if (daHacHoa == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu đá pháp sư", "Đóng");
                            return;
                        }
                        if (itemHacHoa == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu trang bị", "Đóng");
                            return;
                        }
                        if (itemHacHoa != null) {
                            for (ItemOption itopt : itemHacHoa.itemOptions) {
                                if (itopt.optionTemplate.id == 220) {
                                    if (itopt.param >= 8) {
                                        Service.gI().sendThongBao(player, "Trang bị đã đạt tới giới hạn pháp sư");
                                        return;
                                    }
                                }
                            }
                        }
                        String npcSay = "|2|Hiện tại " + itemHacHoa.template.name + "\n|0|";
                        for (Item.ItemOption io : itemHacHoa.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        player.combineNew.ratioCombine = 30;
                        npcSay += "|2|Sau khi nâng cấp sẽ cộng 1 chỉ số pháp sư ngẫu nhiên \n|7|"
                                + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                + "Cần " + Util.numberToMoney(2000000000) + " vàng";

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Nâng cấp\n" + Util.numberToMoney(2000000000) + " vàng", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có trang bị có thể pháp sư và đá pháp sư", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;
            case NANG_TL_LEN_HUY_DIET:
                if (player.combineNew.itemsCombine.size()
                        == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món thần linh, ta sẽ cho 1 món huỷ diệt tương ứng", "Đóng");
                    return;
                }

                if (player.combineNew.itemsCombine.size()
                        == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh rồi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get().typeName() + " Huỷ diệt tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(50000) + " Ngọc hồng";

                    if (player.inventory.ruby < 50000) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(50000) + " Ngọc hồng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_HUY_DIET_LEN_SKH_VIP:
                if (player.combineNew.itemsCombine.size()
                        == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 3 món huỷ diệt....", "Đóng");
                    return;
                }

                if (player.combineNew.itemsCombine.size()
                        == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ huỷ điệt rồi", "Đóng");
                        return;
                    }
                    Item item1 = player.combineNew.itemsCombine.get(0);
                    Item item2 = player.combineNew.itemsCombine.get(1);
                    Item item3 = player.combineNew.itemsCombine.get(2);

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(50000) + " hồng ngọc";

                    if (player.inventory.ruby < 50000) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(50000) + " hồng ngọc", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_HUY_DIET_LEN_SKH:
                if (player.combineNew.itemsCombine.size()
                        == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món huỷ diệt, ta sẽ cho 1 món huỷ diệt tương ứng", "Đóng");
                    return;
                }

                if (player.combineNew.itemsCombine.size()
                        == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ huỷ diệt rồi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get().typeName() + " kích hoạt tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(500000000) + " vàng";

                    if (player.inventory.gold < 500000000) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(500000000) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case PHAN_RA_DO_THAN_LINH:
                if (player.combineNew.itemsCombine.size()
                        == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa ta đồ thần linh để phân rã", "Đóng");
                    return;
                }

                if (player.combineNew.itemsCombine.size() == 1) {
                    List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
                    int couponAdd = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 555 && item.template.id <= 567) {
                            couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
                        }
                    }
                    if (couponAdd == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã đồ thần linh thôi", "Đóng");
                        return;
                    }
                    String npcSay = "Sau khi phân rã vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : " + couponAdd + " Điểm\n"
                            + "Bạn sẽ nhận được : " + 1 + " Đá Ngũ Sắc\n"
                            + (5000 > player.inventory.ruby ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(5000) + " hồng ngọc";

                    if (player.inventory.ruby < 5000) {
                        this.baHatMit.npcChat(player, "Hết tiền rồi\nNghèo ít thôi con");

                    } else {
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Phân rã", "Từ chối");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể phân rã 1 lần 1 món đồ thần linh", "Đóng");
                }
                break;
            case NANG_CAP_DO_TS:
                if (player.combineNew.itemsCombine.size()
                        == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 món Hủy Diệt bất kì và 1 món Thần Linh cùng loại", "Đóng");
                    return;
                }

                if (player.combineNew.itemsCombine.size()
                        == 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh thiên sứ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " thiên sứ tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(RUBY) + " hồng ngọc";

                    if (player.inventory.ruby < RUBY) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(RUBY) + " hồng ngọc", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size()
                        == 0) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món thiên sứ và 2 món SKH ngẫu nhiên", "Đóng");
                    return;
                }

                if (player.combineNew.itemsCombine.size()
                        == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thiên sứ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() < 2) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ kích hoạt ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(RUBY) + " hồng ngọc";

                    if (player.inventory.ruby < RUBY) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Kiếm đủ 50K hồng ngọc rồi quay lại đây", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(RUBY) + " hồng ngọc", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI:// Nâng cấp btc2
                if (player.combineNew.itemsCombine.size()
                        == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    int[] idBongTai = {454, 921, 2064, 2113};
                    int[] idManhVo = {933, 933, 2227}; // Đảm bảo các idManhVo tương ứng với idBongTai
                    int[] slManhVo = {5999, 9999, 19999}; // Đảm bảo số lượng mảnh vỡ tương ứng với idManhVo
                    for (Item item : player.combineNew.itemsCombine) {
                        if (Arrays.stream(idBongTai).anyMatch(id -> id == item.template.id)) {
                            bongTai = item;
                        } else if (Arrays.stream(idManhVo).anyMatch(id -> id == item.template.id)) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null) {
                        int indexBongTai = -1;
                        for (int i = 0; i < idBongTai.length; i++) {
                            if (bongTai.template.id == idBongTai[i]) {
                                indexBongTai = i;
                                break;
                            }
                        }
                        if (indexBongTai == -1) {
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bông tai không hợp lệ.", "Đóng");
                            break;
                        }
                        if (bongTai.template.id == idBongTai[3]) {
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Đã max level", "Đóng");
                            break;
                        }
                        if (bongTai.template.id == idBongTai[2] && manhVo.template.id == idManhVo[1]) {
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần x" + slManhVo[indexBongTai] + " " + ItemService.gI().getTemplate(idManhVo[indexBongTai]).name,
                                    "Đóng");
                            break;
                        }

                        int sl = 100 + 100 * indexBongTai;
                        long gold = cn.vangNangPotara * (indexBongTai + 1);
                        int gem = cn.gemNangPotara * (indexBongTai + 1);
                        player.combineNew.goldCombine = gold;
                        player.combineNew.gemCombine = gem;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        if (manhVo.quantity >= slManhVo[indexBongTai]) {
                            String npcSay = "Bông tai " + ItemService.gI().getTemplate(idBongTai[indexBongTai + 1]).name + "\n|2|";
                            for (Item.ItemOption io : bongTai.itemOptions) {
                                npcSay += io.getOptionString() + "\n";
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n"
                                    + "|1|Cần " + Util.numberToMoney(gold) + " Vàng"
                                    + "\n" + Util.numberToMoney(gem) + " Ngọc Xanh"
                                    + "\n" + sl + " Thỏi vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp");
                        } else {
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần x" + slManhVo[indexBongTai] + " " + ItemService.gI().getTemplate(idManhVo[indexBongTai]).name,
                                    "Đóng");
                        }
                    } else {
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 bông tai và số lượng mảnh vỡ tương ứng.",
                                "Đóng");
                    }
                } else {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 bông tai và số lượng mảnh vỡ tương ứng.",
                            "Đóng");
                }
                break;

            case MO_CHI_SO_BONG_TAI:// mở chỉ số bông tai cấp 2
                if (player.combineNew.itemsCombine.size()
                        == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    int[] idBongTai = {454, 921, 2064, 2113};
                    int[] idManhHon = {934, 934, 934, 934};
                    int[] idDaXanhLam = {935, 935, 935, 935};
                    int[] slManhHon = {39, 49, 69, 99};
                    int[] slDaXanhLam = {10, 19, 29, 99};

                    for (Item item : player.combineNew.itemsCombine) {
                        if (Arrays.stream(idBongTai).anyMatch(id -> id == item.template.id)) {
                            bongTai = item;
                        } else if (Arrays.stream(idManhHon).anyMatch(id -> id == item.template.id)) {
                            manhHon = item;
                        } else if (Arrays.stream(idDaXanhLam).anyMatch(id -> id == item.template.id)) {
                            daXanhLam = item;
                        }
                    }

                    if (bongTai != null && manhHon != null && daXanhLam != null) {
                        int indexBongTai = -1;
                        for (int i = 0; i < idBongTai.length; i++) {
                            if (bongTai.template.id == idBongTai[i]) {
                                indexBongTai = i;
                                break;
                            }
                        }

                        if (indexBongTai != -1 && manhHon.quantity >= slManhHon[indexBongTai] && daXanhLam.quantity >= slDaXanhLam[indexBongTai]) {
                            int sl = 30 + 30 * indexBongTai;
                            long gold = cn.goldOpenPotara;
                            int gem = cn.gemOpenPotara;
                            player.combineNew.goldCombine = gold;
                            player.combineNew.gemCombine = gem;
                            player.combineNew.ratioCombine = RATIO_NANG_CAP;

                            String npcSay = "Gốc: " + ItemService.gI().getTemplate(idBongTai[indexBongTai]).name + " " + "\n|2|";
                            for (Item.ItemOption io : bongTai.itemOptions) {
                                npcSay += io.getOptionString() + "\n";
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            npcSay += "|6|Cần x1 " + ItemService.gI().getTemplate(idBongTai[indexBongTai]).name + " " + "\n";
                            npcSay += "|6|Cần x" + slManhHon[indexBongTai] + " " + ItemService.gI().getTemplate(idManhHon[indexBongTai]).name + " " + "\n";
                            npcSay += "|6|Cần x" + slDaXanhLam[indexBongTai] + " " + ItemService.gI().getTemplate(idDaXanhLam[indexBongTai]).name + " " + "\n";
                            npcSay += "|5| Ngươi có muốn mở chỉ số mất " + sl + " thỏi vàng " + "\n";
                            npcSay += "|1|Cần " + Util.numberToMoney(gem) + " Ngọc xanh\n" + Util.numberToMoney(gold) + " Vàng";

                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Nâng cấp");
                        } else {
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần x" + slManhHon[indexBongTai] + " " + ItemService.gI().getTemplate(idManhHon[indexBongTai]).name + ", x" + slDaXanhLam[indexBongTai] + " " + ItemService.gI().getTemplate(idDaXanhLam[indexBongTai]).name, "Đóng");
                        }
                    } else {
                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 bông tai, mảnh hồn bông tai và đá xanh lam",
                                "Đóng");
                    }
                } else {
                    baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 bông tai, mảnh hồn bông tai và đá xanh lam",
                            "Đóng");
                }
                break;

//            case NANG_CAI_TRANG://nangcaitrang
//                if (player.combineNew.itemsCombine.size()
//                        == 4 || player.combineNew.itemsCombine.size() == 5) {
//                    Item nr1s = null;
//                    Item duahau = null;
//                    Item thoivang = null;
//                    Item dabaove = null;
//                    Item caitrang = null;
//                    for (Item it : player.combineNew.itemsCombine) {
//                        if (it.template.id == 14) {
//                            nr1s = it;
//                        } else if (it.template.id == 457) {
//                            thoivang = it;
//                        } else if (it.template.id == 569) {
//                            duahau = it;
//                        } else if (it.template.id == 987) {
//                            dabaove = it;
//                        } else if (it.template.type == 5) {
//                            caitrang = it;
//                        }
//                    }
//                    if (caitrang != null && dabaove != null && nr1s != null && thoivang != null && thoivang.quantity >= 5 && duahau != null && duahau.quantity >= 1) {
//
//                        player.combineNew.goldCombine = GOLD_BONG_TAI;
//                        player.combineNew.gemCombine = GEM_BONG_TAI;
//                        player.combineNew.ratioCombine = RATIO_BONG_TAI;
//
//                        String npcSay = "Ngươi muốn nâng cấp cải trang?\n|6|Ngẫu nhiên chỉ số ~ 15%-32%" + "\n|2|";
//                        for (Item.ItemOption io : nr1s.itemOptions) {
//                            npcSay += io.getOptionString() + "\n";
//                        }
//                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
//                        if (player.combineNew.goldCombine <= player.inventory.gold) {
//                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
//                            HungVuong.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
//                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
//                        } else {
//                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
//                            HungVuong.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
//                        }
//                    } else {
//                        this.HungVuong.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Cần 1 cải trang, 1 viên 1 sao, 5 Thỏi Vàng và x1 Dưa Hấu", "Đóng");
//                    }
//                } else {
//                    this.HungVuong.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                            "Cần 1 cải trang, 1 viên 1 sao, 5 Thỏi Vàng và x1 Dưa Hấu", "Đóng");
//                }
//
//                break;
//            case SIEU_HOA://nangcaitrang
//                if (player.combineNew.itemsCombine.size()
//                        == 2) {
//                    Item caiTrang = null;
//                    Item manhVo = null;
//                    int star = 0;
//                    for (Item item : player.combineNew.itemsCombine) {
//                        if (item.template.type == 5) {
//                            caiTrang = item;
//                        } else if (item.template.id == 1466) {
//                            manhVo = item;
//                        }
//                    }
//                    if (caiTrang != null) {
//                        for (Item.ItemOption io2 : caiTrang.itemOptions) {
//                            if (io2.optionTemplate.id == 225) {
//                                if (io2.param >= 10) {
//                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                            "Cải Trang đã siêu hóa cấp tối đa", "Đóng");
//                                    return;
//                                }
//                                star = io2.param;
//                                break;
//                            }
//                        }
//
//                    }
//                    player.combineNew.DiemNangcap = getDiemNangcapSieuHoa(star);
//                    player.combineNew.DaNangcap = getDaNangcapSieuHoa(star);
//                    player.combineNew.TileNangcap = getTiLeNangcapSieuHoa(star);
//                    if (caiTrang != null && manhVo != null) {
//                        String npcSay = caiTrang.template.name + "\n|2|";
//                        for (Item.ItemOption io : caiTrang.itemOptions) {
//                            npcSay += io.getOptionString() + "\n";
//                        }
//                        npcSay += "|7|Tỉ lệ thành công: " + getTiLeNangcapSieuHoa(star) + "%" + "\n";
//                        if (getDiemNangcapSieuHoa(star) <= player.event.getEventPointNHS()) {
//                            npcSay += "|1|Cần " + Util.numberToMoney(getDiemNangcapSieuHoa(star)) + " Điểm Ngũ Hành Sơn";
//                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
//                                    "Nâng cấp\ncần " + Util.numberToMoney(getDaNangcapSieuHoa(star)) + " Đá Siêu Hóa");
//                        } else {
//                            npcSay += "Còn thiếu " + Util.numberToMoney(getDiemNangcapSieuHoa(star) - player.event.getEventPointNHS()) + " Điểm Ngũ Hành Sơn";
//                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
//                        }
//                    } else {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Cần 1 Cải Trang và Đá Siêu Hóa", "Đóng");
//                    }
//                } else {
//                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                            "Cần 1 Cải Trang và Đá Siêu Hóa", "Đóng");
//                }
//                break;
//                case NANG_GIAP_LUYEN_TAP://nangcaitrang
//                if (player.combineNew.itemsCombine.size() == 2) {
//                    Item caiTrang = null;
//                    Item manhVo = null;
//                    int star = 0;
//                    for (Item item : player.combineNew.itemsCombine) {
//                        if (item.template.type == 6) {
//                            caiTrang = item;
//                        } else if (item.template.id == 1467) {
//                            manhVo = item;
//                        }
//                    }
//                    if (caiTrang != null) {
//                        for (Item.ItemOption io2 : caiTrang.itemOptions) {
//                            if (io2.optionTemplate.id == 72) {
//                                if (io2.param >= 10) {
//                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                            "Giáp luyện tập đã nâng cấp tối đa", "Đóng");
//                                    return;
//                                }
//                                star = io2.param;
//                                break;
//                            }
//                        }
//
//                    }
//                    player.combineNew.DiemNangcap = getDiemNangcapSieuHoa(star);
//                    player.combineNew.DaNangcap = getDaNangcapSieuHoa(star);
//                    player.combineNew.TileNangcap = getTiLeNangcapSieuHoa(star);
//                    if (caiTrang != null && manhVo != null) {
//                        String npcSay = caiTrang.template.name + "\n|2|";
//                        for (Item.ItemOption io : caiTrang.itemOptions) {
//                            npcSay += io.getOptionString() + "\n";
//                        }
//                        npcSay += "|7|Tỉ lệ thành công: " + getTiLeNangcapSieuHoa(star) + "%" + "\n";
//                        if (getDiemNangcapSieuHoa(star) <= player.event.getEventPointNHS()) {
//                            npcSay += "|1|Cần " + Util.numberToMoney(getDiemNangcapSieuHoa(star)) + " Điểm Ngũ Hành Sơn";
//                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
//                                    "Nâng cấp\ncần " + Util.numberToMoney(getDaNangcapSieuHoa(star)) + " Đá Siêu Hóa");
//                        } else {
//                            npcSay += "Còn thiếu " + Util.numberToMoney(getDiemNangcapSieuHoa(star) - player.event.getEventPointNHS()) + " Điểm Ngũ Hành Sơn";
//                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
//                        }
//                    } else {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Cần 1 Cải Trang và Đá Siêu Hóa", "Đóng");
//                    }
//                } else {
//                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                            "Cần 1 Cải Trang và Đá Siêu Hóa", "Đóng");
//                }
//                break;
//            case TINH_THACH_HOA://nangcaitrang
//                if (player.combineNew.itemsCombine.size()
//                        == 2) {
//                    Item caiTrang = null;
//                    Item manhVo = null;
//                    int star = 0;
//                    for (Item item : player.combineNew.itemsCombine) {
//                        if (item.template.type == 21 || item.template.type == 72 || item.template.type == 11) {
//                            caiTrang = item;
//                        } else if (item.template.type == 93 && item.template.id >= 1360 && item.template.id <= 1366) {
//                            manhVo = item;
//                        }
//                    }
//                    if (caiTrang != null) {
//                        for (Item.ItemOption io2 : caiTrang.itemOptions) {
//                            if (io2.optionTemplate.id == 241 || io2.optionTemplate.id == 242 || io2.optionTemplate.id == 243 || io2.optionTemplate.id == 244 || io2.optionTemplate.id == 245 || io2.optionTemplate.id == 246 || io2.optionTemplate.id == 247) {
//
//                                if (io2.param >= 10) {
//                                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                            "Vật phẩm đã nâng tinh thạch cấp tối đa", "Đóng");
//                                    return;
//                                }
//                                star = io2.param;
//                                break;
//                            }
//                        }
//
//                    }
//                    player.combineNew.DaNangcap = getDaNangcapTinhThach(star);
//                    player.combineNew.TileNangcap = getTiLeNangcapTinhThach(star);
//                    if (caiTrang != null && manhVo != null) {
//                        String npcSay = caiTrang.template.name + "\n|2|";
//                        for (Item.ItemOption io : caiTrang.itemOptions) {
//                            npcSay += io.getOptionString() + "\n";
//                        }
//                        npcSay += "|7|Tỉ lệ thành công: " + getTiLeNangcapTinhThach(star) + "%" + "\n";
//                        npcSay += "|1|Cần " + Util.numberToMoney(getDaNangcapTinhThach(star)) + " Tinh Thạch ";
//                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
//                                "Nâng cấp");
//
//                    } else {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Cần 1 Pet, Linh Thú hoặc Vật phẩm đeo lưng và loại đá tinh thạch", "Đóng");
//                    }
//                } else {
//                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                            "Cần 1 Pet, Linh Thú hoặc Vật phẩm đeo lưng và loại đá tinh thạch", "Đóng");
//                }
//
//                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        switch (player.combineNew.typeCombine) {
//            case NANG_CAP_LINH_THU:
//                Nangcaplinhthu(player);
//                break;
            case LAM_PHEP_NHAP_DA:
                nhapDavun(player);
                break;
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
//            case CHUYEN_HOA_TRANG_BI:
//                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
            case PHAN_RA_DO_THAN_LINH:
                phanradothanlinh(player);
                break;
            case NANG_CAP_DO_TS:
                openDTS(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
                break;
//            case AN_TRANG_BI:
//                antrangbi(player);
//                break;
//            case DAP_DO_AO_HOA:
//                nangCapAoHoa(player);
//                break;
//            case PS_HOA_TRANG_BI:
//                psHoaTrangBi(player);
//                break;
//            case TAY_PS_HOA_TRANG_BI:
//                tayHacHoaTrangBi(player);
//                break;
//            case GIA_HAN_VAT_PHAM:
//                GiaHanTrangBi(player);
//                break;
//            case SIEU_HOA:
//                sieuHoaCaiTrang(player);
//                break;
//            case TINH_THACH_HOA:
//                tinhThach_Pet_LinhThu_VPDL(player);
//                break;
//            case MO_KHOA_ITEM:
//                tayDoKhoaGD(player);
//                break;
//            case NANG_GIAP_LUYEN_TAP:
//                nangGiapLuyenTap(player);
//                break;
            case NANG_TL_LEN_HUY_DIET:
                nangCapThanLinhLenHuyDiet(player);
                break;
            case NANG_HUY_DIET_LEN_SKH:
                huyDietLenKichHoatThuong(player);
                break;
            case NANG_HUY_DIET_LEN_SKH_VIP:
                huyDietLenKichHoathoiVang_IP(player);
                break;
            case NANG_CAP_CHAN_MENH:
                nangCapChanMenh(player);
                break;
            case CHE_TAO_TRANG_BI_TS:
                openCreateItemAngel(player);
                break;
            case CHE_TAO_AO_THIEN_MA:
                chetaoAoThienMa(player);
                break;
            case CHE_TAO_QUAN_THIEN_MA:
                chetaoQuanThienMa(player);
                break;
            case CHE_TAO_GANG_THIEN_MA:
                chetaoGangThienMa(player);
                break;
            case CHE_TAO_GIAY_THIEN_MA:
                chetaoGiayThienMa(player);
                break;
            case CHE_TAO_TRANG_SUC_THIEN_MA:
                chetaoTrangSucThienMa(player);
                break;
            case CHE_TAO_CAI_TRANG_THIEN_MA:
                chetaoCaiTrangThienMa(player);
                break;
            case CHE_TAO_AO_VO_CUC_TU_TAI:
                chetaoAoVoCucTuTai(player);
                break;
            case CHE_TAO_QUAN_VO_CUC_TU_TAI:
                chetaoQuanVoCucTuTai(player);
                break;
            case CHE_TAO_GANG_VO_CUC_TU_TAI:
                chetaoGangVoCucTuTai(player);
                break;
            case CHE_TAO_GIAY_VO_CUC_TU_TAI:
                chetaoGiayVoCucTuTai(player);
                break;
            case CHE_TAO_TRANG_SUC_VO_CUC_TU_TAI:
                chetaoTrangSucVoCucTuTai(player);
                break;
            case CHE_TAO_DAN_DUOC_LUYEN_KHI:
                chetaoDanDuocLuyenKhi(player);
                break;
            case CHE_TAO_TRUC_CO_DAN:
                chetaoTrucCoDan(player);
                break;
        }
        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }

    private void chetaoAoThienMa(Player player) {
        sendEffectSuccessCombine(player);
        createThienMaItem(player, (short) 1674, Arrays.asList(
                new ItemOption(199, Util.nextInt(40, 80)),
                new ItemOption(94, Util.nextInt(50, 150)),
                new ItemOption(190, 0),
                new ItemOption(191, 150)
        ));

        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoQuanThienMa(Player player) {
        sendEffectSuccessCombine(player);
        createThienMaItem(player, (short) 1675, Arrays.asList(
                new ItemOption(196, Util.nextInt(100, 200)),
                new ItemOption(77, Util.nextInt(50, 150)),
                new ItemOption(190, 0),
                new ItemOption(191, 150)
        ));

        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoGangThienMa(Player player) {
        sendEffectSuccessCombine(player);
        createThienMaItem(player, (short) 1676, Arrays.asList(
                new ItemOption(198, Util.nextInt(20, 40)),
                new ItemOption(50, Util.nextInt(50, 150)),
                new ItemOption(190, 0),
                new ItemOption(191, 150)
        ));

        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoGiayThienMa(Player player) {
        sendEffectSuccessCombine(player);
        createThienMaItem(player, (short) 1677, Arrays.asList(
                new ItemOption(197, Util.nextInt(100, 200)),
                new ItemOption(103, Util.nextInt(50, 150)),
                new ItemOption(190, 0),
                new ItemOption(191, 150)
        ));

        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoTrangSucThienMa(Player player) {
        sendEffectSuccessCombine(player);
        createThienMaItem(player, (short) 1678, Arrays.asList(
                new ItemOption(14, Util.nextInt(100, 200)),
                new ItemOption(5, Util.nextInt(10, 50)),
                new ItemOption(190, 0),
                new ItemOption(191, 150)
        ));

        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoCaiTrangThienMa(Player player) {
        sendEffectSuccessCombine(player);
        createThienMaItem(player, (short) 1679, Arrays.asList(
                new Item.ItemOption(196, 500),//hp
                new Item.ItemOption(197, 500),//ki
                new Item.ItemOption(198, 5),//sd
                new Item.ItemOption(199, 50),//giáp

                new Item.ItemOption(5, 25),
                new ItemOption(77, 250),
                new ItemOption(103, 250),
                new ItemOption(50, 250),
                new ItemOption(190, 0),
                new ItemOption(191, 150)
        ));
        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoAoVoCucTuTai(Player player) {
        sendEffectSuccessCombine(player);
        createTuTaiItem(player, (short) 1681, Arrays.asList(
                new ItemOption(199, Util.nextInt(2000, 4000)), // giáp +#00K
                new ItemOption(94, Util.nextInt(100, 150)), // % giáp
                new ItemOption(192, 0),
                new ItemOption(194, 20), // 6 món né đòn 20
                new ItemOption(191, 750)
        ));
        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoQuanVoCucTuTai(Player player) {
        sendEffectSuccessCombine(player);
        createTuTaiItem(player, (short) 1682, Arrays.asList(
                new ItemOption(196, Util.nextInt(5000, 10000)), // hp +#00K
                new ItemOption(77, Util.nextInt(100, 150)),
                new ItemOption(192, 0),
                new ItemOption(194, 20),
                new ItemOption(191, 750)
        ));
        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoGangVoCucTuTai(Player player) {
        sendEffectSuccessCombine(player);
        createTuTaiItem(player, (short) 1683, Arrays.asList(
                new ItemOption(198, Util.nextInt(1000, 2000)), //+#00K
                new ItemOption(50, Util.nextInt(100, 150)),
                new ItemOption(192, 0),
                new ItemOption(194, 20),
                new ItemOption(191, 750)
        ));
        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoGiayVoCucTuTai(Player player) {
        sendEffectSuccessCombine(player);
        createTuTaiItem(player, (short) 1684, Arrays.asList(
                new ItemOption(197, Util.nextInt(5000, 10000)), // ki +#00K
                new ItemOption(103, Util.nextInt(100, 150)),
                new ItemOption(192, 0),
                new ItemOption(194, 20),
                new ItemOption(191, 750)
        ));
        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoTrangSucVoCucTuTai(Player player) {
        sendEffectSuccessCombine(player);
        createTuTaiItem(player, (short) 1685, Arrays.asList(
                new ItemOption(14, Util.nextInt(200, 400)),
                new ItemOption(5, Util.nextInt(50, 100)),
                new ItemOption(192, 0),
                new ItemOption(194, 20),
                new ItemOption(191, 750)
        ));
        player.combineNew.clearItemCombine();
        reOpenItemCombine(player);
    }

    private void chetaoDanDuocLuyenKhi(Player player) {
        sendEffectSuccessCombine(player);
        createDanDuocLuyenKhi(player, (short) 1664);
        // player.combineNew.clearItemCombine();
        player.combineNew.updateItemsCombine(player.inventory.itemsBag);
        reOpenItemCombine(player);
    }

    private void chetaoTrucCoDan(Player player) {
        createTrucCoDan(player, (short) 1666);
        player.combineNew.updateItemsCombine(player.inventory.itemsBag);
        reOpenItemCombine(player);
    }
//    private void GiaHanTrangBi(Player player) {
//        if (player.combineNew.itemsCombine.size() != 2) {
//            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHSD()).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu trang bị HSD");
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1317).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu Bùa Gia Hạn");
//            return;
//        }
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            Item thegh = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1317).findFirst().get();
//            Item tbiHSD = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHSD).findFirst().get();
////              Item thegh = player.combineNew.itemsCombine.get(0);
////                Item tbiHSD = player.combineNew.itemsCombine.get(1);
//            if (thegh == null) {
//                Service.gI().sendThongBao(player, "Thiếu Bùa Gia Hạn");
//                return;
//            }
//            if (tbiHSD == null) {
//                Service.gI().sendThongBao(player, "Thiếu trang bị HSD");
//                return;
//            }
//            if (tbiHSD != null) {
//                for (ItemOption itopt : tbiHSD.itemOptions) {
//                    if (itopt.optionTemplate.id == 93) {
//                        if (itopt.param < 0 || itopt == null) {
//                            Service.gI().sendThongBao(player, "Không Phải Trang Bị Có HSD");
//                            return;
//                        }
//                    }
//                }
//            }
//            if (Util.isTrue(50, 100)) {
//                for (ItemOption itopt : tbiHSD.itemOptions) {
//                    if (itopt.optionTemplate.id == 93) {
//                        itopt.param += Util.nextInt(3, 7);
//                        break;
//                    }
//                }
//            } else {
//                for (ItemOption itopt : tbiHSD.itemOptions) {
//                    if (itopt.optionTemplate.id == 93) {
//                        itopt.param += 1;
//                        break;
//                    }
//                }
//            }
//            sendEffectSuccessCombine(player);
//            InventoryServiceNew.gI().subQuantityItemsBag(player, thegh, 1);
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.gI().sendMoney(player);
//            reOpenItemCombine(player);
//        } else {
//            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
//        }
//    }
//
//    private void GhepManh(Player player) {
//
//        if (player.combineNew.itemsCombine.size() != 2) {
//            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCaiTrangNC()).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu cải trang");
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && (item.template.id == 1466 || item.template.id == 1467)).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu mảnh vạn năng");
//            return;
//        }
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            if (player.inventory.gold < 2000000000) {
//                Service.gI().sendThongBao(player, "Con cần 2 tỉ vàng để đổi...");
//                return;
//            }
//            player.inventory.gold -= 2000000000;
//            Item daHacHoa = player.combineNew.itemsCombine.stream().filter(item -> (item.template.id == 1466 || item.template.id == 1467)).findFirst().get();
//            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isCaiTrangNC).findFirst().get();
//            if (daHacHoa == null) {
//                Service.gI().sendThongBao(player, "Cần 25 mảnh vạn năng");
//                return;
//            }
//            if (trangBiHacHoa == null) {
//                Service.gI().sendThongBao(player, "Thiếu cải trang goku god v2");
//                return;
//            }
//            if (daHacHoa != null && daHacHoa.quantity < 25) {
//                Service.gI().sendThongBao(player, "Thiếu " + (25 - daHacHoa.quantity) + " mảnh vạn năng");
//                return;
//            }
//
//            if (trangBiHacHoa != null) {
//                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
//                    if (itopt.optionTemplate.id == 225) {
//                        if (itopt.param >= 5) {
//
//                            Service.gI().sendThongBao(player, "Cải trang đã đạt tới giới hạn siêu hóa");
//                            return;
//                        }
//                    }
//                }
//            }
//
//            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
//                sendEffectSuccessCombine(player);
//                List<Integer> idOptionHacHoa = Arrays.asList(226, 227, 228, 229);
//                int randomOption = idOptionHacHoa.get(Util.nextInt(0, 3));
//                if (!trangBiHacHoa.haveOption(225)) {
//                    trangBiHacHoa.itemOptions.add(new ItemOption(225, 1));
//                } else {
//                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
//                        if (itopt.optionTemplate.id == 225) {
//                            itopt.param += 1;
//                            break;
//                        }
//                    }
//                }
//                if (!trangBiHacHoa.haveOption(randomOption)) {
//                    trangBiHacHoa.itemOptions.add(new ItemOption(randomOption, 5));
//                } else {
//                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
//                        if (itopt.optionTemplate.id == randomOption) {
//                            itopt.param += 2;
//                            break;
//                        }
//                    }
//                }
//                player.combineNew.ratioCombine = 0;
//                Service.gI().sendThongBao(player, "Bạn đã nâng cấp thành công");
//            } else {
//
//                sendEffectFailCombine(player);
//
//            }
//            InventoryServiceNew.gI().subQuantityItemsBag(player, daHacHoa, 25);
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.gI().sendMoney(player);
//            player.combineNew.itemsCombine.clear();
//            reOpenItemCombine(player);
//        } else {
//            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
//        }
//    }

    private void nangCapChanMenh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            float tiLe = player.combineNew.TileNangcap;
            int diem = player.combineNew.DiemNangcap;
            if (player.event.getEventPointBoss() < diem) {
                Service.gI().sendThongBao(player, "Không đủ Điểm Săn Boss để thực hiện");
                return;
            }
            long gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }

            Item bongTai = null;
            Item manhHon = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id >= 1185 && item.template.id <= 1193) {
                    bongTai = item;
                } else if (item.template.id == 1318) {
                    manhHon = item;
                }
            }
//            for (Item.ItemOption io : bongTai.itemOptions) {
//                if (io.optionTemplate.id == 228) {
//                    Service.gI().sendThongBao(player, "Cần chân mệnh đã kích hoạt chỉ số");
//                    return;
//                }
//            }

            int star = 0;
            if (bongTai != null) {
                for (Item.ItemOption io : bongTai.itemOptions) {
                    if (io.optionTemplate.id == 107 || io.optionTemplate.id == 102 || io.optionTemplate.id == 72) {
                        star = Math.max(star, io.param);
                    }
                }
            }

            if (bongTai != null && manhHon != null && manhHon.quantity > player.combineNew.DaNangcap) {
                if (star >= 9) {
                    Service.gI().sendThongBao(player, "Đã max cấp");
                }
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                player.event.subEventPointBoss(diem);
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhHon, player.combineNew.DaNangcap);
                if (Util.isTrue(tiLe, 100)) {
                    bongTai.template = ItemService.gI().getTemplate((short) (bongTai.template.id + 1));
                    // Random chỉ số mới và thêm vào bông tai mới
                    List<Integer> optionIds = Arrays.asList(232, 233, 234, 235);
                    int randomOptionId = optionIds.get(Util.nextInt(0, optionIds.size() - 1));
                    boolean hasOption = false;

                    for (Item.ItemOption io : bongTai.itemOptions) {
                        if (io.optionTemplate.id == randomOptionId) {
                            io.param += Util.nextInt(500, 1000);
                            hasOption = true;
                            break;
                        }

                    }

                    // Tăng option ID 72, 107, 102 lên 1, tối đa là 9
                    boolean foundOption72_107_102 = false;
                    boolean foundOption236 = false;
                    for (Item.ItemOption io2 : bongTai.itemOptions) {
                        if (io2.optionTemplate.id == 236) {
                            io2.param = Math.min(io2.param + 100, 9);
                            foundOption236 = false;
                        }
                    }

                    for (Item.ItemOption io : bongTai.itemOptions) {
                        if (io.optionTemplate.id == 72 || io.optionTemplate.id == 107 || io.optionTemplate.id == 102 || io.optionTemplate.id == 50 || io.optionTemplate.id == 77 || io.optionTemplate.id == 103) {
                            io.param = Math.min(io.param + 1, 9);
                            foundOption72_107_102 = true;
                        }
                    }
                    if (!foundOption72_107_102) {
                        if (!foundOption236) {
                            bongTai.itemOptions.add(new ItemOption(50, 8));
                            bongTai.itemOptions.add(new ItemOption(77, 8));
                            bongTai.itemOptions.add(new ItemOption(103, 8));
                        }
                        bongTai.itemOptions.add(new ItemOption(72, 2));
                        bongTai.itemOptions.add(new ItemOption(107, 2));
                        bongTai.itemOptions.add(new ItemOption(102, 2));
                    }
                    if (!hasOption) {
                        bongTai.itemOptions.add(new ItemOption(randomOptionId, Util.nextInt(500, 2000)));
                    }
                    sendEffectSuccessCombine(player);
                    InventoryServiceNew.gI().addItemBag(player, bongTai);

                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

//    private void sieuHoaCaiTrang(Player player) {
//        if (player.combineNew.itemsCombine.size() == 2) {
//            float tiLe = player.combineNew.TileNangcap;
//            int diem = player.combineNew.DiemNangcap;
//            if (player.event.getEventPointNHS() < diem) {
//                Service.gI().sendThongBao(player, "Không đủ Điểm Ngũ Hành Sơn để thực hiện");
//                return;
//            }
//            long gold = player.combineNew.goldCombine;
//            if (player.inventory.gold < gold) {
//                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
//                return;
//            }
//            int gem = player.combineNew.gemCombine;
//            if (player.inventory.gem < gem) {
//                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
//                return;
//            }
//
//            Item caiTrang = null;
//            Item manhCaiTrang = null;
//            for (Item item : player.combineNew.itemsCombine) {
//                if (item.template.type == 5) {
//                    caiTrang = item;
//                } else if (item.template.id == 1466) {
//                    manhCaiTrang = item;
//                }
//            }
//            int star = 0;
//            if (caiTrang != null) {
//                for (Item.ItemOption io : caiTrang.itemOptions) {
//                    if (io.optionTemplate.id == 225) {
//                        star = Math.max(star, io.param);
//                    }
//                }
//            }
//            if (star >= 10) {
//                Service.gI().sendThongBao(player, "Đã max cấp");
//                return;
//            }
//
//            if (caiTrang != null && manhCaiTrang != null && manhCaiTrang.quantity > player.combineNew.DaNangcap && star < 10) {
//                player.inventory.gold -= gold;
//                player.inventory.gem -= gem;
//                player.event.subEventPointNHS(diem);
//
//                InventoryServiceNew.gI().subQuantityItemsBag(player, manhCaiTrang, player.combineNew.DaNangcap);
//                if (Util.isTrue(tiLe, 100)) {
//                    Item newCaiTrang = ItemService.gI().createNewItem((short) (caiTrang.template.id));
//
//                    // Copy các chỉ số từ bông tai cũ sang bông tai mới
//                    for (Item.ItemOption io : caiTrang.itemOptions) {
//                        newCaiTrang.itemOptions.add(new ItemOption(io.optionTemplate.id, io.param));
//                    }
//
//                    // Random chỉ số mới và thêm vào cải trang mới
//                    List<Integer> optionIds = Arrays.asList(226, 227, 228, 229);
//                    int randomOptionId = optionIds.get(Util.nextInt(0, optionIds.size() - 1));
//                    boolean hasOption = false;
//
//                    for (Item.ItemOption io : newCaiTrang.itemOptions) {
//                        if (io.optionTemplate.id == randomOptionId) {
//                            io.param += Util.nextInt(500, 3000);
//                            hasOption = true;
//                            break;
//                        }
//
//                    }
//
//                    // Tăng option ID 72, 107, 102 lên 1, tối đa là 30
//                    boolean foundOption72_107_102 = false;
//                    for (Item.ItemOption io : newCaiTrang.itemOptions) {
//                        if (io.optionTemplate.id == 225) {
//                            io.param = Math.min(io.param + 1, 10);
//                            foundOption72_107_102 = true;
//                        }
//                    }
//                    if (!foundOption72_107_102) {
//                        newCaiTrang.itemOptions.add(new ItemOption(225, 1));
//                    }
//                    if (!hasOption) {
//                        newCaiTrang.itemOptions.add(new ItemOption(randomOptionId, Util.nextInt(500, 3000)));
//                    }
//                    sendEffectSuccessCombine(player);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, caiTrang, 1);
//                    InventoryServiceNew.gI().addItemBag(player, newCaiTrang);
//
//                } else {
//                    sendEffectFailCombine(player);
//                }
//                InventoryServiceNew.gI().sendItemBags(player);
//                Service.gI().sendMoney(player);
//                reOpenItemCombine(player);
//            } else {
//                Service.gI().sendThongBao(player, "Thiếu vật phầm để nâng");
//            }
//        }
//    }
//
//    private void nangGiapLuyenTap(Player player) {
//        int countDaNangCap = player.combineNew.countDaQuy;
//        long gold = player.combineNew.goldCombine;
//        short countDaBaoVe = player.combineNew.countDaBaoVe;
//        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
//            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 32).count() != 1) {
//                return;
//            }
//            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
//                return;
//            }
//            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
//                return;//admin
//            }
//            Item itemDo = null;
//            Item itemDNC = null;
//            Item itemDBV = null;
//            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
//                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
//                        itemDBV = player.combineNew.itemsCombine.get(j);
//                        continue;
//                    }
//                    if (player.combineNew.itemsCombine.get(j).template.type == 32) {
//                        itemDo = player.combineNew.itemsCombine.get(j);
//                    } else {
//                        itemDNC = player.combineNew.itemsCombine.get(j);
//                    }
//                }
//            }
//            if (isCoupleItemNangCapCheck3(itemDo, itemDNC)) {
//
//                if (player.inventory.gold < gold) {
//                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
//                    return;
//                }
//                if (player.combineNew.itemsCombine.size() == 3) {
//                    if (itemDBV == null) {
//                        return;
//                    }
//                    if (itemDBV.quantity < countDaBaoVe) {
//                        return;
//                    }
//                }
//
//                int level = 0;
//                Item.ItemOption optionLevel = null;
//                if (itemDo == null) {
//                    Service.gI().sendThongBao(player, "Có lỗi xảy ra vui lòng báo admin");
//                    return;
//                }
//                for (Item.ItemOption io : itemDo.itemOptions) {
//                    if (io.optionTemplate.id == 72) {
//                        level = io.param;
//                        optionLevel = io;
//                        break;
//                    }
//                }
//// Kiểm tra xem item có option nào từ danh sách không
//                Item.ItemOption option = null;
//                for (Item.ItemOption io : itemDo.itemOptions) {
//                    if (io.optionTemplate.id == 2
//                            || io.optionTemplate.id == 8
//                            || io.optionTemplate.id == 19
//                            || io.optionTemplate.id == 27
//                            || io.optionTemplate.id == 28
//                            || io.optionTemplate.id == 16
//                            || (io.optionTemplate.id >= 94
//                            && io.optionTemplate.id <= 101)
//                            || io.optionTemplate.id == 108
//                            || io.optionTemplate.id == 109
//                            || io.optionTemplate.id == 114
//                            || io.optionTemplate.id == 117
//                            || io.optionTemplate.id == 153
//                            || io.optionTemplate.id == 156
//                            || io.optionTemplate.id == 3) {
//                        option = io; // Option đã tồn tại
//                        break;
//                    }
//                }
//                if (level < MAX_LEVEL_ITEM2) {
//                    player.inventory.gold -= gold;
//                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
//                        if (option != null) {
//                            option.param += 1;
//                        } else {
//                            List<Integer> optionIds = Arrays.asList(2, 8, 19, 27, 28, 16, 94, 95, 96, 97, 98, 99, 100, 101, 108, 109, 114, 117, 153, 156, 3);
//                            int randomOptionId = optionIds.get(Util.nextInt(0, optionIds.size() - 1));
//                            itemDo.itemOptions.add(new ItemOption(randomOptionId, 1));
//                        }
//                        if (optionLevel != null) {
//                            optionLevel.param++;
//                        } else {
//                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
//                        }
//
//                        sendEffectSuccessCombine(player);
//                    } else {
//                        if ((level == 2 || level == 4 || level == 6 || level == 8) && (player.combineNew.itemsCombine.size() != 3)) {
//                            if (option != null) {
//                                option.param -= 1;
//                            }
//                            if (optionLevel != null) {
//                                optionLevel.param--;
//                            }
//                        }
//                        sendEffectFailCombine(player);
//                    }
//                    if (player.combineNew.itemsCombine.size() == 3) {
//                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
//                    }
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, countDaNangCap);
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
//
//    private void tinhThach_Pet_LinhThu_VPDL(Player player) {
//        float tiLe = player.combineNew.TileNangcap;
//
//        if (player.combineNew.itemsCombine.size() == 2) {
//            long gold = player.combineNew.goldCombine;
//            if (player.inventory.gold < gold) {
//                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
//                return;
//            }
//            int gem = player.combineNew.gemCombine;
//            if (player.inventory.gem < gem) {
//                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
//                return;
//            }
//
//            Item caiTrang = null;
//            Item manhCaiTrang = null;
//            for (Item item : player.combineNew.itemsCombine) {
//                if (item.template.type == 21 || item.template.type == 72 || item.template.type == 11) {
//                    caiTrang = item;
//                } else if (item.template.type == 93 && item.template.id >= 1360 && item.template.id <= 1366) {
//                    manhCaiTrang = item;
//                }
//            }
//
//            int star = 0;
//            if (caiTrang != null) {
//                for (Item.ItemOption io : caiTrang.itemOptions) {
//                    if (io.optionTemplate.id == 241 || io.optionTemplate.id == 242 || io.optionTemplate.id == 243 || io.optionTemplate.id == 244 || io.optionTemplate.id == 245 || io.optionTemplate.id == 246 || io.optionTemplate.id == 247) {
//                        star = Math.max(star, io.param);
//                    }
//                }
//            }
//            if (star >= 10) {
//                Service.gI().sendThongBao(player, "Đã max cấp");
//                return;
//            }
//            if (caiTrang != null && manhCaiTrang != null && manhCaiTrang.quantity > player.combineNew.DaNangcap) {
//                player.inventory.gold -= gold;
//                player.inventory.gem -= gem;
//                InventoryServiceNew.gI().subQuantityItemsBag(player, manhCaiTrang, player.combineNew.DaNangcap);
//                if (Util.isTrue(tiLe, 100)) {
//                    Item newCaiTrang = ItemService.gI().createNewItem((short) (caiTrang.template.id));
//
//                    // Copy các chỉ số từ bông tai cũ sang bông tai mới
//                    for (Item.ItemOption io : caiTrang.itemOptions) {
//                        newCaiTrang.itemOptions.add(new ItemOption(io.optionTemplate.id, io.param));
//                    }
//
//                    // Khai báo randomOptionId ở ngoài
//                    int randomOptionId = -1;
//
//                    // Xác định giá trị randomOptionId dựa trên manhCaiTrang.template.id
//                    switch (manhCaiTrang.template.id) {
//                        case 1360:
//                            randomOptionId = 241;
//                            break;
//                        case 1361:
//                            randomOptionId = 242;
//                            break;
//                        case 1362:
//                            randomOptionId = 243;
//                            break;
//                        case 1363:
//                            randomOptionId = 244;
//                            break;
//                        case 1364:
//                            randomOptionId = 245;
//                            break;
//                        case 1365:
//                            randomOptionId = 246;
//                            break;
//                        case 1366:
//                            randomOptionId = 247;
//                            break;
//                        default:
//                            break;
//                    }
//
//                    // Kiểm tra nếu newCaiTrang đã có loại tinh thạch khác
//                    for (Item.ItemOption io : newCaiTrang.itemOptions) {
//                        if (io.optionTemplate.id != randomOptionId && (io.optionTemplate.id >= 241 && io.optionTemplate.id <= 247)) {
//                            Service.gI().sendThongBao(player, "Đã nâng một loại tinh thạch khác rồi, không thể nâng tiếp");
//                            return;
//                        }
//                    }
//
//                    // Nếu không có loại tinh thạch khác, tiếp tục thêm hoặc nâng cấp
//                    boolean hasOption = false;
//                    for (Item.ItemOption io : newCaiTrang.itemOptions) {
//                        if (io.optionTemplate.id == randomOptionId) {
//                            io.param = Math.min(io.param + 1, 10);
//                            hasOption = true;
//                            break;
//                        }
//                    }
//
//                    if (!hasOption) {
//                        newCaiTrang.itemOptions.add(new ItemOption(randomOptionId, 1));
//                    }
//
//                    sendEffectSuccessCombine(player);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, caiTrang, 1);
//                    InventoryServiceNew.gI().addItemBag(player, newCaiTrang);
//
//                } else {
//                    sendEffectFailCombine(player);
//                }
//                InventoryServiceNew.gI().sendItemBags(player);
//                Service.gI().sendMoney(player);
//                reOpenItemCombine(player);
//            } else {
//                Service.gI().sendThongBao(player, "Thiếu vật phẩm cần để nâng");
//            }
//        }
//    }
    private void nangCapThanLinhLenHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item dtl = null;
            dtl = player.combineNew.itemsCombine.get(0);
            if (dtl != null && dtl.isDTL()) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.ruby < 50000) {
                    Service.gI().sendThongBao(player, "Con cần 200K hồng ngọc để đổi...");
                    return;
                }
                player.inventory.ruby -= 50000;
                int tiLe = 12; // tỉ lệ
                if (Util.isTrue(tiLe, 100)) {
                    sendEffectSuccessCombine(player);
                    ItemService.gI().OpenDHD2(player, player.gender, dtl.template.type);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                } else {
                    sendEffectFailCombine(player);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);

            }
        }
    }

    public void huyDietLenKichHoatThuong(Player player) {
        //  món đầu  làm gốc
        if (player.combineNew.itemsCombine.size() != 1) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        Item item1 = player.combineNew.itemsCombine.get(0);
        if (item1.isDHD()) {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                if (player.inventory.gold < COST) {
                    Service.gI().sendThongBao(player, "Con cần 500Tr vàng để đổi...");
                    return;
                }

                player.inventory.gold -= COST;
                int tile = 50;
                if (Util.isTrue(tile, 100)) {
                    int type = item1.template.type;
                    int[][] items = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
                    int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
                    int skhv1 = 25;// ti le
                    int skhv2 = 35;//ti le
                    int skhc = 40;//ti le
                    int skhId = -1;
                    int rd = Util.nextInt(1, 100);
                    if (rd <= skhv1) {
                        skhId = 0;
                    } else if (rd <= skhv1 + skhv2) {
                        skhId = 1;
                    } else if (rd <= skhv1 + skhv2 + skhc) {
                        skhId = 2;
                    }
                    Item item = null;
                    switch (player.gender) {
                        case 0:
                            item = ItemService.gI().itemSKH(items[0][item1.template.type], options[0][skhId]);
                            break;
                        case 1:
                            item = ItemService.gI().itemSKH(items[1][item1.template.type], options[1][skhId]);
                            break;
                        case 2:
                            item = ItemService.gI().itemSKH(items[2][item1.template.type], options[2][skhId]);
                            break;
                    }
                    if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().addItemBag(player, item);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                        InventoryServiceNew.gI().sendItemBags(player);
                    } else {
                        Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
                    }
                } else {
                    sendEffectFailCombine(player);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
//                      Service.gI().sendThongBao(player, " Thất bại nên đồ bị bay r nhs " + item.template.name);
                }
            }
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Cần 1 món huỷ diệt");
        }
    }

    public void huyDietLenKichHoathoiVang_IP(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item item1 = player.combineNew.itemsCombine.get(0);
            Item item2 = player.combineNew.itemsCombine.get(1);
            Item item3 = player.combineNew.itemsCombine.get(2);
            if ((item1.isDHD() && item2.isDHD() && item3.isDHD())) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.inventory.ruby < 50000) {
                        Service.gI().sendThongBao(player, "Con không đủ 50k hồng ngọc để đổi...");
                        return;
                    }
                    player.inventory.ruby -= 50000;
                    int tile = 25; // tỉ lệ
                    if (Util.isTrue(tile, 100)) {
                        int type = item1.template.type;
                        int[][][] items
                                = {{{0, 3, 33, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555},// áo //td
                                {6, 9, 35, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, // quần
                                {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, // găng
                                {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, // giày
                                {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}, // rada

                                {{1, 4, 41, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, // nm
                                {7, 10, 43, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558},
                                {22, 25, 45, 46, 160, 161, 162, 163, 258, 259, 260, 261, 564},
                                {28, 31, 47, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565},
                                {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
                                {{2, 5, 49, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, // xd
                                {8, 11, 51, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560},
                                {23, 26, 53, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566},
                                {29, 32, 55, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567},
                                {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}};
                        int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
                        int skhv1 = 25;// ti le
                        int skhv2 = 35;//ti le
                        int skhc = 40;//ti le
                        int skhId = -1;
                        int rd = Util.nextInt(1, 100);
                        if (rd <= skhv1) {
                            skhId = 0;
                        } else if (rd <= skhv1 + skhv2) {
                            skhId = 1;
                        } else if (rd <= skhv1 + skhv2 + skhc) {
                            skhId = 2;
                        }
                        Item item = null;
                        switch (player.gender) {
                            case 0:
                                Random rand0 = new Random();
                                int i0 = rand0.nextInt(items.length);
                                int j0 = rand0.nextInt(items[0].length);
                                int k0 = rand0.nextInt(items[0][item1.template.type].length);
                                int randomThirdElement0 = items[0][item1.template.type][k0];
                                item = ItemService.gI().itemSKH(randomThirdElement0, options[0][skhId]);
                                break;
                            case 1:
                                Random rand1 = new Random();
                                int i1 = rand1.nextInt(items.length);
                                int j1 = rand1.nextInt(items[1].length);
                                int k1 = rand1.nextInt(items[1][item1.template.type].length);
                                int randomThirdElement1 = items[1][item1.template.type][k1];
                                item = ItemService.gI().itemSKH(randomThirdElement1, options[1][skhId]);
                                break;
                            case 2:
                                Random rand2 = new Random();
                                int i2 = rand2.nextInt(items.length);
                                int j2 = rand2.nextInt(items[2].length);
                                int k2 = rand2.nextInt(items[2][item1.template.type].length);
                                int randomThirdElement2 = items[2][item1.template.type][k2];
                                item = ItemService.gI().itemSKH(randomThirdElement2, options[2][skhId]);
                                break;
                        }
                        if (item != null) {
                            sendEffectSuccessCombine(player);
                            InventoryServiceNew.gI().addItemBag(player, item);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                            InventoryServiceNew.gI().sendItemBags(player);
                        }
                    } else {
                        sendEffectFailCombine(player);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                        InventoryServiceNew.gI().sendItemBags(player);
                    }
                    player.combineNew.itemsCombine.clear();
                    reOpenItemCombine(player);
                } else {
                    Service.gI().sendThongBao(player, "Cần 1 ô trong trong hanh trang");
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);

            } else {
                Service.gI().sendThongBao(player, "Cần 3 món huỷ diệt cùng loại");
            }
        }
    }

    public void openCreateItemAngel(Player player) {
        // Công thức vip + x999 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
            Service.gI().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu Đá nâng cấp");
//            return;
//        }
//        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu Đá may mắn");
//            return;
//        }
        Item mTS = null, daNC = null, daMM = null, CthoiVang_ip = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isManhTS()) {
                    mTS = item;
                } else if (item.isDaNangCap()) {
                    daNC = item;
                } else if (item.isDaMayMan()) {
                    daMM = item;
                } else if (item.isCongThucVip()) {
                    CthoiVang_ip = item;
                }
            }
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {//check chỗ trống hành trang
            if (player.inventory.ruby < 5000) {
                Service.gI().sendThongBao(player, "Không đủ hồng ngọc để thực hiện");
                return;
            }
            player.inventory.ruby -= 5000;

            int tilemacdinh = 35;
            int tileLucky = 20;
            if (daNC != null) {
                tilemacdinh += (daNC.template.id - 1073) * 10;
            } else {
                tilemacdinh = tilemacdinh;
            }
            if (daMM != null) {
                tileLucky += tileLucky * (daMM.template.id - 1078) * 10 / 100;
            } else {
                tileLucky = tileLucky;
            }
            if (Util.nextInt(0, 100) < tilemacdinh) {
                Item itemCthoiVang_ip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                if (daNC != null) {
                    Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                }
                if (daMM != null) {
                    Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                }
                Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).findFirst().get();

                tilemacdinh = Util.nextInt(0, 50);
                if (tilemacdinh == 49) {
                    tilemacdinh = 20;
                } else if (tilemacdinh == 48 || tilemacdinh == 47) {
                    tilemacdinh = 19;
                } else if (tilemacdinh == 46 || tilemacdinh == 45) {
                    tilemacdinh = 18;
                } else if (tilemacdinh == 44 || tilemacdinh == 43) {
                    tilemacdinh = 17;
                } else if (tilemacdinh == 42 || tilemacdinh == 41) {
                    tilemacdinh = 16;
                } else if (tilemacdinh == 40 || tilemacdinh == 39) {
                    tilemacdinh = 15;
                } else if (tilemacdinh == 38 || tilemacdinh == 37) {
                    tilemacdinh = 14;
                } else if (tilemacdinh == 36 || tilemacdinh == 35) {
                    tilemacdinh = 13;
                } else if (tilemacdinh == 34 || tilemacdinh == 33) {
                    tilemacdinh = 12;
                } else if (tilemacdinh == 32 || tilemacdinh == 31) {
                    tilemacdinh = 11;
                } else if (tilemacdinh == 30 || tilemacdinh == 29) {
                    tilemacdinh = 10;
                } else if (tilemacdinh <= 28 || tilemacdinh >= 26) {
                    tilemacdinh = 9;
                } else if (tilemacdinh <= 25 || tilemacdinh >= 23) {
                    tilemacdinh = 8;
                } else if (tilemacdinh <= 22 || tilemacdinh >= 20) {
                    tilemacdinh = 7;
                } else if (tilemacdinh <= 19 || tilemacdinh >= 17) {
                    tilemacdinh = 6;
                } else if (tilemacdinh <= 16 || tilemacdinh >= 14) {
                    tilemacdinh = 5;
                } else if (tilemacdinh <= 13 || tilemacdinh >= 11) {
                    tilemacdinh = 4;
                } else if (tilemacdinh <= 10 || tilemacdinh >= 8) {
                    tilemacdinh = 3;
                } else if (tilemacdinh <= 7 || tilemacdinh >= 5) {
                    tilemacdinh = 2;
                } else if (tilemacdinh <= 4 || tilemacdinh >= 2) {
                    tilemacdinh = 1;
                } else if (tilemacdinh <= 1) {
                    tilemacdinh = 0;
                }
                short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

                Item itemTS = ItemService.gI().DoThienSu(itemIds[itemCthoiVang_ip.template.gender > 2 ? player.gender : itemCthoiVang_ip.template.gender][itemManh.typeIdManh()], itemCthoiVang_ip.template.gender);

                tilemacdinh += 10;

                if (tilemacdinh > 0) {
                    for (byte i = 0; i < itemTS.itemOptions.size(); i++) {
                        if (itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                            itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param * tilemacdinh / 100);
                        }
                    }
                }
                tilemacdinh = Util.nextInt(0, 100);

                if (tilemacdinh <= tileLucky) {
                    if (tilemacdinh >= (tileLucky - 3)) {
                        tileLucky = 3;
                    } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                        tileLucky = 2;
                    } else {
                        tileLucky = 1;
                    }
                    itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                    ArrayList<Integer> listOptionBonus = new ArrayList<>();
                    listOptionBonus.add(50);
                    listOptionBonus.add(77);
                    listOptionBonus.add(103);
                    listOptionBonus.add(98);
                    listOptionBonus.add(99);
                    for (int i = 0; i < tileLucky; i++) {
                        tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                        itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                        listOptionBonus.remove(tilemacdinh);
                    }
                }

                InventoryServiceNew.gI().addItemBag(player, itemTS);
                sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            if (mTS != null && daMM != null && daNC != null && CthoiVang_ip != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CthoiVang_ip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
            } else if (CthoiVang_ip != null && mTS != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CthoiVang_ip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
            } else if (CthoiVang_ip != null && mTS != null && daNC != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CthoiVang_ip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
            } else if (CthoiVang_ip != null && mTS != null && daMM != null) {
                InventoryServiceNew.gI().subQuantityItemsBag(player, CthoiVang_ip, 1);
                InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 999);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
            }

            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void GetTrangBiKichHoathuydiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(1000);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1300, 1800)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(70, 95)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(69, 96)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(1, 16)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 1) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void GetTrangBiKichHoatthiensu(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(1000);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(13500, 15999)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(18, 25)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 1) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }

//    private void doiKiemThan(Player player) {
//        if (player.combineNew.itemsCombine.size() == 3) {
//            Item keo = null, luoiKiem = null, chuoiKiem = null;
//            for (Item it : player.combineNew.itemsCombine) {
//                if (it.template.id == 2015) {
//                    keo = it;
//                } else if (it.template.id == 2016) {
//                    chuoiKiem = it;
//                } else if (it.template.id == 2017) {
//                    luoiKiem = it;
//                }
//            }
//            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                    sendEffectSuccessCombine(player);
//                    Item item = ItemService.gI().createNewItem((short) 2018);
//                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
//                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
//                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
//                    if (Util.isTrue(80, 100)) {
//                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
//                    }
//                    InventoryServiceNew.gI().addItemBag(player, item);
//
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);
//
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
//
//    private void doiChuoiKiem(Player player) {
//        if (player.combineNew.itemsCombine.size() == 1) {
//            Item manhNhua = player.combineNew.itemsCombine.get(0);
//            if (manhNhua.template.id == 2014 && manhNhua.quantity >= 99) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                    sendEffectSuccessCombine(player);
//                    Item item = ItemService.gI().createNewItem((short) 2016);
//                    InventoryServiceNew.gI().addItemBag(player, item);
//
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhNhua, 99);
//
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
//
//    private void doiLuoiKiem(Player player) {
//        if (player.combineNew.itemsCombine.size() == 1) {
//            Item manhSat = player.combineNew.itemsCombine.get(0);
//            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                    sendEffectSuccessCombine(player);
//                    Item item = ItemService.gI().createNewItem((short) 2017);
//                    InventoryServiceNew.gI().addItemBag(player, item);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);
//
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
//
//    private void doiManhKichHoat(Player player) {
//        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
//            Item nr1s = null, doThan = null, buaBaoVe = null;
//            for (Item it : player.combineNew.itemsCombine) {
//                if (it.template.id == 14) {
//                    nr1s = it;
//                } else if (it.template.id == 2010) {
//                    buaBaoVe = it;
//                } else if (it.template.id >= 555 && it.template.id <= 567) {
//                    doThan = it;
//                }
//            }
//
//            if (nr1s != null && doThan != null) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
//                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
//                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
//                    int tiLe = buaBaoVe != null ? 100 : 50;
//                    if (Util.isTrue(tiLe, 100)) {
//                        sendEffectSuccessCombine(player);
//                        Item item = ItemService.gI().createNewItem((short) 2009);
//                        item.itemOptions.add(new Item.ItemOption(30, 0));
//                        InventoryServiceNew.gI().addItemBag(player, item);
//                    } else {
//                        sendEffectFailCombine(player);
//                    }
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, nr1s, 1);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, doThan, 1);
//                    if (buaBaoVe != null) {
//                        InventoryServiceNew.gI().subQuantityItemsBag(player, buaBaoVe, 1);
//                    }
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            } else {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
//            }
//        }
//    }
    private void phanradothanlinh(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            player.inventory.ruby -= 5000;
            List<Integer> itemdov2 = new ArrayList<>(Arrays.asList(562, 564, 566));
            Item item = player.combineNew.itemsCombine.get(0);
            int couponAdd = itemdov2.stream().anyMatch(t -> t == item.template.id) ? 2 : item.template.id == 561 ? 3 : 1;
            sendEffectSuccessCombine(player);
            player.inventory.coupon += couponAdd;
            this.baHatMit.npcChat(player, "Con đã nhận được " + couponAdd + " điểm");
            Item newItem = ItemService.gI().createNewItem((short) 674);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            player.combineNew.itemsCombine.clear();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        // new update 2 mon huy diet + 1 mon than linh(skh theo style) +  5 manh bat ki
        if (player.combineNew.itemsCombine.size() != 4) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (player.inventory.ruby < RUBY) {
            Service.gI().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) < 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).findFirst().get();
        List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 1).findFirst().get();
        int tiLe = itemTL != null ? 50 : 100;
        if (Util.isTrue(tiLe, 100)) {
            player.inventory.ruby -= RUBY;
            sendEffectSuccessCombine(player);
            short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

            Item itemTS = ItemService.gI().DoThienSu(itemIds[itemTL.template.gender > 2 ? player.gender : itemTL.template.gender][itemManh.typeIdManh()], itemTL.template.gender);
            InventoryServiceNew.gI().addItemBag(player, itemTS);

            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemManh, 5);
            itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            player.inventory.ruby -= RUBY;
            sendEffectFailCombine(player);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemManh, 1);
            itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            Item bongTai = null;
            Item manhVo = null;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Thiếu ngọc xanh");
                return;
            }
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Thiếu vàng");
                return;
            }
            int[] idBongTai = {454, 921, 2064, 2113};
            int[] idManhVo = {933, 933, 933};
            int[] slManhVo = {5999, 9999, 19999};

            for (Item item : player.combineNew.itemsCombine) {
                if (Arrays.stream(idBongTai).anyMatch(id -> id == item.template.id)) {
                    bongTai = item;
                } else if (Arrays.stream(idManhVo).anyMatch(id -> id == item.template.id)) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null) {
                int indexBongTai = 2;
                for (int i = 0; i < idBongTai.length; i++) {
                    if (bongTai.template.id == idBongTai[i]) {
                        indexBongTai = i;
                        break;
                    }
                }

                int sl = 50 + 50 * indexBongTai;
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                if (!this.SubThoiVang(player, sl)) {
                    Service.gI().sendThongBao(player, "Cần " + sl + " thỏi vàng để thực hiện");
                } else {
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, slManhVo[indexBongTai]);
                        bongTai.template = ItemService.gI().getTemplate(idBongTai[indexBongTai + 1]);
                        sendEffectSuccessCombine(player);
                        Service.gI().sendThongBao(player, "Nâng cấp thành công");
                    } else {
                        player.inventory.gold -= gold;
                        player.inventory.gem -= gem;
                        InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, slManhVo[indexBongTai] / 10);
                        sendEffectFailCombine(player);
                        Service.gI().sendThongBao(player, "Nâng cấp thất bại");
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);

            }
        }
    }

    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Thiếu ngọc xanh");
                return;
            }
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Thiếu vàng");
                return;
            }
            Item bongTai = null;
            Item manhHon = null;
            Item daXanhLam = null;
            int[] idBongTai = {454, 921, 2064, 2113};
            int[] idManhHon = {934, 934, 934, 934};
            int[] idDaXanhLam = {935, 935, 935, 935};
            int[] slManhHon = {39, 49, 69, 99};
            int[] slDaXanhLam = {10, 19, 29, 99};
            for (Item item : player.combineNew.itemsCombine) {
                if (Arrays.stream(idBongTai).anyMatch(id -> id == item.template.id)) {
                    bongTai = item;
                } else if (Arrays.stream(idManhHon).anyMatch(id -> id == item.template.id)) {
                    manhHon = item;
                } else if (Arrays.stream(idDaXanhLam).anyMatch(id -> id == item.template.id)) {
                    daXanhLam = item;

                }
            }
            if (bongTai != null && manhHon != null && daXanhLam != null) {
                int indexBongTai = -1;
                for (int i = 0; i < idBongTai.length; i++) {
                    if (bongTai.template.id == idBongTai[i]) {
                        indexBongTai = i;
                        break;
                    }
                }

                int sl = 5 + 5 * indexBongTai;
                if (!this.SubThoiVang(player, sl)) {
                    Service.gI().sendThongBao(player, "Cần " + sl + " thỏi vàng để thực hiện");

                } else {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhHon, slManhHon[indexBongTai]);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, daXanhLam, slDaXanhLam[indexBongTai]);

                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        bongTai.itemOptions.clear();

                        int rdUp = Util.nextInt(0, 7);
                        if (rdUp == 0) {
                            bongTai.itemOptions.add(new Item.ItemOption(50, (indexBongTai + 1) * Util.nextInt(5, 10)));
                        } else if (rdUp == 1) {
                            bongTai.itemOptions.add(new Item.ItemOption(77, (indexBongTai + 1) * Util.nextInt(5, 10)));
                        } else if (rdUp == 2) {
                            bongTai.itemOptions.add(new Item.ItemOption(103, (indexBongTai + 1) * Util.nextInt(5, 10)));
                        } else if (rdUp == 3) {
                            bongTai.itemOptions.add(new Item.ItemOption(108, (indexBongTai + 1) * Util.nextInt(5, 10)));
                        } else if (rdUp == 4) {
                            bongTai.itemOptions.add(new Item.ItemOption(94, (indexBongTai + 1) * Util.nextInt(5, 10)));
                        } else if (rdUp == 5) {
                            bongTai.itemOptions.add(new Item.ItemOption(14, (indexBongTai + 1) * Util.nextInt(5, 10)));
                        } else if (rdUp == 6) {
                            bongTai.itemOptions.add(new Item.ItemOption(80, (indexBongTai + 1) * Util.nextInt(5, 10)));
                        } else if (rdUp == 7) {
                            bongTai.itemOptions.add(new Item.ItemOption(81, (indexBongTai + 1) * Util.nextInt(5, 10)));
                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);

            }
        }
    }

    public void openSKHVIP(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thiên sứ");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ kích hoạt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.ruby < RUBY) {
                Service.gI().sendThongBao(player, "Con cần thêm hồng ngọc để đổi...");
                return;
            }
            player.inventory.ruby -= RUBY;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (itemTS.template.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.radaSKHVip[Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHVip[6];
                }
            } else {
                itemId = Manager.doSKHVip[itemTS.template.gender][itemTS.template.type][Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHVip[itemTS.template.gender][itemTS.template.type][6];
                }
            }
            int skhId = ItemService.gI().randomSKHId(itemTS.template.gender);
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTS, 1);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

//    private void dapDoKichHoat(Player player) {
//        if (player.combineNew.itemsCombine.size() == 1 || player.combineNew.itemsCombine.size() == 2) {
//            Item dhd = null, dtl = null;
//            for (Item item : player.combineNew.itemsCombine) {
//                if (item.isNotNullItem()) {
//                    if (item.template.id >= 650 && item.template.id <= 662) {
//                        dhd = item;
//                    } else if (item.template.id >= 555 && item.template.id <= 567) {
//                        dtl = item;
//                    }
//                }
//            }
//            if (dhd != null) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
//                        && player.inventory.gold >= COST_DAP_DO_KICH_HOAT) {
//                    player.inventory.gold -= COST_DAP_DO_KICH_HOAT;
//                    int tiLe = dtl != null ? 100 : 50;
//                    if (Util.isTrue(tiLe, 100)) {
//                        sendEffectSuccessCombine(player);
//                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dhd.template.gender, dhd.template.type));
//                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
//                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
//                        InventoryServiceNew.gI().addItemBag(player, item);
//                    } else {
//                        sendEffectFailCombine(player);
//                    }
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, dhd, 1);
//                    if (dtl != null) {
//                        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl, 1);
//                    }
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
//
//    private void doiVeHuyDiet(Player player) {
//        if (player.combineNew.itemsCombine.size() == 1) {
//            Item item = player.combineNew.itemsCombine.get(0);
//            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
//                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
//                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
//                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
//                    ticket.itemOptions.add(new Item.ItemOption(30, 0));
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
//                    InventoryServiceNew.gI().addItemBag(player, ticket);
//                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);
//
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            int gem = player.combineNew.gemCombine;
            int thoiVang_ = player.combineNew.countThoiVang;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            if (!this.SubThoiVang(player, thoiVang_)) {
                Service.gI().sendThongBao(player, "Không đủ thỏi vàng để thực hiện đâu cu");
                return;
            }

            Item trangBi = null;
            Item daPhaLe = null;

            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
                int star = 0; //sao pha lê đã ép
                int starEmpty = 0; //lỗ sao pha lê
                if (trangBi != null && daPhaLe != null) {
                    Item.ItemOption optionStar = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == 102) {
                            star = io.param;
                            optionStar = io;
                        } else if (io.optionTemplate.id == 107) {
                            starEmpty = io.param;
                        }
                    }
                    if (star < starEmpty) {
                        if (this.SubThoiVang(player, thoiVang_)) {
                            player.inventory.gem -= gem;
                            int optionId = getOptionDaPhaLe(daPhaLe);
                            int param = getParamDaPhaLe(daPhaLe);
                            Item.ItemOption option = null;
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id == optionId) {
                                    option = io;
                                    break;
                                }
                            }
                            if (option != null) {
                                option.param += param;
                            } else {
                                trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                            }
                            if (optionStar != null) {
                                optionStar.param++;
                            } else {
                                trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                            }
                            InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                            sendEffectSuccessCombine(player);
                        }
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
        boolean flag = false;
        int solandap = player.combineNew.quantities;
        float tiLe = player.combineNew.ratioCombine;
        while (player.combineNew.quantities > 0 && !player.combineNew.itemsCombine.isEmpty() && !flag) {
            long gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            //int thoiVang_ = player.combineNew.countThoiVang;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                break;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                break;
//            } else if (!this.SubThoiVang(player, thoiVang_)) {
//                Service.gI().sendThongBao(player, "Không đủ thỏi vàng để thực hiện đâu cu");
//                break;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                ItemOption optionStar = null;
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
//                    if (this.SubThoiVang(player, thoiVang_)) {
                    //float ratio = getRatioPhaLeHoa(star);
                    flag = Util.isTrue(tiLe, 100);
                    if (flag) {
                        if (optionStar == null) {
                            item.itemOptions.add(new ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        Service.gI().sendThongBao(player, "Lên cấp sau " + (solandap - player.combineNew.quantities + 1) + " lần đập");
                        if (optionStar != null && optionStar.param >= 7) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
//                    }
                }
            }
            player.combineNew.quantities -= 1;
        }
        if (!flag) {
            sendEffectFailCombine(player);
        }
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }

//    private void Nangcaplinhthu(Player player) {
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            if (!player.combineNew.itemsCombine.isEmpty()) {
//
//                Item bnp = player.combineNew.itemsCombine.get(0);
//                Item item = player.combineNew.itemsCombine.get(1);
//
//                if (bnp != null && bnp.isNotNullItem() && bnp.template != null && bnp.template.type == 72) {
//                    if (item != null && item.isNotNullItem() && item.template != null && item.template.id == 674) {
//                        int sl = 1000;
//
//                        if (item.template != null && item.quantity >= 1 && bnp.quantity >= 1) {
//                            if (!this.SubThoiVang(player, sl)) {
//                                Service.gI().sendThongBao(player, "Cần " + sl + " thỏi vàng để thực hiện");
//
//                            } else {
//                                int linhthu = Util.nextInt(1289, 1313);
//                                Item linhthumoi = ItemService.gI().createNewItem((short) linhthu);
//                                linhthumoi.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15 / (player.gender + 1), 30 / (player.gender + 1))));
//                                linhthumoi.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5 * (player.gender + 1), 15 * (player.gender + 1))));
//                                linhthumoi.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 40)));
//                                linhthumoi.itemOptions.add(new Item.ItemOption(5, Util.nextInt(15, 20)));
//                                InventoryServiceNew.gI().addItemBag(player, linhthumoi);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, bnp, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                reOpenItemCombine(player);
//                                if (item.template != null) {
//                                    sendEffectCombineLT(player, item.template.iconID);
//                                }
//
//                                Service.gI().sendThongBao(player, "Biến đổi linh thú thành công nhận  " + ItemService.gI().getTemplate(linhthu).name + "_Siêu thần");
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Cần ném linh thú vô và cần 1 viên đá ngũ sắc :3");
//                        }
//
//                    } else {
//                        Service.gI().sendThongBao(player, "Đá ngũ sắc đâu cu ô");
//                    }
//                } else {
//                    Service.gI().sendThongBao(player, "Ném Linh thú vào đây");
//                }
//            } else {
//                Service.gI().sendThongBao(player, "Đặt Linh thú trước rồi đá ngũ sắc");
//            }
//        } else {
//            Service.gI().sendThongBao(player, "Cần để trống 1 ô hành trang");
//        }
//    }
    private void nhapDavun(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {

                Item item = player.combineNew.itemsCombine.get(0);
                Item bnp = player.combineNew.itemsCombine.get(1);

                if (bnp != null && bnp.isNotNullItem() && bnp.template != null && bnp.template.id == 226) {
                    if (item != null && item.isNotNullItem() && item.template != null && item.template.id == 225) {
                        int sl = 1;

                        if (item.template != null && item.quantity >= 99 && bnp.quantity >= 1) {
                            if (!this.SubThoiVang(player, sl)) {
                                Service.gI().sendThongBao(player, "Cần " + sl + " thỏi vàng để thực hiện");

                            } else {
                                int dnc = item.template.id - Util.nextInt(1, 5);
                                Item nr = ItemService.gI().createNewItem((short) dnc);
                                InventoryServiceNew.gI().addItemBag(player, nr);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 99);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, bnp, 1);

                                InventoryServiceNew.gI().sendItemBags(player);
                                reOpenItemCombine(player);
                                if (item.template != null) {
                                    sendEffectCombineDV(player, item.template.iconID);
                                }
                                Service.gI().sendThongBao(player, "Nhập mảnh đá vụn thành công nhận  " + ItemService.gI().getTemplate(dnc).name + "");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Không đủ 99 mảnh đá vụn và 1 bình nước phép :3");
                        }

                    } else {
                        Service.gI().sendThongBao(player, "Mới chơi đồ à");
                    }
                } else {
                    Service.gI().sendThongBao(player, "Bình nước phép tk lỏ");
                }
            } else {
                Service.gI().sendThongBao(player, "Đặt mảnh đã vụn trước rồi tới bình nước phép");
            }
        } else {
            Service.gI().sendThongBao(player, "Cần để trống 1 ô hành trang");
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {

                Item item = player.combineNew.itemsCombine.get(0);

                if (item != null && item.isNotNullItem() && item.template != null && (item.template.id > 16 && item.template.id <= 20)) {
                    int subthoiVang_ = (20 - item.template.id);
                    int sl = subthoiVang_;

                    if (item.template != null && item.quantity >= 7) {
                        if (!this.SubThoiVang(player, sl)) {
                            Service.gI().sendThongBao(player, "Cần " + sl + " thỏi vàng để thực hiện");

                        } else {
                            int addnr = item.template.id - 1;

                            Item nr = ItemService.gI().createNewItem((short) addnr);
                            InventoryServiceNew.gI().addItemBag(player, nr);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);

                            InventoryServiceNew.gI().sendItemBags(player);
                            reOpenItemCombine(player);
                            if (item.template != null) {
                                sendEffectCombineDB(player, item.template.iconID);
                            }

                            Service.gI().sendThongBao(player, "Nhập ngọc rồng thành công nhận " + ItemService.gI().getTemplate(addnr).name + "");
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Thiếu " + (7 - item.quantity) + " viên mà cũng đòi nhập là sao :3");
                    }

                } else {
                    Service.gI().sendThongBao(player, "Mới chơi đồ à");
                }
            }
        }
    }

//        private void phanradothanlinh(Player player) {
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            if (!player.combineNew.itemsCombine.isEmpty()) {
//                Item item = player.combineNew.itemsCombine.get(0);
//                if (item != null && item.isNotNullItem() && (item.template.id > 0 && item.template.id <= 3) && item.quantity >= 1) {
//                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 78));
//                    InventoryServiceNew.gI().addItemBag(player, nr);
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
//            }
//        }
//    }
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
//                    Service.gI().sendThongBao(player, "Đã nhận được 1 điểm");
//
//                }
    private void nangCapVatPham(Player player) {
        int countDaNangCap = player.combineNew.countDaNangCap;
        long gold = player.combineNew.goldCombine;
        short countDaBaoVe = player.combineNew.countDaBaoVe;
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {

                if (player.inventory.gold < gold) {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (itemDBV == null) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                if (itemDo == null) {
                    Service.gI().sendThongBao(player, "Có lỗi xảy ra vui lòng báo admin");
                    return;
                }
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23
                                || io.optionTemplate.id == 196
                                || io.optionTemplate.id == 197
                                || io.optionTemplate.id == 198
                                || io.optionTemplate.id == 199) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        if (option == null) {
                            Service.gI().sendThongBao(player, "Chỉ số không hợp lệ để nâng cấp");
                        } else {
                            option.param += Math.max(1, option.param * 10 / 100);
                        }

                        if (option2 != null) {
                            option2.param += Math.max(1, option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
//   
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6 || level == 8) && (player.combineNew.itemsCombine.size() != 3)) {
                            if (option == null) {
                                Service.gI().sendThongBao(player, "Chỉ số không hợp lệ để nâng cấp thêm");
                            } else {
                                option.param -= Math.max(1, option.param * 10 / 100);
                            }
                            if (option2 != null) {
                                option2.param -= Math.max(1, option2.param * 10 / 100);
                            }
                            if (optionLevel == null) {
                                itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                            } else {
                                optionLevel.param--;
                            }
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

//    private void nangCapAoHoa(Player player) {
//        int countDaNangCap = player.combineNew.countDaQuy;
//        long gold = player.combineNew.goldCombine;
//        short countDaBaoVe = player.combineNew.countDaBaoVe;
//        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
//            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
//                return;
//            }
//            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
//                return;
//            }
//            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
//                return;//admin
//            }
//            Item itemDo = null;
//            Item itemDNC = null;
//            Item itemDBV = null;
//            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
//                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
//                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
//                        itemDBV = player.combineNew.itemsCombine.get(j);
//                        continue;
//                    }
//                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
//                        itemDo = player.combineNew.itemsCombine.get(j);
//                    } else {
//                        itemDNC = player.combineNew.itemsCombine.get(j);
//                    }
//                }
//            }
//            if (isCoupleItemNangCapCheck2(itemDo, itemDNC)) {
//
//                if (player.inventory.gold < gold) {
//                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
//                    return;
//                }
//                if (player.combineNew.itemsCombine.size() == 3) {
//                    if (itemDBV == null) {
//                        return;
//                    }
//                    if (itemDBV.quantity < countDaBaoVe) {
//                        return;
//                    }
//                }
//
//                int level = 0;
//                Item.ItemOption optionLevel = null;
//                if (itemDo == null) {
//                    Service.gI().sendThongBao(player, "Có lỗi xảy ra vui lòng báo admin");
//                    return;
//                }
//                for (Item.ItemOption io : itemDo.itemOptions) {
//                    if (io.optionTemplate.id == 206) {
//                        level = io.param;
//                        optionLevel = io;
//                        break;
//                    }
//                }
//                if (level < MAX_LEVEL_ITEM2) {
//                    player.inventory.gold -= gold;
//                    Item.ItemOption option = null;
//                    for (Item.ItemOption io : itemDo.itemOptions) {
//                        if (io.optionTemplate.id == 5
//                                || io.optionTemplate.id == 101
//                                || io.optionTemplate.id == 50
//                                || io.optionTemplate.id == 77
//                                || io.optionTemplate.id == 103) {
//                            option = io;
//                        }
//                    }
//                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
//                        if (option == null) {
//                            Service.gI().sendThongBao(player, "Chỉ số không hợp lệ để nâng cấp");
//                        } else {
//                            option.param += Math.max(1, option.param * 10 / 100 - 2);
//                        }
//                        if (optionLevel == null) {
//                            itemDo.itemOptions.add(new Item.ItemOption(206, 1));
//                        } else {
//                            optionLevel.param++;
//                        }
////   
//                        sendEffectSuccessCombine(player);
//                    } else {
//                        if ((level == 2 || level == 4 || level == 6 || level == 8) && (player.combineNew.itemsCombine.size() != 3)) {
//                            if (option == null) {
//                                Service.gI().sendThongBao(player, "Chỉ số không hợp lệ để nâng cấp thêm");
//                            } else {
//                                option.param -= Math.max(1, option.param * 10 / 100);
//                            }
//                            if (optionLevel == null) {
//                                itemDo.itemOptions.add(new Item.ItemOption(206, 1));
//                            } else {
//                                optionLevel.param--;
//                            }
//                        }
//                        sendEffectFailCombine(player);
//                    }
//                    if (player.combineNew.itemsCombine.size() == 3) {
//                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
//                    }
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, countDaNangCap);
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
//
//    private void antrangbi(Player player) {
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            if (!player.combineNew.itemsCombine.isEmpty()) {
//                Item item = player.combineNew.itemsCombine.get(0);
//                Item dangusac = player.combineNew.itemsCombine.get(1);
//                int star = 0;
//                Item.ItemOption optionStar = null;
//                if (item != null) {
//                    for (Item.ItemOption io : item.itemOptions) {
//                        if (io.optionTemplate.id == 34 || io.optionTemplate.id == 35 || io.optionTemplate.id == 36) {
//                            star = io.param;
//                            optionStar = io;
//                            break;
//                        }
//                    }
//                }
//                if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && (dangusac.template.id == 1314 || dangusac.template.id == 1315 || dangusac.template.id == 1316) && dangusac.quantity >= 99) {
//                    if (optionStar == null) {
//                        if (dangusac.template.id == 1314) {
//                            item.itemOptions.add(new Item.ItemOption(34, 1));
//                            sendEffectSuccessCombine(player);
//                        } else if (dangusac.template.id == 1315) {
//                            item.itemOptions.add(new Item.ItemOption(35, 1));
//                            sendEffectSuccessCombine(player);
//                        } else if (dangusac.template.id == 1316) {
//                            item.itemOptions.add(new Item.ItemOption(36, 1));
//                            sendEffectSuccessCombine(player);
//                        }
////                    InventoryServiceNew.gI().addItemBag(player, item);
//                        InventoryServiceNew.gI().subQuantityItemsBag(player, dangusac, 99);
//                        InventoryServiceNew.gI().sendItemBags(player);
//                        reOpenItemCombine(player);
////                    sendEffectCombineDB(player, item.template.iconID);
//                    } else {
//                        Service.gI().sendThongBao(player, "Trang bị của bạn có ấn rồi mà !!!");
//                    }
//                }
//            }
//        }
//    }
//
//    private void psHoaTrangBi(Player player) {
//
//        if (player.combineNew.itemsCombine.size() != 2) {
//            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHacHoa()).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu trang bị pháp sư");
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 2187).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu đá pháp sư");
//            return;
//        }
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            if (player.inventory.gold < 2000000000) {
//                Service.gI().sendThongBao(player, "Con cần 2 tỉ vàng để đổi...");
//                return;
//            }
//            player.inventory.gold -= 2000000000;
//            Item daHacHoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 2187).findFirst().get();
//            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHacHoa).findFirst().get();
//            if (daHacHoa == null) {
//                Service.gI().sendThongBao(player, "Thiếu đá pháp sư");
//                return;
//            }
//            if (trangBiHacHoa == null) {
//                Service.gI().sendThongBao(player, "Thiếu trang bị pháp sư");
//                return;
//            }
//
////            if (trangBiHacHoa != null) {
//            for (ItemOption itopt : trangBiHacHoa.itemOptions) {
//                if (itopt.optionTemplate.id == 220) {
//                    if (itopt.param >= 8) {
//
//                        Service.gI().sendThongBao(player, "Trang bị đã đạt tới giới hạn pháp sư");
//                        return;
//                    }
//                }
//            }
////            }
//
//            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
//                sendEffectSuccessCombine(player);
//                List<Integer> idOptionHacHoa = Arrays.asList(221, 222, 223, 224);
//                int randomOption = idOptionHacHoa.get(Util.nextInt(0, 3));
//                if (!trangBiHacHoa.haveOption(220)) {
//                    trangBiHacHoa.itemOptions.add(new ItemOption(220, 1));
//                } else {
//                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
//                        if (itopt.optionTemplate.id == 220) {
//                            itopt.param += 1;
//                            break;
//                        }
//                    }
//                }
//                if (!trangBiHacHoa.haveOption(randomOption)) {
//                    trangBiHacHoa.itemOptions.add(new ItemOption(randomOption, 3));
//                } else {
//                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
//                        if (itopt.optionTemplate.id == randomOption) {
//                            itopt.param += 1;
//                            break;
//                        }
//                    }
//                }
//                player.combineNew.ratioCombine = 0;
//                Service.gI().sendThongBao(player, "Bạn đã nâng cấp thành công");
//            } else {
//
//                sendEffectFailCombine(player);
//
//            }
//            InventoryServiceNew.gI().subQuantityItemsBag(player, daHacHoa, 1);
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.gI().sendMoney(player);
//            player.combineNew.itemsCombine.clear();
//            reOpenItemCombine(player);
//        } else {
//            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
//        }
//    }
//
//    private void tayHacHoaTrangBi(Player player) {
//
//        if (player.combineNew.itemsCombine.size() != 2) {
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHacHoa()).count() != 1) {
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 2188).count() != 1) {
//            return;
//        }
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            if (player.inventory.ruby < 20000) {
//                Service.gI().sendThongBao(player, "Con cần 20k hồng ngọc để tẩy...");
//                return;
//            }
//            player.inventory.ruby -= 20000;
//            Item buagiaihachoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 2188).findFirst().get();
//            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHacHoa).findFirst().get();
//            if (buagiaihachoa == null) {
//                Service.gI().sendThongBao(player, "Thiếu trang bị cần tẩy");
//                return;
//            }
//            if (trangBiHacHoa == null) {
//                Service.gI().sendThongBao(player, "Thiếu ngọc tẩy");
//                return;
//            }
//
//            if (Util.isTrue(100, 100)) {
//                sendEffectSuccessCombine(player);
//
//                ItemOption option_220 = new ItemOption();
//                ItemOption option_221 = new ItemOption();
//                ItemOption option_222 = new ItemOption();
//                ItemOption option_223 = new ItemOption();
//                ItemOption option_224 = new ItemOption();
//                ItemOption option_241 = new ItemOption();
//                ItemOption option_242 = new ItemOption();
//                ItemOption option_243 = new ItemOption();
//                ItemOption option_244 = new ItemOption();
//                ItemOption option_245 = new ItemOption();
//                ItemOption option_246 = new ItemOption();
//                ItemOption option_247 = new ItemOption();
//                ItemOption option_232 = new ItemOption();
//                ItemOption option_233 = new ItemOption();
//                ItemOption option_234 = new ItemOption();
//                ItemOption option_235 = new ItemOption();
//                ItemOption option_226 = new ItemOption();
//                ItemOption option_227 = new ItemOption();
//                ItemOption option_228 = new ItemOption();
//                ItemOption option_229 = new ItemOption();
//                ItemOption option_225 = new ItemOption();
//                ItemOption option_206 = new ItemOption();
////                ItemOption option_107 = new ItemOption();
//                ItemOption option_102 = new ItemOption();
//                ItemOption option_50 = new ItemOption();
//                ItemOption option_103 = new ItemOption();
//                ItemOption option_77 = new ItemOption();
//
//                ItemOption option_80 = new ItemOption();
//                ItemOption option_81 = new ItemOption();
//
//                ItemOption option_5 = new ItemOption();
//                ItemOption option_2 = new ItemOption();
//                ItemOption option_8 = new ItemOption();
//                ItemOption option_19 = new ItemOption();
//                ItemOption option_27 = new ItemOption();
//                ItemOption option_28 = new ItemOption();
//                ItemOption option_16 = new ItemOption();
//                ItemOption option_94_101 = new ItemOption();
//                ItemOption option_108 = new ItemOption();
//                ItemOption option_109 = new ItemOption();
//                ItemOption option_114 = new ItemOption();
//                ItemOption option_117 = new ItemOption();
//                ItemOption option_153 = new ItemOption();
//                ItemOption option_156 = new ItemOption();
//                ItemOption option_3 = new ItemOption();
//                ItemOption option_72 = new ItemOption();
//
//                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
//                    if (trangBiHacHoa.template.type == 5) {
//                        switch (itopt.optionTemplate.id) {
//                            case 226:
//                                System.out.println("226");
//                                option_226 = itopt;
//                                break;
//                            case 227:
//                                System.out.println("227");
//                                option_227 = itopt;
//                                break;
//                            case 228:
//                                System.out.println("228");
//                                option_228 = itopt;
//                                break;
//                            case 229:
//                                System.out.println("229");
//                                option_229 = itopt;
//                                break;
//                            case 225:
//                                System.out.println("225");
//                                option_225 = itopt;
//                                break;
//                        }
//                    } else if (trangBiHacHoa.template.type == 21 || trangBiHacHoa.template.type == 72 || trangBiHacHoa.template.type == 11) {
//                        switch (itopt.optionTemplate.id) {
//                            case 220:
//                                System.out.println("220");
//                                option_220 = itopt;
//                                break;
//                            case 221:
//                                System.out.println("221");
//                                option_221 = itopt;
//                                break;
//                            case 222:
//                                System.out.println("222");
//                                option_222 = itopt;
//                                break;
//                            case 223:
//                                System.out.println("223");
//                                option_223 = itopt;
//                                break;
//                            case 224:
//                                System.out.println("224");
//                                option_224 = itopt;
//                                break;
//                            case 241:
//                                System.out.println("241");
//                                option_241 = itopt;
//                                break;
//                            case 242:
//                                System.out.println("242");
//                                option_242 = itopt;
//                                break;
//                            case 243:
//                                System.out.println("243");
//                                option_243 = itopt;
//                                break;
//                            case 244:
//                                System.out.println("244");
//                                option_244 = itopt;
//                                break;
//                            case 245:
//                                System.out.println("245");
//                                option_245 = itopt;
//                                break;
//                            case 246:
//                                System.out.println("246");
//                                option_246 = itopt;
//                                break;
//                            case 247:
//                                System.out.println("247");
//                                option_247 = itopt;
//                                break;
//                        }
//                    } else if (trangBiHacHoa.template.type == 32) {
//                        switch (itopt.optionTemplate.id) {
//                            case 2:
//                                System.out.println("2");
//                                option_2 = itopt;
//                                break;
//                            case 8:
//                                System.out.println("8");
//                                option_8 = itopt;
//                                break;
//                            case 19:
//                                System.out.println("19");
//                                option_19 = itopt;
//                                break;
//                            case 27:
//                                System.out.println("27");
//                                option_27 = itopt;
//                                break;
//                            case 28:
//                                System.out.println("28");
//                                option_28 = itopt;
//                                break;
//                            case 16:
//                                System.out.println("16");
//                                option_16 = itopt;
//                                break;
//                            case 80:
//                            case 81:
//                            case 94:
//                            case 95:
//                            case 96:
//                            case 97:
//                            case 98:
//                            case 99:
//                            case 100:
//                            case 101:
//                                System.out.println("94-101");
//                                option_94_101 = itopt;
//                                break;
//                            case 108:
//                                System.out.println("108");
//                                option_108 = itopt;
//                                break;
//                            case 109:
//                                System.out.println("109");
//                                option_109 = itopt;
//                                break;
//                            case 114:
//                                System.out.println("114");
//                                option_114 = itopt;
//                                break;
//                            case 117:
//                                System.out.println("117");
//                                option_117 = itopt;
//                                break;
//                            case 153:
//                                System.out.println("153");
//                                option_153 = itopt;
//                                break;
//                            case 156:
//                                System.out.println("156");
//                                option_156 = itopt;
//                                break;
//                            case 3:
//                                System.out.println("3");
//                                option_3 = itopt;
//                                break;
//                            case 72:
//                                System.out.println("72");
//                                option_72 = itopt;
//                                break;
////                            case 107:
////                                System.out.println("107");
////                                option_107 = itopt;
////                                break;
//                            case 102:
//                                System.out.println("102");
//                                option_102 = itopt;
//                                break;
//                            case 50:
//                                System.out.println("50");
//                                option_50 = itopt;
//                                break;
//                            case 103:
//                                System.out.println("103");
//                                option_103 = itopt;
//                                break;
//                            case 77:
//                                System.out.println("77");
//                                option_77 = itopt;
//                                break;
//                            case 5:
//                                System.out.println("5");
//                                option_5 = itopt;
//                                break;
//                        }
//                    } else if (trangBiHacHoa.template.type < 5) {
//                        switch (itopt.optionTemplate.id) {
//
//                            case 206:
//                                System.out.println("206");
//                                option_206 = itopt;
//                                break;
////                            case 107:
////                                System.out.println("107");
////                                option_107 = itopt;
////                                break;
//                            case 102:
//                                System.out.println("102");
//                                option_102 = itopt;
//                                break;
//                            case 50:
//                                System.out.println("50");
//                                option_50 = itopt;
//                                break;
//                            case 103:
//                                System.out.println("103");
//                                option_103 = itopt;
//                                break;
//                            case 77:
//                                System.out.println("77");
//                                option_77 = itopt;
//                                break;
//                            case 5:
//                                System.out.println("5");
//                                option_5 = itopt;
//                                break;
//                            case 80:
//                            case 81:
//                            case 94:
//                            case 95:
//                            case 96:
//                            case 97:
//                            case 98:
//                            case 99:
//                            case 100:
//                            case 101:
//                                System.out.println("94-101");
//                                option_94_101 = itopt;
//                                break;
//                            default:
//                                break;
//                        }
//                    } else if (trangBiHacHoa.template.type == 25) {
//                        if (trangBiHacHoa.template.id > 1185 && trangBiHacHoa.template.id <= 1193) {
//                            trangBiHacHoa.template = ItemService.gI().getTemplate(1185);
//                        }
//                        switch (itopt.optionTemplate.id) {
////                            case 107:
////                                System.out.println("107");
////                                option_107 = itopt;
////                                break;
//                            case 102:
//                                System.out.println("102");
//                                option_102 = itopt;
//                                break;
//                            case 232:
//                                System.out.println("232");
//                                option_232 = itopt;
//                                break;
//                            case 233:
//                                System.out.println("233");
//                                option_233 = itopt;
//                                break;
//                            case 234:
//                                System.out.println("234");
//                                option_234 = itopt;
//                                break;
//                            case 235:
//                                System.out.println("235");
//                                option_235 = itopt;
//                                break;
//                            case 72:
//                                System.out.println("72");
//                                option_72 = itopt;
//                                break;
//                            case 50:
//                                System.out.println("50");
//                                option_50 = itopt;
//                                break;
//                            case 103:
//                                System.out.println("103");
//                                option_103 = itopt;
//                                break;
//                            case 77:
//                                System.out.println("77");
//                                option_77 = itopt;
//                                break;
//                            case 5:
//                                System.out.println("5");
//                                option_5 = itopt;
//                                break;
//                        }
//                    }
//                }
//
//                ItemOption[] options = {
//                    option_220, option_221, option_222, option_223, option_224,
//                    option_241, option_242, option_243, option_244, option_245,
//                    option_246, option_247, option_232, option_234, option_235,
//                    option_226, option_227, option_228, option_229, option_225,
//                    option_206, //option_107,
//                    option_102, option_80, option_81,
//                    option_50, option_103,
//                    option_77, option_5, option_2, option_8, option_19,
//                    option_27, option_28, option_16, option_94_101, option_72,
//                    option_108, option_233,
//                    option_109, option_114, option_117, option_153, option_156,
//                    option_3
//                };
//
//                for (ItemOption option : options) {
//                    if (option != null) {
//                        trangBiHacHoa.itemOptions.remove(option);
//                    }
//                }
//
//                player.combineNew.ratioCombine = 0;
//                Service.gI().sendThongBao(player, "Bạn đã tẩy thành công");
//                InventoryServiceNew.gI().sendItemBags(player);
//            } else {
//                sendEffectFailCombine(player);
//            }
//            InventoryServiceNew.gI().subQuantityItemsBag(player, buagiaihachoa, 1);
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.gI().sendMoney(player);
//            player.combineNew.itemsCombine.clear();
//            reOpenItemCombine(player);
//        }
//    }
//
//    private void tayDoKhoaGD(Player player) {
//
//        if (player.combineNew.itemsCombine.size() != 2) {
//            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && (item.isTrangBiKhoaGd())).count() != 1) {
//            Service.gI().sendThongBao(player, "Thiếu Item khóa giao dịch1");
//            return;
//        }
//        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1318).count() != 1) {
//            Service.gI().sendThongBao(player, "Cần Đá Hoàng Kim mua tại Bà Hạt Mít");
//            return;
//        }
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            if (player.inventory.ruby < 2000) {
//                Service.gI().sendThongBao(player, "Con cần 20k hồng ngọc để tẩy...");
//                return;
//            }
//            player.inventory.ruby -= 2000;
//            Item dahoangkim = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1318).findFirst().get();
//            Item trangBiKhoagd = player.combineNew.itemsCombine.stream().filter((item -> item.isTrangBiKhoaGd())).findFirst().get();
//            if (dahoangkim == null) {
//                Service.gI().sendThongBao(player, "Cần Đá Hoàng Kim mua tại Bà Hạt Mít");
//                return;
//            }
//            if (trangBiKhoagd == null) {
//                Service.gI().sendThongBao(player, "Cần Item khóa giao dịch2");
//                return;
//            }
//
//            if (Util.isTrue(30, 100)) {
//                sendEffectSuccessCombine(player);
//                List<Integer> idOptionHacHoa = Arrays.asList(30);
//
//                ItemOption option_30 = new ItemOption();
//
//                for (ItemOption itopt : trangBiKhoagd.itemOptions) {
//                    if (itopt.optionTemplate.id == 30) {
//                        System.out.println("30 _ mở khoa gd");
//                        option_30 = itopt;
//                    }
//                }
//                if (option_30 != null) {
//                    trangBiKhoagd.itemOptions.add(new Item.ItemOption(73, 0));
//                    trangBiKhoagd.itemOptions.remove(option_30);
//                }
//                player.combineNew.ratioCombine = 0;
//                Service.gI().sendThongBao(player, "Bạn đã mở khóa gd thành công");
//                InventoryServiceNew.gI().sendItemBags(player);
//            } else {
//                Service.gI().sendThongBao(player, "Bạn đã mở khóa gd thất bại");
//                sendEffectFailCombine(player);
//            }
//            InventoryServiceNew.gI().subQuantityItemsBag(player, dahoangkim, 1);
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.gI().sendMoney(player);
//            player.combineNew.itemsCombine.clear();
//            reOpenItemCombine(player);
//        }
//    }
    //--------------------------------------------------------------------------
    /**
     * r
     * Hiệu ứng mở item
     *
     * @param player
     * @param icon1
     * @param icon2
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    public void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDV(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DA_VUN);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void sendEffectCombineLT(Player player, short icon) {
//        Message msg;
//        try {
//            msg = new Message(-81);
//            msg.writer().writeByte(COMBINE_DA_VUN);
//            msg.writer().writeShort(icon);
//            player.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    //--------------------------------------------------------------------------Ratio, cost combine
    private int getDiemNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 20;
            case 1:
                return 30;
            case 2:
                return 40;
            case 3:
                return 50;
            case 4:
                return 60;
            case 5:
                return 70;
            case 6:
                return 80;
            case 7:
                return 100;
        }
        return 0;
    }

    private int getDaNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 30;
            case 1:
                return 35;
            case 2:
                return 40;
            case 3:
                return 45;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 65;
            case 7:
                return 80;
        }
        return 0;
    }

    private float getTiLeNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 60f;
            case 1:
                return 40f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 10f;
            case 5:
                return 8f;
            case 6:
                return 4f;
            case 7:
                return 1f;
        }
        return 0;
    }

//    private int getDiemNangcapSieuHoa(int star) {
//        switch (star) {
//            case 0:
//                return 10;
//            case 1:
//                return 12;
//            case 2:
//                return 14;
//            case 3:
//                return 16;
//            case 4:
//                return 18;
//            case 5:
//                return 20;
//            case 6:
//                return 22;
//            case 7:
//                return 24;
//            case 8:
//                return 26;
//            case 9:
//                return 28;
//            case 10:
//                return 30;
//            case 11:
//                return 32;
//            case 12:
//                return 34;
//            case 13:
//                return 36;
//            case 14:
//                return 38;
//            case 15:
//                return 40;
//            case 16:
//                return 42;
//            case 17:
//                return 44;
//            case 18:
//                return 46;
//            case 19:
//                return 48;
//            case 20:
//                return 50;
//        }
//        return 0;
//    }
//
//    private int getDaNangcapSieuHoa(int star) {
//        switch (star) {
//            case 0:
//                return 10;
//            case 1:
//                return 12;
//            case 2:
//                return 14;
//            case 3:
//                return 16;
//            case 4:
//                return 18;
//            case 5:
//                return 20;
//            case 6:
//                return 22;
//            case 7:
//                return 24;
//            case 8:
//                return 26;
//            case 9:
//                return 28;
//            case 10:
//                return 30;
//            case 11:
//                return 32;
//            case 12:
//                return 34;
//            case 13:
//                return 36;
//            case 14:
//                return 38;
//            case 15:
//                return 40;
//            case 16:
//                return 42;
//            case 17:
//                return 44;
//            case 18:
//                return 46;
//            case 19:
//                return 48;
//            case 20:
//                return 50;
//        }
//        return 0;
//    }
//
//    private float getTiLeNangcapSieuHoa(int star) {
//        switch (star) {
//            case 0:
//                return 100f;
//            case 1:
//                return 90f;
//            case 2:
//                return 80f;
//            case 3:
//                return 70f;
//            case 4:
//                return 60f;
//            case 5:
//                return 55f;
//            case 6:
//                return 50f;
//            case 7:
//                return 45f;
//            case 8:
//                return 40f;
//            case 9:
//                return 35f;
//            case 10:
//                return 30f;
//            case 11:
//                return 29f;
//            case 12:
//                return 28f;
//            case 13:
//                return 25f;
//            case 14:
//                return 22f;
//            case 15:
//                return 18f;
//            case 16:
//                return 16f;
//            case 17:
//                return 12f;
//            case 18:
//                return 10f;
//            case 19:
//                return 9f;
//            case 20:
//                return 8f;
//        }
//        return 0;
//    }
//
//    private int getDaNangcapTinhThach(int star) {
//        switch (star) {
//            case 0:
//                return 1;
//            case 1:
//                return 2;
//            case 2:
//                return 3;
//            case 3:
//                return 4;
//            case 4:
//                return 5;
//            case 5:
//                return 6;
//            case 6:
//                return 7;
//            case 7:
//                return 8;
//            case 8:
//                return 9;
//            case 9:
//                return 10;
//
//        }
//        return 0;
//    }
//
//    private float getTiLeNangcapTinhThach(int star) {
//        switch (star) {
//            case 0:
//                return 90f;
//            case 1:
//                return 80f;
//            case 2:
//                return 70f;
//            case 3:
//                return 60f;
//            case 4:
//                return 50f;
//            case 5:
//                return 40f;
//            case 6:
//                return 30f;
//            case 7:
//                return 20f;
//            case 8:
//                return 10f;
//            case 9:
//                return 5f;
//        }
//        return 0;
//    }
    private int getGoldPhaLeHoa(int star) {

        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 7000000;
            case 2:
                return 10000000;
            case 3:
                return 12000000;
            case 4:
                return 15000000;
            case 5:
                return 20000000;
            case 6:
                return 25000000;
            case 7:
                return 33333333;
            case 8:
                return 45000000;
            case 9:
                return 60000000;
            case 10:
                return 70000000;
            case 11:
                return 100000000;
            case 12:
                return 130000000;
//            case 13:
//                return 160000000;
//            case 14:
//                return 200000000;
//            case 15:
//                return 250000000;
//            case 16:
//                return 580000000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 100f;
            case 1:
                return 100f;
            case 2:
                return 100f;
            case 3:
                return 100f;
            case 4:
                return 80f;
            case 5:
                return 70f;
            case 6:
                return 50f;
            case 7: // 7 sao
                return 20f;
            case 8:
                return 10f;
            case 9:
                return 5f;
            case 10:
                return 2.5f;
            case 11:
                return 1.25f;
            case 12: // 7 sao
                return 0.75f;
//            case 13:
//                return 0.075f;
//            case 14:
//                return 0.075f;
//            case 15:
//                return 0.075f;
//            case 16:
//                return 0.075f;
        }

        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 15;
            case 2:
                return 20;
            case 3:
                return 30;
            case 4:
                return 40;
            case 5:
                return 60;
            case 6:
                return 80;
            case 7:
                return 100;
            case 8:
                return 120;
            case 9:
                return 140;
            case 10:
                return 160;
            case 11:
                return 180;
            case 12:
                return 200;
//            case 13:
//                return 300;
//            case 14:
//                return 400;
//            case 15:
//                return 500;
//            case 16:
//                return 600;
        }
        return 0;
    }

    private int getCountThoiVang(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            case 8:
                return 9;
            case 9:
                return 10;
            case 10:
                return 12;
            case 11:
                return 5;
            case 12:
                return 6;
            case 13:
                return 7;
            case 14:
                return 8;
            case 15:
                return 9;
            case 16:
                return 10;
        }
        return 0;
    }

//    private int getCountThoiVang2(int lever) {
//        switch (lever) {
//            case 0:
//                return 0;
//            case 1:
//                return 0;
//            case 2:
//                return 0;
//            case 3:
//                return 0;
//            case 4:
//                return 0;
//            case 5:
//                return 1;
//            case 6:
//                return 1;
//            case 7:
//                return 1;
//            case 8:
//                return 1;
//            case 9:
//                return 2;
//            case 10:
//                return 2;
//
//        }
//        return 0;
//    }
    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 5;
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 50;
            case 4:
                return 70;
            case 5:
                return 80;
            case 6:
                return 100;
            case 7:
                return 200;
            case 8:
                return 250;
            case 9:
                return 300;
            case 10:
                return 400;
        }
        return 0;
    }

    private float getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 50f;
            case 1:
                return 35f;
            case 2:
                return 25.5f;
            case 3:
                return 17.5f;
            case 4:
                return 10f;
            case 5:
                return 5f;
            case 6:
                return 1f;
            case 7: // 7 sao
                return 0.5f;
            case 8:
                return 0.3f;
            case 9:
                return 0.2f;
            case 10:
                return 0.1f;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
            case 8:
                return 80;
            case 9:
                return 90;
            case 10:
                return 150;
        }
        return 0;
    }

    private int getCountDaQuy(int level) {
        switch (level) {
            case 0:
                return 10;
            case 1:
                return 15;
            case 2:
                return 20;
            case 3:
                return 25;
            case 4:
                return 35;
            case 5:
                return 40;
            case 6:
                return 50;
            case 7:
                return 60;
            case 8:
                return 70;
            case 9:
                return 99;
            case 10:
                return 150;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 100000;
            case 1:
                return 300000;
            case 2:
                return 700000;
            case 3:
                return 1500000;
            case 4:
                return 7000000;
            case 5:
                return 23000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
            case 8:
                return 300000000;
            case 9:
                return 450000000;
        }
        return 0;
    }

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck2(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 2162) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 2163) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 2164) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 2165) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 2130) {
                return true;

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck3(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if ((trangBi.template.id >= 529 && trangBi.template.id <= 531 || trangBi.template.id >= 534 && trangBi.template.id <= 536) && daNangCap.template.id == 1467) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private boolean isDaPhaLe(Item item) {
        return item != null && item.isNotNullItem() && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20));
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 529 && item.template.id <= 531 || item.template.id >= 534 && item.template.id <= 536) {
                return true;
            }
            if (item.template.type <= 4
                    // || item.template.type == 11//vật phẩm đeo lưng
                    //                    || item.template.type == 32//giáp tập luyện
                    //  || item.template.type == 23//ván bay
                    // || item.template.type == 21//pet
                    || (item.template.id >= 1149 && item.template.id <= 1151)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiAn(Item item) {
        if (item != null && item.isNotNullItem()) {
//            if (item.template.type <= 5) {
            if (item.template.id >= 650 && item.template.id <= 662) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isNhapNr(Item item) {
        if (item != null && item.isNotNullItem() && item.template.type == 30) {
            if (item.template.id >= 14 || item.template.id <= 20) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean ThoiVang(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id == 457));
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5;
            case 19:
                return 5;
            case 18:
                return 5;
            case 17:
                return 5;
            case 16:
                return 3;
            case 15:
                return 2;
            case 14:
                return 5;
            case 441:
                return 5;
            case 442:
                return 5;
            case 443:
                return 5;
            case 444:
                return 5;
            case 445:
                return 5;
            case 446:
                return 5;
            case 447:
                return 5;
            case 964:
                return 5;
            case 965:
                return 5;
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77; // hp
            case 19:
                return 103; // ki
            case 18:
                return 80; // hp 30s
            case 17:
                return 81; // mp 30s
            case 16:
                return 50; // sức đánh
            case 15:
                return 94; // giáp %
            case 14:
                return 108; // né đòn
            case 441:
                return 95; // hút hp
            case 442:
                return 96; // hút ki
            case 443:
                return 97; // phả sát thương
            case 444:
                return 98; // xuyên giáp chưởng
            case 445:
                return 99; // xuyên giáp đấm
            case 446:
                return 100; // vàng rơi từ quái
            case 447:
                return 19; // tấn công % khi đánh quái
            case 964:
                return 50; // chí mạng
            case 965:
                return 50; // sức đánh
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case LAM_PHEP_NHAP_DA:
                return "Ta sẽ phù phép\n99 mảnh đa vụn của ngươi\nthành đá nâng cấp";
            case NANG_CAP_LINH_THU:
                return "Ta sẽ phù phép\nLinh thú cùi của ngươi\nthành Linh Thú cấp thần";
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case NANG_GIAP_LUYEN_TAP:
                return "Ta sẽ phù phép cho giáp luyện tập của ngươi trở lên mạnh mẽ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai của ngươi trở nên mạnh hơn";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case PHAN_RA_DO_THAN_LINH:
                return "Ta sẽ phân rã \n  trang bị của người thành điểm!";
            case NANG_CAP_DO_TS:
                return "Ta sẽ nâng cấp \n  trang bị của người thành\n đồ thiên sứ!";
            case NANG_CAP_SKH_VIP:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
            case NANG_CAI_TRANG:
                return "Ta sẽ giúp ngươi nâng cấp cải trang có thuộc tính cao hơn\n Cũng có thể về chỉ số vô cùng cùi";
            case NANG_TL_LEN_HUY_DIET:
                return "Ta sẽ nâng cấp đồ thần linh của con lên huỷ diệt tương ứng";
            case NANG_HUY_DIET_LEN_SKH:
                return "Ta sẽ nâng cấp đồ huỷ diệt của con lên đồ kích hoạt thường";
            case NANG_HUY_DIET_LEN_SKH_VIP:
                return "Ta sẽ nâng cấp đồ huỷ diệt của con lên đồ kích hoạt VIP";
            case NANG_CAP_CHAN_MENH:
                return "Ta sẽ Nâng cấp\nChân Mệnh của ngươi\ncao hơn một bậc";
            case GIA_HAN_VAT_PHAM:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\nthêm hạn sử dụng";
            case SIEU_HOA:
                return "Ta sẽ giúp con siêu hóa\n Cải trang";
            case TINH_THACH_HOA:
                return "Ta sẽ giúp con Tinh Thạch đồ";
            case DAP_DO_AO_HOA:
                return "Ta sẽ giúp ngươi ảo hóa đồ để có thuộc tính cao hơn";
            case PS_HOA_TRANG_BI:
                return "Pháp sư hóa linh thú, ván bay";
            case TAY_PS_HOA_TRANG_BI:
                return "Tẩy pháp sư hóa";
            case MO_KHOA_ITEM:
                return "Mở Khóa giao dịch Item";
            case AN_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị Ấn";
            case CHE_TAO_AO_VO_CUC_TU_TAI:
                return "Ta sẽ chế tạo \n cho ngươi\n Áo Vô Cực Tự Tại!";
            case CHE_TAO_QUAN_VO_CUC_TU_TAI:
                return "Ta sẽ chế tạo \n cho ngươi\n Quần Vô Cực Tự Tại!";
            case CHE_TAO_GANG_VO_CUC_TU_TAI:
                return "Ta sẽ chế tạo \n cho ngươi\n Găng Vô Cực Tự Tại!";
            case CHE_TAO_GIAY_VO_CUC_TU_TAI:
                return "Ta sẽ chế tạo \n cho ngươi\n Giày Vô Cực Tự Tại!";
            case CHE_TAO_TRANG_SUC_VO_CUC_TU_TAI:
                return "Ta sẽ chế tạo \n cho ngươi\n Trang Sức Vô Cực Tự Tại!";

            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case LAM_PHEP_NHAP_DA:
                return "Chọn 99 mảnh đá vụn\n Chọn 1 bình nước phép\n Bấm nâng cấp";
            case NANG_CAP_LINH_THU:
                return "Chọn 1 linh thú\n Chọn 1 đá ngũ sắc\n Bấm nâng cấp";
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) \nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)"
                        + "\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";

            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\nChọn loại bông tai tương ứng\n"
                        + "Bông tai Potara + 5999 mảnh vỡ bông tai\n"
                        + "Bông tai cấp 2 + 9999 mảnh vỡ bông tai\n"
                        + "Bông tai cấp 3 + 19999 mảnh vỡ bông tai\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + " Xịt mất 10% mảnh vỡ hoặc hoa cải";

            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn loại bông tai tương ứng,\n"
                        + "Mảnh hồn bông tai và đá xanh lam \n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Bông tai cấp cao chỉ số càng cao";

            case PHAN_RA_DO_THAN_LINH:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để phân rã\n"
                        + "Sau đó chọn 'Phân Rã'";
            case NANG_CAP_DO_TS:
                return "vào hành trang\nChọn 2 trang bị hủy diệt bất kì\nkèm 1 món đồ thần linh\n và 5 mảnh thiên sứ\n "
                        + "sẽ cho ra đồ thiên sứ từ 0-15% chỉ số"
                        + "Sau đó chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_VIP:
                return "vào hành trang\nChọn 1 trang bị thiên sứ bất kì\nChọn tiếp ngẫu nhiên 2 món SKH thường \n "
                        + " đồ SKH VIP sẽ cùng loại \n với đồ thiên sứ!"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAI_TRANG:
                return "Vào hành trang\nChọn 1 cải trang bất kì\n "
                        + "Cần 5 Thỏi Vàng\n 1 dưa hấu"
                        + " Có thể thêm đá bảo vệ để tăng tỉ lệ thành công"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case CHE_TAO_TRANG_BI_TS:
                return "Cần 1 công thức vip\n"
                        + "Mảnh trang bị tương ứng\n"
                        + "1 đá nâng cấp (tùy chọn)"
                        + "1 đá may mắn (tùy chọn)"
                        + "Sau đó chọn 'Nâng cấp'";

            case NANG_TL_LEN_HUY_DIET:
                return "Vào hành trang\n chọn 1 món thần linh bất kỳ, sau đó nhấn 'Nâng cấp'";
            case NANG_HUY_DIET_LEN_SKH:
                return "Vào hành trang\n Chọn 1 món huỷ diệt bất kỳ, sau đó chọn 'Nâng câp'";
            case NANG_HUY_DIET_LEN_SKH_VIP:
                return "Vào hành trang\n Chọn 3 món huỷ diệt bất kỳ, món đầu tiên sẽ làm gốc, sau đó chọn 'Nâng cấp'";
            case NANG_CAP_CHAN_MENH:
                return "Vào hành trang\nChọn Chân mệnh muốn nâng cấp\nChọn Đá Hoàng Kim\n"
                        + "Sau đó chọn 'Nâng cấp'\n\n"
                        + "Lưu ý: Khi Nâng cấp Thành công sẽ tăng 10% chỉ số của cấp trước đó";

            case DAP_DO_AO_HOA:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)"
                        + "\nChọn loại đá quý để nâng cấp\n"
                        + "\nCó thể thêm đá bảo vệ để tránh tụt cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PS_HOA_TRANG_BI:
                return "vào hành trang\nChọn 1 trang bị có thể hắc hóa ( linh thú, chân mệnh, ván bay,..) và đá pháp sư \n "
                        + " để nâng cấp chỉ số pháp sư"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case TAY_PS_HOA_TRANG_BI:
                return "vào hành trang\nChọn 1 trang bị có thể hắc hóa ( phụ kiên,ngọc bội,pet,..) và bùa giải pháp sư \n "
                        + " để xoá nâng cấp chỉ số pháp sư"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case MO_KHOA_ITEM:
                return "vào hành trang\nChọn 1 trang bị khóa giao dịch ( bông tai, item sự kiện, thỏi vàng,..) và Đá Hoàng Kim \n "
                        + " để mở khóa giao dịch Item"
                        + "Chỉ cần chọn 'Mở Khóa'";
            case AN_TRANG_BI:
                return "Vào hành trang\nChọn 1 Trang bị(Áo, Quần ,Giày ,Găng ,Rada) Hủy Diệt và 99 mảnh Ấn\nSau đó chọn 'Làm phép'\n--------\nTinh ấn (5 món +30%HP)\n Nhật ấn (5 món +30%KI\n Nguyệt ấn (5 món +20%SD)";

            case GIA_HAN_VAT_PHAM:
                return "Vào hành trang\n"
                        + "Chọn 1 trang bị có hạn sử dụng\n"
                        + "Chọn thẻ gia hạn\n"
                        + "Sau đó chọn 'Gia hạn'";
            case SIEU_HOA:
                return "Vào hành trang\n"
                        + "Chọn 1 Cải trang\n"
                        + "Chọn Đá Siêu Hóa\n"
                        + "Sau đó chọn 'Nâng Cấp'";
            case TINH_THACH_HOA:
                return "Vào hành trang\n"
                        + "Chọn 1 Vật Phẩm (Pet, Linh Thú, VPDL)\n"
                        + "Chọn 1 loại đá Tinh thạch\n"
                        + "Sau đó chọn 'Nâng Cấp'";
            case NANG_GIAP_LUYEN_TAP:
                return "Vào hành trang\n"
                        + "Chọn 1 Giáp luyện tập\n"
                        + "Chọn đá hổ phách\n"
                        + "Sau đó chọn 'Nâng Cấp'";
            case CHE_TAO_AO_VO_CUC_TU_TAI:
                return "Vào hành trang\n"
                        + "Chọn 4 loại Đan trúc cơ x99\n"
                        + "99.999 Linh khí\n"
                        + "999 Thỏi vàng\n"
                        + "10M Hồng ngọc\n"
                        + "200k COIN\n";
            case CHE_TAO_QUAN_VO_CUC_TU_TAI:
                return "Vào hành trang\n"
                        + "Chọn 4 loại Đan trúc cơ x99\n"
                        + "99.999 Linh khí\n"
                        + "999 Thỏi vàng\n"
                        + "10M Hồng ngọc\n"
                        + "200k COIN\n";
            case CHE_TAO_GANG_VO_CUC_TU_TAI:
                return "Vào hành trang\n"
                        + "Chọn 4 loại Đan trúc cơ x99\n"
                        + "99.999 Linh khí\n"
                        + "999 Thỏi vàng\n"
                        + "10M Hồng ngọc\n"
                        + "200k COIN\n";
            case CHE_TAO_GIAY_VO_CUC_TU_TAI:
                return "Vào hành trang\n"
                        + "Chọn 4 loại Đan trúc cơ x99\n"
                        + "99.999 Linh khí\n"
                        + "999 Thỏi vàng\n"
                        + "10M Hồng ngọc\n"
                        + "200k COIN\n";
            case CHE_TAO_TRANG_SUC_VO_CUC_TU_TAI:
                return "Vào hành trang\n"
                        + "Chọn 4 loại Đan trúc cơ x99\n"
                        + "99.999 Linh khí\n"
                        + "999 Thỏi vàng\n"
                        + "10M Hồng ngọc\n"
                        + "200k COIN\n";
            case CHE_TAO_DAN_DUOC_LUYEN_KHI:
                return "Vào hành trang\n"
                        + "Chọn 9 loại Tàn Đan x50\n";
            case CHE_TAO_TRUC_CO_DAN:
                return "Vào hành trang\n"
                        + "Chọn Hoàng Cực Đan x100\n"
                        + "x1 Trúc Cơ Đan Phương\n";
            default:
                return "Vui lòng chờ thêm thông tin";
        }
    }

}
