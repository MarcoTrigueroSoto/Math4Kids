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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void insert(View vista){
        adminsqliteopenhelper admin = new adminsqliteopenhelper(this, "db", null,1);
        SQLiteDatabase db = admin.getWritableDatabase();


        String nombre = etnombre.getText().toString();


        if(nombre.isEmpty())){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }

        else{

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("nombre", nombre);
            registro.put("precio", precio);
            db.insert("articulos",null, registro);


            Ecodigo.setText("");
            Enombre.setText("");
            Eprecio.setText("");

            Toast.makeText(this, "Se guardaron los datos", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    public void limpiar(View vista){
        adminsqliteopenhelper admin = new adminsqliteopenhelper(this, "db", null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String nombre = etnombre.getText().toString();


        if(nombre.isEmpty()){
            Toast.makeText(this, "Escribe tu nombre para limpiar los datos", Toast.LENGTH_SHORT).show();
        }
        else{
            ContentValues registro = new ContentValues();

            registro.put("nombre", nombre);
            int cantidad = db.update("puntaje", nombre, "nombre="+nombre, null);


            if(cantidad == 1){
                Toast.makeText(this, "Se reinicio la puntuación", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "No se pudo reiniciar la puntuación correctamente", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }

    public void delete(View vista){
        AdministratorSQLHelper admin = new AdministratorSQLHelper(this, "administracion", null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String codigo = Ecodigo.getText().toString();

        if(codigo.isEmpty()){
            Toast.makeText(this, "No puede tener el código vacío", Toast.LENGTH_SHORT).show();
        }
        else{
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            int cantidad = db.delete("articulos", "codigo="+codigo,null);
            if(cantidad == 1){
                Toast.makeText(this, "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                Ecodigo.setText("");
                Enombre.setText("");
                Eprecio.setText("");


            }
            else{
                Toast.makeText(this, "No se encontró el registro", Toast.LENGTH_SHORT).show();
                Ecodigo.setText("");
                Enombre.setText("");
                Eprecio.setText("");

            }

        }
        db.close();

    }
    public void read(View vista) {
        AdministratorSQLHelper admin = new AdministratorSQLHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String codigo = Ecodigo.getText().toString();
        if (codigo.isEmpty()) {
            Toast.makeText(this, "No puede tener el código vacío", Toast.LENGTH_SHORT).show();
        } else {
            Cursor fila = db.rawQuery("select codigo, nombre, precio from articulos where codigo = " + codigo + ";", null);

            if(fila.moveToFirst()){

                Enombre.setText(fila.getString(1));
                Eprecio.setText(fila.getString(2));


            } else {
                Ecodigo.setText("");
                Enombre.setText("");
                Eprecio.setText("");
                Toast.makeText(this, "Datos no encontrados", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }



    @Override
    public  void onBackPressed(){

    }

}
