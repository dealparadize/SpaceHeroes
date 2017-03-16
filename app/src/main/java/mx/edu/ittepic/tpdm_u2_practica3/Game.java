package mx.edu.ittepic.tpdm_u2_practica3;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Game extends AppCompatActivity {
    MediaPlayer starWars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Space(Game.this));
        starWars = MediaPlayer.create(Game.this,R.raw.starwars);
        starWars.setLooping(true);
        starWars.start();

    }

    protected void onPause(){
        super.onPause();
        starWars.release();
        finish();
    }
}
