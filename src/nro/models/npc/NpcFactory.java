package nro.models.npc;

import nro.map.RanDoc.ConDuongRanDoc;

import com.girlkun.database.GirlkunDB;

import nro.consts.*;
//import nro.database.GirlkunDB;
import nro.models.matches.TOP;
import nro.services.*;
import nro.jdbc.daos.GodGK;
import nro.jdbc.daos.PlayerDAO;
import nro.kygui.ShopKyGuiService;
import nro.map.daihoi.DaiHoiManager;
import nro.models.map.challenge.MartialCongressService;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossType;
import nro.models.boss.BossManager;
import nro.models.boss.list_boss.NhanBan;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;

import java.sql.Connection;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

import nro.services.func.ChangeMapService;
import nro.services.func.SummonDragon;

import static nro.services.func.SummonDragon.SHENRON_1_STAR_WISHES_1;
import static nro.services.func.SummonDragon.SHENRON_1_STAR_WISHES_2;
import static nro.services.func.SummonDragon.SHENRON_SAY;

import nro.models.player.Player;
import nro.models.item.Item;
import nro.models.map.BDKB.BanDoKhoBau;
import nro.models.map.BDKB.BanDoKhoBauService;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.map.blackball.BlackBallWar;
import nro.models.map.MapMaBu.MapMaBu;
import nro.models.map.doanhtrai.DoanhTrai;
import nro.models.map.doanhtrai.DoanhTraiService;
import nro.models.map.sieuhang.TaskTraoQua;
import nro.models.player.Inventory;
import nro.models.player.NPoint;
import nro.models.matches.PVPService;
import nro.models.mob.Mob;
import nro.models.pariry.PariryServices;
import nro.models.player.Archivement;
import nro.models.player.Archivement_diem;
//import nro.models.player.Archivement_diem;
import nro.models.shop.ShopServiceNew;
import nro.models.skill.Skill;
import nro.server.Client;
import nro.server.Maintenance;
import nro.server.Manager;
import nro.services.func.CombineServiceNew;
import nro.services.func.Input;
import nro.services.func.LuckyRound;
import nro.services.func.TopService;
import nro.utils.Logger;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.ArrayList;

import nro.services.func.GoiRongXuong;
//import nro.services.func.SummonDragonHallowen;
//import nro.sukienbanhchung.NauBanhServices;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import nro.map.RanDoc.ConDuongRanDocService;
import nro.models.item.Item.ItemOption;
import nro.models.map.giaidauvutru.Giaidauvutru;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.phuban.DragonNamecWar.TranhNgocService;
import nro.models.player.Archivement_BoMong;
import nro.models.tasktraoqua.TaskTraoQuaNHS;
import nro.models.tasktraoqua.TaskTraoQuaNap;
import nro.models.tasktraoqua.TaskTraoQuaSM;
import nro.models.tasktraoqua.TaskTraoQuaTV;
import nro.services.func.Game;
import nro.services.func.MiniGame;
import java.util.Timer;
import java.util.TimerTask;

public class NpcFactory {

    private static final Random RANDOM = new Random();
    private static final int COST_HD = 50000000;
    public static String LOAI_THE;
    public static String MENH_GIA;
    private static final boolean nhanVang = false;
    private static final boolean nhanDeTu = false;
    private static final int QUESTION_TIMEOUT = 30 * 1000; // 30 giây
    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override

            public void openBaseMenu(Player player) {

                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Trứng Linh thú cần:\b|7|" + cn.slItemSub + " Hồn Linh Thú + 50K ngọc hồng", "Đổi Trứng\nLinh thú", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                Item honLinhThu = null;

                                try {
                                    honLinhThu = InventoryServiceNew.gI().findItemBag(player, cn.itemSub);
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                        throw new RuntimeException(e);
                                }
                                if (honLinhThu == null || honLinhThu.template == null) {
                                    this.npcChat(player, "Bạn không có " + ItemService.gI().getTemplate(cn.itemSub).name);
                                } else if (honLinhThu.quantity < cn.slItemSub) {
                                    this.npcChat(player, "Bạn không đủ " + honLinhThu.template.name);
                                } else if (player.inventory.ruby < cn.giaTrung) {
                                    this.npcChat(player, "Bạn không đủ " + cn.giaTrung + " hồng ngọc");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    player.inventory.ruby -= cn.giaTrung;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, cn.slItemSub);
                                    Service.gI().sendMoney(player);
                                    Item trungLinhThu = ItemService.gI().createNewItem(cn.itemCreat);
                                    InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "Bạn nhận được " + trungLinhThu.template.name);
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc ThienMa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        createOtherMenu(player, 1,
                                "Xin chào, ta có thể giúp cậu chế tạo trang bị Thiên Ma.",
                                "Chế Tạo", "Đi úp");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        switch (player.iDMark.getIndexMenu()) {
                            case 1: // Menu chính
                                if (select == 0) { // Chế tạo
                                    createOtherMenu(player, 2,
                                            "Hãy chọn loại trang bị muốn chế tạo:",
                                            "Áo", "Quần", "Găng", "Giày", "Trang Sức", "Cải trang");
                                } else if (select == 1) {
                                    createOtherMenu(player, 3,
                                            "Chọn map đêêêêê",
                                            "Tầng 1", "Tầng 2", "Tầng 3");
                                }
                                break;

                            case 2: // Chọn loại trang bị Thiên Ma
                                switch (select) {
                                    case 0:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_AO_THIEN_MA);
                                        break;
                                    case 1:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_QUAN_THIEN_MA);
                                        break;
                                    case 2:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_GANG_THIEN_MA);
                                        break;
                                    case 3:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_GIAY_THIEN_MA);
                                        break;
                                    case 4:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_SUC_THIEN_MA);
                                        break;
                                    case 5:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_CAI_TRANG_THIEN_MA);
                                        break;
                                }
                                break;
                            case 3:
                                switch (select) {
                                    case 0:
                                        ChangeMapService.gI().changeMapInYard(player, 204, -1, 552);
                                        break;
                                    case 1:
                                        ChangeMapService.gI().changeMapInYard(player, 205, -1, 552);
                                        break;
                                    case 2:
                                        ChangeMapService.gI().changeMapInYard(player, 206, -1, 552);
                                        break;
                                }

                                break;
                            case ConstNpc.MENU_START_COMBINE:
                                if (player.combineNew.typeCombine != -1) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc LoHongKhongGian(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 221) {
                        createOtherMenu(player, 1,
                                "Di Chuyển Sang Vị Diện Khác",
                                "Đấu Phá\nThương Khung", "Hồng Hoang");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 221) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapInYard(player, 157, -1, 552);
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_28_0) {
                                        Service.gI().sendThongBao(player, "Cần hoàn thành nhiệm vụ Siêu Bọ Hung");
                                        return;
                                    }
                                    ChangeMapService.gI().changeMapInYard(player, 202, -1, 552);
                                    break;

                            }
                        }
                    } else {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapInYard(player, 5, -1, 552);
                                break;
                            case 1:

                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc TU_TAI_TIEN(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        createOtherMenu(player, 1,
                                "Xin chào, ta có thể giúp cậu chế tạo trang bị mạnh mẽ hoặc luyện đan.",
                                "Chế Tạo", "Luyện Đan", "Đột phá", "Luyện Chế Trúc Cơ Đan Dược");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        switch (player.iDMark.getIndexMenu()) {
                            case 1: // Menu chính
                                if (select == 0) { // Chế tạo
                                    createOtherMenu(player, 2,
                                            "Cậu muốn chế tạo loại trang bị nào?",
                                            "Chế Tạo Vô Cực");
                                } else if (select == 1) { // Luyện Đan
                                    createOtherMenu(player, 5,
                                            "Cậu muốn luyện loại đan dược nào?",
                                            "Đan Luyện Khí", "Trúc Cơ Đan");
                                } else if (select == 2) { // dot pha
                                    createOtherMenu(player, 6,
                                            "Đột phá dành cho tu sĩ đã đạt giới hạn của Trúc Cơ Cảnh",
                                            "Pháp Tu", "Thể Tu");
                                } else if (select == 3) {
                                    createOtherMenu(player, 7,
                                            "Cậu muốn luyện loại đan dược nào?",
                                            "Trúc Cơ Sơ Kỳ", "Trúc Cơ Trung Kỳ", "Trúc Cơ Hậu Kỳ");
                                }
                                break;

                            case 2: // Chọn loại trang bị
                                if (select == 0) {
                                    createOtherMenu(player, 3,
                                            "Chọn vật phẩm muốn chế tạo:",
                                            "Áo Vô Cực", "Quần Vô Cực", "Găng Vô Cực", "Giày Vô Cực", "Nhẫn Vô Cực");
                                }
//                                else if (select == 1) {
//                                    createOtherMenu(player, 4,
//                                            "Chọn vật phẩm muốn chế tạo:",
//                                            "Áo La Thiên", "Quần La Thiên", "Găng La Thiên", "Giày La Thiên", "Nhẫn La Thiên");
//                                }
                                break;

                            case 3: // Chế tạo Vô Cực
                                switch (select) {
                                    case 0:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_AO_VO_CUC_TU_TAI);
                                        break;
                                    case 1:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_QUAN_VO_CUC_TU_TAI);
                                        break;
                                    case 2:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_GANG_VO_CUC_TU_TAI);
                                        break;
                                    case 3:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_GIAY_VO_CUC_TU_TAI);
                                        break;
                                    case 4:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_SUC_VO_CUC_TU_TAI);
                                        break;
                                }
                                break;

//                            case 4: // Chế tạo La Thiên
//                                switch (select) {
//                                    case 0:
//                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_AO_LA_THIEN_TU_TAI);
//                                        break;
//                                    case 1:
//                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_QUAN_LA_THIEN_TU_TAI);
//                                        break;
//                                    case 2:
//                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_GANG_LA_THIEN_TU_TAI);
//                                        break;
//                                    case 3:
//                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_GIAY_LA_THIEN_TU_TAI);
//                                        break;
//                                    case 4:
//                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_SUC_LA_THIEN_TU_TAI);
//                                        break;
//                                }
//                                break;
                            case 5: // Luyện đan
                                switch (select) {
                                    case 0:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_DAN_DUOC_LUYEN_KHI);
                                        break;
                                    case 1:
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRUC_CO_DAN);
                                        break;
                                }
                                break;
                            case 6: // Đột phá
                                DotPhaService.gI().thucHienDotPha(player, select);
                                break;
                            case 7: // luyện chế trúc cơ đan dược
                                switch (select) {
                                    case 0: // trúc cơ sơ kỳ
                                        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRUC_CO_SO);
                                        break;
                                    case 1: // trúc cơ trung kỳ
                                         CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRUC_CO_TRUNG);
                                        break;
                                    case 2:// trúc cơ hậu kỳ
                                         CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRUC_CO_HAU);
                                        break;
                                }
                                break;
                            case ConstNpc.MENU_START_COMBINE: // Xử lý kết hợp
                                if (player.combineNew.typeCombine != -1) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

//    private static Npc gapthu2(int mapId, int status, int cx, int cy, int tempId, int avatar) {
//        return new Npc(mapId, status, cx, cy, tempId, avatar) {
//            // Phương thức openBaseMenu không thay đổi
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//
//                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
//                        this.createOtherMenu(player, ConstNpc.GAPTHUMENU,
//                                "|4|- •⊹٭NGỌC RỒNG DONUTS٭⊹• -\nMÁY GẮP THÚ"
//                                + "\n|3|GẮP THƯỜNG : CHỈ SỐ THẤP"
//                                + "\n|3|GẮP CAO CẤP : CHỈ SỐ TRUNG"
//                                + "\n|3|GẮP VIP : CHỈ SỐ CAO"
//                                + "\n|7|GẮP X1 : GẮP THỦ CÔNG"
//                                + "\n|7|GẮP X10 : AUTO LẦN GẮP"
//                                + "\n|7|GẮP X100 : AUTO LẦN GẮP"
//                                + "\n|6|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH\nNẾU MUỐN NGƯNG AUTO GẤP CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!",
//                                "Gắp Thường", "Gắp Cao Cấp", "Gắp VIP", "Xem Top", "Rương Đồ");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
//                        switch (player.iDMark.getIndexMenu()) {
//                            case ConstNpc.GAPTHUMENU:
//                                switch (select) {
////                            handleMainOptions(player, select);
//                                    case 0:
//                                        this.createOtherMenu(player, ConstNpc.MENUGAPX1, "|6|Gắp Pet Thường" + "\n|7|Play!!!!",
//                                                "Gắp x1", "Gắp x10", "Gắp x100", "Rương Đồ");
//                                        break;
//                                    case 1:
//                                        this.createOtherMenu(player, ConstNpc.MENUGAPX10, "|6|Gắp Pet Cao Cấp" + "\n|7|Play!!!!",
//                                                "Gắp x1", "Gắp x10", "Gắp x100", "Rương Đồ");
//                                        break;
//                                    case 2:
//                                        this.createOtherMenu(player, ConstNpc.MENUGAPX100, "|6|Gắp Pet VIP" + "\n|7|Play!!!!",
//                                                "Gắp x1", "Gắp x10", "Gắp x100", "Rương Đồ");
//                                        break;
//                                    case 3:
////                                    Service.gI().sendThongBaoFromAdmin(player, "Số lần đã gắp của bạn : " + player.gtPoint);
//                                        break;
//                                    case 4:
//                                        this.createOtherMenu(player, ConstNpc.RUONG_PHU,
//                                                "|1|Tình yêu như một dây đàn\n"
//                                                + "Tình vừa được thì đàn đứt dây\n"
//                                                + "Đứt dây này anh thay dây khác\n"
//                                                + "Mất em rồi anh biết thay ai?",
//                                                "Rương Phụ\n(" + (player.inventory.itemsRuongPhu.size()
//                                                - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsRuongPhu))
//                                                + " món)",
//                                                "Xóa Hết\nRương Phụ", "Đóng");
//                                        break;
//                                }
//                                break;
//
//                            case ConstNpc.MENUGAPX1:
//                            case ConstNpc.MENUGAPX10:
//                            case ConstNpc.MENUGAPX100:
//                                int[] rubyCost = {50, 100, 200};
//                                int[][] choise1 = {{1601, 1602, 457, 457, 457}, {1606, 1607, 1608, 1609, 1610}, {1603, 226, 224, 225, 457}, {220, 221, 222, 223, 224}, {223, 226, 224, 225, 457}, {220, 221, 222, 223, 224}};
//                                int[][] choise2 = {{1601, 1602, 457, 457, 1594}, {1606, 1607, 1608, 1609, 1610}, {1603, 226, 224, 225, 457}, {220, 221, 222, 223, 224}, {550, 551, 552, 1998, 1999}, {220, 221, 222, 223, 224}};
//                                int[][] choise3 = {{1601, 1602, 457, 457, 1595}, {1606, 1607, 1608, 1609, 1610}, {1603, 226, 224, 225, 457}, {220, 221, 222, 223, 224}, {550, 551, 552, 1998, 1999}, {1259, 1270, 1271, 1290, 1291}};
//                                int random1 = new Random().nextInt(choise1.length);
//                                int random2 = new Random().nextInt(choise2.length);
//                                int random3 = new Random().nextInt(choise3.length);
//                                int sel = new Random().nextInt(5);
//                                switch (select) {
//                                    case 0: {
//                                        if (player.inventory.ruby < rubyCost[0]) {
//                                            Service.gI().sendThongBao(player, "Thiếu " + (rubyCost[0] - player.inventory.ruby) + " ngọc hồng");
//                                            return;
//                                        }
//                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                            Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
//                                            return;
//                                        }
//                                        Item gapt = Util.petrandom(choise1[random1][sel]);
////                                        gapt = Util.petrandom(choise1[random1][sel]);
//                                        int[] item = {1908, 1909, 1910, 457, 1596, 1597, 1598, 1599};
//                                        Item newItem1 = ItemService.gI().createNewItem((short) item[Util.nextInt(0, 7)]);
//                                        if (Util.isTrue(2, 100)) {
//                                            InventoryServiceNew.gI().addItemBag(player, gapt);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.createOtherMenu(player, ConstNpc.MENUGAPX1, "|2|Bạn vừa gắp được : " + gapt.template.name + "\n|3|Số Hồng Ngọc Trừ : " + rubyCost[0] + "\n|7|Chiến tiếp ngay!",
//                                                    "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
//                                        } else if (Util.isTrue(20, 100)) {
//                                            newItem1.itemOptions.add(new Item.ItemOption(93, 10));
//                                            newItem1.quantity = 1;
//                                            InventoryServiceNew.gI().addItemBag(player, newItem1);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//                                            this.createOtherMenu(player, ConstNpc.MENUGAPX1, "|2|Bạn vừa gắp được : " + newItem1.template.name + "\n|3|Số Hồng Ngọc Trừ : " + rubyCost[0] + "\n|7|Chiến tiếp ngay!",
//                                                    "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
//
//                                        } else {
//                                            this.createOtherMenu(player, ConstNpc.MENUGAPX1, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?" + "\n|3|Số Hồng Ngọc Trừ : " + rubyCost[0] + "\n|7|Chiến tiếp ngay!",
//                                                    "Gắp X1", "Gắp X10", "Gắp X100", "Rương Đồ");
//                                        }
//                                        player.inventory.ruby -= rubyCost[0];
//                                        Service.gI().sendMoney(player);
//                                    }
//                                    break;
//                                    case 1: {
//                                        int times = 10;
//                                        if (player.inventory.ruby < rubyCost[1] * times) {
//                                            Service.gI().sendThongBao(player, "Hết Tiền Roài");
//                                            return;
//                                        }
//                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp " + times + " lần");
//                                        int hn = 0;
//                                        for (int i = 0; i < times; i++) {
//                                            try {
//                                                hn += rubyCost[1];
//                                                Thread.sleep(50);
//                                                if (1 + player.inventory.itemsRuongPhu.size() > 100) {
//                                                    Service.gI().sendThongBao(player, " Vui lòng làm trống rương phụ đã đầy");
//                                                    break;
//                                                }
//                                                player.inventory.ruby -= rubyCost[1];
//                                                Service.gI().sendMoney(player);
//                                                Item gapt = Util.petccrandom(choise2[random2][sel]);
////                                                gapt = Util.petccrandom(choise2[random2][sel]);
//                                                int[] item = {1908, 1909, 1910, 457, 1596, 1597, 1598, 1599};
//                                                Item newItem1 = ItemService.gI().createNewItem((short) item[Util.nextInt(0, 7)]);
//                                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                                    if (Util.isTrue(2, 100)) {
//                                                        InventoryServiceNew.gI().addItemBag(player, gapt);
//                                                        InventoryServiceNew.gI().sendItemBags(player);
//                                                        this.createOtherMenu(player, 12348, "|7|ĐANG TIẾN HÀNH GẮP AUTO\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Đã gắp được : " + gapt.template.name + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//
//                                                    } else if (Util.isTrue(20, 100)) {
//                                                        newItem1.itemOptions.add(new Item.ItemOption(93, 10));
//                                                        newItem1.quantity = 1;
//                                                        InventoryServiceNew.gI().addItemBag(player, newItem1);
//                                                        InventoryServiceNew.gI().sendItemBags(player);
//                                                        this.createOtherMenu(player, 12348, "|7|ĐANG TIẾN HÀNH GẮP AUTO\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Đã gắp được : " + newItem1.template.name + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//                                                    } else {
//                                                        this.createOtherMenu(player, 12348, "|7|ĐANG TIẾN HÀNH GẮP AUTO\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//                                                    }
//                                                }
//                                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                                    if (Util.isTrue(2, 100)) {
//                                                        player.inventory.itemsRuongPhu.add(gapt);
//                                                        player.inventory.itemsRuongPhu.add(newItem1);
//                                                        this.createOtherMenu(player, 12348, "|7|ĐANG TIẾN HÀNH GẮP AUTO\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Đã gắp được : " + gapt.template.name + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//                                                    } else {
//                                                        this.createOtherMenu(player, 12348, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO VÀO RƯƠNG PHỤ\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//                                                    }
//
//                                                }
//
//                                            } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//                                    break;
//                                    case 2: {
//                                        int times = 100;
//                                        if (player.inventory.ruby < rubyCost[2] * times) {
//                                            Service.gI().sendThongBao(player, "Hết Tiền Roài");
//                                            return;
//                                        }
//                                        Service.gI().sendThongBao(player, "Tiến hành auto gắp " + times + " lần");
//                                        int hn = 0;
//                                        for (int i = 0; i < times; i++) {
//                                            try {
//                                                hn += rubyCost[2];
//                                                Thread.sleep(50);
//                                                if (1 + player.inventory.itemsRuongPhu.size() > 100) {
//                                                    Service.gI().sendThongBao(player, " Vui lòng làm trống rương phụ đã đầy");
//                                                    break;
//                                                }
//                                                player.inventory.ruby -= rubyCost[2];
//                                                Service.gI().sendMoney(player);
//                                                Item gapt = Util.petviprandom(choise3[random3][sel]);
////                                                gapt = Util.petviprandom(choise3[random3][sel]);
//                                                int[] item = {1908, 1909, 1910, 457, 1596, 1597, 1598, 1599};
//                                                Item newItem1 = ItemService.gI().createNewItem((short) item[Util.nextInt(0, 7)]);
//                                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                                    if (Util.isTrue(2, 100)) {
//                                                        InventoryServiceNew.gI().addItemBag(player, gapt);
//                                                        InventoryServiceNew.gI().sendItemBags(player);
//                                                        this.createOtherMenu(player, 12348, "|7|ĐANG TIẾN HÀNH GẮP AUTO\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Đã gắp được : " + gapt.template.name + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//
//                                                    } else if (Util.isTrue(20, 100)) {
//                                                        newItem1.itemOptions.add(new Item.ItemOption(93, 10));
//                                                        newItem1.quantity = 1;
//                                                        InventoryServiceNew.gI().addItemBag(player, newItem1);
//                                                        InventoryServiceNew.gI().sendItemBags(player);
//                                                        this.createOtherMenu(player, 12348, "|7|ĐANG TIẾN HÀNH GẮP AUTO\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Đã gắp được : " + newItem1.template.name + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//                                                    } else {
//                                                        this.createOtherMenu(player, 12348, "|7|ĐANG TIẾN HÀNH GẮP AUTO\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//                                                    }
//                                                }
//                                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                                    if (Util.isTrue(2, 100)) {
//                                                        player.inventory.itemsRuongPhu.add(gapt);
//                                                        player.inventory.itemsRuongPhu.add(newItem1);
//                                                        this.createOtherMenu(player, 12348, "|7|ĐANG TIẾN HÀNH GẮP AUTO\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Đã gắp được : " + gapt.template.name + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//                                                    } else {
//                                                        this.createOtherMenu(player, 12348, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO VÀO RƯƠNG PHỤ\nSỐ LƯỢT ĐÃ GẮP : " + (i + 1) + " LƯỢT\n" + "|2|Gắp hụt rồi!" + "\n|3|Số hồng ngọc đã trừ : " + hn + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                                                "Chờ một lát");
//                                                    }
//
//                                                }
//
//                                            } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//                                    break;
//                                    case 3:
//                                        this.createOtherMenu(player, ConstNpc.RUONG_PHU,
//                                                "|1|Tình yêu như một dây đàn\n"
//                                                + "Tình vừa được thì đàn đứt dây\n"
//                                                + "Đứt dây này anh thay dây khác\n"
//                                                + "Mất em rồi anh biết thay ai?",
//                                                "Rương Phụ\n(" + (player.inventory.itemsRuongPhu.size()
//                                                - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsRuongPhu))
//                                                + " món)",
//                                                "Xóa Hết\nRương Phụ", "Đóng");
//                                        break;
//
//                                }
//                                break;
//
//                            case ConstNpc.STOPGAP:
//                                if (select == 0) {
//                                    player.setAutoGraping(false);
//                                }
//                                break;
//                            case ConstNpc.RUONG_PHU:
////                                handleRuongPhuOptions(player, select);
//                                switch (select) {
//                                    case 0:
//                                        ShopServiceNew.gI().opendShop(player, "RUONG_PHU", true);
//                                        break;
//                                    case 1:
//                                        NpcService.gI().createMenuConMeo(player,
//                                                ConstNpc.CONFIRM_REMOVE_ALL_ITEM_RUONG_PHU, this.avartar,
//                                                "|3|Bạn chắc muốn xóa hết vật phẩm trong rương phụ?\n"
//                                                + "|7|Sau khi xóa sẽ không thể khôi phục!",
//                                                "Đồng ý", "Hủy bỏ");
//                                        break;
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }
//            }
//        };
//    }
    private static Npc gauPo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        String[] menu = {"Trân Bảo Các", "Thoát"};
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thương gia Gấu mập Po",
                                menu[0], menu[1]);
                    }
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
//                    short vp1 = 1596;
//                    short vp2 = 1597;
//                    short vp3 = 1598;
//                    short vp4 = 1599;
//                    byte slchange = 10;
//                    byte sldoivip = 30;
//                    Item i1 = InventoryServiceNew.gI().findItemBag(pl, vp1);
//                    Item i2 = InventoryServiceNew.gI().findItemBag(pl, vp2);
//                    Item i3 = InventoryServiceNew.gI().findItemBag(pl, vp3);
//                    Item i4 = InventoryServiceNew.gI().findItemBag(pl, vp4);
//                    int sli1 = (i1 == null) ? 0 : i1.quantity;
//                    int sli2 = (i2 == null) ? 0 : i2.quantity;
//                    int sli3 = (i3 == null) ? 0 : i3.quantity;
//                    int sli4 = (i4 == null) ? 0 : i4.quantity;
//                    String menu = "|7|Đổi vật phẩm chỉ định ra vật phẩm còn lại ngẫu nhiên"
//                            + "\n|4|Số lượng " + ItemService.gI().getTemplate(vp1).name + ": " + sli1
//                            + "\n|5|Số lượng " + ItemService.gI().getTemplate(vp2).name + ": " + sli2
//                            + "\n|3|Số lượng " + ItemService.gI().getTemplate(vp3).name + ": " + sli3
//                            + "\n|2|Số lượng " + ItemService.gI().getTemplate(vp4).name + ": " + sli4;
//                    String[] menuchoise = {
//                        "Đổi x" + slchange + " " + ItemService.gI().getTemplate(vp1).name + "\n(Ngẫu nhiên)",
//                        "Đổi x" + slchange + " " + ItemService.gI().getTemplate(vp2).name + "\n(Ngẫu nhiên)",
//                        "Đổi x" + slchange + " " + ItemService.gI().getTemplate(vp3).name + "\n(Ngẫu nhiên)",
//                        "Đổi x" + slchange + " " + ItemService.gI().getTemplate(vp4).name + "\n(Ngẫu nhiên)",
//                        "Thoát"
//                    };
                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
                        switch (pl.iDMark.getIndexMenu()) {
                            case ConstNpc.BASE_MENU:
                                switch (select) {
                                    case 0:
                                        ShopServiceNew.gI().opendShop(pl, "Trân Bảo Các", false);
                                        break;
//                                    case 1:
//                                        this.createOtherMenu(pl, ConstNpc.MENU_GAU_PO, menu,
//                                                menuchoise[0], menuchoise[1], menuchoise[2], menuchoise[3], menuchoise[4]);
//                                        break;
//                                    case 2: {
//                                        short[][] ramdomItem = {
//                                            {1493, 1367, 1368, 1369, 1319, 1320, 1321, 1322, 1323, 1344, 1345, 1346, 1387, 1389, 1600},
//                                            {1407, 1408, 1409, 1410, 1411, 1412, 1413, 1414, 1415, 1416, 1417, 1418, 1419, 1420, 1421, 1422},
//                                            {1403, 1404, 1405, 1336, 1337, 1338, 1336, 1337, 1338, 1336, 1337, 1338, 1336, 1401, 1402, 1406}};
//                                        int sel = Util.nextInt(Util.isTrue(1, 100) ? 0 : 3, Util.isTrue(1, 100) ? 15 : 12);
//                                        int choise = Util.nextInt(0, Util.isTrue(1, 100) ? 2 : 1);
//                                        if (sli1 < 30) {
//                                            Service.gI().sendThongBao(pl, "Bạn thiếu " + (sldoivip - sli1) + " " + ItemService.gI().getTemplate(vp1).name);
//                                            return;
//                                        }
//                                        if (sli2 < 30) {
//                                            Service.gI().sendThongBao(pl, "Bạn thiếu " + (sldoivip - sli2) + " " + ItemService.gI().getTemplate(vp2).name);
//                                            return;
//                                        }
//                                        if (sli3 < 30) {
//                                            Service.gI().sendThongBao(pl, "Bạn thiếu " + (sldoivip - sli3) + " " + ItemService.gI().getTemplate(vp3).name);
//                                            return;
//                                        }
//                                        if (sli4 < 30) {
//                                            Service.gI().sendThongBao(pl, "Bạn thiếu " + (sldoivip - sli4) + " " + ItemService.gI().getTemplate(vp4).name);
//                                            return;
//                                        }
//                                        if (InventoryServiceNew.gI().getCountEmptyBag(pl) < 1) {
//                                            Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
//                                            return;
//                                        }
//                                        Item caitrang = ItemService.gI().createNewItem((short) ramdomItem[choise][sel], 1);
//                                        if (choise == 0) {
//                                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(25, 40)));
//                                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(25, 40)));
//                                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(25, 40)));
//                                            if (Util.isTrue(99, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(10, 14)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(14, Util.nextInt(10, 14)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(12, 30)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(Util.nextInt(94, 101), Util.nextInt(10, 25)));
//                                            }
//                                        } else if (choise == 1) {
//                                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(5, 12)));
//                                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(5, 12)));
//                                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(5, 12)));
//                                            if (Util.isTrue(99, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(10, 14)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(14, Util.nextInt(10, 14)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(5, 12)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(Util.nextInt(94, 101), Util.nextInt(10, 25)));
//                                            }
//                                        } else if (choise == 2) {
//                                            caitrang.itemOptions.add(new ItemOption(50, Util.nextInt(10, 15)));
//                                            caitrang.itemOptions.add(new ItemOption(77, Util.nextInt(10, 15)));
//                                            caitrang.itemOptions.add(new ItemOption(103, Util.nextInt(10, 15)));
//                                            if (Util.isTrue(99, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(93, Util.nextInt(10, 14)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(14, Util.nextInt(10, 14)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(5, Util.nextInt(5, 15)));
//                                            }
//                                            if (Util.isTrue(5, 100)) {
//                                                caitrang.itemOptions.add(new ItemOption(Util.nextInt(94, 101), Util.nextInt(10, 25)));
//                                            }
//                                        }
//                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, i1, sldoivip);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, i2, sldoivip);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, i3, sldoivip);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, i4, sldoivip);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                        Service.gI().sendThongBao(pl, "Bạn đã nhận được " + 1 + " " + caitrang.template.name);
//                                        InventoryServiceNew.gI().addItemBag(pl, caitrang);
//                                    }
//
//                                    break;
//                                    case 3:
//                                        Service.gI().sendThongBao(pl, "Ad chưa thêm xong");
//                                        break;
                                    case 1:
                                        break;
                                }

                                break;
//                            case ConstNpc.MENU_GAU_PO:
//                                switch (select) {
//                                    case 0: {
//                                        Item item = InventoryServiceNew.gI().findItemBag(pl, 1596);
//                                        if (item == null) {
//                                            Service.gI().sendThongBao(pl, "Không tìm thấy " + ItemService.gI().getTemplate(1596).name);
//                                        } else {
//                                            short[] ramdomItem = {1597, 1598, 1599};
//                                            int sel = Util.nextInt(0, 2);
//                                            int sl = item.quantity;
//                                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) < 1) {
//                                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
//                                                return;
//                                            }
//                                            if (sl >= 10) {
//                                                Item vpsk = ItemService.gI().createNewItem((short) ramdomItem[sel], 1);
//                                                int slnew = Util.nextInt(0, 9);
//                                                vpsk.itemOptions.add(new ItemOption(93, 10));
//                                                vpsk.quantity = 10 - slnew;
//                                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, slchange);
//                                                InventoryServiceNew.gI().addItemBag(pl, vpsk);
//                                                InventoryServiceNew.gI().sendItemBags(pl);
//                                                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + (10 - slnew) + " " + vpsk.template.name);
//                                            } else {
//                                                Service.gI().sendThongBao(pl, "Thiếu " + (10 - sl) + " " + item.template.name);
//                                            }
//
//                                        }
//                                        this.createOtherMenu(pl, ConstNpc.MENU_GAU_PO, menu,
//                                                menuchoise[0], menuchoise[1], menuchoise[2], menuchoise[3], menuchoise[4]);
//
//                                    }
//                                    break;
//                                    case 1: {
//                                        Item item = InventoryServiceNew.gI().findItemBag(pl, 1597);
//                                        if (item == null) {
//                                            Service.gI().sendThongBao(pl, "Không tìm thấy " + ItemService.gI().getTemplate(1596).name);
//                                        } else {
//                                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) < 1) {
//                                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
//                                                return;
//                                            }
//                                            short[] ramdomItem = {1596, 1598, 1599};
//                                            int sel = Util.nextInt(0, 2);
//                                            int sl = item.quantity;
//                                            if (sl >= 10) {
//                                                Item vpsk = ItemService.gI().createNewItem((short) ramdomItem[sel], 1);
//                                                int slnew = Util.nextInt(0, 9);
//                                                vpsk.itemOptions.add(new ItemOption(93, 10));
//                                                vpsk.quantity = 10 - slnew;
//                                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, slchange);
//                                                InventoryServiceNew.gI().addItemBag(pl, vpsk);
//                                                InventoryServiceNew.gI().sendItemBags(pl);
//                                                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + (10 - slnew) + " " + vpsk.template.name);
//                                            } else {
//                                                Service.gI().sendThongBao(pl, "Thiếu " + (10 - sl) + " " + item.template.name);
//                                            }
//                                        }
//                                        this.createOtherMenu(pl, ConstNpc.MENU_GAU_PO, menu,
//                                                menuchoise[0], menuchoise[1], menuchoise[2], menuchoise[3], menuchoise[4]);
//
//                                    }
//                                    break;
//                                    case 2: {
//                                        Item item = InventoryServiceNew.gI().findItemBag(pl, 1598);
//                                        if (item == null) {
//                                            Service.gI().sendThongBao(pl, "Không tìm thấy " + ItemService.gI().getTemplate(1596).name);
//                                        } else {
//                                            short[] ramdomItem = {1596, 1597, 1599};
//                                            int sel = Util.nextInt(0, 2);
//                                            int sl = item.quantity;
//                                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) < 1) {
//                                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
//                                                return;
//                                            }
//                                            if (sl >= 10) {
//                                                Item vpsk = ItemService.gI().createNewItem((short) ramdomItem[sel], 1);
//                                                int slnew = Util.nextInt(0, 9);
//                                                vpsk.itemOptions.add(new ItemOption(93, 10));
//                                                vpsk.quantity = 10 - slnew;
//                                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, slchange);
//                                                InventoryServiceNew.gI().addItemBag(pl, vpsk);
//                                                InventoryServiceNew.gI().sendItemBags(pl);
//                                                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + (10 - slnew) + " " + vpsk.template.name);
//                                            } else {
//                                                Service.gI().sendThongBao(pl, "Thiếu " + (10 - sl) + " " + item.template.name);
//                                            }
//                                        }
//                                        this.createOtherMenu(pl, ConstNpc.MENU_GAU_PO, menu,
//                                                menuchoise[0], menuchoise[1], menuchoise[2], menuchoise[3], menuchoise[4]);
//
//                                    }
//                                    break;
//                                    case 3: {
//                                        Item item = InventoryServiceNew.gI().findItemBag(pl, 1599);
//                                        if (item == null) {
//                                            Service.gI().sendThongBao(pl, "Không tìm thấy " + ItemService.gI().getTemplate(1596).name);
//                                        } else {
//                                            short[] ramdomItem = {1596, 1597, 1598};
//                                            int sel = Util.nextInt(0, 2);
//                                            int sl = item.quantity;
//                                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) < 1) {
//                                                Service.gI().sendThongBao(pl, "Hành trang không đủ chỗ trống");
//                                                return;
//                                            }
//                                            if (sl >= 10) {
//                                                Item vpsk = ItemService.gI().createNewItem((short) ramdomItem[sel], 1);
//                                                int slnew = Util.nextInt(0, 9);
//                                                vpsk.itemOptions.add(new ItemOption(93, 10));
//                                                vpsk.quantity = 10 - slnew;
//                                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, slchange);
//                                                InventoryServiceNew.gI().addItemBag(pl, vpsk);
//                                                InventoryServiceNew.gI().sendItemBags(pl);
//                                                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + (10 - slnew) + " " + vpsk.template.name);
//                                            } else {
//                                                Service.gI().sendThongBao(pl, "Thiếu " + (10 - sl) + " " + item.template.name);
//                                            }
//                                        }
//                                        this.createOtherMenu(pl, ConstNpc.MENU_GAU_PO, menu,
//                                                menuchoise[0], menuchoise[1], menuchoise[2], menuchoise[3], menuchoise[4]);
//
//                                    }
//                                    break;
//                                }
//                                break;
                        }
                    }
                }
            }
        };
    }

