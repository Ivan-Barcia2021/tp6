package com.ort.tp6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
    }
    public void sacarfoto(View v){
        AlertDialog.Builder mensaje;
        mensaje= new AlertDialog.Builder (this);
        mensaje.setTitle ("opciones");
        String [] misopciones={"edad", "genero", "barba", "sonrisa"};
        boolean[] preseleccion={true, true, false, false, false};
        mensaje.setMultiChoiceItems (misopciones, preseleccion, (DialogInterface.OnMultiChoiceClickListener) escuchador);
        mensaje.setPositiveButton ("Aceptar", otroescuchador);
        mensaje.create ();
        mensaje.show ();
    }
    DialogInterface.OnClickListener otroescuchador= new DialogInterface.OnClickListener () {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
    DialogInterface.OnMultiChoiceClickListener escuchador= new DialogInterface.OnMultiChoiceClickListener () {
        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

        }


    };
}