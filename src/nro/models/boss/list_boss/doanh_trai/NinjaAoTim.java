
package nro.models.boss.list_boss.doanh_trai;

import nro.consts.ConstPlayer;
import nro.consts.cn;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossType;
import nro.models.boss.BossManager;
import nro.models.boss.BossType;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;
import java.util.Random;
import java.util.logging.Level;
import nro.services.TaskService;

public class NinjaAoTim extends TrungUyTrang {

    protected int numPhanThan;

    public NinjaAoTim(long dame, long hp, Zone zone) throws Exception {
        super(dame, hp, zone, BossType.NINJA_AO_TIM, NINJA);
        numPhanThan = 0;
    }

    private static final BossData NINJA = new BossData(
            "Ninja Áo Tím", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{123, 124, 125, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            500, //dame
            new long[]{500}, //hp
            new int[]{1}, //map join
            new int[][]{
                {Skill.LIEN_HOAN, 7, 1000},
                //                {Skill.THAI_DUONG_HA_SAN, 4, 15000},
                {Skill.KAMEJOKO, 7, 3000}},
            new String[]{}, //text chat 1
            new String[]{"|-1|Tao hơn hẳn mày, mày nên cầu cho may mắn ở phía mày đi",
                "|-1|Ha ha ha! Mắt mày mù à? Nhìn máy đo chỉ số đi!!",
                "|-1|Định chạy trốn hả, hử",
                "|-1|Ta sẽ tàn sát khu này trong vòng 5 phút nữa",
                "|-1|Hahaha mày đây rồi",
                "|-1|Tao đã có lệnh từ đại ca rồi"
            }, //text chat 2
            new String[]{"|-2|Đẹp trai nó phải thế"}, //text chat 3
            5 //second rest
    );

//    @Override
//    public void reward(Player plKill) {
//        plKill.inventory.event++;
//        plKill.event.addEventPointBoss(1);
//        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
//        if (Util.isTrue(cn.tileroinr, 100)) {
//            ItemMap it = new ItemMap(this.zone, Util.nextInt(16), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id);
//            Service.gI().dropItemMap(this.zone, it);
//        }else {
//            ItemMap it = new ItemMap(this.zone, Util.nextInt(17, 20), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
//                    this.location.y - 24), plKill.id);
//            Service.gI().dropItemMap(this.zone, it);
//        }
//    }
     @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
       // byte randomDo = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);

        //Item roi
        if (Util.isTrue(9, 10)) {

            Service.gI().dropItemMap(this.zone, new ItemMap(zone,Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
//        if (Util.isTrue(1, 10)) {
//          
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 462, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
//
//        }
        if (Util.isTrue(9, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 457, Util.nextInt(1, 20), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (Util.isTrue(1, 10)) {
            generalRewards(pl);
        }

    }
    @Override
    public Player getPlayerAttack() {
        if (this.getPlayerTarget() != null && (this.getPlayerTarget().isDie() || !this.zone.equals(this.getPlayerTarget().zone))) {
            this.setPlayerTarget(null);
        }
        if (this.getPlayerTarget() == null || Util.canDoWithTime(this.getTimeTargetPlayer(), this.getTimeTargetPlayer())) {
            this.setPlayerTarget(this.zone.getRandomPlayerInMap());
            this.setLastTimeTargetPlayer(System.currentTimeMillis());
            this.setTimeTargetPlayer(Util.nextInt(5000, 7000));
        }
        if (this.getPlayerTarget() != null && this.getPlayerTarget().effectSkin != null && this.getPlayerTarget().effectSkin.isVoHinh) {
            this.setPlayerTarget(null);
            this.setLastTimeTargetPlayer(System.currentTimeMillis());
            this.setTimeTargetPlayer(Util.nextInt(1000, 2000));
        }
        if (this.getPlayerTarget() == this.pet) {
            this.setPlayerTarget(null);
            this.setLastTimeTargetPlayer(System.currentTimeMillis());
            this.setTimeTargetPlayer(Util.nextInt(1000, 2000));
        }
        if (this.getPlayerTarget() != null) {
            if (this.getPlayerTarget().location.x < 300 || this.getPlayerTarget().location.x > 1405) {
                this.setPlayerTarget(null);
                this.setLastTimeTargetPlayer(System.currentTimeMillis());
                this.setTimeTargetPlayer(Util.nextInt(1000, 2000));
            }
        }
        return this.getPlayerTarget();
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }
    private long lastTimeBlame;
    private boolean isSpawnClone = false;

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(700, 1000)) {
                if (System.currentTimeMillis() - lastTimeBlame > 3000) {
                    this.chat("Xí hụt lêu lêu");
                }
                lastTimeBlame = System.currentTimeMillis();
                return 0;
            }
            if (this.numPhanThan > 0) {
                damage /= 2;
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
                if (!isSpawnClone && this.nPoint.hp <= this.nPoint.hpMax / 2) {
                    this.chat("Phân thân chi thuật bùm bùm bùm");
                    this.PhanThanChiThuat();
                    isSpawnClone = true;
                }
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(NinjaAoTim.class.getName()).log(Level.SEVERE, null, ex);
            }
            return damage;
        } else {
            return 0;
        }
    }

    void PhanThanChiThuat() throws Exception {
        this.numPhanThan = Util.nextInt(5, 10);
        for (Byte i = 0; i < this.numPhanThan; i++) {
            this.createPhanThan(BossType.NINJA_AO_TIM_CLONE + i);
        }
    }

    private Boss createPhanThan(int idBoss) throws Exception {
        return new NinjaClone(this, this.location.x, this.location.y, idBoss, (long) (this.nPoint.getDameAttack(false) / 4), this.nPoint.hpMax / 10, this.zone);
    }
}
