package nro.server;

import EmtiManager.EmtiManager;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
//import nro.consts.ConstDataEvent;
//import nro.consts.SK20_10;

import java.net.ServerSocket;

import nro.jdbc.daos.HistoryTransactionDAO;
import nro.kygui.ShopKyGuiManager;
import nro.models.boss.BossManager;
import nro.models.item.Item;
import nro.models.map.challenge.MartialCongressManager;
import nro.models.map.sieuhang.SieuHangManager;
import nro.models.map.sieuhang.TaskTraoQua;
import nro.models.pariry.pariryManager;
import nro.models.player.Player;
import nro.network.session.ISession;
import nro.network.example.MessageSendCollect;
import nro.network.server.GirlkunServer;
import nro.network.server.IServerClose;
import nro.network.server.ISessionAcceptHandler;

import nro.server.io.MyKeyHandler;
import nro.server.io.MySession;
import nro.services.ClanService;
import nro.services.InventoryServiceNew;
import nro.services.NgocRongNamecService;
import nro.services.Service;
import nro.services.func.ChonAiDay;
import nro.services.func.TopService;
//import nro.sukienbanhchung.NauBanhServices;
import nro.utils.Logger;
import nro.utils.TimeUtil;
import nro.utils.Util;
import java.io.File;
import java.net.Socket;

import java.util.*;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JPanel;
import nro.consts.ConstDataEventNAP;
import nro.consts.ConstDataEventSM;
import nro.jdbc.daos.PlayerDAO;
import nro.map.daihoi.DaiHoiService;
import nro.models.clan.Clan;
import nro.models.matches.pvp.DaiHoiVoThuat;
import nro.models.player.MiniPet;
import nro.models.tasktraoqua.TaskTraoQuaNHS;
import nro.models.tasktraoqua.TaskTraoQuaNap;
import nro.models.tasktraoqua.TaskTraoQuaSM;
import nro.models.tasktraoqua.TaskTraoQuaTV;
import nro.services.func.MiniGame;

public class ServerManager {

    public static String timeStart;
    public Player pl;
    public MySession ss;
    public Socket soc;
    public static final Map CLIENTS = new HashMap();

