package nro.services.func;

import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;

import nro.consts.ConstNpc;
import nro.jdbc.daos.GodGK;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.item.Item.ItemOption;
import nro.models.map.Zone;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.pariry.PariryServices;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.network.session.ISession;
import nro.server.Client;
import nro.server.Maintenance;
import nro.services.Service;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.NpcService;
//import nro.sukienbanhchung.NauBanhServices;
import nro.utils.Util;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import nro.consts.ConstDataEvent;
import nro.server.io.MySession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Input {

    public static String LOAI_THE;
    public static String MENH_GIA;
    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();

    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 504;
    public static final int NAP_THE = 505;
    public static final int CHANGE_NAME_BY_ITEM = 506;
    public static final int GIVE_IT = 507;
    public static final int CHOOSE_LEVEL_KG = 515;
    public static final int CHOOSE_LEVEL_CDRD = 516;
    // public static final int SEND_ITEM_OP = 512;

    public static final int QUY_DOI_COIN = 508;
    public static final int QUY_DOI_NGOC_XANH = 509;

    public static final int TAI = 510;
    public static final int XIU = 511;
    public static final int DOITHOI = 512;
    public static final int LE = 562;
    public static final int CHAN = 563;
    public static final int CONSOMAYMAN = 564;
    public static final int DOI_ITEM_SU_KIEN = 565;

    public static final int DOI_VND = 566;
    public static final int DOI_THOI_VANG = 567;
    public static final int DOI_NGOC_XANH = 568;
    public static final int DOI_NGOC_HONG = 569;
    public static final int DOI_COIN_ITEM_QUY_LAO = 570;
    public static final int QUY_DOI_HRZ = 513;

    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;
    public static final int FIND_PLAYER_PHU = 553;
    public static final int SEND_ITEM = 554;
    public static final int SEND_ITEM_OP = 555;
    public static final int SEND_ITEM_SKH = 556;
    public static final int SEND_THOI_VANG = 557;
    public static final int SEND_ITEM_GOLDBAR = 558;
    public static final int BUFFVND = 559;
    public static final int BUFFTOTALVND = 560;
    public static final int SEND_ITEM3 = 561;
    private static Input intance;
    public static final int MailBox = 65675;

    private Input() {

    }

    public void createFromMailBox(Player pl) {
        createForm(pl, MailBox, "Hộp thư gửi đến người chơi",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", ANY),
                new SubInput("Chuỗi option", ANY),
                new SubInput("Số lượng", NUMERIC));
    }

//    public void createFormNauBanhChung(Player player) {
//        createForm(player, NAU_BANH_CHUNG, "Nấu bánh chưng", new SubInput("Nhập số lượng bánh chưng cần nấu", NUMERIC));
//    }
//
//    public void createFormNauBanhTet(Player player) {
//        createForm(player, NAU_BANH_TET, "Nấu bánh tết", new SubInput("Nhập số lượng bánh tết cần nấu", NUMERIC));
//
//    }

//    public static final int NAU_BANH_CHUNG = 373;
//    private static final int NAU_BANH_TET = 6216;

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {

//                case NAU_BANH_TET:
//                    NauBanhServices.nauBanhTet(player, text[0]);
//                    break;
//                case NAU_BANH_CHUNG:
//                    NauBanhServices.nauBanhChung(player, text[0]);
//                    break;
                case MailBox:
                    if (player.isAdmin()) {
//                        int idItemBuff = Integer.parseInt(text[1]);
                        String itemIds = text[1];
                        String option = text[2];
                        int slItemBuff = Integer.parseInt(text[3]);
                        if (slItemBuff > 20000) {
                            Service.gI().sendThongBaoOK(player, "Buff vượt số lượng giới hạn vui lòng để tối đa sl 20000");
                            return;
                        }
                        String plName = text[0].trim();
                        if (plName.equals("all")) {
                            new Thread(() -> {
                                List<Player> allPlayer = GodGK.getAllPlayer();
                                for (Player pBuffItem : allPlayer) {
                                    if (pBuffItem != null) {
                                        String[] itemIdsArray = itemIds.split(",");
                                        for (String itemId : itemIdsArray) {
                                            int idItemBuff = Integer.parseInt(itemId);
                                            Item itembuff = ItemService.gI().createNewItem((short) idItemBuff, slItemBuff);

                                            if (option != null) {
                                                String[] Option = option.split(",");
                                                if (Option.length > 0) {
                                                    for (int i = 0; i < Option.length; i++) {
                                                        String[] optItem = Option[i].split("-");
                                                        int optID = Integer.parseInt(optItem[0]);
                                                        int param = Integer.parseInt(optItem[1]);
                                                        itembuff.itemOptions.add(new ItemOption(optID, param));
                                                    }
                                                }
                                            }
                                            pBuffItem.inventory.itemsMailBox.add(itembuff);

                                            if (GodGK.updateMailBox(pBuffItem)) {
                                                Service.gI().sendThongBao(player, "Bạn vừa gửi " + itembuff.template.name + " thành công cho " + pBuffItem.name);
                                            }
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Player không tồn tại");
                                    }
                                }
                            }).start();
                        } else {
                            Player pBuffItem = GodGK.loadPlayerByName(text[0].trim());
                            if (pBuffItem != null) {
                                String[] itemIdsArray = itemIds.split(",");
                                for (String itemId : itemIdsArray) {
                                    int idItemBuff = Integer.parseInt(itemId);
                                    Item itembuff = ItemService.gI().createNewItem((short) idItemBuff, slItemBuff);
                                    if (option != null) {
                                        String[] Option = option.split(",");
                                        if (Option.length > 0) {
                                            for (int i = 0; i < Option.length; i++) {
                                                String[] optItem = Option[i].split("-");
                                                int optID = Integer.parseInt(optItem[0]);
                                                int param = Integer.parseInt(optItem[1]);
                                                itembuff.itemOptions.add(new ItemOption(optID, param));
                                            }
                                        }
                                    }
                                    pBuffItem.inventory.itemsMailBox.add(itembuff);
                                    if (GodGK.updateMailBox(pBuffItem)) {
                                        Service.gI().sendThongBao(player, "Bạn vừa gửi " + itembuff.template.name + " thành công cho " + pBuffItem.name);
                                    }
                                }
                            } else {
                                Service.gI().sendThongBao(player, "Player không tồn tại");
                            }
                        }
                        break;

                    }
                    break;
                case BUFFVND:
                    try {
                    String username = text[0].trim();
                    int addvnd = Integer.parseInt(text[1].trim());
                    int addtotalvnd = Integer.parseInt(text[2].trim());
                    if (PlayerDAO.Addvnd(username, addvnd)
                            && PlayerDAO.Addtotalvnd(username, addtotalvnd)
                            && PlayerDAO.Addtotalvnd2(username, addtotalvnd)) {

                        Service.gI().sendThongBao(player, "Bạn đã buff cho " + username + " " + addvnd + "COIN\n và" + addtotalvnd + "VNĐ");

                        if (Client.gI().getPlayerByUserName(username) != null) {
                            Client.gI().getPlayerByUserName(username).getSession().vnd += addvnd;
                            Client.gI().getPlayerByUserName(username).getSession().totalvnd += addtotalvnd;
                            Client.gI().getPlayerByUserName(username).getSession().totalvnd2 += addtotalvnd;
                            Service.gI().sendThongBao(Client.gI().getPlayerByUserName(username), "Bạn vừa được cộng " + addvnd + "COIN bởi " + player.name);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra");
                }
                break;

                case BUFFTOTALVND:
                    try {
                    String username = text[0].trim();
                    int addtotalvnd = Integer.parseInt(text[1].trim());
                    if (addtotalvnd > 50000000) {
                        Service.gI().sendThongBaoOK(player, "Buff totalvnd vượt giới hạn vui lòng để buff tối đa 50tr vnd");
                        return;
                    }
                    if (PlayerDAO.Addtotalvnd(username, addtotalvnd)) {
                        Service.gI().sendThongBao(player, "Bạn đã buff cho " + username + " " + addtotalvnd + "VNĐ");
                        if (Client.gI().getPlayerByUserName(username) != null) {
                            Client.gI().getPlayerByUserName(username).getSession().totalvnd2 += addtotalvnd;
                            Service.gI().sendThongBao(Client.gI().getPlayerByUserName(username), "Bạn vừa được cộng " + addtotalvnd + " bới " + player.name);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra");
                }
                break;
                case GIVE_IT:
                    String name = text[0];
                    int id = Integer.valueOf(text[1]);
                    int q = Integer.valueOf(text[2]);
                    if (Client.gI().getPlayer(name) != null) {
                        Item item = ItemService.gI().createNewItem(((short) id));
                        item.quantity = q;
                        InventoryServiceNew.gI().addItemBag(Client.gI().getPlayer(name), item);
                        InventoryServiceNew.gI().sendItemBags(Client.gI().getPlayer(name));
                        Service.gI().sendThongBao(Client.gI().getPlayer(name), "Nhận " + item.template.name + " từ " + player.name);

                    } else {
                        Service.gI().sendThongBao(player, "Không online");
                    }
                    break;

                case CHANGE_PASSWORD:
                    Service.gI().changePassword(player, text[0], text[1], text[2]);
                    break;
                case GIFT_CODE: {
                    String textLevel = text[0];
                    Input.gI().addItemGiftCodeToPlayer(player, textLevel);
                    break;
                }
                case CONSOMAYMAN:
                    int CSMM = Integer.parseInt(text[0]);
                    if (CSMM >= MiniGame.gI().MiniGame_S1.min && CSMM <= MiniGame.gI().MiniGame_S1.max
                            && MiniGame.gI().MiniGame_S1.second > 10) {
                        MiniGame.gI().MiniGame_S1.newData(player, CSMM);
                        InventoryServiceNew.gI().sendItemBags(player);
                    } else {
                        Service.gI().sendThongBao(player, "Nhập số trong khoảng " + MiniGame.gI().MiniGame_S1.min + " - " + MiniGame.gI().MiniGame_S1.max + " thôi cha nội");
                    }

                    break;
                case FIND_PLAYER:
                    Player pl = Client.gI().getPlayer(text[0]);
                    if (pl != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, 1139, "Quyền Năng Thiên Sứ?",
                                new String[]{"Dịch chuyển\nđến\n" + pl.name, "Triệu hồi\n" + pl.name + "\nđến đây", "Đổi tên\n" + pl.name + "", "Khóa\n" + pl.name + "", "Đăng xuất\n" + pl.name + ""},
                                pl);
                    } else {
                        Service.gI().sendThongBao(player, "Cư dân không tồn tại hoặc hiện tại không online!!");
                    }
                    break;
                case CHANGE_NAME: {
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        String newName = text[0].substring(0, Math.min(text[0].length(), 255)); // Giới hạn chiều dài của tên
                        if (GirlkunDB.executeQuery("select * from player where name = ?", newName).next()) {
                            Service.gI().sendThongBao(player, "Tên cư dân đã tồn tại!");
                        } else {
                            plChanged.name = newName;
                            GirlkunDB.executeUpdate("update player set name = ? where id = ?", plChanged.name, plChanged.id);
                            Service.gI().player(plChanged);
                            Service.gI().Send_Caitrang(plChanged);
                            Service.gI().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
                            Service.gI().sendThongBao(plChanged, "Đại thiên sứ đã đổi tên của bạn thành: " + player.name + "");
                            Service.gI().sendThongBao(player, "Quyền năng của ngài đã áp dụng lên cư dân  ");
                        }
                    }
                }
                break;
                case CHANGE_NAME_BY_ITEM: {
                    if (player != null) {
                        String newName = text[0];

                        // Check if the new name exceeds the maximum length allowed
                        int maxNameLength = 50; // Replace with the actual maximum length of the 'name' column
                        GirlkunResultSet rs = GirlkunDB.executeQuery("select * from player where name = ?", newName);
                        if (rs.first()) {
                            Service.gI().sendThongBaoOK(player, "Tên người chơi đã tồn tại");
                        } else if (newName.length() > maxNameLength) {
                            Service.gI().sendThongBao(player, "Tên nhân vật quá dài");
                        } else {
                            Item theDoiTen = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2006);
                            if (theDoiTen == null) {
                                Service.gI().sendThongBao(player, "Không tìm thấy thẻ đổi tên");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(player, theDoiTen, 1);

                                try {
                                    // Update the 'name' column with the new name
                                    GirlkunDB.executeUpdate("update player set name = ? where id = ?", newName, player.id);

                                    // Rest of your code for the name change process
                                    player.name = newName;
                                    Service.gI().player(player);
                                    Service.gI().Send_Caitrang(player);
                                    Service.gI().sendFlagBag(player);
                                    Zone zone = player.zone;
                                    ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                                    Service.gI().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                                } catch (SQLException e) {
                                    // Handle database error
                                    e.printStackTrace();
                                    Service.gI().sendThongBao(player, "Đã xảy ra lỗi khi cập nhật tên");
                                }
                            }
                        }
                    }
                }
                break;

//                 case SEND_ITEM_GOLDBAR: {
//                    if (Maintenance.isRuning) {
//                        break;
//                    }
//                    Player p = (Player) PLAYERID_OBJECT.get(player.id);
//                    if (p != null) {
//                        String name = p.getSession().uu;
//                        PlayerDAO.addGoldBar(p, Integer.parseInt(text[0]), p.getSession().uu);
//
////                        Thread.sleep(1000);
//                        String txtBuff = "|2|Buff goldbar đến tài khoản: " + name + "/Id: " + p.getSession().userId + "/Tên nhân vật: " + p.getSession().player.name + "\b";
//                        String txtBuffpl = "|2|Bạn nhận được: " + Integer.parseInt(text[0]) + " thỏi từ ADMIN\b"
//                                + "|1|Số lượng thỏi trong tài khoản của bạn hiện tại: " + p.getSession().goldBar;
//                        txtBuff += "|2|Số lượng: " + Integer.parseInt(text[0]) + " thỏi\b"
//                                + "|1|Số lượng thỏi trong tài khoản " + p.getSession().uu + " hiện tại: " + p.getSession().goldBar;
//                        NpcService.gI().createTutorial(player, 24, txtBuff);
//                        NpcService.gI().createTutorial(p, 24, txtBuffpl);
//                    } else {
//                        NpcService.gI().createTutorial(player, 24, "Không tìm thấy player");
//                    }
//                }
//                break;
                case TAI:
                    int sotvxiu = Integer.valueOf(text[0]);
                    if (sotvxiu < 0) {
                        Service.gI().sendThongBao(player, "Cút");
                        return;
                    }
                    Item tv2 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.template != null && item.isNotNullItem() && item.template.id == 457) {
                            tv2 = item;
                            break;
                        }
                    }
                    try {
                        if (tv2 != null && tv2.quantity >= sotvxiu) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv2, sotvxiu);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {
                                    Item tvthang = ItemService.gI().createNewItem((short) 1108);
                                    tvthang.quantity = (int) Math.round(sotvxiu * 1.7);
                                    if (tvthang.template.id == 457 || tvthang.template.id == 1108) {
                                        tvthang.itemOptions.add(new Item.ItemOption(30, 1));
                                        tvthang.itemOptions.add(new Item.ItemOption(93, 15));
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, tvthang);

                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " "
                                            + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvxiu
                                            + " thỏi vàng vào Xỉu 1" + "\nKết quả : Xỉu" + "\n\nVề bờ");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvxiu + " thỏi vàng vào Xỉu" + "\nKết quả : Tam hoa" + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :"
                                            + " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                            + sotvxiu + " thỏi vàng vào Xỉu 2" + "\nKết quả : Tài" + "\nCòn cái nịt.");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                case XIU:
                    int sotvtai = Integer.valueOf(text[0]);
                    if (sotvtai < 0) {
                        Service.gI().sendThongBao(player, "Cút");
                        return;
                    }
                    Item tv1 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.template != null && item.isNotNullItem() && item.template.id == 457) {
                            tv1 = item;
                            break;
                        }
                    }
                    try {
                        if (tv1 != null && tv1.quantity >= sotvtai) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tv1, sotvtai);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để biết kết quả.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 6);
                            int y = Util.nextInt(1, 6);
                            int z = Util.nextInt(1, 6);
                            int tong = (x + y + z);
                            if (4 <= (x + y + z) && (x + y + z) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :"
                                            + " " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : "
                                            + sotvtai + " thỏi vàng vào Tài 1" + "\nKết quả : Xỉu " + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if (x == y && x == z) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvtai + " thỏi vàng vào Xỉu" + "\nKết quả : Tam hoa" + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if ((x + y + z) > 10) {

                                if (player != null) {
                                    Item tvthang = ItemService.gI().createNewItem((short) 1108);
                                    tvthang.quantity = (int) Math.round(sotvtai * 1.7);
                                    if (tvthang.template.id == 457 || tvthang.template.id == 1108) {
                                        tvthang.itemOptions.add(new Item.ItemOption(30, 1));
                                        tvthang.itemOptions.add(new Item.ItemOption(93, 15));
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, tvthang);

                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " "
                                            + y + " " + z + "\nTổng là : " + tong + "\nBạn đã cược : " + sotvtai
                                            + " thỏi vàng vào Tài 2 " + "\nKết quả : Tài" + "\n\nVề bờ");
                                    return;
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;

                case CHAN:

                    String inputStr = text[0];
                    inputStr = inputStr.replaceAll("[^\\d]", "");
//                    if (text[0] == "") {
//                        Service.gI().sendThongBao(player, "?");
//                        return;
//                    }
                    int tvCuoc = Integer.parseInt(inputStr);
                    if (tvCuoc < 10) {
                        Service.gI().sendThongBao(player, "Chỉ cược cao hơn 10 thỏi vàng.");
                        return;
                    }
                    if (tvCuoc > 10000) {
                        Service.gI().sendThongBao(player, "Chỉ cược thấp hơn 10.000 thỏi vàng.");
                        return;
                    }
                    if (tvCuoc <= 0) {
                        Service.gI().sendThongBao(player, "?");
                    } else {
                        Item tv = null;
                        for (Item item : player.inventory.itemsBag) {
                            if (item.isNotNullItem() && item.template.id == 457) {
                                tv = item;
                                break;
                            }
                        }
                        try {
                            if (tv != null) {
                                if (tv.quantity >= tvCuoc) {

                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv, tvCuoc);

                                    InventoryServiceNew.gI().sendItemBags(player);
                                    // Service.gI().sendMoney(player);
                                    player.cuoc = tvCuoc;
                                    PariryServices.gI().addPlayerEven(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                                }
                            } else {
                                Service.gI().sendThongBao(player, "Nghèo còn bày đặt chơi");

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Service.gI().sendThongBao(player, "Lỗi.");
                        }
                    }
                    break;

                case LE:
                    String inputStr2 = text[0];
                    inputStr2 = inputStr2.replaceAll("[^\\d]", "");
//                    if (text[0] == "") {
//                        Service.gI().sendThongBao(player, "?");
//                        return;
//                    }
                    tvCuoc = Integer.parseInt(inputStr2);
                    if (tvCuoc < 10) {
                        Service.gI().sendThongBao(player, "Chỉ cược cao hơn 10 thỏi vàng.");
                        return;
                    }
                    if (tvCuoc > 10000) {
                        Service.gI().sendThongBao(player, "Chỉ cược thấp hơn 10.000 thỏi vàng.");
                        return;
                    }
                    if (tvCuoc <= 0) {
                        Service.gI().sendThongBao(player, "?");
                    } else {
                        Item tv = null;
                        for (Item item : player.inventory.itemsBag) {
                            if (item.isNotNullItem() && item.template.id == 457) {
                                tv = item;
                                break;
                            }
                        }
                        try {
                            if (tv != null) {
                                if (tv.quantity >= tvCuoc) {
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, tv, tvCuoc);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.cuoc = tvCuoc;
                                    PariryServices.gI().addPlayerOdd(player);
                                } else {
                                    Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để chơi.");
                                }
                            } else {
                                Service.gI().sendThongBao(player, "Nghèo còn bày đặt chơi");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Service.gI().sendThongBao(player, "Lỗi.");
                        }
                    }
                    break;
                case DOITHOI:

                    int sotvdoi = Integer.valueOf(text[0]);
                    if (sotvdoi < 0) {
                        Service.gI().sendThongBao(player, "Cút");
                        return;
                    }
                    if (sotvdoi > 5000) {
                        Service.gI().sendThongBao(player, "Rèn tối đa 5000 thỏi vàng");
                        return;
                    }
                    Item tvdoi1 = null;
                    for (Item item : player.inventory.itemsBag) {
                        if (item.template != null && item.isNotNullItem() && item.template.id == 457) {
                            tvdoi1 = item;
                            break;
                        }
                    }
                    try {
                        if (tvdoi1 != null && tvdoi1.quantity >= sotvdoi) {
                            InventoryServiceNew.gI().subQuantityItemsBag(player, tvdoi1, sotvdoi);
                            InventoryServiceNew.gI().sendItemBags(player);
                            int TimeSeconds = 10;
                            Service.gI().sendThongBao(player, "Chờ 10 giây để t rèn cái.");
                            while (TimeSeconds > 0) {
                                TimeSeconds--;
                                Thread.sleep(1000);
                            }
                            int x = Util.nextInt(1, 9);
                            int y = Util.nextInt(1, 9);

                            int tong = x + y;
                            if (4 <= (x + y) && (x + y) <= 10) {
                                if (player != null) {// tự sửa lại tên lấy
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra là :"
                                            + " " + x + " " + y + " \nTổng là : " + tong + "\nBạn đã rèn : "
                                            + sotvdoi + " thỏi vàng lỏ" + "\nKết quả : ĐÃ BỊ TRỘM MẤT " + "\nĐÚNG CÒN CÁI NỊT.");
                                    return;
                                }
                            } else if (x == y) {
                                if (player != null) {
                                    Service.gI().sendThongBaoOK(player, "Kết quả" + "Số hệ thống quay ra : " + x + " " + y + " \nTổng là : " + tong + "\nBạn đã rèn : " + sotvdoi + " thỏi vàng vào lò" + "\nKết quả : Số thì đẹp đấy nhưng" + "\nCòn cái nịt.");
                                    return;
                                }
                            } else if ((x + y) > 10) {

                                if (player != null) {
                                    Item tvthang = ItemService.gI().createNewItem((short) 457);
                                    tvthang.quantity = (int) Math.round(sotvdoi * 0.8);
                                    if (tvthang.template.id == 457) {
                                        tvthang.itemOptions.add(new Item.ItemOption(93, 5));
                                    }
                                    player.inventory.itemsMailBox.add(tvthang);
                                    if (GodGK.updateMailBox(player)) {

                                        Service.gI().sendThongBaoOK(player, "Kết quả" + "\nSố hệ thống quay ra : " + x + " "
                                                + y + " \nTổng là : " + tong + "\nBạn đã rèn : " + sotvdoi
                                                + " thỏi vàng vào lò " + "\nKết quả : Rèn Thành Công" + "\n\nChúc Mừng Em Iu"
                                                + "Thỏi vàng đã gửi hòm thư kiểm tra tại npc ở nhà nhé");
                                        return;
                                    }
                                }
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không đủ thỏi vàng để rèn.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.gI().sendThongBao(player, "Lỗi.");
                    }
                    break;
                case SEND_THOI_VANG: {
                    if (Maintenance.isRuning) {
                        break;
                    }
                    if (player.getSession().goldBar > 0) {
                        int checkthoi = (player.getSession().goldBar - Integer.parseInt(text[0]));
                        if (player.getSession().actived == false) {
                            if (checkthoi < 20) {
                                NpcService.gI().createTutorial(player, 24, "|1|Hiện tại bạn chưa kích hoạt thành viên\b"
                                        + "|7|Hãy chừa lại 20 thỏi vàng\b"
                                        + "|1|để kích hoạt thành viên tại Quy lão Kame!");
                                return;
                            }
                        }
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {

                            int goldBefore = player.getSession().goldBar;

                            int quatidyitembeforebag = 0;
                            int quatidyitemafterbag = 0;

                            int quatidyitembeforebox = 0;
                            int quatidyitemafterbox = 0;

                            for (Item itembeforebag : player.inventory.itemsBag) {
                                if (itembeforebag.isNotNullItem() && itembeforebag.template.id == 457) {
                                    quatidyitembeforebag = itembeforebag.quantity;
                                }
                            }

                            for (Item itembeforebox : player.inventory.itemsBox) {
                                if (itembeforebox.isNotNullItem() && itembeforebox.template.id == 457) {
                                    quatidyitembeforebox = itembeforebox.quantity;
                                }
                            }

                            int soluong = Integer.parseInt(text[0]);
                            if (text[0].contains("-")) {
                                NpcService.gI().createTutorial(player, 24, "|7|Số lượng không hợp lệ");
                                return;
                            }

//                            if (Util.isNumeric(text[0])) {
//                                NpcService.gI().createTutorial(player,24, "|7|Chỉ được nhập số");
//                                return;
//                            }
                            if (soluong <= 0) {
                                NpcService.gI().createTutorial(player, 24, "|7|Giá trị nhập vào không đúng");
                                break;
                            }
                            if (soluong > player.getSession().goldBar) {
                                NpcService.gI().createTutorial(player, 24, "|1|Hiện con đang không đủ thỏi vàng\b"
                                        + "|7|hãy liên hệ ADMIN 'B O'\b"
                                        + "|1|để nạp thỏi vàng nhé!");
                                return;
                            }
                            if (PlayerDAO.subGoldBar(player, soluong)) {
                                int goldAfter = player.getSession().goldBar;

                                Item goldBar = ItemService.gI().createNewItem((short) 457, Integer.parseInt(text[0]));
                                InventoryServiceNew.gI().addItemBag(player, goldBar);
                                InventoryServiceNew.gI().sendItemBags(player);

                                for (Item itemafterbag : player.inventory.itemsBag) {
                                    if (itemafterbag.isNotNullItem() && itemafterbag.template.id == 457) {
                                        quatidyitemafterbag = itemafterbag.quantity;
                                    }
                                }

                                for (Item itemafterbox : player.inventory.itemsBox) {
                                    if (itemafterbox.isNotNullItem() && itemafterbox.template.id == 457) {
                                        quatidyitemafterbox = itemafterbox.quantity;
                                    }
                                }
                                NpcService.gI().createTutorial(player, 24, "|1|Ta đã gửi " + Integer.parseInt(text[0]) + " thỏi vàng vào hành trang của con\b con hãy kiểm tra ");
                                PlayerDAO.addHistoryReceiveGoldBar(player, goldBefore, goldAfter, quatidyitembeforebag, quatidyitemafterbag, quatidyitembeforebox, quatidyitemafterbox);
                            } else {
                                NpcService.gI().createTutorial(player, 24, "|7|Lỗi vui lòng báo admin...");
                            }
                        } else {
                            NpcService.gI().createTutorial(player, 24, "|2|Hãy chừa cho ta 1 ô trống");
                        }
                    } else {
                        NpcService.gI().createTutorial(player, 24, "|1|Con đang không có thỏi vàng\b"
                                + "|7|hãy liên hệ ADMIN 'B O'\b"
                                + "|1|để nạp thỏi vàng nhé!");
                    }
                }
                break;
//                case FIND_PLAYER_PHU: {
//                    Player pl = Client.gI().getPlayer(text[0]);
//                    if (pl != null) {
//                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER_PHU, -1, "Lựa chọn muốn..?",
//                                new String[]{"Ban\nTài khoản\nvĩnh viễn", "Ban\ntheo giờ"},
//                                pl);
//                    } else {
//                        Service.gI().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
//                    }
//                }
//                break;
                case CHOOSE_LEVEL_BDKB:
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?\n"
                                    + "Level 20 trở đi sẽ rớt Thỏi Vàng và Ngọc Rồng ?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Vui lòng chọn lv từ 1 đến 110");
                    }

//                    BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                    break;
//                case GIFT_CODE: {
//                    GiftService.gI().giftCode(player, text[0]);
//                }
//                break;
                case CHOOSE_LEVEL_KG:
                    int levelkg = Integer.parseInt(text[0]);
                    if (levelkg >= 1 && levelkg <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_KG,
                                    "Con có chắc chắn muốn tới khí gas cấp độ " + levelkg + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, levelkg);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Vui lòng chọn lv từ 1 đến 110");
                    }

//                    BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                    break;
                case CHOOSE_LEVEL_CDRD:
                    int level3 = Integer.parseInt(text[0]);
                    if (level3 >= 1 && level3 <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                    "Con có chắc chắn muốn tới con đường rắn độc cấp độ " + level3 + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level3);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Vui lòng chọn lv từ 1 đến 110");
                    }
                    break;

                case NAP_THE:
                    //    NapThe.SendCard(player, LOAI_THE, MENH_GIA, text[0], text[1]);
                    break;
                case QUY_DOI_COIN:
                    int ratioGold = 1;
                    int coinGold = 10;
                    int goldTrade = Integer.parseInt(text[0]);
                    if (goldTrade <= 0 || goldTrade >= 500) {
                        Service.gI().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 500");
                        //       } else if (player.session.vnd >= goldTrade * coinGold) {
                        PlayerDAO.subvndBar(player, goldTrade * coinGold);
                        Item thoiVang = ItemService.gI().createNewItem((short) 457, goldTrade * 1);// x3
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + goldTrade * ratioGold
                                + " " + thoiVang.template.name);
                    } else {
                        //               Service.gI().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                        //                         + " đổi " + goldTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - goldTrade));
                    }
                    break;