//    private static Npc suKien2_9(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            private Timer timer; // Khai báo biến Timer
//            private long questionStartTime; // Thời điểm bắt đầu câu hỏi
//            private int remainingTime; // Thời gian còn lại
//
////            @Override
////            public void openBaseMenu(Player player) {
////                if (canOpenNpc(player)) {
////                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
////                        String[] menu = {"Trả lời câu hỏi", "Đổi vật phẩm\n(Ngẫu nhiên)", "Top sự kiện", "Thoát"};
////                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|3|Trả lời câu hỏi, nhận ngay quà khủng"
////                                //                                + "\n|5|diễn ra từ 12h00 ngày 2/6/2024 đến 13h00 ngày 7/6/2024"
////                                + "\n|1|Nhân ngày quốc khánh 2/9/2024 admin sẽ có 20 câu hỏi ngẫu nhiên dành cho các bạn,"
////                                + " số lượng câu đúng càng nhiều quà càng lớn,"
////                                + " các bạn hãy cố gắng vừa ôn lại chút lịch sử,"
////                                + " cũng như giải trí sau giờ học và làm vất vả !!"
////                                + "\n|2|Chú ý: Trả lời đúng được cộng 1 điểm, trả lời sai không được điểm",
////                                menu[0], menu[1], menu[2], menu[3]);
////                    }
////                }
////            }
//            @Override
//            public void openBaseMenu(Player player) {
//
//                String npcSay = "|3|Trả lời câu hỏi, nhận ngay quà khủng"
//                        + "\n|1|Nhân ngày quốc khánh 2/9/2024 admin sẽ có 20 câu hỏi ngẫu nhiên dành cho các bạn,"
//                        + " số lượng câu đúng càng nhiều quà càng lớn,"
//                        + " các bạn hãy cố gắng vừa ôn lại chút lịch sử,"
//                        + " cũng như giải trí sau giờ học và làm vất vả !!"
//                        + "\n|2|Chú ý: Trả lời đúng được cộng 1 điểm, trả lời sai không được điểm";
//                String[] Menus = {"Trả lời câu hỏi", "Đổi vật phẩm\n(Ngẫu nhiên)", "Top sự kiện", "Thoát"};
//                createOtherMenu(player, ConstNpc.BASE_MENU, npcSay, Menus);
//            }
//            String[] menuCauHoi = {"|3|Ngày Quốc khánh của Việt Nam là ngày nào?\n"
//                + "\n|5|"
//                + "A. 19/8/1945\n"
//                + "B. 2/9/1945\n"
//                + "C. 30/4/1975\n"
//                + "D. 7/5/1954", // "B" // Đáp án đúng
//
//                "|3|Chủ tịch Hồ Chí Minh đã đọc bản Tuyên ngôn Độc lập tại đâu vào ngày 2/9/1945?\n"
//                + "\n|5|"
//                + "A. Quảng trường Ba Đình\n"
//                + "B. Phủ Chủ tịch\n"
//                + "C. Lăng Chủ tịch Hồ Chí Minh\n"
//                + "D. Cột cờ Hà Nội", // "A" // Đáp án đúng
//
//                "|3|Quốc khánh Việt Nam lần đầu tiên được tổ chức vào năm nào?\n"
//                + "\n|5|"
//                + "A. 1945\n"
//                + "B. 1946\n"
//                + "C. 1954\n"
//                + "D. 1975", // "A" // Đáp án đúng
//
//                "|3|Tuyên ngôn Độc lập của Việt Nam được Hồ Chí Minh đọc tại đâu?\n"
//                + "\n|5|"
//                + "A. Nhà Rông\n"
//                + "B. Nhà sàn Bác Hồ\n"
//                + "C. Số nhà 48 Hàng Ngang\n"
//                + "D. Quảng trường Ba Đình", // "D" // Đáp án đúng
//
//                "|3|Trong bản Tuyên ngôn Độc lập, Hồ Chí Minh đã trích dẫn tuyên ngôn của quốc gia nào?\n"
//                + "\n|5|"
//                + "A. Mỹ\n"
//                + "B. Pháp\n"
//                + "C. Anh\n"
//                + "D. Mỹ và Pháp", // "D" // Đáp án đúng
//
//                "|3|Ngày Quốc khánh Việt Nam 2/9 được công nhận là ngày nghỉ lễ vào năm nào?\n"
//                + "\n|5|"
//                + "A. 1945\n"
//                + "B. 1946\n"
//                + "C. 1954\n"
//                + "D. 1975", // "B" // Đáp án đúng
//
//                "|3|Ai là người phát động Phong trào Cách mạng tháng Tám dẫn đến ngày Quốc khánh 2/9?\n"
//                + "\n|5|"
//                + "A. Trường Chinh\n"
//                + "B. Hồ Chí Minh\n"
//                + "C. Võ Nguyên Giáp\n"
//                + "D. Lê Duẩn", // "B" // Đáp án đúng
//
//                "|3|Bản Tuyên ngôn Độc lập của Việt Nam được viết theo thể loại nào?\n"
//                + "\n|5|"
//                + "A. Thơ ca\n"
//                + "B. Truyện ngắn\n"
//                + "C. Văn xuôi chính luận\n"
//                + "D. Tiểu thuyết", // "C" // Đáp án đúng
//
//                "|3|Quốc khánh 2/9 còn được gọi là ngày gì?\n"
//                + "\n|5|"
//                + "A. Ngày chiến thắng\n"
//                + "B. Ngày giải phóng\n"
//                + "C. Ngày Độc lập\n"
//                + "D. Ngày toàn quốc kháng chiến", // "C" // Đáp án đúng
//
//                "|3|Bản Tuyên ngôn Độc lập của Việt Nam bắt đầu với câu nào?\n"
//                + "\n|5|"
//                + "A. \"Hỡi đồng bào cả nước!\"\n"
//                + "B. \"Tất cả mọi người sinh ra đều có quyền bình đẳng.\"\n"
//                + "C. \"Tất cả mọi người sinh ra đều có quyền sống, quyền tự do và quyền mưu cầu hạnh phúc.\"\n"
//                + "D. \"Độc lập, Tự do, Hạnh phúc.\"", // "C" // Đáp án đúng
//
//                "|3|Ai là người soạn thảo bản Tuyên ngôn Độc lập của Việt Nam?\n"
//                + "\n|5|"
//                + "A. Trường Chinh\n"
//                + "B. Võ Nguyên Giáp\n"
//                + "C. Phạm Văn Đồng\n"
//                + "D. Hồ Chí Minh", // "D" // Đáp án đúng
//
//                "|3|Sự kiện nào đã mở đầu cho cuộc cách mạng tháng Tám tại Hà Nội?\n"
//                + "\n|5|"
//                + "A. Khởi nghĩa Bắc Sơn\n"
//                + "B. Khởi nghĩa Nam Kỳ\n"
//                + "C. Biểu tình ngày 19/8/1945\n"
//                + "D. Tổng tiến công Xuân Mậu Thân 1968", // "C" // Đáp án đúng
//
//                "|3|Quốc khánh 2/9 là sự kiện đánh dấu điều gì?\n"
//                + "\n|5|"
//                + "A. Chiến thắng Điện Biên Phủ\n"
//                + "B. Thống nhất đất nước\n"
//                + "C. Sự ra đời của nước Việt Nam Dân chủ Cộng hòa\n"
//                + "D. Sự kết thúc chiến tranh chống Mỹ", // "C" // Đáp án đúng
//
//                "|3|Ai là người đã đọc Tuyên ngôn Độc lập vào ngày 2/9/1945?\n"
//                + "\n|5|"
//                + "A. Phạm Văn Đồng\n"
//                + "B. Võ Nguyên Giáp\n"
//                + "C. Hồ Chí Minh\n"
//                + "D. Trường Chinh", // "C" // Đáp án đúng
//
//                "|3|Tuyên ngôn Độc lập của Việt Nam trích dẫn từ tuyên ngôn của nước nào?\n"
//                + "\n|5|"
//                + "A. Hoa Kỳ\n"
//                + "B. Pháp\n"
//                + "C. Hoa Kỳ và Pháp\n"
//                + "D. Nga", // "C" // Đáp án đúng
//
//                "|3|Quốc khánh Việt Nam 2/9 là dịp để người dân tưởng nhớ ai?\n"
//                + "\n"
//                + "A. Các anh hùng liệt sĩ\n"
//                + "B. Các nhà cách mạng\n"
//                + "C. Hồ Chí Minh\n"
//                + "D. Tất cả các đáp án trên", // "D" // Đáp án đúng
//
//                "|3|Ngày Quốc khánh 2/9/1945, nước Việt Nam Dân chủ Cộng hòa được thành lập tại đâu?\n"
//                + "\n|5|"
//                + "A. Hà Nội\n"
//                + "B. Huế\n"
//                + "C. Sài Gòn\n"
//                + "D. Điện Biên Phủ", // "A" // Đáp án đúng
//
//                "|3|Sự kiện nào diễn ra vào ngày 2/9/1945?\n"
//                + "\n|5|"
//                + "A. Bác Hồ đọc Tuyên ngôn Độc lập\n"
//                + "B. Khởi nghĩa Bắc Sơn\n"
//                + "C. Thành lập Đảng Cộng sản Việt Nam\n"
//                + "D. Tổng khởi nghĩa giành chính quyền", // "A" // Đáp án đúng
//
//                "|3|Ngày Quốc khánh Việt Nam 2/9 được tổ chức lần đầu vào năm nào?\n"
//                + "\n|5|"
//                + "A. 1945\n"
//                + "B. 1946\n"
//                + "C. 1954\n"
//                + "D. 1975", // "B" // Đáp án đúng
//
//                "|3|Quảng trường Ba Đình, nơi diễn ra lễ Quốc khánh 2/9, nằm ở đâu\n?"
//                + "\n|5|"
//                + "A. Hà Nội\n"
//                + "B. Huế\n"
//                + "C. Sài Gòn\n"
//                + "D. Hải Phòng" // "A" // Đáp án đúng
//        };
//
//            @Override
//            public void confirmMenu(Player pl, int select) {
////                String plWin = MiniGame.gI().MiniGame_S2.result_name;
////                String KQ = MiniGame.gI().MiniGame_S2.result + "";
////                String Money = MiniGame.gI().MiniGame_S2.money + "";
////                String count = MiniGame.gI().MiniGame_S2.players.size() + "";
//                String second = MiniGame.gI().MiniGame_S2.second + "";
//                //  String number = MiniGame.gI().MiniGame_S2.strNumber((int) player.id);
//                if (canOpenNpc(pl)) {
//                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
//                        switch (pl.iDMark.getIndexMenu()) {
//                            case ConstNpc.BASE_MENU:
//                                switch (select) {
//                                    case 0:
//
//                                        // Chọn một câu hỏi ngẫu nhiên từ menuCauHoi
//                                        int questionIndex = Util.nextInt(0, menuCauHoi.length - 1);
//                                        String question = menuCauHoi[questionIndex];
//                                        String correctAnswer = getCorrectAnswerForIndex(questionIndex); // Phương thức lấy đáp án đúng
//
//                                        // Cập nhật câu hỏi và đáp án đúng cho player
//                                        pl.iDMark.setCurrentQuestion(question);
//                                        pl.iDMark.setCurrentAnswer(correctAnswer);
//
//                                        // Hiển thị câu hỏi
//                                        MiniGame.gI().MiniGame_S2.active(1000);
//                                        this.createOtherMenu(pl, ConstNpc.MENU_ANSWER,
//                                                "<" + second + ">giây\n"
//                                                + question, "A", "B", "C", "D", "Đóng");
//                                        break;
//                                    case 1:
//                                        break;
//                                }
//                                break;
//
//                            case ConstNpc.MENU_ANSWER:
//
//                                String currentQuestion = pl.iDMark.getCurrentQuestion();
//                                String correctAnswer = pl.iDMark.getCurrentAnswer();
//                                // Kiểm tra xem đáp án người chơi chọn có đúng không
//                                if (getAnswerForSelect(select).equals(correctAnswer)) {
//                                    int newQuestionIndex = Util.nextInt(0, menuCauHoi.length - 1);
//                                    String newQuestion = menuCauHoi[newQuestionIndex];
//                                    String newAnswer = getCorrectAnswerForIndex(newQuestionIndex);
//
//                                    // Cập nhật câu hỏi và đáp án đúng mới cho player
//                                    pl.iDMark.setCurrentQuestion(newQuestion);
//                                    pl.iDMark.setCurrentAnswer(newAnswer);
//
//                                    // Hiển thị câu hỏi mới
//                                    MiniGame.gI().MiniGame_S2.active(1000);
//                                    this.createOtherMenu(pl, ConstNpc.MENU_ANSWER, "Đáp án đúng! Bạn đã nhận được điểm. Bạn hãy trả lời câu hỏi tiếp sau đây:\nThời gian còn lại: \n"
//                                            + "<" + second + ">giây\n"
//                                            + newQuestion, "A", "B", "C", "D", "Đóng");
//
//                                } else {
//                                    // Nếu đáp án sai, thông báo và thoát khỏi vòng lặp
//                                    Service.gI().sendThongBao(pl, "Đáp án sai. Bạn không thể tiếp tục.");
//                                    // Bạn có thể thêm logic khác nếu cần, chẳng hạn như reset trạng thái hoặc làm gì đó khi đáp án sai
//                                    break;
//                                }
//                                break;
//                        }
//                    }
//                }
//            }
//
//            private String getCorrectAnswerForIndex(int index) {
//                // Đáp án đúng tương ứng với chỉ số câu hỏi
//                switch (index) {
//                    case 0:
//                        return "B";
//                    case 1:
//                        return "A";
//                    case 2:
//                        return "A";
//                    case 3:
//                        return "D";
//                    case 4:
//                        return "D";
//                    case 5:
//                        return "B";
//                    case 6:
//                        return "C";
//                    case 7:
//                        return "C";
//                    case 8:
//                        return "C";
//                    case 9:
//                        return "C";
//                    case 10:
//                        return "D";
//                    case 11:
//                        return "A";
//                    case 12:
//                        return "C";
//                    case 13:
//                        return "C";
//                    case 14:
//                        return "D";
//                    case 15:
//                        return "A";
//                    case 16:
//                        return "D";
//                    case 17:
//                        return "A";
//                    case 18:
//                        return "A";
//                    case 19:
//                        return "A";
//                    default:
//                        return null;
//                }
//            }
//
//// Phương thức trả về đáp án cho chỉ số lựa chọn
//            private String getAnswerForSelect(int select) {
//                switch (select) {
//                    case 0:
//                        return "A";
//                    case 1:
//                        return "B";
//                    case 2:
//                        return "C";
//                    case 3:
//                        return "D";
//                    default:
//                        return null;
//                }
//            }
//
//        };
//    }
    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.getSession().actived && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                        Service.gI().sendThongBao(player, "Xong nv KUKU mới có thể vô\n"
                                + "Mở thành viên nữa nhé");
                        return;
                    }
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá " + cn.costNhanBan + " hồng ngọc không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                if (player.getSession().actived && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                                    Service.gI().sendThongBao(player, "Xong nv KUKU mới có thể vô\n"
                                            + "Mở thành viên nữa nhé");
                                } else {
                                    Boss oldBossClone = BossManager.gI().getBossByType(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.ruby < cn.costNhanBan) {
                                        this.npcChat(player, "Nhà ngươi không đủ " + cn.costNhanBan + " Ngọc Hồng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản" + player.name,
                                                player.gender,
                                                new short[]{
                                                    player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.getAura(), player.getEffFront()}, player.nPoint.dame,
                                                new long[]{
                                                    player.nPoint.hpMax},
                                                new int[]{
                                                    140},
                                                skillTemp,
                                                new String[]{
                                                    "|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{
                                                    "|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{
                                                    "|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );

                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            // 0358124452
                                            //Logger.logException(NpcFactory.class, e);
                                        }
                                        //trừ ruby khi gọi boss
                                        player.inventory.ruby -= cn.costNhanBan;
                                        Service.gI().sendMoney(player);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Tapion(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 19) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi có muốn tiến vào map Vùng đất thám hiểm ?", "Đồng Ý", "Đóng");

                }
                if (this.mapId == 126) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi có muốn quay về TP Vegeta không ?", "Đồng Ý", "Đóng");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapInYard(player, 126, -1, 552);
                            }
                        }
                    }
                    if (this.mapId == 126) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapInYard(player, 19, -1, 552);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc TruonglaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                Item mcl = InventoryServiceNew.gI().findItemBagByTemp(player, ConstTranhNgocNamek.ITEM_TRANH_NGOC);
                int slMCL = (mcl == null) ? 0 : mcl.quantity;
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân",
                                "Tham gia", "Đổi điểm\nThưởng\n[" + slMCL + "]", "Bảng\nxếp hạng", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            switch (select) {
                                case 0:
                                    if (TranhNgoc.gI().isTimeRegisterWar()) {
                                        if (player.iDMark.getTranhNgoc() == -1) {
                                            this.createOtherMenu(player, ConstNpc.REGISTER_TRANH_NGOC,
                                                    "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân\nPhe Cadic: " + TranhNgoc.gI().getPlayersCadic().size() + "\nPhe Fide: " + TranhNgoc.gI().getPlayersFide().size(),
                                                    "Tham gia phe Cadic", "Tham gia phe Fide", "Đóng");
                                        } else {
                                            this.createOtherMenu(player, ConstNpc.LOG_OUT_TRANH_NGOC,
                                                    "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân\nPhe Cadic: " + TranhNgoc.gI().getPlayersCadic().size() + "\nPhe Fide: " + TranhNgoc.gI().getPlayersFide().size(),
                                                    "Hủy\nĐăng Ký", "Đóng");
                                        }
                                        return;
                                    }
                                    Service.gI().sendPopUpMultiLine(player, 0, 7184, "Sự kiện sẽ mở đăng ký vào lúc " + TranhNgoc.HOUR_REGISTER + ":" + TranhNgoc.MIN_REGISTER + "\nSự kiện sẽ bắt đầu vào " + TranhNgoc.HOUR_OPEN + ":" + TranhNgoc.MIN_OPEN + " và kết thúc vào " + TranhNgoc.HOUR_CLOSE + ":" + TranhNgoc.HOUR_CLOSE);
                                    break;
                                case 1:// Shop
                                    ShopServiceNew.gI().opendShop(player, "TRUONG_LAO", false);
                                    break;
                                case 2:
                                    // Service.gI().showListTop(player, Manager.topDauThan);
                                    // Service.gI().sendThongBao(player, "Update coming soon");
                                    break;
                            }
                            break;
                        case ConstNpc.REGISTER_TRANH_NGOC:
                            switch (select) {
                                case 0:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sửa dụng chức năng này!");
                                        return;
                                    }
                                    player.iDMark.setTranhNgoc((byte) 1);
                                    TranhNgoc.gI().addPlayersCadic(player);
                                    Service.gI().sendThongBao(player, "Đăng ký vào phe Cadic thành công");
                                    break;
                                case 1:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sửa dụng chức năng này!");
                                        return;
                                    }
                                    player.iDMark.setTranhNgoc((byte) 2);
                                    TranhNgoc.gI().addPlayersFide(player);
                                    Service.gI().sendThongBao(player, "Đăng ký vào phe Fide thành công");
                                    break;
                            }
                            break;
                        case ConstNpc.LOG_OUT_TRANH_NGOC:
                            switch (select) {
                                case 0:
                                    player.iDMark.setTranhNgoc((byte) -1);
                                    TranhNgoc.gI().removePlayersCadic(player);
                                    TranhNgoc.gI().removePlayersFide(player);
                                    Service.gI().sendThongBao(player, "Hủy đăng ký thành công");
                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc CauBe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 153 || this.mapId == 5) {
                    if (player.getSession() != null) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi có muốn tiến vào map để up mảnh vỡ và mảnh hồn bông tai hay không ?"
                                + "\n Lãnh địa bang hội: Có tỉ lệ rơi mảnh vỡ, mảnh hồn bông tai và đá xanh lam"
                                + "\n Cung trăng: Có tỉ lệ rơi MẢNH NGUYỆT ẤN"
                                + "\n Địa ngục: Có tỉ lệ rơi MẢNH TINH ẤN"
                                + "\n Dung Nham: Có tỉ lệ rơi MẢNH NHẬT ẤN"
                                + "\n Thung lũng Sharotto: Có tỉ lệ rơi Tinh Thạch"
                                + "\n Vùng đất Cấm: Đang Update"
                                + "\n Khu nghỉ dưỡng: Đang Update"
                                + "\n Trạm tàu hủy diệt: Đang Update"
                                + "\n Vùng đất băng giá: Đang Update"
                                + "\n Cổng tiên môn",
                                "Lãnh địa bang hội",
                                "Cung Trăng",
                                "Địa Ngục",
                                "Dung Nham",
                                "Thung lũng Sharotto",
                                "Vùng đất Cấm",
                                "Khu nghỉ dưỡng",
                                "Trạm tàu hủy diệt",
                                "Vùng đất băng giá",
                                "Cổng tiên môn"
                        );
                    }
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ngươi có muốn về đảo Kame không ?",
                            "Có",
                            "Maybe Not");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapInYard(player, 157, -1, 552);
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_28_0) {
                                        Service.gI().sendThongBao(player, "Cần hoàn thành nhiệm vụ Siêu Bọ Hung");
                                        return;
                                    }
                                    ChangeMapService.gI().changeMapInYard(player, 202, -1, 552);
                                    break;
                                case 2:
                                    if (player.getSession() != null && player.getSession().player.nPoint.power <= 200000000000L) {

                                        Service.gI().sendThongBao(player, "Bạn cần đạt 200 tỉ sức mạnh");
                                        return;
                                    }
                                    ChangeMapService.gI().changeMapInYard(player, 214, -1, 552);
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapInYard(player, 216, -1, 552);
                                    break;

                                case 4:
                                    ChangeMapService.gI().changeMapInYard(player, 176, -1, 552);
                                    break;

                                case 5:
                                    ChangeMapService.gI().changeMapInYard(player, 170, -1, 552);
                                    break;
                                case 6:
                                    ChangeMapService.gI().changeMapInYard(player, 212, -1, 552);
                                    break;
                                case 7:
                                    ChangeMapService.gI().changeMapInYard(player, 146 + player.gender, -1, 552);
                                    break;
                                case 8:
                                    ChangeMapService.gI().changeMapInYard(player, 152, -1, 552);
                                    break;
                                case 9:
                                    ChangeMapService.gI().changeMapInYard(player, 220, -1, 552);
                                    break;
                            }
                        }
                    } else {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapInYard(player, 5, -1, 552);
                                break;
                            case 1:

                                break;
                        }
                    }
                }
            }
        };
    }

    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        return new Npc(mapId, status, cx, cy, tempId, avatar) {
            // Phương thức openBaseMenu không thay đổi
            @Override
            public void openBaseMenu(Player player) {
                // Item mcl = InventoryServiceNew.gI().findItemBagByTemp(player, ConstDataEvent.ID_ITEM_6);
                // int slMCL = (mcl == null) ? 0 : mcl.quantity;
                if (canOpenNpc(player)) {
                    String menu = "Chào con, ta rất vui khi gặp con\n"
                            + " Con muốn làm gì nào ?\n"
                            + "\b|7|Số người online: " + (Client.gI().getPlayers().size() * 2);
                    String[] choisemenu = {"Nói chuyện", "Quà mốc nạp", "Skip Quest"};
                    this.createOtherMenu(player, 1111, menu, choisemenu);
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (!canOpenNpc(player)) {
                    return;
                }
                switch (player.iDMark.getIndexMenu()) {
                    case 1111:
                        handleMainOptions(player, select);
                        break;
                    case 1115:
                        handleMainMocNap(player, select);
                        break;
                    case 1112:
                        handleMainTalk(player, select);
                        break;
//                    case 1113:
//                        handleMainDoiDiem(player, select);
//                        break;
//                    case 1114:
////                        handleMainSKquylao(player, select);
//                        handleMainSKquylao2(player, select);
//                        break;

//                    case 1117:
//                        handleMainNhanQuaDiem(player, select);
//                        break;
//                    case 21111:
//                        handleMainShopDiem(player, select);
//                        break;
//                    case 21112:
//                        handleMainDoiItemSuKien(player, select);
//                        break;
//                    case ConstNpc.BASE_MENU_QUY_LAO:
//                        handleMenuXemTopSKquylao(player, select);
////                        handleMenuXemTopSKquylao2(player, select);
//                        break;
//                    case ConstNpc.MENU_OPEN_SUKIEN:
//                        openMenuSuKien(player, this, tempId, select);
//                        break;
                    case ConstNpc.MENU_OPENED_DBKB:
                        handleMenuBDKB(player, select);
                        break;
                    case ConstNpc.MENU_ACCEPT_GO_TO_BDKB:
                        handleOpenBDKB(player, select);
                        break;
                    case ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK:
                        handleGetTask(player, select);
                        break;
                    case ConstNpc.MENU_OPTION_PAY_SIDE_TASK:
                        handlePayTask(player, select);
                        break;
                }
            }

            private void handleGetTask(Player player, int select) {
                switch (select) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        TaskService.gI().changeSideTask(player, (byte) select);
                        break;
                }
            }

            private void handlePayTask(Player player, int select) {
                switch (select) {
                    case 0:
                        TaskService.gI().paySideTask(player);//trả nhiệm vụ
                        break;
                    case 1:
                        TaskService.gI().removeSideTask(player);//hủy nhiệm vụ
                        break;
                }
            }

//            private void handleMenuXemTopSKquylao(Player player, int select) {
//                if (select == 0) {
//                    Service.gI().sendThongBaoOK(player, TopService.getTopSKHE());
//                }
//                if (select == 1) {
//
//                    Service.gI().sendBigMessage(player, avartar,
//                            "|3|Mốc 500 điểm:"
//                            + " x" + mocdiem.sli10_1 + " " + ItemService.gI().getTemplate(mocdiem.i10_1).name + ",\n"
//                            + " x" + mocdiem.sli10_2 + " " + ItemService.gI().getTemplate(mocdiem.i10_2).name + "\n"
//                            + "|2|Mốc 1000 điểm:"
//                            + " x" + mocdiem.sli20_1 + " " + ItemService.gI().getTemplate(mocdiem.i20_1).name + ",\n"
//                            + " x" + mocdiem.sli20_2 + " Bộ ngọc rồng\n"
//                            + "|1|Mốc 2000 điểm:"
//                            + " x" + mocdiem.sli30_1 + " " + ItemService.gI().getTemplate(mocdiem.i30_1).name + ",\n"
//                            + " x" + mocdiem.sli30_2 + " " + ItemService.gI().getTemplate(mocdiem.i30_2).name + "\n"
//                            + "|4|Mốc 5000 điểm: "
//                            + " x" + mocdiem.sli50_1 + " " + ItemService.gI().getTemplate(mocdiem.i50_1).name + ",\n"
//                            + ItemService.gI().getTemplate(mocdiem.i50_2).name + " " + mocdiem.csi50_2 + "% cs\n"
//                            + "|6|Mốc 20000 điểm: "
//                            + " x" + mocdiem.sli70_1 + " " + ItemService.gI().getTemplate(mocdiem.i70_1).name + " " + mocdiem.csi70_1 + "% cs,\n"
//                            + " x" + mocdiem.sli70_2 + " " + ItemService.gI().getTemplate(mocdiem.i70_2).name + ", " + ItemService.gI().getTemplate(mocdiem.i70_3).name + ", \n"
//                            + " x1 " + ItemService.gI().getTemplate(mocdiem.i70_4).name + "\n"
//                            + "|5|Mốc 30000 điểm: "
//                            + " x" + mocdiem.sli100_1 + " " + ItemService.gI().getTemplate(mocdiem.i100_1).name + ",\n"
//                            + " x" + mocdiem.sli100_2 + " " + ItemService.gI().getTemplate(mocdiem.i100_2).name + ",\n"
//                            + " x" + mocdiem.sli100_3 + " " + ItemService.gI().getTemplate(mocdiem.i100_3).name + "\n"
//                            + "|7|Mốc 40000 điểm: "
//                            + " x" + mocdiem.sli200_1 + " " + ItemService.gI().getTemplate(mocdiem.i200_1).name + " " + mocdiem.csi200_1 + "% cs,\n"
//                            + " x" + mocdiem.sli200_2 + " " + ItemService.gI().getTemplate(mocdiem.i200_2).name + ",\n"
//                            + " x" + mocdiem.sli200_3 + " " + ItemService.gI().getTemplate(mocdiem.i200_3).name + "\n"
//                            + "|7|Mốc 50000 điểm: "
//                            + " x" + mocdiem.sli300_1 + " " + ItemService.gI().getTemplate(mocdiem.i300_1).name + ",\n"
//                            + "x" + mocdiem.sli300_2 + " " + ItemService.gI().getTemplate(mocdiem.i300_2).name + ",\n"
//                            + "x" + mocdiem.sli300_3 + " " + ItemService.gI().getTemplate(mocdiem.i300_3).name + "\n"
//                            + "\b|7|Mốc 100000 điểm: "
//                            + " x" + mocdiem.sli500_1 + " " + ItemService.gI().getTemplate(mocdiem.i500_1).name + " " + mocdiem.csi500_1 + "% cs, " + mocdiem.csi500_2 + "% sđ đẹp,\n"
//                            + " x" + mocdiem.sli500_2 + " " + ItemService.gI().getTemplate(mocdiem.i500_2).name + ",\n"
//                            + " x" + mocdiem.sli500_3 + " " + ItemService.gI().getTemplate(mocdiem.i500_3).name + ",\n"
//                            + " x" + mocdiem.sli500_4 + " " + ItemService.gI().getTemplate(mocdiem.i500_4).name + "\n"
//                    );
//                }
//                if (select == 2) {
//                    Archivement_diem.gI().getAchievement_mocsk20_10(player);
//                }
//            }
//            private void handleMainNhanQuaDiem(Player player, int select) {
//                switch (select) {
//                    case 0:
//                        Archivement_diem.gI().getAchievement_mocsk20_10(player);
//                        break;
//                }
//            }
//            private void handleMenuXemTopSKquylao2(Player player, int select) {
//                if (select == 0) {
//                    Service.gI().showListTop(player, Manager.topSKNEW);
//                }
//            }
            private void handleMainOptions(Player player, int select) {
                switch (select) {
//                    case 0:
//                        Item RuaCon = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 874);
//                        if (RuaCon != null) {
//                            if (RuaCon.quantity >= 1 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                int randomItem = Util.nextInt(0, 6); // Random giữa 0 và 1
//                                switch (randomItem) {
//                                    case 0: {
//                                        Item VatPham = ItemService.gI().createNewItem((short) (Util.nextInt(1211, 1222)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 12)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 12)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 12)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 12)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(30, Util.nextInt(0, 0)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 5)));
//                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
//                                        String npcSay = VatPham.template.name + "\n|2|";
//                                        npcSay += "Bạn đã nhận được " + VatPham.template.name;
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Ok");
//                                        break;
//                                    }
//                                    case 1: {
//
//                                        Item VatPham = ItemService.gI().createNewItem((short) (865));
//                                        VatPham.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 10)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(30, Util.nextInt(0, 0)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 5)));
//                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng " + VatPham.template.name, "Ok");
//                                        break;
//                                    }
//                                    case 2: {
//
//                                        Item VatPham = ItemService.gI().createNewItem((short) (Util.nextInt(1407, 1420)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 12)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 12)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 12)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 12)));
//                                        VatPham.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 5)));
//                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
//                                        String npcSay = VatPham.template.name + "\n|2|";
//                                        npcSay += "Bạn đã nhận được " + VatPham.template.name;
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Ok");
//                                        break;
//                                    }
//                                    case 3: {
//
//                                        Item VatPham = ItemService.gI().createNewItem((short) Util.nextInt(733, 735));
//                                        VatPham.itemOptions.add(new Item.ItemOption(84, 0));
//                                        VatPham.itemOptions.add(new Item.ItemOption(30, 0));
//                                        VatPham.itemOptions.add(new Item.ItemOption(93, 7));
//                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
//                                        String npcSay = VatPham.template.name + "\n|2|";
//                                        npcSay += "Bạn đã nhận được " + VatPham.template.name;
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Ok");
//                                        break;
//                                    }
//                                    case 4: {
//
//                                        Item VatPham = ItemService.gI().createNewItem((short) 733);
//                                        VatPham.itemOptions.add(new Item.ItemOption(84, 0));
//                                        VatPham.itemOptions.add(new Item.ItemOption(30, 0));
//                                        VatPham.itemOptions.add(new Item.ItemOption(93, 14));
//                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu " + VatPham.template.name, "Ok");
//                                        break;
//                                    }
//                                    default: {
//
//                                        Item VatPham = ItemService.gI().createNewItem((short) 18);
//                                        InventoryServiceNew.gI().addItemBag(player, VatPham);
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu " + VatPham.template.name, "Ok");
//                                        break;
//                                    }
//                                }
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, RuaCon, 1);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, " Con làm gì có rùa con mà đổi?");
//                        }
//                        break;

                    case 0:
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 1112,
                                    "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                    "Về khu\nvực bang", "Giải tán\nBang hội", "Kho Báu\ndưới biển");

                        }
                        break;

                    case 2:
                        if (player.getSession() != null) {
                            if (TaskService.gI().getIdTask(player) > ConstTask.TASK_19_0) {
                                Service.gI().sendThongBao(player, "Cày đê ê ee e e ê e e e e e e e e e ");
                                return;

                            }
                            TaskService.gI().sendNextTaskMain(player);
                            break;

                        }

                        break;
//                    case 3:
//                        if (ConstDataEvent.isRunningSK16) {
//                            if (player.getSession() != null) {
//
//                                this.createOtherMenu(player, 1113, "\b|3|Con muốn đổi đồ ăn nào?"
//                                        + "\n " + ConstDataEvent.slItem1_SK + " " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_1).name + " = 1 item " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_1_SK).name + " sự kiện (20%HP+KI)\" +"
//                                        + "\n " + ConstDataEvent.slItem2_SK + " " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_2).name + " = 1 item " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_2_SK).name + "  sự kiện (20% TNSM)\" +"
//                                        + "\n " + ConstDataEvent.slItem3_SK + " " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_3).name + "  = 1 item " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_3_SK).name + " sự kiện (20%sd)\" +"
//                                        + "\n Các Item có tác dụng 10p", "Đổi " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_1_SK).name, "Đổi " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_2_SK).name, "Đổi " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_3_SK).name);
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Đã kết thúc sự kiện");
//                        }
//                        break;
//                    case 4:
//                        if (ConstDataEvent.isRunningSK16) {
//                            String menu = "Sụ kiện đua top Quy Lão:"
//                                    + "\n|1|Bạn đang có: " + player.event.getEventPoint() + " điểm sự kiện"
//                                    + "\n|1|Bạn đang có: " + player.event.getEventPointQuyLao() + " điểm đua top"
//                                    + "\n|3|Thu thập " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name + " từ làm nhiệm để đua top"
//                                    + "\n|2|Item 1 đổi mùa này: " + ItemService.gI().getTemplate(NauBanhServices.banhtet).name
//                                    + "\n|2|Item 2 đổi mùa này: " + ItemService.gI().getTemplate(NauBanhServices.banhchung).name
//                                    + "\n|5|Bạn có: " + player.getSession().vnd + " COIN";
//                            String[] menuchoise = {
//                                "Đổi Item 1 ->\n 10 " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name,
//                                "Đổi Item 2 ->\n 10 " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name,
//                                "Xem Top\nSự Kiện",
//                                "Xem quà\nSự kiện",
//                                "Làm nhiệm vụ lấy \n" + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name,
//                                "Đổi " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name};
//                            if (player.getSession() != null) {
//                                this.createOtherMenu(player, 1114,
//                                        menu,
//                                        menuchoise
//                                );
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Đã kết thúc sự kiện");
//                        }
//                        switch (Manager.EVENT_SEVER) {
//                            case 1:
//                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
//                                        "Sự kiện Halloween chính thức tại Ngọc Rồng BLUE"
//                                        + "\n"
//                                        + "Chuẩn bị x10 nguyên liệu Kẹo, Bánh Quy, Bí ngô để đổi Giỏ Kẹo cho ta nhé\n"
//                                        + "Nguyên Liệu thu thập bằng cách đánh quái tại các hành tinh được chỉ định\n"
//                                        + "Tích lũy 3 Giỏ Kẹo +  3 Vé mang qua đây ta sẽ cho con 1 Hộp Ma Quỷ\n"
//                                        + "Tích lũy 3 Giỏ Kẹo, 3 Hộp Ma Quỷ + 3 Vé \nmang qua đây ta sẽ cho con 1 hộp quà thú vị.",
//                                        "Đổi\nGiỏ Kẹo", "Đổi Hộp\nMa Quỷ", "Đổi Hộp\nQuà Halloween",
//                                        "Từ chối");
//                                break;
//                            case 2:
////                                Attribute at = ServerManager.gI().getAttributeManager()
////                                        .find(ConstAttribute.VANG);
////                                String text = "Sự kiện 20/11 chính thức tại Ngọc Rồng "
////                                        + Manager.SERVER_NAME + "\n "
////                                        + "Số điểm hiện tại của bạn là : "
////                                        + player.event.getEventPoint()
////                                        + "\nTổng số hoa đã tặng trên toàn máy chủ "
////                                        + EVENT_COUNT_QUY_LAO_KAME % 999 + "/999";
////                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
////                                        at != null && !at.isExpired() ? text
////                                        + "\nToàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái,thời gian còn lại "
////                                        + at.getTime() / 60 + " phút."
////                                        : text + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được nhân đôi số vàng rơi ra từ quái trong 60 phút",
////                                        "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
////                                        "Đổi\nHộp quà");
//                                break;
//                            case 3:
//                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
//                                        "Sự kiên giáng sinh 2022 BLUE"
//                                        + "\nKhi đội mũ len bất kì đánh quái sẽ có cơ hội nhận được kẹo giáng sinh"
//                                        + "\nĐem 99 kẹo giáng sinh tới đây để đổi 1 Vớ,tất giáng sinh\nChúc bạn một mùa giáng sinh vui vẻ",
//                                        "Đổi\nTất giáng sinh");
//
//                                break;
//                            case 4:
//                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
//                                        "Sự kiên Tết nguyên đán 2024 BLUE"
//                                        + "\nBạn đang có: " + player.event.getEventPoint()
//                                        + " điểm sự kiện\nChúc bạn năm mới dui dẻ",
//                                        "Nhận Lìxì", "Đổi Điểm\nSự Kiện");
//                                break;
//                            case 5:
//                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
//                                        "Sự kiện 8/3 chính thức tại Ngọc Rồng BLUE"
//                                        + "\nBạn đang có: "
//                                        + player.event.getEventPoint()
//                                        + " điểm sự kiện\nChúc bạn chơi game dui dẻ",
//                                        "Tặng 1\n 1 nước hoa VIP", "Tặng\n10 nước hoa VIP", "Tặng\n99 nước hoa VIP",
//                                        "Đổi Capsule", "Shop điểm ", "Map up điểm");
//                                break;
//                        }
                    // break;

