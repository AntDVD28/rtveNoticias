package com.example.rtvenoticias;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class InsertarNoticia extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etTitulo, etEnlace, etFecha;
    private Button btInsertarNoticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertar_noticia);

        //Registramos la Toolbar y la activamos
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Obtenemos la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etEnlace = (EditText) findViewById(R.id.etEnlace);

        //Rellenamos el editText de la fecha con la fecha actual y lo ponemos no editable
        etFecha = (EditText) findViewById(R.id.etFecha);
        etFecha.setText(fecha);
        etFecha.setEnabled(false);

        //Creamos una instancia para poder acceder a la BD
        final HelperBD dbHelper = new HelperBD(this);

        //Funcionalidad botón insertar noticia
        btInsertarNoticia = (Button) findViewById(R.id.btInsertarNoticia);
        btInsertarNoticia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validarmos la url
                Boolean b;
                b = URLUtil.isNetworkUrl(etEnlace.getText().toString());

                if(b){

                    //Accedemos a la BD en modo escritura
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    values.put(EstructuraBD.COLUMN_2_NAME, etTitulo.getText().toString());
                    values.put(EstructuraBD.COLUMN_3_NAME, etEnlace.getText().toString());
                    values.put(EstructuraBD.COLUMN_4_NAME, etFecha.getText().toString());
                    values.put(EstructuraBD.COLUMN_5_NAME, 0);
                    values.put(EstructuraBD.COLUMN_6_NAME, 0);
                    values.put(EstructuraBD.COLUMN_7_NAME, 0);

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId = db.insert(EstructuraBD.TABLE_NAME, null, values);

                    if(newRowId > 0)
                        Toast.makeText(getApplicationContext(), "Datos insertados", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error en la inserción", Toast.LENGTH_SHORT).show();

                    //Volvemos a la página de inicio
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);


                }else {

                    Toast.makeText(getApplicationContext(), "La url no es correcta.", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }



    //Métodos para las opciones del menú
    @Override
    public boolean onCreateOptionsMenu(Menu mimenu){

        getMenuInflater().inflate(R.menu.menu, mimenu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem opcion_menu){

        int id = opcion_menu.getItemId();

        if(id == R.id.inicio){

            inicio(null);

            return true;
        }

        if(id == R.id.insertar){

            insertar_noticia(null);

            return true;
        }
        if(id == R.id.eliminar) {

            eliminar_noticia(null);

            return true;
        }
        if(id == R.id.salir) {

            /*Finaliza la actividad actual así como todas las actividades que partan de ella.
              A diferencia de finish que sólo cerraría la actividad actual*/
            finishAffinity();
        }

        return super.onOptionsItemSelected(opcion_menu);
    }

    public void inicio(View view){

        Intent i = new Intent (this, MainActivity.class);

        startActivity(i);
    }

    public void insertar_noticia(View vier){

        Intent i = new Intent(this, InsertarNoticia.class);

        startActivity(i);
    }

    public void eliminar_noticia(View view){

        Intent i = new Intent(this, EliminarNoticia.class);

        startActivity(i);
    }
}
