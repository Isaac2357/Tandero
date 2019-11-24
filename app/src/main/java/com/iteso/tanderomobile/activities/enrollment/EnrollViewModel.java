package com.iteso.tanderomobile.activities.enrollment;

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

class EnrollViewModel extends ViewModel {
    /** */
    private AuthenticationManager auth = AuthenticationManager.createInstance();
    /** */
    private DatabaseManager dbManager = DatabaseManager.createInstance();
    /** */
    private MutableLiveData<Boolean> createAccountStatus = new MutableLiveData<>();

    /**
     * Get account status.
     * @return Account status.
     */
    LiveData<Boolean> getCreateAccountStatus() {
        return createAccountStatus;
    }

    /**
     * Create user account method.
     * @param email User email
     * @param password User password.
     */
    void createAccount(final String email, final String password) {
        auth.createAccount(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createAccountStatus.postValue(true);
                        } else {
                            createAccountStatus.setValue(false);
                        }
                    }
                });
    }

    /**
     *
     * @return Current user.
     */
    FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    /**
     * Register user in the database.
     * @param email New user's email.
     * @param password New user's password.
     */
    void writeUserInDatabase(final String email, final String password) {

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);
        user.put("isActivated", true);
        user.put("isPrivate", true);
        user.put("ratingOrganizador", 5);
        user.put("ratingParticipante", 5);
        user.put("tandasOwned", 0);

        dbManager.getCollectionRef("users")
                    .document()
                    .set(user);
    }
}
