package nro.models.boss.list_boss.SuKienTrungThu;

//import nro.Log;
import nro.models.boss.*;
import nro.models.item.Item;
import nro.models.map.ItemMap;
//import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.EffectSkillService;
import nro.services.InventoryServiceNew;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class NhatThan extends Boss {

    public boolean changeFlag = false;
    public static int flag = 2; // cờ xanh

    public NhatThan() throws Exception {
        super(BossType.NHAT_THAN, BossesData.NhatThan);
    }
//    @Override
//    public void die(Player plKill) {
//        super.die(plKill);
//        this.parentBoss.changeStatus(BossStatus.DIE);
//    }

    @Override
    public void active() {
        this.attack();
        if (this.location.y < 100) {
            moveTo(this.location.x, 400);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap();
        moveTo(500, 336);

    }

    @Override
    public void reward(Player plKill) {
        plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        if (plKill.isPl()) {
            if (plKill.isUseKiemGo) {
                Item kiemGo = InventoryServiceNew.gI().findItemBag(plKill, 1919);
                if (kiemGo == null) {
                    Service.gI().sendThongBao(plKill, "Bạn không có điếu cày");
                    return;
                }
                InventoryServiceNew.gI().subQuantityItemsBag(plKill, kiemGo, 1);
                plKill.isUseKiemGo = false;
                ItemMap ctNguyetThan = new ItemMap(plKill.zone, 2231 + plKill.gender, 1, plKill.location.x, plKill.location.y, plKill.id);
                ctNguyetThan.options.add(new Item.ItemOption(50, Util.nextInt(10, 58)));
                ctNguyetThan.options.add(new Item.ItemOption(77, Util.nextInt(10, 58)));
                ctNguyetThan.options.add(new Item.ItemOption(103, Util.nextInt(10, 58)));
                ctNguyetThan.options.add(new Item.ItemOption(5, Util.nextInt(10, 58)));
                ctNguyetThan.options.add(new Item.ItemOption(93, Util.nextInt(1, 5)));
                ctNguyetThan.options.add(new Item.ItemOption(38, 1));
                Service.gI().dropItemMap(zone, ctNguyetThan);
            }
        } else {
            Logger.error("Không có quà");
        }
    }

    @Override
    public void attack() {
        if (this == null) {
            return;
        }
        if (!this.changeFlag) {
            Service.gI().changeFlag(this, flag);
            changeFlag = true;
        }
        if (Util.canDoWithTime(this.lastTimeAttack, 1000) && changeFlag) {
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
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(0, 0)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 0));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(0, 0)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 0));
                        }

                    }
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                } else {
                    if (Util.isTrue(3, 10)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override

    public Player getPlayerAttack() {
        Player playerThis = this;
       if (this.getPlayerTarget() != null && (this.getPlayerTarget().isDie() && (!this.isNewPet || !this.isMiniPet) || !this.name.equals("Jajirô") || !this.zone.equals(this.getPlayerTarget().zone))) {
            this.setPlayerTarget(null);
        }
        if (this.getPlayerTarget() == null || Util.canDoWithTime(this.getLastTimeTargetPlayer(), this.getTimeTargetPlayer())) {
            this.setPlayerTarget(this.zone.getPlayerWithFlagNotIsBlue(playerThis));
            this.setLastTimeTargetPlayer(System.currentTimeMillis());
            this.setTimeTargetPlayer(Util.nextInt(5000, 7000));
        }
        if (this.getPlayerTarget() != null) {

        }
        return this.getPlayerTarget();
    }

    @Override
    public void die(Player plKill) {
        super.die(plKill);
        try {
            this.getParentBoss().die(plKill);
        } catch (Exception e) {

        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (plAtt == null) {
                return 0;
            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            nro.services.PlayerService.gI().hoiPhuc(this, 0, Util.TamkjllGH(damage * this.nPoint.voHieuChuong / 100));
                            return 0;
                        }
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }

            if (plAtt != null && plAtt.isBoss) {
                damage = 100;
            } else {
                if (plAtt != null && plAtt.isUseKiemGo) {
                    damage = 100;
                } else {
                    damage = 0;
                }
            }

            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }
            if (plAtt != null && this.nPoint.hp <= 100) {
                damage = 0;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                Logger.error("\nNhật thần đã chết \n");
                setDie(plAtt);
                die(plAtt);

            }
            return damage;
        } else {
            return 0;
        }

    }

}
