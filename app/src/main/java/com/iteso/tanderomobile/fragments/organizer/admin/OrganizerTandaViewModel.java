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

public class OrganizerTandaViewModel extends ViewModel {
    private MutableLiveData<List<String>> participantes = new MutableLiveData<>();
    private DatabaseManager dbmanager = DatabaseManager.createInstance();
    private Map<String, Object> mapa;
    public LiveData<List<String>> getParticipantes() {
        return participantes;
    }

    //TODO
    public void requestParticipantes(){
        dbmanager.getCollectionRef("user-tanda").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.v("otvm", Parameters.CURRENT_TANDA + "-" + Parameters.CURRENT_USER_ID);
                            final ArrayList<String> participantesAux = new ArrayList<>();
                            mapa = new HashMap<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                if(Parameters.CURRENT_TANDA.equals(document.getString("name"))  ){

                                    mapa = document.getData();
                                    mapa.remove("name");
                                    dbmanager.getCollectionRef("users").get().addOnCompleteListener(
                                            new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                                    if(task2.isSuccessful()){
                                                        for(QueryDocumentSnapshot document2 : task2.getResult()){
                                                            for(String keys : mapa.keySet()){
                                                                if(document2.getId().equals(keys)){
                                                                    participantesAux.add(document2.getString("email"));
                                                                }
                                                            }
                                                        }
                                                        participantes.postValue(participantesAux);
                                                    }
                                                    Log.v("Participantes", participantesAux.toString());
                                                }
                                            }
                                    );


                                    Log.v("Mapa: ", mapa.toString());
                                }
                            }


                        }
                    }
                }
        );
    }
}
