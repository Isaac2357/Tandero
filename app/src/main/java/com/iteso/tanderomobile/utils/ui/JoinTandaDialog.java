package com.iteso.tanderomobile.utils.ui;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Parameters;

import java.util.HashMap;
import java.util.Map;

public class JoinTandaDialog extends DialogFragment {
    final DatabaseManager dbmanager = DatabaseManager.createInstance();
    CustomProgressDialog progressDialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_join_tanda, null);
        progressDialog = new CustomProgressDialog(getActivity());
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final TextInputEditText editText = view.findViewById(R.id.tanda_unique_key_value);


        builder.setView(view)
                .setTitle("Join tanda")
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog.show();
                        writeRelationUserTandaInDatabase(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void writeRelationUserTandaInDatabase(final String uniqueKey) {
        Log.v("Unique key", uniqueKey);
        dbmanager.getCollectionRef("tandas").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    if (doc.get("uniqueKey") != null) {
                                        String docUniqueKey = (String)doc.get("uniqueKey");
                                        if (docUniqueKey.equals(uniqueKey)) {
                                           addUserToTanda((String)doc.get("name"));
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

    private void addUserToTanda(String tandaName) {
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        info.put("inInvited", true);
        info.put("paymentInfo", "");
        user.put(Parameters.CURRENT_USER_ID, info);
        dbmanager.getCollectionRef("user-tanda")
                .document(tandaName.trim().toLowerCase().replace(" ", ""))
                .set(user, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                    }
                });
    }
}
