package nro.jdbc.daos;

//import nro.database.GirlkunDB;
import com.girlkun.database.GirlkunDB;
import nro.models.item.Item;
import nro.models.item.ItemTime;
import nro.models.player.Friend;
import nro.models.player.Fusion;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.MapService;
import nro.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import nro.utils.Util;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class PlayerDAO {

    public static boolean createNewPlayer(int userId, String name, byte gender, int hair) {
        try {
            JSONArray dataArray = new JSONArray();

            dataArray.add(200000000); //vàng
            dataArray.add(100000); //ngọc xanh
            dataArray.add(100000); //hồng ngọc
            dataArray.add(0); //point
            dataArray.add(0); //event

            String inventory = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(39 + gender); //map
            dataArray.add(100); //x
            dataArray.add(384); //y
            String location = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //giới hạn sức mạnh
            dataArray.add(1500000); //sức mạnh
            dataArray.add(1500000); //tiềm năng
            dataArray.add(1000); //thể lực
            dataArray.add(1000); //thể lực đầy
            dataArray.add(gender == 0 ? 200 : 100); //hp gốc
            dataArray.add(gender == 1 ? 200 : 100); //ki gốc
            dataArray.add(gender == 2 ? 30 : 25); //sức đánh gốc
            dataArray.add(0); //giáp gốc
            dataArray.add(0); //chí mạng gốc
            dataArray.add(0); //năng động
            dataArray.add(gender == 0 ? 200 : 100); //hp hiện tại
            dataArray.add(gender == 1 ? 200 : 100); //ki hiện tại
            String point = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(1); //level
            dataArray.add(5); //curent pea
            dataArray.add(0); //is upgrade
            dataArray.add(new Date().getTime()); //last time harvest
            dataArray.add(new Date().getTime()); //last time upgrade
            String magicTree = dataArray.toJSONString();
            dataArray.clear();
            /**
             *
             * [
             * {
             * "temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * {
             * "temp_id":"1","option":[[5,7],[7,3]],"create_time":"49238749283748957""},
             * { "temp_id":"-1","option":[],"create_time":"0""}, ... ]
             */

            int idAo = gender == 0 ? 0 : gender == 1 ? 1 : 2;
            int idQuan = gender == 0 ? 6 : gender == 1 ? 7 : 8;
            int def = gender == 2 ? 3 : 2;
            int hp = gender == 0 ? 30 : 20;

            JSONArray item = new JSONArray();
            JSONArray options = new JSONArray();
            JSONArray opt = new JSONArray();
            JSONArray opt2 = new JSONArray();
            for (int i = 0; i < 11; i++) {
                if (i == 0) {   //áo
                    opt.add(47); //id option
                    opt.add(def); //param option
                    item.add(idAo); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else if (i == 1) {   //quần
                    opt.add(6); //id option
                    opt.add(hp); //param option
                    item.add(idQuan); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBody = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 20; i++) {   // item tạo player 
                if (i == 0) {   //thỏi vàng
                    opt.add(30); //id option cấm giao dịch
                    opt.add(1); //param option
                    item.add(457); //id item
                    item.add(100); //số lượng
                    options.add(opt.toJSONString());
                    //options.add(opt2.toJSONString());
                    opt.clear();
                    // opt2.clear();
                } else if (i == 1) {   //thỏi vàng
                    opt.add(30); //id option cấm giao dịch
                    opt.add(1); //param option
                    item.add(194); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else if (i == 2) {   //thỏi vàng
                    opt.add(30); //id option cấm giao dịch
                    opt.add(1); //param option
                    item.add(1667); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBag = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 20; i++) {
                if (i == 0) {   //rada
                    opt.add(14); //id option
                    opt.add(1); //param option
                    item.add(12); //id item
                    item.add(1); //số lượng
                    options.add(opt.toJSONString());
                    opt.clear();
                } else {
                    item.add(-1); //id item
                    item.add(0); //số lượng
                }
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBox = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemMailBox = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemsBoxLuckyRound = dataArray.toJSONString();
            dataArray.clear();

            for (int i = 0; i < 110; i++) {
                item.add(-1); //id item
                item.add(0); //số lượng
                item.add(options.toJSONString()); //full option item
                item.add(System.currentTimeMillis()); //thời gian item được tạo
                dataArray.add(item.toJSONString());
                options.clear();
                item.clear();
            }
            String itemRuongPhu = dataArray.toJSONString();
            dataArray.clear();

            String friends = dataArray.toJSONString();
            dataArray.clear();
            String enemies = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //id nội tại
            dataArray.add(0); //chỉ số 1
            dataArray.add(0); //chỉ số 2
            dataArray.add(0); //số lần mở
            String intrinsic = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //bổ huyết
            dataArray.add(0); //bổ khí
            dataArray.add(0); //giáp xên
            dataArray.add(0); //cuồng nộ
            dataArray.add(0); //ẩn danh
            dataArray.add(0); //mở giới hạn sức mạnh
            dataArray.add(0); //máy dò
            dataArray.add(0); //máy dò2
            dataArray.add(0); //máy dò3
            dataArray.add(0); //bíngo
            dataArray.add(0); //thức ăn cold
            dataArray.add(0); //icon thức ăn cold
            dataArray.add(0); //tdlt
            String itemTime = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //BHSC
            dataArray.add(0); //BKSC
            dataArray.add(0); //GXSC
            dataArray.add(0); //CNSC
            dataArray.add(0); //ADSC
            dataArray.add(0); //BTET
            dataArray.add(0); //BCHUNG
            dataArray.add(0); //BANHNHEN
            dataArray.add(0); //BANHSAU
            dataArray.add(0); //KEOMAT
            dataArray.add(0); //SUPBI
            dataArray.add(0); //BANHNGOT
            dataArray.add(0); //KEODEO
            dataArray.add(0); //KEMOCQUE
            dataArray.add(0); //KEOTRONGOI
            String data_item_time_sieu_cap = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //vooc
            dataArray.add(0); //saobien
            dataArray.add(0); //concua
            dataArray.add(0); //maydosk
            String data_item_time_su_kien = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0); //x2
            dataArray.add(0); //x5
            dataArray.add(0); //x7
            dataArray.add(0); //x10
            dataArray.add(0); //x15
            String itemTimeTNSM = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(1); //id nhiệm vụ
            dataArray.add(0); //index nhiệm vụ con
            dataArray.add(0); //số lượng đã làm
            String task = dataArray.toJSONString();
            dataArray.clear();

            String mabuEgg = dataArray.toJSONString();
            dataArray.clear();

//            String billEgg = dataArray.toJSONString();
////            dataArray.clear();
//
//            String mabugayEgg = dataArray.toJSONString();
//            dataArray.clear();
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ
            dataArray.add(System.currentTimeMillis()); //bùa mạnh mẽ
            dataArray.add(System.currentTimeMillis()); //bùa da trâu
            dataArray.add(System.currentTimeMillis()); //bùa oai hùng
            dataArray.add(System.currentTimeMillis()); //bùa bất tử
            dataArray.add(System.currentTimeMillis()); //bùa dẻo dai
            dataArray.add(System.currentTimeMillis()); //bùa thu hút
            dataArray.add(System.currentTimeMillis()); //bùa đệ tử
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x3
            dataArray.add(System.currentTimeMillis()); //bùa trí tuệ x4
            String charms = dataArray.toJSONString();
            dataArray.clear();

            int[] skillsArr = gender == 0 ? new int[]{
                0, 1, 6, 9, 10, 20, 22, 24, 27, 19}
                    : gender == 1 ? new int[]{
                        2, 3, 7, 11, 12, 17, 18, 26, 27, 19}
                    : new int[]{
                        4, 5, 8, 13, 14, 21, 23, 25, 27, 19};
            //[{

//"temp_id":"4","point":0,"last_time_use":0},]
            JSONArray skill = new JSONArray();
            for (int i = 0; i < skillsArr.length; i++) {
                skill.add(skillsArr[i]); //id skill
                if (i == 0) {
                    skill.add(1); //level skill
                } else {
                    skill.add(0); //level skill
                }
                skill.add(0); //thời gian sử dụng trước đó
                skill.add(0);
                dataArray.add(skill.toString());
                skill.clear();
            }
            String skills = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(gender == 0 ? 0 : gender == 1 ? 2 : 4);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            dataArray.add(-1);
            String skillsShortcut = dataArray.toJSONString();
            dataArray.clear();

            String petData = dataArray.toJSONString();
            dataArray.clear();

            JSONArray blackBall = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                blackBall.add(0);
                blackBall.add(0);
                blackBall.add(0);
                dataArray.add(blackBall.toJSONString());
                blackBall.clear();
            }
            String dataBlackBall = dataArray.toString();
            dataArray.clear();

            JSONArray blackGiaidau = new JSONArray();
            for (int i = 1; i <= 7; i++) {
                blackGiaidau.add(0);
                blackGiaidau.add(0);
                blackGiaidau.add(0);
                dataArray.add(blackGiaidau.toJSONString());
                blackGiaidau.clear();
            }
            String dataGiaidau = dataArray.toString();
            dataArray.clear();

            dataArray.add(-1); //id side task
            dataArray.add(0); //thời gian nhận
            dataArray.add(0); //số lượng đã làm
            dataArray.add(0); //số lượng cần làm
            dataArray.add(20); //số nhiệm vụ còn lại có thể nhận
            dataArray.add(0); //mức độ nhiệm vụ
            String dataSideTask = dataArray.toJSONString();
            dataArray.clear();

            String minipet = dataArray.toJSONString();
            dataArray.clear();

            String data_card = dataArray.toJSONString();
            dataArray.clear();
            //  String bill_data = dataArray.toJSONString();

            String Achievement = dataArray.toJSONString();
            dataArray.clear();

            String Achievement_diem = dataArray.toJSONString();
            dataArray.clear();

            String Achievement_bomong = dataArray.toJSONString();
            dataArray.clear();

            long lastTimeDropTail = System.currentTimeMillis();
//            dataArray.add(lastTimeDropTail); //bùa trí tuệ x4
//            dataArray.clear();

            dataArray.add(0);
            dataArray.add(0);
            String info_phoban = dataArray.toJSONString();
            dataArray.clear();

            dataArray.add(0);
            dataArray.add(0);
            String info_phoban_cdrd = dataArray.toJSONString();
            dataArray.clear();

//            dataArray.add(0); //event
//            dataArray.add(0); //
//            dataArray.add(0); //
//            dataArray.add(0); //
//            String diem_event = dataArray.toJSONString();
//            dataArray.clear();
//            int event_point = 0;            
//            int event_point_boss = 0;
//            int event_point_nhs = 0;
//            int event_point_quai = 0;
//            int hp_point_fusion = 0;
//            int mp_point_fusion = 0;
//            int dame_point_fusion = 0;
            GirlkunDB.executeUpdate("insert into player"
                    + "(account_id, name, head, gender, have_tennis_space_ship, clan_id_sv" + Manager.SERVER + ", "
                    + "data_inventory, data_location, data_point, data_magic_tree, items_body, "
                    + "items_bag, items_box, items_box_lucky_round, friends, enemies, data_intrinsic, data_item_time,"
                    + "data_task, data_mabu_egg, data_charm, skills, skills_shortcut, pet,"
                    + "data_black_ball, data_giai_dau, data_side_task,minipet, data_card,info_phoban,info_phoban_cdrd,"
                    + " data_item_time_sieu_cap,data_item_time_su_kien,data_item_time_tnsm,item_mails_box, items_ruong_phu,LastTimeDropTail,Achievement,mocsk20_10,Achievement_BoMong,dot_pha,isUseTrucCoDan,capTuTien"
                    //                    + ",event_point, event_point_boss, event_point_nhs, event_point_quai, hp_point_fusion, mp_point_fusion, dame_point_fusion"
                    + ") "
                    + "values ()", userId, name, hair, gender, 0, -1, inventory, location, point, magicTree,
                    itemsBody, itemsBag, itemsBox, itemsBoxLuckyRound, friends, enemies, intrinsic,
                    itemTime, task, mabuEgg, charms, skills, skillsShortcut, petData, dataBlackBall,
                    dataGiaidau, dataSideTask, minipet, data_card, info_phoban, info_phoban_cdrd,
                    data_item_time_sieu_cap, data_item_time_su_kien, itemTimeTNSM, itemMailBox, itemRuongPhu, lastTimeDropTail,
                    Achievement, Achievement_diem, Achievement_bomong,0,0,0
            //                   , event_point, event_point_boss, event_point_nhs, event_point_quai, hp_point_fusion, mp_point_fusion, dame_point_fusion
            );
            Logger.success("Tạo player mới thành công!\n");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Thông báo lỗi lưu cư dân: ");

        }
        return false;
    }

    public static boolean activedUser(Player player) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set active = ? where id = ?");
            ps.setInt(1, player.getSession().actived ? 1 : 0);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean subvnd(Player player, int num) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set vnd = (vnd - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd -= num;
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm subvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean addvnd(Player player, int num) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set vnd = (vnd + ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd += num;
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm addvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean subtotalvnd(Player player, int num) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set tongnap = (tongnap - ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().totalvnd -= num;
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm subtotalvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean addtotalvnd(Player player, int num) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set tongnap = (tongnap + ?) where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().totalvnd += num;
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm addtotalvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean Addvnd(String username, int num) {
        try {
            username = username.trim();
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set vnd = (vnd + ?) where username = ? ");
            ps.setInt(1, num);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm subvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean VND(Player player, int vnd) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set vnd = (vnd - ?) where id = ?");

            ps.setInt(1, vnd);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            player.getSession().vnd -= vnd;
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm subvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean Addtotalvnd(String username, int num) {
        try {
            username = username.trim();
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set tongnap = (tongnap + ?) where username = ?");
            ps.setInt(1, num);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm subvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean Addtotalvnd2(String username, int num) {
        try {
            username = username.trim();
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set tongnap2 = (tongnap2 + ?) where username = ?");
            ps.setInt(1, num);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm subvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean Subtotalvnd(String username, int num) {
        try {
            username = username.trim();
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set tongnap = (tongnap - ?) where username = ?");
            ps.setInt(1, num);
            ps.setString(2, username);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(" Lỗi của Kiệt ở hàm subvnd, gặp lỗi này kêu kiệt fix ");
            return false;
        }

    }

    public static boolean updatenhanngocxanh(Player player) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set nhanngocxanh = ? where id = ?");
            ps.setInt(1, player.getSession().nhanngocxanh ? 1 : 0);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();

            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    public static boolean updatenhanngochong(Player player) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set nhanngochong = ? where id = ?");
            ps.setInt(1, player.getSession().nhanngochong ? 1 : 0);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();

            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    public static boolean updatenhanvang(Player player) {
        try {
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update account set nhanvang = ? where id = ?");
            ps.setInt(1, player.getSession().nhanvang ? 1 : 0);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();

            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

    }

    public static void updatePlayer(Player player) {
        if (player.iDMark.isLoadedAllDataPlayer()) {
            long st = System.currentTimeMillis();
            try {
                JSONArray dataArray = new JSONArray();

                //data kim lượng
                dataArray.add(player.inventory.gold > Inventory.LIMIT_GOLD
                        ? Inventory.LIMIT_GOLD : player.inventory.gold);
                dataArray.add(player.inventory.gem);
                dataArray.add(player.inventory.ruby);
                dataArray.add(player.inventory.coupon);
                dataArray.add(player.inventory.event);
                String inventory = dataArray.toJSONString();
                dataArray.clear();

                int mapId = -1;
                mapId = player.mapIdBeforeLogout;
                int x = player.location.x;
                int y = player.location.y;
                long hp = Util.TamkjllGH(player.nPoint.hp);
                long mp = Util.TamkjllGH(player.nPoint.mp);
                if (player.isDie()) {
                    mapId = player.gender + 21;
                    x = 300;
                    y = 336;
                    hp = 1;
                    mp = 1;
                } else {
                    if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isNguHanhSon(mapId) || MapService.gI().isMapBlackBallWar(mapId) || MapService.gI().isMapGiaidauvutru(mapId)
                            || MapService.gI().isMapKhiGas(mapId) || MapService.gI().isMapConDuongRanDoc(mapId) || MapService.gI().isMapBanDoKhoBau(mapId) || MapService.gI().isMapMaBu(mapId)) {
                        mapId = player.gender + 21;
                        x = 300;
                        y = 336;
                    } else if (MapService.gI().isMapOffline(mapId)) {
                        mapId = player.zone.map.mapId;
                    }
                }

                //data vị trí
                dataArray.add(mapId);
                dataArray.add(x);
                dataArray.add(y);
                String location = dataArray.toJSONString();
                dataArray.clear();

                //data chỉ số
                dataArray.add(player.nPoint.limitPower);
                dataArray.add(player.nPoint.power);
                dataArray.add(player.nPoint.tiemNang);
                dataArray.add(player.nPoint.stamina);
                dataArray.add(player.nPoint.maxStamina);
                dataArray.add(player.nPoint.hpg);
                dataArray.add(player.nPoint.mpg);
                dataArray.add(player.nPoint.dameg);
                dataArray.add(player.nPoint.defg);
                dataArray.add(player.nPoint.critg);
                dataArray.add(0);
                dataArray.add(hp);
                dataArray.add(mp);
                dataArray.add(player.numKillSieuHang);
                dataArray.add(player.rankSieuHang);
                String point = dataArray.toJSONString();
                dataArray.clear();

                //data đậu thần
                dataArray.add(player.magicTree.level);
                dataArray.add(player.magicTree.currPeas);
                dataArray.add(player.magicTree.isUpgrade ? 1 : 0);
                dataArray.add(player.magicTree.lastTimeHarvest);
                dataArray.add(player.magicTree.lastTimeUpgrade);
                String magicTree = dataArray.toJSONString();
                dataArray.clear();

                //data body
                JSONArray dataItem = new JSONArray();
                for (Item item : player.inventory.itemsBody) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBody = dataArray.toJSONString();
                dataArray.clear();

                //data bag
                for (Item item : player.inventory.itemsBag) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBag = dataArray.toJSONString();
                dataArray.clear();

                //data card
                //data box
                for (Item item : player.inventory.itemsBox) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBox = dataArray.toJSONString();
                dataArray.clear();

                //data box crack ball
                for (Item item : player.inventory.itemsBoxCrackBall) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemsBoxLuckyRound = dataArray.toJSONString();
                dataArray.clear();

                //data box mail
                for (Item item : player.inventory.itemsMailBox) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemMailBox = dataArray.toJSONString();
                dataArray.clear();

                //data box mail
                for (Item item : player.inventory.itemsRuongPhu) {
                    JSONArray opt = new JSONArray();
                    if (item.isNotNullItem()) {
                        dataItem.add(item.template.id);
                        dataItem.add(item.quantity);
                        JSONArray options = new JSONArray();
                        for (Item.ItemOption io : item.itemOptions) {
                            opt.add(io.optionTemplate.id);
                            opt.add(io.param);
                            options.add(opt.toJSONString());
                            opt.clear();
                        }
                        dataItem.add(options.toJSONString());
                    } else {
                        dataItem.add(-1);
                        dataItem.add(0);
                        dataItem.add(opt.toJSONString());
                    }
                    dataItem.add(item.createTime);
                    dataArray.add(dataItem.toJSONString());
                    dataItem.clear();
                }
                String itemRuongPhu = dataArray.toJSONString();
                dataArray.clear();

                //data bạn bè
                JSONArray dataFE = new JSONArray();
                for (Friend f : player.friends) {
                    dataFE.add(f.id);
                    dataFE.add(f.name);
                    dataFE.add(f.head);
                    dataFE.add(f.body);
                    dataFE.add(f.leg);
                    dataFE.add(f.bag);
                    dataFE.add(f.power);
                    dataArray.add(dataFE.toJSONString());
                    dataFE.clear();
                }
                String friend = dataArray.toJSONString();
                dataArray.clear();

                //data kẻ thù
                for (Friend e : player.enemies) {
                    dataFE.add(e.id);
                    dataFE.add(e.name);
                    dataFE.add(e.head);
                    dataFE.add(e.body);
                    dataFE.add(e.leg);
                    dataFE.add(e.bag);
                    dataFE.add(e.power);
                    dataArray.add(dataFE.toJSONString());
                    dataFE.clear();
                }
                String enemy = dataArray.toJSONString();
                dataArray.clear();

                //data nội tại
                dataArray.add(player.playerIntrinsic.intrinsic.id);
                dataArray.add(player.playerIntrinsic.intrinsic.param1);
                dataArray.add(player.playerIntrinsic.intrinsic.param2);
                dataArray.add(player.playerIntrinsic.countOpen);
                String intrinsic = dataArray.toJSONString();
                dataArray.clear();

                //data item time
                dataArray.add((player.itemTime.isUseBoHuyet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) : 0));
                dataArray.add((player.itemTime.isUseBoKhi ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) : 0));
                dataArray.add((player.itemTime.isUseGiapXen ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) : 0));
                dataArray.add((player.itemTime.isUseCuongNo ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) : 0));
                dataArray.add((player.itemTime.isUseAnDanh ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) : 0));
                dataArray.add((player.itemTime.isOpenPower ? (ItemTime.TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) : 0));

                dataArray.add((player.itemTime.isBiNgo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBiNgo)) : 0));
                dataArray.add((player.itemTime.isUseMayDo ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) : 0));
                dataArray.add((player.itemTime.isUseMayDo2 ? (ItemTime.TIME_MAY_DO2 - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2)) : 0));
                dataArray.add((player.itemTime.isUseMayDo3 ? (ItemTime.TIME_MAY_DO3 - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo3)) : 0));
                dataArray.add(player.itemTime.iconMeal);
                dataArray.add((player.itemTime.isEatMeal ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) : 0));
                dataArray.add((player.itemTime.isUseTDLT ? ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000) : 0));
                String itemTime = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ
                dataArray.add(player.playerTask.taskMain.id);
                dataArray.add(player.playerTask.taskMain.index);
                dataArray.add(player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).count);
                String task = dataArray.toJSONString();
                dataArray.clear();

                //data nhiệm vụ hàng ngày
                dataArray.add(player.playerTask.sideTask.template != null ? player.playerTask.sideTask.template.id : -1);
                dataArray.add(player.playerTask.sideTask.receivedTime);
                dataArray.add(player.playerTask.sideTask.count);
                dataArray.add(player.playerTask.sideTask.maxCount);
                dataArray.add(player.playerTask.sideTask.leftTask);
                dataArray.add(player.playerTask.sideTask.level);
                String sideTask = dataArray.toJSONString();
                dataArray.clear();

                //data trứng bư
                if (player.mabuEgg != null) {
                    dataArray.add(player.mabuEgg.lastTimeCreate);
                    dataArray.add(player.mabuEgg.timeDone);
                }
                String mabuEgg = dataArray.toJSONString();
                dataArray.clear();

//                data trứng bill
//                if (player.billEgg != null) {  
//                    dataArray.add(player.billEgg.lastTimeCreate);
//                    dataArray.add(player.billEgg.timeDone);
//                }
//                String billEgg = dataArray.toJSONString();
//                dataArray.clear();
//                if (player.BuugayEgg != null) {  
//                    dataArray.add(player.BuugayEgg.lastTimeCreate);
//                    dataArray.add(player.BuugayEgg.timeDone);
//                }
//                String mabugayEgg = dataArray.toJSONString();
//                dataArray.clear();
                //data bùa
                dataArray.add(player.charms.tdTriTue);
                dataArray.add(player.charms.tdManhMe);
                dataArray.add(player.charms.tdDaTrau);
                dataArray.add(player.charms.tdOaiHung);
                dataArray.add(player.charms.tdBatTu);
                dataArray.add(player.charms.tdDeoDai);
                dataArray.add(player.charms.tdThuHut);
                dataArray.add(player.charms.tdDeTu);
                dataArray.add(player.charms.tdTriTue3);
                dataArray.add(player.charms.tdTriTue4);
                String charm = dataArray.toJSONString();
                dataArray.clear();

                //data skill
                JSONArray dataSkill = new JSONArray();
                for (Skill skill : player.playerSkill.skills) {
                    dataSkill.add(skill.template.id);
                    dataSkill.add(skill.point);
                    dataSkill.add(skill.lastTimeUseThisSkill);
                    dataSkill.add(skill.currLevel);
                    dataArray.add(dataSkill.toJSONString());
                    dataSkill.clear();
                }
                String skills = dataArray.toJSONString();
                dataArray.clear();
//                dataArray.clear();

                //data skill shortcut
                for (int skillId : player.playerSkill.skillShortCut) {
                    dataArray.add(skillId);
                }
                String skillShortcut = dataArray.toJSONString();
                dataArray.clear();

                String pet = dataArray.toJSONString();
                String petInfo = dataArray.toJSONString();
                String petPoint = dataArray.toJSONString();
                String petBody = dataArray.toJSONString();
                String petSkill = dataArray.toJSONString();

                //data pet
                if (player.pet != null) {
                    dataArray.add(player.pet.typePet);
                    dataArray.add(player.pet.gender);
                    dataArray.add(player.pet.name);
                    dataArray.add(player.fusion.typeFusion);
                    int timeLeftFusion = (int) (Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion));
                    dataArray.add(timeLeftFusion < 0 ? 0 : timeLeftFusion);
                    dataArray.add(player.pet.status);
                    petInfo = dataArray.toJSONString();
                    dataArray.clear();

                    dataArray.add(player.pet.nPoint.limitPower);
                    dataArray.add(player.pet.nPoint.power);
                    dataArray.add(player.pet.nPoint.tiemNang);
                    dataArray.add(player.pet.nPoint.stamina);
                    dataArray.add(player.pet.nPoint.maxStamina);
                    dataArray.add(player.pet.nPoint.hpg);
                    dataArray.add(player.pet.nPoint.mpg);
                    dataArray.add(player.pet.nPoint.dameg);
                    dataArray.add(player.pet.nPoint.defg);
                    dataArray.add(player.pet.nPoint.critg);
                    dataArray.add(player.pet.nPoint.hp);
                    dataArray.add(player.pet.nPoint.mp);
                    petPoint = dataArray.toJSONString();
                    dataArray.clear();

                    JSONArray items = new JSONArray();
                    JSONArray options = new JSONArray();
                    JSONArray opt = new JSONArray();
                    for (Item item : player.pet.inventory.itemsBody) {
                        if (item.isNotNullItem()) {
                            dataItem.add(item.template.id);
                            dataItem.add(item.quantity);
                            for (Item.ItemOption io : item.itemOptions) {
                                opt.add(io.optionTemplate.id);
                                opt.add(io.param);
                                options.add(opt.toJSONString());
                                opt.clear();
                            }
                            dataItem.add(options.toJSONString());
                        } else {
                            dataItem.add(-1);
                            dataItem.add(0);
                            dataItem.add(options.toJSONString());
                        }

                        dataItem.add(item.createTime);

                        items.add(dataItem.toJSONString());
                        dataItem.clear();
                        options.clear();
                    }
                    petBody = items.toJSONString();

                    JSONArray petSkills = new JSONArray();
                    for (Skill s : player.pet.playerSkill.skills) {
                        JSONArray pskill = new JSONArray();
                        if (s.skillId != -1) {
                            pskill.add(s.template.id);
                            pskill.add(s.point);
                        } else {
                            pskill.add(-1);
                            pskill.add(0);
                        }
                        petSkills.add(pskill.toJSONString());
                    }
                    petSkill = petSkills.toJSONString();

                    dataArray.add(petInfo);
                    dataArray.add(petPoint);
                    dataArray.add(petBody);
                    dataArray.add(petSkill);
                    pet = dataArray.toJSONString();
                }
                dataArray.clear();

                //data thưởng ngọc rồng đen
                for (int i = 0; i < player.rewardBlackBall.timeOutOfDateReward.length; i++) {
                    JSONArray dataBlackBall = new JSONArray();
                    dataBlackBall.add(player.rewardBlackBall.timeOutOfDateReward[i]);
                    dataBlackBall.add(player.rewardBlackBall.lastTimeGetReward[i]);
                    dataBlackBall.add(player.rewardBlackBall.quantilyBlackBall[i]);
                    dataArray.add(dataBlackBall.toJSONString());
                    dataBlackBall.clear();
                }
                String dataBlackBall = dataArray.toJSONString();
                dataArray.clear();

                //data thưởng giai dau
                for (int i = 0; i < player.rewardGiaidau.timeOutOfDateReward.length; i++) {
                    JSONArray dataGiaidau = new JSONArray();
                    dataGiaidau.add(player.rewardGiaidau.timeOutOfDateReward[i]);
                    dataGiaidau.add(player.rewardGiaidau.lastTimeGetReward[i]);
                    dataGiaidau.add(player.rewardGiaidau.quantilyGiaidau[i]);
                    dataArray.add(dataGiaidau.toJSONString());
                    dataGiaidau.clear();
                }
                String dataGiaidau = dataArray.toJSONString();
                dataArray.clear();

//                dataArray.add(player.titleitem == true ? 1 : 0);
//                dataArray.add(player.titlett == true ? 1 : 0);
//                String title = dataArray.toJSONString();
//                dataArray.clear();
//                dataArray.add(player.isTitleUse == true ? 1 : 0);
//                dataArray.add(player.lastTimeTitle1);
//                String dhtime = dataArray.toJSONString();
//                dataArray.clear();
//                dataArray.add(player.isTitleUse2 == true ? 1 : 0);
//                dataArray.add(player.lastTimeTitle2);
//                String dhtime2 = dataArray.toJSONString();
//                dataArray.clear();
//                dataArray.add(player.isTitleUse3 == true ? 1 : 0);
//                dataArray.add(player.lastTimeTitle3);
//                String dhtime3 = dataArray.toJSONString();
//                dataArray.clear();
                String minipet = dataArray.toJSONString();
                String minipetInfo = dataArray.toJSONString();
                String minipetPoint = dataArray.toJSONString();
                if (player.minipet != null) {
                    dataArray.add(player.minipet.gender);
                    dataArray.add(player.minipet.name);
                    dataArray.add(player.minipet.status);
                    minipetInfo = dataArray.toJSONString();
                    dataArray.clear();

                    dataArray.add(player.minipet.nPoint.hpMax);
                    dataArray.add(player.minipet.nPoint.hpg);
                    dataArray.add(player.minipet.nPoint.hp);
                    minipetPoint = dataArray.toJSONString();
                    dataArray.clear();

                    dataArray.add(minipetInfo);
                    dataArray.add(minipetPoint);
                    minipet = dataArray.toJSONString();
                }
                dataArray.clear();

                //data item time siêu cấp
                dataArray.add((player.itemTime.isUseBoHuyetSC ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyetSC)) : 0));
                dataArray.add((player.itemTime.isUseBoKhiSC ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhiSC)) : 0));
                dataArray.add((player.itemTime.isUseGiapXenSC ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXenSC)) : 0));
                dataArray.add((player.itemTime.isUseCuongNoSC ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNoSC)) : 0));
                dataArray.add((player.itemTime.isUseAnDanhSC ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanhSC)) : 0));
                dataArray.add((player.itemTime.isUseBanhTet ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTet)) : 0));
                dataArray.add((player.itemTime.isUseBanhChung ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhChung)) : 0));
                dataArray.add((player.itemTime.isUseBanhNhen ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBanhNhen)) : 0));
                dataArray.add((player.itemTime.isUseBanhSau ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeBanhSau)) : 0));
                dataArray.add((player.itemTime.isUseKeoMotMat ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeKeoMotMat)) : 0));
                dataArray.add((player.itemTime.isUseSupBi ? (ItemTime.TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeSupBi)) : 0));
                dataArray.add((player.itemTime.isUseBanhNgot ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeBanhNgot)) : 0));
                dataArray.add((player.itemTime.isUseKemOcQue ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeKemOcQue)) : 0));
                dataArray.add((player.itemTime.isUseKeoDeo ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeKeoDeo)) : 0));
                dataArray.add((player.itemTime.isUseKeoTrongGoi ? (ItemTime.TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeKeoTrongGoi)) : 0));
                String itemTimeSC = dataArray.toJSONString();
                dataArray.clear();

                //data item time sự kiện
                dataArray.add((player.itemTime.isUseVooc ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseVooc)) : 0));
                dataArray.add((player.itemTime.isUseConCua ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseConCua)) : 0));
                dataArray.add((player.itemTime.isUseSaoBien ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseSaoBien)) : 0));
                dataArray.add((player.itemTime.isUseMayDoSK ? (ItemTime.TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDoSK)) : 0));
                String itemTimeSK = dataArray.toJSONString();
                dataArray.clear();

                //data item time sự kiện
                dataArray.add((player.itemTime.isLoNuocThanhx2 ? (ItemTime.TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx2)) : 0));
                dataArray.add((player.itemTime.isLoNuocThanhx5 ? (ItemTime.TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx5)) : 0));
                dataArray.add((player.itemTime.isLoNuocThanhx7 ? (ItemTime.TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx7)) : 0));
                dataArray.add((player.itemTime.isLoNuocThanhx10 ? (ItemTime.TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx10)) : 0));
                dataArray.add((player.itemTime.isLoNuocThanhx15 ? (ItemTime.TIME_MAX_TNSM - (System.currentTimeMillis() - player.itemTime.lastTimeLoNuocThanhx15)) : 0));

                String itemTimeTNSM = dataArray.toJSONString();
                dataArray.clear();

                dataArray.add(player.event.getTimeCookTetCake());
                dataArray.add(player.event.getTimeCookChungCake());
                dataArray.add(player.event.isCookingTetCake() ? 1 : 0);
                dataArray.add(player.event.isCookingChungCake() ? 1 : 0);
                dataArray.add(player.event.isReceivedLuckyMoney() ? 1 : 0);
                String skTet = dataArray.toJSONString();
                dataArray.clear();

                dataArray.add(player.bdkb_lastTimeJoin);
                dataArray.add(player.bdkb_countPerDay);
                String info_phoban = dataArray.toJSONString();
                dataArray.clear();

                dataArray.add(player.cdrd_lastTimeJoin);
                dataArray.add(player.cdrd_countPerDay);
                String info_phoban_cdrd = dataArray.toJSONString();
                dataArray.clear();

