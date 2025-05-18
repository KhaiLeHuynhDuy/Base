package nro.utils;

import nro.models.boss.BossManager;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import java.text.NumberFormat;
import java.util.*;

import nro.models.matches.TOP;
import nro.models.mob.Mob;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.Manager;
import nro.services.ItemService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import nro.consts.cn;
import org.apache.commons.lang.ArrayUtils;

public class Util {

    private static final Random rand;
    private static final Locale locale = Locale.forLanguageTag("vi-VN");
    private static final NumberFormat num = NumberFormat.getInstance(locale);
    static NumberFormat numberFormat;

    static {
        rand = new Random();
    }

    public static String cutPng(String name) {
        if (name.toLowerCase().contains(".png")) {
            if (name.toLowerCase().contains("_1.png")) {
                return name.toLowerCase().replace("_1.png", "");
            }
            return name.toLowerCase().replace(".png", "");
        }
        return name;
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long IntToLong(long power) {
        numberFormat.setMaximumFractionDigits(1);
        if (power >= 1000) {
            return power / 1000;
        } else {
            return power;
        }
    }

    public static long Tamkjllnext(double from, double to) {
        // Telegram: @Tamkjll
        long result = 0L;
        if (from <= to) {
            double difference = to - from;
            if (difference > 0) {
                result = (long) (from + rand.nextInt((int) (difference + 1)));
            } else {

            }
        } else {

        }
        return result;
    }

    public static long TamkjllGH(double a) {
        return (long) a;
    }

    public static long maxIntValue(double a) {
        if (cn.readInt) {
            if (a > Integer.MAX_VALUE) {
                a = Integer.MAX_VALUE;
            }
            return (int) a;
        }
        return (long) a;
    }

//    public static int HoangKietGH(Player plInfo) {
//        if (plInfo.isBoss && plInfo.nPoint.hp >= Integer.MAX_VALUE - 1) {
//            int percentage = Util.TamkjllGH(((double) plInfo.nPoint.hp / plInfo.nPoint.hpMax * 100));
//            return percentage;
//        } else {
//
//            return Util.TamkjllGH((plInfo.nPoint != null) ? plInfo.nPoint.hp : 1);
//        }
//    }
    public static int createIdBossClone(int idPlayer) {
        return -idPlayer - 100_000_000;
    }

    public static String format(double power) {
        return num.format(power);
    }

    public static int[] pickNRandInArr(int[] array, int n) {
        List<Integer> list = new ArrayList<Integer>(array.length);
        for (int i : array) {
            list.add(i);
        }
        Collections.shuffle(list);
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            answer[i] = list.get(i);
        }
        Arrays.sort(answer);
        return answer;
    }

    public static boolean contains(String[] arr, String key) {
        return Arrays.toString(arr).contains(key);
    }

    public static String getFormatNumber(long hp) {

        return Util.num.format(hp);
    }

    public static String msToTime(long ms) {
        ms = ms - System.currentTimeMillis();
        if (ms < 0) {
            ms = 0;
        }
        long mm = 0;
        long ss = 0;
        long hh = 0;
        ss = ms / 1000;
        mm = ss / 60;
        ss = ss % 60;
        hh = mm / 60;
        mm = mm % 60;
        String ssString = String.valueOf(ss);
        String mmString = String.valueOf(mm);
        String hhString = String.valueOf(hh);
        String time = null;
        if (hh != 0) {
            time = hhString + "h:" + mmString + "m:" + ssString + "s";
        } else if (mm != 0) {
            time = mmString + "m:" + ssString + "s";
        } else if (ss != 0) {
            time = ssString + "s";
        } else {
            time = "Hết hạn";
        }
        return time;
    }

    public static String numberToMoney(long power) {
        Locale locale = Locale.forLanguageTag("vi-VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " k";
        } else {
            return num.format(power);
        }
    }

    public static String powerToString(double power) {
        Locale locale = Locale.forLanguageTag("vi-VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " k";
        } else {
            return num.format(power);
        }
    }

