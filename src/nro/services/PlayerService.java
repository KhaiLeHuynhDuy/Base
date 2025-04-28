package nro.services;

import com.girlkun.database.GirlkunDB;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstTask;
import nro.consts.ConstTranhNgocNamek;
import nro.models.phuban.DragonNamecWar.TranhNgocService;
//import nro.database.GirlkunDB;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.server.Client;
import nro.services.func.ChangeMapService;
import nro.utils.Logger;
import nro.utils.Util;

public class PlayerService {

    private static PlayerService i;

    public PlayerService() {
    }

    public static PlayerService gI() {
        if (i == null) {
            i = new PlayerService();
        }
        return i;
    }

    public void sendTNSM(Player player, byte type, long param) {
        if (param > 0) {
            Message msg;
            try {
                msg = new Message(-3);
                msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
                msg.writeFix((int) param);// số tn cần cộng
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//     public void sendMessageAllPlayer(Message msg) {
//        List<Player> fixPlayers = new ArrayList<>(Client.gI().getPlayers());
//        for (Player pl : fixPlayers) {
//            if (pl != null) {
//                pl.sendMessage(msg);
//            }
//        }
//        msg.cleanup();
//
//    }
//
//    public void sendMessageIgnore(Player plIgnore, Message msg) {
//        for (Player pl : Client.gI().getPlayers()) {
//            if (pl != null && !pl.equals(plIgnore)) {
//                pl.sendMessage(msg);
//            }
//        }
//        msg.cleanup();
//
//    }
//    public void sendMessageAllPlayer(Message msg) {
//        List<Player> players = new ArrayList<>(Client.gI().getPlayers());
//        for (Player pl : players) {
//            if (pl != null) {
//                pl.sendMessage(msg);
//            }
//        }
//        msg.cleanup();
//    }
////
//
//    public void sendMessageIgnore(Player plIgnore, Message msg) {
//        for (Player pl : Client.gI().getPlayers()) {
//            if (pl != null && !pl.equals(plIgnore)) {
//                pl.sendMessage(msg);
//            }
//        }
//        msg.cleanup();
//    }
    public void sendMessageAllPlayer(Message msg) {
        for (Player pl : Client.gI().getPlayers()) {
            if (pl != null) {
                pl.sendMessage(msg);
            }
        }
    }

    public void sendMessageIgnore(Player plIgnore, Message msg) {
        for (Player pl : Client.gI().getPlayers()) {
            if (pl != null && !pl.equals(plIgnore)) {
                pl.sendMessage(msg);
            }
        }
    }

    public void sendInfoHp(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 5);
            msg.writeFix(Util.maxIntValue(player.nPoint.hp));
//            msg.writer().writeLong(player.nPoint.hp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendInfoMp(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 6);
            msg.writeFix(Util.maxIntValue(player.nPoint.mp));

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendInfoHpMp(Player player) {
        sendInfoHp(player);
        sendInfoMp(player);
    }

    public void subHPPlayer(Player player, int hp) {
        if (!player.isDie()) {
            player.nPoint.subMP(hp);
            Service.gI().Send_Info_NV(player);
            if (!player.isPet) {
                PlayerService.gI().sendInfoHpMp(player);
            }
        }
    }

    public void hoiPhuc(Player player, long hp, long mp) {
        if (!player.isDie()) {
            player.nPoint.addHp(hp);
            player.nPoint.addMp(mp);
            Service.gI().Send_Info_NV(player);
            if (!player.isPet && !player.isNewPet && !player.isMiniPet) {
                PlayerService.gI().sendInfoHpMp(player);
            }
        }
    }

    public void sendInfoHpMpMoney(Player player) {
//        if (!player.isPet || !player.isBoss || !player.isMiniPet) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 4);
            msg.writer().writeLong(player.inventory.gold);
            msg.writer().writeInt(player.inventory.gem);//luong
            msg.writeFix(Util.maxIntValue(player.nPoint.hp));//chp
            msg.writeFix(Util.maxIntValue(player.nPoint.mp));//cmp
            msg.writer().writeInt(player.inventory.ruby);//ruby
            player.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerService.class, e);
//            }
        }
    }

    public void playerMove(Player player, int x, int y) {

        if (player.zone == null) {
            return;
        }
        if (!player.isDie()) {
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            player.location.x = x;
            player.location.y = y;
            player.location.lastTimeplayerMove = System.currentTimeMillis();
            switch (player.zone.map.mapId) {
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                    if (!player.isBoss && !player.isPet && !player.isMiniPet) {
                        if (x < 24 || x > player.zone.map.mapWidth - 24 || y < 0 || y > player.zone.map.mapHeight - 24) {
                            if (MapService.gI().getWaypointPlayerIn(player) == null) {
                                ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
                                return;
                            }
                        }
                        int yTop = player.zone.map.yPhysicInTop(player.location.x, player.location.y);
                        if (yTop >= player.zone.map.mapHeight - 24) {
                            ChangeMapService.gI().changeMap(player, 21 + player.gender, 0, 200, 336);
                            return;
                        }
                    }
                    break;
            }
            if (player.pet != null) {
                player.pet.followMaster();
            }
            if (player.newpet != null) {
                player.newpet.followMaster();
            }
            if (player.minipet != null) {
                player.minipet.followMaster();
            }
            MapService.gI().sendPlayerMove(player);
            if (!player.isPl() || player.playerTask == null || player.playerTask.taskMain == null) {
                return;
            }
            switch (player.playerTask.taskMain.id) {
                case ConstTask.TASK_0_1:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_12_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_6_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_7_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_8_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_9_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_17_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_22_1:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_23_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_24_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                case ConstTask.TASK_28_0:
                    TaskService.gI().checkDoneTaskGoToMap(player, player.zone);
                    break;
                default:
                    break;
            }
        }

    }

    public void sendCurrentStamina(Player player) {
        Message msg;
        try {
            msg = new Message(-68);
            msg.writer().writeShort(player.nPoint.stamina);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerService.class, e);
        }
    }