//                case DOI_ITEM_SU_KIEN:
//                    short slItem = Short.parseShort(text[0]);
//                    Item item = InventoryServiceNew.gI().findItemBag(player, ConstDataEvent.ID_ITEM_7);
//                    if (item == null) {
//                        Service.gI().sendThongBao(player, "Hành trang bạn không có " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_7).name);
//                        return;
//                    }
//                    if (item.quantity < slItem) {
//                        Service.gI().sendThongBao(player, "Hành trang bạn không đủ " + slItem + " " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_7).name);
//                        return;
//                    }
//                    if (slItem < 0) {
//                        Service.gI().sendThongBao(player, "Bạn không được phép nhập số âm ");
//                        return;
//                    }
//                    if (slItem >= 0 && slItem <= 30000) {
//                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, slItem);
//                        InventoryServiceNew.gI().sendItemBags(player);
//                        player.event.addEventPoint(slItem);
//                        player.event.addEventPointMoc(slItem);
//                        Service.gI().sendThongBao(player, "Bạn đã nhận được " + slItem + " điểm");
//                    } else {
//                        Service.gI().sendThongBao(player, "Chọn 1 con số từ 1 đến 29999");
//                    }
//                    break;
                case DOI_VND: {
                    int vnd = Integer.parseInt(text[0]);
                    int coin = vnd * 9 / 10;
                    if (player.getSession() != null && player.getSession().totalvnd < vnd) {
                        Service.gI().sendThongBao(player, "Bạn không đủ " + vnd + " VND");
                        return;
                    }
                    if (vnd < 0) {
                        Service.gI().sendThongBao(player, "Bạn không được phép nhập số âm ");
                        return;
                    }

                    if (vnd >= 20000 && vnd <= 100000000) {
                        PlayerDAO.subtotalvnd(player, vnd);
                        PlayerDAO.addvnd(player, coin);
                        Service.gI().sendThongBao(player, "Bạn đã nhận được " + coin + " COIN");
                    } else {
                        Service.gI().sendThongBao(player, "Chọn 1 con số từ 20000 đến 100000000");
                    }
                }
                break;
                case DOI_THOI_VANG: {
                    int coin = Integer.parseInt(text[0]);
                    int sl = coin / 1000;
                    if (player.getSession() != null && player.getSession().vnd < coin) {
                        Service.gI().sendThongBao(player, "Bạn không đủ " + coin + " COIN");
                        return;
                    }
                    if (coin < 0) {
                        Service.gI().sendThongBao(player, "Bạn không được phép nhập số âm ");
                        return;
                    }
                    if (coin >= 20000 && coin <= 100000000) {
                        PlayerDAO.subvnd(player, coin);
                        Item thoiVang = ItemService.gI().createNewItem((short) 457, sl);
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + sl
                                + " " + thoiVang.template.name);
                    } else {
                        Service.gI().sendThongBao(player, "Chọn 1 con số từ 20000 đến 100000000");
                    }
                }
                break;
                case DOI_NGOC_XANH: {
                    int coin = Integer.parseInt(text[0]);
                    int sl = coin * 2;
                    if (player.getSession() != null && player.getSession().vnd < coin) {
                        Service.gI().sendThongBao(player, "Bạn không đủ " + coin + " COIN");
                        return;
                    }
                    if (coin < 0) {
                        Service.gI().sendThongBao(player, "Bạn không được phép nhập số âm ");
                        return;
                    }
                    if (coin >= 20000 && coin <= 100000000) {
                        PlayerDAO.subvnd(player, coin);
                        Item thoiVang = ItemService.gI().createNewItem((short) 77, sl);
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + sl
                                + " " + thoiVang.template.name);
                    } else {
                        Service.gI().sendThongBao(player, "Chọn 1 con số từ 20000 đến 100000000");
                    }
                }
                break;
                case DOI_NGOC_HONG: {
                    int coin = Integer.parseInt(text[0]);
                    int sl = coin*10;
                    if (player.getSession() != null && player.getSession().vnd < coin) {
                        Service.gI().sendThongBao(player, "Bạn không đủ " + coin + " COIN");
                        return;
                    }
                    if (coin < 0) {
                        Service.gI().sendThongBao(player, "Bạn không được phép nhập số âm ");
                        return;
                    }
                    if (coin >= 20000 && coin <= 100000000) {
                        PlayerDAO.subvnd(player, coin);
                        Item thoiVang = ItemService.gI().createNewItem((short) 861, sl);
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + sl
                                + " " + thoiVang.template.name);
                    } else {
                        Service.gI().sendThongBao(player, "Chọn 1 con số từ 20000 đến 100000000");
                    }
                }
                break;
