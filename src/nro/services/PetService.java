package nro.services;

import nro.consts.ConstPlayer;
import nro.consts.cn;
import nro.models.player.NewPet;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.services.func.ChangeMapService;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class PetService {

    private static PetService i;

    public static PetService gI() {
        if (i == null) {
            i = new PetService();
        }
        return i;
    }

    public void createNormalPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createNormalPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }

                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createMabuPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, true, false);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createMabuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Oa oa oa...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createBerusPet(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createBuuGay(Player player, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet5(player, false, true);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createBuuGay(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet5(player, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Hố Hố Hố Hố");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createBerusPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Thần hủy diệt hiện thân tất cả quỳ xuống...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeNormalPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, gender, limitPower);
    }

    public void changeNormalPet(Player player) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createNormalPet(player, limitPower);
    }

    public void changeMabuPet(Player player) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, limitPower);
    }

    public void changeMabuPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMabuPet(player, gender, limitPower);
    }

    public void changeBerusPet(Player player) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;

        createBerusPet(player, limitPower);
    }

    public void changeBerusPet(Player player, int gender) {
        byte limitPower = player.pet.nPoint.limitPower;
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Có lỗi xảy ra với đệ của bạn");
        }
        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusPet(player, gender, limitPower);
    }

    public void changeNamePet(Player player, String name) {
        try {
            if (!InventoryServiceNew.gI().isExistItemBag(player, 400)) {
                Service.gI().sendThongBao(player, "Bạn cần thẻ đặt tên đệ tử, mua tại Santa");
                return;
            } else if (Util.haveSpecialCharacter(name)) {
                Service.gI().sendThongBao(player, "Tên không được chứa ký tự đặc biệt");
                return;
            } else if (name.length() > 10) {
                Service.gI().sendThongBao(player, "Tên quá dài");
                return;
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.name = "$" + name.toLowerCase().trim();
            InventoryServiceNew.gI().subQuantityItemsBag(player, InventoryServiceNew.gI().findItemBag(player, 400), 1);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Service.gI().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã đặt cho con tên " + name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception ex) {

        }
    }

    private int[] getDataPetNormal() {
        int[] hpmp = {
            1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(20, 45); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDataPetMabu() {
        int[] hpmp = {
            1700, 1800, 1900, 2000, 2100, 2200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }

    private int[] getDeTuVip() {
        int[] hpmp = {
            2800, 8200};
        int[] petData = new int[5];
        petData[0] = Util.nextInt(40, 105) * 20; //hp
        petData[1] = Util.nextInt(40, 105) * 20; //mp
        petData[2] = Util.nextInt(50, 120); //dame
        petData[3] = Util.nextInt(9, 50); //def
        petData[4] = Util.nextInt(0, 2); //crit
        return petData;
    }
    // xên bọ hung type  = 4

    public void changeCellPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createCellPet(player, limitPower);
    }

    public void createCellPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, true, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Tao là CellPet...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //  cumber
    public void changeCumberPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createCumberPet(player, limitPower);
    }

    public void createCumberPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, false, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Tao là CumberPet...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //  fidegold
    public void changeFideGoldPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createFideGoldPet(player, limitPower);
    }

    public void createFideGoldPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet(player, false, false, false, false, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Tao là FideGold...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeHeartPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createHeartPet(player, limitPower);
    }

    public void createHeartPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet2(player, false, true, false, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Heart tối thượng là ta.......");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeMaiPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createMaiPet(player, limitPower);
    }

    public void createMaiPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet2(player, false, false, true, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Em là người yêu cuả anh nè !!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeGohanPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGohanPet(player, limitPower);
    }

    public void createGohanPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet2(player, false, false, false, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Hãy nhận con làm sư phụ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeJirenPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createJirenPet(player, limitPower);
    }

    public void createJirenPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet2(player, false, false, false, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Công lý là khi ta đủ sức mạnh ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeBlack3Pet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBlack3Pet(player, limitPower);
    }

    public void createBlack3Pet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet3(player, false, true, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Công lý là khi ta đủ sức mạnh ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeGoku4Pet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGoku4Pet(player, limitPower);
    }

    public void createGoku4Pet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet3(player, false, false, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Công lý là khi ta đủ sức mạnh ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeGokuUltraPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createGokuUltraPet(player, limitPower);
    }

    public void createGokuUltraPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet3(player, false, false, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Công lý là khi ta đủ sức mạnh ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeBerusBiNgoPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBerusBiNgoPet(player, limitPower);
    }

    public void createBerusBiNgoPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet4(player, false, true, false, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Công lý là khi ta đủ sức mạnh ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeZamasuPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createZamasuPet(player, limitPower);
    }

    public void createZamasuPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet4(player, false, false, true, false, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Công lý là khi ta đủ sức mạnh ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeDaishinkanPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createDaishinkanPet(player, limitPower);
    }

    public void createDaishinkanPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet4(player, false, false, false, true, false, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Công lý là khi ta đủ sức mạnh ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void changeBuuGay(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createBuuGay(player, limitPower);
    }

    public void changeWhisPet(Player player, int gender) {
        if (player.pet == null) {
            Service.gI().sendThongBao(player, "Bạn chưa có đệ tử để đổi");
            // Xử lý khi pet chưa được khởi tạo hoặc đã bị xóa bỏ
            // ...
            return;
        }

        byte limitPower = player.pet.nPoint.limitPower;

        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }

        ChangeMapService.gI().exitMap(player.pet);
        player.pet.dispose();
        player.pet = null;
        createWhisPet(player, limitPower);
    }

    public void createWhisPet(Player player, int gender, byte... limitPower) {
        new Thread(() -> {
            try {
                createNewPet4(player, false, false, false, false, true, (byte) gender);
                if (limitPower != null && limitPower.length == 1) {
                    player.pet.nPoint.limitPower = limitPower[0];
                }
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Công lý là khi ta đủ sức mạnh ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createGlanola(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Granlola";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 18;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createKamin(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Kamin";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 19;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createOren(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Oren";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 20;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createKaminOren(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ KamiOren";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 21;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createGojo(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Gojo";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 22;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Vào đây ta cân tất ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createHatchiyack(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ HatchiYack";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 23;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Quyền lực đều năm trong tay ta ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createUltraEgo(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Vegeta Ego";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 24;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Lòng kiêu hãnh của ta sẽ khiến người phải sợ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDrabura(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Drabura";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 25;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Nhổ bọt là nghề của ta rồi ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createGokuSSJ5(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Goku SSJ5";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 26;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ahihi ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createJirenBaby(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Jiren Nhỏ";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 27;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ahihi ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createBabyVegeta(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Baby Vegeta";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 28;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createZamas2(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Zamas Cải Cách";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 29;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createKafula(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Kakula";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 30;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createCumberBase(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Cumber Base";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 31;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createCumberSuper(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$Đệ Cumber Super";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 32;
                pet.nPoint.stamina = 1000;
                pet.nPoint.maxStamina = 1000;
                pet.nPoint.hpg = 4000;
                pet.nPoint.mpg = 4000;
                pet.nPoint.dameg = 300;
                pet.nPoint.defg = 150;
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(0);
                player.pointfusion.setMpFusion(0);
                player.pointfusion.setDameFusion(0);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDe1(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$ZAMASU dị";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 34;
                pet.nPoint.stamina = (short) 1000;
                pet.nPoint.maxStamina = (short) Util.nextInt(1000, 10000);
                pet.nPoint.hpg = Util.nextInt(4000, 10000);
                pet.nPoint.mpg = Util.nextInt(4000, 10000);
                pet.nPoint.dameg = Util.nextInt(300, 1000);
                pet.nPoint.defg = Util.nextInt(300, 1000);
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(Util.nextInt(100, 200));
                player.pointfusion.setMpFusion(Util.nextInt(100, 200));
                player.pointfusion.setDameFusion(Util.nextInt(100, 200));
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDe2(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$YACHIRO";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 35;
                pet.nPoint.stamina = (short) 1000;
                pet.nPoint.maxStamina = (short) Util.nextInt(1000, 10000);
                pet.nPoint.hpg = Util.nextInt(4000, 10000);
                pet.nPoint.mpg = Util.nextInt(4000, 10000);
                pet.nPoint.dameg = Util.nextInt(300, 1000);
                pet.nPoint.defg = Util.nextInt(300, 1000);
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;

                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(Util.nextInt(120, 220));
                player.pointfusion.setMpFusion(Util.nextInt(120, 220));
                player.pointfusion.setDameFusion(Util.nextInt(120, 220));
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDe3(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$OMEGA";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 36;
                pet.nPoint.stamina = (short) 1000;
                pet.nPoint.maxStamina = (short) Util.nextInt(1000, 10000);
                pet.nPoint.hpg = Util.nextInt(4000, 10000);
                pet.nPoint.mpg = Util.nextInt(4000, 10000);
                pet.nPoint.dameg = Util.nextInt(300, 1000);
                pet.nPoint.defg = Util.nextInt(300, 1000);
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;

                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(Util.nextInt(100, 200));
                player.pointfusion.setMpFusion(Util.nextInt(100, 200));
                player.pointfusion.setDameFusion(Util.nextInt(100, 200));
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDe4(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$GOKU_SSJ4";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 37;
                pet.nPoint.stamina = (short) 1000;
                pet.nPoint.maxStamina = (short) Util.nextInt(1000, 10000);
                pet.nPoint.hpg = Util.nextInt(4000, 10000);
                pet.nPoint.mpg = Util.nextInt(4000, 10000);
                pet.nPoint.dameg = Util.nextInt(300, 1000);
                pet.nPoint.defg = Util.nextInt(300, 1000);
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(Util.nextInt(140, 240));
                player.pointfusion.setMpFusion(Util.nextInt(140, 240));
                player.pointfusion.setDameFusion(Util.nextInt(140, 240));
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDe5(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$VEGETA_SSJ4";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 38;
                pet.nPoint.stamina = (short) 1000;
                pet.nPoint.maxStamina = (short) Util.nextInt(1000, 10000);
                pet.nPoint.hpg = Util.nextInt(4000, 10000);
                pet.nPoint.mpg = Util.nextInt(4000, 10000);
                pet.nPoint.dameg = Util.nextInt(300, 1000);
                pet.nPoint.defg = Util.nextInt(300, 1000);
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(Util.nextInt(120, 220));
                player.pointfusion.setMpFusion(Util.nextInt(120, 220));
                player.pointfusion.setDameFusion(Util.nextInt(120, 220));
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDe6(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$GOGETA_SSJ4";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 39;
                pet.nPoint.stamina = (short) 1000;
                pet.nPoint.maxStamina = (short) Util.nextInt(1000, 10000);
                pet.nPoint.hpg = Util.nextInt(4000, 10000);
                pet.nPoint.mpg = Util.nextInt(4000, 10000);
                pet.nPoint.dameg = Util.nextInt(300, 1000);
                pet.nPoint.defg = Util.nextInt(300, 1000);
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;

                player.pointfusion.setHpFusion(Util.nextInt(180, 250));
                player.pointfusion.setMpFusion(Util.nextInt(180, 250));
                player.pointfusion.setDameFusion(Util.nextInt(180, 250));
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDe7(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$TOPPO";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 40;
                pet.nPoint.stamina = (short) 1000;
                pet.nPoint.maxStamina = (short) Util.nextInt(1000, 10000);
                pet.nPoint.hpg = Util.nextInt(4000, 10000);
                pet.nPoint.mpg = Util.nextInt(4000, 10000);
                pet.nPoint.dameg = Util.nextInt(300, 1000);
                pet.nPoint.defg = Util.nextInt(300, 1000);
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(Util.nextInt(180, 280));
                player.pointfusion.setMpFusion(Util.nextInt(180, 280));
                player.pointfusion.setDameFusion(Util.nextInt(180, 280));
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createDe8(Player player, boolean isChange, byte gender) {
        byte limitPower;
        if (isChange) {
            limitPower = player.pet.nPoint.limitPower;
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                player.pet.unFusion();
            }
            ChangeMapService.gI().exitMap(player.pet);
            player.pet.dispose();
            player.pet = null;
        } else {
            limitPower = 1;
        }
        new Thread(() -> {
            try {
                Pet pet = new Pet(player);
                pet.name = "$ZENO";
                pet.gender = gender;
                pet.id = -player.id;
                pet.nPoint.power = 1500000;
                pet.typePet = 41;
                pet.nPoint.stamina = (short) 1000;
                pet.nPoint.maxStamina = (short) Util.nextInt(1000, 10000);
                pet.nPoint.hpg = Util.nextInt(4000, 10000);
                pet.nPoint.mpg = Util.nextInt(4000, 10000);
                pet.nPoint.dameg = Util.nextInt(1000, 2000);
                pet.nPoint.defg = Util.nextInt(3000, 10000);
                pet.nPoint.critg = 20;
                for (int i = 0; i < 7; i++) {
                    pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
                for (int i = 0; i < 4; i++) {
                    pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
                }
                pet.nPoint.setFullHpMp();
                player.pet = pet;
                ;
                player.pet.nPoint.limitPower = limitPower;
                player.pointfusion.setHpFusion(Util.nextInt(200, 300));
                player.pointfusion.setMpFusion(Util.nextInt(200, 300));
                player.pointfusion.setDameFusion(Util.nextInt(200, 300));
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "\b|1|Ta kẻ mạnh nhất vũ trụ ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void createNewPet(Player player, boolean isMabu, boolean isBerus, boolean isPic, boolean isCell, boolean isCumber, boolean isFideGold, byte... gender) {
        int[] data = isMabu ? getDataPetMabu() : getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabư" : isBerus ? "Berus" : isPic ? "Pic" : isCell ? "Xên" : isCumber ? "Cumber" : isFideGold ? "Fide Gold" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.gender = player.gender;
        pet.nPoint.power = isMabu || isBerus || isPic ? 1500000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isBerus ? 2 : isPic ? 3 : isCell ? 4 : isCumber ? 5 : isFideGold ? 6 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pointfusion.setHpFusion(0);
        player.pointfusion.setMpFusion(0);
        player.pointfusion.setDameFusion(0);
    }

    public void createNormalPetGender(Player player, int gender) {
        new Thread(() -> {
            try {
                createNewPet3(player, (byte) gender);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createNormalPetSuper(Player player, int gender, byte type) {
        new Thread(() -> {
            try {
                createNewPetSuper(player, (byte) gender, type);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void createNormalPetSuperGender(Player player, int gender, byte type) {
        new Thread(() -> {
            try {
                createNewPetSuperGender(player, (byte) gender, type);
                Thread.sleep(1000);
                Service.gI().chatJustForMe(player, player.pet, "Xin hãy thu nhận làm đệ tử");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void createNewPet3(Player player, byte gender) {
        int[] data = getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + "Đệ tử";
        pet.gender = (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.gender = player.gender;
        pet.nPoint.power = 1500000;
        pet.typePet = (byte) Util.nextInt(5, 30);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
    }

    private void createNewPetSuper(Player player, byte gender, byte type) {
        int[] data = getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + "Đệ tử";
        pet.gender = (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.nPoint.power = 1500000;
        pet.typePet = type;
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pointfusion.setHpFusion(0);
        player.pointfusion.setMpFusion(0);
        player.pointfusion.setDameFusion(0);
    }

    private void createNewPetSuperGender(Player player, byte gender, byte type) {
        int[] data = getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + "Đệ tử";
        pet.gender = (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.gender = player.gender;
        pet.nPoint.power = 1500000;
        pet.typePet = type;
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 4; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pointfusion.setHpFusion(0);
        player.pointfusion.setMpFusion(0);
        player.pointfusion.setDameFusion(0);
    }

    private void createNewPet(Player player, boolean isMabu, boolean isBerus, byte... gender) {
        int[] data = isMabu ? getDataPetMabu() : getDataPetNormal();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabư" : isBerus ? "Berus" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.gender = player.gender;
        pet.nPoint.power = isMabu || isBerus ? 1500000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isBerus ? 2 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pointfusion.setHpFusion(0);
        player.pointfusion.setMpFusion(0);
        player.pointfusion.setDameFusion(0);
    }

    private void createNewPet2(Player player, boolean isMabu, boolean isHeart, boolean isMai, boolean isGohan, boolean isJiren, byte... gender) {
        int[] data = isMabu ? getDataPetMabu() : getDeTuVip();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabư" : isHeart ? "Heart" : isMai ? "Mai" : isGohan ? "Gohan" : isJiren ? "Jiren" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.gender = player.gender;
        pet.nPoint.power = isMabu || isHeart || isMai || isGohan || isJiren ? 1500000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isHeart ? 7 : isMai ? 8 : isGohan ? 9 : isJiren ? 10 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pointfusion.setHpFusion(0);
        player.pointfusion.setMpFusion(0);
        player.pointfusion.setDameFusion(0);
    }

    private void createNewPet3(Player player, boolean isMabu, boolean isBlack3, boolean isGoku4, boolean isGokuUltra, byte... gender) {
        int[] data = isMabu ? getDataPetMabu() : getDeTuVip();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabư" : isBlack3 ? "Black Rose SSJ3" : isGoku4 ? "Goku SSJ4" : isGokuUltra ? "Goku Vô Cực" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.gender = player.gender;
        pet.nPoint.power = isMabu || isBlack3 || isGoku4 || isGokuUltra ? 15000000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isBlack3 ? 11 : isGoku4 ? 12 : isGokuUltra ? 13 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pointfusion.setHpFusion(0);
        player.pointfusion.setMpFusion(0);
        player.pointfusion.setDameFusion(0);
    }

    private void createNewPet4(Player player, boolean isMabu, boolean isBerusBiNgo, boolean isZamasu, boolean isDaishinkan, boolean isWhis, byte... gender) {
        int[] data = isMabu ? getDataPetMabu() : getDeTuVip();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabu " : isBerusBiNgo ? "Berus Bi Ngo " : isZamasu ? "Zamasu" : isDaishinkan ? "Daishinkan" : isWhis ? "Whis" : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.gender = player.gender;
        pet.nPoint.power = isMabu || isBerusBiNgo || isZamasu || isDaishinkan || isWhis ? 15000000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isBerusBiNgo ? 14 : isZamasu ? 15 : isDaishinkan ? 16 : isWhis ? 17 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pointfusion.setHpFusion(0);
        player.pointfusion.setMpFusion(0);
        player.pointfusion.setDameFusion(0);
    }

    private void createNewPet5(Player player, boolean isMabu, boolean isBuuGay, byte... gender) {
        int[] data = isMabu ? getDataPetMabu() : getDeTuVip();
        Pet pet = new Pet(player);
        pet.name = "$" + (isMabu ? "Mabu " : isBuuGay ? "Bưu Gầy " : "Đệ tử");
        pet.gender = (gender != null && gender.length != 0) ? gender[0] : (byte) Util.nextInt(0, 2);
        pet.id = -player.id;
        pet.gender = player.gender;
        pet.nPoint.power = isMabu || isBuuGay ? 15000000 : 2000;
        pet.typePet = (byte) (isMabu ? 1 : isBuuGay ? 33 : 0);
        pet.nPoint.stamina = 1000;
        pet.nPoint.maxStamina = 1000;
        pet.nPoint.hpg = data[0];
        pet.nPoint.mpg = data[1];
        pet.nPoint.dameg = data[2];
        pet.nPoint.defg = data[3];
        pet.nPoint.critg = data[4];
        for (int i = 0; i < 7; i++) {
            pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
        }
        pet.playerSkill.skills.add(SkillUtil.createSkill(Util.nextInt(0, 2) * 2, 1));
        for (int i = 0; i < 3; i++) {
            pet.playerSkill.skills.add(SkillUtil.createEmptySkill());
        }
        pet.nPoint.setFullHpMp();
        player.pet = pet;
        player.pointfusion.setHpFusion(0);
        player.pointfusion.setMpFusion(0);
        player.pointfusion.setDameFusion(0);
    }

    public void deletePet(Player player) {
        Pet pet = player.pet;
        if (pet != null) {
            if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
                pet.unFusion();
            }
            ChangeMapService.gI().exitMap(pet);
            pet.dispose();
            player.pet = null;
        }
    }

    public static void Pet2(Player pl, int h, int b, int l) {
        if (pl.newpet != null) {
            ChangeMapService.gI().exitMap(pl.newpet);
            pl.newpet.dispose();
            pl.newpet = null;
        }
        pl.newpet = new NewPet(pl, (short) h, (short) b, (short) l);
        pl.newpet.name = "$Thú Cưng";
        pl.newpet.gender = pl.gender;
        pl.newpet.nPoint.tiemNang = 1;
        pl.newpet.nPoint.power = 1;
        pl.newpet.nPoint.limitPower = 1;
        pl.newpet.nPoint.hpg = 5000000;
        pl.newpet.nPoint.mpg = 5000000;
        pl.newpet.nPoint.hp = 5000000;
        pl.newpet.nPoint.dameg = 1;
        pl.newpet.nPoint.defg = 500;
        pl.newpet.nPoint.critg = 1;
        pl.newpet.nPoint.stamina = 1;
        pl.newpet.nPoint.setBasePoint();
        pl.newpet.nPoint.setFullHpMp();
    }
}

//--------------------------------------------------------------------------

