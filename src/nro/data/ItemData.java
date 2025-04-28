package nro.data;

import nro.models.Template.ItemOptionTemplate;
import nro.server.Manager;
import nro.network.io.Message;
import nro.server.io.MySession;
import nro.services.PlayerService;
import nro.utils.Logger;

public class ItemData {

    //------------------------------------------------------ start update client
    public static void updateItem(MySession session) {
        updateItemOptionItemplate(session);
        int count = 800;
//        updateItemTemplate(session, count);
//        updateItemTemplate(session, count, Manager.ITEM_TEMPLATES.size());

        updateItemTemplate(session, count);
        updateItemTemplate(session, count, Manager.ITEM_TEMPLATES.size());
        updateItem2Heads(session);
//        updateItemTemplate(session, 1500, Manager.ITEM_TEMPLATES.size());
    }

    private static void updateItemOptionItemplate(MySession session) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);
            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(0); //update option
            msg.writer().writeByte(Manager.ITEM_OPTION_TEMPLATES.size());
            for (ItemOptionTemplate io : Manager.ITEM_OPTION_TEMPLATES) {
                msg.writer().writeUTF(io.name);
                msg.writer().writeByte(io.type);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    private static void updateItemTemplate(MySession session, int count) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);

            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(1); //reload itemtemplate
            msg.writer().writeShort(count);
            for (int i = 0; i < count; i++) {
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).type);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).gender);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).name);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).description);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).level);
                msg.writer().writeInt(Manager.ITEM_TEMPLATES.get(i).strRequire);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).iconID);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).part);
                msg.writer().writeBoolean(Manager.ITEM_TEMPLATES.get(i).isUpToUp);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            ex.printStackTrace();

            // 0358124452
            // Logger.logException(ItemData.class, e);
        }
    }

    private static void updateItemTemplate(MySession session, int start, int end) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);

            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(2); //add itemtemplate
            msg.writer().writeShort(start);
            msg.writer().writeShort(end);
            for (int i = start; i < end; i++) {
//                System.out.println("start: " + start + " -> " + end + " id " + Manager.ITEM_TEMPLATES.get(i).id);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).type);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).gender);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).name);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).description);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).level);
                msg.writer().writeInt(Manager.ITEM_TEMPLATES.get(i).strRequire);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).iconID);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).part);
                msg.writer().writeBoolean(Manager.ITEM_TEMPLATES.get(i).isUpToUp);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            ex.printStackTrace();
            // 0358124452
            //Logger.logException(ItemData.class, e);
        }
    }
    //-------------------------------------------------------- end update client
    
    private static void updateItem2Heads(MySession session) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);

            msg.writer().writeByte(DataGame.vsItem);
            msg.writer().writeByte(100);

            msg.writer().writeShort(Manager.CAITRANG_2HEADS.size());
            for (int i = 0; i < Manager.CAITRANG_2HEADS.size(); i++) {
                msg.writer().writeByte(Manager.CAITRANG_2HEADS.get(i).head2.size() + 1);
                msg.writer().writeShort(Manager.CAITRANG_2HEADS.get(i).head);
                for (int j = 0; j < Manager.CAITRANG_2HEADS.get(i).head2.size(); j++) {
                    msg.writer().writeShort(Manager.CAITRANG_2HEADS.get(i).head2.get(j));
                }
            }
            
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DataGame.class, e, "updateItemTemplate 1");
        }
    }
}
