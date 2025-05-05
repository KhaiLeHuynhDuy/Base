package nro.models.boss;

import nro.models.boss.list_boss.AnTrom;
import nro.models.boss.list_boss.AnTromTV;
import nro.models.boss.list_boss.BLACK.*;
import nro.models.boss.list_boss.BietDoiGranola.Granona;
//import nro.models.boss.list_boss.Broly.Brolythuong;
//import nro.models.boss.list_boss.Broly.Superbroly;
import nro.models.boss.list_boss.ConDuongRanDoc.Nappa;
import nro.models.boss.list_boss.ConDuongRanDoc.Saibamen;
import nro.models.boss.list_boss.ConDuongRanDoc.Saibamen2;
import nro.models.boss.list_boss.ConDuongRanDoc.Saibamen3;
import nro.models.boss.list_boss.ConDuongRanDoc.Saibamen4;
import nro.models.boss.list_boss.ConDuongRanDoc.Saibamen5;
import nro.models.boss.list_boss.ConDuongRanDoc.Saibamen6;
import nro.models.boss.list_boss.ConDuongRanDoc.Vegeta;
import nro.models.boss.list_boss.Cooler.Cooler;
import nro.models.boss.list_boss.Doraemon.Doraemon;
import nro.models.boss.list_boss.NgucTu.Cumber;
import nro.models.boss.list_boss.android.*;
import nro.models.boss.list_boss.cell.SieuBoHung;
import nro.models.boss.list_boss.cell.XenBoHung;
import nro.models.boss.list_boss.doanh_trai.*;
import nro.models.boss.list_boss.Doraemon.Nobita;
import nro.models.boss.list_boss.Doraemon.Xeko;
import nro.models.boss.list_boss.Doraemon.Xuka;
import nro.models.boss.list_boss.fide.Fide;
import nro.models.boss.list_boss.Doraemon.Chaien;
import nro.models.boss.list_boss.GoHanZoomBi;
//import nro.models.boss.list_boss.MaTroi;
import nro.models.boss.list_boss.Mabu12h.MabuBoss;
import nro.models.boss.list_boss.Mabu12h.BuiBui;
import nro.models.boss.list_boss.Mabu12h.BuiBui2;
import nro.models.boss.list_boss.Mabu12h.Drabura;
import nro.models.boss.list_boss.Mabu12h.Drabura2;
import nro.models.boss.list_boss.Mabu12h.Yacon;
import nro.models.boss.list_boss.SuKienTrungThu.NguyetThan;
import nro.models.boss.list_boss.SuKienTrungThu.NhatThan;
import nro.models.boss.list_boss.TDST.So_1;
import nro.models.boss.list_boss.TDST.So_2;
import nro.models.boss.list_boss.TDST.So_3;
import nro.models.boss.list_boss.TDST.So_4;
import nro.models.boss.list_boss.TDST.TieuDoiTruong;
import nro.models.boss.list_boss.fide.Chill;
import nro.models.boss.list_boss.gas.DrLyChee;
import nro.models.boss.list_boss.gokuvocuc.Gokuvc;
import nro.models.boss.list_boss.halloween.Doi;
import nro.models.boss.list_boss.halloween.MaTroi;
import nro.models.boss.list_boss.halloween.Xuong;
import nro.models.boss.list_boss.nappa.*;
import nro.models.boss.list_boss.phoban.TrungUyXanhLoBdkb;
import nro.models.map.MapMaBu.MapMaBu2h;
import nro.models.map.MapMaBu.MapXHMaBu2h;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.services.ItemMapService;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Logger;
import nro.utils.Util;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import nro.models.boss.list_boss.Broly.Broly;
import nro.models.boss.list_boss.Broly.BrolySuper;
import nro.models.boss.list_boss.HuyetMa.HuyetMa;
import nro.models.boss.list_boss.SpecBoss.Ayaka;
import nro.models.boss.list_boss.SpecBoss.Heart;
import nro.models.boss.list_boss.SpecBoss.Hutao;
import nro.models.boss.list_boss.SpecBoss.Jemniba;
import nro.models.boss.list_boss.SpecBoss.Toppo;
import nro.models.boss.list_boss.SpecBoss.Xiao;
import nro.models.boss.list_boss.fide.FideVang;
import nro.server.ServerNotify;

public class BossManager {

