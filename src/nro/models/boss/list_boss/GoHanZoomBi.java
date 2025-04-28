package nro.models.boss.list_boss;

import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.utils.Util;
import java.util.Random;

public class GoHanZoomBi extends Boss {

    public GoHanZoomBi() throws Exception {
        super(BossType.GOHAN, BossesData.GOHAN_ZOMBI);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(90, 100)) {
            int itDropCount = 0;
            int num = Util.isTrue(50, 100) ? 5 : 10;
            while (itDropCount < num) {
                ItemMap it = new ItemMap(this.zone, 585, 1, this.location.x - (itDropCount * 30), this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
                ItemMap it1 = new ItemMap(this.zone, 76, 30000, this.location.x - (itDropCount * 40), this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
               Service.gI().dropItemMap(this.zone, it);
               Service.gI().dropItemMap(this.zone, it1);
                itDropCount++;
            }
        } else if (Util.isTrue(30, 100)) {
            ItemMap ct = new ItemMap(this.zone, 1374, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
            ct.options.add(new Item.ItemOption(50, new Random().nextInt(5) + 27));
            ct.options.add(new Item.ItemOption(77, new Random().nextInt(10) + 25));
            ct.options.add(new Item.ItemOption(103, new Random().nextInt(10) + 25));
            if (Util.isTrue(25, 100))  {
                ct.options.add(new Item.ItemOption(93, new Random().nextInt(2) + 3));
            }
           Service.gI().dropItemMap(this.zone, ct);
        }
    }
}
