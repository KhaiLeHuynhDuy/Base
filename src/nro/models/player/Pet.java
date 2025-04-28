package nro.models.player;

import nro.consts.ConstPlayer;
import nro.models.item.Item;
import nro.services.MapService;
import nro.models.mob.Mob;
import nro.models.skill.Skill;
import nro.utils.SkillUtil;
import nro.services.Service;
import nro.utils.Util;
import nro.network.io.Message;
import nro.server.Manager;
import nro.services.ItemTimeService;
import nro.services.PlayerService;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Logger;
import nro.utils.TimeUtil;
import java.util.ArrayList;
import java.util.List;

public class Pet extends Player {
// private static final short ARANGE_FIND_ATTACK = 200;

    private static final short ARANGE_CAN_ATTACK = 300;
    private static final short ARANGE_ATT_SKILL1 = 60;
    public List<Player> enemies2 = new ArrayList<>();
//    private List<Player> players;
    private static final short[][] PET_ID = {{285, 286, 287}, {288, 289, 290}, {282, 283, 284}, {304, 305, 303}};

    public static final byte FOLLOW = 0;
    public static final byte PROTECT = 1;
    public static final byte ATTACK = 2;
    public static final byte GOHOME = 3;
    public static final byte FUSION = 4;

    public Player master;
    public byte status = 0;

    public byte typePet;
    public boolean isTransform;

    public long lastTimeDie;

    private boolean goingHome;
//    private long lastTimeDie = -1;
//    private long lastTimeUp;
//    private long lastTimeHome;
    private long lastTimeAskPea;
//    private boolean goingHome;
    private Mob mobAttack;
    private Player playerAttack;

    private static final int TIME_WAIT_AFTER_UNFUSION = 5000;
    private long lastTimeUnfusion;

    public byte getStatus() {
        return this.status;
    }

    public Pet(Player master) {
        this.master = master;
        this.isPet = true;
    }

    public void changeStatus(byte status) {
        if (goingHome || master.fusion.typeFusion != 0 || (this.isDie() && status == FUSION)) {
            Service.gI().sendThongBao(master, "Đệ của bạn đang ở nhà hoặc đã chết mất rồi");
            return;
        }
        Service.gI().chatJustForMe(master, this, getTextStatus(status));
        if (status == GOHOME) {
            goHome();
        } else if (status == FUSION) {
            fusion(false);
        }
        this.status = status;
    }

    public void joinMapMaster() {
        if (status != GOHOME && status != FUSION && !isDie()) {
            this.location.x = master.location.x + Util.nextInt(-10, 10);
            this.location.y = master.location.y;
            ChangeMapService.gI().goToMap(this, master.zone);
            if (this.zone != null) {
                this.zone.load_Me_To_Another(this);
            }
        }
    }

