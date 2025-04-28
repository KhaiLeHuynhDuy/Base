package nro.server;

import com.girlkun.database.GirlkunDB;
import java.sql.SQLException;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.network.server.GirlkunSessionManager;
import nro.network.session.ISession;
import nro.server.io.MySession;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.SummonDragon;
import nro.services.func.TransactionService;
import nro.services.InventoryServiceNew;
import nro.services.NgocRongNamecService;
import nro.utils.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import nro.models.matches.pvp.DaiHoiVoThuat;
import nro.models.matches.pvp.DaiHoiVoThuatService;
import nro.network.session.Session;
import nro.services.func.GoiRongXuong;
import java.util.stream.Collectors;
import nro.consts.ConstPlayer;
import nro.models.map.Zone;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.player.Inventory;
import nro.models.skill.Skill;
import nro.services.ItemService;
import nro.services.MapService;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class Client implements Runnable {

    private static Client i;
    private int id = 1_000_000_000;
    private final List<MySession> sessions = new ArrayList<>();
    private final Map<Integer, Session> sessions_id = new HashMap<>();
    private final Map<Long, Player> players_id = new HashMap<>();
    private final Map<Integer, Player> players_userId = new HashMap<>();
    private final Map<String, Player> players_name = new HashMap<>();
    private final List<Player> players = new ArrayList<>();
    private final List<Player> bots = new ArrayList<>();
    private boolean running = true;

    private Client() {
        new Thread(this).start();
    }
    private static final Random rand = new Random();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static String generateRandomName(int minLength, int maxLength) {
        int length = minLength + rand.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(rand.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public List<Player> getPlayers() {
        return this.players;
    }

//    public List<Player> getPlayers() {
//        synchronized (players) {
//            return this.players.stream().collect(Collectors.toList());
//        }
//    }
    public static Client gI() {
        if (i == null) {
            i = new Client();
        }
        return i;
    }

    public void put(Player player) {
        if (!players_id.containsKey(player.id)) {
            this.players_id.put(player.id, player);
        }
        if (!players_name.containsValue(player)) {
            this.players_name.put(player.name, player);
        }
        if (!players_userId.containsValue(player)) {
            this.players_userId.put(player.getSession().userId, player);
        }
        if (!players.contains(player)) {
            this.players.add(player);
        }

    }

    private void remove(MySession session) {
        if (session.player != null) {
            this.remove(session.player);
            session.player.dispose();
        }

        if (session.joinedGame) {
            session.joinedGame = false;
            try {
                CompletableFuture.runAsync(() -> {
                    try {
                        // Cập nhật cơ sở dữ liệu
                        GirlkunDB.executeUpdate("update account set last_time_logout = ? where id = ?", new Timestamp(System.currentTimeMillis()), session.userId);
                    } catch (SQLException e) {
                        e.printStackTrace();
//                    logger.error("Lỗi khi cập nhật cơ sở dữ liệu bất đồng bộ", e);
                        Logger.logException(Client.class, e, "Lỗi khi cập nhật cơ sở dữ liệu bất đồng bộ");
                        // Xử lý hoặc ghi log lỗi theo cần thiết
                    } catch (Exception e) {
                        e.printStackTrace();
//                    logger.error("Lỗi khác khi cập nhật cơ sở dữ liệu bất đồng bộ", e);
                        Logger.logException(Client.class, e, "Lỗi khi cập nhật cơ sở dữ liệu bất đồng bộ");
                        // Xử lý hoặc ghi log lỗi theo cần thiết
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
//            logger.error("Lỗi khi lên lịch cập nhật cơ sở dữ liệu bất đồng bộ", e);
                Logger.logException(Client.class, e, "Lỗi khi lên lịch cơ sở dữ liệu bất đồng bộ");
                // Xử lý hoặc ghi log lỗi theo cần thiết
            }
        }

        ServerManager.gI().disconnect(session);
    }

    private void remove(Player player) {
        this.players_id.remove(player.id);
        this.players_name.remove(player.name);
        this.players_userId.remove(player.getSession().userId);
        this.players.remove(player);
        if (!player.beforeDispose) {
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.getInstance().getDaiHoiNow()).removePlayerWait(player);
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.getInstance().getDaiHoiNow()).removePlayer(player);
            TranhNgoc.gI().removePlayersCadic(player);
            TranhNgoc.gI().removePlayersFide(player);
            player.beforeDispose = true;
            player.mapIdBeforeLogout = player.zone.map.mapId;
            if (player.idNRNM != -1) {
                ItemMap itemMap = new ItemMap(player.zone, player.idNRNM, 1, player.location.x, player.location.y, -1);
                Service.gI().dropItemMap(player.zone, itemMap);
                NgocRongNamecService.gI().pNrNamec[player.idNRNM - 353] = "";
                NgocRongNamecService.gI().idpNrNamec[player.idNRNM - 353] = -1;
                player.idNRNM = -1;
            }
            ChangeMapService.gI().exitMap(player);
            TransactionService.gI().cancelTrade(player);
            if (player.clan != null) {
                player.clan.removeMemberOnline(null, player);
            }
            if (player.itemTime != null && player.itemTime.isUseTDLT) {
                Item tdlt = null;
                try {
                    tdlt = InventoryServiceNew.gI().findItemBag(player, 521);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (tdlt != null) {
                    ItemTimeService.gI().turnOffTDLT(player, tdlt);
                }
            }
            if (SummonDragon.gI().playerSummonShenron != null
                    && SummonDragon.gI().playerSummonShenron.id == player.id) {
                SummonDragon.gI().isPlayerDisconnect = true;
            }
            if (GoiRongXuong.gI().playerRongXuong != null
                    && GoiRongXuong.gI().playerRongXuong.id == player.id) {
                GoiRongXuong.gI().isPlayerDisconnect = true;
            }
            if (player.mobMe != null) {
                player.mobMe.mobMeDie();
            }
            if (player.pet != null) {
                if (player.pet.mobMe != null) {
                    player.pet.mobMe.mobMeDie();
                }
                if (player.newpet != null) {
                    ChangeMapService.gI().exitMap(player.newpet);
                }
                 if (player.minipet != null) {
                    ChangeMapService.gI().exitMap(player.minipet);
                }
                ChangeMapService.gI().exitMap(player.pet);
            }
        }
        PlayerDAO.updatePlayer(player);
    }

    public void kickSession(MySession session) {
        if (session != null) {
            this.remove(session);
            session.disconnect();
        }
    }

    public Player getPlayer(long playerId) {
        return this.players_id.get(playerId);
    }

    public Player getPlayerByUser(int userId) {
        return this.players_userId.get(userId);
    }

    public Player getPlayerByUserName(String username) {
        for (Player player : players) {
            if (player.getSession() != null && player.getSession().uu.equals(username)) {
                return player;
            }
        }
        return null;
    }

    public Player getPlayerByID(int playerId) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player != null && player.id == playerId) {
                return player;
            }
        }
        return null;
    }

    public void clear() {
        if (!bots.isEmpty()) {
            players.remove(bots.get(0));
            remove(bots.get(0));
            bots.remove(0);
        }
    }
//    String[] playerNamesArray = players_name.keySet().toArray(new String[0]);
//    int randomIndex = Util.nextInt(playerNamesArray.length);

//    public void createBot(MySession s) {
//        String[] name1 = {"kkr", "kaka", "vgt", "hup", "goku", "pham", "nay", "bu"};
//        String[] name2 = {"ot", "bi", "cu", "hoa", "chi", "w", "nha", "ze", "bo"};
//        String[] name3 = {"con", "ga", "hay", "so", "pro", "vip", "m", "dep"};
//        String[] name4 = {"a", "b", "h", "g", "i", "f", "m", "d"};
//        Player pl = new Player();
//        Player temp = Client.gI().getPlayerByUser(1);
////          GodGK.loadById(1271491);
//
//        pl.setSession(s);
//        s.userId = id;
//        System.out.println("Tạo Boss:" + "[" + id + "]");
//        pl.id = id;
//        id++;
//        pl.name = name1[Util.nextInt(name1.length)] + name2[Util.nextInt(name2.length)] + name3[Util.nextInt(name3.length)] + name4[Util.nextInt(name4.length)];
//        pl.gender = (byte) Util.nextInt(0, 2);
//        pl.isBot = true;
//        pl.isBoss = false;
//        pl.isPet = false;
//        pl.nPoint.power = Util.nextInt(20000000, 2000000000);
//        pl.nPoint.power *= Util.nextInt(1, 40);
//        pl.nPoint.hpg = 100000;
//        pl.nPoint.hpMax = Util.nextInt(2000, 50000000);
//        pl.nPoint.hp = pl.nPoint.hpMax / 2;
//        pl.nPoint.mpMax = Util.nextInt(2000, 5000000);
//        pl.nPoint.dame = Util.nextInt(2000, 20000);
//        pl.nPoint.stamina = 32000;
//        pl.itemTime.isUseTDLT = true;
//        pl.typePk = ConstPlayer.NON_PK;
//        //skill
//        int[] skillsArr = pl.gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22}
//                : pl.gender == 1 ? new int[]{2, 3, 7, 11, 17, 18}
//                : new int[]{4, 5, 8, 13, 14, 21, 23};
//        for (int j = 0; j < skillsArr.length; j++) {
//            Skill skill = SkillUtil.createSkill(skillsArr[j], 7);
//            pl.playerSkill.skills.add(skill);
//        }
//        pl.inventory = new Inventory();
//        for (int i = 0; i < 11; i++) {
//            pl.inventory.itemsBody.add(ItemService.gI().createItemNull());
//        }
//        pl.inventory.gold = 2000000000;
//        pl.inventory.itemsBody.set(5, Manager.CT.get(Util.nextInt(0, Manager.CT.size() - 1)));
//        pl.location.y = 400;
//        Zone z = MapService.gI().getMapCanJoin(pl, Util.nextInt(0, 110), Util.nextInt(0, 3));
//        while (z != null && !z.mobs.isEmpty()) {
//            z = MapService.gI().getMapCanJoin(pl, Util.nextInt(0, 110), Util.nextInt(0, 3));
//        }
//        pl.zone = MapService.gI().getMapCanJoin(pl, Util.nextInt(0, 110), Util.nextInt(0, 3));
//        if (pl.zone == null) {
//            return;
//        }
//        if (pl.zone.map == null) {
//            return;
//        }
//        pl.location.x = Util.nextInt(20, pl.zone.map.mapWidth - 20);//temp.location.x + Util.nextInt(-400,400);
//        pl.zone.addPlayer(pl);
//        pl.zone.load_Me_To_Another(pl);
//        Client.gI().put(pl);
//    }
//            rs.close();
//            ps.close();
//            con.close();
//        }
//
//    }
//
//    public Session getSession(MySession session) {
//        synchronized (sessions) {
//            for (MySession se : sessions) {
//                if (se != session && se.userId == session.userId) {
//                    return se;
//                }
//            }
//        }
//        return null;
//    }
    public Player getPlayer(String name) {
        return this.players_name.get(name);
    }

    public Session getSession(int sessionId) {
        return this.sessions_id.get(sessionId);
    }

    public void close() {
        Logger.error("BEGIN KICK OUT SESSION.............................." + players.size() + "\n");
        while (!GirlkunSessionManager.gI().getSessions().isEmpty()) {
            Logger.error("LEFT PLAYER: " + this.players.size() + ".........................\n");
            this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(0));
        }
        while (!players.isEmpty()) {
            this.kickSession((MySession) players.remove(0).getSession());
        }
        Logger.error("...........................................SUCCESSFUL\n");
    }

//    public void cloneMySessionNotConnect() {
//        Logger.error("START KICK SESSION DDOS\n");
//        Logger.error("COUNT: " + GirlkunSessionManager.gI().getSessions().size());
//        if (!GirlkunSessionManager.gI().getSessions().isEmpty()) {
//            for (int j = 0; j < GirlkunSessionManager.gI().getSessions().size(); j++) {
//                MySession m = (MySession) GirlkunSessionManager.gI().getSessions().get(j);
//                if (m != null && m.player == null) {
//                    this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(j));
//                }
//            }
//        }
//        Logger.error("SUCCESSFUL\n");
//    }
    public void cloneMySessionNotConnect() {
        Logger.error("---------START KICK SESSION DDOS---------");
        Logger.error("COUNT: " + GirlkunSessionManager.gI().getSessions().size());

        if (!GirlkunSessionManager.gI().getSessions().isEmpty()) {
            var iterator = GirlkunSessionManager.gI().getSessions().iterator();
            while (iterator.hasNext()) {
                MySession m = (MySession) iterator.next();
                if (m != null && m.player == null) {
                    iterator.remove(); // Loại bỏ session từ danh sách
                    this.kickSession(m); // Gọi phương thức kickSession
                }
            }
        }

        Logger.error("---------SUCCESSFUL---------\n");
    }

    private void update() {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (session.timeWait > 0) {
                    session.timeWait--;
                    if (session.timeWait == 0) {
                        kickSession((MySession) session);
                    }
                }
            }
        }
    }
