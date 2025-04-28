package nro.models.map;

import nro.models.player.Player;
import nro.services.PlayerService;
import nro.services.func.EffectMapService;
import nro.utils.Util;


public class TrapMap {

    public int x;
    public int y;
    public int w;
    public int h;
    public int effectId;
    public int dame;

    public void doPlayer(Player player) {
        switch (this.effectId) {
            case 49:
    if (!player.isDie() && Util.canDoWithTime(player.iDMark.getLastTimeAnXienTrapBDKB(), 1000) && !player.isBoss) {
        int increasedDame = dame * 2; // Tăng lượng dame lên 100%
        player.injured(null, increasedDame + (Util.nextInt(-10, 10) * increasedDame / 100), false, false);
        PlayerService.gI().sendInfoHp(player);
        EffectMapService.gI().sendEffectMapToAllInMap(player.zone, effectId, 2, 1, player.location.x - 32, 1040, 1);
        player.iDMark.setLastTimeAnXienTrapBDKB(System.currentTimeMillis());
    }
    break;
        }
    }

}
