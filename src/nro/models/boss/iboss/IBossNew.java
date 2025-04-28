package nro.models.boss.iboss;

import nro.models.boss.BossStatus;
import nro.models.player.Player;


public interface IBossNew {

    void updateBoss();

    void initBase();

    void changeStatus(BossStatus status);

    Player getPlayerAttack();

    void changeToTypePK();

    void changeToTypeNonPK();
    
    void moveToPlayer(Player player);
    
    void moveTo(int x, int y);
    
    void checkPlayerDie(Player player);
    
    void wakeupAnotherBossWhenAppear();
    
    void wakeupAnotherBossWhenDisappear();
    
    void reward(Player plKill);
    
    void attack();

    //loop
    void rest();

    void respawn();

    void joinMap();

    boolean chatS();
    
    void doneChatS();

    void active();
    
    void chatM();

    void die(Player plKill);

    boolean chatE();
    
    void doneChatE();
    
    void SpawnCombat();

    void leaveMap();
    //end loop
}

