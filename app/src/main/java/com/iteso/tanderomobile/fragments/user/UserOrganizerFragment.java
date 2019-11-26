package com.iteso.tanderomobile.fragments.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.adapters.AdapterTandasOrganizer;
import com.iteso.tanderomobile.utils.Constants;
import com.iteso.tanderomobile.utils.SharedPrefs;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;
import com.iteso.tanderomobile.utils.ui.JoinTandaDialog;
import java.util.List;
/**User fragment, shows the batches that a given user is in.*/
public class UserOrganizerFragment extends Fragment {
    /**The recyclerview for the batches.*/
    private RecyclerView recyclerView;
    /**The recyclerview adapter for the batches.*/
    private RecyclerView.Adapter mAdapter;
    /**Progress dialog that shows whenver the batches havent loaded in.*/
    private CustomProgressDialog progressDialog;
    /** SharedPrefences. */
    private SharedPrefs sharedPrefs;
    /**Creates the view for the fragment.
     * @param inflater the Layout inflater.
     * @param container The ViewGroup container.
     * @param savedInstanceState The bundle.
     * @return the view. */
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {

        UserOrganizerViewModel organizerViewModel =
                ViewModelProviders.of(this).get(UserOrganizerViewModel.class);

        View root = inflater.inflate(
                R.layout.fragment_organizer, container, false);
        progressDialog = new CustomProgressDialog(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());

        organizerViewModel.getTandas().observe(this,
                new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> s) {
                Log.v("Tandas", s.toString());
                mAdapter = new AdapterTandasOrganizer(s);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });

        progressDialog.show();
        String userID = (String) sharedPrefs.getFromPrefs(Constants.CURRENT_USER_ID, "");
        if (!userID.equals("")) {
            organizerViewModel.requestTandas(userID);
        } else  {
            //TODO handle error
            Log.v("--l", "Error request tanda");
        }

        recyclerView = root.findViewById(R.id.fragment_organizer_tandas_rv);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton floatingButton = root.findViewById(
                R.id.fragment_organizer_floatingbutton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                JoinTandaDialog dialog = new JoinTandaDialog();
                dialog.show(getFragmentManager(), "Create tanda");
            }
        });

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }
}
