package nro.models.player;

import nro.jdbc.daos.GodGK;
import nro.models.item.Item;
import nro.models.map.blackball.BlackBallWar;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.util.Date;
import java.util.Random;
import nro.models.map.giaidauvutru.Giaidauvutru;

public class RewardGiaidau {

    private static final int TIME_REWARD = 79200000;

    public static final int R1S_1 = 20;
    public static final int R1S_2 = 15;
    public static final int R2S_1 = 15;
    public static final int R2S_2 = 20;
    public static final int R3S_1 = 20;
    public static final int R3S_2 = 10;
    public static final int R4S_1 = 10;
    public static final int R4S_2 = 20;
    public static final int R5S_1 = 20;
    public static final int R5S_2 = 20;
    public static final int R5S_3 = 20;
    public static final int R6S_1 = 50;
    public static final int R6S_2 = 20;
    public static final int R7S_1 = 10;
    public static final int R7S_2 = 15;

    public static final int TIME_WAIT = 3600000;
    public static long time8h;
    private Player player;

    public long[] timeOutOfDateReward;
    public int[] quantilyGiaidau;
    public long[] lastTimeGetReward;

    public RewardGiaidau(Player player) {
        this.player = player;
        this.timeOutOfDateReward = new long[7];
        this.lastTimeGetReward = new long[7];
        this.quantilyGiaidau = new int[7];
        time8h = Giaidauvutru.TIME_OPEN;
    }

    public void reward(byte star) {
        if (this.timeOutOfDateReward[star - 1] > time8h) {
            quantilyGiaidau[star - 1]++;
        }
        this.timeOutOfDateReward[star - 1] = System.currentTimeMillis() + TIME_REWARD;
        Service.gI().point(player);
    }

    public void getRewardSelect(byte select) {
        int index = 0;
        for (int i = 0; i < timeOutOfDateReward.length; i++) {
            if (timeOutOfDateReward[i] > System.currentTimeMillis()) {
                index++;
                if (index == select + 1) {
                    getReward(i + 1);
                    break;
                }
            }
        }
    }

    private void getReward(int star) {
        if (timeOutOfDateReward[star - 1] > System.currentTimeMillis()
                && Util.canDoWithTime(lastTimeGetReward[star - 1], TIME_WAIT)) {
            switch (star) {
                case 1:
                case 2:
                case 3:
                case 4:
                    Service.gI().sendThongBao(player, "Phần thưởng chỉ số tự động nhận");
                    return;
                case 5:
                    quaNgoc5sao(player);
                    lastTimeGetReward[star - 1] = System.currentTimeMillis();
                    return;
                case 6:
                    quaNgoc6sao(player);
                    lastTimeGetReward[star - 1] = System.currentTimeMillis();
                    return;
                case 7:
                    quaNgoc7sao(player);
                    lastTimeGetReward[star - 1] = System.currentTimeMillis();
                    return;

            }
        } else {
            Service.gI().sendThongBao(player, "Chưa thể nhận phần quà ngay lúc này, vui lòng đợi "
                    + TimeUtil.diffDate(new Date(lastTimeGetReward[star - 1]), new Date(lastTimeGetReward[star - 1] + TIME_WAIT),
                            TimeUtil.MINUTE) + " phút nữa");
        }
    }

    private void quaNgoc7sao(Player player) {
        short[] getallda = {220, 221, 222, 223, 224};
        for (short daquy : getallda) {
            Item qua7sao = ItemService.gI().createNewItem((short) daquy);
            qua7sao.itemOptions.add(new Item.ItemOption(30, 1));
            qua7sao.itemOptions.add(new Item.ItemOption(93, 5));
            qua7sao.quantity = 1;
            player.inventory.itemsMailBox.add(qua7sao);

            GodGK.updateMailBox(player);
            Logger.error("Đã nhận quà 7 sao  " + player.name);
            Service.gI().sendThongBao(player, "Bạn đã nhận được " + qua7sao.template.name + " từ Ngọc Rồng Sao Đen");

        }
    }

    private void quaNgoc6sao(Player player) {
        int[] dotl = new int[]{2162, 2163, 2164, 2165};

        int ramdom = new Random().nextInt(dotl.length);

        Item qua6sao = ItemService.gI().otptl((short) dotl[ramdom]);
        qua6sao.itemOptions.add(new Item.ItemOption(30, 1));
        qua6sao.itemOptions.add(new Item.ItemOption(93, 5));
        qua6sao.quantity = 1;
        player.inventory.itemsMailBox.add(qua6sao);

        GodGK.updateMailBox(player);
        Logger.error("Đã nhận quà 7 sao  " + player.name);
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + qua6sao.template.name + " từ Ngọc Rồng Sao Đen");

    }

    private void quaNgoc5sao(Player player) {
        Item qua5sao = ItemService.gI().createNewItem((short) 457);
        qua5sao.itemOptions.add(new Item.ItemOption(30, 1));
        qua5sao.itemOptions.add(new Item.ItemOption(93, 10));
        qua5sao.quantity = 3;
        player.inventory.itemsMailBox.add(qua5sao);

        GodGK.updateMailBox(player);
        Logger.error("Đã nhận quà 7 sao  " + player.name);
        Service.gI().sendThongBao(player, "Bạn đã nhận được " + qua5sao.template.name + " từ Ngọc Rồng Sao Đen");
    }

    public void dispose() {
        this.player = null;
    }
}
