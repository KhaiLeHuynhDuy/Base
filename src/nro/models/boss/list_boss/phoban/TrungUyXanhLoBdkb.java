package nro.models.boss.list_boss.phoban;

import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.BossType;
import static nro.models.item.ItemTime.BAN_DO_KHO_BAU;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.ItemTimeService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.SkillUtil;
import nro.utils.Util;
public class TrungUyXanhLoBdkb extends Boss {

    protected long dameClan;
    protected long hpClan;

    public TrungUyXanhLoBdkb(long dame, long hp, Zone zone) throws Exception {
        super(BossType.TRUNG_UY_XANH_LO, TRUNG_UY_XANH_LO);
        this.dameClan = dame;
        this.hpClan = hp;
        this.zone = zone;
    }

    private static final BossData TRUNG_UY_XANH_LO = new BossData(
            "Trung úy xanh lơ", //name
            ConstPlayer.TRAI_DAT, //gender
            new short[]{135, 136, 137, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            500, //dame
            new long[]{500}, //hp
            new int[]{1}, //map join
            new int[][]{
                {Skill.DRAGON, 3, 1000},
                {Skill.KAMEJOKO, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 3, 13000}},
            new String[]{}, //text chat 1
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
        this.name = String.format(data.getName(), Util.nextInt(0, 100));
        this.gender = data.getGender();
        this.nPoint.mpg = 23_07_2003;
        this.nPoint.dameg = this.dameClan;
        this.nPoint.hpg = this.hpClan;
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }
    private long lastTimeFindPlayerToChangeFlag;

    @Override
    public void active() {
        if (Util.canDoWithTime(this.lastTimeFindPlayerToChangeFlag, 500) && this.typePk == ConstPlayer.NON_PK) {
            if (getPlayerAttack() == null) {
                this.lastTimeFindPlayerToChangeFlag = System.currentTimeMillis();
            } else {
                this.changeToTypePK();
            }
        }
        this.attack();
    }

    @Override
    public void joinMap() {
        if (zone != null) {
            ChangeMapService.gI().changeMap(this, zone, 222, 456);
            System.out.println("Spawn TUXL BDKB");
        }
    }

    @Override
    public Player getPlayerAttack() {
        if (this.getPlayerTarget() != null && (this.getPlayerTarget().isDie() || !this.zone.equals(this.getPlayerTarget().zone))) {
            this.setPlayerTarget(null);
        }
        if (this.getPlayerTarget() == null || Util.canDoWithTime(this.getTimeTargetPlayer(), this.getTimeTargetPlayer())) {
            this.setPlayerTarget(this.zone.getRandomPlayerInMap());
            this.setLastTimeTargetPlayer(System.currentTimeMillis());
            this.setTimeTargetPlayer(Util.nextInt(5000, 7000));
        }
        if (this.getPlayerTarget() != null && this.getPlayerTarget().effectSkin != null && this.getPlayerTarget().effectSkin.isVoHinh) {
            this.setPlayerTarget(null);
            this.setLastTimeTargetPlayer(System.currentTimeMillis());
            this.setTimeTargetPlayer(Util.nextInt(1000, 2000));
        }
        if (this.getPlayerTarget() == this.pet) {
            this.setPlayerTarget(null);
            this.setLastTimeTargetPlayer(System.currentTimeMillis());
            this.setTimeTargetPlayer(Util.nextInt(1000, 2000));
        }
        if (this.getPlayerTarget() != null && this.typePk == ConstPlayer.NON_PK) {
            if ((this.getPlayerTarget().location.x > -50 || this.getPlayerTarget().location.x < 500) && this.getPlayerTarget().location.y < 300) {
                this.setPlayerTarget(null);
                this.setLastTimeTargetPlayer(System.currentTimeMillis());
                this.setTimeTargetPlayer(Util.nextInt(5000, 7000));
            }
            if (this.getPlayerTarget() != null && this.getPlayerTarget().location.y > 300 && this.getPlayerTarget().location.x > 500) {
                this.setPlayerTarget(null);
                this.setLastTimeTargetPlayer(System.currentTimeMillis());
                this.setTimeTargetPlayer(Util.nextInt(5000, 7000));
            }

        }
        return this.getPlayerTarget();
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= 100) {
                    if (Util.isTrue(5, 10)) {
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
            }
        }
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(50, 100);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -25 : 0));
    }

    @Override
    public void leaveMap() {
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
    public void reward(Player plKill) {
        plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        plKill.clan.banDoKhoBau_haveGone = true;
        plKill.clan.banDoKhoBau.timePickReward = true;
        plKill.clan.banDoKhoBau.setLastTimeOpen(System.currentTimeMillis() + 30000);
        for (Player pl : plKill.clan.membersInGame) {
            ItemTimeService.gI().removeItemTime(pl, violate);
            ItemTimeService.gI().sendTextTime(pl, (byte) BAN_DO_KHO_BAU, "Bản đồ kho báu sắp kết thúc : ", 30);
        }
    }
}
