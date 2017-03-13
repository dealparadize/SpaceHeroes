package mx.edu.ittepic.tpdm_u2_practica3;

import android.content.Context;
import android.content.Loader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jorgearellano on 11/03/17.
 */

public class Space extends View {
    Nave nave;
    Enemy[][] enemies = new Enemy[3][7];
    CountDownTimer timer;
    boolean move = true;
    Bullet bullet = new Bullet(false);
    public Space(Context context)
    {
        super(context);
        inicializateStage();
        timer = new CountDownTimer(20000,50) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(move){
                    for(int i = 0;i<3;i++){
                        for(int j = 0;j<7;j++){
                            enemies[i][j].y1+=15;
                            enemies[i][j].y2+=15;
                        }
                    }
                }
                else {
                    for(int i = 0;i<3;i++){
                        for(int j = 0;j<7;j++){
                            enemies[i][j].y1-=15;
                            enemies[i][j].y2-=15;
                        }
                    }
                }
                if(bullet.shoot){
                    bullet.x1+=15;
                    bullet.x2+=15;
                }
                checkBullet();
                invalidate();
            }

            @Override
            public void onFinish() {
                start();
            }
        };
        timer.start();
    }

    private void checkBullet() {
        if(bullet.shoot) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 7; j++) {
                    if (bullet.x2 < enemies[i][j].x2 && bullet.x2 > enemies[i][j].x1) {
                        if (bullet.y2 < enemies[i][j].y2 && bullet.y2 > enemies[i][j].y1 && enemies[i][j].visible) {
                            enemies[i][j].visible = false;
                            bullet.shoot = false;
                        }
                    }
                }
            }
            if (bullet.x2 > 1200) {
                bullet.shoot = false;
            }
        }
    }

    private void inicializateStage() {
        nave = new Nave(50,700,190,750,100,700,240,750);
        for(int i = 0;i<3;i++){
            for(int j = 0;j<7;j++){
                enemies[i][j] = new Enemy(1000-(i*100),400+(j*150),1050-(i*100),480+(j*150),true);
            }
        }

    }



    protected void onDraw(Canvas c){
        Paint p = new Paint();

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        c.drawRect(0,0,1500,2000,p);


        p.setColor(Color.GREEN);
        c.drawRect(nave.rect1,nave.rect2,nave.rect3,nave.rect4,p);
        c.drawArc(nave.arc1,nave.arc2,nave.arc3,nave.arc4,0,360,true,p);
        //c.drawCircle(nave.rect3,nave.rect2+nave.rect4/2,50,p);


        for(int i = 0;i<3;i++){
            for(int j = 0;j<7;j++){
                if(enemies[i][j].visible) {
                    p.setColor(Color.BLUE);
                    c.drawOval(enemies[i][j].x1, enemies[i][j].y1, enemies[i][j].x2, enemies[i][j].y2, p);
                    p.setColor(Color.WHITE);
                    c.drawOval(enemies[i][j].x1 + 12, enemies[i][j].y1 + 20, enemies[i][j].x2 - 12, enemies[i][j].y2 - 20, p);
                }

            }
        }

        if(bullet.shoot){
            p.setColor(Color.MAGENTA);
            c.drawRect(bullet.x1,bullet.y1,bullet.x2,bullet.y2,p);
        }
        checkBorder();

    }

    private void checkBorder() {
        if(enemies[0][6].y2>1650 && move){
            move = false;
        }
        if(enemies[0][0].y1<50 && !move){
            move = true;
        }
    }

    public boolean onTouchEvent(MotionEvent e){
        if(e.getAction()==MotionEvent.ACTION_DOWN){
            if(e.getX()<600 && e.getY()<900){
                moveShipToLeft();
            }
            else if(e.getX()<600 && e.getY()>900){
                moveShipToRight();
            }
            else {
                shootBullet();
            }
        }
        return true;
    }

    private void shootBullet() {
        if (!bullet.shoot) {
            bullet.x1 = (nave.arc1 + nave.arc3) / 2;
            bullet.y1 = (nave.arc2 + nave.arc4)/ 2;
            bullet.x2 = bullet.x1 + 20;
            bullet.y2 = bullet.y1 + 10;
            bullet.shoot = true;
        }

    }

    private void moveShipToRight() {
        nave.rect2+=10;
        nave.rect4+=10;
        nave.arc2+=10;
        nave.arc4+=10;
    }

    private void moveShipToLeft() {
        nave.rect2-=10;
        nave.rect4-=10;
        nave.arc2-=10;
        nave.arc4-=10;
    }

}
