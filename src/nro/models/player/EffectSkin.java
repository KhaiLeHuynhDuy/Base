package nro.models.player;

import nro.models.item.Item;
import nro.models.mob.Mob;
import nro.services.EffectSkillService;
import nro.services.ItemService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.InventoryServiceNew;
import nro.services.ItemTimeService;
import nro.services.MapService;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EffectSkin {

    private static final String[] textOdo = new String[]{ //Cải trang Ở Dơ
        "Thối dữ bayyy", "Đi ra bạn ê",
        "Tránh xa ta ra !!!!"
    };
    private static final String[] textDraburaFrost = new String[]{ //Cải trang Drabura Frost
        "Ui lạnh quá..", "Đông cứng rồi", "Tránh xa ta ra", "Bất lực"
    };
    private static final String[] textDrabura = new String[]{ //Cải trang Drabura
        "Năng quá", "Chết tiệt", "Hóa đá rồi", "Tránh xa ta ra"
    };
    private static final String[] textThoDaiCa = new String[]{ //Cải trang ThoDaiCa
        "Cái củ cà rốt gì vậy?", "Tên thỏ chết tiệt", "Có tránh xa ta ra không?"
    };
    private static final String[] test = new String[]{
        "Người gì mà đẹp zai zậy", "Ui anh EmTi :3", "Sao anh đẹp zoai zị?"

    };
    private static final String[] textBlackFide = new String[]{
        "Thưa Ngài", "Fide Đại Đế", "Có phải đại ca EmTi không?"

    };
    private Player player;

    public EffectSkin(Player player) {
        this.player = player;
        this.xHPKI = 1;
    }
    private long lastTimeHoaBang;  //Cải trang Dracula Frost
    private long lastTimeHoaDa; //Cải trang Dracula 
    private long lastTimeHoaCaRot; //Cải trang Thỏ Đại Ca
    private long lastTimeSoHai;
    public long lastTimeAttack;
    private long lastTimeOdo;
    private long lastTimeTest;
    private long lastTimeXenHutHpKi;

    public long lastTimeAddTimeTrainArmor;
    public long lastTimeSubTimeTrainArmor;

    public boolean isVoHinh;

    public long lastTimeXHPKI;
    public int xHPKI;

    public long lastTimeUpdateCTHT;

    public boolean isMaPhongBa;
    public int timeMaPhongBa;
    public long lastTimeMaPhongBa;

    public long lastTimeSubHP;

    public void update() {
        if (this.player.zone != null && !MapService.gI().isMapOffline(this.player.zone.map.mapId)) {
            updateOdo();
            updateFideBlack();
            updateXenHutXungQuanh();
            updateDraburaFrost();
            updateDrabura();
            updateThoDaiCa();
            updateFideBlack();
            updateVoHinh();
            updateCTHaiTac();
        }
        if (!this.player.isBoss && !this.player.isPet && !player.isNewPet&& !player.isMiniPet) {
            updateTrainArmor();

        }
        if (xHPKI != 1 && Util.canDoWithTime(lastTimeXHPKI, 1800000)) {
            xHPKI = 1;
            Service.gI().point(player);
        }
        if (isMaPhongBa && (Util.canDoWithTime(lastTimeMaPhongBa, timeMaPhongBa))) {
            EffectSkillService.gI().removeMaPhongBa(this.player);
        }
        if (isMaPhongBa) {
            for (int i = 0; i < (player.effectSkill.timeCaiBinhChua / 1000) + 1; i++) {
                if (Util.canDoWithTime(lastTimeSubHP, 1000)) {
                    try {
                        if (!this.player.isDie()) {
                            if (player.nPoint.hp >= player.dameMaFuBa) {
                                PlayerService.gI().subHPPlayer(this.player, (int) this.player.dameMaFuBa);
                            } else {
                                player.nPoint.hp = 0;
                                Service.gI().charDie(player);
                                EffectSkillService.gI().removeMaPhongBa(player);
                                ItemTimeService.gI().removeItemTime(player, 13915);
                            }
                            PlayerService.gI().sendInfoHpMpMoney(this.player);
                            Service.gI().Send_Info_NV(this.player);
                        }
                        lastTimeSubHP = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void updateDraburaFrost() {
        try {
            if (this.player.zone != null && this.player.nPoint.isDraburaFrost) {
                if (Util.canDoWithTime(lastTimeHoaBang, 50000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet&& !player.isMiniPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().SetHoaBang(pl, System.currentTimeMillis(), 50000);
                        EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 5000);
                        EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, (byte) EffectSkillService.BLIND_EFFECT);
                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 13869, 5000 / 1000);
                        Service.gI().chat(player, "Chíu chíu");
                        Service.gI().chat(pl, textDraburaFrost[Util.nextInt(0, textDraburaFrost.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaBang = System.currentTimeMillis();
                }
            } else {
                // Handle the case when this.player.zone is null or this.player.nPoint.isDraburaFrost is false.
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("An error occurred in updateDraburaFrost(): " + e.getMessage());
        }
    }

    private void updateDrabura() { // Dracula Hóa Đá
        try {
            if (this.player == null || this.player.zone == null) {
                return;
            }
            if (this.player.zone != null && this.player.nPoint.isDrabura == true) {
                if (Util.canDoWithTime(lastTimeHoaDa, 50000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet&& !player.isMiniPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().SetHoaDa(pl, System.currentTimeMillis(), 50000);
                        EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 5000);
                        EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);
                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 4392, 5000 / 1000);
                        Service.gI().chat(player, "Chíu chíu");
                        Service.gI().chat(pl, textDrabura[Util.nextInt(0, textDrabura.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaDa = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("");
        }
    }

    private void updateThoDaiCa() { //Thỏ Đại Ca
        try {
            if (this.player.zone != null && this.player.nPoint.isThoDaiCa == true) {
                if (Util.canDoWithTime(lastTimeHoaCaRot, 30000)) {
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isPet && !pl.isNewPet&& !player.isMiniPet && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 300) {
                            players.add(pl);
                        }
                    }
                    for (Player pl : players) {
                        EffectSkillService.gI().SetHoaCarot(pl, System.currentTimeMillis(), 30000);
                        EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), 30000);
                        //EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.BLIND_EFFECT);

                        Service.gI().Send_Caitrang(pl);
                        ItemTimeService.gI().sendItemTime(pl, 4075, 30000 / 1000);
                        Service.gI().chat(player, "Ta sẽ biến các người thành carot");
                        Service.gI().chat(pl, textThoDaiCa[Util.nextInt(0, textThoDaiCa.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                    }
                    this.lastTimeHoaCaRot = System.currentTimeMillis();
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("");
        }
    }

    private void updateCTHaiTac() {

        if (this.player != null
                && this.player.setClothes.ctHaiTac != -1
                && this.player.zone != null
                && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
            int count = 0;
            int[] cts = new int[9];
            cts[this.player.setClothes.ctHaiTac - 618] = this.player.setClothes.ctHaiTac;
            List<Player> players = new ArrayList<>();
            players.add(player);
            try {
                for (Player pl : player.zone.getNotBosses()) {
                    if (!player.equals(pl) && pl.setClothes != null && pl.setClothes.ctHaiTac != -1 && Util.getDistance(player, pl) <= 300) {
                        cts[pl.setClothes.ctHaiTac - 618] = pl.setClothes.ctHaiTac;
                        players.add(pl);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < cts.length; i++) {
                if (cts[i] != 0) {
                    count++;
                }
            }
            for (Player pl : players) {
                Item ct = pl.inventory.itemsBody.get(5);
                if (ct.isNotNullItem() && ct.template.id >= 618 && ct.template.id <= 626) {
                    for (Item.ItemOption io : ct.itemOptions) {
                        if (io.optionTemplate.id == 147
                                || io.optionTemplate.id == 77
                                || io.optionTemplate.id == 103) {
                            io.param = count * 3;
                        }
                    }
                }
                if (!pl.isPet && !pl.isNewPet&& !pl.isMiniPet && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
                    InventoryServiceNew.gI().sendItemBody(pl);
                }
                pl.effectSkin.lastTimeUpdateCTHT = System.currentTimeMillis();
            }
        }
    }
//   private void updateCTHaiTac() {
//        if (this.player != null
//                && this.player.setClothes.ctHaiTac != -1
//                && this.player.zone != null
//                && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
//
//            List<Integer> validIds = Arrays.asList(618, 619, 620, 621, 622, 623, 624, 626, 627, 1153, 1154, 1155, 1209, 1210);
//            Map<Integer, Integer> cts = new HashMap<>();
//            List<Player> playersInRange = new ArrayList<>();
//            playersInRange.add(this.player);
//
//            Integer ctHaiTacValue = (int) this.player.setClothes.ctHaiTac;
//            if (validIds.contains(ctHaiTacValue)) {
//                cts.put(ctHaiTacValue, cts.getOrDefault(ctHaiTacValue, 0) + 1);
//            }
//
//            try {
//                for (Player pl : this.player.zone.getNotBosses()) {
//                    if (!this.player.equals(pl)
//                            && pl.setClothes != null
//                            && pl.setClothes.ctHaiTac != -1) {
//
//                        Integer plCtHaiTacValue = (int) pl.setClothes.ctHaiTac;
//                        if (validIds.contains(plCtHaiTacValue) && Util.getDistance(this.player, pl) <= 300) {
//                            cts.put(plCtHaiTacValue, cts.getOrDefault(plCtHaiTacValue, 0) + 1);
//                            playersInRange.add(pl);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            int count = cts.size();
//
//            for (Player pl : playersInRange) {
//                Item ct = pl.inventory.itemsBody.get(5);
//                if (ct.isNotNullItem()) {
//                    Integer ctTemplateId = (int) ct.template.id;
//                    if (validIds.contains(ctTemplateId)) {
//                        for (Item.ItemOption io : ct.itemOptions) {
//                            if (io.optionTemplate.id == 147
//                                    || io.optionTemplate.id == 77
//                                    || io.optionTemplate.id == 103) {
//                                io.param = (int) (count * 0.1);
//                            }
//                        }
//
//                        if (!pl.isPet && !pl.isNewPet && !pl.isMiniPet && !pl.isDie() && Util.canDoWithTime(lastTimeUpdateCTHT, 5000)) {
//                            InventoryServiceNew.gI().sendItemBody(pl);
//                        }
//
//                        pl.effectSkin.lastTimeUpdateCTHT = System.currentTimeMillis();
//                    }
//                }
//            }
//        }
//    }

    private void updateXenHutXungQuanh() {
        try {
            int param = this.player.nPoint.tlHutHpMpXQ;
            if (param > 0) {
                if (!this.player.isDie() && Util.canDoWithTime(lastTimeXenHutHpKi, 5000)) {
                    int hpHut = 0;
                    int mpHut = 0;
                    List<Player> players = new ArrayList<>();
                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Mob mob : this.player.zone.mobs) {
                        if (mob.point.gethp() > 1) {
                            if (Util.getDistance(this.player, mob) <= 200) {
                                long subHp = mob.point.getHpFull() * param / 100;
                                if (subHp >= mob.point.gethp()) {
                                    subHp = mob.point.gethp() - 1;
                                }
                                hpHut += subHp;
                                mob.injured(null, subHp, false);
                            }
                        }
                    }
                    for (Player pl : players) {
                        long subHp = Util.TamkjllGH(pl.nPoint.hpMax * param / 100);
                        long subMp = Util.TamkjllGH(pl.nPoint.mpMax * param / 100);
                        if (subHp >= pl.nPoint.hp) {
                            subHp = Util.TamkjllGH(pl.nPoint.hp - 1);
                        }
                        if (subMp >= pl.nPoint.mp) {
                            subMp = Util.TamkjllGH(pl.nPoint.mp - 1);
                        }
                        hpHut += subHp;
                        mpHut += subMp;
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.player.nPoint.addHp(hpHut);
                    this.player.nPoint.addMp(mpHut);
                    PlayerService.gI().sendInfoHpMpMoney(this.player);
                    Service.gI().Send_Info_NV(this.player);
                    this.lastTimeXenHutHpKi = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("");
        }
    }

    private void updateOdo() {
        try {
            int param = this.player.nPoint.tlHpGiamODo;
            if (param > 0) {
                if (Util.canDoWithTime(lastTimeOdo, 10000)) {
                    List<Player> players = new ArrayList<>();

                    List<Player> playersMap = this.player.zone.getNotBosses();
                    for (Player pl : playersMap) {
                        if (!this.player.equals(pl) && !pl.isBoss && !pl.isNewPet && !pl.isMiniPet && !pl.isDie()
                                && Util.getDistance(this.player, pl) <= 200) {
                            players.add(pl);
                        }

                    }
                    for (Player pl : players) {
                        long subHp = Util.TamkjllGH(pl.nPoint.hpMax * param / 100);
                        if (subHp >= pl.nPoint.hp) {
                            subHp = Util.TamkjllGH(pl.nPoint.hp - 1);
                        }
                        Service.gI().chat(pl, textOdo[Util.nextInt(0, textOdo.length - 1)]);
                        PlayerService.gI().sendInfoHpMpMoney(pl);
                        Service.gI().Send_Info_NV(pl);
                        pl.injured(null, subHp, true, false);
                    }
                    this.lastTimeOdo = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("");
        }
    }

    private void updateFideBlack() {
        try {
            if (this.player.zone != null && this.player.nPoint.isSoHai && this.player.nPoint.tlHpFideBlack > 0
                    && Util.canDoWithTime(lastTimeSoHai, 20000)) {
                List<Player> players = this.player.zone.getNotBosses().stream()
                        .filter(pl -> !this.player.equals(pl) && !pl.isPet && !pl.isNewPet && !pl.isMiniPet
                        && !pl.isBoss && !pl.isDie() && Util.getDistance(this.player, pl) <= 200)
                        .collect(Collectors.toList());

                for (Player pl : players) {
                    int param = this.player.nPoint.tlHpFideBlack;

                    // Giảm 30% hp, mp và dame khi sợ hãi.
                    long subHp = Util.TamkjllGH(pl.nPoint.hpMax * param / 100) * 70 / 100;
                    long subMp = Util.TamkjllGH(pl.nPoint.mpMax * param / 100) * 70 / 100;
                    long dame = Util.TamkjllGH(pl.nPoint.dameg * param / 100) * 70 / 100;

                    if (subHp >= pl.nPoint.hp && subMp >= pl.nPoint.mp && dame >= pl.nPoint.dame) {
                        subHp = Util.TamkjllGH(pl.nPoint.hp - 1);
                        subMp = Util.TamkjllGH(pl.nPoint.mp - 1);
                        dame = Util.TamkjllGH(pl.nPoint.dame - 1);
                    }

                    // Áp dụng hiệu ứng sợ hãi trong 10 giây.
                    EffectSkillService.gI().setSoHai(pl, System.currentTimeMillis(), 10000);

                    // Giảm thông số hp, mp và dame của người chơi khi bị sợ hãi.
                    pl.injured(null, subHp, true, false);
                    pl.injured(null, subMp, true, false);
                    pl.injured(null, dame, true, false);

                    // Gửi thông tin cho người chơi về các thông số hp, mp và tiền.
                    PlayerService.gI().sendInfoHpMpMoney(pl);
                    Service.gI().Send_Info_NV(pl);

                    // Hiển thị thông báo và gửi hiệu ứng cho người chơi.
                    Service.gI().chat(player, "Lãnh đòn đi !!!! ");
                    Service.gI().chat(pl, textBlackFide[Util.nextInt(0, textBlackFide.length - 1)]);
                    EffectSkillService.gI().sendEffectPlayer(player, pl, EffectSkillService.TURN_ON_EFFECT, (byte) EffectSkillService.THO_EFFECT);
                    Service.gI().Send_Caitrang(pl);
                    ItemTimeService.gI().sendItemTime(pl, 8139, 20000 / 1000);
                }

                this.lastTimeSoHai = System.currentTimeMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("");
        }
    }

    //giáp tập luyện
    private void updateTrainArmor() {
        if (Util.canDoWithTime(lastTimeAddTimeTrainArmor, 60000) && !Util.canDoWithTime(lastTimeAttack, 30000)) {
            if (this.player.nPoint.wearingTrainArmor) {
                for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                    if (io.optionTemplate.id == 9) {
                        if (io.param < 1000) {
                            io.param++;
                            InventoryServiceNew.gI().sendItemBody(player);
                        }
                        break;
                    }
                }
            }
            this.lastTimeAddTimeTrainArmor = System.currentTimeMillis();
        }
        if (Util.canDoWithTime(lastTimeSubTimeTrainArmor, 60000)) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            for (Item item : this.player.inventory.itemsBox) {
                if (item.isNotNullItem()) {
                    if (ItemService.gI().isTrainArmor(item)) {
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 9) {
                                if (io.param > 0) {
                                    io.param--;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            this.lastTimeSubTimeTrainArmor = System.currentTimeMillis();
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().point(this.player);
        }
    }

    private void updateVoHinh() {
        if (this.player.nPoint.wearingVoHinh) {
            if (Util.canDoWithTime(lastTimeAttack, 5000)) {
                isVoHinh = true;
            } else {
                isVoHinh = false;
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
