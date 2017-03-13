package mx.edu.ittepic.tpdm_u2_practica3;


import android.content.Context;
import android.view.View;

/**
 * Created by Jorge Arellano on 12/03/2017.
 */

public class Enemy  {
    float x2,y2,x1,y1;
    Bullet bullet ;
    boolean visible;
    public Enemy(int x1, int y1, int x2, int y2,boolean visible) {
        this.x2 = x2;
        this.y2 = y2;
        this.x1 = x1;
        this.y1 = y1;
        this.visible = visible;
        bullet = null;
    }


}
