package com.example.rtvenoticias;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ListadoNoticias_copia extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView lvNoticias;
    //Lista de Strings que mostraré en el ListView
    private ArrayList<String> listaDeCadenas;
    //Lista de objetos de tipo Noticia que utilizaré para volcar el resultado de la consulta a la BD
    private ArrayList<Noticia> listaDeNoticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_noticias_old);

        //Registramos la Toolbar y la activamos
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        //Creamos una instancia para poder acceder a la BD
        final HelperBD dbHelper = new HelperBD(this);

        //Listamos las noticias
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                //BaseColumns._ID,
                EstructuraBD.COLUMN_1_NAME,
                EstructuraBD.COLUMN_2_NAME,
                EstructuraBD.COLUMN_7_NAME
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder =
        //        FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                EstructuraBD.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                //selection,              // The columns for the WHERE clause
                null,
                //selectionArgs,          // The values for the WHERE clause
                null,
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                //sortOrder               // The sort order
                null
        );

        //Guardo el resultado de la consulta en una lista de objetos del tipo Noticia
        Noticia noticia = null;
        listaDeNoticias = new ArrayList<Noticia>();
        while(cursor.moveToNext()) {
            noticia = new Noticia();
            noticia.setId(cursor.getInt(0));
            noticia.setTitulo(cursor.getString(1));
            noticia.setFuente(cursor.getInt(2));

            listaDeNoticias.add(noticia);
        }

        //Vuelvo la lista de objetos de tipo noticia en una lista de cadenas
        listaDeCadenas = new ArrayList<String>();
        for(int i=0; i<listaDeNoticias.size();i++){
            listaDeCadenas.add(listaDeNoticias.get(i).getId()+" - "
                    +listaDeNoticias.get(i).getTitulo()+" - "
                    +listaDeNoticias.get(i).getFuente());
        }

        //Utilizamos un adaptador para pasar la lista de cadenas obtenida al ListView
        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaDeCadenas);
        lvNoticias = (ListView) findViewById(R.id.lvNoticias);
        lvNoticias.setAdapter(adaptador);

       lvNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               String cadena ="id: "+listaDeNoticias.get(position).getId()+"\n";
               cadena+="Titulo: "+listaDeNoticias.get(position).getTitulo()+"\n";
               cadena+="Fuente: "+listaDeNoticias.get(position).getFuente()+"\n";

               Toast.makeText(getApplicationContext(),cadena,Toast.LENGTH_LONG).show();



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
