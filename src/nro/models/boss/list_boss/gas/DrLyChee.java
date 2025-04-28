package nro.models.boss.list_boss.gas;

import nro.models.boss.*;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.EffectSkillService;
import static nro.models.item.ItemTime.KHI_GASS;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class DrLyChee extends Boss {

    private int levell;
    protected Player playerAtt;
    private final long dameClan;
    private final long hpClan;

    public DrLyChee(long dame, long hp, Zone zone) throws Exception {
        super(BossType.DR_LYCHEE, BossesData.DrLyche, BossesData.HaChiJack);
        this.dameClan = dame;
        this.hpClan = hp;
        this.zone = zone;
    }

    @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        if (zone != null) {
            ChangeMapService.gI().changeMap(this, zone, 500, 456);
        }
    }

    @Override
    public void initBase() {
        BossData data = this.getData()[this.getCurrentLevel()];
        this.name = data.getName();
        this.gender = data.getGender();
        this.nPoint.mpg = 23_07_2003;
        this.nPoint.dameg = this.dameClan;
        this.nPoint.hpg = this.hpClan;
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (levell <= 30000) {
            if (Util.isTrue((levell / 1000), 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
                Util.isTrue(this.nPoint.tlNeDon, 1000000);
                damage = 0;
            }
        } else {
            if (Util.isTrue(40, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
                Util.isTrue(this.nPoint.tlNeDon, 1000000);
                damage = 0;
            }

        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage / 2);
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
    public void reward(Player plKill) {
        plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        if (this.getCurrentLevel() == this.getData().length - 1) {
            plKill.clan.gas_haveGone = true;
            plKill.clan.khiGas.timePickReward = true;
            plKill.clan.khiGas.setLastTimeOpen(System.currentTimeMillis() + 30000);
            for (Player pl : plKill.clan.membersInGame) {
                ItemTimeService.gI().removeItemTime(pl, violate);
                ItemTimeService.gI().sendTextTime(pl, (byte) KHI_GASS, "Khí Ga Hủy Diệt: ", 30);
            }
        }
    }

}
