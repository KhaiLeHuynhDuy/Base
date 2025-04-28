package nro.services.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

public class Game {

    // Mini game
    public static byte TIME_START_GAME = 8;
    public static byte TIME_END_GAME = 22;
    public static byte TIME_MINUTES_GAME = 5;
    // SETTING GAME
    public long second = TIME_MINUTES_GAME * 60;
    public long currlast = System.currentTimeMillis();
    public long money = 0;
    public long min = 0;
    public long max = 99;

    // KẾT QUẢ
    public long result = 0;
    public long result_next = Util.nextInt((int) min, (int) max);
    // GIA
    public long price = 1;
    boolean giftGiven = false;
    // PLAYER NAME WIN

    public String result_name;

    public List<GameData> players = new ArrayList<>();

    public Timer timer;
    public TimerTask task;
    protected boolean actived = false;

    public void close() {
        try {
            actived = false;
            task.cancel();
            timer.cancel();
            task = null;
            timer = null;
        } catch (Exception e) {
            e.printStackTrace();
            task = null;
            timer = null;
        }
    }

    public void active(int delay) {
        if (!actived) {
            actived = true;
            this.timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    Game.this.update();
                }
            };
            this.timer.schedule(task, delay, delay);
        }
    }

    public void update() {
        try {
            if (second > 0) {
                second--;
                if (second <= 0) {
                    currlast = System.currentTimeMillis();
                }
//                Thread.sleep(1000);
            }
            if (second <= 0 && Util.canDoWithTime(currlast, 10000)) {
                ResetGame((int) result_next, (int) TIME_MINUTES_GAME * 60);
                result_next = Util.nextInt((int) min, (int) max);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newData(Player player, int point) {
        if (player.getSession().totalvnd < 1000) {
            Service.gI().sendThongBao(player, "Bạn không đủ tiền chơi con số may mắn");
            return;
        }
        if (players.stream().filter(d -> d.id == player.id).toList().size() >= 10) {
            Service.gI().sendThongBao(player, "Bạn đã chon 10 số rồi không thể chọn thêm");
            return;
        }
        players.stream().filter(d -> d.id == player.id).forEach((game)
                -> {
            if (game.id == player.id && game.point == point) {
                Service.gI().sendThongBao(player, "Số này bạn đã chọn rồi vui lòng chọn số khác.");
            }
        });
        if (PlayerDAO.subtotalvnd(player, 1000)) {
            if (!players.stream().anyMatch(game -> game.id == player.id && game.point == point)) {

                GameData data = new GameData();
                data.id = (int) player.id;
                data.point = point;
                players.add(data);
                money = 1;// add lên
                Service.gI().sendMoney(player);
                Service.gI().showYourNumber(player, strNumber((int) player.id), null, null, 0);
            }
        }
    }

    //    }
    public String strNumber(int id) {
        String number = "";
        List<GameData> pl = players.stream().filter(d -> d.id == id).toList();
        for (int i = 0; i < pl.size(); i++) {
            GameData d = pl.get(i);
            if (d.id == id) {
                number += d.point + (i >= pl.size() - 1 ? "" : ",");
            }
        }
        return number;
    }

    public String strFinish(int id) {
        String finish = "";
        short[] caitrang = {1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324};
        short[] vpdl = {1502, 1503, 1524, 1620, 1621, 1622, 1624, 1629, 1630, 1631, 1633, 1634, 1635, 1651, 1652, 1653, 1654, 1655, 1920};
        short[] pet = {1430, 1435, 1436, 1460, 1480, 1481, 1500, 1623, 1627, 1628, 1636, 1645, 1646, 1648, 1649, 1650};
        short[] vatpham = {457, 2229, 2228, 2230};

//        if (!giftGiven) {
        for (GameData g : players) {
            if (!giftGiven && id == g.id) {
                Player player = Client.gI().getPlayerByID(g.id);
                Random random = new Random();
                if (g.point != result) {
                    Item item = ItemService.gI().createNewItem(vatpham[random.nextInt(vatpham.length)]);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = item.template.id == vatpham[0] ? Util.nextInt(2, 5) : 3;
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().sendItemBags(player);
                    finish = "Tuy " + (player == null ? "NULL" : player.name) + " không trúng số nào nhưng\n admin vẫn tặng quà cho " + (player == null ? "NULL" : player.name);
                    return finish;
                }
                if (Util.isTrue(10, 100)) {
                    // Tặng caitrang
                    Item item = ItemService.gI().createNewItem(caitrang[random.nextInt(caitrang.length)]);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 48)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 48)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 48)));
                    item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 25)));
                    item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(8, 12)));
                    if (Util.isTrue(30, 100)) {
                        item.itemOptions.add(new Item.ItemOption(96, Util.nextInt(10, 15)));
                        if (Util.isTrue(20, 100)) {
                            item.itemOptions.add(new Item.ItemOption(95, Util.nextInt(10, 15)));
                        }
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().sendItemBags(player);
                    finish = "Chúc mừng " + (player == null ? "NULL" : player.name) + " đã thắng " + item.template.name + " với con số may mắn " + result;
                } else if (Util.isTrue(30, 100)) {
                    // Tặng vpdl
                    Item item = ItemService.gI().createNewItem(vpdl[random.nextInt(vpdl.length)]);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(12, 22)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(12, 22)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(12, 22)));
                    item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(5, 18)));
                    item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 8)));
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().sendItemBags(player);
                    finish = "Chúc mừng " + (player == null ? "NULL" : player.name) + " đã thắng " + item.template.name + " với con số may mắn " + result;
                } else {
                    // Tặng pet
                    Item item = ItemService.gI().createNewItem(pet[random.nextInt(pet.length)]);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(14, 22)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(14, 22)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(14, 22)));
                    item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(5, 12)));
                    item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 8)));
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().sendItemBags(player);
                    finish = "Chúc mừng " + (player == null ? "NULL" : player.name) + " đã thắng " + item.template.name + " với con số may mắn " + result;
                }
                
                break;
            }
        }
//        }
        return finish;

    }

    public void ResetGame(int result, int second) {
        this.result = result;
        this.second = second;
        this.result_name = "";
        players.stream().filter(g -> g.point == result).forEach((g) -> {
            Player player = Client.gI().getPlayerByID(g.id);
            result_name += (player != null ? player.name : "") + ",";
        });
        if (!result_name.isEmpty()) {
            result_name = result_name.substring(0, result_name.length() - 1);
        }
        players.forEach((g) -> {
            Player player = Client.gI().getPlayerByID(g.id);
            if (player != null) {
                Service.gI().showYourNumber(player, "", result + "", strFinish(g.id), 1);
            }
        });
        giftGiven = false;
        money = 0;
        players.clear();
    }

}
