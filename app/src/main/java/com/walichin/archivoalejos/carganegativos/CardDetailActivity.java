package com.walichin.archivoalejos.carganegativos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.walichin.archivoalejos.carganegativos.adapters.ItemClickListener;
import com.walichin.archivoalejos.carganegativos.adapters.Section;
import com.walichin.archivoalejos.carganegativos.adapters.SectionedExpandableLayoutHelper;
import com.walichin.archivoalejos.carganegativos.models.Image;
import com.walichin.archivoalejos.carganegativos.models.Item;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class CardDetailActivity extends AppCompatActivity implements ItemClickListener {

    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //setting the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this,
                mRecyclerView, this, 2);


        final FloatingActionButton fab_previous = (FloatingActionButton) findViewById(R.id.search_previous2);
        fab_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sectionedExpandableLayoutHelper.mSectionMap.clear();
                sectionedExpandableLayoutHelper.mSectionDataMap.clear();
                fab_previous.setEnabled(false);
                CardContent.NEGATIVE_ID = CardContent.EXTRA_DATA.optJSONObject(CardContent.NEGATIVE_ID).optString("previous_negative_id");
                CardContent.MensajeProcInfo(CardDetailActivity.this);
                CargaFicha cargaFicha = new CargaFicha();
                cargaFicha.execute(CardContent.URL + "negative/view_wk/", CardContent.NEGATIVE_ID);
            }
        });

        final FloatingActionButton fab_next = (FloatingActionButton) findViewById(R.id.search_next2);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sectionedExpandableLayoutHelper.mSectionMap.clear();
                sectionedExpandableLayoutHelper.mSectionDataMap.clear();
                fab_next.setEnabled(false);
                CardContent.NEGATIVE_ID = CardContent.EXTRA_DATA.optJSONObject(CardContent.NEGATIVE_ID).optString("next_negative_id");
                CardContent.MensajeProcInfo(CardDetailActivity.this);
                CargaFicha cargaFicha = new CargaFicha();
                cargaFicha.execute(CardContent.URL + "negative/view_wk/", CardContent.NEGATIVE_ID);
            }
        });

        CardContent.MensajeProcInfo(CardDetailActivity.this);
        CargaFicha cargaFicha = new CargaFicha();
        cargaFicha.execute(CardContent.URL + "negative/view_wk/", CardContent.NEGATIVE_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //navigateUpTo(new Intent(this, CardSearchActivity.class));

            // EL MISMO EFECTO QUE PRESIONAR EL BOTON "BACK"
            // EL BOTON "BACK" POR DEFECTO EJECUTA finish(); QUE TERMINA/CIERRA LA ACTIVIDAD ACTUAL
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(Item item) {
        Toast.makeText(this, "Item: " + item.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClicked(Section section) {
        Toast.makeText(this, "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    /****************** CARGA UNA FOTO *******************/

    public void GuardaFoto(String filename, String filepath, String fileserver) {

        CargaFoto cargaFoto = new CargaFoto(filename, filepath, fileserver);
        cargaFoto.execute(fileserver + filepath);

    }

    private class CargaFoto extends AsyncTask<String, Void, Bitmap> {
        String filename;
        String filepath;
        String fileserver;

        public CargaFoto(String filename, String filepath, String fileserver) {
            this.filename = filename;
            this.filepath = filepath;
            this.fileserver = fileserver;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("CargaFoto", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            ArrayList<Object> arrayListImage = new ArrayList<>();
            arrayListImage.add(new Image(filename, filepath, fileserver, result));
            sectionedExpandableLayoutHelper.addSection("Imagen", true, arrayListImage);

            Guarda_Fichas_Datos_Adicionales();

            FloatingActionButton fab_previous = (FloatingActionButton) findViewById(R.id.search_previous2);
            fab_previous.setEnabled(true);

            FloatingActionButton fab_next = (FloatingActionButton) findViewById(R.id.search_next2);
            fab_next.setEnabled(true);

            // apaga la ventana de "procesando informacion..."
            CardContent.handler.sendEmptyMessage(0);

            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }

    /****************** CARGA UNA FICHA *******************/

    class CargaFicha extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;

            try {

                String urlpath = params[0] + params[1];
                String json_string = "";

                URL url = new URL(urlpath);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());

                streamWriter.write(json_string);

                streamWriter.flush();
                StringBuilder stringBuilder = new StringBuilder();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String response = null;
                    while ((response = bufferedReader.readLine()) != null) {
                        stringBuilder.append(response + "\n");
                    }
                    bufferedReader.close();

                    Log.d("CargaFicha", stringBuilder.toString());
                    return stringBuilder.toString();
                } else {
                    Log.e("CargaFicha", connection.getResponseMessage());
                    return null;
                }
            } catch (Exception exception){
                Log.e("CargaFicha", exception.toString());
                return null;
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("CargaFicha", "Result was: " + result);

            parse_JSON (result);

            // GUARDA LA FICHA

            ArrayList<Object> arrayList = new ArrayList<>();
            //arrayList.add(new Item(CardContent.NEGATIVE.optString("negative_id"), "Negativo Id"));
            //arrayList.add(new Item(CardContent.NEGATIVE.optString("img_imgnro"), "Negativo Num"));
            arrayList.add(new Item(CardContent.NEGATIVE.optString("img_name"), "Nombre de la Imagen"));
            sectionedExpandableLayoutHelper.addSection(
                    "ID: " + CardContent.NEGATIVE.optString("negative_id") +
                    " - Numero: " + CardContent.NEGATIVE.optString("img_imgnro"),
                    true, arrayList);

            // CARGA EL BITMAP DE LA FOTO

            String fileName = CardContent.NEGATIVE.optString("img_name");

            if (fileName != null && fileName.trim().length() > 0) {

                GuardaFoto(fileName,
                        CardContent.NEGATIVE.optString("img_path"),
                        CardContent.NEGATIVE.optString("img_server"));

            } else {

                sectionedExpandableLayoutHelper.addSection("Imagen", false, new ArrayList());

                Guarda_Fichas_Datos_Adicionales();

                FloatingActionButton fab_previous = (FloatingActionButton) findViewById(R.id.search_previous2);
                fab_previous.setEnabled(true);

                FloatingActionButton fab_next = (FloatingActionButton) findViewById(R.id.search_next2);
                fab_next.setEnabled(true);

                // apaga la ventana de "procesando informacion..."
                CardContent.handler.sendEmptyMessage(0);

                sectionedExpandableLayoutHelper.notifyDataSetChanged();

            }
        }

        public void parse_JSON (String strJson) {

            try {

                JSONObject jsonRootObject = new JSONObject(strJson);
                CardContent.NEGATIVE = jsonRootObject.getJSONObject("item");
                //CardContent.NEGATIVE_ID = CardContent.NEGATIVE.optString("card_id");

                CardContent.CARDS = CardContent.NEGATIVE.getJSONArray("cards");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /************ GUARDA LAS FICHAS Y DATOS ADICIONALES *************/

    public void Guarda_Fichas_Datos_Adicionales () {

        ArrayList<Object> arrayList;

        if (CardContent.CARDS == null || CardContent.CARDS.length() == 0) {
            sectionedExpandableLayoutHelper.addSection("Fichas", false, new ArrayList());
        } else {

            arrayList = new ArrayList<>();
            for (int i = 0; i < CardContent.CARDS.length(); i++) {
                arrayList.add(new Item(
                        CardContent.CARDS.optJSONObject(i).optString("title") + " | " +
                        CardContent.CARDS.optJSONObject(i).optString("description"),
                        "ID Ficha: " + CardContent.CARDS.optJSONObject(i).optString("card_id") +
                        " - Num.Ficha: " + CardContent.CARDS.optJSONObject(i).optString("card_number")));
            }
            sectionedExpandableLayoutHelper.addSection("Fichas", true, arrayList);
        }

        arrayList = new ArrayList<>();
        arrayList.add(new Item(CardContent.NEGATIVE.optString("box"), "box"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("section"), "section"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("location"), "location"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("is_restored"), "is_restored"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("height"), "height"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("width"), "width"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("thickness"), "thickness"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("img_path"), "img_path"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("img_server"), "img_server"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("create_user"), "create_user"));
        arrayList.add(new Item(CardContent.NEGATIVE.optString("create_date"), "create_date"));
        sectionedExpandableLayoutHelper.addSection("Datos adicionales", true, arrayList);
    }

}
