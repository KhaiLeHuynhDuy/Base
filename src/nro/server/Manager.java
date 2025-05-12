package nro.server;

//import com.arriety.card.OptionCard;
//import com.arriety.card.RadarCard;
//import com.arriety.card.RadarService;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import encrypt.ImageUtil;

import nro.consts.ConstPlayer;
import nro.consts.ConstMap;
import nro.data.DataGame;
import nro.jdbc.daos.ShopDAO;
import nro.kygui.ItemKyGui;
import nro.kygui.ShopKyGuiManager;
import nro.models.Template.*;
import nro.models.card.OptionCard;
import nro.models.card.RadarService;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.intrinsic.Intrinsic;
import nro.models.item.Item;
import nro.models.map.WayPoint;
import nro.models.matches.TOP;
import nro.models.matches.pvp.DaiHoiVoThuat;
import nro.models.npc.Npc;
import nro.models.npc.NpcFactory;
import nro.models.player.Player;
import nro.models.player.Referee;
import nro.models.player.Yajiro;
import nro.models.reward.ItemMobReward;
import nro.models.reward.ItemOptionMobReward;
import nro.models.reward.MobReward;
import nro.models.shop.Shop;
import nro.models.skill.NClass;
import nro.models.skill.Skill;
import nro.models.task.SideTaskTemplate;
import nro.models.task.SubTaskMain;
import nro.models.task.TaskMain;

