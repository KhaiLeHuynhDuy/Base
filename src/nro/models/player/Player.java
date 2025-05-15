package nro.models.player;

import com.girlkun.database.GirlkunDB;
import nro.consts.ConstMap;
import nro.models.map.MapMaBu.MapMaBu;
import nro.models.skill.PlayerSkill;

import java.util.List;

import nro.models.clan.Clan;
import nro.models.intrinsic.IntrinsicPlayer;
import nro.models.item.Item;
import nro.models.item.ItemTime;
import nro.models.npc.specialnpc.MagicTree;
import nro.consts.ConstPlayer;
import nro.consts.ConstTask;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.mob.MobMe;
import nro.data.DataGame;

import nro.models.boss.BossType;
import nro.models.card.Card;
import nro.models.clan.ClanMember;
import nro.models.map.BDKB.BanDoKhoBauService;
import nro.models.map.ItemMap;
import nro.models.map.TrapMap;
import nro.models.map.Zone;
import nro.models.map.blackball.BlackBallWar;
import nro.models.map.doanhtrai.DoanhTraiService;
import nro.models.matches.IPVP;
import nro.models.matches.TYPE_LOSE_PVP;
import nro.models.skill.Skill;
//import nro.models.npc.specialnpc.BillEgg;
//import nro.models.npc.specialnpc.MabuGayEgg;
import nro.services.*;
import nro.server.io.MySession;
import nro.models.task.TaskPlayer;
import nro.network.io.Message;
import nro.server.Client;
import nro.services.func.ChangeMapService;
import nro.services.func.ChonAiDay;
import nro.services.func.CombineNew;
import nro.utils.Logger;
import nro.utils.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import lombok.Getter;
import lombok.Setter;
import nro.consts.ConstItem;
//import static nro.consts.ConstDataEvent.isTraoQua;
import nro.dialog.ConfirmDialog;
import nro.map.RanDoc.ConDuongRanDocService;
import nro.models.map.giaidauvutru.Giaidauvutru;

import nro.models.mob.Mob;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.phuban.DragonNamecWar.TranhNgocService;
import nro.utils.SkillUtil;

public class Player {

    public boolean isAutoGraping;

    public boolean isAutoGraping() {
        return isAutoGraping;
    }

    public void setAutoGraping(boolean autoGraping) {
        isAutoGraping = autoGraping;
    }

    public String[] input;
    public int typeInput = -1;

    public int typeRecvieArchiment = 0;
    public int slBanhChung;
    public int slBanhTet;
    public boolean isUseKiemGo = false;
    public boolean isUseChanMenh = false;
    public int idPlayerForNPC;
    public String Hppl = "\n";
    public MySession session;
    public PlayerEvent event;
    public PointFusion pointfusion;
    public boolean beforeDispose;
    public List<Archivement> archivementList = new ArrayList<>();
    public List<Archivement_diem> archivementList_diem = new ArrayList<>();
    public List<Archivement_BoMong> archivementList_bomong = new ArrayList<>();
    public boolean isPet;
    public boolean isNewPet;
    public boolean isBoss;
    public boolean isPetFollow;

//    public short hpfusion = 0;
//    public short mpfusion = 0;
//    public short damefusion = 0;
    public int NguHanhSonPoint = 0;
    public boolean isBot = false;
    public IPVP pvp;
    public int pointPvp;
    public byte maxTime = 30;
    public byte type = 0;
    public byte type2 = 0;
    // public MiniPet minipet;
    public boolean isMiniPet;
    public boolean callisMiniPet;
    public long limitgold = 0;
    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public List<Zone> mapGiaidauBall;
    public List<Zone> mapMaBu;
    public MiniPet minipet;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public NewPet newpet;
    public NewPet newpet1;
    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    ////    public MabuGayEgg BuugayEgg;
//    public BillEgg billEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public SkillService playerSkill2;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public RewardGiaidau rewardGiaidau;
    public EffectFlagBag effectFlagBag;
    public FightMabu fightMabu;
    public SkillSpecial skillSpecial;
    public Clan clan;
    public ClanMember clanMember;

    public List<Friend> friends;
    public List<Enemy> enemies;
    public long lastTimeHoldBlackBall;
    public int tempIdBlackBallHold = -1;
    public boolean isHoldBlackBall;
    public boolean isHoldNamecBall;
    public boolean isHoldNamecBallTranhDoat;
    public int tempIdNamecBallHoldTranhDoat = -1;
    public long lastTimePickItem;
    public long lastTimeUpdateBallWar;
    //    public Zone zone;
//    public Zone mapBeforeCapsule;
//    public Thu_TrieuHoi TrieuHoipet;
//    public NhiemvuChienthan chienthan;
//    public Taixiu taixiu;
    public long id;
    //    private long id;
    public long vnd;
    public long account_id;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public short head;
    public short body;
    public short leg;

    public byte typePk;
    public List<Card> Cards = new ArrayList<>();
    public short idAura = -1;
    public byte cFlag;

    //Kỹ năng
    public long dameMaFuBa = 0;

    public boolean haveTennisSpaceShip;
    public boolean isWin;
    public boolean justRevived;
    public long lastTimeRevived;
    public long lastTimeWin;
    public int violate;
    public byte totalPlayerViolate;
    public long timeChangeZone;
    public long lastTimeUseOption;

    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public int goldNormar;
    public int goldVIP;
    public int VNDNormar;
    public int VNDVIP;
    public int levelWoodChest;
    public boolean receivedWoodChest;
    public int goldChallenge;
    public int rubyChallenge;
    public int ThoiVangChallenge;

    public byte capCS;
    public byte capTT;
    public int dotpha;
    public transient boolean isProcessing = false; // Tạm thời, không ghi vào DB

    public boolean isUseTrucCoDan;
    public boolean titleitem;
    public boolean titlett;
    public boolean isTitleUse;
    public long lastTimeTitle1;
    public boolean isTitleUse2;
    public long lastTimeTitle2;
    public boolean isTitleUse3;
    public long lastTimeTitle3;

    public String tt = "";
    private Iterable<Item.ItemOption> itemOptions;
    public List<String> textRuongGo = new ArrayList<>();
    private static final long TIME_TRUNG_THU = 120000;
    public boolean khisukien = false;
    public List<Integer> idEffChar = new ArrayList<>();
    //     public String TrieuHoiNamePlayer;
//    public int TrieuHoiCapBac = -1;
//    public String TenThuTrieuHoi;
//    public int TrieuHoiThucAn;
//    public long TrieuHoiDame;
//    public long TrieuHoiHP;
//    public long TrieuHoilastTimeThucan;
//    public int TrieuHoiLevel;
//    public long TrieuHoiExpThanThu;
//    public Player TrieuHoiPlayerAttack;
//    public double TrieuHoidamethanmeo;
//    public long Autothucan;
//    public boolean trangthai = false;
    public byte countKG;
    public boolean firstJoinKG;
    public long lastimeJoinKG;
    public long rankSieuHang;
    public long numKillSieuHang;

    public int bdkb_countPerDay;
    public long bdkb_lastTimeJoin;
    public boolean bdkb_isJoinBdkb;

    public int cdrd_countPerDay;
    public long cdrd_lastTimeJoin;
    public boolean cdrd_isJoinCdrd;

    public boolean lockPK;
    public Timer timerDHVT;
    public Player _friendGiaoDich;
    public int cuoc;
    public int rubyWin = 0;
//    @Setter
//    @Getter
    private ConfirmDialog confirmDialog;

    public boolean isDanhHieu;
    public int idDanhHieu = -1;

    public boolean isChanMenh;
    public int idChanMenh = -1;

    public boolean isDanhHieuTOP;
    public boolean isDanhHieuTOP2;
    public int idDanhHieuTOP = -1;
    public int idDanhHieuTOP2 = -1;

