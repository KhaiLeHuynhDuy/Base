//package com.girlkun.models.boss.list_boss.Broly;
//
//import com.girlkun.models.boss.Boss;
//import com.girlkun.models.boss.BossID;
//import com.girlkun.models.boss.BossStatus;
//import com.girlkun.models.boss.BossesData;
//import com.girlkun.models.map.ItemMap;
//import com.girlkun.models.player.Player;
//import com.girlkun.services.EffectSkillService;
//import com.girlkun.services.Service;
//import com.girlkun.utils.Util;
//import java.util.Random;
//
//
//public class BrolyClone extends Boss {
//
//    public BrolyClone() throws Exception {
//        super(BossID.YADART, BossesData.YADART_CLONE);
//    }
//    
//    @Override
//    public void active() {
//        super.active();
//        if(Util.canDoWithTime(st,300000)){
//            this.changeStatus(BossStatus.LEAVE_MAP);
//        }
//    }
//    
//    @Override
//    public void joinMap() {
//        super.joinMap();
//        st= System.currentTimeMillis();
//    }
//    private long st;
//    
//        @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//    
//    @Override
//     public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
//                this.chat("Xí hụt");
//                return 0;
//            }
//            damage = this.nPoint.subDameInjureWithDeff(damage);
//            if (!piercing && effectSkill.isShielding) {
//                if (damage > nPoint.hpMax) {
//                    EffectSkillService.gI().breakShield(this);
//                }
//                damage = 1;
//            }
//            if (damage >= 1) {
//                damage = 1;
//            }
//            this.nPoint.subHP(damage);
//            if (isDie()) {
//                this.setDie(plAtt);
//                die(plAtt);
//            }
//            return damage;
//        } else {
//            return 0;
//        }
//    }
//}
