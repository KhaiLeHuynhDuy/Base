package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;
/**
 * @author Duy Béo
 */
public class LiuLiu extends BossDHVT {

    public LiuLiu(Player player) throws Exception {
        super(BossType.LIU_LIU, BossesData.LIU_LIU);
        this.playerAtt = player;
    }
}