    public static String powerToString(long power) {
        Locale locale = Locale.forLanguageTag("vi-VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " k";
        } else {
            return num.format(power);
        }
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static double myGetDistcance(int x1, int y1, int x2, int y2) {
        int deltaX = x2 - x1;
        int deltaY = y2 - y1;
        return Math.abs(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }

    public static int getDistance(Player pl1, Player pl2) {
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        if (pl1 != null && pl1.location != null) {
            x1 = pl1.location.x;
            y1 = pl1.location.y;
        }
        if (pl2 != null && pl2.location != null) {
            x2 = pl2.location.x;
            y2 = pl2.location.y;
        }
        return getDistance(x1, y1, x2, y2);
    }

    public static int getDistance(Player pl, Npc npc) {
        return getDistance(pl.location.x, pl.location.y, npc.cx, npc.cy);
    }

    public static int getDistance(Player pl, Mob mob) {

        return getDistance(pl.location.x, pl.location.y, mob.location.x, mob.location.y);
    }

    public static int getDistance(Mob mob1, Mob mob2) {
        return getDistance(mob1.location.x, mob1.location.y, mob2.location.x, mob2.location.y);
    }

//    public static int nextInt(int from, int to) {
//        return from + rand.nextInt(to - from + 1);
//    }
    public static int nextInt(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("'from' must be less than or equal to 'to'");
        }
        return from + rand.nextInt(to - from + 1);
    }

    public static long nextLong(long from, long to) {
        if (from > to) {
            throw new IllegalArgumentException("'from' must be less than or equal to 'to'");
        }
        long range = to - from + 1;
        long randomLong = rand.nextLong() % range;
        if (randomLong < 0) {
            randomLong = -randomLong;
        }
        return from + randomLong;
    }

    public static int nextIntKiet(int from, int to) {
        if (from > to) {
            return -1;
        }
        return from + rand.nextInt(to - from + 1);
    }

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public static int nextIntDhvt(int from, int to) {
        return from + rand.nextInt(to - from);
    }

    public static int nextInt(int[] percen) {
        int next = nextInt(1000), i;
        for (i = 0; i < percen.length; i++) {
            if (next < percen[i]) {
                return i;
            }
            next -= percen[i];
        }
        return i;
    }

    public static int getOne(int n1, int n2) {
        return rand.nextInt() % 2 == 0 ? n1 : n2;
    }

    public static boolean isStringInt(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return false;
        }
        return true;
    }

    public static int Ramdom(int n1, int n2) {
        Random rand = new Random();
        return rand.nextBoolean() ? n1 : n2;
    }

    public static String strSQL(final String str) {
        return str.replaceAll("['\"\\\\%]", "\\\\$0");
    }

    public static int currentTimeSec() {
        return (int) System.currentTimeMillis() / 1000;
    }

    public static String replace(String text, String regex, String replacement) {
        return text.replace(regex, replacement);
    }

