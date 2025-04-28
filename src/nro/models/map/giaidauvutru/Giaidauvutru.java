package nro.models.map.giaidauvutru;

import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Logger;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.Date;
import java.util.List;

public class Giaidauvutru {

    private static final int TIME_CAN_PICK_BLACK_BALL_AFTER_DROP = 5;

    public static final byte X3 = 3;
    public static final byte X5 = 5;
    public static final byte X7 = 7;

    public static final int COST_X3 = 500000;
    public static final int COST_X5 = 1000000;
    public static final int COST_X7 = 2000000;

    public static final byte HOUR_OPEN = 21;
    public static final byte MIN_OPEN = 0;
    public static final byte SECOND_OPEN = 0;

    public static final byte HOUR_CAN_PICK_DB = 21;
    public static final byte MIN_CAN_PICK_DB = 30;
    public static final byte SECOND_CAN_PICK_DB = 0;

    public static final byte HOUR_CLOSE = 22;
    public static final byte MIN_CLOSE = 00;
    public static final byte SECOND_CLOSE = 0;

    public static final int AVAILABLE = 7;
    private static final int TIME_WIN = 300000; //cam toi thieu 300k mili = 5p

    private static Giaidauvutru i;

    public static long TIME_OPEN;
    private static long TIME_CAN_PICK_DB;
    public static long TIME_CLOSE;

    private int day = -1;

    public static Giaidauvutru gI() {
        if (i == null) {
            i = new Giaidauvutru();
        }
        i.setTime();
        return i;
    }

