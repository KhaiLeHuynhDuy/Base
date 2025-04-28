package nro.models.boss.list_boss;

import java.util.Random;
import nro.consts.ConstPlayer;
import nro.models.boss.*;
import nro.models.boss.list_boss.BLACK.Black;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.Util;

public class NhanBan extends Boss {

    public NhanBan(int bossID, BossData bossData, Zone zone) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
    }

//    @Override
//    public void reward(Player plKill) {
//        //vật phẩm rơi khi diệt boss nhân bản
//        ItemMap it = new ItemMap(this.zone, Util.nextInt(1099, 1103), Util.nextInt(3, 4), this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                this.location.y - 24), plKill.id);
//        Service.gI().dropItemMap(this.zone, it);
//    }
    @Override
    public void reward(Player pl) {
        //Item roi
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Util.nextInt(1099, 1103), 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 457, Util.nextInt(1, 5), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(100, 300), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        if (Util.isTrue(1, 10)) {
            generalRewards(pl);
        }

    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }
    private long st;

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        try {
        } catch (Exception ex) {
            Logger.logException(NhanBan.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}