    public void goHome() {
        if (this.status == GOHOME) {
            return;
        }

        goingHome = true;
        new Thread(() -> {
            try {
                Pet.this.status = Pet.ATTACK;
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.master != null) {
                ChangeMapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, master.gender + 21, -1));
            } else {
                Service.gI().sendThongBao(this, "Có lỗi xảy ra với nhân vật chính :)");
            }
            if (this.zone == null) {
                return;
            }
            this.zone.load_Me_To_Another(this);
            Pet.this.status = Pet.GOHOME;
            goingHome = false;
        }).start();
    }

    private String getTextStatus(byte status) {
        switch (status) {
            case FOLLOW:
                return "Ok con theo sư phụ";
            case PROTECT:
                return "Ok con sẽ bảo vệ sư phụ";
            case ATTACK:
                return "Ok con sẽ đi đám bọn nó";
            case GOHOME:
                return "Ok con về nhà đây";
            default:
                return "";
        }
    }

    public void fusion(boolean porata) {
        if (this.isDie()) {
            Service.gI().sendThongBao(master, "Đệ cu chết rồi hợp thể chóa giề");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.gI().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.gI().point(master);
        } else {
            Service.gI().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion2(boolean porata2) {
        if (this.isDie()) {
            Service.gI().sendThongBao(master, "Đệ cu chết rồi hợp thể chóa giề");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata2) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA2;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                        Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.gI().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.gI().point(master);
        } else {
            Service.gI().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion3(boolean porata3) {
        if (this.isDie()) {
            Service.gI().sendThongBao(master, "Đệ cu chết rồi hợp thể chóa giề");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata3) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA3;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                        Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.gI().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.gI().point(master);
        } else {
            Service.gI().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion4(boolean porata4) {
        if (this.isDie()) {
            Service.gI().sendThongBao(master, "Đệ cu chết rồi hợp thể chóa giề");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata4) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA4;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                        Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.gI().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.gI().point(master);
            if (master.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                Item item = master.inventory.itemsBody.get(5);
                Item petItem = this.inventory.itemsBody.get(5);
                boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1229 || item.template.id == 1230);
                boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1229 || petItem.template.id == 1230);
                boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;
                if (hasItem1 && hasItem2 && !sameItem) {
                    System.out.println("ok hopthe");
                    SkillService.gI().sendPlayerPrepareBom(master, 2000);
                }
                boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1247 || item.template.id == 1230);
                boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1247 || petItem.template.id == 1230);
                boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;
                if (hasItem11 && hasItem21 && !sameItem1) {
                    System.out.println("ok hopthe2");
                    SkillService.gI().sendPlayerPrepareBom(master, 2000);
                }
            }
        } else {
            Service.gI().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion5(boolean porata4) {
        if (this.isDie()) {
            Service.gI().sendThongBao(master, "Đệ cu chết rồi hợp thể chóa giề");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata4) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA5;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                        Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.gI().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.gI().point(master);
            if (master.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                Item item = master.inventory.itemsBody.get(5);
                Item petItem = this.inventory.itemsBody.get(5);
                boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1229 || item.template.id == 1230);
                boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1229 || petItem.template.id == 1230);
                boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;
                if (hasItem1 && hasItem2 && !sameItem) {
                    System.out.println("ok hopthe 555");
                    SkillService.gI().sendPlayerPrepareBom(master, 2000);
                }
                boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1248 || item.template.id == 1230);
                boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1248 || petItem.template.id == 1230);
                boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;
                if (hasItem11 && hasItem21 && !sameItem1) {
                    System.out.println("ok hopthe 5");
                    SkillService.gI().sendPlayerPrepareBom(master, 2000);
                }
            }
        } else {
            Service.gI().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion6(boolean porata4) {
        if (this.isDie()) {
            Service.gI().sendThongBao(master, "Đệ cu chết rồi hợp thể chóa giề");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata4) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA6;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                        Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            ChangeMapService.gI().exitMap(this);
            fusionEffect(master.fusion.typeFusion);
            Service.gI().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.gI().point(master);
            if (master.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                Item item = master.inventory.itemsBody.get(5);
                Item petItem = this.inventory.itemsBody.get(5);
                boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1470 || item.template.id == 1468);
                boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1470 || petItem.template.id == 1468);
                boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;
                if (hasItem1 && hasItem2 && !sameItem) {
                    System.out.println("ok hopthe 666");
                    SkillService.gI().sendPlayerPrepareBom(master, 2000);
                }
            }
        } else {
            Service.gI().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void unFusion() {
        master.fusion.typeFusion = 0;
        this.status = PROTECT;
        Service.gI().point(master);
        joinMapMaster();
        fusionEffect(master.fusion.typeFusion);
        Service.gI().Send_Caitrang(master);
        Service.gI().point(master);
        this.lastTimeUnfusion = System.currentTimeMillis();
    }

    private void fusionEffect(int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) master.id);
            Service.gI().sendMessAllPlayerInMap(master.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    public Player getPlayerEnimeInMap() {
        int dis = ARANGE_CAN_ATTACK;
        Player mobAtt = null;
        for (Player pl : zone.getHumanoids()) {
            if (pl.isDie()) {
                continue;
            }
            if (enemies2.contains(pl)) {
                int d = Util.getDistance(this, pl);
                if (d <= dis) {
                    dis = d;
                    mobAtt = pl;
                }
            }
        }
        return mobAtt;
    }

    private void moveIdle() {
        if (status == GOHOME || status == FUSION) {
            return;
        }
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    private long lastTimeMoveAtHome;
    private byte directAtHome = -1;

    private boolean canAttackPlayer(Player p1, Player p2) {
        if (p1.isDie() || p2.isDie()) {
            return false;
        }

        if (p1.typePk == ConstPlayer.PK_ALL || p2.typePk == ConstPlayer.PK_ALL) {
            return true;
        }
        if ((p1.cFlag != 0 && p2.cFlag != 0)
                && (p1.cFlag == 8 || p2.cFlag == 8 || p1.cFlag != p2.cFlag)) {
            return true;
        }
        if (p1.pvp == null || p2.pvp == null) {
            return false;
        }
        if (p1.pvp.isInPVP(p2) || p2.pvp.isInPVP(p1)) {
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        try {
            super.update();
            increasePoint(); //cộng chỉ số
            updatePower(); //check mở skill...
            if (isDie()) {
                if (System.currentTimeMillis() - lastTimeDie > 50000) {
                    if (this.nPoint != null) {
                        Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                    }
                } else {
                    return;
                }
            }

            if (justRevived && this.zone == master.zone) {
                Service.gI().chatJustForMe(master, this, "Sư phụ ơi, con đây nè!");
                justRevived = false;
            }

            if (this.zone == null || this.zone != master.zone) {
                joinMapMaster();
            }
            if (this.master != null && master.isDie() || this.isDie() || effectSkill.isHaveEffectSkill()) {
                return;
            }

            moveIdle();
            switch (status) {
                case FOLLOW:
                    followMaster(60);
                    break;                                                                  
//                                                                             
                case PROTECT:
                    if (useSkill3() || useSkill4()) {
                        break;                                                                  
                    }
                    boolean attackPl = false;
                    if (this.zone != null) {
                        List<Player> plist = new ArrayList<>(this.zone.getHumanoids());

                        synchronized (plist) {
                            for (Player pl : plist) {
                                if (pl != null) {
                                    synchronized (pl) {
                                        if (pl != null && this.enemies2.contains(pl) && !pl.isNewPet && !pl.isMiniPet) {
                                            if (pl.isDie()) {

                                            } else {
                                                // followMaster(60);
                                                int dis = ARANGE_CAN_ATTACK;
                                                int d = Util.getDistance(this, pl);
                                                if (d <= dis) {
                                                    dis = d;
                                                    if (canAttackPlayer(this, pl)) {
                                                        attackPl = true;

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (attackPl) {
                        List<Player> kethu = new ArrayList<>(this.enemies2);
                        if (kethu != null && kethu.size() > 0) {

                            Player kethugannhat = getPlayerEnimeInMap();
                            if (kethugannhat != null) {

                                int disToMob = Util.getDistance(this, kethugannhat);
                                if (disToMob <= ARANGE_ATT_SKILL1) {
                                    //đấm
                                    this.playerSkill.skillSelect = getSkill(1);
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            PlayerService.gI().playerMove(this, kethugannhat.location.x + Util.nextInt(-40, 40), kethugannhat.location.y);

                                            SkillService.gI().useSkill(this, kethugannhat, null, null);
                                        } else {
                                            askPea();
                                        }
                                    }
                                } else {
                                    //chưởng
                                    this.playerSkill.skillSelect = getSkill(2);

                                    if (this.playerSkill.skillSelect.skillId != -1) {
                                        if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                            if (SkillService.gI().canUseSkillWithMana(this)) {
                                                SkillService.gI().useSkill(this, kethugannhat, mobAttack, null);
                                            } else {
                                                askPea();
                                            }
                                        }
                                    }
                                }
                            } else {
                                idle = true;
                            }
                        }
                    } else {
                        mobAttack = findMobAttack();
                        if (mobAttack != null) {
                            int disToMob = Util.getDistance(this, mobAttack);
                            if (disToMob <= ARANGE_ATT_SKILL1) {
                                //đấm
                                this.playerSkill.skillSelect = getSkill(1);
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
//                                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            } else {
                                //chưởng
                                this.playerSkill.skillSelect = getSkill(2);

                                if (this.playerSkill.skillSelect.skillId != -1) {
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {

                                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                        } else {
                                            askPea();
                                        }
                                    }
                                }
                            }

                        } else {
                            idle = true;
                        }
                    }
                    break;                                                                  
                case ATTACK:
                    if (useSkill3() || useSkill4()) {
                        break;                                                                  
                    }
                    boolean attackPl2 = false;
                    if (this.zone != null) {
                        List<Player> plist2 = new ArrayList<>(this.zone.getHumanoids());
                        synchronized (plist2) {
                            for (Player pl : plist2) {
                                if (pl != null) {
                                    synchronized (pl) {
                                        if (pl != null && this.enemies2.contains(pl) && !pl.isNewPet && !pl.isMiniPet) {
                                            if (pl.isDie()) {

                                            } else {
                                                //  followMaster(60);
                                                int dis = ARANGE_CAN_ATTACK;
                                                int d = Util.getDistance(this, pl);
                                                if (d <= dis) {
                                                    dis = d;
                                                    if (SkillService.gI().canAttackPlayer(this, pl)) {
                                                        attackPl2 = true;
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (attackPl2) {
                        List<Player> kethu = new ArrayList<>(this.enemies2);
//                        System.out.println("detu ghim ke thu");
                        if (kethu != null) {
//                            System.out.println("2");
                            Player kethugannhat = getPlayerEnimeInMap();
//                            System.out.println("Kẻ thù gần nhất: " + kethugannhat.name);
                            if (kethugannhat != null) {
//                                System.out.println("3");
                                int disToMob = 0;
                                if (mobAttack != null) {
                                    disToMob = Util.getDistance(this, mobAttack);
                                }
                                if (disToMob <= ARANGE_ATT_SKILL1) {
                                    this.playerSkill.skillSelect = getSkill(1);
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            PlayerService.gI().playerMove(this, kethugannhat.location.x + Util.nextInt(-20, 20), kethugannhat.location.y);
                                            SkillService.gI().useSkill(this, kethugannhat, null, null);
                                        } else {
                                            askPea();
                                        }
                                    }
                                } else {
                                    this.playerSkill.skillSelect = getSkill(2);
                                    if (this.playerSkill.skillSelect.skillId != -1) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            SkillService.gI().useSkill(this, kethugannhat, null, null);
                                        }
                                    } else {
                                        this.playerSkill.skillSelect = getSkill(1);
                                        if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                            if (SkillService.gI().canUseSkillWithMana(this)) {
                                                PlayerService.gI().playerMove(this, kethugannhat.location.x + Util.nextInt(-20, 20), kethugannhat.location.y);
                                                SkillService.gI().useSkill(this, kethugannhat, null, null);
                                            } else {
                                                askPea();
                                            }
                                        }
                                    }
                                }
                            } else {
                                idle = true;
                            }
                        }
                    } else {
                        mobAttack = findMobAttack();
                        if (mobAttack != null) {
                            int disToMob = Util.getDistance(this, mobAttack);
                            if (disToMob <= ARANGE_ATT_SKILL1) {
                                this.playerSkill.skillSelect = getSkill(1);
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
//                                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            } else {
                                this.playerSkill.skillSelect = getSkill(2);
                                if (this.playerSkill.skillSelect.skillId != -1) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    }
                                } else {
                                    this.playerSkill.skillSelect = getSkill(1);
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
//                                            PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                            PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                        } else {
                                            askPea();
                                        }
                                    }
                                }
                            }

                        } else {
                            idle = true;
                        }
                    }
                    break;                                                                  
                case GOHOME:
                    if (this.zone != null && (this.zone.map.mapId == 21 || this.zone.map.mapId == 22 || this.zone.map.mapId == 23)) {
                        if (System.currentTimeMillis() - lastTimeMoveAtHome <= 5000) {
                            return;
                        } else {
                            if (this.zone.map.mapId == 21) {
                                if (directAtHome == -1) {

                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 500, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 452, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 23) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            }
                            Service.gI().chatJustForMe(master, this, "Bibi sư phụ !");
                            lastTimeMoveAtHome = System.currentTimeMillis();
                        }
                    }
                    break;                                                                  
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Pet.class, e);
        }
    }

//    private long lastTimeAskPea;
    public void askPea() {
        if (Util.canDoWithTime(lastTimeAskPea, 10000)) {
            Service.gI().chatJustForMe(master, this, "Sư phụ ơi cho con đậu thần đi, con đói sắp chết rồi !!");
            lastTimeAskPea = System.currentTimeMillis();
        }
    }

    private int countTTNL;

    private boolean useSkill3() {
        try {

            playerSkill.skillSelect = getSkill(3);
            if (playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.THAI_DUONG_HA_SAN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.gI().chatJustForMe(master, this, "Yaaaaaaaaa");
                        return true;
                    }
                    return false;
                case Skill.TAI_TAO_NANG_LUONG:
                    if (this.effectSkill.isCharging && this.countTTNL < Util.nextInt(3, 5)) {
                        this.countTTNL++;
                        return true;
                    }
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)
                            && (this.nPoint.getCurrPercentHP() <= 20 || this.nPoint.getCurrPercentMP() <= 20)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        this.countTTNL = 0;
                        return true;
                    }
                    return false;
                case Skill.KAIOKEN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        mobAttack = this.findMobAttack();
                        if (mobAttack == null) {
                            return false;
                        }
                        int dis = Util.getDistance(this, mobAttack);
                        if (dis > ARANGE_ATT_SKILL1) {
                            PlayerService.gI().playerMove(this, mobAttack.location.x, mobAttack.location.y);
                        } else {
                            if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        getSkill(1).lastTimeUseThisSkill = System.currentTimeMillis();
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean useSkill4() {
        try {
            this.playerSkill.skillSelect = getSkill(4);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.BIEN_KHI:
                    if (!this.effectSkill.isMonkey && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.KHIEN_NANG_LUONG:
                    if (!this.effectSkill.isShielding && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.DE_TRUNG:
                    if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//========================BETA SKILL5=====================
    private boolean useSkill5() {
        try {
            this.playerSkill.skillSelect = getSkill(5);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.QUA_CAU_KENH_KHI:
                    if (!this.effectSkill.isThoiMien && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.TROI:
                    if (!this.effectSkill.isBlindDCTT && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.SOCOLA:
                    if (this.effectSkill.isSocola && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;

                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //====================================================
    private long lastTimeIncreasePoint;

//    private void increasePoint() {
//        if (this.nPoint != null && Util.canDoWithTime(lastTimeIncreasePoint, 60)) {
//            if (Util.isTrue(5, 100)) {
//                this.nPoint.increasePoint((byte) 2, (short) Util.nextInt(1, 20));
//            } else {
//                byte type = (byte) Util.nextInt(0, 4);
//                short point = (short) Util.nextInt(Manager.RATE_EXP_SERVER);
//                this.nPoint.increasePoint(type, point);
//            }
//            lastTimeIncreasePoint = System.currentTimeMillis();
//        }
//    }
    private void increasePoint() {
        if (this.nPoint != null && Util.canDoWithTime(lastTimeIncreasePoint, 60)) {
            if (status != FUSION) {
                int tn = 2;
                if (this.master.itemTime != null && this.master.itemTime.isLoNuocThanhx2) {
                    tn = 4;
                }
                if (this.master.itemTime != null && this.master.itemTime.isLoNuocThanhx5) {
                    tn = 10;
                }
                if (this.master.itemTime != null && this.master.itemTime.isLoNuocThanhx7) {
                    tn = 14;
                }
                if (this.master.itemTime != null && this.master.itemTime.isLoNuocThanhx10) {
                    tn = 20;
                }
                if (this.master.itemTime != null && this.master.itemTime.isLoNuocThanhx15) {
                    tn = 30;
                }
                if (Util.isTrue(95, 100)) {
                    this.nPoint.increasePoint((byte) Util.nextInt(0, 4), (short) Util.nextInt(tn, tn * 3), false);
                } else {
                    this.nPoint.increasePoint((byte) Util.nextInt(0, 2), (short) Util.nextInt(tn, tn * 3), false);
                }
                lastTimeIncreasePoint = System.currentTimeMillis();
            }
        }
    }

    public void followMaster() {
        if (this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.status) {
            case ATTACK:
                if ((mobAttack != null && Util.getDistance(this, master) <= 1500)) {
                    break;                                                                  
                }
            case FOLLOW:
            case PROTECT:
                followMaster(60);
                break;                                                                  
        }
    }

    private void followMaster(int dis) {
        int mX = master.location.x;
        int mY = master.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }

    public short getAvatar() {
        switch (this.typePet) {
            case 1:
                //mabu 27 50
                return 297;
            case 2:
                //berrus 53 79
                return 508;
            case 3:
                //pic 46 40
                return 237;
            case 4:
                //cell 33 50
                return 234;
            case 5:
                //cumber 56 50
                return 2030;
            case 6:
                //fide gold 31 54
                return 502;
            case 7:
                //heart 21 71
                return 2090;
            case 8:
                //mai 41 56
                return 2116;
            case 9:
                //gohanbeast 45 78
                return 2119;
            case 10:
                //jiren 64 79
                return 2125;
            case 11:
                //black 3 70 87
                return 2137;
            case 12:
                //goku4 84 84
                return 2122;
            case 13:
                //goku ultra 85 85
                return 2072;
            case 14:
                //berrus bis 67 106
                return 754;
            case 15:
                //zamasu 98 83
                return 2093;
            case 16:
                //daissinkhan 120 123
                return 703;
            case 17:
                //wwhis 101 115
                return 838;
            case 18:
                //granlola 130 125
                return 2018;
            case 19:
                //Kamin 67 106
                return 2033;
            case 20:
                //oren 68 68
                return 2036;
            case 21:
                //kamin oren 120 123
                return 2039;
            case 22:
                //gojo 57 129
                return 2075;
            case 23:
                //hachiyack 135 135
                return 639;
            case 24:
                //ultra Ego 155 155
                return 2140;
            case 25:
                //drabura 37 121
                return 1248;
            case 26:
                //goku ss5  139 139
                return 2143;
            case 27:
                //baby jiren 30 159
                return 876;
            case 28:
                //Baby Vegeta 133 150
                return 2146;
            case 29:
                //Zamas 2 140 140
                return 2149;
            case 30:
                //Kafula 108 145
                return 2152;
            case 31:
                //Kafula 108 145
                return 1333;
            case 32:
                //Kafula 108 145
                return 1336;
            case 33:
                //Mabu Gay
                return 1384;
            case 34:
                //kamin oren 120 123
                return 1411;
            case 35:
                //gojo 57 129
                return 1346;
            case 36:
                //hachiyack 135 135
                return 1399;
            case 37:
                //ultra Ego 155 155
                return 1402;
            case 38:
                //drabura 37 121
                return 1405;
            case 39:
                //goku ss5  139 139
                return 1408;
            case 40:
                //baby jiren 30 159
                return 1224;
            case 41:
                //Baby Vegeta 133 150
                return 1420;

            default:
                return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getHead() {
        if (this.itemTime != null && this.itemTime.isBienHinhMa) {
//            //            if (Util.isTrue(80, 100)) {
//            if (this.gender == 0) {
//                return 1753;
//            }
//            if (this.gender == 1) {
//                return 1755;
//            }
//            if (this.gender == 2) {
//                return 1757;
//            }
//            } else {
                return 1778;
//            }
        }
        if (this.effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (this.effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 297;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 508;
        } else if (this.typePet == 3 && !this.isTransform) {
            return 237;
        } else if (this.typePet == 4 && !this.isTransform) {
            return 234;
        } else if (this.typePet == 5 && !this.isTransform) {
            return 2030;
        } else if (this.typePet == 6 && !this.isTransform) {
            return 502;
        } else if (this.typePet == 7 && !this.isTransform) {
            return 2090;

        } else if (this.typePet == 8 && !this.isTransform) {
            return 2116;

        } else if (this.typePet == 9 && !this.isTransform) {
            return 2119;

        } else if (this.typePet == 10 && !this.isTransform) {
            return 2125;

        } else if (this.typePet == 11 && !this.isTransform) {
            return 2137;

        } else if (this.typePet == 12 && !this.isTransform) {
            return 2122;

        } else if (this.typePet == 13 && !this.isTransform) {
            return 2072;
        } else if (this.typePet == 14 && !this.isTransform) {
            return 754;

        } else if (this.typePet == 15 && !this.isTransform) {
            return 2093;

        } else if (this.typePet == 16 && !this.isTransform) {
            return 703;

        } else if (this.typePet == 17 && !this.isTransform) {
            return 838;
        } else if (this.typePet == 18 && !this.isTransform) {
            return 2018;

        } else if (this.typePet == 19 && !this.isTransform) {
            return 2033;

        } else if (this.typePet == 20 && !this.isTransform) {
            return 2036;

        } else if (this.typePet == 21 && !this.isTransform) {
            return 2039;

        } else if (this.typePet == 22 && !this.isTransform) {
            return 2075;

        } else if (this.typePet == 23 && !this.isTransform) {
            return 639;
        } else if (this.typePet == 24 && !this.isTransform) {
            return 2140;

        } else if (this.typePet == 25 && !this.isTransform) {
            return 1248;

        } else if (this.typePet == 26 && !this.isTransform) {
            return 2143;

        } else if (this.typePet == 27 && !this.isTransform) {
            return 876;

        } else if (this.typePet == 28 && !this.isTransform) {
            return 2146;

        } else if (this.typePet == 29 && !this.isTransform) {
            return 2149;
        } else if (this.typePet == 30 && !this.isTransform) {
            return 2152;
        } else if (this.typePet == 31 && !this.isTransform) {
            return 1333;
        } else if (this.typePet == 32 && !this.isTransform) {
            return 1336;
        } else if (this.typePet == 33 && !this.isTransform) {
            return 1384;
        } else if (this.typePet == 34 && !this.isTransform) {
            return 1411;
        } else if (this.typePet == 35 && !this.isTransform) {
            return 1346;
        } else if (this.typePet == 36 && !this.isTransform) {
            return 1399;
        } else if (this.typePet == 37 && !this.isTransform) {
            return 1402;
        } else if (this.typePet == 38 && !this.isTransform) {
            return 1405;
        } else if (this.typePet == 39 && !this.isTransform) {
            return 1408;
        } else if (this.typePet == 40 && !this.isTransform) {
            return 1224;
        } else if (this.typePet == 41 && !this.isTransform) {
            return 1420;

        } else if (this.inventory != null && inventory.itemsBody != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int part = inventory.itemsBody.get(5).template.head;
            if (part != -1) {
                return (short) part;
            }
        }
        if (this.nPoint != null && this.nPoint.power < 1500000) {
            return PET_ID[this.gender][0];
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getBody() {
        if (this.itemTime != null && this.itemTime.isBienHinhMa) {
//            //            if (Util.isTrue(80, 100)) {
//            if (this.gender == 0) {
//                return -1;
//            }
//            if (this.gender == 1) {
//                return -1;
//            }
//            if (this.gender == 2) {
//                return -1;
//            }
//            } else {
                return 1781;
//            }
        }
        if (this.effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (this.effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 298;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 509;

        } else if (this.typePet == 3 && !this.isTransform) {
            return 238;

        } else if (this.typePet == 4 && !this.isTransform) {
            return 235;
        } else if (this.typePet == 5 && !this.isTransform) {
            return 2031;
        } else if (this.typePet == 6 && !this.isTransform) {
            return 503;
        } else if (this.typePet == 7 && !this.isTransform) {
            return 2091;

        } else if (this.typePet == 8 && !this.isTransform) {
            return 2117;

        } else if (this.typePet == 9 && !this.isTransform) {
            return 2120;

        } else if (this.typePet == 10 && !this.isTransform) {
            return 2126;

        } else if (this.typePet == 11 && !this.isTransform) {
            return 2138;

        } else if (this.typePet == 12 && !this.isTransform) {
            return 2123;

        } else if (this.typePet == 13 && !this.isTransform) {
            return 2073;
        } else if (this.typePet == 14 && !this.isTransform) {
            return 755;

        } else if (this.typePet == 15 && !this.isTransform) {
            return 2094;

        } else if (this.typePet == 16 && !this.isTransform) {
            return 704;

        } else if (this.typePet == 17 && !this.isTransform) {
            return 839;
        } else if (this.typePet == 18 && !this.isTransform) {
            return 2019;

        } else if (this.typePet == 19 && !this.isTransform) {
            return 2034;

        } else if (this.typePet == 20 && !this.isTransform) {
            return 2037;

        } else if (this.typePet == 21 && !this.isTransform) {
            return 2040;

        } else if (this.typePet == 22 && !this.isTransform) {
            return 2076;

        } else if (this.typePet == 23 && !this.isTransform) {
            return 640;
        } else if (this.typePet == 24 && !this.isTransform) {
            return 2141;

        } else if (this.typePet == 25 && !this.isTransform) {
            return 1249;

        } else if (this.typePet == 26 && !this.isTransform) {
            return 2144;

        } else if (this.typePet == 27 && !this.isTransform) {
            return 877;

        } else if (this.typePet == 28 && !this.isTransform) {
            return 2147;

        } else if (this.typePet == 29 && !this.isTransform) {
            return 2150;
        } else if (this.typePet == 30 && !this.isTransform) {
            return 2153;
        } else if (this.typePet == 31 && !this.isTransform) {//Kafula 108 145
            return 1334;
        } else if (this.typePet == 32 && !this.isTransform) {//Kafula 108 145
            return 1334;
        } else if (this.typePet == 33 && !this.isTransform) {//Kafula 108 145
            return 1385;
        } else if (this.typePet == 34 && !this.isTransform) {
            return 1412;
        } else if (this.typePet == 35 && !this.isTransform) {
            return 1347;
        } else if (this.typePet == 36 && !this.isTransform) {
            return 1400;
        } else if (this.typePet == 37 && !this.isTransform) {
            return 1403;
        } else if (this.typePet == 38 && !this.isTransform) {
            return 1406;
        } else if (this.typePet == 39 && !this.isTransform) {
            return 1409;
        } else if (this.typePet == 40 && !this.isTransform) {
            return 1225;
        } else if (this.typePet == 41 && !this.isTransform) {
            return 1421;
        } else if (this.inventory != null && inventory.itemsBody != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (this.inventory != null && inventory.itemsBody != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        if (this.nPoint != null && this.nPoint.power < 1500000) {
            return PET_ID[this.gender][1];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
        }
    }

    @Override
    public short getLeg() {
        if (this.itemTime != null && this.itemTime.isBienHinhMa) {
//            //            if (Util.isTrue(80, 100)) {
//            if (this.gender == 0) {
//                return -1;
//            }
//            if (this.gender == 1) {
//                return -1;
//            }
//            if (this.gender == 2) {
//                return -1;
//            }
//            } else {
                return 1782;
//            }
        }
        if (this.effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (this.effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 299;
        } else if (this.typePet == 2 && !this.isTransform) {
            return 510;
        } else if (this.typePet == 3 && !this.isTransform) {
            return 239;

        } else if (this.typePet == 4 && !this.isTransform) {
            return 236;
        } else if (this.typePet == 5 && !this.isTransform) {
            return 2032;
        } else if (this.typePet == 6 && !this.isTransform) {
            return 504;

        } else if (this.typePet == 7 && !this.isTransform) {
            return 2092;

        } else if (this.typePet == 8 && !this.isTransform) {
            return 2118;

        } else if (this.typePet == 9 && !this.isTransform) {
            return 2121;

        } else if (this.typePet == 10 && !this.isTransform) {
            return 2127;

        } else if (this.typePet == 11 && !this.isTransform) {
            return 2139;

        } else if (this.typePet == 12 && !this.isTransform) {
            return 2124;

        } else if (this.typePet == 13 && !this.isTransform) {
            return 2074;
        } else if (this.typePet == 14 && !this.isTransform) {
            return 756;

        } else if (this.typePet == 15 && !this.isTransform) {
            return 2095;

        } else if (this.typePet == 16 && !this.isTransform) {
            return 705;

        } else if (this.typePet == 17 && !this.isTransform) {
            return 840;
        } else if (this.typePet == 18 && !this.isTransform) {
            return 2120;

        } else if (this.typePet == 19 && !this.isTransform) {
            return 2035;

        } else if (this.typePet == 20 && !this.isTransform) {
            return 2038;

        } else if (this.typePet == 21 && !this.isTransform) {
            return 2041;

        } else if (this.typePet == 22 && !this.isTransform) {
            return 2077;

        } else if (this.typePet == 23 && !this.isTransform) {
            return 641;
        } else if (this.typePet == 24 && !this.isTransform) {
            return 2142;

        } else if (this.typePet == 25 && !this.isTransform) {
            return 1250;

        } else if (this.typePet == 26 && !this.isTransform) {
            return 2145;

        } else if (this.typePet == 27 && !this.isTransform) {
            return 878;

        } else if (this.typePet == 28 && !this.isTransform) {
            return 2148;

        } else if (this.typePet == 29 && !this.isTransform) {
            return 2151;
        } else if (this.typePet == 30 && !this.isTransform) {
            return 2154;
        } else if (this.typePet == 31 && !this.isTransform) {//Kafula 108 145
            return 1335;
        } else if (this.typePet == 32 && !this.isTransform) {//Kafula 108 145
            return 1335;
        } else if (this.typePet == 33 && !this.isTransform) {//Kafula 108 145
            return 1386;
        } else if (this.typePet == 34 && !this.isTransform) {
            return 1413;
        } else if (this.typePet == 35 && !this.isTransform) {
            return 1348;
        } else if (this.typePet == 36 && !this.isTransform) {
            return 1401;
        } else if (this.typePet == 37 && !this.isTransform) {
            return 1404;
        } else if (this.typePet == 38 && !this.isTransform) {
            return 1407;
        } else if (this.typePet == 39 && !this.isTransform) {
            return 1410;
        } else if (this.typePet == 40 && !this.isTransform) {
            return 1226;
        } else if (this.typePet == 41 && !this.isTransform) {
            return 1422;
        } else if (this.inventory != null && inventory.itemsBody != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (this.inventory != null && inventory.itemsBody != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }

        if (this.nPoint != null && this.nPoint.power < 1500000) {
            return PET_ID[this.gender][2];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 60 : 58);
        }
    }

    private Mob findMobAttack() {
        if (this.zone == null) {
            // Xử lý trường hợp zone là null
            return null;
        }

        int dis = ARANGE_CAN_ATTACK;
        Mob mobAtt = null;
        for (Mob mob : zone.mobs) {
            if (mob.isDie()) {
                continue;
            }
            int d = Util.getDistance(this, mob);
            if (d <= dis) {
                dis = d;
                mobAtt = mob;
            }
        }
        return mobAtt;
    }

    //Sức mạnh mở skill đệ
    private void updatePower() {
        if (this.playerSkill != null) {
            switch (this.playerSkill.getSizeSkill()) {
                case 1:
                    if (this.nPoint.power >= 150000000) {
                        openSkill2();
                    }
                    break;                                                                  
                case 2:
                    if (this.nPoint.power >= 1500000000) {
                        openSkill3();
                    }
                    break;                                                                  
                case 3:
                    if (this.nPoint.power >= 20000000000L) {
                        openSkill4();
                    }
                    break;                                                                  

                case 4:
                    if (this.nPoint.power >= 150000000000L) {
//                        openSkill5();
                    }
                    break;                                                                  
            }
        }
    }

    public void openSkill1() {
        Skill skill = null;
        int tiLeKame = 20;
        int tiLeMasenko = 50;
        int tiLeAntomic = 30;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeKame) {
            skill = SkillUtil.createSkill(Skill.DRAGON, 1);
        } else if (rd <= tiLeKame + tiLeMasenko) {
            skill = SkillUtil.createSkill(Skill.DEMON, 1);
        } else if (rd <= tiLeKame + tiLeMasenko + tiLeAntomic) {
            skill = SkillUtil.createSkill(Skill.GALICK, 1);
        }
        skill.coolDown = 1000;
        this.playerSkill.skills.set(0, skill);
    }

    public void openSkill2() {
        Skill skill = null;
        int tiLeKame = 20;
        int tiLeMasenko = 50;
        int tiLeAntomic = 30;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeKame) {
            skill = SkillUtil.createSkill(Skill.KAMEJOKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko) {
            skill = SkillUtil.createSkill(Skill.MASENKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko + tiLeAntomic) {
            skill = SkillUtil.createSkill(Skill.ANTOMIC, 1);
        }
        skill.coolDown = 1000;
        this.playerSkill.skills.set(1, skill);
    }

    public void openSkill3() {
        Skill skill = null;
        int tiLeTDHS = 30;
        int tiLeTTNL = 30;
        int tiLeKOK = 40;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeTDHS) {
            skill = SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL) {
            skill = SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL + tiLeKOK) {
            skill = SkillUtil.createSkill(Skill.KAIOKEN, 1);
        }
        this.playerSkill.skills.set(2, skill);
    }

    public void openSkill4() {
        Skill skill = null;
        int tiLeBienKhi = 10;
        int tiLeDeTrung = 30;
        int tiLeKNL = 60;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeBienKhi) {
            skill = SkillUtil.createSkill(Skill.BIEN_KHI, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung) {
            skill = SkillUtil.createSkill(Skill.DE_TRUNG, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung + tiLeKNL) {
            skill = SkillUtil.createSkill(Skill.KHIEN_NANG_LUONG, 1);
        }
        this.playerSkill.skills.set(3, skill);
    }

    public void openSkill5() {
        Skill skill = null;
        int tiLeThoiMien = 10; // khi
        int tiLeSoCoLa = 70; // detrung
        int tiLeDCTT = 20; // khienNl
        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeThoiMien) {
            skill = SkillUtil.createSkill(Skill.SOCOLA, 1);
        } else if (rd <= tiLeThoiMien + tiLeSoCoLa) {
            skill = SkillUtil.createSkill(Skill.QUA_CAU_KENH_KHI, 1);
        } else if (rd <= tiLeThoiMien + tiLeSoCoLa + tiLeDCTT) {
            skill = SkillUtil.createSkill(Skill.TROI, 1);
        }
        try {
            this.playerSkill.skills.set(4, skill);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Logger.error("Bị giới hạn index > 4 skill pet");
//                        // Xử lý trường hợp lỗi hoặc ghi log
        }
    }

//    ========================================================
//    private Skill getSkill(int indexSkill) {
//        if (indexSkill - 1 <= this.playerSkill.skills.size()) {
//            return this.playerSkill.skills.get(indexSkill - 1);
//        }
//        return this.playerSkill.skills.get(this.playerSkill.skills.size() - 1);
//    }
//    private Skill getSkill(int indexSkill) {
////        if (this.playerSkill != null) {
////            return null;
////        }
////        if (indexSkill - 1 >= 0 && indexSkill - 1 < this.playerSkill.skills.size()) {
////            return this.playerSkill.skills.get(indexSkill - 1);
////        }
//        return this.playerSkill.skills.get(this.playerSkill.skills.size() - 1);
//
//    }
    private Skill getSkill(int indexSkill) {
        if (this.playerSkill != null) {
            return this.playerSkill.skills.get(indexSkill - 1);
        }
        return null;
    }

    public void transform() {
        switch (this.typePet) {
            case 1:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "!! Bư..Bư..Bư..Ma..Nhân..Bư....");
                break;                                                                  
            case 2:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta là Thần Hủy Diệt, có tin ta cho ngươi bay màu không?");
                break;                                                                  
            case 3:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta là Thần Hủy Diệt, có tin ta cho ngươi bay màu không?");
                break;                                                                  
            case 4:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta thật hoàn hảo!!!!");
                break;                                                                  
            case 5:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta cân tất gruuu");
                break;                                                                  
            case 6:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Hố hố hố hãy chiêm ngưỡng sức mạnh\nFide Đại Đế ta");
                break;                                                                  
            case 7:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta sẽ là người đánh bại ngươi\n Zeno tối thượng hhhhh");
                break;                                                                  
            case 8:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Sư phụ thấy con đáng yêu không ><");
                break;                                                                  
            case 9:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ka....me....ha....me\n....HAaaaaaa");
                break;                                                                  
            case 10:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Sức mạnh là công lý\n không có tình yêu hay tình bạn gì hết !!!");
                break;                                                                  
            case 11:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Đúng là con người sao có thể sánh với thần được chứ!");
                break;                                                                  
            case 12:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ka....me....ha....me\n....HAaaaaaa");
                break;                                                                  
            case 13:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 14:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Sức mạnh là công lý\n không có tình yêu hay tình bạn gì hết !!!");
                break;                                                                  
            case 15:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Đúng là con người sao có thể sánh với thần được chứ!");
                break;                                                                  
            case 16:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ka....me....ha....me\n....HAaaaaaa");
                break;                                                                  
            case 17:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 18:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 19:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 20:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 21:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 22:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 23:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 24:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 25:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 26:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 27:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 28:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 29:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 30:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 31:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 32:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;                                                                  
            case 33:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "\n|7|Ta đã phải luyện tập rất nhiều để trở lên mạnh mẽ");
                break;
            case 34:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "!! Bư..Bư..Bư..Ma..Nhân..Bư....");
                break;                                                                  
            case 35:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta là Thần Hủy Diệt, có tin ta cho ngươi bay màu không?");
                break;                                                                  
            case 36:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta là Thần Hủy Diệt, có tin ta cho ngươi bay màu không?");
                break;                                                                  
            case 37:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta thật hoàn hảo!!!!");
                break;                                                                  
            case 38:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta cân tất gruuu");
                break;                                                                  
            case 39:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Hố hố hố hãy chiêm ngưỡng sức mạnh\nFide Đại Đế ta");
                break;                                                                  
            case 40:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ta sẽ là người đánh bại ngươi\n Zeno tối thượng hhhhh");
                break;                                                                  
            case 41:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Sư phụ thấy con đáng yêu không ><");
                break;                                                                  
            case 42:
                this.isTransform = !this.isTransform;
                Service.gI().Send_Caitrang(this);
                Service.gI().chat(this, "Ka....me....ha....me\n....HAaaaaaa");
                break;                                                                  

            default:
                break;                                                                  
        }
    }

    @Override
    public void dispose() {
        this.mobAttack = null;
        this.master = null;
        super.dispose();
    }
}
