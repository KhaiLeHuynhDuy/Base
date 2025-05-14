package nro.models.map;

import nro.consts.ConstPlayer;
import nro.consts.ConstTask;
//import nro.database.GirlkunDB;
import nro.models.boss.Boss;
import nro.models.boss.dhvt.BossDHVT;
import nro.models.boss.list_boss.SuKienTrungThu.NguyetThan;
import nro.models.boss.list_boss.SuKienTrungThu.NhatThan;
import nro.models.item.Item;
import nro.models.mob.Mob;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
//import nro.models.player.MiniPet;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.player.Yajiro;
import nro.network.io.Message;
import nro.services.ItemMapService;
import nro.services.ItemService;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.InventoryServiceNew;
import nro.services.NgocRongNamecService;
import nro.models.player.Referee;
import nro.utils.FileIO;
import nro.utils.Logger;
import nro.utils.Util;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import nro.consts.ConstItem;
import nro.consts.ConstTranhNgocNamek;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.phuban.DragonNamecWar.TranhNgocService;
import nro.models.player.MiniPet;

public class Zone {

    public static final byte PLAYERS_TIEU_CHUAN_TRONG_MAP = 10;

    public int countItemAppeaerd = 0;
    public byte effDragon = -1;
    public Map map;
    public int zoneId;
    public int maxPlayer;
    public int hours;
    private final List<Player> humanoids; //player, boss, pet
    private final List<Player> notBosses; //player, pet
    private final List<Player> players; //player
    private final List<Player> bosses; //boss
    private final List<Player> pets; //pet
    private final List<Player> minipets; //minipet
    public final List<Mob> mobs;
    public final List<ItemMap> items;

    public long lastTimeDropBlackBall;
    public boolean finishBlackBallWar;
    public long lastTimeDropGiaidauBall;
    public boolean finishGiaidauBallWar;
    public boolean finishMapMaBu;
    public boolean finishMap22h;
    public boolean finishmabu2h;
    public List<TrapMap> trapMaps;

    //tranh ngọc namek
    public int pointFide;
    public int pointCadic;
    private final List<Player> playersFide;
    private final List<Player> playersCadic;
    public long lastTimeStartTranhNgoc;
    public boolean startZoneTranhNgoc;
    public long lastTimeDropBall;

    public boolean isFullPlayer() {
        return this.players.size() >= this.maxPlayer;
    }
    public boolean isTrungUyTrangAlive;
    public boolean isbulon13Alive;
    public boolean isbulon14Alive;

    private void udMob() {
        for (Mob mob : this.mobs) {
            mob.update();
        }
    }

    public int getNumOfBosses() {
        return this.bosses.size();
    }

    @Setter
    @Getter
    private Player referee;
    private Player Yajiro;
    @Setter
    @Getter
    private Player TestDame;

    private void udPlayer() {
        for (int i = this.notBosses.size() - 1; i >= 0; i--) {
            Player pl = this.notBosses.get(i);
            if (!pl.isPet && !pl.isNewPet && !pl.isMiniPet) {
                this.notBosses.get(i).update();
            }
        }
    }

    public List<Player> getPlayersCadic() {
        return this.playersCadic;
    }

    public List<Player> getPlayersFide() {
        return this.playersFide;
    }

    public void addPlayersCadic(Player player) {
        synchronized (playersCadic) {
            if (!this.playersCadic.contains(player)) {
                this.playersCadic.add(player);
            }
        }
    }

    public void addPlayersFide(Player player) {
        synchronized (playersFide) {
            if (!this.playersFide.contains(player)) {
                this.playersFide.add(player);
            }
        }
    }

    public void removePlayersCadic(Player player) {
        synchronized (playersCadic) {
            if (this.playersCadic.contains(player)) {
                this.playersCadic.remove(player);
            }
        }
    }

    public void removePlayersFide(Player player) {
        synchronized (playersFide) {
            if (this.playersFide.contains(player)) {
                this.playersFide.remove(player);
            }
        }
    }

    private void udItem() {
        synchronized (items) {
            for (ItemMap item : items) {
                item.update();
            }
        }
    }

