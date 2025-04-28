package nro.models.boss.list_boss.doanh_trai;

import java.util.Random;
import nro.consts.ConstPlayer;
import nro.consts.cn;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.BossStatus;
import nro.models.boss.BossType;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class TrungUyTrang extends Boss {

    private long dameClan;
    private long hpClan;

    public TrungUyTrang(long dame, long hp, Zone zone) throws Exception {
        super(BossType.TRUNG_UY_TRANG, TrungUyTrang.TRUNG_UY_TRANG);
        this.dameClan = dame;
        this.hpClan = hp;
        this.zone = zone;
    }

    public TrungUyTrang(long dame, long hp, Zone zone, int id, BossData... data) throws Exception {
        super(id, data);
        this.dameClan = dame;
        this.hpClan = hp;
        this.zone = zone;
    }

    private static final BossData TRUNG_UY_TRANG = new BossData(
            "Trung úy trắng", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{141, 142, 143, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            500, //dame
            new long[]{500}, //hp
            new int[]{1}, //map join
            new int[][]{
                {Skill.MASENKO, 3, 1000},
                {Skill.LIEN_HOAN, 7, 1000}},
            new String[]{"|-1|Hế lô em,anh đứng đây từ chiều",
                "|-1|Mày hiểu thế là sao chứ? Cuối cùng tao đã có thể giết mày!",
                "|-2|Tao lại sợ mày quá cơ,cho bố cái địa chỉ!",
                "|-1|Mày làm tao phấn khích rồi đấy hahaha.."
            }, //text chat 1
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
    public void updateBoss() {
        super.update();
        this.nPoint.mp = this.nPoint.mpg;
        if (this.effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.getBossStatus()) {
            case REST:
                this.rest();
                if (this.id == BossType.TRUNG_UY_TRANG) {
                    System.err.println("RESSSS ");
                }
                break;
            case RESPAWN:
                this.respawn();
                if (this.id == BossType.TRUNG_UY_TRANG) {
                    System.err.println("statusREESPAWN ");
                }
                this.changeStatus(BossStatus.JOIN_MAP);
                if (this.id == BossType.TRUNG_UY_TRANG) {
                    System.err.println("currr statusc :::: " + this.getBossStatus());
                }
                break;
            case JOIN_MAP:
                this.joinMapByZoneWithXY(zone, (short) 853, (short) 384);
                this.zone.isTrungUyTrangAlive = true;
                this.changeStatus(BossStatus.CHAT_S);
                System.out.println("Map: " + this.zone.map.mapId + ", Zone: " + this.zone.zoneId
                        + ", Boss: " + this.name + ", X: " + this.location.x + ", Y: " + this.location.y);
                break;
            case CHAT_S:
                if (chatS()) {
                    this.doneChatS();
                    this.setLastTimeChatS(System.currentTimeMillis());
                    this.setLastTimeChatM(5000);
                    this.changeStatus(BossStatus.ACTIVE);
                }
                break;
            case ACTIVE:
                this.chatM();
                if (this.effectSkill.isCharging && !Util.isTrue(1, 20) || this.effectSkill.useTroi) {
                    return;
                }
                this.active();
                break;
            case DIE:
                this.changeStatus(BossStatus.CHAT_E);
                break;
            case CHAT_E:
                if (chatE()) {
                    this.doneChatE();
                    this.changeStatus(BossStatus.LEAVE_MAP);
                }
                break;
            case LEAVE_MAP:
                this.leaveMap();
                break;
        }
    }

    @Override
    public void active() {
        this.attack();
    }

    private long lastTimeFindPlayerToChangeFlag;

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeFindPlayerToChangeFlag, 500) && this.typePk == ConstPlayer.NON_PK) {
            if (getPlayerAttack() == null) {
                this.lastTimeFindPlayerToChangeFlag = System.currentTimeMillis();
            } else {
                this.changeToTypePK();
            }
        } else if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }

                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= 100) {
                    if (Util.isTrue(5, 20)) {
                        if (SkillUtil.isUseSkillChuong(this)) {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
                        } else {
                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
                        }
                    }
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                } else {
                    this.moveToPlayer(pl);
                }
            } catch (Exception ex) {
                Logger.logException(Boss.class, ex);
            }
        }
    }

    @Override
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
        }
        this.changeStatus(BossStatus.DIE);
    }

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
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 16, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
//        else {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 462, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
//
//        }
        if (Util.isTrue(9, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 457, Util.nextInt(1, 20), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 17, 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (Util.isTrue(1, 10)) {
            generalRewards(pl);
        }

    }

    @Override
    public void leaveMap() {
        this.zone.isTrungUyTrangAlive = false;
        super.leaveMap();
        synchronized (this) {
            BossManager.gI().removeBoss(this);
        }
        this.setBossStatus(null);
        this.setLastZone(null);
        this.setPlayerTarget(null);
        this.setBossAppearTogether(null);
        this.setParentBoss(null);
        this.setZoneFinal(null);
        this.dispose();
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.location.x >= 800 || this.location.x <= 995) {
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            byte move = (byte) Util.nextInt(40, 60);
            PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
        }
    }

    private long lastTimeBlame;

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (this.zone.isbulon14Alive) {
                System.out.println("mob 1");
                if (System.currentTimeMillis() - lastTimeBlame > 3000) {
                    this.chat("Ngu lắm đánh bulon trước đi con zai");
                }
                lastTimeBlame = System.currentTimeMillis();
                return 0;
            }
            if (this.zone.isbulon13Alive) {
                System.out.println("mob 2");
                if (System.currentTimeMillis() - lastTimeBlame > 3000) {
                    this.chat("Ngu lắm đánh bulon trước đi con zai");
                }
                lastTimeBlame = System.currentTimeMillis();
                return 0;
            }
            if (!piercing && Util.isTrue(300, 1000)) {
                this.chat("Xí hụt lêu lêu");
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

}