//                    case 5:
//                        createOtherMenu(player, 21111,
//                                "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
//                                "Vật phẩm", "Cải trang", "Pet", "Hàng hiếm");
//                        break;
                    case 1:
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 1115, "Nạp đạt mốc nhận quà he :3", "Xem quà mốc nạp", "Nhận quà mốc nạp", "Đóng");
                        }

                        break;

                }
            }

//            private void handleMainShopDiem(Player player, int select) {
//                switch (select) {
//                    case 0:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_DIEM_1", false);
//                        break;
//                    case 1:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_DIEM_2", false);
//                        break;
//                    case 2:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_DIEM_3", false);
//                        break;
//                    case 3:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_DIEM_4", false);
//                        break;
//
//                }
//            }
//            private void handleMainDoiItemSuKien(Player player, int select) {
//                String menu = "|2|Bạn muốn?\n"
//                        + "\n|4|Tùy chọn 1: Đổi "
//                        + "\n|1|Đổi COIN ra " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name
//                        + "\n|4|Tùy chọn 2: Đổi "
//                        + "\n|1|" + 1000 + " " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name + "\n ->100 " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name
//                        + "\n|4|Tùy chọn 3: Đổi "
//                        + "\n|1|" + 6000 + " " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name + "\n ->600 " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name //                        + "\n|4|Tùy chọn 4: Đổi" + 1000 + " " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name + "\n ->" + 1000 + " điểm"
//                        + "\n|4|Tùy chọn 4: Đổi "
//                        + "\n|1|" + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_7).name + " lấy điểm shop đổi";
//                String[] menuchoise = {
//                    "Tùy chọn 1",
//                    "Tùy chọn 2",
//                    "Tùy chọn 3",
//                    "Tùy chọn 4"
//                };
//                switch (select) {
//                    case 0:
////                        if (player.getSession().vnd < ConstDataEvent.giaDiemVND_SK) {
////                            Service.gI().sendThongBao(player, "Bạn cần nạp " + ConstDataEvent.giaDiemVND_SK);
////                            return;
////                        }
////                        if (PlayerDAO.subvnd(player, ConstDataEvent.giaDiemVND_SK)) {
////                            Item ID_RUONG_2 = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_6);
////                            ID_RUONG_2.quantity = ConstDataEvent.diemVND;
////                            InventoryServiceNew.gI().addItemBag(player, ID_RUONG_2);
////                            InventoryServiceNew.gI().sendItemBags(player);
////                            Service.gI().sendThongBao(player, "Bạn đã nhận được " + ID_RUONG_2.template.name);
////                        }
////                        if (player.getSession() != null) {
////                            createOtherMenu(player, 21112,
////                                    menu,
////                                    menuchoise);
////                        }
//                        Input.gI().createFormDoiItemQuyLao(player);
//                        break;
//                    case 1: {
//                        Item item = InventoryServiceNew.gI().findItemBag(player, ConstDataEvent.idItemDoiDiem);
//                        if (item != null) {
//                            if (item.isNullItem() && item.quantity < 1) {
//                                Service.gI().sendThongBao(player, "Con nghèo đến độ 1 " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name + " cũng không có luôn à! :)");
//                            } else if (item.isNotNullItem() && item.quantity >= 1) {
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
//                                Item ID_RUONG_2 = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_6);
//                                ID_RUONG_2.quantity = 1;
//                                InventoryServiceNew.gI().addItemBag(player, ID_RUONG_2);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.gI().sendThongBao(player, "Bạn đã nhận được " + ID_RUONG_2.template.name);
//                                if (player.getSession() != null) {
//                                    createOtherMenu(player, 21112,
//                                            menu,
//                                            menuchoise);
//                                }
//                            } else {
//                                Service.gI().sendThongBao(player, "Bạn không đủ " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name);
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Không tìm thấy " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name + " trong túi của bạn");
//                        }
//                    }
//                    break;
//                    case 2: {
//
//                        Item item = InventoryServiceNew.gI().findItemBag(player, ConstDataEvent.idItemDoiDiem);
//                        if (item != null) {
//                            if (item.isNullItem() && item.quantity < 1) {
//                                Service.gI().sendThongBao(player, "Con nghèo đến độ 1 " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name + " không có luôn à! :)");
//                            } else if (item.isNotNullItem() && item.quantity >= 6) {
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, item, 6);
//                                Item ID_RUONG_2 = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_6);
//                                ID_RUONG_2.quantity = 6;
//                                InventoryServiceNew.gI().addItemBag(player, ID_RUONG_2);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.gI().sendThongBao(player, "Bạn đã nhận được " + ID_RUONG_2.template.name);
//                                if (player.getSession() != null) {
//                                    createOtherMenu(player, 21112,
//                                            menu,
//                                            menuchoise);
//                                }
//                            } else {
//                                Service.gI().sendThongBao(player, "Bạn không đủ " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name);
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Không tìm thấy " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name + " trong túi của bạn");
//                        }
//
//                        break;
//
//                    }
//                    case 3:
//                        Input.gI().createFormDoiItem(player);
//
//                }
//            }
            private void handleMainTalk(Player player, int select) {
                switch (select) {
                    case 0:
                        if (player.getSession() != null && player.getSession().player.nPoint.power >= 80000000000L) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 842);
                        } else {
                            this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                        }
                        break;
                    case 1:
                        Clan clan = player.clan;
                        if (clan != null) {
                            ClanMember cm = clan.getClanMember((int) player.id);
                            if (cm != null) {
                                if (clan.members.size() > 1) {
                                    Service.gI().sendThongBao(player, "Bang phải còn một người");
                                    break;
                                }
                                if (!clan.isLeader(player)) {
                                    Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                    break;
                                }
                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                        "Đồng ý", "Từ chối!");
                            }
                            break;
                        }
                        Service.gI().sendThongBao(player, "bạn đã có bang hội đâu!!!");
                        break;
                    case 2:
                        if (player.clan == null) {
                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Vào bang hội trước", "Đóng");
                            return;
                        }
//                        int nPlSameClan = 0;
//                        for (Player pl : player.zone.getPlayers()) {
//                            if (!pl.equals(player) && pl.clan != null
//                                    && pl.clan.equals(player.clan) && pl.location.x >= 755
//                                    && pl.location.x <= 1310) {
//                                nPlSameClan++;
//                            }
//                        }
//                        if (nPlSameClan < BanDoKhoBau.N_PLAYER_MAP) {
//                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                    "Ngươi phải có ít nhất " + BanDoKhoBau.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
//                                    + "tuy nhiên ta khuyên ngươi nên đi cùng với " + BanDoKhoBau.N_PLAYER_MAP + " người nữa để khỏi chết.\n"
//                                    + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
//                            return;
//                        }
                        if (player.clan.getMembers().size() < BanDoKhoBau.N_PLAYER_CLAN) {
                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Bang hội phải có ít nhất " + BanDoKhoBau.N_PLAYER_CLAN + " thành viên mới có thể mở", "Đóng");
                            return;
                        }
                        if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Bản đồ kho báu chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                    "OK");
                            return;
                        }

                        if (player.bdkb_countPerDay >= 3) {
                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Con đã đạt giới hạn lượt đi trong ngày",
                                    "OK");
                            return;
                        }

                        player.clan.banDoKhoBau_haveGone = !(System.currentTimeMillis() - player.clan.banDoKhoBau_lastTimeOpen > 60000);
                        if (player.clan.banDoKhoBau_haveGone) {
                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Bang hội của con đã đi Bản Đồ lúc " + TimeUtil.formatTime(player.clan.banDoKhoBau_lastTimeOpen, "HH:mm:ss") + " hôm nay. Người mở\n"
                                    + "(" + player.clan.banDoKhoBau_playerOpen + "). Hẹn con sau 1 phút nữa", "OK");
                            return;
                        }
                        if (player.clan.banDoKhoBau != null) {
                            createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                    "Bang hội của con đang tham gia Bản đồ kho báu cấp độ " + player.clan.banDoKhoBau.level + "\n"
                                    + "Thời gian còn lại là "
                                    + TimeUtil.getSecondLeft(player.clan.banDoKhoBau.getLastTimeOpen(), BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000)
                                    + " giây. Con có muốn tham gia không?",
                                    "Tham gia", "Không");
                            return;
                        }
                        Input.gI().createFormChooseLevelBDKB(player);
                        break;

                }
            }

//            private void handleMainDoiDiem(Player player, int select) {
//                switch (select) {
//                    case 0:
//                        Item voOc = InventoryServiceNew.gI().findItemBag(player, ConstDataEvent.ID_ITEM_1);
//                        if (voOc != null) {
//                            if (voOc.quantity < ConstDataEvent.slItem1_SK) {
//                                Service.gI().sendThongBao(player, "Bạn không đủ: " + voOc.template.name);
//                                return;
//                            }
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, voOc, ConstDataEvent.slItem1_SK);
//                            Item voOcSK = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_1_SK);
//                            InventoryServiceNew.gI().addItemBag(player, voOcSK);
//                            InventoryServiceNew.gI().sendItemBags(player);
//                            Service.gI().sendThongBao(player, "Bạn đã nhận được: " + voOcSK.template.name);
//
//                        } else {
//                            Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_1).name);
//                        }
//                        break;
//                    case 1:
//                        Item saobien = InventoryServiceNew.gI().findItemBag(player, ConstDataEvent.ID_ITEM_2);
//                        if (saobien != null) {
//                            if (saobien.quantity < ConstDataEvent.slItem2_SK) {
//                                Service.gI().sendThongBao(player, "Bạn không đủ: " + saobien.template.name);
//                                return;
//                            }
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, saobien, ConstDataEvent.slItem2_SK);
//                            Item saoBienSK = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_2_SK);
//                            InventoryServiceNew.gI().addItemBag(player, saoBienSK);
//                            InventoryServiceNew.gI().sendItemBags(player);
//                            Service.gI().sendThongBao(player, "Bạn vừa đổi được: " + saoBienSK.template.name);
//
//                        } else {
//
//                            Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_2).name);
//                        }
//                        break;
//                    case 2:
//                        Item concua = InventoryServiceNew.gI().findItemBag(player, ConstDataEvent.ID_ITEM_3);
//                        if (concua != null) {
//                            if (concua.quantity < ConstDataEvent.slItem3_SK) {
//                                Service.gI().sendThongBao(player, "Bạn không đủ " + concua.template.name);
//                                return;
//                            }
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, concua, ConstDataEvent.slItem3_SK);
//                            Item saoBienSK = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_3_SK);
//                            InventoryServiceNew.gI().addItemBag(player, saoBienSK);
//                            InventoryServiceNew.gI().sendItemBags(player);
//                            Service.gI().sendThongBao(player, "Bạn vừa đổi được: " + saoBienSK.template.name);
//
//                        } else {
//                            Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_3).name);
//                        }
//                        break;
//
//                }
//            }
//            private void handleMainSKquylao2(Player player, int select) {
//                String menu = "Sụ kiện đua top Quy Lão:"
//                        + "\n|1|Bạn đang có: " + player.event.getEventPoint() + " điểm sự kiện"
//                        + "\n|1|Bạn đang có: " + player.event.getEventPointQuyLao() + " điểm đua top"
//                        + "\n|3|Thu thập " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name + " từ làm nhiệm để đua top"
//                        + "\n|7|Item 1 đổi mùa này " + ItemService.gI().getTemplate(NauBanhServices.banhtet).name
//                        + "\n|7|Item 2 đổi mùa này " + ItemService.gI().getTemplate(NauBanhServices.banhchung).name
//                        + "\nBạn có: " + player.getSession().vnd + " COIN";
//                String[] menuchoise = {
//                    "Đổi Item 1 ->\n 10" + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name,
//                    "Đổi Item 2 ->\n 10" + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name,
//                    "Xem Top\nSự Kiện",
//                    "Xem quà\nSự kiện",
//                    "Làm nhiệm vụ lấy \n" + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name,
//                    "Đổi " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name};
//                switch (select) {
//                    case 0:
//                        Item concua = InventoryServiceNew.gI().findItemBag(player, NauBanhServices.banhtet);
//                        if (concua != null) {
//                            int sl = 1;
//                            if (concua.quantity < sl) {
//                                Service.gI().sendThongBao(player, "Không đủ: " + concua.template.name);
//                                return;
//                            }
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, concua, sl);
//                            Item ID_RUONG_1 = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_6);
//                            ID_RUONG_1.quantity = 10;
//                            InventoryServiceNew.gI().addItemBag(player, ID_RUONG_1);
//                            InventoryServiceNew.gI().sendItemBags(player);
//                            Service.gI().sendThongBao(player, "Bạn đã nhận được " + ID_RUONG_1.template.name);
//                            if (player.getSession() != null) {
//                                this.createOtherMenu(player, 1114,
//                                        menu,
//                                        menuchoise);
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Đổi cần: " + ItemService.gI().getTemplate(NauBanhServices.banhtet).name + "");
//                        }
//                        break;
//                    case 1:
//                        Item concua2 = InventoryServiceNew.gI().findItemBag(player, NauBanhServices.banhchung);
//                        if (concua2 != null) {
//                            int sl = 1;
//                            if (concua2.quantity < sl) {
//                                Service.gI().sendThongBao(player, "Không đủ: " + concua2.template.name);
//                                return;
//                            }
//                            InventoryServiceNew.gI().subQuantityItemsBag(player, concua2, sl);
//                            Item ID_RUONG_2 = ItemService.gI().createNewItem((short) ConstDataEvent.ID_ITEM_6);
//                            ID_RUONG_2.quantity = 10;
//                            InventoryServiceNew.gI().addItemBag(player, ID_RUONG_2);
//                            InventoryServiceNew.gI().sendItemBags(player);
//                            Service.gI().sendThongBao(player, "Bạn đã nhận được " + ID_RUONG_2.template.name);
//                            if (player.getSession() != null) {
//                                this.createOtherMenu(player, 1114,
//                                        menu,
//                                        menuchoise);
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Đổi cần: " + ItemService.gI().getTemplate(NauBanhServices.banhchung).name + "");
//                        }
//                        break;
//                    case 2:
//                        if (player.getSession() != null) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU_QUY_LAO, "|2|Top Sự Kiện", "Xem TOP", "Xem quà", "Nhận quà");
//                        }
//
//                        break;
//
//                    case 3:
//                        Service.gI().sendBigMessage(player, avartar, Arrays.toString(ConstDataEvent.Textskquylao));
//                        break;
//                    case 4:
////                        ChangeMapService.gI().changeMap(player, 202, 0, 735, 456);
//
//                        int[] rw = {ConstTask.GOLD_EASY, ConstTask.GOLD_NORMAL, ConstTask.GOLD_HARD, ConstTask.GOLD_VERY_HARD, ConstTask.GOLD_HELL};
//
//                        if (player.playerTask.sideTask.template != null) {
//                            String npcSay = "|4|Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " (" + player.playerTask.sideTask.getLevel() + ")"
//                                    + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/" + player.playerTask.sideTask.maxCount + " (" + player.playerTask.sideTask.getPercentProcess() + "%)"
//                                    + "\nSố nhiệm vụ còn lại trong ngày: " + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK
//                                    + "\n|5|Phần thưởng: " + rw[player.playerTask.sideTask.level] + " " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name;
//                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
//                                    npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
//                        } else {
//                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
//                                    "Làm nhiệm vụ sự kiện để thu thập " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name
//                                    + "\nsSức cậu có thể làm được cái nào?",
//                                    "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
//                        }
//
//                        break;
//                    case 5:
//                        Item item = InventoryServiceNew.gI().findItemBag(player, ConstDataEvent.ID_ITEM_6);
//                        if (item == null) {
//                            Service.gI().sendThongBao(player, "Bạn không có " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name + " để đổi");
//                        } else {
//                            createOtherMenu(player, 21112,
//                                    "|2|Bạn muốn?\n"
//                                    + "\n|4|Tùy chọn 1: Đổi "
//                                    + "\n|1|Đổi COIN ra " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name
//                                    + "\n|4|Tùy chọn 2: Đổi "
//                                    + "\n|1|" + 1 + " " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name + "\n ->1 " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name
//                                    + "\n|4|Tùy chọn 3: Đổi "
//                                    + "\n|1|" + 6 + " " + ItemService.gI().getTemplate(ConstDataEvent.idItemDoiDiem).name + "\n ->6 " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name //                        + "\n|4|Tùy chọn 4: Đổi" + 1000 + " " + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_6).name + "\n ->" + 1000 + " điểm"
//                                    + "\n|4|Tùy chọn 4: Đổi "
//                                    + "\n|1|" + ItemService.gI().getTemplate(ConstDataEvent.ID_ITEM_7).name + " lấy điểm shop đổi",
//                                    "Tùy chọn 1",
//                                    "Tùy chọn 2",
//                                    "Tùy chọn 3",
//                                    "Tùy chọn 4"
//                            //                                    "Tùy chọn 4"
//                            );
//                        }
//
//                        break;
//                }
//            }
            private void handleMainMocNap(Player player, int select) {
                switch (select) {
                    case 0:
                        if (player.getSession() != null) {
//                            this.createOtherMenu(player, 22222, Arrays.toString(Archivement.Textmocnap), "Đóng");
                            Service.gI().sendThongBaoFromAdmin(player, "____Reset mỗi thứ 2 đầu tuần____\n"
                                    + "|3|• Mốc " + Archivement.GIADOLACHIADOI * 1 + ":\n"
                                    + " - x" + mocnap_1.sli10_1 + " " + ItemService.gI().getTemplate(mocnap_1.i10_1).name + ",\n"
                                    + "|2|• Mốc " + Archivement.GIADOLACHIADOI * 2 + ":\n"
                                    + " - x" + mocnap_1.sli20_1 + " " + ItemService.gI().getTemplate(mocnap_1.i20_1).name + ",\n"
                                    + " - x" + mocnap_1.sli20_2 + " " + ItemService.gI().getTemplate(mocnap_1.i20_2).name + "\n"
                                    + "|1|• Mốc " + Archivement.GIADOLACHIADOI * 3 + ":\n"
                                    + " - x" + mocnap_1.sli30_1 + " " + ItemService.gI().getTemplate(mocnap_1.i30_1).name + " % cs, \n"
                                    + " - x" + mocnap_1.sli30_2 + " " + ItemService.gI().getTemplate(mocnap_1.i30_2).name + "\n"
                                    + "|4|• Mốc " + Archivement.GIADOLACHIADOI * 5 + ":\n"
                                    + " - x" + mocnap_1.sli50_1 + " " + ItemService.gI().getTemplate(mocnap_1.i50_1).name + ", "
                                    + ItemService.gI().getTemplate(mocnap_1.i50_2).name + " \n"
                                    + "|6|• Mốc " + Archivement.GIADOLACHIADOI * 7 + ":\n"
                                    + " - x" + mocnap_1.sli70_1 + " " + ItemService.gI().getTemplate(mocnap_1.i70_1).name + " " + mocnap_1.csi70_1 + "% cs, \n"
                                    + " - x" + mocnap_1.sli70_2 + " " + ItemService.gI().getTemplate(mocnap_1.i70_2).name + ", " + ItemService.gI().getTemplate(mocnap_1.i70_3).name + ", " + ItemService.gI().getTemplate(mocnap_1.i70_4).name + "\n"
                                    + "|5|• Mốc " + Archivement.GIADOLACHIADOI * 10 + ":\n"
                                    + " - x" + mocnap_1.sli100_1 + " " + ItemService.gI().getTemplate(mocnap_1.i100_1).name + ",\n"
                                    + " - x" + mocnap_1.sli100_2 + " " + ItemService.gI().getTemplate(mocnap_1.i100_2).name + ",\n"
                                    + " - x" + mocnap_1.sli100_3 + " " + ItemService.gI().getTemplate(mocnap_1.i100_3).name + "\n"
                                    + "|3|• Mốc " + Archivement.GIADOLACHIADOI * 20 + ":\n"
                                    + " - x" + mocnap_1.sli200_1 + " " + ItemService.gI().getTemplate(mocnap_1.i200_1).name + ",\n"
                                    + " - x" + mocnap_1.sli200_3 + " " + ItemService.gI().getTemplate(mocnap_1.i200_3).name + ",\n"
                                    + " - x" + mocnap_1.sli200_2 + " " + ItemService.gI().getTemplate(mocnap_1.i200_2).name + " " + mocnap_1.csi200_2 + "\n"
                                    + "|4|• Mốc " + Archivement.GIADOLACHIADOI * 30 + ":\n"
                                    + " - x" + mocnap_1.sli300_1 + " " + ItemService.gI().getTemplate(mocnap_1.i300_1).name + ",\n"
                                    + " - x" + mocnap_1.sli300_2 + " " + ItemService.gI().getTemplate(mocnap_1.i300_2).name + ",\n"
                                    + " - x" + mocnap_1.sli300_3 + " " + ItemService.gI().getTemplate(mocnap_1.i300_3).name + "\n"
                                    + "|5|• Mốc " + Archivement.GIADOLACHIADOI * 50 + ":\n"
                                    + " - x" + mocnap_1.sli500_1 + " " + ItemService.gI().getTemplate(mocnap_1.i500_1).name + " " + mocnap_1.sdi500_1 + " cs, \n"// + mocnap_1.csi500_2 + " sức đánh đẹp\n"
                                    + " - x" + mocnap_1.sli500_2 + " " + ItemService.gI().getTemplate(mocnap_1.i500_2).name + " , x" + mocnap_1.sli500_3 + " " + ItemService.gI().getTemplate(mocnap_1.i500_3).name + ",\n"
                                    + " - x" + mocnap_1.sli500_4 + " " + ItemService.gI().getTemplate(mocnap_1.i500_4).name + " " + mocnap_1.csi500_4 + "\n"
                                    + "|6|• Mốc " + Archivement.GIADOLACHIADOI * 100 + ":\n"
                                    + " - x" + mocnap_1.sli1000_1 + " " + ItemService.gI().getTemplate(mocnap_1.i1000_1).name + ",\n"
                                    + " - x" + mocnap_1.sli1000_2 + " " + ItemService.gI().getTemplate(mocnap_1.i1000_2).name + ",\n"
                                    + " - x" + mocnap_1.sli1000_3 + " " + ItemService.gI().getTemplate(mocnap_1.i1000_3).name + " DAME-HP-KI -> " + mocnap_1.sdi1000_3 + "-" + mocnap_1.hpi1000_3 + "-" + mocnap_1.kii1000_3 + "(%)\n"
                                    + " - x" + mocnap_1.sli1000_4 + " " + ItemService.gI().getTemplate(mocnap_1.i1000_4).name + ",\n"
                                    + " - x" + mocnap_1.sli1000_5 + " " + ItemService.gI().getTemplate(mocnap_1.i1000_5).name + "\n"
                                    + "|1|• Mốc " + Archivement.GIADOLACHIADOI * 200 + ":\n"
                                    + " - x" + mocnap_1.sli2000_1 + " " + ItemService.gI().getTemplate(mocnap_1.i2000_1).name + ",\n"
                                    + " - x" + mocnap_1.sli2000_2 + " " + ItemService.gI().getTemplate(mocnap_1.i2000_2).name + ",\n"
                                    + " - x" + mocnap_1.sli2000_3 + " " + ItemService.gI().getTemplate(mocnap_1.i2000_3).name + " DAME-HP-KI-SDCM -> " + mocnap_1.sdi2000_3 + "-" + mocnap_1.hpi2000_3 + "-" + mocnap_1.kii2000_3 + "-" + mocnap_1.sdcmi2000_3 + "(%)\n"
                                    + " - x" + mocnap_1.sli2000_4 + " " + ItemService.gI().getTemplate(mocnap_1.i2000_4).name + ",\n"
                                    + " - x" + mocnap_1.sli2000_5 + " " + ItemService.gI().getTemplate(mocnap_1.i2000_5).name + "\n"
                                    + "|1|• Mốc " + Archivement.GIADOLACHIADOI * 300 + ":\n"
                                    + " - x" + mocnap_1.sli3000_1 + " " + ItemService.gI().getTemplate(mocnap_1.i3000_1).name + ",\n"
                                    + " - x" + mocnap_1.sli3000_2 + " " + ItemService.gI().getTemplate(mocnap_1.i3000_2).name + ",\n"
                                    + " - x" + mocnap_1.sli3000_3 + " " + ItemService.gI().getTemplate(mocnap_1.i3000_3).name + ",\n" //+ mocnap_1.hpi3000_3 + "-" + mocnap_1.kii3000_3 + "-" + mocnap_1.sdcmi3000_3 + "(%)\n"
                                    + " - x" + mocnap_1.sli3000_4 + " " + ItemService.gI().getTemplate(mocnap_1.i3000_4).name + ",\n"
                                    + " - x" + mocnap_1.sli3000_5 + " " + ItemService.gI().getTemplate(mocnap_1.i3000_5).name + ",\n"
                                    + " - x" + mocnap_1.sli3000_6 + " " + ItemService.gI().getTemplate(mocnap_1.i3000_6).name + ",\n"
                                    + " - x" + mocnap_1.sli3000_7 + " " + ItemService.gI().getTemplate(mocnap_1.i3000_7).name + ",\n"
                                    + " - x" + mocnap_1.sli3000_8 + " " + ItemService.gI().getTemplate(mocnap_1.i3000_8).name + "\n"
                                    + "|1|• Mốc " + Archivement.GIADOLACHIADOI * 500 + ":\n"
                                    + " - x" + mocnap_1.sli5000_1 + " " + ItemService.gI().getTemplate(mocnap_1.i5000_1).name + ",\n"
                                    + " - x" + mocnap_1.sli5000_2 + " " + ItemService.gI().getTemplate(mocnap_1.i5000_2).name + ",\n"
                                    + " - x" + mocnap_1.sli5000_3 + " " + ItemService.gI().getTemplate(mocnap_1.i5000_3).name + " DAME-HP-KI-SDCM-SDĐẸP -> " + mocnap_1.sdi5000_3 + "-" + mocnap_1.hpi5000_3 + "-" + mocnap_1.kii5000_3 + "-" + mocnap_1.sdcmi5000_3 + "-" + mocnap_1.sddepi5000_3 + "(%)\n"
                                    + " - x" + mocnap_1.sli5000_4 + " " + ItemService.gI().getTemplate(mocnap_1.i5000_4).name + ",\n"
                                    + " - x" + mocnap_1.sli5000_5 + " " + ItemService.gI().getTemplate(mocnap_1.i5000_5).name + "\n"
                                    + " - x" + mocnap_1.sli5000_6 + " " + ItemService.gI().getTemplate(mocnap_1.i5000_6).name + ",\n"
                                    + " - x" + mocnap_1.sli5000_7 + " " + ItemService.gI().getTemplate(mocnap_1.i5000_7).name + ",\n"
                                    + " - x" + mocnap_1.sli5000_8 + " " + ItemService.gI().getTemplate(mocnap_1.i5000_8).name + "\n"
                                    + "|1|• Mốc " + Archivement.GIADOLACHIADOI * 700 + ":\n"
                                    + " - x" + mocnap_1.sli7000_1 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_1).name + ",\n"
                                    + " - x" + mocnap_1.sli7000_2 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_2).name + ",\n"
                                    //                                    + " - x" + mocnap_1.sli7000_3 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_3).name + " DAME-SDCM-SDĐẸP -> " + mocnap_1.sdi7000_3 + "-" + mocnap_1.sdcmi7000_3 + "-" + mocnap_1.sddepi7000_3 + "(%)\n"
                                    + " - x" + mocnap_1.sli7000_3 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_3).name + " DAME-HP_KI -> " + mocnap_1.sdi7000_3 + "-" + mocnap_1.sdi7000_3 + "-" + mocnap_1.sdi7000_3 + "(%)\n"
                                    + " - x" + mocnap_1.sli7000_4 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_4).name + ",\n"
                                    + " - x" + mocnap_1.sli7000_5 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_5).name + "\n"
                                    + " - x" + mocnap_1.sli7000_6 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_6).name + ",\n"
                                    + " - x" + mocnap_1.sli7000_7 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_7).name + ",\n"
                                    + " - x" + mocnap_1.sli7000_8 + " " + ItemService.gI().getTemplate(mocnap_1.i7000_8).name + "\n"
                            );

                        }
//

                        break;
                    case 1:
                        if (player.getSession().actived) {
                            Archivement.gI().getAchievement(player);
                        } else {
                            Service.gI().sendThongBao(player, "Mở thành viên tại bardock đi rồi qua đây nhận nhe baby!");
                        }
                        break;
                    case 2:
                        break;
                }
            }

            private void handleMenuBDKB(Player player, int select) {
                if (select == 0) {
                    BanDoKhoBauService.gI().joinBDKB(player);
                }
            }

            private void handleOpenBDKB(Player player, int select) {
                if (select == 0) {
                    Object level = PLAYERID_OBJECT.get(player.id);
                    BanDoKhoBauService.gI().openBDKB(player, (int) level);
                }
            }
        };
    }

//    private static Npc thodaica(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            public void chatWithNpc(Player player) {
//                String[] chat = {
//                    "Chào mừng tới với NR EMTI",
//                    "Qua npc Bardock để đổi VND",
//                    "Qua npc Fide để lấy pet ha ><",
//                    "Mang cho ta 10 củ cà rốt khi săn boss để đổi quà"
//
//                };
//                Timer timer = new Timer();
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    int index = 0;
//
//                    @Override
//                    public void run() {
//                        npcChat(player, chat[index]);
//                        index = (index + 1) % chat.length;
//                    }
//                }, 10000, 10000);
//            }
//
//            @Override
//            public void openBaseMenu(Player player) {
//                //chatWithNpc(player);
//                Item carot = InventoryServiceNew.gI().findItemBag(player, cn.cr);
//                if (canOpenNpc(player)) {
//                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
//                        if (player.getSession() != null) {
//                            if (carot != null && carot.quantity >= cn.check_sl_cr) {
//                                this.createOtherMenu(player, 12, "\b|2|Kiếm đủ " + ItemService.gI().getTemplate(cn.cr).name + " rồi à ? ta sẽ cho ngươi phần thưởng !",
//                                        "Nhận Quà", "Đóng");
//                            } else {
//                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                        "\b|3|Muốn rèn thỏi vàng khóa sang thường hả ?"
//                                        + "Khi rèn để hạn chế việc rèn bị hụt, bạn nên rèn nhỏ giọt thỏi vàng lại.\n"
//                                        + "Rèn xong thỏi vàng sẽ được gửi vô hòm thư\n"
//                                        + "Lưu ý đặc biệt: khi nhận thỏi vàng khi rèn thành công ở hòm thư tuyệt đối không để xót thỏi vàng khoá ở hành trang\n",
//                                        "Rèn Vàng Khóa", "ĐÓNG");
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (player.iDMark.getIndexMenu() == 12) {
//                        switch (select) {
//                            case 0:
//                                if (player.getSession() != null) {
//                                    this.createOtherMenu(player, 5,
//                                            "Cảm ơn cậu đã cứu bụng đói của ta\n Để cảm ơn ta sẽ tặng cậu món quà.",
//                                            "Đổi " + cn.slnx + "\n Ngọc Xanh",
//                                            "Đổi " + cn.slnh + " Hồng Ngọc",
//                                            "Đổi " + cn.slbd + " Bản Đồ Kho Báu",
//                                            "Đổi " + cn.slthoiVang_ + "\n thỏi vàng");
//
//                                }
//                                break;
//                            case 1:
//                                break;
//
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 5) {
//                        switch (select) {
//                            case 0:
//
//                                //if (!player.gift.gemTanThu) { 
//                                if (player.getSession() != null) {
//                                    Item carot = InventoryServiceNew.gI().findItemBag(player, cn.cr);
//                                    player.inventory.gem += cn.slnx;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, carot, cn.get_sl_cr);
//                                    Service.gI().sendMoney(player);
//
//                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được " + cn.slnx + " ngọc xanh");
//                                    player.gift.gemTanThu = true;
//                                }
//                                break;
//
//                            case 1:
//                                if (player.getSession() != null) {
//                                    Item carot = InventoryServiceNew.gI().findItemBag(player, cn.cr);
//                                    player.inventory.ruby += cn.slnh;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, carot, cn.get_sl_cr);
//                                    Service.gI().sendMoney(player);
//                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được " + cn.slnh + " hồng ngọc");
//                                    player.gift.gemTanThu = true;
//                                }
//                                break;
//                            case 2:
//                                if (player.getSession() != null) {
//                                    Item carot = InventoryServiceNew.gI().findItemBag(player, cn.cr);
//                                    Item bdkb = ItemService.gI().createNewItem((short) 611, (short) cn.slbd);
//
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, carot, cn.get_sl_cr);
//                                    InventoryServiceNew.gI().addItemBag(player, bdkb);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.slbd + " Bản Đồ Kho Báu");
//                                }
//                                break;
//                            case 3:
//                                if (player.getSession() != null) {
//                                    Item carot = InventoryServiceNew.gI().findItemBag(player, cn.cr);
//                                    Item bdkb = ItemService.gI().createNewItem((short) 457, (short) cn.slthoiVang_);
//                                    bdkb.itemOptions.add(new Item.ItemOption(93, 10));
//                                    bdkb.itemOptions.add(new Item.ItemOption(30, 1));
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, carot, cn.get_sl_cr);
//                                    InventoryServiceNew.gI().addItemBag(player, bdkb);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.slthoiVang_ + " Thỏi Vàng");
//                                }
//                                break;
//
//                        }
//
//                    }
//                    if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
//                        switch (select) {
//                            case 0:
//                                if (!player.getSession().actived) {
//                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                                } else {
//
//                                    Input.gI().DOITHOI(player);
//                                }
//                                break;
//                            case 1:
//                                break;
//                        }
//
////                    } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_THE) { 
////                        Input.gI().createFormNapThe(player, loaiThe, menhGia);
////            
//                    }
//                }
//            }
//        };
//    }
//    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
//                        super.openBaseMenu(player);
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                canOpenNpc(player);
//            }
//        };
//    }
    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                canOpenNpc(player);
            }
        };
    }

    // su kien nau banh 
