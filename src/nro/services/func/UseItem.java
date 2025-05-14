package nro.services.func;

//import com.arriety.card.Card;
//import com.arriety.card.RadarCard;
//import com.arriety.card.RadarService;
//import nro.consts.ConstDataEvent;
import nro.consts.ConstMap;
import nro.models.item.Item;
import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.consts.cn;
import nro.jdbc.daos.PlayerDAO;
import nro.models.card.Card;
import nro.models.card.RadarCard;
import nro.models.card.RadarService;
import nro.models.item.Item.ItemOption;
import nro.models.map.Zone;

import nro.models.player.Inventory;
import nro.services.NpcService;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.network.io.Message;
//import nro.models.player.MiniPet;
import nro.utils.SkillUtil;
import nro.services.Service;
import nro.utils.Util;
import nro.server.io.MySession;
import nro.services.ItemService;
import nro.services.ItemTimeService;
import nro.services.PetService;
import nro.services.PlayerService;
import nro.services.TaskService;
import nro.services.InventoryServiceNew;
import nro.services.MapService;
import nro.services.NgocRongNamecService;
import nro.services.RewardService;
import nro.services.SkillService;
import nro.utils.Logger;
import nro.models.player.MiniPet;
import nro.utils.TimeUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import nro.consts.ConstEvent;
import nro.consts.ConstItem;
import nro.lib.RandomCollection;
import static nro.models.player.MiniPet.DITHEO;
import nro.server.Manager;
import nro.services.IntrinsicService;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;
//  private Player player;

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;

        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.gI().point(player);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(UseItem.class, e);

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);

            msg = new Message(-43);
            msg.writer().writeByte(type);
            msg.writer().writeByte(where);
            msg.writer().writeByte(index);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(UseItem.class, e);
        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
//             System.out.println("Fix in UseItem.doItem " + type + " " + where + " " + index);
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
//                        if (index != -1) {
                        if (index >= 0) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0 && index >= 0 && index < player.inventory.itemsBody.size()) {
                            item = player.inventory.itemsBody.get(index);
                        } else if (where != 0 && index >= 0 && index < player.inventory.itemsBag.size()) {
                            item = player.inventory.itemsBag.get(index);
                        } else {
                            Service.gI().sendThongBao(player, "Lỗi vất vật phẩm, vui lòng báo admin !!!");
                        }

                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);

                        String itemName = "";
                        if (item != null && item.template != null) {
                            itemName = item.template.name;
                        }

                        if (itemName != null) {
                            msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + itemName + "?");
                        } else {
                            msg.writer().writeUTF("Bạn chắc chắn muốn vứt mục tiêu này?");
                        }
                        player.sendMessage(msg);
                    } else {
                        Service.gI().sendThongBao(player, "Không thể vất item ở nhà được !!!");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.gI().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(UseItem.class, e);
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item.template != null && item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 21:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    MiniPet.callMiniPet(pl, pl.getThuCung());
                    pl.minipet.reCall();
//                    PetService.Pet2(pl, pl.newpet.getHead(), pl.newpet.getBody(), pl.newpet.getLeg());
                    break;
                case 7: //sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 6: //đậu thần
                    this.eatPea(pl);
                    break;

                case 33:
                    UseCard(pl, item);
                    break;
                case 12: //ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    break;

                case 23: //thú cưỡi mới
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 24: //thú cưỡi cũ 
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
//                case 11: //item bag
//                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                    Service.gI().sendFlagBag(pl);
//                    break;
//                case 5: //item bag
//                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                    Service.gI().Send_Caitrang(pl);
//                    break;

                case 35:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendEffDanhHieu(pl, pl.getEffectchar(), 1, -1, 50, -1, -1);
                    break;
                case 25:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendEffChanMenh(pl, pl.getEffectchar2(), 0, -1, 1, -1);
                    break;
                case 72:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendPetFollow(pl, pl.getLinhThu());
                    break;
                default:

                    switch (item.template.id) {
//                        case 936: // tloc
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 718, 719, 720);
//                            Service.gI().point(pl);
//                            break;
//                        case 892: // tho xam   
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 882, 883, 884);
//                            Service.gI().point(pl);
//                            break;
//                        case 893: // tho trang
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 885, 886, 887);
//                            Service.gI().point(pl);
//                            break;
//                        case 942:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 966, 967, 968);
//                            Service.gI().point(pl);
//                            break;
//                        case 943:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 969, 970, 971);
//                            Service.gI().point(pl);
//                            break;
//                        case 944:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 972, 973, 974);
//                            Service.gI().point(pl);
//                            break;
//                        case 967:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1050, 1051, 1052);
//                            Service.gI().point(pl);
//                            break;
//                        case 1039:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1089, 1090, 1091);
//                            Service.gI().point(pl);
//                            break;
//                        case 1040:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1092, 1093, 1094);
//                            Service.gI().point(pl);
//                            break;
//                        case 916:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 931, 932, 933);
//                            Service.gI().point(pl);
//                            break;
//                        case 917:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 928, 929, 930);
//                            Service.gI().point(pl);
//                            break;
//                        case 918:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 925, 926, 927);
//                            Service.gI().point(pl);
//                            break;
//                        case 919:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 934, 935, 936);
//                            Service.gI().point(pl);
//                            break;
//                        case 1107:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1155, 1156, 1157);
//                            Service.gI().point(pl);
//                            break;
//                        case 2045:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 2060, 2061, 2062);
//                            Service.gI().point(pl);
//                            break;
//                        case 2046:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 2063, 2064, 2065);
//                            Service.gI().point(pl);
//                            break;
//                        case 2047:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 2066, 2067, 2068);
//                            Service.gI().point(pl);
//                            break;
//                        case 2048:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 2069, 2070, 2071);
//                            Service.gI().point(pl);
//                            break;
//                        case 2017:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 778, 779, 780);
//                            Service.gI().point(pl);
//                            break;
//                        case 2018:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 813, 814, 815);
//                            Service.gI().point(pl);
//                            break;
//                        case 2019:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 891, 892, 893);
//                            Service.gI().point(pl);
//                            break;
//                        case 2020:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 894, 895, 896);
//                            Service.gI().point(pl);
//                            break;
//                        case 2021:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 897, 898, 899);
//                            Service.gI().point(pl);
//                            break;
//                        case 2022:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 925, 926, 927);
//                            Service.gI().point(pl);
//                            break;
//                        case 2023:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 928, 929, 930);
//                            Service.gI().point(pl);
//                            break;
//                        case 2024:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 931, 932, 933);
//                            Service.gI().point(pl);
//                            break;
//                        case 2025:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 934, 935, 936);
//                            Service.gI().point(pl);
//                            break;
//                        case 2053:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1227, 1228, 1229);
//                            Service.gI().point(pl);
//                            break;
//                        case 1407:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 663, 664, 665);
//                            Service.gI().point(pl);
//                            break;
//                        case 1408:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1074, 1075, 1076);
//                            Service.gI().point(pl);
//                            break;
//                        case 1413:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1239, 1240, 1241);
//                            Service.gI().point(pl);
//                            break;
//                        case 1414:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1242, 1243, 1244);
//                            Service.gI().point(pl);
//                            break;
//                        case 1415:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1245, 1246, 1247);
//                            Service.gI().point(pl);
//                            break;
//                        case 1416:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1077, 1078, 1079);
//                            Service.gI().point(pl);
//                            break;
//                        case 1417:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 763, 764, 765);
//                            Service.gI().point(pl);
//                            break;
//                        case 1418:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 778, 779, 780);
//                            Service.gI().point(pl);
//                            break;
//                        case 1419:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 557, 558, 559);
//                            Service.gI().point(pl);
//                            break;
//                        case 1420:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 813, 814, 815);
//                            Service.gI().point(pl);
//                            break;
//                        case 1409:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1158, 1159, 1160);
//                            Service.gI().point(pl);
//                            break;
//                        case 1410:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1155, 1156, 1157);
//                            Service.gI().point(pl);
//                            break;
//                        case 1411:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1183, 1184, 1185);
//                            Service.gI().point(pl);
//                            break;
//                        case 1412:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1201, 1202, 1203);
//                            Service.gI().point(pl);
//                            break;
//                        case 1421:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1257, 1258, 1259);
//                            Service.gI().point(pl);
//                            break;
//                        case 1422:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1260, 1261, 1262);
//                            Service.gI().point(pl);
//                            break;
//                        case 1128:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1343, 1344, 1345);
//                            Service.gI().point(pl);
//                            break;
//                        case 1430:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1432, 1433, 1434);
//                            Service.gI().point(pl);
//                            break;
//                        case 1435:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1447, 1448, 1449);
//                            Service.gI().point(pl);
//                            break;
//                        case 1436:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1450, 1451, 1452);
//                            Service.gI().point(pl);
//                            break;
//                        case 1460:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1957, 1958, 1959);
//                            Service.gI().point(pl);
//                            break;
//                        case 1390:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1465, 1466, 1467);
//                            Service.gI().point(pl);
//                            break;
//                        case 1391:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1468, 1469, 1470);
//                            Service.gI().point(pl);
//                            break;
//                        case 1392:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1471, 1472, 1473);
//                            Service.gI().point(pl);
//                            break;
//                        case 1480:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1535, 1536, 1537);
//                            Service.gI().point(pl);
//                            break;
//                        case 1481:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1538, 1539, 1540);
//                            Service.gI().point(pl);
//                            break;
//                        case 1500:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1630, 1631, 1632);
//                            Service.gI().point(pl);
//                            break;
//                        case 908:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 891, 892, 893);
//                            Service.gI().point(pl);
//                            break;
//                        case 909:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 897, 898, 899);
//                            Service.gI().point(pl);
//                            break;
//                        case 910:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 894, 895, 896);
//                            Service.gI().point(pl);
//                            break;
//                        case 1018:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1183, 1184, 1185);
//                            Service.gI().point(pl);
//                            break;
//                        case 1019:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1201, 1202, 1203);
//                            Service.gI().point(pl);
//                            break;
//                        case 1020:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl, 1239, 1240, 1241);
//                            Service.gI().point(pl);
//                            break;
//                        case 1919: // thay id kiếm gỗ 
//                            pl.isUseKiemGo = !pl.isUseKiemGo;
//                            Service.gI().sendThongBao(pl, "Bạn đang " + (pl.isUseKiemGo ? " bật " : " tắt ") + item.template.name);
//                            break;

