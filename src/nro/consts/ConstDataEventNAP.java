package nro.consts;

//import nro.database.GirlkunDB;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import nro.jdbc.daos.GodGK;
import nro.models.item.Item;
import nro.models.player.Player;
//import nro.result.GirlkunResultSet;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.Util;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Arrays;
import java.util.List;

// su kien 1/6
public class ConstDataEventNAP {  

    public static ConstDataEventNAP gI;

    public static ConstDataEventNAP gI() {  
        if (gI == null) {  
            gI = new ConstDataEventNAP();
        }
        return gI;
    }

    public static boolean isEventActive() {  
        return false;
    }

    public static boolean isTraoQua = true;

    public static Calendar startEvent;

    public static Calendar endEvents;

    public static boolean initsukien = false;

    public static final byte MONTH_OPEN = 9;
    public static final byte DATE_OPEN = 26;
    public static final byte HOUR_OPEN = 12;
    public static final byte MIN_OPEN = 00;

    public static final byte MONTH_END = 10;
    public static final byte DATE_END = 6;
    public static final byte HOUR_END = 13;
    public static final byte MIN_END = 15;

    public static boolean isActiveEvent() {
        if (!initsukien) {
            initsukien = true;
            startEvent = Calendar.getInstance();

            // Thiết lập ngày và giờ bắt đầu
            startEvent.set(2024, MONTH_OPEN - 1, DATE_OPEN, HOUR_OPEN, MIN_OPEN);
            System.out.println("Ngày bắt đầu sự kiện đua top nạp: " + startEvent.getTime());

            endEvents = Calendar.getInstance();
            // Thiết lập ngày và giờ kết thúc
            endEvents.set(2024, MONTH_END - 1, DATE_END, HOUR_END, MIN_END);
            System.out.println("Ngày kết thúc sự kiện đua top nạp: " + endEvents.getTime());
        }

        Calendar currentTime = Calendar.getInstance();
        if (System.currentTimeMillis() >= startEvent.getTimeInMillis() && System.currentTimeMillis() <= endEvents.getTimeInMillis()) {
            if (isTraoQua && System.currentTimeMillis() + 60000 >= endEvents.getTimeInMillis()) {
                String sql = "SELECT player.id as plid, player.name as name, account.tongnap3 FROM account, player WHERE account.id = player.account_id AND account.tongnap3 >= 100000 ORDER BY account.tongnap3 DESC LIMIT 10;";
                List<Integer> AccIdPlayer = new ArrayList<>();
                GirlkunResultSet rs = null;
                try {
                    rs = GirlkunDB.executeQuery(sql);
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        AccIdPlayer.add(id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < AccIdPlayer.size(); i++) {
                    Player player = GodGK.loadPlayerByID(AccIdPlayer.get(i));
                    TraoQuaSuKien(player, i);

                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isTraoQua = false;
            }
            return true;
        } else {

            return false;
        }
    }

    public static boolean isRunningSK = isActiveEvent();

    public static String[] Text = new String[]{
        "TOP NẠP:\n"
        + "Nhận quà vào ngày " + HOUR_END + "H" + MIN_END + " ngày " + DATE_END + "/" + MONTH_END + "/2024\n"
        + "|3|Top 1:"
        + "x" + 1 + " " + ItemService.gI().getTemplate(657).name + " Kích hoạt [+7] , tấn công gốc găng 8k , \n"
        + "x" + 5 + " " + ItemService.gI().getTemplate(1618).name + ", \n"
        + "x" + 15 + " Bộ Ngọc rồng,\n"
        + "x" + 2000 + " " + ItemService.gI().getTemplate(457).name + ", \n"
        + "|2|Top 2:"
        + "x" + 1 + " " + ItemService.gI().getTemplate(657).name + " Kích hoạt [+6] , tấn công gốc găng 7k9 , \n"
        + "x" + 3 + " " + ItemService.gI().getTemplate(1618).name + ", \n"
        + "x" + 12 + " Bộ Ngọc rồng,\n"
        + "x" + 1000 + " " + ItemService.gI().getTemplate(457).name + ", \n"
        + "|1|Top 3:"
        + "x" + 1 + " " + ItemService.gI().getTemplate(657).name + " Kích hoạt [+5] , tấn công gốc găng 7k8 , \n"
        + "x" + 2 + " " + ItemService.gI().getTemplate(1618).name + ", \n"
        + "x" + 10 + " Bộ Ngọc rồng,\n"
        + "x" + 500 + " " + ItemService.gI().getTemplate(457).name + ", \n"
        + "|4|Top 4: "
        + "x" + 5 + " Bộ Ngọc rồng,\n"
        + "x" + 200 + " " + ItemService.gI().getTemplate(457).name + ", \n"
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

    };

    public static void TraoQuaSuKien(Player pl, int i) {
        int[][] optionSKH = {{127, 128, 129}, {130, 132, 131}, {134, 135, 133}};
        int[][] option2SKH = {{139, 140, 141}, {142, 144, 143}, {137, 138, 136}};
        int option = 0;
        int option2 = 0;
        int select = Util.nextInt(0, 1);
        if (Util.isTrue(30, 100)) {
            option = optionSKH[pl.gender][2];
            option2 = option2SKH[pl.gender][2];
        } else {
            option = optionSKH[pl.gender][select];
            option2 = option2SKH[pl.gender][select];
        }
        switch (i) {
            case 0: {
                {//
                    Item item = ItemService.gI().createNewItem((short) (657 + (pl.gender * 2)));
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(72, 7));
                    item.itemOptions.add(new Item.ItemOption(0, 8000));
                    item.itemOptions.add(new Item.ItemOption(option, 1));
                    item.itemOptions.add(new Item.ItemOption(option2, 1));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {//
                    Item item = ItemService.gI().createNewItem((short) 1619);
                    item.quantity = 3;
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
                    item.quantity = 2000;
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
                    Item item = ItemService.gI().createNewItem((short) 1623);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(50, 18));
                    item.itemOptions.add(new Item.ItemOption(77, 18));
                    item.itemOptions.add(new Item.ItemOption(103, 18));
                    item.itemOptions.add(new Item.ItemOption(5, 12));
                    item.itemOptions.add(new Item.ItemOption(14, 8));
                    pl.inventory.itemsMailBox.add(item);
                }
                {//
                    Item item = ItemService.gI().createNewItem((short) 1619);
                    item.quantity = 2;
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
                    item.quantity = 1000;
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
                    Item item = ItemService.gI().createNewItem((short) 1623);
                    item.quantity = 1;
                    item.itemOptions.add(new Item.ItemOption(50, 17));
                    item.itemOptions.add(new Item.ItemOption(77, 15));
                    item.itemOptions.add(new Item.ItemOption(103, 15));
                    item.itemOptions.add(new Item.ItemOption(5, 10));
                    item.itemOptions.add(new Item.ItemOption(14, 5));
                    pl.inventory.itemsMailBox.add(item);
                }
                {//
                    Item item = ItemService.gI().createNewItem((short) 1619);
                    item.quantity = 1;
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
                    item.quantity = 555;
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
                    item.quantity = 200;
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

        }
    }

}
