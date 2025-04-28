package nro.models.shop;

import nro.models.item.Item;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.server.Manager;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.Util;

import java.util.List;
import nro.consts.ConstNpc;
import nro.models.item.Item.ItemOption;

public class ShopServiceNew {  

    private static final byte COST_GOLD = 0;
    private static final byte COST_GEM = 1;
    private static final byte COST_ITEM_SPEC = 2;
    private static final byte COST_RUBY = 3;
    private static final byte COST_COUPON = 4;
    private static final byte COST_EVENT = 5;
    private static final byte COST_DIEM = 6;
    private static final byte NORMAL_SHOP = 0;
    private static final byte SPEC_SHOP = 3;
    private static final byte BOX = 4;

    private static ShopServiceNew I;

    public static ShopServiceNew gI() {  
        if (ShopServiceNew.I == null) {  
            ShopServiceNew.I = new ShopServiceNew();
        }
        return ShopServiceNew.I;
    }

    public void opendShop(Player player, String tagName, boolean allGender) {  
        if (tagName.equals("ITEMS_MAIL_BOX")) {
            openShopType4(player, tagName, player.inventory.itemsMailBox);
            return;
        }
        if (tagName.equals("RUONG_PHU")) {
            openShopType4(player, tagName, player.inventory.itemsRuongPhu);
            return;
        }
        if (tagName.equals("ITEMS_LUCKY_ROUND")) {  
            openShopType4(player, tagName, player.inventory.itemsBoxCrackBall);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {  
            player.getSession().initItemsReward();
            return;
        }
        try {  
            Shop shop = this.getShop(tagName);
            shop = this.resolveShop(player, shop, allGender);
            switch (shop.typeShop) {  
                case NORMAL_SHOP:
                    openShopType0(player, shop);
                    break;                                                                  
                case SPEC_SHOP:
                    openShopType3(player, shop);
                    break;                                                                  
                }
        } catch (Exception ex) {  
            ex.printStackTrace();
            Service.gI().sendThongBao(player, ex.getMessage());
        }
    }

    private Shop getShop(String tagName) throws Exception {  
        for (Shop s : Manager.SHOPS) {  
            if (s.tagName != null && s.tagName.equals(tagName)) {  
                return s;
            }
        }
        throw new Exception("Shop " + tagName + " không tồn tại!");
    }

//    private void _________________Xử_lý_cửa_hàng_trước_khi_gửi_______________() {  
//        //**********************************************************************
//    }
    private Shop resolveShop(Player player, Shop shop, boolean allGender) {  
        if (shop.tagName != null && (shop.tagName.equals("BUA_1H")
                || shop.tagName.equals("BUA_8H") || shop.tagName.equals("BUA_1M"))) {  
            return this.resolveShopBua(player, new Shop(shop));
        }
        return allGender ? new Shop(shop) : new Shop(shop, player.gender);
    }

    private Shop resolveShopBua(Player player, Shop s) {  
        for (TabShop tabShop : s.tabShops) {  
            for (ItemShop item : tabShop.itemShops) {  
                long min = 0;
                switch (item.temp.id) {  
                    case 213:
                        long timeTriTue = player.charms.tdTriTue;
                        long current = System.currentTimeMillis();
                        min = (timeTriTue - current) / 60000;

                        break;                                                                  
                    case 214:
                        min = (player.charms.tdManhMe - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                    case 215:
                        min = (player.charms.tdDaTrau - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                    case 216:
                        min = (player.charms.tdOaiHung - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                    case 217:
                        min = (player.charms.tdBatTu - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                    case 218:
                        min = (player.charms.tdDeoDai - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                    case 219:
                        min = (player.charms.tdThuHut - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                    case 522:
                        min = (player.charms.tdDeTu - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                    case 671:
                        min = (player.charms.tdTriTue3 - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                    case 672:
                        min = (player.charms.tdTriTue4 - System.currentTimeMillis()) / 60000;
                        break;                                                                  
                }
                if (min > 0) {  
                    item.options.clear();
                    if (min >= 1440) {  
                        item.options.add(new Item.ItemOption(63, (int) min / 1440));
                    } else if (min >= 60) {  
                        item.options.add(new Item.ItemOption(64, (int) min / 60));
                    } else {  
                        item.options.add(new Item.ItemOption(65, (int) min));
                    }
                }
            }
        }
        return s;
    }

//    private void _________________Gửi_cửa_hàng_cho_người_chơi________________() {  
//        //**********************************************************************
//    }
    private void openShopType0(Player player, Shop shop) {  
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {  
            Message msg;
            try {  
                msg = new Message(-44);
                msg.writer().writeByte(NORMAL_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {  
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {  
                        msg.writer().writeShort(itemShop.temp.id);
                        if (itemShop.typeSell == COST_GOLD) {  
                            msg.writer().writeInt(itemShop.cost);
                            msg.writer().writeInt(0);
                        } else if (itemShop.typeSell == COST_GEM) {  
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_RUBY) {  
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_COUPON) {  
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        }
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {  
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {  
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {  
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {  
                e.printStackTrace();
                Logger.logException(ShopServiceNew.class, e);
            }
        }
    }

    private void openShopType3(Player player, Shop shop) {  
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {  
            Message msg;
            try {  
                msg = new Message(-44);
                msg.writer().writeByte(SPEC_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {  
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {  
                        msg.writer().writeShort(itemShop.temp.id);
                        msg.writer().writeShort(itemShop.iconSpec);
                        msg.writer().writeInt(itemShop.cost);
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {  
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {  
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {  
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {  
                e.printStackTrace();
                Logger.logException(ShopServiceNew.class, e);
            }
        }
    }

    private void openShopType4(Player player, String tagName, List<Item> items) {  
        if (items == null) {  
            return;
        }
        player.iDMark.setTagNameShop(tagName);
        Message msg;
        try {  
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            msg.writer().writeByte(items.size());
            for (Item item : items) {  
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|7|NR BLUE");
                msg.writer().writeByte(item.itemOptions.size() + 1);
                for (Item.ItemOption io : item.itemOptions) {  
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                //số lượng
                msg.writer().writeByte(31);
                msg.writer().writeShort(item.quantity);
                //
                msg.writer().writeByte(1);
                if (item.template.type == 5) {  
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(item.template.head);
                    msg.writer().writeShort(item.template.body);
                    msg.writer().writeShort(item.template.leg);
                    msg.writer().writeShort(-1);
                } else {  
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {  

            e.printStackTrace();
        }
    }

//    private void _________________Mua_vật_phẩm______________________________() {  
//        //**********************************************************************
//    }
    public void takeItem(Player player, byte type, int tempId, int quantity) {  
        String tagName = player.iDMark.getTagNameShop();
        if (tagName == null || tagName.length() <= 0) {  
            return;
        }

        if (tagName.equals("ITEMS_MAIL_BOX")) {  
            getItemSideMailBox(player, player.inventory.itemsMailBox, type, tempId);
            return;
        }
        if (tagName.equals("RUONG_PHU")) {  
            getItemSideRuongPhu(player, player.inventory.itemsRuongPhu, type, tempId);
            return;
        }

        if (tagName.equals("ITEMS_LUCKY_ROUND")) {  
            getItemSideBoxLuckyRound(player, player.inventory.itemsBoxCrackBall, type, tempId);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {  
            return;
        }

        if (player.iDMark.getShopOpen() == null) {  
            Service.gI().sendThongBao(player, "Có lỗi xảy ra vui lòng báo admin");
            return;
        }
        switch (tagName) {
            case "BUA_1H":
            case "BUA_8H":
            case "BUA_1M":
                 
                buyItemBua(player, tempId);
                break;
            case "SHOP_DIEM_1":
            case "SHOP_DIEM_2":
            case "SHOP_DIEM_3":
            case "SHOP_DIEM_4":
                 
                buyItemDiem(player, tempId);
                break;
            case "SHOP_CSMM":
                buyItemCSMM(player, tempId);
                break;
//            case "HE2024":
//                buyItemGauPo(player, tempId);
//                break;
            case "SHOP_BA":
                 
                buyItemBaHatMit(player, tempId);
                break;
            default:
                 
                buyItem(player, tempId);
                break;
        }
        Service.gI().sendMoney(player);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private boolean subMoneyByItemShop(Player player, ItemShop is) {  
        int gold = 0;
        int gem = 0;
        int ruby = 0;
        int coupon = 0;
        int event = 0;

        switch (is.typeSell) {  
            case COST_GOLD:
                gold = is.cost;
                break;                                                                  
            case COST_GEM:
                gem = is.cost;
                break;                                                                  
            case COST_RUBY:
                ruby = is.cost;
                break;                                                                  
            case COST_COUPON:
                coupon = is.cost;
                break;                                                                  
            case COST_EVENT:
                event = is.cost;
                break;                               

        }
        if (player.inventory.gold < gold) {  
            Service.gI().sendThongBao(player, "Bạn không có đủ vàng");
            return false;
        } else if (player.inventory.gem < gem) {  
            Service.gI().sendThongBao(player, "Bạn không có đủ ngọc");
            return false;
        } else if (player.inventory.ruby < ruby) {  
            Service.gI().sendThongBao(player, "Bạn không có đủ hồng ngọc");
            return false;
        } else if (player.inventory.coupon < coupon) {  
            Service.gI().sendThongBao(player, "Bạn không có đủ điểm coupon");
            return false;
        } else if (player.inventory.event < event) {  
            Service.gI().sendThongBao(player, "Bạn không có đủ điểm event");
            return false;
        }

        player.inventory.gold -= is.temp.gold;
        player.inventory.gem -= is.temp.gem;
        player.inventory.ruby -= ruby;
        player.inventory.coupon -= coupon;
        player.inventory.event -= event;

        return true;
    }

    /**
     * Mua bùa
     *
     * @param player người chơi
     * @param itemTempId id template vật phẩm
     */
    private void buyItemBua(Player player, int itemTempId) {  
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {  
            Service.gI().sendThongBao(player, "Bùa bị lỗi vui lòng báo admin");
            return;
        }
        if (!subMoneyByItemShop(player, is)) {  
            return;
        }
        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
        InventoryServiceNew.gI().sendItemBags(player);
        opendShop(player, shop.tagName, true);
    }

    /**
     * Mua đồ
     *
     * @param player người chơi
     * @param itemTempId id template vật phẩm
     */
    private void buyItemCSMM(Player player, int itemTempId) {  
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        int pointExchange = 0;
        int evPoint = 0;
        if (is == null) {  
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }

        for (ItemOption io : is.options) {
            if (io.optionTemplate.id == 237) {
                pointExchange = io.param;
            }
        }
        if (pointExchange > 0) {
            if (evPoint >= pointExchange) {

                player.event.subEventPoint(pointExchange);

                InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemService.gI().createItemFromItemShop(is).template.name);

                opendShop(player, shop.tagName, true);
            } else {
//                Service.gI().sendThongBao(player, "Bạn không có đủ điểm quy lão");
                Service.gI().sendThongBao(player, "Quay con số may mắn để nhận quà hấp dẫn tại đây");
            }
        }
    }

//    private void buyItemGauPo(Player player, int itemTempId) {  
//        Shop shop = player.iDMark.getShopOpen();
//        ItemShop is = shop.getItemShop(itemTempId);
//        int pointExchange = 0;
//        int evPoint = 0;
//        if (is == null) {  
//            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
//            return;
//        }
//
//        for (ItemOption io : is.options) {
//            if (io.optionTemplate.id == 240) {
//                pointExchange = io.param;
//            }
//        }
//        if (pointExchange > 0) {
//            if (evPoint >= pointExchange) {
//
//                player.event.subEventPoint(pointExchange);
//
//                InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
//                InventoryServiceNew.gI().sendItemBags(player);
//                Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemService.gI().createItemFromItemShop(is).template.name);
//
//                opendShop(player, shop.tagName, true);
//            } else {
////                Service.gI().sendThongBao(player, "Bạn không có đủ điểm quy lão");
//                Service.gI().sendThongBao(player, "Tham gia đổi quà tại Gấu Po để có cơ hội nhận quà");
//            }
//        }
//    }

    private void buyItemDiem(Player player, int itemTempId) {  
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        int pointExchange = 0;
        int evPoint = player.event.getEventPoint();
        if (is == null) {  
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }

        for (ItemOption io : is.options) {
            if (io.optionTemplate.id == 200) {
                pointExchange = io.param;
            }
        }
        if (pointExchange > 0) {
            if (evPoint >= pointExchange) {

                player.event.subEventPoint(pointExchange);

                InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemService.gI().createItemFromItemShop(is).template.name);

                opendShop(player, shop.tagName, true);
            } else {
                Service.gI().sendThongBao(player, "Bạn không có đủ điểm quy lão");
            }
        }
    }

    private void buyItemBaHatMit(Player player, int itemTempId) {  
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        int pointExchange = 0;
        int evPoint = player.event.getEventPointBoss();
        if (is == null) {  
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }

        for (ItemOption io : is.options) {
            if (io.optionTemplate.id == 201) {
                pointExchange = io.param;
            }
        }
        if (pointExchange > 0) {
            if (evPoint >= pointExchange) {

                player.event.subEventPointBoss(pointExchange);

                InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemService.gI().createItemFromItemShop(is).template.name);

                opendShop(player, shop.tagName, true);
            } else {
                Service.gI().sendThongBao(player, "Bạn không có đủ điểm săn boss");
            }
        }
    }

    /**
     * Mua vật phẩm trong cửa hàng
     *
     * @param player người chơi
     * @param itemTempId id template vật phẩm
     */
    public void buyItem(Player player, int itemTempId) {  
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);

        if (is == null || is.temp == null) {  
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {  
            Service.gI().sendThongBao(player, "Hành trang đã đầy");
            return;
        }

        // Kiểm tra nếu là vật phẩm hủy diệt và không đủ thức ăn
        if (isHuyDietItem(is) && !hasEnoughThucAn(player)) {  
            Service.gI().sendThongBao(player, "Không đủ thức ăn để mua đồ!");
            return;
        }

        if (shop.typeShop == ShopServiceNew.NORMAL_SHOP) {  
            if (!subMoneyByItemShop(player, is)) {  
                return;
            }
        } else if (shop.tagName.equals("SHOP_DIEM_1")
                || shop.tagName.equals("SHOP_DIEM_2")
                || shop.tagName.equals("SHOP_DIEM_3")
                || shop.tagName.equals("SHOP_DIEM_4")) {  
            if (!subMoneyByItemShop(player, is)) {  
                return;
            }

        } else if (shop.tagName.equals("SHOP_BA")) {  
            if (!subMoneyByItemShop(player, is)) {  
                return;
            }

        }
        else if (shop.tagName.equals("HE2024")) {  
            if (!subMoneyByItemShop(player, is)) {  
                return;
            }

        } else if (shop.tagName.equals("SHOP_CSMM")) {  
            if (!subMoneyByItemShop(player, is)) {  
                return;
            }

        } else if (shop.typeShop == ShopServiceNew.SPEC_SHOP) {  
            if (!this.subIemByItemShop(player, is)) {  
                return;
            }
        }

        Item item = ItemService.gI().createItemFromItemShop(is);
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Mua thành công " + is.temp.name);
    }

// Kiểm tra nếu là vật phẩm hủy diệt
    private boolean isHuyDietItem(ItemShop itemShop) {  
        int itemId = itemShop.temp.id;
        return itemId >= 650 && itemId <= 662;
    }

// Kiểm tra nếu có đủ thức ăn
    private boolean hasEnoughThucAn(Player player) {  
        return player.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.isThucAn() && item.quantity >= 99)
                .findFirst().isPresent();
    }

//    private void _________________Bán_vật_phẩm______________________________() {  
//        //**********************************************************************
//    }
    private boolean subIemByItemShop(Player pl, ItemShop itemShop) {  
        boolean isBuy = false;
        short itSpec = ItemService.gI().getItemIdByIcon((short) itemShop.iconSpec);
        int buySpec = itemShop.cost;
        Item itS = ItemService.gI().createNewItem(itSpec);
        switch (itS.template.id) {  
            case 76:
            case 188:
            case 189:
            case 190:
                if (pl.inventory.gold >= buySpec) {  
                    pl.inventory.gold -= buySpec;
                    isBuy = true;
                } else {  
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Vàng Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;                                                                  
            case 861:
                if (pl.inventory.ruby >= buySpec) {  
                    pl.inventory.ruby -= buySpec;
                    isBuy = true;
                } else {  
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Hồng Ngọc Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;

                                              
            default:
                if (InventoryServiceNew.gI().findItemBag(pl, itSpec) == null || !InventoryServiceNew.gI().findItemBag(pl, itSpec).isNotNullItem()) {  
                    Service.gI().sendThongBao(pl, "Không tìm thấy " + itS.template.name);
                    isBuy = false;
                } else if (InventoryServiceNew.gI().findItemBag(pl, itSpec).quantity < buySpec) {  
                    Service.gI().sendThongBao(pl, "Bạn không có đủ " + buySpec + " " + itS.template.name);
                    isBuy = false;
                } else {  
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, itSpec), buySpec);
                    isBuy = true;
                }
                break;                                                                  
        }
        return isBuy;
    }

    public void showConfirmSellItem(Player pl, int where, int index) {  
        //if (index == 2 || index == 1) {  
        //return;
        //}

        Item item = null;
        if (where == 0) {  
            item = pl.inventory.itemsBody.get(index);
        } else {  
            if (pl.getSession().version <= 220) {  
                index -= (pl.inventory.itemsBody.size() - 7);
            }
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null && item.isNotNullItem()) {  
            int quantity = item.quantity;
            int cost = item.template.gold;
            if (item.template.id == 457) {  
                quantity = 1;
            } else {  
                cost /= 4;
            }
            if (cost == 0) {  
                cost = 1;
            }
            cost *= quantity;

            String text = "Bạn có muốn bán\nx" + quantity
                    + " " + item.template.name + "\nvới giá là " + Util.numberToMoney(cost) + " vàng?";
            Message msg = new Message(7);
            try {  
                msg.writer().writeByte(where);
                msg.writer().writeShort(index);
                msg.writer().writeUTF(text);
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {  
                e.printStackTrace();
            }
        }
    }

    public void sellItem(Player pl, int where, int index) {  
        Item item = null;
        if (where == 0) {  
            item = pl.inventory.itemsBody.get(index);
        } else {  
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null && item.template != null
                && item.template.id != 921
                && item.template.id != 454
                && item.template.id != 194) {   // Thêm điều kiện kiểm tra id của vật phẩm khác với 921
            int quantity = item.quantity;
            int cost = item.template.gold;
            if (item.template.id == 457) {  
                quantity = 1;
            } else {  
                cost /= 4;
            }
            if (cost == 0) {  
                cost = 1;
            }
            cost *= quantity;

            if (pl.inventory.gold + cost > Inventory.LIMIT_GOLD) {  
                Service.gI().sendThongBao(pl, "Vàng sau khi bán vượt quá giới hạn");
                return;
            }
            pl.inventory.gold += cost;
            Service.gI().sendMoney(pl);
            Service.gI().sendThongBao(pl, "Đã bán " + item.template.name
                    + " thu được " + Util.numberToMoney(cost) + " vàng");

            if (where == 0) {  
                InventoryServiceNew.gI().subQuantityItemsBody(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBody(pl);
                Service.gI().Send_Caitrang(pl);
            } else {  
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {  
            Service.gI().sendThongBao(pl, "Không thể bán vật phẩm này được");
        }
    }

//    private void _________________Nhận_vật_phẩm_từ_rương_đặc_biệt___________() {  
//        //**********************************************************************
//    }
    private void getItemSideBoxLuckyRound(Player player, List<Item> items, byte type, int index) {  
        if (index == -1 && items == null || items.isEmpty() || index >= items.size()) {  
            // Handle the error and notify the user
            Service.gI().sendThongBao(player, "Có lỗi xảy ra khi xử lý yêu cầu");
            return;
        }
        if (index >= 0) {
            Item item = items.get(index);

            switch (type) {  
                case 0: // nhận
                    if (item.isNotNullItem()) {  
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {  
                            InventoryServiceNew.gI().addItemBag(player, item);
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            InventoryServiceNew.gI().sendItemBags(player);
                            items.remove(index);
                        } else {  
                            Service.gI().sendThongBao(player, "Hành trang đã đầy");
                        }
                    } else {  
                        Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
                    }
                    break;                                                                  
                case 1: // xóa
                    items.remove(index);
                    Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                    break;                                                                  
                case 2: // nhận hết
                    for (int i = items.size() - 1; i >= 0; i--) {  
                        item = items.get(i);
                        if (InventoryServiceNew.gI().addItemBag(player, item)) {  
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            items.remove(i);
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;                                                                  
            }
            openShopType4(player, player.iDMark.getTagNameShop(), items);
        }
    }

    private void getItemSideMailBox(Player player, List<Item> items, byte type, int index) {  
        if (index == -1 && items == null || items.isEmpty() || index >= items.size()) {  
            // Handle the error and notify the user
            Service.gI().sendThongBao(player, "Có lỗi xảy ra khi xử lý yêu cầu");
            return;
        }
        if (index >= 0) {
            Item item = items.get(index);

            switch (type) {  
                case 0: // nhận
                    if (item.isNotNullItem()) {  
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {  
                            InventoryServiceNew.gI().addItemBag(player, item);
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            InventoryServiceNew.gI().sendItemBags(player);
                            items.remove(index);
                        } else {  
                            Service.gI().sendThongBao(player, "Hành trang đã đầy");
                        }
                    } else {  
                        Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
                    }
                    break;                                                                  
                case 1: // xóa
                    items.remove(index);
                    Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                    break;                                                                  
                case 2: // nhận hết
                    for (int i = items.size() - 1; i >= 0; i--) {  
                        item = items.get(i);
                        if (InventoryServiceNew.gI().addItemBag(player, item)) {  
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            items.remove(i);
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;                                                                  
            }
            openShopType4(player, player.iDMark.getTagNameShop(), items);
        }
    }

    private void getItemSideRuongPhu(Player player, List<Item> items, byte type, int index) {  
        if (index == -1 && items == null || items.isEmpty() || index >= items.size()) {  
            // Handle the error and notify the user
            Service.gI().sendThongBao(player, "Có lỗi xảy ra khi xử lý yêu cầu");
            return;
        }
        if (index >= 0) {
            Item item = items.get(index);

            switch (type) {  
                case 0: // nhận
                    if (item.isNotNullItem()) {  
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {  
                            InventoryServiceNew.gI().addItemBag(player, item);
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            InventoryServiceNew.gI().sendItemBags(player);
                            items.remove(index);
                        } else {  
                            Service.gI().sendThongBao(player, "Hành trang đã đầy");
                        }
                    } else {  
                        Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
                    }
                    break;                                                                  
                case 1: // xóa
                    items.remove(index);
                    Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                    break;                                                                  
                case 2: // nhận hết
                    for (int i = items.size() - 1; i >= 0; i--) {  
                        item = items.get(i);
                        if (InventoryServiceNew.gI().addItemBag(player, item)) {  
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            items.remove(i);
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;                                                                  
            }
            openShopType4(player, player.iDMark.getTagNameShop(), items);
        }
    }

}
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Girl Béo
 */
