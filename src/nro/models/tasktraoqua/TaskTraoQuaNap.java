package nro.models.tasktraoqua;

//import nro.database.GirlkunDB;
import nro.models.map.sieuhang.*;
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
import nro.server.Client;
import nro.server.io.MySession;
import nro.services.Service;

public class TaskTraoQuaNap extends TimerTask {

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
        try ( Connection con = GirlkunDB.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT player.id as plid, player.name as name, account.tongnap3 FROM account, player WHERE account.id = player.account_id AND account.tongnap3 >= 100000 ORDER BY account.tongnap3 DESC LIMIT 10;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int temp = rs.getInt("plid");
                arrayList.add(temp);

            }
            rs.close();
            ps.close();
            con.close();
            for (int i = 0; i < arrayList.size(); i++) {
                traoQuaTopI(i, arrayList.get(i));
                Logger.success("Dang trao qua top NAP " + i);
            }
        } catch (Exception e) {
        }
    }

    public static String[] Text = new String[]{
        "TOP NẠP (Nạp trên 100K mới được vô top):\n"
        + "|3|Top 1:"
        + "x" + 1 + " " + ItemService.gI().getTemplate(1600).name + "DAME + 130% , (HP - KI) + 100%, STCM + 30%, hút 15% , \n"
        + "x" + 3 + " " + ItemService.gI().getTemplate(1619).name + ", \n"
        + "x" + 15 + " Bộ Ngọc rồng,\n"
        + "x" + 5555 + " " + ItemService.gI().getTemplate(457).name + ", \n"
        + "|2|Top 2:"
        + "x" + 1 + " " + ItemService.gI().getTemplate(1600).name + "DAME + 110% , (HP - KI) + 80%, STCM + 20%, hút 12% , \n"
            + "x" + 2 + " " + ItemService.gI().getTemplate(1619).name + ", \n"
        + "x" + 12 + " Bộ Ngọc rồng,\n"
        + "x" + 4444 + " " + ItemService.gI().getTemplate(457).name + ", \n"
        + "|1|Top 3:"
        + "x" + 1 + " " + ItemService.gI().getTemplate(1600).name + "DAME + 90% , (HP - KI) + 70%, STCM + 10%, hút 9% , \n" 
            + "x" + 1 + " " + ItemService.gI().getTemplate(1619).name + ", \n"
        + "x" + 10 + " Bộ Ngọc rồng,\n"
        + "x" + 3333 + " " + ItemService.gI().getTemplate(457).name + ", \n"
        + "|4|Top 4: "
        + "x" + 5 + " Bộ Ngọc rồng,\n"
        + "x" + 1000 + " " + ItemService.gI().getTemplate(457).name + ", \n"
        + "|6|Top 5: "
        + "x 5 Bộ ngọc rồng 1 sao, \n"
        + "|5|Top 6: "
        + "x 5 Bộ ngọc rồng 1 sao, \n"
        + "|7|Top 7: "
        + "x 3 Bộ ngọc rồng 1 sao, \n"
        + "|7|Top 8: "
        + "x 3 Bộ ngọc rồng 1 sao, \n"
        + "\b|7|Top 9: "
        + "x 3 Bộ ngọc rồng 1 sao, \n"
        + "\b|7|Top 10: "
        + "x 3 Bộ ngọc rồng 1 sao, \n"
        + "Nhận quà vào ngày 13H15 - 8/8/2024"
    };

    private static void traoQuaTopI(int i, int s) {
        try {
            Player pl = GodGK.loadPlayerByID(s);
            switch (i) {
                case 0: {
                    {//
                        Item item = ItemService.gI().createNewItem((short) 1600);
                        item.quantity = 1;
                        item.itemOptions.add(new Item.ItemOption(50, 130));
                        item.itemOptions.add(new Item.ItemOption(77, 100));
                        item.itemOptions.add(new Item.ItemOption(103, 100));
                        item.itemOptions.add(new Item.ItemOption(5, 30));
                        item.itemOptions.add(new Item.ItemOption(95, 15));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    {//
                        Item item = ItemService.gI().createNewItem((short) 1619);
                        item.quantity = 5;
                        item.itemOptions.add(new Item.ItemOption(30, 1));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 15;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    {//
                        Item item = ItemService.gI().createNewItem((short) 457);
                        item.quantity = 5555;
                        item.itemOptions.add(new Item.ItemOption(93, 30));
                        item.itemOptions.add(new Item.ItemOption(30, 1));
                        pl.inventory.itemsMailBox.add(item);

                    }
                    if (GodGK.updateMailBox(pl)) {
                        Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);
                        Service.gI().sendThongBao(pl, "Bạn nhận được quà top 1");
                    }
                }
                break;

                case 1: {
                    {//
                        Item item = ItemService.gI().createNewItem((short) 1600);
                        item.quantity = 1;
                        item.itemOptions.add(new Item.ItemOption(50, 110));
                        item.itemOptions.add(new Item.ItemOption(77, 80));
                        item.itemOptions.add(new Item.ItemOption(103, 80));
                        item.itemOptions.add(new Item.ItemOption(5, 20));
                        item.itemOptions.add(new Item.ItemOption(95, 15));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    {//
                        Item item = ItemService.gI().createNewItem((short) 1619);
                        item.quantity = 4;
                        item.itemOptions.add(new Item.ItemOption(30, 1));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 12;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    {//
                        Item item = ItemService.gI().createNewItem((short) 457);
                        item.quantity = 4444;
                        item.itemOptions.add(new Item.ItemOption(93, 30));
                        item.itemOptions.add(new Item.ItemOption(30, 1));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);
                }
                break;
                case 2: {
                     {//
                        Item item = ItemService.gI().createNewItem((short) 1600);
                        item.quantity = 1;
                        item.itemOptions.add(new Item.ItemOption(50, 90));
                        item.itemOptions.add(new Item.ItemOption(77, 70));
                        item.itemOptions.add(new Item.ItemOption(103, 70));
                        item.itemOptions.add(new Item.ItemOption(5, 10));
                        item.itemOptions.add(new Item.ItemOption(95, 15));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    {//
                        Item item = ItemService.gI().createNewItem((short) 1619);
                        item.quantity = 3;
                        item.itemOptions.add(new Item.ItemOption(50, 8));
                        item.itemOptions.add(new Item.ItemOption(30, 1));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 10;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    {//
                        Item item = ItemService.gI().createNewItem((short) 457);
                        item.quantity = 3333;
                        item.itemOptions.add(new Item.ItemOption(93, 30));
                        item.itemOptions.add(new Item.ItemOption(30, 1));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);
                }
                break;
                case 3: {
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 5;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    {//
                        Item item = ItemService.gI().createNewItem((short) 457);
                        item.quantity = 1000;
                        item.itemOptions.add(new Item.ItemOption(93, 30));
                        item.itemOptions.add(new Item.ItemOption(30, 1));
                        pl.inventory.itemsMailBox.add(item);
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);
                }
                break;
                case 4: {
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 5;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);
                }
                break;
                case 5: {
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 5;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);

                }
                break;
                case 6: {
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 3;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);

                }
                break;
                case 7: {
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 3;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);

                }
                break;
                case 8: {
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 3;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);

                }
                break;
                case 9: {
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 3;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);

                }
                break;
                case 10: {
                    {//
                        short[] item = {14, 15, 16, 17, 18, 19, 20};
                        for (short nr : item) {
                            Item ngocrong = ItemService.gI().createNewItem((short) nr);
                            ngocrong.itemOptions.add(new Item.ItemOption(93, 10));
                            ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
                            ngocrong.quantity = 3;
                            pl.inventory.itemsMailBox.add(ngocrong);
                        }
                    }
                    GodGK.updateMailBox(pl);
                    Logger.error("Đã trao quà top " + i + 1 + " NẠP cho: " + pl.name);

                }
                break;
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
        if (isDaTraoQuaTop && hour == 13 && minute == 15) {

            traoQuaTopSieuHang();
            isDaTraoQuaTop = false;
        }

        // Đặt lại cờ vào lúc 00:01 để chuẩn bị cho lần trao quà tiếp theo
        if (!isDaTraoQuaTop && hour == 0 && minute == 1) {
            isDaTraoQuaTop = true;
        }
    }

}
