package nro.models.item;

import nro.models.Template;
import nro.models.Template.ItemTemplate;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.utils.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nro.consts.ConstItem;

public class Item {

    public int id;
    public boolean isExpires = false;
    public boolean isUpToUp;
    public int quantityTemp = 1;
    public short headTemp;
    public short bodyTemp;
    public short legTemp;
    public int idTemp;
    public ItemTemplate template;

    public String info;

    public String content;

    public int quantity;

    public int quantityGD = 0;

    public List<ItemOption> itemOptions;

    public long createTime;

    public Item(Item _item) { //INIT ITEM TU TEMPLATE
        this.id = _item.id;
        this.template = _item.template;
        this.info = _item.info;
        this.content = _item.content;
        this.isExpires = _item.isExpires;
        this.isUpToUp = _item.isUpToUp;
        this.quantityTemp = _item.quantityTemp;
        this.headTemp = _item.headTemp;
        this.bodyTemp = _item.bodyTemp;
        this.legTemp = _item.legTemp;
        this.idTemp = _item.idTemp;
//        this.itemOptions = _item.itemOptions;
        this.itemOptions = new ArrayList<ItemOption>();
        for (ItemOption _option : _item.itemOptions) {
            this.itemOptions.add(new ItemOption(_option.optionTemplate.id, _option.param));
        }
        this.quantity = 1;
    }

    public boolean isNotNullItem() {
        return this.template != null;
    }

    public boolean isNullItem() {
        return this.template == null;
    }