    public void sendMaxStamina(Player player) {
        Message msg;
        try {
            msg = new Message(-69);
            msg.writer().writeShort(player.nPoint.maxStamina);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(PlayerService.class, e);
        }
    }

    public void changeAndSendTypePK(Player player, int type) {
        changeTypePK(player, type);
        sendTypePk(player);
    }

    public void changeTypePK(Player player, int type) {
        player.typePk = (byte) type;
    }

    public void sendTypePk(Player player) {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeByte(player.typePk);
            Service.gI().sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void banPlayer(Player playerBaned) {
        try {
            GirlkunDB.executeUpdate("update account set ban = 1 where id = ? and username = ?",
                    playerBaned.getSession().userId, playerBaned.getSession().uu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Service.gI().sendThongBao(playerBaned,
                "Tài khoản của bạn đã bị khóa\nGame sẽ mất kết nối sau 5 giây...");
        playerBaned.iDMark.setLastTimeBan(System.currentTimeMillis());
        playerBaned.iDMark.setIsBan(true);
    }

    public void ActivePlayer(Player playerActived) {
        try {
            GirlkunDB.executeUpdate("update account set active = 1 where id = ? and username = ?",
                    playerActived.getSession().userId, playerActived.getSession().uu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Service.gI().sendThongBao(playerActived,
                "Tài khoản của bạn đã trở thành chính thức tại server EMTI");
        playerActived.iDMark.setIsActive(true);
    }
    private static final int COST_GOLD_HOI_SINH = 100000;
    private static final int COST_GEM_HOI_SINH = 5;
    private static final int COST_GOLD_HOI_SINH_NRSD = 500000;

    public void hoiSinh(Player player) {
       // if (player.isDie()) {

            boolean canHs = false;
            if (MapService.gI().isMapBlackBallWar(player.zone.map.mapId) || MapService.gI().isMapMaBu(player.zone.map.mapId) //                    || MapService.gI().isMapMabuWar(player.zone.map.mapId)
                    ) {
                if (player.inventory.gold >= COST_GOLD_HOI_SINH) {
                    player.inventory.gold -= COST_GOLD_HOI_SINH;
                    canHs = true;
                } else {
                    Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH
                            - player.inventory.gold) + " vàng");
                    return;
                }
            }
            if (!canHs) {
                if (player.inventory.gem > COST_GEM_HOI_SINH) {
                    player.inventory.gem -= COST_GEM_HOI_SINH;
                    canHs = true;

                } else {
                    Service.gI().sendThongBao(player, "Bạn không đủ ngọc xanh để hồi sinh");
                }
            }
            if (canHs) {
                Service.gI().sendMoney(player);
                Service.gI().hsChar(player, Util.maxIntValue(player.nPoint.hpMax), Util.maxIntValue(player.nPoint.mpMax));
                if (player.zone.map.mapId == ConstTranhNgocNamek.MAP_ID) {
                    TranhNgocService.getInstance().sendUpdateLift(player);
                }
            }

       // }
    }

//    public void hoiSinhMaBu(Player player) {
//        if (player.isDie()) {
//            try {
//                boolean canHs = false;
//                if (MapService.gI().isMapMaBu(player.zone.map.mapId)) {
//                    if (player.inventory.gem >= COST_GOLD_HOI_SINH_NRSD) {
//                        player.inventory.gem -= COST_GOLD_HOI_SINH_NRSD;
//                        canHs = true;
//                    } else {
//                        Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH_NRSD
//                                - player.inventory.gem) + " ngọc");
//                        return;
//                    }
//                } else {
//                    if (player.inventory.gem >= COST_GOLD_HOI_SINH) {
//                        player.inventory.gem -= COST_GOLD_HOI_SINH;
//                        canHs = true;
//                    } else {
//                        Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện, còn thiếu " + Util.numberToMoney(COST_GOLD_HOI_SINH
//                                - player.inventory.gem) + " ngọc");
//                        return;
//                    }
//                }
//                if (!canHs) {
//                    if (player.inventory.gem > COST_GEM_HOI_SINH) {
//                        player.inventory.gem -= COST_GEM_HOI_SINH;
//                        canHs = true;
//
//                    } else {
//                        Service.gI().sendThongBao(player, "Bạn không đủ ngọc xanh để hồi sinh");
//                    }
//                }
//                if (canHs) {
//                    Service.gI().sendMoney(player);
////                    int intValue = (int) Math.max(Math.min(Util.maxIntValue( player.nPoint.mpMax), Integer.MAX_VALUE), Integer.MIN_VALUE);
////                    if (intValue < 0) {
////                        intValue = 0;
////                    }
//                    Service.gI().hsChar(player, Util.maxIntValue(player.nPoint.hpMax), Util.maxIntValue(player.nPoint.mpMax));
//
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
