package nro.models.boss.list_boss.cell;

import nro.consts.ConstTask;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class Xencon extends Boss {

    private final short x;
    private final short y;
    SieuBoHung sieuBoHung;

    public Xencon(SieuBoHung sieubohung, int XEN_CON_1, BossData XEN_CON1, Zone zone, int x, int y) throws Exception {
        super(BossType.XEN_CON_1, XEN_CON1);
        this.zone = zone;
        this.x = (short) x;
        this.y = (short) y;
        this.sieuBoHung = sieubohung;
    }

    @Override
    public void reward(Player plKill) {
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        if (Util.isTrue(100, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 76, 35000, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public void joinMap() {
        if (this.zone != null && this.x != 0 && this.y != 0) {
            ChangeMapService.gI().changeMap(this, zone, Util.nextInt(x - 70, x + 70), y);
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
    public void leaveMap() {
        super.leaveMap();
        if (sieuBoHung.listXencon.contains(this)) {
            sieuBoHung.listXencon.remove(this);
        }
        synchronized (this) {
            BossManager.gI().removeBoss(this);
        }
        this.dispose();
    }

}