//                case DOI_COIN_ITEM_QUY_LAO: {
//                    int coin = Integer.parseInt(text[0]);
//                    int sl = coin / 4000;
//                    if (player.getSession() != null && player.getSession().vnd < coin) {
//                        Service.gI().sendThongBao(player, "Bạn không đủ " + coin + " COIN");
//                        return;
//                    }
//                    if (coin < 0) {
//                        Service.gI().sendThongBao(player, "Bạn không được phép nhập số âm ");
//                        return;
//                    }
//                    if (coin >= 4000 && coin <= 100000000) {
//                        PlayerDAO.subvnd(player, coin);
//                        Item thoiVang = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_6, sl);
//                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
//                        InventoryServiceNew.gI().sendItemBags(player);
//                        Service.gI().sendThongBao(player, "bạn nhận được " + sl
//                                + " " + thoiVang.template.name);
//                    } else {
//                        Service.gI().sendThongBao(player, "Chọn 1 con số từ 4000 đến 100000000");
//                    }
//                }
//                break;
                case QUY_DOI_NGOC_XANH:
                    int ratioGem = 1;
                    int coinGem = 1;
                    int GemTrade = Integer.parseInt(text[0]);
                    if (GemTrade <= 0 || GemTrade >= 10000000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 10.000.000 Ngọc xanh");
                        //             } else if (player.session.vnd >= GemTrade * coinGem) {
                        PlayerDAO.subvndBar(player, GemTrade * coinGem);
                        Item thoiVang = ItemService.gI().createNewItem((short) 77, GemTrade * 10);// x3
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + GemTrade * ratioGem
                                + " " + thoiVang.template.name);
                    } else {
                        //           Service.gI().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                        //                   + " đổi " + GemTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - GemTrade));
                    }
                    break;

                case QUY_DOI_HRZ:
                    int ratioHRZ = 1;
                    int coinHRZ = 1;
                    int HRZTrade = Integer.parseInt(text[0]);
                    if (HRZTrade <= 0 || HRZTrade >= 1000000) {
                        Service.gI().sendThongBao(player, "Quá giới hạn mỗi lần chỉ được 1.000.000 Ngọc Hồng");
                        //      } else if (player.session.vnd >= HRZTrade * coinHRZ) {
                        PlayerDAO.subvndBar(player, HRZTrade * coinHRZ);
                        Item thoiVang = ItemService.gI().createNewItem((short) 861, HRZTrade * 10);//
                        InventoryServiceNew.gI().addItemBag(player, thoiVang);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "bạn nhận được " + HRZTrade * ratioHRZ
                                + " " + thoiVang.template.name);
                    } else {
                        //           Service.gI().sendThongBao(player, "Số Xu của bạn là " + player.session.vnd + " không đủ để quy "
                        //                  + " đổi " + HRZTrade + " thỏi vàng " + " " + "bạn cần thêm" + (player.session.vnd - HRZTrade));
                    }
                    break;
                case SEND_ITEM:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int quantityItemBuff = Integer.parseInt(text[2]);
