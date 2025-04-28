package nro.models.boss.list_boss.nappa;

import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.player.Player;
import nro.services.EffectSkillService;
import nro.utils.Util;

public class Kuku extends Boss {

    public Kuku() throws Exception {
        super(BossType.KUKU, BossesData.KUKU);
    }
}
