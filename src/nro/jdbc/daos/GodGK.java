package nro.jdbc.daos;

import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import nro.models.card.Card;
import nro.models.card.OptionCard;
//import nro.database.GirlkunDB;
//import nro.result.GirlkunResultSet;
import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.item.Item;
import nro.models.item.ItemTime;
//import nro.models.npc.specialnpc.BillEgg;
import nro.models.npc.specialnpc.MabuEgg;
//import nro.models.npc.specialnpc.MabuGayEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.player.Enemy;
import nro.models.player.Friend;
import nro.models.player.Fusion;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.models.task.TaskMain;
import nro.server.Client;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.server.io.MySession;
import nro.server.model.AntiLogin;
import nro.services.ClanService;
import nro.services.IntrinsicService;
import nro.services.ItemService;
import nro.services.MapService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.List;

import nro.utils.Util;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class GodGK {

    public static List<OptionCard> loadOptionCard(JSONArray json) {
        List<OptionCard> ops = new ArrayList<>();
        try {
            for (int i = 0; i < json.size(); i++) {
                JSONObject ob = (JSONObject) json.get(i);
                if (ob != null) {
                    ops.add(new OptionCard(Integer.parseInt(ob.get("id").toString()),
                            Integer.parseInt(ob.get("param").toString()), Byte.parseByte(ob.get("active").toString())));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("        loi 25");
        }
        return ops;
    }

    public static Boolean baotri = false;

    public static synchronized Player login(MySession session, AntiLogin al) {
        Player player = null;
        GirlkunResultSet rs = null;

        try {

            rs = GirlkunDB.executeQuery("SELECT * FROM account WHERE username = ? AND password = ?", session.uu, session.pp);
            if (rs.first()) {
                session.userId = rs.getInt("account.id");
                session.isAdmin = rs.getBoolean("is_admin");
                session.lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                session.actived = rs.getBoolean("active");
                session.isAdmin1 = rs.getBoolean("is_admin1");

                session.nhanngocxanh = rs.getBoolean("nhanngocxanh");
                session.nhanngochong = rs.getBoolean("nhanngochong");
                session.nhanvang = rs.getBoolean("nhanvang");
                session.goldBar = rs.getInt("account.thoi_vang");
                session.bdPlayer = rs.getDouble("account.bd_player");
                session.vnd = rs.getInt("account.vnd");
                session.totalvnd = rs.getInt("account.tongnap");
                session.totalvnd2 = rs.getInt("account.tongnap2");
                session.totalvnd3 = rs.getInt("account.tongnap3");
                session.sk20_10 = rs.getInt("account.sk20_10");
                session.SKHE = rs.getInt("account.topsk16");
//                session.NHS = rs.getInt("account.NguHanhSonPoint");
                long lastTimeLogin = rs.getTimestamp("last_time_login").getTime();
                int secondsPass1 = (int) ((System.currentTimeMillis() - lastTimeLogin) / 1000);
                long lastTimeLogout = rs.getTimestamp("last_time_logout").getTime();
                int secondsPass = (int) ((System.currentTimeMillis() - lastTimeLogout) / 1000);
//                if (!session.isAdmin) {
////                    Service.gI().sendThongBaoOK(session, "ADMIN đang sửa lỗi Sever 1 đề nghị ae chọn Sever 2 hoặc\n đi xem sẽ cho đỡ buồn!");
//                    Service.gI().sendThongBaoOK(session, "Xóa dữ liệu hoặc chọn lại sever đang chơi\n(Nếu đã xóa dữ liệu thì chỉ cần chọn sever)");
//                } else
                if (rs.getBoolean("ban")) {
                    Service.gI().sendThongBaoOK(session, "Tài khoản đã bị khóa!");
                } else if (baotri && session.isAdmin) {
                    Service.gI().sendThongBaoOK(session, "Máy chủ đang bảo trì, vui lòng quay lại sau!");
                } else if (secondsPass1 < Manager.SECOND_WAIT_LOGIN) {
                    if (secondsPass < secondsPass1) {
                        Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + "s");
                        return null;
                    }
                    Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass1) + "s");
                    return null;
                } else if (rs.getTimestamp("last_time_login").getTime() > session.lastTimeLogout) {
                    Player plInGame = Client.gI().getPlayerByUser(session.userId);
                    if (plInGame != null) {
                        Client.gI().kickSession(plInGame.getSession());
                        Service.gI().sendThongBaoOK(session, "Ai đó đang đăng nhập tài khoản?");
                    } else {
                        String logoutQuery = "UPDATE account SET last_time_logout = ? WHERE id = ?;";
                        GirlkunDB.executeUpdate(logoutQuery, new Timestamp(System.currentTimeMillis()), session.userId);
                        Service.gI().sendThongBaoOK(session, "Vui lòng đăng nhập lại");
                    }
                } else {
                    if (secondsPass < Manager.SECOND_WAIT_LOGIN) {
                        Service.gI().sendThongBaoOK(session, "Vui lòng chờ " + (Manager.SECOND_WAIT_LOGIN - secondsPass) + "s");
                    } else {//set time logout trước rồi đọc data player
                        rs = GirlkunDB.executeQuery("select * from player where account_id = ? limit 1", session.userId);
                        if (!rs.first()) {
                            Service.gI().switchToCreateChar(session);
                            DataGame.sendDataItemBG(session);
                            DataGame.sendVersionGame(session);
                            DataGame.sendTileSetInfo(session);
                            //  Service.gI().sendMessage(session, -93, "1630679752231_-93_r");
                            DataGame.updateData(session);
                            //  DataGame.effData(session, int, int);

//                            //-28 -4 version data game
//                            DataGame.sendVersionGame(session);
//                            //-31 data item background
//                            DataGame.sendDataItemBG(session);
//                            Service.gI().switchToCreateChar(session);
                            //  DataGame.effData(session, int, int);
                        } else {
                            Player plInGame = Client.gI().getPlayerByUser(session.userId);
                            if (plInGame != null) {
                                Client.gI().kickSession(plInGame.getSession());
                            }
                            long plHp = 2000000000L;
                            long plMp = 2000000000L;
                            JSONValue jv = new JSONValue();
                            JSONArray dataArray = null;

                            player = new Player();

                            //base info
                            player.id = rs.getInt("id");
                            player.name = rs.getString("name");
//                            player.rankSieuHang = rs.getInt("rank_sieu_hang");
//                            if (player.rankSieuHang >= 2 && player.rankSieuHang < 10) {
//                                player.name = "[TOP SIEU HANG] " + rs.getString("name");
//                            } else if (player.rankSieuHang == 1) {
//                                player.name = "[TRUM SOLO] " + rs.getString("name");
//                            } else {
//                                player.name = rs.getString("name");
//                            }

                            player.head = rs.getShort("head");
                            player.gender = rs.getByte("gender");
                            player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                            player.violate = rs.getInt("violate");
                            player.pointPvp = rs.getInt("pointPvp");

                            player.event.setEventPoint(rs.getInt("event_point"));
                            player.event.setEventPointBoss(rs.getInt("event_point_boss"));
                            player.event.setEventPointNHS(rs.getInt("event_point_nhs"));
                            player.event.setEventPointQuai(rs.getInt("event_point_quai"));
                            player.event.setEventPointQuyLao(rs.getInt("diem_quy_lao"));
                            player.event.setEventPointMoc(rs.getInt("diem_moc"));

                            player.pointfusion.setHpFusion(rs.getInt("hp_point_fusion"));
                            player.pointfusion.setMpFusion(rs.getInt("mp_point_fusion"));
                            player.pointfusion.setDameFusion(rs.getInt("dame_point_fusion"));
                            player.dotpha = rs.getInt("dot_pha"); // Add this line
                            player.isUseTrucCoDan = rs.getBoolean("isUseTrucCoDan");
                            player.capTuTien = rs.getByte("capTuTien");


                            player.lastTimeDropTail = Long.parseLong(rs.getString("lastTimeDropTail"));
                            player.totalPlayerViolate = 0;
                            int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                            if (clanId != -1) {
                                Clan clan = ClanService.gI().getClanById(clanId);
                                for (ClanMember cm : clan.getMembers()) {
                                    if (cm.id == player.id) {
                                        clan.addMemberOnline(player);
                                        player.clan = clan;
                                        player.clanMember = cm;
                                        break;
                                    }
                                }
                            }

                            //data kim lượng
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                            //player.inventory.gold = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                            player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            if (dataArray.size() >= 4) {
                                player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            } else {
                                player.inventory.coupon = 0;
                            }
                            if (dataArray.size() >= 5) {
                                player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            } else {
                                player.inventory.event = 0;
                            }
                            dataArray.clear();

//                            //data danh hiệu
//                            dataArray = (JSONArray) JSONValue.parse(rs.getString("dhieu"));
//                            player.titleitem = Integer.parseInt(String.valueOf(dataArray.get(0))) == 1;
//                            player.titlett = Integer.parseInt(String.valueOf(dataArray.get(1))) == 1;
//                            dataArray.clear();
//                            dataArray = (JSONArray) JSONValue.parse(rs.getString("dhtime"));
//                            player.isTitleUse = Integer.parseInt(String.valueOf(dataArray.get(0))) == 1;
//                            player.lastTimeTitle1 = Long.parseLong(String.valueOf(dataArray.get(1)));
//                            dataArray.clear();
//                            dataArray = (JSONArray) JSONValue.parse(rs.getString("dhtime2"));
//                            player.isTitleUse2 = Integer.parseInt(String.valueOf(dataArray.get(0))) == 1;
//                            player.lastTimeTitle2 = Long.parseLong(String.valueOf(dataArray.get(1)));
//                            dataArray.clear();
//                            dataArray = (JSONArray) JSONValue.parse(rs.getString("dhtime3"));
//                            player.isTitleUse3 = Integer.parseInt(String.valueOf(dataArray.get(0))) == 1;
//                            player.lastTimeTitle3 = Long.parseLong(String.valueOf(dataArray.get(1)));
//                            dataArray.clear();
                            // data rada card
                            // data rada card
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_card"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                JSONObject obj = (JSONObject) dataArray.get(i);
                                player.Cards.add(new Card(Short.parseShort(obj.get("id").toString()), Byte.parseByte(obj.get("amount").toString()), Byte.parseByte(obj.get("max").toString()), Byte.parseByte(obj.get("level").toString()), loadOptionCard((JSONArray) JSONValue.parse(obj.get("option").toString())), Byte.parseByte(obj.get("used").toString())));
                            }
                            dataArray.clear();
                            //data tọa độ

                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_location"));
                            int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            player.location.lastTimeplayerMove = System.currentTimeMillis();
                            if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isNguHanhSon(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                                    || MapService.gI().isMapKhiGas(mapId) || MapService.gI().isMapConDuongRanDoc(mapId) || MapService.gI().isMapBanDoKhoBau(mapId)
                                    || MapService.gI().isMapMaBu(mapId) || MapService.gI().isMapGiaidauvutru(mapId)) {
                                mapId = player.gender + 21;
                                player.location.x = 300;
                                player.location.y = 336;
                            }
                            player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);

                            dataArray.clear();

                            //data chỉ số
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                            player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.nPoint.tiemNang = new BigDecimal(String.valueOf(dataArray.get(2))).longValue();
                            player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                            player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                            player.nPoint.hpg = new BigDecimal(String.valueOf(dataArray.get(5))).longValue();
                            player.nPoint.mpg = new BigDecimal(String.valueOf(dataArray.get(6))).longValue();
                            player.nPoint.dameg = new BigDecimal(String.valueOf(dataArray.get(7))).longValue();
                            player.nPoint.defg = new BigDecimal(String.valueOf(dataArray.get(8))).intValue();
                            player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                            dataArray.get(10); //** Năng động
                            plHp = new BigDecimal(String.valueOf(dataArray.get(11))).longValue();
                            plMp = new BigDecimal(String.valueOf(dataArray.get(12))).longValue();

                            dataArray.clear();

                            //data đậu thần
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_magic_tree"));
                            byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
                            long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
                            long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
                            dataArray.clear();

                            //data phần thưởng sao đen
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_black_ball"));
                            JSONArray dataBlackBall = null;
                            for (int i = 0; i < dataArray.size(); i++) {
                                dataBlackBall = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                                player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                                player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                                try {
                                    player.rewardBlackBall.quantilyBlackBall[i] = dataBlackBall.get(2) != null ? Integer.parseInt(String.valueOf(dataBlackBall.get(2))) : 0;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    player.rewardBlackBall.quantilyBlackBall[i] = player.rewardBlackBall.timeOutOfDateReward[i] != 0 ? 1 : 0;
                                }
                                dataBlackBall.clear();
                            }
                            dataArray.clear();
                            //data phần thưởng giai dau vu tru
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_giai_dau"));
                            JSONArray dataGiaidau = null;
                            for (int i = 0; i < dataArray.size(); i++) {
                                dataGiaidau = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                                player.rewardGiaidau.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataGiaidau.get(0)));
                                player.rewardGiaidau.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataGiaidau.get(1)));
                                try {
                                    player.rewardGiaidau.quantilyGiaidau[i] = dataGiaidau.get(2) != null ? Integer.parseInt(String.valueOf(dataGiaidau.get(2))) : 0;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    player.rewardGiaidau.quantilyGiaidau[i] = player.rewardGiaidau.timeOutOfDateReward[i] != 0 ? 1 : 0;
                                }
                                dataGiaidau.clear();
                            }
                            dataArray.clear();

                            //data body
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBody.add(item);
                            }
                            if (player.inventory.itemsBody.size() == 10) {
                                player.inventory.itemsBody.add(ItemService.gI().createItemNull());
                            }
                            dataArray.clear();

                            //data bag
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBag.add(item);
                            }
                            dataArray.clear();

                            //data box
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                    if (ItemService.gI().isOutOfDateTime(item)) {
                                        item = ItemService.gI().createItemNull();
                                    }
                                } else {
                                    item = ItemService.gI().createItemNull();
                                }
                                player.inventory.itemsBox.add(item);
                            }
                            dataArray.clear();

                            //data box lucky round
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box_lucky_round"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    player.inventory.itemsBoxCrackBall.add(item);
                                }
                            }
                            dataArray.clear();

                            //data box ruong phụ
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("items_ruong_phu"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    player.inventory.itemsRuongPhu.add(item);
                                }
                            }
                            dataArray.clear();

                            //data box hòm thư
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("item_mails_box"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                Item item = null;
                                JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                                short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                if (tempId != -1) {
                                    item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                    JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                    for (int j = 0; j < options.size(); j++) {
                                        JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                        item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                Integer.parseInt(String.valueOf(opt.get(1)))));
                                    }
                                    player.inventory.itemsMailBox.add(item);
                                }
                            }
                            dataArray.clear();

                            //data friends
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("friends"));
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray dataFE = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                                    Friend friend = new Friend();
                                    friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                                    friend.name = String.valueOf(dataFE.get(1));
                                    friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                                    friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                                    friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                                    friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                                    friend.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                                    player.friends.add(friend);
                                    dataFE.clear();
                                }
                                dataArray.clear();
                            }

                            //data enemies
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("enemies"));
                            if (dataArray != null) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray dataFE = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                                    Enemy enemy = new Enemy();
                                    enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                                    enemy.name = String.valueOf(dataFE.get(1));
                                    enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                                    enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                                    enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                                    enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                                    enemy.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                                    player.enemies.add(enemy);
                                    dataFE.clear();
                                }
                                dataArray.clear();
                            }

                            player.rankSieuHang = rs.getInt("rank_sieu_hang");
                            if (player.rankSieuHang == 999999) {
                                player.rankSieuHang = ServerManager.gI().getNumPlayer();
                            }
