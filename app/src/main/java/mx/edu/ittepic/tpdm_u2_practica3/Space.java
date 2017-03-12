package mx.edu.ittepic.tpdm_u2_practica3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by jorgearellano on 11/03/17.
 */

public class Space extends View {

    public Space(Context context) {
        super(context);
    }

    protected void onDraw(Canvas c){
        Paint p = new Paint();

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        c.drawRect(0,0,1000,2000,p);
    }

}
