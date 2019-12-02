package com.iteso.tanderomobile.fragments.admin;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iteso.tanderomobile.activities.base.ActivityBase;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Constants;
import com.iteso.tanderomobile.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import static com.iteso.tanderomobile.utils.Constants.FB_COLLECTION_TANDA;
import static com.iteso.tanderomobile.utils.Constants.FB_COLLECTION_USERTANDA;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_NAME;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_ORGANIZER;
import static com.iteso.tanderomobile.utils.Constants.FB_USERTANDA_NAME;

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
    /**Delete tanda, in both user-tanda and tanda.
     * @param tanda Tanda name.
     * @param activity ActivityBase.*/
    public void deleteTandaInformation(final String tanda,
    final ActivityBase activity) {
        dbmanager.getCollectionRef(FB_COLLECTION_USERTANDA).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(
                            @NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document
                                    : task.getResult()) {
                                String tandaName = document.
                                        getString(FB_USERTANDA_NAME);
                                if (tandaName != null
                                    && tandaName.equals(tanda)) {
                                    document.getReference().delete();
                                }
                            }
                            deleteTandaInformationAux(tanda, activity);
                        }
                    }
                });
    }
    /**Thisfunction deletes the data on Tanda documents, its made
     * this way so that doesnt start deleting from the said document
     * and cause conflict with User-Tanda documents.
     * @param tanda Tanda name.
     * @param activity Activity.*/
    private void deleteTandaInformationAux(final String tanda,
                                           final ActivityBase activity) {
        dbmanager.getCollectionRef(FB_COLLECTION_TANDA).get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull final
                                                   Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document
                                            : task.getResult()) {
                                        String tandaOrganizer = document
                                                .getString(FB_TANDA_NAME);
                                        if (tandaOrganizer != null
                                        && tandaOrganizer.equals(tanda)) {
                                            document.getReference().delete();
                                        }
                                    }

                                    activity.openFragment(
                                            new AdminOrganizerFragment(), null);
                                }
                            }
                        });
    }

    /**This method is called on the fragment,
     * so that it requests the tandas on firebase.
     * @param sharedPrefs Shared prefs.*/
    public void requestTandas(final SharedPrefs sharedPrefs) {
        final String userEmail = (String) sharedPrefs
                .getFromPrefs(Constants.CURRENT_USER_EMAIL, "");
        dbmanager.getCollectionRef(FB_COLLECTION_TANDA).get()
                .addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(
                            @NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> tandas = new ArrayList<>();
                            for (QueryDocumentSnapshot document
                                    : task.getResult()) {
                                String tandaOrganizer = document
                                        .getString(FB_TANDA_ORGANIZER);
                                if (tandaOrganizer != null
                                        && tandaOrganizer.equals(userEmail)) {
                                    tandas.add((String)
                                            document.get(FB_TANDA_NAME));
                                }
                            }
                            misTandas.postValue(tandas);
                        }
                    }
                }
        );
    }
}
