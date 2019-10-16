package com.iteso.tanderomobile.cuenta;

import androidx.appcompat.app.AppCompatActivity;
import io.opencensus.trace.MessageEvent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iteso.tanderomobile.R;

public class Enviar_email extends AppCompatActivity {

    EditText mRecipientET,mSubjetET,mMessageET;
    Button mSendEmailBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_email);

        mRecipientET = findViewById(R.id.recipientET);
        mSubjetET = findViewById(R.id.subjetET);
        mMessageET = findViewById(R.id.messageET);
        mSendEmailBtn = findViewById(R.id.sendEmailBtn);
        //evento boton
        mSendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipient = mRecipientET.getText().toString().trim();
                String subjet = mSubjetET.getText().toString().trim();
                String message = mMessageET.getText().toString().trim();

                sendEmail(recipient,subjet,message);
            }
        });
    }

    private void sendEmail(String recipient, String subjet, String message) {
        Intent mEmailIntent = new Intent(Intent.ACTION_SEND);
        mEmailIntent.setData(Uri.parse("mailto:"));
        mEmailIntent.setType("text/plain");
        mEmailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{recipient});
        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT,subjet);
        mEmailIntent.putExtra(Intent.EXTRA_TEXT,message);
        try{
            startActivity(Intent.createChooser(mEmailIntent,"Email de organizador"));
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
