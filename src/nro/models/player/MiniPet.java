package nro.models.player;

import java.time.chrono.ThaiBuddhistEra;
import nro.models.mob.Mob;
import nro.services.ItemService;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;
import nro.services.PlayerService;

/**
 * @author outcast c-cute hột me 😳
 */
public class MiniPet extends Player {

    public static final byte DITHEO = 0;
    public static final byte BAOVE = 1;
    public static final byte TANCONG = 2;
    public static final byte VENHA = 3;
    public static final byte HOPTHE = 4;
    public int PETID = -1;
    public Player suphu;
    public byte trangthai = 0;
    private boolean DIVENHA;

    public static void callMiniPet(Player pl, int iditem) {
        if (pl.minipet == null) {
            init(pl);
            pl.minipet.setByPetId(getMiniPetById(iditem));
            pl.minipet.changeStatus(DITHEO);
        } else {
            pl.minipet.setByPetId(getMiniPetById(iditem));
            pl.minipet.changeStatus(DITHEO);
        }
    }

    public byte getStatus() {
        return this.trangthai;
    }

    public void setByPetId(int id) {
        this.PETID = id;
    }

    public MiniPet(Player suphu) {
        this.suphu = suphu;
        this.isMiniPet = true;
    }

    public void changeStatus(byte trangthai) {
        if (trangthai == VENHA) {
            goHome();
        }
        this.trangthai = trangthai;
    }