    public void setTime() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                Giaidauvutru.TIME_OPEN = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN + ":" + MIN_OPEN + ":" + SECOND_OPEN, "dd/MM/yyyy HH:mm:ss");
                Giaidauvutru.TIME_CAN_PICK_DB = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CAN_PICK_DB + ":" + MIN_CAN_PICK_DB + ":" + SECOND_CAN_PICK_DB, "dd/MM/yyyy HH:mm:ss");
                Giaidauvutru.TIME_CLOSE = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE + ":" + MIN_CLOSE + ":" + SECOND_CLOSE, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception e) {
                        e.printStackTrace();
            }
        }
    }

    public synchronized void dropGiaidau(Player player) {
        if (player.iDMark.isHoldGiaidauBall()) {
            player.iDMark.setHoldGiaidauBall(false);
            ItemMap itemMap = new ItemMap(player.zone,
                    player.iDMark.getTempIdGiaidauBallHold(), 1, player.location.x,
                    player.zone.map.yPhysicInTop(player.location.x, player.location.y - 24),
                    -1);
            Service.gI().dropItemMap(itemMap.zone, itemMap);
            player.iDMark.setTempIdGiaidauBallHold(-1);
            player.zone.lastTimeDropGiaidauBall = System.currentTimeMillis();
            Service.gI().sendFlagBag(player); //gui vao tui do

            if (player.clan != null) {
                List<Player> players = player.zone.getPlayers();
                for (Player pl : players) {
                    if (pl.clan != null && player.clan.equals(pl.clan)) {
                        Service.gI().changeFlag(pl, Util.nextInt(1, 7));
                    }
                }
            } else {
                Service.gI().changeFlag(player, Util.nextInt(1, 7));
            }
        }
    }

    public void update(Player player) {
        if (player.zone == null || !MapService.gI().isMapGiaidauvutru(player.zone.map.mapId)) {
            return;
        }
        if (player.iDMark.isHoldGiaidauBall()) {
            if (Util.canDoWithTime(player.iDMark.getLastTimeHoldGiaidauBall(), TIME_WIN)) {
                win(player);
                return;
            } else {
                if (Util.canDoWithTime(player.iDMark.getLastTimeNotifyTimeHoldGiaidauBall(), 10000)) {
                    Service.gI().sendThongBao(player, "Cố gắng giữ ngọc rồng trong "
                            + TimeUtil.getSecondLeft(player.iDMark.getLastTimeHoldGiaidauBall(), TIME_WIN / 1000)
                            + " giây nữa, đem chiến thắng về cho bang hội!");
                    player.iDMark.setLastTimeNotifyTimeHoldGiaidauBall(System.currentTimeMillis());
                }
            }
        }
        try {
            long now = System.currentTimeMillis();
            if (!(now > TIME_OPEN && now < TIME_CLOSE)) {
                if (player.iDMark.isHoldGiaidauBall()) {
                    win(player);
                } else {
                    kickOutOfMap(player);
                }
            }
        } catch (Exception ex) {
                    ex.printStackTrace();
        }
    }

    private void win(Player player) {
        player.zone.finishGiaidauBallWar = true;
        int star = player.iDMark.getTempIdGiaidauBallHold() - 2219;
        if (player.clan != null) {
            try {
                List<Player> players = player.clan.membersInGame;
                for (Player pl : players) {
                    if (pl != null) {
                        pl.rewardGiaidau.reward((byte) star);
                        Service.gI().sendThongBao(pl, "Chúc mừng bang hội của bạn đã "
                                + "dành chiến thắng giải đấu vũ trụ " + star + " sao");
                    }
                }

            } catch (Exception e) {
                        e.printStackTrace();
                Logger.logException(Giaidauvutru.class, e,
                        "Lỗi ban thưởng giải đấu vũ trụ "
                        + star + " sao cho clan " + player.clan.id);
            }
        } else {
            player.rewardGiaidau.reward((byte) star);
            Service.gI().sendThongBao(player, "Chúc mừng bang hội của bạn đã "
                    + "dành chiến thắng giải đấu vũ trụ " + star + " sao");
        }

        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            kickOutOfMap(pl);
        }
    }

    private void kickOutOfMap(Player player) {
        if (player.cFlag == 8) {
            Service.gI().changeFlag(player, Util.nextInt(1, 7));
        }
        Service.gI().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
    }

    public void changeMap(Player player, byte index) {
        try {
            long now = System.currentTimeMillis();
            if (now > TIME_OPEN && now < TIME_CLOSE) {
                ChangeMapService.gI().changeMap(player,
                        player.mapGiaidauBall.get(index).map.mapId, -1, 50, 50);
            } else {
                Service.gI().sendThongBao(player, "Đại chiến giải đấu vũ trụ chưa mở");
                Service.gI().hideWaitDialog(player);
            }
        } catch (Exception ex) {
                    ex.printStackTrace();
        }
    }

    public void joinMapGiaidauWar(Player player) {
        boolean changed = false;
        if (player.clan != null) {
            List<Player> players = player.zone.getPlayers();
            for (Player pl : players) {
                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
                    Service.gI().changeFlag(player, pl.cFlag);
                    changed = true;
                    break;
                }
            }
        }
        if (!changed && !player.isBoss) {
            Service.gI().changeFlag(player, Util.nextInt(1, 7));
        }
    }

    public boolean pickGiaidauBall(Player player, Item item) {
        try {
            if (System.currentTimeMillis() < Giaidauvutru.TIME_CAN_PICK_DB) {
                Service.gI().sendThongBao(player, "Chưa thể nhặt ngọc rồng ngay lúc này, vui lòng đợi "
                        + TimeUtil.diffDate(new Date(Giaidauvutru.TIME_CAN_PICK_DB),
                                new Date(System.currentTimeMillis()), TimeUtil.SECOND) + " giây nữa");
                return false;
            } else if (player.zone.finishGiaidauBallWar) {
                Service.gI().sendThongBao(player, "Đại chiến giải đấu vũ trụ "
                        + "đã kết thúc, vui lòng đợi đến ngày mai");
                return false;
            } else {
                if (Util.canDoWithTime(player.zone.lastTimeDropGiaidauBall, TIME_CAN_PICK_BLACK_BALL_AFTER_DROP)) {

                    player.iDMark.setHoldGiaidauBall(true);
                    player.iDMark.setTempIdGiaidauBallHold(item.template.id);
                    player.iDMark.setLastTimeHoldGiaidauBall(System.currentTimeMillis());
                    Service.gI().sendFlagBag(player);
                    if (player.clan != null) {
                        List<Player> players = player.zone.getPlayers();
                        for (Player pl : players) {
                            if (pl.clan != null && player.clan.equals(pl.clan)) {
                                Service.gI().changeFlag(pl, 8);
                            }
                        }
                    } else {
                        Service.gI().changeFlag(player, 8);
                    }
                    return true;
                } else {
                    Service.gI().sendThongBao(player, "Không thể nhặt ngọc rồng ngay lúc này");
                    return false;
                }
            }
        } catch (Exception ex) {
                    ex.printStackTrace();
            return false;
        }
    }

    public void xHPKI(Player player, byte x) {
        int cost = 0;
        switch (x) {
            case X3:
                cost = COST_X3;
                break;
            case X5:
                cost = COST_X5;
                break;
            case X7:
                cost = COST_X7;
                break;
        }
        if (player.inventory.ruby >= cost) {
            player.inventory.ruby -= cost;
            Service.gI().sendMoney(player);
            player.effectSkin.lastTimeXHPKI = System.currentTimeMillis();
            player.effectSkin.xHPKI = x;
            player.nPoint.calPoint();
            player.nPoint.setHp((long) player.nPoint.hp * x);
            player.nPoint.setMp((long) player.nPoint.mp * x);
            PlayerService.gI().sendInfoHpMp(player);
            Service.gI().point(player);
        } else {
            Service.gI().sendThongBao(player, "Không đủ ngọc hồng để thực hiện, còn thiếu "
                    + Util.numberToMoney(cost - player.inventory.ruby) + " hồng ngọc");
        }
    }
}
