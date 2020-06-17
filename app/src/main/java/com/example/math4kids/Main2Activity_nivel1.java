package com.example.math4kids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

public class Main2Activity_nivel1 extends AppCompatActivity {


    private TextView tvNombre, tvScore;
    private ImageView ivUno, ivDos,ivVidas;
    private EditText etRespuesta;
    private MediaPlayer mp,mpGood, mpBad;

    private int randon, randon2, result, vidas, score;

    String nombre_jugador, string_score, string_vidas;

    String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_nivel1);
        Intent intent = getIntent();
        vidas= Integer.parseInt(intent.getStringExtra("Vidas"));
        score= Integer.parseInt(intent.getStringExtra("Score"));

        Toast.makeText(this, "Nivel uno: Sumas básicas", Toast.LENGTH_SHORT).show();
        tvNombre = (TextView) findViewById(R.id.txtJugador);
        tvScore = (TextView) findViewById(R.id.txtScore);
        ivVidas = (ImageView) findViewById(R.id.imgVidas);
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
            intent.putExtra("Jugador", nombre_jugador);
            intent.putExtra("Vidas", string_vidas);
            intent.putExtra("Score", string_score);
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
/*
    public void insert(View vista){
        AdministratorSQLHelper admin = new AdministratorSQLHelper(this, "administracion", null,1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String codigo = Ecodigo.getText().toString();
        String nombre = Enombre.getText().toString();
        String precio = Eprecio.getText().toString();

        if(verificar(codigo, nombre, precio)){
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
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
    public void update(View vista){
        AdministratorSQLHelper admin = new AdministratorSQLHelper(this, "administracion", null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String codigo = Ecodigo.getText().toString();
        String nombre = Enombre.getText().toString();
        String precio = Eprecio.getText().toString();

        if(verificar(codigo, nombre, precio)){
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
        else{
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("nombre", nombre);
            registro.put("precio", precio);
            int cantidad = db.update("articulos", registro, "codigo="+codigo, null);


            if(cantidad == 1){
                Toast.makeText(this, "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
                Ecodigo.setText("");
                Enombre.setText("");
                Eprecio.setText("");


            }
            else{
                Toast.makeText(this, "No se actualizó correctamente", Toast.LENGTH_SHORT).show();
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

    public boolean verificar(String codigo, String nombre, String precio){
        if(codigo.isEmpty() || nombre.isEmpty() || precio.isEmpty()){

            return true;

        }
        else{

            return false;
        }

    }

*/
}
