package nro.models.boss.list_boss.cell;

import nro.consts.ConstPlayer;
import nro.consts.cn;
import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Logger;
import nro.utils.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SieuBoHung extends Boss {

    public boolean isSpawnXencon = false;
    public List<Boss> listXencon;
    private Boss xencon;
    private int tileXencon;

    public SieuBoHung() throws Exception {
        super(BossType.SIEU_BO_HUNG, BossesData.SIEU_BO_HUNG_1, BossesData.SIEU_BO_HUNG_2, BossesData.SIEU_BO_HUNG_3);
        this.listXencon = new ArrayList<>();
    }

    public SieuBoHung(Zone zone, int id, BossData... data) throws Exception {
        super(id, data);
        this.zone = zone;
    }

    @Override
    public void joinMap() {
        super.joinMap();
        tileXencon = Util.nextInt(50, 100);
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
            Logger.logException(SieuBoHung.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void reward(Player plKill) {
        plKill.event.addEventPointBoss(3);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 3 điểm săn Boss");
        int[] NRs = new int[]{16, 18, 18, 15, 17, 17, 17};

        int randomNR = new Random().nextInt(NRs.length);

        if (Util.isTrue(cn.tileroinr, 100)) {
            ItemMap it = new ItemMap(this.zone, 14, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
        if (Util.isTrue(cn.tileroinr, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
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
            try {
                if (!this.isSpawnXencon && this.nPoint.hp <= this.nPoint.hpMax / 2 && this.getCurrentLevel() == 0 && tileXencon >= 50) {
                    this.setBossStatus(BossStatus.WAIT);
                    this.changeToTypeNonPK();
                    this.isSpawnXencon = true;
                    this.trieuHoiXencon();
                }
            } catch (Exception e) {
                Logger.logException(SieuBoHung.class, e);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void SpawnCombat() {
        if (this.isSpawnXencon && this.listXencon.isEmpty() && this.getCurrentLevel() == 0) {
            this.setBossStatus(BossStatus.ACTIVE);
            this.changeToTypePK();
        }
    }

    void trieuHoiXencon() throws Exception {
        for (Byte i = 1; i <= 7; i++) {
            this.xencon = this.createXencon(BossType.XEN_CON_1 + i);
            this.listXencon.add(xencon);
        }
    }

    private Boss createXencon(int XEN_CON_1) throws Exception {
        BossData XEN_CON = new BossData(
                "Xên con " + (XEN_CON_1 - BossType.XEN_CON_1), //name
                ConstPlayer.XAYDA, //gender
                new short[]{264, 265, 266, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                150000, //dame
                new long[]{15000000}, //hp
                new int[]{1}, //map join
                new int[][]{
                    {Skill.DRAGON, 7, 1000},
                    {Skill.KAMEJOKO, 7, 3000}},
                new String[]{"|-1|Hello cục cưng",
                    "|-1|Mày có biết tao là ai không?",
                    "|-2|Tao không cần biết mày là ai, mày nghĩ mày dọa được tao à?",
                    "|-1|Thôi không nói nhiều nữa,giờ tao cho mày biết tao là ai."
                }, //text chat 1
                new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                    "|-1|Ghê chưa ghê chưa!",
                    "|-1|Tao có rất nhiều vật phẩm quý giá,nhưng với mày thì có cái..nịt",
                    "|-1|Đánh tao à,lo mà luyện tập thêm đi",
                    "|-1|Nói cho mày biết,tao là con của Xên ",
                    "|-1|Tao sẽ thiêu rụi mày"
                }, //text chat 2
                new String[]{"|-2|Đêm qua em đẹp lắm!"}, //text chat 3
                5 //second rest
        );
        return new Xencon(this, BossType.XEN_CON_1, XEN_CON, this.zone, this.location.x, this.location.y);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
