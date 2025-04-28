package nro.consts;

import nro.models.boss.BossType;
import java.util.*;

/**
 *
 * @Edit By EMTI
 *
 */
public class ConstMap {

    public static final int TILE_TOP = 2;

    //type map
    public static final byte MAP_NORMAL = 0;
    public static final byte MAP_OFFLINE = 1;
    public static final byte MAP_DOANH_TRAI = 2;
    public static final byte MAP_BLACK_BALL_WAR = 3;
    public static final byte MAP_BAN_DO_KHO_BAU = 4;
    public static final byte MAP_MA_BU = 5;
    public static final byte MAP_KHI_GAS = 6;
    public static final byte MAP_CON_DUONG_RAN_DOC = 8;
    public static final byte MAP_GIAI_DAU = 20;

    //type change map
    public static final int CHANGE_CAPSULE = 500;
    public static final int CHANGE_BLACK_BALL = 501;
    public static final int CHANGE_MAP_MA_BU = 502;
    public static final int CHANGE_GIAI_DAU_BALL = 503;
    public static final int TIME_START_SUPPORT = 21;
    public static final int TIME_END_SUPPORT = 21;
    public static final List<Integer> LIST_NV_SUPPORT = Arrays.asList(BossType.KUKU, BossType.MAP_DAU_DINH, BossType.RAMBO, BossType.TDST, BossType.FIDE, BossType.ANDROID_19, BossType.DR_KORE, BossType.ANDROID_13,
            BossType.ANDROID_14, BossType.ANDROID_15, BossType.PIC, BossType.POC, BossType.KING_KONG);
    public static final List<Integer> LIST_NV_FIDE = Arrays.asList();

    public static final int RANGE_VE_TINH = 150;
    public static final int TIME_HOI_VE_TINH = 5000;
    public static final int PERCENT_VE_TINH_TRI_LUC = 20;
    public static final int PERCENT_VE_TINH_TRI_TUE = 20;
    public static final int PERCENT_VE_TINH_SINH_LUC = 20;
    public static final int PERCENT_VE_TINH_PHONG_THU = 20;

}
