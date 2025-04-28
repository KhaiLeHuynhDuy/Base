package nro.models.map.MapMaBu;
import nro.models.map.Map;
import nro.models.map.MapMaBu.MapMaBu2h;
import nro.models.map.Zone;
import nro.services.MapService;
import nro.utils.Logger;

public class MapXHMaBu2h {

    public static MapXHMaBu2h i;

    public static MapXHMaBu2h gI() {
        if (i == null) {
            i = new MapXHMaBu2h();
        }
        return i;
    }

    public void initBoss(int MapId) {
        Map map = MapService.gI().getMapById(MapId);
        try {
            for (Zone zone : map.zones) {
                new BuMap(zone);
            }
            MapMaBu2h.gI().isSpawnMabu = true;
        } catch (Exception e) {
              e.printStackTrace();
            Logger.error("Lỗi init boss : " + e);
        }

    }
}


