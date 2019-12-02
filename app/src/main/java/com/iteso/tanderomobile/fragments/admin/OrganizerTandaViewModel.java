package com.iteso.tanderomobile.fragments.admin;

import android.util.Log;

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
import com.iteso.tanderomobile.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iteso.tanderomobile.utils.Constants.CURRENT_TANDA;
import static com.iteso.tanderomobile.utils.Constants.FB_COLLECTION_USERS;
import static com.iteso.tanderomobile.utils.Constants.FB_COLLECTION_USERTANDA;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_NAME;
import static com.iteso.tanderomobile.utils.Constants.FB_USER_EMAIL;

/**Batch organizer ViewModel view.*/
public class OrganizerTandaViewModel extends ViewModel {
    /**Mutable list of the names of the participants.*/
    private MutableLiveData<List<String>> participants =
            new MutableLiveData<>();
    /**Database manager.*/
    private DatabaseManager dbmanager = DatabaseManager.createInstance();
    /**Map used to retrieve information with the dbmanager.*/
    private Map<String, Object> retrievedInformation;
    /**This method gets the participants.
     * @return the participants.
     * */
    public LiveData<List<String>> getParticipants() {
        return participants;
    }

    /**
     * Requests the participants in Firebase.
     * @param sharedPrefs shared prefs.
     */
    public void requestParticipantes(final SharedPrefs sharedPrefs) {
        final String currentTanda = (String) sharedPrefs.
                getFromPrefs(CURRENT_TANDA, "");

        dbmanager.getCollectionRef(FB_COLLECTION_USERTANDA).get()
                .addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull
                                           final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<String> participantesAux =
                                    new ArrayList<>();
                            retrievedInformation = new HashMap<>();
                            for (QueryDocumentSnapshot document
                                    : task.getResult()) {
                                if (currentTanda.equals(
                                        document.getString(FB_TANDA_NAME))) {

                                    retrievedInformation = document.getData();
                                    retrievedInformation.remove(FB_TANDA_NAME);

                                    doRequest(participantesAux);

                                    Log.v("OrganizerTandaVM:",
                                            retrievedInformation.toString());
                                }
                            }


                        }
                    }
                }
        );
    }
    /** Method responsible for the request in Firebase.
     * @param participantesAux participants
     * */
    private void doRequest(final ArrayList<String> participantesAux) {
        dbmanager.getCollectionRef(FB_COLLECTION_USERS).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull
                                           final Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2
                                    : task2.getResult()) {
                                for (String keys
                                        : retrievedInformation.keySet()) {
                                    if (document2.getId().equals(keys)) {
                                        participantesAux.add(document2.
                                                getString(FB_USER_EMAIL));
                                    }
                                }
                            }
                            participants.postValue(participantesAux);
                        }
                        Log.v("Participants", participantesAux.toString());
                    }
                }
        );
    }
}
