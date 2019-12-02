package com.iteso.tanderomobile.utils.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Constants;
import com.iteso.tanderomobile.utils.Parameters;
import com.iteso.tanderomobile.utils.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class JoinTandaDialog extends DialogFragment {
    /**
     * DatabaseManager .
     */
    private DatabaseManager dbmanager = DatabaseManager.createInstance();
    /**
     * CustomProgressDialog.
     */
    private CustomProgressDialog progressDialog = null;
    /**
     * SharedPrefs .
     */
    private SharedPrefs sharedPrefs = null;

    @NonNull
    final @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        LayoutInflater inflater;
        View view =  null;
        AlertDialog.Builder builder;

        if (activity != null) {
            inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.dialog_join_tanda, null);
            progressDialog = new CustomProgressDialog(activity);
            sharedPrefs = new SharedPrefs(activity);
            // Use the Builder class for convenient dialog construction
            final TextInputEditText editText = view.findViewById(R.id.tanda_unique_key_value);
            builder = new AlertDialog.Builder(activity);
            builder.setView(view)
                    .setTitle(getString(R.string.dialog_join_tanda_title))
                    .setPositiveButton(getString(R.string.dialog_join_tanda_positive), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            progressDialog.show();
                            if (editText.getText() != null) {
                                writeRelationUserTandaInDatabase(editText.getText().toString());
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.dialog_join_tanda_negative), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            // User cancelled the dialog
                        }
                    });
            return builder.create();
        }


        // Create the AlertDialog object and return it.
        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.dialog_error_join_tanda_message))
                .setPositiveButton(R.string.dialog_error_join_tanda_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.dismiss();
                    }
                }).create();
    }

    /**
     *
     * @param uniqueKey .
     */
    private void writeRelationUserTandaInDatabase(final String uniqueKey) {
        Log.v("Unique key", uniqueKey);
        dbmanager.getCollectionRef(Constants.FB_COLLECTION_TANDA).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    if (doc.get(Constants.FB_TANDA_UNIQUE_KEY) != null) {
                                        String docUniqueKey = (String) doc.get(Constants.FB_TANDA_UNIQUE_KEY);
                                        if (docUniqueKey != null && docUniqueKey.equals(uniqueKey)) {
                                            String name;
                                            if (doc.get(Constants.FB_TANDA_NAME) != null) {
                                                name = (String) doc.get(Constants.FB_TANDA_NAME);
                                                if (name != null) {
                                                    addUserToTanda(name);
                                                }
                                            }
                                            break;
                                        }
                                    }

                                }
                                progressDialog.dismiss();
                            }
                        }
                    }
                }
        );
    }

    /**
     *
     * @param tandaName .
     */
    private void addUserToTanda(final String tandaName) {
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        info.put(Constants.FB_USERTANDA_IS_INVITED, true);
        info.put(Constants.FB_USERTANDA_PAYMENT_INFO, "");
        String userID = (String) sharedPrefs.getFromPrefs(Constants.CURRENT_USER_ID, "");
        user.put(userID, info);
        dbmanager.getCollectionRef(Constants.FB_COLLECTION_USERTANDA)
                .document(tandaName.trim().toLowerCase().replace(" ", ""))
                .set(user, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        progressDialog.dismiss();
                    }
                });
    }
}