import nro.services.ItemService;
import nro.services.MapService;
import nro.utils.Logger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nro.utils.Util;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import nro.consts.ConstItem;
import nro.lib.RandomCollection;
import nro.manager.ConsignManager;
import nro.models.consignment.ConsignmentItem;
import nro.models.consignment.ConsignmentShop;
import nro.models.item.CaiTrang;
import nro.models.item.Item.ItemOption;
import nro.models.map.EffectMap;
import nro.models.map.Zone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Manager {

    private static Manager i;
    public static int EVENT_SEVER = 0;
    public static String SERVER_NAME = "";
//         public static int EVENT_COUNT_THAN_HUY_DIET = 0;
//    public static int EVENT_COUNT_QUY_LAO_KAME = 0;
//    public static int EVENT_COUNT_THAN_MEO = 0;
//    public static int EVENT_COUNT_THUONG_DE = 0;
//    public static int EVENT_COUNT_THAN_VU_TRU = 0;
    public static byte SERVER = 1;
    public static byte SECOND_WAIT_LOGIN = 5;
    public static int MAX_PER_IP = 10000;
    public static int MAX_PLAYER = 10000;
    public static byte RATE_EXP_SERVER = 1;
    public static boolean LOCAL = false;

    public static CaiTrang getCaiTrangByItemId(int itemId) {
        for (CaiTrang caiTrang : CAI_TRANGS) {
            if (caiTrang.tempId == itemId) {
                return caiTrang;
            }
        }
        return null;
    }
    public static final List<CaiTrang> CAI_TRANGS = new ArrayList<>();
    public static MapTemplate[] MAP_TEMPLATES;
    public static final List<nro.models.map.Map> MAPS = new ArrayList<>();
    public static final List<ItemOptionTemplate> ITEM_OPTION_TEMPLATES = new ArrayList<>();
    public static final Map<Integer, MobReward> MOB_REWARDS = new HashMap<>();
    public static final List<ItemLuckyRound> LUCKY_ROUND_REWARDS = new ArrayList();
    public static final List<AchievementTemplate> ACHIEVEMENTS = new ArrayList<>();
    public static final Map<String, Byte> IMAGES_BY_NAME = new HashMap<String, Byte>();
    public static final List<ItemTemplate> ITEM_TEMPLATES = new ArrayList<>();
    public static final List<ItemTemplate> CAITRANG_2HEADS = new ArrayList<>();
    public static final List<MobTemplate> MOB_TEMPLATES = new ArrayList<>();
    public static final List<NpcTemplate> NPC_TEMPLATES = new ArrayList<>();
    public static final List<String> CAPTIONS = new ArrayList<>();
    public static final List<TaskMain> TASKS = new ArrayList<>();
    public static List<Item> CT = new ArrayList<>();

    public static List<Item> PET = new ArrayList<>();
    public static final List<SideTaskTemplate> SIDE_TASKS_TEMPLATE = new ArrayList<>();
    public static final List<Intrinsic> INTRINSICS = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_TD = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_NM = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_XD = new ArrayList<>();
    public static final List<HeadAvatar> HEAD_AVATARS = new ArrayList<>();
    public static final List<FlagBag> FLAGS_BAGS = new ArrayList<>();
    public static final List<NClass> NCLASS = new ArrayList<>();
    public static final List<Npc> NPCS = new ArrayList<>();
    public static List<Shop> SHOPS = new ArrayList<>();
    public static final List<Clan> CLANS = new ArrayList<>();
    public static final List<String> NOTIFY = new ArrayList<>();
    public static final ArrayList<DaiHoiVoThuat> LIST_DHVT = new ArrayList<>();
    public static final List<Item> RUBY_REWARDS = new ArrayList<>();
    public static final RandomCollection<Integer> HONG_DAO_CHIN = new RandomCollection<>();
    public static final RandomCollection<Integer> HOP_QUA_TET = new RandomCollection<>();
    //public static final short[] manhts = {1067, 1068, 1069, 1070, 1066};
    public static final short[] tv = {457};
    // public static final short[] chu = {537, 538, 539, 540};
    public static final short[] itembuff = {74, 191, 192, 211, 212};
    public static final short[] ca = {1001, 1002, 1003, 1004, 1010, 1011, 1012};
    public static final short[] thucan = {663, 664, 665, 666, 667};
    //public static final short[] itemhe2023 = {2157, 2158, 2159, 2160};
    //public static final short[] itemtet = {748, 749, 750, 751};
    public static final short[] nr = {18, 19, 20};
    public static final short[] nrbdkb = {18, 19};
    public static final short[] ibdkb = {77, 861};
    //public static final short[] bktk = {2120, 2120};
    public static final short[] spl = { 441, 442, 443, 444, 446, 447};
    public static final short[] nr4s = {17};
    public static final short[] nr1_2s = {14, 15};
//    public static final short[] trungthu = {886,
//        887,
//        888,
//        889};
    public static final short[] ID_ITEMS_EFF = {1555,
        1556,
        1557,
        1558,
        1559,
        1560,
        1561,
        1562,
        1563,
        1564,
        1565,
        1566,
        1567,
        1568,
        1569,
        1570,
        1571,
        1572,
        1573,
        1574,
        1575,
        1576,
        1577,
        1578,
        1579,
        1580,
        1581,
        1582,
        1583,
        1584,
        1585,
        1586,
        1587,
        1588,
        1589};
    public static final short[] itemIds_TL = {555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567, 561};
    public static final short[] itemAVATAR_BLACK = {1124, 1125};
    public static final short[] itemIds_NR_SB = {15,16, 17, 18};
    public static final short[] itemIds_DVT = {1396, 1397, 1398, 1399, 1400};
    public static final short[] itemIds_tinhthach = {1360, 1361, 1362, 1363, 1364, 1365, 1366};
    public static final short[] itemDC12 = {233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277};
    public static final List<Integer> MapNguHanhSon = Arrays.asList(122, 123, 124);
    public static final List<Integer> MapTuongLai = Arrays.asList(92, 93, 94, 95, 96, 97, 98, 99, 100);
    public static final List<Integer> MapBDKB = Arrays.asList(135, 136, 137, 138);
    public static final List<Integer> MapDoanhTrai = Arrays.asList(53, 54, 55, 56, 57, 58, 59, 60, 61, 62);
    public static final List<Integer> MapThucVat = Arrays.asList(160, 161, 162, 163);
    public static final List<Integer> MapNgucTu = Arrays.asList(155);
    public static final List<Integer> MapTD = Arrays.asList(0,
            1,
            2,
            3,
            4,
            5,
            6,
            27,
            28,
            29,
            30);
    public static final List<Integer> MapNM = Arrays.asList(7,
            8,
            9,
            10,
            11,
            12,
            13,
            31,
            32,
            33,
            34);
    public static final List<Integer> MapXD = Arrays.asList(14,
            15,
            16,
            17,
            18,
            19,
            20,
            35,
            36,
            37,
            38);
    public static final List<Integer> MapLDBH = Arrays.asList(156,
            157,
            158,
            159);
    public static final List<Integer> MapFide = Arrays.asList(63,
            64,
            65,
            66,
            67,
            68,
            69,
            70,
            71,
            72,
            73,
            74,
            75,
            76,
            77,
            79,
            80,
            81,
            82,
            83);
    public static final List<Integer> MapCold = Arrays.asList(105,
            106,
            107,
            108,
            109,
            110);
    public static final List<Integer> MapDiaNguc = Arrays.asList(
            192,
            193,
            194);
    public static final List<Integer> MapThungLung = Arrays.asList(
            176);
    public static final List<Integer> MapRungMo = Arrays.asList(
            250);
    public static final List<Integer> MapCungTrang = Arrays.asList(
            202);
    public static final List<Integer> MapMocnhan = Arrays.asList(0,
            7,
            14);

    //khai comment
    public static final String queryTopSM = "SELECT id, CAST( split_str(data_point,',',2) AS UNSIGNED) AS sm FROM player ORDER BY CAST( split_str(data_point,',',2) AS UNSIGNED) DESC LIMIT 20;";

//    public static final String queryTopSM2 = "SELECT id, CAST( split_str(data_point,',',2) AS UNSIGNED) AS sm FROM player WHERE account_id > 2047 ORDER BY CAST( split_str(data_point,',',2) AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopSD = "SELECT id, CAST( split_str(data_point,',',8)  AS UNSIGNED) AS sd FROM player ORDER BY CAST( split_str(data_point,',',8)  AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopHP = "SELECT id, CAST( split_str(data_point,',',6) AS UNSIGNED) AS hp FROM player ORDER BY CAST( split_str(data_point,',',6) AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopKI = "SELECT id, CAST( split_str(data_point,',',7) AS UNSIGNED) AS ki FROM player ORDER BY CAST( split_str(data_point,',',7) AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopNV = "SELECT id, CAST( split_str(split_str(data_task,',',1),'[',2) AS UNSIGNED) AS nv FROM player ORDER BY CAST( split_str(split_str(data_task,',',1),'[',2) AS UNSIGNED) DESC, CAST(split_str(data_task,',',2) AS UNSIGNED) DESC, CAST( split_str(data_point,',',2) AS UNSIGNED) DESC LIMIT 50;";
//    public static final String queryTopSK = "SELECT id, CAST( split_str( data_inventory,',',5) AS UNSIGNED) AS event FROM player ORDER BY CAST( split_str( data_inventory,',',5) AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopPVP = "SELECT id, CAST( split_str( data_inventory,',',3) AS UNSIGNED) AS HONGNGOC FROM player ORDER BY CAST( split_str( data_inventory,',',3) AS UNSIGNED) DESC LIMIT 10;";
//    public static final String queryTopNHS = "SELECT id, CAST( NguHanhSonPoint AS UNSIGNED) AS nhs FROM player ORDER BY CAST( NguHanhSonPoint AS UNSIGNED) DESC LIMIT 20;";
//    public static final String queryTopSKHE = "SELECT id, CAST( topsk16 AS UNSIGNED) AS topsk16 FROM account ORDER BY CAST( topsk16 AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopSK20_10 = "SELECT id, CAST( sk20_10 AS UNSIGNED) AS sk20_10 FROM account ORDER BY CAST( sk20_10 AS UNSIGNED) DESC LIMIT 20;";
//    public static final String queryTopSKHE2 = "SELECT player.id, account.topsk16 FROM account, player WHERE account.id = player.account_id  ORDER BY topsk16 DESC LIMIT 10";
    public static final String queryTopTV = "select \n"
            + "  player.id , \n"
            + "  account.id as accountId, \n"
            + "  player.name, \n"
            + "	CAST(\n"
            + "    REPLACE(\n"
            + "      SUBSTRING_INDEX(\n"
            + "        SUBSTRING_INDEX(\n"
            + "          CONCAT(\n"
            + "            '[457,', \n"
            + "            SUBSTRING_INDEX(\n"
            + "              SUBSTRING_INDEX(player.items_bag, '[457,', -1), \n"
            + "              ']', \n"
            + "              1\n"
            + "            )\n"
            + "          ), \n"
            + "          ',', \n"
            + "          2\n"
            + "        ), \n"
            + "        ']', \n"
            + "        1\n"
            + "      ), \n"
            + "      '[457,', \n"
            + "      ''\n"
            + "    ) as unsigned\n"
            + "  ) as tv_hanhtrang, \n"
            + "	CAST(\n"
            + "    REPLACE(\n"
            + "      SUBSTRING_INDEX(\n"
            + "        SUBSTRING_INDEX(\n"
            + "          CONCAT(\n"
            + "            '[457,', \n"
            + "            SUBSTRING_INDEX(\n"
            + "              SUBSTRING_INDEX(player.items_box, '[457,', -1), \n"
            + "              ']', \n"
            + "              1\n"
            + "            )\n"
            + "          ), \n"
            + "          ',', \n"
            + "          2\n"
            + "        ), \n"
            + "        ']', \n"
            + "        1\n"
            + "      ), \n"
            + "      '[457,', \n"
            + "      ''\n"
            + "    ) as unsigned\n"
            + "  ) as tv_ruong,\n"
            + "  CAST(\n"
            + "    REPLACE(\n"
            + "      SUBSTRING_INDEX(\n"
            + "        SUBSTRING_INDEX(\n"
            + "          CONCAT(\n"
            + "            '[457,', \n"
            + "            SUBSTRING_INDEX(\n"
            + "              SUBSTRING_INDEX(player.items_bag, '[457,', -1), \n"
            + "              ']', \n"
            + "              1\n"
            + "            )\n"
            + "          ), \n"
            + "          ',', \n"
            + "          2\n"
            + "        ), \n"
            + "        ']', \n"
            + "        1\n"
            + "      ), \n"
            + "      '[457,', \n"
            + "      ''\n"
            + "    ) as unsigned\n"
            + "  ) + CAST(\n"
            + "    REPLACE(\n"
            + "      SUBSTRING_INDEX(\n"
            + "        SUBSTRING_INDEX(\n"
            + "          CONCAT(\n"
            + "            '[457,', \n"
            + "            SUBSTRING_INDEX(\n"
            + "              SUBSTRING_INDEX(player.items_box, '[457,', -1), \n"
            + "              ']', \n"
            + "              1\n"
            + "            )\n"
            + "          ), \n"
            + "          ',', \n"
            + "          2\n"
            + "        ), \n"
            + "        ']', \n"
            + "        1\n"
            + "      ), \n"
            + "      '[457,', \n"
            + "      ''\n"
            + "    ) as unsigned\n"
            + "  ) AS thoi_vang \n"
            + "from \n"
            + "  player \n"
            + "  inner join account on account.id = player.account_id \n"
            + "where \n"
            + "  (\n"
            + "    player.items_box like '%\"[457,%' \n"
            + "    or player.items_bag like '%\"[457,%'\n"
            + "  ) \n"
            + "order by \n"
            + "  thoi_vang DESC \n"
            + "limit \n"
            + "  30;";
//    public static final int DAU_THAN = 1475;
//    public static final String queryTopDauThan = "select \n"
//            + "  account.id as accountId, \n"
//            + "  player.name, \n"
//            + "	CAST(\n"
//            + "    REPLACE(\n"
//            + "      SUBSTRING_INDEX(\n"
//            + "        SUBSTRING_INDEX(\n"
//            + "          CONCAT(\n"
//            + "            '[" + DAU_THAN + ",', \n"
//            + "            SUBSTRING_INDEX(\n"
//            + "              SUBSTRING_INDEX(player.items_bag, '[" + DAU_THAN + ",', -1), \n"
//            + "              ']', \n"
//            + "              1\n"
//            + "            )\n"
//            + "          ), \n"
//            + "          ',', \n"
//            + "          2\n"
//            + "        ), \n"
//            + "        ']', \n"
//            + "        1\n"
//            + "      ), \n"
//            + "      '[" + DAU_THAN + ",', \n"
//            + "      ''\n"
//            + "    ) as unsigned\n"
//            + "  ) as tv_hanhtrang, \n"
//            + "	CAST(\n"
//            + "    REPLACE(\n"
//            + "      SUBSTRING_INDEX(\n"
//            + "        SUBSTRING_INDEX(\n"
//            + "          CONCAT(\n"
//            + "            '[" + DAU_THAN + ",', \n"
//            + "            SUBSTRING_INDEX(\n"
//            + "              SUBSTRING_INDEX(player.items_box, '[" + DAU_THAN + ",', -1), \n"
//            + "              ']', \n"
//            + "              1\n"
//            + "            )\n"
//            + "          ), \n"
//            + "          ',', \n"
//            + "          2\n"
//            + "        ), \n"
//            + "        ']', \n"
//            + "        1\n"
//            + "      ), \n"
//            + "      '[" + DAU_THAN + ",', \n"
//            + "      ''\n"
//            + "    ) as unsigned\n"
//            + "  ) as tv_ruong,\n"
//            + "  CAST(\n"
//            + "    REPLACE(\n"
//            + "      SUBSTRING_INDEX(\n"
//            + "        SUBSTRING_INDEX(\n"
//            + "          CONCAT(\n"
//            + "            '[" + DAU_THAN + ",', \n"
//            + "            SUBSTRING_INDEX(\n"
//            + "              SUBSTRING_INDEX(player.items_bag, '[" + DAU_THAN + ",', -1), \n"
//            + "              ']', \n"
//            + "              1\n"
//            + "            )\n"
//            + "          ), \n"
//            + "          ',', \n"
//            + "          2\n"
//            + "        ), \n"
//            + "        ']', \n"
//            + "        1\n"
//            + "      ), \n"
//            + "      '[" + DAU_THAN + ",', \n"
//            + "      ''\n"
//            + "    ) as unsigned\n"
//            + "  ) + CAST(\n"
//            + "    REPLACE(\n"
//            + "      SUBSTRING_INDEX(\n"
//            + "        SUBSTRING_INDEX(\n"
//            + "          CONCAT(\n"
//            + "            '[" + DAU_THAN + ",', \n"
//            + "            SUBSTRING_INDEX(\n"
//            + "              SUBSTRING_INDEX(player.items_box, '[" + DAU_THAN + ",', -1), \n"
//            + "              ']', \n"
//            + "              1\n"
//            + "            )\n"
//            + "          ), \n"
//            + "          ',', \n"
//            + "          2\n"
//            + "        ), \n"
//            + "        ']', \n"
//            + "        1\n"
//            + "      ), \n"
//            + "      '[" + DAU_THAN + ",', \n"
//            + "      ''\n"
//            + "    ) as unsigned\n"
//            + "  ) AS thoi_vang \n"
//            + "from \n"
//            + "  player \n"
//            + "  inner join account on account.id = player.account_id \n"
//            + "where \n"
//            + "  (\n"
//            + "    player.items_box like '%\"[" + DAU_THAN + ",%' \n"
//            + "    or player.items_bag like '%\"[" + DAU_THAN + ",%'\n"
//            + " )\n"
//            + "having thoi_vang >= 0 \n"
//            + "order by \n"
//            + "  thoi_vang DESC \n"
//            + "limit \n"
//            + "  10;";

    public static List<TOP> topSM;
    public static List<TOP> topSM2;
    public static List<TOP> topSD;
    public static List<TOP> topHP;
    public static List<TOP> topKI;
    public static List<TOP> topNV;
    public static List<TOP> topSK;
    public static List<TOP> topPVP;
    public static List<TOP> topNHS;
    public static List<TOP> topSKHE;
    public static List<TOP> topTV;
    public static List<TOP> topDauThan;
    public static List<TOP> topSieuHang;
    public static long timeRealTop = 0;
    public static final short[] aotd = {138, 139, 230, 231, 232, 233, 555};
    public static final short[] quantd = {142, 143, 242, 243, 244, 245, 556};
    public static final short[] gangtd = {146, 147, 254, 255, 256, 257, 562};
    public static final short[] giaytd = {150, 151, 266, 267, 268, 269, 563};
    public static final short[] aoxd = {170, 171, 238, 239, 240, 241, 559};
    public static final short[] quanxd = {174, 175, 250, 251, 252, 253, 560};
    public static final short[] gangxd = {178, 179, 262, 263, 264, 265, 566};
    public static final short[] giayxd = {182, 183, 274, 275, 276, 277, 567};
    public static final short[] aonm = {154, 155, 234, 235, 236, 237, 557};
    public static final short[] quannm = {158, 159, 246, 247, 248, 249, 558};
    public static final short[] gangnm = {162, 163, 258, 259, 260, 261, 564};
    public static final short[] giaynm = {166, 167, 270, 271, 272, 273, 565};
    public static final short[] radaSKHVip = {186, 187, 278, 279, 280, 281, 561};

    public static final short[][][] doSKHVip = {{aotd, quantd, gangtd, giaytd}, {aonm, quannm, gangnm, giaynm}, {aoxd, quanxd, gangxd, giayxd}};

    public static Manager gI() {
        if (i == null) {
            i = new Manager();
        }
        return i;
    }

    public static List<TOP> realTopSieuHang(Player pl) {
        List<TOP> tops = new ArrayList<>();
        try {
            //System.out.println("SELECT id, rank_sieu_hang AS rank FROM player WHERE rank_sieu_hang <= " + pl.rankSieuHang + " ORDER BY rank_sieu_hang DESC LIMIT 10");
            GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT id, rank_sieu_hang AS rank FROM player WHERE rank_sieu_hang <= " + pl.rankSieuHang + " ORDER BY rank_sieu_hang DESC LIMIT 10");
            while (rs.next()) {
                int rank = rs.getInt("rank_sieu_hang");
                if (Math.abs(rank - pl.rankSieuHang) <= 10) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                    top.setInfo1("");
                    top.setInfo2("");
                    top.setRank(rank);
                    tops.add(top);
                }
            }
            Collections.reverse(tops);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tops;
    }

    public static List<TOP> realTopSieuHang(Connection con) {
        List<TOP> tops = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id, rank_sieu_hang AS rank FROM player ORDER BY rank_sieu_hang ASC LIMIT 100");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long rank = rs.getLong("rank");
                if (rank > 0 && rank <= 100) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                    top.setInfo1("");
                    top.setInfo2("");
                    tops.add(top);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tops;
    }

    //    private Manager() {
//        try {
//            loadProperties();
//        } catch (IOException ex) {
//            // ex.printStackTrace();
//            Logger.logException(Manager.class, ex, "Thông báo: Lỗi tải properites");
//            System.exit(0);
//        }
//        this.loadDatabase();
//        NpcFactory.createNpcConMeo();
//        NpcFactory.createNpcRongThieng();
//        this.initMap();
//
//        BanDoKhoBauService bdkb = new BanDoKhoBauService();
//        new Thread(bdkb, "thread bản đồ kho báu").start();
//        System.out.println("init bdkb thành công");
//    }
    private Manager() {
        try {
            loadProperties();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.logException(Manager.class, ex, "Lỗi load properites");
            System.exit(0);
        }
//        ImageUtil.initImage();
        this.initSmallVersion();
        this.loadDatabase();
        initRandomItem();
        nro.TamBaoService.loadItem();
        NpcFactory.createNpcConMeo();
        NpcFactory.createNpcRongThieng();
        NpcFactory.createNpcRongXuong();
        this.initMap();
    }

    private void initRandomItem() {
        HONG_DAO_CHIN.add(10, 1440);
        HONG_DAO_CHIN.add(10, 1441);

        HOP_QUA_TET.add(10, 1440);
        HOP_QUA_TET.add(10, 1441);
        HOP_QUA_TET.add(10, 1442);
        HOP_QUA_TET.add(10, 1443);
        HOP_QUA_TET.add(10, 1444);

        HOP_QUA_TET.add(10, 1130);
        HOP_QUA_TET.add(10, 1211);
        HOP_QUA_TET.add(10, 1214);
        HOP_QUA_TET.add(10, 1215);
        HOP_QUA_TET.add(10, 1216);
        HOP_QUA_TET.add(10, 1258);
        HOP_QUA_TET.add(10, 1256);
        HOP_QUA_TET.add(10, ConstItem.MANH_AO);
        HOP_QUA_TET.add(10, ConstItem.MANH_QUAN);
        HOP_QUA_TET.add(10, ConstItem.MANH_GIAY);
        HOP_QUA_TET.add(10, ConstItem.MANH_NHAN);
        HOP_QUA_TET.add(10, ConstItem.MANH_GANG_TAY);
        HOP_QUA_TET.add(1, 1430);
        HOP_QUA_TET.add(50, 1423);
        HOP_QUA_TET.add(50, 1424);
        HOP_QUA_TET.add(50, 1425);
        HOP_QUA_TET.add(50, 1429);
        HOP_QUA_TET.add(10, ConstItem.CUONG_NO_2);
        HOP_QUA_TET.add(10, ConstItem.BO_HUYET_2);
        HOP_QUA_TET.add(10, ConstItem.BO_KHI_2);
    }

    private void initMap() {
        int[][] tileTyleTop = readTileIndexTileType(ConstMap.TILE_TOP);
        for (MapTemplate mapTemp : MAP_TEMPLATES) {
            int[][] tileMap = readTileMap(mapTemp.id);
            int[] tileTop = tileTyleTop[mapTemp.tileId - 1];
            nro.models.map.Map map = null;
//            if (mapTemp.id == 126) {
//                map = new SantaCity(mapTemp.id,
//                        mapTemp.name, mapTemp.planetId, mapTemp.tileId, mapTemp.bgId,
//                        mapTemp.bgType, mapTemp.type, tileMap, tileTop,
//                        mapTemp.zones, mapTemp.isMapOffline(),
//                        mapTemp.maxPlayerPerZone, mapTemp.wayPoints, mapTemp.effectMaps);
//                SantaCity santaCity = (SantaCity) map;
//                santaCity.timer(22, 0, 0, 3600000);
//            } else {
            map = new nro.models.map.Map(mapTemp.id,
                    mapTemp.name, mapTemp.planetId, mapTemp.tileId, mapTemp.bgId,
                    mapTemp.bgType, mapTemp.type, tileMap, tileTop,
                    mapTemp.zones, mapTemp.isMapOffline(),
                    mapTemp.maxPlayerPerZone, mapTemp.wayPoints, mapTemp.effectMaps);
//            }
            if (map != null) {
                MAPS.add(map);
                map.initMob(mapTemp.mobTemp, mapTemp.mobLevel, mapTemp.mobHp, mapTemp.mobX, mapTemp.mobY);
                map.initNpc(mapTemp.npcId, mapTemp.npcX, mapTemp.npcY);
                new Thread(map, "Update map " + map.mapName).start();
            }

        }
//        new Thread(() -> {
//            try {
//                while (!Maintenance.isRuning) {
//                    long st = System.currentTimeMillis();
//                    for (nro.models.map.Map map : MAPS) {
//                        for (Zone zone : map.zones) {
//                            try {
//                                zone.update();
//                            } catch (Exception e) {
//                            }
//                        }
//                    }
//                    long timeDo = System.currentTimeMillis() - st;
//                    if (1000 - timeDo > 0) {
//                        Thread.sleep(1000 - timeDo);
//                    }
//                }
//            } catch (Exception e) {
//            }
//        }, "Update Maps").start();
//        Referee r = new Referee();
//        r.initReferee();
//        Yajiro r1 = new Yajiro();
//        r1.initYajiro();
        Logger.success("Init map thành công!");
    }
    public static byte[][] SMALL_VERSION_DATA;

    public void initSmallVersion() {
        try {
            SMALL_VERSION_DATA = new byte[4][];
            for (int i = 0; i < 4; i++) {
                File file = new File("data/girlkun/icon/x" + (i + 1) + "/");
                if (!file.exists()) {
                    continue;
                }

                List<File> files = Arrays.stream(Objects.requireNonNull(file.listFiles())).toList()
                        .stream().filter(f -> !f.getName().startsWith("."))
                        .sorted(Comparator.comparing(f -> Integer.valueOf(Util.cutPng(f.getName())))).toList();

                int max = Integer.parseInt(Util.cutPng(files.get(files.size() - 1).getName()));
                SMALL_VERSION_DATA[i] = new byte[max + 1];

                for (File f : files) {
                    if (f.getName().startsWith(".")) {
                        continue;
                    }
                    String name = Util.cutPng(f.getName());
                    int id;
                    try {
                        id = Integer.parseInt(name);
                    } catch (NumberFormatException e) {
                        id = 0;
                    }
//                    System.out.println("ID: " + id);

                    SMALL_VERSION_DATA[i][id] = (byte) (Files.readAllBytes(f.toPath()).length % 127);
                }
                Logger.success("Khởi tạo small version x" + (i + 1) + " thành công!\n");
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.logException(Manager.class, ex, "Lỗi load small version");
        }
    }

    private static void loadPart() {
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            //load part
            ps = con.prepareStatement("select * from part");
            rs = ps.executeQuery();
            List<Part> parts = new ArrayList<>();
            while (rs.next()) {
                Part part = new Part();
                part.id = rs.getShort("id");
                part.type = rs.getByte("type");
                dataArray = (JSONArray) jv.parse(rs.getString("data").replaceAll("\\\"", ""));
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONArray pd = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                    part.partDetails.add(new PartDetail(Short.parseShort(String.valueOf(pd.get(0))),
                            Byte.parseByte(String.valueOf(pd.get(1))),
                            Byte.parseByte(String.valueOf(pd.get(2)))));
                    pd.clear();
                }
                parts.add(part);
                dataArray.clear();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("data/girlkun/update_data/part"));
            dos.writeShort(parts.size());
            for (Part part : parts) {
                dos.writeByte(part.type);
                for (PartDetail partDetail : part.partDetails) {
                    dos.writeShort(partDetail.iconId);
                    dos.writeByte(partDetail.dx);
                    dos.writeByte(partDetail.dy);
                }
            }
            dos.flush();
            dos.close();
            Logger.success("Thông báo tải dữ liệu part thành công (" + parts.size() + ")\n");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void loadDatabase() {
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("select * from part");
            rs = ps.executeQuery();
            List<Part> parts = new ArrayList<>();
            while (rs.next()) {
                Part part = new Part();
                part.id = rs.getShort("id");
                part.type = rs.getByte("type");
                dataArray = (JSONArray) jv.parse(rs.getString("data").replaceAll("\\\"", ""));
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONArray pd = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                    part.partDetails.add(new PartDetail(Short.parseShort(String.valueOf(pd.get(0))),
                            Byte.parseByte(String.valueOf(pd.get(1))),
                            Byte.parseByte(String.valueOf(pd.get(2)))));
                    pd.clear();
                }
                parts.add(part);
                dataArray.clear();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("data/girlkun/update_data/part"));
            dos.writeShort(parts.size());
            for (Part part : parts) {
                dos.writeByte(part.type);
                for (PartDetail partDetail : part.partDetails) {
                    dos.writeShort(partDetail.iconId);
                    dos.writeByte(partDetail.dx);
                    dos.writeByte(partDetail.dy);
                }
            }
            dos.flush();
            dos.close();
            Logger.success("Thông báo tải dữ liệu part thành công (" + parts.size() + ")\n");

//            //load small version
//            ps = con.prepareStatement("select count(id) from small_version");
//            rs = ps.executeQuery();
//            List<byte[]> smallVersion = new ArrayList<>();
//            if (rs.first()) {
//                int size = rs.getInt(1);
//                for (int i = 0; i < 4; i++) {
//                    smallVersion.add(new byte[size]);
//                }
//            }
//            ps = con.prepareStatement("select * from small_version order by id");
//            rs = ps.executeQuery();
//            int index = 0;
//            while (rs.next()) {
//                smallVersion.get(0)[index] = rs.getByte("x1");
//                smallVersion.get(1)[index] = rs.getByte("x2");
//                smallVersion.get(2)[index] = rs.getByte("x3");
//                smallVersion.get(3)[index] = rs.getByte("x4");
//                index++;
//            }
//            for (int i = 0; i < 4; i++) {
//                dos = new DataOutputStream(new FileOutputStream("data/girlkun/data_img_version/x" + (i + 1) + "/img_version"));
//                dos.writeShort(smallVersion.get(i).length);
//                for (int j = 0; j < smallVersion.get(i).length; j++) {
//                    dos.writeByte(smallVersion.get(i)[j]);
//                }
//                dos.flush();
//                dos.close();
//            }
            //load clan
            ps = con.prepareStatement("select * from clan_sv" + SERVER);
            rs = ps.executeQuery();
            while (rs.next()) {
                Clan clan = new Clan();
                clan.id = rs.getInt("id");
                clan.name = rs.getString("name");
                clan.slogan = rs.getString("slogan");
                clan.imgId = rs.getByte("img_id");
                clan.powerPoint = rs.getLong("power_point");
                clan.maxMember = rs.getByte("max_member");
                clan.capsuleClan = rs.getInt("clan_point");
                clan.level = rs.getByte("level");
                clan.createTime = (int) (rs.getTimestamp("create_time").getTime() / 1000);
                dataArray = (JSONArray) jv.parse(rs.getString("members"));
                for (int i = 0; i < dataArray.size(); i++) {
                    dataObject = (JSONObject) jv.parse(String.valueOf(dataArray.get(i)));
                    ClanMember cm = new ClanMember();
                    cm.clan = clan;
                    cm.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                    cm.name = String.valueOf(dataObject.get("name"));
                    cm.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                    cm.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                    cm.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                    cm.role = Byte.parseByte(String.valueOf(dataObject.get("role")));
                    cm.donate = Integer.parseInt(String.valueOf(dataObject.get("donate")));
                    cm.receiveDonate = Integer.parseInt(String.valueOf(dataObject.get("receive_donate")));
                    cm.memberPoint = Integer.parseInt(String.valueOf(dataObject.get("member_point")));
                    cm.clanPoint = Integer.parseInt(String.valueOf(dataObject.get("clan_point")));
                    cm.joinTime = Integer.parseInt(String.valueOf(dataObject.get("join_time")));
                    //khai thu fix phan nay
                    try {
                        Object askPeaTime = dataObject.get("ask_pea_time");
                        if (askPeaTime != null && !"null".equals(String.valueOf(askPeaTime))) {
                            cm.timeAskPea = Long.parseLong(String.valueOf(askPeaTime));
                        } else {
                            cm.timeAskPea = 0;
                        }
                    } catch (Exception e) {
                        cm.timeAskPea = 0;
                    }
                    try {
                        Object powerValue = dataObject.get("power");
                        if (powerValue != null && !String.valueOf(powerValue).equalsIgnoreCase("null")) {
                            cm.powerPoint = Long.parseLong(String.valueOf(powerValue));
                        } else {
                            cm.powerPoint = 0L; // Gán mặc định nếu giá trị là "null"
                        }
                    } catch (Exception e) {
                        cm.powerPoint = 0L; // Đảm bảo không bị gián đoạn
                        e.printStackTrace();
                    }
                    clan.addClanMember(cm);
                }
                dataArray = (JSONArray) JSONValue.parse(rs.getString("doanh_trai"));
                if (!dataArray.isEmpty()) {
                    clan.doanhTrai_lastTimeOpen = Long.parseLong(String.valueOf(dataArray.get(0)));
                    clan.doanhTrai_playerOpen = (String) dataArray.get(1);

                }
                dataArray = (JSONArray) JSONValue.parse(rs.getString("ban_do_kho_bau"));
                if (!dataArray.isEmpty()) {
                    clan.banDoKhoBau_lastTimeOpen = Long.parseLong(String.valueOf(dataArray.get(0)));
                    clan.banDoKhoBau_playerOpen = (String) dataArray.get(1);
                }
                dataArray = (JSONArray) JSONValue.parse(rs.getString("ran_doc"));
                if (!dataArray.isEmpty()) {
                    clan.ConDuongRanDoc_lastTimeOpen = Long.parseLong(String.valueOf(dataArray.get(0)));
                    clan.CDRD_playerOpen = (String) dataArray.get(1);
                }
                CLANS.add(clan);
                dataArray.clear();
                dataObject.clear();
            }

            ps = con.prepareStatement("select id from clan_sv" + SERVER + " order by id desc limit 1");
            rs = ps.executeQuery();
            if (rs.first()) {
                Clan.NEXT_ID = rs.getInt("id") + 1;
            }

            Logger.success("Load clan thành công (" + CLANS.size() + "), clan next id: " + Clan.NEXT_ID + "\n");

            ps = con.prepareStatement("select * from dhvt_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                DaiHoiVoThuat dhvt = new DaiHoiVoThuat();
                dhvt.NameCup = rs.getString(2);
                dhvt.Time = rs.getString(3).split("\n");
                dhvt.gem = rs.getInt(4);
                dhvt.gold = rs.getInt(5);
                dhvt.min_start = rs.getInt(6);
                dhvt.min_start_temp = rs.getInt(6);
                dhvt.min_limit = rs.getInt(7);
                LIST_DHVT.add(dhvt);
            }
            Logger.success("Thông báo: tải dữ liệu Đại Hội Võ Thuật thành công (" + LIST_DHVT.size() + ")\n");

            //load skill
            ps = con.prepareStatement("select * from skill_template order by nclass_id, slot");
            rs = ps.executeQuery();
            byte nClassId = -1;
            NClass nClass = null;
            while (rs.next()) {
                byte id = rs.getByte("nclass_id");
                if (id != nClassId) {
                    nClassId = id;
                    nClass = new NClass();
                    nClass.name = id == ConstPlayer.TRAI_DAT ? "Trái Đất" : id == ConstPlayer.NAMEC ? "Namếc" : "Xayda";
                    nClass.classId = nClassId;
                    NCLASS.add(nClass);
                }
                SkillTemplate skillTemplate = new SkillTemplate();
                skillTemplate.classId = nClassId;
                skillTemplate.id = rs.getByte("id");
                skillTemplate.name = rs.getString("name");
                skillTemplate.maxPoint = rs.getByte("max_point");
                skillTemplate.manaUseType = rs.getByte("mana_use_type");
                skillTemplate.type = rs.getByte("type");
                skillTemplate.iconId = rs.getShort("icon_id");
                skillTemplate.damInfo = rs.getString("dam_info");
                nClass.skillTemplatess.add(skillTemplate);

                dataArray = (JSONArray) jv.parse(
                        rs.getString("skills")
                                .replaceAll("\\[\"", "[")
                                .replaceAll("\"\\[", "[")
                                .replaceAll("\"\\]", "]")
                                .replaceAll("\\]\"", "]")
                                .replaceAll("\\}\",\"\\{", "},{")
                );
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONObject dts = (JSONObject) jv.parse(String.valueOf(dataArray.get(j)));
                    Skill skill = new Skill();
                    skill.template = skillTemplate;
                    skill.skillId = Short.parseShort(String.valueOf(dts.get("id")));
                    skill.point = Byte.parseByte(String.valueOf(dts.get("point")));
                    skill.powRequire = Long.parseLong(String.valueOf(dts.get("power_require")));
                    skill.manaUse = Integer.parseInt(String.valueOf(dts.get("mana_use")));
                    skill.coolDown = Integer.parseInt(String.valueOf(dts.get("cool_down")));
                    skill.dx = Integer.parseInt(String.valueOf(dts.get("dx")));
                    skill.dy = Integer.parseInt(String.valueOf(dts.get("dy")));
                    skill.maxFight = Integer.parseInt(String.valueOf(dts.get("max_fight")));
                    skill.damage = Short.parseShort(String.valueOf(dts.get("damage")));
                    skill.price = Short.parseShort(String.valueOf(dts.get("price")));
                    skill.moreInfo = String.valueOf(dts.get("info"));
                    skillTemplate.skillss.add(skill);
                }
            }

            Logger.success("Thông báo tải dữ liệu skill thành công (" + NCLASS.size() + ")\n");

            //load head avatar
            ps = con.prepareStatement("select * from head_avatar");
            rs = ps.executeQuery();
            while (rs.next()) {
                HeadAvatar headAvatar = new HeadAvatar(rs.getInt("head_id"), rs.getInt("avatar_id"));
                HEAD_AVATARS.add(headAvatar);
            }
            Logger.success("Thông báo tải dữ liệu head avatar thành công (" + HEAD_AVATARS.size() + ")\n");

            //load flag bag
            ps = con.prepareStatement("select * from flag_bag");
            rs = ps.executeQuery();
            while (rs.next()) {
                FlagBag flagBag = new FlagBag();
                flagBag.id = rs.getShort("id");
                flagBag.name = rs.getString("name");
                flagBag.gold = rs.getInt("gold");
                flagBag.gem = rs.getInt("gem");
                flagBag.iconId = rs.getShort("icon_id");
                String[] iconData = rs.getString("icon_data").split(",");
                flagBag.iconEffect = new short[iconData.length];
                for (int j = 0; j < iconData.length; j++) {
                    try {
                        flagBag.iconEffect[j] = Short.parseShort(iconData[j].trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // Xử lý lỗi ở đây, ví dụ:
                        Logger.error("Lỗi chuyển đổi số nguyên ngắn tại dòng " + (j + 1) + ": " + e.getMessage());
                    }
                }
                FLAGS_BAGS.add(flagBag);
            }
            Logger.success("Thông báo tải dữ liệu flag bag thành công (" + FLAGS_BAGS.size() + ")\n");

            //load intrinsic
            ps = con.prepareStatement("select * from intrinsic");
            rs = ps.executeQuery();
            while (rs.next()) {
                Intrinsic intrinsic = new Intrinsic();
                intrinsic.id = rs.getByte("id");
                intrinsic.name = rs.getString("name");
                intrinsic.paramFrom1 = rs.getShort("param_from_1");
                intrinsic.paramTo1 = rs.getShort("param_to_1");
                intrinsic.paramFrom2 = rs.getShort("param_from_2");
                intrinsic.paramTo2 = rs.getShort("param_to_2");
                intrinsic.icon = rs.getShort("icon");
                intrinsic.gender = rs.getByte("gender");
                switch (intrinsic.gender) {
                    case ConstPlayer.TRAI_DAT:
                        INTRINSIC_TD.add(intrinsic);
                        break;
                    case ConstPlayer.NAMEC:
                        INTRINSIC_NM.add(intrinsic);
                        break;
                    case ConstPlayer.XAYDA:
                        INTRINSIC_XD.add(intrinsic);
                        break;
                    default:
                        INTRINSIC_TD.add(intrinsic);
                        INTRINSIC_NM.add(intrinsic);
                        INTRINSIC_XD.add(intrinsic);
                }
                INTRINSICS.add(intrinsic);
            }
            Logger.success("Thông báo tải dữ liệu intrinsic thành công (" + INTRINSICS.size() + ")\n");

            // load nhiem vu bo mong
//            ps = con.prepareStatement("SELECT * FROM achievement");
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                AchievementTemplate achi = new AchievementTemplate(
//                        rs.getInt("id"),
//                        rs.getString("info1"),
//                        rs.getString("info2"),
//                        rs.getInt("count_purpose"),
//                        rs.getInt("gem"));
//                ACHIEVEMENTS.add(achi);
//            }
//            Logger.success("Load achievement done (" + ACHIEVEMENTS.size() + ")");
            //load task
            ps = con.prepareStatement("SELECT id, task_main_template.name, detail, "
                    + "task_sub_template.name AS 'sub_name', max_count, notify, npc_id, map "
                    + "FROM task_main_template JOIN task_sub_template ON task_main_template.id = "
                    + "task_sub_template.task_main_id");
            rs = ps.executeQuery();
            int taskId = -1;
            TaskMain task = null;
            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != taskId) {
                    taskId = id;
                    task = new TaskMain();
                    task.id = taskId;
                    task.name = rs.getString("name");
                    task.detail = rs.getString("detail");
                    TASKS.add(task);
                }
                SubTaskMain subTask = new SubTaskMain();
                subTask.name = rs.getString("sub_name");
                subTask.maxCount = rs.getShort("max_count");
                subTask.notify = rs.getString("notify");
                subTask.npcId = rs.getByte("npc_id");
                subTask.mapId = rs.getShort("map");
                task.subTasks.add(subTask);
            }
            Logger.success("Thông báo tải dữ liệu task thành công (" + TASKS.size() + ")\n");

            //load side task
            ps = con.prepareStatement("select * from side_task_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                SideTaskTemplate sideTask = new SideTaskTemplate();
                sideTask.id = rs.getInt("id");
                sideTask.name = rs.getString("name");
                String[] mc1 = rs.getString("max_count_lv1").split("-");
                String[] mc2 = rs.getString("max_count_lv2").split("-");
                String[] mc3 = rs.getString("max_count_lv3").split("-");
                String[] mc4 = rs.getString("max_count_lv4").split("-");
                String[] mc5 = rs.getString("max_count_lv5").split("-");
                sideTask.count[0][0] = Integer.parseInt(mc1[0]);
                sideTask.count[0][1] = Integer.parseInt(mc1[1]);
                sideTask.count[1][0] = Integer.parseInt(mc2[0]);
                sideTask.count[1][1] = Integer.parseInt(mc2[1]);
                sideTask.count[2][0] = Integer.parseInt(mc3[0]);
                sideTask.count[2][1] = Integer.parseInt(mc3[1]);
                sideTask.count[3][0] = Integer.parseInt(mc4[0]);
                sideTask.count[3][1] = Integer.parseInt(mc4[1]);
                sideTask.count[4][0] = Integer.parseInt(mc5[0]);
                sideTask.count[4][1] = Integer.parseInt(mc5[1]);
                SIDE_TASKS_TEMPLATE.add(sideTask);
            }
            Logger.success("Thông báo tải dữ liệu side task thành công (" + SIDE_TASKS_TEMPLATE.size() + ")\n");

            //load item template
            ps = con.prepareStatement("select * from item_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                ItemTemplate itemTemp = new ItemTemplate();
                itemTemp.id = rs.getShort("id");
                itemTemp.type = rs.getByte("type");
                itemTemp.gender = rs.getByte("gender");
                itemTemp.name = rs.getString("name");
                itemTemp.description = rs.getString("description");
                itemTemp.iconID = rs.getShort("icon_id");
                itemTemp.part = rs.getShort("part");
                itemTemp.isUpToUp = rs.getBoolean("is_up_to_up");
                itemTemp.strRequire = rs.getInt("power_require");
                itemTemp.gold = rs.getInt("gold");
                itemTemp.gem = rs.getInt("gem");
                itemTemp.head = rs.getInt("head");
                String[] head2Str = rs.getString("headF").split(",");
                itemTemp.body = rs.getInt("body");
                itemTemp.leg = rs.getInt("leg");

                for (String s : head2Str) {
                    int headF = Integer.parseInt(s);
                    if (headF != -1) {
                        itemTemp.head2.add(Integer.valueOf(s));
                    }
                }

                if (!itemTemp.head2.isEmpty()) {
                    CAITRANG_2HEADS.add(itemTemp);
                }

                ITEM_TEMPLATES.add(itemTemp);
            }
            Logger.success("Thông báo tải dữ liệu map item template thành công (" + ITEM_TEMPLATES.size() + ")\n");
//            Logger.success("Thông báo tải dữ liệu map item template thành công (" + ITEM_TEMPLATES.size() + ")\n");
//            for (int i = 0; i < ITEM_TEMPLATES.size(); i++) {
//                if (ITEM_TEMPLATES.get(i).type == 5) {
//                    CT.add(ItemService.gI().createNewItem((short) ITEM_TEMPLATES.get(i).id));
//                }
//            }

            //load item option template
            ps = con.prepareStatement("select id, name from item_option_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                ItemOptionTemplate optionTemp = new ItemOptionTemplate();
                optionTemp.id = rs.getInt("id");
                optionTemp.name = rs.getString("name");
                ITEM_OPTION_TEMPLATES.add(optionTemp);
            }
            Logger.success("Thông báo tải dữ liệu map item option template thành công (" + ITEM_OPTION_TEMPLATES.size() + ")\n");

            //load shop
            SHOPS = ShopDAO.getShops(con);
            Logger.success("Thông báo tải dữ liệu shop thành công (" + SHOPS.size() + ")\n");

            //load reward lucky round
            File folder = new File("data/girlkun/data_lucky_round_reward");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        line = line.replaceAll("[{}\\[\\]]", "");
                        String[] arrSub = line.split("\\|");
                        String[] data1 = arrSub[0].split(":");
                        ItemLuckyRound item = new ItemLuckyRound();
                        item.temp = ItemService.gI().getTemplate(Integer.parseInt(data1[0]));
                        item.ratio = Integer.parseInt(data1[1]);
                        item.typeRatio = Integer.parseInt(data1[2]);
                        if (arrSub.length > 1) {
                            String[] data2 = arrSub[1].split(";");
                            for (String str : data2) {
                                String[] data = str.split(":");
                                ItemOptionLuckyRound io = new ItemOptionLuckyRound();
                                Item.ItemOption itemOption = new Item.ItemOption(Integer.parseInt(data[0]), 0);
                                io.itemOption = itemOption;
                                String[] param = data[1].split("-");
                                io.param1 = Integer.parseInt(param[0]);
                                if (param.length == 2) {
                                    io.param2 = Integer.parseInt(param[1]);
                                }
                                item.itemOptions.add(io);
                            }
                        }
                        LUCKY_ROUND_REWARDS.add(item);
                    }
                }
            }
            Logger.success("Thông báo tải dữ liệu reward lucky round thành công (" + LUCKY_ROUND_REWARDS.size() + ")\n");
            //load reward mob
            folder = new File("data/girlkun/mob_reward");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    DataInputStream dis = new DataInputStream(new FileInputStream(fileEntry));
                    int size = dis.readInt();
                    for (int i = 0; i < size; i++) {
                        int mobId = dis.readInt();
                        MobReward mobReward = MOB_REWARDS.get(mobId);
                        if (mobReward == null) {
                            mobReward = new MobReward(mobId);
                            MOB_REWARDS.put(mobId, mobReward);
                        }
                        int itemId = dis.readInt();
                        String[] quantity = dis.readUTF().split("-");
                        String[] ratio = dis.readUTF().split("-");
                        int gender = dis.readInt();
                        String map = dis.readUTF();
                        String[] arrMap = map.replaceAll("[\\]\\[]", "").split(",");
                        int[] mapDrop = new int[arrMap.length];
                        for (int g = 0; g < mapDrop.length; g++) {
                            mapDrop[g] = Integer.parseInt(arrMap[g]);
                        }
                        ItemMobReward item = new ItemMobReward(itemId, mapDrop,
                                new int[]{Integer.parseInt(quantity[0]), Integer.parseInt(quantity[1])},
                                new int[]{Integer.parseInt(ratio[0]), Integer.parseInt(ratio[1])}, gender);
                        if (item.getTemp().type == 30) { // sao pha lê
                            item.setRatio(new int[]{20, Integer.parseInt(ratio[1])});
                        }
                        if (item.getTemp().type == 14) { //14 đá nâng cấp
                            item.setRatio(new int[]{20, Integer.parseInt(ratio[1])});
                        }
                        if (item.getTemp().type < 5) {
                            item.setRatio(new int[]{Integer.parseInt(ratio[0]), Integer.parseInt(ratio[1]) / 4 * 3});
                        }
                        if (item.getTemp().type == 9) { //vàng
                            mobReward.getGoldReward().add(item);
                        } else {
                            mobReward.getItemReward().add(item);
                        }
                        int sizeOption = dis.readInt();
                        for (int j = 0; j < sizeOption; j++) {
                            int optionId = dis.readInt();
                            String[] param = dis.readUTF().split("-");
                            String[] ratioOption = dis.readUTF().split("-");
                            ItemOptionMobReward option = new ItemOptionMobReward(optionId,
                                    new int[]{Integer.parseInt(param[0]), Integer.parseInt(param[1])},
                                    new int[]{Integer.parseInt(ratioOption[0]), Integer.parseInt(ratioOption[1])});
                            item.getOption().add(option);
                        }
                    }

                }
            }
            Logger.success("Thông báo tải dữ liệu reward mob thành công (" + MOB_REWARDS.size() + ")\n");

            //load notify
            folder = new File("data/girlkun/notify");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    StringBuffer notify = new StringBuffer(fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf("."))).append("<>");
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        notify.append(line + "\n");
                    }
                    NOTIFY.add(notify.toString());
                }
            }
            Logger.success("Thông báo tải dữ liệu notify thành công (" + NOTIFY.size() + ")\n");

            //load caption
            ps = con.prepareStatement("select * from caption");
            rs = ps.executeQuery();
            while (rs.next()) {
                CAPTIONS.add(rs.getString("name"));
            }
            Logger.success("Thông báo tải dữ liệu caption thành công (" + CAPTIONS.size() + ")\n");

            //load image by name
            ps = con.prepareStatement("select name, n_frame from img_by_name");
            rs = ps.executeQuery();
            while (rs.next()) {
                IMAGES_BY_NAME.put(rs.getString("name"), rs.getByte("n_frame"));
            }
            Logger.success("Thông báo tải dữ liệu images by name thành công (" + IMAGES_BY_NAME.size() + ")\n");

            //load mob template
            ps = con.prepareStatement("select * from mob_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                MobTemplate mobTemp = new MobTemplate();
                mobTemp.id = rs.getByte("id");
                mobTemp.type = rs.getByte("type");
                mobTemp.name = rs.getString("name");
                mobTemp.hp = rs.getInt("hp");
                mobTemp.rangeMove = rs.getByte("range_move");
                mobTemp.speed = rs.getByte("speed");
                mobTemp.dartType = rs.getByte("dart_type");
                mobTemp.percentDame = rs.getByte("percent_dame");
                mobTemp.percentTiemNang = rs.getByte("percent_tiem_nang");
                MOB_TEMPLATES.add(mobTemp);
            }
            Logger.success("Thông báo tải dữ liệu mob template thành công (" + MOB_TEMPLATES.size() + ")\n");

            ps = con.prepareStatement("SELECT * FROM shop_ky_gui");
            rs = ps.executeQuery();
            while (rs.next()) {
                int i = rs.getInt("id");
                int idPl = rs.getInt("player_id");
                byte tab = rs.getByte("tab");
                short itemId = rs.getShort("item_id");
                int gold = rs.getInt("gold");
                int gem = rs.getInt("gem");
//                int thoivang = rs.getInt("thoivang");
                int quantity = rs.getInt("quantity");
                byte isUp = rs.getByte("isUpTop");
                boolean isBuy = rs.getByte("isBuy") == 1;
                List<Item.ItemOption> op = new ArrayList<>();
                JSONArray jsa2 = (JSONArray) JSONValue.parse(rs.getString("itemOption"));
                for (int j = 0; j < jsa2.size(); ++j) {
                    JSONObject jso2 = (JSONObject) jsa2.get(j);
                    int idOptions = Integer.parseInt(jso2.get("id").toString());
                    int param = Integer.parseInt(jso2.get("param").toString());
                    op.add(new Item.ItemOption(idOptions, param));
                }
                ShopKyGuiManager.gI().listItem.add(new ItemKyGui(i, itemId, idPl, tab, gold, gem, quantity, isUp, op, isBuy));
            }
            Logger.success("Thông báo tải dữ liệu item ký gửi [" + ShopKyGuiManager.gI().listItem.size() + "]!\n");