//                        if (idItemBuff != 861 && quantityItemBuff > 20000) {
//                            Service.gI().sendThongBaoOK(player, "Buff số lượng quá tay r, tối đa 20k thôi");
//                            return;
//                        }
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) quantityItemBuff, (Inventory.LIMIT_GOLD + pBuffItem.limitgold));
                                txtBuff += quantityItemBuff + " vàng\b";
                                Service.gI().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " ngọc\b";
                                Service.gI().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + quantityItemBuff, 2000000000);
                                txtBuff += quantityItemBuff + " hồng ngọc\b";
                                Service.gI().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.quantity = quantityItemBuff;
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                                txtBuff += "x" + quantityItemBuff + " " + itemBuffTemplate.template.name + "\b";
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;
                case SEND_ITEM_OP:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int idOptionBuff = Integer.parseInt(text[2]);
                        int slOptionBuff = Integer.parseInt(text[3]);
                        int slItemBuff = Integer.parseInt(text[4]);
                        if (slItemBuff > 20000) {
                            Service.gI().sendThongBaoOK(player, "Buff số lượng quá tay r, tối đa 20k thôi");
                            return;
                        }
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) slItemBuff, (Inventory.LIMIT_GOLD + pBuffItem.limitgold));
                                txtBuff += slItemBuff + " vàng\b";
                                Service.gI().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc\b";
                                Service.gI().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc khóa\b";
                                Service.gI().sendMoney(player);
                            } else {
                                //Item itemBuffTemplate = ItemBuff.getItem(idItemBuff);
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff, slOptionBuff));
                                itemBuffTemplate.quantity = slItemBuff;
                                txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(player, 24, txtBuff);
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Player không online");
                        }
                        break;
                    }
                    break;
                case SEND_ITEM_SKH:
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        int idOptionSKH = Integer.parseInt(text[2]);
                        int idOptionBuff = Integer.parseInt(text[3]);
                        int slOptionBuff = Integer.parseInt(text[4]);
                        int slItemBuff = Integer.parseInt(text[5]);
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.inventory.gold = Math.min(pBuffItem.inventory.gold + (long) slItemBuff, (Inventory.LIMIT_GOLD + pBuffItem.limitgold));
                                txtBuff += slItemBuff + " vàng\b";
                                Service.gI().sendMoney(player);
                            } else if (idItemBuff == -2) {
                                pBuffItem.inventory.gem = Math.min(pBuffItem.inventory.gem + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc\b";
                                Service.gI().sendMoney(player);
                            } else if (idItemBuff == -3) {
                                pBuffItem.inventory.ruby = Math.min(pBuffItem.inventory.ruby + slItemBuff, 2000000000);
                                txtBuff += slItemBuff + " ngọc khóa\b";
                                Service.gI().sendMoney(player);
                            } else {
                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff);
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionSKH, 0));
                                if (idOptionSKH == 127) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(139, 0));
                                } else if (idOptionSKH == 128) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(140, 0));
                                } else if (idOptionSKH == 129) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(141, 0));
                                } else if (idOptionSKH == 130) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(142, 0));
                                } else if (idOptionSKH == 131) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(143, 0));
                                } else if (idOptionSKH == 132) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(144, 0));
                                } else if (idOptionSKH == 133) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(136, 0));
                                } else if (idOptionSKH == 134) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(137, 0));
                                } else if (idOptionSKH == 135) {
                                    itemBuffTemplate.itemOptions.add(new Item.ItemOption(138, 0));
                                }
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(30, 0));
                                itemBuffTemplate.itemOptions.add(new Item.ItemOption(idOptionBuff, slOptionBuff));
                                itemBuffTemplate.quantity = slItemBuff;
                                txtBuff += "x" + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itemBuffTemplate);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                            }
                            NpcService.gI().createTutorial(player, 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                NpcService.gI().createTutorial(player, 24, txtBuff);
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Player không online");
                        }
                        break;

                    }
                    break;
                case SEND_ITEM3:
