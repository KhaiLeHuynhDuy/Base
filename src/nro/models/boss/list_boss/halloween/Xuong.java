package nro.models.boss.list_boss.halloween;

import nro.consts.ConstPlayer;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.Calendar;
import java.util.Date;
import nro.models.boss.list_boss.BLACK.Black;
import nro.utils.Logger;

/**
 *
 * @Stole By HoangKiet
 */
public class Xuong extends Boss {

    public static final BossData XUONG = new BossData(
//            "Xương", //name
            "VEGETA TET", //name
            ConstPlayer.XAYDA, //gender
//            new short[]{545, 548, 549, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
//            new short[]{940, 941, 942, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                  new short[]{1757, -1, -1, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
           
            5000, //dame
            new long[]{600000}, //hp mau tang day nhe
             new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 29, 24, 25, 27, 28}, //map join
            new int[][]{
                {Skill.GALICK, 3, 1000},
                {Skill.GALICK, 7, 1000}},
            new String[]{}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{"|-2|Đẹp trai nó phải thế"}, //text chat 3
            300000 //second rest
    );

    public Xuong() throws Exception {
        super(BossType.XUONG, XUONG);
    }

    
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(10, 100)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int[] daysOfWeek = {1, 2, 3, 4, 5, 6, 7};
            int currentDayOfWeek = (daysOfWeek[dayOfWeek - 1]);
            ItemMap item = new ItemMap(this.zone, 701 + currentDayOfWeek, 1, this.location.x, this.location.y, plKill.id);
            Service.gI().dropItemMap(zone, item);
        }
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();

                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                        }
                    }
                    SkillService.gI().useSkill(this, pl, null, null);
                    if (pl.isPl() || pl.isPet) {
                        if (!pl.itemTime.isBienHinhMa) {
                            pl.itemTime.isBienHinhMa = true;
                            pl.itemTime.lastTimeBienHinhMa = System.currentTimeMillis();
                            Service.gI().point(pl);
                            ItemTimeService.gI().sendAllItemTime(pl);
                            Service.gI().Send_Caitrang(pl);
                        }
                    }
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.printStackTrace();
            }
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
            Logger.logException(Xuong.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) (this.nPoint.hpMax * 10 / 100);
            damage = (int) this.nPoint.subDameInjureWithDeff(damage);
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

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
