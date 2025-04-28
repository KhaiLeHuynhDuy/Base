package nro.services.func;

import com.girlkun.database.GirlkunDB;
//import nro.database.GirlkunDB;
import nro.jdbc.daos.PlayerDAO;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.server.Client;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.TimeUtil;
import nro.utils.Util;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import nro.models.item.Item;

public class TransactionService implements Runnable {

    private static final int TIME_DELAY_TRADE = 30000;

    static final Map<Player, Trade> PLAYER_TRADE = new HashMap<>();

    private static final byte SEND_INVITE_TRADE = 0;
    private static final byte ACCEPT_TRADE = 1;
    private static final byte ADD_ITEM_TRADE = 2;
    private static final byte CANCEL_TRADE = 3;
    private static final byte LOCK_TRADE = 5;
    private static final byte ACCEPT = 7;

    private static TransactionService i;

    private TransactionService() {
    }

    public static TransactionService gI() {
        if (i == null) {
            i = new TransactionService();
            new Thread(i).start();
        }
        return i;
    }

    public void controller(Player pl, Message msg) {
//        if (true) {
//            Service.gI().sendThongBao(pl, "Chức năng tạm thời đóng");
//            return;
//        }
        if (!pl.getSession().actived) {
            Service.gI().sendThongBao(pl, "Bạn chưa phải là thành viên");
            return;
        }
        try {
            if (pl.getSession().actived) {
                byte action = msg.reader().readByte();
                int playerId = -1;
                Player plMap = null;
                Trade trade = PLAYER_TRADE.get(pl);
                switch (action) {
                    case SEND_INVITE_TRADE:
//                    case ACCEPT_TRADE:
//                        playerId = msg.reader().readInt();
//                        plMap = pl.zone.getPlayerInMap(playerId);
//                        if (plMap != null) {
//                            trade = PLAYER_TRADE.get(pl);
//                            if (trade == null) {
//                                trade = PLAYER_TRADE.get(plMap);
//                            }
//                            if (pl.getSession() == null || pl.getSession().actived == false) {
//                                Service.gI().sendThongBao(pl, "Chưa mở thành viên!!");
//                                return;
//                            }
//                            if (pl.getSession() == null || pl.playerTask.taskMain.id < 20) {
//                                Service.gI().sendThongBao(pl, "Hãy hoàn thành nhiệm vụ kuku để mở giao dịch!!");
//                                return;
//                            }
//                            if (trade == null) {
//                                if (action == SEND_INVITE_TRADE) {
//                                    if (Util.canDoWithTime(pl.iDMark.getLastTimeTrade(), TIME_DELAY_TRADE)
//                                            && Util.canDoWithTime(plMap.iDMark.getLastTimeTrade(), TIME_DELAY_TRADE)) {
//                                        boolean checkLogout1 = false;
//                                        boolean checkLogout2 = false;
//                                        try (Connection con = GirlkunDB.getConnection()) {
//                                            checkLogout1 = PlayerDAO.checkLogout(con, pl);
//                                            checkLogout2 = PlayerDAO.checkLogout(con, plMap);
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                        if (checkLogout1) {
//                                            Client.gI().kickSession(pl.getSession());
//                                            break;
//                                        }
//                                        if (checkLogout2) {
//                                            Client.gI().kickSession(plMap.getSession());
//                                            break;
//                                        }
//                                        pl.iDMark.setLastTimeTrade(System.currentTimeMillis());
//                                        pl.iDMark.setPlayerTradeId((int) plMap.id);
//                                        if (plMap.getSession() == null || plMap.getSession().actived == false) {
//                                            Service.gI().sendThongBao(pl, "Bạn kia chưa mở thành viên!!");
//                                            return;
//                                        }
//                                        if (plMap.getSession() == null || plMap.playerTask.taskMain.id < 20) {
//                                            Service.gI().sendThongBao(pl, "Bạn kia chưa hoàn thành nhiệm vụ kuku để mở giao dịch!!");
//                                            return;
//                                        }
//                                        sendInviteTrade(pl, plMap);
//                                    } else {
//                                        Service.gI().sendThongBao(pl, "Thử lại sau "
//                                                + TimeUtil.getTimeLeft(Math.max(pl.iDMark.getLastTimeTrade(),
//                                                        plMap.iDMark.getLastTimeTrade()), TIME_DELAY_TRADE / 1000));
//                                    }
//                                } else {
//                                    if (plMap.iDMark.getPlayerTradeId() == pl.id) {
//                                        trade = new Trade(pl, plMap);
//                                        trade.openTabTrade();
//                                    }
//                                }
//                            } else {
//                                Service.gI().sendThongBao(pl, "Không thể thực hiện");
//                            }
//                        }
//                        break;
                    case ACCEPT_TRADE:
                        playerId = msg.reader().readInt();
                        plMap = pl.zone.getPlayerInMap(playerId);
                        if (plMap != null) {
                            trade = PLAYER_TRADE.get(pl);
                            if (trade == null) {
                                trade = PLAYER_TRADE.get(plMap);
                            }
                            if (trade == null) {
                                if (action == SEND_INVITE_TRADE) {
                                    if (Util.canDoWithTime(pl.iDMark.getLastTimeTrade(), TIME_DELAY_TRADE)
                                            && Util.canDoWithTime(plMap.iDMark.getLastTimeTrade(), TIME_DELAY_TRADE)) {
                                        pl.iDMark.setPlayerTradeId((int) plMap.id);
                                        sendInviteTrade(pl, plMap);
                                    } else {
                                        Service.gI().sendThongBao(pl, "Không thể giao dịch ngay lúc này");
                                    }
                                } else {
                                    if (plMap.iDMark.getPlayerTradeId() == pl.id) {
                                        pl.iDMark.setLastTimeTrade(System.currentTimeMillis());
                                        plMap.iDMark.setLastTimeTrade(System.currentTimeMillis());
                                        trade = new Trade(pl, plMap);
                                        trade.openTabTrade();
                                    }
                                }
                            } else {
                                Service.gI().sendThongBao(pl, "Không thể thực hiện");
                            }
                        }
                        break;
//                     case ADD_ITEM_TRADE:
//                        if (trade != null) {
//                            byte index = msg.reader().readByte();
//                            int quantity = msg.reader().readInt();
//                            if( quantity <0){
//                                Service.gI().sendThongBao(pl, "Không thể thực hiện");
//                                trade.cancelTrade();
//                                break;
//                            }
//                            if (quantity == 0) {//do
//                                quantity = 1;
//                            }
//                            trade.addItemTrade(pl, index, quantity);
//                        }
//                        break;
                    case ADD_ITEM_TRADE:
                        if (trade != null) {
                            byte index = msg.reader().readByte();
                            int quantity = msg.reader().readInt();
                            // Kiểm tra index hợp lệ
                            if (index < 0 || index >= pl.inventory.itemsBag.size()) {
                                Service.gI().sendThongBao(pl, "Vật phẩm không tồn tại");
                                trade.cancelTrade();
                                break;
                            }
                            // Kiểm tra số lượng hợp lệ
                            Item item = pl.inventory.itemsBag.get(index);
                            if (quantity <= 0 || quantity > item.quantity) {
                                Service.gI().sendThongBao(pl, "Số lượng không hợp lệ");
                                trade.cancelTrade();
                                break;
                            }
                            trade.addItemTrade(pl, index, quantity);
                        }
                        break;
                    case CANCEL_TRADE:
                        if (trade != null) {
                            trade.cancelTrade();
                        }
                        break;
                    case LOCK_TRADE:
                        if (trade != null) {
                            trade.lockTran(pl);
                        }
                        break;
                    case ACCEPT:
                        if (trade != null) {
                            trade.acceptTrade();
                            if (trade.accept == 2) {
                                trade.dispose();
                            }
                        }
                        break;
                }
            } else {
                Service.gI().sendThongBao(pl, "Vui lòng kích hoạt để sử dụng tính năng này!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logException(this.getClass(), e);
        }
    }

    /**
     * Mời giao dịch
     */
    private void sendInviteTrade(Player plInvite, Player plReceive) {
        Message msg;
        try {
            msg = new Message(-86);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plInvite.id);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hủy giao dịch
     *
     * @param player
     */
    public void cancelTrade(Player player) {
        Trade trade = PLAYER_TRADE.get(player);
        if (trade != null) {
            trade.cancelTrade();
        }
    }

    @Override
    public void run() {
//        while (true) {
//            try {
//                long startTime = System.currentTimeMillis();
//
//                for (Trade trade : PLAYER_TRADE.values()) {
//                    trade.update();
//                }
//
//                long elapsedTime = System.currentTimeMillis() - startTime;
//                long sleepTime = Math.max(0, 300 - elapsedTime);
//                Thread.sleep(sleepTime);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                break; // Thoát khỏi vòng lặp nếu bị gián đoạn
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

}
