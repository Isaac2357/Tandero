package com.iteso.tanderomobile.activities.enrollment;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.iteso.tanderomobile.repositories.authentication.AuthenticationManager;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;

public class EnrollViewModel extends ViewModel {
    private AuthenticationManager auth = AuthenticationManager.createInstance();
    private DatabaseManager dbManager = DatabaseManager.createInstance();

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

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void writeUserInDatabase(String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("isActivated",true);
        user.put("isPrivate",true);
        user.put("ratingOrganizador",5);
        user.put("ratingParticipante",5);
        user.put("tandasOwned",0);

        dbManager.getCollectionRef("users")
                    .document(email)
                    .set(user);
    }

}