    public ItemMap veTinhTriLuc = null;
    public ItemMap veTinhSinhLuc = null;
    public ItemMap veTinhPhongThu = null;
    public ItemMap veTinhTriTue = null;

    public long lastTimeVeTinhTriLuc = 0;
    public long lastTimeVeTinhSinhLuc = 0;

    public Player() {
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        rewardGiaidau = new RewardGiaidau(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        charms = new Charms();
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
        archivementList = new ArrayList<>();
        archivementList_diem = new ArrayList<>();
        archivementList_bomong = new ArrayList<>();
        event = new PlayerEvent(this);
        pointfusion = new PointFusion(this);
//        chienthan = new NhiemvuChienthan();
//        taixiu = new Taixiu();
    }

    public PointFusion getPointfusion() {
        return this.pointfusion;
    }

    //--------------------------------------------------------------------------
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    public ConfirmDialog getConfirmDialog() {
        return confirmDialog;
    }

    public void setConfirmDialog(ConfirmDialog confirmDialog) {
        this.confirmDialog = confirmDialog;
    }

    //--------------------------------------------------------------------------
    public void setSession(MySession session) {
        this.session = session;
    }

    public boolean isPl() {
        return !isPet && !(this instanceof Referee) && !(this instanceof Yajiro) && !isBoss && !isNewPet && !isMiniPet;
    }
//    public boolean isPl() {
//        return !isPet && !isBoss && !isNewPet;
//    }

    public boolean isValidSession() {
        return this.session.isConnected();
    }

    public void sendMessage(Message msg) {
        if (session != null) {
            session.sendMessage(msg);
        }
    }

    //   public void pickUpItem(Player player,ItemMap itemMap) {
//        // Kiểm tra nếu hành trang đầy thì không nhặt item
//        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) { 
//            System.out.println("Hành trang đầy, không thể nhặt thêm item.");
//            return;
//        }
//        
//        // Nhặt item và thêm vào hành trang
//         InventoryServiceNew.gI().addItemBag(player,itemMap);
//        System.out.println("Item được nhặt và đưa vào hành trang.");
//        
//        // Xóa item từ bản đồ
//        itemMap.zone.removeItemMap(itemMap);
//    }
    // Các phương thức khác của lớp Player
    public MySession getSession() {
        return this.session;

    }

    public int version() {
        return session.version;
    }

    public boolean isVersionAbove(int version) {
        return version() >= version;
    }

    public void updateSupportTime(Player pl) {
//        if (pl.isPl()) {
//            if (pl.zone != null) {
//                pl.zone.hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//            }
//            if (pl.zone != null && pl.zone.hours >= ConstMap.TIME_START_SUPPORT && pl.zone.hours <= ConstMap.TIME_END_SUPPORT) {
//                if (!pl.zone.getBosses().isEmpty()) {
//                    List<Player> listBoss = pl.zone.getBosses();
//                    for (Player plBoss : listBoss) {
//                        if (plBoss != null) {
//                            if (pl.playerTask.taskMain.id != 19 && (plBoss.id == BossType.KUKU || plBoss.id == BossType.MAP_DAU_DINH || plBoss.id == BossType.RAMBO)) {
//                                ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, -1);
//                                Service.gI().sendThongBaoOK(pl, " changeZone Trong thời gian hỗ trợ\nKhông thể vào khu vực này");
//                                return;
//                            }
//                            if (pl.playerTask.taskMain.id != 20 && (plBoss.id == BossType.TDST)) {
//                                ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, -1);
//                                Service.gI().sendThongBaoOK(pl, " changeZone Trong thời gian hỗ trợ\nKhông thể vào khu vực này");
//                                return;
//                            }
//                            if (pl.playerTask.taskMain.id != 21 && (plBoss.id == BossType.FIDE)) {
//                                ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, -1);
//                                Service.gI().sendThongBaoOK(pl, " changeZone Trong thời gian hỗ trợ\nKhông thể vào khu vực này");
//                                return;
//                            }
//                            if (pl.playerTask.taskMain.id != 22 && (plBoss.id == BossType.ANDROID_19 || plBoss.id == BossType.DR_KORE)) {
//                                ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, -1);
//                                Service.gI().sendThongBaoOK(pl, " changeZone Trong thời gian hỗ trợ\nKhông thể vào khu vực này");
//                                return;
//                            }
//                            if (pl.playerTask.taskMain.id != 23 && (plBoss.id == BossType.ANDROID_13 || plBoss.id == BossType.ANDROID_14 || plBoss.id == BossType.ANDROID_15)) {
//                                ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, -1);
//                                Service.gI().sendThongBaoOK(pl, " changeZone Trong thời gian hỗ trợ\nKhông thể vào khu vực này");
//                                return;
//                            }
//                            if (pl.playerTask.taskMain.id != 24 && (plBoss.id == BossType.PIC || plBoss.id == BossType.POC || plBoss.id == BossType.KING_KONG)) {
//                                ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, -1);
//                                Service.gI().sendThongBaoOK(pl, " changeZone Trong thời gian hỗ trợ\nKhông thể vào khu vực này");
//                                return;
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
    }

    public long lastTimeDropTail;

    public void update() {
//        if (this.isBot) {
//            active();
//        }
        if (!this.beforeDispose && !isBot) {
            try {
                if (!this.iDMark.isIsBan()) {

                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (newpet != null) {
                        newpet.update();
                    }

                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (minipet != null) {
                        minipet.update();
                    }
                    if (event != null) {
                        event.update();
                    }
//                    if (pointfusion != null) {
//                        pointfusion.update();
//                    }

                    if (this.lastTimeTitle1 != 0 && Util.canDoWithTime(this.lastTimeTitle1, 6000)) {
                        lastTimeTitle1 = 0;
                        isTitleUse = false;
                    }
                    if (this.lastTimeTitle2 != 0 && Util.canDoWithTime(this.lastTimeTitle2, 6000)) {
                        lastTimeTitle2 = 0;
                        isTitleUse2 = false;
                    }

                    if (this.lastTimeTitle3 != 0 && Util.canDoWithTime(this.lastTimeTitle3, 6000)) {
                        lastTimeTitle3 = 0;

                        isTitleUse3 = false;
                    }
//                    useItem567891011();
                    if (this.isPl() && this.zone != null && this.zone.items.size() > 0) {
                        for (ItemMap item : this.zone.items) {
                            if (item != null
                                    && Util.myGetDistcance(this.location.x, this.location.y, item.x, item.y) <= ConstMap.RANGE_VE_TINH
                                    && item.isBelongToMe(this)) {
                                if (item.itemTemplate.id == 345) {
                                    veTinhSinhLuc = item;
                                }
                                if (item.itemTemplate.id == 344) {
                                    veTinhPhongThu = item;
                                }
                                if (item.itemTemplate.id == 343) {
                                    veTinhTriTue = item;
                                }
                                if (item.itemTemplate.id == 342) {
                                    veTinhTriLuc = item;
                                }
                            }
                        }
                    }

                    if (!this.isDie() && Util.canDoWithTime(lastTimeVeTinhTriLuc, ConstMap.TIME_HOI_VE_TINH)
                            && ItemMap.isInVeTinhRange(this, ConstItem.VE_TINH_TRI_LUC, this.location.x, this.location.y)) {
                        long mpTriLuc = NPoint.calPercent(this.nPoint.mpMax, ConstMap.PERCENT_VE_TINH_TRI_LUC);
                        this.nPoint.addMp(mpTriLuc);

                        PlayerService.gI().hoiPhuc(this, 0, mpTriLuc);

                        lastTimeVeTinhTriLuc = System.currentTimeMillis();
                    }
                    if (!this.isDie() && Util.canDoWithTime(lastTimeVeTinhSinhLuc, ConstMap.TIME_HOI_VE_TINH)
                            && ItemMap.isInVeTinhRange(this, ConstItem.VE_TINH_SINH_LUC, this.location.x, this.location.y)) {
                        long hpSinhLuc = NPoint.calPercent(this.nPoint.hpMax, ConstMap.PERCENT_VE_TINH_SINH_LUC);
                        this.nPoint.addHp(hpSinhLuc);
                        PlayerService.gI().hoiPhuc(this, hpSinhLuc, 0);

                        lastTimeVeTinhSinhLuc = System.currentTimeMillis();
                    }
                    if (gender == 2) {
                        if (cFlag == 8 && khisukien && Util.canDoWithTime(lastTimeDropTail, TIME_TRUNG_THU)) {
                            if (!this.effectSkill.isMonkey) {
                                EffectSkillService.gI().sendEffectMonkey(this);
                                EffectSkillService.gI().setIsMonkeyTrungThu(this);
                                EffectSkillService.gI().sendEffectMonkey(this);

                                Service.gI().sendSpeedPlayer(this, 0);
                                Service.gI().Send_Caitrang(this);
                                Service.gI().sendSpeedPlayer(this, -1);
                                if (!this.isPet) {
                                    PlayerService.gI().sendInfoHpMp(this);
                                }
                                Service.gI().point(this);
                                Service.gI().Send_Info_NV(this);
                                Service.gI().sendInfoPlayerEatPea(this);
                            }
                            khisukien = false;
                        }
                    }

//                        KhiGasService.gI().update(this);
                    BlackBallWar.gI().update(this);
                    MapMaBu.gI().update(this);
//                    MabuWar.gI().update(this);
//                    MabuWar14h.gI().update(this);
                    TranhNgoc.gI().update(this);
                    updateSupportTime(this);
                    BanDoKhoBauService.gI().updatePlayer(this);
                    DoanhTraiService.gI().updatePlayer(this);
                    Giaidauvutru.gI().update(this);
                    ConDuongRanDocService.gI().updatePlayer(this);

                    if (this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture(false);

                    }
                    if (this.iDMark.isGoToBDKB() && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.iDMark.setGoToBDKB(false);
                    }
                    if (this.iDMark.isGoToGas() && Util.canDoWithTime(this.iDMark.getLastTimeGotoGas(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 149, -1, 163);
                        this.iDMark.setGoToGas(false);
                    }
                    if (this.iDMark.isGoToCDRD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToCDRD(), 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 141, -1, 238);
                        this.iDMark.setGoToCDRD(false);
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                    if (isWin && this.zone.map.mapId == 129 && Util.canDoWithTime(lastTimeWin, 2000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 52, 0, -1);
                        isWin = false;
                    }
//                    if (this.isPl() && this.inventory.itemsBody.get(7) != null) {
//                        Item it = this.inventory.itemsBody.get(7);
//                        if (it != null && it.isNotNullItem() && this.newpet == null) {
//                            switch (it.template.id) {
//                                case 936: // tloc
//                                    PetService.Pet2(this, 718, 719, 720);
//                                    Service.gI().point(this);
//                                    break;
//                                case 892: // tho xam
//                                    PetService.Pet2(this, 882, 883, 884);
//                                    Service.gI().point(this);
//                                    break;
//                                case 893: // tho trang
//                                    PetService.Pet2(this, 885, 886, 887);
//                                    Service.gI().point(this);
//                                    break;
//                                case 942:
//                                    PetService.Pet2(this, 966, 967, 968);
//                                    Service.gI().point(this);
//                                    break;
//                                case 943:
//                                    PetService.Pet2(this, 969, 970, 971);
//                                    Service.gI().point(this);
//                                    break;
//                                case 944:
//                                    PetService.Pet2(this, 972, 973, 974);
//                                    Service.gI().point(this);
//                                    break;
//                                case 967:
//                                    PetService.Pet2(this, 1050, 1051, 1052);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1039:
//                                    PetService.Pet2(this, 1089, 1090, 1091);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1040:
//                                    PetService.Pet2(this, 1092, 1093, 1094);
//                                    Service.gI().point(this);
//                                    break;
//                                case 916:
//                                    PetService.Pet2(this, 931, 932, 933);
//                                    Service.gI().point(this);
//                                    break;
//                                case 917:
//                                    PetService.Pet2(this, 928, 929, 930);
//                                    Service.gI().point(this);
//                                    break;
//                                case 918:
//                                    PetService.Pet2(this, 925, 926, 927);
//                                    Service.gI().point(this);
//                                    break;
//                                case 919:
//                                    PetService.Pet2(this, 934, 935, 936);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1107:
//                                    PetService.Pet2(this, 1155, 1156, 1157);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2045:
//                                    PetService.Pet2(this, 2060, 2061, 2062);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2046:
//                                    PetService.Pet2(this, 2063, 2064, 2065);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2047:
//                                    PetService.Pet2(this, 2066, 2067, 2068);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2048:
//                                    PetService.Pet2(this, 2069, 2070, 2071);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2017:
//                                    PetService.Pet2(this, 778, 779, 780);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2018:
//                                    PetService.Pet2(this, 813, 814, 815);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2019:
//                                    PetService.Pet2(this, 891, 892, 893);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2020:
//                                    PetService.Pet2(this, 894, 895, 896);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2021:
//                                    PetService.Pet2(this, 897, 898, 899);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2022:
//                                    PetService.Pet2(this, 925, 926, 927);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2023:
//                                    PetService.Pet2(this, 928, 929, 930);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2024:
//                                    PetService.Pet2(this, 931, 932, 933);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2025:
//                                    PetService.Pet2(this, 934, 935, 936);
//                                    Service.gI().point(this);
//                                    break;
//                                case 2053:
//                                    PetService.Pet2(this, 1227, 1228, 1229);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1407:
//                                    PetService.Pet2(this, 663, 664, 665);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1408:
//                                    PetService.Pet2(this, 1074, 1075, 1076);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1413:
//                                    PetService.Pet2(this, 1239, 1240, 1241);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1414:
//                                    PetService.Pet2(this, 1242, 1243, 1244);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1415:
//                                    PetService.Pet2(this, 1245, 1246, 1247);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1416:
//                                    PetService.Pet2(this, 1077, 1078, 1079);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1417:
//                                    PetService.Pet2(this, 763, 764, 765);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1418:
//                                    PetService.Pet2(this, 778, 779, 780);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1419:
//                                    PetService.Pet2(this, 557, 558, 559);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1420:
//                                    PetService.Pet2(this, 813, 814, 815);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1409:
//                                    PetService.Pet2(this, 1158, 1159, 1160);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1410:
//                                    PetService.Pet2(this, 1155, 1156, 1157);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1411:
//                                    PetService.Pet2(this, 1183, 1184, 1185);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1412:
//                                    PetService.Pet2(this, 1201, 1202, 1203);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1421:
//                                    PetService.Pet2(this, 1257, 1258, 1259);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1422:
//                                    PetService.Pet2(this, 1260, 1261, 1262);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1128:
//                                    PetService.Pet2(this, 1343, 1344, 1345);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1430:
//                                    PetService.Pet2(this, 1432, 1433, 1434);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1435:
//                                    PetService.Pet2(this, 1447, 1448, 1449);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1436:
//                                    PetService.Pet2(this, 1450, 1451, 1452);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1460:
//                                    PetService.Pet2(this, 1957, 1958, 1959);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1390:
//                                    PetService.Pet2(this, 1465, 1466, 1467);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1391:
//                                    PetService.Pet2(this, 1468, 1469, 1470);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1392:
//                                    PetService.Pet2(this, 1471, 1472, 1473);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1480:
//                                    PetService.Pet2(this, 1535, 1536, 1537);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1481:
//                                    PetService.Pet2(this, 1538, 1539, 1540);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1500:
//                                    PetService.Pet2(this, 1630, 1631, 1632);
//                                    Service.gI().point(this);
//                                    break;
//                                case 908:
//                                    PetService.Pet2(this, 891, 892, 893);
//                                    Service.gI().point(this);
//                                    break;
//                                case 909:
//                                    PetService.Pet2(this, 897, 898, 899);
//                                    Service.gI().point(this);
//                                    break;
//                                case 910:
//                                    PetService.Pet2(this, 894, 895, 896);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1018:
//                                    PetService.Pet2(this, 1183, 1184, 1185);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1019:
//                                    PetService.Pet2(this, 1201, 1202, 1203);
//                                    Service.gI().point(this);
//                                    break;
//                                case 1020:
//                                    PetService.Pet2(this, 1239, 1240, 1241);
//                                    Service.gI().point(this);
//                                    break;
//                            }
//                        }
//                    } else if (this.isPl() && newpet != null && !this.inventory.itemsBody.get(7).isNotNullItem()) {
//                        ChangeMapService.gI().exitMap(newpet);
//                        newpet.dispose();
//                        newpet = null;
//                        // newpet1.dispose();
//                        //  newpet1 = null;
//                    }
                    if (this.isPl() && this.isDie()) {
                        if (this.nPoint.power > 1500000) {
                            if (this.itemTime != null && this.itemTime.isLoNuocThanhx2) {
                                this.nPoint.power -= ((long) this.nPoint.power * 10 / 1000);
                            }
                            if (this.itemTime != null && this.itemTime.isLoNuocThanhx5) {
                                this.nPoint.power -= ((long) this.nPoint.power * 30 / 1000);
                            }
                            if (this.itemTime != null && this.itemTime.isLoNuocThanhx7) {
                                this.nPoint.power -= ((long) this.nPoint.power * 50 / 1000);
                            }
                            if (this.itemTime != null && this.itemTime.isLoNuocThanhx10) {
                                this.nPoint.power -= ((long) this.nPoint.power * 70 / 1000);
                            }
                            if (this.itemTime != null && this.itemTime.isLoNuocThanhx15) {
                                this.nPoint.power -= ((long) this.nPoint.power * 100 / 1000);
                            }
                        }
                        Service.gI().point(this);
                    }

//                    if (this.location != null && location.lastTimeplayerMove < System.currentTimeMillis() - 4 * 60 * 60 * 1000) {
//                        Client.gI().kickSession(session);
//                    }
//                    if (this != null && this.isPl()) {
//                        if (Util.canDoWithTime(lastTimeUpdate, 60000)) {
//
//                            PlayerDAO.updatePlayer(this);
//                            lastTimeUpdate = System.currentTimeMillis();
//                        }
//
//                    }
                } else {
                    if (this.iDMark != null && Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//                e.getStackTrace();
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    public long lastTimeUpdate = System.currentTimeMillis();

//    public static boolean updateCard(Player player) {
//        try (Connection con = GirlkunDB.getConnection(); PreparedStatement ps = con.prepareStatement("update player set data_card = ? where name = ?")) {
//            ps.setString(1, JSONValue.toJSONString(player.Cards));
//            ps.setString(2, player.name);
//            ps.executeUpdate();
//            ps.close();
//            con.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    public void useItem567891011() {
//        ///////////////////////PET INDEX 10////////////////////////////////////
////                    if (this.inventory.itemsBody.size() > 10) {
////                        Item itemAt10 = this.inventory.itemsBody.get(10);
////                        if ((itemAt10.isNullItem() && callisMiniPet1 == true) || (itemAt10.template != null && itemAt10.template.type == 72)) {
////                            if (minipet != null) {
////                                minipet.changeStatus(MiniPet.VENHA);
////                                callisMiniPet1 = false;
////                            }
////                        }
////                    }
////
////                    if (this.inventory.itemsBody.size() > 10) {
////                        Item itemAt10 = this.inventory.itemsBody.get(10);
////                        if ((itemAt10.isNullItem() && !callisMiniPet1) || (itemAt10.template != null && itemAt10.template.type == 21)) {
////                            if (itemAt10.template != null) { // Kiểm tra lại để tránh NullPointerException
////                                MiniPet.callMiniPet(this, itemAt10.template.id);
////                                callisMiniPet1 = true;
//////                                Service.gI().sendPetFollow(this, (short) 0);
////                            }
////                        }
////                    }
////                    ///////////////////////PET INDEX 10////////////////////////////////////
//
////                    if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 7) {
////                    if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 7
////                            && this.inventory.itemsBody.get(7).isNullItem() && this.callisMiniPet1 == true) {
////                        this.minipet.changeStatus(MiniPet.VENHA);
////                        this.callisMiniPet1 = false;
////                    }
//        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 7
//                && this.inventory.itemsBody.get(7).isNotNullItem() && !this.callisMiniPet) {
//            Item item = this.inventory.itemsBody.get(7);
//            if (item != null && item.template != null) {
//                if (item.template.type == 21) {
//                    MiniPet.callMiniPet(this, item.template.id);
//                    this.minipet.reCall();
//                    this.callisMiniPet = true;
//                }
//            }
//        }
////        if (this.isPl() && !this.isDanhHieu
////                && this.inventory != null
////                && this.inventory.itemsBody.size() > 6
////                && this.inventory.itemsBody.get(6) != null) {
////            Item item = this.inventory.itemsBody.get(6);
////            if (item != null && item.template != null) {
////                if (item.template.type == 35) {
////                    Service.gI().addEffectChar(this, item.template.part, 1, -1, -1, 0);
////                }
////            }
////        }
////        if (this.isPl() && !this.isChanMenh
////                && this.inventory != null
////                && this.inventory.itemsBody.size() > 9
////                && this.inventory.itemsBody.get(9) != null) {
////            Item item = this.inventory.itemsBody.get(9);
////            if (item != null && item.template != null) {
////                if (item.template.type == 25) {
////                    Service.gI().addEffectChar(this, item.template.part, 0, -1, 1, -1);
////                }
////            }
////        }
////        if (this.isPl() && !this.isPetFollow
////                && this.inventory != null
////                && this.inventory.itemsBody.size() > 10
////                && this.inventory.itemsBody.get(10) != null) {
////            Item item = this.inventory.itemsBody.get(10);
////            if (item != null && item.template != null) {
////                if (item.template.type == 72) {
////                    Service.gI().sendPetFollow(this, this.getLinhThu(), (byte) 1);
////                }
////            }
////        }
//
////        if (this.isPl() && !isDanhHieuTOP) {
////            for (int i = 0; i < 4; i++) {
////                if (this.getSession() != null && this.getSession().totalvnd >= 2000000) {
////                    Service.gI().addEffectChar(this, 179, 1, -1, -1, 0);
////                    isDanhHieuTOP = true;
////                    break;
////                } else if (this.getSession() != null && this.getSession().totalvnd >= 1000000) {
////                    Service.gI().addEffectChar(this, 180, 1, -1, -1, 0);
////                    isDanhHieuTOP = true;
////                    break;
////                } else if (this.getSession() != null && this.getSession().totalvnd >= 500000) {
////                    Service.gI().addEffectChar(this, 181, 1, -1, -1, 0);
////                    isDanhHieuTOP = true;
////                    break;
////                } else if (this.getSession() != null && this.getSession().totalvnd >= 200000) {
////                    Service.gI().addEffectChar(this, 182, 1, -1, -1, 0);
////                    isDanhHieuTOP = true;
////                    break;
////                }
////            }
////            for (int i = 0; i < 5; i++) {
////                if (this.nPoint != null && this.nPoint.power >= 230000000000L) {
////                    Service.gI().addEffectChar(this, 58, 1, -1, -1, 0);
////                    isDanhHieuTOP2 = true;
////                    break;
////                } else if (this.nPoint != null && this.nPoint.power >= 200000000000L) {
////                    Service.gI().addEffectChar(this, 57, 1, -1, -1, 0);
////                    isDanhHieuTOP2 = true;
////                    break;
////                } else if (this.nPoint != null && this.nPoint.power >= 120000000000L) {
////                    Service.gI().addEffectChar(this, 56, 1, -1, -1, 0);
////                    isDanhHieuTOP2 = true;
////                    break;
////                } else if (this.nPoint != null && this.nPoint.power >= 80000000000L) {
////                    Service.gI().addEffectChar(this, 35, 1, -1, -1, 0);
////                    isDanhHieuTOP2 = true;
////                    break;
////                } else if (this.nPoint != null && this.nPoint.power >= 40000000000L) {
////                    Service.gI().addEffectChar(this, 36, 1, -1, -1, 0);
////                    isDanhHieuTOP2 = true;
////                    break;
////                }
////            }
////        }
//    }
    //--------------------------------------------------------------------------
    /*
     * { 380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * { 383, 384, 385}: ht porata xayda trái đất
     * { 391, 392, 393}: ht namếc
     * { 870, 871, 872}: ht c2 trái đất
     * { 873, 874, 875}: ht c2 namếc
     * { 867, 878, 869}: ht c2 xayda
     */
    private static final short[][] idOutfitFusion = {
        {380, 381, 382},
        {383, 384, 385},
        {391, 392, 393},
        {870, 871, 872},
        {873, 874, 875},
        {867, 868, 869},
        {2108, 2109, 2110},
        {2105, 2106, 2107},
        {2102, 2103, 2104},
        {1324, 1325, 1326},
        {1321, 1322, 1323},
        {1318, 1319, 1320},
        {1372, 1373, 1374},
        {1366, 1367, 1368},
        {1369, 1370, 1371},
        {1372, 1373, 1374},
        {1366, 1367, 1368},
        {1369, 1370, 1371}};

    public byte getAura() {
        if (this.isPl() && this.effectSkill != null && this.effectSkill.isBienHinh) {
            switch (this.effectSkill.levelBienHinh) {
                case 1 -> {
                    return 3; // id aura cua bien hinh 1
                }
                case 2 -> {
                    return 57;
                }
                case 3 -> {
                    return 61;
                }
                case 4 -> {
                    return 60;
                }
                case 5 -> {
                    return 2;
                }
                case 6 -> {
                    return 17;
                }
                case 7 -> { // id aura cua bien hinh 7
                    return 32;
                }
            }
        }

        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 5) {
            Item item = this.inventory.itemsBody.get(5);
            if (!item.isNotNullItem()) {
                return auraPower();
            }
            // Tú ăn cức ko -- OK -- Vay la đồng ý r nhé -- =))) tét di
            switch (item.template.id) {
                case 1523:
                    return 89;
                case 1600:
                    return 28;
                case 1504:
                    return 63;
                case 1488:
                    return 57;
                case 1489:
                    return 65;
                case 1490:
                    return 50;
                case 1491:
                    return 64;
                case 1492:
                    return 64;
                case 1493:
                    return 7;
                case 1494:
                    return 7;
                case 1495:
                    return 7;
                case 1496:
                    return 12;
                case 1497:
                    return 63;
                case 1498:
                    return 59;
                case 1499:
                    return 57;
                case 1479:
                    return 65;
                case 1487:
                    return 57;
                case 1486:
                    return 58;
                case 1485:
                    return 60;
                case 1483:
                    return 64;
                case 1484:
                    return 56;
                case 1469:
                    return 18;
                case 1471:
                    return 17;
                case 1470:
                    return 56;
                case 1468:
                    return 59;
                case 1259:
                    return 63;
                case 1429:
                case 1434:
                    return 62;
                case 1381:
                    return 80;
                case 1224:
                case 1247:
                case 1248:
                    return 60;
                case 1228:
                    return 34;
                case 1229:
                    return 56;
                case 1230:
                    return 37;
                case 1231:
                    return 37;
                case 1232:
                    return 2;

                case 1320:
                    return 36;
                case 1319:
                    return 32;
                case 1999:
                    return 31;
                case 1998:
                    return 29;
                case 1344:
                    return 58;
                case 1345:
                    return 56;
                case 1346:
                    return 57;
                case 1353:
                    //id cai trang
                    return 29; // id img bye=)) name
                case 1367:
                    //id cai trang
                    return 30; // id img bye=)) name
                case 1368:
                    //id cai trang
                    return 31; // id img bye=)) name

                case 2199:
                    //id cai trang
                    return 7; // id img bye=)) name
                case 2200:
                    //id cai trang
                    return 12; // id img bye=)) name
                case 2201:
                    //id cai trang
                    return 35; // id img bye=)) name
                case 2202:
                    //id cai trang
                    return 19; // id img bye=)) name
                case 2203:
                    //id cai trang
                    return 25; // id img bye=)) name

                case 2204:
                    //id cai trang
                    return 9; // id img bye=)) name
                case 2207:
                    //id cai trang
                    return 26; // id img bye=)) name
                case 2208:
                    //id cai trang
                    return 26; // id img bye=)) name
                case 2231:
                    //id cai trang
                    return 2; // id img bye=)) name
                case 2232:
                    //id cai trang
                    return 22; // id img bye=)) name
                case 2233:
                    //id cai trang
                    return 3; // id img bye=)) name
                case 2235:
                    //id cai trang
                    return 10; // id img bye=)) name
                case 2236:
                    //id cai trang
                    return 10; // id img bye=)) name
                case 2055:
                    //id cai trang
                    return 50; // id img bye=)) name
                case 1098:
                    return 9;
                case 2112:
                    return 16;
                case 2099:
                    return 10;
                case 2111:
                    return 11;
                case 2098:
                    return 16;
                case 2008:
                    return 16;
                case 2009:
                    return 11;
                case 2010:
                    return 10;
                case 1097:
                    return 10;
                case 1093:
                    return 9;
                case 2038:
                    return 9;
                case 2110:
                    return 17;
                case 2109:
                    return 17;
                case 2108:
                    return 12;
                case 2107:
                    return 6;
                case 2106:
                    return 16;
                case 2063:
                    return 6;
                case 2011:
                    return 6;
                case 2051:
                    return 12;
                case 2100:
                    return 22;
                case 2035:
                    return 10;
                case 2049:
                    return 11;
                case 2040:
                    return 9;
                case 2062:
                    return 7;
                case 2065:
                    return 10;
                case 2066:
                    return 9;
                case 2090:
                    return 6;
                case 2091:
                    return 6;
                case 2092:
                    return 6;
                case 2093:
                    return 10;
                case 2132:
                    return 23;
                case 2142:
                    return 27;
                case 2143:
                    return 17;
                case 2118:
                    return 17;
                case 2144:
                    return 18;
                case 2147:
                    return 24;
                case 2206:
                    return 77;
                case 2218:
                    return 28;

            }
        }
        return auraPower();
    }

    public byte auraPower() {
        if (this.nPoint == null) {
            return 0;
        }
        if (this.nPoint.power >= 1000_000_000_000L) {
            return 92;
        } else if (this.nPoint.power >= 900_000_000_000L) {
            return 91;
        } else if (this.nPoint.power >= 800_000_000_000L) {
            return 90;
        } else if (this.nPoint.power >= 700_000_000_000L) {
            return 89;
        } else if (this.nPoint.power >= 600_000_000_000L) {
            return 88;
        } else if (this.nPoint.power >= 500_000_000_000L) {
            return 87;
        } else if (this.nPoint.power >= 400_000_000_000L) {
            return 86;
        } else if (this.nPoint.power >= 350_000_000_000L) {
            return 85;
        } else if (this.nPoint.power >= 300_000_000_000L) {
            return 84;
        } else if (this.nPoint.power >= 120_000_000_000L) {
            return 83;
        } else if (this.nPoint.power >= 110_000_000_000L) {
            return 82;
        } else if (this.nPoint.power >= 100_000_000_000L) {
            return 6;
        } else if (this.nPoint.power >= 80_000_000_000L) {
            return 6;
        }
        return -1;
    }

    public int isSKHH() {
        for (Item.ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135) {
                switch (itemOption.optionTemplate.id) {
                    case 133:
                        return 136;
                    case 134:
                        return 137;
                    case 135:
                        return 138;

                }
            }
        }
        return -1;
    }

    public byte getEffFront() {
        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 10) {

            Item ct = this.inventory.itemsBody.get(5);
            if (!ct.isNotNullItem()) {
                return -1;
            } else if (ct.template.id == 2147) {
                return 6;
            }

            int levelAo = 0;
            Item.ItemOption optionLevelAo = null;
            int levelQuan = 0;
            Item.ItemOption optionLevelQuan = null;
            int levelGang = 0;
            Item.ItemOption optionLevelGang = null;
            int levelGiay = 0;
            Item.ItemOption optionLevelGiay = null;
            int levelNhan = 0;
            Item.ItemOption optionLevelNhan = null;
            Item itemAo = this.inventory.itemsBody.get(0);
            Item itemQuan = this.inventory.itemsBody.get(1);
            Item itemGang = this.inventory.itemsBody.get(2);
            Item itemGiay = this.inventory.itemsBody.get(3);
            Item itemNhan = this.inventory.itemsBody.get(4);
            for (Item.ItemOption io : itemAo.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    levelAo = io.param;
                    optionLevelAo = io;
                    break;
                }
            }
            for (Item.ItemOption io : itemQuan.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    levelQuan = io.param;
                    optionLevelQuan = io;
                    break;
                }
            }
            for (Item.ItemOption io : itemGang.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    levelGang = io.param;
                    optionLevelGang = io;
                    break;
                }
            }
            for (Item.ItemOption io : itemGiay.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    levelGiay = io.param;
                    optionLevelGiay = io;
                    break;
                }
            }
            for (Item.ItemOption io : itemNhan.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    levelNhan = io.param;
                    optionLevelNhan = io;
                    break;
                }
            }
            if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                    && levelAo >= 8 && levelQuan >= 8 && levelGang >= 8 && levelGiay >= 8 && levelNhan >= 8) {
                return 8;
            } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                    && levelAo >= 7 && levelQuan >= 7 && levelGang >= 7 && levelGiay >= 7 && levelNhan >= 7) {
                return 7;
            } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                    && levelAo >= 6 && levelQuan >= 6 && levelGang >= 6 && levelGiay >= 6 && levelNhan >= 6) {
                return 6;
            } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                    && levelAo >= 5 && levelQuan >= 5 && levelGang >= 5 && levelGiay >= 5 && levelNhan >= 5) {
                return 5;
            } else if (optionLevelAo != null && optionLevelQuan != null && optionLevelGang != null && optionLevelGiay != null && optionLevelNhan != null
                    && levelAo >= 4 && levelQuan >= 4 && levelGang >= 4 && levelGiay >= 4 && levelNhan >= 4) {
                return 4;
            } else {
                return -1;
            }
        }
        return -1;
    }

    public short getHead() {
        if (effectSkill != null && effectSkill.isBienHinh) {
            return (short) ConstPlayer.HEADBIENHINH[this.gender][effectSkill.levelBienHinh - 1];
        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1229 || item.template.id == 1230);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1229 || petItem.template.id == 1230);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1247 || item.template.id == 1230);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1247 || petItem.template.id == 1230);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                return 1408;
            }
            if (hasItem11 && hasItem21 && !sameItem1) {
                return 1414;
            }
        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1229 || item.template.id == 1230);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1229 || petItem.template.id == 1230);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1248 || item.template.id == 1230);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1248 || petItem.template.id == 1230);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                return 1408;
            }

            if (hasItem11 && hasItem21 && !sameItem1) {
                return 1414;
            }

        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1469 || item.template.id == 1471);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1469 || petItem.template.id == 1471);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1470 || item.template.id == 1468);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1470 || petItem.template.id == 1468);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                if (this.gender == 0) {
                    return 1503;
                }
                if (this.gender == 1) {
                    return 1503;
                }
                if (this.gender == 2) {
                    return 1562;
                }
            }
            if (hasItem11 && hasItem21 && !sameItem1) {
                if (this.gender == 0) {
                    return 1503;
                }
                if (this.gender == 1) {
                    return 1503;
                }
                if (this.gender == 2) {
                    return 1562;
                }
            }

        }
        if (this.itemTime != null && this.itemTime.isBienHinhMa) {
////            if (Util.isTrue(80, 100)) {
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
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (effectSkill != null && effectSkill.isBang) {
            return 1251;
        } else if (effectSkill != null && effectSkill.isDa) {
            return 454;
        } else if (effectSkill != null && effectSkill.isCarot) {
            return 406;
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1254;
//        } else if (effectSkill != null && effectSkill.isBienHinh) { 
//            return 192;
        } else if (effectSkill != null && effectSkill.isSoHai) {
            return 882;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) { 
                //   return idOutfitFusion[3 + this.gender][0];
//                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {
                return idOutfitFusion[15 + this.gender][0];
            }
        } else if (this.inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int part = inventory.itemsBody.get(5).template.head;
            if (part != -1) {
                return (short) part;
            }
        }

        return this.head;
    }

    public short getBody() {
        if (effectSkill != null && effectSkill.isBienHinh) {
            return (short) ConstPlayer.BODYBIENHINH[this.gender][effectSkill.levelBienHinh - 1];
        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1229 || item.template.id == 1230);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1229 || petItem.template.id == 1230);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1247 || item.template.id == 1230);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1247 || petItem.template.id == 1230);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                return 1409;
            }
            if (hasItem11 && hasItem21 && !sameItem1) {
                return 1415;
            }
        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1229 || item.template.id == 1230);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1229 || petItem.template.id == 1230);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1248 || item.template.id == 1230);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1248 || petItem.template.id == 1230);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                return 1409;
            }

            if (hasItem11 && hasItem21 && !sameItem1) {
                return 1415;
            }

        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1469 || item.template.id == 1471);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1469 || petItem.template.id == 1471);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1470 || item.template.id == 1468);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1470 || petItem.template.id == 1468);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                if (this.gender == 0) {
                    return 1518;
                }
                if (this.gender == 1) {
                    return 1518;
                }
                if (this.gender == 2) {
                    return 1521;
                }
            }
            if (hasItem11 && hasItem21 && !sameItem1) {
                if (this.gender == 0) {
                    return 1518;
                }
                if (this.gender == 1) {
                    return 1518;
                }
                if (this.gender == 2) {
                    return 1521;
                }
            }

        }
        if (this.itemTime != null && this.itemTime.isBienHinhMa) {
//            if (Util.isTrue(50, 100)) {
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
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (effectSkill != null && effectSkill.isBang) {
            return 1252;
        } else if (effectSkill != null && effectSkill.isDa) {
            return 455;
        } else if (effectSkill != null && effectSkill.isCarot) {
            return 407;
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1255;
//        } else if (effectSkill != null && effectSkill.isBienHinh) { 
//            return 193;
        } else if (effectSkill != null && effectSkill.isSoHai) {
            return 883;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) { 
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {
                return idOutfitFusion[15 + this.gender][1];
            }
        } else if (this.inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body; //part body
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (effectSkill != null && effectSkill.isBienHinh) {
            return (short) ConstPlayer.LEGBIENHINH[this.gender][effectSkill.levelBienHinh - 1];
        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1229 || item.template.id == 1230);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1229 || petItem.template.id == 1230);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1247 || item.template.id == 1230);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1247 || petItem.template.id == 1230);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                return 1410;
            }
            if (hasItem11 && hasItem21 && !sameItem1) {
                return 1416;
            }
        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1229 || item.template.id == 1230);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1229 || petItem.template.id == 1230);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1248 || item.template.id == 1230);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1248 || petItem.template.id == 1230);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                return 1410;
            }

            if (hasItem11 && hasItem21 && !sameItem1) {
                return 1416;
            }

        }
        if (this.isPl() && this.pet != null && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {
            Item item = inventory.itemsBody.get(5);
            Item petItem = pet.inventory.itemsBody.get(5);

            boolean hasItem1 = item.isNotNullItem() && (item.template.id == 1469 || item.template.id == 1471);
            boolean hasItem2 = petItem.isNotNullItem() && (petItem.template.id == 1469 || petItem.template.id == 1471);
            boolean sameItem = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            boolean hasItem11 = item.isNotNullItem() && (item.template.id == 1470 || item.template.id == 1468);
            boolean hasItem21 = petItem.isNotNullItem() && (petItem.template.id == 1470 || petItem.template.id == 1468);
            boolean sameItem1 = item.isNotNullItem() && petItem.isNotNullItem() && item.template.id == petItem.template.id;

            if (hasItem1 && hasItem2 && !sameItem) {
                if (this.gender == 0) {
                    return 1981;
                }
                if (this.gender == 1) {
                    return 1981;
                }
                if (this.gender == 2) {
                    return 1989;
                }
            }
            if (hasItem11 && hasItem21 && !sameItem1) {
                if (this.gender == 0) {
                    return 1981;
                }
                if (this.gender == 1) {
                    return 1981;
                }
                if (this.gender == 2) {
                    return 1989;
                }
            }
        }
        if (this.itemTime != null && this.itemTime.isBienHinhMa) {
//            if (Util.isTrue(50, 100)) {
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
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (effectSkill != null && effectSkill.isBang) {
            return 1253;
        } else if (effectSkill != null && effectSkill.isDa) {
            return 456;
        } else if (effectSkill != null && effectSkill.isCarot) {
            return 408;
        } else if (effectSkill != null && effectSkill.isCaiBinhChua) {
            return 1256;
//        } else if (effectSkill != null && effectSkill.isBienHinh) { 
//            return 194;
        } else if (effectSkill != null && effectSkill.isSoHai) {
            return 884;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) { 
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
                return idOutfitFusion[3 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
                return idOutfitFusion[6 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
                return idOutfitFusion[9 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
                return idOutfitFusion[12 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {
                return idOutfitFusion[15 + this.gender][2];
            }
        } else if (this.inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public short getFlagBag() {
        if (this.iDMark != null && this.iDMark.isHoldBlackBall()) {
            return 31;
        }
        if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 8 && this.inventory.itemsBody.get(8).isNotNullItem()) {
            return this.inventory.itemsBody.get(8).template.part;
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getLinhThu() {
        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 10 && this.inventory.itemsBody.get(10).isNotNullItem()) {
            return (short) (this.inventory.itemsBody.get(10).template.iconID - 1);
        }
        return 0;
    }

    public short getThuCung() {
        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 7 && this.inventory.itemsBody.get(7).isNotNullItem()) {
            return (short) (this.inventory.itemsBody.get(7).template.id);
        }
        return -1;
    }

    public short getEffectchar() {
        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 6 && this.inventory.itemsBody.get(6).isNotNullItem()) {
            return (short) (this.inventory.itemsBody.get(6).template.part);
        }
        return -1;
    }

    public short getEffectchar2() {
        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 9 && this.inventory.itemsBody.get(9).isNotNullItem()) {
            return (short) (this.inventory.itemsBody.get(9).template.part);
        }
        return -1;
    }

    public short getMount() {
        if (this.isPl() && this.inventory != null && this.inventory.itemsBody.size() > 9 && this.inventory.itemsBody.get(9).isNotNullItem()) {
            Item item = this.inventory.itemsBody.get(9);
            if (item.template.id == 2175) {
                return 18;
            } else if (item.template.type == 24) {
                if (item.template.gender == 3 || item.template.gender == this.gender) {
                    return item.template.id;
                } else {
                    return -1;
                }
            } else {
                if (item.template.id < 500) {
                    return item.template.id;
                } else {
                    Object mountNumObj = DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
                    if (mountNumObj == null) {
                        return -1;
                    } else {
                        short mountNum = ((Number) mountNumObj).shortValue();
                        return mountNum;
                    }
                }
            }
        }
        return -1;
    }

    public Mob mobTarget;

    public long lastTimeTargetMob;

    public long timeTargetMob;

    public long lastTimeAttack;

    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        if (isBot) {
            move = (byte) (move * (byte) 2);
        }
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }

    public void active() {
        if (this.isBot) {
            if (this.isDie()) {
                this.nPoint.hp = this.nPoint.hpMax;
            }
            if (this.nPoint.mp <= 0) {
                this.nPoint.mp = this.nPoint.mpMax;
            }
            this.attack();
        }
    }

    public int getRangeCanAttackWithSkillSelectBot() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else if (skillId == Skill.DRAGON || skillId == Skill.DEMON || skillId == Skill.GALICK) {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
        return 752002;
    }

    public Mob getMobAttack() {
        if (this.mobTarget != null && (this.mobTarget.isDie() || !this.zone.equals(this.mobTarget.zone))) {
            this.mobTarget = null;
        }
        if (this.mobTarget == null && Util.canDoWithTime(lastTimeTargetMob, timeTargetMob)) {
            this.mobTarget = this.zone.getRandomMobInMap();
            this.lastTimeTargetMob = System.currentTimeMillis();
            this.timeTargetMob = 500;
        }
        return this.mobTarget;
    }

    public void attack() {
        if (this.isBot) {
            //this.mobTarget = this.getMobAttack();
            if (Util.canDoWithTime(lastTimeAttack, 100) && this.mobTarget != null) {

                this.lastTimeAttack = System.currentTimeMillis();
                try {
                    Mob m = this.getMobAttack();
                    if (m == null || m.isDie()) {
                        return;
                    }

                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                    //System.out.println(m.name);
                    if (Util.nextInt(100) < 80) {
                        this.playerSkill.skillSelect = this.playerSkill.skills.get(0);
                    }
                    if (Util.getDistance(this, m) <= this.getRangeCanAttackWithSkillSelectBot()) {
                        if (Util.isTrue(5, 20)) {
                            if (SkillUtil.isUseSkillChuong(this)) {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y);
                            } else {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, null, m, null);
                    } else {
                        this.moveTo(m.location.x, m.location.y);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.mobTarget = getMobAttack();
            }
        }
    }

    //    public void PlmoveTo(Player pl, int x, int y) {
//        byte dir = (byte) (pl.location.x - x < 0 ? 1 : -1);
//        byte move = (byte) Util.nextInt(40, 60);
//        if (pl != null) {
//            move = (byte) (move * (byte) 2);
//        }
//        PlayerService.gI().playerMove(pl, pl.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
//    }
//
//    public void Plactive(Player player) {
//        if (player != null) {
//            if (player.isDie()) {
//                player.nPoint.hp = player.nPoint.hpMax;
//            }
//            if (player.nPoint.mp <= 0) {
//                player.nPoint.mp = player.nPoint.mpMax;
//            }
//            player.Plattack(player);
//        }
//    }
//
//    public int PLgetRangeCanAttackWithSkillSelect(Player pl) {
//        int skillId = pl.playerSkill.skillSelect.template.id;
//        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
//            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
//        } else if (skillId == Skill.DRAGON || skillId == Skill.DEMON || skillId == Skill.GALICK) {
//            return Skill.RANGE_ATTACK_CHIEU_DAM;
//        }
//        return 752002;
//    }
//
//    public Mob getMobAttack(Player player) {
//        if (player.mobTarget != null && (player.mobTarget.isDie() || !player.zone.equals(player.mobTarget.zone))) {
//            player.mobTarget = null;
//            System.err.println("Khong tìm tháy quái ");
//        }
//        if (player.mobTarget == null && Util.canDoWithTime(lastTimeTargetMob, timeTargetMob)) {
//            player.mobTarget = player.zone.getRandomMobInMap();
//            player.lastTimeTargetMob = System.currentTimeMillis();
//            player.timeTargetMob = 500;
//        }
//        System.err.println("Ten quaiii :" + player.mobTarget.name);
//        return player.mobTarget;
//    }
//
//    public void Plattack(Player player) {
//        if (/*this.isBot || */player != null) {
////            this.mobTarget = this.getMobAttack();
//            if (Util.canDoWithTime(lastTimeAttack, 100) && player.mobTarget != null) {
//
//                player.lastTimeAttack = System.currentTimeMillis();
//                try {
//                    Mob m = player.getMobAttack(player);
//                    if (m == null || m.isDie()) {
//                        return;
//                    }
//
//                    player.playerSkill.skillSelect = player.playerSkill.skills.get(Util.nextInt(0, player.playerSkill.skills.size() - 1));
//
//                    if (Util.nextInt(100) < 80) {
//                        player.playerSkill.skillSelect = player.playerSkill.skills.get(0);
//                    }
//                    if (Util.getDistance(player, m) <= player.PLgetRangeCanAttackWithSkillSelect(player)) {
//                        if (Util.isTrue(5, 20)) {
//                            if (SkillUtil.isUseSkillChuong(player)) {
//                                player.PlmoveTo(player, m.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
//                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y);
//                            } else {
//                                player.PlmoveTo(player, m.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
//                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y);
//                            }
//                        }
//                        SkillService.gI().useSkill(player, null, m, null);
//                    } else {
//                        player.PlmoveTo(player, m.location.x, m.location.y);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                player.mobTarget = getMobAttack(player);
//            }
//        }
//    }
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            boolean isSkillChuong = false;
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
            if (isMobAttack && this.itemTime.isBienHinhMa && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }

            if (plAtt != null) {
                if (isSkillChuong && plAtt.nPoint.multicationChuong > 0 && Util.canDoWithTime(plAtt.nPoint.lastTimeMultiChuong, PlayerSkill.TIME_MUTIL_CHUONG)) {
                    damage *= plAtt.nPoint.multicationChuong;
                    plAtt.nPoint.lastTimeMultiChuong = System.currentTimeMillis();
                }
            }

            this.nPoint.subHP(damage);
            if (isDie()) {
                if (this.zone != null && this.zone.map.mapId == 112) {
                    if (plAtt != null) {
                        plAtt.pointPvp++;

                    }
                }
                if (this.isPl() && this != null && plAtt != null && plAtt.gender == 0 && plAtt.playerSkill != null && plAtt.playerSkill.skillSelect != null && (plAtt.playerSkill.skillSelect.template.id == Skill.QUA_CAU_KENH_KHI) && this.playerTask != null && this.playerTask.taskMain != null && this.playerTask.taskMain.id >= 19 && effectSkill.isMonkey && khisukien == false && Util.canDoWithTime(lastTimeDropTail, TIME_TRUNG_THU) && cFlag == 8) {

                    try {
                        ItemMap duoikhi = new ItemMap(zone, 579, 1, location.x, location.y, plAtt.id);
                        Service.gI().dropItemMap(zone, duoikhi);

                        lastTimeDropTail = System.currentTimeMillis();
                        GirlkunDB.executeUpdate("update player set lastTimeDropTail = ? where name = ?", System.currentTimeMillis(), name);
                        Service.gI().sendThongBao(plAtt, "Rớt đuôi khỉ kìa");
                        Service.gI().sendThongBao(this, "Bạn đã bị hạ và rớt đuôi khỉ, trong vòng 1 giờ sẽ không rớt đuôi khỉ nữa");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                setDie(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    public static final short ARANGE_CAN_ATTACK = 300;
    public static final short ARANGE_ATT_SKILL1 = 100;
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

    public boolean goingHome;

    public Mob mobAttack;
    public Player playerAttack;
    public long lastTimeMoveIdle;
    public int timeMoveIdle;
    public boolean idle;

    public Mob plfindMobAttack() {
        if (zone == null) {
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

    public void plmoveIdle() {

        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - this.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, this.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), this.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    public long lastTimeMoveAtHome;
    public byte directAtHome = -1;

    private Skill getSkill(int indexSkill) {
        return this.playerSkill.skills.get(indexSkill - 1);
    }

    public void platt() {
        update();
        System.err.print("update pl pem");
        if (isDie()) {
            if (System.currentTimeMillis() - lastTimeDie > 50000) {
                if (this.nPoint != null) {
                    Service.gI().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                }
            } else {
                return;
            }
        }

        if (this != null && this.isDie() || this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        plmoveIdle();

        mobAttack = plfindMobAttack();
        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
        System.err.print("Pem quai");
        if (mobAttack != null) {

            int disToMob = Util.getDistance(this, mobAttack);
            if (disToMob <= ARANGE_ATT_SKILL1) {
                //đấm
                this.playerSkill.skillSelect = getSkill(1);
                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                    if (SkillService.gI().canUseSkillWithMana(this)) {
                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        System.err.print("Pem quai 1");

                    }
                }
            } else {
                //chưởng
                this.playerSkill.skillSelect = getSkill(2);

                if (this.playerSkill.skillSelect.skillId != -1) {
                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                        if (SkillService.gI().canUseSkillWithMana(this)) {
                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                            System.err.print("Pem quai 2");

                        }
                    }
                }
            }

        } else {
            idle = true;

        }

    }

    protected void setDie(Player plAtt) {
        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.gI().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.gI().charDie(this);
        //add kẻ thù
//        if (!this.isPet && !this.isMiniPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isMiniPet && !plAtt.isBoss) {
        if (!this.isPet && !this.isNewPet && !this.isMiniPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet && !plAtt.isMiniPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        //kết thúc pk
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }

        BlackBallWar.gI().dropBlackBall(this);
        Giaidauvutru.gI().dropGiaidau(this);
        if (isHoldNamecBallTranhDoat) {
            TranhNgocService.getInstance().dropBall(this, (byte) -1);
            TranhNgocService.getInstance().sendUpdateLift(this);
        }
    }
    //--------------------------------------------------------------------------

    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        if (this.session != null) {
            return this.session.isAdmin;
        } else {
            return false; // Or throw an exception, log an error, etc.
        }
    }

    public boolean isAdmin1() {
        return this.session.isAdmin1;
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public void preparedToDispose() {

    }

    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (minipet != null) {
            minipet.dispose();
            minipet = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }
        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        if (mapGiaidauBall != null) {
            mapGiaidauBall.clear();
            mapGiaidauBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }

        zone = null;
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }

        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (skillSpecial != null) {
            skillSpecial.dispose();
            skillSpecial = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
//        if (pointfusion != null) {
//            pointfusion.dispose();
//            pointfusion = null;
//        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combineNew != null) {
            combineNew.dispose();
            combineNew = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (gift != null) {
            gift.dispose();
            gift = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (rewardGiaidau != null) {
            rewardGiaidau.dispose();
            rewardGiaidau = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }

        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }

    public String percentGold(int type) {
        try {
            if (type == 0) {
                double percent = ((double) this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
                return String.valueOf(Math.ceil(percent));
            } else if (type == 1) {
                double percent = ((double) this.goldVIP / ChonAiDay.gI().goldVip) * 100;
                return String.valueOf(Math.ceil(percent));
            }
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return "0";
        }
        return "0";
    }

}
