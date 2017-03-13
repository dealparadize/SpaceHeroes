package mx.edu.ittepic.tpdm_u2_practica3;

/**
 * Created by Jorge Arellano on 13/03/2017.
 */

public class Star {
    int radius,x1,y1;

    public Star() {
        Double temp = Math.random()*10;
        Double tempX = Math.random()*1200;
        Double tempY = Math.random()*1900;
        radius = temp.intValue();
        x1 = tempX.intValue();
        y1 = tempY.intValue();

    }
}
