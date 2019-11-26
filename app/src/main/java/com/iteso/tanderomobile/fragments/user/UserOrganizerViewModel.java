package com.iteso.tanderomobile.fragments.user;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Parameters;
import java.util.ArrayList;
import java.util.List;

import static com.iteso.tanderomobile.utils.Constants.FB_COLLECTION_USERTANDA;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_NAME;

/**User Organizer View Model for the User Organizer Fragment.*/
public class UserOrganizerViewModel extends ViewModel {
    /**List of batches.*/
    private MutableLiveData<List<String>> misTandas = new MutableLiveData<>();
    /**The instance of the database.*/
    private DatabaseManager dbmanager = DatabaseManager.createInstance();
    /**Returns the batches.
     * @return the batches variable.*/
    LiveData<List<String>> getTandas() {
        return misTandas;
    }
    /**Request tandas with instance of database.
     * @param userID current user id.
     **/
    public void requestTandas(final String userID) {
        dbmanager.getCollectionRef(FB_COLLECTION_USERTANDA)
                 .get().addOnCompleteListener(
                         new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        ArrayList<String> myTandas = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (doc.get(userID) != null) {
                                myTandas.add((String) doc.get(FB_TANDA_NAME));
                            }
                        }
                        misTandas.postValue(myTandas);
                    }
                }
            }
        });
    }

}
