package nro.utils;

import nro.utils.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class TimeUtil {

    public static final byte SECOND = 1;
    public static final byte MINUTE = 2;
    public static final byte HOUR = 3;
    public static final byte DAY = 4;
    public static final byte WEEK = 5;
    public static final byte MONTH = 6;
    public static final byte YEAR = 7;

    /**
     *
     * @param d1 thời gian bắt đầu
     * @param d2 thời gian kết thúc
     * @param type loại
     * @return khoảng cách thời gian theo loại
     */
    public static long diffDate(Date d1, Date d2, byte type) {
        long timeDiff = Math.abs(d1.getTime() - d2.getTime());
        switch (type) {
            case SECOND:
                return (timeDiff / 1000);
            case MINUTE:
                return (timeDiff / (60 * 1000) % 60);
            case HOUR:
                return (timeDiff / (60 * 60 * 1000) % 24);
            case DAY:
                return (timeDiff / (24 * 60 * 60 * 1000));
            case WEEK:
                return (timeDiff / (7 * 24 * 60 * 60 * 1000));
            case MONTH:
                return (timeDiff / (30 * 24 * 60 * 60 * 1000));
            case YEAR:
                return (timeDiff / (365 * 24 * 60 * 60 * 1000));
            default:
                return 0;
        }
    }

    public static boolean isTimeNowInRangex(String d1, String d2, String format) throws Exception {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        try {
            long time1 = fm.parse(d1).getTime();
            long time2 = fm.parse(d2).getTime();
            long now = fm.parse(fm.format(new Date())).getTime();
            return now > time1 && now < time2;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Thời gian không hợp lệ");
        }
    }

    public static int getCurrDay() {
        LocalDate today = LocalDate.now();
        return today.getDayOfMonth();
    }

    public static int getCurrHour() {
        LocalTime now = LocalTime.now();
        return now.getHour();
    }

    public static int getCurrMin() {
        LocalTime now = LocalTime.now();
        return now.getMinute();
    }

    public static String getTimeLeft(long lastTime, int secondTarget) {
        int secondPassed = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        int secondsLeft = secondTarget - secondPassed;
        if (secondsLeft < 0) {
            secondsLeft = 0;
        }
        return secondsLeft > 60 ? (secondsLeft / 60) + " phút" : secondsLeft + " giây";
    }

    public static int getMinLeft(long lastTime, int secondTarget) {
        int secondPassed = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        int secondsLeft = secondTarget - secondPassed;
        if (secondsLeft < 0) {
            secondsLeft = 0;
        }
        int minLeft = 0;
        if (secondsLeft > 0 && secondsLeft <= 60) {
            minLeft = 1;
        } else if (secondsLeft > 60) {
            minLeft = secondsLeft / 60;
        }
        return minLeft;
    }

    public static int getSecondLeft(long lastTime, int secondTarget) {
        int secondPassed = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        int secondsLeft = secondTarget - secondPassed;
        if (secondsLeft < 0) {
            secondsLeft = 0;
        }
        return secondsLeft;
    }

    public static long getTime(String time, String format) throws Exception {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        try {
            return fm.parse(time).getTime();
        } catch (ParseException ex) {ex.printStackTrace();
            throw new Exception("Thời gian không hợp lệ");
        }
    }

    public static String getTimeNow(String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(new Date());
    }

    public static String getTimeBeforeCurrent(int subTime, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis() - subTime);
        return fm.format(date);
    }

    public static String formatTime(Date time, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(time);
    }

    public static String formatTime(long time, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(new Date(time));
    }
}
