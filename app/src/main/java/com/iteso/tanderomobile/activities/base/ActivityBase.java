package com.iteso.tanderomobile.activities.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.activities.cuenta.ActivityProfile;
import com.iteso.tanderomobile.activities.login.ActivityLogin;
import com.iteso.tanderomobile.fragments.organizer.user.UserOrganizerFragment;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;
import com.iteso.tanderomobile.fragments.organizer.admin.AdminOrganizerFragment;

public class ActivityBase extends AppCompatActivity {
    private BaseViewModel viewModel;
    private CustomProgressDialog progressDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener navBottomListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_tanda1:
                            openFragment(new AdminOrganizerFragment(),null);
                                break;
                        case R.id.navigation_tanda2:
                            openFragment(new UserOrganizerFragment(), null);
                            break;
                    }
                    return true;
                }
            };

    private Toolbar.OnMenuItemClickListener menuItemClickListener =
            new Toolbar.OnMenuItemClickListener(){

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_profile:
                            Intent abrir  = new Intent(getApplication(), ActivityProfile.class);
                            startActivity(abrir);
                            break;
                        case R.id.nav_close_session:
                            displaySignOutDialog();
                            break;

                    }

                    return true;
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavigationView navView;
        Toolbar toolbar;
        setContentView(R.layout.activity_base);
        navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.nav_top);
        progressDialog = new CustomProgressDialog(this);

        navView.setOnNavigationItemSelectedListener(navBottomListener);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        openFragment(new AdminOrganizerFragment(), null);
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

        viewModel.getCurrentUserId();
    }

    public void displaySignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión")
                .setMessage("¿Desea cerrar sesión?")
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.closeSession();
                                progressDialog.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Intent loginIntent = new Intent(getApplication(), ActivityLogin.class);
                                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(loginIntent);
                                    }
                                }, 2000);
                            }
                 })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                })
                .create().show();



    }

}
