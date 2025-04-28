package nro.services;

import nro.consts.ConstMap;
import nro.models.map.Map;
import nro.models.map.WayPoint;
import nro.models.map.Zone;
import nro.models.mob.Mob;
import nro.models.player.Player;
import nro.server.Manager;
import nro.network.io.Message;
import nro.services.func.ChangeMapService;
import nro.utils.Logger;
import nro.utils.Util;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MapService {

    private static MapService i;

    public static MapService gI() {
        if (i == null) {
            i = new MapService();
        }
        return i;
    }

    public WayPoint getWaypointPlayerIn(Player player) {
        for (WayPoint wp : player.zone.map.wayPoints) {
            if (player.location.x >= wp.minX && player.location.x <= wp.maxX && player.location.y >= wp.minY && player.location.y <= wp.maxY) {
                return wp;
            }
        }
        return null;
    }

    /**
     * @param tileTypeFocus tile type: top, bot, left, right...
     * @return [tileMapId][tileType]
     */
    public int[][] readTileIndexTileType(int tileTypeFocus) {
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

    //tilemap for paint
    public int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_map_data/" + mapId));
            dis.readByte();
            int w = dis.readByte();
            int h = dis.readByte();
            tileMap = new int[h][w];
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j] = dis.readByte();
                }
            }
            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileMap;
    }

    public Zone getMapCanJoin(Player player, int mapId, int zoneId) {

        if (isMapOffline(mapId)) {
            return getMapById(mapId).zones.get(0);
        }
        if (this.isMapDoanhTrai(mapId)) {
            if (player.clan == null || player.clan.doanhTrai == null) {
                return null;
            }
            if (this.isMapDoanhTrai(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
                for (Player boss : player.zone.getBosses()) {
                    if (!boss.isDie()) {
                        return null;
                    }
                }
            }
            /**
             * Qua map mới thì làm mới lại mob
             */
            if (player.clan.doanhTrai.getListMap().indexOf(mapId) > player.clan.doanhTrai.getCurrentIndexMap()) {

                player.clan.doanhTrai.setCurrentIndexMap(player.clan.doanhTrai.getListMap().indexOf(mapId));
                player.clan.doanhTrai.init();

            }
            return player.clan.doanhTrai.getMapById(mapId);
        }
        if (this.isMapBanDoKhoBau(mapId)) {
            if (player.clan == null || player.clan.banDoKhoBau == null) {
                return null;
            }
            if (this.isMapBanDoKhoBau(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
                for (Player boss : player.zone.getBosses()) {
                    if (!boss.isDie()) {
                        return null;
                    }
                }
            }

            /**
             * Qua map mới thì làm mới lại mob
             */
            if (player.clan.banDoKhoBau.getListMap().indexOf(mapId) > player.clan.banDoKhoBau.getCurrentIndexMap()) {
                player.clan.banDoKhoBau.setCurrentIndexMap(player.clan.banDoKhoBau.getListMap().indexOf(mapId));
                player.clan.banDoKhoBau.init();

            }
            return player.clan.banDoKhoBau.getMapById(mapId);
        }

        if (this.isMapConDuongRanDoc(mapId)) {
            if (player.clan == null || player.clan.ConDuongRanDoc == null) {
                 Service.gI().sendThongBaoOK(player, "Không thể chuyển map này");
                return null;
            }
            if (this.isMapConDuongRanDoc(player.zone.map.mapId)) {
                for (Mob mob : player.zone.mobs) {
                    if (!mob.isDie()) {
                        return null;
                    }
                }
                for (Player boss : player.zone.getBosses()) {
                    if (!boss.isDie()) {
                        return null;
                    }
                }
                /**
             * Qua map mới thì làm mới lại mob
             */
            if (player.clan.ConDuongRanDoc.getListMap().indexOf(mapId) > player.clan.ConDuongRanDoc.getCurrentIndexMap()) {
                player.clan.ConDuongRanDoc.setCurrentIndexMap(player.clan.ConDuongRanDoc.getListMap().indexOf(mapId));
                player.clan.ConDuongRanDoc.init();

            }
            }
            return player.clan.ConDuongRanDoc.getMapById(mapId);
        }

        //**********************************************************************
        if (zoneId == -1) { //vào khu bất kỳ
            return getZone(mapId);
        } else {
            return getZoneByMapIDAndZoneID(mapId, zoneId);
        }
    }

    public List<Player> getAllPlayerInMap(int mapId) {
        Map map = getMapById(mapId);
        if (map == null) {
            return null;
        }
        List<Player> players = new ArrayList<Player>();
        for (Zone zone : map.zones) {
            players.addAll(zone.getPlayers());
        }
        return players;
    }

    public Zone getZone(int mapId) {
        Map map = getMapById(mapId);
        if (map == null) {
            return null;
        }
        int z = 0;
        int numZones = map.zones.size();
        while (z < numZones && map.zones.get(z).getNumOfPlayers() >= map.zones.get(z).maxPlayer) {
            z = Util.nextInt(0, numZones - 1);
        }
        if (z < numZones) {
            return map.zones.get(z);
        } else {
            return null; // Không tìm thấy zone có sẵn
        }
    }

    private Zone getZoneByMapIDAndZoneID(int mapId, int zoneId) {
        Zone zoneJoin = null;
        try {
            Map map = getMapById(mapId);
            if (map != null) {
                zoneJoin = map.zones.get(zoneId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(MapService.class, e);
        }
        return zoneJoin;
    }

    public Map getMapById(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map;
            }
        }
        return null;
    }

    public Map getMapForCalich() {
        int mapId = Util.nextInt(27, 29);
        return MapService.gI().getMapById(mapId);
    }

    /**
     * Trả về 1 map random cho boss
     *
     * @param mapId
     * @return
     */
    public Zone getMapWithRandZone(int mapId) {
        Map map = MapService.gI().getMapById(mapId);
        Zone zone = null;
        try {
            if (map != null) {
                zone = map.zones.get(Util.nextInt(0, map.zones.size() - 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zone;
    }

    public String getPlanetName(byte planetId) {
        switch (planetId) {
            case 0:
                return "Trái đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    /**
     * lấy danh sách map cho capsule
     *
     * @param pl
     * @return
     */
    public List<Zone> getMapCapsule(Player pl) {
        List<Zone> list = new ArrayList<>();
        if (pl.mapBeforeCapsule != null
                && pl.mapBeforeCapsule.map.mapId != 21
                && pl.mapBeforeCapsule.map.mapId != 22
                && pl.mapBeforeCapsule.map.mapId != 23
                && !isMapTuongLai(pl.mapBeforeCapsule.map.mapId)) {
            addListMapCapsule(pl, list, pl.mapBeforeCapsule);
        }
        addListMapCapsule(pl, list, getMapCanJoin(pl, 21 + pl.gender, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 47, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 45, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 0, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 7, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 14, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 5, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 20, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 13, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 24 + pl.gender, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 27, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 19, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 79, 0));
        addListMapCapsule(pl, list, getMapCanJoin(pl, 84, 0));
        return list;
    }

    public List<Zone> getMapBlackBall() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(85 + i).zones.get(0));
        }
        return list;
    }

    public List<Zone> getMapGiaidauBall() {
        List<Zone> list = new ArrayList<>();

        list.add(getMapById(145).zones.get(0));

        return list;
    }

    public List<Zone> getMapMaBu() {
        List<Zone> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(getMapById(114 + i).zones.get(0));
        }
        return list;
    }

    private void addListMapCapsule(Player pl, List<Zone> list, Zone zone) {
        for (Zone z : list) {
            if (z != null && zone != null && z.map.mapId == zone.map.mapId) {
                return;
            }
        }
        if (zone != null && pl.zone != null && pl.zone.map.mapId != zone.map.mapId) {
            list.add(zone);
        }
    }

    public void sendPlayerMove(Player player) {
        Message msg;
        try {
            msg = new Message(-7);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public boolean isMapOffline(int mapId) {
        for (Map map : Manager.MAPS) {
            if (map.mapId == mapId) {
                return map.type == ConstMap.MAP_OFFLINE;
            }
        }
        return false;
    }

    public boolean isMapBlackBallWar(int mapId) {
        return mapId >= 85 && mapId <= 91;
    }

    public boolean isMapGiaidauvutru(int mapId) {
        return mapId == 145;
    }

    public boolean isMapMaBu(int mapId) {
        return mapId >= 114 && mapId <= 120;
    }

    public boolean isMapPVP(int mapId) {
        return mapId == 112;
    }

    public boolean isMapCold(Map map) {
        int mapId = map.mapId;
        return mapId >= 105 && mapId <= 110;
    }

    public boolean isMapDoanhTrai(int mapId) {
        return mapId >= 53 && mapId <= 62;
    }

    public boolean isNguHanhSon(int mapId) {
        return mapId >= 122 && mapId <= 124;
    }

    public boolean isMapEventt(int mapId) {
        return mapId >= 170 && mapId <= 173;
    }
 public boolean isMapVS(int mapId) {
        return mapId == 129;
    }
    public boolean isMapConDuongRanDoc(int mapId) {
        return mapId == 141 || mapId == 142 || mapId == 143 || mapId == 144;
    }

    public boolean isMapKhiGas(int mapId) {
        return mapId == 149 || mapId == 148 || mapId == 147;
    }

    public boolean isMapHuyDiet(int mapId) {
        return mapId >= 200 && mapId <= 201;
    }

    public boolean isMapBanDoKhoBau(int mapId) {
        return mapId >= 135 && mapId <= 138;
    }

    public boolean isMapMaBu2h(int mapId) {
        return mapId == 127;
    }

    public boolean isMap22h(int mapId) {
        return mapId == 212;
    }

    public boolean isMapKhongCoSieuQuai(int mapId) {
        return !isMapSetKichHoat(mapId)
                && mapId != 4 && mapId != 27 && mapId != 28
                && mapId != 12 && mapId != 31 && mapId != 32
                && mapId != 18 && mapId != 35 && mapId != 36;
    }

    public boolean isMapSetKichHoat(int mapId) {
        return (mapId >= 1 && mapId <= 3)
                || (mapId == 8 || mapId == 9 || mapId == 11)
                || (mapId >= 15 && mapId <= 17);
    }

    public boolean isMapTuongLai(int mapId) {
        return (mapId >= 92 && mapId <= 94)
                || (mapId >= 96 && mapId <= 100)
                || mapId == 102 || mapId == 103;
    }

    public void goToMap(Player player, Zone zoneJoin) {
        Zone oldZone = player.zone;
        if (oldZone != null) {
            ChangeMapService.gI().exitMap(player);
            if (player.mobMe != null) {
                player.mobMe.goToMap(zoneJoin);
            }
        }
        player.zone = zoneJoin;
        player.zone.addPlayer(player);
    }
}
