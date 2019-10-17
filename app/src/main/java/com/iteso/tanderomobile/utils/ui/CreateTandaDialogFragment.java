package com.iteso.tanderomobile.utils.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.Timestamp;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Parameters;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateTandaDialogFragment extends DialogFragment {

    private EditText nombreTanda;
    private EditText numParticipantes;
    private RadioGroup frecuenciaPago;
    private RadioGroup diaDeCobro;
    private EditText monto;
    private EditText dia;
    private EditText mes;
    private EditText año;
    private DatabaseManager dbmanager;
    private Random random;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_createtanda, null);

        dbmanager = DatabaseManager.createInstance();

        random = new Random();
        nombreTanda = view.findViewById(R.id.dialog_createtanda_nombretanda);
        numParticipantes = view.findViewById(R.id.dialog_createtanda_participantes);
        frecuenciaPago = view.findViewById(R.id.dialog_createtanda_frecPago_rg);
        diaDeCobro = view.findViewById(R.id.dialog_createtanda_diasCobro_rg);
        monto = view.findViewById(R.id.dialog_createtanda_montoaportacion);
        dia = view.findViewById(R.id.dialog_createtanda_dia_et);
        mes = view.findViewById(R.id.dialog_createtanda_mes_et);
        año = view.findViewById(R.id.dialog_createtanda_año_et);
        builder
               .setView(view)
                .setTitle("Registra tu tanda")
                .setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Update database
                        Map<String, Object> docData = new HashMap<>();
                        Map<String, Object> data = new HashMap<>();
                        docData.put("cantidadAportacion", Integer.parseInt(monto.getText().toString()));
                        docData.put("diaInicio", new Timestamp(
                                new Date(
                                        Integer.parseInt(año.getText().toString()) - 1900,
                                        Integer.parseInt(mes.getText().toString()) + 1,
                                        Integer.parseInt(dia.getText().toString())
                                )
                        ));

                        docData.put("diasCobro", true);
                        docData.put("fechasPago", new ArrayList<>());
                        docData.put("frecuenciaPago", true);
                        docData.put("isClosed", false);
                        docData.put("uniqueKey",
                                nombreTanda.getText().toString().replaceAll("\\s+","")
                                        +(char)(random.nextInt(26) + 'a')
                                        +(char)(random.nextInt(26) + 'a')
                                        +(char)(random.nextInt(26) + 'a'));
                        docData.put("maxParticipantes", Integer.parseInt(numParticipantes.getText().toString().toLowerCase()));
                        docData.put("name", nombreTanda.getText().toString());
                        docData.put("organizador", Parameters.CURRENT_USER_EMAIL);
                        docData.put("pagosHechos", new ArrayList<>());

                        dbmanager.getCollectionRef("tandas").document().set(docData);

                        data.put("name", nombreTanda.getText().toString());

                        dbmanager.getCollectionRef("user-tanda").document(
                                nombreTanda.getText().toString()
                                        .toLowerCase().replaceAll("\\s+", "")).set(docData);

                        //notify changes on recyclerview

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
