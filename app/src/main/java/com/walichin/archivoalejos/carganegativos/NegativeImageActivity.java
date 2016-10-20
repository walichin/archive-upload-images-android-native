package com.walichin.archivoalejos.carganegativos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.Snackbar;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * Created by walteralejosgongora on 2/21/16.
 */
public class NegativeImageActivity extends AppCompatActivity {

    //final int REQUEST_CODE_PICK_DIR = 1;
    //final int REQUEST_CODE_PICK_FILE = 2;
    final int REQUEST_CODE_NEW_INTENT = 3;

    private Intent mRequestFileIntent;
    private ParcelFileDescriptor mInputPFD;

    //final FileBrowserActivity fileBrowserActivity = new FileBrowserActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NegativeContent.NegativeImageActivity = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negative_image);

        Button btnImage = (Button) findViewById(R.id.btnImage);
        Button btnCancel1 = (Button) findViewById(R.id.btnCancel1);
        Button btnNext1 = (Button) findViewById(R.id.btnNext1);

        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/jpg");

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent fileExploreIntent = new Intent(
//                        fileBrowserActivity.INTENT_ACTION_SELECT_FILE,
//                        null,
//                        NegativeContent.NegativeImageActivity,
//                        fileBrowserActivity.getClass()
//                );
//
//                startActivityForResult(
//                        fileExploreIntent,
//                        REQUEST_CODE_PICK_FILE
//                );

                startActivityForResult(mRequestFileIntent, REQUEST_CODE_NEW_INTENT);

            }
        });

        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // POR SI ACASO: LA INFORMACION YA SE GUARDO CUANDO SE SELECCIONO EL ARCHIVO

                if (NegativeContent.NEGATIVE.filename.trim().isEmpty()) {

                    Snackbar.make(v, "Debe seleccionar una imagen", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {

                    Intent intent = getIntent();
                    intent.setClass(getApplicationContext(), NegativeDetailActivity.class);
                    startActivity(intent);

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

        if (NegativeContent.NegativeCardsActivity != null) {
            NegativeContent.NegativeCardsActivity.finish();
            NegativeContent.NegativeCardsActivity=null;
        }

        if (NegativeContent.NegativeDetailActivity != null) {
            NegativeContent.NegativeDetailActivity.finish();
            NegativeContent.NegativeDetailActivity=null;
        }

        finish();

        Intent intent = this.getIntent();
        intent.setClass(this, CardSearchActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView imgNegative = (ImageView) findViewById(R.id.imgNegative);
        TextView txtFileName = (TextView) findViewById(R.id.txtFileName);

        if (requestCode == REQUEST_CODE_NEW_INTENT) {

            // If the selection didn't work
            if (resultCode != RESULT_OK) {

                NegativeContent.NEGATIVE.filedir = "";
                NegativeContent.NEGATIVE.filename = "";
                txtFileName.setText("");
                imgNegative.setImageBitmap(null);

            } else {
                // Get the file's content URI from the incoming Intent
                Uri returnUri = data.getData();

            /*
             * Try to open the file for "read" access using the
             * returned URI. If the file isn't found, write to the
             * error log and return.
             */
                try {
                /*
                 * Get the content resolver instance for this context, and use it
                 * to get a ParcelFileDescriptor for the file.
                 */
                    mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "File not found.");
                    return;
                }

                // Get a regular file descriptor for the file
                FileDescriptor fd = mInputPFD.getFileDescriptor();

                DocumentFile pickedFile = DocumentFile.fromSingleUri(this, returnUri);
                String filename = pickedFile.getName();
                txtFileName.setText(filename);

                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd);
                imgNegative.setImageBitmap(bitmap);

                NegativeContent.NEGATIVE.filename = filename;
                NegativeContent.NEGATIVE.filedir = "";
                NegativeContent.NEGATIVE.uri = returnUri;
                NegativeContent.NEGATIVE.pfd = mInputPFD;

            }
        }

//        if (requestCode == REQUEST_CODE_PICK_FILE) {
//            if(resultCode == RESULT_OK) {
//
//                NegativeContent.NEGATIVE.filename = data.getStringExtra(
//                        fileBrowserActivity.returnFileParameter);
//
//                NegativeContent.NEGATIVE.filedir = data.getStringExtra(
//                        fileBrowserActivity.returnDirectoryParameter);
//
//                txtFileName.setText(NegativeContent.NEGATIVE.filename);
//
//                Bitmap bitmap = BitmapFactory.decodeFile(NegativeContent.NEGATIVE.filedir+"/"+NegativeContent.NEGATIVE.filename);
//                imgNegative.setImageBitmap(bitmap);
//
//            } else {
//
//                NegativeContent.NEGATIVE.filename = "";
//                NegativeContent.NEGATIVE.filedir = "";
//                txtFileName.setText("");
//                imgNegative.setImageBitmap(null);
//
//            }
//        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