//    private static String getSLBanhChungTet(Player player) {
//        if (NauBanhServices.banhChungBanhTetMaps.containsKey(player.id)) {
//            int tongSLBanh = NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhChung + NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhTet;
//            return String.valueOf(tongSLBanh);
//        }
//        return "0";
//    }
//    public static Npc noibanhChungBanhTet(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                ConstDataEvent.slBanhTrongNoi = NauBanhServices.getTotal();
//                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
//                    PlayerService.gI().banPlayer((player));
//                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
//                    return;
//                }
//                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 5) {
//                    if (ConstDataEvent.thoiGianNauBanh == -999999) {
//                        this.createOtherMenu(player, ConstNpc.CB_NAU_BANH, "Nồi nấu nguyên liệu toàn server\n Đang trong thời gian lấy bánh chín, và cho nguyên liệu vào để nấu tiếp"
//                                + "\nThời gian chuẩn bị để nấu tiếp còn " + ((NauBanhServices.timeCBNau - System.currentTimeMillis()) / 1000)
//                                + "\nToan Server đang có " + ConstDataEvent.slBanhTrongNoi + " đang chuẩn bị nấu "
//                                + "\nTrong đó mày có " + getSLBanhChungTet(player) + " đang chuẩn bị nấu",
//                                //tab menu
//                                "Nấu " + ItemService.gI().getTemplate(NauBanhServices.banhchung).name,
//                                "Nấu " + ItemService.gI().getTemplate(NauBanhServices.banhtet).name,
//                                "Hướng dẫn");
//
//                    } else if (ConstDataEvent.thoiGianNauBanh == 0) {
//                        this.createOtherMenu(player, ConstNpc.BANH_CHIN, "Nguyên liệu đã chín, Mày có 5 phút để lấy", "Lấy ngay", "Hướng dẫn");
//                    } else {
//                        this.createOtherMenu(player, ConstNpc.NAU_BANH, "Nồi nấu nguyên liệu toàn server\n Thời gian nấu còn " + ConstDataEvent.thoiGianNauBanh / 1000
//                                + "\nSố bánh đang nấu " + ConstDataEvent.slBanhTrongNoi
//                                + "\nMức nước trong nồi " + ConstDataEvent.mucNuocTrongNoi + "/" + ConstDataEvent.slBanhTrongNoi + " đang chuẩn bị nấu "
//                                + "\nTrong đó có " + getSLBanhChungTet(player) + " Nguyên liệu của bạn đang nấu\n Chơm đủ nước để nồi không bị cháy và nhận đủ số Nguyên liệu đã nấu"
//                                + "\nThêm " + ItemService.gI().getTemplate(NauBanhServices.cuilua).name + " để tăng tốc thời gian nấu bánh",
//                                //Tab menu
//                                "Thêm " + ItemService.gI().getTemplate(NauBanhServices.binhnuoc).name,
//                                "Thêm " + ItemService.gI().getTemplate(NauBanhServices.cuilua).name,
//                                "Hướng dẫn");
//
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
//                        if (player.iDMark.getIndexMenu() == ConstNpc.CB_NAU_BANH) {
//                            switch (select) {
//                                case 0:
//                                    this.createOtherMenu(player, ConstNpc.NAU_BANH_CHUNG,
//                                            "\b|4|" + ItemService.gI().getTemplate(NauBanhServices.banhchung).name + ": \n"
//                                            + "\n|2|10 " + ItemService.gI().getTemplate(NauBanhServices.item1).name + ", \n"
//                                            + "10 " + ItemService.gI().getTemplate(NauBanhServices.item2).name + ", \n"
//                                            + "10 " + ItemService.gI().getTemplate(NauBanhServices.item3).name + ", \n"
//                                            + "10 " + ItemService.gI().getTemplate(NauBanhServices.item4).name + ", \n"
//                                            + "10 " + ItemService.gI().getTemplate(NauBanhServices.item5).name + " \n"
//                                            + "và 03 " + ItemService.gI().getTemplate(NauBanhServices.binhnuoc).name + " để nấu.",
//                                            "Nấu", "Đóng");
//                                    break;
//                                case 1:
//                                    this.createOtherMenu(player, ConstNpc.NAU_BANH_TET,
//                                            "\b|3|" + ItemService.gI().getTemplate(NauBanhServices.banhtet).name + ": \n"
//                                            + "\n|2|10 " + ItemService.gI().getTemplate(NauBanhServices.item1).name + ", \n"
//                                            + "10 " + ItemService.gI().getTemplate(NauBanhServices.item2).name + ", \n"
//                                            + "10 " + ItemService.gI().getTemplate(NauBanhServices.item3).name + ", \n"
//                                            + "10 " + ItemService.gI().getTemplate(NauBanhServices.item4).name + ", \n"
//                                            + "10 " + ItemService.gI().getTemplate(NauBanhServices.item5).name + " \n"
//                                            + "và 03 " + ItemService.gI().getTemplate(NauBanhServices.binhnuoc).name + " để nấu.",
//                                            "Nấu", "Đóng");
//                                    break;
//                                case 2:
//                                    if (player.getSession() != null) {
//                                        this.createOtherMenu(player, 22222, Arrays.toString(ConstDataEvent.Huongdannaubanh), "Đóng");
//                                    }
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NAU_BANH_CHUNG) {
//                            if (select == 0) {
//                                Input.gI().createFormNauBanhChung(player);
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NAU_BANH_TET) {
//                            if (select == 0) {
//                                Input.gI().createFormNauBanhTet(player);
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.BANH_CHIN) {
//                            switch (select) {
//                                case 0:
//                                    if (!NauBanhServices.banhChungBanhTetMaps.containsKey(player.id)) {
//                                        Service.gI().sendThongBao(player, "Có đâu mà nhận");
//                                        return;
//                                    }
//                                    if (NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhTet == 0 && NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhChung == 0) {
//                                        Service.gI().sendThongBao(player, "Có đâu mà nhận");
//                                        return;
//                                    }
//                                    if (NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhTet != 0) {
//                                        Item banhTet = ItemService.gI().createNewItem((short) NauBanhServices.banhtet, NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhTet);
//                                        InventoryServiceNew.gI().addItemBag(player, banhTet);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn đã nhận được " + banhTet.template.name);
//                                        player.slBanhTet = 0;
//                                        NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhTet = 0;
//                                    }
//                                    if (NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhChung != 0) {
//                                        Item banhChung = ItemService.gI().createNewItem((short) NauBanhServices.banhchung, NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhChung);
//                                        InventoryServiceNew.gI().addItemBag(player, banhChung);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn đã nhận được " + banhChung.template.name);
//                                        player.slBanhChung = 0;
//                                        NauBanhServices.banhChungBanhTetMaps.get(player.id).slBanhChung = 0;
//                                    }
//                                    break;
//                                case 1:
//                                    if (player.getSession() != null) {
//                                        this.createOtherMenu(player, 22222, Arrays.toString(ConstDataEvent.Huongdannaubanh), "Đóng");
//                                    }
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NAU_BANH) {
//                            switch (select) {
//                                case 0:
//                                    if (ConstDataEvent.mucNuocTrongNoi < ConstDataEvent.slBanhTrongNoi) {
//                                        Item nuocNau = InventoryServiceNew.gI().findItemBag(player, NauBanhServices.binhnuoc);
//                                        if (nuocNau == null) {
//                                            Service.gI().sendThongBao(player, "Có " + ItemService.gI().getTemplate(NauBanhServices.binhnuoc).name + " đâu cu");
//                                            return;
//                                        }
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, nuocNau, 1);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        ConstDataEvent.mucNuocTrongNoi++;
//                                        Service.gI().sendThongBao(player, "Thêm " + ItemService.gI().getTemplate(NauBanhServices.binhnuoc).name + " vô nồi thành công");
//
//                                    } else {
//                                        Service.gI().sendThongBao(player, "Đủ " + ItemService.gI().getTemplate(NauBanhServices.binhnuoc).name + " rồi cu");
//                                    }
//                                    //                                        this.createOtherMenu(player, ConstNpc.NAU_BANH, "Nồi nấu bánh toàn server\n Thời gian nấu còn " + ConstDataEvent.thoiGianNauBanh / 1000
//                                    //                                                + "\nSố bánh đang nấu " + ConstDataEvent.slBanhTrongNoi
//                                    //                                                + "\nMức nước trong nồi " + ConstDataEvent.mucNuocTrongNoi + "/" + ConstDataEvent.slBanhTrongNoi + " đang chuẩn bị nấu "
//                                    //                                                + "\nTrong đó có " + getSLBanhChungTet(player) + " bánh của bạn đang nấu\n Chơm đủ nước để nồi không bị cháy và nhận đủ số bánh nấu"
//                                    //                                                + "\nThêm " + ItemService.gI().getTemplate(NauBanhServices.cuilua).name + " để tăng tốc thời gian nấu bánh",
//                                    //                                                //Tab menu
//                                    //                                                "Thêm " + ItemService.gI().getTemplate(NauBanhServices.binhnuoc).name + "",
//                                    //                                                "Thêm " + ItemService.gI().getTemplate(NauBanhServices.cuilua).name + "",
//                                    //                                                "Hướng dẫn");
//
//                                    return;
//                                case 1:
//                                    if (ConstDataEvent.thoiGianNauBanh <= 0) {
//                                        return;
//                                    } else {
//                                        Item cuiLua = InventoryServiceNew.gI().findItemBag(player, NauBanhServices.cuilua);
//                                        if (cuiLua == null) {
//                                            Service.gI().sendThongBao(player, "Có " + ItemService.gI().getTemplate(NauBanhServices.cuilua).name + " đâu cu");
//                                            return;
//                                        }
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cuiLua, 1);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        NauBanhServices.subTimeNauBanh(3000);
//                                        Service.gI().sendThongBao(player, "Đã sử dụng " + ItemService.gI().getTemplate(NauBanhServices.cuilua).name + "\n|5| Giảm 3 giây thời gian nấu nguyên liệu");
//                                    }
//                                    //                                        this.createOtherMenu(player, ConstNpc.NAU_BANH, "Nồi nấu bánh toàn server\n Thời gian nấu còn " + ConstDataEvent.thoiGianNauBanh / 1000
//                                    //                                                + "\nSố bánh đang nấu " + ConstDataEvent.slBanhTrongNoi
//                                    //                                                + "\nMức nước trong nồi " + ConstDataEvent.mucNuocTrongNoi + "/" + ConstDataEvent.slBanhTrongNoi + " đang chuẩn bị nấu "
//                                    //                                                + "\nTrong đó có " + getSLBanhChungTet(player) + " bánh của bạn đang nấu\n Chơm đủ nước để nồi không bị cháy và nhận đủ số bánh nấu"
//                                    //                                                + "\nThêm " + ItemService.gI().getTemplate(NauBanhServices.cuilua).name + " để tăng tốc thời gian nấu bánh",
//                                    //                                                //Tab menu
//                                    //                                                "Thêm " + ItemService.gI().getTemplate(NauBanhServices.binhnuoc).name + "",
//                                    //                                                "Thêm " + ItemService.gI().getTemplate(NauBanhServices.cuilua).name + "",
//                                    //                                                "Hướng dẫn");
//                                    return;
//
//                                case 2:
//                                    if (player.getSession() != null) {
//                                        this.createOtherMenu(player, 22222, Arrays.toString(ConstDataEvent.Huongdannaubanh), "Đóng");
//                                    }
//                                    break;
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.getSession().vnd < cn.ghVnd && player.getSession().totalvnd < cn.ghTotalVnd) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "\n\b|7|Con đang có :" + player.getSession().vnd + " COIN\n"
                                    + player.getSession().totalvnd + " VND"
                                    + "\n Dùng COIN, VND hãy qua NPC HEART, QUY LÃO ở đảo Kame nhé!!!"
                                    + "\nMở thành viên tại Bardock, dùng COIN tại đây!"
                                    + "\n Next all nv đến kuku, có mốc nạp tại Quy lão"
                                    + "\n Con có thể đua top sự kiện tại Quy Lão Kame"
                                    + "\b\n|5|Code tân thủ: emti1 đến emti13\n"
                                    + "\b\n|3|Lưu ý: Chỉ giao dịch nạp tiền qua duy nhất qua admin Emti,\n"
                                    + "mọi rủi ro tự chịu nếu không chấp hành."
                                    + "\n\b|4| Bảo trì định kì 18H mỗi ngày !!!"
                                            .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                    : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                    "Đổi mật khẩu", "Nhận 5tr ngọc xanh", "Nhận đệ tử", "Xóa đệ tử", "GiftCode", "Hòm thư");
                        }
                    } else {
                        Service.gI().sendThongBaoOK(player, "Thích bug không chém giờ");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Input.gI().createFormChangePassword(player);
                                break;
                            case 1:
                                if (!player.getSession().nhanngocxanh) {
                                    player.inventory.gem = cn.slnx1;
                                    Service.gI().sendMoney(player);
                                    player.getSession().nhanngocxanh = true;
                                    if (PlayerDAO.updatenhanngocxanh(player)) {
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được " + cn.slnx1 + " ngọc xanh");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Nhận r đừng nhận nữa");
                                }

                                break;
                            case 2:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                } else {
                                    this.npcChat(player, "Bú ít thôi con, giấu số đá còn lại ở đâu r ");
                                }
                                break;
                            case 3:
                                Item Thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                if (Thoivang == null) {
                                    this.npcChat(player, "Nghèo làm gì có thỏi vàng mà bấm");
                                    return;
                                }
                                if (player.getSession().vnd < 10000 && Thoivang.quantity < 100) {
                                    this.npcChat(player, "Kiếm đủ 10K VND và 100 thỏi vàng rồi bấm!");
                                    return;
                                }
                                if (PlayerDAO.subvnd(player, 10000)) {
                                    if (player.pet != null) {
                                        PetService.gI().deletePet(player);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, Thoivang, 100);
                                        Service.gI().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa xóa đệ tử, đổi map để load lại đệ");
                                    } else {
                                        this.npcChat(player, "Không có đệ thì xóa kiểu gì con, trêu thầy à");
                                    }
                                }
                                break;

                            case 4:
                                Input.gI().createFormGiftCode(player);
                                break;
//                            case 5:
//                                if (!player.getSession().nhanngochong) {
//                                    player.inventory.ruby = cn.slnh1;
//                                    Service.gI().sendMoney(player);
//                                    player.getSession().nhanngochong = true;
//                                    if (PlayerDAO.updatenhanngochong(player)) {
//                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được " + cn.slnh1 + " ngọc hồng");
//                                    }
//                                } else {
//                                    Service.gI().sendThongBao(player, "Nhận r đừng nhận nữa");
//                                }
//
//                                break;

                            case 5:
                                this.createOtherMenu(player, ConstNpc.MAIL_BOX,
                                        "|1|Tình yêu như một dây đàn\n"
                                        + "Tình vừa được thì đàn đứt dây\n"
                                        + "Đứt dây này anh thay dây khác\n"
                                        + "Mất em rồi anh biết thay ai?",
                                        "Hòm Thư\n(" + (player.inventory.itemsMailBox.size()
                                        - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsMailBox))
                                        + " món)",
                                        "Xóa Hết\nHòm Thư", "Đóng");
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MAIL_BOX) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "ITEMS_MAIL_BOX", true);
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player,
                                        ConstNpc.CONFIRM_REMOVE_ALL_ITEM_MAIL_BOX, this.avartar,
                                        "|3|Bạn chắc muốn xóa hết vật phẩm trong hòm thư?\n"
                                        + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                        "Đồng ý", "Hủy bỏ");
                                break;
                            case 2:
                                break;
                        }
                    } //                    else if (player.iDMark.getIndexMenu() == ConstNpc.QUA_TAN_THU) {
                    //                        switch (select) {
                    //                            case 0:
                    //                                player.inventory.gem = 100000;
                    //                                Service.gI().sendMoney(player);
                    //                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 100K ngọc xanh");
                    //                                player.gift.gemTanThu = true;
                    //                                break;
                    //                            case 1:
                    //                                if (nhanVang) {
                    //                                    player.inventory.gold = Inventory.LIMIT_GOLD;
                    //                                    Service.gI().sendMoney(player);
                    //                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được 2 tỉ vàng");
                    //                                } else {
                    //                                    this.npcChat("");
                    //                                }
                    //                                break;
                    //                            case 2:
                    //                                if (nhanDeTu) {
                    //                                    if (player.pet == null) {
                    //                                        PetService.gI().createMabuPet(player);
                    //                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                    //                                    } else {
                    //                                        this.npcChat("Con đã nhận đệ tử rồi");
                    //                                    }
                    //                                }
                    //                                break;
                    //                        }
                    //                    }
                    else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_THUONG) {
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "ITEMS_REWARD", true);
                                break;
                            case 1:
                                if (player.getSession().goldBar > 0) {
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        int quantity = player.getSession().goldBar;
                                        Item goldBar = ItemService.gI().createNewItem((short) 457, quantity);
                                        InventoryServiceNew.gI().addItemBag(player, goldBar);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Ông đã để " + quantity + " thỏi vàng vào hành trang con rồi đấy");
                                        PlayerDAO.subGoldBar(player, quantity);
                                        player.getSession().goldBar = 0;
                                    } else {
                                        this.npcChat(player, "Con phải có ít nhất 1 ô trống trong hành trang ông mới đưa cho con được");
                                    }
                                }
                                break;
                        }
                    }

                }
            }
        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd < cn.ghVnd && player.getSession().totalvnd < cn.ghTotalVnd) {
                    if (canOpenNpc(player)) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Cậu cần trang bị gì cứ đến chỗ tôi nhé", "Cửa\nhàng");
                        }
                    }
                } else {
                    Service.gI().sendThongBaoOK(player, "Con nhợn này thích bug không ");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {//Shop
                            if (player.gender == ConstPlayer.TRAI_DAT) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                            }
                        }
                    }
                }
            }
        };
    }

    private static Npc KyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd < cn.ghVnd && player.getSession().totalvnd < cn.ghTotalVnd) {
                    if (canOpenNpc(player)) {
                        createOtherMenu(player, 0,
                                "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.",
                                "Hướng\ndẫn\nthêm", "Mua bán\nKý gửi", "Từ chối");
                    }
                } else {
                    Service.gI().sendThongBaoOK(player, "Chém đấy bug ít thôi");
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.gI().sendPopUpMultiLine(pl, tempId, avartar,
                                    //                                    "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 50 hồng ngọc\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
                                    "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 50 thỏi vàng\bGiá trị ký gửi 1-100000 thỏi vàng\b|3|Thuế:- 20%\bMột người bán, vạn người mua, mại dô, mại dô");

                            break;
                        case 1:
                            if (!pl.getSession().actived) {
                                Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                            } else {
                                ShopKyGuiService.gI().openShopKyGui(pl);

                            }
                            break;
                        case 2:
                            break;
                    }

                }
            }
        };
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                            "Cửa hàng chúng tôi chuyên mua bán hàng hiệu, hàng độc, cảm ơn bạn đã ghé thăm.",
//                            "Hướng\ndẫn\nthêm", "Kí cũ(Cấm kí)", "Danh sách\nHết Hạn", "Mua bán", "Hủy");
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    switch (player.iDMark.getIndexMenu()) {
//                        case ConstNpc.BASE_MENU:
//                            switch (select) {
//                                case 0:
//                                    Service.gI().sendPopUpMultiLine(player, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bGiá trị ký gửi 10k-200Tr vàng hoặc 2-2k ngọc\bMột người bán, vạn người mua, mại dô, mại dô");
//                                    break;
//
//                                case 1:
//                                    Service.gI().sendThongBao(player, "Error");
//                                    ConsignmentShop.getInstance().show(player);
//                                    break;
//                                case 2:
//                                    ConsignmentShop.getInstance().showExpiringItems(player);
//                                    break;
//                                case 3:
//                                    this.createOtherMenu(player, ConstNpc.KIGUICU, "Kí gửi cũ", "Kí Gửi cũ");
//
//                                    break;
//                            }
//                            break;
//                        case ConstNpc.KIGUICU:
//                            switch (select) {
//                                case 0:
//                                    ShopKyGuiService.gI().openShopKyGui(player);
//                                    break;
//
//                            }
//                            break;
//
//                    }
//                }
//            }
//
//        };
    }

    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {//Shop
                            if (player.gender == ConstPlayer.NAMEC) {
                                ShopServiceNew.gI().opendShop(player, "DENDE", true);
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec(86399000);
//                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.gI().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {//Shop
                            if (player.gender == ConstPlayer.XAYDA) {
                                ShopServiceNew.gI().opendShop(player, "APPULE", true);
                            } else {
                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(player, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(player);
                                switch (taskId) {
                                    case ConstTask.TASK_19_0:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_1:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_19_2:
                                        this.createOtherMenu(player, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else if (player.getSession().player.nPoint.power >= 1500000000L) {
                                this.createOtherMenu(player, 2, "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                            } else {
                                this.createOtherMenu(player, 3,
                                        "Tàu Vũ Trụ của ta có thể đưa cầu thủ đến hành tinh khác chỉ trong 3 giây. Cầu muốn đi đâu?",
                                        "Đến\nTrái Đất", "Đến\nNamếc");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.getIndexMenu() == 2) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 3) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                            }
                        }
                    }
                }
                if (this.mapId == 19) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                        switch (select) {
                            case 0:
//                          
                                Boss boss = BossManager.gI().getBossByType(BossType.KUKU);
                                if (boss != null) {
                                    if (!boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            if (boss.zone != null) {  // check if boss.zone is not null
                                                Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                                if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                    player.inventory.gold -= COST_FIND_BOSS;
                                                    ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                    Service.gI().sendMoney(player);
                                                } else {
                                                    Service.gI().sendThongBao(player, "Khu vực đang full.");
                                                }
                                            } else {
                                                Service.gI().sendThongBao(player, "Không tìm thấy khu vực của boss.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không tìm thấy boss.");
                                }
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                        switch (select) {
                            case 0:
//                       
                                Boss boss = BossManager.gI().getBossByType(BossType.MAP_DAU_DINH);
                                if (boss != null) {
                                    if (!boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            if (boss.zone != null) {  // check if boss.zone is not null
                                                Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                                if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                    player.inventory.gold -= COST_FIND_BOSS;
                                                    ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                    Service.gI().sendMoney(player);
                                                } else {
                                                    Service.gI().sendThongBao(player, "Khu vực đang full.");
                                                }
                                            } else {
                                                Service.gI().sendThongBao(player, "Không tìm thấy khu vực của boss.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không tìm thấy boss.");
                                }
                                break;
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                        switch (select) {
                            case 0:
//                               
                                Boss boss = BossManager.gI().getBossByType(BossType.RAMBO);
                                if (boss != null) {
                                    if (!boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            if (boss.zone != null) {  // check if boss.zone is not null
                                                Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                                if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                    player.inventory.gold -= COST_FIND_BOSS;
                                                    ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                    Service.gI().sendMoney(player);
                                                } else {
                                                    Service.gI().sendThongBao(player, "Khu vực đang full.");
                                                }
                                            } else {
                                                Service.gI().sendThongBao(player, "Không tìm thấy khu vực của boss.");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Chết rồi ba...");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không tìm thấy boss.");
                                }
                            case 1:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 2:
                                if (player.getSession().player.nPoint.power >= 2000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, -90);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 2 triệu sức mạnh để đi đến đây.");
                                    break;
                                }
                        }
                    }
                }
                if (this.mapId == 68) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng", "Cửa hàng cải trang", "Tiệm cắt\n Lông đầu", "Bán nhanh\nThỏi Vàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                case 1: //tiệm hồng ngọc
                                    ShopServiceNew.gI().opendShop(player, "SHOP_HUNG_VUONG", false);
                                    break;
//                                case 2:
////                                    if (player.getSession().isAdmin) { 
//                                    ShopServiceNew.gI().opendShop(player, "SANTA_EVENT", false);
//                                    } else { 
//                                        Service.gI().sendThongBao(player, "ADMIN mới bấm được thôi");
//                                    }
                                // break;

                                case 2: //tiệm hớt tóc
                                    ShopServiceNew.gI().opendShop(player, "SANTA_HEAD", false);
                                    break;
                                case 3: //ban thoi vang
                                    createOtherMenu(player, 1,
                                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                                            "Bán 1 thỏi", "Bán 5 thỏi", "Bán 10 thỏi", "Bán 20 thỏi", "Bán 30 thỏi", "Bán 50 thỏi", "Bán 100 thỏi");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) {
                            int thoiVang_ = 457;
                            int[] itemRequirements = {1, 5, 10, 20, 30, 50, 100};
                            long[] goldRewards = {
                                500_000_000L, // 1 thỏi vàng
                                2_500_000_000L, // 5 thỏi vàng
                                5_000_000_000L, // 10 thỏi vàng
                                10_000_000_000L, // 20 thỏi vàng
                                15_000_000_000L, // 30 thỏi vàng
                                25_000_000_000L, // 50 thỏi vàng
                                50_000_000_000L // 100 thỏi vàng
                            };

                            if (select < 0 || select >= itemRequirements.length) {
                                Service.gI().sendThongBao(player, "Lựa chọn không hợp lệ!");
                                return;
                            }

                            int requiredQuantity = itemRequirements[select];
                            long goldAmount = goldRewards[select];

                            Item thoivang = InventoryServiceNew.gI().findItemBag(player, thoiVang_);
                            if (thoivang == null) {
                                Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(thoiVang_).name);
                                return;
                            }

                            if (thoivang.quantity < requiredQuantity) {
                                Service.gI().sendThongBao(player, "Bạn không đủ " + thoivang.template.name);
                                return;
                            }

                            if (player.inventory.gold + goldAmount > Inventory.LIMIT_GOLD) {
                                Service.gI().sendThongBao(player, "Bạn đã tới giới hạn vàng");
                                return;
                            }

                            // Trừ vật phẩm và cộng vàng
                            InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, requiredQuantity);
                            player.inventory.gold += goldAmount;

                            // Cập nhật trạng thái cho người chơi
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            Service.gI().sendThongBao(player, "Bạn vừa đổi được " + (goldAmount / 1_000_000_000L) + " tỉ vàng");
                        }

//                        else if (player.iDMark.getIndexMenu() == 1) {
//                            int thoiVang_ = 457;
//                            switch (select) {
//                                case 0: {
//                                    Item concu = InventoryServiceNew.gI().findItemBag(player, thoiVang_);
//                                    if (concu != null) {
//                                        if (concu.quantity < 6) {
//                                            Service.gI().sendThongBao(player, "Bạn không đủ " + concu.template.name);
//                                            return;
//                                        }
//                                        if (player.inventory.gold >= Inventory.LIMIT_GOLD) {
//                                            Service.gI().sendThongBao(player, "Bạn đã tới giới hạn vàng");
//                                            return;
//                                        }
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, concu, 6);
//                                        player.inventory.gold += 3000000000L;
//                                        Service.gI().sendMoney(player);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn vừa đổi được 3 tỉ vàng");
//
//                                    } else {
//                                        Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(thoiVang_).name);
//                                    }
//                                }
//                                break;
//                                case 1: {
//                                    Item concu = InventoryServiceNew.gI().findItemBag(player, thoiVang_);
//                                    if (concu != null) {
//                                        if (concu.quantity < 12) {
//                                            Service.gI().sendThongBao(player, "Bạn không đủ " + concu.template.name);
//                                            return;
//                                        }
//                                        if (player.inventory.gold >= Inventory.LIMIT_GOLD) {
//                                            Service.gI().sendThongBao(player, "Bạn đã tới giới hạn vàng");
//                                            return;
//                                        }
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, concu, 12);
//                                        player.inventory.gold += 6000000000L;
//                                        Service.gI().sendMoney(player);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn vừa đổi được 6 tỉ vàng");
//
//                                    } else {
//                                        Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(thoiVang_).name);
//                                    }
//                                }
//                                break;
//                                case 2: {
//                                    Item concu = InventoryServiceNew.gI().findItemBag(player, thoiVang_);
//                                    if (concu != null) {
//                                        if (concu.quantity < 20) {
//                                            Service.gI().sendThongBao(player, "Bạn không đủ " + concu.template.name);
//                                            return;
//                                        }
//                                        if (player.inventory.gold >= Inventory.LIMIT_GOLD) {
//                                            Service.gI().sendThongBao(player, "Bạn đã tới giới hạn vàng");
//                                            return;
//                                        }
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, concu, 20);
//                                        player.inventory.gold += 10000000000L;
//                                        Service.gI().sendMoney(player);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn vừa đổi được 10 tỉ vàng");
//
//                                    } else {
//                                        Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(thoiVang_).name);
//                                    }
//                                }
//                                break;
//
//                                case 3: {
//                                    Item concu = InventoryServiceNew.gI().findItemBag(player, thoiVang_);
//                                    if (concu != null) {
//                                        if (concu.quantity < 30) {
//                                            Service.gI().sendThongBao(player, "Bạn không đủ " + concu.template.name);
//                                            return;
//                                        }
//                                        if (player.inventory.gold >= Inventory.LIMIT_GOLD) {
//                                            Service.gI().sendThongBao(player, "Bạn đã tới giới hạn vàng");
//                                            return;
//                                        }
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, concu, 30);
//                                        player.inventory.gold += 15000000000L;
//                                        Service.gI().sendMoney(player);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn vừa đổi được 15 tỉ vàng");
//
//                                    } else {
//                                        Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(thoiVang_).name);
//                                    }
//                                }
//                                break;
//
//                            }
//                        }
                    }
                }
            }

        };
    }

    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                canOpenNpc(player);
            }
        };
    }

    public static Npc npc70(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (pl.getSession().vnd < cn.ghVnd && pl.getSession().totalvnd < cn.ghTotalVnd) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "\b|1|Đây là nơi ngươi có thể đổi bất cứ thứ gì"
                                + "\nMiễn là ngươi có tiền"
                                + "\b\n|3| Nạp COIN giá trị ( cứ 20k được <20.000 COIN> và <20.000 VND> trong game)"
                                + "\b|5|MBBANK: " + cn.SDT + " \n"
                                + "|1|Nội dung chuyển khoản: " + cn.MANAP + "" + pl.getSession().userId + "\n"
                                + "\b|3|Lưu ý: Chỉ giao dịch nạp tiền qua duy nhất qua admin Emti,\n"
                                + "mọi rủi ro tự chịu nếu không chấp hành."
                                + "\n\b|7|Bạn đang có :" + pl.getSession().vnd + " COIN\n"
                                + " Tổng nạp: " + pl.getSession().totalvnd + " VND",
                                "Cửa hàng",
                                "Menu COIN",
                                "VND -> COIN",
                                //                                "Đổi đệ",
                                " Mở thành viên",
                                "Đổi skill đệ");
                    } else {
                        Service.gI().sendThongBaoOK(pl, "Có Bug không đó :3");
                        PlayerService.gI().banPlayer((pl));
                        Service.gI().sendThongBao(pl, "Bạn bị ban thành công");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {

                            case 0:
                                ShopServiceNew.gI().opendShop(player, "BARDOCK_SHOP", false);
                                break;
                            case 1:
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 777,
                                            "\b|1|Có tiền rồi đổi thôi!\n"
                                            + "|6|\bCó thể nhận mốc nạp khi nạp ở quy lão Kame nha"
                                            + " \n\b|7|Bạn đang có :" + player.getSession().vnd + " COIN",
                                            "Đổi thỏi vàng", "Đổi ngọc xanh", "Đổi hồng ngọc");
                                }
                                break;
                            case 2:
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 12999,
                                            "\b|1|Có VND đỏi COIN tại đây nhá (10K VND = 9K COIN)\n"
                                            + " \n\b|7|Bạn đang có :" + player.getSession().totalvnd + " VND"
                                            + " \n\b|7|Bạn Có :" + player.getSession().vnd + " COIN",
                                            "Đổi VND\n-> COIN");
                                }
                                break;
