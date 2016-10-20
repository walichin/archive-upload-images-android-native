package com.walichin.archivoalejos.carganegativos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by walteralejosgongora on 2/21/16.
 */
public class NegativeCardsActivity extends AppCompatActivity {

    MyFTPClientFunctions ftpclient = new MyFTPClientFunctions();
    Integer NEGATIVE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NegativeContent.NegativeCardsActivity = this;

        if (NegativeContent.CARDS.isEmpty()) {
            NegativeContent.CARDS.add(new NegativeContent.Card());
            NegativeContent.CURRENT_INDEX = 0;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negative_cards);

        final TextView newCardNumber = (TextView) findViewById(R.id.newCardNumber);
        final TextView newTitle = (TextView) findViewById(R.id.newTitle);
        final TextView newDescription = (TextView) findViewById(R.id.newDescription);
        final TextView newObservation = (TextView) findViewById(R.id.newObservation);

        final Button btnNewCard = (Button) findViewById(R.id.btnNewCard);
        final Button btnDeleteCard = (Button) findViewById(R.id.btnDeleteCard);
        final Button btnPrev3 = (Button) findViewById(R.id.btnPrev3);
        final Button btnCancel3 = (Button) findViewById(R.id.btnCancel3);
        final Button btnSave = (Button) findViewById(R.id.btnSave);

        btnNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newCardNumber.getText().toString().trim().isEmpty()) {

                    Snackbar.make(v, "Debe ingresar el numero de la ficha", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else if (newTitle.getText().toString().trim().isEmpty()) {

                    Snackbar.make(v, "Debe ingresar el titulo de la ficha", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {

                    // GUARDA LOS DATOS DE LA FICHA
                    NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).Save(
                            newCardNumber.getText().toString(),
                            newTitle.getText().toString(),
                            newDescription.getText().toString(),
                            newObservation.getText().toString());

                    // SI ES EL ULTIMO O UNICO ELEMENTO DE LA LISTA INSERTA AL FINAL
                    if ((NegativeContent.CURRENT_INDEX + 1) == NegativeContent.CARDS.size()) {

                        NegativeContent.CARDS.add(new NegativeContent.Card());
                        btnSave.setText("Grabar Datos");
                        btnSave.setBackgroundColor(0xFFFF4081);
                        btnSave.setTextColor(0xFFFFFFFF);

                    // CASO CONTRARIO INSERTA DENTRO DE LA LISTA
                    } else {

                        NegativeContent.CARDS.add(NegativeContent.CURRENT_INDEX + 1, new NegativeContent.Card());
                        btnSave.setText("Siguiente >>");
                        btnSave.setBackgroundColor(0xFFD5D5D5);
                        btnSave.setTextColor(0xFF000000);

                    }

                    newCardNumber.setText("");
                    newTitle.setText("");
                    newDescription.setText("");
                    newObservation.setText("");

                    NegativeContent.CURRENT_INDEX += 1;

                    setTitle("Datos de la Ficha ("+(NegativeContent.CURRENT_INDEX + 1)+"/"+NegativeContent.CARDS.size()+")");
                }
            }
        });

        btnDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // SI ES EL UNICO ELEMENTO DE LA LISTA SOLO LIMPIA, NOT REMOVE
                if (NegativeContent.CARDS.size() == 1) {

                    newCardNumber.setText("");
                    newTitle.setText("");
                    newDescription.setText("");
                    newObservation.setText("");

                    NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).Clear();

                } else {

                    // SI ES EL ULTIMO ELEMENTO DE LA LISTA
                    if ((NegativeContent.CURRENT_INDEX + 1) == NegativeContent.CARDS.size()) {

                        NegativeContent.CARDS.remove(NegativeContent.CURRENT_INDEX);
                        NegativeContent.CURRENT_INDEX -= 1;

                    } else {

                        NegativeContent.CARDS.remove(NegativeContent.CURRENT_INDEX);

                    }
                }

                newCardNumber.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).cardnumber);
                newTitle.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).title);
                newDescription.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).description);
                newObservation.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).observation);

                setTitle("Datos de la Ficha (" + (NegativeContent.CURRENT_INDEX + 1) + "/" + NegativeContent.CARDS.size() + ")");

                // SI AHORA ES EL ULTIMO ELEMENTO DE LA LISTA
                if ((NegativeContent.CURRENT_INDEX + 1) == NegativeContent.CARDS.size()) {
                    btnSave.setText("Grabar Datos");
                    btnSave.setBackgroundColor(0xFFFF4081);
                    btnSave.setTextColor(0xFFFFFFFF);
                }
            }
        });

        btnPrev3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // GUARDA LOS DATOS DE LA FICHA
                NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).Save(
                        newCardNumber.getText().toString(),
                        newTitle.getText().toString(),
                        newDescription.getText().toString(),
                        newObservation.getText().toString());

                // SI ES EL PRIMER ELEMENTO DE LA LISTA
                // REGRESA A LOS DATOS DEL NEGATIVO
                if (NegativeContent.CURRENT_INDEX == 0) {

                    onBackPressed();

                // CASO CONTRARIO RETROCEDE A LA FICHA ANTERIOR
                } else {

                    NegativeContent.CURRENT_INDEX -= 1;

                    newCardNumber.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).cardnumber);
                    newTitle.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).title);
                    newDescription.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).description);
                    newObservation.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).observation);

                    setTitle("Datos de la Ficha (" + (NegativeContent.CURRENT_INDEX + 1) + "/" + NegativeContent.CARDS.size() + ")");

                    // COMO NO ES EL ULTIMO DE LA LISTA CAMBIA LA ETIQUETA
                    btnSave.setText("Siguiente >>");
                    btnSave.setBackgroundColor(0xFFD5D5D5);
                    btnSave.setTextColor(0xFF000000);

                }
            }
        });

        btnCancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // REGRESA A LA BUSQUEDA DE NEGATIVOS
                if (NegativeContent.NegativeDetailActivity != null) {
                    NegativeContent.NegativeDetailActivity.finish();
                    NegativeContent.NegativeDetailActivity=null;
                }

                if (NegativeContent.NegativeImageActivity != null) {
                    NegativeContent.NegativeImageActivity.finish();
                    NegativeContent.NegativeImageActivity=null;
                }

                finish();

                Intent intent = getIntent();
                intent.setClass(getApplicationContext(), CardSearchActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newCardNumber.getText().toString().trim().isEmpty()) {

                    Snackbar.make(v, "Debe ingresar el numero de la ficha", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else if (newTitle.getText().toString().trim().isEmpty()) {

                    Snackbar.make(v, "Debe ingresar el titulo de la ficha", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {

                    // GUARDA LOS DATOS DE LA FICHA
                    NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).Save(
                            newCardNumber.getText().toString(),
                            newTitle.getText().toString(),
                            newDescription.getText().toString(),
                            newObservation.getText().toString());

                    // SI ES EL ULTIMO O UNICO ELEMENTO DE LA LISTA
                    if ((NegativeContent.CURRENT_INDEX + 1) == NegativeContent.CARDS.size()) {

                        CardContent.MensajeProcInfo(NegativeCardsActivity.this);

                        // GRABA TODA LA INFORMACION EN EL SERVIDOR
                        new SaveDataServer().execute("");

                    // CASO CONTRARIO AVANZA A LA SIGUIENTE FICHA
                    } else {

                        NegativeContent.CURRENT_INDEX += 1;

                        newCardNumber.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).cardnumber);
                        newTitle.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).title);
                        newDescription.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).description);
                        newObservation.setText(NegativeContent.CARDS.get(NegativeContent.CURRENT_INDEX).observation);

                        setTitle("Datos de la Ficha (" + (NegativeContent.CURRENT_INDEX + 1) + "/" + NegativeContent.CARDS.size() + ")");

                        // SI AHORA ES EL ULTIMO DE LA LISTA CAMBIA LA ETIQUETA AL BOTON PARA GRABAR
                        if ((NegativeContent.CURRENT_INDEX + 1) == NegativeContent.CARDS.size()) {
                            btnSave.setText("Grabar Datos");
                            btnSave.setBackgroundColor(0xFFFF4081);
                            btnSave.setTextColor(0xFFFFFFFF);
                        }
                    }
                }
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

    @Override
    /** Called when the user presses the Back hard button*/
    public void onBackPressed() {
        //startActivity(getIntent().setClass(getApplicationContext(), NegativeDetailActivity.class));

        Intent intent = this.getIntent();
        intent.setClass(this, NegativeDetailActivity.class);
        startActivity(intent);
    }

    private class SaveDataServer extends AsyncTask<String, Void, Boolean> {

        public StringBuilder responseOutput;
        public String errorMessage;

        protected Boolean doInBackground(String... urls) {

            if (!ftpclient.ftpConnect(CardContent.FTP_HOST, CardContent.FTP_USERNAME, CardContent.FTP_PASSWORD, 21)) {
                errorMessage = "Error al conectarse al servidor de imagenes";
                return false;
            }

            if (!ftpclient.ftpUpload(
                    //NegativeContent.NEGATIVE.filedir + "/" + NegativeContent.NEGATIVE.filename,
                    NegativeContent.NEGATIVE.pfd,
                    NegativeContent.NEGATIVE.filename,
                    CardContent.FTP_NEGATIVE_DIR,
                    getApplicationContext())) {
                errorMessage = "Error al subir la imagen al servidor";
                return false;
            }

            // GRABA EL NEGATIVO EN EL SERVIDOR
            try {

                URL url = new URL(CardContent.URL + "negative/new_negative_wk");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                //connection.setRequestMethod("GET");
                connection.setDoOutput(true);

                String urlParameters = MakeNegativeParams();
                //String urlParameters = "list_param={\"img_name\":\"walter.jpg\"}";
                //String urlParameters = "list_param={\"img_imgnro\":\"123\",\"img_server\":null,\"img_path\":null,\"img_name\":\"walter.jpg\",\"thickness\":null,\"width\":null,\"height\":null,\"is_restored\":\"1\",\"box\":\"x\",\"section\":\"x\",\"location\":\"x\",\"create_user\":\"x\"}";

                Log.d("SaveDataServer", "new_negative_wk: urlParameters: " + urlParameters);

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(urlParameters);
                dataOutputStream.flush();
                dataOutputStream.close();

                //int responseCode = connection.getResponseCode();

                String line = "";
                responseOutput = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while((line = bufferedReader.readLine()) != null ) {
                    responseOutput.append(line);
                }
                bufferedReader.close();

                Log.d("SaveDataServer", "new_negative_wk: responseOutput: " + responseOutput.toString());

            } catch (MalformedURLException e) {

                errorMessage = e.getMessage();
                e.printStackTrace();
                return false;

            } catch (IOException e) {

                errorMessage = e.getMessage();
                e.printStackTrace();
                return false;

            }

            // PROCESA EL RESULTADO DE CREAR EL NEGATIVO
            try {

                JSONObject jsonObject = new JSONObject(responseOutput.toString());
                NEGATIVE_ID = jsonObject.optInt("negative_id");

                Log.d("SaveDataServer", "NEGATIVE_ID: " + NEGATIVE_ID);

                if (NEGATIVE_ID == null || NEGATIVE_ID == 0) {
                    errorMessage = "Error al grabar el negativo";
                    return false;
                }

            } catch (JSONException e) {

                errorMessage = e.getMessage();
                e.printStackTrace();
                return false;

            }

            // GRABA LAS FICHAS EN EL SERVIDOR
            int numFichas = NegativeContent.CARDS.size();

            for (int i=0; i<numFichas; i++) {

                try {

                    URL url = new URL(CardContent.URL + "negative/new_card_wk");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    //connection.setRequestMethod("GET");
                    connection.setDoOutput(true);

                    String urlParameters = MakeCardParams(i);
                    //String urlParameters = "list_param={\"img_name\":\"walter.jpg\"}";
                    //String urlParameters = "list_param={\"img_imgnro\":\"123\",\"img_server\":null,\"img_path\":null,\"img_name\":\"walter.jpg\",\"thickness\":null,\"width\":null,\"height\":null,\"is_restored\":\"1\",\"box\":\"x\",\"section\":\"x\",\"location\":\"x\",\"create_user\":\"x\"}";

                    Log.d("SaveDataServer", "new_card_wk: urlParameters: " + urlParameters);

                    DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                    dataOutputStream.writeBytes(urlParameters);
                    dataOutputStream.flush();
                    dataOutputStream.close();

                    //int responseCode = connection.getResponseCode();

                    String line = "";
                    responseOutput = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while((line = bufferedReader.readLine()) != null ) {
                        responseOutput.append(line);
                    }
                    bufferedReader.close();

                    Log.d("SaveDataServer", "new_card_wk: responseOutput: " + responseOutput.toString());

                } catch (MalformedURLException e) {

                    errorMessage = e.getMessage();
                    e.printStackTrace();
                    return false;

                } catch (IOException e) {

                    errorMessage = e.getMessage();
                    e.printStackTrace();
                    return false;

                }

                // PROCESA EL RESULTADO DE CREAR UNA FICHA
                try {

                    JSONObject jsonObject = new JSONObject(responseOutput.toString());
                    Integer card_id = jsonObject.optInt("card_id");

                    Log.d("SaveDataServer", "CARD_ID: " + card_id);

                    if (card_id == null || card_id == 0) {
                        errorMessage = "Error al grabar la ficha";
                        return false;
                    }

                } catch (JSONException e) {

                    errorMessage = e.getMessage();
                    e.printStackTrace();
                    return false;

                }

            }

            // SUCCESS
            return true;
        }

        protected void onPostExecute(Boolean result) {

            CardContent.handler.sendEmptyMessage(0);

            if (result) {

                // DESPUES DE GRABAR
                AlertDialog.Builder builder = new AlertDialog.Builder(NegativeCardsActivity.this);
                builder.setTitle("Negativo ID:" + NEGATIVE_ID)
                        .setMessage("Se grabaron los datos correctamente")
                        .setCancelable(false)
                        .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                                // INICIALIZA DATOS DEL NEGATIVO Y SUS FICHAS EN MEMORIA
                                NegativeContent.NEGATIVE = new NegativeContent.Negative();
                                NegativeContent.CARDS = new ArrayList<>();
                                NegativeContent.CARDS.add(new NegativeContent.Card());
                                NegativeContent.CURRENT_INDEX = 0;

                                // REGRESA A LA PRIMERA ACTIVIDAD DEL NEGATIVO
                                if (NegativeContent.NegativeDetailActivity != null) {
                                    NegativeContent.NegativeDetailActivity.finish();
                                    NegativeContent.NegativeDetailActivity = null;
                                }

                                if (NegativeContent.NegativeImageActivity != null) {
                                    NegativeContent.NegativeImageActivity.finish();
                                    NegativeContent.NegativeImageActivity = null;
                                }

                                finish();

                                Intent intent = getIntent();
                                intent.setClass(getApplicationContext(), NegativeImageActivity.class);
                                startActivity(intent);

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {

                View v = findViewById(R.id.card_linear_layout);
                Snackbar.make(v, errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }
    }

    private String MakeNegativeParams() {

        String result = "list_param={";
        result += "\"img_server\":\"" + CardContent.NEGATIVE_HOST + "\"";

        String box = NegativeContent.NEGATIVE.box.trim();
        result += ",\"box\":" + (box.isEmpty() ? "null" : "\""+box+"\"");

        String section = NegativeContent.NEGATIVE.section.trim();
        result += ",\"section\":" + (section.isEmpty() ? "null" : "\""+section+"\"");

        String location = NegativeContent.NEGATIVE.location.trim();
        result += ",\"location\":" + (location.isEmpty() ? "null" : "\""+location+"\"");

        String is_restored = NegativeContent.NEGATIVE.is_restored.trim();
        result += ",\"is_restored\":" + (is_restored.isEmpty() ? "null" : "\""+is_restored+"\"");

        String height = NegativeContent.NEGATIVE.height.trim();
        result += ",\"height\":" + (height.isEmpty() ? "null" : height);

        String width = NegativeContent.NEGATIVE.width.trim();
        result += ",\"width\":" + (width.isEmpty() ? "null" : width);

        String thickness = NegativeContent.NEGATIVE.thickness.trim();
        result += ",\"thickness\":" + (thickness.isEmpty() ? "null" : thickness);

        String img_name = NegativeContent.NEGATIVE.filename.trim();
        result += ",\"img_name\":" + (img_name.isEmpty() ? "null" : "\""+img_name+"\"");

        String img_path = CardContent.NEGATIVE_PATH + NegativeContent.NEGATIVE.filename.trim();
        result += ",\"img_path\":" + (img_path.isEmpty() ? "null" : "\""+img_path+"\"");

        String img_imgnro = NegativeContent.NEGATIVE.imgnro.trim();
        result += ",\"img_imgnro\":" + (img_imgnro.isEmpty() ? "null" : img_imgnro);

        String create_user = CardContent.APP_USERNAME.trim();
        result += ",\"create_user\":" + (create_user.isEmpty() ? "null" : "\""+create_user+"\"");

        result += "}";

        return result;
    }

    private String MakeCardParams(int index) {

        String result = "list_param={";
        result += "\"archive_id\":" + CardContent.ARCHIVE_ID;

        if (NEGATIVE_ID > 0)
            result += ",\"negative_id\":" + NEGATIVE_ID;

        String card_number = NegativeContent.CARDS.get(index).cardnumber.trim();
        result += ",\"card_number\":" + (card_number.isEmpty() ? "null" : card_number);

        String title = NegativeContent.CARDS.get(index).title.trim();
        result += ",\"title\":" + (title.isEmpty() ? "null" : "\""+title+"\"");

        String description = NegativeContent.CARDS.get(index).description.trim();
        result += ",\"description\":" + (description.isEmpty() ? "null" : "\""+description+"\"");

        String observation = NegativeContent.CARDS.get(index).observation.trim();
        result += ",\"observation\":" + (observation.isEmpty() ? "null" : "\""+observation+"\"");

        String create_user = CardContent.APP_USERNAME.trim();
        result += ",\"create_user\":" + (create_user.isEmpty() ? "null" : "\""+create_user+"\"");

        result += "}";

        return result;
    }

}
