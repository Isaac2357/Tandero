package com.iteso.tanderomobile.utils.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.google.firebase.Timestamp;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.Constants;
import com.iteso.tanderomobile.utils.SharedPrefs;
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
    private EditText year;
    private DatabaseManager dbmanager;
    private Random random;
    private SharedPrefs sharedPrefs = null;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        FragmentActivity activity = getActivity();
        LayoutInflater inflater;
        View view =  null;
        if (activity != null) {
            inflater = getActivity().getLayoutInflater();
            view = inflater.inflate(R.layout.dialog_createtanda, null);
            sharedPrefs = new SharedPrefs(activity);

        }
        dbmanager = DatabaseManager.createInstance();

        if (view != null) {
            initViews(view);
            random = new Random();
            builder.setView(view)
                    .setTitle(getString(R.string.dialog_create_tanda_title))
                    .setPositiveButton(getString(R.string.dialog_create_tanda_positive), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Update database
                            if (isValidForm()) {
                                writeTandaInDatabase();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.app_invalid_form), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.dialog_create_tanda_negative), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

        }
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void initViews(View view) {
        nombreTanda = view.findViewById(R.id.dialog_createtanda_nombretanda);
        numParticipantes = view.findViewById(R.id.dialog_createtanda_participantes);
        frecuenciaPago = view.findViewById(R.id.dialog_createtanda_frecPago_rg);
        diaDeCobro = view.findViewById(R.id.dialog_createtanda_diasCobro_rg);
        monto = view.findViewById(R.id.dialog_createtanda_montoaportacion);
        dia = view.findViewById(R.id.dialog_createtanda_dia_et);
        mes = view.findViewById(R.id.dialog_createtanda_mes_et);
        year = view.findViewById(R.id.dialog_createtanda_año_et);
    }

    private void writeTandaInDatabase() {
        Map<String, Object> docData = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        docData.put(Constants.FB_TANDA_QTY, Double.parseDouble(monto.getText().toString()));
        docData.put(Constants.FB_TANDA_START_DATE, new Timestamp(
                new Date(
                        Integer.parseInt(year.getText().toString()) - 1900,
                        Integer.parseInt(mes.getText().toString()) + 1,
                         Integer.parseInt(dia.getText().toString())
                )
        ));

        docData.put(Constants.FB_TANDA_PAYMENT_DAY, true);
        docData.put(Constants.FB_TANDA_PAYMENT_DATES, new ArrayList<>());
        docData.put(Constants.FB_TANDA_PAYMENT_FREQUENCY, true);
        docData.put(Constants.FB_TANDA_IS_CLOSED, false);
        docData.put(Constants.FB_TANDA_UNIQUE_KEY,
                nombreTanda.getText().toString().replaceAll("\\s+", "")
                        + (char) (random.nextInt(26) + 'a')
                        + (char) (random.nextInt(26) + 'a')
                        + (char) (random.nextInt(26) + 'a'));
        docData.put(Constants.FB_TANDA_MAX_PAR, Integer.parseInt(numParticipantes.getText().toString()));
        docData.put(Constants.FB_TANDA_NAME, nombreTanda.getText().toString());
        String organizaer = (String) sharedPrefs.getFromPrefs(Constants.CURRENT_USER_EMAIL, "");
        docData.put(Constants.FB_TANDA_ORGANIZER, organizaer);
        docData.put(Constants.FB_TANDA_PAYMENTS_DONE, new ArrayList<>());

        dbmanager.getCollectionRef(Constants.FB_COLLECTION_TANDA).document().set(docData);

        data.put(Constants.FB_USERTANDA_NAME, nombreTanda.getText().toString());

        dbmanager.getCollectionRef(Constants.FB_COLLECTION_USERTANDA).document(nombreTanda.getText()
                                                                                            .toString()
                                                                                            .toLowerCase()
                                                                                            .replaceAll("\\s+", ""))
                                                                     .set(data);

        //notify changes on recyclerview
    }

    private boolean isValidForm() {
        return isValidTandaName()
                && isValidaNumOfParticipants()
                && isValidAmount()
                && isValidDate();
    }

    private boolean isNotEmptyOrBlank(String str) {
        return !str.isEmpty()&& !str.replace(" ", "").isEmpty();
    }

    private boolean hasSpaces(String str) {
        return str.contains(" ");
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidTandaName() {
        if (nombreTanda.getText() == null) {
            return false;
        }
        String nameTanda = nombreTanda.getText().toString();
        return isNotEmptyOrBlank(nameTanda);
    }
    private boolean isValidaNumOfParticipants() {
        if (numParticipantes.getText() == null) {
            return false;
        }
        String nP = numParticipantes.getText().toString();
        return isNotEmptyOrBlank(nP) && isInteger(nP);
    }
    private boolean isValidAmount() {
        if (monto.getText() == null) {
            return false;
        }
        String amount = monto.getText().toString();
        return isNotEmptyOrBlank(amount) && isDouble(amount) && Double.parseDouble(amount) > 100.0;
    }
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
