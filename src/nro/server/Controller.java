package nro.server;

import com.girlkun.database.GirlkunDB;
import com.girlkun.network.server.GirlkunServer;
import com.girlkun.result.GirlkunResultSet;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Random;
import nro.TamBaoService;
import nro.consts.ConstIgnoreName;
import nro.consts.ConstMap;
import nro.consts.ConstNpc;
import nro.consts.ConstTask;
import nro.consts.ConstTranhNgocNamek;
import nro.data.DataGame;
import nro.data.ItemData;
import nro.jdbc.daos.PlayerDAO;
import nro.kygui.ShopKyGuiService;
import nro.models.card.Card;
import nro.models.card.RadarCard;
import nro.models.card.RadarService;
import nro.models.consignment.ConsignmentShop;
import nro.models.item.Item;
import nro.models.map.blackball.BlackBallWar;
import nro.models.map.sieuhang.SieuHangService;
import nro.models.matches.PVPService;
import nro.models.npc.NpcManager;
import nro.models.player.Archivement;
import nro.models.player.Archivement_BoMong;
import nro.models.player.Archivement_diem;
import nro.models.player.MiniPet;
import nro.models.player.Player;
import nro.models.shop.ShopServiceNew;
import nro.network.handler.IMessageHandler;
import nro.network.io.Message;
import nro.network.session.ISession;
import nro.server.io.MySession;
import nro.services.ChatGlobalService;
import nro.services.ClanService;
import nro.services.DoKiepService;
import nro.services.FlagBagService;
import nro.services.FriendAndEnemyService;
import nro.services.IntrinsicService;
import nro.services.InventoryServiceNew;
import nro.services.ItemMapService;
import nro.services.ItemTimeService;
import nro.services.NpcService;
import nro.services.PetService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.SubMenuService;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineServiceNew;
import nro.services.func.GoiRongXuong;
import nro.services.func.Input;
import nro.services.func.LuckyRound;
import nro.services.func.MiniGame;
import nro.services.func.SummonDragon;
import nro.services.func.TransactionService;
import nro.services.func.UseItem;
import nro.utils.SkillUtil;
import nro.utils.Util;
import nro.utils.Logger;

import static okhttp3.internal.Internal.logger;

public class Controller implements IMessageHandler {
//  private Player player;

