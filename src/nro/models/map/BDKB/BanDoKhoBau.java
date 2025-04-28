package nro.models.map.BDKB;

import nro.models.boss.BossType;
import nro.models.boss.BossManager;
import nro.models.boss.BossStatus;
import nro.models.boss.list_boss.phoban.TrungUyXanhLoBdkb;
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
import nro.services.Service;

/**
 *
 * @author Khánh Đẹp Zoai
 */
@Data
public class BanDoKhoBau {

    public static final int N_PLAYER_CLAN = 5;// số người trong pt
    public static final int N_PLAYER_MAP = 1;// số ng đứng cạnh
    public static final int AVAILABLE = 30; // số lượng bdkb trong server
    public static final int TIME_BAN_DO_KHO_BAU = 1_800_000;
    public static final long POWER_CAN_GO_TO_DBKB = 20000000000L;
    public static final int MAX_HP_MOB = 1_165_000_000;
    public static final int MIN_HP_MOB = 1_000_000;

    public static final int MAX_DAME_MOB = 165_000_000;
    public static final int MIN_DAME_MOB = 3000;

    private int id;
    private List<Zone> zones;
    private Clan clan;

    private long lastTimeOpen;

    public int level;

    List<Integer> listMap = Arrays.asList(135, 138, 136, 137);
    private int currentIndexMap = -1;

    TrungUyXanhLoBdkb boss;

    public boolean timePickReward;

    public BanDoKhoBau(int id) {
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

    public void openBDKB(Player player, int level) {
        this.lastTimeOpen = System.currentTimeMillis();
        this.clan = player.clan;
        this.level = level;
        player.clan.banDoKhoBau = this;
        player.clan.banDoKhoBau_playerOpen = player.name;
        player.clan.banDoKhoBau_lastTimeOpen = this.lastTimeOpen;
        player.bdkb_isJoinBdkb = true;
        player.bdkb_countPerDay++;
        player.bdkb_lastTimeJoin = System.currentTimeMillis();
        ChangeMapService.gI().goToDBKB(player);
//       //Khởi tạo quái, boss
//        this.init();

        for (Player pl : player.clan.membersInGame) {
            if (pl == null || pl.zone == null) {
                continue;
            }
            ItemTimeService.gI().sendTextBanDoKhoBau(pl);
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
                Service.gI().sendThongBao(pl, "Có lỗi xảy ra khi qua map BDKB");
            }
        }
        long newHpMob = 0;
        long newDameMob = 0;
        //Hồi sinh quái và thả boss
        for (Zone zone : this.zones) {
            if (this.currentIndexMap >= 0 && this.currentIndexMap < this.listMap.size()) {
                if (zone.map.mapId == this.listMap.get(this.currentIndexMap)) {
//                    if (this.level >= 1 && this.level < 20) {
//                        ItemMap it = new ItemMap(zone, 457, 1, this.location.x, zone.map.yPhysicInTop(this.location.x,
//                                this.location.y - 24), player.id);
//                        Service.gI().dropItemMap(zone, it);
//                    }
                    newHpMob = Math.min((long) (((this.level - 1) * 1.0 / (110 - 1)) * (MAX_HP_MOB - MIN_HP_MOB) + totalHp), 2000000000);
                    newDameMob = Math.min((long) (((this.level - 1) * 1.0 / (110 - 1)) * (MAX_DAME_MOB - MIN_DAME_MOB) + totalDame), 2000000000);
//                    }
                    for (Mob mob : zone.mobs) {

                        mob.point.dame = (int) newDameMob;
                        mob.point.maxHp = (int) newHpMob;
                        mob.hoiSinh();

                    }
                    int idBoss = (zone.map.mapId == 137 ? BossType.TRUNG_UY_XANH_LO_BDKB : -1);
                    if (idBoss != -1) {
                        boss = (TrungUyXanhLoBdkb) BossManager.gI().createBossBdkb(idBoss, (int) (newHpMob * 4L > 2_000_000_000 ? 2_000_000_000 : newHpMob * 4L), (int) (newHpMob * 5L > 2_000_000_000 ? 2_000_000_000 : newHpMob * 5L), zone);
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
