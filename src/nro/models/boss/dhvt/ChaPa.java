package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;
/**
 * @author Duy Béo 
 */
public class ChaPa extends BossDHVT {

    public ChaPa(Player player) throws Exception {
        super(BossType.CHA_PA, BossesData.CHA_PA);
        this.playerAtt = player;
    }
}