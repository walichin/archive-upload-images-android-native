package com.walichin.archivoalejos.carganegativos;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by walteralejosgongora on 2/21/16.
 */
public class CardSearchActivity extends AppCompatActivity {

    public static final String ARG_PARAMS = "params";
    //private Handler handler;
    //private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_search);

        CardContent.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                CardContent.progress.dismiss();
                super.handleMessage(msg);
            }
        };

        final EditText txtNegativeId = (EditText) findViewById(R.id.txtNegativeId);
        final EditText txtFileName = (EditText) findViewById(R.id.txtFileName);
        final EditText txtImgnro = (EditText) findViewById(R.id.txtImgnro);
        final EditText txtBox = (EditText) findViewById(R.id.txtBox);
        final EditText txtSection = (EditText) findViewById(R.id.txtSection);
        final EditText txtLocation = (EditText) findViewById(R.id.txtLocation);

        final Button btnSearch = (Button) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSearch.setEnabled(false);
                CardContent.MensajeProcInfo(CardSearchActivity.this);

                //Toast.makeText(SearchActivity.this, "Espere un momento por favor...", Toast.LENGTH_SHORT).show();
                //Snackbar.make(v, "Espere un momento por favor...", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                String params_url = "?limit=" + CardContent.LIMIT;

                String negative_id_search = txtNegativeId.getText().toString().trim();
                String filename_search = txtFileName.getText().toString().trim();
                String imgnro_search = txtImgnro.getText().toString().trim();
                String box_search = txtBox.getText().toString().trim();
                String section_search = txtSection.getText().toString().trim();
                String location_search = txtLocation.getText().toString().trim();

                if (!negative_id_search.isEmpty()) {
                    //if (params_url.length() > 0) { params_url += "&"; }
                    params_url += "&negative_id_search=" + negative_id_search;
                }
                if (!filename_search.isEmpty()) {
                    //if (params_url.length() > 0) { params_url += "&"; }
                    params_url += "&filename_search=" + filename_search;
                }
                if (!imgnro_search.isEmpty()) {
                    //if (params_url.length() > 0) { params_url += "&"; }
                    params_url += "&imgnro_search=" + imgnro_search;
                }
                if (!box_search.isEmpty()) {
                    //if (params_url.length() > 0) { params_url += "&"; }
                    params_url += "&box_search=" + box_search;
                }
                if (!section_search.isEmpty()) {
                    //if (params_url.length() > 0) { params_url += "&"; }
                    params_url += "&section_search=" + section_search;
                }
                if (!location_search.isEmpty()) {
                    //if (params_url.length() > 0) { params_url += "&"; }
                    params_url += "&location_search=" + location_search;
                }
                //if (params_url.length() > 0) { params_url = "?" + params_url; }

                CardContent.PARAMS_SEARCH = params_url;
                CardContent.OFFSET = 0;
                params_url = params_url + "&offset=" + CardContent.OFFSET + "&aditional_data=true";

                LeeFichas leeFichas = new LeeFichas();
                leeFichas.execute(CardContent.URL + "negative/search_wk", params_url);

            }
        });

        Button btnClear = (Button) findViewById(R.id.btnClear);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNegativeId.setText("");
                txtFileName.setText("");
                txtImgnro.setText("");
                txtBox.setText("");
                txtSection.setText("");
                txtLocation.setText("");
            }
        });

        Button btnNewNegative = (Button) findViewById(R.id.btnNewNegative);

        btnNewNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // INICIALIZA DATOS DEL NEGATIVO Y SUS FICHAS EN MEMORIA
                NegativeContent.NEGATIVE = new NegativeContent.Negative();
                NegativeContent.CARDS = new ArrayList<>();
                NegativeContent.CARDS.add(new NegativeContent.Card());
                NegativeContent.CURRENT_INDEX = 0;

                Intent intent = getIntent();
                intent.setClass(getApplicationContext(), NegativeImageActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

    class LeeFichas extends AsyncTask<String, Void, String> {

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

                    Log.d("LeeFichas", stringBuilder.toString());
                    return stringBuilder.toString();
                } else {
                    Log.e("LeeFichas", connection.getResponseMessage());
                    return null;
                }
            } catch (Exception exception){
                Log.e("LeeFichas", exception.toString());
                return null;
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("LeeFichas", "Result was: " + result);

            CardContent.parse_JSON_Cards(result, true);

            Button btnSearch = (Button) findViewById(R.id.btnSearch);
            btnSearch.setEnabled(true);

            // ALWAYS TO CLOSE THE DIALOG BEFORE TO LEAVE THE LAYOUT
            // (TO AVOID THE INFINITE LOOP WHEN COMES BACK)
            CardContent.handler.sendEmptyMessage(0);

            if (CardContent.NUM_REG == 0) {

                //Toast.makeText(CardSearchActivity.this, "Espere un momento por favor...", Toast.LENGTH_SHORT).show();
                View v = findViewById(R.id.search_linear_layout);
                Snackbar.make(v, "No se encontraron registros", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            } else {

                LinearLayout searchLinearLayout= (LinearLayout) findViewById(R.id.search_linear_layout);
                Context context = searchLinearLayout.getContext();
                Intent intent = new Intent(context, CardListActivity.class);
                //intent.putExtra(ARG_PARAMS, holder.mItem.id);
                context.startActivity(intent);

            }
        }
    }
}
