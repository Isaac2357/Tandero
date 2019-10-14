package com.iteso.tanderomobile.activities.enrollment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.activities.base.ActivityBase;
import com.iteso.tanderomobile.utils.CustomProgressDialog;
import com.iteso.tanderomobile.utils.Parameters;


public class ActivityEnroll extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button registerBtn;
    private EnrollViewModel viewModel;
    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        initViews();
        initViewModel();
    }

    private void initViews() {
        email = findViewById(R.id.user_email_value);
        password = findViewById(R.id.user_password_value);
        confirmPassword = findViewById(R.id.user_password_confirm_value);
        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
        progressDialog = new CustomProgressDialog(this);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(EnrollViewModel.class);
        viewModel.getCreateAccountStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if (status) {
                    Log.v("Create acc", "succes");
                    progressDialog.dismiss();
                    Parameters.CURRENT_USER_EMAIL = email.getText().toString();
                    Parameters.CURRENT_USER_PASSWORD = password.getText().toString();
                    Intent base = new Intent(getApplication(), ActivityBase.class);
                    base.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(base);
                } else {
                    progressDialog.dismiss();
                    Log.v("Create acc", "failed");
                    Toast.makeText(getApplication(),"Registration failed... try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_btn) {

            String mail =  email.getText().toString();
            String pass = password.getText().toString();
            String confirmPass = confirmPassword.getText().toString();
            if (pass.equals(confirmPass)) {
                progressDialog.show();
                viewModel.createAccount(mail, pass);
            } else {
                password.setError("Los valores no coinciden");
                confirmPassword.setError("Los valores no coinciden");
            }
        }
    }
}
