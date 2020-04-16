package com.example.rtvenoticias;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class EliminarNoticia extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView lvNoticias;
    //Lista de Strings que mostraré en el ListView
    private ArrayList<String> listaDeCadenas;
    //Lista de objetos de tipo Noticia que utilizaré para volcar el resultado de la consulta a la BD
    private ArrayList<Noticia> listaDeNoticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eliminar_noticia);

        //Registramos la Toolbar y la activamos
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Creamos una instancia para poder acceder a la BD
        final HelperBD dbHelper = new HelperBD(this);

        //Listamos las noticias
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Indicamos columnas a devolver en la consulta
        String[] projection = {
                //BaseColumns._ID,
                EstructuraBD.COLUMN_1_NAME,
                EstructuraBD.COLUMN_2_NAME
        };

        //El resultado de la consulta lo guardamos en un objeto de tipo Cursor
        Cursor cursor = db.query(
                EstructuraBD.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        //Guardo el resultado de la consulta en una lista de objetos del tipo Noticia
        Noticia noticia = null;
        listaDeNoticias = new ArrayList<Noticia>();
        while(cursor.moveToNext()) {
            noticia = new Noticia();
            noticia.setId(cursor.getInt(0));
            noticia.setTitulo(cursor.getString(1));

            //Agrego el objeto de tipo noticia a la lista
            listaDeNoticias.add(noticia);
        }

        //Vuelco la lista de objetos de tipo noticia en una lista de cadenas
        listaDeCadenas = new ArrayList<String>();
        for(int i=0; i<listaDeNoticias.size();i++){
            listaDeCadenas.add(
                    listaDeNoticias.get(i).getId()+" - "
                    +listaDeNoticias.get(i).getTitulo());
        }

        //Utilizamos un adaptador para pasar la lista de cadenas obtenida al ListView
        final ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaDeCadenas);
        lvNoticias = (ListView) findViewById(R.id.lvNoticias);
        lvNoticias.setAdapter(adaptador);

        lvNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                //Mostramos un alert para confirmar la eliminación
                AlertDialog.Builder alerta = new AlertDialog.Builder(EliminarNoticia.this);
                alerta.setMessage("¿Desea eliminar la noticia "+listaDeNoticias.get(position).getId().toString()+"?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Eliminamos la noticia
                                String selection = EstructuraBD.COLUMN_1_NAME + " LIKE ?";
                                String[] selectionArgs = { listaDeNoticias.get(position).getId().toString() };
                                int deletedRows = db.delete(EstructuraBD.TABLE_NAME, selection, selectionArgs);

                                //Debemos refrescar la Activity
                                Intent refresh = new Intent(getApplicationContext(), EliminarNoticia.class);
                                startActivity(refresh);

                                //Mostramos mensaje
                                Toast.makeText(getApplicationContext(),"Datos eliminados",Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Eliminar noticia");
                titulo.show();

            }
        });
    }//fin del método OnCreate





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
