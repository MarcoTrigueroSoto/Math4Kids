package com.example.math4kids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.util.Random;
import android.content.ContentValues;

public class Main2Activity_nivel4 extends AppCompatActivity {
    private TextView tvNombre, tvScore;
    private ImageView ivUno, ivDos,ivVidas,ivOperacion;
    private EditText etRespuesta;
    private MediaPlayer mp,mpGood, mpBad;

    private int randon, randon2, randomOper, result, vidas, score;
    String nombre_jugador, string_score, string_vidas;

    String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve","diez",
    "once","doce","trece","catorce","quince","dieciseis","diecisiete","dieciocho","diecinueve","veinte"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_nivel1);

        Intent intent = getIntent();
        vidas= Integer.parseInt(intent.getStringExtra("Vidas"));
        score= Integer.parseInt(intent.getStringExtra("Score"));

        Toast.makeText(this, "Nivel Cuatro: Sumas, Restas y Multiplicaciones", Toast.LENGTH_SHORT).show();
        tvNombre = (TextView) findViewById(R.id.txtJugador);
        tvScore = (TextView) findViewById(R.id.txtScore);
        ivVidas = (ImageView) findViewById(R.id.imgVidas);
        ivOperacion = (ImageView) findViewById(R.id.txtOperacion);
        ivUno = (ImageView) findViewById(R.id.imgN1);
        ivDos = (ImageView) findViewById(R.id.imgN2);
        etRespuesta = (EditText) findViewById(R.id.txtRespuesta);

        nombre_jugador = getIntent().getStringExtra("Jugador");
        tvNombre.setText("Jugador "+nombre_jugador);
        tvScore.setText("Score: "+score);
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

        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this, R.raw.megamanandbassmuseum);
        mp.start();
        mp.setLooping(true);



        mpGood = MediaPlayer.create(this, R.raw.wonderful);
        mpBad = MediaPlayer.create(this, R.raw.bad);

        numRandom();



    }

    public void numRandom(){
        if(score >= 30){
            randon =new Random().nextInt(21);
            randon2 =new Random().nextInt(21);
            randomOper = new Random().nextInt(3);

            switch (randomOper){
                case 2:
                    ivOperacion.setImageResource(R.drawable.multiplicacion);
                    result = randon * randon2;
                    break;
                case 1:
                    ivOperacion.setImageResource(R.drawable.resta);
                    result = randon - randon2;
                    break;

                case 0:
                    ivOperacion.setImageResource(R.drawable.adicion);
                    result = randon + randon2;
                    break;

            }

            if(result <= 40 && result > -1 ){
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
        }
    }


    public void comprobar(View vista){
        String respuesta = etRespuesta.getText().toString();
        if(!respuesta.isEmpty()){
            adminsqliteopenhelper admin = new adminsqliteopenhelper(this, "db",null, 1);
            SQLiteDatabase DB = admin.getWritableDatabase();
            int resInt = Integer.parseInt(respuesta);
            if(result == resInt){
                mpGood.start();
                score++;
                ContentValues contentValues = new ContentValues();
                contentValues.put("score",score);
                DB.update("puntaje", contentValues, "nombre = ?" , new  String[]{nombre_jugador});
                DB.close();
                tvScore.setText("Score: "+score);

            }
            else{
                mpBad.start();
                vidas--;
                ContentValues contentValues = new ContentValues();
                contentValues.put("vida",vidas);
                DB.update("puntaje", contentValues, "nombre = ?" , new  String[]{nombre_jugador});
                switch (vidas){

                    case 3:
                        DB.close();
                        ivVidas.setImageResource(R.drawable.tresvidas);
                        break;

                    case 2:
                        DB.close();
                        Toast.makeText(this, "Quedan 2 vidas", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(R.drawable.dosvidas);
                        break;

                    case 1:
                        DB.close();
                        Toast.makeText(this, "Queda 1 vida", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(R.drawable.unavida);
                        break;

                    case 0:
                        Cursor consultar = DB.rawQuery("select * from puntaje where nombre = ?", new  String[]{nombre_jugador});
                        if(consultar.moveToFirst()){
                            String temp_best_score = consultar.getString(2);
                            if (score > Integer.parseInt(temp_best_score)){
                                contentValues.put("vida",3);
                                contentValues.put("best_score",score);
                                contentValues.put("score",0);
                                DB.update("puntaje", contentValues, "nombre = ? ", new  String[]{nombre_jugador});
                            }else{
                                contentValues.put("vida",3);
                                contentValues.put("score",0);
                                DB.update("puntaje", contentValues, "nombre = ?" , new  String[]{nombre_jugador});
                            }
                        }
                        DB.close();
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