//                dataArray.add(player.event.getEventPoint());
//                String diemsk = dataArray.toJSONString();
//                dataArray.clear();
//
//                dataArray.add(player.event.getEventPointBoss());
//                String diemskbhm = dataArray.toJSONString();
//                dataArray.clear();
//
//                dataArray.add(player.event.getEventPointNHS());
//                String diemsknhs = dataArray.toJSONString();
//                dataArray.clear();
//
//                dataArray.add(player.event.getEventPointQuai());
//                String diemskquai = dataArray.toJSONString();
//                dataArray.clear();
//
//                dataArray.add(player.event.getEventPointQuyLao());
//                String diemskquylao = dataArray.toJSONString();
//                dataArray.clear();
//
//                dataArray.add(player.pointfusion.getHpFusion());
//                String hpht = dataArray.toJSONString();
//                dataArray.clear();
//
//                dataArray.add(player.pointfusion.getMpFusion());
//                String mpht = dataArray.toJSONString();
//                dataArray.clear();
//
//                dataArray.add(player.pointfusion.getDameFusion());
//                String dameht = dataArray.toJSONString();
//                dataArray.clear();
                String query = "UPDATE player SET rank_sieu_hang = ?,  minipet = ?, data_item_time_sieu_cap = ?, data_item_time_su_kien = ?, data_item_time_tnsm = ?, head = ?, have_tennis_space_ship = ?,"
                        + "clan_id_sv" + Manager.SERVER + " = ?, data_inventory = ?, data_location = ?, data_point = ?, data_magic_tree = ?,"
                        + "items_body = ?, items_bag = ?, items_box = ?, items_box_lucky_round = ?, item_mails_box =?, items_ruong_phu = ?, friends = ?,"
                        + "enemies = ?, data_intrinsic = ?, data_item_time = ?, data_task = ?, data_mabu_egg = ?, pet = ?,"
                        + "data_black_ball = ?, data_giai_dau = ?, data_side_task = ?, data_charm = ?, skills = ?,"
                        + "skills_shortcut = ?, pointPvp = ?, data_card = ?, info_phoban = ?, info_phoban_cdrd = ?,"
                        + "event_point = ?,"
                        + "event_point_boss = ?, event_point_nhs = ?, event_point_quai = ?, diem_quy_lao = ?,diem_moc = ?,"
                        + "hp_point_fusion = ?, mp_point_fusion = ?, dame_point_fusion = ?, dot_pha=?, isUseTrucCoDan = ?, capTuTien = ?"
                        + " WHERE id = ?";

                GirlkunDB.executeUpdate(query,
                        player.rankSieuHang,
                        minipet,
                        itemTimeSC,
                        itemTimeSK,
                        itemTimeTNSM,
                        player.head,
                        player.haveTennisSpaceShip,
                        (player.clan != null ? player.clan.id : -1),
                        inventory,
                        location,
                        point,
                        magicTree,
                        itemsBody,
                        itemsBag,
                        itemsBox,
                        itemsBoxLuckyRound,
                        itemMailBox,
                        itemRuongPhu,
                        friend,
                        enemy,
                        intrinsic,
                        itemTime,
                        task,
                        mabuEgg,
                        pet,
                        dataBlackBall,
                        dataGiaidau,
                        sideTask,
                        charm,
                        skills,
                        skillShortcut,
                        player.pointPvp,
                        JSONValue.toJSONString(player.Cards),
                        info_phoban,
                        info_phoban_cdrd,
                        player.event.getEventPoint(),
                        player.event.getEventPointBoss(),
                        player.event.getEventPointNHS(),
                        player.event.getEventPointQuai(),
                        player.event.getEventPointQuyLao(),
                        player.event.getEventPointMoc(),
                        player.pointfusion.getHpFusion(),
                        player.pointfusion.getMpFusion(),
                        player.pointfusion.getDameFusion(),
                        player.dotpha,
                        player.isUseTrucCoDan,
                        player.capTuTien,  
                        player.id);
