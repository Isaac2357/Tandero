package com.iteso.tanderomobile.activities.base;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.iteso.tanderomobile.repositories.authentication.AuthenticationManager;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Parameters;

class BaseViewModel extends ViewModel {
    /** */
    private MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
    /** */
    private AuthenticationManager auth = AuthenticationManager.createInstance();
    /** */
    private MutableLiveData<Boolean> reauthenticateStatus;
    {
        reauthenticateStatus = new MutableLiveData<>();
    }

    /** */
    private MutableLiveData<Boolean> deleteAccountStatus;

    {
        deleteAccountStatus = new MutableLiveData<>();
    }

    /** */
    private MutableLiveData<Boolean> updatePassword = new MutableLiveData<>();
    /** */
    private DatabaseManager dbManager = DatabaseManager.createInstance();

    /**
     * a.
     * @return a
     */
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    /**
     *
     * @return a
     */
    LiveData<Boolean> getDeleteAccountStatus() {
        return deleteAccountStatus;
    }

    /**
     *
     * @return a
     */
    LiveData<Boolean> getReAuthenticateStatus() {
        return reauthenticateStatus;
    }

    /**
     *  Method that returns the live data of the
     *  password status.
     * @return LiveData to be observed when the password change.
     */
    public LiveData<Boolean> getUpdatePasswordStatus() {
        return updatePassword;
    }

    /**
     *
     */
    private void deleteUser() {
        auth.deleteUser().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull final Task<Void> task) {
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

    /**
     *
     */
    void authenticateUser() {
        auth.reauthenticateUser()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
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

    /**
     *
     */
    public void deleteAccount() {
        auth.reauthenticateUser()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.v("reauth", "Success");
                            deleteUser();
                        } else {
                            Log.v("reauth", "failed");
                        }
                    }
                });
    }

    /**
     *
     * @param newPassword as
     */
    private void updatePassword(final String newPassword) {
        auth.updateUserPassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.v("uppass", "Success");
                        } else {
                            Log.v("uppass", "failed");
                        }
                    }
                });
    }

    /**
     *
     * @param newPassword as
     */
    public void updateUserPassword(final String newPassword) {
        auth.reauthenticateUser()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.v("reauth", "Success");
                            updatePassword(newPassword);
                        } else {
                            Log.v("reauth", "failed");
                        }
                    }
                });
    }

    /**
     *
     */
    void closeSession() {
        auth.signOut();
    }

    /**
     *
     */
    void getCurrentUserId() {
        dbManager.getCollectionRef("users")
                .whereEqualTo("email", Parameters.CURRENT_USER_EMAIL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(
                            @NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            if (query != null && query.size() == 1) {
                                Parameters.CURRENT_USER_ID =
                                        query.getDocuments().get(0).getId();
                            }
                        }
                    }
                });
    }

}