    public static String NAME = "Girlkun75";
    public static int PORT = 14445;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    public void init() {
        Manager.gI();
        try {
            if (Manager.LOCAL) {
                return;
            }
            GirlkunDB.executeUpdate("update account set last_time_login = '2000-01-01', "
                    + "last_time_logout = '2001-01-01'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        HistoryTransactionDAO.deleteHistory();
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public long getNumPlayer() {
        long num = 0;
        try {
            GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT COUNT(*) FROM `player`");
            rs.first();
            num = rs.getLong(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");

//        ServerManager.gI().run();
        new nro.manager.ServerManagerUI().setVisible(true);
    }

//    public void run() {
//        activeCommandLine();
//        activeGame();
//        activeServerSocket();
//        ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
//        NgocRongNamecService.gI().initNgocRongNamec((byte) 0);
//        new Thread(DaiHoiVoThuat.getInstance(), "Thread DHVT").start();// Khởi tạo các thread
//        new Thread(ChonAiDay.gI(), "Thread CAD").start();
//        new Thread(NgocRongNamecService.gI(), "Thread NRNM").start();
//        new Thread(TopService.gI(), "Thread TOP").start();
//        long delay = 500; // Cập nhật thông tin game và player
//        isRunning = true;
//        new Thread(() -> {
//            while (isRunning) {
//                try {
//                    long start = System.currentTimeMillis();
//                    MartialCongressManager.gI().update();
//                    Player player = null;
//                    for (int i = 0; i < Client.gI().getPlayers().size(); ++i) {
//                        if (Client.gI().getPlayers().get(i) != null) {
//                            player = (Client.gI().getPlayers().get(i));
//                            PlayerDAO.updatePlayer(player);
//                        }
//                    }
//                    ShopKyGuiManager.gI().save();
//                    long timeUpdate = System.currentTimeMillis() - start;
//                    if (timeUpdate < delay) {
//                        Thread.sleep(delay - timeUpdate);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "Update dai hoi vo thuat").start();
//        try {
//            Thread.sleep(1000);
//            BossManager.getInstance().loadBoss();
//            Manager.MAPS.forEach(nro.models.map.Map::initBoss);
//        } catch (InterruptedException ex) {
//            java.util.logging.Logger.getLogger(BossManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public void run() {
//        JFrame frame = new JFrame("PANEL ADMIN");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel panel = new Panel();
//        frame.add(panel);
//        frame.pack();
//        frame.setVisible(true);
        long delay = 500;
        isRunning = true;
        activeCommandLine();
        activeGame();
        activeServerSocket();
        Logger.log(Logger.PURPLE, "\n      ▄█████ ]▄▄▄▄▄▄▃\n ▂▄▅███████▅▄▃▂\nI████[EMTI]████]\n ◥⊙▲⊙▲⊙▲⊙▲⊙▲⊙▲⊙◤\n");

//        MaQuaTangManager.gI().init();
        new Thread(DaiHoiVoThuat.getInstance(), "Thread DHVT").start();

        MiniGame.gI().MiniGame_S1.active(1000);
        
//        ChonAiDay.gI().lastTimeEnd = System.currentTimeMillis() + 300000;
//        new Thread(ChonAiDay.gI(), "Thread CAD").start();

        NgocRongNamecService.gI().initNgocRongNamec((byte) 0);

        new Thread(NgocRongNamecService.gI(), "Thread NRNM").start();

        new Thread(TopService.gI(), "Thread TOP").start();

//        new Thread(pariryManager.gI(), "Thread Pariry").start();
        new Thread(() -> {
            while (isRunning) {
                try {

                    long start = System.currentTimeMillis();
                    MartialCongressManager.gI().update();
                    SieuHangManager.gI().update();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update dai hoi vo thuat").start();
//        new Thread(() -> {
//            while (isRunning) {
//                try {
//
//                    long start = System.currentTimeMillis();
//                    ConstDataEvent.isRunningSK16 = ConstDataEvent.isActiveEvent();
//                    SK20_10.isRunningSK2010 = SK20_10.isActiveEvent();
//                    ConstDataEventSM.isRunningSK = ConstDataEventSM.isActiveEvent();
//                    ConstDataEventNAP.isRunningSK = ConstDataEventNAP.isActiveEvent();
//                    TaskTraoQua.startTask();
//                    TaskTraoQuaNHS.startTask();
////                    TaskTraoQuaNap.startTask();
////                    TaskTraoQuaSM.startTask();
//                    TaskTraoQuaTV.startTask();
//
//                    long timeUpdate = System.currentTimeMillis() - start;
//                    if (timeUpdate < delay) {
//                        Thread.sleep(delay - timeUpdate);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "Update sự kiện auto nhận quà").start();
        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    ShopKyGuiManager.update();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update Auto Ký Gửi").start();
//        NauBanhServices nauBanhServices = new NauBanhServices();
//        Thread thread = new Thread(nauBanhServices);
//        thread.setName("Sự kiện nấu bánh đmmmmm tuấn");
//        thread.start();
        try {
            Thread.sleep(1000);
            BossManager.gI().loadBoss();
            Manager.MAPS.forEach(nro.models.map.Map::initBoss);

            DaiHoiService.gI().initDaiHoiVoThuat();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(BossManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void act() throws Exception {
        GirlkunServer.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
            @Override
            public void sessionInit(ISession is) {
                if (!canConnectWithIp(is.getIP())) {
                    is.disconnect();
                    return;
                }
                is = is.setMessageHandler(Controller.getInstance())
                        .setSendCollect(new MessageSendCollect())
                        .setKeyHandler(new MyKeyHandler())
                        .startCollect();
            }

            @Override
            public void sessionDisconnect(ISession session) {
                Client.gI().kickSession((MySession) session);
            }
        }).setTypeSessioClone(MySession.class)
                .setDoSomeThingWhenClose(new IServerClose() {
                    @Override
                    public void serverClose() {
                        System.out.println("server close");
                        System.exit(0);
                    }
                })
                .start(PORT);
    }

    private void activeServerSocket() {
        if (true) {
            try {
                this.act();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    private void activeCommandLine() {

        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                    Maintenance.gI().start(60 * 2);
                } else if (line.equals("athread")) {
                    ServerNotify.gI().notify("Nro Arriety debug server: " + Thread.activeCount());
                } else if (line.equals("nplayer")) {
                    Logger.error("Player in game: " + Client.gI().getPlayers().size() + "\n");
                } else if (line.equals("admin")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }).start();
                } else if (line.startsWith("kick")) {
                    new Thread(() -> {
                        try {

                            Client.gI().cloneMySessionNotConnect();
                            try {
                                Logger.error("Đang tiến hành lưu data người dùng");
                                for (Player pl : Client.gI().getPlayers()) {
                                    try {
                                        if (pl != null && pl.isPl()) {
                                            PlayerDAO.updatePlayer(pl);
                                            Thread.sleep(50);
                                        }
                                    } catch (Exception exx) {
                                        exx.printStackTrace();
                                    }
                                }
                                Thread.sleep(1000);
                                Logger.success("Lưu dữ liệu người dùng thành công");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Logger.error("Lỗi lưu dữ liệu người dùng");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.error("Thông báo: Lỗi khi kick ss.\n");
                        }
                    }).start();
                } else if (line.startsWith("bang")) {
                    new Thread(() -> {
                        try {
                            ClanService.gI().close();
                            Logger.error("Save " + Manager.CLANS.size() + " bang");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
                        }
                    }).start();
                } else if (line.startsWith("a")) {
                    String a = line.replace("a ", "");
                    Service.gI().sendThongBaoAllPlayer(a);
                } else if (line.startsWith("qua")) {
                    try {
                        List<Item.ItemOption> ios = new ArrayList<>();
                        String[] pagram1 = line.split("=")[1].split("-");
                        String[] pagram2 = line.split("=")[2].split("-");
                        if (pagram1.length == 4 && pagram2.length % 2 == 0) {
                            Player p = Client.gI().getPlayer(Integer.parseInt(pagram1[0]));
                            if (p != null) {
                                for (int i = 0; i < pagram2.length; i += 2) {
                                    ios.add(new Item.ItemOption(Integer.parseInt(pagram2[i]), Integer.parseInt(pagram2[i + 1])));
                                }
                                Item i = Util.sendDo(Integer.parseInt(pagram1[2]), Integer.parseInt(pagram1[3]), ios);
                                i.quantity = Integer.parseInt(pagram1[1]);
                                InventoryServiceNew.gI().addItemBag(p, i);
                                InventoryServiceNew.gI().sendItemBags(p);
                                Service.gI().sendThongBao(p, "Admin trả đồ. anh em thông cảm nhé...");
                            } else {
                                System.out.println("Người chơi không online");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Lỗi quà");
                    }
                }
            }
        }, "Active line").start();
    }

    private void activeGame() {
    }

    public void close(long delay) {
        GirlkunServer.gI().stopConnect();

        isRunning = false;
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
        }
        Client.gI().close();
        ShopKyGuiManager.gI().save();

        Logger.log(Logger.RED, "Bảo trì đóng server thành công.\n");
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "run.bat");
            pb.directory(new File("C:\\Users\\Administrator\\Desktop\\Sever1"));
            pb.inheritIO();
            pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);

    }
}
