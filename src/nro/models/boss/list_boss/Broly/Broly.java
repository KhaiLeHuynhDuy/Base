package nro.models.boss.list_boss.Broly;

import nro.models.boss.Boss;
import nro.models.boss.BossesData;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.SkillUtil;
import nro.utils.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import nro.models.boss.BossType;

public class Broly extends Boss {

    private long lastTimeDamaged;
    private boolean hasSuperForm;
    private long lastTimeHP;
    private int timeHP;
    private boolean calledBroly;
    private boolean Super;

    public Broly() throws Exception {
        super(BossType.BROLY, BossesData.BROLY_1);
    }

    @Override
    public void reward(Player plKill) {
         plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        try {
            if (this.nPoint.hpg > 1000000) {
                System.out.println("Đã tạo thành công supper broly");
                Boss superBroly = new BrolySuper(this.zone, 2, Util.nextInt(8_000_000, 15_000_000), Util.randomBossId());
            }
        } catch (Exception e) {
            System.out.println("Lỗi tạo supper");
        }

    }

    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 300_000)) {
            //  this.changeStatus(BossStatus.LEAVE_MAP);

        }
        try {
            this.hoiPhuc();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(BrolySuper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void hoiPhuc() throws Exception {
        if (!Util.canDoWithTime(lastTimeHP, timeHP) || !Util.isTrue(1, 100)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
        byte skillId = (byte) Skill.TAI_TAO_NANG_LUONG;
        if (skillId != 0) {
            playerSkill.skills.add(SkillUtil.createSkill(skillId, 7));
            this.chat("Cảm giác rất tốt khi được hồi phục lại năng lượng :)");
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTimeDamaged >= 60_000) {
                double healAmount = nPoint.hpMax * 0.5; // hồi phục 20% HP gốc khi tấn công trong 1 phút

                this.heal(healAmount);

            }

            lastTimeDamaged = currentTime;

            this.nPoint.dameg += this.nPoint.dame * 5 / 100;
            this.nPoint.hpg += this.nPoint.hpg * 5 / 100;
            this.nPoint.critg++;
            this.nPoint.calPoint();
            PlayerService.gI().hoiPhuc(this, this.nPoint.hp, this.nPoint.mp);

            Service.gI().sendThongBao(pl, "Tên broly hắn lại tăng sức mạnh rồi!");
            this.chat(2, "Mọi người cẩn thận sức mạnh hắn ta tăng đột biến..");
            this.chat("Graaaaaa...");
            lastTimeHP = System.currentTimeMillis();
            timeHP = Util.nextInt(5000, 15000);
        }
    }

    public void heal(double amount) {
        nPoint.hp = (long) Math.min(nPoint.hp + amount, nPoint.hpMax);
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!isDie()) {
            calledBroly = false;
        }
        if (isDie()) {
            return 0;
        }
        if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
            this.chat("Xí hụt");
            return 0;
        }

//        byte skillId = (byte) Skill.TAI_TAO_NANG_LUONG;
//        if (skillId != 0) {
//            playerSkill.skills.add(SkillUtil.createSkill(skillId, 7));
//            this.chat("Cảm giác rất tốt khi được hồi phục lại năng lượng :)");
//            long currentTime = System.currentTimeMillis();
//            if (currentTime - lastTimeDamaged >= 60_000) {
//                double healAmount = nPoint.hpMax * 0.2; // hồi phục 20% HP gốc khi tấn công trong 1 phút
//
//                this.heal(healAmount);
//
//            }
//
//            lastTimeDamaged = currentTime;
//        }
        damage = Math.min(damage, nPoint.hpMax * 2 / 100);
        this.nPoint.subHP(damage);

        if (isDie() && !this.calledBroly) {
            try {
                this.calledBroly = true;
//                if (this.nPoint.hpg > 1000000) {
//                    System.out.println("Đã tạo thành công supper broly");
//                    Boss superBroly = new BrolySuper(this.zone, 2, Util.nextInt(8_000_000, 15_000_000), Util.randomBossId());
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            this.setDie(plAtt);
            die(plAtt);
        }
        return damage;
    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }
    private long st;

}