    public Item() {
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public boolean canConsign() {
        byte type = template.type;
        for (ItemOption o : itemOptions) {
            int optionId = o.optionTemplate.id;
            if (template.id != ConstItem.THOI_VANG && type == 12 || type == 33 || type == 25 || type == 29 || type == 72 || type == 27 || type == 11 || type == 5 || (type < 4)) {
                return true;
            }
        }
        return false;
    }

    public Item(short itemId) {
        this.template = ItemService.gI().getTemplate(itemId);
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public String getInfo() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    public String getInfoItem() {
        String strInfo = "|1|" + template.name + "\n|0|";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString() + "\n";
        }
        strInfo += "|2|" + template.description;
        return strInfo;
    }

//    public List<ItemOption> getDisplayOptions() {
//        List<ItemOption> list = new ArrayList<>();
//        if (itemOptions.isEmpty()) {
//            list.add(OPTION_NULL);
//        } else {
//            for (ItemOption o : itemOptions) {
//                list.add(o.format());
//            }
//        }
//        return list;
//    }
    public String getContent() {
        return "Yêu cầu sức mạnh " + this.template.strRequire + " trở lên";
    }

    public void dispose() {
        this.template = null;
        this.info = null;
        this.content = null;
        if (this.itemOptions != null) {
            for (ItemOption io : this.itemOptions) {
                io.dispose();
            }
            this.itemOptions.clear();
        }
        this.itemOptions = null;
    }

    public static class ItemOption {

        private static Map<String, String> OPTION_STRING = new HashMap<String, String>();

        public int param;

        public Template.ItemOptionTemplate optionTemplate;

        public ItemOption() {
        }

        public ItemOption(ItemOption io) {
            this.param = io.param;
            this.optionTemplate = io.optionTemplate;
        }

        public ItemOption(int tempId, int param) {
            this.optionTemplate = ItemService.gI().getItemOptionTemplate(tempId);
            this.param = param;
        }

        public ItemOption(Template.ItemOptionTemplate temp, int param) {
            this.optionTemplate = temp;
            this.param = param;
        }

        public String getOptionString() {

            return Util.replace(this.optionTemplate.name, "#", String.valueOf(this.param));
        }

        public void dispose() {
            this.optionTemplate = null;
        }

        @Override
        public String toString() {
            final String n = "\"";
            return "{"
                    + n + "id" + n + ":" + n + optionTemplate.id + n + ","
                    + n + "param" + n + ":" + n + param + n
                    + "}";
        }

        public int[] getItemOption(int optionID, int param) {
            int op = optionID;
            int pr = param;
            if (optionID == 6 || optionID == 7) {
                pr = param > Short.MAX_VALUE ? (int) Util.IntToLong(param) : param;
                op = param > Short.MAX_VALUE ? (optionID == 6 ? 22 : optionID == 7 ? 23 : optionID) : optionID;
            }
            return new int[]{
                op,
                pr
            };
        }
//        public int[] getItemOption(int optionID, int param) {
//            int op = optionID;
//            int pr = param;
//
//            if (optionID == 6 || optionID == 7) {
//                if (param > Short.MAX_VALUE) {
//                    pr = param; // Giữ nguyên param thay vì ép kiểu
//                    op = optionID; // Giữ nguyên optionID, không đổi sang 22 hoặc 23
//                }
//            }
//
//            return new int[]{op, pr};
//        }

        public ItemOption format() {
            int id = optionTemplate.id;
            int param = this.param;
            if (param > Short.MAX_VALUE) {
                boolean changed = false;
                switch (id) {
                    case 6:
                        id = 22;
                        param /= 1000;
                        changed = true;
                        break;

                    case 7:
                        id = 23;
                        param /= 1000;
                        changed = true;
                        break;

                    case 31:
                        id = 171;
                        param /= 1000;
                        changed = true;
                        break;

                    case 48:
                        id = 2;
                        param /= 1000;
                        changed = true;
                        break;
                }
                if (changed) {
                    return new ItemOption(id, param);
                }
            }
            return this;
        }
    }

    public boolean isSKH() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135) {
                return true;
            }
        }
        return false;
    }

    public boolean isdo6s() {

        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id == 102 && itemOption.param >= 6) {

                return true;
            }
        }
        return false;
    }

    public boolean isdo7s() {

        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id == 102 && itemOption.param >= 7) {

                return true;
            }
        }
        return false;
    }

    public boolean isdo8s() {

        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id == 102 && itemOption.param >= 8) {

                return true;
            }
        }
        return false;
    }

    public boolean isMinipet() {
        if (this.template.type == 21) {
            return true;
        } else if (this.template.type == -1) {
        }
        return false;
    }

    public boolean isLinhthu() {
        if (this.template.type == 72) {
            return true;
        } else if (this.template.type == -1) {
        }
        return false;
    }

    public boolean isDTS() {
        if (this.template.id >= 1048 && this.template.id <= 1062) {
            return true;
        } else if (this.template.id == -1) {
        }
        return false;
    }

    public boolean isThucAn() {
        if (this.template.id >= 663 && this.template.id <= 667) {
            return true;
        } else if (this.template.id == -1) {
        }
        return false;
    }

    public boolean isDTL() {
        if (this.template.id >= 555 && this.template.id <= 567) {
            return true;
        } else if (this.template.id == -1) {
        }
        return false;
    }

    public boolean isDHD() {
        if (this.template.id >= 650 && this.template.id <= 662) {
            return true;
        } else if (this.template.id == -1) {
        }
        return false;
    }

    public boolean isManhTS() {
        if (this.template.id >= 1066 && this.template.id <= 1070) {
            return true;
        } else if (this.template.id == -1) {
        }
        return false;
    }

    public boolean isCongThucVip() {
        if (this.template.id >= 1084 && this.template.id <= 1086) {
            return true;
        } else if (this.template.id == -1) {
        }
        return false;
    }

    public boolean isDaNangCap() {
        if (this.template.id >= 1074 && this.template.id <= 1078) {
            return true;
        } else if (this.template.id == -1) {
        }
        return false;
    }

    public boolean isDaMayMan() {
        if (this.template.id >= 1079 && this.template.id <= 1083) {
            return true;
        }
        return false;
    }

    public boolean isThienMaThach() {
        if (this.template.id == 1680) {
            return true;
        }
        return false;
    }

    public boolean isTrucCoDan() {
        if (this.template.id >= 1693 && this.template.id <= 1696) {
            return true;
        }
        return false;
    }

    public boolean isLinhKhi() {
        if (this.template.id == 1697) {
            return true;
        }
        return false;
    }

    public boolean isTanDan() {
        if (this.template.id >= 1698 && this.template.id <= 1706) {
            return true;
        }
        return false;
    }

    public boolean haveOption(int idOption) {
        if (this != null && this.isNotNullItem()) {
            return this.itemOptions.stream().anyMatch(op -> op != null && op.optionTemplate.id == idOption);
        }
        return false;
    }

