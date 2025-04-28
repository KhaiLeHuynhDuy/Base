/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.kygui;

import nro.models.item.Item;
import nro.models.item.Item.ItemOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ItemKyGui {
    public int id;
    public short itemId;
    public int player_sell;
    public byte tab;
    public int goldSell;
    public int gemSell;
    public int quantity;
    public byte isUpTop;
    public List<Item.ItemOption> options = new ArrayList();
    public boolean isBuy;

    public ItemKyGui() {
    }

    public ItemKyGui(int i, short id, int plId, byte t, int gold, int gem, int q, byte isUp, List<Item.ItemOption> op, boolean b) {
        this.id = i;
        this.itemId = id;
        this.player_sell = plId;
        this.tab = t;
        this.goldSell = gold;
        this.gemSell = gem;
        this.quantity = q;
        this.isUpTop = isUp;
        this.options = op;
        this.isBuy = b;
    }
}
