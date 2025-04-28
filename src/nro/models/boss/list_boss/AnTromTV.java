package nro.models.boss.list_boss;

import java.util.Iterator;
import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossStatus;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.boss.list_boss.BLACK.Black;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.Manager;
import nro.services.InventoryServiceNew;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Logger;
import nro.utils.Util;

public class AnTromTV extends Boss {

    private long goldAnTrom;
    private long lastTimeAnTrom;
    private long lastTimeJoinMap;
    private static final long timeChangeMap = 1000; // thời gian đổi map 1 lần

    public AnTromTV() throws Exception {
        super(BossType.AN_TROM_TV, BossesData.AN_TROM_TV);
    }

    @Override
    public Zone getMapJoin() {
        int mapId = this.data[this.currentLevel].getMapJoin()[Util.nextInt(0, this.data[this.currentLevel].getMapJoin().length - 1)];
        return MapService.gI().getMapById(mapId).zones.get(0);
    }

    @Override
    public Player getPlayerAttack() {
        return super.getPlayerAttack();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
//            damage = 1;
            if (damage > nPoint.hpMax * 0.01) {
                damage = nPoint.hpMax * 0.01;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
            SkillService.gI().useSkill(this, plAtt, null, null);
            return damage;
        } else {
            return 0;
        }
    }

  @Override
public void attack() {
    if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
        this.lastTimeAttack = System.currentTimeMillis();
        try {
            Player pl = getPlayerAttack();
            if (pl == null || pl.isDie()) {
                return;
            }
            
            Item thoivang = InventoryServiceNew.gI().findItemBag(pl, 457);
            if (thoivang == null) {
                return;
            }
            
            if (Util.getDistance(this, pl) <= 40) {
                if (!Util.canDoWithTime(this.lastTimeAnTrom, 10000) || goldAnTrom > 5_000) {
                    return;
                }
                if (pl.isPl()) {
                    int stolenGold = Util.nextInt(1, 10);
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, thoivang, stolenGold);
                    Service.gI().sendMoney(pl);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    InventoryServiceNew.gI().addQuantityItemsBag(this, thoivang, stolenGold);
                    InventoryServiceNew.gI().sendItemBags(this);
                    if (thoivang.quantity > 0) {
                        goldAnTrom += stolenGold;
                        Service.gI().stealMoney(pl, -stolenGold);
                        ItemMap itemMap = new ItemMap(this.zone, 457, stolenGold, (this.location.x + pl.location.x) / 2, this.location.y, this.id);
                        Service.gI().dropItemMap(this.zone, itemMap);
                        Service.gI().sendToAntherMePickItem(this, itemMap.itemMapId);
                        this.zone.removeItemMap(itemMap);
                        this.lastTimeAnTrom = System.currentTimeMillis();
                    }
                }
            } else {
                if (Util.isTrue(1, 2)) {
                    this.moveToPlayer(pl);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(30, 40);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
    }

    @Override
    public void reward(Player plKill) {

        if (goldAnTrom != 0) {

            int goldReward = (int) (goldAnTrom * 1 / 10);
            for (byte i = 0; i < 5; i++) {
                Service.gI().dropItemMap(
                        this.zone, Util.ItemRotTuQuai(zone, 457, (int) goldReward / 5, this.location.x, this.location.y, plKill.id));
            }
            Service.gI().dropItemMap(
                    this.zone, new ItemMap(zone, plKill.gender == 0 ? 2000 : (plKill.gender == 1 ? 2001 : 2002), Util.nextInt(3, 4), this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));

//            Service.gI().dropItemMap(this.zone, it);
        }
        //bật để ăn trộm rơi không thỏi vàng

        goldAnTrom = 0;
    }

    @Override
    public void joinMap() {
        super.joinMap();
        lastTimeJoinMap = System.currentTimeMillis() + timeChangeMap;
        goldAnTrom = 0;
    }
//@Override
//    public void joinMap() {
//        super.joinMap();
//        st = System.currentTimeMillis();
//    }
//    private long st;

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        try {
        } catch (Exception ex) {
              ex.printStackTrace();
            Logger.logException(AnTromTV.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(lastTimeJoinMap, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void update() {
        super.update();
        Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
        if (ramdonPlayer != null && ramdonPlayer.zone.isKhongCoTrongTaiTrongKhu()) {
            if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && this.zone.getPlayers().size() <= 0 && System.currentTimeMillis() > this.lastTimeJoinMap) {
                if (ramdonPlayer.id != -1000000) {
                    lastTimeJoinMap = System.currentTimeMillis() + timeChangeMap;
                    System.err.println("Ăn Trộm Đã Tìm Thấy: " + this.zone.getPlayers().size() + " Player");
                    ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
                    ChangeMapService.gI().exitMap(this);
                    this.zoneFinal = null;
                    this.lastZone = null;
                    this.zone = ramdonPlayer.zone;
                    this.location.x = Util.nextInt(100, zone.map.mapWidth - 100);
                    this.location.y = zone.map.yPhysicInTop(this.location.x, 100);
                    this.joinMap();
                    System.err.println("New MAP: " + this.zone.map.mapId);
                    System.err.println("Ăn Trộm Đã Tìm Thấy: " + this.zone.getPlayers().size() + " Player");
                }
            }
        }
    }
}
