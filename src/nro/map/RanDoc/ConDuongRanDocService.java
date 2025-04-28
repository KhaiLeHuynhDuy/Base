package nro.map.RanDoc;

import nro.models.map.Zone;
import nro.models.player.Player;

import nro.services.ItemTimeService;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import java.util.ArrayList;
import java.util.List;
import nro.services.InventoryServiceNew;

/**
 *
 * @author Khánh Đẹp Zoai
 */
public class ConDuongRanDocService {

    private static ConDuongRanDocService I;

    public static ConDuongRanDocService gI() {
        if (ConDuongRanDocService.I == null) {
            ConDuongRanDocService.I = new ConDuongRanDocService();
        }
        return ConDuongRanDocService.I;
    }

    public List<ConDuongRanDoc> RAN_DOC;

    private ConDuongRanDocService() {
        this.RAN_DOC = new ArrayList<>();
        for (int i = 0; i < ConDuongRanDoc.AVAILABLE; i++) {
            this.RAN_DOC.add(new ConDuongRanDoc(i));
        }
    }

    public void addMapCDRD(int id, Zone zone) {
        this.RAN_DOC.get(id).getZones().add(zone);
    }

    public void openCDRD(Player pl, int level) {
        if (pl.clan == null) {
            Service.gI().sendThongBao(pl, "Mi làm gì có bang hội");
            return;
        }
        if (pl.cdrd_countPerDay >= 3) {
            Service.gI().sendThongBao(pl, "Bạn đã đạt giới hạn lượt đi trong ngày");
            return;
        }

        ConDuongRanDoc cdrd = null;
        for (ConDuongRanDoc rd : this.RAN_DOC) {
            if (rd.getClan() == null) {
                cdrd = rd;
                break;
            }
        }
        if (cdrd == null) {
            Service.gI().sendThongBao(pl, "Con đường rắn độc đã đầy, hãy quay lại vào lúc khác!");
            return;
        }
        if (pl.clan.ConDuongRanDoc == null && InventoryServiceNew.gI().findItemBag(pl, 457) == null) {
            Service.gI().sendThongBao(pl, "Cần 1000 thỏi vàng làm phí di chuyển");
            return;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 1000);
        InventoryServiceNew.gI().sendItemBags(pl);
        cdrd.openCDRD(pl, level);
        ItemTimeService.gI().sendTextConDuongRanDoc(pl);
    }

    public void joinCDRD(Player pl) {
        if (pl.clan == null) {
            Service.gI().sendThongBao(pl, "Mi làm gì có bang hội");
            return;
        }
        if (pl.cdrd_countPerDay >= 3) {
            Service.gI().sendThongBao(pl, "Bạn đã đạt giới hạn lượt đi trong ngày");
            return;
        }
        if (pl.clan.ConDuongRanDoc != null) {
            if (!pl.cdrd_isJoinCdrd) {
                pl.cdrd_countPerDay++;
                pl.cdrd_isJoinCdrd = true;
            }
            pl.cdrd_lastTimeJoin = System.currentTimeMillis();
            ChangeMapService.gI().goToCDRD(pl);
//            ChangeMapService.gI().changeMapBySpaceShip(pl, 141, -1, -1);
            ItemTimeService.gI().sendTextConDuongRanDoc(pl);
        }
    }

    public void updatePlayer(Player player) {
        if (player.zone == null || !MapService.gI().isMapConDuongRanDoc(player.zone.map.mapId)) {
            return;
        }
        try {
            if (player.clan != null && player.clan.ConDuongRanDoc != null) {
                long now = System.currentTimeMillis();
                if (!(now >= player.clan.ConDuongRanDoc_lastTimeOpen && now <= (player.clan.ConDuongRanDoc_lastTimeOpen + ConDuongRanDoc.TIME_CDRD))) {
                    ketthuccdrd(player);
                } else if (player.clan.ConDuongRanDoc.timePickReward && (System.currentTimeMillis() - player.clan.ConDuongRanDoc.getLastTimeOpen() > 2000)) {
                    ketthuccdrd(player);
                }
            }
        } catch (Exception ignored) {
              ignored.printStackTrace();
        }
    }

    private void ketthuccdrd(Player player) {
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            /**
             * Đưa mọi người trong map doanh trại về nhà
             */
            if (MapService.gI().isMapConDuongRanDoc(pl.zone.map.mapId)) {
                pl.cdrd_isJoinCdrd = false;
                Service.gI().sendThongBao(pl, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
//                ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Bạn đã được đưa về tới nhà", 0);
                ItemTimeService.gI().removeTextConDuongRanDoc(pl);
                ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, 250);
                if (pl.clan.ConDuongRanDoc != null) {
                    pl.clan.ConDuongRanDoc.dispose();
                    pl.clan.ConDuongRanDoc = null;
                }
            }
        }
    }

}
