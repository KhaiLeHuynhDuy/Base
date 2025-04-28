package nro.services.func;

import com.girlkun.database.GirlkunDB;

import nro.models.player.Player;
import nro.server.Manager;
import nro.utils.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import nro.consts.ConstDataEvent;
import nro.consts.ConstDataEventSM;
import nro.consts.cn;
import nro.services.ItemService;

public class TopService implements Runnable {

    private static Player highestScorer = null;
    private static TopService i;

    public static TopService gI() {
        if (i == null) {
            i = new TopService();
        }
        return i;
    }

    public static String getTopNap() {
        StringBuffer sb = new StringBuffer("");

//        String SELECT_TOP_POWER = "SELECT username, tongnap FROM account ORDER BY tongnap DESC LIMIT 10;";
        String SELECT_TOP_POWER = "SELECT player.id as plid, player.`name`, account.tongnap3 FROM account, player WHERE account.id = player.account_id AND account.tongnap3 >= 20000 ORDER BY account.tongnap3 DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("tongnap3")).append(" Đã Nạp\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String getTopSM() {
        StringBuffer sb = new StringBuffer("");

//        String SELECT_TOP_POWER = "SELECT username, tongnap FROM account ORDER BY tongnap DESC LIMIT 10;";
        String SELECT_TOP_POWER
                = //                = "SELECT name, \n"
                //                + "       CAST(split_str(data_point, ',', 2) AS UNSIGNED) AS sm \n"
                //                + "FROM player \n"
                //                + "WHERE (account_id > 34414 AND account_id < 40001)\n"
                //                + "   OR (account_id > 46718 AND account_id < 50000)\n"
                //                + "   OR account_id > 53447 \n"
                //                + "ORDER BY sm DESC \n"
                //                + "LIMIT 20;" ;
                "SELECT name, CAST( split_str(data_point,',',2) AS UNSIGNED) AS sm FROM player WHERE create_time > '2024-" + ConstDataEventSM.MONTH_OPEN + "-" + ConstDataEventSM.DATE_OPEN + " "+ConstDataEventSM.HOUR_OPEN+":"+ConstDataEventSM.MIN_OPEN+":00' ORDER BY CAST( split_str(data_point,',',2) AS UNSIGNED) DESC LIMIT 20;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("sm")).append(" Sức Mạnh\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String getTopVND() {
        StringBuffer sb = new StringBuffer("");

//        String SELECT_TOP_POWER = "SELECT username, vnd FROM account ORDER BY vnd DESC LIMIT 10;";
        String SELECT_TOP_POWER = "SELECT player.id as plid, player.`name`, account.id as account, account.vnd\n"
                + "                        FROM account, player \n"
                + "                        WHERE account.id = player.account_id\n"
                + "                         ORDER BY account.vnd DESC \n"
                + "                         LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("vnd")).append(" COIN\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String getTopNguHanhSon() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_POWER = "SELECT name, event_point_nhs FROM player ORDER BY event_point_nhs DESC LIMIT 10;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection conn = GirlkunDB.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while (rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("event_point_nhs")).append(" điểm\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

//    public static String getTopSKHE() {
//        StringBuffer sb = new StringBuffer("");
//
////        String SELECT_TOP_POWER = "SELECT id, username, topsk16 FROM account ORDER BY topsk16 DESC LIMIT 10;";
//        String SELECT_TOP_POWER = "SELECT account.id as accid, player.`name`, account.topsk16\n"
//                + "                        FROM account, player \n"
//                + "                        WHERE account.id = player.account_id \n"
//                + "                         ORDER BY account.topsk16 DESC \n"
//                + "                         LIMIT 10;";
//        PreparedStatement ps;
//        ResultSet rs;
//        try {
//            Connection conn = GirlkunDB.getConnection();
//            ps = conn.prepareStatement(SELECT_TOP_POWER);
//            conn.setAutoCommit(false);
//
//            rs = ps.executeQuery();
//            byte i = 1;
//            while (rs.next()) {
//                int id = rs.getInt("accid");
//                String username = rs.getString("name");
//                int topsk16 = rs.getInt("topsk16");
//                sb.append(i).append(".").append(id).append("-").append(username).append(": ").append(topsk16).append(" điểm\b");
//                i++;
//            }
//
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return sb.toString();
//    }
//    public static String getTopSKHE() {
//        StringBuffer sb = new StringBuffer("");
//
////        String SELECT_TOP_POWER = "SELECT id, username, topsk16 FROM account ORDER BY topsk16 DESC LIMIT 10;";
////        String SELECT_TOP_POWER = "SELECT id, name, diem_quy_lao FROM player ORDER BY diem_quy_lao DESC LIMIT 10;";
//        String SELECT_TOP_POWER = "select \n"
//                + "  account.id as accountId, \n"
//                + "  player.name, \n"
//                + "	CAST(\n"
//                + "    REPLACE(\n"
//                + "      SUBSTRING_INDEX(\n"
//                + "        SUBSTRING_INDEX(\n"
//                + "          CONCAT(\n"
//                + "            '[" + ConstDataEvent.ID_ITEM_6 + ",', \n"
//                + "            SUBSTRING_INDEX(\n"
//                + "              SUBSTRING_INDEX(player.items_bag, '[" + ConstDataEvent.ID_ITEM_6 + ",', -1), \n"
//                + "              ']', \n"
//                + "              1\n"
//                + "            )\n"
//                + "          ), \n"
//                + "          ',', \n"
//                + "          2\n"
//                + "        ), \n"
//                + "        ']', \n"
//                + "        1\n"
//                + "      ), \n"
//                + "      '[" + ConstDataEvent.ID_ITEM_6 + ",', \n"
//                + "      ''\n"
//                + "    ) as unsigned\n"
//                + "  ) as tv_hanhtrang, \n"
//                + "	CAST(\n"
//                + "    REPLACE(\n"
//                + "      SUBSTRING_INDEX(\n"
//                + "        SUBSTRING_INDEX(\n"
//                + "          CONCAT(\n"
//                + "            '[" + ConstDataEvent.ID_ITEM_6 + ",', \n"
//                + "            SUBSTRING_INDEX(\n"
//                + "              SUBSTRING_INDEX(player.items_box, '[" + ConstDataEvent.ID_ITEM_6 + ",', -1), \n"
//                + "              ']', \n"
//                + "              1\n"
//                + "            )\n"
//                + "          ), \n"
//                + "          ',', \n"
//                + "          2\n"
//                + "        ), \n"
//                + "        ']', \n"
//                + "        1\n"
//                + "      ), \n"
//                + "      '[" + ConstDataEvent.ID_ITEM_6 + ",', \n"
//                + "      ''\n"
//                + "    ) as unsigned\n"
//                + "  ) as tv_ruong,\n"
//                + "  CAST(\n"
//                + "    REPLACE(\n"
//                + "      SUBSTRING_INDEX(\n"
//                + "        SUBSTRING_INDEX(\n"
//                + "          CONCAT(\n"
//                + "            '[" + ConstDataEvent.ID_ITEM_6 + ",', \n"
//                + "            SUBSTRING_INDEX(\n"
//                + "              SUBSTRING_INDEX(player.items_bag, '[" + ConstDataEvent.ID_ITEM_6 + ",', -1), \n"
//                + "              ']', \n"
//                + "              1\n"
//                + "            )\n"
//                + "          ), \n"
//                + "          ',', \n"
//                + "          2\n"
//                + "        ), \n"
//                + "        ']', \n"
//                + "        1\n"
//                + "      ), \n"
//                + "      '[" + ConstDataEvent.ID_ITEM_6 + ",', \n"
//                + "      ''\n"
//                + "    ) as unsigned\n"
//                + "  ) + CAST(\n"
//                + "    REPLACE(\n"
//                + "      SUBSTRING_INDEX(\n"
//                + "        SUBSTRING_INDEX(\n"
//                + "          CONCAT(\n"
//                + "            '[" + ConstDataEvent.ID_ITEM_6 + ",', \n"
//                + "            SUBSTRING_INDEX(\n"
//                + "              SUBSTRING_INDEX(player.items_box, '[" + ConstDataEvent.ID_ITEM_6 + ",', -1), \n"
//                + "              ']', \n"
//                + "              1\n"
//                + "            )\n"
//                + "          ), \n"
//                + "          ',', \n"
//                + "          2\n"
//                + "        ), \n"
//                + "        ']', \n"
//                + "        1\n"
//                + "      ), \n"
//                + "      '[" + ConstDataEvent.ID_ITEM_6 + ",', \n"
//                + "      ''\n"
//                + "    ) as unsigned\n"
//                + "  ) AS thoi_vang \n"
//                + "from \n"
//                + "  player \n"
//                + "  inner join account on account.id = player.account_id \n"
//                + "where \n"
//                + "  (\n"
//                + "    player.items_box like '%\"[" + ConstDataEvent.ID_ITEM_6 + ",%' \n"
//                + "    or player.items_bag like '%\"[" + ConstDataEvent.ID_ITEM_6 + ",%'\n"
//                + " )\n"
//                + "order by \n"
//                + "  thoi_vang DESC \n"
//                + "limit \n"
//                + "  10;";
//        PreparedStatement ps;
//        ResultSet rs;
//        try {
//            Connection conn = GirlkunDB.getConnection();
//            ps = conn.prepareStatement(SELECT_TOP_POWER);
//            conn.setAutoCommit(false);
//
//            rs = ps.executeQuery();
//            byte i = 1;
//            while (rs.next()) {
//                int id = rs.getInt("accountId");
//                String username = rs.getString("name");
////                int topsk16 = rs.getInt("diem_quy_lao");
//                int topsk16 = rs.getInt("thoi_vang");
//
//                sb.append(i).append(".").append(id).append("-").append(username).append(": ").append(topsk16).append(" số lượng\b");
//
//                i++;
//            }
//
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return sb.toString();
//    }

//    public static String getTopSK20_10() {
//        StringBuffer sb = new StringBuffer("");
//
//        String SELECT_TOP_POWER = "SELECT account.id as accid, player.`name`, account.sk20_10\n"
//                + "                        FROM account, player \n"
//                + "                        WHERE account.id = player.account_id \n"
//                + "                         ORDER BY account.sk20_10 DESC \n"
//                + "                         LIMIT 10;";
//        PreparedStatement ps;
//        ResultSet rs;
//        try {
//            Connection conn = GirlkunDB.getConnection();
//            ps = conn.prepareStatement(SELECT_TOP_POWER);
//            conn.setAutoCommit(false);
//
//            rs = ps.executeQuery();
//            byte i = 1;
//            while (rs.next()) {
//                int id = rs.getInt("accid");
//                String username = rs.getString("name");
//                int sk20_10 = rs.getInt("sk20_10");
//                sb.append(i).append(".").append(id).append("-").append(username).append(": ").append(sk20_10).append(" điểm\b");
//                i++;
//            }
//
//            conn.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return sb.toString();
//    }

    @Override
    public void run() {
//        while (true) {
//            try {
//                if (Manager.timeRealTop + (30 * 60 * 1000) < System.currentTimeMillis()) {
//                    Manager.timeRealTop = System.currentTimeMillis();
//                    try ( Connection con = GirlkunDB.getConnection()) {
//                        //Manager.topSieuHang = Manager.realTopSieuHang(con);
//                        //Manager.topNV = Manager.realTop(Manager.queryTopNV, con);
//                        Manager.topSM = Manager.realTop(Manager.queryTopSM, con);
//                        //Manager.topSM2 = Manager.realTop(Manager.queryTopSM2, con);
//                       // Manager.topSK = Manager.realTop(Manager.queryTopSK, con);
//                        //Manager.topPVP = Manager.realTop(Manager.queryTopPVP, con);
////                        Manager.topNHS = Manager.realTop(Manager.queryTopNHS, con);
//                       // Manager.topSKHE = Manager.realTop(Manager.queryTopSKHE2, con);
//                        Manager.topTV = Manager.realTop(Manager.queryTopTV, con);
//                        // Manager.topDauThan = Manager.realTop(Manager.queryTopDauThan, con);
////                        Manager.topSKNEW = Manager.realTop(Manager.queryTopSKNEW, con);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                        Logger.error("Lỗi đọc top");
//                    }
//                }
//                Thread.sleep(1000);
//            } 
//            catch (InterruptedException e) {
//                e.printStackTrace();
//                Thread.currentThread().interrupt();
//                break; // Thoát khỏi vòng lặp nếu bị gián đoạn
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

}
