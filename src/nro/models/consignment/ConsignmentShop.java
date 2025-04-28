package nro.models.consignment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import nro.dialog.ConfirmDialog;
import nro.models.item.Item;
import nro.models.item.Item.ItemOption;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.NpcService;
import nro.services.Service;
import nro.utils.Logger;

/**
 * @author outcast c-cute hột me 😳
 */
public class ConsignmentShop {

    private static final ConsignmentShop INSTANCE = new ConsignmentShop();

    private static final byte CONSIGN = 0;

    private static final byte CCANCEL_CONSIGN = 1;

    private static final byte GET_MONEY = 2;

    private static final byte BUY = 3;

    private static final byte NEXT_PAGE = 4;

    private static final byte UP_TOP = 5;

    public static ConsignmentShop getInstance() {
        return INSTANCE;
    }

    @Getter
    private List<ConsignmentItem> list = new ArrayList<>();

    private Map<Long, ConsignmentItem> mapItemsExpired = new HashMap<>();

    public String[] tabName = {"Trang bị", "Phụ kiện", "Hỗ trợ", "Linh tinh", ""};

    public int countItemsOfPlayer(long playerId) {
        int count = 0;
        for (ConsignmentItem item : list) {
            if (item.getConsignorID() == playerId) {
                count++;
            }
        }
        return count;
    }

