package nro.models.map.sieuhang;

//import nro.database.GirlkunDB;
import com.girlkun.database.GirlkunDB;
import nro.jdbc.daos.GodGK;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.services.ItemService;
import nro.utils.Logger;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class TaskTraoQua extends TimerTask {

    public static boolean isDaTraoQuaTop = true;

    public static void scheduleDailyTask() {
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        Logger.success("Da len lich trao qua top sieu hang");
//        // Lên lịch công việc chạy vào 12 giờ đêm và lặp lại mỗi 24 giờ
//        scheduler.scheduleAtFixedRate(() -> {
//            Logger.error("Da toi gio trao top sieu hang");
//            
//        }, getDelayUntilMidnight(), 24, TimeUnit.HOURS);
    }

    private static void traoQuaTopSieuHang() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        try (Connection con = GirlkunDB.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT id,name, rank_sieu_hang AS rank FROM player ORDER BY rank_sieu_hang ASC LIMIT 10");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int temp = rs.getInt("id");
                arrayList.add(temp);
            }
            rs.close();
            ps.close();
            con.close();
            for (int i = 0; i < arrayList.size(); i++) {
                traoQuaTopI(i, arrayList.get(i));
                Logger.success("Dang trao qua top " + i);
            }
        } catch (Exception e) {
                    e.printStackTrace();
        }
    }
    public static String[] Text = new String[]{
        "|3|Top 1:"
        + "x" + 5 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "|2|Top 2:"
        + "x" + 3 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "|1|Top 3:"
        + "x" + 2 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "|4|Top 4: "
        + "x" + 2 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "|6|Top 5: "
        + "x" + 1 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "|5|Top 6: "
        + "x" + 1 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "|7|Top 7: "
        + "x" + 1 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "|7|Top 8: "
        + "x" + 1 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "\b|7|Top 9: "
        + "x" + 1 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "\b|7|Top 10: "
        + "x" + 1 + " " + ItemService.gI().getTemplate(1467).name + ", \n"
        + "Nhận quà sau mỗi 00h15 hàng ngày!"

    };

    private static void traoQuaTopI(int i, int s) {
        try {

//            String name = s.trim();
//
//            Player pl = null;
//            if (pl == null) {
//                pl = GodGK.loadPlayerByName(name.trim());
//            }
            Player pl = null;
            if (pl == null) {
                pl = GodGK.loadPlayerByID(s);
            }

            switch (i) {
                case 0: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 5;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 1: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 3;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 2: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 2;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                   Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 3: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 2;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 4: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 2;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                   Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 5: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 2;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                   Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 6: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 7: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 8: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                   Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 9: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                case 10: {
                    Item item = ItemService.gI().createNewItem((short) 1467);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(93, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + (i + 1) + " siêu hạng cho: " + pl.name);
                    break;
                }
                default:

                    break;

            }
//            Logger.success("Da trao qua " + i);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.success("Loi trao top " + i + " " + s);
        }
    }

    private static long getDelayUntilMidnight() {
        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();

        // Tính thời gian còn lại đến 12 giờ đêm
        long midnightTime = (currentTime / 86400000 + 1) * 86400000; // Làm tròn lên đến ngày tiếp theo

        return midnightTime - currentTime;
    }
    private final static long ONCE_PER_DAY = 1000 * 60 * 60 * 24;

    //private final static int ONE_DAY = 1;
    private final static int TWO_AM = 0;
    private final static int ZERO_MINUTES = 0;

    @Override
    public void run() {

        // Do your Job Here
        System.out.println("Start Job");
        traoQuaTopSieuHang();
        System.out.println("End Job" + System.currentTimeMillis());
    }

    private static ZonedDateTime getTomorrowMorning0AM() {
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();
        // Lấy thời điểm 0 giờ 0 phút ngày hôm sau
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate tomorrow = today.plusDays(1);
        // Kết hợp ngày và giờ để có được ZonedDateTime
        return ZonedDateTime.of(tomorrow, midnight, ZoneId.systemDefault());
    }

    public static void startTask() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        int hour = now.getHour();
        int minute = now.getMinute();

        // Đặt thời gian trao quà là 00:15
        if (isDaTraoQuaTop && hour == 0 && minute == 15) {
            traoQuaTopSieuHang();
            isDaTraoQuaTop = false;
        }

        // Đặt lại cờ vào lúc 00:01 để chuẩn bị cho lần trao quà tiếp theo
        if (!isDaTraoQuaTop && hour == 0 && minute == 1) {
            isDaTraoQuaTop = true;
        }
    }

}
