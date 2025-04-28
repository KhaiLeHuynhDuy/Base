package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;

/**
 * @author Duy Béo
 */
public class PonPut extends BossDHVT {

    public PonPut(Player player) throws Exception {
        super(BossType.PON_PUT, BossesData.PON_PUT);
        this.playerAtt = player;
    }
}