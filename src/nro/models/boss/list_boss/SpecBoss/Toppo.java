package nro.models.boss.list_boss.SpecBoss;

import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.Service;
import nro.utils.Util;
import java.util.Random;
import nro.consts.ConstPlayer;
import nro.models.boss.BossStatus;
import nro.models.boss.list_boss.BLACK.Black;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.PlayerService;
import nro.services.TaskService;
import nro.utils.Logger;

public class Toppo extends Boss {

    public Toppo() throws Exception {
        super(BossType.TOP_PO, BossesData.TOP_PO);
    }

    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(3);
        Service.gI().sendThongBao(pl, "Bạn nhận được 3 điểm săn boss");
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        //byte randomDo2 = (byte) new Random().nextInt(Manager.manhts.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);
         byte randomTT = (byte) new Random().nextInt(Manager.itemIds_tinhthach.length - 1);
        byte randomDVT = (byte) new Random().nextInt(Manager.itemIds_DVT.length - 1);

        //Item roi
        if (Util.isTrue(9, 10)) {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x + 5, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
//        if (Util.isTrue(9, 10)) {
////            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.manhts[randomDo2], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
////        } else {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 462, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
//
//        }
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_DVT[randomDVT], Util.nextInt(1, 20), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_tinhthach[randomTT], 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
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
            Logger.logException(Toppo.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                    case Skill.KAIOKEN:
                    case Skill.LIEN_HOAN:
                        PlayerService.gI().hoiPhuc(this, (long) damage, 0);
                        if (Util.isTrue(1, 5)) {
                            this.chat("Hakai....................");
                        }
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
}
