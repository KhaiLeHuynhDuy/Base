//package nro.consts;
//
////import nro.database.GirlkunDB;
//import com.girlkun.database.GirlkunDB;
//import com.girlkun.result.GirlkunResultSet;
//import nro.jdbc.daos.GodGK;
//import nro.models.item.Item;
//import nro.models.player.Player;
////import nro.result.GirlkunResultSet;
//import nro.services.ItemService;
//import nro.services.Service;
//import nro.utils.Logger;
//import nro.utils.Util;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import java.util.Arrays;
//import java.util.List;
//
//// su kien 1/6
//public class SK20_10 {  
//
//    public static final int id1 = 1606;
//    public static final int id2 = 1607;
//    public static final int id3 = 1608;
//    public static final int id4 = 1609;
//    public static final int id5 = 1610;
//    public static final int iddoi1 = 1911;
//    public static final int iddoi2 = 1912;
//    public static final int id_Item_doi_diem = 457;//thỏi vàng
//
//    public static final int diem_thoi_1 = 10;
//    public static final int diem_thoi_2 = 20;
//    public static final int diem_thoi_3 = 30;
//
//    public static final int diem_ruong_1 = 100;
//    public static final int diem_ruong_2 = 100;
//    public static final int diem_ruong_3 = 100;
//    public static final int slVoOcSK = 1;
//
//    public static final int slSaoBienSK = 1;
//
//    public static List<Integer> listVPSK = Arrays.asList(id1, id2, id3, id4, id5);
//    public static int daquy1 = 2162;
//    public static int daquy2 = 2163;
//    public static int daquy3 = 2164;
//    public static int daquy4 = 2165;
//    public static int daquy5 = 2166;
//
//    public static int hopKHHD = 2114;
//    public static int hopKHTL = 2114;
//
//    public static int nr1s = 14;
//    public static int nr2s = 15;
//    public static int nr3s = 16;
//    public static int nr4s = 17;
//    public static int nr5s = 18;
//    public static int nr6s = 19;
//    public static int nr7s = 20;
//
////_______________________________________________________________________________________________
////    TOP 1
//    public static int top1_1 = 1136;
//    public static short sltop1_1 = 1;
////    public static int cs_top1_1_1 = 30;
////    public static int cs_top1_1_2 = 15;
////    public static int cs_top1_1_3 = 15;
//
//    public static int sl_daquy_top1 = 99;
//
//    public static int sl_hopKH_top1 = 5;
//
//    public static int sl_nr_top1 = 20;
//
//    //    TOP 2
//    public static int top2_1 = 1110;
//    public static short sltop2_1 = 1;
////    public static int cs_top2_1_1 = 22;
//
//    public static int sl_daquy_top2 = 50;
//
//    public static int sl_hopKH_top2 = 3;
//
//    public static int sl_nr_top2 = 10;
//
//    //    TOP 3
//    public static int top3_1 = 1111;
//    public static short sltop3_1 = 1;
////    public static int cs_top3_1_1 = 21;
//
//    public static int sl_daquy_top3 = 20;
//
//    public static int sl_hopKH_top3 = 2;
//
//    public static int sl_nr_top3 = 5;
//
//    //TOP 4
//    public static int sl_nr_top4 = 3;
//
//    public static int getRandomFromList() {  
//        int tile_id1 = 10;
//        int tile_id2 = 20;
//        int tile_id3 = 30;
//        int tile_id4 = 40;
//        int tile_id5 = 50;
//        int tile = Util.nextInt(0, 100);
//        if (tile < tile_id1) {  
//            return listVPSK.get(0);
//        } else if (tile < tile_id2) {  
//            return listVPSK.get(1);
//        } else if (tile < tile_id3) {  
//            return listVPSK.get(2);
//        } else if (tile < tile_id4) {  
//            return listVPSK.get(3);
//        } else if (tile < tile_id5) {  
//            return listVPSK.get(4);
//        } else {  
//            return listVPSK.get(Util.nextInt(listVPSK.size()));
//        }
//    }
//
//    public static SK20_10 gI;
//
//    public static SK20_10 gI() {  
//        if (gI == null) {  
//            gI = new SK20_10();
//        }
//        return gI;
//    }
//    public static final String[] Textskquylao = new String[]{  
//        "Sự kiện diễn ra 29/12 - 00h00 ngày 3/1/2024: \n"
//        + "____x2 tnsm trong thời gian diễn ra sự kiện______\n"
//        + "- Đua top sự kiện bằng cách nấu " + ItemService.gI().getTemplate(iddoi1).name + ", " + ItemService.gI().getTemplate(iddoi2).name + "\n"
//        + "|5| Nấu tại nồi nằm ở các làng\n"
//        + "\b|5|- Có thể đổi " + ItemService.gI().getTemplate(id_Item_doi_diem).name + " ra điểm sự kiện NGAY 20/11\n|4|"
//        + "_______Quà Top Sự Kiện________:\n|3| "
//        + "• Top 1:\n"
//        + " -> " + ItemService.gI().getTemplate(top1_1).name +  "x1"
//        //        + " -> " + ItemService.gI().getTemplate(top1_1).name + " full cs " + cs_top1_1_1 + "% + STCM " + cs_top1_1_2 + "% + đẹp " + cs_top1_1_3 + "%,\n "
//        + " -> Full bộ đá quý x" + sl_daquy_top1 + ", " + sl_hopKH_top1 + " " + ItemService.gI().getTemplate(hopKHHD).name + ", x" + sl_nr_top1 + " bộ 1 sao\n|2| "
//        + "• Top 2:\n"
//        + " -> " + ItemService.gI().getTemplate(top2_1).name + " x1"
//        + " -> Full bộ đá quý x" + sl_daquy_top2 + ", " + sl_hopKH_top2 + " " + ItemService.gI().getTemplate(hopKHHD).name + ", x" + sl_nr_top2 + " bộ 1 sao\n|4| "
//        + "• Top 3:\n"
//        + " -> " + ItemService.gI().getTemplate(top3_1).name + " x1"
//        + " -> Full bộ đá quý x" + sl_daquy_top3 + ", " + sl_hopKH_top3 + " " + ItemService.gI().getTemplate(hopKHTL).name + ", x" + sl_nr_top3 + " bộ 1 sao\n|5| "
//        + "• Top 4-10:\n "
//        + " -> x" + sl_nr_top3 + " bộ 1 sao"
//
//    };
//
//    public static boolean isEventActive() {  
//        return false;
//    }
//
//    public static boolean isTraoQua = true;
//
//    public static Calendar startEvent;
//
//    public static Calendar endEvents;
//
//    public static boolean initsukien = false;
//
//    public static boolean isActiveEvent() {
//        if (!initsukien) {
//            initsukien = true;
//            startEvent = Calendar.getInstance();
//
//            // Thiết lập ngày và giờ bắt đầu
//            startEvent.set(2024, Calendar.MARCH, 7, 00, 15);
//            System.out.println("Ngày bat dau su kien NGAY 20/11: " + startEvent.getTime());
//
//            endEvents = Calendar.getInstance();
//            // Thiết lập ngày và giờ ket thuc
//            endEvents.set(2024, Calendar.SEPTEMBER, 3, 00, 00);
//            System.out.println("Ngày ket thuc su kien up banh: " + endEvents.getTime());
//
//        }
//        Calendar currentTime = Calendar.getInstance();
//        if (System.currentTimeMillis() >= startEvent.getTimeInMillis() && System.currentTimeMillis() <= endEvents.getTimeInMillis()) {
////            System.out.println("Nam trong thoi gian su kien");
//            if (isTraoQua && System.currentTimeMillis() + 60000 >= endEvents.getTimeInMillis()) {
//
//                String sql = "SELECT player.id as plid , player.`name`, account.id as account, account.sk20_10 FROM account, player WHERE account.id = player.account_id  ORDER BY sk20_10 DESC LIMIT 10;";
//
//                List<String> NamePlayer = new ArrayList<>();
//                GirlkunResultSet rs = null;
//                try {
//                    rs = GirlkunDB.executeQuery(sql);
//                    while (rs.next()) {
//                        String name = rs.getString("name");
//                        NamePlayer.add(name);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                for (int i = 0; i < NamePlayer.size(); i++) {
//                    Player player = GodGK.loadPlayerByName(NamePlayer.get(i));
//                    TraoQuaSuKien(player, i);
//                    try {
//                        Thread.sleep(100);
//                  } catch (Exception e) {
//                e.printStackTrace();
//            }
//                }
//                isTraoQua = false;
//            }
//            return true;
//        } else {
//
//            return false;
//        }
//    }
//
//    public static boolean isRunningSK2010 = isActiveEvent();
//
//    public static void TraoQuaSuKien(Player player, int hang) {
//        switch (hang) {
//            case 0: // top 1
//
//                Item itop1_1 = ItemService.gI().createNewItem((short) top1_1);
////                itop1_1.itemOptions.add(new Item.ItemOption(50, cs_top1_1_1));
////                itop1_1.itemOptions.add(new Item.ItemOption(77, cs_top1_1_1));
////                itop1_1.itemOptions.add(new Item.ItemOption(103, cs_top1_1_1));
////                itop1_1.itemOptions.add(new Item.ItemOption(5, cs_top1_1_2));
////                itop1_1.itemOptions.add(new Item.ItemOption(117, cs_top1_1_3));
//                itop1_1.quantity = sltop1_1;
//                player.inventory.itemsMailBox.add(itop1_1);
//
//                short[] getallda = {2162, 2163, 2164, 2165};
//                for (short daquy : getallda) {
//                    Item itop1_2 = ItemService.gI().createNewItem((short) daquy);
//                    itop1_2.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop1_2.quantity = sl_daquy_top1;
//                    player.inventory.itemsMailBox.add(itop1_2);
//                }
//
//                short[] getallnr = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr) {
//                    Item itop1_3 = ItemService.gI().createNewItem((short) nr);
//                    itop1_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop1_3.quantity = sl_nr_top1;
//                    player.inventory.itemsMailBox.add(itop1_3);
//                }
//
//                Item itop1_4 = ItemService.gI().createNewItem((short) cn.hopKHHD);
//                itop1_4.itemOptions.add(new Item.ItemOption(30, 1));
//                itop1_4.itemOptions.add(new Item.ItemOption(93, 15));
//                itop1_4.quantity = sl_hopKH_top1;
//                player.inventory.itemsMailBox.add(itop1_4);
//
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 1 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 1 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//            case 1: // top 2
//
//                Item itop2_1 = ItemService.gI().createNewItem((short) top2_1);
////                itop2_1.itemOptions.add(new Item.ItemOption(50, cs_top2_1_1));
////                itop2_1.itemOptions.add(new Item.ItemOption(77, cs_top2_1_1));
////                itop2_1.itemOptions.add(new Item.ItemOption(103, cs_top2_1_1));
//                itop2_1.quantity = sltop2_1;
//                player.inventory.itemsMailBox.add(itop2_1);
//
//                short[] getallda2 = {2162, 2163, 2164, 2165};
//                for (short daquy : getallda2) {
//                    Item itop2_2 = ItemService.gI().createNewItem((short) daquy);
//                    itop2_2.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop2_2.quantity = sl_daquy_top2;
//                    player.inventory.itemsMailBox.add(itop2_2);
//                }
//
//                short[] getallnr2 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr2) {
//                    Item itop2_3 = ItemService.gI().createNewItem((short) nr);
//                    itop2_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop2_3.quantity = sl_nr_top2;
//                    player.inventory.itemsMailBox.add(itop2_3);
//                }
//
//                Item itop2_4 = ItemService.gI().createNewItem((short) cn.hopKHHD);
//                itop2_4.itemOptions.add(new Item.ItemOption(30, 1));
//                itop2_4.itemOptions.add(new Item.ItemOption(93, 15));
//                itop2_4.quantity = sl_hopKH_top2;
//                player.inventory.itemsMailBox.add(itop2_4);
//
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 2 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 2 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//
//            case 2: // top 3
//
//                Item itop3_1 = ItemService.gI().createNewItem((short) top3_1);
////                itop3_1.itemOptions.add(new Item.ItemOption(50, cs_top3_1_1));
////                itop3_1.itemOptions.add(new Item.ItemOption(77, cs_top3_1_1));
////                itop3_1.itemOptions.add(new Item.ItemOption(103, cs_top3_1_1));
//                itop3_1.quantity = sltop3_1;
//                player.inventory.itemsMailBox.add(itop3_1);
//
//                short[] getallda3 = {2162, 2163, 2164, 2165};
//                for (short daquy : getallda3) {
//                    Item itop3_2 = ItemService.gI().createNewItem((short) daquy);
//                    itop3_2.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop3_2.quantity = sl_daquy_top3;
//                    player.inventory.itemsMailBox.add(itop3_2);
//                }
//
//                short[] getallnr3 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr3) {
//                    Item itop3_3 = ItemService.gI().createNewItem((short) nr);
//                    itop3_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop3_3.quantity = sl_nr_top3;
//                    player.inventory.itemsMailBox.add(itop3_3);
//                }
//
//                Item itop3_4 = ItemService.gI().createNewItem((short) cn.hopKHTL);
//                itop3_4.itemOptions.add(new Item.ItemOption(30, 1));
//                itop3_4.itemOptions.add(new Item.ItemOption(93, 15));
//                itop3_4.quantity = sl_hopKH_top3;
//                player.inventory.itemsMailBox.add(itop3_4);
//
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 3 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 3 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//
//            case 3: // TOP 4
//                short[] getallnr4 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr4) {
//                    Item itop4_3 = ItemService.gI().createNewItem((short) nr);
//                    itop4_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop4_3.quantity = sl_nr_top4;
//                    player.inventory.itemsMailBox.add(itop4_3);
//                }
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 4 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 4 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//            case 4: // TOP 5
//                short[] getallnr5 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr5) {
//                    Item itop5_3 = ItemService.gI().createNewItem((short) nr);
//                    itop5_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop5_3.quantity = sl_nr_top4;
//                    player.inventory.itemsMailBox.add(itop5_3);
//                }
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 5 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 5 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//            case 5: // TOP 6
//                short[] getallnr6 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr6) {
//                    Item itop6_3 = ItemService.gI().createNewItem((short) nr);
//                    itop6_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop6_3.quantity = sl_nr_top4;
//                    player.inventory.itemsMailBox.add(itop6_3);
//                }
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 6 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 6 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//            case 6: // TOP 7
//                short[] getallnr7 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr7) {
//                    Item itop7_3 = ItemService.gI().createNewItem((short) nr);
//                    itop7_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop7_3.quantity = sl_nr_top4;
//                    player.inventory.itemsMailBox.add(itop7_3);
//                }
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 7 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 7 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//            case 7: // TOP 8
//                short[] getallnr8 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr8) {
//                    Item itop8_3 = ItemService.gI().createNewItem((short) nr);
//                    itop8_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop8_3.quantity = sl_nr_top4;
//                    player.inventory.itemsMailBox.add(itop8_3);
//                }
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 8 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 8 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//            case 8: // TOP 9
//                short[] getallnr9 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr9) {
//                    Item itop9_3 = ItemService.gI().createNewItem((short) nr);
//                    itop9_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop9_3.quantity = sl_nr_top4;
//                    player.inventory.itemsMailBox.add(itop9_3);
//                }
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 9 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 9 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//            case 9: // TOP 10
//                short[] getallnr10 = {14, 15, 16, 17, 18, 19, 20};
//                for (short nr : getallnr10) {
//                    Item itop10_3 = ItemService.gI().createNewItem((short) nr);
//                    itop10_3.itemOptions.add(new Item.ItemOption(30, 1));
//                    itop10_3.quantity = sl_nr_top4;
//                    player.inventory.itemsMailBox.add(itop10_3);
//                }
//                if (GodGK.updateMailBox(player)) {
//                    Logger.error("Da trao qua top 10 su kien NGAY 20/11 cho  " + player.name);
//                    Service.gI().sendThongBao(player, "Bạn đã nhận được quà top 10 SK NGAY 20/11 về hòm thư!");
//                }
//                break;
//
//        }
//    }
//
//}
