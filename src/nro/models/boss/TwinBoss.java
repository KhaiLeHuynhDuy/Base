
package nro.models.boss;

public class TwinBoss extends Boss{
    private TwinBoss another;
    public TwinBoss(int idgroup, BossData... data) throws Exception {
        super(idgroup, data);
    }
    
     public TwinBoss(int idgroup,TwinBoss tBoss, BossData... data) throws Exception {
        super(idgroup, data);
        this.another = tBoss;
    }
    
}
