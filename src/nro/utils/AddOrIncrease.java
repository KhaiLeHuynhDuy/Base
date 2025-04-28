
package nro.utils;

import java.util.List;
import nro.models.item.Item;
/**
 *
 * @author khail
 */
public class AddOrIncrease {
   
    public static boolean addItemToBag(List<Item> itemsBag, int itemId, int quantity) {
        for (Item item : itemsBag) {
            // Kiểm tra theo ID vật phẩm 
            if (item.id == itemId) {
                item.quantity += quantity;
                return true;
            }
        }
        return false;
    }
}
