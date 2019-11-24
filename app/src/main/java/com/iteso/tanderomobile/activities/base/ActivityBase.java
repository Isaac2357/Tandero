package com.iteso.tanderomobile.activities.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.activities.cuenta.ActivityProfile;
import com.iteso.tanderomobile.activities.login.ActivityLogin;
import com.iteso.tanderomobile.fragments.organizer.admin.AdminOrganizerFragment;
import com.iteso.tanderomobile.fragments.organizer.user.UserOrganizerFragment;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;

public class ActivityBase extends AppCompatActivity {
    /** Default wait time.*/
    private static final int DEFAULT_WAIT_TIME = 2000;
    /** View model.*/
    private BaseViewModel viewModel;
    /** Progress dialog.*/
    private CustomProgressDialog progressDialog;
    /** BottomNavView's listener.*/
    private BottomNavigationView.OnNavigationItemSelectedListener
            navBottomListener =
                        new BottomNavigationView
                            .OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_tanda1:
                            openFragment(new AdminOrganizerFragment(), null);
                            break;
                        case R.id.navigation_tanda2:
                            openFragment(new UserOrganizerFragment(), null);
                            break;
                        default:
                                return true;
                    }
                    return true;
                }
            };
    /**
     * Toolbar's OnItemClickListener.
     */
    private Toolbar.OnMenuItemClickListener menuItemClickListener =
            new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(final MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_profile:
                            Intent abrir  = new Intent(getApplication(),
                                                        ActivityProfile.class);
                            startActivity(abrir);
                            break;
                        case R.id.nav_close_session:
                            displaySignOutDialog();
                            break;
                        default:
                            return true;
                    }
                    return true;
                }
            };

    /**
     * OnCreate callback for the app lifecycle.
     * @param savedInstanceState Saved instance.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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

    /**
     *  Open new fragment in the base activity.
     * @param fragment fragment to be opened
     * @param bundle data to be transferred to the fragment.
     */
    public void openFragment(final Fragment fragment, final Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                                    .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Initialize view model and observers for the base actiity.
     */
    private void initViewModel() {

        viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        viewModel.getReAuthenticateStatus()
                    .observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean status) {
                if (status) {
                    Log.v("reauth", "fine");
                } else {
                    Log.v("reauth", "bad");
                }
            }
        });

        viewModel.getDeleteAccountStatus()
            .observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean status) {
                if (status) {
                    Log.v("delete", "fine");
                    Intent login = new Intent(getApplication(),
                                              ActivityLogin.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                } else {
                    Log.v("delete", "bad");
                }
            }
        });

        viewModel.getCurrentUserId();
    }

    /**
     * Displays the sign out dialog.
     */
    private void displaySignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
            .setTitle("Cerrar sesión")
            .setMessage("¿Desea cerrar sesión?")
            .setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                                        final int which) {
                        viewModel.closeSession();
                        progressDialog.show();
                        goToLoginActivity();
                    }
                 })
                .setNegativeButton("Cancelar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog,
                                            final int which) {
                            dialog.dismiss();
                        }
                    })
                .create().show();
    }

    private void goToLoginActivity() {
        new Handler()
            .postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent loginIntent = new Intent(getApplication(),
                        ActivityLogin.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }, DEFAULT_WAIT_TIME);
    }
}
