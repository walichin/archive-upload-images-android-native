package com.walichin.archivoalejos.carganegativos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by walteralejosgongora on 2/21/16.
 */
public class NegativeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        NegativeContent.NegativeDetailActivity = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negative_detail);

        final TextView newBox = (TextView) findViewById(R.id.newBox);
        final TextView newSection = (TextView) findViewById(R.id.newSection);
        final TextView newLocation = (TextView) findViewById(R.id.newLocation);
        final CheckBox newIs_restored = (CheckBox) findViewById(R.id.newIs_restored);
        final TextView newHeight = (TextView) findViewById(R.id.newHeight);
        final TextView newWidth = (TextView) findViewById(R.id.newWidth);
        final TextView newThickness = (TextView) findViewById(R.id.newThickness);
        final TextView newImgnro = (TextView) findViewById(R.id.newImgnro);

        Button btnPrev2 = (Button) findViewById(R.id.btnPrev2);

        btnPrev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // GUARDA INFORMACION
                if (newIs_restored.isChecked()) NegativeContent.NEGATIVE.is_restored = "1";
                else NegativeContent.NEGATIVE.is_restored = "0";

                NegativeContent.NEGATIVE.box = newBox.getText().toString();
                NegativeContent.NEGATIVE.height = newHeight.getText().toString();
                NegativeContent.NEGATIVE.imgnro = newImgnro.getText().toString();
                NegativeContent.NEGATIVE.location = newLocation.getText().toString();
                NegativeContent.NEGATIVE.section = newSection.getText().toString();
                NegativeContent.NEGATIVE.thickness = newThickness.getText().toString();
                NegativeContent.NEGATIVE.width = newWidth.getText().toString();

                onBackPressed();
            }
        });

        Button btnCancel2 = (Button) findViewById(R.id.btnCancel2);

        btnCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NegativeContent.NegativeCardsActivity != null) {
                    NegativeContent.NegativeCardsActivity.finish();
                    NegativeContent.NegativeCardsActivity=null;
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

        Button btnNext2 = (Button) findViewById(R.id.btnNext2);

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newBox.getText().toString().trim().isEmpty()) {

                    Snackbar.make(v, "Debe ingresar la caja", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else if (newSection.getText().toString().trim().isEmpty()) {

                    Snackbar.make(v, "Debe ingresar la seccion", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else if (newLocation.getText().toString().trim().isEmpty()) {

                    Snackbar.make(v, "Debe seleccionar la ubicacion", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {

                    // GUARDA INFORMACION
                    if (newIs_restored.isChecked()) NegativeContent.NEGATIVE.is_restored = "1";
                    else NegativeContent.NEGATIVE.is_restored = "0";

                    NegativeContent.NEGATIVE.box = newBox.getText().toString();
                    NegativeContent.NEGATIVE.height = newHeight.getText().toString();
                    NegativeContent.NEGATIVE.imgnro = newImgnro.getText().toString();
                    NegativeContent.NEGATIVE.location = newLocation.getText().toString();
                    NegativeContent.NEGATIVE.section = newSection.getText().toString();
                    NegativeContent.NEGATIVE.thickness = newThickness.getText().toString();
                    NegativeContent.NEGATIVE.width = newWidth.getText().toString();

                    Intent intent = getIntent();
                    intent.setClass(getApplicationContext(), NegativeCardsActivity.class);
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
        //startActivity(getIntent().setClass(getApplicationContext(), NegativeImageActivity.class));

        Intent intent = this.getIntent();
        intent.setClass(this, NegativeImageActivity.class);
        startActivity(intent);
    }

}
