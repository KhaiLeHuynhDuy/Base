package nro.models.player;


import nro.models.shop.ShopServiceNew;
import nro.services.MapService;
import nro.consts.ConstMap;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;


public class Yajiro extends Player {

    private long lastTimeChat;
    private Player playerTarget;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 10000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;
    private long lastTimeMove;

    public void initYajiro() {
        init();
    }

    @Override
    public short getHead() {
        return 77;
    }

    @Override
    public short getBody() {
        return 78;
    }

    @Override
    public short getLeg() {
        return 79;
    }
    
    @Override
    public short getFlagBag() {
        return 21;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }
    @Override
     public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }
    @Override
    public void update() {
          if (Util.canDoWithTime(lastTimeMove, 1000)){
            if(this.zone.map.mapId == 1 && this.name.equals("Jajirô")){
                this.moveTo(Util.nextInt(950,1350), Util.nextInt(360,360));
            }
            lastTimeMove = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeChat, 10000)) {
            Service.gI().chat(this, "Kia là đường lên trên tháp karin đó, nơi đó có thần mèo Karin");
            lastTimeChat = System.currentTimeMillis();
            
        }
    }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 1) {
                for (Zone z : m.zones) {
                    Yajiro pl = new Yajiro();
                    pl.name = "Jajirô";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 1100;
                    pl.nPoint.hpg = 1100;
                    pl.nPoint.hp = 1100;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 1200;
                    pl.location.y = 336;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            }
        }
    }
}