//                        case ConstDataEvent.ID_RUONG_1:
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//                                short id_ct_nangtienca = 681;
//                                int id_tuiqua = Util.nextInt(1327, 1328);
//
//                                Item ct_nangtienca = ItemService.gI().createNewItem(id_ct_nangtienca);
//                                ct_nangtienca.itemOptions.add(new ItemOption(77, 35));
//                                ct_nangtienca.itemOptions.add(new ItemOption(103, 35));
//                                ct_nangtienca.itemOptions.add(new ItemOption(50, 35));
//                                if (Util.isTrue(90, 100)) {
//                                    // nhows thay id option ngay
//                                    ct_nangtienca.itemOptions.add(new ItemOption(93, Util.nextInt(0, 30)));
//                                }
//
//                                Item tuiqua = ItemService.gI().createNewItem((short) id_tuiqua);
//
//                                tuiqua.itemOptions.add(new ItemOption(93, 3));
//
//                                Item thoivang = ItemService.gI().createNewItem((short) 457);
//                                thoivang.quantity = Util.nextInt(1, 10);
//
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//
//                                ArrayList<Item> list = new ArrayList<>();
//                                list.add(ct_nangtienca);
//                                list.add(tuiqua);
//                                list.add(thoivang);
//                                for (Item i : list) {
//                                    if (Util.isTrue(50, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 3 ô trống trong hành trang");
//                            }
//                            break;                                                                  
//                        case ConstDataEvent.ID_RUONG_2:
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//                                int idPetHo = Util.nextInt(1421, 1422);
//                                int id_tuiqua = Util.nextInt(1334, 1335);
//                                Item petHo = ItemService.gI().createNewItem((short) idPetHo);
//                                petHo.itemOptions.add(new ItemOption(77, 12));
//                                petHo.itemOptions.add(new ItemOption(103, 12));
//                                petHo.itemOptions.add(new ItemOption(50, 12));
//                                if (Util.isTrue(90, 100)) {
//                                    // nhows thay id option ngay
//                                    petHo.itemOptions.add(new ItemOption(93, Util.nextInt(0, 30)));
//                                    petHo.itemOptions.add(new ItemOption(95, Util.nextInt(0, 3)));
//                                } else {
//                                    petHo.itemOptions.add(new ItemOption(95, 3));
//                                }
//
//                                Item VanLuotSong = ItemService.gI().createNewItem((short) id_tuiqua);
//                                VanLuotSong.itemOptions.add(new ItemOption(93, 3));
//
//                                Item thoivang = ItemService.gI().createNewItem((short) 457);
//                                thoivang.quantity = Util.nextInt(1, 20);
//
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//
//                                ArrayList<Item> list = new ArrayList<>();
//                                list.add(petHo);
//                                list.add(VanLuotSong);
//                                list.add(thoivang);
//                                for (Item i : list) {
//                                    if (Util.isTrue(50, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 3 ô trống trong hành trang");
//                            }
//                            break;                                                                  
//                        case ConstDataEvent.ruongvip1:
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//                                short idPetHo = 2146;
//                                short idVanLuotSong = 2142;
//                                short idgay = 920;
//
//                                Item petHo = ItemService.gI().createNewItem(idPetHo);
//                                petHo.itemOptions.add(new ItemOption(77, 20));
//                                petHo.itemOptions.add(new ItemOption(103, 20));
//                                petHo.itemOptions.add(new ItemOption(50, 20));
//                                if (Util.isTrue(100, 100)) {
//                                    // nhows thay id option ngay
//                                }
//
//                                Item VanLuotSong = ItemService.gI().createNewItem(idVanLuotSong);
//                                VanLuotSong.itemOptions.add(new ItemOption(77, 80));
//                                VanLuotSong.itemOptions.add(new ItemOption(103, 80));
//                                VanLuotSong.itemOptions.add(new ItemOption(50, 80));
//                                VanLuotSong.itemOptions.add(new ItemOption(0, 20000));
//
//                                Item gay = ItemService.gI().createNewItem(idgay);
//                                gay.itemOptions.add(new ItemOption(50, 15));
//                                gay.itemOptions.add(new ItemOption(103, 15));
//                                gay.itemOptions.add(new ItemOption(77, 15));
//
//                                Item thoivang = ItemService.gI().createNewItem((short) 457);
//                                thoivang.quantity = Util.nextInt(200, 200);
//                                Item hongngoc = ItemService.gI().createNewItem((short) 861);
//                                hongngoc.quantity = Util.nextInt(200000, 200000);
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//
//                                ArrayList<Item> list = new ArrayList<>();
//                                list.add(petHo);
//                                list.add(VanLuotSong);
//                                list.add(gay);
//                                list.add(hongngoc);
//
//                                list.add(thoivang);
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                                list.add(hongngoc);
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 3 ô trống trong hành trang");
//                            }
//                              break;                                                                  
//                        case ConstDataEvent.ruongvip2:
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//                                short idPetHo = 2147;
//                                short idVanLuotSong = 2142;
//                                short idgay = 920;
//
//                                Item petHo = ItemService.gI().createNewItem(idPetHo);
//                                petHo.itemOptions.add(new ItemOption(77, 15));
//                                petHo.itemOptions.add(new ItemOption(103, 15));
//                                petHo.itemOptions.add(new ItemOption(50, 15));
//                                if (Util.isTrue(100, 100)) {
//                                    // nhows thay id option ngay
//                                }
//
//                                Item VanLuotSong = ItemService.gI().createNewItem(idVanLuotSong);
//                                VanLuotSong.itemOptions.add(new ItemOption(77, 60));
//                                VanLuotSong.itemOptions.add(new ItemOption(103, 60));
//                                VanLuotSong.itemOptions.add(new ItemOption(50, 60));
//                                VanLuotSong.itemOptions.add(new ItemOption(0, 10000));
//
//                                Item gay = ItemService.gI().createNewItem(idgay);
//                                gay.itemOptions.add(new ItemOption(50, 10));
//                                gay.itemOptions.add(new ItemOption(103, 10));
//                                gay.itemOptions.add(new ItemOption(77, 10));
//
//                                Item thoivang = ItemService.gI().createNewItem((short) 457);
//                                thoivang.quantity = Util.nextInt(50, 50);
//                                Item hongngoc = ItemService.gI().createNewItem((short) 861);
//                                hongngoc.quantity = Util.nextInt(100000, 100000);
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//
//                                ArrayList<Item> list = new ArrayList<>();
//                                list.add(petHo);
//                                list.add(VanLuotSong);
//                                list.add(gay);
//                                list.add(hongngoc);
//
//                                list.add(thoivang);
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                                list.add(hongngoc);
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 3 ô trống trong hành trang");
//                            }
//                              break;                                                                  
//                        case ConstDataEvent.ruongvip3:
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//                                short idPetHo = 2148;
//                                short idVanLuotSong = 2142;
//                                short idgay = 920;
//
//                                Item petHo = ItemService.gI().createNewItem(idPetHo);
//                                petHo.itemOptions.add(new ItemOption(77, 10));
//                                petHo.itemOptions.add(new ItemOption(103, 10));
//                                petHo.itemOptions.add(new ItemOption(50, 10));
//                                if (Util.isTrue(100, 100)) {
//                                    // nhows thay id option ngay
//                                }
//
//                                Item VanLuotSong = ItemService.gI().createNewItem(idVanLuotSong);
//                                VanLuotSong.itemOptions.add(new ItemOption(77, 40));
//                                VanLuotSong.itemOptions.add(new ItemOption(103, 40));
//                                VanLuotSong.itemOptions.add(new ItemOption(50, 40));
//                                VanLuotSong.itemOptions.add(new ItemOption(0, 5000));
//
//                                Item gay = ItemService.gI().createNewItem(idgay);
//                                gay.itemOptions.add(new ItemOption(50, 10));
//                                gay.itemOptions.add(new ItemOption(103, 10));
//                                gay.itemOptions.add(new ItemOption(77, 10));
//
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//
//                                ArrayList<Item> list = new ArrayList<>();
//                                list.add(petHo);
//                                list.add(VanLuotSong);
//
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 3 ô trống trong hành trang");
//                            }
//                              break;                                                                  
//                        case ConstDataEvent.top1sm:
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 10) {
//                                short idPetHo = 2153;
//                                short idVanLuotSong = 2140;
//
//                                Item petHo = ItemService.gI().createNewItem(idPetHo);
//                                petHo.itemOptions.add(new ItemOption(77, 10));
//                                petHo.itemOptions.add(new ItemOption(103, 10));
//                                petHo.itemOptions.add(new ItemOption(50, 10));
//                                if (Util.isTrue(100, 100)) {
//                                    // nhows thay id option ngay
//                                }
//
//                                Item VanLuotSong = ItemService.gI().createNewItem(idVanLuotSong);
//                                VanLuotSong.itemOptions.add(new ItemOption(77, 50));
//                                VanLuotSong.itemOptions.add(new ItemOption(103, 50));
//                                VanLuotSong.itemOptions.add(new ItemOption(50, 50));
//                                VanLuotSong.itemOptions.add(new ItemOption(0, 5000));
//
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//
//                                ArrayList<Item> list = new ArrayList<>();
//                                list.add(petHo);
//                                list.add(VanLuotSong);
//                                Item ngocrong1 = ItemService.gI().createNewItem((short) 14);
//                                ngocrong1.quantity = Util.nextInt(3, 3);
//                                Item ngocrong2 = ItemService.gI().createNewItem((short) 15);
//                                ngocrong2.quantity = Util.nextInt(3, 3);
//                                Item ngocrong3 = ItemService.gI().createNewItem((short) 16);
//                                ngocrong3.quantity = Util.nextInt(3, 3);
//                                Item ngocrong4 = ItemService.gI().createNewItem((short) 17);
//                                ngocrong4.quantity = Util.nextInt(3, 3);
//                                Item ngocrong5 = ItemService.gI().createNewItem((short) 18);
//                                ngocrong5.quantity = Util.nextInt(3, 3);
//                                Item ngocrong6 = ItemService.gI().createNewItem((short) 19);
//                                ngocrong6.quantity = Util.nextInt(3, 3);
//                                Item ngocrong7 = ItemService.gI().createNewItem((short) 20);
//                                ngocrong7.quantity = Util.nextInt(3, 3);
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 10 ô trống trong hành trang");
//                            }
//                              break;                                                                  
//                        case ConstDataEvent.top2sm:
//
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//
//                                short ngocrong1 = 14;
//                                short ngocrong2 = 15;
//                                short ngocrong3 = 16;
//                                short ngocrong4 = 17;
//                                short ngocrong5 = 18;
//                                short ngocrong6 = 19;
//
//                                if (Util.isTrue(100, 100)) {
//                                    // nhows thay id option ngay
//                                }
//
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 2);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//                                ArrayList<Item> list = new ArrayList<>();
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 10 ô trống trong hành trang");
//                            }
//                              break;                                                                  
//                        case ConstDataEvent.top3sm:
//
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//
//                                short ngocrong1 = 14;
//                                short ngocrong2 = 15;
//                                short ngocrong3 = 16;
//                                short ngocrong4 = 17;
//                                short ngocrong5 = 18;
//                                short ngocrong6 = 19;
//
//                                if (Util.isTrue(100, 100)) {
//                                    // nhows thay id option ngay
//                                }
//
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 2);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//                                ArrayList<Item> list = new ArrayList<>();
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 10 ô trống trong hành trang");
//                            }
//                              break;                                                                  
//                        case ConstDataEvent.top4sm:
//
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//
//                                short ngocrong1 = 14;
//                                short ngocrong2 = 15;
//                                short ngocrong3 = 16;
//                                short ngocrong4 = 17;
//                                short ngocrong5 = 18;
//                                short ngocrong6 = 19;
//
//                                if (Util.isTrue(100, 100)) {
//                                    // nhows thay id option ngay
//                                }
//
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//                                ArrayList<Item> list = new ArrayList<>();
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 10 ô trống trong hành trang");
//                            }
//                              break;                                                                  
//                        case ConstDataEvent.trumcuoi:
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 3) {
//                                short idPetHo = 2154;
//                                short idVanLuotSong = 2142;
//                                short idgay = 920;
//                                short idZamas = 2140;
//
//                                Item petHo = ItemService.gI().createNewItem(idPetHo);
//                                petHo.itemOptions.add(new ItemOption(77, 30));
//                                petHo.itemOptions.add(new ItemOption(103, 30));
//                                petHo.itemOptions.add(new ItemOption(50, 30));
//                                if (Util.isTrue(100, 100)) {
//                                    // nhows thay id option ngay
//                                }
//                                Item VanLuotSong = ItemService.gI().createNewItem(idVanLuotSong);
//                                VanLuotSong.itemOptions.add(new ItemOption(77, 120));
//                                VanLuotSong.itemOptions.add(new ItemOption(103, 120));
//                                VanLuotSong.itemOptions.add(new ItemOption(50, 120));
//                                VanLuotSong.itemOptions.add(new ItemOption(0, 40000));
//
//                                Item Zamas = ItemService.gI().createNewItem(idZamas);
//                                Zamas.itemOptions.add(new ItemOption(77, 100));
//                                Zamas.itemOptions.add(new ItemOption(103, 100));
//                                Zamas.itemOptions.add(new ItemOption(50, 100));
//                                Zamas.itemOptions.add(new ItemOption(0, 35000));
//
//                                Item gay = ItemService.gI().createNewItem(idgay);
//                                gay.itemOptions.add(new ItemOption(50, 25));
//                                gay.itemOptions.add(new ItemOption(103, 25));
//                                gay.itemOptions.add(new ItemOption(77, 25));
//
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//
//                                Item thoivang = ItemService.gI().createNewItem((short) 457);
//                                thoivang.quantity = Util.nextInt(800, 800);
//                                Item hongngoc = ItemService.gI().createNewItem((short) 861);
//                                hongngoc.quantity = Util.nextInt(800000, 800000);
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//
//                                InventoryServiceNew.gI().sendItemBags(pl);
//
//                                ArrayList<Item> list = new ArrayList<>();
//                                list.add(petHo);
//                                list.add(VanLuotSong);
//                                list.add(gay);
//                                list.add(hongngoc);
//                                list.add(Zamas);
//
//                                list.add(thoivang);
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                                list.add(hongngoc);
//                                for (Item i : list) {
//                                    if (Util.isTrue(100, 100)) {
//                                        InventoryServiceNew.gI().addItemBag(pl, i);
//                                        String mess = "Bạn vừa nhận được x" + i.quantity + " " + i.template.name;
//                                        Service.gI().sendThongBao(pl, mess);
//                                        InventoryServiceNew.gI().sendItemBags(pl);
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Cần 3 ô trống trong hành trang");
//                            }
//                              break;                                                                  
                        case 992:
                            if (pl.nPoint.power < 60000000000L) {
                                Service.gI().sendThongBao(pl, "Bạn cần 60 tỷ sm để sử dụng");
                                return;
                            }
                            List<Integer> listmapthoiVang_ = Arrays.asList(160, 161, 162, 163);
                            if (listmapthoiVang_.contains(pl.zone.map.mapId)) {
                                Service.gI().sendThongBao(pl, "Bạn đang ở hành tinh thực vật rồi !");
                                return;
                            } else {
                                ChangeMapService.gI().goToHanhTinhThucVat(pl);
                            }
                            break;
                        case 361:
                            if (pl.idNRNM != -1) {
                                Service.gI().sendThongBao(pl, "Có lỗi xảy ra với ngọc rồng namek");
                                return;
                            }
                            pl.idGo = (short) Util.nextInt(0, 6);
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.CONFIRM_TELE_NAMEC, -1, "1 Sao (" + NgocRongNamecService.gI().getDis(pl, 0, (short) 353) + " m)\n2 Sao (" + NgocRongNamecService.gI().getDis(pl, 1, (short) 354) + " m)\n3 Sao (" + NgocRongNamecService.gI().getDis(pl, 2, (short) 355) + " m)\n4 Sao (" + NgocRongNamecService.gI().getDis(pl, 3, (short) 356) + " m)\n5 Sao (" + NgocRongNamecService.gI().getDis(pl, 4, (short) 357) + " m)\n6 Sao (" + NgocRongNamecService.gI().getDis(pl, 5, (short) 358) + " m)\n7 Sao (" + NgocRongNamecService.gI().getDis(pl, 6, (short) 359) + " m)", "Đến ngay\nViên " + (pl.idGo + 1) + " Sao\n50 ngọc", "Kết thức");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;

                        case 293:
                            openGoiDau1(pl, item);
                            break;
                        case 294:
                            openGoiDau2(pl, item);
                            break;
                        case 295:
                            openGoiDau3(pl, item);
                            break;
                        case 296:
                            openGoiDau4(pl, item);
                            break;
                        case 297:
                            openGoiDau5(pl, item);
                            break;
                        case 298:
                            openGoiDau6(pl, item);
                            break;
                        case 299:
                            openGoiDau7(pl, item);
                            break;
                        case 596:
                            openGoiDau8(pl, item);
                            break;
                        case 597:
                            openGoiDau9(pl, item);
                            break;
//                        case 1194:
//                            if (pl.lastTimeTitle1 == 0) {
//                                pl.lastTimeTitle1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 3);
//                            } else {
//                                pl.lastTimeTitle1 += (1000 * 60 * 60 * 24 * 3);
//                            }
//                            pl.isTitleUse = true;
//                            Service.gI().point(pl);
//                            Service.gI().sendTitle(pl, 888);
//                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//                            Service.gI().sendThongBao(pl, "Bạn nhận được 3 ngày danh hiệu !");
//                            break;                                                                  
//                        case 1988:
//                            if (pl.lastTimeTitle2 == 0) {
//                                pl.lastTimeTitle2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
//                            } else {
//                                pl.lastTimeTitle2 += (1000 * 60 * 60 * 24 * 7);
//                            }
//                            pl.isTitleUse2 = true;
//                            Service.gI().point(pl);
//                            Service.gI().sendTitle(pl, 889);
//                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//                            Service.gI().sendThongBao(pl, "Bạn nhận được 7 ngày danh hiệu !");
//                            break;                                                                  
//                        case 1195:
//                            if (pl.lastTimeTitle3 == 0) {
//                                pl.lastTimeTitle3 += System.currentTimeMillis() + (86400000L * 30L);
//                            } else {
//                                pl.lastTimeTitle3 += (1000 * 60 * 60 * 24 * 30);
//                            }
//                            pl.isTitleUse3 = true;
//                            Service.gI().point(pl);
//                            Service.gI().sendTitle(pl, 890);
//                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//                            Service.gI().sendThongBao(pl, "Bạn nhận được 30 ngày danh hiệu !");
//
//                            break;                                                                  
                        case 457:
                        case 1108:

                            Service.gI().sendThongBao(pl, "Không thể sử dụng");
                            break;
                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 74: //đùi gà
                        case 191: //cà chua
                        case 192: //cà rốt
                            eatHpMp(pl, item);
                            break;

                        case 2127:

                            SkillService.gI().learSkillSpecial(pl, Skill.SUPER_KAME);
                            break;
                        case 2128:
                            SkillService.gI().learSkillSpecial(pl, Skill.MA_PHONG_BA);
                            break;
                        case 2129:

                            SkillService.gI().learSkillSpecial(pl, Skill.LIEN_HOAN_CHUONG);
                            break;
                        case 1996:
                            SkillService.gI().learSkillSpecial(pl, Skill.HAKAI);
                            break;
                        case 2117://hop qua 1/6
                            ItemService.gI().OpenItem2117(pl, item);
                            break;
                        case 2114://set tl kh

                            UseItem.gI().Hopdothanlinh(pl, item);
                            break;
                        case 2115://set hd kh

                            UseItem.gI().Hopdohuydiet(pl, item);
                            break;
                        case 2116://set ts kh
                            UseItem.gI().Hopdothiensu(pl, item);
                            break;
                        case 1667:
                            IntrinsicService.gI().setTanThu(pl);//set tan thu
                            break;
                        case 1908:
                            UseItem.gI().tuivang(pl, item);
                            break;
                        case 1909:
                            UseItem.gI().quagapthu(pl, item);
                            break;
                        case 1910:
                            UseItem.gI().quagapthu2(pl, item);
                            break;
