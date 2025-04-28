package nro.models.boss;

import nro.consts.ConstPlayer;
import static nro.models.boss.BossStatus.ACTIVE;
import static nro.models.boss.BossStatus.CHAT_E;
import static nro.models.boss.BossStatus.CHAT_S;
import static nro.models.boss.BossStatus.DIE;
import static nro.models.boss.BossStatus.JOIN_MAP;
import static nro.models.boss.BossStatus.LEAVE_MAP;
import static nro.models.boss.BossStatus.RESPAWN;
import static nro.models.boss.BossStatus.REST;
import static nro.models.boss.BossStatus.WAIT;
import nro.models.boss.iboss.IBossNew;
import nro.models.boss.iboss.IBossOutfit;
//import nro.models.boss.list_boss.android.SuperAndroid17;
//import nro.models.boss.list_boss.android.SuperAndroid17;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.ServerNotify;
import nro.services.EffectSkillService;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.SkillUtil;
import nro.utils.Util;
import java.util.Arrays;
import java.util.Objects;
import lombok.Data;
import nro.consts.ConstItem;
import nro.lib.RandomCollection;
import nro.models.map.ItemMap;
import nro.services.RewardService;

@Data

public class Boss extends Player implements IBossNew, IBossOutfit {

    public int currentLevel = 0;
    public BossData[] data;

    public BossStatus bossStatus;
    public Zone lastZone;
    private long lastTimeRest;
    private int secondsRest;
    private int secondsNotify;
    private long lastTimeChatS;
    private int timeChatS;
    private byte indexChatS;
    private long lastTimeChatE;
    private int timeChatE;
    private byte indexChatE;
    private long lastTimeChatM;
    private int timeChatM;
    private long lastTimeNotify;
    private int typeBoss;
    private long lastTimeTargetPlayer;
    private int timeTargetPlayer;
    private long timeToRestart;
    private Player playerTarget;
    private Boss parentBoss;
    private Boss[][] bossAppearTogether;
    public long lastTimeJoinMap = System.currentTimeMillis();
//    public long lastTimeRest;
//    public int secondsRest;
//    public int secondsNotify;
//    public long lastTimeChatS;
//    public int timeChatS;
//    public byte indexChatS;
//    public long lastTimeChatE;
//    public int timeChatE;
//    public byte indexChatE;
//    public long lastTimeChatM;
//    public int timeChatM;
//    public long lastTimeNotify;
//    public int typeBoss;
//    public long lastTimeTargetPlayer;
//    public int timeTargetPlayer;
//    public long timeToRestart;
//    public Player playerTarget;
//    public Boss parentBoss;
//    public Boss[][] bossAppearTogether;
    public Zone zoneFinal;
    protected Boss bossInstance;

    public Boss(int type, BossData... data) throws Exception {
        initializeBoss(type, data);
    }

    private void initializeBoss(int type, BossData... data) throws Exception {
        this.id = BossManager.gI().idBase++;
        this.isBoss = true;

        if (data == null || data.length == 0) {
            throw new Exception("Invalid boss data");
        }
        this.typeBoss = type;
        this.data = data;
        this.secondsRest = data[0].getSecondsRest();
        this.secondsNotify = data[0].getSecondsNotify();
        this.bossStatus = REST;
        BossManager.gI().addBoss(this);
        bossInstance = this;
        createBossAppearTogether();
    }

    private void createBossAppearTogether() {
        bossAppearTogether = new Boss[data.length][];

        for (int i = 0; i < bossAppearTogether.length; i++) {
            if (data[i].getBossesAppearTogether() != null) {
                bossAppearTogether[i] = new Boss[data[i].getBossesAppearTogether().length];

                for (int j = 0; j < data[i].getBossesAppearTogether().length; j++) {
                    Boss boss = BossManager.gI().createBoss(data[i].getBossesAppearTogether()[j]);

                    if (boss != null) {
                        boss.parentBoss = this;
                        this.timeToRestart = -1;
                        bossAppearTogether[i][j] = boss;
                    }
                }
            }
        }
    }

