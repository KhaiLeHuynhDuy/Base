package nro.models.boss.list_boss.android;

import java.util.Random;
import nro.consts.ConstPlayer;
import nro.consts.cn;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.boss.SmallBoss;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class Pic extends SmallBoss {

    protected boolean isReady;
    private short x;
    private short y;

    public Pic() throws Exception {
        super(BossType.PIC, BossesData.PIC);
    }

    public Pic(Boss bigBoss, Zone zone, short x, short y, BossData data) throws Exception {
        super(BossType.PIC, bigBoss, data);
        this.isReady = false;
        this.zone = zone;
        this.x = x;
        this.y = y;
    }

    @Override
    public void joinMap() {
        if (this.bigBoss == null) {
            super.joinMap();
        } else {
            ChangeMapService.gI().changeMap(this, this.zone, x + Util.getOne(-1, 1) * 50, y);
        }
    }
   @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
       // byte randomDo = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
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
    public void doneChatE() {
        if (this.getParentBoss() == null || this.getParentBoss().getBossAppearTogether() == null
                || this.getParentBoss().getBossAppearTogether()[this.getParentBoss().getCurrentLevel()] == null) {
            this.changeToTypePK();
        } else {
            for (Boss boss : this.getParentBoss().getBossAppearTogether()[this.getParentBoss().getCurrentLevel()]) {
                if (boss.id == BossType.POC && !boss.isDie()) {
                    boss.changeToTypePK();
                    break;
                }
            }
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
           damage = this.nPoint.subDameInjureWithDeff(damage);
        if (plAtt != null && !piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage * 0.5;
            }
            if (damage >= this.nPoint.hp && this.bigBoss != null && !this.isReady) {
                this.changeToTypeNonPK();
                this.isReady = true;
                this.nPoint.hp = 1;
//                this.effectSkill.removeSkillEffectWhenDie();
//                if (((SuperAndroid17) this.bigBoss).isReady) {
//                    ((SuperAndroid17) this.bigBoss).lastTimeFusion = System.currentTimeMillis();
//                    ((SuperAndroid17) this.bigBoss).lastTimecanAttack = System.currentTimeMillis();
//                }
                return 0;
            } if (plAtt != null && !piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
//                    EffectSkillService.gI().breakShield(this);
                    damage = nPoint.hpMax * 0.1;
                } else {

                    damage = Util.nextInt((int) plAtt.nPoint.dameg, (int) (plAtt.nPoint.dameg * 1.3));
                }

            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void active() {
        if (this.bigBoss == null) {
            if (this.typePk == ConstPlayer.NON_PK) {
                return;
            }
            this.attack();
        } else {
            if (this.bigBoss != null && this.bigBoss.typePk == ConstPlayer.PK_ALL && !this.isReady) {
                this.changeToTypePK();
            }
            this.attack();
        }
    }

    @Override
    public void wakeupAnotherBossWhenDisappear() {
        Boss boss = this.getParentBoss();
        if (boss != null && !boss.isDie()) {
            boss.changeToTypePK();
        }
    }

    @Override
    public void leaveMap() {
        if (this.bigBoss == null) {
            super.leaveMap();
        } else {
            synchronized (this) {
                BossManager.gI().removeBoss(this);
            }
            this.dispose();
        }
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