    public static boolean isTrue(int ratio, int typeRatio) {
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean isTrue(float ratio, int typeRatio) {
        if (ratio < 1) {
            ratio *= 10;
            typeRatio *= 10;
        }
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean haveSpecialCharacter(String text) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        boolean b = m.find();
        return b || text.contains(" ");
    }

    public static boolean canDoWithTime(long lastTime, long miniTimeTarget) {
        return System.currentTimeMillis() - lastTime > miniTimeTarget;
    }

    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
        'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
        'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
        'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
        'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
        'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
        'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
        'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
        'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
        'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
        'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
        'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
        'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
        'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
        'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
        'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
        'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
        'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
        'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
        'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
        'U', 'u', 'U', 'u',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    public static String generateRandomText(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                + "lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static Object[] addArray(Object[]... arrays) {
        if (arrays == null || arrays.length == 0) {
            return null;
        }
        if (arrays.length == 1) {
            return arrays[0];
        }
        Object[] arr0 = arrays[0];
        for (int i = 1; i < arrays.length; i++) {
            arr0 = ArrayUtils.addAll(arr0, arrays[i]);
        }
        return arr0;
    }

    public static Item petrandom(int tempId) {

        Item gapthuong = ItemService.gI().createNewItem((short) tempId);
        if (Util.isTrue(90, 100)) {
            switch (gapthuong.template.type) {
                case 11:
                case 72:
                case 21:
                    gapthuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 10)));
                    gapthuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 10)));
                    gapthuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 10)));
                    if (Util.isTrue(80, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    break;
                case 5:
                    gapthuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(22, 39)));
                    gapthuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(22, 39)));
                    gapthuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(22, 39)));
                    if (Util.isTrue(80, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    break;
                default:
                    gapthuong.itemOptions.add(new Item.ItemOption(30, 1));
                    break;
            }
        } else {
            switch (gapthuong.template.type) {
                case 11:
                case 72:
                case 21:
                    gapthuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(1000, 1200)));
                    gapthuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(1000, 1200)));
                    gapthuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(1000, 1200)));
                    if (Util.isTrue(80, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    break;
                case 5:
                    gapthuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(2200, 7700)));
                    gapthuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(2200, 7700)));
                    gapthuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(2200, 7700)));
                    if (Util.isTrue(80, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    break;
                default:
                    gapthuong.itemOptions.add(new Item.ItemOption(30, 1));
                    break;
            }
        }
        return gapthuong;
    }

    public static Item petccrandom(int tempId) {
        Item gapthuong = ItemService.gI().createNewItem((short) tempId);
        if (Util.isTrue(90, 100)) {
            switch (gapthuong.template.type) {
                case 11:
                case 72:
                case 21:
                    gapthuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(8, 12)));
                    gapthuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 12)));
                    gapthuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 12)));
                    if (Util.isTrue(99, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    if (Util.isTrue(50, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(Util.nextInt(94, 101), Util.nextInt(5, 20)));
                    }
                    break;
                case 5:
                    gapthuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(22, 35)));
                    gapthuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(22, 35)));
                    gapthuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(22, 35)));
                    if (Util.isTrue(99, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    if (Util.isTrue(50, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(Util.nextInt(94, 101), Util.nextInt(5, 20)));
                    }
                    break;
                default:
                    gapthuong.itemOptions.add(new Item.ItemOption(30, 1));
                    break;
            }
        } else {
            switch (gapthuong.template.type) {
                case 11:
                case 72:
                case 21:
                    gapthuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(2000, 3200)));
                    gapthuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(2000, 3200)));
                    gapthuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(2000, 3200)));
                    if (Util.isTrue(99, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    if (Util.isTrue(50, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(Util.nextInt(94, 101), Util.nextInt(5, 20)));
                    }
                    break;
                case 5:
                    gapthuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(2000, 9000)));
                    gapthuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(2000, 9000)));
                    gapthuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(2000, 9000)));
                    if (Util.isTrue(99, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    if (Util.isTrue(50, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(Util.nextInt(94, 101), Util.nextInt(5, 20)));
                    }
                    break;
                default:
                    gapthuong.itemOptions.add(new Item.ItemOption(30, 1));
                    break;
            }
        }
        return gapthuong;
    }

    public static Item petviprandom(int tempId) {
        Item gapthuong = ItemService.gI().createNewItem((short) tempId);
        if (Util.isTrue(90, 100)) {
            switch (gapthuong.template.type) {
                case 11:
                case 72:
                case 21:
                    gapthuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 15)));
                    gapthuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 15)));
                    gapthuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 15)));
                    if (Util.isTrue(99, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    if (Util.isTrue(50, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(Util.nextInt(94, 101), Util.nextInt(5, 20)));
                    }
                    break;
                case 5:
                    gapthuong.itemOptions.add(new Item.ItemOption(50, Util.nextInt(22, 42)));
                    gapthuong.itemOptions.add(new Item.ItemOption(103, Util.nextInt(22, 42)));
                    gapthuong.itemOptions.add(new Item.ItemOption(77, Util.nextInt(22, 42)));
                    if (Util.isTrue(99, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    if (Util.isTrue(50, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(Util.nextInt(94, 101), Util.nextInt(5, 20)));
                    }
                    break;
                default:
                    gapthuong.itemOptions.add(new Item.ItemOption(30, 1));
                    break;
            }
        } else {
            switch (gapthuong.template.type) {
                case 11:
                case 72:
                case 21:
                    gapthuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(2000, 4500)));
                    gapthuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(2000, 4500)));
                    gapthuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(2000, 4500)));
                    if (Util.isTrue(99, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    if (Util.isTrue(50, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(Util.nextInt(94, 101), Util.nextInt(5, 20)));
                    }
                    break;
                case 5:
                    gapthuong.itemOptions.add(new Item.ItemOption(0, Util.nextInt(2000, 10000)));
                    gapthuong.itemOptions.add(new Item.ItemOption(6, Util.nextInt(2000, 10000)));
                    gapthuong.itemOptions.add(new Item.ItemOption(7, Util.nextInt(2000, 10000)));
                    if (Util.isTrue(99, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 9)));
                    }
                    if (Util.isTrue(50, 100)) {
                        gapthuong.itemOptions.add(new Item.ItemOption(Util.nextInt(94, 101), Util.nextInt(5, 20)));
                    }
                    break;
                default:
                    gapthuong.itemOptions.add(new Item.ItemOption(30, 1));
                    break;
            }
        }
        return gapthuong;
    }
