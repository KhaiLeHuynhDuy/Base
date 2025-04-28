package nro.models.boss.list_boss.android;

import java.util.Random;
import nro.consts.ConstPlayer;
import nro.consts.cn;
import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossStatus;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class Android14 extends Boss {

    public boolean callApk13;

    Android13 adr13;
    Android15 adr15;

    public Android14() throws Exception {
        super(BossType.ANDROID_14, BossesData.ANDROID_14);
    }

    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
        //byte randomDo = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);

        //Item roi
        if (Util.isTrue(99, 100)) {
          
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        if (Util.isTrue(1, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1466, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
//        } else {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 462, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 457, Util.nextInt(1, 20), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(100, 300), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (Util.isTrue(1, 10)) {
            generalRewards(pl);
        }

    }

    @Override
    protected void resetBase() {
        super.resetBase();
        this.callApk13 = false;
        this.adr13 = (Android13) this.getBossAppearTogether()[this.getCurrentLevel()][0];
        this.adr15 = (Android15) this.getBossAppearTogether()[this.getCurrentLevel()][1];
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK && !this.callApk13) {
            this.changeToTypePK();
        } else if (this.callApk13 && adr13.typePk == ConstPlayer.PK_ALL) {
            this.changeToTypePK();
        }
        this.attack();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.callApk13 && this.nPoint.hp - damage <= this.nPoint.hpMax / 2) {
            this.callApk13();
            return 0;
        }
        damage = this.nPoint.subDameInjureWithDeff(damage);
        if (plAtt != null && !piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage * 0.5;
            }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    public void callApk13() {
        if (this.getBossAppearTogether() == null || this.getBossAppearTogether()[this.getCurrentLevel()] == null) {
            return;
        }
        this.adr15.changeToTypeNonPK();
        this.changeToTypeNonPK();
        this.adr15.callApk13 = true;
        this.callApk13 = true;
        this.adr15.recoverHP();
        this.recoverHP();
        this.adr13.changeStatus(BossStatus.RESPAWN);
    }

    public void recoverHP() {
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    @Override
    public void doneChatS() {
        if (this.getBossAppearTogether() == null || this.getBossAppearTogether()[this.getCurrentLevel()] == null) {
            return;
        }
        for (Boss boss : this.getBossAppearTogether()[this.getCurrentLevel()]) {
            if (boss.id == BossType.ANDROID_15) {
                boss.changeToTypePK();
                break;
            }
        }
    }

}
