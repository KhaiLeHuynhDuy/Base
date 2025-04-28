
package nro.models.boss;

public class GBoss {

    private static GBoss i;

    public static GBoss gI() {
        if (i == null) {
            i = new GBoss();
        }
        return i;
    }

    public BossData getBossByType(int type) {
//        switch (type) {
//            case BossType.ANDROID_15:
//                return BossesData.ANDROID_15;
//        }
        return null;
    }
    

}
