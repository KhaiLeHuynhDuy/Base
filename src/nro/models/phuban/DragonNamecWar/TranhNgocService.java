/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.phuban.DragonNamecWar;

import java.util.List;
import nro.consts.ConstTranhNgocNamek;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.services.InventoryServiceNew;
import nro.services.ItemMapService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @Build Arriety
 */
public class TranhNgocService {

    private static TranhNgocService instance;

    public static TranhNgocService getInstance() {
        if (instance == null) {
            instance = new TranhNgocService();
        }
        return instance;
    }

    public void sendCreatePhoBan(Player pl) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
            msg.writer().writeShort(ConstTranhNgocNamek.MAP_ID);
            msg.writer().writeUTF(ConstTranhNgocNamek.CADIC); // team 1
            msg.writer().writeUTF(ConstTranhNgocNamek.FIDE); // team 2
            msg.writer().writeInt(ConstTranhNgocNamek.MAX_LIFE);
            msg.writer().writeShort(ConstTranhNgocNamek.TIME_SECOND);
            msg.writer().writeByte(ConstTranhNgocNamek.MAX_POINT);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpdateLift(Player pl) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.zone.getPlayersCadic().stream().filter(p -> p != null && !p.isDie()).count());
            msg.writer().writeInt((int) pl.zone.getPlayersFide().stream().filter(p -> p != null && !p.isDie()).count());
            Service.gI().sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEndPhoBan(Zone zone, byte type, boolean isFide) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(2);
            msg.writer().writeByte(type);
            if (zone != null) {
                List<Player> players = isFide ? zone.getPlayersFide() : zone.getPlayersCadic();
                synchronized (players) {
                    for (Player pl : players) {
                        if (pl != null) {
                            pl.sendMessage(msg);
                        }
                    }
                }
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpdateTime(Player pl, short second) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(5);
            msg.writer().writeShort(second);
            Service.gI().sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUpdatePoint(Player pl) {
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(4);
            msg.writer().writeByte(pl.zone.pointCadic);
            msg.writer().writeByte(pl.zone.pointFide);
            Service.gI().sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void givePrice(List<Player> players, byte type, int point) {
        switch (type) {
            case ConstTranhNgocNamek.LOSE:
                int pointDiff = ConstTranhNgocNamek.MAX_POINT - point;
                for (Player pl : players) {
                    if (pl != null) {
                        Item mcl = InventoryServiceNew.gI().findItemBagByTemp(pl, ConstTranhNgocNamek.ITEM_TRANH_NGOC);
                        if (mcl != null) {
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, mcl, pointDiff);
                            InventoryServiceNew.gI().sendItemBags(pl);
                        }
                        TranhNgoc.gI().removePlayersCadic(pl);
                        TranhNgoc.gI().removePlayersFide(pl);
                    }
                }
                break;
            case ConstTranhNgocNamek.WIN:
                for (Player pl : players) {
                    if (pl != null) {
                        Item mcl = ItemService.gI().createNewItem((short) ConstTranhNgocNamek.ITEM_TRANH_NGOC);
                        mcl.quantity = point;
                        InventoryServiceNew.gI().addItemBag(pl, mcl);
                        InventoryServiceNew.gI().sendItemBags(pl);
                        TranhNgoc.gI().removePlayersCadic(pl);
                        TranhNgoc.gI().removePlayersFide(pl);
                    }
                }
                break;
            case ConstTranhNgocNamek.DRAW:
                for (Player pl : players) {
                    if (pl != null) {
                        Item mcl = ItemService.gI().createNewItem((short) ConstTranhNgocNamek.ITEM_TRANH_NGOC);
                        mcl.quantity = point;
                        InventoryServiceNew.gI().addItemBag(pl, mcl);
                        InventoryServiceNew.gI().sendItemBags(pl);
                        TranhNgoc.gI().removePlayersCadic(pl);
                        TranhNgoc.gI().removePlayersFide(pl);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void pickBall(Player player, ItemMap item) {
        if (player.isHoldNamecBallTranhDoat || item.typeHaveBallTranhDoat == player.iDMark.getTranhNgoc()) {
            return;
        }
        if (item.typeHaveBallTranhDoat != -1 && item.typeHaveBallTranhDoat != player.iDMark.getTranhNgoc()) {
            if (player.iDMark.getTranhNgoc() == 1) {
                player.zone.pointFide--;
            } else if (player.iDMark.getTranhNgoc() == 2) {
                player.zone.pointCadic--;
            }
            sendUpdatePoint(player);
        }
        player.tempIdNamecBallHoldTranhDoat = item.itemTemplate.id;
        player.isHoldNamecBallTranhDoat = true;
        ItemMapService.gI().removeItemMapAndSendClient(item);
        Service.gI().sendFlagBag(player);
        Service.gI().sendThongBao(player, "Bạn đang giữ viên ngọc rồng Namek");
    }

    public void dropBall(Player player, byte a) {
        if (player.tempIdNamecBallHoldTranhDoat != -1) {
            player.isHoldNamecBallTranhDoat = false;
        }

        // Xác định tọa độ ngẫu nhiên trong bản đồ
        int x = Util.nextInt(20, player.zone.map.mapWidth);  // X ngẫu nhiên từ 20 đến chiều rộng bản đồ
        int y = player.zone.map.yPhysicInTop(x, player.zone.map.mapHeight / 2);  // Y được tính dựa trên vật lý map

        // Tạo ItemMap mới
        ItemMap itemMap = new ItemMap(player.zone, player.tempIdNamecBallHoldTranhDoat, 1, x, y, -1);
        itemMap.isNamecBallTranhDoat = true;  // Đánh dấu là Ngọc Rồng Namek
        itemMap.typeHaveBallTranhDoat = a;

        // Đặt vị trí xuất hiện của vật phẩm
        itemMap.x = player.location.x;
        itemMap.y = player.location.y;

        // Thêm vật phẩm vào bản đồ và gửi thông tin cho người chơi
        Service.gI().dropItemMap(player.zone, itemMap);
        Service.gI().sendFlagBag(player);

        // Reset trạng thái người chơi
        player.tempIdNamecBallHoldTranhDoat = -1;
        Service.gI().sendThongBao(player, "Ngon");  // Thông báo cho người chơi
    }

}