//            ps = con.prepareStatement("SELECT * FROM shop_ky_gui");
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                int i = rs.getInt("id");
//                int idPl = rs.getInt("player_id");
//                byte tab = rs.getByte("tab");
//                short itemId = rs.getShort("item_id");
//                int gold = rs.getInt("gold");
//                int gem = rs.getInt("gem");
////                int thoivang = rs.getInt("thoivang");
//                int quantity = rs.getInt("quantity");
//                byte isUp = rs.getByte("isUpTop");
//                boolean isBuy = rs.getByte("isBuy") == 1;
//                List<Item.ItemOption> op = new ArrayList<>();
//                JSONArray jsa2 = (JSONArray) JSONValue.parse(rs.getString("itemOption"));
//                for (int j = 0; j < jsa2.size(); ++j) {
//                    JSONObject jso2 = (JSONObject) jsa2.get(j);
//                    int idOptions = Integer.parseInt(jso2.get("id").toString());
//                    int param = Integer.parseInt(jso2.get("param").toString());
//                    op.add(new Item.ItemOption(idOptions, param));
//                }
//                long update_at = Long.parseLong(rs.getString("update_at"));
//                ShopKyGuiManager.gI().listItem.add(
//                        new ItemKyGui(i, itemId, idPl, tab, gold, gem, quantity, isUp, op, isBuy, update_at)
//                );
//
//            }
//            Logger.success("Thông báo tải dữ liệu item ký gửi [" + ShopKyGuiManager.gI().listItem.size() + "]!\n");
            //load npc template
            ps = con.prepareStatement("select * from npc_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                NpcTemplate npcTemp = new NpcTemplate();
                npcTemp.id = rs.getByte("id");
                npcTemp.name = rs.getString("name");
                npcTemp.head = rs.getShort("head");
                npcTemp.body = rs.getShort("body");
                npcTemp.leg = rs.getShort("leg");
                npcTemp.avatar = rs.getInt("avatar");
                NPC_TEMPLATES.add(npcTemp);
            }
            Logger.success("Thông báo tải dữ liệu npc template thành công (" + NPC_TEMPLATES.size() + ")\n");

            //load map template
            ps = con.prepareStatement("select count(id) from map_template");
            rs = ps.executeQuery();
            if (rs.first()) {
                int countRow = rs.getShort(1);
                MAP_TEMPLATES = new MapTemplate[countRow];
                ps = con.prepareStatement("select * from map_template");
                rs = ps.executeQuery();
                short i = 0;
                while (rs.next()) {
                    MapTemplate mapTemplate = new MapTemplate();
                    int mapId = rs.getInt("id");
                    String mapName = rs.getString("name");
                    mapTemplate.id = mapId;
                    mapTemplate.name = mapName;
                    //load data

                    dataArray = (JSONArray) jv.parse(rs.getString("data"));
                    mapTemplate.type = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    mapTemplate.planetId = Byte.parseByte(String.valueOf(dataArray.get(1)));
                    mapTemplate.bgType = Byte.parseByte(String.valueOf(dataArray.get(2)));
                    mapTemplate.tileId = Byte.parseByte(String.valueOf(dataArray.get(3)));
                    mapTemplate.bgId = Byte.parseByte(String.valueOf(dataArray.get(4)));
                    dataArray.clear();
                    ///////////////////////////////////////////////////////////////////
                    mapTemplate.type = rs.getByte("type");
                    mapTemplate.planetId = rs.getByte("planet_id");
                    mapTemplate.bgType = rs.getByte("bg_type");
                    mapTemplate.tileId = rs.getByte("tile_id");
                    mapTemplate.bgId = rs.getByte("bg_id");
                    mapTemplate.zones = rs.getByte("zones");
                    mapTemplate.maxPlayerPerZone = rs.getByte("max_player");
                    //load waypoints
                    dataArray = (JSONArray) jv.parse(rs.getString("waypoints")
                            .replaceAll("\\[\"\\[", "[[")
                            .replaceAll("\\]\"\\]", "]]")
                            .replaceAll("\",\"", ",")
                    );
                    for (int j = 0; j < dataArray.size(); j++) {
                        WayPoint wp = new WayPoint();
                        JSONArray dtwp = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                        wp.name = String.valueOf(dtwp.get(0));
                        wp.minX = Short.parseShort(String.valueOf(dtwp.get(1)));
                        wp.minY = Short.parseShort(String.valueOf(dtwp.get(2)));
                        wp.maxX = Short.parseShort(String.valueOf(dtwp.get(3)));
                        wp.maxY = Short.parseShort(String.valueOf(dtwp.get(4)));
                        wp.isEnter = Byte.parseByte(String.valueOf(dtwp.get(5))) == 1;
                        wp.isOffline = Byte.parseByte(String.valueOf(dtwp.get(6))) == 1;
                        wp.goMap = Short.parseShort(String.valueOf(dtwp.get(7)));
                        wp.goX = Short.parseShort(String.valueOf(dtwp.get(8)));
                        wp.goY = Short.parseShort(String.valueOf(dtwp.get(9)));
                        mapTemplate.wayPoints.add(wp);
                        dtwp.clear();
                    }
                    dataArray.clear();
                    //load mobs
                    dataArray = (JSONArray) jv.parse(rs.getString("mobs").replaceAll("\\\"", ""));
                    mapTemplate.mobTemp = new byte[dataArray.size()];
                    mapTemplate.mobLevel = new byte[dataArray.size()];
                    mapTemplate.mobHp = new long[dataArray.size()];
                    mapTemplate.mobX = new short[dataArray.size()];
                    mapTemplate.mobY = new short[dataArray.size()];
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONArray dtm = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                        mapTemplate.mobTemp[j] = Byte.parseByte(String.valueOf(dtm.get(0)));
                        mapTemplate.mobLevel[j] = Byte.parseByte(String.valueOf(dtm.get(1)));
                        mapTemplate.mobHp[j] = new BigDecimal(String.valueOf(dtm.get(2))).longValue();

                        mapTemplate.mobX[j] = Short.parseShort(String.valueOf(dtm.get(3)));
                        mapTemplate.mobY[j] = Short.parseShort(String.valueOf(dtm.get(4)));
                        dtm.clear();
                    }
                    dataArray.clear();
                    //load npcs
                    dataArray = (JSONArray) jv.parse(rs.getString("npcs").replaceAll("\\\"", ""));
                    mapTemplate.npcId = new byte[dataArray.size()];
                    mapTemplate.npcX = new short[dataArray.size()];
                    mapTemplate.npcY = new short[dataArray.size()];
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONArray dtn = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                        mapTemplate.npcId[j] = Byte.parseByte(String.valueOf(dtn.get(0)));
                        mapTemplate.npcX[j] = Short.parseShort(String.valueOf(dtn.get(1)));
                        mapTemplate.npcY[j] = Short.parseShort(String.valueOf(dtn.get(2)));
                        dtn.clear();
                    }
                    dataArray.clear();

                    dataArray = (JSONArray) jv.parse(rs.getString("effect"));
                    for (int j = 0; j < dataArray.size(); j++) {
                        EffectMap em = new EffectMap();
                        dataObject = (JSONObject) jv.parse(dataArray.get(j).toString());
                        em.setKey(String.valueOf(dataObject.get("key")));
                        em.setValue(String.valueOf(dataObject.get("value")));
                        mapTemplate.effectMaps.add(em);
                    }
                    if (Manager.EVENT_SEVER == 3) {
                        EffectMap em = new EffectMap();
                        em.setKey("beff");
                        em.setValue("11");
                        mapTemplate.effectMaps.add(em);
                    }
                    dataArray.clear();
                    dataObject.clear();
                    MAP_TEMPLATES[i++] = mapTemplate;
                }
                Logger.success("Thông báo tải dữ liệu map template thành công (" + MAP_TEMPLATES.length + ")\n");
                RUBY_REWARDS.add(Util.sendDo(861, 0, new ArrayList<>()));
            }

            ps = con.prepareStatement("select * from radar");
            rs = ps.executeQuery();
            while (rs.next()) {
                nro.models.card.RadarCard rd = new nro.models.card.RadarCard();
                rd.Id = rs.getShort("id");
                rd.IconId = rs.getShort("iconId");
                rd.Rank = rs.getByte("rank");
                rd.Max = rs.getByte("max");
                rd.Type = rs.getByte("type");
                rd.Template = rs.getShort("template");
                rd.Name = rs.getString("name");
                rd.Info = rs.getString("info");
                JSONArray arr = (JSONArray) JSONValue.parse(rs.getString("body"));
                for (int i = 0; i < arr.size(); i++) {
                    JSONObject ob = (JSONObject) arr.get(i);
                    if (ob != null) {
                        rd.Head = Short.parseShort(ob.get("head").toString());
                        rd.Body = Short.parseShort(ob.get("body").toString());
                        rd.Leg = Short.parseShort(ob.get("leg").toString());
                        rd.Bag = Short.parseShort(ob.get("bag").toString());
                    }
                }
                rd.Options.clear();
                arr = (JSONArray) JSONValue.parse(rs.getString("options"));
                for (int i = 0; i < arr.size(); i++) {
                    JSONObject ob = (JSONObject) arr.get(i);
                    if (ob != null) {
                        rd.Options.add(new OptionCard(Integer.parseInt(ob.get("id").toString()),
                                Short.parseShort(ob.get("param").toString()),
                                Byte.parseByte(ob.get("activeCard").toString())));
                    }
                }
                rd.Require = rs.getShort("require");
                rd.RequireLevel = rs.getShort("require_level");
                rd.AuraId = rs.getShort("aura_id");
                RadarService.gI().RADAR_TEMPLATE.add(rd);
            }
            Logger.success("Load radar template thành công (" + RadarService.gI().RADAR_TEMPLATE.size() + ")\n");