//                            case 3:
//                                if (player.getSession() != null) {
//                                    this.createOtherMenu(player, 13000,
//                                            "\b|1|Có tiền rồi đổi thôi!\n "
//                                            + "\n\b|3| Đổi đệ thì tháo cái đồ đệ ra, mất tự chịu nha!\n "
//                                            + "\n\b|4| Chỉ số hợp thể chỉ có tác dụng với bông tai cấp 3 trở lên!\n "
//                                            + "\n\b|7|Bạn đang có :" + player.getSession().vnd + " COIN",
//                                            "Đổi đệ tầm trung", "Đổi đệ vip", "Đổi đệ vip pro", "Đổi đệ siêu cấp", "Đổi đệ siêu thần");
//                                }
//                                break;

                            case 3:
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 782,
                                            "\b|2|Mở thành viên giá 20k \n \b|7|Bạn đã nạp :"
                                            + "" + player.getSession().totalvnd2 + " đồng",
                                            "Mở", "Đóng");
                                }
                                break;
                            case 4:
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 888,
                                            "|3|Lưu ý: Đổi Skill đệ bằng tiền nạp sẽ mất COIN\n"
                                            + "\nBạn có: " + player.getSession().vnd + " COIN",
                                            //Menu CHọn
                                            "Đổi skill 2-3\n " + cn.skill23, "Đổi skill 2-4\n " + cn.skill24);

                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 12999) {
                        switch (select) {
                            case 0:
                                Input.gI().createFormDoiVND(player);
                                break;
                        }
                    } //else if (player.iDMark.getIndexMenu() == 13000) {
                    //                        switch (select) {
                    //                            case 0:
                    //                                if (player.pet == null) {
                    //                                    Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                    //                                    return;
                    //                                }
                    //                                for (Item item : player.pet.inventory.itemsBody) {
                    //                                    if (item.isNotNullItem()) {
                    //                                        Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                    //                                        return;
                    //                                    }
                    //                                }
                    //                                if (player.getSession() != null) {
                    //                                    this.createOtherMenu(player, 781,
                    //                                            "\b|2|Muốn đổi đệ mới bằng COIN à?\n"
                    //                                            + "|3|Tùy chọn đệ tử với chỉ số hợp thể tương đương HP-MP + DMG (%)\n"
                    //                                            + "Tùy chọn 1: Đệ tử Cell: HP + " + cn.hpPet4 + "%, MP + " + cn.mpPet4 + "%, Dame + " + cn.damePet4 + " %\n"
                    //                                            + "Tùy chọn 2: Đệ tử Cumber: HP + " + cn.hpPet5 + "%, MP + " + cn.mpPet5 + "%, Dame + " + cn.damePet5 + " %\n"
                    //                                            + "Tùy chọn 3: Đệ tử Fide Vàng: HP + " + cn.hpPet6 + "%, MP + " + cn.mpPet6 + "%, Dame + " + cn.damePet6 + " %\n"
                    //                                            + "Tùy chọn 4: Đệ tử Mai: HP + " + cn.hpPet8 + "%, MP + " + cn.mpPet8 + "%, Dame + " + cn.damePet8 + " %\n"
                    //                                            + "Tùy chọn 5: Đệ tử Heart: HP + " + cn.hpPet7 + "%, MP + " + cn.mpPet7 + "%, Dame + " + cn.damePet7 + " %\n"
                    //                                            + "Tùy chọn 6: Đệ tử Beerus: HP + " + cn.hpPet2 + "%, MP + " + cn.mpPet2 + "%, Dame + " + cn.damePet2 + " %\n"
                    //                                            + "Tùy chọn 7: Đệ tử Gohan: HP + " + cn.hpPet9 + "%, MP + " + cn.mpPet9 + "%, Dame + " + cn.damePet9 + " %\n"
                    //                                            + "Tùy chọn 8: Đệ tử Jiren Full Power: HP + " + cn.hpPet10 + "%, MP + " + cn.mpPet10 + "%, Dame + " + cn.damePet10 + " %\n"
                    //                                            + "\n\b|7|Bạn đang có :" + player.getSession().vnd + " COIN\n"
                    //                                            + "",
                    //                                            //                                            + "|6|\nChỉ số hợp thể đệ hiện tại :\n"
                    //                                            //                                            + ""
                    //                                            //                                            + "-HP:" + player.pointfusion.getHpFusion() + "\n-KI:" + player.pointfusion.getMpFusion() + "\n-DAME:" + player.pointfusion.getDameFusion() + "",
                    //                                            //Menu Chọn
                    //                                            "Đệ cell\n " + cn.de1 + " COIN",
                    //                                            "Đệ Cumber\n " + cn.de2 + " COIN",
                    //                                            "Đệ Fide Vàng\n " + cn.de3 + " COIN",
                    //                                            "Đệ Mai\n " + cn.de4 + " COIN",
                    //                                            "Đệ Heart\n " + cn.de5 + " COIN",
                    //                                            "Đệ berus\n " + cn.de6 + " COIN",
                    //                                            "Đệ Gohan\n " + cn.de7 + " COIN",
                    //                                            "Đệ Jiren Full Power\n " + cn.de8 + " COIN");
                    //                                }
                    //                                break;
                    //                            case 1:
                    //                                if (player.pet == null) {
                    //                                    Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                    //                                    return;
                    //                                }
                    //                                for (Item item : player.pet.inventory.itemsBody) {
                    //                                    if (item.isNotNullItem()) {
                    //                                        Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                    //                                        return;
                    //                                    }
                    //                                }
                    //                                if (player.getSession() != null) {
                    //
                    //                                    this.createOtherMenu(player, 783,
                    //                                            "\b|2|Muốn đổi đệ Vip thì mua giá hơi chát nhá!!! "
                    //                                            + "\n|4|Tùy chọn đệ tử với chỉ số hợp thể tương đương HP-MP + DMG (%)"
                    //                                            + "\n Tùy chọn 1: Đệ tử Goku Black SSJ3: HP + " + cn.hpPet11 + "%, MP + " + cn.mpPet11 + "%, Dame + " + cn.damePet11 + "% "
                    //                                            + "\n Tùy chọn 2: Đệ tử GOGETA SSJ4: HP + " + cn.hpPet12 + "%, MP + " + cn.mpPet12 + "%, Dame + " + cn.damePet12 + "% "
                    //                                            + "\n Tùy chọn 3: Đệ tử Goku VÔ CỰC: HP + " + cn.hpPet13 + "%, MP + " + cn.mpPet13 + "%, Dame + " + cn.damePet13 + "% "
                    //                                            + "\n Tùy chọn 4: Đệ tử Berus Bí Ngô: HP + " + cn.hpPet14 + "%, MP + " + cn.mpPet14 + "%, Dame + " + cn.damePet14 + "% "
                    //                                            + "\n Tùy chọn 5: Đệ tử Zamasu: HP + " + cn.hpPet15 + "%, MP + " + cn.mpPet15 + "%, Dame + " + cn.damePet15 + "% "
                    //                                            + "\n Tùy chọn 6: Đệ tử Daisinkan: HP + " + cn.hpPet16 + "%, MP + " + cn.mpPet16 + "%, Dame + " + cn.damePet16 + "% "
                    //                                            + "\n Tùy chọn 7: Đệ tử Whis: HP + " + cn.hpPet17 + "%, MP + " + cn.mpPet17 + "%, Dame + " + cn.damePet17 + "% "
                    //                                            + "\n Tùy chọn 8: Đệ tử Granola: HP + " + cn.hpPet18 + "%, MP + " + cn.mpPet18 + "%, Dame + " + cn.damePet18 + "% "
                    //                                            + "\n\b|7|Bạn đang có :" + player.getSession().vnd + " COIN\n"
                    //                                            + "",
                    //                                            //                                            + "|6|\nChỉ số hợp thể đệ hiện tại :\n"
                    //                                            //                                            + ""
                    //                                            //                                            + "-HP:" + player.pointfusion.getHpFusion() + "\n-KI:" + player.pointfusion.getMpFusion() + "\n-DAME:" + player.pointfusion.getDameFusion() + "",
                    //                                            //Menu Chọn
                    //                                            "Đệ Goku Black3\n " + cn.de9 + " COIN",
                    //                                            "Đệ Gogeta C4\n " + cn.de10 + " COIN",
                    //                                            "Đệ Goku VÔ CỰC\n " + cn.de11 + " COIN",
                    //                                            "Đệ Berus Bí\n " + cn.de12 + " COIN",
                    //                                            "Đệ Zamasu\n " + cn.de13 + " COIN",
                    //                                            "Đệ Daisinkan\n " + cn.de14 + " COIN",
                    //                                            "Đệ Whis\n " + cn.de15 + " COIN",
                    //                                            "Đệ Granlola\n " + cn.de16 + " COIN");
                    //                                }
                    //                                break;
                    //                            case 2:
                    //                                if (player.pet == null) {
                    //                                    Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                    //                                    return;
                    //                                }
                    //
                    //                                for (Item item : player.pet.inventory.itemsBody) {
                    //                                    if (item.isNotNullItem()) {
                    //                                        Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                    //                                        return;
                    //                                    }
                    //                                }
                    //                                if (player.getSession() != null) {
                    //                                    this.createOtherMenu(player, 784,
                    //                                            "\b|2|Muốn đổi đệ Vip thì mua giá hơi chát nhá!!! "
                    //                                            + "\n|5|Tùy chọn đệ tử với chỉ số hợp thể tương đương HP-MP + DMG (%)"
                    //                                            + "\n Tùy chọn 1: Đệ tử Oren: HP + " + cn.hpPet20 + "%, MP + " + cn.mpPet20 + "%, Dame + " + cn.damePet20 + " %"
                    //                                            + "\n Tùy chọn 2: Đệ tử Kamin: HP + " + cn.hpPet19 + "%, MP + " + cn.mpPet19 + "%, Dame + " + cn.damePet19 + " %"
                    //                                            + "\n Tùy chọn 3: Đệ tử Drabura: HP + " + cn.hpPet25 + "%, MP + " + cn.mpPet25 + "%, Dame + " + cn.damePet25 + " %"
                    //                                            + "\n Tùy chọn 4: Đệ tử Kamin Oren: HP + " + cn.hpPet21 + "%, MP + " + cn.mpPet21 + "%, Dame + " + cn.damePet21 + " %"
                    //                                            + "\n Tùy chọn 5: Đệ tử Gojo: HP + " + cn.hpPet22 + "%, MP + " + cn.mpPet22 + "%, Dame + " + cn.damePet22 + " %"
                    //                                            + "\n Tùy chọn 6: Đệ tử Baby jiren: HP + " + cn.hpPet27 + "%, MP + " + cn.mpPet27 + "%, Dame + " + cn.damePet27 + " %"
                    //                                            + "\n Tùy chọn 7: Đệ tử Hachiyack: HP + " + cn.hpPet23 + "%, MP + " + cn.mpPet23 + "%, Dame + " + cn.damePet23 + " %"
                    //                                            + "\n Tùy chọn 8: Đệ tử Goku SSSJ5: HP + " + cn.hpPet26 + "%, MP + " + cn.mpPet26 + "%, Dame + " + cn.damePet26 + " %"
                    //                                            + "\n\b|7|Bạn đang có :" + player.getSession().vnd + " COIN\n"
                    //                                            + "",
                    //                                            //                                            + "|6|\nChỉ số hợp thể đệ hiện tại :\n"
                    //                                            //                                            + ""
                    //                                            //                                            + "-HP:" + player.pointfusion.getHpFusion() + "\n-KI:" + player.pointfusion.getMpFusion() + "\n-DAME:" + player.pointfusion.getDameFusion() + "",
                    //                                            //Menu Chọn
                    //                                            "Đệ Oren\n " + cn.de17 + " COIN",
                    //                                            "Đệ Kamin\n " + cn.de18 + " COIN",
                    //                                            "Đệ Drabura\n " + cn.de19 + " COIN",
                    //                                            "Đệ Kamin Oren\n " + cn.de20 + " COIN",
                    //                                            "Đệ Gôjo\n " + cn.de21 + " COIN",
                    //                                            "Đệ Baby jiren\n " + cn.de22 + " COIN",
                    //                                            "Đệ Hachiyack\n " + cn.de23 + " COIN",
                    //                                            "Đệ Goku SSJ5\n " + cn.de24 + " COIN");
                    //                                }
                    //                                break;
                    //                            case 3:
                    //                                if (player.pet == null) {
                    //                                    Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                    //                                    return;
                    //                                }
                    //                                for (Item item : player.pet.inventory.itemsBody) {
                    //                                    if (item.isNotNullItem()) {
                    //                                        Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                    //                                        return;
                    //                                    }
                    //                                }
                    //                                if (player.getSession() != null) {
                    //                                    this.createOtherMenu(player, 785,
                    //                                            "\b|2|Muốn đổi đệ Vip thì mua giá hơi chát nhá!!! "
                    //                                            + "\n|1|Tùy chọn đệ tử với chỉ số hợp thể tương đương HP-MP + DMG (%)"
                    //                                            + "\n Tùy chọn 1: Đệ tử Zamas Cải Cách: HP + " + cn.hpPet29 + "%, MP + " + cn.mpPet29 + "%, Dame + " + cn.damePet29 + " %"
                    //                                            + "\n Tùy chọn 2: Đệ tử Baby Vegeta: HP + " + cn.hpPet28 + "%, MP + " + cn.mpPet28 + "%, Dame + " + cn.damePet28 + " %"
                    //                                            + "\n Tùy chọn 3: Đệ tử Kafula: HP + " + cn.hpPet30 + "%, MP + " + cn.mpPet30 + "%, Dame + " + cn.damePet30 + " %"
                    //                                            + "\n Tùy chọn 4: Đệ tử Ultra Ego: HP + " + cn.hpPet24 + "%, MP + " + cn.mpPet24 + "%, Dame + " + cn.damePet24 + " %"
                    //                                            + "\n Tùy chọn 5: Đệ tử Cumber Base: HP + " + cn.hpPet31 + "%, MP + " + cn.mpPet31 + "%, Dame + " + cn.damePet31 + " %"
                    //                                            + "\n Tùy chọn 6: Đệ tử Cumber Super: HP + " + cn.hpPet32 + "%, MP + " + cn.mpPet32 + "%, Dame + " + cn.damePet32 + " %"
                    //                                            + "\n\b|7|Bạn đang có :" + player.getSession().vnd + " COIN\n"
                    //                                            + "",
                    //                                            //                                            + "|6|\nChỉ số hợp thể đệ hiện tại :\n"
                    //                                            //                                            + ""
                    //                                            //                                            + "-HP:" + player.pointfusion.getHpFusion() + "\n-KI:" + player.pointfusion.getMpFusion() + "\n-DAME:" + player.pointfusion.getDameFusion() + "",
                    //                                            //Menu Chọn
                    //                                            "Đệ Zamas Cải\n " + cn.de25 + " COIN",
                    //                                            "Đệ Baby Vegeta\n " + cn.de26 + " COIN",
                    //                                            "Đệ Kafula\n " + cn.de27 + " COIN",
                    //                                            "Đệ Ultra Ego\n " + cn.de28 + " COIN",
                    //                                            "Đệ Cumber Base\n " + cn.de29 + " COIN",
                    //                                            "Đệ Cumber Super\n " + cn.de30 + " COIN");
                    //                                }
                    //                                break;
                    //                            case 4:
                    //                                if (player.pet == null) {
                    //                                    Service.gI().sendThongBao(player, "Ngươi cần phải có đệ mới sử dụng được chức năng này?");
                    //                                    return;
                    //                                }
                    //                                for (Item item : player.pet.inventory.itemsBody) {
                    //                                    if (item.isNotNullItem()) {
                    //                                        Service.gI().sendThongBao(player, "Cần bỏ đồ đệ tử đang mặc để sử dụng chức năng?");
                    //                                        return;
                    //                                    }
                    //                                }
                    //                                if (player.getSession() != null) {
                    //                                    this.createOtherMenu(player, 786,
                    //                                            "\b|2|Muốn đổi đệ Siêu Vip thì mua giá hơi chát nhá!!! "
                    //                                            + "\n|1|Tùy chọn đệ tử với chỉ số hợp thể tương đương HP-MP + DMG (%)\n"
                    //                                            + "\n|3| Đây là đệ đặc biệt khi hợp thể sẽ ramdom chỉ số cộng không cố định"
                    //                                            + "\n Tùy chọn 0: Đệ tử OMEGA: Ramdom từ 100 - 200%"
                    //                                            + "\n Tùy chọn 1: Đệ tử ZAMASU DỊ: Ramdom từ 100 - 200%"
                    //                                            + "\n Tùy chọn 2: Đệ tử YACHIRO: Ramdom từ 120 - 220%"
                    //                                            + "\n Tùy chọn 3: Đệ tử Goku_SJJ4: Ramdom từ 140 - 240%"
                    //                                            + "\n Tùy chọn 4: Đệ tử VEGETA_SSJ4: Ramdom từ 160 - 260%"
                    //                                            + "\n Tùy chọn 5: Đệ tử GOGETA_SSJ4: Ramdom từ 180 - 280%"
                    //                                            + "\n Tùy chọn 6: Đệ tử TOPPO: Ramdom từ 180 - 280%"
                    //                                            + "\n Tùy chọn 7: Đệ tử Zeno: Ramdom từ 200 - 350%"
                    //                                            + "\n\b|7|Bạn đang có :" + player.getSession().vnd + " COIN\n"
                    //                                            + ""
                    //                                            + "|6|\nChỉ số hợp thể đệ Ramdom :\n"
                    //                                            + ""
                    //                                            + "-HP:" + player.pointfusion.getHpFusion() + "\n-KI:" + player.pointfusion.getMpFusion() + "\n-DAME:" + player.pointfusion.getDameFusion() + "",
                    //                                            //Menu Chọn
                    //                                            "Đệ OMEGA\n " + cn.de31 + " COIN",
                    //                                            "Đệ ZAMASU DỊ\n " + cn.de31 + " COIN",
                    //                                            "Đệ YACHIRO\n " + cn.de32 + " COIN",
                    //                                            "Đệ Goku_SJJ4\n " + cn.de33 + " COIN",
                    //                                            "Đệ VEGETA_SSJ4\n " + cn.de34 + " COIN",
                    //                                            "Đệ GOGETA_SSJ4\n " + cn.de35 + " COIN",
                    //                                            "Đệ TOPPO\n " + cn.de35 + " COIN",
                    //                                            "Đệ Zeno\n " + cn.de36 + " COIN");
                    //                                }
                    //                                break;
                    //
                    //                        }
                    //    }
                    else if (player.iDMark.getIndexMenu() == 888) {
                        switch (select) {
                            case 0: //thay chiêu 2-3 đệ tử
                                if (player.getSession() != null && player.getSession().vnd < cn.skill23) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.skill23 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.skill23)) {
                                    if (player.pet != null) {
                                        if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                            player.pet.openSkill2();
                                            if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                                                player.pet.openSkill3();
                                            }
                                            Service.gI().sendThongBao(player, "Đổi skill 2-3 đệ thành công");
                                        } else {
                                            Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");

                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");

                                    }
                                }
                                break;
                            case 1: //thay chiêu 2-4 đệ tử
                                if (player.getSession() != null && player.getSession().vnd < cn.skill24) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.skill24 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.skill24)) {
                                    if (player.pet != null) {
                                        if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                            player.pet.openSkill2();
                                            if (player.pet.playerSkill.skills.get(3).skillId != -1) {
                                                player.pet.openSkill4();
                                            }
                                            Service.gI().sendThongBao(player, "Đổi skill 2-4 đệ thành công");

                                        } else {
                                            Service.gI().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");

                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Ngươi làm gì có đệ tử?");

                                    }
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 777) {
                        switch (select) {
                            case 0:
                                Input.gI().createFormDoiThoiVang(player);
                                break;
                            case 1:
                                Input.gI().createFormDoiNgocXanh(player);
                                break;
                            case 2:
                                Input.gI().createFormDoiNgocHong(player);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 778) {
                        switch (select) {
                            case 0:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_1) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_1 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_1)) {
                                    if (InventoryServiceNew.gI().findItemBag(player, cn.thoivang) != null && InventoryServiceNew.gI().findItemBag(player, cn.thoivang).quantity > cn.ghThoiVang) {
                                        Service.gI().sendThongBaoOK(player, ItemService.gI().getTemplate(cn.thoivang).name + "của bạn đã đạt giới hạn vui lòng sử dụng bớt coi");
                                        return;
                                    }
                                    Item i = ItemService.gI().createNewItem(cn.thoivang, cn.thoiVang_1);
                                    i.itemOptions.add(new Item.ItemOption(93, 15));
                                    InventoryServiceNew.gI().addItemBag(player, i);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.thoiVang_1 + " " + ItemService.gI().getTemplate(cn.thoivang).name);

                                }

                                break;

                            case 1:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_2) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_2 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_2)) {
                                    if (InventoryServiceNew.gI().findItemBag(player, cn.thoivang) != null && InventoryServiceNew.gI().findItemBag(player, cn.thoivang).quantity > cn.ghThoiVang) {
                                        Service.gI().sendThongBaoOK(player, ItemService.gI().getTemplate(cn.thoivang).name + "của bạn đã đạt giới hạn vui lòng sử dụng bớt coi");
                                        return;
                                    }
                                    Item i = ItemService.gI().createNewItem(cn.thoivang, cn.thoiVang_2);
                                    i.itemOptions.add(new Item.ItemOption(93, 15));
                                    InventoryServiceNew.gI().addItemBag(player, i);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.thoiVang_2 + " " + ItemService.gI().getTemplate(cn.thoivang).name);

                                }
                                if (player.getSession() != null && player.getSession().actived) {
                                    this.createOtherMenu(player, 778,
                                            "\b|1|Muốn đổi  " + ItemService.gI().getTemplate(cn.thoivang).name + " à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            //Menu Chọn
                                            " " + cn.giaVND_1 + " COIN\n " + cn.thoiVang_1 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_2 + " COIN\n " + cn.thoiVang_2 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_3 + " COIN\n " + cn.thoiVang_3 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_4 + " COIN\n " + cn.thoiVang_4 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_5 + " COIN\n " + cn.thoiVang_5 + " " + ItemService.gI().getTemplate(cn.thoivang).name);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần mở thành viên để sử dụng đổi thỏi");
                                }
                                break;
                            case 2:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_3) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_3 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_3)) {
                                    if (InventoryServiceNew.gI().findItemBag(player, cn.thoivang) != null && InventoryServiceNew.gI().findItemBag(player, cn.thoivang).quantity > cn.ghThoiVang) {
                                        Service.gI().sendThongBaoOK(player, ItemService.gI().getTemplate(cn.thoivang).name + "của bạn đã đạt giới hạn vui lòng sử dụng bớt coi");
                                        return;
                                    }
                                    Item i = ItemService.gI().createNewItem(cn.thoivang, cn.thoiVang_3);
                                    i.itemOptions.add(new Item.ItemOption(93, 15));
                                    InventoryServiceNew.gI().addItemBag(player, i);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.thoiVang_3 + " " + ItemService.gI().getTemplate(cn.thoivang).name);

                                }
                                if (player.getSession() != null && player.getSession().actived) {
                                    this.createOtherMenu(player, 778,
                                            "\b|1|Muốn đổi  " + ItemService.gI().getTemplate(cn.thoivang).name + " à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            //Menu Chọn
                                            " " + cn.giaVND_1 + " COIN\n " + cn.thoiVang_1 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_2 + " COIN\n " + cn.thoiVang_2 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_3 + " COIN\n " + cn.thoiVang_3 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_4 + " COIN\n " + cn.thoiVang_4 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_5 + " COIN\n " + cn.thoiVang_5 + " " + ItemService.gI().getTemplate(cn.thoivang).name);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần mở thành viên để sử dụng đổi thỏi");
                                }
                                break;
                            case 3:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_4) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_4 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_4)) {
                                    if (InventoryServiceNew.gI().findItemBag(player, cn.thoivang) != null && InventoryServiceNew.gI().findItemBag(player, cn.thoivang).quantity > cn.ghThoiVang) {
                                        Service.gI().sendThongBaoOK(player, ItemService.gI().getTemplate(cn.thoivang).name + "của bạn đã đạt giới hạn vui lòng sử dụng bớt coi");
                                        return;
                                    }
                                    Item i = ItemService.gI().createNewItem(cn.thoivang, cn.thoiVang_4);
                                    i.itemOptions.add(new Item.ItemOption(93, 15));
                                    InventoryServiceNew.gI().addItemBag(player, i);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.thoiVang_4 + " " + ItemService.gI().getTemplate(cn.thoivang).name);

                                }
                                if (player.getSession() != null && player.getSession().actived) {
                                    this.createOtherMenu(player, 778,
                                            "\b|1|Muốn đổi  " + ItemService.gI().getTemplate(cn.thoivang).name + " à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            //Menu Chọn
                                            " " + cn.giaVND_1 + " COIN\n " + cn.thoiVang_1 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_2 + " COIN\n " + cn.thoiVang_2 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_3 + " COIN\n " + cn.thoiVang_3 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_4 + " COIN\n " + cn.thoiVang_4 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_5 + " COIN\n " + cn.thoiVang_5 + " " + ItemService.gI().getTemplate(cn.thoivang).name);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần mở thành viên để sử dụng đổi thỏi");
                                }
                                break;

                            case 4:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_5) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_5 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_5)) {
                                    if (InventoryServiceNew.gI().findItemBag(player, cn.thoivang) != null && InventoryServiceNew.gI().findItemBag(player, cn.thoivang).quantity > cn.ghThoiVang) {
                                        Service.gI().sendThongBaoOK(player, ItemService.gI().getTemplate(cn.thoivang).name + "của bạn đã đạt giới hạn vui lòng sử dụng bớt coi");
                                        return;
                                    }
                                    Item i = ItemService.gI().createNewItem(cn.thoivang, cn.thoiVang_5);
                                    i.itemOptions.add(new Item.ItemOption(93, 15));
                                    InventoryServiceNew.gI().addItemBag(player, i);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.thoiVang_5 + " " + ItemService.gI().getTemplate(cn.thoivang).name);

                                }
                                if (player.getSession() != null && player.getSession().actived) {
                                    this.createOtherMenu(player, 778,
                                            "\b|1|Muốn đổi  " + ItemService.gI().getTemplate(cn.thoivang).name + " à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            //Menu Chọn
                                            " " + cn.giaVND_1 + " COIN\n " + cn.thoiVang_1 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_2 + " COIN\n " + cn.thoiVang_2 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_3 + " COIN\n " + cn.thoiVang_3 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_4 + " COIN\n " + cn.thoiVang_4 + " " + ItemService.gI().getTemplate(cn.thoivang).name,
                                            " " + cn.giaVND_5 + " COIN\n " + cn.thoiVang_5 + " " + ItemService.gI().getTemplate(cn.thoivang).name);
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Cần mở thành viên để sử dụng đổi thỏi");
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 779) {
                        switch (select) {
                            case 0:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_1) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_1 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_1)) {
                                    if (player.inventory != null && player.inventory.gem > cn.ghNgocXanh) {
                                        Service.gI().sendThongBao(player, "Số ngọc xanh của bạn đã vượt giới hạn vui lòng dùng bớt coi");
                                        return;
                                    }
                                    assert player.inventory != null;
                                    player.inventory.gem += cn.nx1;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.nx1 + " ngọc xanh");
                                }
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 779,
                                            "\b|1|Muốn đổi ngọc xanh à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            //Menu Chọn
                                            cn.giaVND_1 + " COIN\n" + cn.nx1 + " ngọc xanh",
                                            cn.giaVND_2 + " COIN\n" + cn.nx2 + " ngọc xanh",
                                            cn.giaVND_3 + " COIN\n" + cn.nx3 + " ngọc xanh",
                                            cn.giaVND_4 + " COIN\n" + cn.nx4 + " ngọc xanh");
                                }
                                break;

                            case 1:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_2) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_2 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_2)) {
                                    if (player.inventory != null && player.inventory.gem > cn.ghNgocXanh) {
                                        Service.gI().sendThongBao(player, "Số ngọc xanh của bạn đã vượt giới hạn vui lòng dùng bớt coi");
                                        return;
                                    }
                                    assert player.inventory != null;
                                    player.inventory.gem += cn.nx2;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.nx2 + " ngọc xanh");
                                }
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 779,
                                            "\b|1|Muốn đổi ngọc xanh à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            //Menu Chọn
                                            cn.giaVND_1 + " COIN\n" + cn.nx1 + " ngọc xanh",
                                            cn.giaVND_2 + " COIN\n" + cn.nx2 + " ngọc xanh",
                                            cn.giaVND_3 + " COIN\n" + cn.nx3 + " ngọc xanh",
                                            cn.giaVND_4 + " COIN\n" + cn.nx4 + " ngọc xanh");
                                }
                                break;

                            case 2:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_3) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_3 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_3)) {
                                    if (player.inventory != null && player.inventory.gem > cn.ghNgocXanh) {
                                        Service.gI().sendThongBao(player, "Số ngọc xanh của bạn đã vượt giới hạn vui lòng dùng bớt coi");
                                        return;
                                    }
                                    assert player.inventory != null;
                                    player.inventory.gem += cn.nx3;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.nx3 + " ngọc xanh");
                                }
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 779,
                                            "\b|1|Muốn đổi ngọc xanh à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            //Menu Chọn
                                            cn.giaVND_1 + " COIN\n" + cn.nx1 + " ngọc xanh",
                                            cn.giaVND_2 + " COIN\n" + cn.nx2 + " ngọc xanh",
                                            cn.giaVND_3 + " COIN\n" + cn.nx3 + " ngọc xanh",
                                            cn.giaVND_4 + " COIN\n" + cn.nx4 + " ngọc xanh");
                                }
                                break;
                            case 3:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_4) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_4 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_4)) {
                                    if (player.inventory != null && player.inventory.gem > cn.ghNgocXanh) {
                                        Service.gI().sendThongBao(player, "Số ngọc xanh của bạn đã vượt giới hạn vui lòng dùng bớt coi");
                                        return;
                                    }
                                    assert player.inventory != null;
                                    player.inventory.gem += cn.nx4;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.nx4 + " ngọc xanh");
                                }
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 779,
                                            "\b|1|Muốn đổi ngọc xanh à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            //Menu Chọn
                                            cn.giaVND_1 + " COIN\n" + cn.nx1 + " ngọc xanh",
                                            cn.giaVND_2 + " COIN\n" + cn.nx2 + " ngọc xanh",
                                            cn.giaVND_3 + " COIN\n" + cn.nx3 + " ngọc xanh",
                                            cn.giaVND_4 + " COIN\n" + cn.nx4 + " ngọc xanh");
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 780) {
                        switch (select) {
                            case 0:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_1) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_1 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_1)) {
                                    if (player.inventory != null && player.inventory.ruby > cn.ghNgocHong) {
                                        Service.gI().sendThongBao(player, "Số ngọc hồng của bạn đã vượt giới hạn vui lòng dùng bớt coi");
                                        return;
                                    }
                                    assert player.inventory != null;
                                    player.inventory.ruby += cn.nh1;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.nh1 + " hồng ngọc");
                                }
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 780,
                                            "\b|1|Muốn đổi hồng ngọc à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            cn.giaVND_1 + " COIN\n" + cn.nh1 + " ngọc hồng",
                                            cn.giaVND_2 + " COIN\n" + cn.nh2 + " ngọc hồng",
                                            cn.giaVND_3 + " COIN\n" + cn.nh3 + " ngọc hồng",
                                            cn.giaVND_4 + " COIN\n" + cn.nh4 + " ngọc hồng");
                                }
                                break;

                            case 1:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_2) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_2 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_2)) {
                                    if (player.inventory != null && player.inventory.ruby > cn.ghNgocHong) {
                                        Service.gI().sendThongBao(player, "Số ngọc hồng của bạn đã vượt giới hạn vui lòng dùng bớt coi");
                                        return;
                                    }
                                    assert player.inventory != null;
                                    player.inventory.ruby += cn.nh2;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.nh2 + " hồng ngọc");
                                }
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 780,
                                            "\b|1|Muốn đổi hồng ngọc à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            cn.giaVND_1 + " COIN\n" + cn.nh1 + " ngọc hồng",
                                            cn.giaVND_2 + " COIN\n" + cn.nh2 + " ngọc hồng",
                                            cn.giaVND_3 + " COIN\n" + cn.nh3 + " ngọc hồng",
                                            cn.giaVND_4 + " COIN\n" + cn.nh4 + " ngọc hồng");
                                }
                                break;
                            case 2:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_3) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_3 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_3)) {
                                    if (player.inventory != null && player.inventory.ruby > cn.ghNgocHong) {
                                        Service.gI().sendThongBao(player, "Số ngọc hồng của bạn đã vượt giới hạn vui lòng dùng bớt coi");
                                        return;
                                    }
                                    assert player.inventory != null;
                                    player.inventory.ruby += cn.nh3;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.nh3 + " hồng ngọc");
                                }
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 780,
                                            "\b|1|Muốn đổi hồng ngọc à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            cn.giaVND_1 + " COIN\n" + cn.nh1 + " ngọc hồng",
                                            cn.giaVND_2 + " COIN\n" + cn.nh2 + " ngọc hồng",
                                            cn.giaVND_3 + " COIN\n" + cn.nh3 + " ngọc hồng",
                                            cn.giaVND_4 + " COIN\n" + cn.nh4 + " ngọc hồng");
                                }
                                break;
                            case 3:
                                if (player.getSession() != null && player.getSession().vnd < cn.giaVND_4) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.giaVND_4 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.giaVND_4)) {
                                    if (player.inventory != null && player.inventory.ruby > cn.ghNgocHong) {
                                        Service.gI().sendThongBao(player, "Số ngọc hồng của bạn đã vượt giới hạn vui lòng dùng bớt coi");
                                        return;
                                    }
                                    assert player.inventory != null;
                                    player.inventory.ruby += cn.nh4;
                                    Service.gI().sendMoney(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được " + cn.nh4 + " hồng ngọc");
                                }
                                if (player.getSession() != null) {
                                    this.createOtherMenu(player, 780,
                                            "\b|1|Muốn đổi hồng ngọc à?"
                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd + " COIN\n"
                                            + " Tổng nạp: " + player.getSession().totalvnd + " VND",
                                            cn.giaVND_1 + " COIN\n" + cn.nh1 + " ngọc hồng",
                                            cn.giaVND_2 + " COIN\n" + cn.nh2 + " ngọc hồng",
                                            cn.giaVND_3 + " COIN\n" + cn.nh3 + " ngọc hồng",
                                            cn.giaVND_4 + " COIN\n" + cn.nh4 + " ngọc hồng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 781) {
                        switch (select) {

                            case 0:

                                if (player.getSession() != null && player.getSession().vnd < cn.de1) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de1 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de1)) {
                                    PetService.gI().changeCellPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Xên");
                                }
                                break;

                            case 1:
                                if (player.getSession() != null && player.getSession().vnd < cn.de2) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de2 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de2)) {

                                    PetService.gI().changeCumberPet(player, player.gender);

                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Cumber");
                                }
                                break;
                            case 2:
                                if (player.getSession() != null && player.getSession().vnd < cn.de3) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de3 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de3)) {

                                    PetService.gI().changeFideGoldPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Fide Vàng");
                                }
                                break;
                            case 3:
                                if (player.getSession() != null && player.getSession().vnd < cn.de4) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de4 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de4)) {
                                    PetService.gI().changeMaiPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Mai");
                                }
                                break;

                            case 4:
                                if (player.getSession() != null && player.getSession().vnd < cn.de5) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de5 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de5)) {
                                    PetService.gI().changeHeartPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Heart");
                                }
                                break;
                            case 5:
                                if (player.getSession() != null && player.getSession().vnd < cn.de6) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de6 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de6)) {
                                    PetService.gI().changeBerusPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Beerus");
                                }
                                break;

                            case 6:
                                if (player.getSession() != null && player.getSession().vnd < cn.de7) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de7 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de7)) {

                                    PetService.gI().changeGohanPet(player, player.gender);

                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Gohan");
                                }
                                break;

                            case 7:
                                if (player.getSession() != null && player.getSession().vnd < cn.de8) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de8 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de8)) {

                                    PetService.gI().changeJirenPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Jiren");
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 783) {
                        switch (select) {
                            case 0:
                                if (player.getSession() != null && player.getSession().vnd < cn.de9) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de9 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de9)) {
                                    PetService.gI().changeBlack3Pet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Goku Black SSJ3");
                                }
                                break;
                            case 1:
                                if (player.getSession() != null && player.getSession().vnd < cn.de10) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de10 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de10)) {
                                    PetService.gI().changeGoku4Pet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Goku SSJ4");
                                }
                                break;

                            case 2:
                                if (player.getSession() != null && player.getSession().vnd < cn.de11) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de11 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de11)) {

                                    PetService.gI().changeGokuUltraPet(player, player.gender);

                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Goku Vô Cực");
                                }
                                break;
                            case 3:
                                if (player.getSession() != null && player.getSession().vnd < cn.de12) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de12 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de12)) {
                                    PetService.gI().changeBerusBiNgoPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Berus Bí Ngô");
                                }
                                break;
                            case 4:
                                if (player.getSession() != null && player.getSession().vnd < cn.de13) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de13 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de13)) {
                                    PetService.gI().changeZamasuPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được Zamasu");
                                }
                                break;

                            case 5:
                                if (player.getSession() != null && player.getSession().vnd < cn.de14) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de14 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de14)) {

                                    PetService.gI().changeDaishinkanPet(player, player.gender);

                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Daisinkan");
                                }
                                break;
                            case 6:
                                if (player.getSession() != null && player.getSession().vnd < cn.de15) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de15 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de15)) {

                                    PetService.gI().changeWhisPet(player, player.gender);

                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Whis");
                                }
                                break;
                            case 7:
                                if (player.getSession() != null && player.getSession().vnd < cn.de16) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de16 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de16)) {
                                    PetService.gI().createGlanola(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Granola");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 784) {
                        switch (select) {
                            case 0:
                                if (player.getSession() != null && player.getSession().vnd < cn.de17) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de17 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de17)) {
                                    PetService.gI().createOren(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Oren");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 1:
                                if (player.getSession() != null && player.getSession().vnd < cn.de18) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de18 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de18)) {
                                    PetService.gI().createKamin(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Kamin");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;

                            case 2:
                                if (player.getSession() != null && player.getSession().vnd < cn.de19) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de19 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de19)) {
                                    PetService.gI().createDrabura(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Drabura");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 3:
                                if (player.getSession() != null && player.getSession().vnd < cn.de20) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de20 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de20)) {
                                    PetService.gI().createKaminOren(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ KamiOren");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 4:
                                if (player.getSession() != null && player.getSession().vnd < cn.de21) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de21 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de21)) {
                                    PetService.gI().createGojo(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ GOJO");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;

                            case 5:
                                if (player.getSession() != null && player.getSession().vnd < cn.de22) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de22 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de22)) {
                                    PetService.gI().createJirenBaby(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Baby Jiren");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 6:
                                if (player.getSession() != null && player.getSession().vnd < cn.de23) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de23 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de23)) {
                                    PetService.gI().createHatchiyack(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Hatchiyack");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 7:
                                if (player.getSession() != null && player.getSession().vnd < cn.de24) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de24 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de24)) {
                                    PetService.gI().createGokuSSJ5(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Goku SSJ 5");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == 785) {
                        switch (select) {
                            case 0:
                                if (player.getSession() != null && player.getSession().vnd < cn.de25) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de25 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de25)) {
                                    PetService.gI().createZamas2(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Zamas Cải Cách");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 1:
                                if (player.getSession() != null && player.getSession().vnd < cn.de26) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de26 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de26)) {
                                    PetService.gI().createBabyVegeta(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Baby Vegeta");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 2:
                                if (player.getSession() != null && player.getSession().vnd < cn.de27) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de27 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de27)) {
                                    PetService.gI().createKafula(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Kafula");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 3:
                                if (player.getSession() != null && player.getSession().vnd < cn.de28) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de28 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de28)) {
                                    PetService.gI().createUltraEgo(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Ultra Ego");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 4:
                                if (player.getSession() != null && player.getSession().vnd < cn.de29) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de29 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de29)) {
                                    PetService.gI().createCumberBase(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Cumber Base");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 5:
                                if (player.getSession() != null && player.getSession().vnd < cn.de30) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de30 + " VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de30)) {
                                    PetService.gI().createCumberSuper(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Cumber Super");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == 786) {
                        switch (select) {
                            case 0:
                                if (player.getSession() != null && player.getSession().vnd < cn.de31) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de31 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de31)) {
                                    PetService.gI().createDe3(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ OMEGA");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 1:
                                if (player.getSession() != null && player.getSession().vnd < cn.de31) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de31 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de31)) {
                                    PetService.gI().createDe1(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Zamasu DỊ");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 2:
                                if (player.getSession() != null && player.getSession().vnd < cn.de32) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de32 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de32)) {
                                    PetService.gI().createDe2(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Yachiro");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 3:
                                if (player.getSession() != null && player.getSession().vnd < cn.de33) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de33 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de33)) {
                                    PetService.gI().createDe4(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ GOKU SSJ4");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 4:
                                if (player.getSession() != null && player.getSession().vnd < cn.de34) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de34 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de34)) {
                                    PetService.gI().createDe5(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ VEGETA SSJ4");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 5:
                                if (player.getSession() != null && player.getSession().vnd < cn.de35) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de35 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de35)) {
                                    PetService.gI().createDe6(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ GOGETA SSJ4");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 6:
                                if (player.getSession() != null && player.getSession().vnd < cn.de35) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de35 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de35)) {
                                    PetService.gI().createDe7(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ TÔPPO");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                            case 7:
                                if (player.getSession() != null && player.getSession().vnd < cn.de36) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + cn.de36 + " COIN");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, cn.de36)) {
                                    PetService.gI().createDe8(player, player.pet != null, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ ZENO");
                                } else {
                                    Service.gI().sendThongBao(player, "Đã có lỗi xảy ra !!");
                                }

                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == 782) {
                        switch (select) {
                            case 0:
                                if (player.getSession() != null && player.getSession().actived) {
                                    Service.gI().sendThongBao(player, "Bạn đã mở thành viên rồi");
                                    return;
                                }
                                if (player.getSession() != null && player.getSession().totalvnd2 < 20000) {
                                    Service.gI().sendThongBao(player, "Cần nạp 20K để mở khóa giao dịch");
                                    return;
                                }
                                if (PlayerDAO.subvnd(player, 20000)) {
                                    player.getSession().actived = true;

                                    if (PlayerDAO.activedUser(player)) {
                                        Service.gI().sendThongBao(player, "Bạn đã mở thành viên thành công");
                                    } else {
                                        Service.gI().sendThongBao(player, "Đã có lỗi xẩy ra khi kích hoạt tài khoản, vui long liên hệ admin nếu bị trừ tiền mà không kích hoạt được, chụp lại thông báo này");
                                    }
                                }
                                break;
                            case 1:

                                break;

                        }
                    }
                }
            }
        };
    }

    public static Npc ThoRen(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi muốn rèn mọi món đồ thành item, vật phẩm hiếm không?",
                                "Rèn Item\n>Item hiếm");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {

                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {//                                                CombineService.gI().openTabCombine(player, CombineService.EP_SAO_TRANG_BI);
                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            if (player.combineNew.typeCombine == CombineServiceNew.EP_SAO_TRANG_BI) {//                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
//                                case CombineServiceNew.DAP_DO_AO_HOA:
//
////                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
//                                case CombineServiceNew.NANG_TL_LEN_HUY_DIET:
//                                case CombineServiceNew.NANG_HUY_DIET_LEN_SKH:
//                                case CombineServiceNew.NANG_HUY_DIET_LEN_SKH_VIP:
//                                case CombineServiceNew.PS_HOA_TRANG_BI:
//                                case CombineServiceNew.TAY_PS_HOA_TRANG_BI:

                                if (select == 0) {
                                    CombineServiceNew.gI().startCombine(player);
                                }
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?\n",
                                "Ép sao\ntrang bị",
                                "Pha lê\nhóa\ntrang bị");
                        //                                "Đập đồ\nẢo Hóa",
                        //                                "Build Đồ",
                        //                                "Pháp sư hoá",
                        //                                "Siêu hóa\n Cải trang",
                        // "Shop Bà Hạt Mít"
                        //                                "Tinh ấn\ntrang bị",
                        //                                "Tinh thạch\ntrang bị",
                        //                                "Nâng cấp\nGiáp LT"

                    } //                    else if (this.mapId == 121) {
                    //                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                    //                                "Ngươi tìm ta có việc gì?",
                    //                                "Về đảo\nKame");
                    //
                    //                    } 
                    else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm",
                                "Nâng cấp\nBông tai\nPorata",
                                "Làm phép\nNhập đá",
                                "Nhập\nNgọc Rồng",
                                "Phân Rã\nĐồ Thần Linh",
                                //                                "Nâng Cấp \nĐồ Thiên Sứ",
                                "Mở chỉ số Bông tai");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
