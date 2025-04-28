/*
 * Dev By Duy
 */
package nro.kygui;

import nro.models.item.Item;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.NpcService;
import nro.services.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import nro.jdbc.daos.GodGK;

/**
 * Stole By Arriety
 */
public class ShopKyGuiService {

    private static ShopKyGuiService instance;
    private final short costThoiVangKiGui = 5;
    private final short costThoiVangUpTop = 10;

    public ShopKyGuiService() {
    }

    public static ShopKyGuiService gI() {
        if (instance == null) {
            instance = new ShopKyGuiService();
        }

        return instance;
    }

    private List<ItemKyGui> getItemKyGui2(Player pl, short tab, short to, short max) {
        List<ItemKyGui> its = new ArrayList();
        List<ItemKyGui> listSort = new ArrayList();
        List<ItemKyGui> listSort2 = new ArrayList();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> {
            return it != null && it.tab == tab && !it.isBuy;
        }).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter((ix) -> {
            return ix != null;
        }).sorted(Comparator.comparing((ix) -> {
            return ix.isUpTop;
        }, Comparator.reverseOrder())).forEach((ix) -> {
            listSort.add(ix);
        });

        for (int i = to; i <= max && i < listSort.size(); ++i) {
            listSort2.add((ItemKyGui) listSort.get(i));
        }

