package nro.models.boss;

import lombok.Builder;
import lombok.Data;
import nro.models.player.Player;

@Data

public class BossData {

    public static final int DEFAULT_APPEAR = 0;
    public static final int APPEAR_WITH_ANOTHER = 1;
    public static final int ANOTHER_LEVEL = 2;

    private String name;

    private byte gender;

    private short[] outfit;

    private long dame;

    private long[] hp;

    private int[] mapJoin;

    private int[][] skillTemp;

    private String[] textS;

    private String[] textM;

    private String[] textE;

    private int secondsRest;

    private TypeAppear typeAppear;

    private int[] bossesAppearTogether;

    private int secondsNotify;

    private int[] groupBoss;

    private BossData(String name, byte gender, short[] outfit, long dame, long[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE) {
        this.name = name;
        this.gender = gender;
        this.outfit = outfit;
        this.dame = dame;
        this.hp = hp;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.textS = textS;
        this.textM = textM;
        this.textE = textE;
        this.secondsRest = 0;
        this.typeAppear = TypeAppear.DEFAULT_APPEAR;
    }

    public BossData(String name, byte gender, short[] outfit, long dame, long[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE);
        this.secondsRest = secondsRest;
    }

    public BossData(String name, byte gender, short[] outfit, long dame, long[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest, int secondsNotify) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE);
        this.secondsRest = secondsRest;
        this.secondsNotify = secondsNotify;
    }

    public BossData(String name, byte gender, short[] outfit, long dame, long[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest, int secondsNotify, int[] bossesAppearTogether) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE, secondsRest, secondsNotify);
        this.bossesAppearTogether = bossesAppearTogether;
    }

    public BossData(String name, byte gender, short[] outfit, long dame, long[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, TypeAppear typeAppear) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE);
        this.typeAppear = typeAppear;
    }

    public BossData(String name, byte gender, short[] outfit, long dame, long[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest, int secondsNotify, TypeAppear typeAppear) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE, secondsRest, secondsNotify);
        this.typeAppear = typeAppear;
    }

    public BossData(String name, byte gender, short[] outfit, long dame, long[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest, TypeAppear typeAppear, int secondsNotify) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE, secondsRest);
        this.typeAppear = typeAppear;
        this.secondsNotify = secondsNotify;
    }

    public BossData(String name, byte gender, short[] outfit, long dame, long[] hp,
            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
            String[] textE, int secondsRest, TypeAppear typeAppear, int secondsNotify, int[] bosses) {
        this(name, gender, outfit, dame, hp, mapJoin, skillTemp, textS, textM, textE, secondsRest);
        this.typeAppear = typeAppear;
        this.secondsNotify = secondsNotify;
        this.groupBoss = bosses;
    }

    @Builder
    public BossData(String name, byte gender, long dame, long[] hp,
            short[] outfit, int[] mapJoin, int[][] skillTemp,
            int secondsRest, String[] textS, String[] textM,
            String[] textE) {
        this.name = name;
        this.gender = gender;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.textS = new String[]{};
        this.textM = new String[]{};
        this.textE = new String[]{};
    }

//    public BossData(String name, byte gender, short[] outfit, Player player, long[] hp,
//            int[] mapJoin, int[][] skillTemp, String[] textS, String[] textM,
//            String[] textE, int secondsRest, int secondsNotify) {
//        this.name = name;
//        this.gender = gender;
//        this.outfit = outfit;
//        this.dame = (long) (player.nPoint.hpMax * 0.2); // Dame bằng 20% HP của người chơi
//        this.hp = hp;
//        this.mapJoin = mapJoin;
//        this.skillTemp = skillTemp;
//        this.textS = textS;
//        this.textM = textM;
//        this.textE = textE;
//        this.secondsRest = secondsRest;
//        this.secondsNotify = secondsNotify;
//    }

}
