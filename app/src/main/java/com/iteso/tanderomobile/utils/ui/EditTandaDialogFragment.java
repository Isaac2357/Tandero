package com.iteso.tanderomobile.utils.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Constants;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static com.iteso.tanderomobile.utils.Constants.FB_COLLECTION_TANDA;

/**Edit Tanda dialog fragment.*/
public class EditTandaDialogFragment extends DialogFragment {
    /**Tanda name editText.*/
    private EditText nombreTanda;
    /**numParticipantes editText.*/
    private EditText numParticipantes;
    /**frecuenciaPago RadioGroup.*/
    private RadioGroup frecuenciaPago;
    /**diaDeCobro RadioGroup.*/
    private RadioGroup diaDeCobro;
    /**Monto editText.*/
    private EditText monto;
    /**dia editText.*/
    private EditText dia;
    /**mes editText.*/
    private EditText mes;
    /**year editText.*/
    private EditText year;
    /**Database manager.*/
    private DatabaseManager dbmanager;
    /**View.*/
    private View view;
    /**Alert Dialog Builder.*/
    private AlertDialog.Builder builder;
    /**Tanda name variable.*/
    private String strName;
    /**NumParticipantes variable.*/
    private long intNumParticipantes;
    /**payment frequency variable.*/
    private boolean boolFrecuenciaPago;
    /**pay day variable.*/
    private boolean boolDiaCobro;
    /**dia variable.*/
    private int intDia;
    /**mes variable.*/
    private int intMes;
    /**year variable.*/
    private int intYear;
    /**monto variable.*/
    private long intMonto;
    /**Document id.*/
    private String documentID;

    /**Constructor that sets the variables.
     * @param docID document id.
     * @param name Tanda name.
     * @param numParticipantes max number of participants.
     * @param money Money.
     * @param frecPago Payment frequency.
     * @param diaCobro Pay day.
     * @param dia day of the month.
     * @param mes month of the year.
     * @param year year.*/
    public EditTandaDialogFragment(final String docID,
                                   final String name,
                                   final long numParticipantes,
                                   final long money,
                                   final boolean frecPago,
                                   final boolean diaCobro,
                                   final int dia,
                                   final int mes,
                                   final int year
                                   ) {
        this.documentID = docID;
        this.strName = name;
        this.intNumParticipantes = numParticipantes;
        this.intMonto = money;
        this.boolFrecuenciaPago = frecPago;
        this.boolDiaCobro = diaCobro;
        this.intDia = dia;
        this.intMes = mes;
        this.intYear = year;
    }
    /**Empty constructor.*/
    public EditTandaDialogFragment() {}

    /**Dialog on create method.
     * @param savedInstanceState Bundle.
     * @return an Alert Dialog.*/
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        builder = new AlertDialog.Builder(getActivity());
        FragmentActivity activity = getActivity();
        LayoutInflater inflater;
        view =  null;
        if (activity != null) {
            inflater = getActivity().getLayoutInflater();
            view = inflater.inflate(R.layout.dialog_createtanda, null);
        }
        dbmanager = DatabaseManager.createInstance();

