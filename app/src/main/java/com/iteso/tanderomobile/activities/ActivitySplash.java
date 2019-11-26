package com.iteso.tanderomobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.activities.login.ActivityLogin;

public class ActivitySplash extends AppCompatActivity {
    /** Progress bar view.*/
    private ProgressBar progressBar;
    /**  Default progress bar time (millis).*/
    private static final int DEFAULT_PROGRESS_BAR_TIME = 5000;
    /**
     * OnCreate callback.
     * @param savedInstanceState Instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress_bar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                openLoginActivity();

            }
        }, DEFAULT_PROGRESS_BAR_TIME);

    }

    /**
     * Method that opens the login activity.
     */
    private void openLoginActivity() {
        Intent loginIntent = new Intent(this, ActivityLogin.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                             | Intent.FLAG_ACTIVITY_NEW_TASK
                             | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }
}
