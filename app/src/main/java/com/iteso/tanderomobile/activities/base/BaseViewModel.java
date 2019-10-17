package com.iteso.tanderomobile.activities.base;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.internal.LibraryVersion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.iteso.tanderomobile.repositories.authentication.AuthenticationManager;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Parameters;

public class BaseViewModel extends ViewModel {
    private MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
    private AuthenticationManager auth = AuthenticationManager.createInstance();
    private MutableLiveData<Boolean> reauthenticateStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteAccountStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> updatePassword = new MutableLiveData<>();
    private DatabaseManager dbmanager = DatabaseManager.createInstance();

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public LiveData<Boolean> getDeleteAccountStatus() {
        return deleteAccountStatus;
    }

    public LiveData<Boolean> getReauthenticateStatus() {
        return reauthenticateStatus;
    }

    public LiveData<Boolean> getUpdatePasswordStatus() {
        return updatePassword;
    }

    private void deleteUser() {
        auth.deleteUser().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v("delete", "Success");
                    deleteAccountStatus.postValue(true);
                } else {
                    Log.v("delete", "failed");
                    deleteAccountStatus.postValue(false);
                }
            }
        });
    }

    public void authenticateUser() {
        auth.reauthenticateUser().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v("reauth", "Success");
                    reauthenticateStatus.postValue(true);
                } else {
                    Log.v("reauth", "failed");
                    reauthenticateStatus.postValue(false);
                }
            }
        });
    }

    public void deleteAccount() {
        auth.reauthenticateUser().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v("reauth", "Success");
                    deleteUser();
                } else {
                    Log.v("reauth", "failed");
                }
            }
        });
    }

    private void updatePassword(String newPassword) {
        auth.updateUserPassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v("uppass", "Success");
                } else {
                    Log.v("uppass", "failed");
                }
            }
        });
    }

    public void updateUserPassword(final String newPassword) {
        auth.reauthenticateUser().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.v("reauth", "Success");
                    updatePassword(newPassword);
                } else {
                    Log.v("reauth", "failed");
                }
            }
        });
    }

    public void closeSession() {
        auth.signOut();
    }

    public void getCurrentUserId() {
        dbmanager.getCollectionRef("users")
                .whereEqualTo("email",Parameters.CURRENT_USER_EMAIL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            if (query != null && query.size() == 1) {
                                Log.v("ID--", query.getDocuments().get(0).getId());
                                Parameters.CURRENT_USER_ID = query.getDocuments().get(0).getId();
                            }
                        }
                    }
                });
    }

}
