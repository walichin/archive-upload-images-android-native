package com.walichin.archivoalejos.carganegativos;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by walteralejosgongora on 2/21/16.
 */
public class CardContent {

    public static String APP_USERNAME;
    public static String APP_PASSWORD;

    public static Handler handler;
    public static ProgressDialog progress;

    public static List<CardItem> ITEMS;
    public static Map<String, CardItem> ITEM_MAP;
    public static String NEGATIVE_ID;
    public static JSONObject NEGATIVE;
    public static JSONArray CARDS;
    public static JSONObject EXTRA_DATA;
    public static int NUM_REG;
    public static int LIMIT = 13;
    public static int OFFSET = 0;
    public static String PARAMS_SEARCH;
    public static int ARCHIVE_ID = 1;

    public static String URL = "http://php-archivoalejos.rhcloud.com/photo-adm/index.php/";
    //public static String URL = "http://172.16.0.6/Archivo-Alejos/archivo-backend/photo-adm/index.php/";

    public static String FTP_HOST = "ftp.walichin.a2hosted.com";
    public static String FTP_NEGATIVE_DIR = "/public_html/archivoalejos/negativos";
    public static String FTP_USERNAME = "walichin";
    public static String FTP_PASSWORD = "C0lm1ll0";

    public static String NEGATIVE_HOST = "http://walichin.a2hosted.com/archivoalejos/";
    public static String NEGATIVE_PATH = "negativos/";

    public static class CardItem {
        public final String id;
        public final String content;
        public final String details;

        public CardItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static void MensajeProcInfo(Context context) {

        CardContent.progress = new ProgressDialog(context);
        CardContent.progress.setTitle("Procesando Informacion");
        CardContent.progress.setMessage("Por favor espere...");
        CardContent.progress.setCancelable(false);
        CardContent.progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        CardContent.progress.show();
    }

    public static void parse_JSON_Cards (String strJson, boolean aditional_data) {

        try {

            JSONObject jsonRootObject = new JSONObject(strJson);
            CardContent.NUM_REG = jsonRootObject.optInt("totalCount");

            if (CardContent.NUM_REG > 0) {

                JSONArray jsonArray = jsonRootObject.optJSONArray("items");

                if (aditional_data) {

                    JSONArray extradataArray = jsonRootObject.optJSONArray("extra_data");


                    CardContent.EXTRA_DATA = new JSONObject();

                    for (int i = 0; i < extradataArray.length(); i++) {

                        JSONObject jsonPair = new JSONObject();
                        jsonPair.put("previous_negative_id", extradataArray.getJSONObject(i).optString("previous_negative_id"));
                        jsonPair.put("next_negative_id", extradataArray.getJSONObject(i).optString("next_negative_id"));
                        CardContent.EXTRA_DATA.put(extradataArray.getJSONObject(i).optString("negative_id"), jsonPair);
                    }
                }

                CardContent.ITEMS = new ArrayList<>();
                CardContent.ITEM_MAP = new HashMap<>();

                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String negative_id = jsonObject.optString("negative_id");
                    String img_name = jsonObject.optString("img_name");
                    String img_imgnro = jsonObject.optString("img_imgnro");
                    String box = jsonObject.optString("box");
                    String section = jsonObject.optString("section");
                    String location = jsonObject.optString("location");

                    String details = "Id Negativo: "+ negative_id + "\n" +
                            "Num.Negativo: " + img_name + "\n" +
                            "Imgnro: "+ img_imgnro + "\n" +
                            "Caja: "+ box + "\n" +
                            "Seccion: "+ section + "\n" +
                            "Ubicacion: "+ location;


                    CardContent.CardItem cardItem = new CardContent.CardItem(
                            negative_id,
                            img_name + " | " + img_imgnro + " | " + box + " | " + section + " | " + location,
                            details);

                    CardContent.ITEMS.add(cardItem);
                    CardContent.ITEM_MAP.put(negative_id, cardItem);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
