package nro.models.boss.list_boss.Mabu12h;

import nro.consts.ConstTask;
import nro.consts.cn;
import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

import java.util.Random;

public class Drabura2 extends Boss {

    private long lastTimeHoaDaPlayer;
    private int timeHoaDa = 30000;

    public Drabura2() throws Exception {
        super(BossType.DRABURA_2, BossesData.DRABURA_2);
    }

    @Override
    public void active() {
        super.active();
//        if (Util.canDoWithTime(lastTimeHoaDaPlayer, timeHoaDa)) {
//            for (Player player : this.zone.getPlayers()) {
//                player.nPoint.IsBiHoaDa = true;
//                Service.gI().SendMsgUpdateHoaDa(player, (byte) 1, (byte) 0, (byte) 42);
//                Service.gI().Send_Caitrang(player);
//                ItemTimeService.gI().sendItemTime(player, 4392, (int) player.effectSkin.getTIME_HOA_DA() / 1000);
//                player.effectSkin.lastTimeBiHoaDa = System.currentTimeMillis();
//            }
//            this.lastTimeHoaDaPlayer = System.currentTimeMillis();
//        }
    }

   @Override
    public void reward(Player plKill) {
       plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        //byte randomc12 = (byte) new Random().nextInt(Manager.itemDC12.length - 1);

        if (Util.isTrue(1, 130)) {
            if (Util.isTrue(1, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1135, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }
        if (Util.isTrue(cn.tileroinr, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
        plKill.fightMabu.changePoint((byte) 40);
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
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
