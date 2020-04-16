package com.example.rtvenoticias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton boton_ver_noticias;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Registramos la Toolbar y la activamos
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Registramos el boton para ver las noticias
        boton_ver_noticias = (ImageButton) findViewById(R.id.imageButton);

        //Evento clic en el botón
        boton_ver_noticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Debemos de comprobar si existen registros en la BD
                //Creamos una instancia para poder acceder a la BD
                final HelperBD dbHelper = new HelperBD(getApplicationContext());

                //Listamos las noticias
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String[] projection = {

                        EstructuraBD.COLUMN_1_NAME

                };

                Cursor cursor = db.query(
                        EstructuraBD.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                if(cursor.getCount() > 0 ){

                    Intent i = new Intent(getApplicationContext(), ListadoNoticias.class);

                    startActivity(i);

                }else {

                    Toast.makeText(getApplicationContext(),"No existen registros.",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    //Métodos para las opciones del menú
    @Override
    public boolean onCreateOptionsMenu(Menu mimenu){

        getMenuInflater().inflate(R.menu.menu_inicio, mimenu);

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

        if(id == R.id.externo){

            conexion_servidor_externo(null);

            return true;
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

    public void conexion_servidor_externo(View view){

        Intent i = new Intent(this, ServidorExterno.class);

        startActivity(i);
    }

}
