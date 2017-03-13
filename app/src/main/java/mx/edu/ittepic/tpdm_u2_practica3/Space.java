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
import android.widget.Toast;

/**
 * Created by jorgearellano on 11/03/17.
 */

public class Space extends View {
    int numEnemy;
    int lifes = 3;
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
                moveBullets();
                checkBullet();
                invalidate();
            }

            @Override
            public void onFinish() {
                start();
            }
        };
        timer.start();

        CountDownTimer timerBullets = new CountDownTimer(20000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Double temp = Math.random()*20;
                numEnemy =  temp.intValue();

                //Todos los enemigos pueden lanzar, hay que restringirlo a que solo los que esten vivos.
                if(numEnemy<7){
                    enemies[0][numEnemy].bullet = new Bullet(true);
                    enemies[0][numEnemy].bullet.x1 = enemies[0][numEnemy].x1-20;
                    enemies[0][numEnemy].bullet.y1 = enemies[0][numEnemy].y1-10;
                    enemies[0][numEnemy].bullet.x2 = enemies[0][numEnemy].x1;
                    enemies[0][numEnemy].bullet.y2 = enemies[0][numEnemy].y1;
                }
                else if(numEnemy<14){
                    enemies[1][numEnemy-7].bullet = new Bullet(true);
                    enemies[1][numEnemy-7].bullet.x1 = enemies[1][numEnemy-7].x1-20;
                    enemies[1][numEnemy-7].bullet.y1 = enemies[1][numEnemy-7].y1-10;
                    enemies[1][numEnemy-7].bullet.x2 = enemies[1][numEnemy-7].x1;
                    enemies[1][numEnemy-7].bullet.y2 = enemies[1][numEnemy-7].y1;
                }
                else{
                    enemies[2][numEnemy-14].bullet = new Bullet(true);
                    enemies[2][numEnemy-14].bullet.x1 = enemies[2][numEnemy-14].x1-20;
                    enemies[2][numEnemy-14].bullet.y1 = enemies[2][numEnemy-14].y1-10;
                    enemies[2][numEnemy-14].bullet.x2 = enemies[2][numEnemy-14].x1;
                    enemies[2][numEnemy-14].bullet.y2 = enemies[2][numEnemy-14].y1;
                }
                invalidate();
            }

            @Override
            public void onFinish() {
                start();
            }
        };
        timerBullets.start();
    }

    private void moveBullets() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if(enemies[i][j].bullet!=null){
                    enemies[i][j].bullet.x1-=15;
                    enemies[i][j].bullet.x2-=15;
                }
            }
        }
    }

    private void checkBullet() {
        //Bala de la nave
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

        //Balas enemigas
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                if(enemies[i][j].bullet!=null){
                    if(enemies[i][j].bullet.x1<=nave.rect3){
                        float center = (nave.rect2+nave.rect4)/2;
                        if(enemies[i][j].bullet.y1<center+15 && enemies[i][j].bullet.y1>center-15) {
                            lifes--;
                            enemies[i][j].bullet=null;
                            Log.d("Final del juego","jijiji");
                            if (lifes <= 0) {
                                Toast.makeText(getContext(), "FINAAAAL", Toast.LENGTH_LONG);
                            }
                            return;
                        }
                    }
                    else if(enemies[i][j].bullet.x1<10){
                        enemies[i][j].bullet = null;
                    }

                }
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

        for(int i = 0;i<3;i++){
            for(int j = 0;j<7;j++){
                Bullet temp = enemies[i][j].bullet;
                if(temp!=null) {
                    p.setColor(Color.MAGENTA);
                    c.drawRect(temp.x1, temp.y1, temp.x2, temp.y2, p);

                }
            }
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
