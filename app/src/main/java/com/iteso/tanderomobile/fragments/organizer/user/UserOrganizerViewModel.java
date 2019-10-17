package com.iteso.tanderomobile.fragments.organizer.user;

import android.util.Log;

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

public class UserOrganizerViewModel extends ViewModel {

    private MutableLiveData<List<String>> misTandas = new MutableLiveData<>();
    private DatabaseManager dbmanager = DatabaseManager.createInstance();

    public LiveData<List<String>> getTandas() {
        return misTandas;
    }

    public void requestTandas() {
        dbmanager.getCollectionRef("user-tanda")
                 .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        ArrayList<String> myTandas = new ArrayList<>();
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (doc.get(Parameters.CURRENT_USER_ID) != null) {
                                myTandas.add((String)doc.get("name"));
                            }
                        }
                        misTandas.postValue(myTandas);
                    }
                }
            }
        });
    }

}