//khaile comment
//    public static ItemMap manhTS(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
//        return new ItemMap(zone, tempId, quantity, x, y, playerId);
//    }

    public static ItemMap ratiDTL(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(501) + 1300)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(2) + 15));
        }
        it.options.add(new Item.ItemOption(209, 1)); // đồ rơi từ boss
        it.options.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
        it.options.add(new Item.ItemOption(30, 1)); // ko thể gd
        if (Util.isTrue(90, 100)) {// tỉ lệ ra spl
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 1));
        } else if (Util.isTrue(4, 100)) {
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
        } else {
            it.options.add(new Item.ItemOption(107, new Random().nextInt(5) + 1));
        }
        return it;
    }

//    public static ItemMap SKH(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
//        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
//        List<Integer> ao = Arrays.asList(0, 1, 2);
//        List<Integer> quan = Arrays.asList(6, 7, 8);
//        List<Integer> gang = Arrays.asList(21, 22, 23);
//        List<Integer> giay = Arrays.asList(27, 28, 29);
//        int rada = 12;
//        if (ao.contains(tempId)) {
//
//            it.options.addAll(ItemService.gI().getListOptionItemShop((short) itemId));
//            it.options.add(new Item.ItemOption(skhId, 1));
//            it.options.add(new Item.ItemOption(optionIdSKH(skhId), 1));
//        }
//        if (quan.contains(tempId)) {
//            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 45)));
//        }
//        if (gang.contains(tempId)) {
//            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
//        }
//        if (giay.contains(tempId)) {
//            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 35)));
//        }
//        if (rada == tempId) {
//            it.options.add(new Item.ItemOption(14, new Random().nextInt(2) + 15));
//        }
//        it.options.add(new Item.ItemOption(30, 1)); // ko thể gd
//        if (Util.isTrue(90, 100)) {// tỉ lệ ra spl
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 1));
//        } else if (Util.isTrue(4, 100)) {
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
//        } else {
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(5) + 1));
//        }
//        return it;
//    }
    // public static List<Integer> listVPSK = Arrays.asList(1918, 1917, 886, 887, 888, 889);
