package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;

/**
 * @author Duy Béo 
 */
public class Yamcha extends BossDHVT {

    public Yamcha(Player player) throws Exception {
        super(BossType.YAMCHA, BossesData.YAMCHA);
        this.playerAtt = player;
    }
}