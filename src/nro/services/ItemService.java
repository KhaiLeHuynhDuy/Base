package nro.services;

import nro.models.Template;
import nro.models.Template.ItemOptionTemplate;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.shop.ItemShop;
import nro.server.Manager;
import nro.services.func.CombineServiceNew;
import nro.utils.TimeUtil;
import nro.utils.Util;
import nro.models.item.Item.ItemOption;
import nro.models.map.Zone;

import java.util.*;
import java.util.stream.Collectors;
import nro.models.consignment.ConsignmentItem;

public class ItemService {

    private static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public short getItemIdByIcon(short IconID) {
        for (int i = 0; i < Manager.ITEM_TEMPLATES.size(); i++) {
            if (Manager.ITEM_TEMPLATES.get(i).iconID == IconID) {
                return Manager.ITEM_TEMPLATES.get(i).id;
            }
        }
        return -1;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (Item.ItemOption io : itemShop.options) {
            item.itemOptions.add(new Item.ItemOption(io));
        }
        return item;
    }

    public ConsignmentItem convertToConsignmentItem(Item item) {
        ConsignmentItem it = new ConsignmentItem();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        it.setPriceGold(-1);
        it.setPriceGem(-1);
        return it;
    }

    public ConsignmentItem createNewConsignmentItem(short tempId, int quantity) {
        ConsignmentItem item = new ConsignmentItem();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (Item.ItemOption io : item.itemOptions) {
            it.itemOptions.add(new Item.ItemOption(io));
        }
        return it;
    }

    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }

    public Item otpts(short tempId) {
        return otpts(tempId, 1);
    }

    public Item otptl(short tempId) {
        return otptl(tempId, 1);
    }

    public Item otphd(short tempId) {
        return otphd(tempId, 1);
    }

    public Item otpTanThu(short tempId) {
        return otpTanThu(tempId, 1);
    }

//    public Item otpThienMa(short tempId) {
//        return otpThienMa(tempId, 1);
//    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();

        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item otpts(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item otptl(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 19));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(800, 1200)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 19));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(24, 28)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 19));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(5500, 7800)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 19));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(23, 29)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 19));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(1, 14)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item otphd(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1200, 2100)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(60, 80)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(8500, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(59, 82)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 80));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(5, 18)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    //tao chi so cho trang bi tan thu
    public Item otpTanThu(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, 9999));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(6, 9999));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, 9999));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(7, 9999));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, 99));
        }
        if (item.template.type == 5) {
            item.itemOptions.add(new Item.ItemOption(50, 15));
            item.itemOptions.add(new Item.ItemOption(77, 15));
            item.itemOptions.add(new Item.ItemOption(103, 15));
            item.itemOptions.add(new Item.ItemOption(101, 100));

        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }
//tao chi so cho trang bi thien ma

