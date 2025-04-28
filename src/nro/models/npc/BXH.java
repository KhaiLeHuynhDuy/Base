/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.models.npc;

//import nro.database.GirlkunDB;
//import nro.result.GirlkunResultSet;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import nro.services.Service;
import java.util.ArrayList;
import java.util.Timer;

/**
 *
 * @author T
 */
public class BXH {
    public static ArrayList<Entry>[] bangXH = new ArrayList[11];
    public static ArrayList<Entry2> bangXHTopSM = new ArrayList<>();
    public static Timer t = new Timer(true);
    
     public static void init() {
        BXH.updateTopSM();
        for (int i = 0; i < BXH.bangXH.length; ++i) {
            BXH.bangXH[i] = new ArrayList<Entry>();
        }
        System.out.println("load BXH");
        for (int i = 0; i < BXH.bangXH.length; ++i) {
            initBXH(i);
        }
       
    }
     
    public static String getStringBXH(int type) {
        String str = "";
        switch (type) {
            case 0: {
                if (BXH.bangXH[type].isEmpty()) {
                    str = "Chưa có thông tin";
                    break;
                }
                if(BXH.bangXHTopSM.size() < 1) {
                    for (Entry bxh : BXH.bangXH[type]) {
                        str = str + bxh.index + ". " + bxh.name + ": " + ")\n";
                    }
                } else {
                    for (Entry2 bxh : BXH.bangXHTopSM) {
                        str = str + bxh.index + ". " + bxh.name + " ("+ Service.gI().get_HanhTinh(bxh.gender) +") đã đạt sức mạnh " + bxh.power + ".\n";
                    }
                }
                break;
            }
        }
        return str;
    }
     
    public static void updateTopSM() {
        BXH.bangXHTopSM.clear();
        GirlkunResultSet rs = null;
        try {          
            int i = 1;
            rs = GirlkunDB.executeQuery("select name, power, gender from player where power > 0 ORDER BY power DESC LIMIT 10;");
            Entry2 bXHTopSM;
            if (rs != null) {
                while (rs.next()) {
                    bXHTopSM = new Entry2();
                    bXHTopSM.name = rs.getString("name");
                    bXHTopSM.power = rs.getLong("power");
                    bXHTopSM.gender = rs.getInt("gender");
                    bXHTopSM.index = i;
                    bangXHTopSM.add(bXHTopSM);
                    i++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public static void initBXH(int type) {
        BXH.bangXH[type].clear();
        ArrayList<Entry> bxh = BXH.bangXH[type];
        switch (type) {
            case 0: {
                GirlkunResultSet rs = null;
                try {
                    int i = 1;
                    rs = GirlkunDB.executeQuery("select name, power, gender from player where power > 0 ORDER BY power DESC LIMIT 10;");
                    Entry bXHE0;
                    String name;
                    long power;
                    int gender;
                    if (rs != null) {
                        while (rs.next()) {
                            name = rs.getString("name");
                            power = rs.getLong("power");
                            gender = rs.getInt("gender");
                            bXHE0 = new Entry();
                            bXHE0.nXH = new long[3];
                            bXHE0.name = name;
                            bXHE0.index = i;
                            bXHE0.nXH[0] = power;
                            bXHE0.nXH[2] = gender;
                            bxh.add(bXHE0);
                            i++;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    
    public static class Entry
    {
        int index;
        String name;
        long[] nXH;
    }

    public static class Entry2 {  
        int index;
        String name;
        long power;
        int gender;
    }
}
