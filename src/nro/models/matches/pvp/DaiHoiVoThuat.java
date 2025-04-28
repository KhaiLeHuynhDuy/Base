package nro.models.matches.pvp;

import nro.models.player.Player;
import nro.server.Manager;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DaiHoiVoThuat implements Runnable {

    public ArrayList<Player> listReg = new ArrayList<>();
    public ArrayList<Player> listPlayerWait = new ArrayList<>();
    public String NameCup;
    public String[] Time;
    public int gem;
    public int gold;
    public int min_start;
    public int min_start_temp;
    public int min_limit;
    public int round = 1;

    public int Hour;
    public int Minutes;
    public int Second;

    private static DaiHoiVoThuat instance;

    public static DaiHoiVoThuat getInstance() {
        if (instance == null) {
            instance = new DaiHoiVoThuat();
        }
        return instance;
    }

    public DaiHoiVoThuat getDaiHoiNow() {
        for (DaiHoiVoThuat dh : Manager.LIST_DHVT) {
            if (dh != null && Util.contains(dh.Time, String.valueOf(Hour))) {
                return dh;
            }
        }
        return null;
    }

    public String getInfo() {
        for (DaiHoiVoThuat daihoi : Manager.LIST_DHVT) {
            if (daihoi.gold > 0) {
                return "Lịch thi đấu trong ngày\nGiải Nhi đồng: 8,14,18h\nGiải Siêu cấp 1: 9,13,19h\nGiải Siêu Cấp 2: 10,15,20h\nGiải Siêu cấp 3: 11,16,21h\nGiải Ngoại hạng: 12,17,22,23h\n"
                        + "Giải thưởng khi thắng mỗi vòng\nGiải Nhi đồng: 2 ngọc\nGiải Siêu cấp 1: 4 ngọc\nGiải Siêu cấp 2: 6 ngọc\nGiải Siêu cấp 3: 8 ngọc\nGiải Ngoại hạng: 20.000 vàng\n"
                        + "Lệ phí đăng ký các giải đấu\nGiải Nhi đồng: 2 ngọc\nGiải Siêu cấp 1: 4 ngọc\nGiải Siêu cấp 2: 6 ngọc\nGiải Siêu cấp 3: 8 ngọc\nGiải Ngoại hạng: 20.000 vàng";
            } else if (daihoi.gem > 0) {
                return "Lịch thi đấu trong ngày\nGiải Nhi đồng: 8,14,18h\nGiải Siêu cấp 1: 9,13,19h\nGiải Siêu Cấp 2: 10,15,20h\nGiải Siêu cấp 3: 11,16,21h\nGiải Ngoại hạng: 12,17,22,23h\n"
                        + "Giải thưởng khi thắng mỗi vòng\nGiải Nhi đồng: 2 ngọc\nGiải Siêu cấp 1: 4 ngọc\nGiải Siêu cấp 2: 6 ngọc\nGiải Siêu cấp 3: 8 ngọc\nGiải Ngoại hạng: 20.000 vàng\n"
                        + "Lệ phí đăng ký các giải đấu\nGiải Nhi đồng: 2 ngọc\nGiải Siêu cấp 1: 4 ngọc\nGiải Siêu cấp 2: 6 ngọc\nGiải Siêu cấp 3: 8 ngọc\nGiải Ngoại hạng: 20.000 vàng";
            }
        }
        return "Đã hết thời gian đăng ký, vui lòng đợi cho đến giải đấu sau.";
    }

    public synchronized void registerPlayer(Player player) {
        if (!listReg.contains(player)) {
            listReg.add(player);
            System.out.println("Đăng ký thành công.");
        } else {
            System.out.println("Người chơi đã đăng ký trước đó.");
        }
    }

    public synchronized void unregisterPlayer(Player player) {
        if (listReg.contains(player)) {
            listReg.remove(player);
            System.out.println("Hủy đăng ký thành công.");
        } else {
            System.out.println("Người chơi chưa đăng ký.");
        }
    }

    @Override
    public void run() {
        while (true) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            DaiHoiVoThuat daiHoiNow = getDaiHoiNow();

            if (daiHoiNow != null) {
                if (minutes == daiHoiNow.min_start && second == 0) {
                    // logic để bắt đầu giải đấu
                    System.out.println("Bắt đầu giải đấu: " + daiHoiNow.NameCup);
                    round = 1;
                } else if (minutes == daiHoiNow.min_start_temp && second == 0) {
                    // logic để chuyển vòng sau khi kết thúc vòng trước đó
                    round++;
                    System.out.println("Vòng " + round + " của giải đấu " + daiHoiNow.NameCup);
                }
            }

            try {
                long sleepTime = calculateSleepTime(hour, minutes, second);
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long calculateSleepTime(int hour, int minutes, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, second);
        cal.add(Calendar.SECOND, 1); // Tăng thời gian lên 1 giây

        long currentTime = System.currentTimeMillis();
        long nextTime = cal.getTimeInMillis();
        long sleepTime = nextTime - currentTime;

        return Math.max(0, sleepTime);
    }

}

//public class DaiHoiVoThuat implements Runnable{
//    public ArrayList<Player> listReg = new ArrayList<>();
//    public ArrayList<Player> listPlayerWait = new ArrayList<>();
//    public String NameCup;
//    public String[] Time;
//    public int gem;
//    public int gold;
//    public int min_start;
//    public int min_start_temp;
//    public int min_limit;
//    public int round = 1;
//    
//    public int Hour;
//    public int Minutes;
//    public int Second;
//    
//    private static DaiHoiVoThuat instance;    
//
//    public static DaiHoiVoThuat gI() {
//        if (instance == null) {
//            instance = new DaiHoiVoThuat();
//        }
//        return instance;
//    }
//    
//    public DaiHoiVoThuat getDaiHoiNow(){
//        for(DaiHoiVoThuat dh : Manager.LIST_DHVT){
//            if(dh != null && Util.contains(dh.Time, String.valueOf(Hour))){
//                return dh;
//            }
//        }
//        return null;
//    }
//    
//    public String Info() {
//        for(DaiHoiVoThuat daihoi : Manager.LIST_DHVT){
//            if (daihoi.gold > 0) {
//                return "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,14,18h\bGiải Siêu cấp 1: 9,13,19h\bGiải Siêu Cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\n"
//                        + "Giải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng\n"
//                        + "Lệ phí đăng ký các giải đấu\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng";
//            } else if (daihoi.gem > 0) {
//                return "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,14,18h\bGiải Siêu cấp 1: 9,13,19h\bGiải Siêu Cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\n"
//                        + "Giải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng\n"
//                        + "Lệ phí đăng ký các giải đấu\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng";
//            }
//        }
//        return "Đã hết thời gian đăng ký vui lòng đợi đến giải đấu sau\b";
//    }
//
//    
//    @Override
//    public void run() {
//        while (true) {
//            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
//            try { 
//                Second = calendar.get(Calendar.SECOND);
//                Minutes = calendar.get(Calendar.MINUTE);
//                Hour = calendar.get(Calendar.HOUR_OF_DAY);
//               
//                DaiHoiVoThuatService.gI(getDaiHoiNow()).Update();
//                Thread.sleep(1000);
//            }catch(Exception e){
//            }
//        }
//    }
//}