//                        case 992:
//                            if (pl.nPoint.power < 60000000000L) {
//                                Service.gI().sendThongBao(pl, "Bạn cần 60 tỷ sm để sử dụng");
//                                return;
//                            }
//                            List<Integer> listmapthoiVang_ = Arrays.asList(160, 161, 162, 163);
//                            if (listmapthoiVang_.contains(pl.zone.map.mapId)) {
//                                Service.gI().sendThongBao(pl, "Bạn đang ở hành tinh thực vật rồi !");
//                                return;
//                            } else {
//                                ChangeMapService.gI().goToHanhTinhThucVat(pl);
//                            }
//                              break;                                                                  
                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22).count() < 3) {
                                Service.gI().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                            } else {
                                Service.gI().sendThongBao(pl, "Chỉ được đặt 3 vệ tinh trong map");
                            }
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò capsule
                        case 902:
                        case 903:
                        case 900:
                        case 899:
                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case 752:
                        case 753:
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1103:
                        case 2157:
                        case 2158:
                        case 2159:
                        case 2160:
                        case 2161:
                        case 2166:
                        case 2189:
                        case 2196:
                        case 2197:
                        case 2198:
                        case 2199:
                        case 579:
                        case 890:
                        case 891:
                        case 1601:
                        case 1602:
                        case 1603:
                        case 1604:
                        case 1605:
                            useItemTime(pl, item);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
//                        case 361:
//                            if (pl.idNRNM != -1) {
//                                Service.gI().sendThongBao(pl, "Không thể thực hiện");
//                                return;
//                            }
//                            pl.idGo = (short) Util.nextInt(0, 6);
//                            NpcService.gI().createMenuConMeo(pl, ConstNpc.CONFIRM_TELE_NAMEC, -1, "1 Sao (" + NgocRongNamecService.gI().getDis(pl, 0, (short) 353) + " m)\n2 Sao (" + NgocRongNamecService.gI().getDis(pl, 1, (short) 354) + " m)\n3 Sao (" + NgocRongNamecService.gI().getDis(pl, 2, (short) 355) + " m)\n4 Sao (" + NgocRongNamecService.gI().getDis(pl, 3, (short) 356) + " m)\n5 Sao (" + NgocRongNamecService.gI().getDis(pl, 4, (short) 357) + " m)\n6 Sao (" + NgocRongNamecService.gI().getDis(pl, 5, (short) 358) + " m)\n7 Sao (" + NgocRongNamecService.gI().getDis(pl, 6, (short) 359) + " m)", "Đến ngay\nViên " + (pl.idGo + 1) + " Sao\n50 ngọc", "Kết thức");
//                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//                            InventoryServiceNew.gI().sendItemBags(pl);
//                              break;                                                                  
                        case 454: //bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 921: //bông tai
                            UseItem.gI().usePorata2(pl);
                            break;
                        case 2064: //bông tai
                            if (pl.nPoint.power < 80000000000L) {
                                Service.gI().sendThongBao(pl, "Bạn cần 80 tỷ sm để sử dụng");
                                return;
                            }
                            UseItem.gI().usePorata3(pl);
                            break;
                        case 2113:
                            if (pl.nPoint.power < 150000000000L) {
                                Service.gI().sendThongBao(pl, "Bạn cần 150 tỷ sm để sử dụng");
                                return;
                            }
                            UseItem.gI().usePorata4(pl);

                            break;
                        case 1324:
                            if (pl.nPoint.power < 200000000000L) {
                                Service.gI().sendThongBao(pl, "Bạn cần 200 tỷ sm để sử dụng");
                                return;
                            }
                            UseItem.gI().usePorata5(pl);

                            break;
                        case 1472:
                            if (pl.nPoint.power < 500000000000L) {
                                Service.gI().sendThongBao(pl, "Bạn cần 500 tỷ sm để sử dụng");
                                return;
                            }
                            UseItem.gI().usePorata6(pl);
                            break;

                        case 193: //gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                        case 570:
                            openWoodChest(pl, item);
                            break;
//                        case 1108: //đổi đệ tử
//                            changePetBerus(pl, item);
//                              break;                                                                  
                        case 1109:
                            changePetBerusBiNgo(pl, item);
//                            UseItem.gI().tuivang(pl, item);
                            break;
                        case 1110:
                            changePetZamasu(pl, item);
//                            UseItem.gI().tuivang(pl, item);
                            break;
                        case 1111:
                            changePetDaishinkan(pl, item);
//                            UseItem.gI().tuivang(pl, item);
                            break;
                        case 1112:
                            changePetWhis(pl, item);
//                            UseItem.gI().tuivang(pl, item);
                            break;
                        case 628:
                            openPhieuCaiTrangHaiTac(pl, item);

                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                            upSkillPet(pl, item);
                            break;
                        case 2123:

                            openDaBaoVe(pl, item);
                            break;
                        case 2124:

//                            openSPL(pl, item);
                            openAllSPL(pl, item);
                            break;
                        case 2125:

                            openDaNangCap(pl, item);
                            break;
                        case 2126:

                            openManhTS(pl, item);
                            break;
                        case 2000://hop qua skh, item 2000 td
                        case 2001://hop qua skh, item 2001 nm
                        case 2002://hop qua skh, item 2002 xd

                            UseItem.gI().ItemSKH(pl, item);
                            break;

                        case 1618:

                            UseItem.gI().saoPhaLeTrungCap(pl, item);
                            break;
                        case 1619:
                            UseItem.gI().saoPhaLeCaoCap(pl, item);
                            break;
                        case 2085:
                        case 2086:
                        case 2087:

                            UseItem.gI().itemDoHuyDiet(pl, item);
                        case 2013://hop qua skh, item 2003 td
                        case 2014://hop qua skh, item 2004 nm
                        case 2015://hop qua skh, item 2005 xd

                            UseItem.gI().itemDoThanLinh(pl, item);
                            break;
                        case 2003://hop qua skh, item 2003 td
                        case 2004://hop qua skh, item 2004 nm
                        case 2005://hop qua skh, item 2005 xd

                            UseItem.gI().ItemDHD(pl, item);
                            break;
                        case 736:

                            ItemService.gI().OpenItem736(pl, item);
                            break;
                        case 987:

                            Service.gI().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); //đá bảo vệ
                            break;
                        case 2006:
