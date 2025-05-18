/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nro.models.boss.list_boss.HuyetMa;

import nro.models.boss.Boss;
import nro.models.boss.BossType;
//import nro.models.boss.BossID;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;
import java.util.Random;
import nro.consts.ConstPlayer;
import nro.models.boss.BossStatus;
import nro.services.ItemMapService;
import nro.utils.AddOrIncrease;
import nro.utils.Logger;

/**
 *
 * @author Administrator
 */
/**
 *
 * @author Administrator
 */
public class HuyetMa extends Boss {

    public HuyetMa() throws Exception {
        super(BossType.HuyetMa, BossesData.HuyetMa);
    }

//   @Override
//    public void reward(Player pl) {
//        pl.event.addEventPointBoss(1);
//        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
//        if (Util.isTrue(99, 100)) {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1680, 1000, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
//        }
//    }
    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");

        int itemId = 1680;
        int quantity = 1000;

        // Gọi hàm đồng bộ
        if (!AddOrIncrease.addItemDropToBag(pl.inventory.itemsBag, itemId, quantity)) {
            // Nếu không cộng dồn được -> rơi xuống đất
            Service.gI().dropItemMap(this.zone,
                    new ItemMap(zone, itemId, quantity, this.location.x + 6,
                            zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1710, Util.nextInt(1, 3), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

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
            Logger.logException(HuyetMa.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(40, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                        damage = damage / 2;
                    case Skill.LIEN_HOAN:
                        damage = damage * 75 / 100;
                    case Skill.MASENKO:
                        damage = damage * 130 / 100;
                    case Skill.GALICK:
                        damage = damage * 70 / 100;
                }
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
}
