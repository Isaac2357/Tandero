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
/**
 * clase.
 */
public class ActivityProfile extends AppCompatActivity {
    /**
     * variable .
     */
    private ImageView imagen;
    /**
     * variable .
     */
    private Button olvidarContra;
    /**
     * variable .
     */
    private EditText email;
    /**
     * variable .
     */
    private EditText pass;
    /**
     * Varibale VAL .
     */
    public static final int VAL = 10;

    @Override
    protected final void onCreate(final
                                  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        imagen = (ImageView) findViewById(R.id.imagenId);
        olvidarContra = (Button) findViewById(R.id.btnsave);
        email = (EditText) findViewById(R.id.emailET);
        pass = (EditText) findViewById(R.id.password);
        email.setText(Parameters.CURRENT_USER_EMAIL);
        email.setEnabled(false);
        pass.setText(Parameters.CURRENT_USER_PASSWORD);
        pass.setEnabled(false);
        olvidarContra.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("checkstyle:HiddenField")
            @Override
            public void onClick(final View view) {
                Intent olderContra = new Intent(
                        ActivityProfile.this, ActivitySendEmail.class);
                startActivity(olderContra);
            }
        });
    }
    /**
     *
     * @param view .
     */
    final void onclick(final View view) {
        cargarImagen();
    }
    /**
     *
     */
    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,
                "Selecciona aplicacion"), VAL);
    }

    @Override
    protected final void onActivityResult(final int requestCode,
                                          final int resultCode,
                                          final @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            imagen.setImageURI(path);
        }
    }


}