//            ps = con.prepareStatement("SELECT * FROM consignment_shop");
//            rs = ps.executeQuery();
//            JSONArray jsonArray = null;
//            JSONValue jsonValue = new JSONValue();
//            ConsignmentShop consignmentShop = ConsignmentShop.getInstance();
//            while (rs.next()) {
//                short itemID = rs.getShort("item_id");
//                int quantity = rs.getInt("quantity");
//                ConsignmentItem item = ItemService.gI().createNewConsignmentItem(itemID, quantity);
//                item.setConsignID(rs.getInt("id"));
//                item.setConsignorID(rs.getLong("consignor_id"));
//                item.setTab(rs.getByte("tab"));
//                item.setPriceGold(rs.getInt("gold"));
//                item.setPriceGem(rs.getInt("gem"));
//                item.setUpTop(rs.getBoolean("up_top"));
//                item.setSold(rs.getBoolean("sold"));
//                item.createTime = rs.getLong("time_consign");
//                jsonArray = (JSONArray) jsonValue.parse(rs.getString("item_options"));
//
//                for (int j = 0; j < jsonArray.size(); j++) {
//                    JSONArray opt = (JSONArray) jsonValue.parse(String.valueOf(jsonArray.get(j)));
//                    item.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
//                            Integer.parseInt(String.valueOf(opt.get(1)))));
//                }
//                int daysExpired = ConsignmentShop.getInstance().getDaysExpried(item.createTime);
//                if (daysExpired > 3 && daysExpired < 6) {
//                    consignmentShop.addExpiredItem(item);
//                }
//                if (daysExpired < 6) {
//                    consignmentShop.addItem(item);
//                }
////                System.err.println("sss " + item.getConsignID());
//            }
//            Logger.success("Load kí gửi new thành công (" + RadarService.gI().RADAR_TEMPLATE.size() + ")\n");
//khai comment
            topSM = realTop(queryTopSM, con);
            Logger.success("TOP POWER (" + topSM.size() + ")  --> Success !!\n");
