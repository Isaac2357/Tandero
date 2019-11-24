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
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;
import com.iteso.tanderomobile.utils.Parameters;

public class ActivityEnroll extends AppCompatActivity implements View.OnClickListener {
    /** Email edit text. */
    private EditText email;
    /** Password edit text.*/
    private EditText password;
    /** Confirm password edit text.*/
    private EditText confirmPassword;
    /** Register button. */
    private Button registerBtn;
    /** View model.*/
    private EnrollViewModel viewModel;
    /** Progress dialog.*/
    private CustomProgressDialog progressDialog;
    /**
     * OnCreate callback.
     * @param savedInstanceState Instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        initViews();
        initViewModel();
    }

    /**
     * Method init all views.
     */
    private void initViews() {
        email = findViewById(R.id.user_email_value);
        password = findViewById(R.id.user_password_value);
        confirmPassword = findViewById(R.id.user_password_confirm_value);
        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
        progressDialog = new CustomProgressDialog(this);
    }

    /**
     * Method init view model and observers.
     */
    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(EnrollViewModel.class);
        viewModel.getCreateAccountStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean status) {
                if (status) {
                    Log.v("Create acc", "succes");
                    progressDialog.dismiss();
                    Parameters.CURRENT_USER_EMAIL = email.getText().toString();
                    Parameters.CURRENT_USER_PASSWORD = password.getText().toString();
                    viewModel.writeUserInDatabase(Parameters.CURRENT_USER_EMAIL,
                                                  Parameters.CURRENT_USER_PASSWORD);
                    Intent base = new Intent(getApplication(), ActivityBase.class);
                    base.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(base);
                } else {
                    progressDialog.dismiss();
                    Log.v("Create acc", "failed");
                    Toast.makeText(getApplication(), "Registration failed... try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * OnClick method.
     * @param v View clicked.
     */
    @Override
    public void onClick(final View v) {
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