//                            dataArray.clear();
                            //data nội tại

                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_intrinsic"));
                            byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
                            player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                            player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
                            player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
                            player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
                            dataArray.clear();

                            //data item time
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_time"));
                            int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            int timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(5)));

                            int timeBiNgo = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            int timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            int timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(8)));
                            int timeMayDo3 = Integer.parseInt(String.valueOf(dataArray.get(9)));

                            int timeMeal = Integer.parseInt(String.valueOf(dataArray.get(10)));
                            int iconMeal = Integer.parseInt(String.valueOf(dataArray.get(11)));

                            int timeUseTDLT = 0;
                            if (dataArray.size() == 13) {
                                timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(12)));
                            }
//                            timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(12)));

                            player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                            player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                            player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                            player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                            player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);
                            player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);

                            player.itemTime.lastTimeBiNgo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeBiNgo);
                            player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);
                            player.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO2 - timeMayDo2);
                            player.itemTime.lastTimeUseMayDo3 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO3 - timeMayDo3);
                            player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                            player.itemTime.timeTDLT = timeUseTDLT * 60 * 1000;
                            player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();

                            player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                            player.itemTime.isUseBoKhi = timeBoKhi != 0;
                            player.itemTime.isUseGiapXen = timeGiapXen != 0;
                            player.itemTime.isUseCuongNo = timeCuongNo != 0;
                            player.itemTime.isUseAnDanh = timeAnDanh != 0;
                            player.itemTime.isOpenPower = timeOpenPower != 0;

                            player.itemTime.isBiNgo = timeBiNgo != 0;
                            player.itemTime.isUseMayDo = timeMayDo != 0;
                            player.itemTime.isUseMayDo2 = timeMayDo2 != 0;
                            player.itemTime.isUseMayDo3 = timeMayDo3 != 0;
                            player.itemTime.iconMeal = iconMeal;
                            player.itemTime.isEatMeal = timeMeal != 0;
                            player.itemTime.isUseTDLT = timeUseTDLT != 0;
                            dataArray.clear();

                            //dataa siu cap
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_time_sieu_cap"));
                            int timeBoHuyetSC = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timeBoKhiSC = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timeGiapXenSC = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timeCuongNoSC = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            int timeAnDanhSC = Integer.parseInt(String.valueOf(dataArray.get(4)));
                            int timeBanhTet = Integer.parseInt(String.valueOf(dataArray.get(5)));
                            int timeBanhChung = Integer.parseInt(String.valueOf(dataArray.get(6)));
                            int timeBanhNhen = Integer.parseInt(String.valueOf(dataArray.get(7)));
                            int timeBanhSau = Integer.parseInt(String.valueOf(dataArray.get(8)));
                            int timeKeoMotMat = Integer.parseInt(String.valueOf(dataArray.get(9)));
                            int timeSupBi = Integer.parseInt(String.valueOf(dataArray.get(10)));
                            int timeBanhNgot = Integer.parseInt(String.valueOf(dataArray.get(11)));
                            int timeKemOcQue = Integer.parseInt(String.valueOf(dataArray.get(12)));
                            int timeKeoDeo = Integer.parseInt(String.valueOf(dataArray.get(13)));
                            int timeKeoTrongGoi = Integer.parseInt(String.valueOf(dataArray.get(14)));

                            player.itemTime.lastTimeBoHuyetSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyetSC);
                            player.itemTime.lastTimeBoKhiSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhiSC);
                            player.itemTime.lastTimeGiapXenSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXenSC);
                            player.itemTime.lastTimeCuongNoSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNoSC);
                            player.itemTime.lastTimeAnDanhSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanhSC);
                            player.itemTime.lastTimeBanhTet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanhTet);
                            player.itemTime.lastTimeBanhChung = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanhChung);
                            player.itemTime.lastTimeBanhNhen = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeBoHuyetSC);
                            player.itemTime.lastTimeBanhSau = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeBoKhiSC);
                            player.itemTime.lastTimeKeoMotMat = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeGiapXenSC);
                            player.itemTime.lastTimeSupBi = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeCuongNoSC);
                            player.itemTime.lastTimeBanhNgot = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeAnDanhSC);
                            player.itemTime.lastTimeKeoDeo = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeBanhTet);
                            player.itemTime.lastTimeKemOcQue = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeBanhChung);
                            player.itemTime.lastTimeKeoTrongGoi = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeBanhChung);

                            player.itemTime.isUseBoHuyetSC = timeBoHuyetSC != 0;
                            player.itemTime.isUseBoKhiSC = timeBoKhiSC != 0;
                            player.itemTime.isUseGiapXenSC = timeGiapXenSC != 0;
                            player.itemTime.isUseCuongNoSC = timeCuongNoSC != 0;
                            player.itemTime.isUseAnDanhSC = timeAnDanhSC != 0;
                            player.itemTime.isUseBanhTet = timeBanhTet != 0;
                            player.itemTime.isUseBanhChung = timeBanhChung != 0;
                            player.itemTime.isUseBanhNhen = timeBanhNhen != 0;
                            player.itemTime.isUseBanhSau = timeBanhSau != 0;
                            player.itemTime.isUseKeoMotMat = timeKeoMotMat != 0;
                            player.itemTime.isUseSupBi = timeSupBi != 0;
                            player.itemTime.isUseBanhNgot = timeBanhNgot != 0;
                            player.itemTime.isUseKemOcQue = timeKemOcQue != 0;
                            player.itemTime.isUseKeoDeo = timeKeoDeo != 0;
                            player.itemTime.isUseKeoTrongGoi = timeKeoTrongGoi != 0;
                            dataArray.clear();

                            //data su kien
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_time_su_kien"));
                            int timevooc = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timesaobien = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timeconcua = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timemaydo = Integer.parseInt(String.valueOf(dataArray.get(3)));

                            player.itemTime.lastTimeUseVooc = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timevooc);
                            player.itemTime.lastTimeUseSaoBien = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timesaobien);
                            player.itemTime.lastTimeUseConCua = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeconcua);
                            player.itemTime.lastTimeUseMayDoSK = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timemaydo);

                            player.itemTime.isUseVooc = timevooc != 0;
                            player.itemTime.isUseSaoBien = timesaobien != 0;
                            player.itemTime.isUseConCua = timeconcua != 0;
                            player.itemTime.isUseMayDoSK = timemaydo != 0;
                            dataArray.clear();

                            //data item tnsm
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_time_tnsm"));
                            int timelonuocthanhx2 = Integer.parseInt(String.valueOf(dataArray.get(0)));
                            int timelonuocthanhx5 = Integer.parseInt(String.valueOf(dataArray.get(1)));
                            int timelonuocthanhx7 = Integer.parseInt(String.valueOf(dataArray.get(2)));
                            int timelonuocthanhx10 = Integer.parseInt(String.valueOf(dataArray.get(3)));
                            int timelonuocthanhx15 = Integer.parseInt(String.valueOf(dataArray.get(4)));

                            player.itemTime.lastTimeLoNuocThanhx2 = System.currentTimeMillis() - (ItemTime.TIME_MAX_TNSM - timelonuocthanhx2);
                            player.itemTime.lastTimeLoNuocThanhx5 = System.currentTimeMillis() - (ItemTime.TIME_MAX_TNSM - timelonuocthanhx5);
                            player.itemTime.lastTimeLoNuocThanhx7 = System.currentTimeMillis() - (ItemTime.TIME_MAX_TNSM - timelonuocthanhx7);
                            player.itemTime.lastTimeLoNuocThanhx10 = System.currentTimeMillis() - (ItemTime.TIME_MAX_TNSM - timelonuocthanhx10);
                            player.itemTime.lastTimeLoNuocThanhx15 = System.currentTimeMillis() - (ItemTime.TIME_MAX_TNSM - timelonuocthanhx15);

                            player.itemTime.isLoNuocThanhx2 = timelonuocthanhx2 != 0;
                            player.itemTime.isLoNuocThanhx5 = timelonuocthanhx5 != 0;
                            player.itemTime.isLoNuocThanhx7 = timelonuocthanhx7 != 0;
                            player.itemTime.isLoNuocThanhx10 = timelonuocthanhx10 != 0;
                            player.itemTime.isLoNuocThanhx15 = timelonuocthanhx15 != 0;
                            dataArray.clear();
