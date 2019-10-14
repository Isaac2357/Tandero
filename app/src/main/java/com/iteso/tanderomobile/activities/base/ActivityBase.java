package com.iteso.tanderomobile.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.activities.login.ActivityLogin;
import com.iteso.tanderomobile.fragments.home.HomeFragment;

//test
public class ActivityBase extends AppCompatActivity {
    private BottomNavigationView navView;
    private BottomNavigationView.OnNavigationItemSelectedListener navBottomListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            Log.v("","");
                            openFragment(new HomeFragment(),null);
                            break;
                        case R.id.navigation_close_session:
                                Log.v("","");

                                break;
                    }
                    return true;
                }
            };
    private BaseViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navBottomListener);
        openFragment(new HomeFragment(), null);
        initViewModel();
    }

    public void openFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        viewModel.getReauthenticateStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if (status) {
                    Log.v("reauth", "fine");
                } else {
                    Log.v("reauth", "bad");
                }
            }
        });

        viewModel.getDeleteAccountStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if (status) {
                    Log.v("delete", "fine");
                    Intent login = new Intent(getApplication(), ActivityLogin.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                } else {
                    Log.v("delete", "bad");
                }
            }
        });
    }

}