    public void handler(Player player, Message m) {
        try {
            DataInputStream dis = m.reader();
            byte action = dis.readByte();
            switch (action) {
                case CONSIGN: {
                    short itemID = dis.readShort();
                    byte monneyType = dis.readByte();
                    int money = dis.readInt();
                    int quantity = 0;
                    if (player.isVersionAbove(222)) {
                        quantity = dis.readInt();
                    } else {
                        quantity = dis.readByte();
                    }
                    consign(player, itemID, monneyType, money, quantity);
                }
                break;
                case BUY: {
                    short itemID = dis.readShort();
                    byte monneyType = dis.readByte();
                    int money = dis.readInt();
                    buy(player, itemID, monneyType, money);
                }
                break;
                case GET_MONEY: {
                    short itemID = dis.readShort();
                    getMoney(player, itemID);
                }
                break;
                case CCANCEL_CONSIGN: {
                    short itemID = dis.readShort();
                    cancelConsign(player, itemID);
                }
                break;
                case NEXT_PAGE: {
                    byte tab = dis.readByte();
                    byte page = dis.readByte();
                    nextPage(player, tab, page);
                }
                break;
                case UP_TOP: {
                    short itemID = dis.readShort();
                    upTop(player, itemID);
                }
                break;
            }
        } catch (EOFException e) {
            Logger.error("Đã đọc hết dữ liệu hoặc kết nối bị ngắt.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void upTop(Player player, short itemID) {
        ConfirmDialog confirmDialog = new ConfirmDialog("Bạn có muốn đưa vật phẩm này của bản thân lên trang đầu?\nYêu cầu 100 thỏi vàng.", () -> {
//            if (player.inventory.ruby < 50) {
//                Service.gI().sendThongBao(player, "Bạn không đủ hồng ngọc");
//                return;
//            }
            if (!this.SubThoiVang(player, 100)) {
                Service.gI().sendThongBao(player, "Bạn cần có ít nhất 100 thỏi vàng đưa vật phẩm lên trang đầu");
            } else {
                ConsignmentItem consignmentItem = findItemConsign(player.id, itemID);
                if (consignmentItem.isUpTop()) {
                    Service.gI().sendThongBao(player, "Vật phẩm này đã up top rồi");
                    return;
                }
                if (consignmentItem == null || consignmentItem.isSold()) {
                    Service.gI().sendThongBao(player, "Vật phẩm không tồn tại hoặc đã được bán");
                    return;
                }
//            player.inventory.subRuby(50);
                Service.gI().sendMoney(player);
                consignmentItem.setUpTop(true);
                Service.gI().sendThongBao(player, "Vật phẩm " + consignmentItem.template.name + " của bạn đã up top thành công");
                show(player);
            }
        }
        );
        confirmDialog.show(player);

    }

    private void cancelConsign(Player player, short itemID) {
        ConsignmentItem item = findItemConsign(player.id, itemID);
        if (item == null) {
            Service.gI().sendThongBao(player, "không tìm thấy vật phẩm");
            return;
        }
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Hủy kí gửi thành công");
        removeItem(item);
        show(player);
    }

    public void addItem(ConsignmentItem item) {
        synchronized (list) {
            list.add(item);
        }
    }

    public void addExpiredItem(ConsignmentItem item) {
        mapItemsExpired.put(item.getConsignorID(), item);
    }

    public void removeItem(ConsignmentItem item) {
        synchronized (list) {
            list.remove(item);
        }
    }

    private void consign(Player player, short itemID, byte monneyType, int money, int quantity) {
        if (quantity < 0 || quantity > 99) {
            Service.gI().sendThongBao(player, "Chỉ có thể kí gửi tối đa x99");
            return;
        }
        int itemCount = countItemsOfPlayer(player.id);
        if (itemCount >= 30) {
            Service.gI().sendThongBao(player, "Bạn chỉ có thể kí gửi tối đa 30 mặt hàng");
            // Gửi người chơi trở lại cửa hàng kí gửi
            show(player);
            return;
        }
        if (!this.SubThoiVang(player, 50)) {
            Service.gI().sendThongBao(player, "Bạn cần có ít nhất 50 thỏi vàng để làm phí đăng bán");
            return;
        }
        if (money <= 0 || money >= 100000) {
            Service.gI().sendThongBao(player, "Giá kí gửi tối đa 100000");
            return;
        }
        if (findItemConsign(player.id, itemID) != null) {
            Service.gI().sendThongBao(player, "Không thể kí gửi nhiều vật phẩm giống nhau");
            return;
        }
        Item item = InventoryServiceNew.gI().findItemBag(player, itemID);
        if (item == null) {
            Service.gI().sendThongBao(player, "Không tìm thấy vật phẩm");
            return;
        }
        ConsignmentItem consignmentItem = ItemService.gI().convertToConsignmentItem(item);
        if (monneyType == 0) {
            consignmentItem.setPriceGold(money);
        } else {
            consignmentItem.setPriceGem(money);
        }
        consignmentItem.createTime = System.currentTimeMillis();
        consignmentItem.setConsignorID(player.id);
        consignmentItem.setTab(getTabByType(consignmentItem.template.type));
        consignmentItem.quantity = quantity;
        addItem(consignmentItem);
        InventoryServiceNew.gI().subQuantityItemsBag(player, item, quantity);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        show(player);
        Service.gI().sendThongBao(player, "Kí gửi vật phẩm thành công");
    }

    public ConsignmentItem findItemConsign(long consignerID, short itemID) {
        for (ConsignmentItem consignmentItem : list) {
            if (consignmentItem.getConsignorID() == consignerID && consignmentItem.template.id == itemID) {
                return consignmentItem;
            }
        }
        return null;
    }

    private List<ConsignmentItem> getItemConsignByTab(Player player, byte tab, int... max) {
        List<ConsignmentItem> items = new ArrayList<>();
        List<ConsignmentItem> listSort = new ArrayList<>();
        List<ConsignmentItem> listSort2 = new ArrayList<>();

        for (ConsignmentItem item : list) {
            if (item != null && item.getTab() == tab && !item.isSold()) {
                items.add(item);
            }
        }

        Collections.sort(items, (item1, item2) -> Boolean.compare(item2.template.isUpToUp, item1.template.isUpToUp));

        if (max.length == 2) {
            int startIndex = Math.min(max[0], items.size());
            int endIndex = Math.min(max[1], items.size());
            listSort.addAll(items.subList(startIndex, endIndex));
        } else if (max.length == 1) {
            int endIndex = Math.min(max[0], items.size());
            listSort.addAll(items.subList(0, endIndex));
        } else {
            listSort.addAll(items);
        }

        for (ConsignmentItem item : listSort) {
            if (item != null) {
                listSort2.add(item);
            }
        }
        return listSort2;
    }

    private List<ConsignmentItem> getItemCanConsign(Player player) {
        List<ConsignmentItem> items = new ArrayList<>();
        list.stream().filter((it) -> (it != null && it.getConsignorID() == player.id)).forEachOrdered((it) -> {
            items.add(it);
        });
        player.inventory.itemsBag.stream().filter((item) -> (item.isNotNullItem() && canConsign(item.template.type) && item.canConsign())).forEachOrdered((it) -> {
            ConsignmentItem consignmentItem = ItemService.gI().convertToConsignmentItem(it);
            consignmentItem.setConsignorID(-1);
            consignmentItem.setTab((byte) 4);
            consignmentItem.setPriceGem(-1);
            consignmentItem.setPriceGold(-1);
            consignmentItem.setSold(false);
            items.add(consignmentItem);
        });
        return items;
    }

    private boolean canConsign(int type) {
        return (type < 5 && type >= 0) || type == 12 || type == 33 || type == 29 || type == 27 || type == 11 || type == 5;
    }

    private byte getTabByType(byte type) {
        byte tab = -1;
        if (type >= 0 && type <= 2) {
            tab = 0;
        } else if ((type >= 3 && type <= 4) || type == 33) {
            tab = 1;
        } else if (type == 29) {
            tab = 2;
        } else {
            tab = 3;
        }
        return tab;
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

    public void buy(Player player, short itemID, byte monneyType, int money) {
        for (ConsignmentItem item : list) {
            if (item.template.id == itemID && monneyType == monneyType && money == money) {
                if (item.isSold()) {
                    NpcService.gI().createTutorial(player, -1, "Vật phẩm đã được bán");
                    return;
                }
                Inventory inventory = player.inventory;
                if (monneyType == 0) {
//                    if (inventory.gold < money) {
//                        NpcService.gI().createTutorial(player, -1, "Bạn không đủ vàng");
//                        return;
//                    }
//                    player.inventory.subGold(money);
                    if (!this.SubThoiVang(player, money)) {
                        NpcService.gI().createTutorial(player, -1, "Bạn không đủ thỏi vàng");
                        return;
                    }
                } else {
                    if (inventory.ruby < money) {
                        NpcService.gI().createTutorial(player, -1, "Bạn không đủ hồng ngọc");
                        return;
                    }
                    player.inventory.subRuby(money);
//                    player.inventory.subGemAndRuby(money);
                }
                InventoryServiceNew.gI().addItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                Service.gI().sendThongBao(player, "Mua vật phẩm thành công");
                item.setSold(true);
                show(player);
                return;
            }
        }
    }

    public void getMoney(Player player, short itemID) {
        for (ConsignmentItem item : list) {
            if (item.template.id == itemID && item.getConsignorID() == player.id && item.isSold()) {
                Item tvAdd = ItemService.gI().createNewItem((short) 457);
                if (item.getPriceGold() > 0) {
                    tvAdd.quantity = item.getPriceGold() - (item.getPriceGold() * 10 / 100);
                    InventoryServiceNew.gI().addItemBag(player, tvAdd);
//                    player.inventory.gold += item.getPriceGold() - (item.getPriceGold() * 10 / 100);
                } else if (item.getPriceGem() > 0) {
                    player.inventory.ruby += item.getPriceGem() - (item.getPriceGem() * 10 / 100);
                }
                removeItem(item);
                Service.gI().sendMoney(player);
                Service.gI().sendThongBao(player, "Nhận tiền thành công (Phí 10%)");
                show(player);
                return;
            }
        }
    }

    public void nextPage(Player player, byte tab, int page) {
        Message msg = new Message(-100);
        try {
            int maxPage = (byte) (list.size() / 20 > 0 ? list.size() / 20 : 1);
            DataOutputStream ds = msg.writer();
            ds.writeByte(tab);
            ds.writeByte(maxPage);
            ds.writeByte(page);
            List<ConsignmentItem> list = getItemConsignByTab(player, tab, (byte) (page * 20), (byte) (page * 20 + 20));
            for (ConsignmentItem item : list) {
                ds.writeShort(item.template.id);
                ds.writeShort(item.template.id);
                ds.writeInt(item.getPriceGold());
                ds.writeInt(item.getPriceGem());

                ds.writeByte(0);

                if (player.isVersionAbove(222)) {
                    ds.writeInt(item.quantity);
                } else {
                    ds.writeByte(item.quantity);
                }
                ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
                ds.writeByte(item.itemOptions.size());
                for (ItemOption option : item.itemOptions) {
                    ds.writeByte(option.optionTemplate.id);
                    ds.writeShort(option.param);
                }
                ds.writeByte(0);
                ds.writeByte(0);
            }
            showItemCanConsign(player, ds);
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(Player player) {
        Message msg = new Message(-44);
        try {
            int tabLength = tabName.length;
            int maxPage = (byte) (list.size() / 20 > 0 ? list.size() / 20 : 1);
            DataOutputStream ds = msg.writer();
            ds.writeByte(2);
            ds.writeByte(tabLength);
            for (byte i = 0; i < tabLength - 1; i++) {
                List<ConsignmentItem> list = getItemConsignByTab(player, i);
                ds.writeUTF(tabName[i]);
                ds.writeByte(maxPage); // max page
                ds.writeByte(list.size());
                for (ConsignmentItem item : list) {
                    ds.writeShort(item.template.id);
                    ds.writeShort(item.template.id);
                    ds.writeInt(item.getPriceGold());
                    ds.writeInt(item.getPriceGem());

                    ds.writeByte(0);

                    if (player.isVersionAbove(222)) {
                        ds.writeInt(item.quantity);
                    } else {
                        ds.writeByte(item.quantity);
                    }
                    ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
                    ds.writeByte(item.itemOptions.size());
                    for (ItemOption option : item.itemOptions) {
                        ds.writeByte(option.optionTemplate.id);
                        ds.writeShort(option.param);
                    }
                    ds.writeByte(0);
                    ds.writeByte(0);
                }
            }
            showItemCanConsign(player, ds);
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showItemCanConsign(Player player, DataOutputStream ds) throws IOException {
        List<ConsignmentItem> items = getItemCanConsign(player);
        ds.writeUTF("");
        ds.writeByte(0); // max page
        ds.writeByte(items.size());
        for (ConsignmentItem item : items) {
            ds.writeShort(item.template.id);
            ds.writeShort(item.template.id);
            ds.writeInt(item.getPriceGold());
            ds.writeInt(item.getPriceGem());
            if (item.getConsignorID() == -1) {
                ds.writeByte(0);
            } else if (item.isSold()) {
                ds.writeByte(2);
            } else {
                ds.writeByte(1);
            }
            if (player.isVersionAbove(222)) {
                ds.writeInt(item.quantity);
            } else {
                ds.writeByte(item.quantity);
            }
            ds.writeByte(item.getConsignorID() == player.id ? 0 : 1); // isMe
            ds.writeByte(item.itemOptions.size());
            for (ItemOption option : item.itemOptions) {
                ds.writeByte(option.optionTemplate.id);
                ds.writeShort(option.param);
            }
            ds.writeByte(0);
            ds.writeByte(0);
        }
    }

    public int getDaysExpried(Long createTime) {
        long now = System.currentTimeMillis();
        long elapsedTimeMillis = now - createTime;
        long elapsedDays = elapsedTimeMillis / (24 * 60 * 60 * 1000);
        return (int) elapsedDays;
    }

    public void showExpiringItems(Player player) {
        if (mapItemsExpired.containsKey(player.id)) {
            StringBuilder sb = new StringBuilder();
            sb.append("|1|Danh sách vật phẩm sắp hết hạn:\n\n");
            for (Map.Entry<Long, ConsignmentItem> entry : mapItemsExpired.entrySet()) {
                ConsignmentItem item = entry.getValue();
                sb.append("- ").append(item.template.name).append("\n");
            }
            sb.append("Vật phẩm sẽ bị xóa nếu quá hạn 2 ngày");
            NpcService.gI().createMenuConMeo(player, -1, -1, sb.toString(), "OK");
            return;
        }
        Service.gI().sendThongBao(player, "Không có vật phẩm nào sắp hết hạn kí gửi");
    }

    public void sendExpirationNotification(Player player) {
        if (mapItemsExpired.containsKey(player.id)) {
            Service.gI().sendThongBao(player, "Bạn có vật phẩm sắp hết hạn đang kí gửi tại siêu thị");
        }
    }
}
