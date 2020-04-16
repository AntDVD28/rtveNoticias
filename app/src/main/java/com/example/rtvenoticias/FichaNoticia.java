package com.example.rtvenoticias;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FichaNoticia extends AppCompatActivity {

    private Toolbar toolbar;

    EditText etTitular;
    EditText etFecha;
    EditText etEnlace;
    CheckBox cbLeido;
    CheckBox cbFavorita;
    RadioButton rbFiable;
    RadioButton rbNoFiable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha_noticia);

        //Registramos la Toolbar y la activamos
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Rescato la información
        Bundle datos = getIntent().getExtras();
        String id = datos.getString("id");
        //Toast.makeText(getApplicationContext(), "Id:"+id, Toast.LENGTH_LONG).show();

        //Creamos una instancia para poder acceder a la BD
        final HelperBD dbHelper = new HelperBD(this);

        //Nos conectamos a la BD en modo lectura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Definimoa las columnas a devolver en la consulta
        String[] projection = {
                EstructuraBD.COLUMN_1_NAME,
                EstructuraBD.COLUMN_2_NAME,
                EstructuraBD.COLUMN_3_NAME,
                EstructuraBD.COLUMN_4_NAME,
                EstructuraBD.COLUMN_5_NAME,
                EstructuraBD.COLUMN_6_NAME,
                EstructuraBD.COLUMN_7_NAME
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = EstructuraBD.COLUMN_1_NAME + " = ?";
        String[] selectionArgs = {id} ;

        final Cursor cursor = db.query(
                EstructuraBD.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null
        );
        //Tenemos que ejecutar esta instrucción para situarnos en el primer registro
        cursor.moveToNext();

        //Guardo el resultado en un objeto de tipo noticia

        Noticia noticia = new Noticia(

                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6)
        );

        //Rellenar los views de la interfaz con los datos guardados en el objeto de tipo Noticia
        etTitular = (EditText) findViewById(R.id.etTitular);
        etTitular.setText(noticia.getTitulo());
        etFecha = (EditText) findViewById(R.id.etFecha);
        etFecha.setText(noticia.getFecha());
        etEnlace = (EditText) findViewById(R.id.etEnlace);
        etEnlace.setText(noticia.getEnlace());

        cbLeido = (CheckBox) findViewById(R.id.cbLeido);
        if(noticia.getLeido() == 1)
            cbLeido.setChecked(true);

        cbFavorita = (CheckBox) findViewById(R.id.cbFavorita);
        if(noticia.getFavorito() == 1)
            cbFavorita.setChecked(true);

        rbFiable = (RadioButton) findViewById(R.id.rbFiable);
        rbNoFiable = (RadioButton) findViewById(R.id.rbNoFiable);
        if(noticia.getFuente() == 1)
            rbFiable.setChecked(true);
        else
            rbNoFiable.setChecked(true);

        Button btActualizar = (Button) findViewById(R.id.btActualizar);

        btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int leido=0, favorito=0, fuente=0;

                if(cbLeido.isChecked())
                    leido = 1;

                if(cbFavorita.isChecked())
                    favorito = 1;

                if(rbFiable.isChecked())
                    fuente = 1;

                if(rbNoFiable.isChecked())
                    fuente = 0;

                //Rescato los valores de los views y los guardo en un objeto de tipo noticia
                Noticia noticia = new Noticia(
                        cursor.getInt(0),
                        etTitular.getText().toString(),
                        etEnlace.getText().toString(),
                        etFecha.getText().toString(),
                        leido,
                        favorito,
                        fuente
                );

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(EstructuraBD.COLUMN_1_NAME, noticia.getId());
                values.put(EstructuraBD.COLUMN_2_NAME, noticia.getTitulo());
                values.put(EstructuraBD.COLUMN_3_NAME, noticia.getEnlace());
                values.put(EstructuraBD.COLUMN_4_NAME, noticia.getFecha());
                values.put(EstructuraBD.COLUMN_5_NAME, noticia.getLeido());
                values.put(EstructuraBD.COLUMN_5_NAME, noticia.getLeido());
                values.put(EstructuraBD.COLUMN_6_NAME, noticia.getFavorito());
                values.put(EstructuraBD.COLUMN_7_NAME, noticia.getFuente());


                String selection = EstructuraBD.COLUMN_1_NAME + " LIKE ?";
                String[] selectionArgs = { noticia.getId().toString() };

                int count = db.update(
                        EstructuraBD.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                //Mostramos mensaje
                Toast.makeText(getApplicationContext(),"Valoración guardada",Toast.LENGTH_SHORT).show();

                //Volvemos a la página de inicio
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

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
