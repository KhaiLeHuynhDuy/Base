package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;

/**
 * @author Duy Béo 
 */
public class SoiHecQuyn extends BossDHVT {
    public SoiHecQuyn(Player player) throws Exception {
        super(BossType.SOI_HEC_QUYN, BossesData.SOI_HEC_QUYN);
        this.playerAtt = player;
    }
}
