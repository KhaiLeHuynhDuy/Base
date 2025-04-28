package nro.models.boss.list_boss.Mabu12h;

import nro.consts.ConstTask;
import nro.consts.cn;
import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

import java.util.Random;

public class BuiBui2 extends Boss {

    public BuiBui2() throws Exception {
        super(BossType.BUI_BUI_2, BossesData.BUI_BUI_2);
    }

   @Override
    public void reward(Player plKill) {
       plKill.inventory.event++;
        plKill.event.addEventPointBoss(1);
        Service.gI().sendThongBao(plKill, "Bạn đã nhận được 1 điểm săn Boss");
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
       // byte randomc12 = (byte) new Random().nextInt(Manager.itemDC12.length - 1);

        if (Util.isTrue(1, 130)) {
            if (Util.isTrue(1, 50)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 1135, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } 
        
        if (Util.isTrue(cn.tileroinr, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
        plKill.fightMabu.changePoint((byte) 40);
    }
}
