package nro.models.boss.dhvt;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;

/**
 * @author Duy Béo
 */
public class ODo extends BossDHVT {

    public ODo(Player player) throws Exception {
        super(BossType.O_DO, BossesData.O_DO);
        this.playerAtt = player;
    }
}
