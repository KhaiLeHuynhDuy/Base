package nro.models.boss.dhvt;

import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;
import nro.services.EffectSkillService;
import nro.utils.Util;

/**
 * @author Duy Béo
 */
public class ThienXinHang extends BossDHVT {

    private long lastTimePhanThan
            = System.currentTimeMillis();

    public ThienXinHang(Player player) throws Exception {
        super(BossType.THIEN_XIN_HANG, BossesData.THIEN_XIN_HANG);
        this.playerAtt = player;
//        phanThan();
    }

    @Override
    public void attack() {
        super.attack();
        try {
            EffectSkillService.gI().removeStun(this);
            if (Util.canDoWithTime(lastTimePhanThan, 20000)) {
                lastTimePhanThan = System.currentTimeMillis();
                phanThan();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void phanThan() {
        try {
            new ThienXinHangClone(BossType.THIEN_XIN_HANG_CLONE, playerAtt);
            new ThienXinHangClone(BossType.THIEN_XIN_HANG_CLONE1, playerAtt);
            new ThienXinHangClone(BossType.THIEN_XIN_HANG_CLONE2, playerAtt);
            new ThienXinHangClone(BossType.THIEN_XIN_HANG_CLONE3, playerAtt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}