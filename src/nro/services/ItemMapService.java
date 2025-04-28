package nro.services;

import java.util.List;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.utils.Logger;
import nro.utils.Util;

public class ItemMapService {

    private static ItemMapService i;

    public static ItemMapService gI() {
        if (i == null) {
            i = new ItemMapService();
        }
        return i;
    }

    public void pickItem(Player player, int itemMapId, boolean isThuHut) {
//        mới sửa 0358124452
        if (player.iDMark != null && isThuHut || Util.canDoWithTime(player.iDMark.getLastTimePickItem(), 1000)) {
            player.zone.pickItem(player, itemMapId);
            if (player.iDMark != null) {
                player.iDMark.setLastTimePickItem(System.currentTimeMillis());
            }
        }
    }
//    public void pickItem(Player player, int itemMapId, boolean isThuHut) {
//        if (player.iDMark != null && (isThuHut || Util.canDoWithTime(player.iDMark.getLastTimePickItem(), 1000))) {
//            player.zone.pickItem(player, itemMapId);
//            player.iDMark.setLastTimePickItem(System.currentTimeMillis());
//        }
//    }

    //xóa item map và gửi item map biến mất
    public void removeItemMapAndSendClient(ItemMap itemMap) {
        sendItemMapDisappear(itemMap);
        removeItemMap(itemMap);
    }

    public void sendItemMapDisappear(ItemMap itemMap) {
        Message msg;
        try {
            msg = new Message(-21);
            msg.writer().writeShort(itemMap.itemMapId);
            Service.gI().sendMessAllPlayerInMap(itemMap.zone, msg);
            msg.cleanup();
        } catch (Exception e) {

            e.printStackTrace();
            Logger.logException(ItemMapService.class, e);
        }
    }



    public void removeItemMap(ItemMap itemMap) {
        itemMap.zone.removeItemMap(itemMap);
        itemMap.dispose();
    }

    public boolean isBlackBall(int tempId) {
        return tempId >= 372 && tempId <= 378;
    }

    public boolean isGiaidauBall(int tempId) {
        return tempId >= 2220 && tempId <= 2226;
    }

    public boolean isNamecBall(int tempId) {
        return tempId >= 353 && tempId <= 360;
    }

    private boolean isExistItem(List<Item> inventory, short s) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
