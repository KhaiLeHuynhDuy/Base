package nro.models.map.MapMaBu;

import nro.models.player.Player;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.List;

public class MapMaBu2h {

    // bư 2h
    public static final byte HOUR_OPEN_MAP_MABU_2H = 14;
    public static final byte MIN_OPEN_MAP_MABU_2H = 0;
    public static final byte SECOND_OPEN_MAP_MABU_2H = 0;

    public static final byte HOUR_CLOSE_MAP_MABU_2H = 15;
    public static final byte MIN_CLOSE_MAP_MABU_2H = 0;
    public static final byte SECOND_CLOSE_MAP_MABU_2H = 0;

    public boolean isSpawnMabu = false;

    public static final int AVAILABLE = 7;

    private static MapMaBu2h i;

    public static long TIME_OPEN_MABU_2H;
    public static long TIME_CLOSE_MABU_2H;

    int day = -1;

    public static MapMaBu2h gI() {
        if (i == null) {
            i = new MapMaBu2h();
        }
        i.setTimeJoinMapMaBu2h();
        return i;
    }

    public void setTimeJoinMapMaBu2h() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                TIME_OPEN_MABU_2H = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_MABU_2H + ":" + MIN_OPEN_MAP_MABU_2H + ":" + SECOND_OPEN_MAP_MABU_2H, "dd/MM/yyyy HH:mm:ss");
                TIME_CLOSE_MABU_2H = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_MABU_2H + ":" + MIN_CLOSE_MAP_MABU_2H + ":" + SECOND_CLOSE_MAP_MABU_2H, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception ignored) {
            }
        }
    }

    private void kickOutOfMapMabu2h(Player player) {
        if (MapService.gI().isMapMaBu2h(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    private void ketthucmabu2h(Player player) {
        player.zone.finishMapMaBu = true;
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMapMabu2h(pl);
        }
    }

    public void joinMapMabu2h(Player player) {
        boolean changed = false;
        if (player.clan != null) {
            List<Player> players = player.zone.getPlayers();
            for (Player pl : players) {
                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
                    Service.gI().changeFlag(player, Util.nextInt(9, 10));
                    changed = true;
                    break;
                }
            }
        }
        if (!changed && !player.isBoss) {
            Service.gI().changeFlag(player, Util.nextInt(9, 10));
        }
    }

    public void update(Player player) {
        if (player.zone == null || !MapService.gI().isMapMaBu2h(player.zone.map.mapId)) {
            try {
                long now = System.currentTimeMillis();
                if (now < TIME_OPEN_MABU_2H || now > TIME_CLOSE_MABU_2H) {
                    ketthucmabu2h(player);
                }
            } catch (Exception ignored) {
            }
        }

    }
}
