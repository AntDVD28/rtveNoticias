package com.example.rtvenoticias;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ServidorExterno extends AppCompatActivity {

    EditText etTitulo;
    EditText etEnlace;
    EditText etFecha;

    Button btEnviar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servidor_externo);

        //Obtengo una referencia a los views
        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etEnlace = (EditText) findViewById(R.id.etEnlace);
        etFecha = (EditText) findViewById(R.id.etFecha);
        btEnviar = (Button) findViewById(R.id.btInsertarNoticia);

        //Obtenemos la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        //Inserto la fecha
        etFecha.setText(fecha);
        etFecha.setEnabled(false);

        //Evento clic en el botón
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtengo los valores
                String titulo = etTitulo.getText().toString();
                String enlace = etEnlace.getText().toString();
                String fecha = etFecha.getText().toString();
                new EnviarDatos(ServidorExterno.this).execute(titulo, enlace, fecha);
            }
        });
    }

    public static class EnviarDatos extends AsyncTask<String, Void, String>{

        private WeakReference<Context> context;

        public EnviarDatos(Context context) {
            this.context = new WeakReference<>(context);
        }

        protected String doInBackground(String... params) {
            String dir_url = "http://antdvd.000webhostapp.com/registrar_noticia.php";
            String resultado;

            try {

                URL url = new URL(dir_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String titulo = params[0];
                String enlace = params[1];
                String fecha = params[2];
                //Al dar de alta una noticia los siguientes campos por defecto serán cero
                //Debemos de pasarlos como string
                String leido = "0";
                String favorito = "0";
                String fuente = "0";

                String data = URLEncoder.encode("titulo", "UTF-8") + "=" + URLEncoder.encode(titulo, "UTF-8")
                        + "&" + URLEncoder.encode("enlace", "UTF-8") + "=" + URLEncoder.encode(enlace, "UTF-8")
                        + "&" + URLEncoder.encode("fecha", "UTF-8") + "=" + URLEncoder.encode(fecha, "UTF-8")
                        + "&" + URLEncoder.encode("leido", "UTF-8") + "=" + URLEncoder.encode(leido, "UTF-8")
                        + "&" + URLEncoder.encode("favorito", "UTF-8") + "=" + URLEncoder.encode(favorito, "UTF-8")
                        + "&" + URLEncoder.encode("fuente", "UTF-8") + "=" + URLEncoder.encode(fuente, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while((line = bufferedReader.readLine()) != null){

                    stringBuilder.append(line);
                }
                resultado = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            }catch (MalformedURLException mue){
                Log.d("Mi_app", "URL con formato incorrecto");
                resultado ="Se ha producido un error";
            } catch (IOException ioe){
                Log.d("Mi_app", "Problemas de conexión");
                resultado = "Compruebe la conexión de red";
            }
            return resultado;
        }

        protected void onPostExecute(String resultado) {
            Toast.makeText(context.get(), resultado, Toast.LENGTH_LONG).show();
        }


    }
}
