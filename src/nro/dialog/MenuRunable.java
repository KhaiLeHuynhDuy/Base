package nro.dialog;

import lombok.Getter;
import lombok.Setter;

/**
 * @Build by Arriety
 */
@Setter
@Getter
public abstract class MenuRunable implements Runnable {
    private int indexSelected;
}
