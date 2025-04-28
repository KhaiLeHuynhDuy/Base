package nro.services;

//import nro.database.GirlkunDB;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import java.io.DataOutputStream;
import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.utils.FileIO;
import nro.data.DataGame;
import nro.jdbc.daos.GodGK;
import nro.models.boss.BossManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.mob.Mob;
import nro.models.player.Pet;
import nro.models.item.Item.ItemOption;
import nro.models.map.Zone;
import nro.models.matches.TOP;
import nro.models.player.Player;
import nro.server.io.MySession;
import nro.models.skill.Skill;
import nro.network.io.Message;
import nro.network.server.GirlkunSessionManager;
import nro.network.session.ISession;
import nro.network.session.Session;
//import nro.result.GirlkunResultSet;
import nro.server.Client;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.services.func.ChangeMapService;
import nro.services.func.Input;

import nro.utils.Logger;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import nro.models.card.RadarService;
import nro.models.npc.Npc;
import nro.utils.ErrorResLover;

public class Service {

    private static Service instance;

    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void sendMessAllPlayer(Message msg) {
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendTopRank(Player pl) {
        Message msg;
        try {
            msg = new Message(-119);
            msg.writer().writeInt(1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEffDanhHieu(Player pl, int id, int layer, int loop, int loopcount, int stand, int sss) {
        Message msg;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopcount);
            msg.writer().writeByte(stand);
            msg.writer().writeByte(sss);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEffChanMenh(Player pl, int id, int layer, int loop, int loopcount, int stand) {
        Message msg;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopcount);
            msg.writer().writeByte(stand);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeEffDH(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.sendMessage(me);
            this.sendMessAllPlayerInMap(player.zone, me);
            me.cleanup();
//            if (player.inventory.itemsBody.get(9).isNotNullItem()) {
//                 Service.gI().sendEffChanMenh(player, player.getEffectchar2(), 0, -1, 1, -1);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeEffCM(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.sendMessage(me);
            this.sendMessAllPlayerInMap(player.zone, me);
            me.cleanup();
            if (player.inventory.itemsBody.get(6).isNotNullItem()) {
                Service.gI().sendEffDanhHieu(player, player.getEffectchar(), 1, -1, 50, -1, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAlleff(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.sendMessage(me);
            this.sendMessAllPlayerInMap(player.zone, me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void addEffectChar(Player pl, int id, int layer, int loop, int loopcount, int stand) {
//        try {
////            id = pl.getEffectchar();
//            Message msg = new Message(-128);
//            msg.writer().writeByte(0);
//            msg.writer().writeInt((int) pl.id);
//            msg.writer().writeShort(id);
//            msg.writer().writeByte(layer);
//            msg.writer().writeByte(loop);
//            msg.writer().writeShort(loopcount);
//            msg.writer().writeByte(stand);
//
////            sendMessAllPlayerInMap(pl.zone, msg);
//            sendMessAllPlayerInMap(pl.zone, msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
////    public void removeEffectChar2(Player pl, int... id) {
////        try {
////            Message msg = new Message(-128);
////            if (id.length > 0) {
////                msg.writer().writeByte(1);
////            } else {
////                msg.writer().writeByte(2);
////            }
////            msg.writer().writeInt((int) pl.id);
////            if (id.length > 0) {
////                msg.writer().writeShort(id[0]);
////            }
////            pl.isChanMenh = false;
////            sendMessAllPlayerInMap(pl.zone, msg);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
////
//    public void addEffectChar2(Player pl, int id, int layer, int loop, int loopcount, int stand) {
//        try {
//            Message msg = new Message(-128);
//            msg.writer().writeByte(0);
//            msg.writer().writeInt((int) pl.id);
//            msg.writer().writeShort(id);
//            msg.writer().writeByte(layer);
//            msg.writer().writeByte(loop);
//            msg.writer().writeShort(loopcount);
//            msg.writer().writeByte(stand);
//            Service.gI().sendMessAllPlayerInMap(pl.zone, msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
    //_____________________Top Siêu Hạng___________________________________
    public void showListTop(Player player, List<TOP> tops, byte isPVP) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int) pl.rankSieuHang);
//                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
//                msg.writer().writeUTF(isPVP == 1 ? top.getInfo2() : top.getInfo2() + pl.numKillSieuHang);
                msg.writer().writeUTF(isPVP == 1 ? ("Sức Đánh: " + pl.nPoint.dame + "\n" + "HP: " + pl.nPoint.hp + "\n" + "KI: " + pl.nPoint.mp + "\n" + "Điểm hạng: " + pl.rankSieuHang + "\n" + pl.name + (pl.zone.map.mapId == 113 ? " đang thi đấu tại " : " đang hoạt động tại ") + pl.zone.map.mapName + " khu vực " + pl.zone.zoneId + "(" + pl.zone.map.mapId + ")") : top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void showListTopSD(Player player, List<TOP> tops) {
//        Message msg;
//        try {
//            // Tạo Map để cache dữ liệu Player, tránh gọi loadById nhiều lần
//            Map<Integer, Player> playerMap = new HashMap<>();
//            for (TOP top : tops) {
//                playerMap.put(top.getId_player(), GodGK.loadById(top.getId_player()));
//            }
//
//            // Sắp xếp danh sách theo sức đánh giảm dần
//            tops.sort((a, b) -> {
//                Player p1 = playerMap.get(a.getId_player());
//                Player p2 = playerMap.get(b.getId_player());
//                if (p1 == null || p2 == null) {
//                    return 0;
//                }
//                return Long.compare(p2.nPoint.dame, p1.nPoint.dame);
//            });
//
//            msg = new Message(-96);
//            msg.writer().writeByte(0);
//            msg.writer().writeUTF("Top");
//            msg.writer().writeByte(tops.size());
//
//            for (int i = 0; i < tops.size(); i++) {
//                TOP top = tops.get(i);
//                Player pl = playerMap.get(top.getId_player());
//                if (pl == null) {
//                    continue;
//                }
//
//                msg.writer().writeInt(i + 1);
//                msg.writer().writeInt((int) pl.id);
//                msg.writer().writeShort(pl.getHead());
//                if (player.getSession().version > 214) {
//                    msg.writer().writeShort(-1);
//                }
//                msg.writer().writeShort(pl.getBody());
//                msg.writer().writeShort(pl.getLeg());
//                msg.writer().writeUTF(pl.name);
//                msg.writer().writeUTF(top.getInfo1());
//
//                // Hiển thị thông tin sức đánh
//                msg.writer().writeUTF("🔥 Sức Đánh: " + pl.nPoint.dame
//                        + "\n❤️ HP: " + pl.nPoint.hp
//                        + "\n🔋 KI: " + pl.nPoint.mp
//                        + "\n📍 Vị trí: " + (pl.zone.map.mapId == 113 ? " Đang thi đấu tại " : " Đang hoạt động tại ")
//                        + pl.zone.map.mapName + " - Khu vực " + pl.zone.zoneId + "(" + pl.zone.map.mapId + ")");
//            }
//
//            player.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void showListTopDauNhanh(Player player, List<TOP> tops, byte isPVP, int start) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());

            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
//                msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int)pl.rankSieuHang);
                msg.writer().writeInt(start);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
//                msg.writer().writeUTF(isPVP == 1 ? top.getInfo2() : top.getInfo2() + pl.numKillSieuHang);
                msg.writer().writeUTF(isPVP == 1 ? ("Sức Đánh: " + pl.nPoint.dame + "\n" + "HP: " + pl.nPoint.hp + "\n" + "KI: " + pl.nPoint.mp + "\n" + "Điểm hạng: " + pl.rankSieuHang + "\n" + pl.name + (pl.zone.map.mapId == 113 ? " đang thi đấu tại " : " đang hoạt động tại ") + pl.zone.map.mapName + " khu vực " + pl.zone.zoneId + "(" + pl.zone.map.mapId + ")") : top.getInfo2());
                start++;
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListTop(Player player, List<TOP> tops) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadById(top.getId_player());
                if (pl == null) {
                    System.out.println("Player object is null for top player ID: " + top.getId_player());
                    continue; // Skip to the next iteration if player object is null
                }
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(top.getInfo1());
                msg.writer().writeUTF(top.getInfo2());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendInputText(Player pl, String title, int row, int[] inputtype, String[] text) {
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(row);
            for (int i = 0; i < row; i++) {
                msg.writer().writeUTF(text[i]);
                msg.writer().writeByte(inputtype[i]);
            }
            pl.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPetFollow(Player player, short smallId) {
        Message msg;
        try {
            byte frame = 1;
            msg = new Message(31);
            msg.writer().writeInt((int) player.id);
            if (smallId == 0) {
                msg.writer().writeByte(0);
            } else {
                msg.writer().writeByte(1);
                msg.writer().writeShort(smallId);
                msg.writer().writeByte(frame);
                int fr = 8; // so anh
                int x = 75; // dai rong = nhau
                int y = x;
                switch (smallId) {
                    case 20636:
                        fr = 44;
                        x = 70;
                        y = x;
                        break;
                    case 20638:
                        fr = 71;
                        x = 70;
                        y = x;
                        break;
                    case 20640:
                        fr = 51;
                        x = 70;
                        y = x;
                        break;
                    case 20642:
                        fr = 48;
                        x = 86;
                        y = x;
                        break;
                    case 20644:
                        fr = 95;
                        x = 75;
                        y = x;
                        break;
                    case 20646:
                        fr = 81;
                        x = 96;
                        y = x;
                        break;
                    case 20648:
                        fr = 36;
                        x = 96;
                        y = x;
                        break;
                    case 20650:
                        fr = 77;
                        x = 70;
                        y = x;
                        break;
                    case 20652:
                        fr = 48;
                        x = 50;
                        y = x;
                        break;
                    case 20654:
                        fr = 61;
                        x = 50;
                        y = x;
                        break;
                    case 20656:
                        fr = 71;
                        x = 96;
                        y = x;
                        break;
                    case 20658:
                        fr = 80;
                        x = 70;
                        y = x;
                        break;
                    case 20660:
                        fr = 61;
                        x = 96;
                        y = x;
                        break;
                    ///____________________________________
                    case 15067:
                        // id anh
                        x = 65;
                        y = x;
                        fr = 8;
                        break;
                    case 16280:
                        // id anh
                        x = 70;
                        y = 38;
                        fr = 8;
                        break;

                    case 15262:
                        fr = 10;
                        x = 32;
                        y = 45;
                        break;
                    case 15264:
                        fr = 10;
                        x = 32;
                        y = 40;
                        break;
                    case 15266:
                        fr = 10;
                        x = 32;
                        y = 42;
                        break;

                    case 16167:
                        fr = 71;
                        x = 96;
                        y = 96;
                        break;
                    case 16149:
                        fr = 71;
                        x = 70;
                        y = 70;
                        break;

                    case 16147:
                        fr = 44;
                        x = 70;
                        y = 70;
                        break;
                    case 16151:
                        fr = 51;
                        x = 70;
                        y = 70;
                        break;
                    case 16175:
                        fr = 45;
                        x = 45;
                        y = 45;
                        break;
                    case 16153:
                        fr = 48;
                        x = 86;
                        y = 86;
                        break;
                    case 16155:
                        fr = 95;
                        x = 75;
                        y = 75;
                        break;
                    case 16157:
                        fr = 81;
                        x = 96;
                        y = 96;
                        break;
                    case 16159:
                        fr = 36;
                        x = 96;
                        y = 96;
                        break;
                    case 16161:
                        fr = 77;
                        x = 70;
                        y = 70;
                        break;
                    case 16163:
                        fr = 48;
                        x = 50;
                        y = 50;
                        break;
                    case 16165:
                        fr = 61;
                        x = 50;
                        y = 50;
                        break;
                    case 16169:
                        fr = 80;
                        x = 70;
                        y = 70;
                        break;
                    case 16171:
                        fr = 61;
                        x = 96;
                        y = 96;
                        break;

                    case 14200:
                    case 14202:
                    case 14204:
                    case 14206:
                        fr = 3;
                        x = 24;// kéo dãn khung hình dạng ngang
                        y = 26;// tăng để lên khung hình, giảm để xuống khung hình
                        break;
                    case 14208:
                    case 14210:

                    case 16044:
                        fr = 3;
                        x = 24;// kéo dãn khung hình dạng ngang
                        y = 28;// tăng để lên khung hình, giảm để xuống khung hình
                        break;
                    case 16081:
                        fr = 3;
                        x = 32;// // kéo dãn khung hình dạng ngang
                        y = 28;// tăng để lên khung hình
                        break;
                    case 11703:
                        fr = 3;
                        x = 32;// // kéo dãn khung hình dạng ngang
                        y = 28;// tăng để lên khung hình
                        break;
                    case 28892:
                        fr = 9;
                        x = 32;// // kéo dãn khung hình dạng ngang
                        y = 40;// tăng để lên khung hình
                        break;
                    case 25246:
                        fr = 6;
                        x = 28;// // kéo dãn khung hình dạng ngang
                        y = 32;// tăng để lên khung hình
                        break;

                    case 20418:
                        fr = 9;
                        x = 32;// // kéo dãn khung hình dạng ngang
                        y = 40;// tăng để lên khung hình
                        break;
                    case 20420:
                        fr = 9;
                        x = 32;// // kéo dãn khung hình dạng ngang
                        y = 40;// tăng để lên khung hình
                        break;
                    case 20421:
                        fr = 9;
                        x = 32;// // kéo dãn khung hình dạng ngang
                        y = 40;// tăng để lên khung hình
                        break;
                    case 20211:
                        fr = 6;
                        x = 28;// // kéo dãn khung hình dạng ngang
                        y = 32;// tăng để lên khung hình
                        break;
                    case 20213:
                        fr = 6;
                        x = 28;// // kéo dãn khung hình dạng ngang
                        y = 32;// tăng để lên khung hình
                        break;

                    default:
                        break;
                }
                msg.writer().writeByte(fr);
                for (int i = 0; i < fr; ++i) {
                    msg.writer().writeByte(i);

                }
                msg.writer().writeShort(x);
                msg.writer().writeShort(y);

            }
//            sendMessAllPlayerInMap(player.zone, msg);
            sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace(); // hoặc xử lý lỗi một cách thích hợp
        }

    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        if (zone != null) {
            List<Player> players = zone.getPlayers();
//            synchronized (players) {
            for (Player pl : players) {
                if (pl != null) {
                    pl.sendMessage(msg);
                }
            }
//            }
//            for (int i = 0; i < players.size(); i++) {
//                Player pl = players.get(i);
//                if (pl != null) {
//                    pl.sendMessage(msg);
//                }
//            }
            msg.cleanup();
        }
    }

//    public void sendMessAllPlayerInMap(Player player, Message msg) {
//        msg.transformData();
//        if (player.zone != null) {
//            if (player.zone.map.isMapOffline) {
//                if (player.isPet) {
//                    ((Pet) player).master.sendMessage(msg);
//                } else {
//                    player.sendMessage(msg);
//                }
//            } else {
//                List<Player> players = player.zone.getPlayers();
//                for (Player pl : players) {
////                for (Player pl : player.zone.getPlayers()) {
//                    if (pl != null) {
//                        pl.sendMessage(msg);
//                    }
//                }
//                msg.cleanup();
//            }
//        }
//    }
//    public void sendMessAllPlayerInMap(Player player, Message msg) {
//        msg.transformData();
//        if (player.zone != null) {
//            if (player.zone.map.isMapOffline) {
//                if (player.isPet) {
//                    ((Pet) player).master.sendMessage(msg);
//                } else {
//                    player.sendMessage(msg);
//                }
//            } else {
//                List<Player> players = player.zone.getPlayers();
//                synchronized (players) {
//                    for (Player pl : players) {
//                        if (pl != null) {
//                            pl.sendMessage(msg);
//                        }
//                    }
//                }
//
//                msg.cleanup();
////            } else {
////                List<Player> players = player.zone.getPlayers();
////                for (int i = 0; i < players.size(); i++) {
////                    try {
////                        Player pl = players.get(i);
////                        if (pl != null) {
////                            pl.sendMessage(msg);
////                        }
////                    } catch (Exception e) {
////
////                    }
////                }
////
////                msg.cleanup();
//            }
//        }
//    }
    public void regisAccount(Session session, Message _msg) {
        try {
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            _msg.readUTF();
            String user = _msg.readUTF();
            String pass = _msg.readUTF();
            if (!(user.length() >= 4 && user.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Tài khoản phải có độ dài 4-18 ký tự");
                return;
            }
            if (!(pass.length() >= 6 && pass.length() <= 18)) {
                sendThongBaoOK((MySession) session, "Mật khẩu phải có độ dài 6-18 ký tự");
                return;
            }
            GirlkunResultSet rs = GirlkunDB.executeQuery("select * from account where username = ?", user);
            if (rs.first()) {
                sendThongBaoOK((MySession) session, "Tài khoản đã tồn tại");
            } else {
                pass = (pass);
                GirlkunDB.executeUpdate("insert into account (username, password, recaf, admin, vnd, tongnap) values()", user, pass, 0, 0, 0, 0);
                sendThongBaoOK((MySession) session, "Đăng ký tài khoản thành công!");
            }
            rs.dispose();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

//    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
//        if (player.zone != null) {
//            List<Player> players = player.zone.getPlayers();
//            synchronized (players) {
//                for (Player pl : players) {
//                    if (pl != null) {
//                        pl.sendMessage(msg);
//                    }
//                }
//            }
//            msg.cleanup();
//        }
//    }
    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player.zone != null) {
            List<Player> players = player.zone.getPlayers();
//            synchronized (players) {
            for (Player pl : players) {
                if (pl != null && !pl.equals(player)) {
                    pl.sendMessage(msg);
                }
            }
//            }
//            for (int i = 0; i < players.size(); i++) {
//                try {
//                    Player pl = players.get(i);
//                    if (pl != null) {
//                        pl.sendMessage(msg);
//                    }
//                } catch (Exception e) {
//
//                }
//            }
            msg.cleanup();
        }
    }
//    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
//        try {
//            Message mes = msg;
//            player.zone.getPlayers().stream()
//                    .filter(pl -> pl != null && pl.session != null && pl.isPl() && !player.equals(pl)).forEach((pl) -> {
//                pl.sendMessage(mes);
//            });
//            mes.cleanup();
//        } catch (Exception e) {
//        } finally {
//            if (msg != null) {
//                msg.cleanup();
//                msg = null;
//            }
//        }
//    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);//Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writeFix(Util.maxIntValue(pl.nPoint.hp));
            msg.writer().writeByte(0);//Hiệu ứng Ăn Đậu
            msg.writeFix(Util.maxIntValue(pl.nPoint.hpMax));
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writeFix(Util.maxIntValue(pl.nPoint.hp));
            msg.writer().writeByte(1);
            msg.writeFix(Util.maxIntValue(pl.nPoint.hpMax));
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void loginDe(MySession session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToRegisterScr(ISession session) {

        Message msg;
        try {
            msg = new Message(42);
            msg.writeByte(0);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chat(Player player, String text) {
        if (player == null) {
            Logger.error("player null in Service_chat");
            return;
        }

        if (text.equals("tt")) {
            String dotPhaText;
            if (player.dotpha == 0) {
                dotPhaText = "Không";
            } else if (player.dotpha == 2) {
                dotPhaText = "Thể";
            } else if (player.dotpha == 1) {
                dotPhaText = "Pháp";
            } else {
                dotPhaText = "Không xác định"; // Trường hợp giá trị không hợp lệ
            }
            String capTTText;
            if(player.capTT ==0){
                capTTText = "Người Phàm";
            }
            else if(player.capTT == 1){
                capTTText = "Luyện Khí";
            }
            else if(player.capTT == 2 && player.isUseTrucCoDan == true){
                capTTText = "Thiên Đạo Trúc Cơ";
            }
            else if(player.capTT == 2){
                capTTText = "Trúc Cơ";
            }else {
                capTTText = "Không xác định"; // Trường hợp giá trị không hợp lệ
            }
            sendThongBaoOK(player, "Thông tin nhân vật: " + player.name
                    + "\n\nSức Mạnh: " + Util.getFormatNumber(player.nPoint.power)
                    + "\nChí Mạng: " + Util.getFormatNumber(player.nPoint.overflowcrit)
                    + "\nSức Đánh Chí Mạng: " + Util.getFormatNumber(
                            player.nPoint.tlDameCrit.stream().mapToInt(Integer::intValue).sum()
                    )
                    + "\n\nHp: " + Util.getFormatNumber(player.nPoint.hp) + "/" + Util.getFormatNumber(player.nPoint.hpMax)
                    + "\n\nKi: " + Util.getFormatNumber(player.nPoint.mp) + "/" + Util.getFormatNumber(player.nPoint.mpMax)
                    + "\n\nSức đánh: " + Util.getFormatNumber(player.nPoint.dame)
                    + "\n\nCấp chuyển sinh: " + Util.getFormatNumber(player.capCS)
                    + "\n\nCảnh giới: " + capTTText
                    + "\n\nĐột Phá: " + dotPhaText
                    + "\n\nĐại khai sát giới : Cấp 0 ( sắp update )"
            );
        }
        if (text.equals("boss")) {
            BossManager.gI().showListBoss(player);
        }
        if (text.equals("top")) {
            Service.gI().showListTop(player, Manager.topSD);
        }
//        if (text.equals("info")) {
//            DecimalFormat decimalFormat = new DecimalFormat("#,###");
//            String hp = decimalFormat.format(player.nPoint.hp);
//            String hpMax = decimalFormat.format(player.nPoint.hpMax);
//            String ki = decimalFormat.format(player.nPoint.mp);
//            String kiMax = decimalFormat.format(player.nPoint.mpMax);
//            String dame = decimalFormat.format(player.nPoint.dame);
////            String dameg = decimalFormat.format(player.nPoint.d);
//            String info = "HP: " + hp + " / " + hpMax + "\n";
//            info += "KI: " + ki + " / " + kiMax + "\n";
//            info += "Sức đánh: " + dame + "";
//            Service.gI().sendThongBaoOK(player, info);
//            return;
//        }
        if (text.startsWith("cheat")) {
            try {
                String s = text.substring(5);
                int speed = Integer.parseInt(s);
                player.nPoint.speed = (byte) speed;
                point(player);
                return;
            } catch (NumberFormatException e) {
                System.err.println("Invalid speed input: " + e.getMessage());
            }
        }

        if (player.isAdmin()) {
            if (text.equals("hskill")) {
                Service.gI().releaseCooldownSkill(player);
                return;
            }
            if (text.equals("skillxd")) {
                SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                return;
            }
            if (text.equals("skilltd")) {
                SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                return;
            }
            if (text.equals("skillnm")) {
                SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                return;
            }
            switch (text) {
                case "client":
                    Client.gI().show(player);
                    break;
                case "map":
                    sendThongBao(player, "Thông tin map: " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                    return;
                case "vt":
                    sendThongBao(player, player.location.x + " - " + player.location.y + "\n"
                            + player.zone.map.yPhysicInTop(player.location.x, player.location.y));
                    break;
                case "hs":
                    player.nPoint.setFullHpMp();
                    PlayerService.gI().sendInfoHpMp(player);
                    sendThongBao(player, "Quyền năng trị liệu\n");
                    return;
                    
                    //khai comment
                    
//                case "b": {
//                    Message msg;
//                    try {
//                        msg = new Message(52);
//                        msg.writer().writeByte(0);
//                        msg.writer().writeInt((int) player.id);
//                        sendMessAllPlayerInMap(player.zone, msg);
//                        msg.cleanup();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//                case "c": {
//                    Message msg;
//                    try {
//                        msg = new Message(52);
//                        msg.writer().writeByte(2);
//                        msg.writer().writeInt((int) player.id);
//                        msg.writer().writeInt((int) player.zone.getHumanoids().get(1).id);
//                        sendMessAllPlayerInMap(player.zone, msg);
//                        msg.cleanup();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
                default:
                    break;
            }
            if (player.isAdmin() && text.equals("nrnm")) {
                Service.gI().activeNamecShenron(player);
            }
//            if (player.isAdmin() && text.equals("ts")) {
//                sendThongBao(player, "Time start server: " + ServerManager.timeStart + "\n");
//                return;
//            }
            if (player.isAdmin() && text.equals("showboss")) {
                BossManager.gI().showListBoss(player);
                return;
            }
            if (text.equals("rs")) { // hồi all skill, Ki
                Service.gI().releaseCooldownSkill(player);
                player.nPoint.setFullHpMp();
                PlayerService.gI().sendInfoHpMp(player);
                return;
            }
//            if (text.equals("test")) { // test
//                try {
//
//                } catch (Exception e) {
//                    ErrorResLover.howToFix(e.toString());
//                }
//                return;
//            }
            if (text.startsWith("i")) {
                String[] parts = text.split(" ");
                if (parts.length >= 3) {
                    short id = Short.parseShort(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    Item item = ItemService.gI().createNewItem(id, quantity);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendThongBao(player, "Bạn nhận được " + item.template.name + " số lượng: " + quantity);
                    return;
                } else {
                    Service.gI().sendThongBao(player, "Lỗi buff item");
                    return;
                }
            }
            if (player.isAdmin() && text.startsWith("admin")) {

                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1, "Quản trị Blue:\n"
                        + "\b|7|Số người online: " + (Client.gI().getPlayers().size()) + "\n"
                        + "\b|1|Thread: " + Thread.activeCount() + "\n"
                        + "|2|sessions: " + GirlkunSessionManager.gI().getSessions().size() + "\n"
                        + "Time start server: " + ServerManager.timeStart + "\n",
                        "Ngọc rồng", "Đệ tử", "Bảo trì", "Tìm kiếm\nngười chơi", "Call\nBoss", "Send item", "Send item\noption", "Send item\nSKH", "Send\nGOLDBAR", "Buff VNĐ", "BUFF TOTAL VND", "Buff item2", "Mail box", "Top tv", "Đóng");
                return;
            }
            if (text.startsWith("fly ")) {
                try {
                    String mapIdText = text.substring(4); // Loại bỏ "m " ở đầu chuỗi
                    if (!mapIdText.isEmpty()) {
                        int mapId = Integer.parseInt(mapIdText);
                        ChangeMapService.gI().changeMapBySpaceShip(player, mapId, -1, -1);
                        sendThongBao(player, "" + player.name + " đã bay đến: " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                        return;
                    } else {
                        sendThongBao(player, "Không hợp lệ");
                        // Xử lý trường hợp không có mã bản đồ được cung cấp
                        // Hiển thị thông báo lỗi hoặc thực hiện các hành động phù hợp khác
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // Xử lý ngoại lệ theo cách tương ứng
                }
            }

            if (text.startsWith("go ")) {
                try {
                    String mapIdText = text.substring(3); // Loại bỏ "m " ở đầu chuỗi
                    if (!mapIdText.isEmpty()) {
                        int mapId = Integer.parseInt(mapIdText);
                        ChangeMapService.gI().changeMapInYard(player, mapId, -1, -1);
                        sendThongBao(player, "" + player.name + " đã dịch chuyển tức thời đến: " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                        return;
                    } else {
                        sendThongBao(player, "Không hợp lệ");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("thread")) {

                sendThongBao(player, "Current thread: " + Thread.activeCount());
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                for (Thread beodeptrai : threadSet) {
                    System.out.println(beodeptrai.getName());
                }
            }
        }
        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
        } else if (text.equals(
                "info")) {
            Service.gI().sendThongBao(player, "##HP bản thân: " + Util.powerToString(player.nPoint.hp)
                    + "\n##KI bản thân: " + Util.powerToString(player.nPoint.mp) + "\n##SĐ bản thân: " + Util.powerToString(player.nPoint.dame)
                    + player.Hppl);
        }

        if (player.pet != null) {
            switch (text) {
                case "di theo":
                case "follow":
                    player.pet.changeStatus(Pet.FOLLOW);
                    break;
                case "bao ve":
                case "protect":
                    player.pet.changeStatus(Pet.PROTECT);
                    break;
                case "tan cong":
                case "attack":
                    player.pet.changeStatus(Pet.ATTACK);
                    break;
                case "ve nha":
                case "go home":
                    player.pet.changeStatus(Pet.GOHOME);
                    break;
                case "bien hinh":
                    player.pet.transform();
                    break;
                default:
                    break;
            }
        }

        if (text.length()
                > 100) {
            text = text.substring(0, 100);
        }
        Message msg;

        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Service.class, e);
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Transport(Player pl) {
        Message msg;
        try {
            msg = new Message(-105);
            msg.writer().writeShort(pl.maxTime);
            msg.writer().writeByte(pl.type);
            pl.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long exp_level1(long sucmanh) {
        if (sucmanh < 3000) {
            return 3000;
        } else if (sucmanh < 15000) {
            return 15000;
        } else if (sucmanh < 40000) {
            return 40000;
        } else if (sucmanh < 90000) {
            return 90000;
        } else if (sucmanh < 170000) {
            return 170000;
        } else if (sucmanh < 340000) {
            return 340000;
        } else if (sucmanh < 700000) {
            return 700000;
        } else if (sucmanh < 1500000) {
            return 1500000;
        } else if (sucmanh < 15000000) {
            return 15000000;
        } else if (sucmanh < 150000000) {
            return 150000000;
        } else if (sucmanh < 1500000000) {
            return 1500000000;
        } else if (sucmanh < 5000000000L) {
            return 5000000000L;
        } else if (sucmanh < 10000000000L) {
            return 10000000000L;
        } else if (sucmanh < 40000000000L) {
            return 40000000000L;
        } else if (sucmanh < 50010000000L) {
            return 50010000000L;
        } else if (sucmanh < 60010000000L) {
            return 60010000000L;
        } else if (sucmanh < 70010000000L) {
            return 70010000000L;
        } else if (sucmanh < 80010000000L) {
            return 80010000000L;
        } else if (sucmanh < 200010000000L) {
            return 200010000000L;
        } else if (sucmanh < 1000010000000L) {
            return 1000010000000L;
        }
        return 1000;
    }

    public void point(Player player) {
        player.nPoint.calPoint(); // Call the method if player.nPoint is not null
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss && !player.isNewPet && !player.isMiniPet) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writeFix(Util.maxIntValue(player.nPoint.hpg));
                msg.writeFix(Util.maxIntValue(player.nPoint.mpg));
                msg.writeFix(Util.maxIntValue(player.nPoint.dameg));
                msg.writeFix(Util.maxIntValue(player.nPoint.hpMax));// hp full
                msg.writeFix(Util.maxIntValue(player.nPoint.mpMax));// mp full
                msg.writeFix(Util.maxIntValue(player.nPoint.hp));
                msg.writeFix(Util.maxIntValue(player.nPoint.mp));// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writeFix(Util.maxIntValue(player.nPoint.dame));// dam base
                msg.writer().writeInt(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writer().writeInt(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
                Logger
                        .logException(Service.class,
                                e);
            }
        }
    }

    private void activeNamecShenron(Player pl) {
        Message msg;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(0);
            msg.writer().writeShort(pl.zone.map.mapId);
            msg.writer().writeShort(pl.zone.map.bgId);
            msg.writer().writeByte(pl.zone.zoneId);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeUTF("");
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            msg.writer().writeByte(1);
            Service.gI().sendMessAllPlayerInMap(pl.zone, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void player(Player pl) {
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); //cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            //--------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.skills;

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            //---vang---luong--luongKhoa
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            //--------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            //--------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }

            //--------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            //-----------------
            DataGame.sendHeadAvatar(msg);
            //-----------------
            msg.writer().writeShort(514); //char info id - con chim thông báo
            msg.writer().writeShort(515); //char info id
            msg.writer().writeShort(537); //char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); //nhập thể
            msg.writer().writeInt(333); //deltatime
//            msg.writer().writeInt(1632811835);
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); //is new member
//            msg.writer().writeShort(pl.idAura); //idauraeff
            msg.writer().writeShort(pl.getAura()); //idauraeff
            msg.writer().writeByte(pl.getEffFront());
            pl.sendMessage(msg);
        } catch (Exception e) {
            pl.getSession().disconnect();

        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
//                Service.gI().sendThongBao(player, "Cần mở giới hạn đệ tử");
                return;
            }
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            if (master.nPoint.power < master.nPoint.getPowerLimit()) {
                master.nPoint.powerUp(param);
            }
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        if (pl == null) {
            System.out.println("Player is null in getCurrStrLevel()");
            return "null power";
        }

        if (pl.nPoint == null) {
            System.out.println(pl.name + " getCurrStrLevel()_null nPoint");
            return "null power";
        }

        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 150010000000L) {
            return "Siêu thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 211010000000L) {
            return "Siêu thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 311010000000L) {
            return "Siêu thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 411010000000L) {
            return "Bản năng " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 411010000000L) {
            return "Bản năng " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 511010000000L) {
            return "Bản năng " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 611010000000L) {
            return "Bản ngã " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 711010000000L) {
            return "Bản ngã " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 811010000000L) {
            return "Bản ngã " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 911010000000L) {
            return "Thần Hủy Diệt " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 991010000000L) {
            return "Thần Hủy Diệt " + get_HanhTinh(pl.gender) + " cấp 2";
        }
//        return "Đại Thiên Sứ";
        return "null";
    }

    public int getCurrLevel(Player pl) {
        if (pl == null) {
            System.out.println("Player is null in getCurrLevel()");
            return 0;
        }

        if (pl.nPoint == null) {
            System.out.println(pl.name + " getCurrLevel()_null nPoint");
            return 0;
        }
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return 1;
        } else if (sucmanh < 15000) {
            return 2;
        } else if (sucmanh < 40000) {
            return 3;
        } else if (sucmanh < 90000) {
            return 4;
        } else if (sucmanh < 170000) {
            return 5;
        } else if (sucmanh < 340000) {
            return 6;
        } else if (sucmanh < 700000) {
            return 7;
        } else if (sucmanh < 1500000) {
            return 8;
        } else if (sucmanh < 15000000) {
            return 9;
        } else if (sucmanh < 150000000) {
            return 10;
        } else if (sucmanh < 1500000000) {
            return 11;
        } else if (sucmanh < 5000000000L) {
            return 12;
        } else if (sucmanh < 10000000000L) {
            return 13;
        } else if (sucmanh < 40000000000L) {
            return 14;
        } else if (sucmanh < 50010000000L) {
            return 15;
        } else if (sucmanh < 60010000000L) {
            return 16;
        } else if (sucmanh < 70010000000L) {
            return 17;
        } else if (sucmanh < 80010000000L) {
            return 18;
        } else if (sucmanh < 100010000000L) {
            return 19;
        } else if (sucmanh < 150010000000L) {
            return 20;
        } else if (sucmanh < 210010000000L) {
            return 21;
        } else if (sucmanh < 310000000000L) {
            return 22;
        } else if (sucmanh < 410000000000L) {
            return 23;
        } else if (sucmanh < 510000000000L) {
            return 24;
        } else if (sucmanh < 610000000000L) {
            return 25;
        } else if (sucmanh < 710000000000L) {
            return 26;
        } else if (sucmanh < 810000000000L) {
            return 27;
        } else if (sucmanh < 910000000000L) {
            return 28;
        } else if (sucmanh < 1000100000000L) {
            return 29;

        }
        return 0;
    }

    public void hsChar(Player pl, long hp, long mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet && !pl.isNewPet && !pl.isMiniPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }

            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writeFix(Util.maxIntValue(hp));
            msg.writeFix(Util.maxIntValue(mp));
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(Service.class,
                            e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet && !pl.isNewPet && !pl.isMiniPet) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else if (pl.isPet) {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }
            if (!pl.isPet && !pl.isNewPet && !pl.isMiniPet && !pl.isBoss && pl.idNRNM != -1) {
                ItemMap itemMap = new ItemMap(pl.zone, pl.idNRNM, 1, pl.location.x, pl.location.y, -1);
                Service.gI().dropItemMap(pl.zone, itemMap);
                NgocRongNamecService.gI().pNrNamec[pl.idNRNM - 353] = "";
                NgocRongNamecService.gI().idpNrNamec[pl.idNRNM - 353] = -1;
                pl.idNRNM = -1;
                PlayerService.gI().changeAndSendTypePK(pl, ConstPlayer.NON_PK);
                Service.gI().sendFlagBag(pl);
            }
            if (pl.zone.map.mapId == 51) {
                ChangeMapService.gI().changeMapBySpaceShip(pl, 21 + pl.gender, 0, -1);
            }
            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); //cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(Service.class,
                            e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);
                    break;
                }
            }
        }
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); //id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();

                msg.writer().writeShort(head);//set head
                msg.writer().writeShort(body);//setbody
                msg.writer().writeShort(leg);//set leg

                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);
                sendMessAllPlayerInMap(player.zone, msg);
                RadarService.gI().RadarSetAura(player);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
                Logger
                        .logException(Service.class,
                                e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(Service.class,
                            e);
        }
    }

    public void setNotBienHinh(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(Service.class,
                            e);
        }
    }

    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.getFlagBag());
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet || pl.isNewPet || pl.isMiniPet || pl.isBoss) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(Service.class,
                            e);
        }

    }

    public void sendThongBaoOK(MySession session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBigMessage2(Player player, int indexMenu, int iconId, String Npcsay, String... menuSelect) {
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(Npcsay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 1209, text);
    }

//    public void sendThongBaoFromNPC(Player player, short avatar, String text, String... menuSelect) {
//        sendBigMessage2(player, avatar, text, menuSelect);
//    }
    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendThongBao(List<Player> pl, String thongBao) {
        for (int i = 0; i < pl.size(); i++) {
            Player ply = pl.get(i);
            if (ply != null) {
                this.sendThongBao(ply, thongBao);
            }
        }
    }

    public void showYourNumber(Player player, String Number, String result, String finish, int type) {
        Message msg;
        try {
            msg = new Message(-126);
            msg.writer().writeByte(type); // 1 = RESET GAME | 0 = SHOW CON SỐ CỦA PLAYER
            if (type == 0) {
                msg.writer().writeUTF("Số may mắn bạn chọn là: " + Number);
            } else if (type == 1) {
                msg.writer().writeByte(type);
                msg.writer().writeUTF(result); //
                msg.writer().writeUTF(finish);
            }
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBigBoss(Zone map, int action, int size, int id, int dame) {
        Message msg;
        try {
            msg = new Message(102);
            msg.writer().writeByte(action);
            if (action != 6 && action != 7) {
                msg.writer().writeByte(size); // SIZE PLAYER ATTACK
                msg.writer().writeInt(id); // PLAYER ID
                msg.writer().writeInt(dame); // DAME
            }
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBigBoss2(Zone map, int action, Mob bigboss) {
        Message msg;
        try {
            msg = new Message(101);
            msg.writer().writeByte(action);
            msg.writer().writeShort(bigboss.location.x);
            msg.writer().writeShort(bigboss.location.y);
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            if (pl.getSession().version >= 214) {
                msg.writer().writeLong(pl.inventory.gold);
            } else {
                msg.writer().writeInt((int) pl.inventory.gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
//            sendMessAllPlayerIgnoreMe(player, msg);
            sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void sendBag(Player pl) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.getFlagBag());
            this.sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final int[] flagTempId = {363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747};
    public static final int[] flagIconId = {2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325};

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeShort(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeShort(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeFlag(Player pl, int index) {
        try {
            pl.cFlag = (byte) index;
            if (index < 0 || index >= flagIconId.length) {
                System.out.println("Service_changeFlag : Index không hợp lệ: " + index);
                return;
            }
            Message msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            sendMessAllPlayerInMap(pl.zone, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                sendMessAllPlayerInMap(pl.pet.zone, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                sendMessAllPlayerInMap(pl.pet.zone, msg);
                msg.cleanup();
            }
            pl.iDMark.setLastTimeChangeFlag(System.currentTimeMillis());
            if (pl.gender == 2 && index == 8) {
                pl.khisukien = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        try {
            if (pl.cFlag >= 0 && pl.cFlag < flagIconId.length) { // Kiểm tra chỉ số hợp lệ trước khi truy cập mảng
                Message msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(pl.cFlag);
                msg.writer().writeShort(flagIconId[pl.cFlag]); // Sử dụng chỉ số hợp lệ của cFlag để truy cập mảng
                me.sendMessage(msg);
                msg.cleanup();
            } else {
                sendThongBao(pl, "Có lỗi xảy ra vui lòng báo admin_sendFlagPlayerToMe");
                System.out.println("sendFlagPlayerToMe_Invalid cFlag value: " + pl.cFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (MapService.gI().isMapBlackBallWar(pl.zone.map.mapId) || MapService.gI().isMapMaBu(pl.zone.map.mapId) || MapService.gI().isMapPVP(pl.zone.map.mapId) || MapService.gI().isMapVS(pl.zone.map.mapId)) {
            sendThongBao(pl, "Không thể đổi cờ ở khu vực này!");
            return;
        }
        if (Util.canDoWithTime(pl.iDMark.getLastTimeChangeFlag(), 60000)) {
            changeFlag(pl, index);
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi " + TimeUtil.getTimeLeft(pl.iDMark.getLastTimeChangeFlag(), 60) + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                skill.coolDown = 0;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void HoiSkill(Player pl, short id, int cooldown) {
        Message msg;
        try {
            msg = new Message(-94);
            msg.writer().writeShort(id);
            msg.writer().writeInt(cooldown);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.skills) {
                msg.writer().writeShort(skill.skillId);
                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt(3);//
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(Service.class,
                            e);
        }
    }

    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        int countOption = item.itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : item.itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeShort(iop.param);
                        }
                    }
                }

                //msg.writeFix(Util.maxIntValue(pl.pet.nPoint.hp)); //hp
                msg.writeFix(Util.maxIntValue(pl.pet.nPoint.hp));
                msg.writeFix(Util.maxIntValue(pl.pet.nPoint.hpMax)); //hpfull
                msg.writeFix(Util.maxIntValue(pl.pet.nPoint.mp)); //mp
                msg.writeFix(Util.maxIntValue(pl.pet.nPoint.mpMax)); //mpfull
                msg.writeFix(Util.maxIntValue(pl.pet.nPoint.dame)); //damefull
                msg.writer().writeUTF(pl.pet.name); //name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); //curr level
                msg.writer().writeLong(pl.pet.nPoint.power); //power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); //tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); //status
                msg.writer().writeShort(pl.pet.nPoint.stamina); //stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); //stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); //crit
                msg.writer().writeInt(pl.pet.nPoint.def); //def
                int sizeSkill = pl.pet.playerSkill.skills.size();
                msg.writer().writeByte(5); //counnt pet skill
                for (int i = 0; i < sizeSkill; i++) {
                    if (pl.pet.playerSkill.skills.get(i).skillId != -1) {
                        msg.writer().writeShort(pl.pet.playerSkill.skills.get(i).skillId);
                    } else {
                        switch (i) {
                            case 1:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 150tr để mở");
                                break;
                            case 2:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 1tỷ5 để mở");
                                break;
                            case 3:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 20tỷ\nđể mở");
                                break;
                            case 4:
                                msg.writer().writeShort(-1);
                                msg.writer().writeUTF("Cần đạt sức mạnh 150tỷ\nđể mở");
                                break;
                            default:
                                break;
                        }
                    }
                }

                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                e.printStackTrace();
                Logger
                        .logException(Service.class,
                                e);
            }
        }
    }

    public void sendChiSoPetGoc(Player pl) {
        if (pl == null || pl.pet == null) {
            return;
        }

        try {
            Message msg = new Message(-109);
            msg.writeFix(pl.pet.nPoint.hpg);
            msg.writeFix(pl.pet.nPoint.mpg);
            msg.writeFix(pl.pet.nPoint.dameg);
            msg.writer().writeInt(pl.pet.nPoint.defg);
            msg.writer().writeShort(pl.pet.nPoint.critg);

            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(Service.class, e, "sendChiSoPetGoc");
        }
    }

    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);

            if (pl.nPoint != null) {
                msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(Service.class,
                            e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.gI().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);

            }
        } catch (Exception e) {
            Logger.logException(Service.class,
                    e);
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|7|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            msg.writer().writeShort(-1);
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); //bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 6) {
                if (newPass.equals(rePass)) {
                    String hashedPassword = newPass;
                    player.getSession().pp = hashedPassword;
                    try {
                        GirlkunDB.executeUpdate("update account set password = ? where id = ? and username = ?",
                                hashedPassword, player.getSession().userId, player.getSession().uu);
                        Service.gI().sendThongBao(player, "Đổi mật khẩu thành công!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Service.gI().sendThongBao(player, "Đổi mật khẩu thất bại!");
                        Logger
                                .logException(Service.class,
                                        ex);
                    }
                } else {
                    Service.gI().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.gI().sendThongBao(player, "Mật khẩu ít nhất 6 ký tự!");
            }
        } else {
            Service.gI().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(MySession session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCaption(MySession session, byte gender) {
        Message msg;
        try {
            msg = new Message(-41);
            msg.writer().writeByte(Manager.CAPTIONS.size());
            for (String caption : Manager.CAPTIONS) {
                msg.writer().writeUTF(caption.replaceAll("%1", gender == ConstPlayer.TRAI_DAT ? "Trái đất"
                        : (gender == ConstPlayer.NAMEC ? "Namếc" : "Xayda")));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendWaitToLogin(MySession session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger
                    .logException(Service.class,
                            e);
        }
    }

//    public void sendMessage(MySession session, int cmd, String path) {
//        Message msg;
//        try {
//            byte[] b = FileIO.readFile(path);
//            if (b != null) {
//                msg = new Message(cmd);
//                msg.writer().write(b);
//                session.sendMessage(msg);
//                msg.cleanup();
//            } else {
//                // Xử lý trường hợp không thể đọc tệp
////                System.err.println("Không thể đọc hoặc tệp không tồn tại.");
//            }
//        } catch (FileNotFoundException e) {
//            // Xử lý ngoại lệ khi không tìm thấy tệp
//            e.printStackTrace();
//        } catch (IOException e) {
//            // Xử lý các ngoại lệ liên quan đến IO khác
//            e.printStackTrace();
//        } catch (Exception e) {
//            // Xử lý bất kỳ ngoại lệ nào khác
//            e.printStackTrace();
//        }
//    }
//    public void sendMessage(MySession session, int cmd, String path) {
//        Message msg;
//        try {
//            msg = new Message(cmd);
//            msg.writer().write(FileIO.readFile(path));
//            session.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void sendMessage(ISession session, int cmd, String filename) {
        Message msg = null;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile("data/girlkun/msg/" + filename));
            session.sendMessage(msg);
        } catch (Exception e) {
            Logger
                    .logException(Service.class,
                            e);
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setClientType(MySession session, Message msg) {
//        try {
//            session.typeClient = (msg.reader().readByte());//client_type
//            session.zoomLevel = msg.reader().readByte();//zoom_level
//            msg.reader().readBoolean();//is_gprs
//            msg.reader().readInt();//width
//            msg.reader().readInt();//height
//            msg.reader().readBoolean();//is_qwerty
//            msg.reader().readBoolean();//is_touch
//            String platform = msg.reader().readUTF();
//            String[] arrPlatform = platform.split("\\|");
//            session.version = Integer.parseInt(arrPlatform[1].replaceAll("\\.", ""));
//
//            System.out.println("Thietbi:" + platform +  "
//         );
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            msg.cleanup();
//        }
//        DataGame.sendLinkIP(session);
//    }
    private static final HashMap<String, Long> lastRequestTime = new HashMap<>();

    public void setClientType(MySession session, Message msg) {
        try {
            session.typeClient = (msg.reader().readByte()); // client_type
            session.zoomLevel = msg.reader().readByte(); // zoom_level
            msg.reader().readBoolean(); // is_gprs
            msg.reader().readInt(); // width
            msg.reader().readInt(); // height
            msg.reader().readBoolean(); // is_qwerty
            msg.reader().readBoolean(); // is_touch
            String platform = msg.reader().readUTF();
            String[] arrPlatform = platform.split("\\|");
            session.version = Integer.parseInt(arrPlatform[1].replaceAll("\\.", ""));

////             Kiểm tra thời gian yêu cầu từ thiết bị
//            synchronized (lastRequestTime) {
//                long currentTime = System.currentTimeMillis();
//                Long lastTime = lastRequestTime.get(platform);
//                if (lastTime != null && currentTime - lastTime < 5000) {
//                    // Yêu cầu quá thường xuyên, không xử lý và trả về
//                    
//                    System.out.println("Yêu cầu từ thiết bị " + platform + " bị từ chối do thực hiện quá thường xuyên.");
//                    return;
//                }
//                lastRequestTime.put(platform, currentTime);
//            }
            System.out.println("Thiết bị: " + platform);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            msg.cleanup();
        }
        DataGame.sendLinkIP(session);
    }

    public void DropVeTinh(Player pl, Item item, Zone map, int x, int y) {
        ItemMap itemMap = null;
        if (pl.clan != null) {
            itemMap = new ItemMap(map, item.template, item.quantity, x, y, pl.id, pl.clan);
        } else {
            itemMap = new ItemMap(map, item.template, item.quantity, x, y, pl.id);
        }
        itemMap.options = item.itemOptions;
        map.addItem(itemMap);
        Message msg = null;
        try {
            msg = new Message(68);
            msg.writer().writeShort(itemMap.itemMapId);
            msg.writer().writeShort(itemMap.itemTemplate.id);
            msg.writer().writeShort(itemMap.x);
            msg.writer().writeShort(itemMap.y);
            msg.writer().writeInt(-2);
            msg.writer().writeShort(200);
            sendMessAllPlayerInMap(map, msg);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void stealMoney(Player pl, int stealMoney) {//danh cho boss an trom
        Message msg;
        try {
            msg = new Message(95);
            msg.writer().writeInt(stealMoney);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendImgSkill9(short SkillId, int IdAnhSKill) {
        Message msg = new Message(62);
        DataOutputStream ds = msg.writer();
        try {
            ds.writeShort(SkillId);
            ds.writeByte(1);
            ds.writeByte(IdAnhSKill);
            ds.flush();
            Service.gI().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void sendTextTime(Player pl, int itemId, String text, int time) {
//        Message msg = null;
//        try {
//            msg = new Message(65);
//            msg.writer().writeByte(itemId);
//            msg.writer().writeUTF(text);
//            msg.writer().writeShort(time);
//            this.sendMessClanAllPlayer(msg, pl);
//        } catch (Exception e) {
//        } finally {
//            if (msg != null) {
//                msg.cleanup();
//                msg = null;
//            }
//        }
//    }
//      public void sendMessClanAllPlayer(Message msg, Player player) {
//        try {
//            Message mes = msg;
//            Client.gI().getPlayers().stream().filter(pl -> pl.session != null && pl != null && pl.clan != null
//                    && pl.clan.id == player.clan.id && pl.isPl()).forEach((pl) -> {
//                pl.sendMessage(mes);
//            });
//            mes.cleanup();
//        } catch (Exception e) {
//        } finally {
//            if (msg != null) {
//                msg.cleanup();
//                msg = null;
//            }
//        }
//    }
//
//    public void removeTextTime(Player pl, int itemId) {
//        sendTextTime(pl, itemId, "", 0);
//    }
//
//    public void sendItemTime(Player pl, int itemId, int time) {
//        Message msg = null;
//        try {
//            msg = new Message(-106);
//            msg.writer().writeShort(itemId);
//            msg.writer().writeShort(time);
//            pl.sendMessage(msg);
//        } catch (Exception e) {
//        } finally {
//            if (msg != null) {
//                msg.cleanup();
//                msg = null;
//            }
//        }
//    }
//
//    public void removeItemTime(Player pl, int itemTime) {
//        sendItemTime(pl, itemTime, 0);
//    }
}
