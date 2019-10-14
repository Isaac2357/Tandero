package com.iteso.tanderomobile.activities.enrollment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.iteso.tanderomobile.repositories.authentication.AuthenticationManager;

public class EnrollViewModel extends ViewModel {
    private AuthenticationManager auth = AuthenticationManager.createInstance();
    private MutableLiveData<Boolean> createAccountStatus = new MutableLiveData<>();

    public LiveData<Boolean> getCreateAccountStatus() {
        return createAccountStatus;
    }

    public void createAccount(String email, String password) {
        auth.createAccount(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createAccountStatus.postValue(true);
                        } else {
                            createAccountStatus.setValue(false);
                        }
                    }
                });
    }
}