//
//    @Override
//    public void run() {
//        while (ServerManager.isRunning) {
//            try {
//                long st = System.currentTimeMillis();
//                update();
//                Thread.sleep(800 - (System.currentTimeMillis() - st));
//            } catch (Exception e) {
//            }
//        }
//    }
//    private void update() {
//        for (ISession s : GirlkunSessionManager.gI().getSessions()) {
//            MySession session = (MySession) s;
//            if (session.timeWait > 0) {
//                session.timeWait--;
//                if (session.timeWait == 0) {
//                    kickSession(session);
//                }
//            }
//        }
//    }
//public void update() {
//        for (int i = 0; i < MySession.Session.size(); i++) {
//            try {
//                MySession session = MySession.Session.get(i);
////                System.out.println(session.getIP() + " " + session.timeWait);
//                if (session.timeWait > 0) {
//                    session.timeWait--;
//                    if (session.timeWait == 0) {
//                        if (session.player == null) {
//                            kickSession(session);
//                            MySession.Session.remove(session);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                continue;
//            }
//        }
//    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                update();
                Thread.sleep(800 - (System.currentTimeMillis() - st));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void show(Player player) {
        String txt = "";
        txt += "|2|sessions: " + GirlkunSessionManager.gI().getSessions().size() + "\n";
        txt += "|3|players_id: " + players_id.size() + "\n";
        txt += "|4|players_userId: " + players_userId.size() + "\n";
        txt += "|5|players_name: " + players_name.size() + "\n";
        txt += "|6|players: " + players.size() + "\n";
        Service.gI().sendThongBao(player, txt);
    }
}
