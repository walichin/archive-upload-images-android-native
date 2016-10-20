package com.walichin.archivoalejos.carganegativos;

import android.app.Activity;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walteralejosgongora on 2/21/16.
 */
public class NegativeContent {

    public static Negative NEGATIVE = new Negative();
    public static List<Card> CARDS = new ArrayList<>();
    public static int CURRENT_INDEX;

    public static Activity NegativeImageActivity=null;
    public static Activity NegativeDetailActivity=null;
    public static Activity NegativeCardsActivity=null;

    public static class Negative {

        public String filename="";
        public String filedir="";
        public Uri uri=null;
        public ParcelFileDescriptor pfd=null;
        //public String filepath="";
        public String imgnro="";
        public String box="";
        public String section="";
        public String location="";
        public String height="";
        public String width="";
        public String thickness="";
        public String is_restored="";

        public void Clear() {
            filename="";
            filedir="";
            uri=null;
            pfd=null;
            //filepath="";
            imgnro="";
            box="";
            section="";
            location="";
            height="";
            width="";
            thickness="";
            is_restored="";
        }

        public void Save(
            String filename,
            String filedir,
            Uri uri,
            ParcelFileDescriptor pfd,
            //String filepath,
            String imgnro,
            String box,
            String section,
            String location,
            String height,
            String width,
            String thickness,
            String is_restored) {

            this.filename=filename;
            this.filedir=filedir;
            this.uri=uri;
            this.pfd=pfd;
            //this.filepath=filepath;
            this.imgnro=imgnro;
            this.box=box;
            this.section=section;
            this.location=location;
            this.height=height;
            this.width=width;
            this.thickness=thickness;
            this.is_restored=is_restored;
        }
    }

    public static class Card {

        public String cardnumber="";
        public String title="";
        public String description="";
        public String observation="";

        public void Clear() {
            cardnumber="";
            title="";
            description="";
            observation="";
        }

        public void Save(
                String cardnumber,
                String title,
                String description,
                String observation) {

            this.cardnumber=cardnumber;
            this.title=title;
            this.description=description;
            this.observation=observation;
        }
    }

}
