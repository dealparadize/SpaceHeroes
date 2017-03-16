package mx.edu.ittepic.tpdm_u2_practica3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jorgearellano on 11/03/17.
 */

public class Space extends View {
    int numEnemy;
    int lifes = 3;
    Nave nave;
    Enemy[][] enemies = new Enemy[3][7];
    ArrayList<Star> stars = new ArrayList<Star>();
    CountDownTimer timer;
    boolean move = true;
    Bullet bullet = new Bullet(false);
    Bitmap heart;
    Context context;

    public Space(Context context){
        super(context);
        inicializateStage();

        this.context = context;

        heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);

        timer = new CountDownTimer(20000,50) {
            @Override
            public void onTick(long millisUntilFinished) {

                for(int i = 0;i<3;i++) {
                    for (int j = 0; j < 7; j++) {
                        if(enemies[i][j].move) {
                            enemies[i][j].y1 += 15;
                            enemies[i][j].y2 += 15;
                        }
                        else{
                           enemies[i][j].y1-=15;
                           enemies[i][j].y2-=15;
                        }
                    }
                }
                if(bullet.shoot){
                    bullet.x1+=50;
                    bullet.x2+=50;
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

        CountDownTimer timerBullets = new CountDownTimer(20000,400) {
            @Override
            public void onTick(long millisUntilFinished) {
                Double temp = Math.random()*20;
                numEnemy =  temp.intValue();

                //Todos los enemigos pueden lanzar, hay que restringirlo a que solo los que esten vivos y continuamente
                if(numEnemy<7){
                    if(enemies[0][numEnemy].visible) {
                        enemies[0][numEnemy].bullet = new Bullet(true);
                        enemies[0][numEnemy].bullet.x1 = enemies[0][numEnemy].x1 - 20;
                        enemies[0][numEnemy].bullet.y1 = enemies[0][numEnemy].y1 - 10;
                        enemies[0][numEnemy].bullet.x2 = enemies[0][numEnemy].x1;
                        enemies[0][numEnemy].bullet.y2 = enemies[0][numEnemy].y1;
                    }
                }
                else if(numEnemy<14){
                    if (enemies[0][numEnemy-7].visible) {
                        enemies[1][numEnemy - 7].bullet = new Bullet(true);
                        enemies[1][numEnemy - 7].bullet.x1 = enemies[1][numEnemy - 7].x1 - 20;
                        enemies[1][numEnemy - 7].bullet.y1 = enemies[1][numEnemy - 7].y1 - 10;
                        enemies[1][numEnemy - 7].bullet.x2 = enemies[1][numEnemy - 7].x1;
                        enemies[1][numEnemy - 7].bullet.y2 = enemies[1][numEnemy - 7].y1;
                    }
                }
                else{
                    if(enemies[0][numEnemy-14].visible) {
                        enemies[2][numEnemy - 14].bullet = new Bullet(true);
                        enemies[2][numEnemy - 14].bullet.x1 = enemies[2][numEnemy - 14].x1 - 20;
                        enemies[2][numEnemy - 14].bullet.y1 = enemies[2][numEnemy - 14].y1 - 10;
                        enemies[2][numEnemy - 14].bullet.x2 = enemies[2][numEnemy - 14].x1;
                        enemies[2][numEnemy - 14].bullet.y2 = enemies[2][numEnemy - 14].y1;
                    }
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
                    enemies[i][j].bullet.x1-=50;
                    enemies[i][j].bullet.x2-=50;
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
                            if(!isAnyoneAlive()){
                                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                                ad.setMessage("YOU WON");
                                ad.setPositiveButton("END GAME", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((Activity)getContext()).finish();
                                    }
                                });
                                ad.show();
                            }
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
                    if(enemies[i][j].bullet.x1<=nave.rect3 && enemies[i][j].bullet.x1>=nave.rect1){
                        float center = (nave.rect2+nave.rect4)/2;
                        if(enemies[i][j].bullet.y1<center+15 && enemies[i][j].bullet.y1>center-15) {
                            MediaPlayer sound = MediaPlayer.create(getContext().getApplicationContext(),R.raw.weapon);
                            sound.start();
                            lifes--;
                            enemies[i][j].bullet=null;
                            Log.d("Final del juego","jijiji");
                            if (lifes <= 0) {
                                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                                ad.setMessage("YOU LOSE");
                                ad.setPositiveButton("END GAME", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((Activity)getContext()).finish();
                                    }
                                });
                                ad.show();
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

    private boolean isAnyoneAlive() {
        for (int i = 0;i<3;i++){
            for (int j = 0; j<7; j++){
                if(enemies[i][j].visible)
                    return true;
            }
        }
        return false;
    }

    private void inicializateStage() {
        nave = new Nave(10,700,50,750,10,700,100,750);
        nave.lnX = 720;
        nave.lnY = 10;
        nave.lnX2 = 730;
        nave.lnY2 = 40;

        nave.prX = 0;
        nave.prY = 690;
        nave.prX2 = 60;
        nave.prY2 = 710;

        nave._prX = 0;
        nave._prY = 740;
        nave._prX2 = 60;
        nave._prY2 = 760;


        for(int i = 0;i<3;i++){
            for(int j = 0;j<7;j++){
                enemies[i][j] = new Enemy(1000-(i*100),400+(j*150),1050-(i*100),480+(j*150),true);
            }
        }

        for(int i = 0;i<200;i++){
            stars.add(new Star());
        }

    }



    protected void onDraw(Canvas c){
        Paint p = new Paint();

        //Fondo negro
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);
        c.drawRect(0,0,1500,2000,p);

        //Nave
        p.setColor(Color.rgb(220, 220, 220));
        c.drawRect(nave.rect1,nave.rect2,nave.rect3,nave.rect4,p);
        c.drawArc(nave.arc1,nave.arc2,nave.arc3,nave.arc4,0,360,true,p);

        p.setColor(Color.RED);
        c.drawArc(nave.prX, nave.prY, nave.prX2, nave.prY2, 0, 360, true, p);
        c.drawArc(nave._prX, nave._prY, nave._prX2, nave._prY2, 0, 360, true, p);

        p.setColor(Color.RED);
        c.drawRect(nave.lnY, nave.lnX, nave.lnY2, nave.lnX2, p);
        //c.drawCircle(nave.rect3,nave.rect2+nave.rect4/2,50,p);



        //Estrellas
        for(int i = 0;i<stars.size();i++){
            Double temp = Math.random()*255;

            p.setColor(Color.argb(temp.intValue(),255,255,255));
            c.drawCircle(stars.get(i).x1,stars.get(i).y1,stars.get(i).radius,p);
        }

        for(int i = 0;i<3;i++){
            for(int j = 0;j<7;j++){
                if(enemies[i][j].visible) {
                    p.setColor(Color.rgb(255, 255, 255));
                    c.drawOval(enemies[i][j].x1, enemies[i][j].y1, enemies[i][j].x2, enemies[i][j].y2, p);
                    p.setColor(Color.rgb(220, 220, 255));
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

        for(int i = 0, j = 50; i < lifes; i++, j += 70){
            c.drawBitmap(heart, j, 20, p);
        }

        checkBorder();

    }

    private void checkBorder() {

        for(int i = 0;i<3;i++){
           //Checar el ciclo las posiciones finales e iniciales de cada uno de los extremos de los arreglos.
            int end = 6;
            while(!enemies[i][end].visible && end>0){
                end--;
            }
            if(enemies[i][end].y2>1650 && enemies[i][end].move){
                moveEnemiesToLeft(i);
            }

            int start = 0;

            while(start<7 && !enemies[i][start].visible){
                start++;
            }
            if(start==7){
                start--;
            }
            if(enemies[i][start].y1<50 && !enemies[i][start].move){
                moveEnemiesToRight(i);
            }
        }
    }

    private void moveEnemiesToLeft(int i) {
        for(int j=0;j<7;j++)
            enemies[i][j].move = false;
    }

    private void moveEnemiesToRight(int i) {
        for(int j=0;j<7;j++)
            enemies[i][j].move = true;
    }


    public boolean onTouchEvent(MotionEvent e){
        if(e.getAction()==MotionEvent.ACTION_DOWN){
            if(e.getX()<600 && e.getY()<900){
                //moveShipToLeft();
            }
            else if(e.getX()<600 && e.getY()>900){
                //moveShipToRight();
            }
            else {
                shootBullet();
            }

        }
        if(e.getAction()==MotionEvent.ACTION_MOVE){
            nave.rect2=(int)e.getY();
            nave.rect4=(int)e.getY()+50;
            nave.arc2=(int)e.getY();
            nave.arc4=(int)e.getY()+50;

            nave.lnX = (int)e.getY() + 20;
            nave.lnX2 = (int)e.getY() + 30;

            nave.prY = (int)e.getY() - 10;
            nave.prY2 = (int)e.getY() + 10;

            nave._prY = (int)e.getY() + 40;
            nave._prY2 = (int)e.getY() + 60;



        }
        return true;
    }

    private void shootBullet() {
        if (!bullet.shoot) {
            MediaPlayer sound = MediaPlayer.create(getContext().getApplicationContext(),R.raw.solo);
            sound.start();
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
