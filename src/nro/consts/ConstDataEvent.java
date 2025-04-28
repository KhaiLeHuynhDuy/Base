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
//public class ConstDataEvent {  
//
//    public static final int ID_ITEM_1 = 1002;
//    public static final int ID_ITEM_2 = 1003;
//    public static final int ID_ITEM_3 = 1004;
//
//    public static final int ID_ITEM_4 = 1008;
//    public static final int ID_ITEM_5 = 1610;
//
//    public static final int ID_ITEM_6 = 1512;
//
//    public static final int ID_ITEM_7 = 1512;
//    public static final int MIN_POINT = 5000;
//    public static int ID_ITEM_4Sk = 1194;
//    public static int ID_ITEM_5Sk = 1195;
//
//    public static int idItemDoiDiem = 457;//thỏi vàng
//
//    public static final int ID_ITEM_1_SK = 1099;
//    public static final int ID_ITEM_2_SK = 1100;
//    public static final int ID_ITEM_3_SK = 1101;
//
//    public static int mayDoSuKien = 2189;
//    public static final int ID_RUONG_1 = 2190;
//
//    public static final int ID_RUONG_2 = 2191;
//
//    public static int diemThoi1 = 2;
//    public static int diemThoi2 = 10;
//
//    public static int diemRuong1 = 10;
//    public static int diemRuong2 = 5;
//
//    public static int slItem1_SK = 99;
//
//    public static int slItem2_SK = 99;
//
//    public static int slItem3_SK = 99;
//
//    public static List<Integer> listVPSK = Arrays.asList(ID_ITEM_3, ID_ITEM_1, ID_ITEM_2, ID_ITEM_4, ID_ITEM_5);
//
//    public static int getRandomFromList() {  
//        int tile_saoBien = 30;
//        int tile_ConCua = 50;
//        int tile_VoOc = 40;
//        int tile = Util.nextInt(0, 100);
//        if (tile < tile_ConCua) {  
//            return listVPSK.get(0);
//        } else if (tile < tile_VoOc) {  
//            return listVPSK.get(1);
//        } else if (tile < tile_saoBien) {  
//            return listVPSK.get(2);
//        } else {  
//            return listVPSK.get(Util.nextInt(listVPSK.size()));
//        }
//    }
//
//    public static ConstDataEvent gI;
//
//    public static ConstDataEvent gI() {  
//        if (gI == null) {  
//            gI = new ConstDataEvent();
//        }
//        return gI;
//    }
//    public static final String[] Huongdannaubanh = new String[]{  
//        "|3|Sử dụng đuôi khỉ để up quái rơi ra củi lửa và nước nấu hoặc up mộc nhân"
//        + "\n |6|Hành tinh trái đất sử dụng tuyệt kĩ Quả cầu kinh khí hành tinh Xayda để cướp đuôi khỉ"
//        + "\nMỗi lần đun củi sẽ giảm 3 giây nấu nguyên liệu"
//        + "\nTăng lượng nước trong nồi bằng cách chêm thêm nước"
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
//   public static final byte MONTH_OPEN = 9;
//    public static final byte DATE_OPEN = 29;
//    public static final byte HOUR_OPEN = 12;
//    public static final byte MIN_OPEN = 00;
//
//    public static final byte MONTH_END = 10;
//    public static final byte DATE_END = 8;
//    public static final byte HOUR_END = 13;
//    public static final byte MIN_END = 15;
//
//    public static boolean isActiveEvent() {
//        if (!initsukien) {
//            initsukien = true;
//            startEvent = Calendar.getInstance();
//
//            // Thiết lập ngày và giờ bắt đầu
//            startEvent.set(2024, MONTH_OPEN - 1, DATE_OPEN, HOUR_OPEN, MIN_OPEN);
//            System.out.println("Ngày bắt đầu sự kiện đua top quy lão: " + startEvent.getTime());
//
//            endEvents = Calendar.getInstance();
//            // Thiết lập ngày và giờ kết thúc
//            endEvents.set(2024, MONTH_END - 1, DATE_END, HOUR_END, MIN_END);
//            System.out.println("Ngày kết thúc sự kiện đua top quy lão: " + endEvents.getTime());
//        }
//
//        Calendar currentTime = Calendar.getInstance();
//        if (System.currentTimeMillis() >= startEvent.getTimeInMillis() && System.currentTimeMillis() <= endEvents.getTimeInMillis()) {
//            if (isTraoQua && System.currentTimeMillis() + 60000 >= endEvents.getTimeInMillis()) {
////                String sql = "SELECT player.id as plid, player.`name`, account.id as account, account.topsk16\n"
////                        + "FROM account, player \n"
////                        + "WHERE account.id = player.account_id AND account.topsk16 >= 20000\n"
////                        + "ORDER BY account.topsk16 DESC \n"
////                        + "LIMIT 10;";
////                String sql = "SELECT account_id, name, diem_quy_lao FROM player WHERE diem_quy_lao >= 20000 ORDER BY diem_quy_lao DESC LIMIT 10;";
//                String sql = "select \n"
//                        + "  account.id as accountId, \n"
//                        + "  player.name, \n"
//                        + "	CAST(\n"
//                        + "    REPLACE(\n"
//                        + "      SUBSTRING_INDEX(\n"
//                        + "        SUBSTRING_INDEX(\n"
//                        + "          CONCAT(\n"
//                        + "            '[" + ID_ITEM_6 + ",', \n"
//                        + "            SUBSTRING_INDEX(\n"
//                        + "              SUBSTRING_INDEX(player.items_bag, '[" + ID_ITEM_6 + ",', -1), \n"
//                        + "              ']', \n"
//                        + "              1\n"
//                        + "            )\n"
//                        + "          ), \n"
//                        + "          ',', \n"
//                        + "          2\n"
//                        + "        ), \n"
//                        + "        ']', \n"
//                        + "        1\n"
//                        + "      ), \n"
//                        + "      '[" + ID_ITEM_6 + ",', \n"
//                        + "      ''\n"
//                        + "    ) as unsigned\n"
//                        + "  ) as tv_hanhtrang, \n"
//                        + "	CAST(\n"
//                        + "    REPLACE(\n"
//                        + "      SUBSTRING_INDEX(\n"
//                        + "        SUBSTRING_INDEX(\n"
//                        + "          CONCAT(\n"
//                        + "            '[" + ID_ITEM_6 + ",', \n"
//                        + "            SUBSTRING_INDEX(\n"
//                        + "              SUBSTRING_INDEX(player.items_box, '[" + ID_ITEM_6 + ",', -1), \n"
//                        + "              ']', \n"
//                        + "              1\n"
//                        + "            )\n"
//                        + "          ), \n"
//                        + "          ',', \n"
//                        + "          2\n"
//                        + "        ), \n"
//                        + "        ']', \n"
//                        + "        1\n"
//                        + "      ), \n"
//                        + "      '[" + ID_ITEM_6 + ",', \n"
//                        + "      ''\n"
//                        + "    ) as unsigned\n"
//                        + "  ) as tv_ruong,\n"
//                        + "  CAST(\n"
//                        + "    REPLACE(\n"
//                        + "      SUBSTRING_INDEX(\n"
//                        + "        SUBSTRING_INDEX(\n"
//                        + "          CONCAT(\n"
//                        + "            '[" + ID_ITEM_6 + ",', \n"
//                        + "            SUBSTRING_INDEX(\n"
//                        + "              SUBSTRING_INDEX(player.items_bag, '[" + ID_ITEM_6 + ",', -1), \n"
//                        + "              ']', \n"
//                        + "              1\n"
//                        + "            )\n"
//                        + "          ), \n"
//                        + "          ',', \n"
//                        + "          2\n"
//                        + "        ), \n"
//                        + "        ']', \n"
//                        + "        1\n"
//                        + "      ), \n"
//                        + "      '[" + ID_ITEM_6 + ",', \n"
//                        + "      ''\n"
//                        + "    ) as unsigned\n"
//                        + "  ) + CAST(\n"
//                        + "    REPLACE(\n"
//                        + "      SUBSTRING_INDEX(\n"
//                        + "        SUBSTRING_INDEX(\n"
//                        + "          CONCAT(\n"
//                        + "            '[" + ID_ITEM_6 + ",', \n"
//                        + "            SUBSTRING_INDEX(\n"
//                        + "              SUBSTRING_INDEX(player.items_box, '[" + ID_ITEM_6 + ",', -1), \n"
//                        + "              ']', \n"
//                        + "              1\n"
//                        + "            )\n"
//                        + "          ), \n"
//                        + "          ',', \n"
//                        + "          2\n"
//                        + "        ), \n"
//                        + "        ']', \n"
//                        + "        1\n"
//                        + "      ), \n"
//                        + "      '[" + ID_ITEM_6 + ",', \n"
//                        + "      ''\n"
//                        + "    ) as unsigned\n"
//                        + "  ) AS thoi_vang \n"
//                        + "from \n"
//                        + "  player \n"
//                        + "  inner join account on account.id = player.account_id \n"
//                        + "where \n"
//                        + "  (\n"
//                        + "    player.items_box like '%\"[" + ID_ITEM_6 + ",%' \n"
//                        + "    or player.items_bag like '%\"[" + ID_ITEM_6 + ",%'\n"
//                        + " )\n"
//                        + "having thoi_vang >= " + MIN_POINT + " \n"
//                        + "order by \n"
//                        + "  thoi_vang DESC \n"
//                        + "limit \n"
//                        + "  10;";
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
//
//                    TraoQuaSuKien(player, i);
//
//                    try {
//                        Thread.sleep(100);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
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
//    public static boolean isRunningSK16 = isActiveEvent();
//
//    public static final String[] Textskquylao = new String[]{  
//        "- Đua top Điểm bằng cách làm nhiệm vụ lấy " + ItemService.gI().getTemplate(ID_ITEM_6).name + "\n"
//        + "_______Quà Top Sự Kiện________:\n|3| "
//        + "Nhận quà vào ngày " + HOUR_END + "H" + MIN_END + " ngày " + DATE_END + "/" + MONTH_END + "/2024\n"
//        + "|3|Top 1:"
//        + "x" + 2 + " " + ItemService.gI().getTemplate(2115).name + ", \n"
//        + "x" + 3 + " " + ItemService.gI().getTemplate(1618).name + ", \n"
//        + "x" + 12 + " Bộ Ngọc rồng,\n"
//        + "x" + 3333 + " " + ItemService.gI().getTemplate(457).name + ", \n"
//        + "|2|Top 2:"
//        + "x" + 1 + " " + ItemService.gI().getTemplate(2115).name + ", \n"
//        + "x" + 2 + " " + ItemService.gI().getTemplate(1618).name + ", \n"
//        + "x" + 1 + " " + ItemService.gI().getTemplate(2114).name + ", \n"
//        + "x" + 10 + " Bộ Ngọc rồng,\n"
//        + "x" + 2222 + " " + ItemService.gI().getTemplate(457).name + ", \n"
//        + "|1|Top 3:"
//        + "x" + 2 + " " + ItemService.gI().getTemplate(2114).name + ", \n"
//        + "x" + 1 + " " + ItemService.gI().getTemplate(1618).name + ", \n"
//        + "x" + 8 + " Bộ Ngọc rồng,\n"
//        + "x" + 1111 + " " + ItemService.gI().getTemplate(457).name + ", \n"
//        + "|4|Top 4-10: "
//        + "x" + 5 + " Bộ Ngọc rồng,\n"
//        + "x" + 500 + " " + ItemService.gI().getTemplate(457).name + ", \n"
//    };
//
//    public static void TraoQuaSuKien(Player pl, int i) {
//
//        switch (i) {
//            case 0: {
//
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 1618);
//                    item.quantity = 3;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 2115);
//                    item.quantity = 2;
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 15;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 3333;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//
//            case 1: {
//
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 1618);
//                    item.quantity = 2;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 2115);
//                    item.quantity = 1;
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 2114);
//                    item.quantity = 1;
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 12;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 2222;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 2: {
//
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 1618);
//                    item.quantity = 1;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 2114);
//                    item.quantity = 1;
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 10;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 1111;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 3: {
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 5;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 500;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 4: {
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 5;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 5000;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 5: {
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 5;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 5000;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 6: {
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 5;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 5000;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 7: {
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 5;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 5000;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 8: {
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 5;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 5000;
//                    item.itemOptions.add(new Item.ItemOption(95, 20));
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 9: {
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 5;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 5000;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//            case 10: {
//                {//
//                    short[] item = {14, 15, 16, 17, 18, 19, 20};
//                    for (short nr : item) {
//                        Item ngocrong = ItemService.gI().createNewItem((short) nr);
//
//                        ngocrong.itemOptions.add(new Item.ItemOption(30, 1));
//                        ngocrong.quantity = 5;
//                        pl.inventory.itemsMailBox.add(ngocrong);
//                    }
//                }
//                {//
//                    Item item = ItemService.gI().createNewItem((short) 457);
//                    item.quantity = 5000;
//
//                    item.itemOptions.add(new Item.ItemOption(30, 1));
//                    pl.inventory.itemsMailBox.add(item);
//                }
//                GodGK.updateMailBox(pl);
//                Logger.error("Đã trao quà top quy lao " + (i + 1) + "cho " + pl.name + "");
//            }
//            break;
//
//        }
//    }
//    // su kien nau banh
//    public static int slBanhTrongNoi;
//
//    public static int mucNuocTrongNoi;
//    public static long thoiGianNauBanh = -999999;
//
//}
