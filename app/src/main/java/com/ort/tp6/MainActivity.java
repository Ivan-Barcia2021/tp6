package com.ort.tp6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ProgressDialog dialogo;
        dialogo= new ProgressDialog (this);
     int codigopermiso=0;
        Button tomar_foto;
        tomar_foto=findViewById (R.id.miboton);
        TextView textoresutado= findViewById (R.id.mitexto);
        SharedPreferences preferencias;
        preferencias=getSharedPreferences ("", Context.MODE_PRIVATE);
        String apiendpoint = "https://miservicio.cognitiveservices.azure.com/";
        String key="6b6cba9466d145f18a68b06204507ab7";
        try {
        FaceServiceRestClient    servicioprocesamiento = new FaceServiceRestClient (apiendpoint, key);
        }
catch (Exception error){

}
        if(ContextCompat.checkSelfPermission (this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
tomar_foto.setEnabled (false);
            ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, codigopermiso);
        }else {tomar_foto.setEnabled (true);}

    }
    public void vergaleria(View view){
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