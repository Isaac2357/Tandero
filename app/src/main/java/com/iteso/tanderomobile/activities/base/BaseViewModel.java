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
import com.iteso.tanderomobile.utils.Constants;

public class BaseViewModel extends ViewModel {
    /** Current user.*/
    private MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
    /** Auth repository instance. */
    private AuthenticationManager auth = AuthenticationManager.createInstance();
    /** Reauth status.*/
    private MutableLiveData<Boolean> reauthenticateStatus;
    {
        reauthenticateStatus = new MutableLiveData<>();
    }
    /** Delete account status.*/
    private MutableLiveData<Boolean> deleteAccountStatus;

    {
        deleteAccountStatus = new MutableLiveData<>();
    }
    /** ID value.*/
    private MutableLiveData<String> idValue =  new MutableLiveData<>();
    /** Update password status.*/
    private MutableLiveData<Boolean> updatePassword = new MutableLiveData<>();
    /** DB manager instance.*/
    private DatabaseManager dbManager = DatabaseManager.createInstance();

    /**
     * Get current user.
     * @return FirebaseUser current user.
     */
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    /**
     * Account status' getter.
     * @return Delete account live data.
     */
    LiveData<Boolean> getDeleteAccountStatus() {
        return deleteAccountStatus;
    }

    /**
     * ReAuth's getter.
     * @return ReAuth live data.
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
     * @return ID value live data.
     */
    public LiveData<String> getIdValue() {
        return idValue;
    }
    /**
     * Delete user method.
     */
    private void deleteUser() {
        Task<Void> deleteTask = auth.deleteUser();
        if (deleteTask != null) {
            deleteTask.addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    /**
     * Method to authenticate the current user.
     * @param email user email.
     * @param password user password.
     */
    void authenticateUser(final String email, final String password) {
        Task<Void> authTask = auth.reauthenticateUser(email, password);
        if (authTask != null) {
            authTask.addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    /**
     *  Delete account method.
     * @param email user email.
     * @param password user password.
     */
    public void deleteAccount(final String email, final String password) {
        Task<Void> reauthTask = auth.reauthenticateUser(email, password);
        if (reauthTask != null) {
            reauthTask.addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    /**
     *  Method to change the password of the current user.
     * @param newPassword new password.
     */
    private void updatePassword(final String newPassword) {
        Task<Void> updatePassTask = auth.updateUserPassword(newPassword);
        if (updatePassTask != null) {
            updatePassTask.addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    /**
     * Method to chage the password.
     * @param newPassword new password.
     * @param email user email
     * @param password user password
     */
    public void updateUserPassword(final String newPassword, final String email, final String password) {
        Task<Void> updateUsrPassTask = auth.reauthenticateUser(email, password);
        if (updateUsrPassTask != null) {
            updateUsrPassTask.addOnCompleteListener(new OnCompleteListener<Void>() {
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
    }

    /**
     * Close session.
     */
    void closeSession() {
        auth.signOut();
    }

    /**
     * Getter of current user ID.
     * @param userEmail current user email.
     */
    void getCurrentUserId(final String userEmail) {
        dbManager.getCollectionRef(Constants.FB_COLLECTION_USERS)
                .whereEqualTo(Constants.FB_USER_EMAIL, userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(
                            @NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot query = task.getResult();
                            if (query != null && query.size() == 1) {
                                String id =
                                        query.getDocuments().get(0).getId();
                                idValue.postValue(id);
                            } else {
                                idValue.postValue("");
                            }
                        }
                    }
                });
    }

}
