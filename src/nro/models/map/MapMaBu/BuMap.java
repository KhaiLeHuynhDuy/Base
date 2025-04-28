package nro.models.map.MapMaBu;

import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;
import java.util.Random;

public class BuMap extends Boss {

    public Zone idZone;
    public int xAttack = 200;
    public int yAttack = 100;
    public int xinit = 0;
    public int yinit = 312;

    public BuMap(Zone zone) throws Exception {
        super(BossType.BU_MAP, BossesData.BU_MAP_2H, BossesData.SUPER_BU, BossesData.BU_TENK, BossesData.BU_HAN, BossesData.KID_BU);
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        if (Util.isTrue(30, 130)) {
            if (Util.isTrue(5, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
        plKill.fightMabu.changePoint((byte) 40);
    }
    @Override
    public void joinMap() {
        if (this.zone == null) {
            if (this.getParentBoss() != null) {
                this.zone = getParentBoss().zone;
            } else if (this.getLastZone() == null) {
                this.zone = this.idZone;
            } else {
                this.zone = this.getLastZone();
            }
        } else {
            if (this.getCurrentLevel() == 0) {
                if (this.getParentBoss() == null) {
                    ChangeMapService.gI().changeMap(this, this.zone, -1, this.yinit);
                } else {
                    ChangeMapService.gI().changeMap(this, this.zone, this.getParentBoss().location.x, this.zone.map.yPhysicInTop(this.location.x, 100));
                }
                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.zone.map.yPhysicInTop(this.location.x, 100));
            }
            Service.gI().sendFlagBag(this);
        }
    }

}