    public void goHome() {
        if (this.trangthai == VENHA) {
            return;
        }
        DIVENHA = true;
        new Thread(() -> {
//            try {
//                MiniPet.this.trangthai = MiniPet.TANCONG;
//                //Thread.sleep(2000);
//            } catch (Exception e) {
//            }
            ChangeMapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, suphu.gender + 21, -1));
            this.zone.load_Me_To_Another(this);
            MiniPet.this.trangthai = MiniPet.VENHA;
            DIVENHA = false;
        }).start();
    }

    public void reCall() {
        if (this.suphu == null || this.suphu.zone == null) {
            return;
        }
        ChangeMapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, suphu.gender + 21, suphu.zone.zoneId));
        this.zone.load_Me_To_Another(this);
    }

    public void joinMapMaster() {
        if (this.zone != null && trangthai != VENHA && !isDie()) {
            this.location.x = suphu.location.x + Util.nextInt(-20, 20);
            this.location.y = suphu.location.y;
            ChangeMapService.gI().goToMap(this, suphu.zone);
            this.zone.load_Me_To_Another(this);
        }
    }

    public long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    private void moveIdle() {
        if (trangthai == VENHA) {
            return;
        }
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - suphu.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, suphu.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), suphu.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    private long lastTimeMoveAtHome;
    private byte directAtHome = -1;

    @Override
    public void update() {
        try {
            super.update();
            if (this.zone == null || this.zone != suphu.zone) {
                joinMapMaster();
            }
            moveIdle();
            //System.out.println("Gọi pet");
            switch (trangthai) {
                case DITHEO:
//                    followMaster(60);
                    break;
                case VENHA:
                    if (this.zone != null && (this.zone.map.mapId == 21 || this.zone.map.mapId == 22 || this.zone.map.mapId == 23)) {
                        if (System.currentTimeMillis() - lastTimeMoveAtHome <= 5000) {
                            return;
                        } else {
                            if (this.zone.map.mapId == 21) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 500, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 452, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 23) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            }
                            Service.gI().chatJustForMe(suphu, this, "H2O + C12H22O11 -> Uống ngọt lắm sư phụ ạ!");
                            lastTimeMoveAtHome = System.currentTimeMillis();
                        }
                    }
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void followMaster() {
        switch (this.trangthai) {
            case DITHEO:
                followMaster(80);
                break;
        }
    }

    private void followMaster(int dis) {
        int mX = suphu.location.x;
        int mY = suphu.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(dis - 1, dis);
            } else {
                this.location.x = mX + Util.nextInt(dis - 1, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }

    public static void init(Player player) {
        MiniPet minipet = new MiniPet(player);
        minipet.name = "#Thú Cưng " + player.getThuCung();
        minipet.gender = player.gender;
        minipet.id = player.id - 1000000;
        minipet.nPoint.hpMax = 500000;
        minipet.nPoint.hpg = 500000;
        minipet.nPoint.hp = 500000;
        minipet.nPoint.setFullHpMp();
        player.minipet = minipet;
    }
//    public static void init(Player player) {
//        MiniPet minipet = new MiniPet(player);
//        minipet.name = "# ";
//        minipet.gender = 0;
//        minipet.id = player.id - 10000;
//        minipet.nPoint.hpMax = 5000000;
//        minipet.nPoint.hpg = 5000000;
//        minipet.nPoint.hp = 5000000;
//        minipet.nPoint.setFullHpMp();
//        player.minipet = minipet;
//    }

    public static int getMiniPetById(int iditem) {
        switch (iditem) {
            case -1:
                return 0;
            case 936: // tloc
                return 1;
            case 892: // tho xam
                return 2;
            case 893: // tho trang
                return 3;
            case 942:
                return 4;
            case 943:
                return 5;
            case 944:
                return 6;
            case 967:
                return 7;
            case 1039:
                return 8;
            case 1040:
                return 9;
            case 916:
                return 10;
            case 917:
                return 11;
            case 918:
                return 12;
            case 919:
                return 13;
            case 1107:
                return 14;
            case 2045:
                return 15;
            case 2046:
                return 16;
            case 2047:
                return 17;
            case 2048:
                return 18;
            case 2017:
                return 19;
            case 2018:
                return 20;
            case 2019:
                return 21;
            case 2020:
                return 22;
            case 2021:
                return 23;
            case 2022:
                return 24;
            case 2023:
                return 25;
            case 2024:
                return 26;
            case 2025:
                return 27;
            case 2053:
                return 28;
            case 1407:
                return 29;
            case 1408:
                return 30;
            case 1413:
                return 31;
            case 1414:
                return 32;
            case 1415:
                return 33;
            case 1416:
                return 34;
            case 1417:
                return 35;
            case 1418:
                return 36;
            case 1419:
                return 37;
            case 1420:
                return 38;
            case 1409:
                return 39;
            case 1410:
                return 40;
            case 1411:
                return 41;
            case 1412:
                return 42;
            case 1421:
                return 43;
            case 1422:
                return 44;
            case 1128:
                return 45;
            case 1430:
                return 46;
            case 1435:
                return 47;
            case 1436:
                return 48;
            case 1460:
                return 49;
            case 1390:
                return 50;
            case 1391:
                return 51;
            case 1392:
                return 52;
            case 1480:
                return 53;
            case 1481:
                return 54;
            case 1500:
                return 55;

            case 908:
                return 56;
            case 909:
                return 57;
            case 910:
                return 58;
            case 1018:
                return 59;
            case 1019:
                return 60;
            case 1020:
                return 61;
            case 1623:
                return 62;
            case 1627:
                return 63;
            case 1628:
                return 64;
            case 1636:
                return 65;

            case 1645:
                return 66;
            case 1646:
                return 67;
            case 1648:
                return 68;
            case 1649:
                return 69;
            case 1650:
                return 70;

        }
        return -1;
    }

    private Mob mobAttack;

    @Override
    public void dispose() {
        this.mobAttack = null;
        this.suphu = null;
        super.dispose();
    }//763 764 765

    @Override
    public short getHead() {
        switch (PETID) {
            case 0:
                return -1;
            case 1:
                return 718;//tuần lộc
            case 2:
                return 882;
            case 3:
                return 885;
            case 4:
                return 966;
            case 5:
                return 969;
            case 6:
                return 972;
            case 7:
                return 1050;
            case 8:
                return 1089;
            case 9:
                return 1092;
            case 10:
                return 931;//3 giác
            case 11:
                return 928;//vuông
            case 12:
                return 925;//tròn
            case 13:
                return 934;//búp bê
            case 14:
                return 1155;
            case 15:
                return 2060;
            case 16:
                return 2063;
            case 17:
                return 2066;
            case 18:
                return 2069;
            case 19:
                return 778;
            case 20:
                return 813;
            case 21:
                return 891;
            case 22:
                return 894;
            case 23:
                return 897;
            case 24:
                return 925;
            case 25:
                return 928;
            case 26:
                return 931;
            case 27:
                return 934;
            case 28:
                return 1227;
            case 29:
                return 663;
            case 30:
                return 1074;
            case 31:
                return 1239;
            case 32:
                return 1242;
            case 33:
                return 1245;
            case 34:
                return 1077;
            case 35:
                return 763;
            case 36:
                return 778;
            case 37:
                return 557;
            case 38:
                return 813;
            case 39:
                return 1158;
            case 40:
                return 1155;
            case 41:
                return 1183;
            case 42:
                return 1201;
            case 43:
                return 1257;
            case 44:
                return 1260;
            case 45:
                return 1343;
            case 46:
                return 1432;
            case 47:
                return 1447;
            case 48:
                return 1450;
            case 49:
                return 1957;
            case 50:
                return 1465;
            case 51:
                return 1468;
            case 52:
                return 1471;
            case 53:
                return 1535;
            case 54:
                return 1538;
            case 55:
                return 1630;
            case 56:
                return 891;
            case 57:
                return 897;
            case 58:
                return 894;
            case 59:
                return 1183;
            case 60:
                return 1201;
            case 61:
                return 1239;
            case 62:
                return 1721;
            case 63:
                return 1732;
            case 64:
                return 1735;
            case 65:
                return 1743;

            case 66:
                return 1765;
            case 67:
                return 1770;
            case 68:
                return 1778;
            case 69:
                return 1783;
            case 70:
                return 1786;

        }
        return -1;
    }

    @Override
    public short getBody() {
        switch (PETID) {
            case 0:
                return -1;
            case 1:
                return 719;//tuần lộc
            case 2:
                return 883;
            case 3:
                return 886;
            case 4:
                return 967;
            case 5:
                return 970;
            case 6:
                return 973;
            case 7:
                return 1051;
            case 8:
                return 1090;
            case 9:
                return 1093;
            case 10:
                return 932;//tam giác
            case 11:
                return 929;//vuông
            case 12:
                return 926;
            case 13:
                return 935;
            case 14:
                return 1156;
            case 15:
                return 2061;
            case 16:
                return 2064;
            case 17:
                return 2067;
            case 18:
                return 2070;
            case 19:
                return 779;
            case 20:
                return 814;
            case 21:
                return 892;
            case 22:
                return 895;
            case 23:
                return 898;
            case 24:
                return 926;
            case 25:
                return 929;
            case 26:
                return 932;
            case 27:
                return 935;
            case 28:
                return 1228;
            case 29:
                return 664;
            case 30:
                return 1075;
            case 31:
                return 1240;
            case 32:
                return 1243;
            case 33:
                return 1246;
            case 34:
                return 1078;
            case 35:
                return 764;
            case 36:
                return 779;
            case 37:
                return 558;
            case 38:
                return 814;
            case 39:
                return 1159;
            case 40:
                return 1156;
            case 41:
                return 1184;
            case 42:
                return 1202;
            case 43:
                return 1258;
            case 44:
                return 1261;
            case 45:
                return 1344;
            case 46:
                return 1433;
            case 47:
                return 1448;
            case 48:
                return 1451;
            case 49:
                return 1958;
            case 50:
                return 1466;
            case 51:
                return 1469;
            case 52:
                return 1472;
            case 53:
                return 1536;
            case 54:
                return 1539;
            case 55:
                return 1631;
            case 56:
                return 892;
            case 57:
                return 898;
            case 58:
                return 895;
            case 59:
                return 1184;
            case 60:
                return 1202;
            case 61:
                return 1240;
            case 62:
                return 1722;
            case 63:
                return 1733;
            case 64:
                return 1736;
            case 65:
                return 1744;

            case 66:
                return 1768;
            case 67:
                return 1771;
            case 68:
                return 1781;
            case 69:
                return 1784;
            case 70:
                return 1787;
        }
        return -1;
    }

    @Override
    public short getLeg() {
        switch (PETID) {
            case 0:
                return -1;
            case 1:
                return 720;//tuần lộc
            case 2:
                return 884;
            case 3:
                return 887;
            case 4:
                return 968;
            case 5:
                return 971;
            case 6:
                return 974;
            case 7:
                return 1052;
            case 8:
                return 1091;
            case 9:
                return 1094;
            case 10:
                return 933;
            case 11:
                return 930;
            case 12:
                return 927;
            case 13:
                return 936;
            case 14:
                return 1157;
            case 15:
                return 2062;
            case 16:
                return 2065;
            case 17:
                return 2068;
            case 18:
                return 2071;
            case 19:
                return 780;
            case 20:
                return 815;
            case 21:
                return 893;
            case 22:
                return 896;
            case 23:
                return 899;
            case 24:
                return 927;
            case 25:
                return 930;
            case 26:
                return 933;
            case 27:
                return 936;
            case 28:
                return 1229;
            case 29:
                return 665;
            case 30:
                return 1076;
            case 31:
                return 1241;
            case 32:
                return 1244;
            case 33:
                return 1247;
            case 34:
                return 1079;
            case 35:
                return 765;
            case 36:
                return 780;
            case 37:
                return 559;
            case 38:
                return 815;
            case 39:
                return 1160;
            case 40:
                return 1157;
            case 41:
                return 1185;
            case 42:
                return 1203;
            case 43:
                return 1259;
            case 44:
                return 1262;
            case 45:
                return 1345;
            case 46:
                return 1434;
            case 47:
                return 1449;
            case 48:
                return 1452;
            case 49:
                return 1959;
            case 50:
                return 1467;
            case 51:
                return 1470;
            case 52:
                return 1473;
            case 53:
                return 1537;
            case 54:
                return 1540;
            case 55:
                return 1632;
            case 56:
                return 893;
            case 57:
                return 899;
            case 58:
                return 896;
            case 59:
                return 1185;
            case 60:
                return 1203;
            case 61:
                return 1241;
            case 62:
                return 1723;
            case 63:
                return 1734;
            case 64:
                return 1737;
            case 65:
                return 1745;
                
            case 66:
                return 1769;
            case 67:
                return 1772;
            case 68:
                return 1782;
            case 69:
                return 1785;
            case 70:
                return 1788;
        }
        return -1;
    }
}
