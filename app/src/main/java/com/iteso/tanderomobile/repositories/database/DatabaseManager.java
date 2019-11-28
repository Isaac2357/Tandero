package com.iteso.tanderomobile.repositories.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public final class DatabaseManager {
    /** DatbaseManager instance.*/
    private static DatabaseManager instance = null;
    /** Firebase instance.*/
    private final FirebaseFirestore firestoreInstance;

    /**
     * Private constructor for singleton.
     */
    private DatabaseManager() {
        firestoreInstance = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings =
                new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .setPersistenceEnabled(true)
                .setSslEnabled(true)
                .build();
        firestoreInstance.setFirestoreSettings(settings);
    }

    /**
     * Creante instance.
     * @return DatabaseManager instance.
     */
    public static DatabaseManager createInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Get Collection.
     * @param ref Collection name.
     * @return Collenction reference.
     */
    public CollectionReference getCollectionRef(final String ref) {
        if (ref == null) {
            return null;
        }
        return firestoreInstance.collection(ref);
    }

    /**
     * Get dcoument.
     * @param col Collection name.
     * @param ref Document name.
     * @return Document reference.
     */
    public DocumentReference getDocument(final String col, final String ref) {
        return firestoreInstance.collection(col).document(ref);
    }

    /**
     * Get host.
     * @return Firestore instance host.
     */
    public static String getHost() {
        if (instance != null) {
            return instance.firestoreInstance.getFirestoreSettings().getHost();
        }
        return "NO INSTANCE AVAILABLE";
    }
}
