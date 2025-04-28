package nro.map.RanDoc;

import nro.models.boss.BossType;
import nro.models.boss.BossManager;
import nro.models.boss.BossStatus;

import nro.models.clan.Clan;
import nro.models.map.Zone;
import nro.models.mob.Mob;
import nro.models.player.Player;
import nro.services.ItemTimeService;
import nro.services.func.ChangeMapService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import nro.models.boss.list_boss.ConDuongRanDoc.Nappa;
import nro.models.boss.list_boss.ConDuongRanDoc.Vegeta;
import nro.services.Service;

/**
 *
 * @author Khánh Đẹp Zoai
 */
@Data
public class ConDuongRanDoc {

    public static final int N_PLAYER_CLAN = 5;// số người trong pt
    public static final int N_PLAYER_MAP = 1;// số ng đứng cạnh
    public static final int AVAILABLE = 30; // số lượng bdkb trong server
    public static final int TIME_CDRD = 1_800_000;
    public static final long POWER_CAN_GO_TO_CDRD = 20000000000L;
    public static final int MAX_HP_MOB = 1_165_000_000;
    public static final int MIN_HP_MOB = 1_000_000;

    public static final int MAX_DAME_MOB = 165_000_000;
    public static final int MIN_DAME_MOB = 3000;

    private int id;
    private List<Zone> zones;
    private Clan clan;

    private long lastTimeOpen;

    public int level;

    List<Integer> listMap = Arrays.asList(141, 142, 143, 144);
    private int currentIndexMap = -1;

    Nappa boss;
    Vegeta boss1;

    public boolean timePickReward;

    public ConDuongRanDoc(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public void openCDRD(Player player, int level) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        this.level = level;
        player.clan.ConDuongRanDoc = this;
        player.clan.CDRD_playerOpen = player.name;
        player.clan.ConDuongRanDoc_lastTimeOpen = this.lastTimeOpen;
        player.cdrd_isJoinCdrd = true;
        player.cdrd_countPerDay++;
        player.cdrd_lastTimeJoin = System.currentTimeMillis();
        ChangeMapService.gI().goToCDRD(player);
//        ChangeMapService.gI().changeMapBySpaceShip(player, 141, -1, -1);
//       //Khởi tạo quái, boss
//        this.init();

        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null) {
                continue;
            }
            ItemTimeService.gI().sendTextConDuongRanDoc(pl);
        }

    }
//    public Location location;
//    public Player player;

    public void init() {
        long totalDame = 0;
        long totalHp = 0;
        for (Player pl : this.clan.membersInGame) {
            if (pl.nPoint != null) {
                totalDame += pl.nPoint.dame;
                totalHp += pl.nPoint.hpMax;

            } else {
                Service.gI().sendThongBao(pl, "Có lỗi xảy ra khi qua map CDRD");
            }
        }
        long newHpMob = 0;
        long newDameMob = 0;
        //Hồi sinh quái và thả boss
        for (Zone zone : this.zones) {
            if (this.currentIndexMap >= 0 && this.currentIndexMap < this.listMap.size()) {
                if (zone.map.mapId == this.listMap.get(this.currentIndexMap)) {

//                    newHpMob = Math.min((long) (((this.level - 1) * 1.0 / (110 - 1)) * (MAX_HP_MOB - MIN_HP_MOB) + totalHp), 2000000000);
//                    newDameMob = Math.min((long) (((this.level - 1) * 1.0 / (110 - 1)) * (MAX_DAME_MOB - MIN_DAME_MOB) + totalDame), 2000000000);
                    newHpMob = (long) (((this.level - 1) * 1.0 / (110 - 1)) * (MAX_HP_MOB - MIN_HP_MOB) + totalHp);
                    newDameMob = (long) (((this.level - 1) * 1.0 / (110 - 1)) * (MAX_DAME_MOB - MIN_DAME_MOB) + totalDame);
//                    }
                    for (Mob mob : zone.mobs) {

                        mob.point.dame = (int) newDameMob;
                        mob.point.maxHp = (int) newHpMob;
                        mob.hoiSinh();

                    }
                    int idBoss = (zone.map.mapId == 144 ? BossType.NAPPA : -1);
                    if (idBoss != -1) {
                        boss = (Nappa) BossManager.gI().createBossCDRD(idBoss, 1, (int) (newHpMob * 4L > 2_000_000_000 ? 2_000_000_000 : newHpMob * 4L), (int) (newHpMob * 5L > 2_000_000_000 ? 2_000_000_000 : newHpMob * 5L), zone);
//                        boss1 = (Vegeta) BossManager.gI().createBossCDRD(idBoss, 1, (int) (newHpMob * 4L > 2_000_000_000 ? 2_000_000_000 : newHpMob * 4L), (int) (newHpMob * 5L > 2_000_000_000 ? 2_000_000_000 : newHpMob * 5L), zone);

                    }
                }
            }
        }
    }

    public void dispose() {
        boss.changeStatus(BossStatus.LEAVE_MAP);
        this.clan = null;
        this.boss = null;
        timePickReward = false;
        currentIndexMap = -1;
    }

    public long getLastTimeOpen() {
        return lastTimeOpen;
    }
}