//    public Item otpThienMa(short tempId, int quantity) {
//        Item item = new Item();
//        item.template = getTemplate(tempId);
//        item.quantity = quantity;
//        item.createTime = System.currentTimeMillis();
//        if (item.template.type == 0) {
//            item.itemOptions.add(new ItemOption(47, Util.nextInt(100000, 200000)));
//            item.itemOptions.add(new ItemOption(94, Util.nextInt(50, 150)));
//        }
//        if (item.template.type == 1) {
//            item.itemOptions.add(new ItemOption(6, Util.nextInt(10000000, 20000000)));
//            item.itemOptions.add(new ItemOption(77, Util.nextInt(50, 150)));
//        }
//        if (item.template.type == 2) {
//            item.itemOptions.add(new ItemOption(0, Util.nextInt(1000000, 2000000)));
//            item.itemOptions.add(new ItemOption(50, Util.nextInt(50, 150)));
//        }
//        if (item.template.type == 3) {
//            item.itemOptions.add(new ItemOption(7, Util.nextInt(10000000, 20000000)));
//            item.itemOptions.add(new ItemOption(103, Util.nextInt(50, 150)));
//        }
//        if (item.template.type == 4) {
//            item.itemOptions.add(new ItemOption(14, Util.nextInt(100, 200)));
//            item.itemOptions.add(new ItemOption(5, Util.nextInt(10, 50)));
//        }
//        if (item.template.type == 5) {
//            item.itemOptions.add(new Item.ItemOption(6, 50000000));//hp
//            item.itemOptions.add(new Item.ItemOption(7, 50000000));//ki
//            item.itemOptions.add(new Item.ItemOption(47, 5000000));//giáp
//            item.itemOptions.add(new Item.ItemOption(0, 500000));//sd
//            item.itemOptions.add(new Item.ItemOption(5, 25));
//            item.itemOptions.add(new ItemOption(77, 250));
//            item.itemOptions.add(new ItemOption(50, 250));
//            item.itemOptions.add(new ItemOption(103, 250));
//        }
//        item.content = item.getContent();
//        item.info = item.getInfo();
//        return item;
//    }

    public Item createItemSetKichHoat(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public ItemMap createItemMapSetKichHoat(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap item = new ItemMap(zone, tempId, quantity, x, y, playerId);
        item.quantity = quantity;
        item.options = createItemNull().itemOptions;
        item.itemTemplate = getTemplate(tempId);

        return item;
    }

    public Item createItemDoHuyDiet(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public Template.ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public boolean isItemActivation(Item item) {
        return false;
    }

    public int getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isOutOfDateTime(Item item) {
        if (item != null) {
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    if (io.param == 0) {
                        return true;
                    }
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
        return false;
    }

    public void openDoHuyDiet(Player player, int itemUseId, int select) throws Exception {
        if (select < 0 || select > 4) {
            return;
        }
        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
        int[][] items = {{650, 651, 657, 658, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}};
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
        switch (itemUseId) {
            case 2085: {
                int gender = 0;
                item = itemDoHuyDiet(items[0][select], options[0][gender]);
            }
            break;

            case 2086: {
                int gender = 1;
                item = itemDoHuyDiet(items[1][select], options[1][gender]);
            }
            break;

            case 2087: {
                int gender = 2;
                item = itemDoHuyDiet(items[2][select], options[2][gender]);
            }
            break;

        }
        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void OpenDHD2(Player player, int gender, int itemtype) {

        int[][] items = {{650, 651, 657, 658, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}}; //td, namec,xd
        Item item = randomCS_DHD(items[player.gender][itemtype], gender);

        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void OpenSKH(Player player, int itemUseId, int select) throws Exception {
        if (select < 0 || select > 4) {
            return;
        }
        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
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
        switch (itemUseId) {
            case 2000:
                item = itemSKH(items[0][select], options[0][skhId]);
                break;
            case 2001:
                item = itemSKH(items[1][select], options[1][skhId]);
                break;
            case 2002:
                item = itemSKH(items[2][select], options[2][skhId]);
                break;
        }
        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

//    public void OpenSaoPhaLeTrungCap(Player player, int itemUseId, int select) throws Exception {
//        if (select < 0 || select > 3) {
//            return;
//        }
//        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
//        int[] items = {1441, 1442, 1443, 1444};
//        int[] options = {50, 77, 103, 5};
//        int[] param = {5, 8, 8, 8};
//
//        Item item = null;
//        switch (select) {
//            case 0:
//            case 1:
//            case 2:
//            case 3:
//                item = ItemService.gI().createNewItem((short) items[select]);
//                item.quantity = 1;
//                item.itemOptions.add(new ItemOption(options[select], param[select]));
//                item.itemOptions.add(new ItemOption(30, 1));
//                break;
//
//        }
//        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            InventoryServiceNew.gI().addItemBag(player, item);
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
//            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
//            InventoryServiceNew.gI().sendItemBags(player);
//        } else {
//            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
//        }
//    }
//
//    public void OpenSaoPhaLeCaoCap(Player player, int itemUseId, int select) throws Exception {
//        if (select < 0 || select > 3) {
//            return;
//        }
//        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
//        int[] items = {1445, 1446, 1447, 1448};
//        int[] options = {50, 77, 103, 5};
//        int[] param = {8, 12, 12, 12};
//
//        Item item = null;
//        switch (select) {
//            case 0:
//            case 1:
//            case 2:
//            case 3:
//                item = ItemService.gI().createNewItem((short) items[select]);
//                item.quantity = 1;
//                item.itemOptions.add(new ItemOption(options[select], param[select]));
//                item.itemOptions.add(new ItemOption(30, 1));
//                break;
//
//        }
//        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//            InventoryServiceNew.gI().addItemBag(player, item);
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
//            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
//            InventoryServiceNew.gI().sendItemBags(player);
//        } else {
//            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
//        }
//    }
    public int randomBTId(byte random) {
        int[][] options = {{14, 27, 28, 50, 77, 103, 108, 175}};
        int skhv1 = 25;
        int skhv2 = 35;
        int skhc = 40;
        int btId = -1;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            btId = 0;
        } else if (rd <= skhv1 + skhv2) {
            btId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            btId = 2;
        }
        return options[random][btId];
    }

    public int randomSKHId(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
        int skhv1 = 25;
        int skhv2 = 35;
        int skhc = 40;
        int skhId = 0;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        return options[gender][skhId];
    }

    public void OpenDTL(Player player, int itemUseId, int select) throws Exception {
        if (select < 0 || select > 4) {
            return;
        }
        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
        int gender = -1;
        switch (itemUseId) {
            case 2013: //td
                gender = 0;
                break;
            case 2014: //xd
                gender = 1;
                break;
            case 2015: //nm
                gender = 2;
                break;
        }
        int[][] items = {{555, 556, 562, 563, 561}, {557, 558, 564, 565, 561}, {559, 560, 566, 567, 561}}; //td, namec,xd
        Item item = randomCS_DTL(items[gender][select], gender);

        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void OpenDHD(Player player, int itemUseId, int select) throws Exception {
        if (select < 0 || select > 4) {
            return;
        }
        Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, itemUseId);
        int gender = -1;
        switch (itemUseId) {
            case 2003: //td
                gender = 0;
                break;
            case 2004: //xd
                gender = 1;
                break;
            case 2005: //nm
                gender = 2;
                break;
        }
        int[][] items = {{650, 651, 657, 658, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}}; //td, namec,xd
        Item item = randomCS_DHD(items[gender][select], gender);

        if (item != null && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void OpenItem736(Player player, Item itemUse) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            int rd = Util.nextInt(1, 100);
            int rac = 50;
            int ruby = 20;
            int dbv = 10;
            int vb = 10;
            int bh = 5;
            int ct = 5;
            Item item = randomRac();
            if (rd <= rac) {
                item = randomRac();
            } else if (rd <= rac + ruby) {
                item = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
            } else if (rd <= rac + ruby + dbv) {
                item = daBaoVe();
            } else if (rd <= rac + ruby + dbv + vb) {
                item = vanBay2011(true);
            } else if (rd <= rac + ruby + dbv + vb + bh) {
                item = phuKien2011(true);
            } else if (rd <= rac + ruby + dbv + vb + bh + ct) {
                item = caitrang2011(true);
            }
            if (item.template.id == 861) {
                item.quantity = Util.nextInt(10, 30);
            }
            icon[0] = itemUse.template.iconID;
            icon[1] = item.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            player.inventory.event++;
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenItem2117(Player player, Item itemUse) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            int rd = Util.nextInt(1, 100);
            int rac = 50;
            int ruby = 20;
            int dbv = 10;
            int vb = 10;
            int bh = 5;
            int ct = 5;
            Item item = randomRac();
            if (rd <= rac) {
                item = randomRac();
            } else if (rd <= rac + ruby) {
                item = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
            } else if (rd <= rac + ruby + dbv) {
                item = daBaoVe();
            } else if (rd <= rac + ruby + dbv + vb) {
                item = vanBay2011(true);
            } else if (rd <= rac + ruby + dbv + vb + bh) {
                item = phuKien2011(true);
            } else if (rd <= rac + ruby + dbv + vb + bh + ct) {
                item = caitrangblackrose(true);
            }
            if (item.template.id == 861) {
                item.quantity = Util.nextInt(10, 30);
            }
            icon[0] = itemUse.template.iconID;
            icon[1] = item.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, 1);
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            player.inventory.event++;
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settaiyoken(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1048);
            Item quan = ItemService.gI().otpts((short) 1051);
            Item gang = ItemService.gI().otpts((short) 1054);
            Item giay = ItemService.gI().otpts((short) 1057);
            Item nhan = ItemService.gI().otpts((short) 1060);
            ao.itemOptions.add(new Item.ItemOption(212, 0));
            quan.itemOptions.add(new Item.ItemOption(212, 0));
            gang.itemOptions.add(new Item.ItemOption(212, 0));
            giay.itemOptions.add(new Item.ItemOption(212, 0));
            nhan.itemOptions.add(new Item.ItemOption(212, 0));
            ao.itemOptions.add(new Item.ItemOption(213, 0));
            quan.itemOptions.add(new Item.ItemOption(213, 0));
            gang.itemOptions.add(new Item.ItemOption(213, 0));
            giay.itemOptions.add(new Item.ItemOption(213, 0));
            nhan.itemOptions.add(new Item.ItemOption(213, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgenki(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1048);
            Item quan = ItemService.gI().otpts((short) 1051);
            Item gang = ItemService.gI().otpts((short) 1054);
            Item giay = ItemService.gI().otpts((short) 1057);
            Item nhan = ItemService.gI().otpts((short) 1060);
            ao.itemOptions.add(new Item.ItemOption(128, 0));
            quan.itemOptions.add(new Item.ItemOption(128, 0));
            gang.itemOptions.add(new Item.ItemOption(128, 0));
            giay.itemOptions.add(new Item.ItemOption(128, 0));
            nhan.itemOptions.add(new Item.ItemOption(128, 0));
            ao.itemOptions.add(new Item.ItemOption(140, 0));
            quan.itemOptions.add(new Item.ItemOption(140, 0));
            gang.itemOptions.add(new Item.ItemOption(140, 0));
            giay.itemOptions.add(new Item.ItemOption(140, 0));
            nhan.itemOptions.add(new Item.ItemOption(140, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setkamejoko(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1048);
            Item quan = ItemService.gI().otpts((short) 1051);
            Item gang = ItemService.gI().otpts((short) 1054);
            Item giay = ItemService.gI().otpts((short) 1057);
            Item nhan = ItemService.gI().otpts((short) 1060);
            ao.itemOptions.add(new Item.ItemOption(129, 0));
            quan.itemOptions.add(new Item.ItemOption(129, 0));
            gang.itemOptions.add(new Item.ItemOption(129, 0));
            giay.itemOptions.add(new Item.ItemOption(129, 0));
            nhan.itemOptions.add(new Item.ItemOption(129, 0));
            ao.itemOptions.add(new Item.ItemOption(141, 0));
            quan.itemOptions.add(new Item.ItemOption(141, 0));
            gang.itemOptions.add(new Item.ItemOption(141, 0));
            giay.itemOptions.add(new Item.ItemOption(141, 0));
            nhan.itemOptions.add(new Item.ItemOption(141, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgodki(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1049);
            Item quan = ItemService.gI().otpts((short) 1052);
            Item gang = ItemService.gI().otpts((short) 1055);
            Item giay = ItemService.gI().otpts((short) 1058);
            Item nhan = ItemService.gI().otpts((short) 1061);
            ao.itemOptions.add(new Item.ItemOption(214, 0));
            quan.itemOptions.add(new Item.ItemOption(214, 0));
            gang.itemOptions.add(new Item.ItemOption(214, 0));
            giay.itemOptions.add(new Item.ItemOption(214, 0));
            nhan.itemOptions.add(new Item.ItemOption(214, 0));
            ao.itemOptions.add(new Item.ItemOption(215, 0));
            quan.itemOptions.add(new Item.ItemOption(215, 0));
            gang.itemOptions.add(new Item.ItemOption(215, 0));
            giay.itemOptions.add(new Item.ItemOption(215, 0));
            nhan.itemOptions.add(new Item.ItemOption(215, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgoddam(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1049);
            Item quan = ItemService.gI().otpts((short) 1052);
            Item gang = ItemService.gI().otpts((short) 1055);
            Item giay = ItemService.gI().otpts((short) 1058);
            Item nhan = ItemService.gI().otpts((short) 1061);
            ao.itemOptions.add(new Item.ItemOption(131, 0));
            quan.itemOptions.add(new Item.ItemOption(131, 0));
            gang.itemOptions.add(new Item.ItemOption(131, 0));
            giay.itemOptions.add(new Item.ItemOption(131, 0));
            nhan.itemOptions.add(new Item.ItemOption(131, 0));
            ao.itemOptions.add(new Item.ItemOption(143, 0));
            quan.itemOptions.add(new Item.ItemOption(143, 0));
            gang.itemOptions.add(new Item.ItemOption(143, 0));
            giay.itemOptions.add(new Item.ItemOption(143, 0));
            nhan.itemOptions.add(new Item.ItemOption(143, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setsummon(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1049);
            Item quan = ItemService.gI().otpts((short) 1052);
            Item gang = ItemService.gI().otpts((short) 1055);
            Item giay = ItemService.gI().otpts((short) 1058);
            Item nhan = ItemService.gI().otpts((short) 1061);
            ao.itemOptions.add(new Item.ItemOption(132, 0));
            quan.itemOptions.add(new Item.ItemOption(132, 0));
            gang.itemOptions.add(new Item.ItemOption(132, 0));
            giay.itemOptions.add(new Item.ItemOption(132, 0));
            nhan.itemOptions.add(new Item.ItemOption(132, 0));
            ao.itemOptions.add(new Item.ItemOption(144, 0));
            quan.itemOptions.add(new Item.ItemOption(144, 0));
            gang.itemOptions.add(new Item.ItemOption(144, 0));
            giay.itemOptions.add(new Item.ItemOption(144, 0));
            nhan.itemOptions.add(new Item.ItemOption(144, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgodgalick(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1050);
            Item quan = ItemService.gI().otpts((short) 1053);
            Item gang = ItemService.gI().otpts((short) 1056);
            Item giay = ItemService.gI().otpts((short) 1059);
            Item nhan = ItemService.gI().otpts((short) 1062);
            ao.itemOptions.add(new Item.ItemOption(133, 0));
            quan.itemOptions.add(new Item.ItemOption(133, 0));
            gang.itemOptions.add(new Item.ItemOption(133, 0));
            giay.itemOptions.add(new Item.ItemOption(133, 0));
            nhan.itemOptions.add(new Item.ItemOption(133, 0));
            ao.itemOptions.add(new Item.ItemOption(136, 0));
            quan.itemOptions.add(new Item.ItemOption(136, 0));
            gang.itemOptions.add(new Item.ItemOption(136, 0));
            giay.itemOptions.add(new Item.ItemOption(136, 0));
            nhan.itemOptions.add(new Item.ItemOption(136, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setmonkey(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1050);
            Item quan = ItemService.gI().otpts((short) 1053);
            Item gang = ItemService.gI().otpts((short) 1056);
            Item giay = ItemService.gI().otpts((short) 1059);
            Item nhan = ItemService.gI().otpts((short) 1062);
            ao.itemOptions.add(new Item.ItemOption(134, 0));
            quan.itemOptions.add(new Item.ItemOption(134, 0));
            gang.itemOptions.add(new Item.ItemOption(134, 0));
            giay.itemOptions.add(new Item.ItemOption(134, 0));
            nhan.itemOptions.add(new Item.ItemOption(134, 0));
            ao.itemOptions.add(new Item.ItemOption(137, 0));
            quan.itemOptions.add(new Item.ItemOption(137, 0));
            gang.itemOptions.add(new Item.ItemOption(137, 0));
            giay.itemOptions.add(new Item.ItemOption(137, 0));
            nhan.itemOptions.add(new Item.ItemOption(137, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void setgodhp(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2116 + i);
            Item ao = ItemService.gI().otpts((short) 1050);
            Item quan = ItemService.gI().otpts((short) 1053);
            Item gang = ItemService.gI().otpts((short) 1056);
            Item giay = ItemService.gI().otpts((short) 1059);
            Item nhan = ItemService.gI().otpts((short) 1062);
            ao.itemOptions.add(new Item.ItemOption(135, 0));
            quan.itemOptions.add(new Item.ItemOption(135, 0));
            gang.itemOptions.add(new Item.ItemOption(135, 0));
            giay.itemOptions.add(new Item.ItemOption(135, 0));
            nhan.itemOptions.add(new Item.ItemOption(135, 0));
            ao.itemOptions.add(new Item.ItemOption(138, 0));
            quan.itemOptions.add(new Item.ItemOption(138, 0));
            gang.itemOptions.add(new Item.ItemOption(138, 0));
            giay.itemOptions.add(new Item.ItemOption(138, 0));
            nhan.itemOptions.add(new Item.ItemOption(138, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
            quan.itemOptions.add(new Item.ItemOption(30, 0));
            gang.itemOptions.add(new Item.ItemOption(30, 0));
            giay.itemOptions.add(new Item.ItemOption(30, 0));
            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
                InventoryServiceNew.gI().addItemBag(player, quan);
                InventoryServiceNew.gI().addItemBag(player, gang);
                InventoryServiceNew.gI().addItemBag(player, giay);
                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
            }
        }
    }

    public void settlkaio(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
            int[] dotl = new int[]{555, 556, 562, 563, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);

//            Item quan = ItemService.gI().otptl((short) 556);
//            Item gang = ItemService.gI().otptl((short) 562);
//            Item giay = ItemService.gI().otptl((short) 563);
//            Item nhan = ItemService.gI().otptl((short) 561);
            ao.itemOptions.add(new Item.ItemOption(212, 0));
//            quan.itemOptions.add(new Item.ItemOption(212, 0));
//            gang.itemOptions.add(new Item.ItemOption(212, 0));
//            giay.itemOptions.add(new Item.ItemOption(212, 0));
//            nhan.itemOptions.add(new Item.ItemOption(212, 0));
            ao.itemOptions.add(new Item.ItemOption(213, 0));
//            quan.itemOptions.add(new Item.ItemOption(213, 0));
//            gang.itemOptions.add(new Item.ItemOption(213, 0));
//            giay.itemOptions.add(new Item.ItemOption(213, 0));
//            nhan.itemOptions.add(new Item.ItemOption(213, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được 1 món Thần Linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }

        }
    }

    public void settlgenki(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
            int[] dotl = new int[]{555, 556, 562, 563, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(128, 0));
//            quan.itemOptions.add(new Item.ItemOption(128, 0));
//            gang.itemOptions.add(new Item.ItemOption(128, 0));
//            giay.itemOptions.add(new Item.ItemOption(128, 0));
//            nhan.itemOptions.add(new Item.ItemOption(128, 0));
            ao.itemOptions.add(new Item.ItemOption(140, 0));
//            quan.itemOptions.add(new Item.ItemOption(140, 0));
//            gang.itemOptions.add(new Item.ItemOption(140, 0));
//            giay.itemOptions.add(new Item.ItemOption(140, 0));
//            nhan.itemOptions.add(new Item.ItemOption(140, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                    InventoryServiceNew.gI().addItemBag(player, quan);
//                    InventoryServiceNew.gI().addItemBag(player, gang);
//                    InventoryServiceNew.gI().addItemBag(player, giay);
//                    InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Thần Linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }

    }

    public void settlson(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
            int[] dotl = new int[]{555, 556, 562, 563, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(129, 0));

            ao.itemOptions.add(new Item.ItemOption(141, 0));

            ao.itemOptions.add(new Item.ItemOption(30, 0));

            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);

                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Thần Linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void settlpico(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
//            Item ao = ItemService.gI().otptl((short) 557);
//            Item quan = ItemService.gI().otptl((short) 558);
//            Item gang = ItemService.gI().otptl((short) 564);
//            Item giay = ItemService.gI().otptl((short) 565);
//            Item nhan = ItemService.gI().otptl((short) 561);
            int[] dotl = new int[]{557, 558, 564, 565, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(130, 0));
//            quan.itemOptions.add(new Item.ItemOption(214, 0));
//            gang.itemOptions.add(new Item.ItemOption(214, 0));
//            giay.itemOptions.add(new Item.ItemOption(214, 0));
//            nhan.itemOptions.add(new Item.ItemOption(214, 0));
            ao.itemOptions.add(new Item.ItemOption(142, 0));
//            quan.itemOptions.add(new Item.ItemOption(215, 0));
//            gang.itemOptions.add(new Item.ItemOption(215, 0));
//            giay.itemOptions.add(new Item.ItemOption(215, 0));
//            nhan.itemOptions.add(new Item.ItemOption(215, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Thần Linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void settloctieu(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
//            Item ao = ItemService.gI().otptl((short) 557);
//            Item quan = ItemService.gI().otptl((short) 558);
//            Item gang = ItemService.gI().otptl((short) 564);
//            Item giay = ItemService.gI().otptl((short) 565);
//            Item nhan = ItemService.gI().otptl((short) 561);
            int[] dotl = new int[]{557, 558, 564, 565, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(131, 0));
//            quan.itemOptions.add(new Item.ItemOption(131, 0));
//            gang.itemOptions.add(new Item.ItemOption(131, 0));
//            giay.itemOptions.add(new Item.ItemOption(131, 0));
//            nhan.itemOptions.add(new Item.ItemOption(131, 0));
            ao.itemOptions.add(new Item.ItemOption(143, 0));
//            quan.itemOptions.add(new Item.ItemOption(143, 0));
//            gang.itemOptions.add(new Item.ItemOption(143, 0));
//            giay.itemOptions.add(new Item.ItemOption(143, 0));
//            nhan.itemOptions.add(new Item.ItemOption(143, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Thần Linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void settlpiko(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
//            Item ao = ItemService.gI().otptl((short) 557);
//            Item quan = ItemService.gI().otptl((short) 558);
//            Item gang = ItemService.gI().otptl((short) 564);
//            Item giay = ItemService.gI().otptl((short) 565);
//            Item nhan = ItemService.gI().otptl((short) 561);
            int[] dotl = new int[]{557, 558, 564, 565, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(132, 0));
//            quan.itemOptions.add(new Item.ItemOption(132, 0));
//            gang.itemOptions.add(new Item.ItemOption(132, 0));
//            giay.itemOptions.add(new Item.ItemOption(132, 0));
//            nhan.itemOptions.add(new Item.ItemOption(132, 0));
            ao.itemOptions.add(new Item.ItemOption(144, 0));
//            quan.itemOptions.add(new Item.ItemOption(144, 0));
//            gang.itemOptions.add(new Item.ItemOption(144, 0));
//            giay.itemOptions.add(new Item.ItemOption(144, 0));
//            nhan.itemOptions.add(new Item.ItemOption(144, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Thần Linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void settlgalick(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
//            Item ao = ItemService.gI().otptl((short) 559);
//            Item quan = ItemService.gI().otptl((short) 560);
//            Item gang = ItemService.gI().otptl((short) 566);
//            Item giay = ItemService.gI().otptl((short) 567);
//            Item nhan = ItemService.gI().otptl((short) 561);
            int[] dotl = new int[]{559, 560, 566, 567, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(133, 0));
//            quan.itemOptions.add(new Item.ItemOption(133, 0));
//            gang.itemOptions.add(new Item.ItemOption(133, 0));
//            giay.itemOptions.add(new Item.ItemOption(133, 0));
//            nhan.itemOptions.add(new Item.ItemOption(133, 0));
            ao.itemOptions.add(new Item.ItemOption(136, 0));
//            quan.itemOptions.add(new Item.ItemOption(136, 0));
//            gang.itemOptions.add(new Item.ItemOption(136, 0));
//            giay.itemOptions.add(new Item.ItemOption(136, 0));
//            nhan.itemOptions.add(new Item.ItemOption(136, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món thần linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void settlcadick(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
//            Item ao = ItemService.gI().otptl((short) 559);
//            Item quan = ItemService.gI().otptl((short) 560);
//            Item gang = ItemService.gI().otptl((short) 566);
//            Item giay = ItemService.gI().otptl((short) 567);
//            Item nhan = ItemService.gI().otptl((short) 561);
            int[] dotl = new int[]{559, 560, 566, 567, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(134, 0));
//            quan.itemOptions.add(new Item.ItemOption(134, 0));
//            gang.itemOptions.add(new Item.ItemOption(134, 0));
//            giay.itemOptions.add(new Item.ItemOption(134, 0));
//            nhan.itemOptions.add(new Item.ItemOption(134, 0));
            ao.itemOptions.add(new Item.ItemOption(137, 0));
//            quan.itemOptions.add(new Item.ItemOption(137, 0));
//            gang.itemOptions.add(new Item.ItemOption(137, 0));
//            giay.itemOptions.add(new Item.ItemOption(137, 0));
//            nhan.itemOptions.add(new Item.ItemOption(137, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Thàn linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void settlnappa(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2114 + i);
//            Item ao = ItemService.gI().otptl((short) 559);
//            Item quan = ItemService.gI().otptl((short) 560);
//            Item gang = ItemService.gI().otptl((short) 566);
//            Item giay = ItemService.gI().otptl((short) 567);
//            Item nhan = ItemService.gI().otptl((short) 561);
            int[] dotl = new int[]{559, 560, 566, 567, 561};

            int ramdom = new Random().nextInt(dotl.length);

            Item ao = ItemService.gI().otptl((short) dotl[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(135, 0));
//            quan.itemOptions.add(new Item.ItemOption(135, 0));
//            gang.itemOptions.add(new Item.ItemOption(135, 0));
//            giay.itemOptions.add(new Item.ItemOption(135, 0));
//            nhan.itemOptions.add(new Item.ItemOption(135, 0));
            ao.itemOptions.add(new Item.ItemOption(138, 0));
//            quan.itemOptions.add(new Item.ItemOption(138, 0));
//            gang.itemOptions.add(new Item.ItemOption(138, 0));
//            giay.itemOptions.add(new Item.ItemOption(138, 0));
//            nhan.itemOptions.add(new Item.ItemOption(138, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món thần linh ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdkaio(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 650);
//            Item quan = ItemService.gI().otphd((short) 651);
//            Item gang = ItemService.gI().otphd((short) 657);
//            Item giay = ItemService.gI().otphd((short) 658);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{650, 651, 657, 658, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(212, 0));
//            quan.itemOptions.add(new Item.ItemOption(212, 0));
//            gang.itemOptions.add(new Item.ItemOption(212, 0));
//            giay.itemOptions.add(new Item.ItemOption(212, 0));
//            nhan.itemOptions.add(new Item.ItemOption(212, 0));
            ao.itemOptions.add(new Item.ItemOption(213, 0));
//            quan.itemOptions.add(new Item.ItemOption(213, 0));
//            gang.itemOptions.add(new Item.ItemOption(213, 0));
//            giay.itemOptions.add(new Item.ItemOption(213, 0));
//            nhan.itemOptions.add(new Item.ItemOption(213, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy Diệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdgenki(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 650);
//            Item quan = ItemService.gI().otphd((short) 651);
//            Item gang = ItemService.gI().otphd((short) 657);
//            Item giay = ItemService.gI().otphd((short) 658);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{650, 651, 657, 658, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(128, 0));
//            quan.itemOptions.add(new Item.ItemOption(128, 0));
//            gang.itemOptions.add(new Item.ItemOption(128, 0));
//            giay.itemOptions.add(new Item.ItemOption(128, 0));
//            nhan.itemOptions.add(new Item.ItemOption(128, 0));
            ao.itemOptions.add(new Item.ItemOption(140, 0));
//            quan.itemOptions.add(new Item.ItemOption(140, 0));
//            gang.itemOptions.add(new Item.ItemOption(140, 0));
//            giay.itemOptions.add(new Item.ItemOption(140, 0));
//            nhan.itemOptions.add(new Item.ItemOption(140, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy Diệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdson(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 650);
//            Item quan = ItemService.gI().otphd((short) 651);
//            Item gang = ItemService.gI().otphd((short) 657);
//            Item giay = ItemService.gI().otphd((short) 658);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{650, 651, 657, 658, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(129, 0));
//            quan.itemOptions.add(new Item.ItemOption(129, 0));
//            gang.itemOptions.add(new Item.ItemOption(129, 0));
//            giay.itemOptions.add(new Item.ItemOption(129, 0));
//            nhan.itemOptions.add(new Item.ItemOption(129, 0));
            ao.itemOptions.add(new Item.ItemOption(141, 0));
//            quan.itemOptions.add(new Item.ItemOption(141, 0));
//            gang.itemOptions.add(new Item.ItemOption(141, 0));
//            giay.itemOptions.add(new Item.ItemOption(141, 0));
//            nhan.itemOptions.add(new Item.ItemOption(141, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy Diệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdpico(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 652);
//            Item quan = ItemService.gI().otphd((short) 653);
//            Item gang = ItemService.gI().otphd((short) 659);
//            Item giay = ItemService.gI().otphd((short) 660);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{652, 653, 659, 660, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(130, 0));
//            quan.itemOptions.add(new Item.ItemOption(214, 0));
//            gang.itemOptions.add(new Item.ItemOption(214, 0));
//            giay.itemOptions.add(new Item.ItemOption(214, 0));
//            nhan.itemOptions.add(new Item.ItemOption(214, 0));
            ao.itemOptions.add(new Item.ItemOption(142, 0));
//            quan.itemOptions.add(new Item.ItemOption(215, 0));
//            gang.itemOptions.add(new Item.ItemOption(215, 0));
//            giay.itemOptions.add(new Item.ItemOption(215, 0));
//            nhan.itemOptions.add(new Item.ItemOption(215, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy Diệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdoctieu(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 652);
//            Item quan = ItemService.gI().otphd((short) 653);
//            Item gang = ItemService.gI().otphd((short) 659);
//            Item giay = ItemService.gI().otphd((short) 660);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{652, 653, 659, 660, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(131, 0));
//            quan.itemOptions.add(new Item.ItemOption(131, 0));
//            gang.itemOptions.add(new Item.ItemOption(131, 0));
//            giay.itemOptions.add(new Item.ItemOption(131, 0));
//            nhan.itemOptions.add(new Item.ItemOption(131, 0));
            ao.itemOptions.add(new Item.ItemOption(143, 0));
//            quan.itemOptions.add(new Item.ItemOption(143, 0));
//            gang.itemOptions.add(new Item.ItemOption(143, 0));
//            giay.itemOptions.add(new Item.ItemOption(143, 0));
//            nhan.itemOptions.add(new Item.ItemOption(143, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy Diệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdpiko(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 652);
//            Item quan = ItemService.gI().otphd((short) 653);
//            Item gang = ItemService.gI().otphd((short) 659);
//            Item giay = ItemService.gI().otphd((short) 660);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{652, 653, 659, 660, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(132, 0));
//            quan.itemOptions.add(new Item.ItemOption(132, 0));
//            gang.itemOptions.add(new Item.ItemOption(132, 0));
//            giay.itemOptions.add(new Item.ItemOption(132, 0));
//            nhan.itemOptions.add(new Item.ItemOption(132, 0));
            ao.itemOptions.add(new Item.ItemOption(144, 0));
//            quan.itemOptions.add(new Item.ItemOption(144, 0));
//            gang.itemOptions.add(new Item.ItemOption(144, 0));
//            giay.itemOptions.add(new Item.ItemOption(144, 0));
//            nhan.itemOptions.add(new Item.ItemOption(144, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 4) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy DIệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdcadick(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 654);
//            Item quan = ItemService.gI().otphd((short) 655);
//            Item gang = ItemService.gI().otphd((short) 661);
//            Item giay = ItemService.gI().otphd((short) 662);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{654, 655, 661, 662, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(133, 0));
//            quan.itemOptions.add(new Item.ItemOption(133, 0));
//            gang.itemOptions.add(new Item.ItemOption(133, 0));
//            giay.itemOptions.add(new Item.ItemOption(133, 0));
//            nhan.itemOptions.add(new Item.ItemOption(133, 0));
            ao.itemOptions.add(new Item.ItemOption(136, 0));
//            quan.itemOptions.add(new Item.ItemOption(136, 0));
//            gang.itemOptions.add(new Item.ItemOption(136, 0));
//            giay.itemOptions.add(new Item.ItemOption(136, 0));
//            nhan.itemOptions.add(new Item.ItemOption(136, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy Diệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdcadic(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 654);
//            Item quan = ItemService.gI().otphd((short) 655);
//            Item gang = ItemService.gI().otphd((short) 661);
//            Item giay = ItemService.gI().otphd((short) 662);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{654, 655, 661, 662, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(134, 0));
//            quan.itemOptions.add(new Item.ItemOption(134, 0));
//            gang.itemOptions.add(new Item.ItemOption(134, 0));
//            giay.itemOptions.add(new Item.ItemOption(134, 0));
//            nhan.itemOptions.add(new Item.ItemOption(134, 0));
            ao.itemOptions.add(new Item.ItemOption(137, 0));
//            quan.itemOptions.add(new Item.ItemOption(137, 0));
//            gang.itemOptions.add(new Item.ItemOption(137, 0));
//            giay.itemOptions.add(new Item.ItemOption(137, 0));
//            nhan.itemOptions.add(new Item.ItemOption(137, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy Diệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }

    public void sethdnappa(Player player) throws Exception {
        for (int i = 0; i < 1; i++) {
            Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2115 + i);
//            Item ao = ItemService.gI().otphd((short) 654);
//            Item quan = ItemService.gI().otphd((short) 655);
//            Item gang = ItemService.gI().otphd((short) 661);
//            Item giay = ItemService.gI().otphd((short) 662);
//            Item nhan = ItemService.gI().otphd((short) 656);
            int[] dohd = new int[]{654, 655, 661, 662, 656};

            int ramdom = new Random().nextInt(dohd.length);

            Item ao = ItemService.gI().otphd((short) dohd[ramdom]);
            ao.itemOptions.add(new Item.ItemOption(135, 0));
//            quan.itemOptions.add(new Item.ItemOption(135, 0));
//            gang.itemOptions.add(new Item.ItemOption(135, 0));
//            giay.itemOptions.add(new Item.ItemOption(135, 0));
//            nhan.itemOptions.add(new Item.ItemOption(135, 0));
            ao.itemOptions.add(new Item.ItemOption(138, 0));
//            quan.itemOptions.add(new Item.ItemOption(138, 0));
//            gang.itemOptions.add(new Item.ItemOption(138, 0));
//            giay.itemOptions.add(new Item.ItemOption(138, 0));
//            nhan.itemOptions.add(new Item.ItemOption(138, 0));
            ao.itemOptions.add(new Item.ItemOption(30, 0));
//            quan.itemOptions.add(new Item.ItemOption(30, 0));
//            gang.itemOptions.add(new Item.ItemOption(30, 0));
//            giay.itemOptions.add(new Item.ItemOption(30, 0));
//            nhan.itemOptions.add(new Item.ItemOption(30, 0));
            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 1) {
                InventoryServiceNew.gI().addItemBag(player, ao);
//                InventoryServiceNew.gI().addItemBag(player, quan);
//                InventoryServiceNew.gI().addItemBag(player, gang);
//                InventoryServiceNew.gI().addItemBag(player, giay);
//                InventoryServiceNew.gI().addItemBag(player, nhan);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã nhận được món Hủy Diệt ");
                InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }
//tao chi so option trang bi tan thu

    public void setTanThu(Player player) {
        Item hq = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1667);
        Item ao = ItemService.gI().otpTanThu((short) 1668);
        Item quan = ItemService.gI().otpTanThu((short) 1669);
        Item gang = ItemService.gI().otpTanThu((short) 1670);
        Item giay = ItemService.gI().otpTanThu((short) 1671);
        Item nhan = ItemService.gI().otpTanThu((short) 1672);
        Item caitrang = ItemService.gI().otpTanThu((short) 1673);

        ao.itemOptions.add(new Item.ItemOption(188, 0));
        quan.itemOptions.add(new Item.ItemOption(188, 0));
        gang.itemOptions.add(new Item.ItemOption(188, 0));
        giay.itemOptions.add(new Item.ItemOption(188, 0));
        nhan.itemOptions.add(new Item.ItemOption(188, 0));
        caitrang.itemOptions.add(new Item.ItemOption(188, 0));

        ao.itemOptions.add(new Item.ItemOption(189, 100));
        quan.itemOptions.add(new Item.ItemOption(189, 100));
        gang.itemOptions.add(new Item.ItemOption(189, 100));
        giay.itemOptions.add(new Item.ItemOption(189, 100));
        nhan.itemOptions.add(new Item.ItemOption(189, 100));
        caitrang.itemOptions.add(new Item.ItemOption(189, 100));

        ao.itemOptions.add(new Item.ItemOption(30, 0));
        quan.itemOptions.add(new Item.ItemOption(30, 0));
        gang.itemOptions.add(new Item.ItemOption(30, 0));
        giay.itemOptions.add(new Item.ItemOption(30, 0));
        nhan.itemOptions.add(new Item.ItemOption(30, 0));
        caitrang.itemOptions.add(new Item.ItemOption(30, 0));
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 5) {
            InventoryServiceNew.gI().addItemBag(player, ao);
            InventoryServiceNew.gI().addItemBag(player, quan);
            InventoryServiceNew.gI().addItemBag(player, gang);
            InventoryServiceNew.gI().addItemBag(player, giay);
            InventoryServiceNew.gI().addItemBag(player, nhan);
            InventoryServiceNew.gI().addItemBag(player, caitrang);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được set Tân thủ ");
            InventoryServiceNew.gI().subQuantityItemsBag(player, hq, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 6 ô trống hành trang");
        }
    }

//    public void setThienMa(Player player) {
//        Item ao = ItemService.gI().otpThienMa((short) 1674);
//        Item quan = ItemService.gI().otpThienMa((short) 1675);
//        Item gang = ItemService.gI().otpThienMa((short) 1676);
//        Item giay = ItemService.gI().otpThienMa((short) 1677);
//        Item nhan = ItemService.gI().otpThienMa((short) 1678);
//        Item caitrang = ItemService.gI().otpThienMa((short) 1679);
//
//        ao.itemOptions.add(new Item.ItemOption(191, 500));
//        quan.itemOptions.add(new Item.ItemOption(191, 500));
//        gang.itemOptions.add(new Item.ItemOption(191, 500));
//        giay.itemOptions.add(new Item.ItemOption(191, 500));
//        nhan.itemOptions.add(new Item.ItemOption(191, 500));
//        caitrang.itemOptions.add(new Item.ItemOption(191, 500));
//
//        ao.itemOptions.add(new Item.ItemOption(192, 1));
//        quan.itemOptions.add(new Item.ItemOption(192, 1));
//        gang.itemOptions.add(new Item.ItemOption(192, 1));
//        giay.itemOptions.add(new Item.ItemOption(192, 1));
//        nhan.itemOptions.add(new Item.ItemOption(192, 1));
//        caitrang.itemOptions.add(new Item.ItemOption(192, 1));
//
//        ao.itemOptions.add(new Item.ItemOption(30, 0));
//        quan.itemOptions.add(new Item.ItemOption(30, 0));
//        gang.itemOptions.add(new Item.ItemOption(30, 0));
//        giay.itemOptions.add(new Item.ItemOption(30, 0));
//        nhan.itemOptions.add(new Item.ItemOption(30, 0));
//        caitrang.itemOptions.add(new Item.ItemOption(30, 0));
//
//    }
    public Item itemBT(int itemId, int btId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            item.itemOptions.add(new Item.ItemOption(btId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdSKH(btId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public Item itemSKH(int itemId, int skhId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
            item.itemOptions.add(new Item.ItemOption(skhId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdSKH(skhId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public ItemMap itemMapSKH(Zone zone, int tempId, int quantity, int x, int y, long playerId, int skhid) {
        ItemMap item = createItemMapSetKichHoat(zone, tempId, quantity, x, y, playerId);
        if (item != null) {
            item.options.addAll(ItemService.gI().getListOptionItemShop((short) tempId));
            item.options.add(new Item.ItemOption(skhid, 1));
            item.options.add(new Item.ItemOption(optionIdSKH(skhid), 1));
            item.options.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public Item itemDoHuyDiet(int itemId, int gender) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        List<Integer> nhan = Arrays.asList(656);
        //áo
        if (ao.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
        }
        //quần
        if (Util.isTrue(80, 100)) {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(11) + 120))); // hp 120k-130k
            }
        } else {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(21) + 130))); // hp 130-150k 15%
            }
        }
        //găng
        if (Util.isTrue(80, 100)) {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(651) + 9350))); // 9350-10000
            }
        } else {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 10000))); // gang 15% 10-11k -xayda 12k1
            }
        }
        //giày
        if (Util.isTrue(80, 100)) {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 90))); // ki 90-110k
            }
        } else {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 110))); // ki 110-130k
            }
        }

        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(14, Util.highlightsItem(gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
        }
        dots.itemOptions.add(new Item.ItemOption(21, 120));
        dots.itemOptions.add(new Item.ItemOption(30, 1));
        return dots;
    }

    public int optionItemSKH(int typeItem) {
        switch (typeItem) {
            case 0:
                return 47;
            case 1:
                return 6;
            case 2:
                return 0;
            case 3:
                return 7;
            default:
                return 14;
        }
    }

    public int pagramItemSKH(int typeItem) {
        switch (typeItem) {
            case 0:
            case 2:
                return Util.nextInt(5);
            case 1:
            case 3:
                return Util.nextInt(20, 30);
            default:
                return Util.nextInt(1, 3);
        }
    }

    public int optionIdBTC2(int btId) {
        switch (btId) {
            case 14:
                return 14;
            case 27:
                return 27;
            case 28:
                return 28;
            case 50:
                return 50;
            case 77:
                return 77;
            case 94:
                return 94;
            case 103:
                return 103;
            case 187:
                return 187;
        }
        return -1;
    }

    public int optionIdSKH(int skhId) {
        switch (skhId) {
            case 127: //Set Arriety Taiyoken
                return 139;
            case 128: //Set Arriety Genki
                return 140;
            case 129: //Set Arriety Kamejoko
                return 141;
            case 130: //Set Arriety KI
                return 142;
            case 131: //Set Arriety Dame
                return 143;
            case 132: //Set Arriety Summon
                return 144;
            case 133: //Set Arriety Galick
                return 136;
            case 134: //Set Arriety Monkey
                return 137;
            case 135: //Set Arriety HP
                return 138;
            case 212: //Set Arriety Galick
                return 213;
            case 214: //Set Arriety Monkey
                return 215;
            case 216: //Set Arriety HP
                return 217;
        }
        return 0;
    }

    public Item itemDHD(int itemId, int dhdId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.add(new Item.ItemOption(dhdId, 1));
            item.itemOptions.add(new Item.ItemOption(optionIdDHD(dhdId), 1));
            item.itemOptions.add(new Item.ItemOption(30, 1));
        }
        return item;
    }

    public int optionIdDHD(int skhId) {
        switch (skhId) {
            case 127: //Set Arriety Taiyoken
                return 139;
            case 128: //Set Arriety Genki
                return 140;
            case 129: //Set Arriety Kamejoko
                return 141;
            case 130: //Set Arriety KI
                return 142;
            case 131: //Set Arriety Dame
                return 143;
            case 132: //Set Arriety Summon
                return 144;
            case 133: //Set Arriety Galick
                return 136;
            case 134: //Set Arriety Monkey
                return 137;
            case 135: //Set Arriety HP
                return 138;
        }
        return 0;
    }

    public Item randomCS_DTL(int itemId, int gender) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int nhd = 561;
        if (ao.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(101) + 800))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(12) + 35))); // hp 85-100k
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(150) + 4000))); // 8500-10000
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(16) + 30))); // ki 80-90k
        }
        if (nhd == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(4) + 11)); //chí mạng 17-19%
        }
        it.itemOptions.add(new Item.ItemOption(21, 40));// yêu cầu sm 80 tỉ
        it.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
        return it;
    }

    public Item randomCS_DHD(int itemId, int gender) {
        Item it = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 1800))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(16) + 85))); // hp 85-100k
        }
        if (gang.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(150) + 8500))); // 8500-10000
        }
        if (giay.contains(itemId)) {
            it.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(11) + 80))); // ki 80-90k
        }
        if (nhd == itemId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 17)); //chí mạng 17-19%
        }
        it.itemOptions.add(new Item.ItemOption(21, 80));// yêu cầu sm 80 tỉ
        it.itemOptions.add(new Item.ItemOption(30, 1));// ko the gd
        return it;
    }

    //Cải trang sự kiện 20/11
    public Item caitrang2011(boolean rating) {
        Item item = createItemSetKichHoat(680, 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, 24));//hp 28%
        item.itemOptions.add(new Item.ItemOption(103, 25));//ki 25%
        item.itemOptions.add(new Item.ItemOption(147, 24));//sd 26%
        item.itemOptions.add(new Item.ItemOption(117, 18));//Đẹp + 18% sd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item caitrangblackrose(boolean rating) {
        Item item = createItemSetKichHoat(2109, 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, 26));//hp 28%
        item.itemOptions.add(new Item.ItemOption(103, 26));//ki 25%
        item.itemOptions.add(new Item.ItemOption(50, 26));//sd 26%
        item.itemOptions.add(new Item.ItemOption(116, 0));//sd 26%
        item.itemOptions.add(new Item.ItemOption(117, 18));//Đẹp + 18% sd  
        if (Util.isTrue(950, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
            item.itemOptions.add(new Item.ItemOption(5, new Random().nextInt(5) + 25));//stcm

        }
        return item;
    }

    //610 - bong hoa
    //Phụ kiện bó hoa 20/11
    public Item phuKien2011(boolean rating) {
        Item item = createItemSetKichHoat(954, 1);
        item.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(5) + 5));
        item.itemOptions.add(new Item.ItemOption(147, new Random().nextInt(5) + 5));
        if (Util.isTrue(1, 100)) {
            item.itemOptions.get(Util.nextInt(item.itemOptions.size() - 1)).param = 10;
        }
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item vanBay2011(boolean rating) {
        Item item = createItemSetKichHoat(795, 1);
        item.itemOptions.add(new Item.ItemOption(89, 1));
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        if (Util.isTrue(950, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(3) + 1));//hsd
        }
        return item;
    }

    public Item daBaoVe() {
        Item item = createItemSetKichHoat(987, 1);
        item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
        return item;
    }

    public Item randomRac() {
        short[] racs = {20, 19, 18, 17};
        Item item = createItemSetKichHoat(racs[Util.nextInt(racs.length - 1)], 1);
        if (optionRac(item.template.id) != 0) {
            item.itemOptions.add(new Item.ItemOption(optionRac(item.template.id), 1));
        }
        return item;
    }

    public byte optionRac(short itemId) {
        switch (itemId) {
            case 220:
                return 71;
            case 221:
                return 70;
            case 222:
                return 69;
            case 224:
                return 67;
            case 223:
                return 68;
            default:
                return 0;
        }
    }

    public void openBoxVip(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 1) {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 2 ô trống hành trang");
            return;
        }
        if (player.inventory.event < 3000) {
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
            return;
        }
        Item item;
        if (Util.isTrue(45, 100)) {
            item = caitrang2011(false);
        } else {
            item = phuKien2011(false);
        }
        short[] icon = new short[2];
        icon[0] = 6983;
        icon[1] = item.template.iconID;
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        player.inventory.event -= 3000;
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
        CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
    }

    public void giaobong(Player player, int quantity) {
        if (quantity > 10000) {
            return;
        }
        try {
            Item itemUse = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 610);
            if (itemUse.quantity < quantity) {
                Service.gI().sendThongBao(player, "Bạn không đủ bông...");
                return;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemUse, quantity);
            Item item = createItemSetKichHoat(736, (quantity / 100));
            item.itemOptions.add(new Item.ItemOption(30, 1));//ko the gd
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được x" + (quantity / 100) + " " + item.template.name);
        } catch (Exception e) {
            e.printStackTrace();
            Service.gI().sendThongBao(player, "Bạn không đủ bông...");
        }
    }

    public Item PK_WC(int itemId) {
        Item phukien = createItemSetKichHoat(itemId, 1);
        int co = 983;
        int cup = 982;
        int bong = 966;
        if (cup == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(77, new Random().nextInt(6) + 5)); // hp 5-10%
        }
        if (co == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(103, new Random().nextInt(6) + 5)); // ki 5-10%
        }
        if (bong == itemId) {
            phukien.itemOptions.add(new Item.ItemOption(50, new Random().nextInt(6) + 5)); // sd 5- 10%
        }
        phukien.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
        phukien.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100)) {// tỉ lệ ra hsd
            phukien.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return phukien;
    }

    //Cải trang Gohan WC
    public Item CT_WC(boolean rating) {
        Item caitrang = createItemSetKichHoat(883, 1);
        caitrang.itemOptions.add(new Item.ItemOption(77, 30));// hp 30%
        caitrang.itemOptions.add(new Item.ItemOption(103, 15));// ki 15%
        caitrang.itemOptions.add(new Item.ItemOption(50, 20));// sd 20%
        caitrang.itemOptions.add(new Item.ItemOption(192, 1));//WORLDCUP
        caitrang.itemOptions.add(new Item.ItemOption(193, 1));//(2 món kích hoạt ....)
        if (Util.isTrue(99, 100) && rating) {// tỉ lệ ra hsd
            caitrang.itemOptions.add(new Item.ItemOption(93, new Random().nextInt(2) + 1));//hsd
        }
        return caitrang;
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        if (player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 555 && item.template.id <= 567).count() < 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thần linh");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 650 && item.template.id <= 662).count() < 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ hủy diệt");
            return;
        }
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 555 && item.template.id <= 567).findFirst().get();
            List<Item> itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.template.id >= 650 && item.template.id <= 662).collect(Collectors.toList());
            short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

            Item itemTS = DoThienSu(itemIds[player.gender][itemTL.template.type], player.gender);
            InventoryServiceNew.gI().addItemBag(player, itemTS);

            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTL, 1);
            itemHDs.forEach(item -> InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1));

            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public Item DoThienSu(int itemId, int gender) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
        }
        //quần
        if (Util.isTrue(80, 100)) {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(11) + 120))); // hp 120k-130k
            }
        } else {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(21) + 130))); // hp 130-150k 15%
            }
        }
        //găng
        if (Util.isTrue(80, 100)) {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(651) + 9350))); // 9350-10000
            }
        } else {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(1001) + 10000))); // gang 15% 10-11k -xayda 12k1
            }
        }
        //giày
        if (Util.isTrue(80, 100)) {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 90))); // ki 90-110k
            }
        } else {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new Item.ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 110))); // ki 110-130k
            }
        }

        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new Item.ItemOption(14, Util.highlightsItem(gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
        }
        dots.itemOptions.add(new Item.ItemOption(21, 120));
        dots.itemOptions.add(new Item.ItemOption(30, 1));
        return dots;
    }


    public List<Item.ItemOption> getListOptionItemShop(short id) {
        List<Item.ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            if (itemShop.temp.id == id && list.size() == 0) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }
}