    private static Controller instance;

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    @Override
    public void onMessage(ISession s, Message _msg) {
        MySession _session = (MySession) s;
        long st = System.currentTimeMillis();
        Player player = null;
        try {
            player = _session.player;
            byte cmd = _msg.command;

//            Logger.error("Read cmd>>>: " + cmd);
            switch (cmd) {
                case 70:
                    TamBaoService.readData(_msg, player);
                    break;
                case -100:
                    byte action = _msg.reader().readByte();
                    switch (action) {
                        case 0:
                            short idItem = _msg.reader().readShort();
                            byte moneyType = _msg.reader().readByte();
                            int money = _msg.reader().readInt();
                            int quantity;
                            if (player.getSession().version >= 222) {
                                quantity = _msg.reader().readInt();
                            } else {
                                quantity = _msg.reader().readByte();
                            }
                            if (quantity > 0) {
                                ShopKyGuiService.gI().KiGui(player, idItem, money, moneyType, quantity);
                            }
                            break;
                        case 1:
                        case 2:
                            idItem = _msg.reader().readShort();
                            ShopKyGuiService.gI().claimOrDel(player, action, idItem);
                            break;
                        case 3:
                            idItem = _msg.reader().readShort();
                            _msg.reader().readByte();
                            _msg.reader().readInt();
                            ShopKyGuiService.gI().buyItem(player, idItem);
                            break;
                        case 4:
                            moneyType = _msg.reader().readByte();
                            money = _msg.reader().readByte();
                            ShopKyGuiService.gI().openShopKyGui(player, moneyType, money);
                            break;
                        case 5:
                            idItem = _msg.reader().readShort();
                            ShopKyGuiService.gI().upItemToTop(player, idItem);
                            break;
                        default:
                            Service.gI().sendThongBao(player, "Có lỗi xảy ra tại shop kí gửi");
                            break;
                    }
                    break;

//                    if (player != null) {
//
//                        if (player.getSession().actived) {
//                            if (player.iDMark.getIndexMenu() == ConstNpc.KIGUICU) {
//                                byte action = _msg.reader().readByte();
//                                switch (action) {
//                                    case 0:
//                                        short idItem = _msg.reader().readShort();
//                                        byte moneyType = _msg.reader().readByte();
//                                        int money = _msg.reader().readInt();
//                                        int quantity;
//                                        if (player.getSession().version >= 222) {
//                                            quantity = _msg.reader().readInt();
//                                        } else {
//                                            quantity = _msg.reader().readByte();
//                                        }
//                                        if (quantity > 0) {
//                                            ShopKyGuiService.gI().KiGui(player, idItem, money, moneyType, quantity);
//                                        }
//                                        break;
//                                    case 1:
//                                    case 2:
//                                        idItem = _msg.reader().readShort();
//                                        ShopKyGuiService.gI().claimOrDel(player, action, idItem);
//                                        break;
//                                    case 3:
//                                        idItem = _msg.reader().readShort();
//                                        _msg.reader().readByte();
//                                        _msg.reader().readInt();
//                                        ShopKyGuiService.gI().buyItem(player, idItem);
//                                        break;
//                                    case 4:
//                                        moneyType = _msg.reader().readByte();
//                                        money = _msg.reader().readByte();
//                                        ShopKyGuiService.gI().openShopKyGui(player, moneyType, money);
//                                        break;
//                                    case 5:
//                                        idItem = _msg.reader().readShort();
//                                        ShopKyGuiService.gI().upItemToTop(player, idItem);
//                                        break;
//                                    default:
//                                        Service.gI().sendThongBao(player, "Không thể thực hiện");
//                                        break;
//                                }
//                            } else {
//                                ConsignmentShop.getInstance().handler(player, _msg);
//                            }
//                        } else {
////                            ConsignmentShop.getInstance().show(player);
//                            Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản!");
//                        }
//                    }
//                    break;
//                case -82:
//                    Client.gI().createBot(_session);
//                    break;
//                case -83:
//                    Client.gI().clear();
//                    break;
                case 127:
                    if (player != null) {
                        byte actionRadar = _msg.reader().readByte();
                        switch (actionRadar) {
                            case 0:
                                RadarService.gI().sendRadar(player, player.Cards);
                                break;
                            case 1:
                                short idC = _msg.reader().readShort();
                                Card card = player.Cards.stream().filter(r -> r != null && r.Id == idC).findFirst().orElse(null);
                                if (card != null) {
                                    if (card.Level == 0) {
                                        return;
                                    }
                                    if (card.Used == 0) {
                                        if (player.Cards.stream().anyMatch(c -> c != null && c.Used == 2)) {
                                            Service.gI().sendThongBao(player, "Số thẻ sử dụng đã đạt tối đa");
                                            return;
                                        }
                                        card.Used = 1;
                                        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream()
                                                .filter(r -> r.Id == idC).findAny().orElse(null);
                                        if (radarTemplate != null && card.Level >= 2) {
                                            player.idAura = radarTemplate.AuraId;
                                        }
                                    } else {
                                        card.Used = 0;
                                        player.idAura = -1;
                                    }
                                    RadarService.gI().Radar1(player, idC, card.Used);
                                    Service.gI().point(player);
                                }
                                break;
                        }
                    }
                    break;
                case -105: // Nhẫn thời không
//                    if (player.type == 0 && player.maxTime == 30) {
//                        ChangeMapService.gI().changeMap(player, 102, 0, 100, 336);
//                    } else if (player.type == 1 && player.maxTime == 5) {
//                        ChangeMapService.gI().changeMap(player, 160, 0, -1, 5);
//                    } else if (player.type == 2 && player.maxTime == 5) {
//                        ChangeMapService.gI().changeMap(player, 170, 0, 1560, 336);
//                    }
                    if (player != null) {
                        if (player.type == 1 && player.maxTime == 5) {
                            ChangeMapService.gI().changeMap(player, 160, 0, -1, 5);
                        }
                    }
                    break;
                case 71:
                    DoKiepService.gI().process(player);
                    break;
                case 42:
                    Service.gI().regisAccount(_session, _msg);
                    break;
                case -127:
                    if (player != null) {
                        LuckyRound.gI().readOpenBall(player, _msg);
                    }
                    break;
                case -125:

                    if (player != null) {
                        Input.gI().doInput(player, _msg);
                    }
                    break;
                case 112:
                    if (player != null) {
                        IntrinsicService.gI().showMenu(player);
                    }
                    break;

                case -34:
                    if (player != null) {
                        switch (_msg.reader().readByte()) {
                            case 1:
                                player.magicTree.openMenuTree();
                                break;
                            case 2:
                                player.magicTree.loadMagicTree();
                                break;
                        }
                    }
                    break;
                case -99:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerEnemy(player, _msg);
                    }
                    break;
                case 18:
                    if (player != null) {
                        FriendAndEnemyService.gI().goToPlayerWithYardrat(player, _msg);
                    }
                    break;
                case -72:
                    if (player != null) {
                        FriendAndEnemyService.gI().chatPrivate(player, _msg);
                    }
                    break;
                case -80:
                    if (player != null) {
                        FriendAndEnemyService.gI().controllerFriend(player, _msg);
                    }
                    break;
                case -59:
                    if (player != null) {
                        PVPService.gI().controllerThachDau(player, _msg);
                    }
                    break;
                case -86:
                    if (player != null) {
                        if (player.getSession().actived) {
                            TransactionService.gI().controller(player, _msg);
                        } else {
                            Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản!");
                        }
                    }
                    break;
                case -107:
                    if (player != null) {
                        Service.gI().sendChiSoPetGoc(player);
                        Service.gI().showInfoPet(player);
                    }
                    break;
//                case -108:
//                    if (player != null && player.pet != null) {
//                        player.pet.changeStatus(_msg.reader().readByte());
//                        //  player.minipet.changeStatus(_msg.reader().readByte());
//                    }
//                    break;
                case -108:
                    if (player != null && player.pet != null) {
                        player.pet.changeStatus(_msg.reader().readByte());
                    }
                    break;

//                case 6: // mua vật phẩm
//                    if (player != null && !Maintenance.isRunning) {
//                        byte typeBuy = _msg.reader().readByte();
//                        int tempId = _msg.reader().readShort();
//                        int quantity = 0;
//                        try {
//                            if (_msg.reader().available() >= 2) {
//                                quantity = _msg.reader().readShort();
//                            }
//                        } catch (IOException e) {
//
//                            e.printStackTrace();
//                        } catch (Exception e) {
//
//                            e.printStackTrace();
//                        }
//                        ShopServiceNew.gI().takeItem(player, typeBuy, tempId, quantity);
//                    }
//                    break;
                case 6: //buy item

                    if (player != null && !Maintenance.isRuning) {
                        try {
                            InputStream inputStream = _msg.reader();
                            byte typeBuy = _msg.reader().readByte();
                            int tempId = -1;
                            int quantity = -1;
                            byte[] buffer = new byte[4];
                            int bytesRead = 0;
                            while (bytesRead < 4) {
                                int count = inputStream.read(buffer, bytesRead, 4 - bytesRead);
                                if (count == -1) {
                                    break;
                                }
                                bytesRead += count;
                            }
                            tempId = new BigInteger(new byte[]{buffer[0], buffer[1]}).intValue();
                            quantity = new BigInteger(new byte[]{buffer[2], buffer[3]}).intValue();
                            ShopServiceNew.gI().takeItem(player, typeBuy, tempId, quantity);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    break;
//               

                case 7: //sell item
                    if (player != null && !Maintenance.isRuning) {
                        action = _msg.reader().readByte();
                        int where = _msg.reader().readByte();
                        int index = _msg.reader().readShort();
                        if (action == 0) {
                            ShopServiceNew.gI().showConfirmSellItem(player, where,
                                    index);
                        } else {
                            ShopServiceNew.gI().sellItem(player, where, index);
                        }
                    }
                    break;

//                case 7: //sell item
//                    if (player != null && !Maintenance.isRuning) {
//                        byte action = _msg.reader().readByte();
//                        int where = _msg.reader().readByte();
//                        int index = _msg.reader().readShort();
//                        if (action == 0) {
//                            ShopServiceNew.gI().showConfirmSellItem(player, where,
//                                    !player.isVersionAbove(220) ? index - 3 : index);
//                        } else {
//                            ShopServiceNew.gI().sellItem(player, where, index);
//                        }
//                    }
//                    break;
//                case 29:
//                    if (player != null) {
//                        ChangeMapService.gI().openZoneUI(player);
//                    }
//                    break;
//                case 21:
//                    if (player != null) {
//                        int zoneId = _msg.reader().readByte();
//                        ChangeMapService.gI().changeZone(player, zoneId);
//                    }
//                    break;
                case 29:
                    if (player != null) {
                        if (player.zone.map.mapId == ConstTranhNgocNamek.MAP_ID) {
                            Service.gI().sendPopUpMultiLine(player, 0, 7184, "Không thể thực hiện");
                            return;
                        }
                        ChangeMapService.gI().openZoneUI(player);
                    }
                    break;
                case 21:
                    if (player != null) {
                        if (player.zone.map.mapId == ConstTranhNgocNamek.MAP_ID) {
                            Service.gI().sendPopUpMultiLine(player, 0, 7184, "Không thể thực hiện");
                            return;
                        }
                        int zoneId = _msg.reader().readByte();
                        ChangeMapService.gI().changeZone(player, zoneId);
                    }
                    break;
                case -71:
                    if (player != null) {
                        ChatGlobalService.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
//                case -77:
//                    if (player != null) {
//                        byte index = _msg.reader().readByte();
////                        player.achievement.receiveGem(index);
//                    }
//                    break;
                case -76:
                    if (player != null && _msg.reader().available() >= 1) {
//                        byte index = _msg.reader().readByte();
                        if (player.zone != null && player.zone.map != null && player.typeRecvieArchiment == 2) {
                            Archivement_diem.gI().receiveGem(_msg.reader().readByte(), player);
                        } else if (player.zone != null && player.zone.map != null && player.typeRecvieArchiment == 1) {
                            Archivement.gI().receiveGem(_msg.reader().readByte(), player);
                        } else if (player.zone != null && player.zone.map != null && player.typeRecvieArchiment == 0) {
                            Archivement_BoMong.gI().receiveGem(_msg.reader().readByte(), player);
                        }

//                        player.achievement.receiveGem(index);
                    }
                    break;

                case -79:
                    if (player != null) {
                        Service.gI().getPlayerMenu(player, _msg.reader().readInt());
                    }
                    break;
                case -113:
                    if (player != null) {
                        for (int i = 0; i < 10; i++) {
                            player.playerSkill.skillShortCut[i] = _msg.reader().readByte();
                        }
                        player.playerSkill.sendSkillShortCut();
                    }
                    break;
                case -101:
                    login2(_session, _msg);
                    break;
                case -118:
                    int pId = _msg.reader().readInt();
                    if (pId != -1 && player.id != pId) {
                        SieuHangService.gI().startChallenge(player, pId);
//                        BossManager.gI().FindBoss(player, pId);
                    }
                    break;
                case -103:
                    if (player != null) {
                        byte act = _msg.reader().readByte();
                        if (act == 0) {
                            Service.gI().openFlagUI(player);
                        } else if (act == 1) {
                            Service.gI().chooseFlag(player, _msg.reader().readByte());
                        } else {
                        }
                    }
                    break;
//                case -7:
//                    if (player != null && player.location != null) {
//                        int toX = player.location.x;
//                        int toY = player.location.y;
//                        try {
//                            InputStream inputStream = _msg.reader();
//                            byte b = (byte) inputStream.read();
//                            byte[] buffer = new byte[4];
//                            inputStream.read(buffer);
//                            toX = new DataInputStream(new ByteArrayInputStream(buffer, 0, 2)).readShort();
//                            toY = new DataInputStream(new ByteArrayInputStream(buffer, 2, 2)).readShort();
//
//                            if (toY == 0) {
//                                toY = player.location.y;
//                            }
//                            PlayerService.gI().playerMove(player, toX, toY);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    break;

                case -7:
                    if (player != null && player.location != null && !player.effectSkill.isHaveEffectSkill()) {
                        int toX = player.location.x;
                        int toY = player.location.y;
                        try {
                            byte b = _msg.reader().readByte();
                            toX = _msg.reader().readShort();
                            if (_msg.reader().available() >= 2) {
                                toY = _msg.reader().readShort();
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PlayerService.gI().playerMove(player, toX, toY);
                    }
                    break;
//
//                    if (player != null) {
//                        int toX = player.location.x;
//                        int toY = player.location.y;
//                        try {
//                            InputStream inputStream = _msg.reader();
//                            byte b = (byte) inputStream.read();
//                            byte[] buffer = new byte[4];
//                            int bytesRead = 0;
//                            while (bytesRead < 4) {
//                                int count = inputStream.read(buffer, bytesRead, 4 - bytesRead);
//                                if (count == -1) {
//                                    break;
//                                }
//                                bytesRead += count;
//
//                            }
//                            toX = new BigInteger(new byte[]{buffer[0], buffer[1]}).intValue();
//                            toY = new BigInteger(new byte[]{buffer[2], buffer[3]}).intValue();
//                            if (toY == 0) {
//                                toY = player.location.y;
//                            }
//                            PlayerService.gI().playerMove(player, toX, toY);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    break;
//                    if (player != null) {
//                        int toX = player.location.x;
//                        int toY = player.location.y;
//                        try {
//                            byte b = _msg.reader().readByte();
//                            toX = _msg.reader().readShort();
//                            toY = _msg.reader().readShort();
//                        } catch (Exception e) {
//                            //e.printStackTrace();
//                        }
//                        PlayerService.gI().playerMove(player, toX, toY);
//                    }
//                    break;
//                    if (player != null) {
//                        int toX = player.location.x;
//                        int toY = player.location.y;
//                        try {
//                            byte b = _msg.reader().readByte();
////                           if (_msg.reader().available() >= 4) { // Kiểm tra nếu còn đủ byte để đọc toX và toY
//                            toX = _msg.reader().readShort();
//                            toY = _msg.reader().readShort();
//                            // } else {
//                            //  }
//                        } catch (EOFException e) {
////                            e.printStackTrace();
//                        } catch (Exception e) {
////                            e.printStackTrace();
//                        }
//                        PlayerService.gI().playerMove(player, toX, toY);
//                    }A
                case -74:

                    String ip = _session.ipAddress;
                    Logger.error("ip " + ip + " đang tải dữ liệu");
                    if (nro.network.server.GirlkunServer.firewallDownDataGame.containsKey(ip)) {
                        int soLanConnect = nro.network.server.GirlkunServer.firewallDownDataGame.get(ip).intValue();
                        if (soLanConnect > 12) {
                            Service.gI().sendThongBaoOK(_session, "Bạn đã tải dữ liệu nhiều lần, đợi bảo trì rồi quay lại");
                            return;
                        } else {
                            nro.network.server.GirlkunServer.firewallDownDataGame.put(ip, soLanConnect += 1);
                        }

                    } else {

                        nro.network.server.GirlkunServer.firewallDownDataGame.put(ip, 1);
                    }
                    byte type = _msg.reader().readByte();
                    if (type == 1) {
                        DataGame.sendSizeRes(_session);
                    } else if (type == 2) {
                        DataGame.sendRes(_session);
                    }
                    break;
//          

//          
                case -81:
                    if (player != null) {
                        try {
                            _msg.reader().readByte();
                            int[] indexItem = new int[_msg.reader().readByte()];
                            for (int i = 0; i < indexItem.length; i++) {
                                indexItem[i] = _msg.reader().readByte();
                            }
                            CombineServiceNew.gI().showInfoCombine(player, indexItem);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                    break;
                case -87:
                    DataGame.updateData(_session);
                    break;
                case -67:
//                    try {
                    int id = _msg.reader().readInt();
                    DataGame.sendIcon(_session, id);
//                    Thread.sleep(50);
//                } catch (Exception e) {
//                    System.err.println("Controler: case 67: " + e.getMessage());
//                }
                    break;
//                case -67:
//                    int id = _msg.reader().readInt();
//                    if (id < 0) {
//                        String errorMessage = "ID không hợp lệ";
//                        // Gửi thông báo lỗi chi tiết đến client
//                        DataGame.sendErrorMessage(_session, errorMessage);
//                    } else {
//                        try {
//                            // Xử lý logic khi ID hợp lệ
//                            DataGame.sendIcon(_session, id);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//
//                        }
//                    }
//                    break;
//                case 66:
//                    DataGame.sendImageByName(_session, _msg.reader().readUTF());
//                    break;
                case 66:
//                try {
                    String imageName = _msg.reader().readUTF();
                    DataGame.sendImageByName(_session, imageName);
//                    Thread.sleep(50);
//                } catch (Exception e) {
//                    System.err.println("Controler: case 66: " + e.getMessage());
//                }

                    break;
                case -66:
                    int effId = _msg.reader().readShort();
                    int idT = effId;
                    if (effId == 25 && GoiRongXuong.gI().isRongxuongAppear == true) {
                        idT = 51; // id eff rong muon thay doi ( hien tai la rong xuong) 
                    }
                    if (effId == 25 && player.zone.map.mapId == 7) {
                        idT = 50; // id eff rong muon thay doi ( hien tai la rong vang) 
                    }
                    DataGame.effData(_session, effId, idT);
                    break;
                case -62:
                    if (player != null) {
                        FlagBagService.gI().sendIconFlagChoose(player, _msg.reader().readByte());
                    }
                    break;
                case -63:

                    if (player != null) {
                        byte fbid = _msg.readByte();
                        int fbidz = fbid & 0xFF;
                        FlagBagService.gI().sendIconEffectFlag(player, fbidz);
                    }
                    break;
                case -32:
                    int bgId = _msg.reader().readShort();
                    DataGame.sendItemBGTemplate(_session, bgId);
                    break;
                case 22:
                    if (player != null) {
                        _msg.reader().readByte();
                        NpcManager.getNpc(ConstNpc.DAU_THAN).confirmMenu(player, _msg.reader().readByte());
                    }
                    break;
                case -33:
                case -23:
                    if (player != null) {
                        ChangeMapService.gI().changeMapWaypoint(player);
                        Service.gI().hideWaitDialog(player);
                    }
                    break;
                case -45:
                    if (player != null) {
                        SkillService.gI().useSkill(player, null, null, _msg);
                    }
                    break;
                case -46:
                    if (player != null) {
                        ClanService.gI().getClan(player, _msg);
                    }
                    break;
                case -51:
                    if (player != null) {
                        ClanService.gI().clanMessage(player, _msg);
                    }
                    break;
                case -54:
                    if (player != null) {
                        ClanService.gI().clanDonate(player, _msg);
                        Service.gI().sendThongBao(player, "Can not invoke clan donate");
                    }
                    break;
                case -49:
                    if (player != null) {
                        ClanService.gI().joinClan(player, _msg);
                    }
                    break;
                case -50:
                    if (player != null) {
                        ClanService.gI().sendListMemberClan(player, _msg.reader().readInt());
                    }
                    break;
                case -56:
                    if (player != null) {
                        ClanService.gI().clanRemote(player, _msg);
                    }
                    break;
                case -47:
                    if (player != null) {
                        ClanService.gI().sendListClan(player, _msg.reader().readUTF());
                    }
                    break;
                case -55:
                    if (player != null) {
                        ClanService.gI().showMenuLeaveClan(player);
                    }
                    break;
                case -57:
                    if (player != null) {
                        ClanService.gI().clanInvite(player, _msg);
                    }
                    break;
//                
                case -40:

                    UseItem.gI().getItem(_session, _msg);

                    break;
                case -41:
                    Service.gI().sendCaption(_session, _msg.reader().readByte());
                    break;
                case -43:
                    if (player != null) {
                        UseItem.gI().doItem(player, _msg);
                    }
                    break;
                case -91:
                    if (player != null) {
                        switch (player.iDMark.getTypeChangeMap()) {
                            case ConstMap.CHANGE_CAPSULE:
                                UseItem.gI().choseMapCapsule(player, _msg.reader().readByte());
                                break;
                            case ConstMap.CHANGE_BLACK_BALL:
//                                Service.gI().sendThongBao(player, "Đang bảo trì...");
                                BlackBallWar.gI().changeMap(player, _msg.reader().readByte());
                                break;
                        }
                    }
                    break;
                case -39:
                    if (player != null) {
                        //finishLoadMap
                        ChangeMapService.gI().finishLoadMap(player);
                        if (player.zone.map.mapId == (21 + player.gender)) {
                            if (player.mabuEgg != null) {
                                player.mabuEgg.sendMabuEgg();
                            }
                        }
//                        if (player.zone.map.mapId == 154) {
//                            if (player.billEgg != null) {
//                                player.billEgg.sendBillEgg();
//                            }
//                        }
//                        if (player.zone.map.mapId == (21 + player.gender)) {
//                            if (player.BuugayEgg != null) {
//                                player.BuugayEgg.sendMabuGayEgg();
//                            }
//                        }
                    }
                    break;
                case 11:
                    byte modId = _msg.reader().readByte();
                    DataGame.requestMobTemplate(_session, modId);
                    break;
                case 44:
                    if (player != null) {
                        Service.gI().chat(player, _msg.reader().readUTF());
                    }
                    break;
                case 32:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        int select = _msg.reader().readByte();
                        MenuController.getInstance().doSelectMenu(player, npcId, select);
                    }
                    break;
                case 33:
                    if (player != null) {
                        int npcId = _msg.reader().readShort();
                        MenuController.getInstance().openMenuNPC(_session, npcId, player);
                    }
                    break;
                case 34:
                    if (player != null) {
                        int selectSkill = _msg.reader().readShort();
                        //  System.err.println("id" + selectSkill);
                        SkillService.gI().selectSkill(player, selectSkill);
                    }
                    break;
                case 54:
                    if (player != null) {
                        Service.gI().attackMob(player, (int) (_msg.reader().readByte()));
                    }
                    break;
                case -60:
                    if (player != null) {
                        int playerId = _msg.reader().readInt();
                        Service.gI().attackPlayer(player, playerId);
                    }
                    break;
                case -27:
                    _session.sendKey();
                    DataGame.sendVersionRes(_session);
                    break;
                case -111:
                    DataGame.sendDataImageVersion(_session);
                    break;
                case -20:
                    if (player != null && !player.isDie()) {
                        int itemMapId = _msg.reader().readShort();
                        ItemMapService.gI().pickItem(player, itemMapId, false);
                    }
                    break;
                case -28:
                    messageNotMap(_session, _msg);
                    break;
                case -29:
                    messageNotLogin(_session, _msg);
                    break;
                case -30:
                    messageSubCommand(_session, _msg);
                    break;
                case -15: // về nhà
                    if (player != null) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                    }
                    break;
                case -16: // hồi sinh
                    if (player != null) {
                        PlayerService.gI().hoiSinh(player);
                    }
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            _msg.cleanup();
            _msg.dispose();
        }
    }

    public void messageNotLogin(MySession session, Message msg) {
        if (msg != null) {
            try {
                byte cmd = msg.reader().readByte();
                switch (cmd) {
                    case 0:
                        session.login(msg.reader().readUTF(), msg.reader().readUTF());
                        if (Manager.LOCAL) {
                            break;
                        }
                        System.out.println("version: " + msg.readUTF());
                        break;
                    case 2:
                        Service.gI().setClientType(session, msg);
                        break;
                    default:
                        break;

                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.logException(Controller.class,
                        e);
            }
        }
    }

    public void messageNotMap(MySession _session, Message _msg) {
        if (_msg != null) {
//            Player player = null;
            try {
                Player player = Client.gI().getPlayerByUser(_session.userId);
                byte cmd = _msg.reader().readByte();
//                System.out.println("CMD receive -28 / " + cmd);
                switch (cmd) {
                    case 2:
                        createChar(_session, _msg);
                        break;
                    case 6:
                        DataGame.updateMap(_session);
                        break;
                    case 7:
                        DataGame.updateSkill(_session);
                        break;
                    case 8:
                        ItemData.updateItem(_session);
                        break;
                    case 10:
                        DataGame.sendMapTemp(_session, _msg.reader().readUnsignedByte());
                        break;
                    case 13:
                        if (player != null) {
                            Service.gI().player(player);
                            Service.gI().Send_Caitrang(player);
                            player.zone.load_Another_To_Me(player);

                            // -64 my flag bag
                            Service.gI().sendFlagBag(player);

                            // -113 skill shortcut
                            player.playerSkill.sendSkillShortCut();

                            SkillService.gI().sendCurrLevelSpecial(player, SkillUtil.getSkillbyId(player, player.gender == 0 ? 24 : player.gender == 1 ? 26 : 25));

                            // item time
                            ItemTimeService.gI().sendAllItemTime(player);

                            // send current task
                            TaskService.gI().sendInfoCurrentTask(player);
                            ItemTimeService.gI().sendTextBanDoKhoBau(player);
                            ItemTimeService.gI().sendTextConDuongRanDoc(player);
//                            ItemTimeService.gI().sendTextKhiGas(player);
                            ItemTimeService.gI().sendTextDoanhTrai(player);
                        }
                        break;
                    default:
                        break;

                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.logException(Controller.class,
                        e);
            }
        }
    }

    public void messageSubCommand(MySession _session, Message _msg) {
        if (_msg != null) {
//            Player player = null;
            try {
                Player player = Client.gI().getPlayerByUser(_session.userId);
//                player = _session.player;
                byte command = _msg.reader().readByte();
//                 System.out.println("CMD receive -30 / " + command);
                switch (command) {
                    case 16:
                        byte type = _msg.reader().readByte();
                        short point = _msg.reader().readShort();
                        if (player != null && player.nPoint != null) {
                            player.nPoint.increasePoint(type, point, false);
                        }
                        break;
                    case 18:
                        byte type2 = _msg.reader().readByte();
                        short point2 = _msg.reader().readShort();
                        if (player != null && player.getSession().totalvnd2 < 1000000) {
                            Service.gI().sendThongBaoOK(player, "Cần duy trì VND ở mức 1.000.000 để sử dụng chức năng này!");
                            return;
                        }
                        if (player != null && player.pet != null) {
                            player.pet.nPoint.increasePoint(type2, point2, true);
                        }
                        break;
                    case 64:
                        int playerId = _msg.reader().readInt();
                        int menuId = _msg.reader().readShort();
                        SubMenuService.gI().controller(player, playerId, menuId);
                        break;
                    default:
//                        Logger.error("Read in cmd 30 >>>> " + command);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger
                        .logException(Controller.class,
                                e);
            }
        }
    }

    public void createChar(MySession session, Message msg) {
        if (!Maintenance.isRuning) {
            GirlkunResultSet rs = null;
            boolean created = false;
            try {
                String name = msg.reader().readUTF();
                int gender = msg.reader().readByte();
                int hair = msg.reader().readByte();
                if (name.length() <= 10) {
                    rs = GirlkunDB.executeQuery("select * from player where name = ?", name);

                    if (rs.first()) {
                        Service.gI().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                    } else {
                        if (Util.haveSpecialCharacter(name)) {
                            Service.gI().sendThongBaoOK(session, "Tên nhân vật không được chứa ký tự đặc biệt");
                        } else {
                            boolean isNotIgnoreName = true;
                            for (String n : ConstIgnoreName.IGNORE_NAME) {
                                if (name.equals(n)) {
                                    Service.gI().sendThongBaoOK(session, "Tên nhân vật đã tồn tại");
                                    isNotIgnoreName = false;
                                    break;
                                }
                            }
                            if (isNotIgnoreName) {
                                created = PlayerDAO.createNewPlayer(session.userId, name.toLowerCase(), (byte) gender, hair);
                            }
                        }
                    }
                } else {
                    Service.gI().sendThongBaoOK(session, "Tên nhân vật tối đa 10 ký tự");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger
                        .logException(Controller.class,
                                e);
            } finally {
                if (rs != null) {
                    rs.dispose();
                }
            }
            if (created) {
                session.login(session.uu, session.pp);
            }
        }
    }

    public void login2(MySession session, Message msg) {
        Service.gI().switchToRegisterScr(session);
        //Service.gI().sendThongBaoOK(session, "Vui lòng đăng ký tài khoản tại trang chủ!");
    }

    public void sendInfo(MySession session) {

        Player player = Client.gI().getPlayerByUser(session.userId);
        // -82 set tile map
        DataGame.sendTileSetInfo(session);

        // 112 my info intrinsic
        IntrinsicService.gI().sendInfoIntrinsic(player);

        // -42 my point
        Service.gI().point(player);

        // 40 task
        TaskService.gI().sendTaskMain(player);

        // -22 reset all
        Service.gI().clearMap(player);

        // -53 my clan
        ClanService.gI().sendMyClan(player);

        // -69 max statima
        PlayerService.gI().sendMaxStamina(player);

        // -68 cur statima
        PlayerService.gI().sendCurrentStamina(player);

        // -97 năng động
        Service.gI().sendNangDong(player);
        // -107 have pet
        Service.gI().sendHavePet(player);

        // -119 top rank
//            Service.getInstance().sendMessage(session, -119, "1630679754740_-119_r");
//
//            Service.getInstance().sendMessage(session, -93, "1630679752231_-93_r");
        // -50 thông tin bảng thông báo
        ServerNotify.gI().sendNotifyTab(player);
        // -24 join map - map info
        player.zone.load_Me_To_Another(player);
        player.zone.mapInfo(player);

        // -70 thông báo bigmessage
        sendThongBaoServer(player);
        //check activation set
        player.setClothes.setup();
        if (player.pet != null) {
            player.pet.setClothes.setup();
        }

        //last time use skill
        Service.gI().sendTimeSkill(player);

        //clear vt sk
        clearVTSK(player);
//        if (player.inventory.itemsBody.get(8).isNotNullItem()) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                    Service.gI().sendFlagBag(player);
//                } catch (Exception e) {
//                }
//            }, "flagbag update").start();
//        }
//        if (player.inventory.itemsBody.get(5).isNotNullItem()) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(1000);
//                    Service.gI().Send_Caitrang(player);
//                } catch (Exception e) {
//                }
//            }, "cai trang update").start();
//        }
        if (player.inventory.itemsBody.get(6).isNotNullItem()) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.gI().sendEffDanhHieu(player, player.getEffectchar(), 1, -1, 50, -1, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "danh hieu update").start();
        }
        if (player.inventory.itemsBody.get(9).isNotNullItem()) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.gI().sendEffChanMenh(player, player.getEffectchar2(), 0, -1, 1, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "chan menh update").start();
        }
        if (player.inventory.itemsBody.get(7).isNotNullItem()) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
//                    PetService.Pet2(player, player.newpet.getHead(), player.newpet.getBody(), player.newpet.getLeg());
                    MiniPet.callMiniPet(player, player.getThuCung());
                    player.minipet.reCall();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Pet update").start();
        }
        if (player.inventory.itemsBody.get(10).isNotNullItem() && !player.isPetFollow) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.gI().sendPetFollow(player, (player.getLinhThu()));
                    player.isPetFollow = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Linh thu update").start();
        }

        if (TaskService.gI().getIdTask(player) == ConstTask.TASK_0_0) {
            NpcService.gI().createTutorial(player, -1,
                    "Chào mừng " + player.name + " đến với Ngọc Rồng Emti\n"
                    + "Nhiệm vụ đầu tiên của bạn là di chuyển\n"
                    + "Bạn hãy di chuyển nhân vật theo mũi tên chỉ hướng");
        }
        if (GoiRongXuong.gI().playerRongXuong != null
                && GoiRongXuong.gI().playerRongXuong.id == player.id) {
            ServerNotify.gI().notify("Người chơi: " + player.name + " gọi Rồng Xương tại "
                    + GoiRongXuong.gI().mapRongxuongAppear.map.mapName + " khu " + GoiRongXuong.gI().mapRongxuongAppear.zoneId + " đã vào lại Game");
        }
//        if (SummonDragon.gI().playerSummonShenron != null
//                && SummonDragon.gI().playerSummonShenron.id == player.id) {
//            ServerNotify.gI().notify("Người chơi: " + player.name + " gọi Rồng Thần tại "
//                    + SummonDragon.gI().mapShenronAppear.map.mapName + " khu " + SummonDragon.gI().mapShenronAppear.zoneId + " đã vào lại Game");
//        }
    }

    private void sendThongBaoServer(Player player) {
        Service.gI().sendThongBaoFromAdmin(player, "\b|6|Ngọc Rồng EMTI Thông Báo\n"
                + "\n|2|Sự kiện đua top đang diễn ra tại đảo Kame"
                + "\n|5|Tại các làng sẽ có Gấu Po , sự kiện vô cùng hấp dẫn"
                + "\n|4|Chú ý: Update bản mới nhất <12-9-2024> tại NREMTI.COM để cập nhật mới nhất!!!");
    }

//    private void handleDoKiep(MySession session, Message msg, Player player) {
//        try {
//
//            int playerId = msg.readInt();
//            int clientCapTT = msg.readInt();
//            long clientGold = msg.readLong();
//
//            if (player == null || player.id != playerId || session.player != player) {
//                Logger.error("Không xác định được người chơi hoặc phiên không hợp lệ.");
//                return;
//            }
//
//            // Kiểm tra điều kiện độ kiếp
//            final long goldRequired = 1_000_000;
//            if (player.inventory.gold < goldRequired) {
//                Logger.error("Không đủ vàng.");
//                return;
//            }
//
//            if (clientCapTT != player.capTT) {
//                Logger.error("Cảnh giới không đồng bộ giữa client và server.");
//                return;
//            }
//
//            // Tránh spam độ kiếp
//            if (player.isProcessing) {
//                Logger.error("Đang độ kiếp, từ chối yêu cầu.");
//                return;
//            }
//
//            player.isProcessing = true;
//
//            boolean success = new Random().nextInt(100) < 90;
//            int newCapTT = success ? player.capTT + 1 : player.capTT;
//            long newGold = player.inventory.gold - goldRequired;
//
//            if (success) {
//                player.capTT = (byte) newCapTT;
//            }
//            player.inventory.gold = newGold;
//
//            // Ghi vào DB
//            PlayerDAO.updatePlayer(player);
//
//            // Gửi phản hồi
//            Message response = new Message(71);
//            response.writeBoolean(success);
//            response.writeInt((int) newGold);
//            response.writeInt(newCapTT);
//            session.sendMessage(response);
//
//        } catch (Exception e) {
//            Logger.error("Lỗi xử lý độ kiếp: " + e.getMessage());
//        } finally {
//            if (player != null) {
//                player.isProcessing = false;
//            }
//        }
//    }

    private void clearVTSK(Player player) {
//        _________Xóa item 1______________

        // Tạo mới item 17 một lần duy nhất
//Item item17 = ItemService.gI().createNewItem((short) (17));
//
//player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 16).forEach(item -> {
//    // Tính số lượng item 17 cần thêm vào
//    int quantityToAdd = item.quantity * 7;
//
//    // Thêm số lượng item 17 vào túi đồ
//    InventoryServiceNew.gI().addQuantityItemsBag(player, item17, quantityToAdd);
//
//    // Sau đó, trừ số lượng item 16
//    InventoryServiceNew.gI().subQuantityItemsBag(player, item, item.quantity);
//});
//
//player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.id == 16).forEach(item -> {
//    // Tính số lượng item 17 cần thêm vào
//    int quantityToAdd = item.quantity * 7;
//
//    // Thêm số lượng item 17 vào hộp đồ
//    InventoryServiceNew.gI().addQuantityItemsBox(player, item17, quantityToAdd);
//
//    // Sau đó, trừ số lượng item 16
//    InventoryServiceNew.gI().subQuantityItemsBox(player, item, item.quantity);
//});
////        //        _________Xóa item 2______________
//       
//        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).forEach(item -> {
//
//            if (!item.haveOption(93)) {
//                item.itemOptions.add(new Item.ItemOption(93, 15));
//            }
//        });
//        player.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).forEach(item -> {
//            item.itemOptions.add(new Item.ItemOption(30, 1));
//            item.itemOptions.add(new Item.ItemOption(93, 10));
//        });
//        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).forEach(item -> {
//            item.itemOptions.add(new Item.ItemOption(30, 1));
//            item.itemOptions.add(new Item.ItemOption(93, 10));
//        });
////        //        _________Xóa item 3______________
//        player.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && item.template.id == 1193).forEach(item -> {
//            InventoryServiceNew.gI().subQuantityItemsBody(player, item, item.quantity);
//        });
//        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 1193).forEach(item -> {
//            InventoryServiceNew.gI().subQuantityItemsBag(player, item, item.quantity);
//        });
//        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.id == 1193).forEach(item -> {
//            InventoryServiceNew.gI().subQuantityItemsBox(player, item, item.quantity);
//        });
//        //        _________Xóa item 3______________
//        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 2113).forEach(item -> {
//            InventoryServiceNew.gI().subQuantityItemsBag(player, item, item.quantity);
//        });
//        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.id == 2113).forEach(item -> {
//            InventoryServiceNew.gI().subQuantityItemsBox(player, item, item.quantity);
//        });
//item type 27
        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && (item.template.type == 5)).forEach(item -> {
            if (item.isBUg()) {
                item.itemOptions.clear();
                item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(10, 18)));
                item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 25)));
            }
        });
        player.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && item.template.type == 5).forEach(item -> {
            if (item.isBUg()) {
                item.itemOptions.clear();
                item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(10, 18)));
                item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 25)));
            }
        });
        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.type == 5).forEach(item -> {
            if (item.isBUg()) {
                item.itemOptions.clear();
                item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 43)));
                item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(10, 18)));
                item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 25)));
            }
        });
        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && (item.template.type == 11 || item.template.type == 35)).forEach(item -> {
            if (item.isBUg2()) {
                item.itemOptions.clear();
                item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(3, 10)));
                item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 12)));
            }
        });
        player.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && (item.template.type == 11 || item.template.type == 35)).forEach(item -> {
            if (item.isBUg2()) {
                item.itemOptions.clear();
                item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(3, 10)));
                item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 12)));
            }
        });
        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && (item.template.type == 11 || item.template.type == 35)).forEach(item -> {
            if (item.isBUg2()) {
                item.itemOptions.clear();
                item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(12, 22)));
                item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(3, 10)));
                item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(10, 12)));
            }
        });
