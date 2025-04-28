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

public class Archivement {

    public String info1;
    public String info2;
    public short money;
    public boolean isFinish;
    public boolean isRecieve;
    public static Archivement gI = null;
    public static int GIADOLACHIADOI = 10000;

    public static Archivement gI() {
        if (gI == null) {
            return new Archivement();
        }
        return gI;
    }

    public Archivement() {
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

    public Archivement(String info1, String info2, short money, boolean isFinish, boolean isRecieve) {
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
            msg.writer().writeByte(pl.archivementList.size());
            for (int i = 0; i < pl.archivementList.size(); i++) {

                Archivement archivement = pl.archivementList.get(i);
                if (pl.getSession().version <= 231 || pl.getSession().version > 235) {
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
            pl.typeRecvieArchiment = 1;
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
        if (index == 0 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 1) {
            return true;
        }
        if (index == 1 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 2) {
            return true;
        }
        if (index == 2 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 3) {
            return true;
        }
        if (index == 3 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 5) {
            return true;
        }
        if (index == 4 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 7) {
            return true;
        }
        if (index == 5 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 10) {
            return true;
        }
        if (index == 6 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 20) {
            return true;
        }
        if (index == 7 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 30) {
            return true;
        }
        if (index == 8 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 50) {
            return true;
        }
        if (index == 9 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 100) {
            return true;
        }
        if (index == 10 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 200) {
            return true;
        }
        if (index == 11 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 300) {
            return true;
        }
        if (index == 12 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 500) {
            return true;
        }
        if (index == 13 && pl.getSession().totalvnd2 >= GIADOLACHIADOI * 700) {
            return true;
        }
        return false;
    }

    public void receiveGem(int index, Player pl) {
        Archivement temp = pl.archivementList.get(index);
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
            } catch (IOException e) {   e.printStackTrace();
                Logger.logException(this.getClass(), e);
            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }

            pl.archivementList.get(index).setRecieve(true);
            try {
                JSONArray dataArray = new JSONArray();

                for (Archivement arr : pl.archivementList) {
                    dataArray.add(arr.isRecieve ? "1" : "0");
                }
                String inventory = dataArray.toJSONString();
                dataArray.clear();
                GirlkunDB.executeUpdate("update player set Achievement = ? where id = ?", inventory, pl.id);
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

        int ql = 0;
//        int ql5 = 0;
        nro.models.item.Item item = null;

        switch (index) {
            case 0://10000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i10_1);
                    item.quantity = mocnap_1.sli10_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i10_2);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli10_2;
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 1://20000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i20_1);
                    item.quantity = mocnap_1.sli20_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i20_2);
                    item.quantity = mocnap_1.sli20_2;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 2://30000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i30_1);
                    item.quantity = mocnap_1.sli30_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i30_2);
                    item.quantity = mocnap_1.sli30_2;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 3://50000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i50_1);
                    item.quantity = mocnap_1.sli50_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i50_2);
                    item.quantity = mocnap_1.sli50_2;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 4://70000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i70_1);
                    item.quantity = mocnap_1.sli70_1;
                    item.itemOptions.add(new Item.ItemOption(50, mocnap_1.csi70_1));
                    item.itemOptions.add(new Item.ItemOption(77, mocnap_1.csi70_1));
                    item.itemOptions.add(new Item.ItemOption(103, mocnap_1.csi70_1));
                    item.itemOptions.add(new Item.ItemOption(114, 30));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i70_2);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli70_2;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i70_3);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli70_3;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i70_4);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli70_4;
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 5://100000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i100_1);
                    item.quantity = mocnap_1.sli100_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i100_2);
                    item.quantity = mocnap_1.sli100_2;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i100_3);
                    item.quantity = mocnap_1.sli100_3;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;

            case 6://200000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i200_1);
                    item.quantity = mocnap_1.sli200_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i200_2);
                    item.quantity = mocnap_1.sli200_2;
                    item.itemOptions.add(new Item.ItemOption(50, mocnap_1.csi200_2));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i200_3);
                    item.quantity = mocnap_1.sli200_3;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 7://300000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i300_1);
                    item.quantity = mocnap_1.sli300_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i300_2);
                    item.quantity = mocnap_1.sli300_2;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i300_3);
                    item.quantity = mocnap_1.sli300_3;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            case 8: //500000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i500_1);
                    item.quantity = mocnap_1.sli500_1;
                    item.itemOptions.add(new Item.ItemOption(50, mocnap_1.sdi500_1));
                    item.itemOptions.add(new Item.ItemOption(77, mocnap_1.hpi500_1));
                    item.itemOptions.add(new Item.ItemOption(103, mocnap_1.kii500_1));
                    item.itemOptions.add(new Item.ItemOption(5, mocnap_1.sdcmi500_1));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i500_2);
                    item.quantity = mocnap_1.sli500_2;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i500_3);
                    item.quantity = mocnap_1.sli500_3;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i500_4);
                    item.itemOptions.add(new Item.ItemOption(50, mocnap_1.csi500_4));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli500_4;
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 9: //1000000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i1000_1);

                    item.quantity = mocnap_1.sli1000_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i1000_2);
                    item.quantity = mocnap_1.sli1000_2;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i1000_3);
                    item.itemOptions.add(new Item.ItemOption(50, mocnap_1.sdi1000_3));
                    item.itemOptions.add(new Item.ItemOption(77, mocnap_1.hpi1000_3));
                    item.itemOptions.add(new Item.ItemOption(103, mocnap_1.kii1000_3));
                    item.itemOptions.add(new Item.ItemOption(5, mocnap_1.sdcmi1000_3));
                    item.itemOptions.add(new Item.ItemOption(95, 10));
                    item.itemOptions.add(new Item.ItemOption(96, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli1000_3;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i1000_4);
                    item.quantity = mocnap_1.sli1000_4;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i1000_5);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli1000_5;

                    pl.inventory.itemsMailBox.add(item);
                }

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 10: //2000000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i2000_1);

                    item.quantity = mocnap_1.sli2000_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i2000_2);
                    item.quantity = mocnap_1.sli2000_2;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i2000_3);
                    item.itemOptions.add(new Item.ItemOption(50, mocnap_1.sdi2000_3));
                    item.itemOptions.add(new Item.ItemOption(77, mocnap_1.hpi2000_3));
                    item.itemOptions.add(new Item.ItemOption(103, mocnap_1.kii2000_3));
                    item.itemOptions.add(new Item.ItemOption(5, mocnap_1.sdcmi2000_3));
                    item.itemOptions.add(new Item.ItemOption(95, 10));
                    item.itemOptions.add(new Item.ItemOption(96, 10));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli2000_3;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i2000_4);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli2000_4;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i2000_5);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli2000_5;
                    pl.inventory.itemsMailBox.add(item);
                }

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 11: //3000000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i3000_1);

                    item.quantity = mocnap_1.sli3000_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i3000_2);
                    item.quantity = mocnap_1.sli3000_2;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i3000_3);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli3000_3;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i3000_4);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli3000_4;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i3000_5);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli3000_5;
                    pl.inventory.itemsMailBox.add(item);
                }

                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 12://5000000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i5000_1);

                    item.quantity = mocnap_1.sli5000_1;
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i5000_2);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli5000_2;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i5000_3);
                    item.itemOptions.add(new Item.ItemOption(50, mocnap_1.sdi5000_3));
                    item.itemOptions.add(new Item.ItemOption(77, mocnap_1.hpi5000_3));
                    item.itemOptions.add(new Item.ItemOption(103, mocnap_1.kii5000_3));
                    item.itemOptions.add(new Item.ItemOption(5, mocnap_1.sdcmi5000_3));
                    item.itemOptions.add(new Item.ItemOption(117, mocnap_1.sddepi5000_3));
                    item.itemOptions.add(new Item.ItemOption(95, 15));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli5000_3;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i5000_4);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli5000_4;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i5000_5);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli5000_5;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i5000_6);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli5000_6;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i5000_7);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli5000_7;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i5000_8);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli5000_8;
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
            }
            break;
            case 13://7000000
            {
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i7000_1);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli7000_1;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i7000_2);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli7000_2;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i7000_3);
                    item.itemOptions.add(new Item.ItemOption(50, mocnap_1.sdi7000_3));
                    item.itemOptions.add(new Item.ItemOption(77, mocnap_1.sdi7000_3));
                    item.itemOptions.add(new Item.ItemOption(103, mocnap_1.sdi7000_3));
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli5000_3;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i7000_4);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli7000_4;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i7000_5);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli7000_5;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i7000_6);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli7000_6;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i7000_7);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli7000_7;
                    pl.inventory.itemsMailBox.add(item);
                }
                {
                    item = ItemService.gI().createNewItem((short) mocnap_1.i7000_8);
                    item.itemOptions.add(new Item.ItemOption(30, 1));
                    item.quantity = mocnap_1.sli7000_8;
                    pl.inventory.itemsMailBox.add(item);
                }
                if (GodGK.updateMailBox(pl)) {
                    Service.gI().sendThongBao(pl, "Bạn vừa nhận quà về mail thành công ");
                }
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
            ps = con.prepareStatement("SELECT `Achievement` FROM `player` WHERE id = ? LIMIT 1");
            ps.setInt(1, (int) player.id);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String achievementData = rs.getString(1);
                    try {
                        dataArray = (JSONArray) jv.parse(achievementData);
                        if (dataArray != null && dataArray.size() != 14) {
                            if (dataArray.size() < 14) {
                                for (int j = dataArray.size(); j < 14; j++) {
                                    dataArray.add(0);
                                }
                            }

                            while (dataArray.size() > 14) {

                                dataArray.remove(14);

                            }

                        }
                        player.archivementList.clear();
                        if (dataArray != null) {

                            for (int i = 0; i < dataArray.size(); i++) {
                                try {
                                    Archivement achievement = new Archivement();
//                                    achievement.setInfo1("Mốc nạp ");
                                    achievement.setInfo1("Mốc nạp " + getNhiemVu(i));
                                    achievement.setInfo2("Đã nạp: " + getNhiemVu2(player, i) + "/" + getNhiemVu(i));
                                    achievement.setFinish(checktongnap(player, i));
                                    achievement.setMoney((short) getRuby(i));
                                    achievement.setRecieve(Integer.parseInt(String.valueOf(dataArray.get(i))) != 0);
                                    player.archivementList.add(achievement);

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
                System.out.println("Player: " + player.name + " dang xem moc nap");
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
                return "" + GIADOLACHIADOI * 1;
            case 1:
                return "" + GIADOLACHIADOI * 2;
            case 2:
                return "" + GIADOLACHIADOI * 3;
            case 3:
                return "" + GIADOLACHIADOI * 5;
            case 4:
                return "" + GIADOLACHIADOI * 7;
            case 5:
                return "" + GIADOLACHIADOI * 10;
            case 6:
                return "" + GIADOLACHIADOI * 20;
            case 7:
                return "" + GIADOLACHIADOI * 30;
            case 8:
                return "" + GIADOLACHIADOI * 50;
            case 9:
                return "" + GIADOLACHIADOI * 100;
            case 10:
                return "" + GIADOLACHIADOI * 200;
            case 11:
                return "" + GIADOLACHIADOI * 300;
            case 12:
                return "" + GIADOLACHIADOI * 500;
            case 13:
                return "" + GIADOLACHIADOI * 700;
            default:
                return "";
        }
    }

    public String getNhiemVu2(Player player, int index) {
        switch (index) {
            case 0:
                return " " + player.getSession().totalvnd2 + "";
            case 1:
                return " " + player.getSession().totalvnd2 + "";
            case 2:
                return " " + player.getSession().totalvnd2 + "";
            case 3:
                return " " + player.getSession().totalvnd2 + "";
            case 4:
                return " " + player.getSession().totalvnd2 + "";
            case 5:
                return " " + player.getSession().totalvnd2 + "";
            case 6:
                return " " + player.getSession().totalvnd2 + "";
            case 7:
                return " " + player.getSession().totalvnd2 + "";
            case 8:
                return " " + player.getSession().totalvnd2 + "";
            case 9:
                return " " + player.getSession().totalvnd2 + "";
            case 10:
                return " " + player.getSession().totalvnd2 + "";
            case 11:
                return " " + player.getSession().totalvnd2 + "";
            case 12:
                return " " + player.getSession().totalvnd2 + "";
            case 13:
                return " " + player.getSession().totalvnd2 + "";
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
            case 11:
                return 0;
            case 12:
                return 0;
            case 13:
                return 0;

            default:
                return -1;
        }
    }
}
