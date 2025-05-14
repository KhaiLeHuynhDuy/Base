package nro.models.player;

import nro.models.shop.ShopServiceNew;
import nro.services.MapService;
import nro.consts.ConstMap;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.server.Manager;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstPlayer;

public class TestDame extends Player {

    private long lastTimeChat;
    private Player playerTarget;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 10000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;

    public void initTestDame() {
        init();
    }

    @Override
    public short getHead() {
        return 114;
    }

    @Override
    public short getBody() {
        return 115;
    }

    @Override
    public short getLeg() {
        return 116;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
        player.nPoint.setFullHpMp();
    }

    @Override
    public void update() {
        if (isDie()) {
            // Hồi đầy HP/MP
            this.nPoint.hp = this.nPoint.hpMax;
            this.nPoint.mp = this.nPoint.mpMax;
            this.nPoint.setFullHpMp();
            // Gửi thông tin hồi sinh về client
            Service.gI().hsChar(this, this.nPoint.hp, this.nPoint.mp); // cập nhật HP/MP

        }
    }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 52) {
                for (Zone z : m.zones) {
                    TestDame pl = new TestDame();
                    pl.name = "Tay Đấm Người Bồ";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.setFullHpMp();
                    pl.nPoint.hpMax = 99_000_000_000_000L;
                    pl.nPoint.hpg = 99_000_000_000_000L;
                    pl.nPoint.hp = 99_000_000_000_000L;
                    pl.typePk = ConstPlayer.PK_ALL;
                    pl.location.x = 387;
                    pl.location.y = 336;
                    joinMap(z, pl);
                    z.setTestDame(pl);
                }
            }
        }
    }
}