//                            if (item.quantity > 10) {
//                                Service.gI().sendThongBaoOK(pl, "Có Bug không đó :3");
//                                PlayerService.gI().banPlayer((pl));
//                                Service.gI().sendThongBao(pl, "Bạn bị ban thành công");
//                                return;
//                            }
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                        case 2145:

                            UseItem.gI().hopquavnd1(pl);
                            break;
                        case 2146:

                            UseItem.gI().hopquavnd2(pl);
                            break;
                        case 2134:

                            UseItem.gI().hopquat1nap(pl);
                            break;
                        case 2135:

                            UseItem.gI().hopquat2nap(pl);
                            break;
                        case 2136:

                            UseItem.gI().hopquat3nap(pl);
                            break;
                        case 2137:

                            UseItem.gI().hopquat45nap(pl);
                            break;
                        case 2138:

                            UseItem.gI().hopquat610nap(pl);
                            break;
                        case 2139:

                            UseItem.gI().hopquamocnap300(pl);
                            break;
                        case 2140:

                            UseItem.gI().hopquamocnap500(pl);
                            break;
                        case 2141:

                            UseItem.gI().hopquamocnap1000(pl);
                            break;
                        case 2167:

                            UseItem.gI().Top1nv(pl);
                            break;
                        case 2168:

                            UseItem.gI().Top2nv(pl);
                            break;
                        case 2169:

                            UseItem.gI().Top3nv(pl);
                            break;
                        case 2170:

                            UseItem.gI().Top4nv(pl);
                            break;
                        case 2171:

                            UseItem.gI().Top1sm(pl);
                            break;
                        case 2172:

                            UseItem.gI().Top2sm(pl);
                            break;
                        case 2173:
                            UseItem.gI().Top3sm(pl);
                            break;
                        case 2174:
                            UseItem.gI().Top4sm(pl);
                            break;
                        case 2175:
                            UseItem.gI().Top5sm(pl);
                            break;
                        case 2228:
                            UseItem.gI().Tuingocxanh(pl);
                            break;
                        case 2229:
                            UseItem.gI().Tuingochong(pl);
                            break;
                        case 2230:
                            UseItem.gI().Itemsieucap(pl);
                            break;
                        case 1989:
                            openboxsukien(pl, item, 4);
                            break;
                        case 2028:
                            UseItem.gI().openLinhthu(pl, item);
                            break;
                        case 1331://hop qua
                            OpenhopThucuoi(pl, item);
                            break;
                        case 1327:
                            Openhopct(pl, item);
                            break;
                        case 1328:
                            Openhopflagbag(pl, item);
                            break;
                        case 1334://hop qua
                            Openhoppet(pl, item);
                            break;
                        case 1335:
                            OpenHopQuaNhapHoc(pl, item);
                            break;
                        case 722:
                            Openhop8_3(pl, item);
                            break;
                        case 1664: // Hoàng Cuc Đan
                        {
                            int requiredCap = 1;
                            if (pl.capTT != requiredCap) {
                                Service.gI().sendThongBao(pl, "Cảnh giới chưa đạt yêu cầu để sử dụng Hoàng Cúc Đan");
                                break;
                            } else {
                                int damegIncrease = 10_000;
                                int hpMpIncrease = 30_000;
                                int maxDameg = 3_600_000;
                                int maxHpMp = maxDameg * 3;
                                boolean avaiable = true;
                                // Kiểm tra giới hạn hiện tại
                                boolean damegMaxed = pl.nPoint.dameg >= maxDameg;
                                boolean hpMpMaxed = pl.nPoint.hpg >= maxHpMp && pl.nPoint.mpg >= maxHpMp;

                                if (damegMaxed && hpMpMaxed) {
                                    Service.gI().sendThongBao(pl, "Đã đạt giới hạn, không thể sử dụng vật phẩm");
                                    avaiable = false;
                                    break;
                                }

                                // Tăng chỉ số nếu chưa đạt giới hạn
                                if (avaiable) {
                                    if (!damegMaxed) {
                                        pl.nPoint.dameg = Math.min(pl.nPoint.dameg + damegIncrease, maxDameg);
                                    }
                                    if (!hpMpMaxed) {
                                        pl.nPoint.hpg = Math.min(pl.nPoint.hpg + hpMpIncrease, maxHpMp);
                                        pl.nPoint.mpg = Math.min(pl.nPoint.mpg + hpMpIncrease, maxHpMp);
                                    }
                                    // Cập nhật trạng thái
                                    Service.gI().point(pl);
                                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                    InventoryServiceNew.gI().sendItemBags(pl);
                                    Service.gI().sendThongBao(pl, "Bạn nhận được SD, HP và KI");
                                }
                                break;
                            }
                        }
                        case 1666: // Trúc Cơ Đan
                        {
                            int requiredCap = 1;
                            if (pl.capTT != requiredCap) {
                                Service.gI().sendThongBao(pl, "Cảnh giới chưa đạt yêu cầu để sử dụng Trúc Cơ Đan");
                                break;
                            } else {
                                int requiredDame = 3_600_000;
                                int requiredHpKi = requiredDame * 3;

                                int damegIncrease = 1_000_000;
                                int hpMpIncrease = damegIncrease * 3;

                                int maxDameg = 4_600_000;
                                int maxHpMp = maxDameg * 3;
                                boolean avaiable = true;

                                // Kiểm tra nếu chưa đạt điều kiện đột phá
                                boolean notEligible = pl.nPoint.dameg < requiredDame
                                        || pl.nPoint.hpg < requiredHpKi
                                        || pl.nPoint.mpg < requiredHpKi;

                                if (notEligible) {
                                    avaiable = false;
                                    Service.gI().sendThongBao(pl, "Chưa xong luyện khí mà đòi trúc cơ à mày !!!");
                                    break;
                                }

                                // Kiểm tra nếu đã vượt ngưỡng yêu cầu
                                boolean alreadyBeyond = pl.nPoint.dameg >= requiredDame
                                        || pl.nPoint.hpg >= requiredHpKi
                                        || pl.nPoint.mpg >= requiredHpKi;

                                if (alreadyBeyond) {
                                    avaiable = false;
                                    Service.gI().sendThongBao(pl, "Mạnh rồi trúc cơ gì nữa !!!");
                                    break;
                                }
                                if (avaiable) {

                                    // Nếu đủ điều kiện, tiến hành đột phá Trúc Cơ
                                    pl.nPoint.dameg = Math.min(pl.nPoint.dameg + damegIncrease, maxDameg);
                                    pl.nPoint.hpg = Math.min(pl.nPoint.hpg + hpMpIncrease, maxHpMp);
                                    pl.nPoint.mpg = Math.min(pl.nPoint.mpg + hpMpIncrease, maxHpMp);
                                    pl.isUseTrucCoDan = true;
                                    Service.gI().point(pl);
                                    Service.gI().sendThongBao(pl, "Cảnh giới của bạn đã hoàn mỹ, lần tiếp theo đột phá vì Thiên Đạo Trúc Cơ");

                                    // Trừ vật phẩm
                                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                    InventoryServiceNew.gI().sendItemBags(pl);
                                    break;
                                }
                            }
                        }
                        case 1665:// truc co so ky
                        {
                            int requiredCap = 2;
                            if (pl.capTT != requiredCap) {
                                Service.gI().sendThongBao(pl, "Cảnh giới chưa đạt yêu cầu để sử dụng Long Tủy Đan");
                                break;
                            } else {
                                int requiredDame = 3_600_000;
                                int requiredHpKi = requiredDame * 3;

                                int damegIncrease = 50_000;
                                int hpMpIncrease = damegIncrease * 3;

                                int maxDameg;
                                int maxHpMp;
                                if (pl.isUseTrucCoDan) {
                                    maxDameg = 7_000_000;
                                    maxHpMp = maxDameg * 3;
                                } else {
                                    maxDameg = 6_000_000;
                                    maxHpMp = maxDameg * 3;
                                }
                                boolean notEligible = pl.nPoint.dameg < requiredDame
                                        || pl.nPoint.hpg < requiredHpKi
                                        || pl.nPoint.mpg < requiredHpKi;

                                if (notEligible) {
                                    Service.gI().sendThongBao(pl, "Éo dùng được đâu em, lo mà về tu luyện tiếp !!!");
                                    break;
                                }
                                // Kiểm tra nếu đã vượt ngưỡng yêu cầu
                                boolean alreadyBeyond = pl.nPoint.dameg >= maxDameg
                                        || pl.nPoint.hpg >= maxHpMp
                                        || pl.nPoint.mpg >= maxHpMp;

                                if (alreadyBeyond) {
                                    Service.gI().sendThongBao(pl, "Mạnh rồi !!!");
                                    break;
                                }
                                pl.nPoint.dameg = Math.min(pl.nPoint.dameg + damegIncrease, maxDameg);
                                pl.nPoint.hpg = Math.min(pl.nPoint.hpg + hpMpIncrease, maxHpMp);
                                pl.nPoint.mpg = Math.min(pl.nPoint.mpg + hpMpIncrease, maxHpMp);
                                Service.gI().point(pl);
                                Service.gI().sendThongBao(pl, "Bạn nhận được SD, HP và KI");
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                break;
                            }
                        }
                        case 1693: {// truc co trung ky
                            int requiredCap = 2;
                            if (pl.capTT != requiredCap) {
                                Service.gI().sendThongBao(pl, "Cảnh giới chưa đạt yêu cầu để sử dụng Chân Nguyên Đan");
                                break;
                            } else {
                                int requiredDame = 6_000_000;
                                int requiredHpKi = requiredDame * 3;

                                int damegIncrease = 50_000;
                                int hpMpIncrease = damegIncrease * 3;

                                int maxDameg;
                                int maxHpMp;
                                if (pl.isUseTrucCoDan) {
                                    maxDameg = 13_000_000;
                                    maxHpMp = maxDameg * 3;
                                } else {
                                    maxDameg = 12_000_000;
                                    maxHpMp = maxDameg * 3;
                                }

                                boolean notEligible = pl.nPoint.dameg < requiredDame
                                        || pl.nPoint.hpg < requiredHpKi
                                        || pl.nPoint.mpg < requiredHpKi;

                                if (notEligible) {
                                    Service.gI().sendThongBao(pl, "Éo dùng được đâu em, lo mà về tu luyện tiếp !!!");
                                    break;
                                }
                                // Kiểm tra nếu đã vượt ngưỡng yêu cầu
                                boolean alreadyBeyond = pl.nPoint.dameg >= maxDameg
                                        || pl.nPoint.hpg >= maxHpMp
                                        || pl.nPoint.mpg >= maxHpMp;

                                if (alreadyBeyond) {
                                    Service.gI().sendThongBao(pl, "Mạnh rồi !!!");
                                    break;
                                }
                                pl.nPoint.dameg = Math.min(pl.nPoint.dameg + damegIncrease, maxDameg);
                                pl.nPoint.hpg = Math.min(pl.nPoint.hpg + hpMpIncrease, maxHpMp);
                                pl.nPoint.mpg = Math.min(pl.nPoint.mpg + hpMpIncrease, maxHpMp);
                                Service.gI().point(pl);
                                Service.gI().sendThongBao(pl, "Bạn nhận được SD, HP và KI");
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                break;
                            }
                        }
                        case 1694: {// truc co hau ky
                            int requiredCap = 2;
                            if (pl.capTT != requiredCap) {
                                Service.gI().sendThongBao(pl, "Cảnh giới chưa đạt yêu cầu để sử dụng Ngũ Hành Ngưng Đan");
                                break;
                            } else {
                                int requiredDame = 12_000_000;
                                int requiredHpKi = requiredDame * 3;

                                int damegIncrease = 50_000;
                                int hpMpIncrease = damegIncrease * 3;

                                int maxDameg;
                                int maxHpMp;
                                if (pl.isUseTrucCoDan) {
                                    maxDameg = 19_000_000;
                                    maxHpMp = maxDameg * 3;
                                } else {
                                    maxDameg = 18_000_000;
                                    maxHpMp = maxDameg * 3;
                                }

                                boolean notEligible = pl.nPoint.dameg < requiredDame
                                        || pl.nPoint.hpg < requiredHpKi
                                        || pl.nPoint.mpg < requiredHpKi;

                                if (notEligible) {
                                    Service.gI().sendThongBao(pl, "Éo dùng được đâu em, lo mà về tu luyện tiếp !!!");
                                    return;
                                }
                                // Kiểm tra nếu đã vượt ngưỡng yêu cầu
                                boolean alreadyBeyond = pl.nPoint.dameg >= maxDameg
                                        || pl.nPoint.hpg >= maxHpMp
                                        || pl.nPoint.mpg >= maxHpMp;

                                if (alreadyBeyond) {
                                    Service.gI().sendThongBao(pl, "Mạnh rồi !!!");
                                    return;
                                }
                                pl.nPoint.dameg = Math.min(pl.nPoint.dameg + damegIncrease, maxDameg);
                                pl.nPoint.hpg = Math.min(pl.nPoint.hpg + hpMpIncrease, maxHpMp);
                                pl.nPoint.mpg = Math.min(pl.nPoint.mpg + hpMpIncrease, maxHpMp);
                                Service.gI().point(pl);
                                Service.gI().sendThongBao(pl, "Bạn nhận được SD, HP và KI");
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                break;
                            }
                        }
                        case 1695: // pháp nguyên đan
                        {
                            int requiredCap = 3;
                            if (pl.capTT != requiredCap) {
                                Service.gI().sendThongBao(pl, "Cảnh giới chưa đạt yêu cầu để sử dụng Ngũ Hành Ngưng Đan");
                                break;
                            } else {
                                break;
                            }
                        }
                        case 1696: {
                            break;
                        }
                        case 1709: {
                            long amount = 5_000_000_000L;
                            pl.nPoint.powerUp(amount);
                            PlayerService.gI().sendTNSM(pl, (byte)0, amount);
                            Service.gI().sendThongBao(pl, "Bạn nhận 5 tỷ tu vi");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;
                        }

                    }

            }
        }
    }

    private void openDaBaoVe(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {987, 987};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(30, 1));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private int initBaseOptionSaoPhaLe(Item item) {
        int optionId = 73;
        switch (item.template.id) {
            case 441: //hút máu
                optionId = 95;
                break;
            case 442: //hút ki
                optionId = 96;
                break;
            case 443: //phản sát thương
                optionId = 97;
                break;
            case 444:
                optionId = 98;
                break;
            case 445:
                optionId = 156;
                break;
            case 446: //vàng
                optionId = 100;
                break;
            case 447: //tnsm
                optionId = 101;
                break;
        }
        return optionId;
    }

    private int initBaseOptionLinhthu(Item item) {
        int optionId = 73;
        switch (item.template.id) {
            case 441: //hút máu
                optionId = 95;
                break;
            case 442: //hút ki
                optionId = 96;
                break;
            case 443: //phản sát thương
                optionId = 97;
                break;
            case 444:
                optionId = 98;
                break;
            case 445:
                optionId = 156;
                break;
            case 446: //vàng
                optionId = 100;
                break;
            case 447: //tnsm
                optionId = 101;
                break;
        }
        return optionId;
    }

    private void openLinhthu(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] linhthu = {2209, 2210, 2211, 2212, 2213, 2214, 2215, 2216, 2234, 1227, 1233, 1234, 1235, 1236, 1237, 1238, 1239, 1240, 1241, 1242, 1245, 1246, 1285, 1286, 1287};
            byte rd = (byte) Util.nextInt(0, linhthu.length - 1); // Đã chỉnh lại để chọn loại ngẫu nhiên trong khoảng đầy đủ
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            // Chọn loại vật phẩm ngẫu nhiên từ danh sách 'spl'
            short selectedItemType = linhthu[rd];
            Item newItem = ItemService.gI().createNewItem(selectedItemType);

            // Thêm option tương ứng dựa trên loại vật phẩm đã chọn
//            newItem.itemOptions.add(new ItemOption(initBaseOptionLinhthu(newItem), 5));
            newItem.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
            newItem.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
            newItem.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
            newItem.itemOptions.add(new ItemOption(14, Util.nextInt(1, 10)));

            newItem.itemOptions.add(new ItemOption(95, Util.nextInt(1, 5)));
            newItem.itemOptions.add(new ItemOption(96, Util.nextInt(1, 5)));
            newItem.quantity = 1;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openAllSPL(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 7) {
            short[] spl = {441, 442, 443, 444, 445, 446, 447};
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            for (short selectedItemType : spl) {
                Item newItem = ItemService.gI().createNewItem(selectedItemType);
                newItem.itemOptions.add(new ItemOption(initBaseOptionSaoPhaLe(newItem), 5));

                // Ngẫu nhiên số lượng từ 2 đến 5 viên
                newItem.quantity = (short) Util.nextInt(2, 5);

                InventoryServiceNew.gI().addItemBag(player, newItem);
                Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
                icon[1] = newItem.template.iconID;
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);

        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openDaNangCap(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 5) {
            short[] possibleItems = {220, 221, 222, 223, 224};
//            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            for (short selectedItemType : possibleItems) {
                Item newItem = ItemService.gI().createNewItem(selectedItemType);
                //newItem.itemOptions.add(new ItemOption(93, 15));
                newItem.quantity = (short) Util.nextInt(20, 40);
                InventoryServiceNew.gI().addItemBag(player, newItem);
                Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
                icon[1] = newItem.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    public void UseCard(Player pl, Item item) {
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id)
                .findFirst().orElse(null);
        if (radarTemplate == null) {
            return;
        }
        if (radarTemplate.Require != -1) {
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream()
                    .filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null);
            if (radarRequireTemplate == null) {
                return;
            }
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);
//            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel) {
//                Service.gI().sendThongBao(pl, "Bạn cần sưu tầm " + radarRequireTemplate.Name + " ở cấp độ "
//                        + radarTemplate.RequireLevel + " mới có thể sử dụng thẻ này");
//                return;
//            }
        }
        Card card = pl.Cards.stream().filter(r -> r.Id == item.template.id).findFirst().orElse(null);
        if (card == null) {
            Card newCard = new Card(item.template.id, (byte) 1, radarTemplate.Max, (byte) -1, radarTemplate.Options);
            if (pl.Cards.add(newCard)) {
                RadarService.gI().RadarSetAmount(pl, newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl, newCard.Id, newCard.Level);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {
            if (card.Level >= 2) {
                Service.gI().sendThongBao(pl, "Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount) {
                card.Amount = 0;
                if (card.Level == -1) {
                    card.Level = 1;
                } else {
                    card.Level++;
                }
                Service.gI().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl, card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl, card.Id, card.Level);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        }
    }

    private void openGoiDau1(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {13, 13};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau2(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {60, 60};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau3(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {61, 61};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau4(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {62, 62};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau5(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {63, 63};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau6(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {64, 64};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau7(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {65, 65};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau8(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {352, 352};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openGoiDau9(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {523, 523};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.quantity = 30;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void Itemsieucap(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 4) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 4 ô trống hành trang");
                return;
            }
            Item itemsieucap = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2230) {
                    itemsieucap = item;
                    break;
                }
            }
            if (itemsieucap != null) {
                Item cn = ItemService.gI().createNewItem((short) 1099);
                Item bh = ItemService.gI().createNewItem((short) 1100);
                Item gx = ItemService.gI().createNewItem((short) 1101);
                Item bk = ItemService.gI().createNewItem((short) 1102);
                Item ad = ItemService.gI().createNewItem((short) 1103);
                int ql = 1;

                cn.quantity = ql;
                bh.quantity = ql;
                gx.quantity = ql;
                bk.quantity = ql;
                ad.quantity = ql;

                cn.itemOptions.add(new Item.ItemOption(30, 0));
                bh.itemOptions.add(new Item.ItemOption(30, 0));
                gx.itemOptions.add(new Item.ItemOption(30, 0));
                bk.itemOptions.add(new Item.ItemOption(30, 0));
                ad.itemOptions.add(new Item.ItemOption(30, 0));

                InventoryServiceNew.gI().subQuantityItemsBag(pl, itemsieucap, 1);
                InventoryServiceNew.gI().addItemBag(pl, cn);
                InventoryServiceNew.gI().addItemBag(pl, bh);
                InventoryServiceNew.gI().addItemBag(pl, gx);
                InventoryServiceNew.gI().addItemBag(pl, bk);
                InventoryServiceNew.gI().addItemBag(pl, ad);

                InventoryServiceNew.gI().sendItemBags(pl);

                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + cn.template.name);
//           
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + bh.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gx.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + bk.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ad.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top1nv(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 5) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 6 ô trống hành trang");
                return;
            }
            Item top1nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2167) {
                    top1nv = item;
                    break;
                }
            }
            if (top1nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1139);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
                Item ct = ItemService.gI().createNewItem((short) 2045);
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);

                thoiVang_.quantity = 200;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;

                ct.itemOptions.add(new ItemOption(50, 10));
                ct.itemOptions.add(new ItemOption(77, 10));
                ct.itemOptions.add(new ItemOption(103, 10));
                ct.itemOptions.add(new ItemOption(5, 10));
//                ct.itemOptions.add(new ItemOption(98, 10));

//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, top1nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
                InventoryServiceNew.gI().addItemBag(pl, ct);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top2nv(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 6) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 7 ô trống hành trang");
                return;
            }
            Item top2nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2168) {
                    top2nv = item;
                    break;
                }
            }
            if (top2nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1139);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
                Item ct = ItemService.gI().createNewItem((short) 2048);
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);

                thoiVang_.quantity = 100;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;

                ct.itemOptions.add(new ItemOption(50, 10));
                ct.itemOptions.add(new ItemOption(77, 10));
                ct.itemOptions.add(new ItemOption(103, 10));
                ct.itemOptions.add(new ItemOption(5, 10));
//                ct.itemOptions.add(new ItemOption(98, 10));

//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, top2nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
                InventoryServiceNew.gI().addItemBag(pl, ct);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top3nv(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 6) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 7 ô trống hành trang");
                return;
            }
            Item top3nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2169) {
                    top3nv = item;
                    break;
                }
            }
            if (top3nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1139);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
//                Item ct = ItemService.gI().createNewItem((short) 0);
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);

                thoiVang_.quantity = 70;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;