//            topSM2 = realTop(queryTopSM2, con);
//            Logger.success("TOP POWER 2 (" + topSM2.size() + ")  --> Success !!\n");
//            topNV = realTop(queryTopNV, con);
//            Logger.success("TOP QUEST (" + topNV.size() + ")  --> Success !!\n");
//            topSK = realTop(queryTopSK, con);
//            Logger.success("TOP EVENT (" + topSK.size() + ")  --> Success !!\n");
//
//            Logger.success("TOP RUBY (" + topSK.size() + ")  --> Success !!\n");
            topPVP = realTop(queryTopPVP, con);
            topSD = realTop(queryTopSD, con);

//            topSKHE = realTop(queryTopSKHE2, con);
//            Logger.success("TOP SKHE (" + topSKHE.size() + ")  --> Success !!\n");
            topTV = realTop(queryTopTV, con);
            Logger.success("TOP Thỏi (" + topTV.size() + ")  --> Success !!\n");

//            topDauThan = realTop(queryTopDauThan, con);
//            Logger.success("TOP đậu thần (" + topDauThan.size() + ")  --> Success !!\n");
//            
//            Logger.success("TOP NHS (" + topNHS.size() + ")  --> Success !!\n");
//            topNHS = realTop(queryTopNHS, con);
//            Logger.success("TOP POINT (BYTE 2) (" + topSD.size() + ")  --> Success !!\n");
            Manager.timeRealTop = System.currentTimeMillis();
