package nro.models.player;

import com.girlkun.database.GirlkunDB;
import nro.consts.mocdiem;
//import nro.database.GirlkunDB;
import nro.jdbc.daos.GodGK;
import nro.models.item.Item;
import nro.network.io.Message;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Archivement_diem {

    public String info1;
    public String info2;
    public short money;
    public boolean isFinish;
    public boolean isRecieve;
    public static Archivement_diem gI = null;

    public static Archivement_diem gI() {
        if (gI == null) {
            return new Archivement_diem();
        }
        return gI;
    }

    public Archivement_diem() {
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public short getMoney() {
        return money;
    }

    public void setMoney(short money) {
        this.money = money;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isRecieve() {
        return isRecieve;
    }

    public void setRecieve(boolean recieve) {
        isRecieve = recieve;
    }

    public Archivement_diem(String info1, String info2, short money, boolean isFinish, boolean isRecieve) {
        this.info1 = info1;
        this.info2 = info2;
        this.money = money;
        this.isFinish = isFinish;
        this.isRecieve = isRecieve;
    }

    public void Show(Player pl) {

        Message msg = null;
        try {
            msg = new Message(-76);
            msg.writer().writeByte(0); // action
            msg.writer().writeByte(pl.archivementList_diem.size());
            for (int i = 0; i < pl.archivementList_diem.size(); i++) {
                Archivement_diem archivement = pl.archivementList_diem.get(i);
                if (pl.getSession().version < 231 || pl.getSession().version > 235) {
                    msg.writer().writeUTF(archivement.getInfo1());
                    msg.writer().writeUTF(archivement.getInfo2());
                    msg.writer().writeShort(archivement.getMoney()); //money
                    msg.writer().writeBoolean(archivement.isFinish);
                    msg.writer().writeBoolean(archivement.isRecieve);

                } else {
                    msg.writer().writeUTF(archivement.getInfo1());
                    msg.writer().writeUTF(archivement.getInfo2());
                    msg.writer().writeShort(archivement.getMoney()); //money
                    msg.writer().writeUTF("");
                    msg.writer().writeBoolean(archivement.isFinish);
                    msg.writer().writeBoolean(archivement.isRecieve);
                    msg.writer().writeShort(10893);//res icon
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
            pl.typeRecvieArchiment = 2;
        } catch (IOException e) {

            e.getStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public boolean checktongnap(Player pl, int index) {
        if (index == 0 && pl.event.getEventPointMoc()>= 500) {
            return true;
        }
        if (index == 1 && pl.event.getEventPointMoc() >= 1000) {
            return true;
        }
        if (index == 2 && pl.event.getEventPointMoc() >= 2000) {
            return true;
        }
        if (index == 3 && pl.event.getEventPointMoc() >= 5000) {
            return true;
        }
        if (index == 4 && pl.event.getEventPointMoc() >= 20000) {
            return true;
        }
        if (index == 5 && pl.event.getEventPointMoc() >= 30000) {
            return true;
        }
        if (index == 6 && pl.event.getEventPointMoc() >= 40000) {
            return true;
        }
        if (index == 7 && pl.event.getEventPointMoc() >= 50000) {
            return true;
        }
        if (index == 8 && pl.event.getEventPointMoc() >= 100000) {
            return true;
        }

        return false;
    }

    public void receiveGem(int index, Player pl) {
        Archivement_diem temp = pl.archivementList_diem.get(index);
        if (temp.isRecieve) {
            Service.gI().sendThongBaoOK(pl, "Nhận rồi đừng nhận nữua");
            return;
        }
        if (temp != null) {
            Message msg = null;
            try {
                msg = new Message(-76);
                msg.writer().writeByte(1); // action
                msg.writer().writeByte(index); // index
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (IOException e) {
                Logger.logException(this.getClass(), e);
            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }

            pl.archivementList_diem.get(index).setRecieve(true);
            try {
                JSONArray dataArray = new JSONArray();

                for (Archivement_diem arr : pl.archivementList_diem) {
                    dataArray.add(arr.isRecieve ? "1" : "0");
                }
                String inventory = dataArray.toJSONString();
                dataArray.clear();
                GirlkunDB.executeUpdate("update player set mocsk20_10 = ? where name = ?", inventory, pl.name);
                nhanQua(pl, index);
                System.out.println("Player " + pl.name + " Nhận quà thành công");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Service.gI().sendThongBao(pl, "Nhận thành công, vui lòng kiểm tra hòm thư ");
        } else {
            Service.gI().sendThongBao(pl, "Không có phần thưởng");
        }
    }

    public static final String[] Textmocdiem = new String[]{
        "|3|Mốc 10K điểm:"
        + "x" + mocdiem.sli10_1 + " " + ItemService.gI().getTemplate(mocdiem.i10_1).name + ",x" + mocdiem.sli10_2 + " " + ItemService.gI().getTemplate(mocdiem.i10_2).name + "\n"
        + "|2|Mốc 20k điểm:"
        + " x" + mocdiem.sli20_1 + " " + ItemService.gI().getTemplate(mocdiem.i20_1).name + ", " + "x" + mocdiem.sli20_2 + " Bộ ngọc rồng\n"
        + "|1|Mốc 30K điểm:"
        + " x" + mocdiem.sli30_1 + " " + ItemService.gI().getTemplate(mocdiem.i30_1).name + ", " + "x" + mocdiem.sli30_2 + " " + ItemService.gI().getTemplate(mocdiem.i30_2).name + "\n"
        + "|4|Mốc 50K điểm: "
        + "x" + mocdiem.sli50_1 + " " + ItemService.gI().getTemplate(mocdiem.i50_1).name + ", " + ItemService.gI().getTemplate(mocdiem.i50_2).name + " " + mocdiem.csi50_2 + "% cs\n"
        + "|6|Mốc 70K điểm: "
        + " x" + mocdiem.sli70_1 + " " + ItemService.gI().getTemplate(mocdiem.i70_1).name + " " + mocdiem.csi70_1 + "% cs, " + "x" + mocdiem.sli70_2 + " " + ItemService.gI().getTemplate(mocdiem.i70_2).name + ", " + ItemService.gI().getTemplate(mocdiem.i70_3).name + ", " + ItemService.gI().getTemplate(mocdiem.i70_4).name + "\n"
        + "|5|Mốc 100K điểm: "
        + " x" + mocdiem.sli100_1 + " " + ItemService.gI().getTemplate(mocdiem.i100_1).name + ", x" + mocdiem.sli100_2 + " " + ItemService.gI().getTemplate(mocdiem.i100_2).name + ", x" + mocdiem.sli100_3 + " " + ItemService.gI().getTemplate(mocdiem.i100_3).name + "\n"
        + "|7|Mốc 200K điểm: "
        + " x" + mocdiem.sli200_1 + " " + ItemService.gI().getTemplate(mocdiem.i200_1).name + " " + mocdiem.csi200_1 + "% cs, " + "x" + mocdiem.sli200_2 + " " + ItemService.gI().getTemplate(mocdiem.i200_2).name + ", x" + mocdiem.sli200_3 + " " + ItemService.gI().getTemplate(mocdiem.i200_3).name + "\n"
        + "|7|Mốc 300K điểm: "
        + " x" + mocdiem.sli300_1 + " " + ItemService.gI().getTemplate(mocdiem.i300_1).name + ", " + "x" + mocdiem.sli300_2 + " " + ItemService.gI().getTemplate(mocdiem.i300_2).name + ", x" + mocdiem.sli300_3 + " " + ItemService.gI().getTemplate(mocdiem.i300_3).name + "\n"
        + "\b|7|Mốc 500K điểm: "
        + " x" + mocdiem.sli500_1 + " " + ItemService.gI().getTemplate(mocdiem.i500_1).name + " " + mocdiem.csi500_1 + "% cs, " + mocdiem.csi500_2 + "% sđ đẹp, x" + mocdiem.sli500_2 + " " + ItemService.gI().getTemplate(mocdiem.i500_2).name + ", x" + mocdiem.sli500_3 + " " + ItemService.gI().getTemplate(mocdiem.i500_3).name + ", x" + mocdiem.sli500_4 + " " + ItemService.gI().getTemplate(mocdiem.i500_4).name + "\n"
        + "\b|7|Mốc 1M điểm: "
        + " x" + mocdiem.sli1000_1 + " " + ItemService.gI().getTemplate(mocdiem.i1000_1).name + ", " + "x" + mocdiem.sli1000_2 + " " + ItemService.gI().getTemplate(mocdiem.i1000_2).name + ", x" + mocdiem.sli1000_3 + " " + ItemService.gI().getTemplate(mocdiem.i1000_3).name + ", x" + mocdiem.sli1000_4 + " " + ItemService.gI().getTemplate(mocdiem.i1000_4).name + "\n"

    };

    private void nhanQua(Player pl, int index) {

        int ql = 0;
        int ql1 = 0;
        int ql2 = 0;
        int ql3 = 0;
        int ql4 = 0;
//        int ql5 = 0;
        nro.models.item.Item item = null;
        nro.models.item.Item item1 = null;
        nro.models.item.Item item2 = null;
        nro.models.item.Item item3 = null;
        nro.models.item.Item item4 = null;

        switch (index) {
            case 0://10000
//                ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) mocdiem.i10_1);

                item.quantity = mocdiem.sli10_1;
                item.itemOptions.add(new Item.ItemOption(93, 10));
                item.itemOptions.add(new Item.ItemOption(30, 1));
//                ______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i10_2);
                item2.quantity = mocdiem.sli10_2;
                item2.itemOptions.add(new Item.ItemOption(93, 10));
                item2.itemOptions.add(new Item.ItemOption(30, 1));

                pl.inventory.itemsMailBox.add(item);
                pl.inventory.itemsMailBox.add(item2);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

                break;
            case 1://20000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) mocdiem.i20_1);

                item.quantity = mocdiem.sli20_1;
                item.itemOptions.add(new Item.ItemOption(30, 1));
                //______________Item 2_______________
                short[] getallnr = {14, 15, 16, 17, 18, 19, 20};
                for (short nr : getallnr) {
                    item2 = ItemService.gI().createNewItem((short) nr);
                    item2.itemOptions.add(new Item.ItemOption(30, 1));
                    item2.quantity = mocdiem.sli20_2;
                    item2.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item2);
                }
                //                ______________Item 2_______________

                pl.inventory.itemsMailBox.add(item);
                pl.inventory.itemsMailBox.add(item2);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

                break;
            case 2://30000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) mocdiem.i30_1);

                item.quantity = mocdiem.sli30_1;
                item.itemOptions.add(new Item.ItemOption(93, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));

                //______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i30_2);

                item2.quantity = mocdiem.sli30_2;
                item2.itemOptions.add(new Item.ItemOption(93, 15));
                item2.itemOptions.add(new Item.ItemOption(30, 1));

                //                ______________Item 2_______________
                pl.inventory.itemsMailBox.add(item);
                pl.inventory.itemsMailBox.add(item2);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

                break;
            case 3://50000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) (mocdiem.i50_1));

                item.quantity = mocdiem.sli50_1;
                item.itemOptions.add(new Item.ItemOption(30, 1));

                //______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i50_2);

                item2.quantity = mocdiem.sli50_2;
                item2.itemOptions.add(new Item.ItemOption(30, 1));
                item2.itemOptions.add(new Item.ItemOption(50, mocdiem.csi50_2));
                item2.itemOptions.add(new Item.ItemOption(77, mocdiem.csi50_2));
                item2.itemOptions.add(new Item.ItemOption(103, mocdiem.csi50_2));
                item2.itemOptions.add(new Item.ItemOption(5, mocdiem.csi50_2));
                item2.itemOptions.add(new Item.ItemOption(30, 1));

                pl.inventory.itemsMailBox.add(item);
                pl.inventory.itemsMailBox.add(item2);
//           
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

                break;
            case 4://70000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) mocdiem.i70_1);

                item.quantity = mocdiem.sli70_1;
                item.itemOptions.add(new Item.ItemOption(30, 1));
                item.itemOptions.add(new Item.ItemOption(0, mocdiem.csi70_1));
                //______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i70_2);
                item3 = ItemService.gI().createNewItem((short) mocdiem.i70_3);
                item4 = ItemService.gI().createNewItem((short) mocdiem.i70_4);

                item2.itemOptions.add(new Item.ItemOption(30, 1));
                item3.itemOptions.add(new Item.ItemOption(30, 1));
                item4.itemOptions.add(new Item.ItemOption(30, 1));
                item2.quantity = mocdiem.sli70_2;
                item3.quantity = mocdiem.sli70_3;
                item4.quantity = mocdiem.sli70_4;

                pl.inventory.itemsMailBox.add(item);
                pl.inventory.itemsMailBox.add(item2);
                pl.inventory.itemsMailBox.add(item3);
                pl.inventory.itemsMailBox.add(item4);
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

                break;
            case 5://100000

                //______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i100_1);

                item2.quantity = mocdiem.sli100_1;
                item2.itemOptions.add(new Item.ItemOption(30, 1));
                //                ______________Item 2_______________
                item3 = ItemService.gI().createNewItem((short) mocdiem.i100_2);
                item3.itemOptions.add(new Item.ItemOption(30, 1));
                item3.quantity = mocdiem.sli100_2;
                //                ______________Item 2_______________
                item4 = ItemService.gI().createNewItem((short) mocdiem.i100_3);
                item4.itemOptions.add(new Item.ItemOption(30, 1));
                item4.quantity = mocdiem.sli100_3;

                pl.inventory.itemsMailBox.add(item2);
                pl.inventory.itemsMailBox.add(item3);
                pl.inventory.itemsMailBox.add(item4);
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

                break;

            case 6://200000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) mocdiem.i200_1);

                item.quantity = mocdiem.sli200_1;
                item.itemOptions.add(new Item.ItemOption(30, 1));
                item.itemOptions.add(new Item.ItemOption(50, mocdiem.csi200_1));
                item.itemOptions.add(new Item.ItemOption(77, mocdiem.csi200_1));
                item.itemOptions.add(new Item.ItemOption(103, mocdiem.csi200_1));
                item.itemOptions.add(new Item.ItemOption(5, mocdiem.csi200_1));
                //______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i200_2);
                item2.itemOptions.add(new Item.ItemOption(30, 1));
                item2.quantity = mocdiem.sli200_2;
                //                ______________Item 2_______________
                item3 = ItemService.gI().createNewItem((short) mocdiem.i200_3);
                item3.itemOptions.add(new Item.ItemOption(30, 1));
                item3.quantity = mocdiem.sli200_3;
                //                ______________Item 2_______________

                pl.inventory.itemsMailBox.add(item);
                pl.inventory.itemsMailBox.add(item2);
                pl.inventory.itemsMailBox.add(item3);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 7://300000

                //______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i300_1);
                item2.itemOptions.add(new Item.ItemOption(30, 1));
                item2.quantity = mocdiem.sli300_1;
                //                ______________Item 2_______________
                item3 = ItemService.gI().createNewItem((short) mocdiem.i300_2);
                item3.itemOptions.add(new Item.ItemOption(30, 1));
                item3.quantity = mocdiem.sli300_2;
                //                ______________Item 2_______________
                item4 = ItemService.gI().createNewItem((short) mocdiem.i300_3);
                item4.itemOptions.add(new Item.ItemOption(30, 1));
                item4.quantity = mocdiem.sli300_3;

                pl.inventory.itemsMailBox.add(item2);
                pl.inventory.itemsMailBox.add(item3);
                pl.inventory.itemsMailBox.add(item4);
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

                break;
            case 8:
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) mocdiem.i500_1);

                item.quantity = mocdiem.sli500_1;
                item.itemOptions.add(new Item.ItemOption(30, 1));
                item.itemOptions.add(new Item.ItemOption(50, mocdiem.csi500_1));
                item.itemOptions.add(new Item.ItemOption(77, mocdiem.csi500_1));
                item.itemOptions.add(new Item.ItemOption(103, mocdiem.csi500_1));
                item.itemOptions.add(new Item.ItemOption(5, mocdiem.csi500_1));
                item.itemOptions.add(new Item.ItemOption(117, mocdiem.csi500_2));
                //______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i500_2);
                item2.itemOptions.add(new Item.ItemOption(30, 1));
                item2.quantity = mocdiem.sli500_2;
                //                ______________Item 2_______________
                item3 = ItemService.gI().createNewItem((short) mocdiem.i500_3);
                item3.itemOptions.add(new Item.ItemOption(30, 1));
                item3.quantity = mocdiem.sli500_3;
                //                ______________Item 2_______________
                item4 = ItemService.gI().createNewItem((short) mocdiem.i500_4);
                item4.itemOptions.add(new Item.ItemOption(30, 1));
                item4.quantity = mocdiem.sli500_4;

                pl.inventory.itemsMailBox.add(item);
                pl.inventory.itemsMailBox.add(item2);
                pl.inventory.itemsMailBox.add(item3);
                pl.inventory.itemsMailBox.add(item4);
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

                break;
            case 9:
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) mocdiem.i1000_1);
                item.itemOptions.add(new Item.ItemOption(30, 1));
                item.quantity = mocdiem.sli1000_1;

                //______________Item 2_______________
                item2 = ItemService.gI().createNewItem((short) mocdiem.i1000_2);
                item2.itemOptions.add(new Item.ItemOption(30, 1));
                item2.quantity = mocdiem.sli1000_2;
                //                ______________Item 2_______________
                item3 = ItemService.gI().createNewItem((short) mocdiem.i1000_3);
                item3.itemOptions.add(new Item.ItemOption(30, 1));
                item3.quantity = mocdiem.sli1000_3;
                //                ______________Item 2_______________
                item4 = ItemService.gI().createNewItem((short) mocdiem.i1000_4);
                item4.itemOptions.add(new Item.ItemOption(30, 1));
                item4.quantity = mocdiem.sli1000_4;

                pl.inventory.itemsMailBox.add(item);
                pl.inventory.itemsMailBox.add(item2);
                pl.inventory.itemsMailBox.add(item3);
                pl.inventory.itemsMailBox.add(item4);
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }

        }
        InventoryServiceNew
                .gI()
                .sendItemBags(pl);
        Service.gI()
                .sendMoney(pl);
    }

    public void getAchievement_mocsk20_10(Player player) {
        try {
            if (player.getSession() == null) {
                return;
            }

            Connection con = null;
            PreparedStatement ps = null;
            JSONValue jv = new JSONValue();
            JSONArray dataArray = null;
            JSONArray dataArrayTemp = null;
            con = GirlkunDB.getConnection();
            ps = con.prepareStatement("SELECT `mocsk20_10` FROM `player` WHERE name = ? LIMIT 1");
            ps.setString(1, player.name);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String achievementData = rs.getString(1);
                    try {
                        dataArray = (JSONArray) jv.parse(achievementData);
                        if (dataArray != null && dataArray.size() != 9) {
                            if (dataArray.size() < 9) {
                                for (int j = dataArray.size(); j < 9; j++) {
                                    dataArray.add(0);
                                }
                            }

                            while (dataArray.size() > 9) {

                                dataArray.remove(9);

                            }

                        }
                        player.archivementList_diem.clear();
                        if (dataArray != null) {
                            for (int i = 0; i < dataArray.size(); i++) {
                                try {
                                    Archivement_diem achievement = new Archivement_diem();

                                    achievement.setInfo1("Mốc điểm " + getNhiemVu(i));
                                    achievement.setInfo2("Điểm bản thân: " + getNhiemVu2(player, i) + "/" + getNhiemVu(i));
                                    achievement.setFinish(checktongnap(player, i));
                                    achievement.setMoney((short) getRuby(i));
                                    achievement.setRecieve(Integer.parseInt(String.valueOf(dataArray.get(i))) != 0);

                                    player.archivementList_diem.add(achievement);
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                    return;
                                }
                            }

                        }
                        dataArray.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Player: " + player.name + " dang xem moc diem");
                Show(player);
                rs.close();
                ps.close();
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNhiemVu(int index) {
        switch (index) {
            case 0:
                return "500 điểm";
            case 1:
                return "1000 điểm";
            case 2:
                return "2000 điểm";
            case 3:
                return "5000 điểm";
            case 4:
                return "20000 điểm";
            case 5:
                return "30000 điểm";
            case 6:
                return "40000 điểm";
            case 7:
                return "50000 điểm";
            case 8:
                return "100000 điểm";

            default:
                return "";
        }
    }

    public String getNhiemVu2(Player player, int index) {
        switch (index) {
            case 0:
                return " " + player.event.getEventPointMoc() + "";
            case 1:
                return " " + player.event.getEventPointMoc() + "";
            case 2:
                return " " + player.event.getEventPointMoc() + "";
            case 3:
                return " " + player.event.getEventPointMoc() + "";
            case 4:
                return " " + player.event.getEventPointMoc() + "";
            case 5:
                return " " + player.event.getEventPointMoc() + "";
            case 6:
                return " " + player.event.getEventPointMoc() + "";
            case 7:
                return " " + player.event.getEventPointMoc() + "";
            case 8:
                return " " + player.event.getEventPointMoc() + "";
//            case 9:
//                  return " " + player.event.getEventPointMoc() + "";
//            case 10:
//                 return " " + player.event.getEventPointMoc() + "";
            default:
                return "";
        }
    }

    public int getRuby(int index) {
        switch (index) {
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 0;
            case 4:
                return 0;
            case 5:
                return 0;
            case 6:
                return 0;
            case 7:
                return 0;
            case 8:
                return 0;
//            case 9:
//                return 0;

            default:
                return -1;
        }
    }
}
