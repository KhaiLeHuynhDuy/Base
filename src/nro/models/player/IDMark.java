package nro.models.player;

import nro.consts.ConstNpc;
import nro.models.npc.Npc;
import nro.models.shop.Shop;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data

public class IDMark {

    public int idItemUpTop;
    private int typeChangeMap; //capsule, ngọc rồng đen...
    private int indexMenu; //menu npc
    private int typeInput; //input
    private int shopId; //shop ope
    private byte typeLuckyRound; //type lucky round
    private byte isTranhNgoc = -1;

    private long idPlayThachDau; //id người chơi được mời thách đấu
    private int goldThachDau; //vàng thách đấu

    private long idEnemy; //id kẻ thù - trả thù

    private Shop shopOpen; //shop người chơi đang mở
    private String tagNameShop; //thẻ tên shop đang mở

    /**
     * loại tàu vận chuyển dùng ;0 - Không dùng ;1 - Tàu vũ trụ ;2 - Dịch chuyển
     * tức thời ;3 - Tàu tenis
     */
    private byte idSpaceShip;

    private long lastTimeBan;
    private boolean isBan;
    private boolean isActive;
    //giao dịch
    private int playerTradeId = -1;
    private Player playerTrade;
    private long lastTimeTrade;

    private long lastTimeNotifyTimeHoldBlackBall;
    private long lastTimeHoldBlackBall;
    private int tempIdBlackBallHold = -1;
    private boolean holdBlackBall;

    private long lastTimeNotifyTimeHoldGiaidauBall;
    private long lastTimeHoldGiaidauBall;
    private int tempIdGiaidauBallHold = -1;
    private boolean holdGiaidauBall;

    private int tempIdNamecBallHold = -1;
    private boolean holdNamecBall;

    private boolean loadedAllDataPlayer; //load thành công dữ liệu người chơi từ database

    private long lastTimeChangeFlag;

    //tới tương lai
    private boolean gotoFuture;
    private long lastTimeGoToFuture;

    private long lastTimeChangeZone;
    private long lastTimeChatGlobal;
    private long lastTimeChatPrivate;

    private long lastTimePickItem;

    private boolean goToBDKB;
    private long lastTimeGoToBDKB;
    private long lastTimeAnXienTrapBDKB;
    private boolean goToCDRD;
    private long lastTimeGoToCDRD;

    private boolean goToGas;
    private long lastTimeGotoGas;
    private boolean goToKG;
    private long lastTimeGoToKG;

    private Npc npcChose; //npc mở

    private byte loaiThe; //loại thẻ nạp
    private String currentQuestion; // Câu hỏi hiện tại
    private String correctAnswer;   // Đáp án đúng

    public IDMark() {

    }

    public boolean isBaseMenu() {
        return this.indexMenu == ConstNpc.BASE_MENU;
    }

    public byte getTranhNgoc() {
        return isTranhNgoc;
    }
    // Các thuộc tính khác và phương thức
    // Getter và Setter cho currentQuestion