        if (view != null) {
            initViews(view);
            builder.setView(view)
                    .setTitle(getString(R.string.dialog_edit_tanda_title))
                    .setPositiveButton(getString(R.string.dialog_edit_tanda_positive), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Update database
                            if (isValidForm()) {
                                updateTandaInDatabase();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.app_invalid_form), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.dialog_edit_tanda_negative), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
        }
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**This method updates the Tanda in the Database.*/
    private void updateTandaInDatabase() {
        Map<String, Object> docData = new HashMap<>();
        docData.put(Constants.FB_TANDA_QTY, Double.parseDouble(monto.getText().toString()));
        docData.put(Constants.FB_TANDA_START_DATE, new Timestamp(
                new Date(
                        Integer.parseInt(year.getText().toString()) - 1900,
                        Integer.parseInt(mes.getText().toString()) + 1,
                        Integer.parseInt(dia.getText().toString())
                )
        ));

        docData.put(Constants.FB_TANDA_PAYMENT_DAY, true);
        docData.put(Constants.FB_TANDA_PAYMENT_FREQUENCY, true);

        docData.put(Constants.FB_TANDA_MAX_PAR, Integer.parseInt(numParticipantes.getText().toString()));

        dbmanager.getCollectionRef(FB_COLLECTION_TANDA).document(this.documentID)
                .update(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("EditTandaDiaFrag", "Sucess!!!");
                    }
                });
    }

    /**Init views method.*/
    private void initViews(View view) {
        nombreTanda = view.findViewById(R.id.dialog_createtanda_nombretanda);
        nombreTanda.setText(strName);
        numParticipantes = view.findViewById(R.id.dialog_createtanda_participantes);
        numParticipantes.setText(intNumParticipantes + "");
        frecuenciaPago = view.findViewById(R.id.dialog_createtanda_frecPago_rg);
        if (boolFrecuenciaPago) {
            frecuenciaPago.check(R.id.dialog_createtanda_frecpago_quincenal);
        } else {
            frecuenciaPago.check(R.id.dialog_createtanda_frecpago_mensual);
        }
        diaDeCobro = view.findViewById(R.id.dialog_createtanda_diasCobro_rg);
        if (boolDiaCobro) {
            diaDeCobro.check(R.id.dialog_createtanda_diasCobro_primeros);
        } else {
            diaDeCobro.check(R.id.dialog_createtanda_diasCobro_mediados);
        }
        monto = view.findViewById(R.id.dialog_createtanda_montoaportacion);
        monto.setText(intMonto + "");
        dia = view.findViewById(R.id.dialog_createtanda_dia_et);
        dia.setText(intDia + "");
        mes = view.findViewById(R.id.dialog_createtanda_mes_et);
        mes.setText(intMes + "");
        year = view.findViewById(R.id.dialog_createtanda_año_et);
        year.setText(intYear + "");
    }
    /**Check if its a valid form.
     * @return true or false.*/
    private boolean isValidForm() {
        return isValidTandaName()
                && isValidaNumOfParticipants()
                && isValidAmount()
                && isValidDate();
    }
    /**Check if its a valid form.
     * @param str String.
     * @return true or false.*/
    private boolean isNotEmptyOrBlank(String str) {
        return !str.isEmpty()&& !str.replace(" ", "").isEmpty();
    }
    /**Check if its a valid form.
     * @param str String.
     * @return true or false.*/
    private boolean hasSpaces(String str) {
        return str.contains(" ");
    }
    /**Check if its a valid form.
     * @param str String.
     * @return true or false.*/
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**Check if its a valid form.
     * @param str String.
     * @return true or false.*/
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**Check if its a valid form.
     * @return true or false.*/
    private boolean isValidTandaName() {
        if (nombreTanda.getText() == null) {
            return false;
        }
        String nameTanda = nombreTanda.getText().toString();
        return isNotEmptyOrBlank(nameTanda);
    }
    /**Check if its a valid form.
     * @return true or false.*/
    private boolean isValidaNumOfParticipants() {
        if (numParticipantes.getText() == null) {
            return false;
        }
        String nP = numParticipantes.getText().toString();
        return isNotEmptyOrBlank(nP) && isInteger(nP);
    }
    /**Check if its a valid form.
     * @return true or false.*/
    private boolean isValidAmount() {
        if (monto.getText() == null) {
            return false;
        }
        String amount = monto.getText().toString();
        return isNotEmptyOrBlank(amount) && isDouble(amount) && Double.parseDouble(amount) > 100.0;
    }
    /**Check if its a valid form.
     * @return true or false.*/
    private boolean isValidDate() {
        if (dia.getText() == null) {
            return false;
        }
        if (mes.getText() == null) {
            return false;
        }
        if (year.getText() == null) {
            return false;
        }
        String day = dia.getText().toString();
        String month = mes.getText().toString();
        String year = this.year.getText().toString();
        return isNotEmptyOrBlank(day) && isInteger(day) && Integer.parseInt(day) > 0 && Integer.parseInt(day) <= 31
                && isNotEmptyOrBlank(month) && isInteger(month) && Integer.parseInt(month) > 0 && Integer.parseInt(month) <= 12
                && isNotEmptyOrBlank(year) && isInteger(year) && Integer.parseInt(year) > 0 && Integer.parseInt(year) <= 2300;
    }
}
