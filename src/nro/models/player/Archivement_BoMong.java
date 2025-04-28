package nro.models.player;

import com.girlkun.database.GirlkunDB;
import nro.consts.mocnap_1;
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

public class Archivement_BoMong {

    public String info1;
    public String info2;
    public short money;
    public boolean isFinish;
    public boolean isRecieve;
    public static Archivement_BoMong gI = null;

    public static Archivement_BoMong gI() {
        if (gI == null) {
            return new Archivement_BoMong();
        }
        return gI;
    }

    public Archivement_BoMong() {
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

    public Archivement_BoMong(String info1, String info2, short money, boolean isFinish, boolean isRecieve) {
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
            msg.writer().writeByte(pl.archivementList_bomong.size());
            for (int i = 0; i < pl.archivementList_bomong.size(); i++) {

                Archivement_BoMong archivement = pl.archivementList_bomong.get(i);
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
                    msg.writer().writeShort(10895);//res icon
                }

            }
            pl.sendMessage(msg);
            msg.cleanup();
            pl.typeRecvieArchiment = 0;
        } catch (IOException e) {

            e.getStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void receiveGem(int index, Player pl) {
        Archivement_BoMong temp = pl.archivementList_bomong.get(index);
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

            pl.archivementList_bomong.get(index).setRecieve(true);
            try {
                JSONArray dataArray = new JSONArray();

                for (Archivement_BoMong arr : pl.archivementList_bomong) {
                    dataArray.add(arr.isRecieve ? "1" : "0");
                }
                String inventory = dataArray.toJSONString();
                dataArray.clear();
                GirlkunDB.executeUpdate("update player set Achievement_BoMong = ? where name = ?", inventory, pl.name);
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

    private void nhanQua(Player pl, int index) {

        nro.models.item.Item item = null;
        switch (index) {
            case 0://10000
//             // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1572);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(0, 2500));
                item.itemOptions.add(new Item.ItemOption(101, 50));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 1://20000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1573);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(50, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 2://30000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1589);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(77, 25));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 3://50000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1579);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(103, 25));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 4://70000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1583);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(50, 10));
                item.itemOptions.add(new Item.ItemOption(77, 10));
                item.itemOptions.add(new Item.ItemOption(103, 10));
                item.itemOptions.add(new Item.ItemOption(14, 10));
                item.itemOptions.add(new Item.ItemOption(5, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 5://100000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1573);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(50, 10));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;

            case 6://200000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1576);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(50, 10));
                item.itemOptions.add(new Item.ItemOption(77, 10));
                item.itemOptions.add(new Item.ItemOption(103, 10));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 7://300000
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1575);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(50, 12));
                item.itemOptions.add(new Item.ItemOption(77, 12));
                item.itemOptions.add(new Item.ItemOption(103, 12));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 8:
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1574);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(50, 14));
                item.itemOptions.add(new Item.ItemOption(77, 14));
                item.itemOptions.add(new Item.ItemOption(103, 14));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 9:
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1564);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(50, 19));
                item.itemOptions.add(new Item.ItemOption(77, 19));
                item.itemOptions.add(new Item.ItemOption(103, 19));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;
            case 10:
                // ______________Item 1_______________
                item = ItemService.gI().createNewItem((short) 1578);

                item.quantity = 1;
                item.itemOptions.add(new Item.ItemOption(50, 20));
                item.itemOptions.add(new Item.ItemOption(77, 20));
                item.itemOptions.add(new Item.ItemOption(103, 20));
                item.itemOptions.add(new Item.ItemOption(30, 1));
                pl.inventory.itemsMailBox.add(item);

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
                break;

        }
        InventoryServiceNew
                .gI()
                .sendItemBags(pl);
        Service.gI()
                .sendMoney(pl);
    }

    public void getAchievement(Player player) {
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
            ps = con.prepareStatement("SELECT `Achievement_BoMong` FROM `player` WHERE name = ? LIMIT 1");
            ps.setString(1, player.name);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String achievementData = rs.getString(1);
                    try {
                        dataArray = (JSONArray) jv.parse(achievementData);
                        if (dataArray != null && dataArray.size() != 11) {
                            if (dataArray.size() < 11) {
                                for (int j = dataArray.size(); j < 11; j++) {
                                    dataArray.add(0);
                                }
                            }

                            while (dataArray.size() > 11) {

                                dataArray.remove(11);

                            }

                        }
                        player.archivementList_bomong.clear();
                        if (dataArray != null) {

                            for (int i = 0; i < dataArray.size(); i++) {
                                try {
                                    Archivement_BoMong achievement = new Archivement_BoMong();
//                                    achievement.setInfo1("Mốc nhiệm vụ ");
                                    achievement.setInfo1("" + getInfoTask(i));
                                    achievement.setInfo2(getDone(player, i) + "/" + getNhiemVu(i));
                                    achievement.setFinish(checktongnap(player, i));
                                    achievement.setMoney((short) getRuby(i));
                                    achievement.setRecieve(Integer.parseInt(String.valueOf(dataArray.get(i))) != 0);
                                    player.archivementList_bomong.add(achievement);

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
                System.out.println("Player: " + player.name + " dang xem nv bo mong");
                Show(player);
                rs.close();
                ps.close();
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getInfoTask(int index) {
        switch (index) {
            case 0:
                return "Nông dân chính hiệu";
            case 1:
                return "Trùm Cuối";
            case 2:
                return "Bất Phục";
            case 3:
                return "Phong Ba";
            case 4:
                return "Thần Thoại";
            case 5:
                return "Cày cuốc";
            case 6:
                return "Dân chơi +6";
            case 7:
                return "Dân chơi +7";
            case 8:
                return "Dân chơi +8";
            case 9:
                return "Ước rồng 100 lần";
            case 10:
                return "Đại gia";
            default:
                return "";
        }
    }

    public boolean checktongnap(Player pl, int index) {
        if (index == 0 && pl.event.getEventPointQuai() >= 10000) {
            return true;
        }
        if (index == 1 && pl.nPoint.dame >= 20000000) {
            return true;
        }
        if (index == 2 && pl.nPoint.hpMax >= 100000000) {
            return true;
        }
        if (index == 3 && pl.nPoint.mpMax >= 100000000) {
            return true;
        }
        if (index == 4 && pl.event.getEventPointBoss() >= 1000) {
            return true;
        }
        if (index == 5 && pl.event.getEventPointNHS() >= 2000) {
            return true;
        }
        if (index == 6 && (pl.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && item.isdo6s()).limit(5).count() == 5)) {
            return true;
        }
        if (index == 7 && (pl.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && item.isdo7s()).limit(5).count() == 5)) {
            return true;
        }
        if (index == 8 && (pl.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && item.isdo8s()).limit(5).count() == 5)) {
            return true;
        }
        if (index == 9 && pl.getSession().totalvnd2 >= 5000000) {
            return true;
        }
        if (index == 10 && InventoryServiceNew.gI().findItemBag(pl, 457) != null
                && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 500000) {
            return true;
        }

        return false;
    }

    public String getNhiemVu(int index) {
        switch (index) {
            case 0:
                return "10000[con]";
            case 1:
                return "20000000[Sức đánh]";
            case 2:
                return "100000000[HP]";
            case 3:
                return "100000000[MP]";
            case 4:
                return "1000[BOSS]";
            case 5:
                return "2000[QUÁI NGŨ HÀNH SƠN]";
            case 6:
                return "5[MÓN ÉP SPL +6]";
            case 7:
                return "5[MÓN ÉP SPL +7]";
            case 8:
                return "5[MÓN ÉP SPL +8]";
            case 9:
                return "100[LẦN]";
            case 10:
                return "500000[THỎI VÀNG]";
            default:
                return "";
        }
    }

    public String getDone(Player pl, int index) {
        switch (index) {
            case 0:
                return "" + pl.event.getEventPointQuai() + "";
            case 1:
                return "" + pl.nPoint.dame + "";
            case 2:
                return "" + pl.nPoint.hpMax + "";
            case 3:
                return "" + pl.nPoint.mpMax + "";
            case 4:
                return "" + pl.event.getEventPointBoss() + "";
            case 5:
                return "" + pl.event.getEventPointNHS() + "";
            case 6:
                return "Cần mặc";
            case 7:
                return "Cần mặc";
            case 8:
                return "Cần mặc";
            case 9:
                return "" + 100 + "";
            case 10:
                return InventoryServiceNew.gI().findItemBag(pl, 457) != null ? "" + InventoryServiceNew.gI().findItemBag(pl, 457).quantity + "" : "Không có";
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
            case 9:
                return 0;
            case 10:
                return 0;

            default:
                return -1;
        }
    }
}
