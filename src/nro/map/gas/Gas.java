package nro.map.gas;

import nro.models.boss.Boss;
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

@Data
public class Gas {

    public static final long POWER_CAN_GO_TO_GAS = 2000000000;

    public static final int MAX_AVAILABLE = 100;
    public static final int TIME_KHI_GAS = 1800000;

    private Player player;

    public int id;
    public int level;
    public final List<Zone> zones;

    public Clan clan;
    public boolean isOpened;
    private long lastTimeOpen;
    List<Integer> listMap = Arrays.asList(149, 147, 152, 151, 148);
    private int currentIndexMap = -1;
    public static final int MAX_HP_MOB = 16_700_000;
    public static final int MIN_HP_MOB = 33000;

    public static final int MAX_DAME_MOB = 2_000_000;
    public static final int MIN_DAME_MOB = 3000;

    public long BOSS_HP = 1;
    public long BOSS_DAMAGE = 1;
    Boss boss;
    public boolean timePickReward;

    public boolean isSpawnBoss = false;

    public Gas(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
    }

    public void openKhiGas(Player plOpen, Clan clan, int level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timeOpenKhiGas = this.lastTimeOpen;
        this.clan.playerOpenKhiGas = plOpen;
        this.clan.khiGas = this;
        plOpen.lastimeJoinKG = System.currentTimeMillis();
        ChangeMapService.gI().goToGas(plOpen);
        sendTextGas();
    }

    //kết thúc bản đồ kho báu
    public void finish() {
        List<Player> plOutDT = new ArrayList();
        for (Zone zone : zones) {
            for (Player pl : zone.getPlayers()) {
                ChangeMapService.gI().changeMapBySpaceShip(pl, 5, -1, 384);
            }
        }
        this.clan.khiGas = null;
        this.clan = null;
        this.isOpened = false;
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    private void sendTextGas() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextGas(pl);
        }
    }

    public void init() {
        for (Zone zone : this.zones) {
            if (zone.map.mapId == this.listMap.get(this.currentIndexMap)) {
                long newHpMob = this.level * (MIN_HP_MOB * ((this.level - 1) * 33) / 10);
//                System.out.println(newHpMob);
                long newDameMob = this.level * (MIN_DAME_MOB * ((this.level - 1) * 33) / 1000);
//                System.out.println(newDameMob);
                this.BOSS_HP = newHpMob;
                this.BOSS_DAMAGE = newDameMob;
                 for (Mob mob : zone.mobs) {
                    mob.point.dame = (int) (newDameMob);
                    mob.point.maxHp = (int) (newHpMob);

                    mob.hoiSinh();
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
}
