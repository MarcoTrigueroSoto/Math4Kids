package com.example.math4kids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_nivel1 extends AppCompatActivity {

    private TextView tvNombre, tvScore;
    private ImageView ivUno, ivDos,ivVidas;
    private EditText etRespuesta;
    private MediaPlayer mp,mpGood, mpBad;

    private int score, randon, randon2, result, vidas=3;
    String nombre_jugador, string_score, string_vidas;

    String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_nivel1);

        Toast.makeText(this, "Nivel uno: Sumas básicas", Toast.LENGTH_SHORT).show();
        tvNombre = (TextView) findViewById(R.id.txtJugador);
        tvScore = (TextView) findViewById(R.id.txtScore);
        ivVidas = (ImageView) findViewById(R.id.imgVidas);
        ivUno = (ImageView) findViewById(R.id.imgN1);
        ivDos = (ImageView) findViewById(R.id.imgN2);
        etRespuesta = (EditText) findViewById(R.id.txtRespuesta);

        nombre_jugador = getIntent().getStringExtra("Jugador");

        tvNombre.setText("Jugador "+nombre_jugador);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);



        mpGood = MediaPlayer.create(this, R.raw.wonderful);
        mpBad = MediaPlayer.create(this, R.raw.bad);

        numRandom();



    }

    public void numRandom(){
        if(score < 10){
            randon =(int) (Math.random()*10);
            randon2 =(int) (Math.random()*10);

            result = randon + randon2;

            if(result <= 10){
                for (int i = 0; i < numero.length; i++){
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if(randon == i){
                        ivUno.setImageResource(id);
                    }
                    if(randon2 == i){
                        ivDos.setImageResource(id);
                    }
                }
            }else{
                numRandom();
            }
        }else{
            Intent intent = new Intent(this, Main2Activity_nivel2.class);
            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);
            intent.putExtra("jugador", nombre_jugador);
            intent.putExtra("vidas", string_vidas);
            intent.putExtra("score", string_score);
            startActivity(intent);
            finish();
            mp.stop();
            mp.release();

        }


    }


    public void comprobar(View vista){
        String respuesta = etRespuesta.getText().toString();
        if(!respuesta.isEmpty()){
                int resInt = Integer.parseInt(respuesta);
                if(result == resInt){
                    mpGood.start();
                    score++;
                    tvScore.setText("Score: "+score);

                }
                else{
                    mpBad.start();
                    vidas--;
                    switch (vidas){
                    case 3:
                        ivVidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, "Quedan 2 vidas", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(R.drawable.dosvidas);
                        break;

                    case 1:
                        Toast.makeText(this, "Queda 1 vida", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(R.drawable.unavida);
                     break;

                    case 0:
                        Toast.makeText(this, "Ya no te quedan vidas", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        mp.stop();
                        mp.release();
                        finish();
                        break;

                    }
                }
        etRespuesta.setText("");
        numRandom();
        }
        else{
            Toast.makeText(this, "Escriba una respuesta", Toast.LENGTH_SHORT).show();
        }

    }


}
