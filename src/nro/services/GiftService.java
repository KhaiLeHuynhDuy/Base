package nro.services;

import nro.data.ItemData;
import nro.models.item.Item;
import nro.models.player.Player;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class GiftService {

    private static GiftService i;

    private GiftService() {

    }
    public String code;
    public int idGiftcode;
    public int gold;
    public int gem;
    public int dayexits;
    public Timestamp timecreate;
    public ArrayList<Item> listItem = new ArrayList<>();
    public static ArrayList<GiftService> gifts = new ArrayList<>();

    public static GiftService gI() {
        if (i == null) {
            i = new GiftService();
        }
        return i;
    }

    public void giftCode(Player player, String code) {

    }

}
