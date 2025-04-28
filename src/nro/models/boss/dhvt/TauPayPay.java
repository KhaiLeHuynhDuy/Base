package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;

/**
 * @authorDuy Béo
 */
public class TauPayPay extends BossDHVT {

    public TauPayPay(Player player) throws Exception {
        super(BossType.TAU_PAY_PAY, BossesData.TAU_PAY_PAY);
        this.playerAtt = player;
    }
}