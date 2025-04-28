//package nro.models.pvp_old;
//
//import nro.consts.ConstPlayer;
//import nro.models.player.Player;
//import nro.services.PlayerService;
//import nro.services.Service;
//
///**
// *
// * @Edit By EMTI💖
// *
// */
//public class ChallengePVP extends PVP {
//
//    public ChallengePVP(Player pl1, Player pl2) {
//        this.player1 = pl1;
//        this.player2 = pl2;
//        this.typePVP = TYPE_PVP_CHALLENGE;
//    }
//
//    public int gold;
//    public int totalGold;
//
//    @Override
//    public void start() {
//        super.start();
//        this.totalGold += ((long)gold * 90 / 100) * 2;
//        this.player1.inventory.gold -= gold;
//        this.player2.inventory.gold -= gold;
//        Service.gI().sendMoney(player1);
//        Service.gI().sendMoney(player2);
//        PlayerService.gI().changeAndSendTypePK(this.player1, ConstPlayer.PK_PVP);
//        PlayerService.gI().changeAndSendTypePK(this.player2, ConstPlayer.PK_PVP);
//    }
//
//    @Override
//    public void sendResultMatch(Player winer, Player loser, byte typeWin) {
//        switch (typeWin) {
//            case PVP.TYPE_DIE:
//                Service.gI().sendThongBao(winer, "Đối thử đã kiệt sức, bạn thắng được " + this.totalGold + " vàng");
//                Service.gI().sendThongBao(loser, "Bạn đã thua vì đã kiệt sức");
//                break;
//            case PVP.TYPE_LEAVE_MAP:
//                Service.gI().sendThongBao(winer, "Đối thủ sợ quá bỏ chạy, bạn thắng được " + this.totalGold + " vàng");
//                Service.gI().sendThongBao(loser, "Đạn bị xử thua vì đã bỏ chạy");
//                break;
//        }
//    }
//
//    @Override
//    public void reward(Player plWin) {
//        plWin.inventory.addGold(this.totalGold);
//        Service.gI().sendMoney(plWin);
//    }
//
//}
