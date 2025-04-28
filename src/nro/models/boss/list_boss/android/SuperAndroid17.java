package nro.models.boss.list_boss.android;

import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossStatus;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.server.Manager;
import nro.server.ServerNotify;
import nro.services.EffectSkillService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;
import java.io.IOException;
import java.util.Random;

public class SuperAndroid17 extends Boss {

    private Pic adr17;
    protected boolean isReady;
    public boolean isFusion;
    protected long lastTimeFusion;
    private final int timeToFusion = 5000;
    protected long lastTimecanAttack;
    public boolean canAttack;

    public SuperAndroid17() throws Exception {
        super(BossType.SUPER_ANDROID_17, BossesData.SUPER_ANDROID_17);
        this.adr17 = null;
        this.isReady = false;
        this.isFusion = false;
    }

    @Override
    public void initBase() {
        BossData data = this.getData()[this.getCurrentLevel()];
        this.name = String.format(data.getName(), Util.nextInt(0, 100));
        this.gender = data.getGender();
        this.nPoint.mpg = 23_07_2003;
        this.nPoint.dameg = data.getDame();
        this.nPoint.hpg = data.getHp()[Util.nextInt(0, data.getHp().length - 1)];
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.defg = (short) (this.nPoint.hpg / 100000);
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

    public void createSmallBoss() {
        try {
            this.adr17 = new Pic(this, this.zone, (short) this.location.x, (short) this.location.y, BossesData.PIC_TA_AC);
        } catch (Exception ex) {

        }
    }

    public void hoptheAdr() {
        if (this.adr17 != null && this.adr17.typePk == ConstPlayer.NON_PK && this.adr17.isReady
                && this.typePk == ConstPlayer.NON_PK && this.isReady && !this.isFusion) {
            if (Util.canDoWithTime(lastTimeFusion, this.timeToFusion)) {
                this.isFusion = true;
                setBaseFusion();
                fusion(false);
                ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
            }
        }
    }

    private void setBaseFusion() {
        BossData data = this.getData()[this.getCurrentLevel()];
        this.name = "Super Android 17";
        this.nPoint.mpg = 23_07_2003;
        this.nPoint.dameg = 100_100;
        this.nPoint.hpg = data.getHp()[Util.nextInt(0, data.getHp().length - 1)] + 1_000_000_000;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

    public void fusion(boolean porata) {
        ChangeMapService.gI().exitMap(this.adr17);
        this.adr17.leaveMap();
        this.adr17 = null;
        this.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
        fusionEffect(this.fusion.typeFusion);
        this.location.y = this.zone.map.yPhysicInTop(this.location.x, this.location.y);
        ChangeMapService.gI().changeZone(this, this.zone.zoneId);
        Service.gI().Send_Caitrang(this);
        PlayerService.gI().hoiPhuc(this, this.nPoint.hpMax, 0);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo1 = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        if (Util.isTrue(20, 100)) {
            ItemMap it = new ItemMap(this.zone, Manager.itemIds_TL[randomDo1], 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        } else {
            int itDropCount = 0;
            int num = Util.isTrue(50, 100) ? 3 : 5;
            while (itDropCount < num) {
                ItemMap it = new ItemMap(this.zone, 2031, 1, this.location.x - (itDropCount * 30), this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                Service.gI().dropItemMap(this.zone, it);
                itDropCount++;
            }
        }
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, 76, Util.nextInt(10000, 30000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, 76, Util.nextInt(10000, 30000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, 76, Util.nextInt(10000, 30000), Util.nextInt(this.location.x - 20, this.location.x + 20), zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
    }

    private void fusionEffect(int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) this.id);
            Service.gI().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
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
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (damage >= this.nPoint.hp && !this.isReady && this.adr17 != null) {
                this.changeToTypeNonPK();
                this.nPoint.hp = 1;
                this.isReady = true;
                this.effectSkill.removeSkillEffectWhenDie();
                if (this.adr17.isReady) {
                    this.lastTimeFusion = System.currentTimeMillis();
                    this.lastTimecanAttack = System.currentTimeMillis();
                }
                return 0;
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
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
            //ServerNotify.gI().notify(plKill.name + " vừa tiêu diệt được " + this.name);
        }
        this.fusion.typeFusion = ConstPlayer.NON_FUSION;
        this.isFusion = false;
        this.isReady = false;
        this.canAttack = false;
        this.changeStatus(BossStatus.DIE);
    }

    @Override
    public void active() {
        hoptheAdr();
        if (this.typePk == ConstPlayer.NON_PK && !isReady) {
            this.changeToTypePK();
            return;
        }
        if (this.adr17 == null && Util.canDoWithTime(lastTimecanAttack, timeToFusion * 2) && !this.canAttack) {
            this.changeToTypePK();
            this.canAttack = true;
            return;
        }
        this.attack();
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        this.createSmallBoss();
        st = System.currentTimeMillis();
    }
    private long st;

}