//                Logger.log(Logger.RED, "Cư dân: " + player.name + " đã lưu dữ liệu thành công! " + (System.currentTimeMillis() - st) + "\n");
            } catch (Exception e) {
                e.printStackTrace();
                Logger.logException(PlayerDAO.class, e, "Thông báo lỗi lưu cư dân: " + player.name);
            }
        }
    }

    public static boolean subGoldBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set thoi_vang = (thoi_vang - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().goldBar -= num;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
            return false;
        } finally {
        }
        return false;
    }

    public static boolean subvndBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set vnd = (vnd - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().vnd -= num;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Lỗi update vnd " + player.name);
            return false;
        } finally {
        }
        return false;
    }

    public static boolean subcoinBar(Player player, int num) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set coin = (coin - ?), active = ? where id = ?");
            ps.setInt(1, num);
            ps.setInt(2, player.getSession().actived ? 1 : 0);
            ps.setInt(3, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
            player.getSession().coinBar -= num;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Lỗi update Coin " + player.name);
            return false;
        } finally {
        }
        if (num > 1000) {
            insertHistoryGold(player, num);
        }
        return true;
    }

    public static boolean setIs_gift_box(Player player) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set is_gift_box = 0 where id = ?");
            ps.setInt(1, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Lỗi update new_reg " + player.name);
            return false;
        }
        return true;
    }

    public static void addHistoryReceiveGoldBar(Player player, int goldBefore, int goldAfter,
            int goldBagBefore, int goldBagAfter, int goldBoxBefore, int goldBoxAfter) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("insert into history_receive_goldbar(player_id,player_name,gold_before_receive,"
                    + "gold_after_receive,gold_bag_before,gold_bag_after,gold_box_before,gold_box_after) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, (int) player.id);
            ps.setString(2, player.name);
            ps.setInt(3, goldBefore);
            ps.setInt(4, goldAfter);
            ps.setInt(5, goldBagBefore);
            ps.setInt(6, goldBagAfter);
            ps.setInt(7, goldBoxBefore);
            ps.setInt(8, goldBoxAfter);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Lỗi update thỏi vàng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateItemReward(Player player) {
        String dataItemReward = "";
        for (Item item : player.getSession().itemsReward) {
            if (item.isNotNullItem()) {
                dataItemReward += "{  " + item.template.id + ":" + item.quantity;
                if (!item.itemOptions.isEmpty()) {
                    dataItemReward += "|";
                    for (Item.ItemOption io : item.itemOptions) {
                        dataItemReward += "[" + io.optionTemplate.id + ":" + io.param + "],";
                    }
                    dataItemReward = dataItemReward.substring(0, dataItemReward.length() - 1) + "};";
                }
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("update account set reward = ? where id = ?");
            ps.setString(1, dataItemReward);
            ps.setInt(2, player.getSession().userId);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Lỗi update phần thưởng " + player.name);
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean insertHistoryGold(Player player, int quantily) {
        PreparedStatement ps = null;
        try (Connection con = GirlkunDB.getConnection();) {
            ps = con.prepareStatement("insert into history_gold(name,gold) values (?,?)");
            ps.setString(1, player.name);
            ps.setInt(2, quantily);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerDAO.class, e, "Lỗi insert history_gold " + player.name);
            return false;
        }
        return true;
    }

    public static boolean checkLogout(Connection con, Player player) {
        if (player.getSession() == null) {
            // Handle the case when the session is null
            return false;
        }

        long lastTimeLogout = 0;
        long lastTimeLogin = 0;
        try (PreparedStatement ps = con.prepareStatement("select * from account where id = ? limit 1")) {
            ps.setInt(1, player.getSession().userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                    lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return lastTimeLogout > lastTimeLogin;
    }

    public static void LogNapTIen(String uid, String menhgia, String seri, String code, String tranid) {
        String UPDATE_PASS = "INSERT INTO naptien(uid,sotien,seri,code,loaithe,time,noidung,tinhtrang,tranid,magioithieu) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            Connection conn = GirlkunDB.getConnection();
            PreparedStatement ps = null;
            //UPDATE NRSD,
            ps = conn.prepareStatement(UPDATE_PASS);
            conn.setAutoCommit(false);
            //NGOC RONG SAO DEN
            ps.setString(1, uid);
            ps.setString(2, menhgia);
            ps.setString(3, seri);
            ps.setString(4, code);

            ps.setString(5, "VIETTEL");
            ps.setString(6, "123123123123");
            ps.setString(7, "dang nap the");
            ps.setString(8, "0");
            ps.setString(9, tranid);
            ps.setString(10, "0");
            if (ps.executeUpdate() == 1) {
            }

            conn.commit();
            //UPDATE NRSD
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
