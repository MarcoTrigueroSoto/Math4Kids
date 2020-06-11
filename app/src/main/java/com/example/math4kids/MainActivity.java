package com.example.math4kids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etnombre;
    private ImageView im_personaje;
    private TextView tvScore;
    private MediaPlayer mp;

    int random = (int) (Math.random() * 10);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etnombre = (EditText) findViewById(R.id.txtName);
        im_personaje = (ImageView) findViewById(R.id.imgfrutita);
        tvScore = (TextView) findViewById(R.id.txtScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);




        int id;
        if( random == 0 || random == 10){
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            im_personaje.setImageResource(id);
        }
        if( random == 1 || random == 9){
            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            im_personaje.setImageResource(id);
        }
        if( random == 2 || random == 8){
            id = getResources().getIdentifier("frutita", "drawable", getPackageName());
            im_personaje.setImageResource(id);
        }
        if( random == 3 || random == 7){
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            im_personaje.setImageResource(id);
        }
        if( random == 4 || random == 6 || random == 5){
            id = getResources().getIdentifier("naranja", "drawable", getPackageName());
            im_personaje.setImageResource(id);
        }

        adminsqliteopenhelper admin = new adminsqliteopenhelper(this, "db",null, 1);
        SQLiteDatabase DB = admin.getWritableDatabase();
        Cursor consultar = DB.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);

        if(consultar.moveToFirst()){
            String temp_name = consultar.getString(0);
            String temp_score = consultar.getString(1);

            tvScore.setText("Record: "+ temp_score+" de "+ temp_name);
        }
        DB.close();


        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);




    }

    public void jugar(View vista){

        String nombre = etnombre.getText().toString();

        if(!nombre.isEmpty()){
            mp.stop();
            mp.release();
            Intent intent = new Intent(this, Main2Activity_nivel1.class);
            intent.putExtra("Jugador", nombre);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(this, "Escribe tu nombre primero", Toast.LENGTH_SHORT).show();
            etnombre.requestFocus();
            InputMethodManager imn = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imn.showSoftInput(etnombre, InputMethodManager.SHOW_IMPLICIT);

        }

    }

    @Override
    public  void onBackPressed(){

    }

}
