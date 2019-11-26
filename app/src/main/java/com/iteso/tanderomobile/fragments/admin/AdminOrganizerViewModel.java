package com.iteso.tanderomobile.fragments.admin;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Parameters;

import java.util.ArrayList;
import java.util.List;
/**Admin Organizer ViewModel for the batches owned view.*/
public class AdminOrganizerViewModel extends ViewModel {
    /**Live data of lists of strings, containing the names of the batches.*/
    private MutableLiveData<List<String>> misTandas = new MutableLiveData<>();
    /**Database manager.*/
    private DatabaseManager dbmanager = DatabaseManager.createInstance();

    /**Getter for the variable that contains the batches names.
     * @return misTandas variable.
     * */
    public LiveData<List<String>> getTandas() {
        return misTandas;
    }

    /**This method is called on the fragment,
     * so that it requests the tandas on firebase.*/
    public void requestTandas() {
        dbmanager.getCollectionRef("tandas").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(
                            @NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> tandas = new ArrayList<>();
                            for (QueryDocumentSnapshot document
                                    : task.getResult()) {
                                if (document.get("organizador").
                                        toString().
                                        equals(Parameters.CURRENT_USER_EMAIL)) {
                                    tandas.add((String) document.get("name"));
                                }
                            }
                            misTandas.postValue(tandas);
                        }
                    }
                }
        );
    }
}
