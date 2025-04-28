package nro.consts;

/**
 *
 * @Edit By EMTI
 *
 */
public class ConstPlayer {

    public static final int[] HEADMONKEY = {192, 195, 196, 199, 197, 200, 198};
//    public static final int[] HEADBIENHINH = {192, 195, 196, 199, 197, 200, 198};

    public static final int[][] HEADBIENHINH = {
        {1665, 1667, 1669, 1672, 1675, 1675, 1675}, // TD
        {1700, 1701, 1702, 1703, 1704, 1704, 1704}, // NM
        {1678, 1680, 1683, 1685, 1687, 1687, 1687} // XD
    };

    public static final int[][] BODYBIENHINH = {
        {1661, 1661, 1661, 1661, 1661, 1661, 1661},
        {1698, 1698, 1698, 1698, 1698, 1698, 1698},
        {1663, 1663, 1663, 1663, 1659, 1659, 1659}
    };

    public static final int[][] LEGBIENHINH = {
        {1662, 1662, 1662, 1662, 1662, 1662, 1662},
        {1699, 1699, 1699, 1699, 1699, 1699, 1699},
        {1664, 1664, 1664, 1664, 1660, 1660, 1660}
    };

    public static final byte TRAI_DAT = 0;
    public static final byte NAMEC = 1;
    public static final byte XAYDA = 2;

    //type pk
    public static final byte NON_PK = 0;
    public static final byte PK_PVP = 3;
    public static final byte PK_ALL = 5;

    //type fushion
    public static final byte NON_FUSION = 0;
    public static final byte LUONG_LONG_NHAT_THE = 4;
    public static final int HOP_THE_PORATA = 6;
    public static final int HOP_THE_PORATA2 = 8;

    public static final int HOP_THE_PORATA3 = 10;
    public static final int HOP_THE_PORATA4 = 12;
    public static final int HOP_THE_PORATA5 = 14;
    public static final int HOP_THE_PORATA6 = 16;

    public static byte NAMEC1;

}
