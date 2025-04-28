package nro.models.boss.list_boss.ConDuongRanDoc;


import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.BossType;
import nro.models.boss.Boss;
import nro.consts.ConstPlayer;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.utils.Util;


public class Saibamen6 extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public Saibamen6(Zone zone , int level, long dame, long hp) throws Exception {
        super(BossType.SAIBAMEN_6, new BossData(
                "số 6",
                ConstPlayer.TRAI_DAT,
                new short[]{642, 643, 644, -1, -1, -1},
                ((10000 + dame) * level),
                new long[]{((500000 + hp) * level)},
                new int[]{143},
                (int[][]) Util.addArray(FULL_DEMON),
                new String[]{},
                new String[]{"|-1|Nhóc con"},
                new String[]{},
                60
        ));
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 19, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
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
            if (this.nPoint.hp == 0) {
                try {
                    
                 new Nappa(this.zone, 2, Util.nextInt(1000, 10000), BossType.SAIBAMEN_6);      
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
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
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}