    @Override
    public void initBase() {
        BossData dataBoss = this.data[this.currentLevel];
        if (typeBoss == BossType.BROLY || typeBoss == BossType.BROLY_SUPER) {
            this.name = dataBoss.getName() + " " + id;
        } else {
            this.name = String.format(dataBoss.getName(), Util.nextInt(0, 100));
        }
        this.gender = dataBoss.getGender();
        this.nPoint.mpg = 23_07_2003;
        this.nPoint.dameg = dataBoss.getDame();
        this.nPoint.hpg = dataBoss.getHp()[Util.nextInt(0, dataBoss.getHp().length - 1)];
        this.nPoint.hp = nPoint.hpg;
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

    protected void initSkill() {
        for (Skill skill : this.playerSkill.skills) {
            skill.dispose();
        }
        this.playerSkill.skills.clear();
        this.playerSkill.skillSelect = null;
        int[][] skillTemp = data[this.currentLevel].getSkillTemp();
        for (int[] skillTemp1 : skillTemp) {
            Skill skill = SkillUtil.createSkill(skillTemp1[0], skillTemp1[1]);
            if (skillTemp1.length == 3) {
                skill.coolDown = skillTemp1[2];
            }
            this.playerSkill.skills.add(skill);
        }
    }

    protected void resetBase() {
        this.lastTimeChatS = 0;
        this.lastTimeChatE = 0;
        this.timeChatS = 0;
        this.timeChatE = 0;
        this.indexChatS = 0;
        this.indexChatE = 0;
    }

    //.outfit.
    @Override
    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        }
        if (effectSkill.isCaiBinhChua || effectSkin.isMaPhongBa) {
            return 1254;
        }
        return this.data[this.currentLevel].getOutfit()[0];
    }

    @Override
    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        }
        if (effectSkill.isCaiBinhChua || effectSkin.isMaPhongBa) {
            return 1255;
        }
        return this.data[this.currentLevel].getOutfit()[1];
    }

    @Override
    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        }
        if (effectSkill.isCaiBinhChua || effectSkin.isMaPhongBa) {
            return 1256;
        }
        return this.data[this.currentLevel].getOutfit()[2];
    }

    @Override
    public short getFlagBag() {
        return this.data[this.currentLevel].getOutfit()[3];
    }

    @Override
    public byte getAura() {
        return (byte) this.data[this.currentLevel].getOutfit()[4];
    }

    @Override
    public byte getEffFront() {
        return (byte) this.data[this.currentLevel].getOutfit()[5];
    }

    protected void goToXY(int x, int y, boolean isTeleport) {
        if (!isTeleport) {
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            byte move = (byte) Util.nextInt(50, 100);
            PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y);
        } else {
            ChangeMapService.gI().changeMapYardrat(this, this.zone, x, y);
        }
    }
//    public Zone getMapJoin() {
//        int mapId = this.data[this.currentLevel].getMapJoin()[Util.nextInt(0, this.data[this.currentLevel].getMapJoin().length - 1)];
//        Zone map = MapService.gI().getMapWithRandZone(mapId);
//        return map;
//    }

    public Zone getMapJoin() {
        int[] mapJoinArray = this.data[this.currentLevel].getMapJoin();
        if (mapJoinArray != null && mapJoinArray.length > 0) {
            int mapId = mapJoinArray[Util.nextInt(0, mapJoinArray.length - 1)];
            Zone map = MapService.gI().getMapWithRandZone(mapId);
            return map;
        } else {
            return null;
        }
    }

    @Override
    public void changeStatus(BossStatus status) {
        this.bossStatus = status;
    }

