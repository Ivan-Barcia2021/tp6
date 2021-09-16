package com.ort.tp6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    int codigopermiso=0;
    Button tomar_foto;
    int codigotomarfoto=1;
    int codigoelegirfoto=-1;
    ProgressDialog dialogo;
    TextView textoresutado= findViewById (R.id.mitexto);
    ImageView imagen= findViewById (R.id.imagen);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        dialogo= new ProgressDialog (this);


        tomar_foto=findViewById (R.id.miboton);

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
        }else {tomar_foto.setEnabled (true);

        }

    }

    @Override
    public void onRequestPermissionsResult(int codigoRespuesta, @NonNull String [] nombresPermisos, @NonNull int [] resultadosPermisos){
        if (codigoRespuesta == codigopermiso){
            Log.d ("PermisosPedidos", "PermisosObtenidos:"+nombresPermisos.length);
            for(int punteropermiso=0; punteropermiso<nombresPermisos.length; punteropermiso ++){
                Log.d ("PermisosPedidos", "Permiso:"+punteropermiso, "-nombre:"+nombresPermisos[punteropermiso]+""+(resultadosPermisos[punteropermiso]==PackageManager.PERMISSION_GRANTED));
            }
            Boolean obtuvotodoslospermisos=true;
            for( int punteropermiso=0; punteropermiso<nombresPermisos.length; punteropermiso++){
if(resultadosPermisos[punteropermiso]!=PackageManager.PERMISSION_GRANTED){
    obtuvotodoslospermisos=false;
}
            }
            if(obtuvotodoslospermisos){
                tomar_foto.setEnabled (true);
            }
        }


    }
    public void Presionatomarfoto(View v){
        Intent intentfoto;
        intentfoto= new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult (intentfoto, codigotomarfoto);

    }
public void presionaElegir(View v){
        Intent i;
        i= new Intent (Intent.ACTION_GET_CONTENT);
i.setType ("");
startActivityForResult (Intent.createChooser (i, "Seleccione foto"), codigoelegirfoto);
}
@Override
protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent recibirdatos){
        super.onActivityResult (requestcode,resultcode, recibirdatos);
        textoresutado.setText ("Procesando...");
        if(requestcode==codigotomarfoto&& resultcode==RESULT_OK){
            Bitmap fotorecibida=(Bitmap)recibirdatos.getExtras ().get("data");
imagen.setImageBitmap (fotorecibida);
procesarimagenobtenida(fotorecibida);
        }
        if(requestcode==codigoelegirfoto && resultcode==RESULT_OK && recibirdatos != null){
            Uri ubicacion=recibirdatos.getData ();
            Bitmap imagenfoto=null;
            try {
                imagenfoto=MediaStore.Images.Media.getBitmap (getContentResolver (), ubicacion);
            }
            catch (Exception error){
                Log.d("Fotoobtenida", "Error");
            }
        }
}
public void procesarimagenobtenida(final Bitmap fotorecibida){
    ByteArrayOutputStream streamsalida=new ByteArrayOutputStream ();
    fotorecibida.compress (Bitmap.CompressFormat.JPEG, 100, streamsalida);
    ByteArrayInputStream streamentrada= new ByteArrayInputStream (streamsalida.toByteArray ());
    class procesarimagen extends AsyncTask<InputStream, String, Face[]>{
@Override
       protected Face[] doInBackground(InputStream... fotorecibida){
publishProgress ("Deectando caras...");
Face[] resultado=null;
try{
    FaceServiceClient.FaceAttributeType[] atributos;
    atributos= new FaceServiceClient.FaceAttributeType[]{
            FaceServiceClient.FaceAttributeType.Age,
    FaceServiceClient.FaceAttributeType.Glasses,
    FaceServiceClient.FaceAttributeType.Smile,
    FaceServiceClient.FaceAttributeType.FacialHair,
    FaceServiceClient.FaceAttributeType.Gender
    };
    resultado=servicioprocesamiento.detect(fotorecibida[0], true, false, atributos);

}
catch (Exception error){

}



    return resultado;
}
@Override
        protected void onPreExecute(){
    super.onPreExecute ();
    dialogo.show ();

}
@Override
        protected void onProgressUpdate(String... mensajeprogreso){
    super.onProgressUpdate ();
    dialogo.setMessage (mensajeprogreso[0]);

}
@Override protected void onPostExecute(Face[] resultado){
super.onPostExecute (resultado);
dialogo.dismiss ();
if(resultado== null){
    textoresutado.setText ("Error de procesamiento");

}
else {
    if(resultado.length>0){
        recuadrarcaras(imagen, resultado);
        procesarResul(resultado);
    }
    else{
        textoresutado.setText ("no se detecto cara ");
    }
}
}
void recuadrarcaras(Bitmap imagenoriginal, Face[]caras_recuadrar){
    Bitmap imagendibujar;
    imagendibujar=imagenoriginal.copy (Bitmap.Config.ARGB_8888, true);
    Canvas Lienzo;
    Lienzo= new Canvas (imagendibujar);
    Paint pincel;
    pincel= new Paint ();
    pincel.setAntiAlias (true);
    pincel.setStyle (Paint.Style.STROKE);
    pincel.setColor (Color.RED);
    for(Face unacara: caras_recuadrar){
        FaceRectangle rectangulo;
        rectangulo=unacara.faceRectangle;
        Lienzo.drawRect (rectangulo.left, rectangulo.top, rectangulo.left+rectangulo.width, rectangulo.top+rectangulo.height);
        imagen.setImageBitmap (imagendibujar);
    }
}
        void procesarResul(Face[] caras){
int cantidadhombres, cantidadmujeres;
cantidadhombres=preferencias.getInt("cantidadhombres", 0);
            cantidadmujeres=preferencias.getInt("cantidamujeres", 0);
            String mensaje="";
            for(int punterocara=0; punterocara<caras.length; punterocara++){
                mensaje+="Edad:"+caras[punterocara].faceAttributes.age;
                mensaje+="Sonrisa:"+caras[punterocara].faceAttributes.smile;
                mensaje+="Barba:"+caras[punterocara].faceAttributes.facialHair.beard;
                mensaje+="Genero:"+caras[punterocara].faceAttributes.gender;
                mensaje+="Anteojos:"+caras[punterocara].faceAttributes.glasses;

                if(caras[punterocara].faceAttributes.gender.equals ("male")){
                    cantidadhombres++;
                }
                else {
                    cantidadmujeres++;
                }
            }

        }
    }

    procesarimagen mitarea= new procesarimagen ();
    mitarea.execute (streamentrada);


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