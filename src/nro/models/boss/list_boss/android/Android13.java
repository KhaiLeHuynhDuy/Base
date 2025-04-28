package nro.models.boss.list_boss.android;

import java.util.Random;
import nro.consts.cn;
import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

public class Android13 extends Boss {

    public Android13() throws Exception {
        super(BossType.ANDROID_13, BossesData.ANDROID_13);
    }

//     @Override
//    public void reward(Player plKill) {
//        plKill.inventory.event++;
//        plKill.event.addEventPointBoss(1);
//        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
//        int[] itemRan = new int[]{1142, 382, 383, 384, 1142};
//        int itemId = itemRan[2];
//        if (Util.isTrue(cn.tileroinr, 100)) {
//            ItemMap it = new ItemMap(this.zone, itemId, 17, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id);
//            Service.gI().dropItemMap(this.zone, it);
//        }
//        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
//    }
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
    public void doneChatS() {
        if (this.getParentBoss() == null) {
            return;
        }
        if (this.getParentBoss().getBossAppearTogether() == null
                || this.getParentBoss().getBossAppearTogether()[getParentBoss().getCurrentLevel()] == null) {
            return;
        }
        for (Boss boss : this.getParentBoss().getBossAppearTogether()[getParentBoss().getCurrentLevel()]) {
            if (boss.id == BossType.ANDROID_15 && !boss.isDie()) {
                boss.changeToTypePK();
                break;
            }
        }
        this.getParentBoss().changeToTypePK();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (damage >= this.nPoint.hp) {
            boolean flag = true;
            if (this.getParentBoss() != null) {
                if (this.getParentBoss().getBossAppearTogether() != null && getParentBoss().getBossAppearTogether()[this.getParentBoss().getCurrentLevel()] != null) {
                    for (Boss boss : this.getParentBoss().getBossAppearTogether()[this.getParentBoss().getCurrentLevel()]) {
                        if (boss.id == BossType.ANDROID_15 && !boss.isDie()) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag && !this.getParentBoss().isDie()) {
                    flag = false;
                }
            }
            if (!flag) {
                return 0;
            }
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
}