//    @Override
    public void generalRewards(Player player) {//Hmmmmm....phẩn thưởng chung (boss nào cũng rớt - boss phó bản)
        if (player != null) {
            ItemMap itemMap = null;
            int x = this.location.x;
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
//            itemMap = new ItemMap(this.zone, 886, Util.nextInt(1, 5), x, y, player.id);
//            itemMap = new ItemMap(this.zone, 887, Util.nextInt(1, 5), x, y, player.id);
//            itemMap = new ItemMap(this.zone, 888, Util.nextInt(1, 5), x, y, player.id);
//            itemMap = new ItemMap(this.zone, 889, Util.nextInt(1, 5), x, y, player.id);
//            if (!(this instanceof Kuku) && !(this instanceof Rambo) && !(this instanceof MapDauDinh)) {
            RandomCollection<Integer> rd = new RandomCollection<>();
//            rd.add(1, ConstItem.NGOC_RONG_4_SAO);
//            rd.add(1, Util.nextInt(16, 18));
            rd.add(1, ConstItem.NGOC_RONG_3_SAO);
//            rd.add(1, 886);
//            rd.add(1, 887);
//            rd.add(1, 888);
            int rwID = rd.next();
//            itemMap = new ItemMap(this.zone, rwID, Util.nextInt(1, 5), x, y, player.id);
            itemMap = new ItemMap(this.zone, rwID, Util.nextInt(1, 2), x, y, player.id);

            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
//            }
            if (itemMap != null) {
                Service.gI().dropItemMap(zone, itemMap);
            }
        }
    }

    @Override
    public Player getPlayerAttack() {
        if (this.playerTarget != null && (this.playerTarget.isDie() || !this.zone.equals(this.playerTarget.zone))) {
            this.playerTarget = null;
        }
        if (this.playerTarget == null || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            this.playerTarget = this.zone.getRandomPlayerInMap();
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(5000, 7000);
        }
        if (this.playerTarget != null && this.playerTarget.effectSkin != null && this.playerTarget.effectSkin.isVoHinh) {
            this.playerTarget = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(1000, 2000);
        }
        if (this.playerTarget == this.pet) {
            this.playerTarget = null;
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(1000, 2000);
        }
        return this.playerTarget;
    }

    @Override
    public void changeToTypePK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
    }

    @Override
    public void changeToTypeNonPK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.NON_PK);
    }

    public void bossNotify() {
        if (this.secondsNotify == 0) {
            return;
        }
        if (this != null && !this.isDie() && this.zone != null && Util.canDoWithTime(this.lastTimeNotify, this.secondsNotify * 1000)) {
            this.lastTimeNotify = System.currentTimeMillis();
            if (timeToRestart == -1 && data[0].getBossesAppearTogether() != null && data[0].getBossesAppearTogether().length > 1) {
                for (Boss boss : bossAppearTogether[0]) {
                    if (boss != null && boss.zone != null) {
                        ServerNotify.gI().notify("BOSS " + boss.name + " vừa xuất hiện tại " + boss.zone.map.mapName);
                    }
                }
                ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
            } else if (this.data[0].getBossesAppearTogether() == null) {
                ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
            }
        }
    }

    @Override
    public void updateBoss() {
        super.update();
        bossNotify();
        this.nPoint.mp = this.nPoint.mpg;
        if (this.effectSkill.isHaveEffectSkill()) {
            return;
        }

        switch (this.bossStatus) {
            case REST:
                this.rest();
                break;
            case RESPAWN:
                this.respawn();
                this.changeStatus(JOIN_MAP);
                break;
            case JOIN_MAP:
                this.joinMap();
                this.changeStatus(CHAT_S);
//                System.out.println("Map: " + this.zone.map.mapId + ", Zone: " + this.zone.zoneId + ", Boss: " + this.name);
                break;
            case CHAT_S:
                if (Util.canDoWithTime(lastTimeJoinMap, 900000)) {
                    this.bossStatus = BossStatus.LEAVE_MAP;
                }
                if (chatS()) {
                    this.doneChatS();
                    this.lastTimeChatM = System.currentTimeMillis();
                    this.timeChatM = 5000;
                    this.changeStatus(ACTIVE);
                }
                break;
            case ACTIVE:
                if (Util.canDoWithTime(lastTimeJoinMap, 900000)) {
                    this.bossStatus = BossStatus.LEAVE_MAP;
                }
                this.chatM();
                if (this.effectSkill.isCharging && !Util.isTrue(1, 20) || this.effectSkill.useTroi) {
                    return;
                }
                this.active();
                break;
            case WAIT:
                this.SpawnCombat();
                break;
            case DIE:
                this.changeStatus(CHAT_E);
                break;
            case CHAT_E:
                if (chatE()) {
                    this.doneChatE();
                    this.changeStatus(LEAVE_MAP);
                }
                break;
            case LEAVE_MAP:
                this.leaveMap();
                break;
        }

    }

    //loop
    @Override
    public void rest() {
//        int nextLevel = this.currentLevel + 1;
//        if (nextLevel >= this.data.length) {
//            nextLevel = 0;
//        }
        if (this.data[0].getTypeAppear() == TypeAppear.DEFAULT_APPEAR) {
            if (Util.canDoWithTime(lastTimeRest, secondsRest * 1000)) {
                changeStatus(RESPAWN);
            }
        }
    }

    @Override
    public void respawn() {
        this.initBase();
        this.changeToTypeNonPK();
    }

    @Override
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);

            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {
                ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            }
            Service.gI().sendFlagBag(this);
            this.notifyJoinMap();
        }
        lastTimeJoinMap = System.currentTimeMillis();
    }

    public void joinMapByZone(Player player) {
        if (player.zone != null) {
            this.zone = player.zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
        }

    }

    public void joinMapByZone(Zone zone) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, -1);
        }
    }

    public void joinMapByZoneWithXY(Zone zone, short x, short y) {
        if (zone != null) {
            this.zone = zone;
            ChangeMapService.gI().changeMap(this, zone, x, y);
        }
    }

    protected void notifyJoinMap() {
//        if (this.id >= -22 && this.id <= -20) {
//            return;
//        }
//        if (this.typeBoss == BossType.BROLY) {
//            return;
//        }
        if (this.zone.map.mapId == 140
                || MapService.gI().isMapMaBu(this.zone.map.mapId)
                || MapService.gI().isMapBlackBallWar(this.zone.map.mapId)
                || MapService.gI().isMapGiaidauvutru(this.zone.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(this.zone.map.mapId)
                || MapService.gI().isMapConDuongRanDoc(this.zone.map.mapId)
                || MapService.gI().isMapDoanhTrai(this.zone.map.mapId)) {
            return;
        }
        ServerNotify.gI().notify("BOSS " + this.name + " vừa xuất hiện tại " + this.zone.map.mapName);
        this.lastTimeNotify = System.currentTimeMillis();
    }

    @Override
    public boolean chatS() {
        if (Util.canDoWithTime(lastTimeChatS, timeChatS)) {
            if (this.indexChatS == this.data[this.currentLevel].getTextS().length) {
                return true;
            }
            String textChat = this.data[this.currentLevel].getTextS()[this.indexChatS];
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            if (!this.chat(prefix, textChat)) {
                return false;
            }
            this.lastTimeChatS = System.currentTimeMillis();
            this.timeChatS = textChat.length() * 100;
            if (this.timeChatS > 2000) {
                this.timeChatS = 2000;
            }
            this.indexChatS++;
        }
        return false;
    }

    @Override
    public void doneChatS() {

    }

    @Override
    public void chatM() {
        if (this.typePk == ConstPlayer.NON_PK) {
            return;
        }
        if (this.data[this.currentLevel].getTextM().length == 0) {
            return;
        }
        if (!Util.canDoWithTime(this.lastTimeChatM, this.timeChatM)) {
            return;
        }
        String textChat = this.data[this.currentLevel].getTextM()[Util.nextInt(0, this.data[this.currentLevel].getTextM().length - 1)];
        int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
        textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
        this.chat(prefix, textChat);
        this.lastTimeChatM = System.currentTimeMillis();
        this.timeChatM = Util.nextInt(3000, 10000);
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.attack();
    }

    protected long lastTimeAttack;

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie() || pl.isNewPet || pl.isMiniPet) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
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
                    if (Util.isTrue(1, 2)) {
                        this.moveToPlayer(pl);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    }

    @Override
    public void checkPlayerDie(Player player) {
        if (player.isDie()) {
            this.chat("Chừa nha con!!!");
        }
    }

    protected int getRangeCanAttackWithSkillSelect() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else if (skillId == Skill.DRAGON || skillId == Skill.DEMON || skillId == Skill.GALICK) {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
        return 500;
    }

    @Override
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
            ServerNotify.gI().notify(plKill.name + " vừa tiêu diệt được " + this.name);
            System.out.println(plKill.name + " vừa tiêu diệt được " + this.name);
        }
        this.changeStatus(DIE);
    }

    @Override
    public void reward(Player plKill) {
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public boolean chatE() {
        if (Util.canDoWithTime(lastTimeChatE, timeChatE)) {
            if (this.indexChatE == this.data[this.currentLevel].getTextE().length) {
                return true;
            }
            String textChat = this.data[this.currentLevel].getTextE()[this.indexChatE];
            int prefix = Integer.parseInt(textChat.substring(1, textChat.lastIndexOf("|")));
            textChat = textChat.substring(textChat.lastIndexOf("|") + 1);
            if (!this.chat(prefix, textChat)) {
                return false;
            }
            this.lastTimeChatE = System.currentTimeMillis();
            this.timeChatE = textChat.length() * 100;
            if (this.timeChatE > 2000) {
                this.timeChatE = 2000;
            }
            this.indexChatE++;
        }
        return false;
    }

    @Override
    public void doneChatE() {
    }

    @Override
    public void leaveMap() {
        if (this.currentLevel < this.data.length - 1) {
            this.lastZone = this.zone;
            this.currentLevel++;
            this.changeStatus(RESPAWN);
        } else {
            ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
            ChangeMapService.gI().exitMap(this);
            this.lastZone = null;
            this.lastTimeRest = System.currentTimeMillis();
            if (this.parentBoss == null) {
                BossManager.gI().setTimeSpawnBoss.put(typeBoss, System.currentTimeMillis() + (secondsRest * 1000));
            }
            bossInstance = null;
            BossManager.gI().removeBoss(this);
        }
        this.wakeupAnotherBossWhenDisappear();
    }

    //end loop
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
    public void moveToPlayer(Player player) {
        this.moveTo(player.location.x, player.location.y);
    }

    @Override
    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }

    public void chat(String text) {
        Service.gI().chat(this, text);
    }

    protected boolean chat(int prefix, String textChat) {
        if (prefix == -1) {
            this.chat(textChat);
        } else if (prefix == -2) {
            if (this.zone == null) {
                return false;
            }
            Player plMap = this.zone.getRandomPlayerInMap();
            if (plMap != null && !plMap.isDie() && Util.getDistance(this, plMap) <= 600) {
                Service.gI().chat(plMap, textChat);
            } else {
                return false;
            }
        } else if (prefix == -3) {
            if (this.parentBoss != null && !this.parentBoss.isDie()) {
                this.parentBoss.chat(textChat);
            }
        } else if (prefix >= 0) {
            if (this.bossAppearTogether != null && this.bossAppearTogether[this.currentLevel] != null) {
                Boss boss = this.bossAppearTogether[this.currentLevel][prefix];
                if (!boss.isDie()) {
                    boss.chat(textChat);
                }
            } else if (this.parentBoss != null && this.parentBoss.bossAppearTogether != null
                    && this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] != null) {
                Boss boss = this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel][prefix];
                if (!boss.isDie()) {
                    boss.chat(textChat);
                }
            }
        }
        return true;
    }

    @Override
    public void wakeupAnotherBossWhenAppear() {

        if (!MapService.gI().isMapMaBu(this.zone.map.mapId) && MapService.gI().isMapBlackBallWar(this.zone.map.mapId)) {
            System.out.println("BOSS " + this.name + " : " + this.zone.map.mapName + " khu vực " + this.zone.zoneId + "(" + this.zone.map.mapId + ")");
        }
        if (this.bossAppearTogether == null || this.bossAppearTogether[this.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.bossAppearTogether[this.currentLevel]) {
            int nextLevelBoss = boss.currentLevel + 1;
            if (nextLevelBoss >= boss.data.length) {
                nextLevelBoss = 0;
            }
            if (boss.data[nextLevelBoss].getTypeAppear() == TypeAppear.CALL_BY_ANOTHER) {
                if (boss.zone != null) {
                    boss.leaveMap();
                }
            }
            if (boss.data[nextLevelBoss].getTypeAppear() == TypeAppear.APPEAR_WITH_ANOTHER) {
                if (boss.zone != null) {
                    boss.leaveMap();
                }
                boss.changeStatus(RESPAWN);
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), currentLevel, data, bossStatus, lastZone);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Boss other = (Boss) obj;
        if (this.currentLevel != other.currentLevel) {
            return false;
        }
        if (!Arrays.deepEquals(this.data, other.data)) {
            return false;
        }
        if (this.bossStatus != other.bossStatus) {
            return false;
        }
        if (!Objects.equals(this.lastZone, other.lastZone)) {
            return false;
        }
        return Objects.equals(this.zoneFinal, other.zoneFinal);
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        
//        final Boss other = (Boss) obj;
//        if (this.currentLevel != other.currentLevel) {
//            return false;
//        }
//        if (this.lastTimeRest != other.lastTimeRest) {
//            return false;
//        }
//        if (this.secondsRest != other.secondsRest) {
//            return false;
//        }
//        if (this.secondsNotify != other.secondsNotify) {
//            return false;
//        }
//        if (this.lastTimeChatS != other.lastTimeChatS) {
//            return false;
//        }
//        if (this.timeChatS != other.timeChatS) {
//            return false;
//        }
//        if (this.indexChatS != other.indexChatS) {
//            return false;
//        }
//        if (this.lastTimeChatE != other.lastTimeChatE) {
//            return false;
//        }
//        if (this.timeChatE != other.timeChatE) {
//            return false;
//        }
//        if (this.indexChatE != other.indexChatE) {
//            return false;
//        }
//        if (this.lastTimeChatM != other.lastTimeChatM) {
//            return false;
//        }
//        if (this.timeChatM != other.timeChatM) {
//            return false;
//        }
//        if (this.lastTimeNotify != other.lastTimeNotify) {
//            return false;
//        }
//        if (this.typeBoss != other.typeBoss) {
//            return false;
//        }
//        if (this.lastTimeTargetPlayer != other.lastTimeTargetPlayer) {
//            return false;
//        }
//        if (this.timeTargetPlayer != other.timeTargetPlayer) {
//            return false;
//        }
//        if (this.timeToRestart != other.timeToRestart) {
//            return false;
//        }
//        if (this.lastTimeAttack != other.lastTimeAttack) {
//            return false;
//        }
//        if (!Arrays.deepEquals(this.data, other.data)) {
//            return false;
//        }
//        if (this.bossStatus != other.bossStatus) {
//            return false;
//        }
//        if (!Objects.equals(this.lastZone, other.lastZone)) {
//            return false;
//        }
//        if (!Objects.equals(this.playerTarget, other.playerTarget)) {
//            return false;
//        }
//        if (!Objects.equals(this.parentBoss, other.parentBoss)) {
//            return false;
//        }
//        if (!Arrays.deepEquals(this.bossAppearTogether, other.bossAppearTogether)) {
//            return false;
//        }
//        if (!Objects.equals(this.zoneFinal, other.zoneFinal)) {
//            return false;
//        }
//        return Objects.equals(this.bossInstance, other.bossInstance);
//    }
    @Override
    public void wakeupAnotherBossWhenDisappear() {
        System.out.println("Boss " + this.name + " die....");
    }

    @Override
    public void SpawnCombat() {
        System.out.println("ahihi");
    }

    public void setBossStatus(BossStatus bossStatus) {
        this.bossStatus = bossStatus;
    }
}