//                                                CombineService.gI().openTabCombine(player, CombineService.EP_SAO_TRANG_BI);
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
//                                case 2:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DAP_DO_AO_HOA);
//                                    break;
//                                case 3:
//                                    this.createOtherMenu(player, 22233,
//                                            "|7|\bChi Tiết:\n"
//                                            + "\n|0|Thần Linh->Hủy diệt: tỉ lệ thành công 30% - Xịt mất đồ"
//                                            + "\nThần Linh->Hủy diệt: tỉ lệ thành công 30% - Xịt mất đồ"
//                                            + "\nHủy diệt->SKH: tỉ lệ thành công 30% - Xịt mất đồ"
//                                            + "\nHủy diệt->SKH VIP: tỉ lệ thành công 30% - Xịt mất đồ"
//                                            + "\nMở Khóa GD: tỉ lệ thành công 30%"
//                                            + "\nGia hạn Vật Phẩm: tỉ lệ thành công 30% + 3 - 7 ngày, 70% + 1 ngày"
//                                            + "\nTẩy đồ: tẩy sao pha lê, chỉ số đặc biệt một số trang bị",
//                                            "Thần Linh\n->Hủy diệt", "Hủy diệt\n->SKH", "Hủy diệt\n->SKH VIP", "Mở Khóa GD", "Gia hạn\n Vật Phẩm", "Tẩy Đồ"
//                                    );
//                                    break;
//                                case 4:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PS_HOA_TRANG_BI);
//                                    break;
//                                case 5:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.SIEU_HOA);
//                                    break;
//                                case 2: //nâng cấp Chân mệnh
//                                    this.createOtherMenu(player, 5701,
//                                            "|7|CHÂN MỆNH"
//                                            + "\n\n|1|Bạn đang có: " + Util.format(player.event.getEventPointBoss()) + " Điểm Săn Boss"
//                                            + "\n|3| Lưu ý: Chỉ được nhận Chân mệnh 1 lần (Hành trang chỉ tồn tại 1 Chân mệnh)"
//                                            + "\nNếu đã có Chân mệnh. Ta sẽ giúp ngươi nâng cấp bậc lên với các dòng chỉ số cao hơn",
//                                            player.event.getEventPointBoss() >= 1000 ? "Nhận Chân mệnh" : "Cần 1000 điểm\n để nhận", "Nâng cấp Chân mệnh", "Shop\nĐIỂM", "Shop\nBà Hạt Mít"
//                                    );
//                                    break;
//                                case 7:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI);
//                                    break;
//                                case 8:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TINH_THACH_HOA);
//                                    break;
//                                case 9:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_GIAP_LUYEN_TAP);
//                                    break;

                            }

                        } //                        else if (player.iDMark.getIndexMenu() == 22233) {
                        //                            switch (select) {
                        //                                case 0:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_TL_LEN_HUY_DIET);
                        //                                    break;
                        //                                case 1:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_HUY_DIET_LEN_SKH);
                        //                                    break;
                        //                                case 2:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_HUY_DIET_LEN_SKH_VIP);
                        //                                    break;
                        //                                case 3:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_KHOA_ITEM);
                        //                                    break;
                        //                                case 4:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.GIA_HAN_VAT_PHAM);
                        //                                    break;
                        //                                case 5:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PS_HOA_TRANG_BI);
                        //                                    break;
                        //                            }
                        //                        } 
                        //                        else if (player.iDMark.getIndexMenu() == 5701) {
                        //                            switch (select) {
                        //                                case 0:
                        //                                    for (int i = 0; i < 9; i++) {
                        //                                        Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 1185 + i);
                        //                                        Item findItemBody = InventoryServiceNew.gI().findItemBody(player, 1185 + i);
                        //                                        if (findItemBag != null || findItemBody != null) {
                        //                                            Service.gI().sendThongBao(player, "|7|Ngươi đã có Chân mệnh rồi mà");
                        //                                            return;
                        //                                        }
                        //                                    }
                        //                                    if (player.event.getEventPointBoss() >= 1000) {
                        //                                        player.event.subEventPointBoss(1000);
                        //                                        Item chanmenh = ItemService.gI().createNewItem((short) 1185);
                        //                                        chanmenh.itemOptions.add(new Item.ItemOption(0, 1000));
                        //                                        chanmenh.itemOptions.add(new Item.ItemOption(30, 1));
                        //                                        InventoryServiceNew.gI().addItemBag(player, chanmenh);
                        //                                        Service.gI().sendMoney(player);
                        //                                        InventoryServiceNew.gI().sendItemBags(player);
                        //                                        this.npcChat(player, "|1|Bạn nhận được Chân mệnh Cấp 1");
                        //                                    } else {
                        //                                        this.npcChat(player, "|1|Thiếu " + (1000 - player.event.getEventPointBoss() + " điểm săn boss\n Đi săn thêm đi nha"));
                        //                                    }
                        //                                    break;
                        //                                case 1:
                        //                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                        //                                    break;
                        //                                case 2:
                        //                                    ShopServiceNew.gI().opendShop(player, "SHOP_BA", false);
                        //                                    break;
                        //                                case 3:
                        //                                    ShopServiceNew.gI().opendShop(player, "SHOP_THOI", false);
                        //                                    break;
                        //                            }
                        //                        }
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
//                                case CombineServiceNew.DAP_DO_AO_HOA:
//                                case CombineServiceNew.NANG_TL_LEN_HUY_DIET:
//                                case CombineServiceNew.NANG_HUY_DIET_LEN_SKH:
//                                case CombineServiceNew.NANG_HUY_DIET_LEN_SKH_VIP:
//                                case CombineServiceNew.PS_HOA_TRANG_BI:
//                                case CombineServiceNew.TAY_PS_HOA_TRANG_BI:
//                                case CombineServiceNew.SIEU_HOA:
//                                case CombineServiceNew.MO_KHOA_ITEM:
//                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
//                                case CombineServiceNew.GIA_HAN_VAT_PHAM:
//                                case CombineServiceNew.AN_TRANG_BI:
//                                case CombineServiceNew.TINH_THACH_HOA:
//                                case CombineServiceNew.NANG_GIAP_LUYEN_TAP:
//                                    if (select == 0) {
//                                        CombineServiceNew.gI().startCombine(player);
//                                    }
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;
                                    }
                                    CombineServiceNew.gI().startCombine(player);
                                    break;
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    if (player.getSession() != null) {

                                        createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                                "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                                "Bùa\n1 giờ", "Bùa\n8 giờ", "Bùa\n1 tháng", "Đóng");
                                    }
                                    break;
                                case 1:
//                                                CombineService.gI().openTabCombine(player, CombineService.NANG_CAP_TRANG_BI);
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2: //nâng cấp bông tai
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                    break;
                                case 3: //làm phép nhập đá
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.LAM_PHEP_NHAP_DA);
                                    break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;
                                case 5: //phân rã đồ thần linh
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_DO_THAN_LINH);
                                    break;
//                                case 6:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_TS);
//                                    break;
                                case 6:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                case CombineServiceNew.LAM_PHEP_NHAP_DA:
                                case CombineServiceNew.NHAP_NGOC_RONG:
                                case CombineServiceNew.PHAN_RA_DO_THAN_LINH:
//                                case CombineServiceNew.NANG_CAP_DO_TS:
//                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
//                    else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_THAN_LINH) {
//                        if (select == 0) {
//                            CombineServiceNew.gI().startCombine(player);
//                        }
//                    } 
//                    else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
//                        if (select == 0) {
//                            CombineServiceNew.gI().startCombine(player);
//                        }
//                    }
                }
            }
        };
    }
//
//    public static Npc TRUNGTHU(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (player.getSession() != null) {
//                        createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
//                                "Cửa hàng");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 0 || this.mapId == 14) {
//                        if (player.iDMark.isBaseMenu()) {
//                            if (select == 0) { //shop
//                                ShopServiceNew.gI().opendShop(player, "TRUNGTHU", true);
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }

//    public static Npc HungVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
////            public void chatWithNpc(Player player) { 
////                String[] chat = { 
////                    "Chào mừng tới với NR EMTI",
////                    "Thừa tiền nạp thì ra đây đổi quà nào",
////                    "Mang cho ta ít tiền để đổi quà"
////
////                };
////                Timer timer = new Timer();
////                timer.scheduleAtFixedRate(new TimerTask() { 
////                    int index = 0;
////
////                    @Override
////                    public void run() { 
////                        npcChat(player, chat[index]);
////                        index = (index + 1) % chat.length;
////                    }
////                }, 10000, 10000);
////            }
//
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//                        if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
//                            PlayerService.gI().banPlayer((player));
//                            Service.gI().sendThongBao(player, "Bạn bị ban thành công");
//                            return;
//                        }
//                        if (player.getSession() != null) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                    "Ngươi muốn chơi EVENT không ?",
//                                    "Nâng cấp cải trang", "Đổi trang");
//
//                        }
//                    }
//                    if (this.mapId == 0) {
//                        if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
//                            PlayerService.gI().banPlayer((player));
//                            Service.gI().sendThongBao(player, "Bạn bị ban thành công");
//                            return;
//                        }
//                        if (!player.getSession().actived) {
//                            Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//                        } else {
//
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                    "Ngươi có muốn đổi tiền nạp ra vật phẩm không ?",
//                                    "Đổi Tiền Nạp", "Hòm Thư");
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//                        if (player.iDMark.isBaseMenu()) {
//                            if (select == 0) {//                                                CombineService.gI().openTabCombine(player, CombineService.EP_SAO_TRANG_BI);
//                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAI_TRANG);
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
//                            if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAI_TRANG) {
//                                if (select == 0) {
//                                    CombineServiceNew.gI().startCombine(player);
//                                }
//                            }
//                        }
//                    }
//                    if (this.mapId == 0) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                case 0:
//                                    if (player.getSession() != null) {
//                                        this.createOtherMenu(player, 911,
//                                                " Tổng nạp : " + player.getSession().totalvnd + " VND",
//                                                "Đổi Ngọc Rồng", "RamDom\n Sao Pha lê");
//                                    }
//                                    break;
//                                case 1:
//                                    if (player.getSession() != null) {
//                                        this.createOtherMenu(player, 912,
//                                                "Mở Hòm Thư Xem Có Quà Không Con?",
//                                                "Mở Hòm Thư");
//
//                                    }
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == 911) {
//                            switch (select) {
//                                case 0:
//                                    if (player.getSession() != null) {
//                                        this.createOtherMenu(player, 555,
//                                                "\b|1|Muốn đổi Ngọc Rồng à?"
//                                                + " Tổng nạp : " + player.getSession().totalvnd + " VND",
//                                                "20k\n1 viên\n3 Sao");
//                                    }
//                                    break;
//                                case 1:
//                                    if (player.getSession() != null) {
//                                        this.createOtherMenu(player, 556,
//                                                "\b|1|Muốn đổi Sao Pha lê à?"
//                                                + " Tổng nạp : " + player.getSession().totalvnd + " VND",
//                                                "5k\n1 viên\nngẫu nhiên");
//                                    }
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == 555) {
//                            //                                    if (player.getSession() != null && player.getSession().totalvnd < 20000) {
//                            //                                        Service.gI().sendThongBao(player, "Bạn không đủ 20k VND đã nạp");
//                            //                                        return;
//                            //                                    }
//                            //
//                            //                                    if (PlayerDAO.subtotalvnd(player, 20000)) {
//                            //
//                            //                                        Item i = ItemService.gI().createNewItem((short) 16, 1);
//                            //                                        InventoryServiceNew.gI().addItemBag(player, i);
//                            //                                        InventoryServiceNew.gI().sendItemBags(player);
//                            //                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 1 viên 3 sao");
//                            //                                    }
//                        } else if (player.iDMark.getIndexMenu() == 556) {
//                            //                                    if (player.getSession() != null && player.getSession().totalvnd < 5000) {
//                            //                                        Service.gI().sendThongBao(player, "Bạn không đủ 5k VND đã nạp");
//                            //                                        return;
//                            //                                    }
//                            //
//                            //                                    if (PlayerDAO.subtotalvnd(player, 5000)) {
//                            //
//                            //                                        Item i = ItemService.gI().createNewItem((short) 17, 1);
//                            //                                        InventoryServiceNew.gI().addItemBag(player, i);
//                            //                                        InventoryServiceNew.gI().sendItemBags(player);
//                            //                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 1 viên 4 sao");
//                            //                                    }
//                        }
//                    }
//
//                }
//            }
//        };
//    }
    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                canOpenNpc(player);
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 0, "Ngũ Hàng Sơn x2 Tnsm\nHỗ trợ cho Ae \b|1|Trên 1tr5 SM", "Vào");
                        }
                    }
                    if (mapId == 123) {
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Aru?", "Quay về");
                        }
                    }
                    if (mapId == 122) {
                        if (player.getSession() != null) {
                            this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: " + player.event.getEventPointNHS() + " điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang x4 chưởng ko?\b|3| Up quái sẽ được cộng điểm sự kiện tại Ngũ Hành Sơn", "Âu kê\n Mất 300 điểm", "Đổi Giải-Khai-Phong-Ấn", "No");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (select == 0) {
                        if (mapId == 0) {
                            if (player.nPoint.power < 1500000L) {
                                Service.gI().sendThongBao(player, "Sức mạnh bạn không phù hợp để qua map!");
                                return;
                            }
                            ChangeMapService.gI().changeMapInYard(player, 123, -1, -1);
                        }
                        if (mapId == 123) {
                            ChangeMapService.gI().changeMapInYard(player, 0, -1, -1);
                        }
                        if (mapId == 122) {

                            if (player.event.getEventPointNHS() > 300) {
                                player.event.subEventPointNHS(300);
                                if (Util.isTrue(30, 100)) {
                                    Item item = ItemService.gI().createNewItem((short) Util.nextInt(710, 711));
                                    item.itemOptions.add(new ItemOption(Util.nextInt(33), 1));
                                    item.itemOptions.add(new ItemOption(50, Util.nextInt(20, 40)));
                                    item.itemOptions.add(new ItemOption(77, Util.nextInt(20, 40)));
                                    item.itemOptions.add(new ItemOption(103, Util.nextInt(20, 40)));
                                    item.itemOptions.add(new ItemOption(207, 1));
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                                } else {
                                    Item item = ItemService.gI().createNewItem((short) Util.nextInt(2123, 2126));
                                    item.itemOptions.add(new ItemOption(30, 1));
                                    item.itemOptions.add(new ItemOption(93, Util.nextInt(1, 20)));

                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");

                                }
                            } else {
                                Service.gI().sendThongBao(player, "Không đủ 300 điểm");
                            }
                        }
                    }
                    if (select == 1) {
                        Item i1 = InventoryServiceNew.gI().findItemBag(player, 537);
                        Item i2 = InventoryServiceNew.gI().findItemBag(player, 538);
                        Item i3 = InventoryServiceNew.gI().findItemBag(player, 539);
                        Item i4 = InventoryServiceNew.gI().findItemBag(player, 540);
                        if (i1 != null && i2 != null && i3 != null && i4 != null) {
                            int sl = 99;
                            if (i1.quantity < sl) {
                                Service.gI().sendThongBao(player, "Còn thiếu " + (sl - i1.quantity) + " " + i1.template.name);
                                return;
                            }
                            if (i2.quantity < sl) {
                                Service.gI().sendThongBao(player, "Còn thiếu " + (sl - i2.quantity) + " " + i2.template.name);
                                return;
                            }
                            if (i3.quantity < sl) {
                                Service.gI().sendThongBao(player, "Còn thiếu " + (sl - i3.quantity) + " " + i3.template.name);
                                return;
                            }
                            if (i4.quantity < sl) {
                                Service.gI().sendThongBao(player, "Còn thiếu " + (sl - i4.quantity) + " " + i4.template.name);
                                return;
                            }
                            InventoryServiceNew.gI().subQuantityItemsBag(player, i1, sl);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, i2, sl);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, i3, sl);
                            InventoryServiceNew.gI().subQuantityItemsBag(player, i4, sl);
                            if (Util.isTrue(90, 100)) {
                                Item item = ItemService.gI().createNewItem((short) Util.nextInt(2000, 2002));
                                item.itemOptions.add(new ItemOption(Util.nextInt(33), 1));
                                InventoryServiceNew.gI().addItemBag(player, item);
                                Service.gI().sendThongBao(player, "Chúc mừng Bạn nhận được " + item.template.name + " ");
                            } else {
                                Item item = ItemService.gI().createNewItem((short) Util.nextInt(2123, 2126));
                                item.itemOptions.add(new ItemOption(Util.nextInt(33), 1));
                                InventoryServiceNew.gI().addItemBag(player, item);
                                Service.gI().sendThongBao(player, "Chúc mừng Bạn nhận được " + item.template.name + " ");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không có chữ nào");
                        }
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 11) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {

                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Cần làm đến nhiệm vụ Tiểu đội sát thủ");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 102) {

                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nQuá khứ");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai //
                        changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Có lỗi xảy ra tại Calich");
                    }
                }
            }
        };
    }

    public static Npc VegetaBNTT(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 174) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        this.mapId = map.mapId;
                        this.cx = Util.nextInt(100, map.mapWidth - 100);
                        this.cy = map.yPhysicInTop(this.cx, 0);
                        this.map = map;
                        this.map.npcs.add(this);
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Hoàn thành nv Kuku để tới");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Vegita Bản Ngã đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }

                if (this.mapId == 174) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào chú, cháu có thể giúp gì?",
                            "Kể\nChuyện", "Quay về\nnhà");
                } else {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào ngươi, ta có thể giúp gì?", "Kể\nChuyện", "Đi\n tập luyện", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 174) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.VEGETA_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().changeMapBySpaceShip(player, 4, -1, -1);

                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.VEGETA_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai //
                        changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 164, -1, -1);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Hoàn thành nhiệm vụ kuku trước");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    } else if (this.mapId == 213 || this.mapId == 215) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi muốn làm gì?", "Quay về\nĐảo Kame", "Xem quà", "Nhận quà \nđiểm sự kiện", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến potaufeu
                                ChangeMapService.gI().goToPotaufeu(player);
                            }
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            //về trạm vũ trụ
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                            }
                        }
                    } else if (this.mapId == 213 || this.mapId == 215) {
                        if (player.iDMark.isBaseMenu()) {
                            //về trạm vũ trụ
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, -1);
                            }
                            if (select == 1) {

                                Service.gI().sendBigMessage(player, avartar,
                                        "|3|Mốc 500 điểm:"
                                        + " x" + mocdiem.sli10_1 + " " + ItemService.gI().getTemplate(mocdiem.i10_1).name + ",\n"
                                        + " x" + mocdiem.sli10_2 + " " + ItemService.gI().getTemplate(mocdiem.i10_2).name + "\n"
                                        + "|2|Mốc 1000 điểm:"
                                        + " x" + mocdiem.sli20_1 + " " + ItemService.gI().getTemplate(mocdiem.i20_1).name + ",\n"
                                        + " x" + mocdiem.sli20_2 + " Bộ ngọc rồng\n"
                                        + "|1|Mốc 2000 điểm:"
                                        + " x" + mocdiem.sli30_1 + " " + ItemService.gI().getTemplate(mocdiem.i30_1).name + ",\n"
                                        + " x" + mocdiem.sli30_2 + " " + ItemService.gI().getTemplate(mocdiem.i30_2).name + "\n"
                                        + "|4|Mốc 5000 điểm: "
                                        + " x" + mocdiem.sli50_1 + " " + ItemService.gI().getTemplate(mocdiem.i50_1).name + ",\n"
                                        + ItemService.gI().getTemplate(mocdiem.i50_2).name + " " + mocdiem.csi50_2 + "% cs\n"
                                        + "|6|Mốc 20000 điểm: "
                                        + " x" + mocdiem.sli70_1 + " " + ItemService.gI().getTemplate(mocdiem.i70_1).name + " " + mocdiem.csi70_1 + "% cs,\n"
                                        + " x" + mocdiem.sli70_2 + " " + ItemService.gI().getTemplate(mocdiem.i70_2).name + ", " + ItemService.gI().getTemplate(mocdiem.i70_3).name + ", \n"
                                        + " x1 " + ItemService.gI().getTemplate(mocdiem.i70_4).name + "\n"
                                        + "|5|Mốc 30000 điểm: "
                                        + " x" + mocdiem.sli100_1 + " " + ItemService.gI().getTemplate(mocdiem.i100_1).name + ",\n"
                                        + " x" + mocdiem.sli100_2 + " " + ItemService.gI().getTemplate(mocdiem.i100_2).name + ",\n"
                                        + " x" + mocdiem.sli100_3 + " " + ItemService.gI().getTemplate(mocdiem.i100_3).name + "\n"
                                        + "|7|Mốc 40000 điểm: "
                                        + " x" + mocdiem.sli200_1 + " " + ItemService.gI().getTemplate(mocdiem.i200_1).name + " " + mocdiem.csi200_1 + "% cs,\n"
                                        + " x" + mocdiem.sli200_2 + " " + ItemService.gI().getTemplate(mocdiem.i200_2).name + ",\n"
                                        + " x" + mocdiem.sli200_3 + " " + ItemService.gI().getTemplate(mocdiem.i200_3).name + "\n"
                                        + "|7|Mốc 50000 điểm: "
                                        + " x" + mocdiem.sli300_1 + " " + ItemService.gI().getTemplate(mocdiem.i300_1).name + ",\n"
                                        + "x" + mocdiem.sli300_2 + " " + ItemService.gI().getTemplate(mocdiem.i300_2).name + ",\n"
                                        + "x" + mocdiem.sli300_3 + " " + ItemService.gI().getTemplate(mocdiem.i300_3).name + "\n"
                                        + "\b|7|Mốc 100000 điểm: "
                                        + " x" + mocdiem.sli500_1 + " " + ItemService.gI().getTemplate(mocdiem.i500_1).name + " " + mocdiem.csi500_1 + "% cs, " + mocdiem.csi500_2 + "% sđ đẹp,\n"
                                        + " x" + mocdiem.sli500_2 + " " + ItemService.gI().getTemplate(mocdiem.i500_2).name + ",\n"
                                        + " x" + mocdiem.sli500_3 + " " + ItemService.gI().getTemplate(mocdiem.i500_3).name + ",\n"
                                        + " x" + mocdiem.sli500_4 + " " + ItemService.gI().getTemplate(mocdiem.i500_4).name + "\n"
                                );
                            }
                            if (select == 2) {
                                Archivement_diem.gI().getAchievement_mocsk20_10(player);
                            }
                        }
                    }
                }
            }
        };
    }

    //public static Npc Potage(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) { 
//            @Override
//            public void openBaseMenu(Player player) { 
//                if (canOpenNpc(player)) { 
//                    if (this.mapId == 149) { 
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "tét", "Gọi nhân bản");
//                    }
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) { 
//                if (canOpenNpc(player)) { 
//                   if (select == 0){ 
//                        BossManager.gI().createBoss(-214);
//                   }
//                }
//            }
//        };
//    }
//    public static Npc noibanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
//                    PlayerService.gI().banPlayer((player));
//                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
//                    return;
//                }
//                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 5) {
//                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                            "|3|Hãy đem đến cho ta:\n+x80 thúng nếp\n+x80 thịt heo\n+x80 thúng đậu xanh\n+x80 là dong\n+x1 thỏi vàng"
//                            + "\n|6|Ta sẽ nấu cho ngươi bánh tét với tác dụng ngầu nhiên tăng từ 20% chỉ số\nNgoài ra người còn có thể dùng x99 nguyên liệu và 5 thỏi vàng để chế thành 10 thỏi vàng\n(Bánh chưng tăng 30% x99 mỗi loại nguyên liệu đầu vào và x5 thòi vàng)", "Nấu\nbánh tét", "Nấu\nbánh chưng", "Nấu\nThỏi vàng");
//
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                case 0: {
//                                    Item thitheo = null;
//                                    Item thungnep = null;
//                                    Item thungdxanh = null;
//                                    Item ladong = null;
//                                    Item thoivang = null;
//
//                                    try {
//                                        thitheo = InventoryServiceNew.gI().findItemBag(player, 748);
//                                        thungnep = InventoryServiceNew.gI().findItemBag(player, 749);
//                                        thungdxanh = InventoryServiceNew.gI().findItemBag(player, 750);
//                                        ladong = InventoryServiceNew.gI().findItemBag(player, 751);
//                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
////                                        throw new RuntimeException(e);
//                                    }
//                                    if (thitheo == null || thitheo.quantity < 80 || thungnep == null || thungnep.quantity < 80 || thungdxanh == null || thungdxanh.quantity < 80 || ladong == null || ladong.quantity < 80) {
//                                        this.npcChat(player, "Bạn không đủ 80 nguyên liệu các loại để nấu bánh");
//                                    } else if (thoivang == null || thoivang.quantity < 1) {
//                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
//                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                    } else {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thitheo, 80);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungnep, 80);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungdxanh, 80);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ladong, 80);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 1);
//                                        Service.gI().sendMoney(player);
//                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 752);
//                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        this.npcChat(player, "Bạn nhận được 1 bánh tét");
//                                    }
//                                    break;
//                                }
//                                case 1: {
//                                    Item thitheo = null;
//                                    Item thungnep = null;
//                                    Item thungdxanh = null;
//                                    Item ladong = null;
//                                    Item thoivang = null;
//
//                                    try {
//                                        thitheo = InventoryServiceNew.gI().findItemBag(player, 748);
//                                        thungnep = InventoryServiceNew.gI().findItemBag(player, 749);
//                                        thungdxanh = InventoryServiceNew.gI().findItemBag(player, 750);
//                                        ladong = InventoryServiceNew.gI().findItemBag(player, 751);
//                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
////                                        throw new RuntimeException(e);
//                                    }
//                                    if (thitheo == null || thitheo.quantity < 99 || thungnep == null || thungnep.quantity < 99 || thungdxanh == null || thungdxanh.quantity < 99 || ladong == null || ladong.quantity < 99) {
//                                        this.npcChat(player, "Bạn không đủ 99 nguyên liệu các loại để nấu bánh");
//                                    } else if (thoivang == null || thoivang.quantity < 1) {
//                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
//                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                    } else {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thitheo, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungnep, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungdxanh, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ladong, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 1);
//                                        Service.gI().sendMoney(player);
//                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 753);
//                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        this.npcChat(player, "Bạn nhận được 1 bánh chưng");
//                                    }
//                                    break;
//                                }
//                                case 2: {
//                                    Item thitheo = null;
//                                    Item thungnep = null;
//                                    Item thungdxanh = null;
//                                    Item ladong = null;
//                                    Item thoivang = null;
//
//                                    try {
//                                        thitheo = InventoryServiceNew.gI().findItemBag(player, 748);
//                                        thungnep = InventoryServiceNew.gI().findItemBag(player, 749);
//                                        thungdxanh = InventoryServiceNew.gI().findItemBag(player, 750);
//                                        ladong = InventoryServiceNew.gI().findItemBag(player, 751);
//                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
////                                        throw new RuntimeException(e);
//                                    }
//                                    if (thitheo == null || thitheo.quantity < 99 || thungnep == null || thungnep.quantity < 99 || thungdxanh == null || thungdxanh.quantity < 99 || ladong == null || ladong.quantity < 99) {
//                                        this.npcChat(player, "Bạn không đủ 99 nguyên liệu các loại để nấu thỏi vàng");
//                                    } else if (thoivang == null || thoivang.quantity < 5) {
//                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
//                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
//                                    } else {
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thitheo, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungnep, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thungdxanh, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ladong, 99);
//                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
//                                        Service.gI().sendMoney(player);
//                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 457, 10);
//                                        trungLinhThu.itemOptions.add(new Item.ItemOption(93, 10));
//                                        trungLinhThu.itemOptions.add(new Item.ItemOption(30, 1));
//
//                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        this.npcChat(player, "Bạn nhận được 10 thỏi vàng");
//                                    }
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    public static Npc Monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            final int item1 = 748;
            final int item2 = 749;
            final int item3 = 750;
            final int item4 = 751;

            final int sl = 10;

            @Override

            public void openBaseMenu(Player player) {
                createOtherMenu(player, ConstNpc.BASE_MENU,
                        "|5|Sự Kiện TẾT đang diễn ra bạn muốn đổi Gì đó chứ ?\n"
                        + "Đổi lấy danh hiệu coll ngầu đê !!"
                        + "Có tỉ lệ lấy được cải trang tiềm năng sức mạnh cao!",
                        "Đổi 10\n" + ItemService.gI().getTemplate(item1).name,
                        "Đổi 10\n" + ItemService.gI().getTemplate(item2).name,
                        "Đổi 10\n" + ItemService.gI().getTemplate(item3).name,
                        "Đổi 10\n" + ItemService.gI().getTemplate(item4).name + "\n- Danh hiệu",
                        "Đổi 30\n" + ItemService.gI().getTemplate(item4).name + "\n- Thỏi vàng");

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                Item voOc = InventoryServiceNew.gI().findItemBag(player, item1);
                                if (voOc != null) {
                                    if (voOc.quantity < sl) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ: " + voOc.template.name);
                                        return;
                                    }
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, voOc, sl);
                                    Item voOcSK = ItemService.gI().createNewItem((short) Util.nextInt(945, 948));
                                    voOcSK.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 5)));
                                    voOcSK.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 50)));
                                    voOcSK.itemOptions.add(new Item.ItemOption(Util.nextInt(0, 170), Util.nextInt(10, 25)));
                                    if (Util.isTrue(1, 10)) {
                                        voOcSK.itemOptions.add(new Item.ItemOption(101, Util.nextInt(30, 105)));
                                    }
                                    voOcSK.quantity = 1;
                                    InventoryServiceNew.gI().addItemBag(player, voOcSK);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được: " + voOcSK.template.name);

                                } else {
                                    Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(item1).name);
                                }
                                break;
                            case 1:
                                Item saobien = InventoryServiceNew.gI().findItemBag(player, item2);
                                if (saobien != null) {
                                    if (saobien.quantity < sl) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ: " + saobien.template.name);
                                        return;
                                    }
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, saobien, sl);
                                    Item saoBienSK = ItemService.gI().createNewItem((short) Util.nextInt(957, 959));
                                    saoBienSK.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 5)));
                                    saoBienSK.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 50)));
                                    saoBienSK.itemOptions.add(new Item.ItemOption(Util.nextInt(0, 170), Util.nextInt(10, 25)));
                                    if (Util.isTrue(1, 10)) {
                                        saoBienSK.itemOptions.add(new Item.ItemOption(101, Util.nextInt(30, 105)));
                                    }
                                    saoBienSK.quantity = 1;
                                    InventoryServiceNew.gI().addItemBag(player, saoBienSK);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa đổi được: " + saoBienSK.template.name);

                                } else {

                                    Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(item2).name);
                                }
                                break;
                            case 2:
                                Item concua = InventoryServiceNew.gI().findItemBag(player, item3);
                                if (concua != null) {
                                    if (concua.quantity < sl) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ " + concua.template.name);
                                        return;
                                    }
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, concua, sl);
                                    Item saoBienSK = ItemService.gI().createNewItem((short) Util.nextInt(282, 292));
                                    saoBienSK.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 5)));
                                    saoBienSK.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 50)));
                                    saoBienSK.itemOptions.add(new Item.ItemOption(Util.nextInt(0, 170), Util.nextInt(10, 25)));
                                    if (Util.isTrue(1, 10)) {
                                        saoBienSK.itemOptions.add(new Item.ItemOption(101, Util.nextInt(30, 105)));
                                    }
                                    saoBienSK.quantity = 1;
                                    InventoryServiceNew.gI().addItemBag(player, saoBienSK);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa đổi được: " + saoBienSK.template.name);

                                } else {
                                    Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(item3).name);
                                }
                                break;
                            case 3:
                                Item concua1 = InventoryServiceNew.gI().findItemBag(player, item4);
                                if (concua1 != null) {
                                    if (concua1.quantity < sl) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ " + concua1.template.name);
                                        return;
                                    }
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, concua1, sl);
                                    Item saoBienSK = ItemService.gI().createNewItem((short) Util.nextInt(381, 384));
                                    saoBienSK.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 5)));
                                    if (Util.isTrue(1, 10)) {
                                        saoBienSK.itemOptions.add(new Item.ItemOption(101, Util.nextInt(30, 105)));
                                    }
                                    saoBienSK.quantity = 1;
                                    InventoryServiceNew.gI().addItemBag(player, saoBienSK);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa đổi được: " + saoBienSK.template.name);

                                } else {
                                    Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(item4).name);
                                }
                                break;
                            case 4:
                                Item concu = InventoryServiceNew.gI().findItemBag(player, item4);
                                if (concu != null) {
                                    if (concu.quantity < 30) {
                                        Service.gI().sendThongBao(player, "Bạn không đủ " + concu.template.name);
                                        return;
                                    }
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, concu, 30);
                                    Item saoBienSK = ItemService.gI().createNewItem((short) 457);
                                    saoBienSK.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 5)));
                                    saoBienSK.quantity = 10;
                                    InventoryServiceNew.gI().addItemBag(player, saoBienSK);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa đổi được: " + saoBienSK.template.name);

                                } else {
                                    Service.gI().sendThongBao(player, "Không tìm thấy: " + ItemService.gI().getTemplate(item4).name);
                                }
                                break;

                        }
                    }
                }
            }

        };

    }

//    public static Npc ongbut(int mapId, int status, int cx, int cy, int tempId, int avartar) { 
//        return new Npc(mapId, status, cx, cy, tempId, avartar) { 
//            @Override
//            public void openBaseMenu(Player player) { 
//                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) { 
//                    PlayerService.gI().banPlayer((player));
//                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
//                    return;
//                }
//                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) { 
//                    if (player.getSession() != null) { 
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Hiện tại ta sẽ bán cải trang cho các ngươi", "Cửa hàng");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) { 
//                if (canOpenNpc(player)) { 
//                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) { 
//                        if (player.iDMark.isBaseMenu()) { 
//                            switch (select) { 
//                                case 0:
//                                    ShopServiceNew.gI().opendShop(player, "SHOP_HUNG_VUONG", false);
//                                    break;                                 
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
//    public static Npc conSoMayMan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                String plWin = MiniGame.gI().MiniGame_S1.result_name;
//                String KQ = MiniGame.gI().MiniGame_S1.result + "";
//                String Money = MiniGame.gI().MiniGame_S1.money + "";
//                String count = MiniGame.gI().MiniGame_S1.players.size() + "";
//                String second = MiniGame.gI().MiniGame_S1.second + "";
//                String number = MiniGame.gI().MiniGame_S1.strNumber((int) player.id);
//                String npcSay = ""
//                        + "Kết quả giải trước: " + KQ + "\n"
//                        + (plWin != null ? "Thắng giải trước: " + plWin + "\n" : "")
//                        //                        + "Tham gia: " + count + " tổng giải thưởng: " + Money + " ngọc\n"
//                        + "|5|Tham gia: " + count + " số\b"
//                        + "|3|Giải thưởng: Xem tại Giải thưởng\b"
//                        + "Bạn đang có: " + player.getSession().totalvnd + " VND\n"
//                        + "<" + second + ">giây\n"
//                        + (number != "" ? "Các số bạn chọn: " + number : "")
//                        + "Không trúng số sẽ nhận quà an ủi ngẫu nhiên:\n- 10~15 thỏi vàng -\nhoặc 3 túi ngọc xanh\n- hoặc 3 túi ngọc hồng ";
//                String[] Menus = {
//                    "1 Số\n1000\nVND ",
//                    "Hướng\ndẫn\nthêm",
//                    "Xem\ngiải thưởng",
//                    "Đóng"
//                };
//                createOtherMenu(player, 0, npcSay, Menus);
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                switch (select) {
//                    case 0:
//                        if (player.iDMark.getIndexMenu() == 0) {
//                            Input.gI().Consomayman(player);
//                        }
//                        break;
//                    case 1:
//                        if (player.iDMark.getIndexMenu() == 0) {
//                            String npcSay = ""
//                                    + "Thời gian từ " + Game.TIME_START_GAME + "h đến hết "
//                                    + (Game.TIME_END_GAME - 1) + "h59 hàng ngày\n"
//                                    + "Mỗi lượt được chọn 10 con số từ " + MiniGame.gI().MiniGame_S1.min
//                                    + "-" + MiniGame.gI().MiniGame_S1.max + "\n"
//                                    + "Thời gian mỗi lượt là " + Game.TIME_MINUTES_GAME + " phút.";
//                            createOtherMenu(player, 1, npcSay, "Đồng ý");
//                        }
//                        break;
//                    case 2:
//                        ShopServiceNew.gI().opendShop(player, "SHOP_CSMM", false);
//                        break;
//
//                }
//            }
////
//        };
//    }
//    public static Npc npclytieunuongCLTX(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (player.rubyWin > 0) {
//                        createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Chẵn lẻ đê.\n "
//                                + "Bạn đã thắng được " + Util.numberToMoney((int) (player.rubyWin * 1.5)) + " thỏi vàng\n"
//                                + "|5|Chú ý : Chỉ cược 1 lần tối đa 10k thỏi vàng, mọi sự mất mát ad không giải quyết!",
//                                "Chẵn", "Lẻ", "Xem \nlịch sử\nbản thân", "Xem lịch sử",
//                                "Nhận\nphần thưởng");
//                    } else {
//                        createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Chẵn lẻ đê.\n Bạn chưa thắng cược lần nào\n"
//                                + "|5|Chú ý : Chỉ cược 1 lần tối đa 10k thỏi vàng, mọi sự mất mát ad không giải quyết!",
//                                "Chẵn", "Lẻ", "Xem \nlịch sử\nbản thân", "Xem lịch sử");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//
//                        if (!player.getSession().actived) {
//                            Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//
//                        } else if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//
//                                case 0:
//                                    if (!PariryServices.gI().checkHavePariry()) {
//                                        Service.gI().sendThongBaoOK(player, "Chưa có phiên chẵn lẻ nào khởi động");
//                                        break;
//                                    }
//                                    Input.gI().CHAN(player);
//                                    break;
//                                case 1:
//                                    if (!PariryServices.gI().checkHavePariry()) {
//                                        Service.gI().sendThongBaoOK(player, "Chưa có phiên chẵn lẻ nào khởi động");
//                                        break;
//                                    }
//                                    Input.gI().LE(player);
//                                    break;
//                                case 2:
//                                    Service.gI().sendThongBaoOK(player,
//                                            PariryServices.gI().getHistoryPlayer(player));
//                                    break;
//                                case 3:
//                                    Service.gI().sendThongBaoOK(player,
//                                            PariryServices.gI().getHistory());
//                                    break;
//                                case 4:
//                                    if (player.rubyWin <= 0) {
//                                        Service.gI().sendThongBaoOK(player, "Có cái nịt mà nhận");
//                                        break;
//                                    }
//                                    PariryServices.gI().rewardRuby(player);
//                                    break;
//
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào",
                                "VQMM", "Đến Kaio");
                    }
                    if (this.mapId == 0) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?\nCon đang còn : " + player.pointPvp + " điểm PvP Point", "Đến DHVT", "Đổi Cải trang sự kiên", "Top PVP", "Ra đảo Kame");
                    }
                    if (this.mapId == 129) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?", "Quay về");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 0) {
                        if (player.iDMark.getIndexMenu() == 0) {  // 
                            switch (select) {

                                case 0:
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "You chưa là cư dân");
                                    } else {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 129, -1, 354);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    }
                                    break;                                  // qua dhvt
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang HatchiYack với tất cả chỉ số là 40%\n ", "Ok", "Tu choi");
                                    // bat menu doi item
                                    break;

                                case 2:  // 
                                    //Service.gI().showListTop(player, Manager.topPVP);
                                    // mo top pvp
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 354);
                                    break;                                  // dao kame

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) {  // action doi item
                            if (select == 0) { // trade
                                if (player.pointPvp >= 500) {
                                    player.pointPvp -= 500;
                                    Item item = ItemService.gI().createNewItem((short) (729));
                                    item.itemOptions.add(new ItemOption(50, 40));
                                    item.itemOptions.add(new ItemOption(77, 40));
                                    item.itemOptions.add(new ItemOption(103, 40));
                                    item.itemOptions.add(new ItemOption(207, 0));
                                    item.itemOptions.add(new ItemOption(33, 0));
//
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                    Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                } else {
                                    Service.gI().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                }
                            }
                        }
                    }
                    if (this.mapId == 129) {
                        if (select == 0) { // quay ve
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
                        }
                    }
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 2:
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
//                                case 1:
//                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GEM);
//                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }

                    }
                }
            }
        };
    }