    private static BossManager I;
    public static final byte RATIO_REWARD = 25;
    public static Player pl;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }
    public int idBase = 1;

    public int idGroup = 1;
    protected ConcurrentHashMap<Integer, Long> setTimeSpawnBoss;

    private BossManager() {
        this.bosses = new CopyOnWriteArrayList<>();
        this.setTimeSpawnBoss = new ConcurrentHashMap<>();
    }
    private boolean loadedBoss;

    protected CopyOnWriteArrayList<Boss> bosses;

    public void addBoss(Boss boss) {
        bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        bosses.remove(boss);
    }

    public void loadBoss() {
        active(500);
        try {
            if (this.loadedBoss) {
                return;
            }
////        // Tạo boss một cách bất đồng bộ (ví dụ sử dụng ExecutorService)
//        ExecutorService executor = Executors.newFixedThreadPool(10); // Số lượng threads có thể được điều chỉnh

            int[] bossTypes = {
                BossType.TIEU_DOI_TRUONG, BossType.KING_KONG, BossType.CUMBER, BossType.XEN_BO_HUNG, BossType.MA_TROI,
                BossType.SIEU_BO_HUNG, BossType.BLACK, BossType.PIC, BossType.BU_MAP, BossType.KUKU, BossType.MAP_DAU_DINH,
                BossType.RAMBO, BossType.FIDE, BossType.DR_KORE, BossType.ANDROID_14,// BossType.SUPER_ANDROID_17,
                            BossType.DORAEMON, BossType.NOBITA, BossType.CHAIEN, BossType.XUKA, BossType.XEKO,
                BossType.GOKU_VOCUC, BossType.ZAMASZIN, BossType.SUPER_BLACK_GOKU_2,
                BossType.CHILL,
                BossType.DOI,
                BossType.MATROI,
                BossType.XUONG,
                BossType.NHAT_THAN, BossType.FIDE2, BossType.HEART, BossType.HUTAO, BossType.XIAO, BossType.AKAYA, BossType.JEM_NI_BA, BossType.TOP_PO,
                BossType.NGUYET_THAN, BossType.GRANONA, BossType.GOHAN, BossType.COOLER, BossType.BROLY, BossType.BROLY_SUPER, BossType.AN_TROM, BossType.AN_TROM_TV
            };
            for (int i = 0; i < bossTypes.length; i++) {
                switch (bossTypes[i]) {
                    case BossType.KUKU:
                    case BossType.MAP_DAU_DINH:
                    case BossType.RAMBO:
                        for (int j = 0; j < 1; j++) {
                            createBoss(bossTypes[i]);
                            Thread.sleep(1000);
                        }
                        break;
                    case BossType.DOI:
                    case BossType.AN_TROM:
                    case BossType.MATROI:
                    case BossType.XUONG:
                    case BossType.NHAT_THAN:
                    case BossType.NGUYET_THAN:
                    case BossType.AN_TROM_TV:

                        for (int k = 0; k < 4; k++) {
                            createBoss(bossTypes[i]);
                            Thread.sleep(1000);
                        }
                        break;
                    case BossType.BROLY_THUONG:
                        for (int q = 0; q < 2; q++) {
                            createBoss(BossType.BROLY_THUONG);
                            Thread.sleep(1000);
                        }
                        break;
                    case BossType.BROLY_SUPER:
                        for (int q = 0; q < 2; q++) {
                            createBoss(BossType.BROLY_SUPER);
                            Thread.sleep(1000);
                        }
                        break;
                    default:
                        createBoss(bossTypes[i]);
                        Thread.sleep(1000);
                        break;
                }
            }
            this.loadedBoss = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
////************************TEST**********************************
//        for (int bossType : bossTypes) {
//            switch (bossType) {
//                case BossType.KUKU:
//                case BossType.MAP_DAU_DINH:
//                case BossType.RAMBO:
//                    for (int q = 0; q < 2; q++) {
//                        int finalBossType = bossType;
//                        executor.submit(() -> createBoss(finalBossType));
//
//                    }
//                    break;
//                case BossType.DOI:
//                case BossType.AN_TROM:
//                    for (int q = 0; q < 2; q++) {
//                        int finalBossType = bossType;
//                        executor.submit(() -> createBoss(finalBossType));
//
//                    }
//                    break;
//                case BossType.MATROI:
//                case BossType.XUONG:
//                case BossType.NHAT_THAN:
//                case BossType.NGUYET_THAN:
//                case BossType.AN_TROM_TV:
//                    for (int q = 0; q < 2; q++) {
//                        int finalBossType = bossType;
//                        executor.submit(() -> createBoss(finalBossType));
//
//                    }
//                    break;
//                case BossType.BROLY:
//                case BossType.BROLY_SUPER:
//                    for (int q = 0; q < 3; q++) {
//                        int finalBossType = bossType;
//                        executor.submit(() -> createBoss(finalBossType));
//
//                    }
//                    break;
//                default:
//                    executor.submit(() -> createBoss(bossType));
//                    break;
//            }
//        }
//
//        executor.shutdown();
//        try {
//            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            Logger.logException(BossManager.class, e);
//        }
//        this.loadedBoss = true;
//    }
//    //************************TEST**********************************

    public Boss createBossDoanhTrai(int bossID, long dame, long hp, Zone zone) {
        try {
            switch (bossID) {
                case BossType.TRUNG_UY_TRANG:
                    return new TrungUyTrang(dame, hp, zone);
                case BossType.TRUNG_UY_XANH_LO:
                    return new TrungUyXanhLo(dame, hp, zone);
                case BossType.TRUNG_UY_THEP:
                    return new TrungUyThep(dame, hp, zone);
                case BossType.NINJA_AO_TIM:
                    return new NinjaAoTim(dame, hp, zone);
                case BossType.ROBOT_VE_SI1:
                case BossType.ROBOT_VE_SI2:
                case BossType.ROBOT_VE_SI3:
                case BossType.ROBOT_VE_SI4:
                    return new RobotVeSi(bossID, dame, hp, zone);
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(BossManager.class, e);
            return null;
        }
    }

    public Boss createBossBdkb(int bossID, long dame, long hp, Zone zone) {
        try {
            switch (bossID) {
                case BossType.TRUNG_UY_XANH_LO_BDKB:
                    return new TrungUyXanhLoBdkb(dame, hp, zone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(BossManager.class, e);
        }
        return null;
    }

    public Boss createBossGas(int bossID, long dame, long hp, Zone zone) {
        try {
            switch (bossID) {
                case BossType.DR_LYCHEE:
                    return new DrLyChee(dame, hp, zone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(BossManager.class, e);
        }
        return null;
    }

    public Boss createBossCDRD(int bossID, int level, long dame, long hp, Zone zone) {
        try {
            switch (bossID) {
                case BossType.NAPPA:
                    return new Nappa(zone, level, dame, hp);
                case BossType.VEGETA:
                    return new Vegeta(zone, level, dame, hp);
                case BossType.SAIBAMEN_1:
                    return new Saibamen(zone, level, dame, hp);
                case BossType.SAIBAMEN_2:
                    return new Saibamen2(zone, level, dame, hp);
                case BossType.SAIBAMEN_3:
                    return new Saibamen3(zone, level, dame, hp);
                case BossType.SAIBAMEN_4:
                    return new Saibamen4(zone, level, dame, hp);
                case BossType.SAIBAMEN_5:
                    return new Saibamen5(zone, level, dame, hp);
                case BossType.SAIBAMEN_6:
                    return new Saibamen6(zone, level, dame, hp);
//                case BossType.SAIBAMEN_7:
//                    return new Saibamen7(zone, level, dame, hp);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(BossManager.class, e);
        }
        return null;
    }

//    public Boss createBoss(int bossType, BossData bossdata) {
//        try {
//            switch (bossType) {
//                case BossType.SUPER_BROLY:
//                    return new Superbroly(bossType, bossdata);
//                case BossType.BROLY_THUONG:
//                    return new Brolythuong(bossType, bossdata);
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
    public Boss createBoss(int bossID) {
        try {
//            int[] idmapbroly = new int[]{5, 6, 10, 11, 12, 13, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
//            int indexmapxh = Util.nextInt(idmapbroly.length);
//            if (bossID <= BossType.BROLY_THUONG + 20 && bossID >= BossType.BROLY_THUONG) {
//                BossData brolythuong = new BossData(
//                        "Broly",
//                        ConstPlayer.XAYDA,
//                        new short[]{291, 292, 293, -1, -1, -1},
//                        100,
//                        new long[]{500},
//                        new int[]{idmapbroly[indexmapxh]},
//                        new int[][]{
//                            {Skill.KAMEJOKO, 7, 5000},
//                            {Skill.ANTOMIC, 7, 5000},
//                            {Skill.MASENKO, 7, 5000},
//                            {Skill.DEMON, 7, 1000},
//                            {Skill.GALICK, 7, 1000},
//                            {Skill.DRAGON, 7, 1000},
//                            {Skill.TAI_TAO_NANG_LUONG, 7, 20000}},
//                        new String[]{"|-2|Làm sao mà đỡ được!"}, //text chat 1
//                        new String[]{"|-1|Thấy ảo chưa nè!"}, //text chat 2
//                        new String[]{"|-1|Chết mọe m chưa con!",
//                            "|-1|Tobe continue.."}, //text chat 3
//                        TypeAppear.DEFAULT_APPEAR
//                );
//                return new Brolythuong(bossID, brolythuong);
//            }
            switch (bossID) {
                case BossType.GRANONA:
                    return new Granona();
//                case BossType.AN_TROM:
//                    return new AnTrom();
//                case BossType.AN_TROM_TV:
//                    return new AnTromTV();
                case BossType.SO_4:
                    return new So_4();
                case BossType.SO_3:
                    return new So_3();
                case BossType.SO_2:
                    return new So_2();
                case BossType.SO_1:
                    return new So_1();
                case BossType.TIEU_DOI_TRUONG:
                    return new TieuDoiTruong();
                case BossType.KUKU:
                    return new Kuku();
                case BossType.MAP_DAU_DINH:
                    return new MapDauDinh();
                case BossType.RAMBO:
                    return new Rambo();
                case BossType.DRABURA:
                    return new Drabura();
                case BossType.DRABURA_2:
                    return new Drabura2();
                case BossType.BUI_BUI:
                    return new BuiBui();
                case BossType.BUI_BUI_2:
                    return new BuiBui2();
                case BossType.YA_CON:
                    return new Yacon();
                case BossType.MABU_12H:
                    return new MabuBoss();
                case BossType.FIDE:
                    return new Fide();
                case BossType.FIDE2:
                    return new FideVang();
                case BossType.DR_KORE:
                    return new DrKore();
                case BossType.ANDROID_19:
                    return new Android19();
                case BossType.ANDROID_13:
                    return new Android13();
                case BossType.ANDROID_14:
                    return new Android14();
                case BossType.ANDROID_15:
                    return new Android15();
                case BossType.SUPER_ANDROID_17:
                    return new SuperAndroid17();
                case BossType.PIC:
                    return new Pic();
                case BossType.POC:
                    return new Poc();
                case BossType.KING_KONG:
                    return new KingKong();
                case BossType.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossType.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossType.XUKA:
                    return new Xuka();
                case BossType.NOBITA:
                    return new Nobita();
                case BossType.XEKO:
                    return new Xeko();
                case BossType.CHAIEN:
                    return new Chaien();
                case BossType.DORAEMON:
                    return new Doraemon();
                case BossType.COOLER:
                    return new Cooler();
                case BossType.BLACK:
                    return new Black();
                case BossType.CUMBER:
                    return new Cumber();
//                case BossType.DOI:
//                    return new Doi();
//                case BossType.MATROI:
//                    return new MaTroi();
//                case BossType.XUONG:
//                    return new Xuong();
//                case BossType.NHAT_THAN:
//                    return new NhatThan();
//                case BossType.NGUYET_THAN:
//                    return new NguyetThan();
                case BossType.HuyetMa:
                    return new HuyetMa();

                case BossType.CHILL:
                    return new Chill();
                case BossType.GOHAN:
                    return new GoHanZoomBi();
                case BossType.GOKU_VOCUC:
                    return new Gokuvc();
                case BossType.ZAMASZIN:
                    return new ZamasKaio();
                case BossType.SUPER_BLACK_GOKU_2:
                    return new SuperBlack2();
                case BossType.BROLY:
                    return new Broly();
                case BossType.BROLY_SUPER:
                    return new BrolySuper();

                case BossType.AKAYA:
                    return new Ayaka();
                case BossType.HUTAO:
                    return new Hutao();
                case BossType.XIAO:
                    return new Xiao();
                case BossType.HEART:
                    return new Heart();
//                case BossType.TOP_PO:
//                    return new Toppo();
                case BossType.JEM_NI_BA:
                    return new Jemniba();
                default:
                    return null;
            }
        } catch (Exception e) {
            // Ghi nhận ngoại lệ để phân tích và sửa chữa lỗi
            e.printStackTrace();
            Logger.logException(BossManager.class, e);
            // Có thể thực hiện thêm các bước xử lý lỗi cụ thể tại đây
            return null; // Trả về null hoặc có thể trả về một đối tượng boss "rỗng" nếu cần
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return !player.zone.getBosses().isEmpty();
    }

    public void showListBoss(Player player) {
        if (!player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Danh Sách Boss");
            msg.writer().writeByte((int) bosses.stream().filter(boss
                    -> !MapService.gI().isMapMaBu(boss.getData()[0].getMapJoin()[0])
                    && !MapService.gI().isMapMaBu2h(boss.getData()[0].getMapJoin()[0])
                    && !MapService.gI().isMapBlackBallWar(boss.getData()[0].getMapJoin()[0])
                    && !MapService.gI().isMapGiaidauvutru(boss.getData()[0].getMapJoin()[0])).count());
            int stt = 0;
            for (Boss boss : bosses) {
                if (boss == null) {
                    continue;
                }
                if (MapService.gI().isMapMaBu(boss.getData()[0].getMapJoin()[0])
                        || MapService.gI().isMapMaBu2h(boss.getData()[0].getMapJoin()[0])
                        || MapService.gI().isMapBlackBallWar(boss.getData()[0].getMapJoin()[0])
                        || MapService.gI().isMapGiaidauvutru(boss.getData()[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt(stt);
                msg.writer().writeInt(0);
                msg.writer().writeShort(boss.getData()[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.getData()[0].getOutfit()[1]);
                msg.writer().writeShort(boss.getData()[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.getData()[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF("Đã chết");
                    msg.writer().writeUTF("Chưa có thông tin !");
                }
                stt++;
                player.sendMessage(msg);
                msg.cleanup();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FindBoss(Player player, int id) {
        Boss boss = BossManager.gI().getBossByType(id);
        if (boss != null && !boss.isDie()) {
            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
            if (z.getNumOfPlayers() < z.maxPlayer) {
                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                Service.gI().sendMoney(player);
            } else {
                Service.gI().sendThongBao(player, "Khu vực đang full.");
            }
        } else {
            Service.gI().sendThongBao(player, "Chê cụ òi");
        }
    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player)
                    || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                    || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            switch (mapId) {
            }
            if (k != null) {
                k.setCurrentLevel(0);
                k.joinMapByZone(player);
            }
        } catch (Exception e) {
        }
    }

    public Boss getBossByType(int type) {
        return (Boss) BossManager.gI().bosses.stream().filter(boss -> boss.getTypeBoss() == type).findFirst().orElse(null);
    }

    public void bossNotify(Boss boss) {
        if (boss.getSecondsNotify() == 0) {
            return;
        }
        if (Util.canDoWithTime(boss.getLastTimeNotify(), boss.getSecondsNotify())) {
            boss.setLastTimeNotify(System.currentTimeMillis());
        }
        long att = pl.nPoint.dame;
        if (pl != null && att > 100000000) {
            ServerNotify.gI().notify("" + pl.name + " vừa gây " + att + " sát thương lên " + boss.name + ".Mọi người đều ngưỡng mộ!!");
        }
    }

    public Timer timer;
    public TimerTask task;
    protected boolean actived = false;

    public void close() {
        try {
            actived = false;
            task.cancel();
            timer.cancel();
            task = null;
            timer = null;
        } catch (Exception e) {
            e.printStackTrace();
            task = null;
            timer = null;
        }
    }

    public void active(int delay) {
        if (!actived) {
            actived = true;
            this.timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    BossManager.this.updateBossTask();
                    BossManager.this.loopCreateBoss();
                }
            };
            this.timer.schedule(task, delay, delay);
        }
    }

    private void loopCreateBoss() {
        for (Map.Entry<Integer, Long> entry : setTimeSpawnBoss.entrySet()) {
            if (System.currentTimeMillis() >= entry.getValue()) {
                System.out.println("Value: " + entry.getKey());
                createBoss(entry.getKey());
                setTimeSpawnBoss.remove(entry.getKey());
            }
        }
    }

    private void updateBossTask() {
        long st = System.currentTimeMillis();
        for (int i = 0; i < bosses.size(); i++) {
            Boss boss = bosses.get(i);
            if (boss != null && boss.bossInstance != null) {
                boss.updateBoss();
            }
        }
        if (st >= MapMaBu2h.TIME_OPEN_MABU_2H && st <= MapMaBu2h.TIME_CLOSE_MABU_2H && !MapMaBu2h.gI().isSpawnMabu) {
            MapXHMaBu2h.gI().initBoss(127);
        }

    }
}