//    public boolean isTrangBiHSD() {
//        if (this.template.type == 27) {
//
//            return InventoryServiceNew.gI().hasOptionTemplateId(this, 93);
//        }
//        return false;
//    }
    public boolean isTrangBiKhoa() {
        return InventoryServiceNew.gI().hasOptionTemplateId(this, 30);
    }

    public ItemOption getOption(int idOption) {
        if (this != null && this.isNotNullItem()) {
            return this.itemOptions.stream()
                    .filter(op -> op != null && op.optionTemplate.id == idOption)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public boolean isTrangBiHacHoa() {
        if (this.template.type <= 5 || this.template.type == 32 || this.template.type == 21 || this.template.type == 25 || this.template.type == 11 || this.template.type == 72) {
            return true;
        }

        return false;
    }

    public boolean isTrangBiHSD() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id == 93 && itemOption.param >= 0) {

                return true;
            }
        }
        return false;
    }

    public boolean isBUg() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id == 200 //  && itemOption.param > 180
                    ) {

                return true;
            }
        }
        return false;
    }

    public boolean isBUg2() {
        for (ItemOption itemOption : itemOptions) {
            if ((itemOption.optionTemplate.id == 50 || itemOption.optionTemplate.id == 77 || itemOption.optionTemplate.id == 103 || itemOption.optionTemplate.id == 5)
                    && itemOption.param >= 26) {

                return true;
            }
        }
        return false;
    }

    public boolean isBUg3() {
        for (ItemOption itemOption : itemOptions) {
            if ((itemOption.optionTemplate.id == 102 || itemOption.optionTemplate.id == 107)
                    && itemOption.param >= 0) {

                return true;
            }
        }
        return false;
    }

    public boolean isTrangBiKhoaGd() {
        if (this.template.type == 11 || this.template.type == 5 || this.template.type == 30 || this.template.type == 12 || this.template.type == 21 || this.template.type == 27 || this.template.type == 72) {
            return true;
        }

        return false;
    }

//     public boolean isTrangBiKhoaGd() {
//        for (ItemOption itemOption : itemOptions) {
//            if (itemOption.optionTemplate.id == 30 && itemOption.param >= 0) {
//
//                return true;
//            }
//        }
//        return false;
//    }
    public boolean isCaiTrangNC() {
        if (this.template.type == 5) {
            return true;
        }

        return false;
    }

    public boolean isTrangBiNgocBoi() {

        if (this.template.type == 5) {
            return true;
        }

        return false;
    }

    public String typeName() {
        switch (this.template.type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Rada";
            default:
                return "";
        }
    }

    public String typeHanhTinh() {
        switch (this.template.id) {
            case 1071:
                return "Trái đất";
            case 1084:
                return "Trái đất";
            case 1072:
                return "Namếc";
            case 1085:
                return "Namếc";
            case 1073:
                return "Xayda";
            case 1086:
                return "Xayda";
            default:
                return "";
        }
    }

    public byte typeIdManh() {
        if (!isManhTS()) {
            return -1;
        }
        switch (this.template.id) {
            case 1066:
                return 0;
            case 1067:
                return 1;
            case 1070:
                return 2;
            case 1068:
                return 3;
            case 1069:
                return 4;
            default:
                return -1;
        }
    }

    public String typeNameManh() {
        switch (this.template.id) {
            case 1066:
                return "Áo";
            case 1067:
                return "Quần";
            case 1070:
                return "Găng";
            case 1068:
                return "Giày";
            case 1069:
                return "Nhẫn";
            default:
                return "";
        }
    }

    public String typeDanangcap() {
        switch (this.template.id) {
            case 1074:
                return "cấp 1";
            case 1075:
                return "cấp 2";
            case 1076:
                return "cấp 3";
            case 1077:
                return "cấp 4";
            case 1078:
                return "cấp 5";
            default:
                return "";
        }
    }

    public String typeDaMayman() {
        switch (this.template.id) {
            case 1079:
                return "cấp 1";
            case 1080:
                return "cấp 2";
            case 1081:
                return "cấp 3";
            case 1082:
                return "cấp 4";
            case 1083:
                return "cấp 5";
            default:
                return "";
        }
    }
}
