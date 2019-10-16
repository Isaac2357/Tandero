package com.iteso.tanderomobile.activities.cuenta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.utils.Parameters;

public class Cuenta extends AppCompatActivity {

    ImageView imagen;
    Button olvidarContra;
    EditText email;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        imagen = (ImageView) findViewById(R.id.imagenId);
        olvidarContra =(Button) findViewById(R.id.btnsave);
        email =(EditText) findViewById(R.id.emailET);
        pass = (EditText) findViewById(R.id.password);
        email.setText(Parameters.CURRENT_USER_EMAIL);
        email.setEnabled(false);
        pass.setText(Parameters.CURRENT_USER_PASSWORD);
        pass.setEnabled(false);
        olvidarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent olvidarContra = new Intent(Cuenta.this, Enviar_email.class);
                startActivity(olvidarContra);
            }
        });
    }

    public void onclick(View view) {
        cargarImagen();
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Selecciona aplicacion"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path =data.getData();
            imagen.setImageURI(path);
        }
    }


}
