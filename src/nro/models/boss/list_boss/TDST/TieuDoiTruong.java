package nro.models.boss.list_boss.TDST;

import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.BossStatus;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;
import java.util.Arrays;
import java.util.Random;
import nro.server.Manager;

public class TieuDoiTruong extends Boss {

    private boolean startRespawn = false;

    public TieuDoiTruong() throws Exception {
        super(BossType.TIEU_DOI_TRUONG, BossesData.TIEU_DOI_TRUONG);
    }

    public TieuDoiTruong(int type, BossData... data) throws Exception {
        super(type, data);
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            return;
        }
        this.attack();
    }

    @Override
    public void updateBoss() {
        super.updateBoss();
        bossRestart();
    }

    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
        //byte randomDo = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);

        //Item roi
        if (Util.isTrue(990, 1000)) {

            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        if (Util.isTrue(1, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 17, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
//        } else {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 462, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 457, Util.nextInt(1, 20), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(100, 300), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1710, Util.nextInt(1, 3), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (Util.isTrue(1, 10)) {
            generalRewards(pl);
        }

    }

    public void bossRestart() {
        if (this.zone != null && this.getTimeToRestart() != -1
                && Util.canDoWithTime(getTimeToRestart(), getSecondsNotify() * 1000)) {
            if (Arrays.asList(getBossAppearTogether()[0]).stream().anyMatch(x -> x.isDie()) && !startRespawn) {
                handleSubBossRestart();
            }
            setLastTimeNotify(System.currentTimeMillis());
        }
    }

    private void handleSubBossRestart() {
        startRespawn = true;
        for (Boss boss : getBossAppearTogether()[0]) {
            boss.changeStatus(BossStatus.LEAVE_MAP);
        }
        this.changeStatus(BossStatus.LEAVE_MAP);
        BossManager.gI().createBoss(BossType.TIEU_DOI_TRUONG);
    }

}
