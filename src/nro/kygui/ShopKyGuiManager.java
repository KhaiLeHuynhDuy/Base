/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.kygui;

//import nro.database.GirlkunDB;
import com.girlkun.database.GirlkunDB;
import nro.utils.Util;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONValue;

/**
 *
 * @author Administrator
 */
public class ShopKyGuiManager {

    private static ShopKyGuiManager instance;
    public long lastTimeUpdate;
    public String[] tabName = new String[]{"Trang bị", "Phụ kiện", "Hỗ trợ", "Linh tinh", ""};
    public List<ItemKyGui> listItem = new ArrayList();

    public int countItemsOfPlayer(long playerId) {
        int count = 0;
        for (ItemKyGui item : listItem) {
            if (item.player_sell == playerId) {
                count++;
            }
        }
        return count;
    }

    public static void update() {
        if (Util.canDoWithTime(ShopKyGuiManager.gI().lastTimeUpdate, 60000)) {
            ShopKyGuiManager.gI().save();
            ShopKyGuiManager.gI().lastTimeUpdate = System.currentTimeMillis();
        }
    }

    public ShopKyGuiManager() {
    }

    public static ShopKyGuiManager gI() {
        if (instance == null) {
            instance = new ShopKyGuiManager();
        }

        return instance;
    }

    public void save() {
        try {
            Connection con = GirlkunDB.getConnection();

            try {
                Statement s = con.createStatement();
                s.execute("TRUNCATE shop_ky_gui");
                Iterator var3 = this.listItem.iterator();

                while (var3.hasNext()) {
                    ItemKyGui it = (ItemKyGui) var3.next();
                    if (it != null) {
                        s.execute(String.format("INSERT INTO `shop_ky_gui`(`id`, `player_id`, `tab`, `item_id`, `gold`, `gem`, `quantity`, `itemOption`, `isUpTop`, `isBuy`) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')", it.id, it.player_sell, it.tab, it.itemId, it.goldSell, it.gemSell, it.quantity, JSONValue.toJSONString(it.options).equals("null") ? "[]" : JSONValue.toJSONString(it.options), it.isUpTop, it.isBuy ? 1 : 0));
                    }
                }
            } catch (Throwable var6) {
                if (con != null) {
                    try {
                        con.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (con != null) {
                con.close();
            }
        } catch (Exception var7) {
        }

    }
}
