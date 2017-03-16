package mx.edu.ittepic.tpdm_u2_practica3;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;

public class Principal extends AppCompatActivity {
    MediaPlayer starWars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Thread thread= new Thread(){
            @Override
            public void run(){
                try{
                    sleep(3000);
                   openGame();
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        };

        thread.start();

    }

    private void openGame() {
        Intent other = new Intent(Principal.this, Game.class);
        startActivity(other);
    }


}
