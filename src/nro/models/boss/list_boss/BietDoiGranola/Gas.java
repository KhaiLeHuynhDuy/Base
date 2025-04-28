package nro.models.boss.list_boss.BietDoiGranola;

import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.BossStatus;
import nro.models.boss.BossType;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class Gas extends Boss {

    public Zone idZone;
    public int yinit = 312;
    public int xAttack = 200;
    public int yAttack = 100;
    public int xinit = 0;

    public Gas(Zone zone) throws Exception {
        super(BossType.GAS, new BossData(
                "Gas", //name
                ConstPlayer.XAYDA, //gender
                new short[]{2006, 2007, 2014, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                10000, //dame
                new long[]{800000000}, //hp
                new int[]{170,171}, //map join
                new int[][]{
                    {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                    {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                    {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                    {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23}, {Skill.TAI_TAO_NANG_LUONG, 2, 25000},},
                new String[]{"|-1| Xin chào! ta là trùm đây"}, //text chat 1
                new String[]{"|-1| Các ngươi định đánh bại ta sao?"},
                new String[]{"|-1| Còn lâu!!"}, //text chat 3
                60
        ));
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        if (Util.isTrue(50, 100)) {
            int itDropCount = 0;
            int num = Util.isTrue(50, 100) ? 3 : 5;
            while (itDropCount < num) {
                ItemMap it = new ItemMap(this.zone, 2029, 1, this.location.x - (itDropCount * 30), this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                Service.gI().dropItemMap(this.zone, it);
                itDropCount++;
            }
        } else if (Util.isTrue(30, 100)) {
            ItemMap it = new ItemMap(this.zone, 16, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }generalRewards(plKill);
    }

    @Override
    public void active() {
        super.active();
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
    public void joinMap() {
        if (this.zone == null) {
            if (this.getParentBoss() != null) {
                this.zone = getParentBoss().getLastZone();
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

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }

    @Override

    public void die(Player plKill) {

        if (plKill != null) {
            reward(plKill);
        }
        this.changeStatus(BossStatus.DIE);
    }
}