//                ct.itemOptions.add(new ItemOption(50, 10));
//                ct.itemOptions.add(new ItemOption(77, 10));
//                ct.itemOptions.add(new ItemOption(103, 10));
//                ct.itemOptions.add(new ItemOption(5, 10));
//                ct.itemOptions.add(new ItemOption(98, 10));
//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, top3nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
//                InventoryServiceNew.gI().addItemBag(pl, ct);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top4nv(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 6) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 7 ô trống hành trang");
                return;
            }
            Item top1nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2170) {
                    top1nv = item;
                    break;
                }
            }
            if (top1nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1139);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
//                Item ct = ItemService.gI().createNewItem((short) 0);
//                Item nr1s = ItemService.gI().createNewItem((short) 14);
//                Item nr2s = ItemService.gI().createNewItem((short) 15);
//                Item nr3s = ItemService.gI().createNewItem((short) 16);

                thoiVang_.quantity = 50;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
//                nr1s.quantity = 1;
//                nr2s.quantity = 1;
//                nr3s.quantity = 1;

//                ct.itemOptions.add(new ItemOption(50, 10));
//                ct.itemOptions.add(new ItemOption(77, 10));
//                ct.itemOptions.add(new ItemOption(103, 10));
//                ct.itemOptions.add(new ItemOption(5, 10));
////                ct.itemOptions.add(new ItemOption(98, 10));
//
//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, top1nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
//                InventoryServiceNew.gI().addItemBag(pl, ct);
//                InventoryServiceNew.gI().addItemBag(pl, nr1s);
//                InventoryServiceNew.gI().addItemBag(pl, nr2s);
//                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top1sm(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 5) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 6 ô trống hành trang");
                return;
            }
            Item top1nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2171) {
                    top1nv = item;
                    break;
                }
            }
            if (top1nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 2006);
                Item dhd = ItemService.gI().createNewItem((short) 2006);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
                Item ct = ItemService.gI().createNewItem((short) 2008);
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);

                thoiVang_.quantity = 100;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;

                ct.itemOptions.add(new ItemOption(50, 39));
                ct.itemOptions.add(new ItemOption(77, 39));
                ct.itemOptions.add(new ItemOption(103, 39));
                ct.itemOptions.add(new ItemOption(5, 10));
//                ct.itemOptions.add(new ItemOption(98, 10));

                dhd.itemOptions.add(new Item.ItemOption(30, 0));

                InventoryServiceNew.gI().subQuantityItemsBag(pl, top1nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
//                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
                InventoryServiceNew.gI().addItemBag(pl, ct);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top2sm(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 6) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 7 ô trống hành trang");
                return;
            }
            Item top2nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2172) {
                    top2nv = item;
                    break;
                }
            }
            if (top2nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1139);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
                Item ct = ItemService.gI().createNewItem((short) 2009);
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);

                thoiVang_.quantity = 80;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;

                ct.itemOptions.add(new ItemOption(50, 38));
                ct.itemOptions.add(new ItemOption(77, 38));
                ct.itemOptions.add(new ItemOption(103, 38));
                ct.itemOptions.add(new ItemOption(5, 10));
//                ct.itemOptions.add(new ItemOption(98, 10));

//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, top2nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
                InventoryServiceNew.gI().addItemBag(pl, ct);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top3sm(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 6) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 7 ô trống hành trang");
                return;
            }
            Item top3nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2173) {
                    top3nv = item;
                    break;
                }
            }
            if (top3nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 730);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
                Item ct = ItemService.gI().createNewItem((short) 730);
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);

                thoiVang_.quantity = 50;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;

                ct.itemOptions.add(new ItemOption(50, 35));
                ct.itemOptions.add(new ItemOption(77, 35));
                ct.itemOptions.add(new ItemOption(103, 35));
                ct.itemOptions.add(new ItemOption(5, 3));
//                ct.itemOptions.add(new ItemOption(98, 10));

//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, top3nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
                InventoryServiceNew.gI().addItemBag(pl, ct);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top4sm(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 6) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 7 ô trống hành trang");
                return;
            }
            Item top1nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2174) {
                    top1nv = item;
                    break;
                }
            }
            if (top1nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1139);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
//                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
                Item ct = ItemService.gI().createNewItem((short) (pl.gender + 1010));
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);

//                thoiVang_.quantity = 50;
//                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
//                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;

                ct.itemOptions.add(new ItemOption(50, 30));
                ct.itemOptions.add(new ItemOption(77, 30));
                ct.itemOptions.add(new ItemOption(103, 30));
                ct.itemOptions.add(new ItemOption(5, 10));
////                ct.itemOptions.add(new ItemOption(98, 10));
//
//                dhd.itemOptions.add(new Item.ItemOption(30, 0));

                InventoryServiceNew.gI().subQuantityItemsBag(pl, top1nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
//                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
                InventoryServiceNew.gI().addItemBag(pl, ct);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Top5sm(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 1) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            Item top1nv = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2175) {
                    top1nv = item;
                    break;
                }
            }
            if (top1nv != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1139);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
//                Item ct = ItemService.gI().createNewItem((short) 0);
//                Item nr1s = ItemService.gI().createNewItem((short) 14);
//                Item nr2s = ItemService.gI().createNewItem((short) 15);
//                Item nr3s = ItemService.gI().createNewItem((short) 16);

                thoiVang_.quantity = 50;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
//                nr1s.quantity = 1;
//                nr2s.quantity = 1;
//                nr3s.quantity = 1;

//                ct.itemOptions.add(new ItemOption(50, 10));
//                ct.itemOptions.add(new ItemOption(77, 10));
//                ct.itemOptions.add(new ItemOption(103, 10));
//                ct.itemOptions.add(new ItemOption(5, 10));
////                ct.itemOptions.add(new ItemOption(98, 10));
//
//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, top1nv, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//        
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
//                InventoryServiceNew.gI().addItemBag(pl, ct);
//                InventoryServiceNew.gI().addItemBag(pl, nr1s);
//                InventoryServiceNew.gI().addItemBag(pl, nr2s);
//                InventoryServiceNew.gI().addItemBag(pl, nr3s);

                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//           
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//       
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Tuingocxanh(Player pl) {
        try {
            if (pl.inventory.gem > 5000000) {
                Service.gI().sendThongBao(pl, "Bạn cần tiêu bớt ngọc xanh đi");
                return;
            }
            Item tuingocxanh = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2228) {
                    tuingocxanh = item;
                    break;
                }
            }
            if (tuingocxanh != null) {
                pl.inventory.gem += 1000;

                InventoryServiceNew.gI().subQuantityItemsBag(pl, tuingocxanh, 1);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được 1000 ngọc xanh");
                Service.gI().sendMoney(pl);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void Tuingochong(Player pl) {
        try {
            if (pl.inventory.ruby > 1000000) {
                Service.gI().sendThongBao(pl, "Bạn cần tiêu bớt ngọc hồng đi");
                return;
            }
            Item tuingochong = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2229) {
                    tuingochong = item;
                    break;
                }
            }
            if (tuingochong != null) {
                pl.inventory.ruby += 300;

                InventoryServiceNew.gI().subQuantityItemsBag(pl, tuingochong, 1);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được 300 ngọc hồng");
                Service.gI().sendMoney(pl);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquavnd1(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống hành trang");
                return;
            }
            Item hopquavnd = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2145) {
                    hopquavnd = item;
                    break;
                }
            }
            if (pl.getSession() != null && pl.getSession().vnd > cn.ghVnd) {
                Service.gI().sendThongBao(pl, "Bạn cần tiêu bớt VND");
                return;
            }
            if (hopquavnd != null) {
                PlayerDAO.addvnd(pl, 100000);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquavnd, 1);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được 100.000 VND");
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquavnd2(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống hành trang");
                return;
            }
            Item hopquavnd2 = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2146) {
                    hopquavnd2 = item;
                    break;
                }
            }
            if (pl.getSession() != null && pl.getSession().vnd > cn.ghVnd) {
                Service.gI().sendThongBao(pl, "Bạn cần tiêu bớt VND");
                return;
            }
            if (hopquavnd2 != null) {
                PlayerDAO.addvnd(pl, 200000);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquavnd2, 1);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được 200.000 VND");
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquat1nap(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 4) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 5 ô trống hành trang");
                return;
            }
            Item hopquat1nap = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2134) {
                    hopquat1nap = item;
                    break;
                }
            }
            if (hopquat1nap != null) {
                Item dh = ItemService.gI().createNewItem((short) 1232);
                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
//                Item vanbay = ItemService.gI().createNewItem((short) 1140);
//                Item ct = ItemService.gI().createNewItem((short) 2118);
                Item trungzamas = ItemService.gI().createNewItem((short) 2006);
//                Item saophale = ItemService.gI().createNewItem((short) 964);
//                Item dabaove = ItemService.gI().createNewItem((short) 987);
//                dabaove.quantity = 10;
                thoiVang_.quantity = 300;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));

                dh.itemOptions.add(new ItemOption(50, 10));
                dh.itemOptions.add(new ItemOption(77, 10));
                dh.itemOptions.add(new ItemOption(103, 10));
//                dh.itemOptions.add(new ItemOption(14, 10));
//                dh.itemOptions.add(new ItemOption(5, 10));
                dh.itemOptions.add(new ItemOption(30, 0));

//                vanbay.itemOptions.add(new ItemOption(50, 10));
//                vanbay.itemOptions.add(new ItemOption(77, 10));
//                vanbay.itemOptions.add(new ItemOption(103, 10));
//                vanbay.itemOptions.add(new ItemOption(14, 10));
//                vanbay.itemOptions.add(new ItemOption(5, 10));
//                vanbay.itemOptions.add(new ItemOption(30, 0));
//                ct.itemOptions.add(new ItemOption(50, 34));
//                ct.itemOptions.add(new ItemOption(77, 34));
//                ct.itemOptions.add(new ItemOption(103, 34));
//                ct.itemOptions.add(new ItemOption(5, 34));
//                ct.itemOptions.add(new ItemOption(14, 34));
//                ct.itemOptions.add(new ItemOption(218, 1));
                dhd.itemOptions.add(new Item.ItemOption(30, 0));

                trungzamas.itemOptions.add(new Item.ItemOption(30, 0));

//                saophale.itemOptions.add(new Item.ItemOption(30, 0));
//                saophale.itemOptions.add(new Item.ItemOption(50, 5));
//                saophale.quantity = 15;
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquat1nap, 1);
                InventoryServiceNew.gI().addItemBag(pl, dhd);
                InventoryServiceNew.gI().addItemBag(pl, dh);
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
//                InventoryServiceNew.gI().addItemBag(pl, ct);
//                InventoryServiceNew.gI().addItemBag(pl, vanbay);
                InventoryServiceNew.gI().addItemBag(pl, trungzamas);
//                InventoryServiceNew.gI().addItemBag(pl, saophale);
//                InventoryServiceNew.gI().addItemBag(pl, dabaove);
                InventoryServiceNew.gI().sendItemBags(pl);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dh.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + vanbay.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dabaove.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + trungzamas.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + saophale.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquat2nap(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 6) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 7 ô trống hành trang");
                return;
            }
            Item hopquat2nap = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2135) {
                    hopquat2nap = item;
                    break;
                }
            }
            if (hopquat2nap != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1139);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
                Item ct = ItemService.gI().createNewItem((short) Util.nextInt(1407, 1422));
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);
//                Item dabaove = ItemService.gI().createNewItem((short) 987);
//                dabaove.quantity = 10;
                //  Item trungzamas = ItemService.gI().createNewItem((short) 1110);
//                Item saophale = ItemService.gI().createNewItem((short) 964);
                thoiVang_.quantity = 200;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;
//                dh.itemOptions.add(new ItemOption(50, 8));
//                dh.itemOptions.add(new ItemOption(77, 8));
//                dh.itemOptions.add(new ItemOption(103, 8));
//                dh.itemOptions.add(new ItemOption(14, 8));
//                dh.itemOptions.add(new ItemOption(5, 8));
//                dh.itemOptions.add(new ItemOption(30, 0));

                ct.itemOptions.add(new ItemOption(50, 10));
                ct.itemOptions.add(new ItemOption(77, 10));
                ct.itemOptions.add(new ItemOption(103, 10));
                ct.itemOptions.add(new ItemOption(5, 10));
//                ct.itemOptions.add(new ItemOption(98, 10));

//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                //trungzamas.itemOptions.add(new Item.ItemOption(30, 0));
//                saophale.itemOptions.add(new Item.ItemOption(30, 0));
//                saophale.itemOptions.add(new Item.ItemOption(50, 5));
//                saophale.quantity = 7;
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquat2nap, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//                InventoryServiceNew.gI().addItemBag(pl, dh);
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
                InventoryServiceNew.gI().addItemBag(pl, ct);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);
//                InventoryServiceNew.gI().addItemBag(pl, saophale);
//                InventoryServiceNew.gI().addItemBag(pl, dabaove);
                // InventoryServiceNew.gI().addItemBag(pl, trungzamas);
                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dh.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);

//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + saophale.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dabaove.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquat3nap(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 5) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 6 ô trống hành trang");
                return;
            }
            Item hopquat3nap = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2136) {
                    hopquat3nap = item;
                    break;
                }
            }
            if (hopquat3nap != null) {
//                Item dh = ItemService.gI().createNewItem((short) 1141);
//                Item dhd = ItemService.gI().createNewItem((short) 2114);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);
//                Item ct = ItemService.gI().createNewItem((short) 865);
//                Item dabaove = ItemService.gI().createNewItem((short) 987);
//                dabaove.quantity = 10;
                thoiVang_.quantity = 150;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));
                nr1s.quantity = 1;
                nr2s.quantity = 1;
                nr3s.quantity = 1;
//                dh.itemOptions.add(new ItemOption(50, 5));
//                dh.itemOptions.add(new ItemOption(77, 5));
//                dh.itemOptions.add(new ItemOption(103, 5));
//                dh.itemOptions.add(new ItemOption(14, 5));
//
//                dh.itemOptions.add(new ItemOption(30, 0));

//                ct.itemOptions.add(new ItemOption(50, 4));
//                ct.itemOptions.add(new ItemOption(77, 4));
//                ct.itemOptions.add(new ItemOption(103, 4));
//                ct.itemOptions.add(new ItemOption(5, 4));
//                ct.itemOptions.add(new ItemOption(98, 10));
//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
                //trungzamas.itemOptions.add(new Item.ItemOption(30, 0));
