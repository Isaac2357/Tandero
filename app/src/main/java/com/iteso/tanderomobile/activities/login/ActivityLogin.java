package com.iteso.tanderomobile.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.activities.base.ActivityBase;
import com.iteso.tanderomobile.activities.enrollment.ActivityEnroll;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;
import com.iteso.tanderomobile.utils.Parameters;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    /** */
    private EditText userEmail;
    /** */
    private EditText userPassword;
    /** */
    private Button login;
    /** */
    private LoginViewModel viewModel;
    /** */
    private TextView register;
    /** */
    private CustomProgressDialog progressDialog;

    /**
     * OnCreate callback.
     * @param savedInstanceState Instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initViewModel();
    }

    /**
     * Init views.
     */
    private void initViews() {
        userEmail = findViewById(R.id.user_email_value);
        userPassword = findViewById(R.id.user_password_value);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        progressDialog = new CustomProgressDialog(this);
    }

    /**
     * Init view model and observers.
     */
    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.getLoginStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean status) {
                if (status) {
                    Log.v("login", "success");
                    progressDialog.dismiss();
                    // Save user credentials
                    Parameters.CURRENT_USER_EMAIL = userEmail.getText().toString();
                    Parameters.CURRENT_USER_PASSWORD = userPassword.getText().toString();
                    //Go to main act
                    Intent base = new Intent(getApplication(), ActivityBase.class);
                    base.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(base);
                } else {
                    //show error dialog
                    Log.v("login", "failed");
                    progressDialog.dismiss();
                    Toast.makeText(getApplication(),
                               "Login failed... try again",
                                    Toast.LENGTH_LONG).show();
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
        if (v.getId() == R.id.login_btn) {
            progressDialog.show();
            String email = userEmail.getText().toString();
            String password = userPassword.getText().toString();
            viewModel.login(email, password);
        } else if (v.getId() == R.id.register) {
            Log.v("--", "open register");
            startActivity(new Intent(this, ActivityEnroll.class));
        }
    }
}
