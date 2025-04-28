package nro.models.boss.list_boss.BietDoiGranola;

import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;
import nro.services.EffectSkillService;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.server.Manager;
import nro.services.Service;
import nro.utils.Util;
import java.util.Random;

public class Granona extends Boss {

    public Granona() throws Exception {
        super(BossType.GRANONA, BossesData.GRANONA_1);
    }

    @Override
    public void reward(Player plKill) {
       plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.nr4s.length);
        if (Util.isTrue(40, 100)) {
            if (Util.isTrue(20, 100)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
            } else if (Util.isTrue(50, 100)) {
                ItemMap it = new ItemMap(this.zone, 2032, 1, this.location.x, this.location.y, plKill.id);
                it.options.add(new Item.ItemOption(50, new Random().nextInt(5) + 20));
                it.options.add(new Item.ItemOption(77, new Random().nextInt(10) + 20));
                it.options.add(new Item.ItemOption(103, new Random().nextInt(10) + 20));
                it.options.add(new Item.ItemOption(93, new Random().nextInt(2) + 3));
                Service.gI().dropItemMap(this.zone, it);
            }
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.nr4s[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }generalRewards(plKill);

    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack
    ) {
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
                try {
                    Macki macki = new Macki(zone);
                    OLL oll = new OLL(zone);
                    Gas gas = new Gas(zone);
                    ELec eLec = new ELec(zone);
                    // Logic sử dụng các đối tượng này
                } catch (Exception ex) {
                    ex.printStackTrace(); // Ghi nhận ngoại lệ
                }

                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

}