//                saophale.itemOptions.add(new Item.ItemOption(30, 0));
//                saophale.itemOptions.add(new Item.ItemOption(50, 5));
//                saophale.quantity = 7;
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquat3nap, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
//                InventoryServiceNew.gI().addItemBag(pl, dh);
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);
//                InventoryServiceNew.gI().addItemBag(pl, ct);
//                InventoryServiceNew.gI().addItemBag(pl, dabaove);
                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dh.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dabaove.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquat45nap(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 5) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 6 ô trống hành trang");
                return;
            }
            Item hopquat45nap = null;

            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2137) {
                    hopquat45nap = item;

                    break;
                }
            }
            if (hopquat45nap != null) {

                Item nr1s = ItemService.gI().createNewItem((short) 14);
                Item nr2s = ItemService.gI().createNewItem((short) 15);
                Item nr3s = ItemService.gI().createNewItem((short) 16);
//                Item dhd = ItemService.gI().createNewItem((short) 1010);
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
//                Item dabaove = ItemService.gI().createNewItem((short) 987);
//                dabaove.quantity = 10;

                thoiVang_.quantity = 100;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));

                nr1s.quantity = 6;
                nr2s.quantity = 6;
                nr3s.quantity = 6;

//                dhd.itemOptions.add(new Item.ItemOption(30, 0));
//                dhd.itemOptions.add(new Item.ItemOption(50, 30));
//                dhd.itemOptions.add(new Item.ItemOption(77, 30));
//                dhd.itemOptions.add(new Item.ItemOption(103, 30));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquat45nap, 1);
//                InventoryServiceNew.gI().addItemBag(pl, dhd);
                InventoryServiceNew.gI().addItemBag(pl, nr1s);
                InventoryServiceNew.gI().addItemBag(pl, nr2s);
                InventoryServiceNew.gI().addItemBag(pl, nr3s);
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);

//                InventoryServiceNew.gI().addItemBag(pl, dabaove);
                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dhd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dabaove.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquat610nap(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 1) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 2 ô trống hành trang");
                return;
            }
            Item hopquat610nap = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2138) {
                    hopquat610nap = item;
                    break;
                }
            }
            if (hopquat610nap != null) {
//                Item nr1s = ItemService.gI().createNewItem((short) 14);
//                Item nr2s = ItemService.gI().createNewItem((short) 15);
//                Item nr3s = ItemService.gI().createNewItem((short) 16);
//                Item gang = ItemService.gI().createNewItem((short) (pl.gender + 657));//gang hd
                Item thoiVang_ = ItemService.gI().createNewItem((short) 457);
//                Item dabaove = ItemService.gI().createNewItem((short) 987);
//                dabaove.quantity = 10;

                thoiVang_.quantity = 100;
                thoiVang_.itemOptions.add(new Item.ItemOption(211, 0));
                thoiVang_.itemOptions.add(new Item.ItemOption(30, 0));

//                nr1s.quantity = 3;
//                nr2s.quantity = 3;
//                nr3s.quantity = 3;
//
//                gang.itemOptions.add(new Item.ItemOption(0, 9000));
//                gang.itemOptions.add(new Item.ItemOption(21, 80));
//                gang.itemOptions.add(new Item.ItemOption(30, 0));
                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquat610nap, 1);
//                InventoryServiceNew.gI().addItemBag(pl, gang);
//                InventoryServiceNew.gI().addItemBag(pl, nr1s);
//                InventoryServiceNew.gI().addItemBag(pl, nr2s);
//                InventoryServiceNew.gI().addItemBag(pl, nr3s);
                InventoryServiceNew.gI().addItemBag(pl, thoiVang_);
//                InventoryServiceNew.gI().addItemBag(pl, dabaove);

                // InventoryServiceNew.gI().addItemBag(pl, trungzamas);
                InventoryServiceNew.gI().sendItemBags(pl);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gang.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + thoiVang_.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr1s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr2s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + nr3s.template.name);
//                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + dabaove.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquamocnap300(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 2) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 3 ô trống hành trang");
                return;
            }
            Item hopquamocnap300 = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2139) {
                    hopquamocnap300 = item;
                    break;
                }
            }
            if (hopquamocnap300 != null) {
                Item gang = ItemService.gI().createNewItem((short) (pl.gender + 657));//gang hd
                Item vpdl = ItemService.gI().createNewItem((short) 1271);
                Item saophale = ItemService.gI().createNewItem((short) 964);
                saophale.quantity = 15;
                saophale.itemOptions.add(new ItemOption(50, 5));

                gang.itemOptions.add(new Item.ItemOption(0, 9000));
                gang.itemOptions.add(new Item.ItemOption(21, 80));
                gang.itemOptions.add(new Item.ItemOption(30, 0));

                vpdl.itemOptions.add(new ItemOption(50, 5));
                vpdl.itemOptions.add(new ItemOption(77, 5));
                vpdl.itemOptions.add(new ItemOption(103, 5));
                vpdl.itemOptions.add(new ItemOption(5, 5));

                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquamocnap300, 1);
                InventoryServiceNew.gI().addItemBag(pl, gang);
                InventoryServiceNew.gI().addItemBag(pl, saophale);
                InventoryServiceNew.gI().addItemBag(pl, vpdl);

                InventoryServiceNew.gI().sendItemBags(pl);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + gang.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + saophale.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + vpdl.template.name);

            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquamocnap500(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 2) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 3 ô trống hành trang");
                return;
            }
            Item hopquamocnap500 = null;
            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2140) {
                    hopquamocnap500 = item;
                    break;
                }
            }
            if (hopquamocnap500 != null) {
                Item setdohd = ItemService.gI().createNewItem((short) (1109));//gang hd

                Item saophale = ItemService.gI().createNewItem((short) 964);
                saophale.quantity = 15;
                saophale.itemOptions.add(new ItemOption(50, 5));

//                setdohd.itemOptions.add(new Item.ItemOption(0, 9000));
//                setdohd.itemOptions.add(new Item.ItemOption(21, 80));
                setdohd.itemOptions.add(new Item.ItemOption(30, 0));

                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquamocnap500, 1);
                InventoryServiceNew.gI().addItemBag(pl, setdohd);
                InventoryServiceNew.gI().addItemBag(pl, saophale);

                InventoryServiceNew.gI().sendItemBags(pl);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + setdohd.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + saophale.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void hopquamocnap1000(Player pl) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) <= 4) {
                Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 5 ô trống hành trang");
                return;
            }
            Item hopquamocnap1000 = null;

            for (Item item : pl.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2141) {
                    hopquamocnap1000 = item;
                    break;
                }
            }
            if (hopquamocnap1000 != null) {

                Item vanbay = ItemService.gI().createNewItem((short) (pl.gender + 349));

                Item trungde = ItemService.gI().createNewItem((short) (1111));
                trungde.itemOptions.add(new Item.ItemOption(30, 1));
                Item pet = ItemService.gI().createNewItem((short) (Util.nextInt(1407, 1417)));
                Item robot = ItemService.gI().createNewItem((short) (Util.nextInt(2045, 2048)));

                robot.itemOptions.add(new Item.ItemOption(50, 5));
                robot.itemOptions.add(new Item.ItemOption(77, 5));
                robot.itemOptions.add(new Item.ItemOption(103, 5));

                pet.itemOptions.add(new Item.ItemOption(50, 8));
                pet.itemOptions.add(new Item.ItemOption(77, 8));
                pet.itemOptions.add(new Item.ItemOption(103, 8));
                pet.itemOptions.add(new Item.ItemOption(Util.nextInt(117, 126), 10));

                vanbay.itemOptions.add(new ItemOption(50, 4));
                vanbay.itemOptions.add(new Item.ItemOption(77, 4));
                vanbay.itemOptions.add(new Item.ItemOption(103, 4));

                InventoryServiceNew.gI().subQuantityItemsBag(pl, hopquamocnap1000, 1);
                InventoryServiceNew.gI().addItemBag(pl, trungde);
                InventoryServiceNew.gI().addItemBag(pl, robot);
                InventoryServiceNew.gI().addItemBag(pl, pet);
                InventoryServiceNew.gI().sendItemBags(pl);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + trungde.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
                Service.gI().sendThongBao(pl, "Bạn đã nhận được " + robot.template.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 0358124452
            //Logger.logException(UseItem.class, e);
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.gI().point(player);
        Service.gI().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Có đệ đâu mà đòi dùng ngáo à");
        }
    }

    private void changePetBerus(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBerusPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Phải có đệ mới dùng được");
        }
    }

    private void changePetBerusBiNgo(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBerusBiNgoPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Phải có đệ mới dùng được");
        }
    }

    private void changePetDaishinkan(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeDaishinkanPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Phải có đệ mới dùng được");
        }
    }

    private void changePetZamasu(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeZamasuPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Phải có đệ mới dùng được");
        }
    }

    private void changePetWhis(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeWhisPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.gI().sendThongBao(player, "Phải có đệ mới dùng được");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.gI().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.gI().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void eatHpMp(Player pl, Item item) {
        long percentHP = Util.TamkjllGH(pl.nPoint.hp * 100 / pl.nPoint.hpMax);
        long percentMP = Util.TamkjllGH(pl.nPoint.mp * 100 / pl.nPoint.mpMax);
//        if (percentHP > 70) {
//            Service.gI().sendThongBao(pl, "HP vẫn còn trên 70%");
//            return;
//        } else if (percentMP > 70) {
//            Service.gI().sendThongBao(pl, "MP vẫn còn trên 70%");
//            return;
//        }
        if (item.template.id == 74) {
            if (percentHP > 70) {
                Service.gI().sendThongBao(pl, "HP vẫn còn trên 70%");
                return;
            } else if (percentMP > 70) {
                Service.gI().sendThongBao(pl, "MP vẫn còn trên 70%");
                return;
            } else {
                pl.nPoint.hp = Util.TamkjllGH(pl.nPoint.hpMax);
                pl.nPoint.mp = Util.TamkjllGH(pl.nPoint.mpMax);
                Service.gI().sendThongBao(pl, "HP-MP của bạn đã được hồi phục 100%");
            }
        } else if (item.template.id == 191 || item.template.id == 1002 || item.template.id == 1003) {
            if (percentHP > 70) {
                Service.gI().sendThongBao(pl, "HP vẫn còn trên 70%");
                return;
            } else {
                pl.nPoint.hp += Util.TamkjllGH(pl.nPoint.hpMax * 30 / 100);
                Service.gI().sendThongBao(pl, "hp của bạn đã được hồi phục thêm 30%");
            }
        } else if (item.template.id == 192 || item.template.id == 1004) {
            if (percentMP > 70) {
                Service.gI().sendThongBao(pl, "MP vẫn còn trên 70%");
                return;
            } else {
                pl.nPoint.mp += Util.TamkjllGH(pl.nPoint.mpMax * 30 / 100);
                Service.gI().sendThongBao(pl, "Ki của bạn đã được hồi phục thêm 30%");
            }
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendInfoHpMp(pl);
    }

    private void quagapthu(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] linhthu = {457, 457, 457, 457, 457, 457, 457, 457, 457, 2157, 2158, 2159, 2227, 2228, 2229, 1002, 1003, 1004, 1918, 1917};
            byte rd = (byte) Util.nextInt(0, linhthu.length - 1); // Đã chỉnh lại để chọn loại ngẫu nhiên trong khoảng đầy đủ
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            // Chọn loại vật phẩm ngẫu nhiên từ danh sách 'spl'
            short selectedItemType = linhthu[rd];
            Item newItem = ItemService.gI().createNewItem(selectedItemType);

            // Thêm option tương ứng dựa trên loại vật phẩm đã chọn
//            newItem.itemOptions.add(new ItemOption(initBaseOptionLinhthu(newItem), 5));
            newItem.itemOptions.add(new ItemOption(93, Util.nextInt(1, 10)));
            newItem.quantity = 1;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void quagapthu2(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] linhthu = {457, 457, 457, 457, 457, 457, 457, 457, 457, 2157, 2158, 2159, 2227, 2228, 2229, 1002, 1003, 1004, 1918, 1917};
            byte rd = (byte) Util.nextInt(0, linhthu.length - 1); // Đã chỉnh lại để chọn loại ngẫu nhiên trong khoảng đầy đủ
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            // Chọn loại vật phẩm ngẫu nhiên từ danh sách 'spl'
            short selectedItemType = linhthu[rd];
            Item newItem = ItemService.gI().createNewItem(selectedItemType);

            // Thêm option tương ứng dựa trên loại vật phẩm đã chọn
//            newItem.itemOptions.add(new ItemOption(initBaseOptionLinhthu(newItem), 5));
            newItem.itemOptions.add(new ItemOption(93, Util.nextInt(1, 10)));
            newItem.quantity = 1;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void tuivang(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] linhthu = {457, 457, 457, 457, 457, 457, 457, 457, 457};
            byte rd = (byte) Util.nextInt(0, linhthu.length - 1); // Đã chỉnh lại để chọn loại ngẫu nhiên trong khoảng đầy đủ
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            // Chọn loại vật phẩm ngẫu nhiên từ danh sách 'spl'
            short selectedItemType = linhthu[rd];
            Item newItem = ItemService.gI().createNewItem(selectedItemType);

            // Thêm option tương ứng dựa trên loại vật phẩm đã chọn
//            newItem.itemOptions.add(new ItemOption(initBaseOptionLinhthu(newItem), 5));
            newItem.itemOptions.add(new ItemOption(93, Util.nextInt(1, 10)));
            newItem.quantity = 1;
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
            Service.gI().sendThongBao(player, "Chúc mừng bạn nhận được " + newItem.template.name);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {20, 19, 18, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{1000000, 2000000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openManhTS(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {1066, 1067, 1068, 1069, 1070};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void Openhopct(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int id = Util.nextInt(0, 100);
            int[] rdct = new int[]{1344, 1345, 1346};
            int randomct = new Random().nextInt(rdct.length);
            Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);
            if (id <= 90) {
                ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 45)));
                ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 45)));
                ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 45)));
                ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(10, 50)));
                ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 15)));
            } else {
                ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 50)));
                ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 45)));
                ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 45)));
                ct.itemOptions.add(new Item.ItemOption(101, Util.nextInt(10, 50)));
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
        } else {
            Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void OpenHopQuaNhapHoc(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int id = Util.nextInt(0, 100);
//            int[] hopquanhaphoc = new int[]{1244, 1245};
            int[] rdop = new int[]{1339, 1340, 1341, 1342};
//            int randomct = new Random().nextInt(hopquanhaphoc.length);
            int randomop = new Random().nextInt(rdop.length);
//            Item ct = ItemService.gI().createNewItem((short) hopquanhaphoc[randomct]);
            Item ct = ItemService.gI().createNewItem((short) rdop[randomop]);
            if (id <= 50) {

                ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 15)));
            } else {
                ct.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 15)));
            }
