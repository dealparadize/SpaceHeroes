package mx.edu.ittepic.tpdm_u2_practica3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;

public class Principal extends AppCompatActivity {
    MediaPlayer starWars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Space(this));

        starWars = MediaPlayer.create(Principal.this,R.raw.starwars);
        starWars.setLooping(true);
        starWars.start();
    }

    protected void onPause(){
        super.onPause();
        starWars.release();
        finish();
    }
}
