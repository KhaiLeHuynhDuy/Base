package nro.models.map;

import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstItem;
import nro.consts.ConstMap;
import nro.models.Template.ItemTemplate;
import nro.models.clan.Clan;
import nro.models.item.Item.ItemOption;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.utils.Util;
import nro.services.ItemMapService;
import nro.services.ItemService;
import nro.services.Service;

public class ItemMap {

    public Zone zone;
    public int itemMapId;
    public ItemTemplate itemTemplate;
    public int quantity;

    public int x;
    public int y;
    public long playerId;
    public List<ItemOption> options;

    private long createTime;

    public boolean isBlackBall;
    public boolean isGiaidauBall;
    public boolean isNamecBall;
    public boolean isDoanhTraiBall;

    public boolean isNamecBallTranhDoat;
    public byte typeHaveBallTranhDoat = -1;
    public Object item;

    public Clan clan;

    public ItemMap(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        this.zone = zone;
        this.itemMapId = zone.countItemAppeaerd++;
        if (zone.countItemAppeaerd >= 2000000000) {
            zone.countItemAppeaerd = 0;
        }
        this.itemTemplate = ItemService.gI().getTemplate((short) tempId);
        this.quantity = quantity;
        this.x = x;
        this.y = y;
        this.playerId = playerId != -1 ? Math.abs(playerId) : playerId;
        this.createTime = System.currentTimeMillis();
        this.options = new ArrayList<>();
        this.isBlackBall = ItemMapService.gI().isBlackBall(this.itemTemplate.id);
        this.isGiaidauBall = ItemMapService.gI().isGiaidauBall(this.itemTemplate.id);
        this.isNamecBall = ItemMapService.gI().isNamecBall(this.itemTemplate.id);
        this.lastTimeMoveToPlayer = System.currentTimeMillis();
        this.zone.addItem(this);
    }

    public ItemMap(Zone zone, ItemTemplate temp, int quantity, int x, int y, long playerId) {
        this.zone = zone;
        this.itemMapId = zone.countItemAppeaerd++;
        if (zone.countItemAppeaerd >= 2000000000) {
            zone.countItemAppeaerd = 0;
        }
        this.itemTemplate = temp;
        this.quantity = quantity;
        this.x = x;
        this.y = y;
        this.playerId = playerId != -1 ? Math.abs(playerId) : playerId;
        this.createTime = System.currentTimeMillis();
        this.options = new ArrayList<>();
        this.isBlackBall = ItemMapService.gI().isBlackBall(this.itemTemplate.id);
        this.isGiaidauBall = ItemMapService.gI().isGiaidauBall(this.itemTemplate.id);
        this.isNamecBall = ItemMapService.gI().isNamecBall(this.itemTemplate.id);
        this.lastTimeMoveToPlayer = System.currentTimeMillis();
        this.zone.addItem(this);
    }

    public ItemMap(Zone zone, ItemTemplate temp, int quantity, int x, int y, long playerId, Clan clan) {
        this.zone = zone;
        this.itemMapId = zone.countItemAppeaerd++;
        if (zone.countItemAppeaerd >= 2000000000) {
            zone.countItemAppeaerd = 0;
        }
        this.itemTemplate = temp;
        this.quantity = quantity;
        this.x = x;
        this.y = y;
        this.playerId = playerId != -1 ? Math.abs(playerId) : playerId;
        this.createTime = System.currentTimeMillis();
        this.options = new ArrayList<>();
        this.isBlackBall = ItemMapService.gI().isBlackBall(this.itemTemplate.id);
        this.isGiaidauBall = ItemMapService.gI().isGiaidauBall(this.itemTemplate.id);
        this.isNamecBall = ItemMapService.gI().isNamecBall(this.itemTemplate.id);
        this.lastTimeMoveToPlayer = System.currentTimeMillis();
        this.clan = clan;
        this.zone.addItem(this);

    }

    public ItemMap(ItemMap itemMap) {
        this.zone = itemMap.zone;
        this.itemMapId = itemMap.itemMapId;
        this.itemTemplate = itemMap.itemTemplate;
        this.quantity = itemMap.quantity;
        this.x = itemMap.x;
        this.y = itemMap.y;
        this.playerId = itemMap.playerId;
        this.options = itemMap.options;
        this.isBlackBall = itemMap.isBlackBall;
        this.isGiaidauBall = itemMap.isGiaidauBall;
        this.isNamecBall = itemMap.isNamecBall;
        this.lastTimeMoveToPlayer = itemMap.lastTimeMoveToPlayer;
        this.createTime = System.currentTimeMillis();
        this.zone.addItem(this);

    }

