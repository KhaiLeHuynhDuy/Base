package nro.model.boss.list_boss.Yadart;

import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossStatus;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.utils.Util;
import java.util.Random;

public class Yadart extends Boss {

    public Yadart() throws Exception {
        super(BossType.YADART, BossesData.YADART);
    }

    @Override
    public void active() {
        super.active();
        if (Util.canDoWithTime(st, 300000)) {

        }
    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }
    private long st;

    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }

    @Override
    public void reward(Player plKill) {
        super.reward(plKill);
        if (this.currentLevel == 1) {
            return;
        }

        // Kiểm tra nếu đã trôi qua khoảng thời gian 1-3 phút thì không rớt item
        long timeElapsed = System.currentTimeMillis() - st; // Tính toán thời gian kể từ khi boss tham gia vào map
        if (timeElapsed < 60000 || timeElapsed > 180000) { // Khoảng thời gian là 1-3 phút, tương đương 60.000 - 180.000ms
            return;
        }

        int itemId = 590;
        int quantity = new Random().nextInt(20) + 1;

        Service.gI().dropItemMap(this.zone, new ItemMap(zone, itemId, quantity,
                this.location.x, this.location.y, plKill.id));
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(70, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (damage >= 5000000) {
                damage = 5000000;
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
