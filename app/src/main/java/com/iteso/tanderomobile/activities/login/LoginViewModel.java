package com.iteso.tanderomobile.activities.login;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.iteso.tanderomobile.repositories.authentication.AuthenticationManager;

public class LoginViewModel extends ViewModel {
    /** Auth repository.*/
    private AuthenticationManager auth = AuthenticationManager.createInstance();
    /** Login status live data.*/
    private MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();
    /**
     * Get login status live data.
     * @return Login status.
     */
    LiveData<Boolean> getLoginStatus() {
        return loginStatus;
    }
    /**
     * Login method.
     * @param email user email
     * @param password user password.
     */
    void login(final String email, final String password) {
        auth.signIn(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginStatus.postValue(true);
                        } else {
                            loginStatus.setValue(false);
                        }
                    }
                });
    }
}