    public void update() {
        udMob();
        udPlayer();
        udItem();
        updateZoneTranhNgoc();
    }

    public Zone(Map map, int zoneId, int maxPlayer) {
        this.map = map;
        this.zoneId = zoneId;
        this.maxPlayer = maxPlayer;
        this.humanoids = new ArrayList<>();
        this.notBosses = new ArrayList<>();
        this.players = new ArrayList<>();
        this.bosses = new ArrayList<>();
        this.pets = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.items = new ArrayList<>();
        this.trapMaps = new ArrayList<>();
        this.minipets = new ArrayList<>();
        this.playersFide = new ArrayList<>();
        this.playersCadic = new ArrayList<>();
    }

    public int getNumOfPlayers() {
        return this.players.size();
    }

    public boolean isBossCanJoin(Boss boss) {
        for (Player b : this.bosses) {
            if (b.id == boss.id) {
                return false;
            }
        }
        return true;
    }

    public List<Player> getNotBosses() {
        return this.notBosses;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Player> getHumanoids() {
        return this.humanoids;
    }

    public List<Player> getBosses() {
        return this.bosses;
    }

    public void addPlayer(Player player) {
        if (player != null) {
            synchronized (humanoids) {
                if (!this.humanoids.contains(player)) {
                    this.humanoids.add(player);
                }
            }
            if (!player.isBoss) {
                synchronized (notBosses) {
                    if (!this.notBosses.contains(player)) {
                        this.notBosses.add(player);
                    }
                }
                if (player.isPet) {
                    synchronized (pets) {
                        this.pets.add(player);
                    }
                } else if (player.isMiniPet) {
                    synchronized (minipets) {
                        this.minipets.add(player);
                    }
                } else {
                    synchronized (players) {
                        if (!this.players.contains(player)) {
                            this.players.add(player);
                        }
                    }
                }
            } else {
                synchronized (bosses) {
                    this.bosses.add(player);
                }
            }

        }
    }
//    public void addPlayer(Player player) {
//        if (player != null) {
//            if (!this.humanoids.contains(player)) {
//                this.humanoids.add(player);
//            }
//            if (!player.isBoss && !this.notBosses.contains(player)) {
//                this.notBosses.add(player);
//            }
//            if (!player.isBoss && !player.isNewPet && !player.isMiniPet && !player.isPet && !this.players.contains(player)) {
//                this.players.add(player);
//            }
//            if (player.isBoss) {
//                this.bosses.add(player);
//            }
//            if (player.isPet || player.isNewPet || player.isMiniPet) {
//                this.pets.add(player);
//            }
//        }
//    }

//    public void removePlayer(Player player) {
//        this.humanoids.remove(player);
//        this.notBosses.remove(player);
//        this.players.remove(player);
//        this.bosses.remove(player);
//        this.pets.remove(player);
//    }
    public void removePlayer(Player player) {
        if (player != null) {
            this.humanoids.remove(player);
            if (!player.isBoss) {
                synchronized (notBosses) {
                    this.notBosses.remove(player);
                }
                if (player.isPet) {
                    synchronized (pets) {
                        this.pets.remove(player);
                    }
                } else if (player.isMiniPet) {
                    synchronized (minipets) {
                        this.minipets.remove(player);
                    }
                } else {
                    synchronized (players) {
                        this.players.remove(player);
                    }
                }
            } else {
                synchronized (bosses) {
                    this.bosses.remove(player);
                }

            }
        }

    }
//    // EMti

    public ItemMap getItemMapByItemMapId(int itemId) {
        if (this.items != null) {
            for (ItemMap item : this.items) {
                if (item != null && item.itemMapId == itemId) {
                    return item;
                }
            }
        }
        return null;
    }

    public ItemMap getItemMapByTempId(int tempId) {
        if (this.items != null) {

            for (ItemMap item : this.items) {
                if (item != null && item.itemTemplate.id == tempId) {
                    return item;
                }
            }
        }
        return null;
    }

    public List<ItemMap> getItemMapsForPlayer(Player player) {
        List<ItemMap> list = new ArrayList<>();
        for (ItemMap item : items) {
//            if (item.itemTemplate == null) {
//                Service.gI().sendThongBao(player, "Có lỗi xảy ra với vật phẩm");
//            }
            if (item != null && item.itemTemplate != null) {
                if (item.itemTemplate.id == 78) {
                    if (TaskService.gI().getIdTask(player) != ConstTask.TASK_3_1) {
                        continue;
                    }
                }
                if (item.itemTemplate.id == 74) {
                    if (TaskService.gI().getIdTask(player) < ConstTask.TASK_3_0) {
                        continue;
                    }
                }
                list.add(item);
            }
        }
        return list;
    }

    public Player getPlayerInMap(long idPlayer) {
        for (Player pl : humanoids) {
            if (pl.id == idPlayer) {
                return pl;
            }
        }
        return null;
    }
//    public Player getPlayerInMap(long idPlayer) {
//        return humanoids.stream()
//                .filter(pl -> pl.id == idPlayer)
//                .findFirst()
//                .orElse(null);
//    }

    public void pickItem(Player player, int itemMapId) {
        ItemMap itemMap = getItemMapByItemMapId(itemMapId);
        if (itemMap != null) {
            if (itemMap.playerId == player.id || itemMap.playerId == -1) {
                Item item = ItemService.gI().createItemFromItemMap(itemMap);
                if (item.template.type == 22) {
                    return;
                }
                boolean picked = true;
                if (!ItemMapService.gI().isNamecBall(item.template.id)) {
                    picked = InventoryServiceNew.gI().addItemBag(player, item);
                }
                if (itemMap.isNamecBallTranhDoat) {
                    TranhNgocService.getInstance().pickBall(player, itemMap);
                    return;
                }
                if (picked) {
                    int itemType = item.template.type;
                    Message msg;
                    try {
                        msg = new Message(-20);
                        msg.writer().writeShort(itemMapId);
                        switch (itemType) {
                            case 9:
                            case 10:
                            case 34:
                                msg.writer().writeUTF("");
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                break;
                            default:
                                switch (item.template.id) {
                                    case 362:
                                        Service.gI().sendThongBao(player, "Chỉ là cục đá thôi, nhặt làm gì?");
                                        break;
                                    case 353:
                                    case 354:
                                    case 355:
                                    case 356:
                                    case 357:
                                    case 358:
                                    case 359:
                                        if (System.currentTimeMillis() >= NgocRongNamecService.gI().tOpenNrNamec) {
                                            if (player.idNRNM == -1) {
                                                PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                                                player.idNRNM = item.template.id;
                                                NgocRongNamecService.gI().mapNrNamec[item.template.id - 353] = player.zone.map.mapId;
                                                NgocRongNamecService.gI().nameNrNamec[item.template.id - 353] = player.zone.map.mapName;
                                                NgocRongNamecService.gI().zoneNrNamec[item.template.id - 353] = (byte) player.zone.zoneId;
                                                NgocRongNamecService.gI().pNrNamec[item.template.id - 353] = player.name;
                                                NgocRongNamecService.gI().idpNrNamec[item.template.id - 353] = (int) player.id;
                                                player.lastTimePickNRNM = System.currentTimeMillis();
                                                Service.gI().sendFlagBag(player);
                                                msg.writer().writeUTF("Bạn đã nhặt được " + item.template.name);
                                                msg.writer().writeShort(item.quantity);
                                                player.sendMessage(msg);
                                                msg.cleanup();
                                            } else {
                                                Service.gI().sendThongBao(player, "Bạn đã mang ngọc rồng trên người");
                                            }
                                        } else {
                                            Service.gI().sendThongBao(player, "Chỉ là cục đá thôi, nhặt làm gì?");
                                        }
                                        break;
                                    case 73:
                                        msg.writer().writeUTF("");
                                        msg.writer().writeShort(item.quantity);
                                        player.sendMessage(msg);
                                        TaskService.gI().checkDoneTaskPickItem(player, itemMap);
                                        msg.cleanup();
                                        break;
                                    case 74:
                                        msg.writer().writeUTF("Bạn mới vừa ăn " + item.template.name);
                                        break;
                                    case 78:
                                        msg.writer().writeUTF("Wow, một cậu bé dễ thương!");
                                        msg.writer().writeShort(item.quantity);
                                        player.sendMessage(msg);
                                        TaskService.gI().checkDoneTaskPickItem(player, itemMap);
                                        msg.cleanup();
                                        break;
                                    case 380:
                                    case 15:
                                    case 992:
                                    case 865:
                                    case 874:
                                    case 725:
                                        TaskService.gI().checkDoneTaskPickItem(player, itemMap);
                                        break;
                                    default:
                                        if (item.template.type >= 0 && item.template.type < 5) {
                                            msg.writer().writeUTF(item.template.name + " ngon ngon...");
                                        } else {
                                            msg.writer().writeUTF("Bạn mới nhặt được " + item.template.name);
                                        }
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        break;
                                }

                        }
                        msg.writer().writeShort(item.quantity);
                        player.sendMessage(msg);
                        msg.cleanup();
                        Service.gI().sendToAntherMePickItem(player, itemMapId);
                        if (!(this.map.mapId >= 21 && this.map.mapId <= 23
                                && itemMap.itemTemplate != null
                                && itemMap.itemTemplate.id == 74
                                || this.map.mapId >= 42 && this.map.mapId <= 44
                                && itemMap.itemTemplate.id == 78)) {
                            removeItemMap(itemMap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.logException(Zone.class, e);
                    }
                } else {
                    if (!ItemMapService.gI().isBlackBall(item.template.id)) {
                        String text = "Hành trang không còn chỗ trống";
                        Service.gI().sendThongBao(player, text);
                    }
                    if (!ItemMapService.gI().isGiaidauBall(item.template.id)) {
                        String text = "Hành trang không còn chỗ trống";
                        Service.gI().sendThongBao(player, text);
                    }
                }
            } else {
                Service.gI().sendThongBao(player, "Không thể nhặt vật phẩm của người khác");
            }
        }
        TaskService.gI().checkDoneSideTaskPickItem(player, itemMap);
    }

    public void addItem(ItemMap itemMap) {
        if (itemMap != null && !items.contains(itemMap)) {
            items.add(0, itemMap);
        }
    }

    public void removeItemMap(ItemMap itemMap) {
        this.items.remove(itemMap);
    }

    private void updateZoneTranhNgoc() {
        if (!TranhNgoc.gI().isTimeStartWar() && startZoneTranhNgoc) {
            startZoneTranhNgoc = false;
            playersCadic.clear();
            playersFide.clear();
            pointCadic = 0;
            pointFide = 0;
            return;
        }
        if (startZoneTranhNgoc) {
            if (Util.canDoWithTime(this.lastTimeStartTranhNgoc, ConstTranhNgocNamek.TIME)) {
                startZoneTranhNgoc = false;
                if (pointCadic > pointFide) {
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.WIN, false);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.LOSE, true);
                    TranhNgocService.getInstance().givePrice(getPlayersCadic(), ConstTranhNgocNamek.WIN, pointCadic);
                    TranhNgocService.getInstance().givePrice(getPlayersFide(), ConstTranhNgocNamek.LOSE, pointFide);
                } else if (pointFide > pointCadic) {
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.WIN, true);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.LOSE, false);
                    TranhNgocService.getInstance().givePrice(getPlayersFide(), ConstTranhNgocNamek.WIN, pointFide);
                    TranhNgocService.getInstance().givePrice(getPlayersCadic(), ConstTranhNgocNamek.LOSE, pointCadic);
                } else {
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.DRAW, true);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.DRAW, false);
                }
                items.clear();
                playersCadic.clear();
                playersFide.clear();
                pointCadic = 0;
                pointFide = 0;
            } else {
                if (pointCadic == 7) {
                    startZoneTranhNgoc = false;
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.WIN, false);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.LOSE, true);
                    TranhNgocService.getInstance().givePrice(getPlayersCadic(), ConstTranhNgocNamek.WIN, pointCadic);
                    TranhNgocService.getInstance().givePrice(getPlayersFide(), ConstTranhNgocNamek.LOSE, pointFide);
                    items.clear();
                    playersCadic.clear();
                    playersFide.clear();
                    pointCadic = 0;
                    pointFide = 0;
                } else if (pointFide == 7) {
                    startZoneTranhNgoc = false;
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.WIN, true);
                    TranhNgocService.getInstance().sendEndPhoBan(this, ConstTranhNgocNamek.LOSE, false);
                    TranhNgocService.getInstance().givePrice(getPlayersFide(), ConstTranhNgocNamek.WIN, pointFide);
                    TranhNgocService.getInstance().givePrice(getPlayersCadic(), ConstTranhNgocNamek.LOSE, pointCadic);
                    items.clear();
                    playersCadic.clear();
                    playersFide.clear();
                    pointCadic = 0;
                    pointFide = 0;
                }
            }
            if (Util.canDoWithTime(lastTimeDropBall, ConstTranhNgocNamek.LAST_TIME_DROP_BALL)) {
                int id = Util.nextInt(ConstItem.NGOC_RONG_NAMEK_1_SAO, ConstItem.NGOC_RONG_NAMEK_7_SAO);
                ItemMap it = this.getItemMapByTempId(id);
                if (it == null && !findPlayerHaveBallTranhDoat(id)) {
                    lastTimeDropBall = System.currentTimeMillis();
                    int x = Util.nextInt(20, map.mapWidth);
                    int y = map.yPhysicInTop(x, Util.nextInt(20, map.mapHeight - 200));
                    ItemMap itemMap = new ItemMap(this, id, 1, x, y, -1);
                    itemMap.isNamecBallTranhDoat = true;
                    Service.gI().dropItemMap(this, itemMap);
                }
            }
        }
    }

    public boolean findPlayerHaveBallTranhDoat(int id) {
        for (Player pl : this.getPlayers()) {
            if (pl != null && pl.isHoldNamecBallTranhDoat && pl.tempIdNamecBallHoldTranhDoat == id) {
                return true;
            }
        }
        return false;
    }

    public Player getRandomPlayerInMap() {
        if (!this.notBosses.isEmpty()) {
            return this.notBosses.get(Util.nextInt(0, this.notBosses.size() - 1));
        } else {
            return null;
        }
    }

    public ArrayList<Player> getPlayerNotBlueFlag() {
        ArrayList<Player> pls = new ArrayList<>();
        for (Player pl : getPlayers()) {
            if (pl != null && !pl.isDie() && pl.cFlag != 0 && pl.cFlag != NhatThan.flag) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public ArrayList<Player> getPlayerNotRedFlag() {
        ArrayList<Player> pls = new ArrayList<>();
        for (Player pl : getPlayers()) {
            if (pl != null && !pl.isDie() && pl.cFlag != 0 && pl.cFlag != NguyetThan.flag) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public ArrayList<Player> getBossNotBlueFlag() {
        ArrayList<Player> pls = new ArrayList<>();
        for (Player pl : getBosses()) {
            if (pl != null && !pl.isDie() && pl.cFlag != 0 && pl.cFlag != NhatThan.flag) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public ArrayList<Player> getBossNotRedFlag() {
        ArrayList<Player> pls = new ArrayList<>();
        for (Player pl : getBosses()) {
            if (pl != null && !pl.isDie() && pl.cFlag != 0 && pl.cFlag != NguyetThan.flag) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public Player getPlayerWithFlagNotIsRed(Player player) {
        for (Player pl : humanoids) {
            if (pl.equals(player)) {
                continue;
            }
//            if(pl != null && pl.cFlag != 0 && pl.cFlag != NguyetThan.flag){
//               // Logger.error("NotIsRed " + pl.name +"\n");
//                return pl;
//            }
            ArrayList<Player> players1 = new ArrayList<>();
            ArrayList<Player> boss = getBossNotRedFlag();
            ArrayList<Player> players = getPlayerNotRedFlag();
            if (players.size() > 0) {
                players1 = players;
            } else {
                players1 = boss;
            }
            if (players1.size() > 0) {
                return players1.get(Util.nextInt(0, players1.size() - 1));
            }
        }
        return null;
    }

    public Player getPlayerWithFlagNotIsBlue(Player player) {
        for (Player pl : humanoids) {
            if (pl.equals(player)) {
                continue;
            }
//            if(pl != null && pl.cFlag != 0 && pl.cFlag != NhatThan.flag){
//              //  Logger.error("NotIsBlue " + pl.name+"\n" );
//                return pl;
//            }

            ArrayList<Player> players1 = new ArrayList<>();
            ArrayList<Player> boss = getBossNotBlueFlag();
            ArrayList<Player> players = getPlayerNotBlueFlag();
            if (players.size() > 0) {
                players1 = players;
            } else {
                players1 = boss;
            }
            if (players1.size() > 0) {
                return players1.get(Util.nextInt(0, players1.size() - 1));
            }
        }
        return null;
    }
//    private boolean hasLoggedError = false; // Biến cờ để đánh dấu đã gửi log lỗi hay chưa

//    @Emti
    public void load_Me_To_Another(Player player) {
        try {
            if (player.zone != null) {
                if (this.map.isMapOffline) {
                    if (player.isPet && this.equals(((Pet) player).master.zone)) {
                        infoPlayer(((Pet) player).master, player);
                    }
                } else {
                    for (int i = 0; i < players.size(); i++) {
                        Player pl = players.get(i);
                        if (!player.equals(pl)) {
                            infoPlayer(pl, player);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }
//    public void load_Me_To_Another(Player player) { //load thông tin người chơi cho những người chơi khác
//        try {
//            if (player.zone != null) {
//                if (this.map.isMapOffline) {
//                    if (player.isPet && this.equals(((Pet) player).master.zone)) {
//                        infoPlayer(((Pet) player).master, player);
//                    }
//                } else {
//                    synchronized (this.players) {
//                        for (int i = 0; i < players.size(); i++) {
//                            Player pl = players.get(i);
//                            if (!player.equals(pl)) {
//                                infoPlayer(pl, player);
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Logger.logException(MapService.class, e);
//        }
//    }

//    public void load_Another_To_Me(Player player) { //load những player trong map và gửi cho player vào map
//        try {
//            if (this.map.isMapOffline) {
//                for (int i = this.humanoids.size() - 1; i >= 0; i--) {
//                    Player pl = this.humanoids.get(i);
//                    if (pl != null) {
//                        if (pl.id == -player.id) {
//                            infoPlayer(player, pl);
//                            break;
//                        }
//                    }
//                }
//            } else {
//                for (int i = this.humanoids.size() - 1; i >= 0; i--) {
//                    Player pl = this.humanoids.get(i);
//                    if (pl != null) {
//                        if (player != pl) {
//                            infoPlayer(player, pl);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Logger.logException(MapService.class, e);
//        }
//    }
    public void load_Another_To_Me(Player player) { //load những player trong map và gửi cho player vào map
        try {
            if (this.map.isMapOffline) {
                for (Player pl : this.humanoids) {
                    if (pl.id == -player.id) {
                        infoPlayer(player, pl);
                        break;
                    }
                }
            } else {
                for (Player pl : this.humanoids) {
                    if (pl != null && !player.equals(pl)) {
                        infoPlayer(player, pl);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
    }

    public void loadBoss(Boss boss) {
        try {
            if (this.map.isMapOffline) {
                for (Player pl : this.humanoids) {
                    if (pl.id == -boss.id) {
                        infoPlayer(boss, pl);
                        break;
                    }
                }
            } else {
                for (Player pl : this.bosses) {
                    if (!boss.equals(pl)) {
                        infoPlayer(boss, pl);
                        infoPlayer(pl, boss);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(MapService.class, e);
        }
    }

    private void infoPlayer(Player plReceive, Player plInfo) {
        Message msg;
        try {
            msg = new Message(-5);
            msg.writer().writeInt((int) plInfo.id);
            msg.writer().writeInt(plInfo.clan != null ? plInfo.clan.id : -1);
            msg.writer().writeByte(Service.gI().getCurrLevel(plInfo));
            msg.writer().writeBoolean(false);
            msg.writer().writeByte(plInfo.typePk);
            msg.writer().writeByte(plInfo.gender);
            msg.writer().writeByte(plInfo.gender);
            msg.writer().writeShort(plInfo.getHead());
            msg.writer().writeUTF(plInfo.clan != null ? "[" + plInfo.clan.name + "]" + plInfo.name != null ? plInfo.name : "null1" : plInfo.name != null ? plInfo.name : "null2");
            if (plInfo.nPoint == null) {
//                msg.writeFix(Util.maxIntValue(100));
//                msg.writeFix(Util.maxIntValue(100));
                System.err.println(plInfo.name + " null nPoint_infoPlayer");
                return;
            }
            msg.writeFix(Util.maxIntValue(plInfo.nPoint.hp));
            msg.writeFix(Util.maxIntValue(plInfo.nPoint.hpMax));

            msg.writer().writeShort(plInfo.getBody());
            msg.writer().writeShort(plInfo.getLeg());
            msg.writer().writeByte(plInfo.getFlagBag()); //bag

            msg.writer().writeByte(-1);
            if (plInfo.location == null) {
//                msg.writeFix(Util.maxIntValue(-1));
//                msg.writeFix(Util.maxIntValue(-1));
                System.err.println(plInfo.name + " null location_infoPlayer");
                return;
            }
            msg.writer().writeShort(plInfo.location.x);
            msg.writer().writeShort(plInfo.location.y);

            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(0);
            msg.writer().writeByte(plInfo.iDMark.getIdSpaceShip());
            msg.writer().writeByte(plInfo.effectSkill.isMonkey ? 1 : 0);
            msg.writer().writeShort(plInfo.getMount());
            msg.writer().writeByte(plInfo.cFlag);
            msg.writer().writeByte(0);
            msg.writer().writeShort(plInfo.getAura()); //idauraeff
            msg.writer().writeByte(plInfo.getEffFront()); //seteff
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Service.gI().sendFlagPlayerToMe(plReceive, plInfo);
        if (!plInfo.isBoss && !plInfo.isPet && !plInfo.isNewPet && !plInfo.isMiniPet && !(plInfo instanceof BossDHVT) && !(plInfo instanceof Referee) & !(plInfo instanceof Yajiro)) {
            Service.gI().sendPetFollow(plReceive, (plReceive.getLinhThu()));
        }
        try {
            if (plInfo.isDie()) {
                msg = new Message(-8);
                msg.writer().writeInt((int) plInfo.id);
                msg.writer().writeByte(0);
                if (plInfo.location == null) {
//                msg.writeFix(Util.maxIntValue(-1));
//                msg.writeFix(Util.maxIntValue(-1));
                    System.err.println(plInfo.name + " null2 location_infoPlayer");
                    return;
                }
                msg.writer().writeShort(plInfo.location.x);
                msg.writer().writeShort(plInfo.location.y);
                plReceive.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mapInfo(Player pl) {
        Message msg;
        try {
            msg = new Message(-24);
            msg.writer().writeByte(this.map.mapId);
            msg.writer().writeByte(this.map.planetId);
            msg.writer().writeByte(this.map.tileId);
            msg.writer().writeByte(this.map.bgId);
            msg.writer().writeByte(this.map.type);
            msg.writer().writeUTF(this.map.mapName);
            msg.writer().writeByte(this.zoneId);

            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);

            // waypoint
            List<WayPoint> wayPoints = this.map.wayPoints;
            msg.writer().writeByte(wayPoints.size());
            for (WayPoint wp : wayPoints) {
                msg.writer().writeShort(wp.minX);
                msg.writer().writeShort(wp.minY);
                msg.writer().writeShort(wp.maxX);
                msg.writer().writeShort(wp.maxY);
                msg.writer().writeBoolean(wp.isEnter);
                msg.writer().writeBoolean(wp.isOffline);
                msg.writer().writeUTF(wp.name);
            }
            // mob
            List<Mob> mobs = this.mobs;
            msg.writer().writeByte(mobs.size());
            for (Mob mob : mobs) {
                msg.writer().writeBoolean(false); //is disable
                msg.writer().writeBoolean(false); //is dont move
                msg.writer().writeBoolean(false); //is fire
                msg.writer().writeBoolean(false); //is ice
                msg.writer().writeBoolean(false); //is wind
                msg.writer().writeByte(mob.tempId);
                msg.writer().writeByte(0);
                msg.writeFix(Util.maxIntValue(mob.point.gethp())); // long
                msg.writer().writeByte(mob.level);
                msg.writeFix(Util.maxIntValue(mob.point.getHpFull()));  // long
                msg.writer().writeShort(mob.location.x);
                msg.writer().writeShort(mob.location.y);
                msg.writer().writeByte(mob.status);
                msg.writer().writeByte(mob.lvMob);
                msg.writer().writeBoolean(false);
            }

            msg.writer().writeByte(0);

            // npc
            List<Npc> npcs = NpcManager.getNpcsByMapPlayer(pl);
            msg.writer().writeByte(npcs.size());
            for (Npc npc : npcs) {
                msg.writer().writeByte(npc.status);
                msg.writer().writeShort(npc.cx);
                msg.writer().writeShort(npc.cy);
                msg.writer().writeByte(npc.tempId);
                msg.writer().writeShort(npc.avartar);
            }

            // item
            List<ItemMap> itemsMap = this.getItemMapsForPlayer(pl);

            try {
                msg.writer().writeByte(itemsMap.size());

                for (ItemMap it : itemsMap) {
                    msg.writer().writeShort(it.itemMapId);
                    msg.writer().writeShort(it.itemTemplate.id);
                    msg.writer().writeShort(it.x);
                    msg.writer().writeShort(it.y);
                    msg.writer().writeInt((int) it.playerId);
                }
            } catch (IOException e) {
                System.err.println("Lỗi IOException: " + e.getMessage());
                e.printStackTrace();
            }
            try {
                byte[] bgItem = FileIO.readFile("data/girlkun/map/item_bg_map_data/" + this.map.mapId);
                if (bgItem != null) {
                    msg.writer().write(bgItem);
                } else {
                    msg.writer().writeShort(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
//                msg.writer().writeShort(0);
            }

            // eff item
//                msg.writer().writeShort(0);
//            try {
//                byte[] effItem = FileIO.readFile("data/girlkun/map/eff_map/" + this.map.mapId);
//                msg.writer().write(effItem);
//            } catch (Exception e) {
//                msg.writer().writeShort(0);
//            }
            List<EffectMap> em = this.map.effMap;
            msg.writer().writeShort(em.size());
            for (EffectMap e : em) {
                msg.writer().writeUTF(e.getKey());
                msg.writer().writeUTF(e.getValue());
            }

            msg.writer().writeByte(this.map.bgType);
            msg.writer().writeByte(pl.iDMark.getIdSpaceShip());
            msg.writer().writeByte(0);
            pl.sendMessage(msg);

            msg.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Service.class, e);
        }
    }

    public Mob getRandomMobInMap() {
        if (!this.mobs.isEmpty()) {
            return this.mobs.get(Util.nextInt(this.mobs.size()));
        }
        return null;
    }

    public boolean isKhongCoTrongTaiTrongKhu() {
        boolean cccccc = true;
        for (Player pl : players) {
            if (pl.name.compareTo("Trọng Tài") == 0) {
                cccccc = false;
                break;
            }
            if (pl.zone.map.mapId >= 21 && pl.zone.map.mapId <= 23) {
                cccccc = false;
            }
        }
        return cccccc;
    }

    public TrapMap isInTrap(Player player) {
        for (TrapMap trap : this.trapMaps) {
            if (player.location.x >= trap.x && player.location.x <= trap.x + trap.w
                    && player.location.y >= trap.y && player.location.y <= trap.y + trap.h) {
                return trap;
            }
        }
        return null;
    }
}