//    public static Npc VongQuayMayMan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 45) {
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                                "Con muốn làm gì nào",
//                                "Đến Kaio", "VQMM");
//                    }
//                    if (this.mapId == 0) {
//                        this.createOtherMenu(player, 0,
//                                "Con muốn gì nào?\nCon đang còn : " + player.pointPvp + " điểm PvP Point", "Đến DHVT", "Đổi Cải trang sự kiên", "Top PVP", "Ra đảo Kame");
//                    }
//                    if (this.mapId == 129) {
//                        this.createOtherMenu(player, 0,
//                                "Con muốn gì nào?", "Quay về");
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 0) {
//                        if (player.iDMark.getIndexMenu() == 0) {  // 
//                            switch (select) {
//
//                                case 0:
//                                    if (!player.getSession().actived) {
//                                        Service.gI().sendThongBao(player, "You chưa là cư dân của NR BLUE");
//                                    } else {
//                                        ChangeMapService.gI().changeMapBySpaceShip(player, 129, -1, 354);
//                                        Service.gI().changeFlag(player, Util.nextInt(8));
//                                    }
//                                    break;                                  // qua dhvt
//                                case 1:  // 
//                                    this.createOtherMenu(player, 1,
//                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang HatchiYack với tất cả chỉ số là 40%\n ", "Ok", "Tu choi");
//                                    // bat menu doi item
//                                    break;
//
//                                case 2:  // 
//                                    // Service.gI().showListTop(player, Manager.topPVP);
//                                    // mo top pvp
//                                    break;
//                                case 3:
//                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 354);
//                                    break;                                  // dao kame
//
//                            }
//                        }
//                        if (player.iDMark.getIndexMenu() == 1) {  // action doi item
//                            if (select == 0) { // trade
//                                if (player.pointPvp >= 500) {
//                                    player.pointPvp -= 500;
//                                    Item item = ItemService.gI().createNewItem((short) (729));
//                                    item.itemOptions.add(new ItemOption(50, 40));
//                                    item.itemOptions.add(new ItemOption(77, 40));
//                                    item.itemOptions.add(new ItemOption(103, 40));
//                                    item.itemOptions.add(new ItemOption(207, 0));
//                                    item.itemOptions.add(new ItemOption(33, 0));
////
//                                    InventoryServiceNew.gI().addItemBag(player, item);
//                                    Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
//                                } else {
//                                    Service.gI().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
//                                }
//                            }
//                        }
//                    }
//                    if (this.mapId == 129) {
//                        if (select == 0) { // quay ve
//                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
//                        }
//                    }
//                    if (this.mapId == 45) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                case 0:
//                                    break;
//                                case 1:
//                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
//                                    break;
//                                case 2:
//                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
//                                            "Con muốn làm gì nào?", "Quay bằng\nvàng", "Quay bằng\nngọc",
//                                            "Rương phụ\n("
//                                            + (player.inventory.itemsBoxCrackBall.size()
//                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
//                                            + " món)",
//                                            "Xóa hết\ntrong rương", "Đóng");
//                                    break;
//                            }
//                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
//                            switch (select) {
//                                case 0:
//                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
//                                    break;
//                                case 1:
//                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GEM);
//                                    break;
//                                case 2:
//                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
//                                    break;
//                                case 3:
//                                    NpcService.gI().createMenuConMeo(player,
//                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
//                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
//                                            + "sẽ không thể khôi phục!",
//                                            "Đồng ý", "Hủy bỏ");
//                                    break;
//                            }
//                        }
//
//                    }
//                }
//            }
//        };
//    }
    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                        "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc", "Từ chối");
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    if (player.clan == null) {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Vào bang hội trước", "Đóng");
                                        return;
                                    }
//                                int nPlSameClan = 0;
//                                for (Player pl : player.zone.getPlayers()) {
//                                    if (!pl.equals(player) && pl.clan != null
//                                            && pl.clan.equals(player.clan) && pl.location.x >= 1285
//                                            && pl.location.x <= 1645) {
//                                        nPlSameClan++;
//                                    }
//                                }
//                                if (nPlSameClan < BanDoKhoBau.N_PLAYER_MAP) {
//                                    createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                            "Ngươi phải có ít nhất " + BanDoKhoBau.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
//                                            + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
//                                            + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
//                                    break;
//                                }
                                    if (player.clan.getMembers().size() < ConDuongRanDoc.N_PLAYER_CLAN) {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội phải có ít nhất " + ConDuongRanDoc.N_PLAYER_CLAN + " thành viên mới có thể mở", "Đóng");
                                        return;
                                    }
                                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Con đường rắn độc chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                                "OK");
                                        return;
                                    }

                                    if (player.cdrd_countPerDay >= 3) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Con đã đạt giới hạn lượt đi trong ngày",
                                                "OK");
                                        return;
                                    }

//                                    player.clan.haveGoneConDuongRanDoc = !(System.currentTimeMillis() - player.clan.lastTimeOpenConDuongRanDoc > 120000);
//                                    if (player.clan.haveGoneConDuongRanDoc) {
//                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                                "Bang hội của con đã đi Con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
//                                                + "(" + player.clan.CDRD_playerOpen + "). Hẹn con sau 2 phút nữa", "OK");
//                                        return;
//                                    }
                                    if (player.clan.ConDuongRanDoc != null) {
                                        createOtherMenu(player, ConstNpc.MENU_OPENED_CDRD,
                                                "Bang hội của con đang tham gia Con đường rắn độc cấp độ " + player.clan.ConDuongRanDoc.level + "\n"
                                                + "Thời gian còn lại là "
                                                + TimeUtil.getSecondLeft(player.clan.ConDuongRanDoc.getLastTimeOpen(), ConDuongRanDoc.TIME_CDRD / 1000)
                                                + " giây. Con có muốn tham gia không?",
                                                "Tham gia", "Không");
                                        return;
                                    }
                                    Input.gI().createFormChooseLevelCDRD(player);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_CDRD) {
                            if (select == 0) {
                                ConDuongRanDocService.gI().joinCDRD(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_CDRD) {
                            if (select == 0) {
                                if (player.isAdmin() || player.nPoint.power >= ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                    Input.gI().createFormChooseLevelCDRD(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                }
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                            if (select == 0) {
                                Object level = PLAYERID_OBJECT.get(player.id);
                                ConDuongRanDocService.gI().openCDRD(player, (int) level);
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Từ chối");
                    } else if (this.mapId == 135) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn có muốn về đảo kame không??",
                                "Đi chứ\nhehe", "Từ chối");
                    }
                    if (this.mapId == 114) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                    if (this.mapId == 52) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                            }
                        }
                    } else if (this.mapId == 135) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 5, -1, 335, 288);
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                    } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                    } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 1:
                                    if (player.nPoint.power < 60000000000L) {
                                        Service.gI().sendThongBao(player, "Đạt đủ 60 tỉ sức mạnh mới có thể vào map ");
                                    } else {
                                        ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                        break;
                                    }
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
                                    if (!player.getSession().actived) {
                                        Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                    }
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc docNhan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai", "OK");
                        return;
                    }

                    boolean flag = true;
                    for (Mob mob : player.zone.mobs) {
                        if (!mob.isDie()) {
                            flag = false;
                        }
                    }
                    for (Player boss : player.zone.getBosses()) {
                        if (!boss.isDie()) {
                            flag = false;
                        }
                    }

                    if (flag) {
                        player.clan.doanhTrai_haveGone = true;
                        player.clan.doanhTrai.setLastTimeOpen(System.currentTimeMillis() + 290_000);
                        player.clan.doanhTrai.DropNgocRong();
                        for (Player pl : player.clan.membersInGame) {
                            ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Doanh trại độc nhãn sắp kết thúc : ", 300);
                        }
                        player.clan.doanhTrai.timePickDragonBall = true;
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ta đã thả ngọc rồng ở tất cả các map,mau đi nhặt đi. Hẹn ngươi quay lại vào ngày mai", "OK");
                    } else {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy tiêu diệt hết quái và boss trong map", "OK");
                    }

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + TimeUtil.getSecondLeft(player.clan.doanhTrai.getLastTimeOpen(), DoanhTrai.TIME_DOANH_TRAI / 1000)
                                + ". Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
//                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
//                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
//                                "Doanh trại chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
//                                "OK", "Hướng\ndẫn\nthêm");
//                        return;
//                    }

                    if (!player.clan.doanhTrai_haveGone) {
                        // Chuyển đổi từ long (milliseconds) sang LocalDate
                        LocalDate lastTimeOpen = Instant.ofEpochMilli(player.clan.doanhTrai_lastTimeOpen)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        LocalDate today = LocalDate.now();

                        // So sánh ngày của hai LocalDate
                        player.clan.doanhTrai_haveGone = lastTimeOpen.isEqual(today);
                    }

                    if (player.clan.doanhTrai_haveGone) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.doanhTrai_lastTimeOpen, "HH:mm:ss") + " hôm nay. Người mở\n"
                                + "(" + player.clan.doanhTrai_playerOpen + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().joinDoanhTrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 500000;

            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    player.mabuEgg.sendMabuEgg();
                    if (player.mabuEgg.getSecondDone() != 0) {
                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " VND", "Đóng");
                    } else {
                        this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.CAN_NOT_OPEN_EGG:
                            if (select == 0) {
                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư Gầy?", "Đồng ý", "Từ chối");
                            } else if (select == 1) {
                                if (player.getSession() != null && player.getSession().totalvnd < COST_AP_TRUNG_NHANH) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ " + COST_AP_TRUNG_NHANH + " VND");
                                    return;
                                }

                                if (PlayerDAO.subtotalvnd(player, COST_AP_TRUNG_NHANH)) {
                                    player.mabuEgg.timeDone = 0;
                                    Service.gI().sendMoney(player);
                                    player.mabuEgg.sendMabuEgg();
                                }
                            }
                            break;
                        case ConstNpc.CAN_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                            "Bạn có chắc chắn cho trứng nở?\n"
                                            + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư Gầy\n"
                                            + "|5|Chỉ số : 170% hợp thể và 100% sát thương chưởng",
                                            "Mabuu Gầy\nTrái Đất", "Mabuu Gầy\nNamếc", "Mabuu Gầy\nXayda", "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabuu Gầy?", "Đồng ý", "Từ chối");
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                    break;
                                case 1:
                                    player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                    break;
                                case 2:
                                    player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_DESTROY_EGG:
                            if (select == 0) {
                                player.mabuEgg.destroyEgg();
                            }
                            break;
                    }
                }
            }
        };
    }

//    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//
//            private final int COST_AP_TRUNG_NHANH = 500000;
//
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == (21 + player.gender)) {
//                        player.BuugayEgg.sendMabuGayEgg();
//                        if (player.BuugayEgg.getSecondDone() != 0) {
//                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
//                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " VNĐ", "Đóng");
//                        } else {
//                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
//                        }
//                    }
////                    if (this.mapId == 154) {
////                        player.billEgg.sendBillEgg();
////                        if (player.billEgg.getSecondDone() != 0) {
////                            this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Burk Burk...",
////                                    "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " VNĐ", "Đóng");
////                        } else {
////                            this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Burk Burk...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
////                        }
////                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == (21 + player.gender)) {
//                        switch (player.iDMark.getIndexMenu()) {
//                            case ConstNpc.CAN_NOT_OPEN_EGG:
//                            if (select == 0) { 
//                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
//                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư Gầy?", "Đồng ý", "Từ chối");
//                            } else if (select == 1) { 
//                                if (player.getSession() != null && player.getSession().totalvnd < COST_AP_TRUNG_NHANH) { 
//                                    Service.gI().sendThongBao(player, "Bạn không đủ " + COST_AP_TRUNG_NHANH + " VND");
//                                    return;
//                                }
//
//                                if (PlayerDAO.subtotalvnd(player, COST_AP_TRUNG_NHANH)) { 
//                                    player.BuugayEgg.timeDone = 0;
//                                    Service.gI().sendMoney(player);
//                                    player.BuugayEgg.sendMabuGayEgg();
//                                }
//                            }
//                            break;   
//                           case ConstNpc.CAN_OPEN_EGG:
//                            switch (select) { 
//                                case 0:
//                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
//                                            "Bạn có chắc chắn cho trứng nở?\n"
//                                            + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư Gầy\n"
//                                            + "|5|Chỉ số : 170% hợp thể và 100% sát thương chưởng",
//                                            "Mabuu Gầy\nTrái Đất", "Mabuu Gầy\nNamếc", "Mabuu Gầy\nXayda", "Từ chối");
//                                    break;                                 
//                                case 1:
//                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
//                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabuu Gầy?", "Đồng ý", "Từ chối");
//                                    break;                                 
//                            }
//                            break;                                 
//                        case ConstNpc.CONFIRM_OPEN_EGG:
//                            switch (select) { 
//                                case 0:
//                                    player.BuugayEgg.openEgg(ConstPlayer.TRAI_DAT);
//                                    break;                                 
//                                case 1:
//                                    player.BuugayEgg.openEgg(ConstPlayer.NAMEC);
//                                    break;                                 
//                                case 2:
//                                    player.BuugayEgg.openEgg(ConstPlayer.XAYDA);
//                                    break;                                 
//                                default:
//                                    break;                                 
//                            }
//                            break;                                 
//                        case ConstNpc.CONFIRM_DESTROY_EGG:
//                            if (select == 0) { 
//                                player.BuugayEgg.destroyEgg();
//                            }
//                            break;   
//                        }
//                    }
////                    if (this.mapId == 154) {
////                        switch (player.iDMark.getIndexMenu()) {
////                            case ConstNpc.CAN_NOT_OPEN_BILL:
////                                if (select == 0) {
////                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
////                                            "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
////                                } else if (select == 1) {
////                                    if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
////                                        player.inventory.gold -= COST_AP_TRUNG_NHANH;
////                                        player.billEgg.timeDone = 0;
////                                        Service.gI().sendMoney(player);
////                                        player.billEgg.sendBillEgg();
////                                    } else {
////                                        Service.gI().sendThongBao(player,
////                                                "Bạn không đủ vàng để thực hiện, còn thiếu "
////                                                + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
////                                    }
////                                }
////                                break;
////                            case ConstNpc.CAN_OPEN_EGG:
////                                switch (select) {
////                                    case 0:
////                                        this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_BILL,
////                                                "Bạn có chắc chắn cho trứng nở?\n"
////                                                + "Đệ tử của bạn sẽ được thay thế bằng đệ Bill",
////                                                "Đệ Bill\nTrái Đất", "Đệ Bill\nNamếc", "Đệ Bill\nXayda", "Từ chối");
////                                        break;
////                                    case 1:
////                                        this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_BILL,
////                                                "Bạn có chắc chắn muốn hủy bỏ trứng Bill?", "Đồng ý", "Từ chối");
////                                        break;
////                                }
////                                break;
////                            case ConstNpc.CONFIRM_OPEN_BILL:
////                                switch (select) {
////                                    case 0:
////                                        player.billEgg.openEgg(ConstPlayer.TRAI_DAT);
////                                        break;
////                                    case 1:
////                                        player.billEgg.openEgg(ConstPlayer.NAMEC);
////                                        break;
////                                    case 2:
////                                        player.billEgg.openEgg(ConstPlayer.XAYDA);
////                                        break;
////                                    default:
////                                        break;
////                                }
////                                break;
////                            case ConstNpc.CONFIRM_DESTROY_BILL:
////                                if (select == 0) {
////                                    player.billEgg.destroyEgg();
////                                }
////                                break;
////                        }
////                    }
//
//                }
//            }
//        };
//    }
    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " ngọc", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " ngọc", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Mi làm gì có đệ mà nâng sức mạnh !!!");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gem >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gem -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ ngọc để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gem)) + " ngọc");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gem >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gem -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ ngọc để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gem)) + "ngọc ");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc DAISHINKAN(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    Giaidauvutru.gI().setTime();
                    if (this.mapId == 4) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > Giaidauvutru.TIME_OPEN && now < Giaidauvutru.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_GD, "Đường đến với Giải đấu vũ trụ đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardGiaidau.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardGiaidau.quantilyGiaidau[i] > 1 ? "x" + player.rewardGiaidau.quantilyGiaidau[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_GD, "Ngươi có một vài phần thưởng Giải đấu sức mạnh đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_GD,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Logger.error("Lỗi mở menu Daishinkan");
                        }
                    }
                    if (this.mapId == 145) {
                        if (player.iDMark.isHoldGiaidauBall()) {
                            this.createOtherMenu(player, ConstNpc.MENU_PHU_CS, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                        } else {
                            if (BossManager.gI().existBossOnPlayer(player)
                                    || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isGiaidauBall(itemMap.itemTemplate.id))
                                    || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldGiaidauBall())) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOMEE, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                            } else {
                                this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOMEE, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_GD:
                            player.rewardGiaidau.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_GD:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_GIAI_DAU);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMap(player, 145, -1, 826, 504);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_GD:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_GIAI_DAU);
                            }
                            break;
                        case ConstNpc.MENU_PHU_CS:
                            if (select == 0) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                        "Ta sẽ giúp ngươi tăng chỉ số lên mức kinh hoàng, ngươi chọn đi",
                                        "x3 HP\n" + Util.numberToMoney(Giaidauvutru.COST_X3) + " hồng ngọc ",
                                        "x5 HP\n" + Util.numberToMoney(Giaidauvutru.COST_X5) + " hồng ngọc ",
                                        "x7 HP\n" + Util.numberToMoney(Giaidauvutru.COST_X7) + " hồng ngọc",
                                        "Từ chối"
                                );
                            }
                            break;
                        case ConstNpc.MENU_OPTION_GO_HOMEE:
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                    break;
                                case 2:
                                    BossManager.gI().callBoss(player, mapId);
                                    break;
                                case 1:
                                    this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                    break;
                            }
                            break;

                        case ConstNpc.MENU_OPTION_PHU_CS:
                            if (player.effectSkin.xHPKI > 1) {
                                Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                                return;
                            }
                            switch (select) {
                                case 0:
                                    Giaidauvutru.gI().xHPKI(player, Giaidauvutru.X3);
                                    break;
                                case 1:
                                    Giaidauvutru.gI().xHPKI(player, Giaidauvutru.X5);
                                    break;
                                case 2:
                                    Giaidauvutru.gI().xHPKI(player, Giaidauvutru.X7);
                                    break;
                                case 3:
                                    this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                    break;
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
//                                if (!player.getSession().actived) { 
//                                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
//
//                                } else
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " hồng ngọc ",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " hồng ngọc ",
                                    "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " hồng ngọc",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.gI().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 145) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                            "Đổi SKH VIP", "Từ Chối");
                }
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xem các chức năng sự kiện!",
                            "Đua top", "Sự kiện Trung Thu", "Lệnh chat trong game");

                }
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.!",
                            "Nói chuyện", "Học \nTuyệt kỹ", "Hướng dẫn");

                }

            }

            //if (player.inventory.gold < 500000000) { 
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                return;
//            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (select == 0) {
                            createOtherMenu(player, 11111,
                                    "Xem ở box zalo nhé: \n"
                                    + "Link: zalo.me/g/hzsbon415\n"
                                    + "Zalo Admin: 0358124452", "Ok");
                        }
                        if (select == 1) {
                            createOtherMenu(player, 11112, "- Dân tộc Xayda ( nhiệm vụ kuku trở đi) khi bật cờ Đen sẽ tự động hóa khỉ khổng lồ bất cứ khi nào,\n"
                                    + " không phải chờ thời gian hồi phục kỹ năng. Khi đang hóa khỉ mà bị hạ bằng 1 chiêu đặc biệt của dân tộc Trái Đất sẽ rớt ra vật phẩm Đuôi Khỉ,\n"
                                    + " người Xayda bị mất đuôi khỉ trong vòng 1 giờ sẽ không thể hóa khỉ bằng cờ đen nữa (vẫn hóa khỉ bằng kỹ năng được).\n"
                                    + "* Khi sử dụng vật phẩm Đuôi Khỉ sẽ được x2 sức mạnh và tiềm năng trong một khoảng thời gian: Trái Đất 30 phút, Namếc 2 giờ, Xayda không sử dụng được.\n"
                                    + "\n|3|* Sử dụng đuôi khỉ để up vật phẩm trung thu, đổi quà tại làng kakarot :v\n"
                                    + "\n- Khung giờ hỗ trợ nhiệm vụ cho người chơi từ 21h00 đến 22h00", "Ok");
                        }
                        if (select == 2) {
                            createOtherMenu(player, 11113,
                                    "Lệnh chat sử dụng Mod 231 mới\n"
                                    + "cheatX - Đổi tốc độ game, X là số tốc độ muốn đổi 1-3\n"
                                    + "ak - Tự đánh\n"
                                    + "setdo - Mở nút mặc đồ nhanh\n"
                                    + "xoanguoi - Xóa người trong khu\n"
                                    + "ahs - Auto hồi sinh\n"
                                    + "kX - Đổi khu, X là 1 số tương ứng với khu\n"
                                    + "sX - Đổi tốc chạy nhân vật, X là số tốc độ muốn đổi\n"
                                    + "ts - Tàn sát chạy bộ\n"
                                    + "tdlt - Tàn sát tele, như tự động luyện tập\n"
                                    + "ahs - Tự động hồi sinh\n"
                                    + "add - Thêm/xóa vật phẩm, quái vào danh sách\n"
                                    + "Còn lại thao tác trên Menu Mod bên phải màn hình", "Ok");
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 145) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 450);
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }

                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Cửa hàng", "Chế tạo", "Đóng");
                                break;
                            case 1:
                                Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2120);
                                if (biKiep == null) {
                                    Service.gI().sendThongBao(player, "Đi kiếm bí kíp tuyệt kĩ đi cu");
                                    return;
                                }
                                this.createOtherMenu(player, 6, "Ta sẽ giúp ngươi học tuyệt kỹ: %skill \nBí kiếp tuyệt kỹ: " + biKiep.quantity + "/9999\nGiá vàng: 10.000.000\nGiá ngọc: 99",
                                        "Học Tuyệt Kỹ", "Từ chối");

                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == 6) {

                        switch (select) {
                            case 0:
                                Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 2120);
                                if (biKiep != null) {
                                    if (biKiep.quantity >= 9999 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item stk = ItemService.gI().createNewItem((short) (player.gender + 2127));

                                        stk.itemOptions.add(new Item.ItemOption(30, 0));
                                        InventoryServiceNew.gI().addItemBag(player, stk);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 9999);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được sách Tuyệt Kỹ Đặc Biệt");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Con không đủ bí kíp tuyệt kỹ , hãy luyện tập để mạnh hơn");

                                }
                                break;
                            case 1:
                                break;

                        }
                    }

                }

            }

        };

    }

    public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Hãy thu thập đủ x99 thức ăn , ta sẽ ban cho ngươi trang bị huỷ diệt!",
                            "Xem Điểm ", "Shop Pudding", "Shop Xúc Xích", "Shop Kem Dâu", "Shop Mì Ly", "Shop Sushi");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48 || this.mapId == 154) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi đang có: " + player.inventory.coupon + " điểm", "Đóng");
                                return;
                            }
                            if (select == 1) {
                                ShopServiceNew.gI().opendShop(player, "BILL_PUDDING", false);
                                return;

                            }
                            if (select == 2) {
                                ShopServiceNew.gI().opendShop(player, "BILL_XUCXICH", false);
                                return;
                            }
                            if (select == 3) {
                                ShopServiceNew.gI().opendShop(player, "BILL_KEMDAU", false);
                                return;
                            }
                            if (select == 4) {
                                ShopServiceNew.gI().opendShop(player, "BILL_MILY", false);
                                return;
                            }
                            if (select == 5) {
                                ShopServiceNew.gI().opendShop(player, "BILL_SUSHI", false);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, cậu muốn tôi giúp gì?", "Nhiệm vụ\nhàng ngày", "Nhận quà nạp", "Nhận danh hiệu", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
//                                    int[] rw = {ConstTask.GOLD_EASY, ConstTask.GOLD_NORMAL, ConstTask.GOLD_HARD, ConstTask.GOLD_VERY_HARD, ConstTask.GOLD_HELL};
//
//                                    if (player.playerTask.sideTask.template != null) {
//                                        String npcSay = "|4|Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " (" + player.playerTask.sideTask.getLevel() + ")"
//                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/" + player.playerTask.sideTask.maxCount + " (" + player.playerTask.sideTask.getPercentProcess() + "%)"
//                                                + "\nSố nhiệm vụ còn lại trong ngày: " + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK
//                                                + "\n|5|Phần thưởng: " + rw[player.playerTask.sideTask.level] + " vàng";
//                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
//                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
//                                    } else {
//                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
//                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
//                                                + "sức cậu có thể làm được cái nào?",
//                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
//                                    }
                                    break;
                                case 1:
                                    Archivement.gI().getAchievement(player);
                                    break;
                                case 2:
                                    Archivement_BoMong.gI().getAchievement(player);
                                    break;
                            }
                        }
//                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
//                            switch (select) {
//                                case 0:
//                                case 1:
//                                case 2:
//                                case 3:
//                                case 4:
//                                    TaskService.gI().changeSideTask(player, (byte) select);
//                                    break;
//                            }
//                        } 
//                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
//                            switch (select) {
//                                case 0:
//                                    TaskService.gI().paySideTask(player);//trả nhiệm vụ
//                                    break;
//                                case 1:
//                                    TaskService.gI().removeSideTask(player);//hủy nhiệm vụ
//                                    break;
//                            }
//                        }
                    }
                }
            }
        };
    }

    public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (this.mapId == 46) {
                            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con hãy bay theo cây Gậy Như Ý trên đỉnh tháp để đến Thần Điện gặp Thượng Đế\nCon rất xứng đáng để làm đệ tự của ông ấy",
                                        "Cửa hàng\nĐậu Thần", "Đóng");
                            }
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "KARIN", false);
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

//    public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {
//                    createOtherMenu(player, ConstNpc.BASE_MENU,
//                            "|2|Ta Vừa Hắc Mắp Xêm Được Tóp Của Toàn Server\b|7|Người Muốn Xem Tóp Gì?",
//                            "Tóp Sức Mạnh", "Đua Top Sức Mạnh\nMùa 2", "Top Nhiệm Vụ", "Top Đại Gia\nHồng Ngọc", "Đua Top Nạp\nMùa 2", "Top Tài Sản", "Top Ngũ Hành Sơn", "Top Thỏi Vàng", "Đóng");
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 5) {
//                        switch (player.iDMark.getIndexMenu()) {
//                            case ConstNpc.BASE_MENU:
//                                if (select == 0) {
//                                    createOtherMenu(player, 1,
//                                            "Top Sức mạnh",
//                                            "Xem top", "Xem quà");
//                                    return;
//                                }
//                                if (select == 1) {
//
//                                    createOtherMenu(player, 2,
//                                            "Đua top Sức Mạnh",
//                                            "Xem top", "Xem quà");
//                                    return;
//                                }
//                                if (select == 2) {
//                                    createOtherMenu(player, 3,
//                                            "Top nhiệm vụ",
//                                            "Xem top", "Xem quà");
//                                    return;
//                                }
//
//                                if (select == 3) {
//                                    createOtherMenu(player, 4,
//                                            "Top Ngọc Hồng",
//                                            "Xem top", "Xem quà");
//                                    return;
//                                }
//                                if (select == 4) {
//                                    createOtherMenu(player, 5,
//                                            "Đua Top nạp",
//                                            "Xem top", "Xem quà");
//                                    return;
//                                }
//                                if (select == 5) {
//                                    createOtherMenu(player, 6,
//                                            "Top VND",
//                                            "Xem top", "Xem quà");
//                                    return;
//                                }
//                                if (select == 6) {
//                                    createOtherMenu(player, 7,
//                                            "Top Ngũ Hành Sơn",
//                                            "Xem top", "Xem quà");
//                                    return;
//                                }
//                                if (select == 7) {
//                                    createOtherMenu(player, 8,
//                                            "Top Thỏi Vàng",
//                                            "Xem top", "Xem quà");
//                                    return;
//                                }
//                                break;
//                            case 1:
//                                if (select == 0) {
//                                    Service.gI().showListTop(player, Manager.topSM);
//                                } else if (select == 1) {
//                                    this.createOtherMenu(player, 11111, "Tham gia đua top sức mạnh khác", "Đóng");
//
//                                }
//                                break;
//                            case 2:
//                                if (ConstDataEventSM.isRunningSK) {
//                                    if (select == 0) {
//                                        Service.gI().sendThongBaoOK(player, TopService.getTopSM());
////                                   Service.gI().showListTop(player, Manager.topSM2);
//                                    } else if (select == 1) {
////                                    this.createOtherMenu(player, 11111, Arrays.toString(TaskTraoQuaSM.Text), "Đóng");
//                                        Service.gI().sendBigMessage(player, avartar, Arrays.toString(ConstDataEventSM.Text));
//                                    }
//                                } else {
//                                    Service.gI().sendThongBao(player, "Vui lòng chờ sự kiện tiếp theo");
//                                }
//                                break;
//                            case 12:
//                                if (select == 0) {
//                                    Service.gI().sendThongBao(player, "Ăn cức không???");
//                                }
//                                break;
//                            case 3:
//                                if (select == 0) {
//                                    Service.gI().showListTop(player, Manager.topNV);
//                                }
//                                break;
//                            case 4:
//                                if (select == 0) {
//                                    Service.gI().showListTop(player, Manager.topPVP);
//                                }
//                                break;
//                            case 5:
//                                if (ConstDataEventNAP.isRunningSK) {
//                                    if (select == 0) {
//
//                                        Service.gI().sendThongBaoOK(player, TopService.getTopNap());
//                                    } else if (select == 1) {
//                                        Service.gI().sendBigMessage(player, avartar, Arrays.toString(ConstDataEventNAP.Text));
//
//                                    }
//                                } else {
//                                    Service.gI().sendThongBao(player, "Vui lòng chờ sự kiện tiếp theo");
//                                }
//                                break;
//                            case 6:
//                                if (select == 0) {
//                                    Service.gI().sendThongBaoOK(player, TopService.getTopVND());
//                                }
//                                break;
//                            case 7:
//                                if (select == 0) {
//                                    Service.gI().sendThongBaoOK(player, TopService.getTopNguHanhSon());
//                                } else if (select == 1) {
//                                    this.createOtherMenu(player, 66666, Arrays.toString(TaskTraoQuaNHS.Text), "Đóng");
//                                }
//                                break;
//                            case 8:
//                                if (select == 0) {
//                                    Service.gI().showListTop(player, Manager.topTV);
//                                } else if (select == 1) {
//                                    this.createOtherMenu(player, 77777, Arrays.toString(TaskTraoQuaTV.Text), "Đóng");
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }
//            }
//        };
//    }
    public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    if (this.mapId == 80 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                        if (this.mapId == 80 || this.mapId == 5) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 456);
                            }
                        }
                        if (this.mapId == 131) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        if (player.iDMark.getTranhNgoc() == 2) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Cút!Ta không nói chuyện với sinh vật hạ đẳng", "Đóng");
                            return;
                        }
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Hãy mang ngọc rồng về cho ta", "Đưa ngọc", "Đóng");
                    } else {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + biKiep.quantity + " bí kiếp.\n"
                                    + "Hãy kiếm đủ 9999 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart", "Học dịch\nchuyển", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn không có bí kíp nào hãy up yadart.\n"
                                    + "Hãy kiếm đủ 9999 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart", "Học dịch\nchuyển", "Đóng");
                        }
                    }
                }

            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 153) {
                        switch (select) {
                            case 0:
                                if (player.iDMark.getTranhNgoc() == 1 && player.isHoldNamecBallTranhDoat) {
                                    if (!Util.canDoWithTime(player.lastTimePickItem, 20000)) {
                                        Service.gI().sendThongBao(player, "Vui lòng đợi " + ((player.lastTimePickItem + 20000 - System.currentTimeMillis()) / 1000) + " giây để có thể trả");
                                        return;
                                    }
                                    TranhNgocService.getInstance().dropBall(player, (byte) 1);
                                    player.zone.pointCadic++;
                                    if (player.zone.pointCadic > ConstTranhNgocNamek.MAX_POINT) {
                                        player.zone.pointCadic = ConstTranhNgocNamek.MAX_POINT;
                                    }
                                    TranhNgocService.getInstance().sendUpdatePoint(player);
                                }
                                break;
                            case 1:
                                break;
                        }
                    } else {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (biKiep != null) {
                            if (biKiep.quantity >= 9999 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                yardart.itemOptions.add(new Item.ItemOption(50, 25));
                                yardart.itemOptions.add(new Item.ItemOption(77, 20));
                                yardart.itemOptions.add(new Item.ItemOption(103, 20));
                                yardart.itemOptions.add(new Item.ItemOption(94, 90));
                                yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                InventoryServiceNew.gI().addItemBag(player, yardart);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 9999);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn không có cuốn bí kíp nào để đổi");

                        }

                    }
                }
            }
        };
    }

    public static Npc Fide(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 20 || this.mapId == 5) {
                    if (canOpenNpc(player)) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào, Ngươi có muốn kiếm lấy thú nuôi cho riêng mình không?",
                                "Cửa hàng PET");
                    }
                } else {
                    if (player.iDMark.getTranhNgoc() == 1) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đi đi cu!! Chém giờ", "Đóng");
                        return;
                    }
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Hãy mang ngọc rồng về cho ta", "Đưa ngọc", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 20 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "FIDE", true);
                                    break;
                                case 1: //tiệm hồng ngọc
                                    ShopServiceNew.gI().opendShop(player, "FIDE", true);
                                    break;
                                case 2: //tiệm hồng ngọc

                                    ShopServiceNew.gI().opendShop(player, "FIDE", true);
                                    break;

                            }
                        }
                    } else {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.iDMark.getTranhNgoc() == 2 && player.isHoldNamecBallTranhDoat) {
                                        if (!Util.canDoWithTime(player.lastTimePickItem, 20000)) {
                                            Service.gI().sendThongBao(player, "Vui lòng đợi " + ((player.lastTimePickItem + 20000 - System.currentTimeMillis()) / 1000) + " giây để có thể trả");
                                            return;
                                        }
                                        TranhNgocService.getInstance().dropBall(player, (byte) 2);
                                        player.zone.pointFide++;
                                        if (player.zone.pointFide > ConstTranhNgocNamek.MAX_POINT) {
                                            player.zone.pointFide = ConstTranhNgocNamek.MAX_POINT;
                                        }
                                        TranhNgocService.getInstance().sendUpdatePoint(player);
                                    }
                                    break;
                                case 1:
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc Heart(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
                    PlayerService.gI().banPlayer((player));
                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
                    return;
                }
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Shop sự kiện cho dân cày nạp vip ?"
                            + "\b|2|Đây là chức năng thay admin bán đồ nên sẽ trừ tiền nạp nhé !!!"
                            + "\b\n|3|Lưu ý: Chỉ giao dịch nạp tiền qua duy nhất admin Emti,\n"
                            + "mọi rủi ro tự chịu nếu không chấp hành.",
                            "Shop đá\n ngũ sắc");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5 || this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "HEART", true);
                                    break;
//                                case 1: //tiệm hồng ngọc
//                                    ShopServiceNew.gI().opendShop(player, "HEART", true);
//                                    break;                                 
//                                case 2: //tiệm hồng ngọc
//                                    ShopServiceNew.gI().opendShop(player, "HEART", true);
//                                    break;                                 

//                                case 1:
//                                    this.createOtherMenu(player, 1112,
//                                            "|3|Lưu ý: Đổi đá ngũ sắc bằng tiền nạp sẽ mất tiền nạp(Không phải VND đâu nha)\n"
//                                            + "Nên nhận mốc nạp tại quy lão trước khi qua đây đổi"
//                                            + "\nBạn có: " + player.getSession().totalvnd + " VND",
//                                            "Đổi đá\n ngũ sắc");
//                                    break;
                            }
                        }
//                        else if (player.iDMark.getIndexMenu() == 1112) {
//                            if (select == 0) {
//                                this.createOtherMenu(player, 1113,
//                                        "\b|1|Muốn đổi đá ngũ sắc hen?"
//                                        //                                            + "\n\b|7|Bạn đang có : " + player.getSession().vnd
//                                        + "|3|Lưu ý: Đổi đá ngũ sắc bằng tiền nạp sẽ mất tiền nạp(Không phải VND đâu nha)\n"
//                                        + "Nên nhận mốc nạp tại quy lão trước khi qua đây đổi"
//                                        + "\nBạn có: " + player.getSession().totalvnd + " VND",
//                                        "50k\n20 viên", "100k\n40 viên", "150k\n60 viên", "200k\n80 viên", "250K\n120 viên");
//                            }
                        //  } 
