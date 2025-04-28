package nro.data;

import encrypt.IconEncrypt;
import encrypt.ImageUtil;
import static encrypt.ImageUtil.encryptImage;
import static encrypt.ImageUtil.encryptString;
import static encrypt.ImageUtil.generateRandomKey;
import nro.models.Template.HeadAvatar;
import nro.models.Template.MapTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nro.utils.FileIO;
import nro.services.Service;
import nro.models.skill.NClass;
import nro.models.skill.Skill;
import nro.models.Template.MobTemplate;
import nro.models.Template.NpcTemplate;
import nro.models.Template.SkillTemplate;
import nro.network.session.ISession;
import nro.network.io.Message;
import nro.server.Manager;
import nro.server.io.MySession;
import nro.utils.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DataGame {

    public static byte vsData = 22;
    public static byte vsMap = 7;
    public static byte vsSkill = 5;
    public static byte vsItem = 85;
//    public static byte vsData = 22;
//    public static byte vsMap = 7;
//    public static byte vsSkill = 6;
//    public static byte vsItem = 50;
//    public static int vsRes = 752011;
    public static int vsRes = 752035;

    public static String LINK_IP_PORT = "NR 5.1:127.0.0.1:65143:0";
    private static final String MOUNT_NUM = "733:1,734:2,735:3,743:4,744:5,746:6,795:7,849:8,897:9,920:10";
    public static final Map MAP_MOUNT_NUM = new HashMap();

    static {
        String[] array = MOUNT_NUM.split(",");
        for (String str : array) {
            String[] data = str.split(":");
            short num = (short) (Short.parseShort(data[1]) + 30000);
            MAP_MOUNT_NUM.put(data[0], num);
        }
    }

    private DataGame() {

    }

    public static void sendVersionGame(MySession session) {
        Message msg;
        try {
            msg = Service.gI().messageNotMap((byte) 4);
            msg.writer().writeByte(vsData);
            msg.writer().writeByte(vsMap);
            msg.writer().writeByte(vsSkill);
            msg.writer().writeByte(vsItem);
            msg.writer().writeByte(0);

            long[] smtieuchuan = {1000L, 3000L, 15000L, 40000L, 90000L, 170000L, 340000L, 700000L,
                1500000L, 15000000L, 150000000L, 1500000000L, 5000000000L, 10000000000L, 40000000000L,
                50010000000L, 60010000000L, 70010000000L, 80010000000L, 100010000000L};
            msg.writer().writeByte(smtieuchuan.length);
            for (int i = 0; i < smtieuchuan.length; i++) {
                msg.writer().writeLong(smtieuchuan[i]);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //vcData
    public static void updateData(MySession session) {
        System.out.println("update data");
        byte[] dart = FileIO.readFile("data/girlkun/update_data/dart");
        byte[] arrow = FileIO.readFile("data/girlkun/update_data/arrow");
        byte[] effect = FileIO.readFile("data/girlkun/update_data/effect");
        byte[] image = FileIO.readFile("data/girlkun/update_data/image");
        byte[] part = FileIO.readFile("data/girlkun/update_data/part");
        byte[] skill = FileIO.readFile("data/girlkun/update_data/skill");

        Message msg;
        try {
            msg = new Message(-87);
            msg.writer().writeByte(vsData);
            msg.writer().writeInt(dart.length);
            msg.writer().write(dart);
            msg.writer().writeInt(arrow.length);
            msg.writer().write(arrow);
            msg.writer().writeInt(effect.length);
            msg.writer().write(effect);
            msg.writer().writeInt(image.length);
            msg.writer().write(image);
            msg.writer().writeInt(part.length);
            msg.writer().write(part);
            msg.writer().writeInt(skill.length);
            msg.writer().write(skill);

            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //vcMap
    public static void updateMap(MySession session) {
        Message msg;
        try {
            msg = Service.gI().messageNotMap((byte) 6);
            msg.writer().writeByte(vsMap);
            msg.writer().writeByte(Manager.MAP_TEMPLATES.length);
            for (MapTemplate temp : Manager.MAP_TEMPLATES) {
                msg.writer().writeUTF(temp.name);
            }
            msg.writer().writeByte(Manager.NPC_TEMPLATES.size());
            for (NpcTemplate temp : Manager.NPC_TEMPLATES) {
                msg.writer().writeUTF(temp.name);
                msg.writer().writeShort(temp.head);
                msg.writer().writeShort(temp.body);
                msg.writer().writeShort(temp.leg);
                msg.writer().writeByte(0);
            }
            msg.writer().writeByte(Manager.MOB_TEMPLATES.size());
            for (MobTemplate temp : Manager.MOB_TEMPLATES) {
                msg.writer().writeByte(temp.type);
                msg.writer().writeUTF(temp.name);
                msg.writer().writeInt(temp.hp);
                msg.writer().writeByte(temp.rangeMove);
                msg.writer().writeByte(temp.speed);
                msg.writer().writeByte(temp.dartType);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    //vcSkill
    public static void updateSkill(MySession session) {
        System.out.println("update skill");
        Message msg;
        try {
            msg = new Message(-28);

            msg.writer().writeByte(7);
            msg.writer().writeByte(vsSkill);
            msg.writer().writeByte(0); //count skill option

            msg.writer().writeByte(Manager.NCLASS.size());
            for (NClass nClass : Manager.NCLASS) {
                msg.writer().writeUTF(nClass.name);

                msg.writer().writeByte(nClass.skillTemplatess.size());
                for (SkillTemplate skillTemp : nClass.skillTemplatess) {
                    msg.writer().writeByte(skillTemp.id);
                    msg.writer().writeUTF(skillTemp.name);
                    msg.writer().writeByte(skillTemp.maxPoint);
                    msg.writer().writeByte(skillTemp.manaUseType);
                    msg.writer().writeByte(skillTemp.type);
                    msg.writer().writeShort(skillTemp.iconId);
                    msg.writer().writeUTF(skillTemp.damInfo);
                    msg.writer().writeUTF("Girlkun75");
                    if (skillTemp.id != 0) {
                        msg.writer().writeByte(skillTemp.skillss.size());
                        for (Skill skill : skillTemp.skillss) {
                            msg.writer().writeShort(skill.skillId);
                            msg.writer().writeByte(skill.point);
                            msg.writer().writeLong(skill.powRequire);
                            msg.writer().writeShort(skill.manaUse);
                            msg.writer().writeInt(skill.coolDown);
                            msg.writer().writeShort(skill.dx);
                            msg.writer().writeShort(skill.dy);
                            msg.writer().writeByte(skill.maxFight);
                            msg.writer().writeShort(skill.damage);
                            msg.writer().writeShort(skill.price);
                            msg.writer().writeUTF(skill.moreInfo);
                        }
                    } else {
                        //Thêm 2 skill trống 105, 106
                        msg.writer().writeByte(skillTemp.skillss.size() + 2);
                        for (Skill skill : skillTemp.skillss) {
                            msg.writer().writeShort(skill.skillId);
                            msg.writer().writeByte(skill.point);
                            msg.writer().writeLong(skill.powRequire);
                            msg.writer().writeShort(skill.manaUse);
                            msg.writer().writeInt(skill.coolDown);
                            msg.writer().writeShort(skill.dx);
                            msg.writer().writeShort(skill.dy);
                            msg.writer().writeByte(skill.maxFight);
                            msg.writer().writeShort(skill.damage);
                            msg.writer().writeShort(skill.price);
                            msg.writer().writeUTF(skill.moreInfo);
                        }
                        for (int i = 105; i <= 106; i++) {
                            msg.writer().writeShort(i);
                            msg.writer().writeByte(0);
                            msg.writer().writeLong(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeInt(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeByte(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeShort(0);
                            msg.writer().writeUTF("");
                        }
                    }
                }
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    public static void sendDataImageVersion(MySession session) {
        Message msg;
        try {
            msg = new Message(-111);
            msg.writer().write(FileIO.readFile("data/girlkun/data_img_version/x" + session.zoomLevel + "/img_version"));
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    //--------------------------------------------------------------------------
    public static void sendEffectTemplate(MySession session, int id) {

        Message msg;
        try {

            byte[] eff_data = FileIO.readFile("data/girlkun/effdata/x" + session.zoomLevel + "/" + id);
            msg = new Message(-66);
            msg.writer().write(eff_data);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void effData(MySession session, int id, int... idtemp) {
        int idT = id;
//        if (idtemp.length > 0 && idtemp[0] != 0) {
//            idT = idtemp[0];
//        }
//        if (id == 25) {
//            if (session.player != null && session.player.zone != null) {
//                byte effDragon = session.player.zone.effDragon;
//                if (effDragon != -1) {
//                    idT = effDragon;
//                    if (idT == 60) {
//                        if (session.version != 220) {
//                            idT = 61;
//                        }
//                    }
//                }
//            }
//        }
        Message msg;
        try {
//            byte[] effData = FileIO.readFile("data/girlkun/effect/x" + session.zoomLevel + "/data/DataEffect_" + idT);
//            byte[] effImg = FileIO.readFile("data/girlkun/effect/x" + session.zoomLevel + "/img/ImgEffect_" + idT + ".png");

            byte[] effData = FileIO.readFile("data/girlkun/effect/x" + session.zoomLevel + "/Data/" + idT + (idT == 60 || idT == 62 || idT == 63 || idT == 65 || idT == 70 ? "_2" : idT >= 201 ? "" : "_0"));
            byte[] effImg = FileIO.readFile("data/girlkun/effect/x" + session.zoomLevel + "/Image/ImgEffect_" + idT + ".png");

            msg = new Message(-66);
            msg.writer().writeShort(id);
            msg.writer().writeInt(effData.length);
            msg.writer().write(effData);
//            msg.writer().writeByte(0);
            msg.writer().writeByte(idT == 60 || idT == 62 || idT == 63 || idT == 65 || idT == 70 ? 2 : 0);
            msg.writer().writeInt(effImg.length);
            msg.writer().write(effImg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
//                e.printStackTrace();
        }
    }

    public static void sendItemBGTemplate(MySession session, int id) {
        Message msg;
        try {
            byte[] bg_temp = FileIO.readFile("data/girlkun/item_bg_temp/x" + session.zoomLevel + "/" + id + ".png");

            msg = new Message(-32);
            msg.writer().writeShort(id);
            if (bg_temp != null) {
                msg.writer().writeInt(bg_temp.length);
                msg.writer().write(bg_temp);
            }
            session.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    public static void sendDataItemBG(MySession session) {
        Message msg;
        try {
            byte[] item_bg = FileIO.readFile("data/girlkun/item_bg_temp/item_bg_data");
            msg = new Message(-31);
            msg.writer().write(item_bg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendIcon(MySession session, int id) {
        Message msg;
        try {
            byte[] icon = FileIO.readFile("data/girlkun/icon/x" + session.zoomLevel + "/" + id + ".png");
            msg = new Message(-67);
            msg.writer().writeInt(id);
            msg.writer().writeInt(icon.length);
            msg.writer().write(icon);
            Manager.SMALL_VERSION_DATA[session.zoomLevel - 1][id] = (byte) (icon.length % 127);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
//    
//        public static void sendIcon(MySession session, int id) {
//        Message msg;
//        try {
//            byte zoomLevel = session.zoomLevel;
//            IconEncrypt icon = ImageUtil.ICON_IMAGE.get(zoomLevel).getOrDefault(id, null);
//            if (icon == null) {
//                return;
//            }
//            msg = new Message(-67);
//            msg.writer().writeInt(id);
//            msg.writer().writeUTF(icon.keyDecryptImage);
//            msg.writer().writeInt(icon.dataImageEncrypt.length);
//            msg.writer().write(icon.dataImageEncrypt);
//            session.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//        }
//    }

    public static void sendSmallVersion(MySession session) {
        try {
            byte[][] smallVersion = Manager.SMALL_VERSION_DATA;
            byte[] data = smallVersion[session.zoomLevel - 1];
            Message ms = new Message(-77);
            ms.writer().writeShort(data.length);
            ms.writer().write(data);
            session.sendMessage(ms);
            ms.cleanup();
        } catch (IOException e) {
            Logger.logException(DataGame.class, e, "sendSmallVersion");
        }
    }
//  public static void sendSmallVersion(MySession session) {
//    Message msg;
//    try {
//        msg = new Message(-77);
//        byte[] data = FileIO.readFile("data/girlkun/data_img_version/x" + session.zoomLevel + "/img_version");
//        if (data != null) {
//            msg.writer().write(data);
//            session.sendMessage(msg);
//        } else {
//            System.err.println("Cannot read file: data/girlkun/data_img_version/x" + session.zoomLevel + "/img_version");
//        }
//        msg.cleanup();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}

    private static List<Integer> list = new ArrayList<>();

    public static void requestMobTemplate(MySession session, int id) {
        Message msg;
        try {
            byte[] mob = FileIO.readFile("data/girlkun/mob/x" + session.zoomLevel + "/" + id);

            if (mob == null) {
                System.err.println("Cannot read file: data/girlkun/mob/x" + session.zoomLevel + "/" + id);
                return;
            }

            msg = new Message(11);
            if (id != 84 && id != 85 && id != 93 && id != 88 && id != 89 && id != 94) {
                msg.writer().writeByte(id);
            }
            if (id >= 95 && id <= 98) {
                msg.writer().writeByte(0);
            }

            msg.writer().write(mob);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTileSetInfo(MySession session) {
        Message msg;
        try {
            msg = new Message(-82);
            msg.writer().write(FileIO.readFile("data/girlkun/map/tile_set_info"));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mainz(String[] args) {
        try {
            File folder = new File("data/girlkun/map/tile_map_data");
            for (File f : folder.listFiles()) {
                if (f.getName().equals("5")) {
                    DataInputStream dis = new DataInputStream(new FileInputStream(f));
                    int w = dis.readByte();
                    int h = dis.readByte();
                    for (int i = 0; i < h; i++) {
                        for (int j = 0; j < w; j++) {
                            System.out.print(dis.readByte() + " maizzzzz");
                        }
//                        System.out.println("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mainzz(String[] args) throws Exception {
        List<String> list = new ArrayList<>();
        File folder = new File("C:\\Users\\khail\\Documents\\Main_Src_NRO (Not Delete)\\SRC_BASE\\BASE\\data\\girlkun\\map\\tile_map_data");
//        for (File f : folder.listFiles()) {
//            list.add(f.getName());
//        }
        folder = new File("C:\\Users\\khail\\Documents\\Main_Src_NRO (Not Delete)\\SRC_BASE\\BASE\\data\\girlkun\\map\\tile_map_dataz");
        for (File f : folder.listFiles()) {
            if (!list.contains(f.getName())) {
                DataInputStream dis = new DataInputStream(new FileInputStream(f));
                dis.readByte();
                int w = dis.readByte();
                int h = dis.readByte();
                byte[] data = new byte[w * h];
                for (int i = 0; i < data.length; i++) {
                    data[i] = dis.readByte();
                }
                dis.close();
                DataOutputStream dos = new DataOutputStream(new FileOutputStream("C:\\Users\\admin\\Desktop\\cbro\\data\\girlkun\\map\\tile_map_data\\" + f.getName()));
                dos.writeByte(w);
                dos.writeByte(h);
                for (int i = 0; i < data.length; i++) {
                    System.out.print(data[i] + ",");
                    dos.writeByte(data[i]);
                }
                dos.flush();
                dos.close();
            }
            System.out.println("-----------------------------------------");
        }
        System.out.println("done");
    }

    //data vẽ map
    public static void sendMapTemp(MySession session, int id) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(10);
            msg.writer().write(FileIO.readFile("data/girlkun/map/tile_map_data/" + id));
//            byte[] fileData = FileIO.readFile("data/girlkun/map/tile_map_data/" + id);
//            if (fileData != null) {
//            msg.writer().write(fileData);
//            } else {
//                Logger.log("Error reading map file for id: " + id);
//            }

            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    //head-avatar
    public static void sendHeadAvatar(Message msg) {
        try {
            msg.writer().writeShort(Manager.HEAD_AVATARS.size());
            for (HeadAvatar ha : Manager.HEAD_AVATARS) {
                msg.writer().writeShort(ha.headId);
                msg.writer().writeShort(ha.avatarId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendImageByName(MySession session, String imgName) {
        Message msg;
        try {
            msg = new Message(66);
            msg.writer().writeUTF(imgName);
            msg.writer().writeByte(Manager.getNFrameImageByName(imgName));
            byte[] data = FileIO.readFile("data/girlkun/img_by_name/x" + session.zoomLevel + "/" + imgName + ".png");
            if (data != null) {
                msg.writer().writeInt(data.length);
                msg.writer().write(data);
            }

            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void sendIcon(MySession session, int id) {
//        Message msg;
//        try {
//            byte zoomLevel = session.zoomLevel;
//            IconEncrypt icon = ImageUtil.ICON_IMAGE.get(zoomLevel).getOrDefault(id, null);
//            if (icon == null) {
//                String key = generateRandomKey(15);
//                //byte[] encrypt = encryptImage(new File(String.format("data/girlkun/icon/x%s/%s.png", zoomLevel, id)), key);
////                File imageFile = new File(String.format("C:/Users/Administrator/Desktop/Sever1/data/girlkun/icon/x%s/%s.png", zoomLevel, id));       
//                File imageFile = new File(String.format("data/girlkun/icon/x%s/%s.png", zoomLevel, id));
//                if (!imageFile.exists()) {
//                    System.err.println("Image file does not exist: " + imageFile.getPath());
//                    return;
//                }
//                byte[] encrypt = encryptImage(imageFile, key);
//                if (encrypt == null) {
//                    System.err.println("Failed to encrypt image: " + imageFile.getPath());
//                    return;
//                }
//                icon = new IconEncrypt();
//                icon.dataImageEncrypt = encrypt;
//                icon.keyDecryptImage = encryptString(key);
//                icon.keyOrigin = key;
//                ImageUtil.ICON_IMAGE.get(zoomLevel).put(id, icon);
//                Manager.SMALL_VERSION_DATA[session.zoomLevel - 1][id] = (byte) (encrypt.length % 127);
//            }
//            msg = new Message(-67);
//            msg.writer().writeInt(id);
//            msg.writer().writeUTF(icon.keyDecryptImage);
//            msg.writer().writeInt(icon.dataImageEncrypt.length);
//            msg.writer().write(icon.dataImageEncrypt);
//            session.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
//    }
//    //download data res --------------------------------------------------------
//
//    public static void sendVersionRes(ISession session) {
//        Message msg;
//        try {
//            msg = new Message(-74);
//            msg.writer().writeByte(0);
//            msg.writer().writeInt(vsRes);
//            msg.writer().writeUTF(ImageUtil.key);
//            session.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    download data res --------------------------------------------------------
    public static void sendVersionRes(ISession session) {
        Message msg;
        try {
            msg = new Message(-74);
            msg.writer().writeByte(0);
            msg.writer().writeInt(vsRes);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void sendSizeRes(MySession session) {
        Message msg;
        try {
            msg = new Message(-74);
            msg.writer().writeByte(1);
            msg.writer().writeShort(new File("data/girlkun/res/x" + session.zoomLevel).listFiles().length);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendRes(MySession session) {
        Message msg;
        try {
            for (final File fileEntry : new File("data/girlkun/res/x" + session.zoomLevel).listFiles()) {
                String original = fileEntry.getName();
                byte[] res = FileIO.readFile(fileEntry.getAbsolutePath());
                if (res == null) {
                    System.err.print("Null res for file: " + original);
                    return;
                }
                msg = new Message(-74);
                msg.writer().writeByte(2);
                msg.writer().writeUTF(original);
                msg.writer().writeInt(res.length);
                msg.writer().write(res);
                session.sendMessage(msg);
                msg.cleanup();
                Thread.sleep(10);
            }

            sendDataMucsic(session);

            msg = new Message(-74);
            msg.writer().writeByte(3);
            msg.writer().writeInt(vsRes);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e);
        }
    }

    public static void sendLinkIP(MySession session) {
        Message msg;
        try {
            msg = new Message(-29);
            msg.writer().writeByte(2);
            msg.writer().writeUTF(LINK_IP_PORT + ",0,0");
            msg.writer().writeByte(1);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * -server -Xms24G -Xmx24G -XX:PermSize=512m -XX:+UseG1GC
     * -XX:MaxGCPauseMillis=200 -XX:ParallelGCThreads=20 -XX:ConcGCThreads=5
     * -XX:InitiatingHeapOccupancyPercent=70
     */
    private static void sendDataMucsic(MySession session) {
        Message msg;
        try {
            File[] files = new File("data/girlkun/music").listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            for (final File fileEntry : files) {
                if (fileEntry.isDirectory()) {
                    continue;
                }
                if (!fileEntry.getName().endsWith(".ogg")) {
                    continue;
                }
                if (fileEntry.getName().startsWith(".")) {
                    continue;
                }
                byte[] res = FileIO.readFile(fileEntry.getAbsolutePath());
                if (res == null) {
                    continue;
                }

                msg = new Message(-74);
                msg.writer().writeByte(4);
                msg.writer().writeUTF(fileEntry.getName());
                msg.writer().writeInt(res.length);
                msg.writer().write(res);
                session.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            Logger.logException(DataGame.class, e, "sendDataMucsic");
        }
    }
}
