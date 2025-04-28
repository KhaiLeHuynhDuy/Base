package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;

/**
 * @author Duy Béo 
 */
public class JackyChun extends BossDHVT {

    public JackyChun(Player player) throws Exception {
        super(BossType.JACKY_CHUN, BossesData.JACKY_CHUN);
        this.playerAtt = player;
    }
}