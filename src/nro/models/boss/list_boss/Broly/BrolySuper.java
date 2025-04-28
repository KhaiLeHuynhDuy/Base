package nro.models.boss.list_boss.Broly;

import nro.consts.ConstPlayer;
import nro.models.boss.*;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.PetService;
import nro.services.Service;
import nro.utils.Util;

public class BrolySuper extends Boss {

    private long lastUpdate = System.currentTimeMillis();
    private long timeJoinMap;
    protected Player playerAtt;
    private int timeLive = 200000000;
    public int petgender = 0;
    public Player mypett;

    @Override
    public void die(Player player) {
        super.die(player);

    }

    public BrolySuper() throws Exception {
        super(Util.randomBossId(), BossesData.SUPPER);
    }

    public BrolySuper(Zone zone, long dame, long hp, int id) throws Exception {
        super(id, new BossData(
                "Super Broly", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{1228, 1229, 1230, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((50000 + dame)), //dame
                new long[]{((5000000 + hp))}, //hp
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, //map join
                new int[][]{
                    {Skill.KAMEJOKO, 7, 30000},
                    {Skill.MASENKO, 7, 10000},
                    {Skill.ANTOMIC, 7, 15000},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 20000},},
                new String[]{
                    "|-1|Gaaaaaa",
                    "|-2|Tới đây đi!"
                }, //text chat 1
                new String[]{"|-1|Các ngươi tới số rồi mới gặp phải ta",
                    "|-1|Gaaaaaa",
                    "|-2|Không ngờ..Hắn mạnh cỡ này sao..!!"
                }, //text chat 2
                new String[]{"|-1|Gaaaaaaaa!!!"}, //text chat 3
                600
        ));

    }

    @Override
    public void reward(Player plKill) {
       plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");

//        PetService.gI().createNormalPetSuperGender(plKill, this.pet.gender, this.pet.typePet);
        if (plKill.pet == null) {
            PetService.gI().createNormalPetSuperGender(plKill, this.pet.gender, this.pet.typePet);
            Service.gI().sendThongBao(plKill, "Bạn đã nhận được đệ tử\n Vui lòng thoát game vào lại");
        } else {
             Service.gI().sendThongBao(plKill, "Có đệ rồi mà con");
        }
        this.pet.dispose();
        this.pet = null;

    }

    @Override
    public void active() {
        super.active();
        if (this.pet == null) {
            PetService.gI().createNormalPetSuper(this, Util.nextInt(0, 2), (byte) Util.nextInt(0, 10));
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
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                if (damage > this.nPoint.hpMax * 0.05) {
                    damage = this.nPoint.hpMax * 0.05;
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
}
