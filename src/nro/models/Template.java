package nro.models;

import nro.consts.ConstMap;
import nro.models.item.Item;
import nro.models.map.WayPoint;
import nro.models.skill.Skill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import nro.models.map.EffectMap;

public class Template {

    @Data
    public static class AchievementTemplate {

        int index;
        String info1;
        String info2;
        int count_Purpose;
        int gem;

        public AchievementTemplate() {

        }

        public AchievementTemplate(int index, String info1, String info2, int count, int gem) {
            this.index = index;
            this.info1 = info1;
            this.info2 = info2;
            this.count_Purpose = count;
            this.gem = gem;
        }

    }

    public static class ItemOptionTemplate {

        public int id;

        public String name;

        public int type;

        public ItemOptionTemplate() {
        }

        public ItemOptionTemplate(int id, String name, int type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }
    }

    public static class ItemTemplate {

        public short id;

        public byte type;

        public byte gender;

        public String name;

        public String description;

        public byte level;

        public short iconID;

        public short part;

        public boolean isUpToUp;

        public int strRequire;

        public int gold;

        public int gem;

        public int ruby;

        public int head;

        public List<Integer> head2 = new ArrayList<>();

        public int body;

        public int leg;

        public ItemTemplate() {
        }

        public ItemTemplate(short id, byte type, byte gender, String name, String description, short iconID, short part, short head, short body, short leg, boolean isUpToUp, int strRequire) {
            this.id = id;
            this.type = type;
            this.gender = gender;
            this.name = name;
            this.description = description;
            this.iconID = iconID;
            this.part = part;
            this.head = head;
//            this.head2 = head2;
            this.body = body;
            this.leg = leg;

            this.isUpToUp = isUpToUp;
            this.strRequire = strRequire;
        }
    }

    public static class MobTemplate {

        public int id;
        public byte type;
        public String name;
        public int hp;
        public byte rangeMove;
        public byte speed;
        public byte dartType;
        public byte percentDame;
        public byte percentTiemNang;
    }

    public static class NpcTemplate {

        public int id;
        public String name;
        public int head;
        public int head2;
        public int body;
        public int leg;
        public int avatar;
    }

    public static class MapTemplate {

        public int id;
        public String name;

        public byte type;
        public byte planetId;
        public byte bgType;
        public byte tileId;
        public byte bgId;

        public byte zones;
        public byte maxPlayerPerZone;
        public List<WayPoint> wayPoints;

        public byte[] mobTemp;
        public byte[] mobLevel;
        public long[] mobHp;
        public short[] mobX;
        public short[] mobY;

        public byte[] npcId;
        public short[] npcX;
        public short[] npcY;
        public short[] npcAvatar;
        public List<EffectMap> effectMaps;

        public MapTemplate() {
            this.wayPoints = new ArrayList<>();
            this.effectMaps = new ArrayList<>();
        }

        public boolean isMapOffline() {
            return this.type == ConstMap.MAP_OFFLINE;
        }
    }

    public static class SkillTemplate {

        public byte id;

        public int classId;

        public String name;

        public int maxPoint;

        public int manaUseType;

        public int type;

        public int iconId;

        public String[] description;

        public Skill[] skills;

        public List<Skill> skillss = new ArrayList<>();

        public String damInfo;
    }

    public static class Part {

        public int id;

        public int type;

        public List<PartDetail> partDetails;

        public Part() {
            this.partDetails = new ArrayList();
        }
    }

    public static class PartDetail {

        public short iconId;

        public byte dx;

        public byte dy;

        public PartDetail(short iconId, byte dx, byte dy) {
            this.iconId = iconId;
            this.dx = dx;
            this.dy = dy;
        }
    }

    public static class HeadAvatar {

        public int headId;

        public int avatarId;

        public HeadAvatar(int headId, int avatarId) {
            this.headId = headId;
            this.avatarId = avatarId;
        }
    }

    public static class FlagBag {

        public int id;
        public short iconId;
        public short[] iconEffect;
        public String name;
        public int gold;
        public int gem;
    }

    public static class ItemLuckyRound {

        public Template.ItemTemplate temp;
        public int ratio;
        public int typeRatio;
        public List<ItemOptionLuckyRound> itemOptions;

        public ItemLuckyRound() {
            this.itemOptions = new ArrayList<>();
        }
    }

    public static class ItemOptionLuckyRound {

        public Item.ItemOption itemOption;
        public int param1;
        public int param2;

        public ItemOptionLuckyRound() {
            this.param2 = -1;
        }
    }
}