//            ConsignManager.getInstance().load();
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Thông báo lỗi tải database");
            System.exit(0);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        Logger.success("Tổng thời gian tải dữ liệu: " + (System.currentTimeMillis() - st) + "(ms)\n");
    }

    public static List<TOP> realTop(String query, Connection con) {
        List<TOP> tops = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                switch (query) {
                    case queryTopSM:
                        top.setInfo1(rs.getLong("sm") + "");
                        top.setInfo2(rs.getLong("sm") + "");
                        break;
//                    case queryTopSM2:
//                        top.setInfo1(rs.getLong("sm2") + "");
//                        top.setInfo2(rs.getLong("sm2") + "");
//                        break;
                    case queryTopNV:
                        top.setInfo1(rs.getByte("nv") + "");
                        top.setInfo2(rs.getByte("nv") + "");
                        break;
//                    case queryTopSK:
//                        top.setInfo1(rs.getInt("event") + " điểm");
//                        top.setInfo2(rs.getInt("event") + " điểm");
//                        break;
                    case queryTopPVP:
                        top.setInfo1(rs.getInt("HONGNGOC") + " Hồng Ngọc");
                        top.setInfo2(rs.getInt("HONGNGOC") + " Hồng Ngọc");
                        break;
                    case queryTopSD:
                        top.setInfo1(rs.getLong("sd") + " Sức Đánh");
                        top.setInfo2(rs.getLong("sd") + " Sức Đánh");
                        break;
//                    case queryTopSKHE2:
//                        top.setInfo1(rs.getLong("topsk16") + "");
//                        top.setInfo2(rs.getLong("topsk16") + "");
//                        break;
//                    case queryTopNHS:
//                        top.setInfo1(rs.getLong("nhs") + "Điểm");
//                        top.setInfo2(rs.getLong("nhs") + "Điểm");
//                        break;
                    case queryTopTV:
                        top.setInfo1("Tổng :" + rs.getLong("thoi_vang") + " tv");
                        String info2 = "Rương : " + rs.getLong("tv_ruong") + "tv\n";
                        info2 += "Hành trang :" + rs.getLong("tv_hanhtrang");
                        top.setInfo2(info2);
                        break;
//                    case queryTopDauThan:
//                        top.setInfo1("Tổng :" + rs.getLong("thoi_vang") + " tv");
//                        String info22 = "Rương : " + rs.getLong("tv_ruong") + "tv\n";
//                        info22 += "Hành trang :" + rs.getLong("tv_hanhtrang");
//                        top.setInfo2(info22);
//                        break;
                }
                tops.add(top);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return tops;
    }

    public void loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("data/girlkun/girlkun.properties"));
        Object value = null;
        //###Config sv
        if ((value = properties.get("server.girlkun.port")) != null) {
            ServerManager.PORT = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.name")) != null) {
            ServerManager.NAME = String.valueOf(value);
        }
        if ((value = properties.get("server.girlkun.sv")) != null) {
            SERVER = Byte.parseByte(String.valueOf(value));
        }
        String linkServer = "";
        for (int i = 1; i <= 10; i++) {
            value = properties.get("server.girlkun.sv" + i);
            if (value != null) {
                linkServer += String.valueOf(value) + ":0,";
            }
        }
        DataGame.LINK_IP_PORT = linkServer.substring(0, linkServer.length() - 1);
        if ((value = properties.get("server.girlkun.waitlogin")) != null) {
            SECOND_WAIT_LOGIN = Byte.parseByte(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.maxperip")) != null) {
            MAX_PER_IP = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.maxplayer")) != null) {
            MAX_PLAYER = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.expserver")) != null) {
            RATE_EXP_SERVER = Byte.parseByte(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.event")) != null) {
            EVENT_SEVER = Byte.parseByte(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.local")) != null) {
            LOCAL = String.valueOf(value).toLowerCase().equals("true");
        }
    }

    /**
     * @param tileTypeFocus tile type: top, bot, left, right...
     * @return [tileMapId][tileType]
     */
    private int[][] readTileIndexTileType(int tileTypeFocus) {
        int[][] tileIndexTileType = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_set_info"));
            int numTileMap = dis.readByte();
            tileIndexTileType = new int[numTileMap][];
            for (int i = 0; i < numTileMap; i++) {
                int numTileOfMap = dis.readByte();
                for (int j = 0; j < numTileOfMap; j++) {
                    int tileType = dis.readInt();
                    int numIndex = dis.readByte();
                    if (tileType == tileTypeFocus) {
                        tileIndexTileType[i] = new int[numIndex];
                    }
                    for (int k = 0; k < numIndex; k++) {
                        int typeIndex = dis.readByte();
                        if (tileType == tileTypeFocus) {
                            tileIndexTileType[i][k] = typeIndex;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(MapService.class, e);
        }
        return tileIndexTileType;
    }

    /**
     * @param mapId mapId
     * @return tile map for paint
     */
    private int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            String filePath = "data/girlkun/map/tile_map_data/" + mapId;
            File file = new File(filePath);
            if (file.exists()) {
                DataInputStream dis = new DataInputStream(new FileInputStream(file));
                int w = dis.readByte();
                int h = dis.readByte();
                tileMap = new int[h][w];
                for (int i = 0; i < tileMap.length; i++) {
                    for (int j = 0; j < tileMap[i].length; j++) {
                        tileMap[i][j] = dis.readByte();
                    }
                }
                dis.close();
            } else {
                System.out.println("Không tìm thấy id: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileMap;
    }

    //service*******************************************************************
    public static Clan getClanById(int id) throws Exception {
        for (Clan clan : CLANS) {
            if (clan.id == id) {
                return clan;
            }
        }
        throw new Exception("Thông báo không tìm thấy id bang hội: " + id);
    }

    public static void addClan(Clan clan) {
        CLANS.add(clan);
    }

    public static int getNumClan() {
        return CLANS.size();

    }

    public static MobTemplate getMobTemplateByTemp(int mobTempId) {
        for (MobTemplate mobTemp : MOB_TEMPLATES) {
            if (mobTemp.id == mobTempId) {
                return mobTemp;
            }
        }
        return null;
    }

    public static byte getNFrameImageByName(String name) {
        Object n = IMAGES_BY_NAME.get(name);
        if (n != null) {
            return Byte.parseByte(String.valueOf(n));
        } else {
            return 0;
        }
    }

}