//                         
                            //data nhiệm vụ
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_task"));
                            TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
                            taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
                            taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
                            player.playerTask.taskMain = taskMain;
                            dataArray.clear();

                            //data nhiệm vụ hàng ngày
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_side_task"));
                            String format = "dd-MM-yyyy";
                            long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
                            Date date = new Date(receivedTime);
                            if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                                player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                                player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                                player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                                player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                                player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                                player.playerTask.sideTask.receivedTime = receivedTime;
                            }
                            dataArray.clear();

                            //data trứng bư
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_mabu_egg"));
                            if (dataArray.size() != 0) {
                                player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                                        Long.parseLong(String.valueOf(dataArray.get(1))));
                            }
                            dataArray.clear();

                            // data trứng bill
//                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_bill_egg"));
//                            if (dataArray.size() != 0) {
//                                player.billEgg = new BillEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
//                                        Long.parseLong(String.valueOf(dataArray.get(1))));
//                            }
//                            dataArray.clear();
////
//                            // data trứng Babugay
//                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_mabugay_egg"));
//                            if (dataArray.size() != 0) {
//                                player.BuugayEgg = new MabuGayEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
//                                        Long.parseLong(String.valueOf(dataArray.get(1))));
//                            }
//                            dataArray.clear();
                            //data bùa
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("data_charm"));
                            player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
                            player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
                            player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
                            player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
                            player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
                            player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
                            player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
                            player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
                            player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
                            player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
                            dataArray.clear();

                            //data skill
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("skills"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                JSONArray dataSkill = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                                int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                                byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                                Skill skill = null;
                                if (point != 0) {
                                    skill = SkillUtil.createSkill(tempId, point);
                                } else {
                                    skill = SkillUtil.createSkillLevel0(tempId);
                                }
                                skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                                if (dataSkill.size() > 3) {
                                    skill.currLevel = Short.parseShort(String.valueOf(dataSkill.get(3)));
                                }
                                player.playerSkill.skills.add(skill);
                            }
                            dataArray.clear();

                            //data skill shortcut
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("skills_shortcut"));
                            for (int i = 0; i < dataArray.size(); i++) {
                                player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                            }
                            for (int i : player.playerSkill.skillShortCut) {
                                if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                                    break;
                                }
                            }
                            if (player.playerSkill.skillSelect == null) {
                                player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                                        ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                            }
                            dataArray.clear();

