package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;
/**
 * @author Duy Béo
 */
public class ChanXu extends BossDHVT {

    public ChanXu(Player player) throws Exception {
        super(BossType.CHAN_XU, BossesData.CHAN_XU);
        this.playerAtt = player;
    }
}