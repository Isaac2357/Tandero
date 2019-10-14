package com.iteso.tanderomobile.repositories.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class DatabaseManager {

    private static DatabaseManager instance = null;
    private final FirebaseFirestore _firestoreInstance;

    private DatabaseManager(){
        _firestoreInstance = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .setPersistenceEnabled(true)
                .setSslEnabled(true)
                .build();
        _firestoreInstance.setFirestoreSettings(settings);
    }

    public static DatabaseManager createInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public CollectionReference getCollectionRef(String ref){
        if(ref == null) return null;
        return _firestoreInstance.collection(ref);
    }

    public DocumentReference getDocument(String col, String ref){
        return _firestoreInstance.collection(col).document(ref);
    }

    public static String getHost(){
        if(instance != null)
            return instance._firestoreInstance.getFirestoreSettings().getHost();

        return "NO INSTANCE AVAILABLE";
    }

}