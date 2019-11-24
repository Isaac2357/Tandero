package com.iteso.tanderomobile.fragments.organizer.admin;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     */
    public void requestParticipantes() {
        dbmanager.getCollectionRef("user-tanda").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull
                                           final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.v("otvm", Parameters.CURRENT_TANDA
                                    + "-" + Parameters.CURRENT_USER_ID);
                            final ArrayList<String> participantesAux =
                                    new ArrayList<>();
                            retrievedInformation = new HashMap<>();
                            for (QueryDocumentSnapshot document
                                    : task.getResult()) {
                                if (Parameters.CURRENT_TANDA.equals(
                                        document.getString("name"))) {

                                    retrievedInformation = document.getData();
                                    retrievedInformation.remove("name");

                                    doRequest(participantesAux);

                                    Log.v("Mapa: ",
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
        dbmanager.getCollectionRef("users").get().addOnCompleteListener(
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
                                                getString("email"));
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
