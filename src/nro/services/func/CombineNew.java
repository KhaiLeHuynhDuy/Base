package nro.services.func;

import nro.models.item.Item;
import java.util.ArrayList;
import java.util.List;

public class CombineNew {

    public long lastTimeCombine;

    public List<Item> itemsCombine;
    public int typeCombine;
    public int rubyCombine;
    public long goldCombine;
    public int gemCombine;
    public float ratioCombine;
    public int countDaNangCap;
    public short countDaBaoVe;
    public int countThoiVang;
    public int countDaQuy;
    public short quantities = 1;
    public int DiemNangcap;
    public int DaNangcap;
    public float TileNangcap;

    public CombineNew() {
        this.itemsCombine = new ArrayList<>();
    }

    public void setTypeCombine(int type) {
        this.typeCombine = type;
    }

    public void clearItemCombine() {
        this.itemsCombine.clear();
    }

    public void updateItemsCombine(List<Item> itemsBag) {
        List<Item> updatedItems = new ArrayList<>();

        for (Item item : this.itemsCombine) {
            // Kiểm tra item còn tồn tại trong túi đồ không
            if (itemsBag.contains(item)) {
                // Nếu item vẫn còn nhưng số lượng giảm, cập nhật số lượng mới
                for (Item bagItem : itemsBag) {
                    if (bagItem.equals(item) && bagItem.quantity > 0) {
                        updatedItems.add(bagItem);
                        break;
                    }
                }
            }
        }

        // Cập nhật danh sách itemsCombine
        this.itemsCombine = updatedItems;
    }

    public void clearParamCombine() {
        this.goldCombine = 0;
        this.gemCombine = 0;
        this.rubyCombine = 0;
        this.ratioCombine = 0;
        this.countDaNangCap = 0;
        this.countDaQuy = 0;
        this.countDaBaoVe = 0;
        this.countThoiVang = 0;
        this.DiemNangcap = 0;
        this.DaNangcap = 0;
        this.TileNangcap = 0;
    }

    public void dispose() {
        this.itemsCombine = null;
    }
}