//        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.type == 32).forEach(item -> {
//            if (item.isBUg3()) {
//                item.itemOptions.clear();
//                item.itemOptions.add(new Item.ItemOption(73, 0));
//                }
//        });
//        player.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && item.template.type == 32).forEach(item -> {
//           if (item.isBUg3()) {
//                item.itemOptions.clear();
//                item.itemOptions.add(new Item.ItemOption(73, 0));
//                }
//        });
//        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.type == 32).forEach(item -> {
//            if (item.isBUg3()) {
//                item.itemOptions.clear();
//                item.itemOptions.add(new Item.ItemOption(73, 0));
//                }
//        });

//        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id >= 1272 && item.template.id <= 1282).forEach(item -> {
//            InventoryServiceNew.gI().subQuantityItemsBag(player, item, item.quantity);
//        });
//        player.inventory.itemsBody.stream().filter(item -> item.isNotNullItem() && item.template.id >= 1272 && item.template.id <= 1282).forEach(item -> {
//            InventoryServiceNew.gI().subQuantityItemsBody(player, item, item.quantity);
//        });
//        player.inventory.itemsBox.stream().filter(item -> item.isNotNullItem() && item.template.id >= 1272 && item.template.id <= 1282).forEach(item -> {
//            InventoryServiceNew.gI().subQuantityItemsBox(player, item, item.quantity);
//        });
//thỏi vàng       
//        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 457 && item.quantity <= 100000).forEach(item -> {
//            if (!item.isTrangBiHSD()) {
//                item.itemOptions.add(new Item.ItemOption(93, 15));
//            }
//
//        });
//        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 457 && (item.quantity > 100000 && item.quantity <= 500000)).forEach(item -> {
//            if (!item.isTrangBiHSD()) {
//                item.itemOptions.add(new Item.ItemOption(93, 25));
//            }
//
//        });
//        player.inventory.itemsBag.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).forEach(item -> {
//            Item.ItemOption option_210 = new Item.ItemOption();
//            for (Item.ItemOption itopt : item.itemOptions) {
//                if (itopt.optionTemplate.id == 210) {
//                    System.out.println("220");
//                    option_210 = itopt;
//                }
//
//            }
//            if (option_210 != null) {
//                item.itemOptions.remove(option_210);
//            }
//
//        });
        InventoryServiceNew.gI().sendItemBags(player);
    }
}
