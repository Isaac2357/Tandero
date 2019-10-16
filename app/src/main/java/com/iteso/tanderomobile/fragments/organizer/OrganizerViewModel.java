package com.iteso.tanderomobile.fragments.organizer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizerViewModel extends ViewModel {

    private MutableLiveData<List<String>> misTandas = new MutableLiveData<>();
    private DatabaseManager dbmanager = DatabaseManager.createInstance();


    public LiveData<List<String>> getTandas() {
        return misTandas;
    }

    public void requestTandas(){

        dbmanager.getCollectionRef("tandas").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> tandas = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                tandas.add((String)document.get("name"));
                            }
                            misTandas.postValue(tandas);
                        } else {

                        }
                    }
                }
        );
    }
}