//                            //data event
//                            dataArray = (JSONArray) JSONValue.parse(rs.getString("diem_event"));
//                            player.event.setEventPointPoint(Integer.parseInt(String.valueOf(dataArray.get(0))));
//                            player.event.setEventPointPointBHM(Integer.parseInt(String.valueOf(dataArray.get(1))));
//                            player.event.setEventPointPointNHS(Integer.parseInt(String.valueOf(dataArray.get(2))));
//                            player.event.setEventPointPointQuai(Integer.parseInt(String.valueOf(dataArray.get(3))));
//                            dataArray.clear();
                            //data pet
                            JSONArray petData = (JSONArray) JSONValue.parse(rs.getString("pet"));
                            if (!petData.isEmpty()) {
                                dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(0)));
                                Pet pet = new Pet(player);
                                pet.id = -player.id;
                                pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                                pet.name = String.valueOf(dataArray.get(2));
                                player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                                player.fusion.lastTimeFusion = System.currentTimeMillis()
                                        - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                                pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));

                                //data chỉ số
                                dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(1)));
                                pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                                pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                                pet.nPoint.tiemNang = new BigDecimal(String.valueOf(dataArray.get(2))).longValue();
                                pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                                pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                                pet.nPoint.hpg = new BigDecimal(String.valueOf(dataArray.get(5))).longValue();
                                pet.nPoint.mpg = new BigDecimal(String.valueOf(dataArray.get(6))).longValue();
                                pet.nPoint.dameg = new BigDecimal(String.valueOf(dataArray.get(7))).longValue();
                                pet.nPoint.defg = new BigDecimal(String.valueOf(dataArray.get(8))).intValue();
                                pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                                long hp = new BigDecimal(String.valueOf(dataArray.get(10))).longValue();
                                long mp = new BigDecimal(String.valueOf(dataArray.get(11))).longValue();

                                //data body
                                dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(2)));
                                for (int i = 0; i < dataArray.size(); i++) {
                                    Item item = null;
                                    JSONArray dataItem = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                                    if (tempId != -1) {
                                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                                        for (int j = 0; j < options.size(); j++) {
                                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                                        }
                                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                                        if (ItemService.gI().isOutOfDateTime(item)) {
                                            item = ItemService.gI().createItemNull();
                                        }
                                    } else {
                                        item = ItemService.gI().createItemNull();

                                    }
                                    pet.inventory.itemsBody.add(item);
                                }

                                //data skills
                                dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(3)));
                                for (int i = 0; i < dataArray.size(); i++) {
                                    JSONArray skillTemp = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                                    int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                                    byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                                    Skill skill = null;
                                    if (point != 0) {
                                        skill = SkillUtil.createSkill(tempId, point);
                                    } else {
                                        skill = SkillUtil.createSkillLevel0(tempId);
                                    }
                                    switch (skill.template.id) {
                                        case Skill.KAMEJOKO:
                                        case Skill.MASENKO:
                                        case Skill.ANTOMIC:
                                            skill.coolDown = 1000;
                                            break;
                                    }
                                    pet.playerSkill.skills.add(skill);
                                }
                                if (pet.playerSkill.skills.size() < 5) {
                                    pet.playerSkill.skills.add(4, SkillUtil.createSkillLevel0(-1));
                                }
                                pet.nPoint.hp = hp;
                                pet.nPoint.mp = mp;
                                player.pet = pet;
                            }
                            dataArray.clear();
                            LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());

                            dataArray = (JSONArray) JSONValue.parse(rs.getString("info_phoban"));
                            if (!dataArray.isEmpty()) {
                                player.bdkb_countPerDay = Integer.parseInt(String.valueOf(dataArray.get(1)));
                                player.bdkb_lastTimeJoin = Long.parseLong(String.valueOf(dataArray.get(0)));

                            }
                            dataArray.clear();
                            player.bdkb_isJoinBdkb = false;
//                            if ((new java.sql.Date(player.bdkb_lastTimeJoin)).getDay() != (new java.sql.Date(System.currentTimeMillis())).getDay()) {
//                                player.bdkb_countPerDay = 0;
//                            }
// Chuyển đổi `bdkb_lastTimeJoin` sang `LocalDate`
                            LocalDate lastBdkbJoinDate = new Date(player.bdkb_lastTimeJoin).toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDate();

                            if (!lastBdkbJoinDate.isEqual(currentDate)) {
                                player.bdkb_countPerDay = 0;
                            }
                            dataArray = (JSONArray) JSONValue.parse(rs.getString("info_phoban_cdrd"));
                            if (!dataArray.isEmpty()) {
                                player.cdrd_countPerDay = Integer.parseInt(String.valueOf(dataArray.get(1)));
                                player.cdrd_lastTimeJoin = Long.parseLong(String.valueOf(dataArray.get(0)));

                            }
                            dataArray.clear();
                            player.cdrd_isJoinCdrd = false;
