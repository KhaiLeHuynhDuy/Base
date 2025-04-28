///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package nro.services;
//
//import nro.database.GirlkunDB;
//import nro.models.item.Item;
//import nro.models.player.Player;
//
///**
// *
// * @author Administrator
// */
//public class NapVangService {
//    
//    public static void ChonGiaTien(int chon, Player p) throws Exception{
//        switch (chon){
//            case 20: {//(20k)
//                if (p.session.vnd < 20) {
//                    Service.gI().sendThongBao(p, "Bạn phải có tối thiêu 20 Xu");
//                    return;
//                }
//                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
//                    Service.gI().sendThongBao(p, "Hành trang không đủ chỗ trống");
//                    return;
//                }
//                Item thoivang = ItemService.gI().createNewItem((short) 1998, 200);
//                if (thoivang != null) {
//                    p.session.vnd -= 20;
//                    InventoryServiceNew.gI().addItemBag(p, thoivang);
//                    InventoryServiceNew.gI().sendItemBags(p);
//                    GirlkunDB.executeUpdate("update account set vnd = '" + p.getSession().vnd + "' where id = " + p.getSession().userId);
//                    Service.gI().sendThongBao(p, "Bạn vừa đổi thành công 200 HRZ");
//
//                }
//                break;
//            }
//            case 50: {
//                if (p.session.vnd < 50) {
//                    Service.gI().sendThongBao(p, "Bạn phải có tối thiêu 50 Xu");
//                    return;
//                }
//                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
//                    Service.gI().sendThongBao(p, "Hành trang không đủ chỗ trống");
//                    return;
//                }
//                Item thoivang = ItemService.gI().createNewItem((short) 1998, 500);
//                if (thoivang != null) {
//                    p.session.vnd -= 50;
//                    InventoryServiceNew.gI().addItemBag(p, thoivang);
//                    InventoryServiceNew.gI().sendItemBags(p);
//                    GirlkunDB.executeUpdate("update account set vnd = '" + p.getSession().vnd + "' where id = " + p.getSession().userId);
//                    Service.gI().sendThongBao(p, "Bạn vừa đổi thành công 500 HRZ");
//
//                }
//                break;
//            }
//            case 100: {
//                if (p.session.vnd < 100) {
//                    Service.gI().sendThongBao(p, "Bạn phải có tối thiêu 500 Xu");
//                    return;
//                }
//                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
//                    Service.gI().sendThongBao(p, "Hành trang không đủ chỗ trống");
//                    return;
//                }
//                Item thoivang = ItemService.gI().createNewItem((short) 1998, 1000);
//                if (thoivang != null) {
//                    p.session.vnd -= 100;
//                    InventoryServiceNew.gI().addItemBag(p, thoivang);
//                    InventoryServiceNew.gI().sendItemBags(p);
//                    GirlkunDB.executeUpdate("update account set vnd = '" + p.getSession().vnd + "' where id = " + p.getSession().userId);
//                    Service.gI().sendThongBao(p, "Bạn vừa đổi thành công 1000 HRZ");
//
//                }
//                break;
//            }
//            case 500: {
//                if (p.session.vnd < 500) {
//                    Service.gI().sendThongBao(p, "Bạn phải có tối thiêu 500 Xu");
//                    return;
//                }
//                if (InventoryServiceNew.gI().getCountEmptyBag(p) == 0) {
//                    Service.gI().sendThongBao(p, "Hành trang không đủ chỗ trống");
//                    return;
//                }
//                Item thoivang = ItemService.gI().createNewItem((short) 1998, 6000);
//                if (thoivang != null) {
//                    p.session.vnd -= 500;
//                    InventoryServiceNew.gI().addItemBag(p, thoivang);
//                    InventoryServiceNew.gI().sendItemBags(p);
//                    GirlkunDB.executeUpdate("update account set vnd = '" + p.getSession().vnd + "' where id = " + p.getSession().userId);
//                    Service.gI().sendThongBao(p, "Bạn vừa đổi thành công 6000 HRZ");
//
//                }
//                break;
//            }
//        }
//    }
//}