    public void update() {
        if (this.isBlackBall) {
            if (Util.canDoWithTime(lastTimeMoveToPlayer, timeMoveToPlayer)) {
                if (this.zone != null && !this.zone.getPlayers().isEmpty()) {
                    Player player = this.zone.getPlayers().get(0);
                    if (player.zone != null && player.zone.equals(this.zone)) {
                        this.x = player.location.x;
                        this.y = this.zone.map.yPhysicInTop(this.x, player.location.y - 24);
                        reAppearItem();
                        this.lastTimeMoveToPlayer = System.currentTimeMillis();
                    }
                }
            }
            return;
        }
        if (this.isGiaidauBall) {
            if (Util.canDoWithTime(lastTimeMoveToPlayer, timeMoveToPlayer)) {
                if (this.zone != null && !this.zone.getPlayers().isEmpty()) {
                    Player player = this.zone.getPlayers().get(0);
                    if (player.zone != null && player.zone.equals(this.zone)) {
                        this.x = player.location.x;
                        this.y = this.zone.map.yPhysicInTop(this.x, player.location.y - 24);
                        reAppearItem();
                        this.lastTimeMoveToPlayer = System.currentTimeMillis();
                    }
                }
            }
            return;
        }

        if (Util.canDoWithTime(createTime, 20000) && !this.isNamecBall) {
            if (this.itemTemplate.type != 22) {
                if (this.isDoanhTraiBall && Util.canDoWithTime(createTime, 300_000)) {
                    ItemMapService.gI().removeItemMapAndSendClient(this);
                } else {
                    if (this.zone.map.mapId != 21 && this.zone.map.mapId != 22
                            && this.zone.map.mapId != 23 && this.itemTemplate.id != 78) {
                        ItemMapService.gI().removeItemMapAndSendClient(this);
                    }
                }
            } else if (Util.canDoWithTime(createTime, 1800000)) {
                ItemMapService.gI().removeItemMapAndSendClient(this);
            }
//            else if (Util.canDoWithTime(createTime, 60000)) {
//                ItemMapService.gI().removeItemMapAndSendClient(this);
//            }
        }
        if (Util.canDoWithTime(createTime, 15000)) {
            this.playerId = -1;
        }
    }

    private final int timeMoveToPlayer = 10000;
    private long lastTimeMoveToPlayer;

    private void reAppearItem() {
        ItemMapService.gI().sendItemMapDisappear(this);
        Service.gI().dropItemMap(this.zone, this);
    }

    public void dispose() {
        this.zone = null;
        this.itemTemplate = null;
        this.options = null;
    }

    public Object getItem() {
        return this.item;
    }

    public boolean isBelongToMe(Player player) {
        try {
            if (player == null) {
                return false;
            }
            if (this.playerId == Math.abs(player.id)) { // kiểm tra là sư phụ hoặc đệ tử của nó
                return true;
            }
            if (this.clan != null) {
                if (player.clan != null && this.clan.id == player.clan.id) {
                    return true;
                }
                if (player.isPet) {
                    if (((Pet) player).master.clan != null) {
                        if (((Pet) player).master.clan.id == this.clan.id) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isInVeTinhRange(Player pl, int code, int locationX, int locationY) {
        ItemMap itemMap = null;
        if (pl == null) {
            return false;
        }
        switch (code) {
            case ConstItem.VE_TINH_TRI_LUC:
                itemMap = pl.veTinhTriLuc;
                break;
            case ConstItem.VE_TINH_TRI_TUE:
                itemMap = pl.veTinhTriTue;
                break;
            case ConstItem.VE_TINH_PHONG_THU:
                itemMap = pl.veTinhPhongThu;
                break;
            case ConstItem.VE_TINH_SINH_LUC:
                itemMap = pl.veTinhSinhLuc;
                break;
        }

        if (itemMap == null || itemMap.itemTemplate == null) {
            return false;
        }

        boolean isInRange = Util.myGetDistcance(itemMap.x, itemMap.y, locationX, locationY) <= ConstMap.RANGE_VE_TINH;
        return !pl.isDie() && isInRange;
    }

}