//                            if ((new java.sql.Date(player.cdrd_lastTimeJoin)).getDay() != (new java.sql.Date(System.currentTimeMillis())).getDay()) {
//                                player.cdrd_countPerDay = 0;
//                            }
// Chuyển đổi `cdrd_lastTimeJoin` sang `LocalDate`
                            LocalDate lastCdrdJoinDate = new Date(player.cdrd_lastTimeJoin).toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDate();

                            if (!lastCdrdJoinDate.isEqual(currentDate)) {
                                player.cdrd_countPerDay = 0;
                            }
                            player.nPoint.hp = plHp;
                            player.nPoint.mp = plMp;
                            player.iDMark.setLoadedAllDataPlayer(true);
                            GirlkunDB.executeUpdate("update account set last_time_login = '" + new Timestamp(System.currentTimeMillis()) + "', ip_address = '" + session.ipAddress + "' where id = " + session.userId);
                        }
                    }
                }
                al.reset();
            } else {
                Service.gI().sendThongBaoOK(session, "Thông tin tài khoản hoặc mật khẩu không chính xác");
                al.wrong();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(session.uu);
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e, "");
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }

        return player;
    }

    public static void checkDo() {
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        Player player;
        PreparedStatement ps = null;
        String name = "";
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("select * from player");
            rs = ps.executeQuery();
            while (rs.next()) {
                long plHp = 200000000;
                long plMp = 200000000;
                player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (dataArray.size() == 4) {
                    player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                } else {
                    player.inventory.coupon = 0;
                }
                if (dataArray.size() >= 5) {
                    player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
                } else {
                    player.inventory.event = 0;
                }
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = new BigDecimal(String.valueOf(dataArray.get(2))).longValue();
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = new BigDecimal(String.valueOf(dataArray.get(5))).longValue();
                player.nPoint.mpg = new BigDecimal(String.valueOf(dataArray.get(6))).longValue();

                //  player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.dameg = new BigDecimal(String.valueOf(dataArray.get(7))).longValue();
                //  player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.defg = new BigDecimal(String.valueOf(dataArray.get(8))).intValue();

                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = new BigDecimal(String.valueOf(dataArray.get(11))).longValue();
                plMp = new BigDecimal(String.valueOf(dataArray.get(12))).longValue();

                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));

                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "body");
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "bag");
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "box");
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();

                //data box lucky round
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box_lucky_round"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                }
                dataArray.clear();

                //data pet
                JSONArray petData = (JSONArray) JSONValue.parse(rs.getString("pet"));
                if (!petData.isEmpty()) {
                    dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(0)));
                    Pet pet = new Pet(player);
                    pet.id = -player.id;
                    pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                    pet.name = String.valueOf(dataArray.get(2));
                    player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                    player.fusion.lastTimeFusion = System.currentTimeMillis()
                            - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                    pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));

                    //data chỉ số
                    dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(1)));
                    pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                    pet.nPoint.tiemNang = new BigDecimal(String.valueOf(dataArray.get(2))).longValue();
                    pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                    pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                    pet.nPoint.hpg = new BigDecimal(String.valueOf(dataArray.get(5))).longValue();
                    pet.nPoint.mpg = new BigDecimal(String.valueOf(dataArray.get(6))).longValue();

                    //  player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                    pet.nPoint.dameg = new BigDecimal(String.valueOf(dataArray.get(7))).longValue();
                    //  player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                    pet.nPoint.defg = new BigDecimal(String.valueOf(dataArray.get(8))).intValue();

                    pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                    long hp = new BigDecimal(String.valueOf(dataArray.get(10))).longValue();
                    long mp = new BigDecimal(String.valueOf(dataArray.get(11))).longValue();

                    //data body
                    dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(2)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONArray dataItem = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                        short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                            JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        Util.useCheckDo(player, item, "pet");
                        pet.inventory.itemsBody.add(item);
                    }

                }

            }
        } catch (Exception e) {
            System.out.println(name);
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi load database");
            System.exit(0);
        }
    }

    public static void checkVang(int x) {
        int thoi_vang = 0;
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        Player player;
        PreparedStatement ps = null;
        String name = "";
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection()) {
            ps = con.prepareStatement("select * from player");
            rs = ps.executeQuery();
            while (rs.next()) {
                long plHp = 200000000;
                long plMp = 200000000;
                player = new Player();
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (dataArray.size() == 4) {
                    player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                } else {
                    player.inventory.coupon = 0;
                }
                if (dataArray.size() >= 5) {
                    player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
                } else {
                    player.inventory.event = 0;
                }
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = new BigDecimal(String.valueOf(dataArray.get(2))).longValue();
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = new BigDecimal(String.valueOf(dataArray.get(5))).longValue();
                player.nPoint.mpg = new BigDecimal(String.valueOf(dataArray.get(6))).longValue();

                //  player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.dameg = new BigDecimal(String.valueOf(dataArray.get(7))).longValue();
                //  player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.defg = new BigDecimal(String.valueOf(dataArray.get(8))).intValue();

                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = new BigDecimal(String.valueOf(dataArray.get(11))).longValue();
                plMp = new BigDecimal(String.valueOf(dataArray.get(12))).longValue();

                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));

                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "body");
                    player.inventory.itemsBody.add(item);
                    if (item.template.id == 457 || item.template.id == 1108) {
                        thoi_vang += item.quantity;
                    }
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "bag");
                    if (item.template.id == 457 || item.template.id == 1108) {
                        thoi_vang += item.quantity;
                    }
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    Util.useCheckDo(player, item, "box");
                    if (item.template.id == 457 || item.template.id == 1108) {
                        thoi_vang += item.quantity;
                    }
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();
                if (thoi_vang > x) {
                    Logger.error("play:" + player.name);
                    Logger.error("thoi_vang:" + thoi_vang);
                }

            }

        } catch (Exception e) {
            System.out.println(name);
            e.printStackTrace();
            Logger.logException(Manager.class, e, "Lỗi load database");
        }
    }

    public static Player loadById(int id) {
        Player player = null;
        GirlkunResultSet rs = null;
        if (Client.gI().getPlayer(id) != null) {
            player = Client.gI().getPlayer(id);
            return player;
        }
        try {
            rs = GirlkunDB.executeQuery("select * from player where id = ? limit 1", id);
            if (rs.first()) {
                long plHp = 200000000;
                long plMp = 200000000;
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;

                player = new Player();

                //base info
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");

                int clanId = rs.getInt("clan_id_sv" + Manager.SERVER);
                if (clanId != -1) {
                    Clan clan = ClanService.gI().getClanById(clanId);
                    for (ClanMember cm : clan.getMembers()) {
                        if (cm.id == player.id) {
                            clan.addMemberOnline(player);
                            player.clan = clan;
                            player.clanMember = cm;
                            break;
                        }
                    }
                }

                //data kim lượng
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_inventory"));
                player.inventory.gold = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.inventory.gem = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.inventory.ruby = Integer.parseInt(String.valueOf(dataArray.get(2)));
                player.inventory.coupon = Integer.parseInt(String.valueOf(dataArray.get(3)));
                player.inventory.event = Integer.parseInt(String.valueOf(dataArray.get(4)));
                dataArray.clear();
                player.rankSieuHang = rs.getInt("rank_sieu_hang");
                if (player.rankSieuHang == 999999) {
                    player.rankSieuHang = ServerManager.gI().getNumPlayer();
                }
                //data tọa độ

                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_location"));
                int mapId = Integer.parseInt(String.valueOf(dataArray.get(0)));
                player.location.x = Integer.parseInt(String.valueOf(dataArray.get(1)));
                player.location.y = Integer.parseInt(String.valueOf(dataArray.get(2)));
                if (MapService.gI().isMapDoanhTrai(mapId) || MapService.gI().isNguHanhSon(mapId) || MapService.gI().isMapBlackBallWar(mapId)
                        || MapService.gI().isMapKhiGas(mapId) || MapService.gI().isMapConDuongRanDoc(mapId)
                        || MapService.gI().isMapBanDoKhoBau(mapId) || MapService.gI().isMapMaBu(mapId)) {
                    mapId = player.gender + 21;
                    player.location.x = 300;
                    player.location.y = 336;
                }
                player.zone = MapService.gI().getMapCanJoin(player, mapId, -1);
                dataArray.clear();

                //data chỉ số
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_point"));
                player.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.nPoint.tiemNang = new BigDecimal(String.valueOf(dataArray.get(2))).longValue();
                player.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                player.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                player.nPoint.hpg = new BigDecimal(String.valueOf(dataArray.get(5))).longValue();
                player.nPoint.mpg = new BigDecimal(String.valueOf(dataArray.get(6))).longValue();

                //  player.nPoint.dameg = Integer.parseInt(String.valueOf(dataArray.get(7)));
                player.nPoint.dameg = new BigDecimal(String.valueOf(dataArray.get(7))).longValue();
                //  player.nPoint.defg = Integer.parseInt(String.valueOf(dataArray.get(8)));
                player.nPoint.defg = new BigDecimal(String.valueOf(dataArray.get(8))).intValue();

                player.nPoint.critg = Byte.parseByte(String.valueOf(dataArray.get(9)));
                dataArray.get(10); //** Năng động
                plHp = new BigDecimal(String.valueOf(dataArray.get(11))).longValue();
                plMp = new BigDecimal(String.valueOf(dataArray.get(12))).longValue();
                dataArray.clear();

                //data đậu thần
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_magic_tree"));
                byte level = Byte.parseByte(String.valueOf(dataArray.get(0)));
                byte currPea = Byte.parseByte(String.valueOf(dataArray.get(1)));
                boolean isUpgrade = Byte.parseByte(String.valueOf(dataArray.get(2))) == 1;
                long lastTimeHarvest = Long.parseLong(String.valueOf(dataArray.get(3)));
                long lastTimeUpgrade = Long.parseLong(String.valueOf(dataArray.get(4)));
                player.magicTree = new MagicTree(player, level, currPea, lastTimeHarvest, isUpgrade, lastTimeUpgrade);
                dataArray.clear();

                //data phần thưởng sao đen
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_black_ball"));
                JSONArray dataBlackBall = null;
                for (int i = 0; i < dataArray.size(); i++) {
                    dataBlackBall = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                    player.rewardBlackBall.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(0)));
                    player.rewardBlackBall.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataBlackBall.get(1)));
                    dataBlackBall.clear();
                }
                dataArray.clear();

                //data phần thưởng giai dau
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_giai_dau"));
                JSONArray dataGiaidau = null;
                for (int i = 0; i < dataArray.size(); i++) {
                    dataGiaidau = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                    player.rewardGiaidau.timeOutOfDateReward[i] = Long.parseLong(String.valueOf(dataGiaidau.get(0)));
                    player.rewardGiaidau.lastTimeGetReward[i] = Long.parseLong(String.valueOf(dataGiaidau.get(1)));
                    dataGiaidau.clear();
                }
                dataArray.clear();

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_body"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBody.add(item);
                }
                dataArray.clear();

                //data bag
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_bag"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBag.add(item);
                }
                dataArray.clear();

                //data box
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsBox.add(item);
                }
                dataArray.clear();

                //data box lucky round
                dataArray = (JSONArray) JSONValue.parse(rs.getString("items_box_lucky_round"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        player.inventory.itemsBoxCrackBall.add(item);
                    }
                }
                dataArray.clear();

                //data friends
                dataArray = (JSONArray) JSONValue.parse(rs.getString("friends"));
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray dataFE = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                        Friend friend = new Friend();
                        friend.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                        friend.name = String.valueOf(dataFE.get(1));
                        friend.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                        friend.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                        friend.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                        friend.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                        friend.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                        player.friends.add(friend);
                        dataFE.clear();
                    }
                    dataArray.clear();
                }

                //data enemies
                dataArray = (JSONArray) JSONValue.parse(rs.getString("enemies"));
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray dataFE = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                        Enemy enemy = new Enemy();
                        enemy.id = Integer.parseInt(String.valueOf(dataFE.get(0)));
                        enemy.name = String.valueOf(dataFE.get(1));
                        enemy.head = Short.parseShort(String.valueOf(dataFE.get(2)));
                        enemy.body = Short.parseShort(String.valueOf(dataFE.get(3)));
                        enemy.leg = Short.parseShort(String.valueOf(dataFE.get(4)));
                        enemy.bag = Byte.parseByte(String.valueOf(dataFE.get(5)));
                        enemy.power = Long.parseLong(String.valueOf(dataFE.get(6)));
                        player.enemies.add(enemy);
                        dataFE.clear();
                    }
                    dataArray.clear();
                }

                //data nội tại
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_intrinsic"));
                byte intrinsicId = Byte.parseByte(String.valueOf(dataArray.get(0)));
                player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(intrinsicId);
                player.playerIntrinsic.intrinsic.param1 = Short.parseShort(String.valueOf(dataArray.get(1)));
                player.playerIntrinsic.intrinsic.param2 = Short.parseShort(String.valueOf(dataArray.get(2)));
                player.playerIntrinsic.countOpen = Byte.parseByte(String.valueOf(dataArray.get(3)));
                dataArray.clear();

                //data item time
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_time"));
                int timeBoHuyet = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int timeBoKhi = Integer.parseInt(String.valueOf(dataArray.get(1)));
                int timeGiapXen = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timeCuongNo = Integer.parseInt(String.valueOf(dataArray.get(3)));
                int timeAnDanh = Integer.parseInt(String.valueOf(dataArray.get(4)));
                int timeOpenPower = Integer.parseInt(String.valueOf(dataArray.get(5)));
                int timeMayDo = Integer.parseInt(String.valueOf(dataArray.get(6)));
                int timeMayDo2 = Integer.parseInt(String.valueOf(dataArray.get(10)));
                int timeMayDo3 = Integer.parseInt(String.valueOf(dataArray.get(11)));
                int timeMeal = Integer.parseInt(String.valueOf(dataArray.get(7)));
                int iconMeal = Integer.parseInt(String.valueOf(dataArray.get(8)));
                int timeUseTDLT = 0;
                if (dataArray.size() == 10) {
                    timeUseTDLT = Integer.parseInt(String.valueOf(dataArray.get(9)));
                }
                int timeBiNgo = Integer.parseInt(String.valueOf(dataArray.get(10)));
                player.itemTime.lastTimeBoHuyet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyet);
                player.itemTime.lastTimeBoKhi = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhi);
                player.itemTime.lastTimeGiapXen = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXen);
                player.itemTime.lastTimeCuongNo = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNo);
                player.itemTime.lastTimeAnDanh = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanh);
                player.itemTime.lastTimeOpenPower = System.currentTimeMillis() - (ItemTime.TIME_OPEN_POWER - timeOpenPower);
                player.itemTime.lastTimeUseMayDo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeMayDo);
                player.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO2 - timeMayDo2);
                player.itemTime.lastTimeUseMayDo3 = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO3 - timeMayDo3);
                player.itemTime.lastTimeEatMeal = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeMeal);
                player.itemTime.timeTDLT = timeUseTDLT * 60 * 1000;
                player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
                player.itemTime.lastTimeBiNgo = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeBiNgo);

                player.itemTime.iconMeal = iconMeal;
                player.itemTime.isUseBoHuyet = timeBoHuyet != 0;
                player.itemTime.isUseBoKhi = timeBoKhi != 0;
                player.itemTime.isUseGiapXen = timeGiapXen != 0;
                player.itemTime.isUseCuongNo = timeCuongNo != 0;
                player.itemTime.isUseAnDanh = timeAnDanh != 0;

                player.itemTime.isOpenPower = timeOpenPower != 0;
                player.itemTime.isUseMayDo = timeMayDo != 0;
                player.itemTime.isUseMayDo2 = timeMayDo2 != 0;
                player.itemTime.isUseMayDo3 = timeMayDo3 != 0;
                player.itemTime.isEatMeal = timeMeal != 0;
                player.itemTime.isUseTDLT = timeUseTDLT != 0;
                player.itemTime.isBiNgo = timeBiNgo != 0;
                dataArray.clear();

                //data sieu cap
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_time_sieu_cap"));
                int timeBoHuyetSC = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int timeBoKhiSC = Integer.parseInt(String.valueOf(dataArray.get(1)));
                int timeGiapXenSC = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timeCuongNoSC = Integer.parseInt(String.valueOf(dataArray.get(3)));
                int timeAnDanhSC = Integer.parseInt(String.valueOf(dataArray.get(4)));
                int timeBanhTet = Integer.parseInt(String.valueOf(dataArray.get(5)));
                int timeBanhChung = Integer.parseInt(String.valueOf(dataArray.get(6)));
                int timeBanhNhen = Integer.parseInt(String.valueOf(dataArray.get(7)));
                int timeBanhSau = Integer.parseInt(String.valueOf(dataArray.get(8)));
                int timeKeoMotMat = Integer.parseInt(String.valueOf(dataArray.get(9)));
                int timeSupBi = Integer.parseInt(String.valueOf(dataArray.get(10)));
                int timeBanhNgot = Integer.parseInt(String.valueOf(dataArray.get(11)));
                int timeKemOcQue = Integer.parseInt(String.valueOf(dataArray.get(12)));
                int timeKeoDeo = Integer.parseInt(String.valueOf(dataArray.get(13)));
                int timeKeoTrongGoi = Integer.parseInt(String.valueOf(dataArray.get(14)));
                player.itemTime.lastTimeBoHuyetSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoHuyetSC);
                player.itemTime.lastTimeBoKhiSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBoKhiSC);
                player.itemTime.lastTimeGiapXenSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeGiapXenSC);
                player.itemTime.lastTimeCuongNoSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeCuongNoSC);
                player.itemTime.lastTimeAnDanhSC = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeAnDanhSC);
                player.itemTime.lastTimeBanhTet = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanhTet);
                player.itemTime.lastTimeBanhChung = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeBanhChung);
                player.itemTime.lastTimeBanhNhen = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeBoHuyetSC);
                player.itemTime.lastTimeBanhSau = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeBoKhiSC);
                player.itemTime.lastTimeKeoMotMat = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeGiapXenSC);
                player.itemTime.lastTimeSupBi = System.currentTimeMillis() - (ItemTime.TIME_MAY_DO - timeCuongNoSC);
                player.itemTime.lastTimeBanhNgot = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeAnDanhSC);
                player.itemTime.lastTimeKeoDeo = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeBanhTet);
                player.itemTime.lastTimeKemOcQue = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeBanhChung);
                player.itemTime.lastTimeKeoTrongGoi = System.currentTimeMillis() - (ItemTime.TIME_EAT_MEAL - timeBanhChung);

                player.itemTime.isUseBoHuyetSC = timeBoHuyetSC != 0;
                player.itemTime.isUseBoKhiSC = timeBoKhiSC != 0;
                player.itemTime.isUseGiapXenSC = timeGiapXenSC != 0;
                player.itemTime.isUseCuongNoSC = timeCuongNoSC != 0;
                player.itemTime.isUseAnDanhSC = timeAnDanhSC != 0;
                player.itemTime.isUseBanhTet = timeBanhTet != 0;
                player.itemTime.isUseBanhChung = timeBanhChung != 0;
                player.itemTime.isUseBanhNhen = timeBanhNhen != 0;
                player.itemTime.isUseBanhSau = timeBanhSau != 0;
                player.itemTime.isUseKeoMotMat = timeKeoMotMat != 0;
                player.itemTime.isUseSupBi = timeSupBi != 0;
                player.itemTime.isUseBanhNgot = timeBanhNgot != 0;
                player.itemTime.isUseKemOcQue = timeKemOcQue != 0;
                player.itemTime.isUseKeoDeo = timeKeoDeo != 0;
                player.itemTime.isUseKeoTrongGoi = timeKeoTrongGoi != 0;
                dataArray.clear();

                //data su kien
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_item_time_su_kien"));
                int timevooc = Integer.parseInt(String.valueOf(dataArray.get(0)));
                int timesaobien = Integer.parseInt(String.valueOf(dataArray.get(1)));
                int timeconcua = Integer.parseInt(String.valueOf(dataArray.get(2)));
                int timemaydo = Integer.parseInt(String.valueOf(dataArray.get(3)));

                player.itemTime.lastTimeUseVooc = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timevooc);
                player.itemTime.lastTimeUseSaoBien = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timesaobien);
                player.itemTime.lastTimeUseConCua = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timeconcua);
                player.itemTime.lastTimeUseMayDoSK = System.currentTimeMillis() - (ItemTime.TIME_ITEM - timemaydo);

                player.itemTime.isUseVooc = timevooc != 0;
                player.itemTime.isUseSaoBien = timesaobien != 0;
                player.itemTime.isUseConCua = timeconcua != 0;
                player.itemTime.isUseMayDoSK = timemaydo != 0;

                //data nhiệm vụ
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_task"));
                TaskMain taskMain = TaskService.gI().getTaskMainById(player, Byte.parseByte(String.valueOf(dataArray.get(0))));
                taskMain.index = Byte.parseByte(String.valueOf(dataArray.get(1)));
                taskMain.subTasks.get(taskMain.index).count = Short.parseShort(String.valueOf(dataArray.get(2)));
                player.playerTask.taskMain = taskMain;
                dataArray.clear();

                dataArray = (JSONArray) JSONValue.parse(rs.getString("info_phoban"));
                if (!dataArray.isEmpty()) {
                    player.bdkb_countPerDay = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    player.bdkb_lastTimeJoin = Long.parseLong(String.valueOf(dataArray.get(0)));

                }
                dataArray.clear();

                dataArray = (JSONArray) JSONValue.parse(rs.getString("info_phoban_cdrd"));
                if (!dataArray.isEmpty()) {
                    player.cdrd_countPerDay = Integer.parseInt(String.valueOf(dataArray.get(1)));
                    player.cdrd_lastTimeJoin = Long.parseLong(String.valueOf(dataArray.get(0)));

                }
                dataArray.clear();
                //data nhiệm vụ hàng ngày
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_side_task"));
                String format = "dd-MM-yyyy";
                long receivedTime = Long.parseLong(String.valueOf(dataArray.get(1)));
                Date date = new Date(receivedTime);
                if (TimeUtil.formatTime(date, format).equals(TimeUtil.formatTime(new Date(), format))) {
                    player.playerTask.sideTask.template = TaskService.gI().getSideTaskTemplateById(Integer.parseInt(String.valueOf(dataArray.get(0))));
                    player.playerTask.sideTask.count = Integer.parseInt(String.valueOf(dataArray.get(2)));
                    player.playerTask.sideTask.maxCount = Integer.parseInt(String.valueOf(dataArray.get(3)));
                    player.playerTask.sideTask.leftTask = Integer.parseInt(String.valueOf(dataArray.get(4)));
                    player.playerTask.sideTask.level = Integer.parseInt(String.valueOf(dataArray.get(5)));
                    player.playerTask.sideTask.receivedTime = receivedTime;
                }

                //data trứng bư
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_mabu_egg"));
                if (dataArray.size() != 0) {
                    player.mabuEgg = new MabuEgg(player, Long.parseLong(String.valueOf(dataArray.get(0))),
                            Long.parseLong(String.valueOf(dataArray.get(1))));
                }
                dataArray.clear();

                //data bùa
                dataArray = (JSONArray) JSONValue.parse(rs.getString("data_charm"));
                player.charms.tdTriTue = Long.parseLong(String.valueOf(dataArray.get(0)));
                player.charms.tdManhMe = Long.parseLong(String.valueOf(dataArray.get(1)));
                player.charms.tdDaTrau = Long.parseLong(String.valueOf(dataArray.get(2)));
                player.charms.tdOaiHung = Long.parseLong(String.valueOf(dataArray.get(3)));
                player.charms.tdBatTu = Long.parseLong(String.valueOf(dataArray.get(4)));
                player.charms.tdDeoDai = Long.parseLong(String.valueOf(dataArray.get(5)));
                player.charms.tdThuHut = Long.parseLong(String.valueOf(dataArray.get(6)));
                player.charms.tdDeTu = Long.parseLong(String.valueOf(dataArray.get(7)));
                player.charms.tdTriTue3 = Long.parseLong(String.valueOf(dataArray.get(8)));
                player.charms.tdTriTue4 = Long.parseLong(String.valueOf(dataArray.get(9)));
                dataArray.clear();

                //data skill
                dataArray = (JSONArray) JSONValue.parse(rs.getString("skills"));
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONArray dataSkill = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                    int tempId = Integer.parseInt(String.valueOf(dataSkill.get(0)));
                    byte point = Byte.parseByte(String.valueOf(dataSkill.get(1)));
                    Skill skill = null;
                    if (point != 0) {
                        skill = SkillUtil.createSkill(tempId, point);
                    } else {
                        skill = SkillUtil.createSkillLevel0(tempId);
                    }
                    skill.lastTimeUseThisSkill = Long.parseLong(String.valueOf(dataSkill.get(2)));
                    player.playerSkill.skills.add(skill);
                }
                dataArray.clear();

                //data skill shortcut
                dataArray = (JSONArray) JSONValue.parse(rs.getString("skills_shortcut"));
                for (int i = 0; i < dataArray.size(); i++) {
                    player.playerSkill.skillShortCut[i] = Byte.parseByte(String.valueOf(dataArray.get(i)));
                }
                for (int i : player.playerSkill.skillShortCut) {
                    if (player.playerSkill.getSkillbyId(i) != null && player.playerSkill.getSkillbyId(i).damage > 0) {
                        player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(i);
                        break;
                    }
                }
                if (player.playerSkill.skillSelect == null) {
                    player.playerSkill.skillSelect = player.playerSkill.getSkillbyId(player.gender == ConstPlayer.TRAI_DAT
                            ? Skill.DRAGON : (player.gender == ConstPlayer.NAMEC ? Skill.DEMON : Skill.GALICK));
                }
                dataArray.clear();

                //data pet
                JSONArray petData = (JSONArray) JSONValue.parse(rs.getString("pet"));
                if (!petData.isEmpty()) {
                    dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(0)));
                    Pet pet = new Pet(player);
                    pet.id = -player.id;
                    pet.typePet = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.gender = Byte.parseByte(String.valueOf(dataArray.get(1)));
                    pet.name = String.valueOf(dataArray.get(2));
                    player.fusion.typeFusion = Byte.parseByte(String.valueOf(dataArray.get(3)));
                    player.fusion.lastTimeFusion = System.currentTimeMillis()
                            - (Fusion.TIME_FUSION - Integer.parseInt(String.valueOf(dataArray.get(4))));
                    pet.status = Byte.parseByte(String.valueOf(dataArray.get(5)));

                    //data chỉ số
                    dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(1)));
                    pet.nPoint.limitPower = Byte.parseByte(String.valueOf(dataArray.get(0)));
                    pet.nPoint.power = Long.parseLong(String.valueOf(dataArray.get(1)));
                    pet.nPoint.tiemNang = new BigDecimal(String.valueOf(dataArray.get(2))).longValue();
                    pet.nPoint.stamina = Short.parseShort(String.valueOf(dataArray.get(3)));
                    pet.nPoint.maxStamina = Short.parseShort(String.valueOf(dataArray.get(4)));
                    pet.nPoint.hpg = new BigDecimal(String.valueOf(dataArray.get(5))).longValue();
                    pet.nPoint.mpg = new BigDecimal(String.valueOf(dataArray.get(6))).longValue();
                    pet.nPoint.dameg = new BigDecimal(String.valueOf(dataArray.get(7))).longValue();
                    pet.nPoint.defg = new BigDecimal(String.valueOf(dataArray.get(8))).intValue();
                    pet.nPoint.critg = Integer.parseInt(String.valueOf(dataArray.get(9)));
                    long hp = new BigDecimal(String.valueOf(dataArray.get(10))).longValue();
                    long mp = new BigDecimal(String.valueOf(dataArray.get(11))).longValue();

                    //data body
                    dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(2)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONArray dataItem = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                        short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                            JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();;
                        }
                        pet.inventory.itemsBody.add(item);
                    }

                    //data skills
                    dataArray = (JSONArray) JSONValue.parse(String.valueOf(petData.get(3)));
                    for (int i = 0; i < dataArray.size(); i++) {
                        JSONArray skillTemp = (JSONArray) JSONValue.parse(String.valueOf(dataArray.get(i)));
                        int tempId = Integer.parseInt(String.valueOf(skillTemp.get(0)));
                        byte point = Byte.parseByte(String.valueOf(skillTemp.get(1)));
                        Skill skill = null;
                        if (point != 0) {
                            skill = SkillUtil.createSkill(tempId, point);
                        } else {
                            skill = SkillUtil.createSkillLevel0(tempId);
                        }
                        switch (skill.template.id) {
                            case Skill.KAMEJOKO:
                            case Skill.MASENKO:
                            case Skill.ANTOMIC:
                                skill.coolDown = 1000;
                                break;
                        }
                        pet.playerSkill.skills.add(skill);
                    }
                    pet.nPoint.hp = hp;
                    pet.nPoint.mp = mp;
                    player.pet = pet;
                }

                player.nPoint.hp = plHp;
                player.nPoint.mp = plMp;
                player.iDMark.setLoadedAllDataPlayer(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    public static List<Item> getMailBox(Player player) {
        try {
            List<Item> mailBoxs = new ArrayList<>();
            JSONValue jv = new JSONValue();
            JSONArray dataArray = null;
            player.inventory.itemsMailBox.clear();
            PreparedStatement ps = null;
            ResultSet rs = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("select `item_mails_box` from player where id = ?");
            ps.setLong(1, player.id);
            rs = ps.executeQuery();
            while (rs.next()) {
                //data box hòm thư
                dataArray = (JSONArray) JSONValue.parse(rs.getString("item_mails_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        player.inventory.itemsMailBox.add(item);
                    }
                    mailBoxs.add(item);
                }
                dataArray.clear();
            }
            rs.close();
            ps.close();
            con.close();
            return mailBoxs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Player> getAllPlayer() {
        try {
            List<Player> players = new ArrayList<>();
            GirlkunResultSet rs = null;
            try {
                Player player = new Player();
                rs = GirlkunDB.executeQuery("select * from player");
                while (rs.next()) {
                    long plHp = 200000000;
                    long plMp = 200000000;
                    JSONValue jv = new JSONValue();
                    JSONArray dataArray = null;

                    player = new Player();

                    //base info
                    player.id = rs.getInt("id");
                    player.name = rs.getString("name");
                    player.head = rs.getShort("head");
                    player.gender = rs.getByte("gender");
                    player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");
                    //data box hòm thư
                    dataArray = (JSONArray) JSONValue.parse(rs.getString("item_mails_box"));
                    for (int i = 0; i < dataArray.size(); i++) {
                        Item item = null;
                        JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                        short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                        if (tempId != -1) {
                            item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                            JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                            for (int j = 0; j < options.size(); j++) {
                                JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                                item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                        Integer.parseInt(String.valueOf(opt.get(1)))));
                            }
                            item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                            if (ItemService.gI().isOutOfDateTime(item)) {
                                item = ItemService.gI().createItemNull();
                            }
                        } else {
                            item = ItemService.gI().createItemNull();
                        }
                        player.inventory.itemsMailBox.add(item);
                    }
                    dataArray.clear();

                    player.nPoint.hp = plHp;
                    player.nPoint.mp = plMp;
                    player.iDMark.setLoadedAllDataPlayer(true);

                    players.add(player);

                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (rs != null) {
                    rs.dispose();
                }
            }

            return players;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Player loadPlayerByName(String name) {
        Player player = null;
        GirlkunResultSet rs = null;
        try {
            player = Client.gI().getPlayer(name);
            if (player != null) {
                return player;
            }
            rs = GirlkunDB.executeQuery("select * from player where name = ? limit 1", name);
            if (rs.first()) {
                long plHp = 200000000;
                long plMp = 200000000;
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;

                player = new Player();

                //base info
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("item_mails_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsMailBox.add(item);
                }
                dataArray.clear();
                player.nPoint.hp = plHp;
                player.nPoint.mp = plMp;
                player.iDMark.setLoadedAllDataPlayer(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    public static Player loadPlayerByID(int id) {
        Player player = null;
        GirlkunResultSet rs = null;
        try {
            player = Client.gI().getPlayer(id);
            if (player != null) {
                return player;
            }
            rs = GirlkunDB.executeQuery("select * from player where id = ? limit 1", id);
            if (rs.first()) {
                long plHp = 200000000;
                long plMp = 200000000;
                JSONValue jv = new JSONValue();
                JSONArray dataArray = null;

                player = new Player();

                //base info
                player.id = rs.getInt("id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.haveTennisSpaceShip = rs.getBoolean("have_tennis_space_ship");

                //data body
                dataArray = (JSONArray) JSONValue.parse(rs.getString("item_mails_box"));
                for (int i = 0; i < dataArray.size(); i++) {
                    Item item = null;
                    JSONArray dataItem = (JSONArray) JSONValue.parse(dataArray.get(i).toString());
                    short tempId = Short.parseShort(String.valueOf(dataItem.get(0)));
                    if (tempId != -1) {
                        item = ItemService.gI().createNewItem(tempId, Integer.parseInt(String.valueOf(dataItem.get(1))));
                        JSONArray options = (JSONArray) JSONValue.parse(String.valueOf(dataItem.get(2)).replaceAll("\"", ""));
                        for (int j = 0; j < options.size(); j++) {
                            JSONArray opt = (JSONArray) JSONValue.parse(String.valueOf(options.get(j)));
                            item.itemOptions.add(new Item.ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                                    Integer.parseInt(String.valueOf(opt.get(1)))));
                        }
                        item.createTime = Long.parseLong(String.valueOf(dataItem.get(3)));
                        if (ItemService.gI().isOutOfDateTime(item)) {
                            item = ItemService.gI().createItemNull();
                        }
                    } else {
                        item = ItemService.gI().createItemNull();
                    }
                    player.inventory.itemsMailBox.add(item);
                }
                dataArray.clear();
                player.nPoint.hp = plHp;
                player.nPoint.mp = plMp;
                player.iDMark.setLoadedAllDataPlayer(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            player.dispose();
            player = null;
            Logger.logException(GodGK.class, e);
        } finally {
            if (rs != null) {
                rs.dispose();
            }
        }
        return player;
    }

    public static boolean updateMailBox(Player player) {
        try {
            JSONArray dataArray = new JSONArray();
            JSONArray dataItem = new JSONArray();
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
            String itemsBox = dataArray.toJSONString();
            dataArray.clear();
            PreparedStatement ps = null;
            Connection con = GirlkunDB.getConnection();
            ps = con.prepareStatement("update `player` set item_mails_box = ? where id = ?");
            ps.setString(1, itemsBox);
            ps.setLong(2, player.id);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