    public String getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(String currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public String getCurrentAnswer() {
        return correctAnswer;
    }

    public void setCurrentAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setTranhNgoc(byte tn) {
        this.isTranhNgoc = tn;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void dispose() {
        if (this.shopOpen != null) {
            this.shopOpen.dispose();
            this.shopOpen = null;
        }
        this.npcChose = null;
        this.tagNameShop = null;
        this.playerTrade = null;
    }

    public void setTypeLuckyRound(byte typeLuckyRound) {
        this.typeLuckyRound = typeLuckyRound;
    }

    public byte getTypeLuckyRound() {
        return this.typeLuckyRound;
    }

    public void setIdItemUpTop(int idItemUpTop) {
        this.idItemUpTop = idItemUpTop;
    }

    public byte getIsTranhNgoc() {
        return isTranhNgoc;
    }

    public void setIsTranhNgoc(byte isTranhNgoc) {
        this.isTranhNgoc = isTranhNgoc;
    }

    public long getIdPlayThachDau() {
        return idPlayThachDau;
    }

    public void setIdPlayThachDau(long idPlayThachDau) {
        this.idPlayThachDau = idPlayThachDau;
    }

    public int getGoldThachDau() {
        return goldThachDau;
    }

    public void setGoldThachDau(int goldThachDau) {
        this.goldThachDau = goldThachDau;
    }

    public long getIdEnemy() {
        return idEnemy;
    }

    public void setIdEnemy(long idEnemy) {
        this.idEnemy = idEnemy;
    }

    public Shop getShopOpen() {
        return shopOpen;
    }

    public void setShopOpen(Shop shopOpen) {
        this.shopOpen = shopOpen;
    }

    public String getTagNameShop() {
        return tagNameShop;
    }

    public void setTagNameShop(String tagNameShop) {
        this.tagNameShop = tagNameShop;
    }

    public byte getIdSpaceShip() {
        return idSpaceShip;
    }

    public void setIdSpaceShip(byte idSpaceShip) {
        this.idSpaceShip = idSpaceShip;
    }

    public long getLastTimeBan() {
        return lastTimeBan;
    }

    public void setLastTimeBan(long lastTimeBan) {
        this.lastTimeBan = lastTimeBan;
    }

    public boolean isIsBan() {
        return isBan;
    }

    public void setIsBan(boolean isBan) {
        this.isBan = isBan;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getPlayerTradeId() {
        return playerTradeId;
    }

    public void setPlayerTradeId(int playerTradeId) {
        this.playerTradeId = playerTradeId;
    }

    public Player getPlayerTrade() {
        return playerTrade;
    }

    public void setPlayerTrade(Player playerTrade) {
        this.playerTrade = playerTrade;
    }

    public long getLastTimeTrade() {
        return lastTimeTrade;
    }

    public void setLastTimeTrade(long lastTimeTrade) {
        this.lastTimeTrade = lastTimeTrade;
    }

    public long getLastTimeNotifyTimeHoldBlackBall() {
        return lastTimeNotifyTimeHoldBlackBall;
    }

    public void setLastTimeNotifyTimeHoldBlackBall(long lastTimeNotifyTimeHoldBlackBall) {
        this.lastTimeNotifyTimeHoldBlackBall = lastTimeNotifyTimeHoldBlackBall;
    }

    public long getLastTimeHoldBlackBall() {
        return lastTimeHoldBlackBall;
    }

    public void setLastTimeHoldBlackBall(long lastTimeHoldBlackBall) {
        this.lastTimeHoldBlackBall = lastTimeHoldBlackBall;
    }

    public int getTempIdBlackBallHold() {
        return tempIdBlackBallHold;
    }

    public void setTempIdBlackBallHold(int tempIdBlackBallHold) {
        this.tempIdBlackBallHold = tempIdBlackBallHold;
    }

    public boolean isHoldBlackBall() {
        return holdBlackBall;
    }

    public void setHoldBlackBall(boolean holdBlackBall) {
        this.holdBlackBall = holdBlackBall;
    }

    public long getLastTimeNotifyTimeHoldGiaidauBall() {
        return lastTimeNotifyTimeHoldGiaidauBall;
    }

    public void setLastTimeNotifyTimeHoldGiaidauBall(long lastTimeNotifyTimeHoldGiaidauBall) {
        this.lastTimeNotifyTimeHoldGiaidauBall = lastTimeNotifyTimeHoldGiaidauBall;
    }

    public long getLastTimeHoldGiaidauBall() {
        return lastTimeHoldGiaidauBall;
    }

    public void setLastTimeHoldGiaidauBall(long lastTimeHoldGiaidauBall) {
        this.lastTimeHoldGiaidauBall = lastTimeHoldGiaidauBall;
    }

    public int getTempIdGiaidauBallHold() {
        return tempIdGiaidauBallHold;
    }

    public void setTempIdGiaidauBallHold(int tempIdGiaidauBallHold) {
        this.tempIdGiaidauBallHold = tempIdGiaidauBallHold;
    }

    public boolean isHoldGiaidauBall() {
        return holdGiaidauBall;
    }

    public void setHoldGiaidauBall(boolean holdGiaidauBall) {
        this.holdGiaidauBall = holdGiaidauBall;
    }

    public int getTempIdNamecBallHold() {
        return tempIdNamecBallHold;
    }

    public void setTempIdNamecBallHold(int tempIdNamecBallHold) {
        this.tempIdNamecBallHold = tempIdNamecBallHold;
    }

    public boolean isHoldNamecBall() {
        return holdNamecBall;
    }

    public void setHoldNamecBall(boolean holdNamecBall) {
        this.holdNamecBall = holdNamecBall;
    }

    public boolean isLoadedAllDataPlayer() {
        return loadedAllDataPlayer;
    }

    public void setLoadedAllDataPlayer(boolean loadedAllDataPlayer) {
        this.loadedAllDataPlayer = loadedAllDataPlayer;
    }

    public long getLastTimeChangeFlag() {
        return lastTimeChangeFlag;
    }

    public void setLastTimeChangeFlag(long lastTimeChangeFlag) {
        this.lastTimeChangeFlag = lastTimeChangeFlag;
    }

    public boolean isGotoFuture() {
        return gotoFuture;
    }

    public void setGotoFuture(boolean gotoFuture) {
        this.gotoFuture = gotoFuture;
    }

    public long getLastTimeGoToFuture() {
        return lastTimeGoToFuture;
    }

    public void setLastTimeGoToFuture(long lastTimeGoToFuture) {
        this.lastTimeGoToFuture = lastTimeGoToFuture;
    }

    public long getLastTimeChangeZone() {
        return lastTimeChangeZone;
    }

    public void setLastTimeChangeZone(long lastTimeChangeZone) {
        this.lastTimeChangeZone = lastTimeChangeZone;
    }

    public long getLastTimeChatGlobal() {
        return lastTimeChatGlobal;
    }

    public void setLastTimeChatGlobal(long lastTimeChatGlobal) {
        this.lastTimeChatGlobal = lastTimeChatGlobal;
    }

    public long getLastTimeChatPrivate() {
        return lastTimeChatPrivate;
    }

    public void setLastTimeChatPrivate(long lastTimeChatPrivate) {
        this.lastTimeChatPrivate = lastTimeChatPrivate;
    }

    public long getLastTimePickItem() {
        return lastTimePickItem;
    }

    public void setLastTimePickItem(long lastTimePickItem) {
        this.lastTimePickItem = lastTimePickItem;
    }

    public boolean isGoToBDKB() {
        return goToBDKB;
    }

    public void setGoToBDKB(boolean goToBDKB) {
        this.goToBDKB = goToBDKB;
    }

    public long getLastTimeGoToBDKB() {
        return lastTimeGoToBDKB;
    }

    public void setLastTimeGoToBDKB(long lastTimeGoToBDKB) {
        this.lastTimeGoToBDKB = lastTimeGoToBDKB;
    }

    public long getLastTimeAnXienTrapBDKB() {
        return lastTimeAnXienTrapBDKB;
    }

    public void setLastTimeAnXienTrapBDKB(long lastTimeAnXienTrapBDKB) {
        this.lastTimeAnXienTrapBDKB = lastTimeAnXienTrapBDKB;
    }

    public boolean isGoToCDRD() {
        return goToCDRD;
    }

    public void setGoToCDRD(boolean goToCDRD) {
        this.goToCDRD = goToCDRD;
    }

    public long getLastTimeGoToCDRD() {
        return lastTimeGoToCDRD;
    }

    public void setLastTimeGoToCDRD(long lastTimeGoToCDRD) {
        this.lastTimeGoToCDRD = lastTimeGoToCDRD;
    }

    public boolean isGoToGas() {
        return goToGas;
    }

    public void setGoToGas(boolean goToGas) {
        this.goToGas = goToGas;
    }

    public long getLastTimeGotoGas() {
        return lastTimeGotoGas;
    }

    public void setLastTimeGotoGas(long lastTimeGotoGas) {
        this.lastTimeGotoGas = lastTimeGotoGas;
    }

    public boolean isGoToKG() {
        return goToKG;
    }

    public void setGoToKG(boolean goToKG) {
        this.goToKG = goToKG;
    }

    public long getLastTimeGoToKG() {
        return lastTimeGoToKG;
    }

    public void setLastTimeGoToKG(long lastTimeGoToKG) {
        this.lastTimeGoToKG = lastTimeGoToKG;
    }

    public Npc getNpcChose() {
        return npcChose;
    }

    public void setNpcChose(Npc npcChose) {
        this.npcChose = npcChose;
    }

    public byte getLoaiThe() {
        return loaiThe;
    }

    public void setLoaiThe(byte loaiThe) {
        this.loaiThe = loaiThe;
    }

    public int getIdItemUpTop() {
        return this.idItemUpTop;
    }

    public int getIndexMenu() {
        return indexMenu;
    }

    public void setIndexMenu(int indexMenu) {
        this.indexMenu = indexMenu;
    }

    public void setTypeInput(int typeInput) {
        this.typeInput = typeInput;
    }

    public int getTypeInput() {
        return this.typeInput;
    }

    public int getShopId() {
        return this.shopId;
    }

    public int getTypeChangeMap() {
        return typeChangeMap;
    }

    public void setTypeChangeMap(int typeChangeMap) {
        this.typeChangeMap = typeChangeMap;
    }
}
