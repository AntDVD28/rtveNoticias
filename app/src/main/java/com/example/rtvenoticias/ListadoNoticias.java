package com.example.rtvenoticias;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ListadoNoticias extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView lvNoticias;

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
                projection,                // The array of columns to return (pass null to get all)
                //selection,               // The columns for the WHERE clause
                null,
                //selectionArgs,           // The values for the WHERE clause
                null,
                null,             // don't group the rows
                null,              // don't filter by row groups
                //sortOrder               // The sort order
                null
        );



        //Guardo el resultado de la consulta en una lista de objetos del tipo Noticia
        Noticia noticia = null;
        listaDeNoticias = new ArrayList<Noticia>();
        while (cursor.moveToNext()) {
            noticia = new Noticia();
            noticia.setId(cursor.getInt(0));
            noticia.setTitulo(cursor.getString(1));
            //En el caso del campo fuente guardo en el objeto el identificador que corresponde al icono
            if (cursor.getInt(2) == 0)
                noticia.setFuente(android.R.drawable.presence_busy);
            else
                noticia.setFuente(android.R.drawable.presence_online);
            //System.out.println(noticia.toString());

            //Agrego el objeto de tipo noticia a la lista
            listaDeNoticias.add(noticia);
        }


        //Debo de utilizar un adaptador para volcar la lista de objetos de tipo noticia en el ListView
        //Tengo que crear uno nuevo para poder incluir los iconos
        lvNoticias = (ListView) findViewById(R.id.lvNoticias);
        lvNoticias.setAdapter(new ListNoticiasAdapter(this, listaDeNoticias));

        //Evento hacer clic sobre una fila del listView
        lvNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Envío el id de la noticia seleccionada a la activity FichaNoticia
                Intent i = new Intent(getApplicationContext(), FichaNoticia.class);
                i.putExtra("id", listaDeNoticias.get(position).getId().toString());
                startActivity(i);

            }
        });

    }//fin del método OnCreate

    static class ListNoticiasAdapter extends BaseAdapter {

        private final Context context;
        private final ArrayList<Noticia> listaDeNoticias;

        public ListNoticiasAdapter(Context context, ArrayList<Noticia> listaDeNoticias) {

            this.context = context;
            this.listaDeNoticias = listaDeNoticias;
        }

        @Override
        public int getCount(){
            return listaDeNoticias.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);


            Noticia noticia = listaDeNoticias.get(position);
            ListViewHolder holder;
            //El proceso de generar views requiere de muchos recursos por lo que tendremos que desarrollar un mecanismo para que nuestro código sea más eficiente
            //Dicho mecanismo consistirá en reciclar views, para ello utilizamos viewHolder
            //Si no recibimos un view, a través de inflate lo construiremos utilizando el layout linea_listview
            //Además reciclaremos dicho view a través de ViewHolder etiquetándolo para un futuro uso
            if (view == null) {
                view = inflater.inflate(R.layout.linea_listview, viewGroup, false);
                holder = new ListViewHolder();
                holder.tvId = (TextView) view.findViewById(R.id.tvId);
                holder.tvTitulo = (TextView) view.findViewById(R.id.tvTitulo);
                holder.imgFuente = (ImageView) view.findViewById(R.id.imgFuente);

                view.setTag(holder);
            //Al existir el view, hacemos uso del ya creado
            } else {
                Log.d("ListView", "RECYCLED");
                holder = (ListViewHolder) view.getTag();
            }

            //Creamos la interfaz gráfica
            holder.tvId.setText(noticia.getId().toString());
            holder.tvTitulo.setText(noticia.getTitulo());
            holder.imgFuente.setImageResource(noticia.getFuente());
            return view;
        }

        static class ListViewHolder {
            TextView tvId;
            TextView tvTitulo;
            ImageView imgFuente;
        }

    }

    //Método auxiliar para recortar una cadena de texto al número de caracteres indicado.
    public static String recortaCadena(String cadena, int num_caracteres){

        String cadena_recortada = cadena.substring(0, num_caracteres);

        cadena_recortada = cadena_recortada + "...";

        return cadena_recortada;
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

    public void insertar_noticia(View view){

        Intent i = new Intent(this, InsertarNoticia.class);

        startActivity(i);
    }

    public void eliminar_noticia(View view){

        Intent i = new Intent(this, EliminarNoticia.class);

        startActivity(i);
    }
}