//            if (id <= 99) {
//                vt.itemOptions.add(new Item.ItemOption(30, Util.nextInt(1, 2)));
//            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, ct);
//            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.gI().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
        } else {
            Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void Openhopflagbag(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int id = Util.nextInt(0, 100);
            int[] rdfl = new int[]{1157, 1203, 1204, 1205, 1206, 1207, 954, 955, 1211, 1212, 1213,
                1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 966, 1222, 1223, 1224, 1225, 1226, 1228,
                1229, 467, 468, 469, 470, 982, 471, 983, 994, 995, 740, 996, 741, 997, 998, 999, 1000, 745,
                1001, 1007, 2035, 1013, 1021, 766, 1022, 767, 1023};
            int[] rdop = new int[]{50, 77, 103};
            int randomfl = new Random().nextInt(rdfl.length);
            int randomop = new Random().nextInt(rdop.length);
//            Item fl = ItemService.gI().createNewItem((short) rdfl[randomfl]);
            Item fl = ItemService.gI().createNewItem((short) Util.nextInt(1347, 1352));
            if (id <= 90) {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 16)));
                fl.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 15)));
            } else {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 16)));
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, fl);
//            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.gI().sendThongBao(pl, "Bạn đã nhận được " + fl.template.name);
        } else {
            Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void Openhop8_3(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int id = Util.nextInt(0, 100);
            int[] rdfl = new int[]{1157, 1203, 1204, 1205, 1206, 1207, 954, 955, 1211, 1212, 1213,
                1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 966, 1222, 1223, 1224, 1225, 1226, 1228,
                1229, 467, 468, 469, 470, 982, 471, 983, 994, 995, 740, 996, 741, 997, 998, 999, 1000, 745,
                1001, 1007, 2035, 1013, 1021, 766, 1022, 767, 1023, 1099, 1100, 1101, 1102};
            int[] rdop = new int[]{50, 77, 103};
            int randomfl = new Random().nextInt(rdfl.length);
            int randomop = new Random().nextInt(rdop.length);
//            Item fl = ItemService.gI().createNewItem((short) rdfl[randomfl]);
            Item fl = ItemService.gI().createNewItem((short) Util.nextInt(1347, 1352));
            if (id <= 90) {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 16)));
                fl.itemOptions.add(new Item.ItemOption(93, Util.nextInt(3, 15)));
            } else {
                fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 16)));
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, fl);
//            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.gI().sendThongBao(pl, "Bạn đã nhận được " + fl.template.name);
        } else {
            Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void Openhoppet(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int id = Util.nextInt(0, 100);
            int[] rdpet = new int[]{1311, 1312, 1313, 1236, 1237, 1238};
            int[] rdop = new int[]{50, 77, 103};
            int chiso = Util.nextInt(5, 12);
            int randompet = new Random().nextInt(rdpet.length);
            int randomop = new Random().nextInt(rdop.length);
//            Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
            Item pet = ItemService.gI().createNewItem((short) Util.nextInt(1407, 1422));
            if (id <= 99) {
                pet.itemOptions.add(new Item.ItemOption(50, chiso));
                pet.itemOptions.add(new Item.ItemOption(77, chiso));
                pet.itemOptions.add(new Item.ItemOption(103, chiso));
                pet.itemOptions.add(new Item.ItemOption(5, chiso));

                pet.itemOptions.add(new Item.ItemOption(93, chiso));
            } else {
                pet.itemOptions.add(new Item.ItemOption(50, chiso));
                pet.itemOptions.add(new Item.ItemOption(77, chiso));
                pet.itemOptions.add(new Item.ItemOption(103, chiso));
                pet.itemOptions.add(new Item.ItemOption(5, chiso));
                pet.itemOptions.add(new Item.ItemOption(89, chiso));
                pet.itemOptions.add(new Item.ItemOption(93, chiso));

            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, pet);
//            InventoryServiceNew.gI().addItemBag(pl, vt);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.gI().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
        } else {
            Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    public void openboxsukien(Player pl, Item item, int idsukien) {
        try {
            switch (idsukien) {
                case 1:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 865, 999, 1000, 1001, 739, 742, 743};
                            int[][] gold = {{5000, 20000}};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            Item it = ItemService.gI().createNewItem(temp[index]);
                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));

                            } else if (temp[index] == 865) {

                                it.itemOptions.add(new ItemOption(30, 0));

                                if (Util.isTrue(1, 30)) {
                                    it.itemOptions.add(new ItemOption(93, 365));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 739) { // cải trang Billes

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 742) { // cải trang Caufila

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 743) { // chổi bay
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryServiceNew.gI().addItemBag(pl, it);
                            icon[1] = it.template.iconID;

                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);

                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        } else {
                            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                        break;
                    } else {
                        Service.gI().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                case ConstEvent.SU_KIEN_20_11:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 1039, 954, 955, 710, 711, 1040, 2023, 999, 1000, 1001};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            Item it = ItemService.gI().createNewItem(temp[index]);
                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));
                            } else if (temp[index] == 1039) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 954) {
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 955) {
                                it.itemOptions.add(new ItemOption(50, 20));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 710) {//cải trang quy lão kame
                                it.itemOptions.add(new ItemOption(50, 22));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(194, 0));
                                it.itemOptions.add(new ItemOption(160, 35));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 711) { // cải trang jacky chun
                                it.itemOptions.add(new ItemOption(50, 23));
                                it.itemOptions.add(new ItemOption(77, 21));
                                it.itemOptions.add(new ItemOption(103, 21));
                                it.itemOptions.add(new ItemOption(195, 0));
                                it.itemOptions.add(new ItemOption(160, 50));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1040) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 2023) {
                                it.itemOptions.add(new ItemOption(30, 0));
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            icon[1] = it.template.iconID;
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryServiceNew.gI().addItemBag(pl, it);
                            int ruby = Util.nextInt(1, 5);
                            pl.inventory.ruby += ruby;
                            InventoryServiceNew.gI().sendItemBags(pl);
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.gI().sendThongBao(pl, "Bạn được tặng kèm " + ruby + " Hồng Ngọc");
                        } else {
                            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.gI().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_NOEL:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
                            int spl = Util.nextInt(441, 445);
                            int dnc = Util.nextInt(220, 224);
                            int nr = Util.nextInt(16, 18);
                            int nrBang = Util.nextInt(926, 931);

                            if (Util.isTrue(5, 90)) {
                                int ruby = Util.nextInt(1, 3);
                                pl.inventory.ruby += ruby;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                                PlayerService.gI().sendInfoHpMpMoney(pl);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.gI().sendThongBao(pl, "Bạn nhận được " + ruby + " Hồng Ngọc");
                            } else {
                                int[] temp = {spl, dnc, nr, nrBang, 387, 390, 393, 821, 822, 746, 380, 999, 1000, 1001, 936, 2022};
                                byte index = (byte) Util.nextInt(0, temp.length - 1);
                                short[] icon = new short[2];
                                icon[0] = item.template.iconID;
                                Item it = ItemService.gI().createNewItem((short) temp[index]);

                                if (temp[index] >= 441 && temp[index] <= 443) {// sao pha le
                                    it.itemOptions.add(new ItemOption(temp[index] - 346, 5));
                                    it.quantity = 10;
                                } else if (temp[index] >= 444 && temp[index] <= 445) {
                                    it.itemOptions.add(new ItemOption(temp[index] - 346, 3));
                                    it.quantity = 10;
                                } else if (temp[index] >= 220 && temp[index] <= 224) { // da nang cap
                                    it.quantity = 10;
                                } else if (temp[index] >= 387 && temp[index] <= 393) { // mu noel do
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(80, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(106, 0));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                    it.itemOptions.add(new ItemOption(199, 0));
                                } else if (temp[index] == 936) { // tuan loc
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                } else if (temp[index] == 822) { //cay thong noel
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else if (temp[index] == 746) { // xe truot tuyet
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(30, 360)));
                                    }
                                } else if (temp[index] == 999) { // mèo mun
                                    it.itemOptions.add(new ItemOption(77, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 1000) { // xiên cá
                                    it.itemOptions.add(new ItemOption(103, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 1001) { // Phóng heo
                                    it.itemOptions.add(new ItemOption(50, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 2022 || temp[index] == 821) {
                                    it.itemOptions.add(new ItemOption(30, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(73, 0));
                                }
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                icon[1] = it.template.iconID;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                                InventoryServiceNew.gI().addItemBag(pl, it);
                                InventoryServiceNew.gI().sendItemBags(pl);
                            }
                        } else {
                            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.gI().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_TET:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            RandomCollection<Integer> rd = Manager.HOP_QUA_TET;
                            int tempID = rd.next();
                            Item it = ItemService.gI().createNewItem((short) tempID);
                            if (it.template.type == 11) {//FLAGBAG
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 20)));
                                if (Util.isTrue(1999, 2009)) {
                                    it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
                                }
                            } else if (it.template.type == 5) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 75)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 75)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 75)));
                                it.itemOptions.add(new ItemOption(101, Util.nextInt(20, 75)));
                                if (Util.isTrue(1999, 2009)) {
                                    it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 75)));
                                }
                                it.itemOptions.add(new ItemOption(106, 0));
                            } else if (it.template.type == 21) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(2, 28)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(2, 28)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(2, 28)));
                                it.itemOptions.add(new ItemOption(104, Util.nextInt(5, 15)));
                                if (Util.isTrue(1999, 2009)) {
                                    it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
                                }
                            }
                            int type = it.template.type;
                            if (type == 5 || type == 11) {// cải trang & flagbag
                                if (Util.isTrue(1999, 2009)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    it.itemOptions.add(new ItemOption(30, 1));
                                }
                                it.itemOptions.add(new ItemOption(199, 0));//KHÔNG THỂ GIA HẠN
                            } else if (type == 21) {// thú cưỡi
                                if (Util.isTrue(1999, 2009)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                    it.itemOptions.add(new ItemOption(30, 1));
                                }
                            }
                            if (tempID >= ConstItem.MANH_AO && tempID <= ConstItem.MANH_GANG_TAY) {
                                it.quantity = 1;
                            } else {
                                it.itemOptions.add(new ItemOption(93, 5));
                            }
                            icon[1] = it.template.iconID;
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryServiceNew.gI().addItemBag(pl, it);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;
                        } else {
                            Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.gI().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case 5:
                    if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
                        short[] icon = new short[2];
                        icon[0] = item.template.iconID;
                        int tempID;
                        if (Util.isTrue(30, 100)) {
                            byte getRandom = (byte) Util.getOne(0, 1);
                            if (getRandom == 0) {
                                tempID = 584;
                            } else {
                                tempID = 861;
                            }
                        } else if (Util.isTrue(20, 100)) {
                            byte getRandom = (byte) Util.getOne(0, 1);
                            if (getRandom == 0) {
                                tempID = Util.getOne(733, 734);
                            } else {
                                tempID = 861;
                            }
                        } else if (Util.isTrue(15, 100)) {
                            tempID = 457;
                        } else {
                            tempID = 190;
                        }
                        Item it = ItemService.gI().createNewItem((short) tempID);
                        switch (tempID) {
                            case 457:
                                it.quantity = Util.nextInt(1, 5);
                                break;
                            case 861:
                                it.quantity = Util.nextInt(100, 500);
                                break;
                            case 584:
                                it.itemOptions.add(new ItemOption(77, 45));
                                it.itemOptions.add(new ItemOption(103, 45));
                                it.itemOptions.add(new ItemOption(50, 45));
                                it.itemOptions.add(new ItemOption(116, 0));
                                if (!Util.isTrue(10, 150)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                }
                                it.itemOptions.add(new ItemOption(74, 0));
                                break;
                            case 733:
                            case 734:
                                it.itemOptions.add(new ItemOption(77, 5));
                                it.itemOptions.add(new ItemOption(103, 5));
                                it.itemOptions.add(new ItemOption(50, 5));
                                it.itemOptions.add(new ItemOption(74, 0));
                                break;
                        }
                        icon[1] = it.template.iconID;
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        InventoryServiceNew.gI().addItemBag(pl, it);
                        InventoryServiceNew.gI().sendItemBags(pl);
                        break;
                    } else {
                        Service.gI().sendThongBao(pl, "Hàng trang đã đầy");
                    }
                    break;

            }
        } catch (Exception e) {
            Logger.logException(UseItem.class,
                    e, "Lỗi sử dụng item sự kiện");
        }
    }

    private void OpenhopThucuoi(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            int tile = Util.nextInt(0, 100);
            int[] rdpet = new int[]{1143, 1144, 1145, 1146, 1147, 1148, 1149};
            int[] rdop = new int[]{50, 77, 103};
            int chiso = Util.nextInt(5, 12);
            int randompet = new Random().nextInt(rdpet.length);
            int randomop = new Random().nextInt(rdop.length);

            Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);

