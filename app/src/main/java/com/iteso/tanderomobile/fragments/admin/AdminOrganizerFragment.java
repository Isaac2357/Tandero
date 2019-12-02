package com.iteso.tanderomobile.fragments.admin;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.activities.base.ActivityBase;
import com.iteso.tanderomobile.adapters.AdapterTandasOrganizer;
import com.iteso.tanderomobile.repositories.database.DatabaseManager;
import com.iteso.tanderomobile.utils.SharedPrefs;
import com.iteso.tanderomobile.utils.ui.CreateTandaDialogFragment;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;
import com.iteso.tanderomobile.utils.ui.EditTandaDialogFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import static com.iteso.tanderomobile.utils.Constants.CURRENT_TANDA;
import static com.iteso.tanderomobile.utils.Constants.FB_COLLECTION_TANDA;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_MAX_PAR;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_NAME;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_PAYMENT_DAY;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_PAYMENT_FREQUENCY;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_QTY;
import static com.iteso.tanderomobile.utils.Constants.FB_TANDA_START_DATE;

/**This class holds the AdminOrganizerFragment.
 * It is responsible for showing the batches the current user is organizing.
 * As well as the register form for another new batch.
 * */
public class AdminOrganizerFragment extends Fragment {
    /**Fuck Magic Number. */
    private static final int MONTH_CONST = 1900;
    /**Recycler view to show the batches.*/
    private RecyclerView recyclerView;
    /**Adapter for the batches.*/
    private RecyclerView.Adapter mAdapter;
    /**Progress dialog that shows in the screen whenever the batches have not
     * yet being loaded.*/
    private CustomProgressDialog progressDialog;
    /** SharedPrefences. */
    private SharedPrefs sharedPrefs;
    /**Database Manager.*/
    private DatabaseManager dbmanager;
    /**This method creates the view for this fragment.
     * @param inflater The Layout inflater.
     * @param container The ViewGroup container.
     * @param savedInstanceState The Saved instance bundle.
     * @return A view for the fragment.
     * .*/
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        dbmanager = DatabaseManager.createInstance();
        final AdminOrganizerViewModel organizerViewModel =
                ViewModelProviders.of(this).get(AdminOrganizerViewModel.class);
        final View root = inflater.inflate(
                R.layout.fragment_organizer, container, false);
        if (getActivity() != null) {
            progressDialog = new CustomProgressDialog(getActivity());
        }
        sharedPrefs = new SharedPrefs(getActivity());

        organizerViewModel.getTandas().observe(
                this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> s) {
                //Log.v("AdminOrgFragment", s.toString());
                mAdapter = new AdapterTandasOrganizer(s, true);
                ((AdapterTandasOrganizer) mAdapter).setTandaButtonsListener(
                        new OnTandaClickListener() {
                    @Override
                    public void onEditButtonClick(final String tanda) {
                        editTandaAction(tanda);
                    }

                    @Override
                    public void onDeleteButtonClick(final String tanda) {
                        deleteTandaAction(tanda, organizerViewModel);
                    }
                });
                ((AdapterTandasOrganizer) mAdapter).setOnClickListener(
                        new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        String tanda;
                        if (s != null) {
                            tanda = s.get(recyclerView.
                                    getChildAdapterPosition(view));
                            sharedPrefs.saveToPrefs(CURRENT_TANDA, tanda);
                        }

                        Log.v("AdminOrgFrag", view.getId() + "");
                        ((ActivityBase) getActivity()).openFragment(
                                new OrganizerTandaFragment(), null);

                    }
                });

                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
        organizerViewModel.requestTandas(sharedPrefs);
        recyclerView = root.findViewById(R.id.fragment_organizer_tandas_rv);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton floatingButton =
                root.findViewById(R.id.fragment_organizer_floatingbutton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                CreateTandaDialogFragment dialog =
                        new CreateTandaDialogFragment();
                if (getFragmentManager() != null) {
                    dialog.show(getFragmentManager(), "Create tanda");
                }
            }
        });

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // mAdapter = new AdapterTandasOrganizer(myDataset);
        return root;
    }

    /**Edit tanda action.
     * @param tanda Tanda name.*/
    private void editTandaAction(final String tanda) {
        dbmanager.getCollectionRef(FB_COLLECTION_TANDA).get()
                .addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull final
                                                   Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document
                                            : task.getResult()) {
                                        String tandaOrganizer = document
                                                .getString(FB_TANDA_NAME);
                                        if (tandaOrganizer != null
                                                && tandaOrganizer
                                                .equals(tanda)) {
                                            openEditFragment(document);
                                            break;
                                        }
                                    }
                                }
                            }
                        });
    }

    private void openEditFragment(final QueryDocumentSnapshot document) {
        Date date = document.getDate(FB_TANDA_START_DATE);
        EditTandaDialogFragment dialog =
                new EditTandaDialogFragment(
                        document.getId(),
                        document.getString(FB_TANDA_NAME),
                        document.getLong(FB_TANDA_MAX_PAR),
                        document.getLong(FB_TANDA_QTY),
                        document.getBoolean(FB_TANDA_PAYMENT_FREQUENCY),
                        document.getBoolean(FB_TANDA_PAYMENT_DAY),
                        date.getDate(),
                        date.getMonth() + 1,
                        date.getYear() + MONTH_CONST
                );

        if (getFragmentManager() != null) {
            dialog.show(getFragmentManager(), "Edit tanda");
        }
    }
    /**Delete Tanda action so that passes checkstyle test.
     * @param tanda Tanda name.
     * @param vm View Model.*/
    private void deleteTandaAction(final String tanda,
                                   final AdminOrganizerViewModel vm) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(getContext());
        builder
                .setTitle(R.string.dialog_delete_tanda_title)
                .setMessage(getString(R.string
                        .dialog_delete_tanda_text)
                        + " " + tanda + "?")
                .setPositiveButton(getString(R.string.
                                dialog_sign_out_postive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {
                                vm.
                                        deleteTandaInformation(
                                                tanda,
                                                (ActivityBase) getActivity());

                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_sign_out_negative),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                                final int which) {
                                dialog.dismiss();
                            }
                        })
                .create().show();
    }

}
