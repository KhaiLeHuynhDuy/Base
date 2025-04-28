package nro.models.consignment;

import nro.models.item.Item;
import lombok.Getter;
import lombok.Setter;

/**
 * Debug Arriety
 */
@Setter
@Getter
public class ConsignmentItem extends Item {

    private int consignID;
    private long consignorID;
    private int priceGold;
    private int priceGem;
    private byte tab;
    private boolean sold;
    private boolean upTop;
    private long timeConsign;
    private long playerSell;
}
