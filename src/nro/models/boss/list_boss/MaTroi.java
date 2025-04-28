//package nro.models.boss.list_boss;
//
//import nro.consts.ConstPlayer;
//import nro.models.boss.Boss;
//import nro.models.boss.BossType;
//import nro.models.boss.BossesData;
//import nro.models.map.Zone;
//import nro.models.player.Player;
//import nro.server.Client;
//import nro.services.MapService;
//import nro.services.SkillService;
//import nro.services.func.ChangeMapService;
//import nro.utils.SkillUtil;
//import nro.utils.Util;
//
//public class MaTroi extends Boss {
//
//    private final long timeChangeMap = 10000;
//    private long lastTimeJoinMap;
//
//    public MaTroi() throws Exception {
//        super(BossType.MA_TROI, BossesData.MA_TROI);
//    }
//
//    @Override
//    public Player getPlayerAttack() {
//        for (Player player : this.zone.getNotBosses()) {
//            if (player != null && !player.isDie() && !player.effectSkin()) {
//                return player;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            damage = 1;
//            this.nPoint.subHP(damage);
//            if (isDie()) {
//                this.setDie(plAtt);
//                die(plAtt);
//            }
//            this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
//            SkillService.gI().useSkill(this, plAtt, null, null);
//            return damage;
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public void attack() {
//        if (Util.canDoWithTime(this.lastTimeAttack, 10) && this.typePk == ConstPlayer.PK_ALL) {
//            this.lastTimeAttack = System.currentTimeMillis();
//            try {
//                Player pl = getPlayerAttack();
//                if (pl == null || pl.isDie()) {
//                    return;
//                }
//                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
//                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
//                    if (Util.isTrue(5, 20)) {
//                        if (SkillUtil.isUseSkillChuong(this)) {
//                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
//                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 70));
//                        } else {
//                            this.moveTo(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
//                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50));
//                        }
//                    }
//                    SkillService.gI().useSkill(this, pl, null, null);
//                    checkPlayerDie(pl);
//                } else {
//                    if (Util.isTrue(1, 2)) {
//                        this.moveToPlayer(pl);
//                    }
//                }
//            } catch (Exception ex) {
//            }
//        }
//    }
//
//    @Override
//    public Zone getMapJoin() {
//        int mapId = this.getData()[this.getCurrentLevel()].getMapJoin()[Util.nextInt(0, this.getData()[this.getCurrentLevel()].getMapJoin().length - 1)];
//        return MapService.gI().getMapById(mapId).zones.get(0);
//    }
//
//    @Override
//    public void joinMap() {
//        super.joinMap();
//        lastTimeJoinMap = System.currentTimeMillis() + timeChangeMap;
//    }
//
//    @Override
//    public void update() {
//        super.update();
//        Player ramdonPlayer = Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size()));
//        if (ramdonPlayer != null && ramdonPlayer.zone.isKhongCoTrongTaiTrongKhu()) {
//            if (ramdonPlayer.zone.map.mapId != 51 && ramdonPlayer.zone.map.mapId != 113 && ramdonPlayer.zone.map.mapId != 129 && this.zone.getPlayers().size() <= 0 && System.currentTimeMillis() > this.lastTimeJoinMap) {
//                if (ramdonPlayer.id != -1000000) {
//                    lastTimeJoinMap = System.currentTimeMillis() + timeChangeMap;
//                    System.err.println("Ma Trơi Đã Tìm Thấy: " + this.zone.getPlayers().size() + " Player");
//                    ChangeMapService.gI().spaceShipArrive(this, (byte) 2, ChangeMapService.DEFAULT_SPACE_SHIP);
//                    ChangeMapService.gI().exitMap(this);
//                    this.setZoneFinal(null);
//                    this.setLastZone(null);
//                    this.zone = ramdonPlayer.zone;
//                    this.location.x = Util.nextInt(100, zone.map.mapWidth - 100);
//                    this.location.y = zone.map.yPhysicInTop(this.location.x, 100);
//                    this.joinMap();
//                    System.err.println("New MAP: " + this.zone.map.mapId);
//                    System.err.println("Ma Trơi Đã Tìm Thấy: " + this.zone.getPlayers().size() + " Player");
//                }
//
//            }
//        }
//
//    }
//
//}