//                    else if (player.iDMark.getIndexMenu() == 1113) {
//                            switch (select) {
//                                case 0:
//                                    if (player.getSession().totalvnd < 50000) {
//                                        Service.gI().sendThongBao(player, "Tổng nạp không đủ 50k");
//                                        return;
//                                    }
////                                    if (player.getSession().vnd < 50000) { 
////                                        Service.gI().sendThongBao(player, "Bạn không đủ 50k VNĐ");
////                                        return;
////                                    }
//
//                                    if (PlayerDAO.subtotalvnd(player, 50000)) {
//
//                                        Item i = ItemService.gI().createNewItem((short) 674, 20);
//                                        i.itemOptions.add(new Item.ItemOption(30, 1));
//                                        i.itemOptions.add(new Item.ItemOption(93, 15));
//                                        InventoryServiceNew.gI().addItemBag(player, i);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 20 viên ngũ sắc");
//                                    }
//                                    break;
//                                case 1:
//                                    if (player.getSession().totalvnd < 100000) {
//                                        Service.gI().sendThongBao(player, "Tổng nạp không đủ 100k");
//                                        return;
//                                    }
////                                    if (player.getSession().vnd < 100000) { 
////                                        Service.gI().sendThongBao(player, "Bạn không đủ 100k VNĐ");
////                                        return;
////                                    }
//
//                                    if (PlayerDAO.subtotalvnd(player, 100000)) {
//
//                                        Item i = ItemService.gI().createNewItem((short) 674, 40);
//                                        i.itemOptions.add(new Item.ItemOption(30, 1));
//                                        i.itemOptions.add(new Item.ItemOption(93, 15));
//                                        InventoryServiceNew.gI().addItemBag(player, i);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 40 viên ngũ sắc");
//                                    }
//                                    break;
//                                case 2:
//                                    if (player.getSession().totalvnd < 150000) {
//                                        Service.gI().sendThongBao(player, "Tổng nạp không đủ 150k");
//                                        return;
//                                    }
////                                    if (player.getSession().vnd < 150000) { 
////                                        Service.gI().sendThongBao(player, "Bạn không đủ 150k VNĐ");
////                                        return;
////                                    }
//
//                                    if (PlayerDAO.subtotalvnd(player, 150000)) {
//
//                                        Item i = ItemService.gI().createNewItem((short) 674, 60);
//                                        i.itemOptions.add(new Item.ItemOption(30, 1));
//                                        i.itemOptions.add(new Item.ItemOption(93, 15));
//                                        InventoryServiceNew.gI().addItemBag(player, i);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 60 viên ngũ sắc");
//                                    }
//                                    break;
//                                case 3:
//                                    if (player.getSession().totalvnd < 200000) {
//                                        Service.gI().sendThongBao(player, "Tổng nạp không đủ 200k");
//                                        return;
//                                    }
////                                    if (player.getSession().vnd < 200000) { 
////                                        Service.gI().sendThongBao(player, "Bạn không đủ 200k VNĐ");
////                                        return;
////                                    }
//
//                                    if (PlayerDAO.subtotalvnd(player, 200000)) {
//
//                                        Item i = ItemService.gI().createNewItem((short) 674, 80);
//                                        i.itemOptions.add(new Item.ItemOption(30, 1));
//                                        i.itemOptions.add(new Item.ItemOption(93, 15));
//                                        InventoryServiceNew.gI().addItemBag(player, i);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 80 viên ngũ sắc");
//                                    }
//                                    break;
//                                case 4:
//                                    if (player.getSession().totalvnd < 250000) {
//                                        Service.gI().sendThongBao(player, "Tổng nạp không đủ 250k");
//                                        return;
//                                    }
////                                    if (player.getSession().vnd < 250000) { 
////                                        Service.gI().sendThongBao(player, "Bạn không đủ 250k VNĐ");
////                                        return;
////                                    }
//
//                                    if (PlayerDAO.subtotalvnd(player, 250000)) {
//
//                                        Item i = ItemService.gI().createNewItem((short) 674, 120);
//                                        i.itemOptions.add(new Item.ItemOption(30, 1));
//                                        i.itemOptions.add(new Item.ItemOption(93, 15));
//                                        InventoryServiceNew.gI().addItemBag(player, i);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        Service.gI().sendThongBao(player, "Bạn đã nhận được 120 viên ngũ sắc");
//                                    }
//                                    break;
//                            }
//                        }
                    }
                }
            }
        };
    }

    public static Npc Jiren(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                            "Cửa hàng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop
                                    ShopServiceNew.gI().opendShop(player, "JIREN", true);
                                    break;
                                case 1: //tiệm hồng ngọc
                                    ShopServiceNew.gI().opendShop(player, "JIREN", true);
                                    break;
                                case 2: //tiệm hồng ngọc
                                    ShopServiceNew.gI().opendShop(player, "JIREN", true);
                                    break;

                            }
                        }
                    }
                }
            }
        };
    }
//
//    public static Npc He2023(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            @Override
//            public void openBaseMenu(Player player) {
//                if (player.getSession().vnd > cn.ghVnd && player.getSession().totalvnd > cn.ghTotalVnd) {
//                    PlayerService.gI().banPlayer((player));
//                    Service.gI().sendThongBao(player, "Bạn bị ban thành công");
//                    return;
//                }
//                if (canOpenNpc(player)) {
//                    createOtherMenu(player, ConstNpc.BASE_MENU,
//                            "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
//                            "Cửa hàng");
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (canOpenNpc(player)) {
//                    if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14) {
//                        if (player.iDMark.isBaseMenu()) {
//                            switch (select) {
//                                case 0: //shop
//                                    ShopServiceNew.gI().opendShop(player, "HE2023", true);
//                                    break;
//                                case 1: //tiệm hồng ngọc
//                                    ShopServiceNew.gI().opendShop(player, "HE2023", true);
//                                    break;
//                                case 2: //tiệm hồng ngọc
//                                    ShopServiceNew.gI().opendShop(player, "HE2023", true);
//                                    break;
//
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.map.mapId == 52) {
                        if (DaiHoiManager.gI().openDHVT && (System.currentTimeMillis() <= DaiHoiManager.gI().tOpenDHVT)) {
                            String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                            this.createOtherMenu(pl, ConstNpc.MENU_DHVT, "Hiện đang có giải đấu " + nameDH + " bạn có muốn đăng ký không? \nSố người đã đăng ký :" + DaiHoiManager.gI().lstIDPlayers.size(), new String[]{"Giải\n" + nameDH + "\n(" + DaiHoiManager.gI().costRoundDHVT() + ")", "Từ chối", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Giải siêu hạng"});
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau", new String[]{"Thông tin\bChi tiết", "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23", "Giải siêu hạng\n(thử nghiệm)"});
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.map.mapId == 52) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Service.gI().sendThongBaoFromAdmin(player, "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,13,18h\bGiải Siêu cấp 1: 9,14,19h\bGiải Siêu cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\nGiải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 10.000 vàng\bVô địch: 5 viên đá nâng cấp\nVui lòng đến đúng giờ để đăng ký thi đấu");
                                    break;
                                case 1:
                                    Service.gI().sendThongBaoFromAdmin(player, "Nhớ Đến Đúng Giờ nhé");
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DHVT) {
                            switch (select) {
                                case 0:
//                                    if (DaiHoiService.gI().canRegisDHVT(player.nPoint.power)) {
                                    if (DaiHoiManager.gI().lstIDPlayers.size() < 256) {
                                        if (DaiHoiManager.gI().typeDHVT == (byte) 5 && player.inventory.gold >= 10000) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.gI().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gold -= 10000;
                                                Service.gI().sendMoney(player);
                                                Service.gI().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else if (DaiHoiManager.gI().typeDHVT > (byte) 0 && DaiHoiManager.gI().typeDHVT < (byte) 5 && player.inventory.gem >= (2 * DaiHoiManager.gI().typeDHVT)) {
                                            if (DaiHoiManager.gI().isAssignDHVT(player.id)) {
                                                Service.gI().sendThongBao(player, "Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                            } else {
                                                player.inventory.gem -= 2 * DaiHoiManager.gI().typeDHVT;
                                                Service.gI().sendMoney(player);
                                                Service.gI().sendThongBao(player, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                                DaiHoiManager.gI().lstIDPlayers.add(player.id);
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng ngọc để đăng ký thi đấu");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải sau");
                                    }

//                                    } else {
//                                        Service.gI().sendThongBao(player, "Bạn không đủ điều kiện tham gia giải này, hãy quay lại vào giải phù hợp");
//                                    }
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                                    break;
                            }
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 2000000;
                                        } else {
                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 1:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.gI().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.gI().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    //    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) { 
//            String[] menuselect = new String[]{ };
//
//            @Override
//            public void openBaseMenu(Player pl) { 
//                if (pl.getSession().vnd > cn.ghVnd && pl.getSession().totalvnd > cn.ghTotalVnd) { 
//                    PlayerService.gI().banPlayer((pl));
//                    Service.gI().sendThongBao(pl, "Bạn bị ban thành công");
//                    return;
//                }
//                if (canOpenNpc(pl)) { 
//                    if (this.mapId == 52) { 
//                        createOtherMenu(pl, 0, DaiHoiVoThuatService.gI(DaiHoiVoThuat.getInstance().getDaiHoiNow()).Giai(pl), "Thông tin\nChi tiết", DaiHoiVoThuatService.gI(DaiHoiVoThuat.getInstance().getDaiHoiNow()).CanReg(pl) ? "Đăng ký" : "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23");
//                    } else if (this.mapId == 129) { 
//                        int goldchallenge = pl.goldChallenge;
//                        if (pl.levelWoodChest == 0) { 
//                            menuselect = new String[]{ "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
//                        } else { 
//                            menuselect = new String[]{ "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
//                        }
//                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");
//
//                    } else { 
//                        super.openBaseMenu(pl);
//                    }
//                }
//            }
//
//            @Override
//            public void confirmMenu(Player player, int select) { 
//                if (canOpenNpc(player)) { 
//                    if (this.mapId == 52) { 
//                        switch (select) { 
//                            case 0:
//                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, DaiHoiVoThuat.getInstance().getInfo());
//                             break;                                 
//                            case 1:
//                                if (DaiHoiVoThuatService.gI(DaiHoiVoThuat.getInstance().getDaiHoiNow()).CanReg(player)) { 
//                                    DaiHoiVoThuatService.gI(DaiHoiVoThuat.getInstance().getDaiHoiNow()).Reg(player);
//                                }
//                             break;                                 
//                            case 2:
//                                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
//                             break;                                 
//                        }
//                    } else if (this.mapId == 129) { 
//                        int goldchallenge = player.goldChallenge;
//                        if (player.levelWoodChest == 0) { 
//                            switch (select) { 
//                                case 0:
//                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) { 
//                                        if (player.inventory.gold >= goldchallenge) { 
//                                            MartialCongressService.gI().startChallenge(player);
//                                            player.inventory.gold -= (goldchallenge);
//                                            PlayerService.gI().sendInfoHpMpMoney(player);
//                                            player.goldChallenge += 2000000;
//                                        } else { 
//                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
//                                        }
//                                    } else { 
//                                        Service.gI().sendThongBao(player, "Hãy mở rương báu vật trước");
//                                    }
//                                 break;                                 
//                                case 1:
//                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
//                                 break;                                 
//                            }
//                        } else { 
//                            switch (select) { 
//                                case 0:
//                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) { 
//                                        if (player.inventory.gold >= goldchallenge) { 
//                                            MartialCongressService.gI().startChallenge(player);
//                                            player.inventory.gold -= (goldchallenge);
//                                            PlayerService.gI().sendInfoHpMpMoney(player);
//                                            player.goldChallenge += 2000000;
//                                        } else { 
//                                            Service.gI().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
//                                        }
//                                    } else { 
//                                        Service.gI().sendThongBao(player, "Hãy mở rương báu vật trước");
//                                    }
//                                 break;                                 
//                                case 1:
//                                    if (!player.receivedWoodChest) { 
//                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) { 
//                                            Item it = ItemService.gI().createNewItem((short) 570);
//                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
//                                            it.itemOptions.add(new Item.ItemOption(30, 0));
//                                            it.createTime = System.currentTimeMillis();
//                                            InventoryServiceNew.gI().addItemBag(player, it);
//                                            InventoryServiceNew.gI().sendItemBags(player);
//
//                                            player.receivedWoodChest = true;
//                                            player.levelWoodChest = 0;
//                                            Service.gI().sendThongBao(player, "Bạn nhận được rương gỗ");
//                                        } else { 
//                                            this.npcChat(player, "Hành trang đã đầy");
//                                        }
//                                    } else { 
//                                        Service.gI().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
//                                    }
//                                 break;                                 
//                                case 2:
//                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
//                                 break;                                 
//                            }
//                        }
//                    }
//                }
//            }
//        };
//    }
    private static Npc TrongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 113) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật Siêu Hạng\n(thử nghiệm)\ndiễn ra 24/7 kể cả ngày lễ và chủ nhật\nHãy thi đấu để khẳng định đẳng cấp của mình nhé", "Top 100\nCao thủ\n(thử nghiệm)", "Hướng\ndẫn\nthêm", "Đấu ngay\n(thử nghiệm)", "Về\nĐại Hội\nVõ Thuật");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 113) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    try (Connection con = GirlkunDB.getConnection()) {
                                    // Manager.topSieuHang = Manager.realTopSieuHang(con);
                                } catch (Exception ignored) {
                                    ignored.printStackTrace();
                                    Logger.error("Lỗi đọc top");
                                }
                                //Service.gI().showListTop(player, Manager.topSieuHang, (byte) 1);
                                break;
                                case 1:
                                    if (player.getSession() != null) {
                                        this.createOtherMenu(player, 22222, Arrays.toString(TaskTraoQua.Text), "Đóng");
                                    }
                                    break;
                                case 2:
                                    List<TOP> tops = new ArrayList<>();
                                    tops.addAll(Manager.realTopSieuHang(player));
                                    Service.gI().showListTopDauNhanh(player, tops, (byte) 1, tops.get(0).rank);
                                    tops.clear();
                                    break;
                                case 3:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, -1, 432);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.TRONG_TAI:
                    return TrongTai(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CAU_BE:
                    return CauBe(mapId, status, cx, cy, tempId, avatar);

//                case ConstNpc.BERUS_DHVT:
//                 
//                    return berus_dhvt(mapId, status, cx, cy, tempId, avatar);    
//                case ConstNpc.NOI_BANH:
//                    return noibanhChungBanhTet(mapId, status, cx, cy, tempId, avatar);
//                       return noibanh(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.ONG_BUT:
//                    return ongbut(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.GAPTHU:
//                    return gapthu2(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.QUOCKHANH2:
//                    return quockhanh(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.TORIBOT:
////                    return sk20_10(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAISHINKAN:
                    return DAISHINKAN(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VEGETABNTT:
                    return VegetaBNTT(mapId, status, cx, cy, tempId, avatar);
//                      case ConstNpc.RONG_7S:
//                    return Rongs(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUA_HANG_KY_GUI:
                    return KyGui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.UNKNOW:
                    return Monaito(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.HUNG_VUONG:
//                    return HungVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.FIDE:
                    return Fide(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JIREN:
                    return Jiren(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GAU_PO:
                    return gauPo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THIEN_MA:
                    return ThienMa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LO_HONG_KHONG_GIAN:
                    return LoHongKhongGian(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TU_TAI_TIEN:
                    return TU_TAI_TIEN(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.TRUNGTHU:
//                    return TRUNGTHU(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VUA_VEGETA:
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return TruonglaoGuru(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.TRUONG_LAO_GURU:
//                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.VUA_VEGETA:
//                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TAPION:
                    return Tapion(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_70:
                    return npc70(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.VADOS:
//                    return vados(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.POTAGE:
//                    return Potage(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.LY_TIEU_NUONG:
//                    return conSoMayMan(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.LY_TIEU_NUONG_CLTX:
//                    return npclytieunuongCLTX(mapId, status, cx, cy, tempId, avatar);

                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:

                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HEART:
                    return Heart(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.THAN_MEO_KARIN:
//                    return karin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ2:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
//                case ConstNpc.THO_DAI_CA:
//                    return thodaica(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            canOpenNpc(player);//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                        }
                    };

            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(NpcFactory.class,
                            e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcRongXuong() {
        new Npc(-1, -1, -1, -1, ConstNpc.RONG_XUONG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.HALLOWEN_CONFIRM:
                        if (select == 0) {
                            GoiRongXuong.gI().confirmWish();
                        } else if (select == 1) {
                            GoiRongXuong.gI().reOpenRongxuongWishes(player);
                        }
                        break;
                    default:
                        GoiRongXuong.gI().showConfirmRongxuong(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case 527:
                        if (select == 0) {
                            ShopKyGuiService.gI().StartupItemToTop(player);
                        }
                        break;

                    case ConstNpc.MAKE_MATCH_PVP: //   if (player.getSession().actived) 
                    {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
//                        else { 
//                            Service.gI().sendThongBao(player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN TẠI\n|7|NROGOD.COM\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG");
//                         break;                                 
//                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_RONG_XUONG:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, GoiRongXuong.RONG_XUONG_TUTORIAL);
                        }
                        break;
                    case ConstNpc.RONG_HALLOWEN:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, GoiRongXuong.RONG_XUONG_TUTORIAL);
                        } else if (select == 1) {
                            GoiRongXuong.gI().summonRongxuong(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2114:
                        if (select == 0) {
                            IntrinsicService.gI().settltd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().settlnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().settlxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2115:
                        if (select == 0) {
                            IntrinsicService.gI().sethdtd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().sethdnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().sethdxd(player);
                        }
                        break;
//                    case ConstNpc.MENU_OPTION_USE_ITEM1618:
//                         try {
//                        ItemService.gI().OpenSaoPhaLeTrungCap(player, player.iDMark.getIndexMenu(), select);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Logger.error("Lỗi mở hộp quà");
//                    }
//                    break;
//                    case ConstNpc.MENU_OPTION_USE_ITEM1619:
//                         try {
//                        ItemService.gI().OpenSaoPhaLeCaoCap(player, player.iDMark.getIndexMenu(), select);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Logger.error("Lỗi mở hộp quà");
//                    }
//                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2116:
                        if (select == 0) {
                            IntrinsicService.gI().settstd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().settsnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().settsxd(player);
                        }
                        break;
//                    case ConstNpc.MENU_OPTION_USE_ITEM1667:
//                        if (select == 0) {
//                            IntrinsicService.gI().settanthu(player);
//                        }
//                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                        ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;

                    case ConstNpc.MENU_OPTION_USE_ITEM2085:
                    case ConstNpc.MENU_OPTION_USE_ITEM2086:
                    case ConstNpc.MENU_OPTION_USE_ITEM2087:
                        try {
                        ItemService.gI().openDoHuyDiet(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2013:
                    case ConstNpc.MENU_OPTION_USE_ITEM2014:
                    case ConstNpc.MENU_OPTION_USE_ITEM2015:
                        try {
                        ItemService.gI().OpenDTL(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                    case ConstNpc.MENU_OPTION_USE_ITEM2005:
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                        ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.error("Lỗi mở hộp quà");
                    }
                    break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.gI().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.ACTIVE_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().ActivePlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.gI().sendThongBao(player, "Activated  " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:

                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 1:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
//                                        PetService.gI().changePicPet(player);
                                    } else if (player.pet.typePet == 2) {
                                        PetService.gI().changeMabuPet(player);
                                    }
                                    PetService.gI().changeBerusPet(player);
                                }
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
                                    // PlayerService.gI().baoTri();
                                    Maintenance.gI().start(600);
                                    System.out.println(player.name);
                                    Service.gI().sendThongBaoFromAdmin(player, "\b|3|Bảo trì định kì trong 5 phút");

                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                if (player.isAdmin()) {

                                    this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                            "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                            "Cụm\nDoanh trại", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA", "NGỤC\nTÙ");
                                } else {
                                    Service.gI().sendThongBaoOK(player, "THích bug lệnh không t chém giờ");

                                }
                                break;
                            case 5:
                                Input.gI().createFormSenditem(player);
                                break;
                            case 6:
                                Input.gI().createFormSenditem1(player);
                                break;
                            case 7:
                                Input.gI().createFormSenditem2(player);
                                break;
                            case 8:
                                Input.gI().createFormSendGoldBar(player);
                                break;
                            case 9:
                                Input.gI().createFormBuffVND(player);
                                break;
                            case 10:
                                Input.gI().createFormBuffToTalVND(player);
                                break;
                            case 11:
                                Input.gI().createFormSenditem3(player);
                                break;
//                            case 12:
//                                MaQuaTangManager.gI().checkInfomationGiftCode(player);
//                             break;                                 

                            case 12:
                                Input.gI().createFromMailBox(player);
                                break;
                            case 13:
                                //Service.gI().showListTop(player, Manager.topTV);
                                break;
                        }
                        break;

                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:

                                BossManager.gI().createBoss(BossType.ANDROID_13);
                                BossManager.gI().createBoss(BossType.ANDROID_14);
                                BossManager.gI().createBoss(BossType.ANDROID_15);
                                BossManager.gI().createBoss(BossType.ANDROID_19);
                                BossManager.gI().createBoss(BossType.DR_KORE);
                                BossManager.gI().createBoss(BossType.KING_KONG);
                                BossManager.gI().createBoss(BossType.PIC);
                                BossManager.gI().createBoss(BossType.POC);

                                break;
                            case 1:

                                BossManager.gI().createBoss(BossType.BLACK);
                                break;
                            case 2:

                                BossManager.gI().createBoss(BossType.BROLY);
                                break;
                            case 3:

                                BossManager.gI().createBoss(BossType.SIEU_BO_HUNG);
                                BossManager.gI().createBoss(BossType.XEN_BO_HUNG);
                                break;
                            case 4:

                                Service.gI().sendThongBao(player, "Không có boss");
                                break;
                            case 5:

                                BossManager.gI().createBoss(BossType.CHAIEN);
                                BossManager.gI().createBoss(BossType.XEKO);
                                BossManager.gI().createBoss(BossType.XUKA);
                                BossManager.gI().createBoss(BossType.NOBITA);
                                BossManager.gI().createBoss(BossType.DORAEMON);
                                break;
                            case 6:

                                BossManager.gI().createBoss(BossType.FIDE);
                                break;
                            case 7:

                                BossManager.gI().createBoss(BossType.FIDE_ROBOT);
                                BossManager.gI().createBoss(BossType.VUA_COLD);
                                break;
                            case 8:

                                BossManager.gI().createBoss(BossType.SO_1);
                                BossManager.gI().createBoss(BossType.SO_2);
                                BossManager.gI().createBoss(BossType.SO_3);
                                BossManager.gI().createBoss(BossType.SO_4);
                                BossManager.gI().createBoss(BossType.TIEU_DOI_TRUONG);
                                break;
                            case 9:

                                BossManager.gI().createBoss(BossType.KUKU);
                                BossManager.gI().createBoss(BossType.MAP_DAU_DINH);
                                BossManager.gI().createBoss(BossType.RAMBO);
                                break;
                            case 10:

                                BossManager.gI().createBoss(BossType.COOLER_GOLD);
                                BossManager.gI().createBoss(BossType.CUMBER);
                                BossManager.gI().createBoss(BossType.SONGOKU_TA_AC);
                                break;
                        }
                        break;

                    //  case ConstNpc.MENU_TD:
                    case ConstNpc.SET_TSTD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;

                    // case ConstNpc.MENU_NM:
                    case ConstNpc.SET_TSNM:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;

                    //  case ConstNpc.MENU_XD:
                    case ConstNpc.SET_TSXD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;
                    case ConstNpc.SET_TLTD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settlkaio(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().settlgenki(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().settlson(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;

                    case ConstNpc.SET_TLNM:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settlpico(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().settloctieu(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().settlpiko(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;

                    case ConstNpc.SET_TLXD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settlgalick(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().settlcadick(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().settlnappa(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;
                    case ConstNpc.SET_HDTD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().sethdkaio(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().sethdgenki(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().sethdson(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;

                    case ConstNpc.SET_HDNM:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().sethdpico(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().sethdoctieu(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().sethdpiko(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;

                    case ConstNpc.SET_HDXD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().sethdcadick(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().sethdcadic(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().sethdnappa(player);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        break;
                    case ConstNpc.SET_TANTHU:
                        switch (select) {
                            case 0:
                                ItemService.gI().setTanThu(player);
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        if (select == 0) {
                            Clan clan = player.clan;
                            clan.deleteDB(clan.id);
                            Manager.CLANS.remove(clan);
                            player.clan = null;
                            player.clanMember = null;
                            ClanService.gI().sendMyClan(player);
                            ClanService.gI().sendClanId(player);
                            Service.gI().sendThongBao(player, "Đã giải tán bang hội.");
                        }
                        break;
                    case ConstNpc.CONFIRM_ACTIVE:
                        if (select == 0) {
                            if (player.getSession().goldBar >= 20) {
                                player.getSession().actived = true;
                                if (PlayerDAO.subGoldBar(player, 20)) {
                                    Service.gI().sendThongBao(player, "Đã mở thành viên thành công!");
                                } else {
                                    this.npcChat(player, "Lỗi vui lòng báo admin...");
                                }
                            }
//                                Service.gI().sendThongBao(player, "Bạn không có vàng\n Vui lòng NROGOD.COM để nạp thỏi vàng");
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            player.inventory.itemsBoxCrackBall.replaceAll(ignored -> ItemService.gI().createItemNull());
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_RUONG_PHU:
                        if (select == 0) {
                            player.inventory.itemsRuongPhu.replaceAll(ignored -> ItemService.gI().createItemNull());
                            player.inventory.itemsRuongPhu.clear();
                            Service.gI().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_MAIL_BOX:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsMailBox.size(); i++) {
                                player.inventory.itemsMailBox.set(i, ItemService.gI().createItemNull());
                            }
                            if (GodGK.updateMailBox(player)) {
                                Service.gI().sendThongBao(player, "Xóa hết vật phẩm hòm thư thành công");
                            }
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{
                                        "Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.gI().sendThongBao(player, "Kick người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                                case 5:
                                    String[] selectss = new String[]{
                                        "Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.ACTIVE_PLAYER, -1,
                                            "Bạn có chắc chắn muốn mở thành viên cho " + p.name, selectss, p);
                                    break;
                            }
                        }
                        break;
//                    case ConstNpc.MENU_EVENT:
//                        switch (select) { 
//                            case 0:
//                                Service.gI().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
//                             break;                                 
//                            case 1:
//                                Util.showListTop(player, (byte) 2);
//                             break;                                 
//                            case 2:
//                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
////                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
////                                        "100 bông", "1000 bông", "10000 bông");
//                             break;                                 
//                            case 3:
//                                Service.gI().sendThongBao(player, "Sự kiện đã kết thúc...");
////                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
////                                        "Đồng ý", "Từ chối");
//                             break;                                 
//
//                        }
//                     break;                                 
                    case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.gI().sendMoney(player);
                        }
                        break;
                }

            }
        };

    }

    public static void openMenuSuKien(Player player, Npc npc, int tempId, int select) {
        switch (Manager.EVENT_SEVER) {
            case 0:
                break;
            case 1:// hlw
                switch (select) {
                    case 0:
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                            Item keo = InventoryServiceNew.gI().finditemnguyenlieuKeo(player);
                            Item banh = InventoryServiceNew.gI().finditemnguyenlieuBanh(player);
                            Item bingo = InventoryServiceNew.gI().finditemnguyenlieuBingo(player);

                            if (keo != null && banh != null && bingo != null) {
                                Item GioBingo = ItemService.gI().createNewItem((short) 2016, 1);

                                // - Số item sự kiện có trong rương
                                InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 10);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, banh, 10);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, bingo, 10);

                                GioBingo.itemOptions.add(new ItemOption(74, 0));
                                InventoryServiceNew.gI().addItemBag(player, GioBingo);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Vui lòng chuẩn bị x10 Nguyên Liệu Kẹo, Bánh Quy, Bí Ngô để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 1:
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryServiceNew.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryServiceNew.gI().finditemnguyenlieuGiokeo(player);

                            if (ve != null && giokeo != null) {
                                Item Hopmaquy = ItemService.gI().createNewItem((short) 2017, 1);
                                // - Số item sự kiện có trong rương
                                InventoryServiceNew.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, giokeo, 3);

                                Hopmaquy.itemOptions.add(new ItemOption(74, 0));
                                InventoryServiceNew.gI().addItemBag(player, Hopmaquy);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 2:
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryServiceNew.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryServiceNew.gI().finditemnguyenlieuGiokeo(player);
                            Item hopmaquy = InventoryServiceNew.gI().finditemnguyenlieuHopmaquy(player);

                            if (ve != null && giokeo != null && hopmaquy != null) {
                                Item HopQuaHLW = ItemService.gI().createNewItem((short) 2012, 1);
                                // - Số item sự kiện có trong rương
                                InventoryServiceNew.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, giokeo, 3);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, hopmaquy, 3);

                                HopQuaHLW.itemOptions.add(new ItemOption(74, 0));
                                HopQuaHLW.itemOptions.add(new ItemOption(30, 0));
                                InventoryServiceNew.gI().addItemBag(player, HopQuaHLW);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player,
                                        "Đổi quà hộp quà sự kiện Halloween thành công");
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Hộp Ma Quỷ, x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                }
                break;
            case 2:// 20/11
//                switch (select) {
//                    case 3:
//                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                            int evPoint = player.event.getEventPoint();
//                            if (evPoint >= 999) {
//                                Item HopQua = ItemService.gI().createNewItem((short) 2021, 1);
//                                player.event.setEventPoint(evPoint - 999);
//
//                                HopQua.itemOptions.add(new ItemOption(74, 0));
//                                HopQua.itemOptions.add(new ItemOption(30, 0));
//                                InventoryServiceNew.gI().addItemBag(player, HopQua);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.gI().sendThongBao(player, "Bạn nhận được Hộp Quà Teacher Day");
//                            } else {
//                                Service.gI().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Hành trang đầy.");
//                        }
//                        break;
//                    default:
//                        int n = 0;
//                        switch (select) {
//                            case 0:
//                                n = 1;
//                                break;
//                            case 1:
//                                n = 10;
//                                break;
//                            case 2:
//                                n = 99;
//                                break;
//                        }
//
//                        if (n > 0) {
//                            Item bonghoa = InventoryServiceNew.gI().finditemBongHoa(player, n);
//                            if (bonghoa != null) {
//                                int evPoint = player.event.getEventPoint();
//                                player.event.setEventPoint(evPoint + n);
//                                ;
//                                InventoryServiceNew.gI().subQuantityItemsBag(player, bonghoa, n);
//                                Service.gI().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
//                                int pre;
//                                int next;
//                                String text = null;
//                                AttributeManager am = ServerManager.gI().getAttributeManager();
//                                switch (tempId) {
//                                    case ConstNpc.THAN_MEO_KARIN:
//                                        pre = EVENT_COUNT_THAN_MEO / 999;
//                                        EVENT_COUNT_THAN_MEO += n;
//                                        next = EVENT_COUNT_THAN_MEO / 999;
//                                        if (pre != next) {
//                                            am.setTime(ConstAttribute.TNSM, 3600);
//                                            text = "Toàn bộ máy chủ tăng được 20% TNSM cho đệ tử khi đánh quái trong 60 phút.";
//                                        }
//                                        break;
//
//                                    case ConstNpc.QUY_LAO_KAME:
//                                        pre = EVENT_COUNT_QUY_LAO_KAME / 999;
//                                        EVENT_COUNT_QUY_LAO_KAME += n;
//                                        next = EVENT_COUNT_QUY_LAO_KAME / 999;
//                                        if (pre != next) {
//                                            am.setTime(ConstAttribute.VANG, 3600);
//                                            text = "Toàn bộ máy chủ được tăng 100% vàng từ quái trong 60 phút.";
//                                        }
//                                        break;
//
//                                    case ConstNpc.THUONG_DE:
//                                        pre = EVENT_COUNT_THUONG_DE / 999;
//                                        EVENT_COUNT_THUONG_DE += n;
//                                        next = EVENT_COUNT_THUONG_DE / 999;
//                                        if (pre != next) {
//                                            am.setTime(ConstAttribute.KI, 3600);
//                                            text = "Toàn bộ máy chủ được tăng 20% KI trong 60 phút.";
//                                        }
//                                        break;
//
//                                    case ConstNpc.THAN_VU_TRU:
//                                        pre = EVENT_COUNT_THAN_VU_TRU / 999;
//                                        EVENT_COUNT_THAN_VU_TRU += n;
//                                        next = EVENT_COUNT_THAN_VU_TRU / 999;
//                                        if (pre != next) {
//                                            am.setTime(ConstAttribute.HP, 3600);
//                                            text = "Toàn bộ máy chủ được tăng 20% HP trong 60 phút.";
//                                        }
//                                        break;
//
//                                    case ConstNpc.BILL:
//                                        pre = EVENT_COUNT_THAN_HUY_DIET / 999;
//                                        EVENT_COUNT_THAN_HUY_DIET += n;
//                                        next = EVENT_COUNT_THAN_HUY_DIET / 999;
//                                        if (pre != next) {
//                                            am.setTime(ConstAttribute.SUC_DANH, 3600);
//                                            text = "Toàn bộ máy chủ được tăng 20% Sức đánh trong 60 phút.";
//                                        }
//                                        break;
//                                }
//                                if (text != null) {
//                                    Service.gI().sendThongBaoAllPlayer(text);
//                                }
//
//                            } else {
//                                Service.gI().sendThongBao(player,
//                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                            }
//                        } else {
//                            Service.gI().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
//                        }
//                }
                break;
            case 3:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    Item keogiangsinh = InventoryServiceNew.gI().finditemKeoGiangSinh(player);

                    if (keogiangsinh != null && keogiangsinh.quantity >= 99) {
                        Item tatgiangsinh = ItemService.gI().createNewItem((short) 649, 1);
                        // - Số item sự kiện có trong rương
                        InventoryServiceNew.gI().subQuantityItemsBag(player, keogiangsinh, 99);

                        tatgiangsinh.itemOptions.add(new ItemOption(74, 0));
                        tatgiangsinh.itemOptions.add(new ItemOption(30, 0));
                        InventoryServiceNew.gI().addItemBag(player, tatgiangsinh);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBao(player, "Bạn nhận được Tất,vớ giáng sinh");
                    } else {
                        Service.gI().sendThongBao(player,
                                "Vui lòng chuẩn bị x99 kẹo giáng sinh để đổi vớ tất giáng sinh");
                    }
                } else {
                    Service.gI().sendThongBao(player, "Hành trang đầy.");
                }
                break;
            case 4:
                switch (select) {
                    case 0:
//                         player.event.setReceivedLuckyMoney(false);
                        if (!player.event.isReceivedLuckyMoney()) {
                            Calendar cal = Calendar.getInstance();
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            if (day >= 1 && day <= 24) {
                                Item goldBar = ItemService.gI().createNewItem((short) 1989,
                                        Util.nextInt(1, 3));
                                player.inventory.ruby += Util.nextInt(10, 30);
                                goldBar.quantity = Util.nextInt(1, 3);
                                InventoryServiceNew.gI().addItemBag(player, goldBar);
                                InventoryServiceNew.gI().sendItemBags(player);
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                player.event.setReceivedLuckyMoney(true);
                                Service.gI().sendThongBao(player,
                                        "Nhận lì xì thành công,chúc bạn năm mới dui dẻ");
                            } else if (day > 24) {
                                Service.gI().sendThongBao(player, "Hết tết rồi còn đòi lì xì");
                            } else {
                                Service.gI().sendThongBao(player, "Đã tết đâu mà đòi lì xì");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Bạn đã nhận lì xì rồi");
                        }
                        break;
                    case 1:
                        ShopServiceNew.gI().opendShop(player, "SHOP_DIEM", false);

                        break;
                }
                break;
            case ConstEvent.SU_KIEN_8_3:
                switch (select) {

                    case 3:
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item capsule = ItemService.gI().createNewItem((short) 722, 1);
                                player.event.setEventPoint(evPoint - 999);

                                capsule.itemOptions.add(new ItemOption(76, 0));
                                capsule.itemOptions.add(new ItemOption(30, 1));
                                InventoryServiceNew.gI().addItemBag(player, capsule);
                                InventoryServiceNew.gI().sendItemBags(player);
                                Service.gI().sendThongBao(player, "Bạn nhận được Capsule Hồng");
                            } else {
                                Service.gI().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 4:
                        ShopServiceNew.gI().opendShop(player, "SHOP_DIEM", false);

                        break;
                    case 5:
                        ChangeMapService.gI().changeMapBySpaceShip(player, 214, 0, 512);

                        break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryServiceNew.gI().finditemnuochoaVIP(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);
                                InventoryServiceNew.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.gI().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Cần ít nhất " + n + " nước hoa VIP để có thể tặng");
                            }
                        } else {
                            Service.gI().sendThongBao(player, "Cần ít nhất " + n + " nước hoa VIP để có thể tặng");
                        }
                }
                break;
        }
    }

    public static String getMenuSuKien(int id) {
        switch (id) {
            case ConstEvent.KHONG_CO_SU_KIEN:
                return "Chưa có\n Sự Kiện";
            case ConstEvent.SU_KIEN_HALLOWEEN:
                return "Sự Kiện\nHaloween";
            case ConstEvent.SU_KIEN_20_11:
                return "Sự Kiện\n 20/11";
            case ConstEvent.SU_KIEN_NOEL:
                return "Sự Kiện\n Giáng Sinh";
            case ConstEvent.SU_KIEN_TET:
                return "Sự Kiện\n Tết Nguyên\nĐán 2024";
            case ConstEvent.SU_KIEN_8_3:
                return "Sự Kiện\n Hậu 8/3";
        }
        return "Chưa có\n Sự Kiện";
    }

}