//                      new SubInput("Tên người chơi", ANY),
//                new SubInput("ID Trang Bị", NUMERIC),
//             
//                 new SubInput("Chuỗi option", ANY),
//                new SubInput("Số lượng", NUMERIC));
                    if (player.isAdmin()) {
                        int idItemBuff = Integer.parseInt(text[1]);
                        String option = text[2];
                        int slItemBuff = Integer.parseInt(text[3]);
                        if (slItemBuff > 20000) {
                            Service.gI().sendThongBaoOK(player, "Buff số lượng quá tay r, tối đa 20k thôi");
                            return;
                        }
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        if (pBuffItem != null) {

                            try {
                                Item itembuff = ItemService.gI().createNewItem((short) idItemBuff, slItemBuff);

                                if (option != null) {
                                    String[] Option = option.split(",");
                                    if (Option.length > 0) {
                                        for (int i = 0; i < Option.length; i++) {
                                            String[] optItem = Option[i].split("-");
                                            int optID = Integer.parseInt(optItem[0]);
                                            int param = Integer.parseInt(optItem[1]);
                                            itembuff.itemOptions.add(new ItemOption(optID, param));
                                        }
                                    }
                                }
                                InventoryServiceNew.gI().addItemBag(pBuffItem, itembuff);
                                InventoryServiceNew.gI().sendItemBags(pBuffItem);
                                Service.gI().sendThongBao(player, "Bạn vừa buff" + itembuff.template.name + " thành công cho " + player.name);
                                Service.gI().sendThongBao(pBuffItem, "Bạn vừa được buff " + itembuff.template.name + " thành công bởi " + player.name);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Service.gI().sendThongBao(player, "Đã có lỗi xảy ra vui lòng thực hiện lại");
                            }

                        } else {
                            Service.gI().sendThongBao(player, "Player không online");
                        }
                        break;

                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createForm(ISession session, int typeInput, String title, SubInput... subInputs) {
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Quên Mật Khẩu", new SubInput("Nhập mật khẩu đã quên", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormGiveItem(Player pl) {
        createForm(pl, GIVE_IT, "Tặng vật phẩm", new SubInput("Tên", ANY), new SubInput("Id Item", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "Mã Quà Tặng", new SubInput("Nhập ở đây!", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }

    public void TAI(Player pl) {
        createForm(pl, TAI, "Chọn số thỏi vàng đặt Xỉu", new SubInput("Số thỏi vàng", ANY));//????
    }

    public void XIU(Player pl) {
        createForm(pl, XIU, "Chọn số thỏi vàng đặt Tài", new SubInput("Số thỏi vàng", ANY));
    }

    public void CHAN(Player pl) {
        createForm(pl, CHAN, "Nhập số thỏi vàng đặt chẵn", new SubInput("Số thỏi vàng", ANY));
    }

    public void Consomayman(Player pl) {
        createForm(pl, CONSOMAYMAN, "Hãy chọn 1 số từ " + MiniGame.gI().MiniGame_S1.min + "-"
                + MiniGame.gI().MiniGame_S1.max + ", giá 1000 VND", new SubInput("Chọn số", ANY));
    }

    public void LE(Player pl) {
        createForm(pl, LE, "Nhập số thỏi vàng đặt lẻ", new SubInput("Số thỏi vàng", ANY));
    }

    public void DOITHOI(Player pl) {
        createForm(pl, DOITHOI, "Chọn số thỏi vàng cần đổi", new SubInput("Số thỏi vàng", ANY));
    }

    public void createFormNapThe(Player pl, String loaiThe, String menhGia) {
        LOAI_THE = loaiThe;
        MENH_GIA = menhGia;
        createForm(pl, NAP_THE, "Nạp thẻ", new SubInput("Số Seri", ANY), new SubInput("Mã thẻ", ANY));
    }
//    public void createFormSenditem1(Player pl) {
//    createForm(pl, SEND_ITEM_OP, "Quyền năng Riot",
//            new SubInput("Name", ANY),
//            new SubInput("ID Item", NUMERIC),
//            new SubInput("ID Option", ANY),
//            new SubInput("Quantity", NUMERIC));
//}

    public void createFormQDTV(Player pl) {

        createForm(pl, QUY_DOI_COIN, "Quy đổi thỏi vàng, giới hạn đổi không quá 500 Thỏi vàng."
                + "\n10 Xu = 1 Thỏi vàng", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

//    public void createFormDoiItem(Player pl) {
//
//        createForm(pl, DOI_ITEM_SU_KIEN, "Đổi " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_7).name + " lấy điểm đổi shop",
//                new SubInput("Nhập số lượng muốn đổi", NUMERIC));
//    }

    public void createFormDoiVND(Player pl) {

        createForm(pl, DOI_VND, "Đổi VND --> COIN < VND x 0.9 >",
                new SubInput("Nhập số lượng VND muốn đổi ra COIN", NUMERIC));
    }

    public void createFormDoiThoiVang(Player pl) {

        createForm(pl, DOI_THOI_VANG, "Đổi COIN --> Thỏi vàng < COIN / 1000 >",
                new SubInput("Nhập số lượng COIN muốn đổi ra thỏi vàng", NUMERIC));
    }

    public void createFormDoiNgocXanh(Player pl) {

        createForm(pl, DOI_NGOC_XANH, "Đổi COIN --> Ngọc xanh < COIN x 2 >",
                new SubInput("Nhập số lượng COIN muốn đổi ra ngọc xanh", NUMERIC));
    }

    public void createFormDoiNgocHong(Player pl) {

        createForm(pl, DOI_NGOC_HONG, "Đổi COIN --> Ngọc hồng < COIN x 10 >",
                new SubInput("Nhập số lượng COIN muốn đổi ra ngọc hồng", NUMERIC));
    }

//    public void createFormDoiItemQuyLao(Player pl) {
//
//        createForm(pl, DOI_COIN_ITEM_QUY_LAO, "Đổi COIN --> " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name + " < COIN / 4000 >",
//                new SubInput("Nhập số lượng COIN muốn đổi ra " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name, NUMERIC));
//    }

    public void createFormQDNX(Player pl) {

        createForm(pl, QUY_DOI_NGOC_XANH, "Quy đổi Xu sang Ngọc Xanh."
                + "\n1 Xu = 10 Ngọc Xanh", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormQDHRZ(Player pl) {

        createForm(pl, QUY_DOI_HRZ, "Quy đổi Xu sang Ngọc Hồng."
                + "\n1 Xu = 10 Ngọc Hồng", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelKG(Player pl) {
        createForm(pl, CHOOSE_LEVEL_KG, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelCDRD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_CDRD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormNhanTV(Player pl) {
        createForm(pl, SEND_THOI_VANG, "Nhập số lượng thỏi vàng muốn đổi", new SubInput("Nhập giá trị là số....", NUMERIC));
    }

    public void createFormFindPlayer1(Player pl) {
        createForm(pl, FIND_PLAYER_PHU, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }
//
//    public void createFormFindPlayer2(Player pl) {
//        createForm(pl, NHAP_GIO, "Nhập số giờ", new SubInput("Nhập tên nhân vật cần ban...", ANY), new SubInput("Nhập số...", NUMERIC));
//    }

    public void createFormSendGoldBar(Player pl) {
        createForm(pl, SEND_ITEM_GOLDBAR, "SEND THỎI NẠP",
                new SubInput("Số lượng", ANY));
    }

    public void createFormSenditem(Player pl) {
        createForm(pl, SEND_ITEM, "SEND ITEM",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID item", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormSenditem1(Player pl) {
        createForm(pl, SEND_ITEM_OP, "SEND Vật Phẩm Option",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", NUMERIC),
                new SubInput("ID Option", NUMERIC),
                new SubInput("Param", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormSenditem2(Player pl) {
        createForm(pl, SEND_ITEM_SKH, "Buff SKH Option V2",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", NUMERIC),
                new SubInput("ID Option SKH 127 > 135", NUMERIC),
                new SubInput("ID Option Bonus", NUMERIC),
                new SubInput("Param", NUMERIC),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormSenditem3(Player pl) {
        createForm(pl, SEND_ITEM3, "Buff SKH Option V2",
                new SubInput("Tên người chơi", ANY),
                new SubInput("ID Trang Bị", NUMERIC),
                new SubInput("Chuỗi option", ANY),
                new SubInput("Số lượng", NUMERIC));
    }

    public void createFormBuffVND(Player player) {
        createForm(player, BUFFVND, "Buff VNĐ và Tổng Nạp",
                new SubInput("TK game người chơi", ANY),
                new SubInput("VNĐ CẦN BUFF", ANY),
                new SubInput("TỔNG NẠP CẦN BUFF", ANY));
    }

    public void createFormBuffToTalVND(Player player) {
        createForm(player, BUFFTOTALVND, "Buff  Tổng VNĐ",
                new SubInput("Tên người chơi", ANY),
                new SubInput("Sô tổng tiền cần buff", ANY));
    }

    public static class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }

    public void addItemGiftCodeToPlayer(Player p, final String giftcode) {
        try {
            final GirlkunResultSet red = GirlkunDB.executeQuery("SELECT * FROM `giftcode` WHERE `code` LIKE '" + Util.strSQL(giftcode) + "' LIMIT 1;");
            if (red.first()) {
                String text = "Mã quà tặng" + ": " + giftcode + "\b- " + "Phần quà của bạn là:" + "\b";
                final byte type = red.getByte("type");
                int limit = red.getInt("limit");
                final boolean isDelete = red.getBoolean("Delete");
                final boolean isCheckbag = red.getBoolean("bagCount");
                final JSONArray listUser = (JSONArray) JSONValue.parseWithException(red.getString("listUser"));
                final JSONArray listItem = (JSONArray) JSONValue.parseWithException(red.getString("listItem"));
                final JSONArray option = (JSONArray) JSONValue.parseWithException(red.getString("itemoption"));
                if (limit == 0) {
                    NpcService.gI().createTutorial(p, 24, "Số lượng mã quà tặng này đã hết.");
                } else {
                    if (type == 1) {
                        for (int i = 0; i < listUser.size(); ++i) {
                            final int playerId = Integer.parseInt(listUser.get(i).toString());
                            if (playerId == p.id) {
                                NpcService.gI().createTutorial(p, 24, "Mỗi tài khoản chỉ được phép sử dụng mã quà tặng này 1 lần duy nhất.");
                                return;
                            }
                        }
                    } else if (type == 2) {
                        if (!p.getSession().actived) { // Giả sử bạn có một hàm kiểm tra trạng thái mở thành viên
                            NpcService.gI().createTutorial(p, 24, "Bạn cần mở thành viên để có thể sử dụng code này.");
                            return;
                        }
                    }
                    if (isCheckbag && listItem.size() > InventoryServiceNew.gI().getCountEmptyBag(p)) {
                        NpcService.gI().createTutorial(p, 24, "Hành trang cần phải có ít nhất " + listItem.size() + " ô trống để nhận vật phẩm");
                    } else {
                        for (int i = 0; i < listItem.size(); ++i) {
                            final JSONObject item = (JSONObject) listItem.get(i);
                            final int idItem = Integer.parseInt(item.get("id").toString());
                            final int quantity = Integer.parseInt(item.get("quantity").toString());

                            if (idItem == -1) {
                                p.inventory.gold = Math.min(p.inventory.gold + (long) quantity, Inventory.LIMIT_GOLD);
                                text += quantity + " vàng\b";
                            } else if (idItem == -2) {
                                p.inventory.gem = Math.min(p.inventory.gem + quantity, 2000000000);
                                text += quantity + " ngọc\b";
                            } else if (idItem == -3) {
                                p.inventory.ruby = Math.min(p.inventory.ruby + quantity, 2000000000);
                                text += quantity + " ngọc khóa\b";
                            } else {
                                Item itemGiftTemplate = ItemService.gI().createNewItem((short) idItem);

                                itemGiftTemplate.quantity = quantity;
                                if (option != null) {
                                    for (int u = 0; u < option.size(); u++) {
                                        JSONObject jsonobject = (JSONObject) option.get(u);
                                        itemGiftTemplate.itemOptions.add(new Item.ItemOption(Integer.parseInt(jsonobject.get("id").toString()), Integer.parseInt(jsonobject.get("param").toString())));

                                    }

                                }
                                text += "x" + quantity + " " + itemGiftTemplate.template.name + "\b";
                                InventoryServiceNew.gI().addItemBag(p, itemGiftTemplate);
                                InventoryServiceNew.gI().sendItemBags(p);
                            }

                            if (i < listItem.size() - 1) {
                                text += "";
                            }
                        }
                        if (limit != -1) {
                            --limit;
                        }
                        listUser.add(p.id);
                        GirlkunDB.executeUpdate("UPDATE `giftcode` SET `limit` = " + limit + ", `listUser` = '" + listUser.toJSONString() + "' WHERE `code` LIKE '" + Util.strSQL(giftcode) + "';");
                        NpcService.gI().createTutorial(p, 24, text);
                    }
                }
            } else {
                NpcService.gI().createTutorial(p, 24, "Mã quà tặng không tồn tại hoặc đã được sử dụng");
            }
        } catch (Exception e) {

            NpcService.gI().createTutorial(p, 24, "Có lỗi xảy ra hãy báo ngay cho QTV để khắc phục.");
            e.printStackTrace();
        }
    }

}
