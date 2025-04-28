package nro.models.map.doanhtrai;

import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.ItemTimeService;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khánh Đẹp Zoai
 */
public class DoanhTraiService {

    private static DoanhTraiService I;

    public static DoanhTraiService gI() {
        if (DoanhTraiService.I == null) {
            DoanhTraiService.I = new DoanhTraiService();
        }
        return DoanhTraiService.I;
    }

    public List<DoanhTrai> doanhTrais;

    private DoanhTraiService() {
        this.doanhTrais = new ArrayList<>();
        for (int i = 0; i < DoanhTrai.AVAILABLE; i++) {
            this.doanhTrais.add(new DoanhTrai(i));
        }
    }

    public void addMapDoanhTrai(int id, Zone zone) {
        this.doanhTrais.get(id).getZones().add(zone);
    }

    public void joinDoanhTrai(Player pl) {
        if (pl.clan == null) {
            Service.gI().sendThongBao(pl, "Mi làm gì có bang hội");
            return;
        }
        if (pl.clan.doanhTrai != null) {
            ChangeMapService.gI().changeMapInYard(pl, 53, -1, 60);
            return;
        }
        DoanhTrai doanhTrai = null;
        for (DoanhTrai dt : this.doanhTrais) {
            if (dt.getClan() == null) {
                doanhTrai = dt;
                break;
            }
        }
        if (doanhTrai == null) {
            Service.gI().sendThongBao(pl, "Doanh trại đã đầy, hãy quay lại vào lúc khác!");
            return;
        }
        doanhTrai.openDoanhTrai(pl);
        ItemTimeService.gI().sendTextDoanhTrai(pl);
    }

    private void ketthucDoanhTrai(Player player) {
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            if (MapService.gI().isMapDoanhTrai(pl.zone.map.mapId)) {
                /**
                 * Đưa mọi người trong map doanh trại về nhà
                 */
                if (MapService.gI().isMapDoanhTrai(pl.zone.map.mapId)) {
                    Service.gI().sendThongBao(pl, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                    ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Khánh Rác", 0);
                    ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, 250);
                    if (pl.clan.doanhTrai != null) {
                        pl.clan.doanhTrai.dispose();
                        pl.clan.doanhTrai = null;
                    }
                }
            }
        }
    }

    public void updatePlayer(Player player) {
        if (player.zone == null || !MapService.gI().isMapDoanhTrai(player.zone.map.mapId)) {
            return;
        }
        try {
            if (player.clan != null && player.clan.doanhTrai != null) {
                long now = System.currentTimeMillis();
                if (!(now >= player.clan.doanhTrai_lastTimeOpen && now <= (player.clan.doanhTrai_lastTimeOpen + DoanhTrai.TIME_DOANH_TRAI))) {
                    ketthucDoanhTrai(player);
                } else if (player.clan.doanhTrai.timePickDragonBall && (System.currentTimeMillis() - player.clan.doanhTrai.getLastTimeOpen() > 5000)) {
                    ketthucDoanhTrai(player);
                }
            }
        } catch (Exception ignored) {
                    ignored.printStackTrace();
        }
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Khánh đê zéc
 */
