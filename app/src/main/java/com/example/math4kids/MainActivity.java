package com.example.math4kids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        Cursor consultar = DB.rawQuery("select * from puntaje where best_score = (select max(best_score) from puntaje)", null);

        if(consultar.moveToFirst()){
            String temp_name = consultar.getString(0);
           /* String temp_score = consultar.getString(2);*/
            String temp_score = "30";
            tvScore.setText("Record: "+ temp_score+" de "+ temp_name);
        }
        DB.close();


        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);



    }

    public void jugar(View vista){

        String nombre = etnombre.getText().toString();
        String temp_name = nombre;
        String temp_score;
        String temp_vida;

        if(!nombre.isEmpty()){
            adminsqliteopenhelper admin = new adminsqliteopenhelper(this, "db",null, 1);
            SQLiteDatabase DB = admin.getWritableDatabase();
            Cursor consultar = DB.rawQuery("select * from puntaje where nombre = ?", new  String[]{temp_name});
            if(consultar.moveToFirst()){
                 temp_name = consultar.getString(0);
                 temp_score = consultar.getString(1);
                 temp_vida = consultar.getString(3);
                Log.d("tagC",nombre);
                Log.d("tagC",temp_name);
            }else{
                temp_score = "0";
                temp_vida = "3";
                String sql = "insert into puntaje values(?,?,?,?)";
                DB.execSQL(sql, new Object[] {nombre,0,0,3} );
                Log.d("tagE",nombre);
                Log.d("tagE",temp_name);
            }
            DB.close();
            mp.stop();
            mp.release();

            if(Integer.parseInt(temp_score)>=0 && Integer.parseInt(temp_score) < 10){
                Intent intent = new Intent(this, Main2Activity_nivel1.class);
                intent.putExtra("Jugador", temp_name);
                intent.putExtra("Score", temp_score);
                intent.putExtra("Vidas", temp_vida);
                startActivity(intent);
                finish();
            } else if(Integer.parseInt(temp_score)>=10 && Integer.parseInt(temp_score) < 20){
                Intent intent = new Intent(this, Main2Activity_nivel2.class);
                intent.putExtra("Jugador", temp_name);
                intent.putExtra("Score", temp_score);
                intent.putExtra("Vidas", temp_vida);
                startActivity(intent);
                finish();
            }else if(Integer.parseInt(temp_score)>=20 && Integer.parseInt(temp_score) < 30){
                Intent intent = new Intent(this, Main2Activity_nivel3.class);
                intent.putExtra("Jugador", temp_name);
                intent.putExtra("Score", temp_score);
                intent.putExtra("Vidas", temp_vida);
                startActivity(intent);
                finish();
            }else if(Integer.parseInt(temp_score)>=30){
                Intent intent = new Intent(this, Main2Activity_nivel4.class);
                intent.putExtra("Jugador", temp_name);
                intent.putExtra("Score", temp_score);
                intent.putExtra("Vidas", temp_vida);
                startActivity(intent);
                finish();
            }



        }else{
            Toast.makeText(this, "Escribe tu nombre primero", Toast.LENGTH_SHORT).show();
            etnombre.requestFocus();
            InputMethodManager imn = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imn.showSoftInput(etnombre, InputMethodManager.SHOW_IMPLICIT);

        }

    }

    public void limpiar(View vista){
        adminsqliteopenhelper admin = new adminsqliteopenhelper(this, "db", null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String nombre = etnombre.getText().toString();

        /*btnLimpiar= findViewById(R.id.btnLimpiar);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                limpiar(v);
            }
        });*/


        if(nombre.isEmpty()){
            Toast.makeText(this, "Escribe tu nombre para limpiar los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            ContentValues registro = new ContentValues();

            registro.put("score", 0);
            registro.put("vida", 3);

            //int cantidad = db.update("puntaje", registro, "nombre="+nombre, null);
            int cantidad =  db.update("puntaje", registro,"nombre = ?" , new  String[]{nombre});

            if(cantidad == 1){

                Toast.makeText(this, "Se reinicio la puntuación del usuario", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "No se pudo reiniciar la puntuación correctamente", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }

    @Override
    public  void onBackPressed(){

    }

}