//
//    public static int getRandomFromListSKtrungthu() {  
//        int tile_item1 = 10;
//        int tile_item2 = 20;
//        int tile_item3 = 40;
//        int tile_item4 = 40;
//        int tile_item5 = 5;
//        int tile_item6 = 5;
//        int tile = Util.nextInt(0, 100);
//        if (tile < tile_item1) {  
//            return listVPSK.get(0);
//        } else if (tile < tile_item2) {  
//            return listVPSK.get(1);
//        } else if (tile < tile_item3) {  
//            return listVPSK.get(2);
//        } else if (tile < tile_item4) {  
//            return listVPSK.get(3);
//        } else if (tile < tile_item5) {  
//            return listVPSK.get(4);
//        } else if (tile < tile_item6) {  
//            return listVPSK.get(5);
//        } else {  
//            return listVPSK.get(Util.nextInt(listVPSK.size()));
//        }
//    }
    public static ItemMap ItemRotTuQuai(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
         List<Integer> thucan = Arrays.asList(663, 664, 665, 666, 667);
        //List<Integer> itemhe2023 = Arrays.asList(2192, 2193, 2194, 2157, 2158, 2159, 2160, 695, 697, 698);
        //List<Integer> ca = Arrays.asList(1001, 1002, 1003, 1004, 1010, 1011, 1012);
        //List<Integer> itembuff = Arrays.asList(462, 569, 74, 191, 192, 211, 212);
        //List<Integer> chu = Arrays.asList(537, 538, 539, 540);
        //List<Integer> trungthu = Arrays.asList(886, 887, 888, 889, 1132, 1133);
        List<Integer> item = Arrays.asList(457, 17, 18, 19, 20);
        //List<Integer> tinhan = Arrays.asList(1314, 1315, 1316);
        //List<Integer> tinhthach = Arrays.asList(1360, 1361, 1362, 1363, 1364, 1365, 1366);
        int hm = 441;
        int hk = 442;
        int pst = 443;
        int xgc = 444;
        int xgcc = 445;
        int upvang = 446;
        int tnsm = 447;
//        if (tinhan.contains(tempId)) {
//            it.options.add(new Item.ItemOption(30, 3));//còn lại # ngày
//            it.options.add(new Item.ItemOption(93, 1));//còn lại # ngày
//        }
//        if (tinhthach.contains(tempId)) {
//            it.options.add(new Item.ItemOption(30, 3));//còn lại # ngày
//            it.options.add(new Item.ItemOption(93, 1));//còn lại # ngày
//        }
//        if (tinhan.contains(tempId)) {
//            it.options.add(new Item.ItemOption(30, 3));//còn lại # ngày
//            it.options.add(new Item.ItemOption(93, 1));//còn lại # ngày
//        }
//        if (chu.contains(tempId)) {
//            it.options.add(new Item.ItemOption(93, 10));//còn lại # ngày
//        }
//        if (trungthu.contains(tempId)) {
//            it.options.add(new Item.ItemOption(93, 3));//còn lại # ngày
//            it.options.add(new Item.ItemOption(30, 1));//còn lại # ngày
//        }
        if (thucan.contains(tempId)) {
            it.options.add(new Item.ItemOption(93, 20));//còn lại # ngày
        }
//        if (itemhe2023.contains(tempId)) {
//            it.options.add(new Item.ItemOption(93, 3));//còn lại # ngày
//            it.options.add(new Item.ItemOption(30, 1));//còn lại # ngày
//        }
//        if (ca.contains(tempId)) {
//            it.options.add(new Item.ItemOption(93, 10));//còn lại # ngày
//        }
//        if (itembuff.contains(tempId)) {
//            it.options.add(new Item.ItemOption(93, 10));//còn lại # ngày
//        }
        if (item.contains(tempId)) {
            it.options.add(new Item.ItemOption(93, 10));//
        }
        if (hm == tempId) {
            it.options.add(new Item.ItemOption(95, 5));
           
        }
        if (hk == tempId) {
            it.options.add(new Item.ItemOption(96, 5));
           
        }
        if (pst == tempId) {
            it.options.add(new Item.ItemOption(97, 5));
           
        }
        if (xgc == tempId) {
            it.options.add(new Item.ItemOption(98, 5));
            
        }
        if (xgcc == tempId) {
            it.options.add(new Item.ItemOption(99, 5));
           
        }
        if (upvang == tempId) {
            it.options.add(new Item.ItemOption(100, 5));
           
        }
        if (tnsm == tempId) {
            it.options.add(new Item.ItemOption(101, 5));
           
        }
//        it.options.add(new Item.ItemOption(208, 1)); // đồ rơi từ quái
//        it.options.add(new Item.ItemOption(21, 18)); // ycsm 18 tỉ
//        it.options.add(new Item.ItemOption(30, 1)); // ko thể gd
//        if (Util.isTrue(90, 100)) {// tỉ lệ ra spl
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 1));
//        } else if (Util.isTrue(4, 100)) {
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
//        } else {
//            it.options.add(new Item.ItemOption(107, new Random().nextInt(5) + 1));
//        }
        return it;
    }

    public static ItemMap RaitiDoc12(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(233, 237, 241);
        List<Integer> quan = Arrays.asList(245, 249, 253);
        List<Integer> gang = Arrays.asList(257, 261, 265);
        List<Integer> giay = Arrays.asList(269, 273, 277);
        int rd12 = 281;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(121) + 350)));//giáp 350-470
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(5) + 20)));//hp 20-24k
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(51) + 2200)));//2200-2250
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(4) + 20)));//20-23k ki
        }
        if (rd12 == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 10));//10-12cm
        }
        it.options.add(new Item.ItemOption(209, 1));//đồ rơi từ boss
        if (Util.isTrue(70, 100)) {// tỉ lệ ra spl 1-3 sao 70%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(1) + 3));
        } else if (Util.isTrue(4, 100)) {// tỉ lệ ra spl 5-7 sao 4%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
        } else {// tỉ lệ ra spl 1-5 sao 6%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(2) + 3));
        }
        return it;
    }

    public static ItemMap ratiItem(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.options.add(new Item.ItemOption(209, 1));
        it.options.add(new Item.ItemOption(21, 15));
        if (Util.isTrue(70, 100)) {// tỉ lệ ra spl 1-3 sao 70%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(1) + 3));
        } else if (Util.isTrue(1, 100)) {// tỉ lệ ra spl 5-7 sao 4%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(3) + 5));
        } else {// tỉ lệ ra spl 1-5 sao 6%
            it.options.add(new Item.ItemOption(107, new Random().nextInt(2) + 3));
        }
        if (Util.isTrue(1, 100)) {
            it.options.add(new Item.ItemOption(Util.nextInt(232, 235), new Random().nextInt(Util.nextInt(100, 120)) + Util.nextInt(232, 235)));
        }
        return it;
    }

    public static ItemMap ratiItemTL(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);

        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.options.add(new Item.ItemOption(47, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.options.add(new Item.ItemOption(22, highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.options.add(new Item.ItemOption(0, highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.options.add(new Item.ItemOption(23, highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.options.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.options.add(new Item.ItemOption(208, 1));// đồ rơi từ quái
        it.options.add(new Item.ItemOption(21, 15));
        if (Util.isTrue(1, 100)) {
            it.options.add(new Item.ItemOption(Util.nextInt(232, 235), new Random().nextInt(Util.nextInt(10, 25)) + Util.nextInt(20, 50)));
        }
        return it;
    }

    public static Item ratiItemTL(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.itemOptions.add(new Item.ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.itemOptions.add(new Item.ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.itemOptions.add(new Item.ItemOption(21, 15));
        return it;
    }

    public static ItemMap useItem(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, zone.map.yPhysicInTop(x, y - 24), playerId);
        List<Integer> tanjiro = Arrays.asList(1087, 1088, 1091, 1090);
        if (tanjiro.contains(tempId)) {
            it.options.add(new Item.ItemOption(77, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
            it.options.add(new Item.ItemOption(103, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
            it.options.add(new Item.ItemOption(50, highlightsItem(it.itemTemplate.gender == 3, new Random().nextInt(30) + 1)));
        }
        it.options.add(new Item.ItemOption(209, 1)); // đồ rơi từ boss
        it.options.add(new Item.ItemOption(30, 1)); // ko thể gd

        return it;
    }

    public static int highlightsItem(boolean highlights, int value) {
        double highlightsNumber = 1.1;
        return highlights ? (int) (value * highlightsNumber) : value;
    }

    public static Item sendDo(int itemId, int sql, List<Item.ItemOption> ios) {
//    InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
//    InventoryServiceNew.gI().sendItemBags(player);

        Item item = ItemService.gI().createNewItem((short) itemId);
        item.itemOptions.addAll(ios);
        item.itemOptions.add(new Item.ItemOption(107, sql));

        return item;
    }

    public static boolean checkDo(Item.ItemOption itemOption) {
        switch (itemOption.optionTemplate.id) {
            case 0:// tấn công
                if (itemOption.param > 12000) {
                    return false;
                }
                break;
            case 14:// chí mạng
                if (itemOption.param > 30) {
                    return false;
                }
                break;
            case 107:// spl
            case 102:// spl
                if (itemOption.param > 16) {
                    return false;
                }
                break;
            case 77:
            case 103:
            case 95:
            case 96:
                if (itemOption.param > 100) {
                    return false;
                }
                break;
            case 50:// sd 3%
                if (itemOption.param > 100) {
                    return false;
                }
                break;
            case 6:// hp
            case 7:// ki
                if (itemOption.param > 120000) {
                    return false;
                }
                break;
            case 47:// giáp
                if (itemOption.param > 35000) {
                    return false;
                }
                break;
            case 108:// giáp
                if (itemOption.param > 90) {
                    return false;
                }
                break;
        }
        return true;
    }

    public static void useCheckDo(Player player, Item item, String position) {
        try {
            if (item.template != null) {
                if (item.template.id >= 381 && item.template.id <= 385) {
                    return;
                }
                if (item.template.id >= 66 && item.template.id <= 135) {
                    return;
                }
                if (item.template.id >= 474 && item.template.id <= 515) {
                    return;
                }
                item.itemOptions.forEach(itemOption -> {
                    if (!Util.checkDo(itemOption)) {
                        Logger.error(player.name + "-" + item.template.name + "-" + position + "\n");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showListTop(Player player, byte select) {
//        List<TOP> tops = Manager.topSK;
//        switch (select) {
//            case 0:
//                tops = Manager.topSM;
//                break;
//            case 1:
//                tops = Manager.topNV;
//                break;
//            case 2:
//                tops = Manager.topSK;
//                break;
//            case 3:
//                tops = Manager.topPVP;
//                break;
//            case 4:
//                tops = Manager.topNHS;
//                break;
//        }
    }

    public static String phanthuong(int i) {
        switch (i) {
            case 1:
                return "5tr";
            case 2:
                return "3tr";
            case 3:
                return "1tr";
            default:
                return "100k";
        }
    }

//    public static int randomBossId() {
//        int bossId = Util.nextInt(10000);
//        while (BossManager.gI().getBossById(bossId) != null) {
//            bossId = Util.nextInt(10000);
//        }
//        return bossId;
//    }
//    public static int randomBossId() {
//        int bossId = Util.nextInt(10000);
//        while (BossManager.getInstance().getBossById(bossId) != null) {
//            bossId = Util.nextInt(10000);
//        }
//        return bossId;
//    }
//    public static int randomBossId() {
//        int bossId = Util.nextInt(-10000, - 300);
//        while (BossManager.gI().getBossById(bossId) != null) {
//            bossId = Util.nextInt(-10000, - 300);
//        }
//        return bossId;
//    }
    public static int randomBossId() {
        int bossId = Util.nextInt(-10000, - 300);
        while (BossManager.gI().getBossByType(bossId) != null) {
            bossId = Util.nextInt(-10000, - 300);
        }
        return bossId;
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e);
            }
        }).start();
    }

    public static long tinhLuyThua(int coSo, int soMu) {
        long ketQua = 1;

        for (int i = 0; i < soMu; i++) {
            ketQua *= coSo;
        }
        return ketQua;
    }

    public static void checkPlayer(Player player) {
        new Thread(() -> {
            List<Player> list = Client.gI().getPlayers().stream().filter(p -> !p.isPet && !p.isNewPet && !p.isMiniPet && p.getSession().userId == player.getSession().userId).collect(Collectors.toList());
            if (list.size() > 1) {
                list.forEach(pp -> Client.gI().kickSession(pp.getSession()));
                list.clear();
            }
        }).start();
    }
}
