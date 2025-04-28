package nro.server.io;

import java.io.IOException;
import java.net.Socket;

import nro.models.player.Player;
import nro.server.Controller;
import nro.data.DataGame;
import nro.jdbc.daos.GodGK;
import nro.models.item.Item;
import nro.network.session.Session;
import nro.network.io.Message;
import nro.server.Client;
import nro.server.Maintenance;
import nro.server.Manager;
import nro.server.model.AntiLogin;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MySession extends Session {

    private static final Map<String, AntiLogin> ANTILOGIN = new HashMap<>();
    public Player player;

    public byte timeWait = 100;

    public boolean connected;
    public boolean sentKey;

    public static final byte[] KEYS = {0};
    public byte curR, curW;

    public String ipAddress;
    public boolean isAdmin;
    public boolean isAdmin1;
    public int userId;
    public String uu;
    public String pp;

    public int typeClient;
    public byte zoomLevel;

    public long lastTimeLogout;
    public boolean joinedGame;

    public long lastTimeReadMessage;

    public boolean actived;

    public boolean nhanvang;
    public boolean nhanngocxanh;
    public boolean nhanngochong;
    public int vnd;
    public int totalvnd;
    public int totalvnd2;
    public int totalvnd3;
    public int sk20_10;
    public int NHS;
    public int SKHE;

    public int goldBar;

    public int coinBar;
    public List<Item> itemsReward;
    public String dataReward;
    public boolean is_gift_box;
    public double bdPlayer;

    public int version;

    public int Bar;
//    public boolean isRIcon;

    public MySession(Socket socket) {

        super(socket);

        ipAddress = socket.getInetAddress().getHostAddress();
//        isRIcon = false;
//        if (ServerManager.gI().canConnectWithIp(this.ipAddress)) {
//
//            return;
//        }
//        Logger.success("Login success on port " + socket.getPort() + " with IP address: " + ipAddress + "\n");
    }

    public void initItemsReward() {
        this.itemsReward = new ArrayList<>();
        if (dataReward == null || dataReward.isEmpty()) {
            return;
        }
        try {
//            this.itemsReward = new ArrayList<>();
            String[] itemsReward = dataReward.split(";");
            for (String itemInfo : itemsReward) {
//                if (itemInfo == null || itemInfo.equals("")) {
//                    continue;
//                }
                if (itemInfo == null || itemInfo.isEmpty()) {
                    continue;
                }
                String[] subItemInfo = itemInfo.replaceAll("[{}\\[\\]]", "").split("\\|");
                String[] baseInfo = subItemInfo[0].split(":");
                int itemId = Integer.parseInt(baseInfo[0]);
                int quantity = Integer.parseInt(baseInfo[1]);
                Item item = ItemService.gI().createNewItem((short) itemId, quantity);
                if (subItemInfo.length == 2) {
                    String[] options = subItemInfo[1].split(",");
                    for (String opt : options) {
                        if (opt == null || opt.equals("")) {
                            continue;
                        }
                        String[] optInfo = opt.split(":");
                        int tempIdOption = Integer.parseInt(optInfo[0]);
                        int param = Integer.parseInt(optInfo[1]);
                        item.itemOptions.add(new Item.ItemOption(tempIdOption, param));
                    }
                }
                this.itemsReward.add(item);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Ghi log lỗi
            Logger.error("Lỗi định dạng số trong initItemsReward: " + e.getMessage());
        } catch (Exception e) { e.printStackTrace();
            // Ghi log các lỗi khác
            Logger.error("Lỗi không xác định trong initItemsReward: " + e.getMessage());
        }
    }

    @Override
    public void sendKey() throws Exception {
        super.sendKey();
        this.startSend();
    }

    public void sendSessionKey() {
        Message msg = new Message(-27);
        try {
            msg.writer().writeByte(KEYS.length);
            msg.writer().writeByte(KEYS[0]);
            for (int i = 1; i < KEYS.length; i++) {
                msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
            }
            this.sendMessage(msg);
            msg.cleanup();
            sentKey = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {
//        this.sessionActive = true;
        AntiLogin al = ANTILOGIN.get(this.ipAddress);
        if (al == null) {
            al = new AntiLogin();
            ANTILOGIN.put(this.ipAddress, al);
        }
        if (!al.canLogin()) {
            Service.gI().sendThongBaoOK(this, al.getNotifyCannotLogin());
            return;
        }
        if (Manager.LOCAL) {
            Service.gI().sendThongBaoOK(this, "Server này chỉ để lưu dữ liệu\nVui lòng qua server khác");
            return;
        }
      
        if (Maintenance.isRuning) {
            Service.gI().sendThongBaoOK(this, "Server đang trong thời gian bảo trì, vui lòng quay lại sau");
            return;
        }
        if (!this.isAdmin && Client.gI().getPlayers().size() >= Manager.MAX_PLAYER) {
            Service.gI().sendThongBaoOK(this, "Máy chủ hiện đang quá tải, "
                    + "cư dân vui lòng di chuyển sang máy chủ khác.");
            return;
        }
        if (this.player != null) {
            return;
        } else {
            Player player = null;
            try {
                long st = System.currentTimeMillis();
                this.uu = username;
                this.pp = password;

                player = GodGK.login(this, al);
                if (player != null) {

                     // -77 max small
                    DataGame.sendSmallVersion(this);
                    // -93 bgitem version
                 //  Service.gI().sendMessage(this, -93, "1630679752231_-93_r");

                    this.timeWait = 0;
                    this.joinedGame = true;
                    player.nPoint.calPoint();
                    player.nPoint.setHp(Util.maxIntValue(player.nPoint.hp));
                    player.nPoint.setMp(Util.maxIntValue(player.nPoint.mp));
                    player.zone.addPlayer(player);
                    if (player.pet != null) {
                        player.pet.nPoint.calPoint();
                        player.pet.nPoint.setHp(Util.maxIntValue(player.pet.nPoint.hp));
                        player.pet.nPoint.setMp(Util.maxIntValue(player.pet.nPoint.mp));
                    }

                     player.setSession(this);
                    Client.gI().put(player);
                    this.player = player;
                    //-28 -4 version data game
                    DataGame.sendVersionGame(this);
                    //-31 data item background
                    DataGame.sendDataItemBG(this);
                    Controller.getInstance().sendInfo(this);
//                    

//                    player.setSession(this);
//                    Client.gI().put(player);
//                    this.player = player;
//                    //-28 -4 version data game
//                    DataGame.sendVersionGame(this);
//                    //-31 data item background
//                    DataGame.sendDataItemBG(this);
//                    Controller.getInstance().sendInfo(this);
//                    
//                    Service.gI().sendTimeSkill(player);
//                    PlayerService.gI().sendInfoHpMp(player);
                    Logger.warning("Login thành công player " + this.player.name + ": " + (System.currentTimeMillis() - st) + " ms\n");
//                    Service.gI().sendThongBaoOK(this, "Ngọc rồng sao đen sẽ mở lúc 21h hôm nay");
                }
            } catch (Exception e) { e.printStackTrace();
                if (player != null) {
                    player.dispose();
                }

            }
        }
    }

}