//            Item pet = ItemService.gI().createNewItem((short)  Util.nextInt(5, 16));
            if (tile <= 99) {
                pet.itemOptions.add(new Item.ItemOption(50, chiso));
                pet.itemOptions.add(new Item.ItemOption(77, chiso));
                pet.itemOptions.add(new Item.ItemOption(103, chiso));
                pet.itemOptions.add(new Item.ItemOption(5, chiso));
                pet.itemOptions.add(new Item.ItemOption(89, chiso));
                pet.itemOptions.add(new Item.ItemOption(93, chiso));
            } else {
                pet.itemOptions.add(new Item.ItemOption(50, chiso));
                pet.itemOptions.add(new Item.ItemOption(77, chiso));
                pet.itemOptions.add(new Item.ItemOption(103, chiso));
                pet.itemOptions.add(new Item.ItemOption(5, chiso));
                pet.itemOptions.add(new Item.ItemOption(89, chiso));

            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().addItemBag(pl, pet);
            InventoryServiceNew.gI().sendItemBags(pl);
            Service.gI().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
        } else {
            Service.gI().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
        }
    }

    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {

            case 1601:
                if (pl.itemTime.isLoNuocThanhx2 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx5 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx7 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx10 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx15 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                pl.itemTime.lastTimeLoNuocThanhx2 = System.currentTimeMillis();
                pl.itemTime.isLoNuocThanhx2 = true;
                break;
            case 1602:
                if (pl.itemTime.isLoNuocThanhx2 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx5 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx7 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx10 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx15 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                pl.itemTime.lastTimeLoNuocThanhx5 = System.currentTimeMillis();
                pl.itemTime.isLoNuocThanhx5 = true;
                break;
            case 1603:
                if (pl.itemTime.isLoNuocThanhx2 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx5 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx7 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx10 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx15 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                pl.itemTime.lastTimeLoNuocThanhx7 = System.currentTimeMillis();
                pl.itemTime.isLoNuocThanhx7 = true;
                break;
            case 1604:
                if (pl.itemTime.isLoNuocThanhx2 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx5 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx7 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx10 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx15 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                pl.itemTime.lastTimeLoNuocThanhx10 = System.currentTimeMillis();
                pl.itemTime.isLoNuocThanhx10 = true;
                break;
            case 1605:
                if (pl.itemTime.isLoNuocThanhx2 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx5 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx7 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx10 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                if (pl.itemTime.isLoNuocThanhx15 == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng nước thánh rồi");
                    return;
                }
                pl.itemTime.lastTimeLoNuocThanhx15 = System.currentTimeMillis();
                pl.itemTime.isLoNuocThanhx15 = true;
                break;
            case 579:
                if (pl.itemTime.isUseDuoiKhi == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeDuoiKhi = System.currentTimeMillis();
                pl.itemTime.isUseDuoiKhi = true;
                break;

            case 2196:
                pl.itemTime.lastTimeUseVooc = System.currentTimeMillis();
                pl.itemTime.isUseVooc = true;
                break;
            case 2197:
                pl.itemTime.lastTimeUseSaoBien = System.currentTimeMillis();
                pl.itemTime.isUseSaoBien = true;
                break;
            case 2198:
                pl.itemTime.lastTimeUseConCua = System.currentTimeMillis();
                pl.itemTime.isUseConCua = true;
                break;
            case 2189:
                pl.itemTime.lastTimeUseMayDoSK = System.currentTimeMillis();
                pl.itemTime.isUseMayDoSK = true;

                break;
            case 2157: //banh ngọt
                if (pl.itemTime.isUseBanhNgot == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBanhNgot = System.currentTimeMillis();
                pl.itemTime.isUseBanhNgot = true;

                break;
            case 2158: //kem ốc quế
                if (pl.itemTime.isUseKemOcQue == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeKemOcQue = System.currentTimeMillis();
                pl.itemTime.isUseKemOcQue = true;

                break;
            case 2159: //kẻo dẻo
                if (pl.itemTime.isUseKeoDeo == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeKeoDeo = System.currentTimeMillis();
                pl.itemTime.isUseKeoDeo = true;

                break;
            case 2160: //kẹo tỏng gói
                if (pl.itemTime.isUseKeoTrongGoi == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeKeoTrongGoi = System.currentTimeMillis();
                pl.itemTime.isUseKeoTrongGoi = true;

                break;
            case 903: //banh sau
                if (pl.itemTime.isUseBanhSau == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBanhSau = System.currentTimeMillis();
                pl.itemTime.isUseBanhSau = true;

                break;
            case 902: //giáp xên
                if (pl.itemTime.isUseBanhNhen == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBanhNhen = System.currentTimeMillis();
                pl.itemTime.isUseBanhNhen = true;

                break;
            case 900: //cuồng nộ
                if (pl.itemTime.isUseSupBi == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeSupBi = System.currentTimeMillis();
                pl.itemTime.isUseSupBi = true;

                break;
            case 899: //ẩn danh
                if (pl.itemTime.isUseKeoMotMat == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeKeoMotMat = System.currentTimeMillis();
                pl.itemTime.isUseKeoMotMat = true;

                break;
            case 753:
                if (pl.itemTime.isUseBanhChung == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBanhChung = System.currentTimeMillis();
                pl.itemTime.isUseBanhChung = true;

                break;
            case 752:
                if (pl.itemTime.isUseBanhTet == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBanhTet = System.currentTimeMillis();
                pl.itemTime.isUseBanhTet = true;

                break;
            case 382: //bổ huyết
                if (pl.itemTime.isUseBoHuyetSC == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;

                break;
            case 383: //bổ khí
                if (pl.itemTime.isUseBoKhiSC == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: //giáp xên
                if (pl.itemTime.isUseGiapXenSC == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: //cuồng nộ
                if (pl.itemTime.isUseCuongNoSC == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.gI().point(pl);
                break;
            case 385: //ẩn danh
                if (pl.itemTime.isUseAnDanhSC == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: //máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1100: //bổ huyết
                if (pl.itemTime.isUseBoHuyet == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBoHuyetSC = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyetSC = true;
                break;
            case 1101: //bổ khí
                if (pl.itemTime.isUseBoKhi == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeBoKhiSC = System.currentTimeMillis();
                pl.itemTime.isUseBoKhiSC = true;
                break;
            case 1102: //giáp xên
                if (pl.itemTime.isUseGiapXen == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeGiapXenSC = System.currentTimeMillis();
                pl.itemTime.isUseGiapXenSC = true;
                break;
            case 1099: //cuồng nộ
                if (pl.itemTime.isUseCuongNo == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeCuongNoSC = System.currentTimeMillis();
                pl.itemTime.isUseCuongNoSC = true;
                Service.gI().point(pl);
                break;
            case 1103: //ẩn danh
                if (pl.itemTime.isUseAnDanh == true) {
                    Service.gI().sendThongBao(pl, "Bạn đang sử dụng rồi");
                    return;
                }
                pl.itemTime.lastTimeAnDanhSC = System.currentTimeMillis();
                pl.itemTime.isUseAnDanhSC = true;
                break;

            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                break;
            case 2166: //máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;
            case 2161: //máy dò đồ
                pl.itemTime.lastTimeUseMayDo3 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo3 = true;
                break;
        }
        Service.gI().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
//                case SummonDragon.NGOC_RONG_2_SAO:
//                case SummonDragon.NGOC_RONG_3_SAO:
//                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13), SummonDragon.DRAGON_SHENRON);
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
        if (tempId >= GoiRongXuong.XUONG_1_SAO && tempId <= GoiRongXuong.XUONG_7_SAO) {
            switch (tempId) {
                case GoiRongXuong.XUONG_1_SAO:
                    GoiRongXuong.gI().openMenuRongXuong(pl, (byte) (tempId - 701));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_RONG_XUONG,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }
//     private void controllerCallRongThan(Player pl, Item item) {
//        int tempId = item.template.id;
//        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
//            switch (tempId) {
//                case SummonDragon.NGOC_RONG_1_SAO:
////                case SummonDragon.NGOC_RONG_2_SAO:
////                case SummonDragon.NGOC_RONG_3_SAO:
//                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13), SummonDragon.DRAGON_SHENRON);
//                    break;
//                default:
//                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON, -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
//                    break;
//            }
//        } else if (tempId == SummonDragon.NGOC_RONG_SIEU_CAP) {
//            SummonDragon.gI().openMenuSummonShenron(pl, (byte) 1015, SummonDragon.DRAGON_BLACK_SHENRON);
//        } else if (tempId >= SummonDragon.NGOC_RONG_BANG[0] && tempId <= SummonDragon.NGOC_RONG_BANG[6]) {
//            switch (tempId) {
//                case 925:
//                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) 925, SummonDragon.DRAGON_ICE_SHENRON);
//                    break;
//                default:
//                    Service.getInstance().sendThongBao(pl, "Bạn chỉ có thể gọi rồng băng từ ngọc 1 sao");
//                    break;
//            }
//        }
//    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {

                if (item.template.id == 2127) {
                    SkillService.gI().learSkillSpecial(pl, Skill.SUPER_KAME);
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    return;
                } else if (item.template.id == 2128) {
                    SkillService.gI().learSkillSpecial(pl, Skill.MA_PHONG_BA);
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    return;
                } else if (item.template.id == 2129) {
                    SkillService.gI().learSkillSpecial(pl, Skill.LIEN_HOAN_CHUONG);
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    return;
                } else if (item.template.id == 1996) {
                    SkillService.gI().learSkillSpecial(pl, Skill.HAKAI);
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    InventoryServiceNew.gI().sendItemBags(pl);
                    return;
                }
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                try {
                    if (curSkill == null) {
                        Service.gI().sendThongBao(pl, "Không có skill");
                        // Xử lý trường hợp curSkill là null
                        // Bạn có thể khởi tạo hoặc gán giá trị cho curSkill ở đây
                    } else if (curSkill.point == 7) {
                        Service.gI().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                    } else {
                        if (curSkill.point == 0) {
                            if (level == 1) {
                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                                SkillUtil.setSkill(pl, curSkill);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                msg = Service.gI().messageSubCommand((byte) 23);
                                msg.writer().writeShort(curSkill.skillId);
                                pl.sendMessage(msg);
                                msg.cleanup();
                            } else {
                                Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                                Service.gI().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                            }
                        } else {
                            if (curSkill.point + 1 == level) {
                                curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                                //System.out.println(curSkill.template.name + " - " + curSkill.point);
                                SkillUtil.setSkill(pl, curSkill);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                msg = Service.gI().messageSubCommand((byte) 62);
                                msg.writer().writeShort(curSkill.skillId);
                                pl.sendMessage(msg);
                                msg.cleanup();
                            } else {
                                Service.gI().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                            }
                        }
                        InventoryServiceNew.gI().sendItemBags(pl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger
                            .logException(UseItem.class,
                                    e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(UseItem.class,
                            e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            Service.gI().sendThongBao(pl, "Không có đệ để dùng");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
            Service.gI().sendThongBao(pl, "Không có đệ để dùng");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata3(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            Service.gI().sendThongBao(pl, "Không có đệ để dùng");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion3(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata4(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            Service.gI().sendThongBao(pl, "Không có đệ để dùng");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion4(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata5(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE || pl.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            Service.gI().sendThongBao(pl, "Không có đệ để dùng");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion5(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata6(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE || pl.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            Service.gI().sendThongBao(pl, "Không có đệ để dùng");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion6(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        if (index < 0 || index >= pl.mapCapsule.size()) {
            Service.gI().sendThongBao(pl, "Có lỗi xảy ra khi chuyển map!!");
            return;
        }
        Zone zoneChose = pl.mapCapsule.get(index);
        if (zoneChose.getNumOfPlayers() > 25
                || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)
                || MapService.gI().isMapConDuongRanDoc(zoneChose.map.mapId)
                || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
            Service.gI().sendThongBao(pl, "Hiện tại không thể vào được khu!");
            return;
        }
        if (index != 0
                || zoneChose.map.mapId == 21
                || zoneChose.map.mapId == 22
                || zoneChose.map.mapId == 23) {
            pl.mapBeforeCapsule = pl.zone;
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);

    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            if (!player.isAdmin() && Manager.MapCold.contains(player.zone.map.mapId)) {
                Service.gI().sendThongBao(player, "Trời quá lạnh nên đậu thần cứng hết rồi!!! :)");
                return;
            }
            player.nPoint.setHp(Util.maxIntValue(player.nPoint.hp + hpKiHoiPhuc));
            player.nPoint.setMp(Util.maxIntValue(player.nPoint.mp + hpKiHoiPhuc));
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(Util.maxIntValue(player.pet.nPoint.hp + hpKiHoiPhuc));
                player.pet.nPoint.setMp(Util.maxIntValue(player.pet.nPoint.mp + hpKiHoiPhuc));
                Service.gI().sendInfoPlayerEatPea(player.pet);
                Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.gI().sendThongBao(pl, "Làm gì có pet mà dùng :)");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        InventoryServiceNew.gI().sendItemBags(pl);
                    } else {
                        Service.gI().sendThongBao(pl, "Có lỗi xảy ra với skill 1 đệ");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        InventoryServiceNew.gI().sendItemBags(pl);
                    } else {
                        Service.gI().sendThongBao(pl, "Đã có skill đâu mà nâng ảo à");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        InventoryServiceNew.gI().sendItemBags(pl);
                    } else {
                        Service.gI().sendThongBao(pl, "Đã có skill đâu mà nâng ảo à");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.gI().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        InventoryServiceNew.gI().sendItemBags(pl);
                    } else {
                        Service.gI().sendThongBao(pl, "Đã có skill đâu mà nâng ảo à");
                    }

                    break;

            }

        } catch (Exception e) {
//            e.printStackTrace();
            Service.gI().sendThongBao(pl, "Đã có skill đâu mà nâng má !!!");
        }
    }

    private void saoPhaLeTrungCap(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn sao pha lê theo cách của bạn"
                + "\n|3|DAME: sức đánh + 5%"
                + "\n|1|HP: hp + 8%"
                + "\n|2|KI: ki + 8%"
                + "\n|4|STCM: sát thương chí mạng + 8%", "DAME", "HP", "KI", "STCM", "Từ Chối");
    }

    private void saoPhaLeCaoCap(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn sao pha lê theo cách của bạn"
                + "\n|3|DAME: sức đánh + 8%"
                + "\n|1|HP: hp + 12%"
                + "\n|2|KI: ki + 12%"
                + "\n|4|STCM: sát thương chí mạng + 12%", "DAME", "HP", "KI", "STCM", "Từ Chối");
    }

    private void ItemSKH(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void itemDoThanLinh(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void itemDoHuyDiet(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món đồ huỷ diệt!", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void ItemDHD(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void Hopts(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của Bạn đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hopdothanlinh(Player pl, Item item) {//hop qua do thần linh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của Bạn đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hopdohuydiet(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của Bạn đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void Hopdothiensu(Player pl, Item item) {//hop qua do thiên sứ
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của Bạn đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0 && item.itemOptions != null) {
            Item itemReward = null;
            int param = item.itemOptions.size();
            int gold = 0;
            int[] listItem = {441, 442, 443, 444, 445, 446, 447, 220, 221, 222, 223, 224, 225};
            int[] listClothesReward;
            int[] listItemReward;
            String text = "Bạn nhận được\n";
            if (param < 8) {
                gold = 100000 * param;
                listClothesReward = new int[]{randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 3);
            } else if (param < 10) {
                gold = 250000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 4);
            } else {
                gold = 500000 * param;
                listClothesReward = new int[]{randClothes(param), randClothes(param), randClothes(param)};
                listItemReward = Util.pickNRandInArr(listItem, 5);
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                pl.textRuongGo.add(text + "|1| " + ruby + " Hồng Ngọc");
            }
            for (int i : listClothesReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionClothes(itemReward.template.id, itemReward.template.type, itemReward.itemOptions);
                RewardService.gI().initStarOption(itemReward, new RewardService.RatioStar[]{new RewardService.RatioStar((byte) 1, 1, 2), new RewardService.RatioStar((byte) 2, 1, 3), new RewardService.RatioStar((byte) 3, 1, 4), new RewardService.RatioStar((byte) 4, 1, 5),});
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            for (int i : listItemReward) {
                itemReward = ItemService.gI().createNewItem((short) i);
                RewardService.gI().initBaseOptionSaoPhaLe(itemReward);
                itemReward.quantity = Util.nextInt(1, 5);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            if (param == 11) {
                itemReward = ItemService.gI().createNewItem((short) 0);
                itemReward.quantity = Util.nextInt(1, 3);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                pl.textRuongGo.add(text + itemReward.getInfoItem());
            }
            NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1, "Bạn nhận được\n|1|+" + Util.numberToMoney(gold) + " vàng", "OK [" + pl.textRuongGo.size() + "]");
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            pl.inventory.addGold(gold);
            InventoryServiceNew.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.gI().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private int randClothes(int level) {
        return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }

    public static final int[][][] LIST_ITEM_CLOTHES = {
        // áo , quần , găng ,giày,rada
        //td -> nm -> xd
        {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555}, {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558}, {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564}, {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
        {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560}, {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566}, {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}
    };

}
