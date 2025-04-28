package nro.map.gas;

import nro.models.boss.BossType;
import nro.models.boss.BossManager;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.InventoryServiceNew;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import java.util.ArrayList;
import java.util.List;

public class GasService {

    public static GasService i;
    public List<Gas> KHI_GAS;

    public GasService() {
        KHI_GAS = new ArrayList<>();
        for (int j = 0; j < Gas.MAX_AVAILABLE; j++) {
            KHI_GAS.add(new Gas(j));
        }
    }

    public static GasService gI() {
        if (i == null) {
            i = new GasService();
        }
        return i;
    }

    public void update(Player player) {
        if (player.clan == null) {
            return;
        }
        if (player.isPl() == true && player.clan.khiGas != null && player.clan.timeOpenKhiGas != 0) {
            long now = System.currentTimeMillis();
            if (!(now >= player.clan.timeOpenKhiGas && now <= (player.clan.timeOpenKhiGas + Gas.TIME_KHI_GAS))) {
                ketthucGas(player);
            } else if (player.clan.khiGas.timePickReward && (System.currentTimeMillis() - player.clan.khiGas.getLastTimeOpen() > 2000)) {
                ketthucGas(player);
            }
        }
        if (player.isPl() == true && player.clan.khiGas != null && player.clan.timeOpenKhiGas != 0 && player.clan.khiGas.getCurrentIndexMap() == 4
                && player.zone.mobs.stream().allMatch(x -> x.isDie()) && !player.clan.khiGas.isSpawnBoss && MapService.gI().isMapKhiGas(player.zone.map.mapId)) {
            player.clan.khiGas.boss = BossManager.gI().createBossGas(
                    BossType.DR_LYCHEE, (int) (player.clan.khiGas.BOSS_DAMAGE * 4 > 2_000_000_000 ? 2_000_000_000 : player.clan.khiGas.BOSS_DAMAGE * 4), (int) (player.clan.khiGas.BOSS_HP * 5 > 2_000_000_000 ? 2_000_000_000 : player.clan.khiGas.BOSS_HP * 5), player.zone);
            player.clan.khiGas.isSpawnBoss = true;
        }
    }

    private void kickOutOfGas(Player player) {
        if (MapService.gI().isMapKhiGas(player.zone.map.mapId)) {
            Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
        }
    }

    private void ketthucGas(Player player) {
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfGas(pl);
        }
    }

    public void addZoneGas(int idGas, Zone zone) {
        KHI_GAS.get(idGas).getZones().add(zone);
    }

    public void openKhiGas(Player player, int level) {
        if (level >= 1 && level <= 110) {
            if (player.clan != null && player.clan.khiGas == null) {
                Gas gas = null;
                for (Gas bdkb : KHI_GAS) {
                    if (!bdkb.isOpened) {
                        gas = bdkb;
                        break;
                    }
                }
                if (gas != null) {
                    InventoryServiceNew.gI().sendItemBags(player);
                    gas.openKhiGas(player, player.clan, level);
                } else {
                   Service.gI().sendThongBao(player, "Bản đồ Khí Gas, vui lòng quay lại sau");
                }
            } else {
               Service.gI().sendThongBao(player, "Mi làm gì có bang hội");
            }
        } else {
           Service.gI().sendThongBao(player, "Vui lòng chọn cấp dộ từ 1 đến 110");
        }
    }
}
