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

/**
 * clase .
 */
public class CreateTandaDialogFragment extends DialogFragment {
    /**
     * YEAR .
     */
    public static final int YEAR = 1900;
    /**
     * MONTH .
     */
    public static final int MONTH = 1;
    /**
     * RANDOM .
     */
    public static final int RANDOM = 26;
    /**
     * AMOUNT_CIEN .
     */
    public static final int AMOUNT_CIEN = 100;
    /**
     * PARSE_DAY .
     */
    public static final int PARSE_DAY = 31;
    /**
     * PARSE_MONTH .
     */
    public static final int PARSE_MONTH = 12;
    /**
     * PARSE_YEAR .
     */
    public static final int PARSE_YEAR = 2300;




    /**
     * EditText.
     */
    private EditText nombreTanda;
    /**
     * EditText.
     */
    private EditText numParticipantes;
    /**
     * EditText.
     */
    private RadioGroup frecuenciaPago;
    /**
     * RadioGroup.
     */
    private RadioGroup diaDeCobro;
    /**
     * EditText.
     */
    private EditText monto;
    /**
     * EditText.
     */
    private EditText dia;
    /**
     * EditText.
     */
    private EditText mes;
    /**
     * EditText.
     */
    private EditText year;
    /**
     * DatabaseManager.
     */
    private DatabaseManager dbmanager;
    /**
     * Random.
     */
    private Random random;
    /**
     * SharedPrefs.
     */
    private SharedPrefs sharedPrefs = null;

    final @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
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
                    .setTitle(getString(
                            R.string.dialog_create_tanda_title))
                    .setPositiveButton(getString(
                            R.string.dialog_create_tanda_positive),
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    // Update database
                                    if (isValidForm()) {
                                        writeTandaInDatabase();
                                    } else {
                                        Toast.makeText(getContext(),
                                                getString(R.string.app_invalid_form),
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            })
                    .setNegativeButton(getString(
                            R.string.dialog_create_tanda_negative),
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog,
                                                    final int id) {
                                    // User cancelled the dialog
                                }
                            });

        }
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     *
     * @param view .
     */
    private void initViews(final View view) {
        nombreTanda = view.findViewById(
                R.id.dialog_createtanda_nombretanda);
        numParticipantes = view.findViewById(
                R.id.dialog_createtanda_participantes);
        frecuenciaPago = view.findViewById(
                R.id.dialog_createtanda_frecPago_rg);
        diaDeCobro = view.findViewById(
                R.id.dialog_createtanda_diasCobro_rg);
        monto = view.findViewById(
                R.id.dialog_createtanda_montoaportacion);
        dia = view.findViewById(
                R.id.dialog_createtanda_dia_et);
        mes = view.findViewById(
                R.id.dialog_createtanda_mes_et);
        year = view.findViewById(
                R.id.dialog_createtanda_año_et);
    }

    /**
     *
     */
    private void writeTandaInDatabase() {
        Map<String, Object> docData = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        docData.put(Constants.FB_TANDA_QTY,
                Double.parseDouble(monto.getText().toString()));
        docData.put(Constants.FB_TANDA_START_DATE, new Timestamp(
                new Date(
                        Integer.parseInt(year.getText().toString()) - YEAR,
                        Integer.parseInt(mes.getText().toString()) + MONTH,
                        Integer.parseInt(dia.getText().toString())
                )
        ));

        docData.put(Constants.FB_TANDA_PAYMENT_DAY, true);
        docData.put(Constants.FB_TANDA_PAYMENT_DATES, new ArrayList<>());
        docData.put(Constants.FB_TANDA_PAYMENT_FREQUENCY, true);
        docData.put(Constants.FB_TANDA_IS_CLOSED, false);
        docData.put(Constants.FB_TANDA_UNIQUE_KEY,
                nombreTanda.getText().toString().replaceAll("\\s+", "")
                        + (char) (random.nextInt(RANDOM) + 'a')
                        + (char) (random.nextInt(RANDOM) + 'a')
                        + (char) (random.nextInt(RANDOM) + 'a'));
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

    /**
     *
     * @return .
     */
    private boolean isValidForm() {
        return isValidTandaName()
                && isValidaNumOfParticipants()
                && isValidAmount()
                && isValidDate();
    }

    /**
     *
     * @param str .
     * @return .
     */
    private boolean isNotEmptyOrBlank(final String str) {
        return !str.isEmpty() && !str.replace(" ", "").isEmpty();
    }

    /**
     *
     * @param str .
     * @return .
     */
    private boolean hasSpaces(final String str) {
        return str.contains(" ");
    }

    /**
     *
     * @param str .
     * @return .
     */
    private boolean isInteger(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @param str .
     * @return .
     */
    private boolean isDouble(final String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @return .
     */
    private boolean isValidTandaName() {
        if (nombreTanda.getText() == null) {
            return false;
        }
        String nameTanda = nombreTanda.getText().toString();
        return isNotEmptyOrBlank(nameTanda);
    }

    /**
     *
     * @return .
     */
    private boolean isValidaNumOfParticipants() {
        if (numParticipantes.getText() == null) {
            return false;
        }
        String nP = numParticipantes.getText().toString();
        return isNotEmptyOrBlank(nP) && isInteger(nP);
    }

    /**
     *
     * @return .
     */
    private boolean isValidAmount() {
        if (monto.getText() == null) {
            return false;
        }
        String amount = monto.getText().toString();
        return isNotEmptyOrBlank(amount) && isDouble(amount) && Double.parseDouble(amount) > AMOUNT_CIEN;
    }

    /**
     *
     * @return .
     */
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
        return isNotEmptyOrBlank(day) && isInteger(day) && Integer.parseInt(day) > 0 && Integer.parseInt(day) <= PARSE_DAY
                && isNotEmptyOrBlank(month) && isInteger(month) && Integer.parseInt(month) > 0 && Integer.parseInt(month) <= PARSE_MONTH
                && isNotEmptyOrBlank(year) && isInteger(year) && Integer.parseInt(year) > 0 && Integer.parseInt(year) <= PARSE_YEAR;
    }
}