        return listSort2;
    }

    private List<ItemKyGui> getItemKyGui(Player pl, short tab, short... max) {
        List<ItemKyGui> its = new ArrayList();
        List<ItemKyGui> listSort = new ArrayList();
        List<ItemKyGui> listSort2 = new ArrayList();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> {
            return it != null && it.tab == tab && !it.isBuy && (long) it.player_sell != pl.id;
        }).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter((ix) -> {
            return ix != null;
        }).sorted(Comparator.comparing((ix) -> {
            return ix.isUpTop;
        }, Comparator.reverseOrder())).forEach((ix) -> {
            listSort.add(ix);
        });
        int i;
        if (max.length != 2) {
            if (max.length == 1 && listSort.size() > max[0]) {
                for (i = 0; i < max[0]; ++i) {
                    if (listSort.get(i) != null) {
                        listSort2.add((ItemKyGui) listSort.get(i));
                    }
                }

                return listSort2;
            } else {
                return listSort;
            }
        } else {
            if (listSort.size() > max[1]) {
                for (i = max[0]; i < max[1]; ++i) {
                    if (listSort.get(i) != null) {
                        listSort2.add((ItemKyGui) listSort.get(i));
                    }
                }
            } else {
                for (i = max[0]; i <= max[0]; ++i) {
                    if (listSort.get(i) != null) {
                        listSort2.add((ItemKyGui) listSort.get(i));
                    }
                }
            }

            return listSort2;
        }
    }

    private List<ItemKyGui> getItemKyGui() {
        List<ItemKyGui> its = new ArrayList();
        List<ItemKyGui> listSort = new ArrayList();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> {
            return it != null && !it.isBuy;
        }).forEachOrdered((it) -> {
            its.add(it);
        });
        its.stream().filter((i) -> {
            return i != null;
        }).sorted(Comparator.comparing((i) -> {
            return i.isUpTop;
        }, Comparator.reverseOrder())).forEach((i) -> {
            listSort.add(i);
        });
        return listSort;
    }

    private boolean isKyGui(Item item) {
        switch (item.template.type) {
            case 21:
            case 72:
                return true;
            case 27:
                switch (item.template.id) {
                    case 568:
                    case 921:
                    case 1155:
                    case 1156:
                        return true;
                    default:
                        return false;
                }
            default:
                for (int i = 0; i < item.itemOptions.size(); ++i) {
                    if (((Item.ItemOption) item.itemOptions.get(i)).optionTemplate.id == 86) {
                        return true;
                    }
                }

                return false;
        }
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

    public void buyItem(Player pl, int id) {
        if (pl.getSession().actived && pl.nPoint.power < 17000000000L) {
            Service.gI().sendThongBao(pl, "Yêu cầu Kích hoạt tài khoản và sức mạnh lớn hơn 17 tỷ");
            this.openShopKyGui(pl);
        } else {
            ItemKyGui it = this.getItemBuy(id);
            if (it != null && !it.isBuy) {
                if ((long) it.player_sell == pl.id) {
                    Service.gI().sendThongBao(pl, "Không thể mua vật phẩm bản thân đăng bán");
                    this.openShopKyGui(pl);
                } else {
                    boolean isBuy = false;
                    if (it.goldSell > 0) {
                        if (this.SubThoiVang(pl, it.goldSell)) {
                            isBuy = true;
                        } else {
                            Service.gI().sendThongBao(pl, "Bạn không đủ thỏi vàng để mua vật phẩm");
                            isBuy = false;
                        }
                    } else if (it.gemSell > 0) {
                        if (pl.inventory.ruby >= it.gemSell) {
                            Inventory var10000 = pl.inventory;
                            var10000.ruby -= it.gemSell;
                            isBuy = true;
                        } else {
                            Service.gI().sendThongBao(pl, "Bạn không đủ hồng ngọc để mua vật phẩm này!");
                            isBuy = false;
                        }
                    }

                    Service.gI().sendMoney(pl);
                    if (isBuy) {
                        Item item = ItemService.gI().createNewItem(it.itemId);
                        item.quantity = it.quantity;
                        item.itemOptions.addAll(it.options);
                        it.isBuy = true;
                        if (it.isBuy) {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 0) {
                                pl.inventory.itemsMailBox.add(item);
                                GodGK.updateMailBox(pl);
                                Service.gI().sendThongBao(pl, "Bạn đã nhận " + item.template.name + " về hòm thư");
                            } else {
                                InventoryServiceNew.gI().addItemBag(pl, item);
                                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + item.template.name);
                            }
                            InventoryServiceNew.gI()
                                    .sendItemBags(pl);
                            Service.gI()
                                    .sendMoney(pl);

                            this.openShopKyGui(pl);
                        }
                    }

                }
            } else {
                Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
                this.openShopKyGui(pl);
            }
        }
    }

    public ItemKyGui getItemBuy(int id) {
        Iterator var2 = this.getItemKyGui().iterator();

        ItemKyGui it;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            it = (ItemKyGui) var2.next();
        } while (it == null || it.id != id);

        return it;
    }

    public ItemKyGui getItemBuy(Player pl, int id) {
        Iterator var3 = ShopKyGuiManager.gI().listItem.iterator();

        ItemKyGui it;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            it = (ItemKyGui) var3.next();
        } while (it == null || it.id != id || (long) it.player_sell != pl.id);

        return it;
    }

    public void openShopKyGui(Player pl, byte index, int page) {
        if (page <= this.getItemKyGui(pl, index).size()) {
            Message msg = null;

            try {
                msg = new Message(-100);
                msg.writer().writeByte(index);
                List<ItemKyGui> items = this.getItemKyGui(pl, index);
//                Chỉnh giới hạn trang 
                List<ItemKyGui> itemsSend = this.getItemKyGui2(pl, index, (short) (page * 20), (short) (page * 20 + 20));
                short tab = (short) (items.size() / 20 > 0 ? items.size() / 20 + 1 : 1);

                msg.writer().writeByte(tab);
                msg.writer().writeByte(page);
                msg.writer().writeByte(itemsSend.size());

                for (int j = 0; j < itemsSend.size(); ++j) {
                    ItemKyGui itk = (ItemKyGui) itemsSend.get(j);
                    Item it = ItemService.gI().createNewItem(itk.itemId);
                    it.itemOptions.clear();
                    if (itk.options.isEmpty()) {
                        it.itemOptions.add(new Item.ItemOption(73, 0));
                    } else {
                        it.itemOptions.addAll(itk.options);
                    }

                    msg.writer().writeShort(it.template.id);
                    msg.writer().writeShort(itk.id);
                    msg.writer().writeInt(itk.goldSell);
                    msg.writer().writeInt(itk.gemSell);
                    msg.writer().writeByte(0);
                    if (pl.getSession().version >= 222) {
                        msg.writer().writeInt(itk.quantity);
                    } else {
                        msg.writer().writeByte(itk.quantity);
                    }

                    msg.writer().writeByte((long) itk.player_sell == pl.id ? 1 : 0);
                    msg.writer().writeByte(it.itemOptions.size());

                    for (int a = 0; a < it.itemOptions.size(); ++a) {
                        msg.writer().writeByte(((Item.ItemOption) it.itemOptions.get(a)).optionTemplate.id);
                        msg.writer().writeShort(((Item.ItemOption) it.itemOptions.get(a)).param);
                    }

                    msg.writer().writeByte(0);
                }

                pl.sendMessage(msg);
            } catch (Exception var15) {
                var15.printStackTrace();
            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }

            }

        }
    }

    public void upItemToTop(Player pl, int id) {
        ItemKyGui it = this.getItemBuy(id);
        if (it != null && !it.isBuy) {
            if ((long) it.player_sell != pl.id) {
                Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
                this.openShopKyGui(pl);
            } else {
                pl.iDMark.setIdItemUpTop((short) id);
                NpcService.gI().createMenuConMeo(pl, 527, -1, "Bạn có muốn đưa vật phẩm ['" + ItemService.gI().createNewItem(it.itemId).template.name + "'] của bản thân lên trang đầu?\nYêu cầu "+costThoiVangUpTop+" thỏi vàng.", new String[]{"Đồng ý", "Từ Chối"});
            }
        } else {
            Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
        }
    }

    public void StartupItemToTop(Player pl) {
        if (!this.SubThoiVang(pl, costThoiVangUpTop)) {
            Service.gI().sendThongBao(pl, "Bạn cần có ít nhất " + costThoiVangUpTop + " thỏi vàng đưa vật phẩm lên trang đầu");
        } else {
            Iterator var2 = ShopKyGuiManager.gI().listItem.iterator();

            while (var2.hasNext()) {
                ItemKyGui its = (ItemKyGui) var2.next();
                if (its.id == pl.iDMark.getIdItemUpTop()) {
                    its.isUpTop = 1;
                    Service.gI().sendThongBao(pl, "Đưa vật phẩm lên trang đầu thành công");
                    break;
                }
            }

            this.openShopKyGui(pl);
        }
    }

    public void claimOrDel(Player pl, byte action, int id) {
        ItemKyGui it = this.getItemBuy(pl, id);

        switch (action) {
            case 1:
                // Xử lý hủy bán vật phẩm
                if (it == null || it.isBuy) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc đã được bán");
                    return;
                }

                if ((long) it.player_sell != pl.id) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
                    this.openShopKyGui(pl);
                    return;
                }

                Item item = ItemService.gI().createNewItem(it.itemId);
                item.quantity = it.quantity;
                item.itemOptions.addAll(it.options);

                if (ShopKyGuiManager.gI().listItem.remove(it)) {
                    InventoryServiceNew.gI().addItemBag(pl, item);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    Service.gI().sendMoney(pl);
                    Service.gI().sendThongBao(pl, "Hủy bán vật phẩm thành công");
                    this.openShopKyGui(pl);
                }
                break;

            case 2:
                // Xử lý kí gửi mặt hàng mới
                if (it == null || !it.isBuy) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại hoặc chưa được bán");
                    return;
                }

                if ((long) it.player_sell != pl.id) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thuộc quyền sở hữu");
                    this.openShopKyGui(pl);
                    return;
                }

                if (it.goldSell > 0) {
                    Item tvAdd = ItemService.gI().createNewItem((short) 457);
                    tvAdd.quantity = it.goldSell - it.goldSell * 20 / 100;
                    InventoryServiceNew.gI().addItemBag(pl, tvAdd);
                    InventoryServiceNew.gI().sendItemBags(pl);
                } else if (it.gemSell > 0) {
                    pl.inventory.ruby += it.gemSell - it.gemSell * 20 / 100;
                }

                if (ShopKyGuiManager.gI().listItem.remove(it)) {
                    Service.gI().sendMoney(pl);

                    Service.gI().sendThongBao(pl, "Bạn đã bán vật phẩm thành công");
                    this.openShopKyGui(pl);
                }
                break;

            // Các trường hợp khác nếu có
        }
    }

    public List<ItemKyGui> getItemCanKiGui(Player pl) {
        List<ItemKyGui> its = new ArrayList();
        ShopKyGuiManager.gI().listItem.stream().filter((it) -> {
            return it != null && (long) it.player_sell == pl.id;
        }).forEachOrdered((it) -> {
            its.add(it);
        });
        pl.inventory.itemsBag.stream().filter((it) -> {
            return it.isNotNullItem();
        }).forEachOrdered((it) -> {
            its.add(new ItemKyGui(InventoryServiceNew.gI().getIndexBag(pl, it), it.template.id, (int) pl.id, (byte) 4, -1, -1, it.quantity, (byte) -1, it.itemOptions, false));
        });
        return its;
    }

    public int getMaxId() {
        try {
            List<Integer> id = new ArrayList();
            ShopKyGuiManager.gI().listItem.stream().filter((it) -> {
                return it != null;
            }).forEachOrdered((it) -> {
                id.add(it.id);
            });
            return id.isEmpty() ? 0 : (Integer) Collections.max(id);
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0;
        }
    }

    public byte getTabKiGui(Item it) {
        if (it.template.type >= 0 && it.template.type <= 2) {
            return 0;
        } else if (it.template.type >= 3 && it.template.type <= 4) {
            return 1;
        } else {
            return (byte) (it.template.type != 29 && it.template.type != 33 && it.template.type != 12 ? 3 : 2);
        }
    }

    public void KiGui(Player pl, int id, int money, byte moneyType, int quantity) {

        try {
            ItemKyGui existingItem = ShopKyGuiManager.gI().listItem.stream()
                    .filter(item -> item.itemId == pl.inventory.itemsBag.get(id).template.id && item.player_sell == pl.id)
                    .findFirst().orElse(null);
            if (existingItem != null) {
                Service.gI().sendThongBao(pl, "Mặt hàng đã có tại shop !!!");
                this.openShopKyGui(pl);
                return;
            }
            int itemCount = ShopKyGuiManager.gI().countItemsOfPlayer(pl.id);
            if (itemCount >= 30) {
                Service.gI().sendThongBao(pl, "Bạn chỉ có thể kí gửi tối đa 30 mặt hàng");
                // Gửi người chơi trở lại cửa hàng kí gửi
                this.openShopKyGui(pl);
                return;
            }

            if (!this.SubThoiVang(pl, costThoiVangKiGui)) {
                Service.gI().sendThongBao(pl, "Bạn cần có ít nhất "+costThoiVangKiGui+" thỏi vàng để làm phí đăng bán");
                this.openShopKyGui(pl);
                return;
            }

            Item it = ItemService.gI().copyItem((Item) pl.inventory.itemsBag.get(id));
            Iterator var7 = it.itemOptions.iterator();

            while (var7.hasNext()) {
                Item.ItemOption daubuoi = (Item.ItemOption) var7.next();
                if (daubuoi.optionTemplate.id == 30) {
                    Service.gI().sendThongBao(pl, "Vật phẩm không thể kí gửi");
                    this.openShopKyGui(pl);
                    return;
                }
            }

            if (money <= 0 || quantity > it.quantity) {
                this.openShopKyGui(pl);
                return;
            }

            if (quantity > 99) {
                Service.gI().sendThongBao(pl, "Ký gửi tối đa x99");
                this.openShopKyGui(pl);
                return;
            }

            Inventory var10000 = pl.inventory;
            var10000.gem -= 5;
            switch (moneyType) {
                case 0:
                    if (money > 100000) {
                        Service.gI().sendThongBao(pl, "không thể ký gửi quá 1000 thỏi vàng");
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, (Item) pl.inventory.itemsBag.get(id), quantity);
                        ShopKyGuiManager.gI().listItem.add(new ItemKyGui(this.getMaxId() + 1, it.template.id, (int) pl.id, this.getTabKiGui(it), money, -1, quantity, (byte) 0, it.itemOptions, false));
                        InventoryServiceNew.gI().sendItemBags(pl);
                        this.openShopKyGui(pl);
                        Service.gI().sendMoney(pl);
                        Service.gI().sendThongBao(pl, "Đăng bán thành công");
                    }
                    break;
                case 1:
                    if (money > 100000) {
                        Service.gI().sendThongBao(pl, "không thể ký gửi quá 100000 ngọc");
                    } else {
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, (Item) pl.inventory.itemsBag.get(id), quantity);
                        ShopKyGuiManager.gI().listItem.add(new ItemKyGui(this.getMaxId() + 1, it.template.id, (int) pl.id, this.getTabKiGui(it), -1, money, quantity, (byte) 0, it.itemOptions, false));
                        InventoryServiceNew.gI().sendItemBags(pl);
                        this.openShopKyGui(pl);
                        Service.gI().sendMoney(pl);
                        Service.gI().sendThongBao(pl, "Đăng bán thành công");
                    }
                    break;
                default:
                    Service.gI().sendThongBao(pl, "Có lỗi xảy ra");
                    this.openShopKyGui(pl);
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

    }

    public void openShopKyGui(Player pl) {
        Message msg = null;

        try {
            msg = new Message(-44);
            msg.writer().writeByte(2);
            msg.writer().writeByte(5);

            for (byte i = 0; i < 5; ++i) {
                int a;
                if (i == 4) {
                    msg.writer().writeUTF(ShopKyGuiManager.gI().tabName[i]);
                    msg.writer().writeByte(0);
                    msg.writer().writeByte(this.getItemCanKiGui(pl).size());

                    for (int j = 0; j < this.getItemCanKiGui(pl).size(); ++j) {
                        ItemKyGui itk = (ItemKyGui) this.getItemCanKiGui(pl).get(j);
                        if (itk != null) {
                            Item it = ItemService.gI().createNewItem(itk.itemId);
                            it.itemOptions.clear();
                            if (itk.options.isEmpty()) {
                                it.itemOptions.add(new Item.ItemOption(73, 0));
                            } else {
                                it.itemOptions.addAll(itk.options);
                            }

                            msg.writer().writeShort(it.template.id);
                            msg.writer().writeShort(itk.id);
                            msg.writer().writeInt(itk.goldSell);
                            msg.writer().writeInt(itk.gemSell);
                            if (this.getItemBuy(pl, itk.id) == null) {
                                msg.writer().writeByte(0);
                            } else if (itk.isBuy) {
                                msg.writer().writeByte(2);
                            } else {
                                msg.writer().writeByte(1);
                            }

                            msg.writer().writeInt(itk.quantity);
                            msg.writer().writeByte(1);
                            msg.writer().writeByte(it.itemOptions.size());

                            for (a = 0; a < it.itemOptions.size(); ++a) {
                                msg.writer().writeByte(((Item.ItemOption) it.itemOptions.get(a)).optionTemplate.id);
                                msg.writer().writeShort(((Item.ItemOption) it.itemOptions.get(a)).param);
                            }

                            msg.writer().writeByte(0);
                            msg.writer().writeByte(0);
                        }
                    }
                } else {
                    List<ItemKyGui> items = this.getItemKyGui(pl, i);
                    List<ItemKyGui> itemsSend = this.getItemKyGui2(pl, i, (byte) 0, (byte) 20);

                    msg.writer().writeUTF(ShopKyGuiManager.gI().tabName[i]);
                    short tab = (short) (items.size() / 20 > 0 ? items.size() / 20 + 1 : 1);

                    msg.writer().writeByte(tab); // 1 
                    msg.writer().writeByte(itemsSend.size()); // 2

                    for (a = 0; a < itemsSend.size(); ++a) {
                        ItemKyGui itk = (ItemKyGui) itemsSend.get(a);
                        Item it = ItemService.gI().createNewItem(itk.itemId);
                        it.itemOptions.clear();
                        if (itk.options.isEmpty()) {
                            it.itemOptions.add(new Item.ItemOption(73, 0));
                        } else {
                            it.itemOptions.addAll(itk.options);
                        }

                        msg.writer().writeShort(it.template.id);
                        msg.writer().writeShort(itk.id);
                        msg.writer().writeInt(itk.goldSell);
                        msg.writer().writeInt(itk.gemSell);
                        msg.writer().writeByte(0);
                        msg.writer().writeInt(itk.quantity);
                        msg.writer().writeByte((long) itk.player_sell == pl.id ? 1 : 0);
                        msg.writer().writeByte(it.itemOptions.size());

                        for (int b = 0; b < it.itemOptions.size(); ++b) {
                            msg.writer().writeByte(((Item.ItemOption) it.itemOptions.get(b)).optionTemplate.id);
                            msg.writer().writeShort(((Item.ItemOption) it.itemOptions.get(b)).param);
                        }

                        msg.writer().writeByte(0);
                        msg.writer().writeByte(0);
                    }
                }
            }

            pl.sendMessage(msg);
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }

        }

    }
}
