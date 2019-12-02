package com.iteso.tanderomobile.fragments.admin;

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
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.adapters.AdapterTandasOrganizerDetail;
import com.iteso.tanderomobile.utils.SharedPrefs;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;

import java.util.List;
/**Organizer tanda fragment, shows given a batch, its
 * current information, and their current participants.*/
public class OrganizerTandaFragment extends Fragment {
    /**Recycler view for the participants.*/
    private RecyclerView recyclerView;
    /**Adapter for the recyclerview variable.*/
    private RecyclerView.Adapter mAdapter;
    /**Progress dialog that shows in the screen whenever the batches have not
     * yet being loaded.*/
    private CustomProgressDialog progressDialog;
    /** SharedPrefences. */
    private SharedPrefs sharedPrefs;
    /**This method creates a view for this fragment.
    * @param inflater inflater used to inflate the layout.
    * @param container Container of the view group.
    * @param savedInstanceState Bundle of the view.
    * @return a view for the fragment.
    * */
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        sharedPrefs = new SharedPrefs(getActivity());

        OrganizerTandaViewModel organizerTandaViewModel =
                ViewModelProviders.of(this).get(
                        OrganizerTandaViewModel.class);
        View root = inflater.inflate(
                R.layout.fragment_organizer_tanda,
                container,
                false);
        progressDialog = new CustomProgressDialog(getActivity());

        organizerTandaViewModel.getParticipants().observe(
                this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> s) {
                if (s != null) {
                    Log.v("participantes", s.toString());
                }
                mAdapter = new AdapterTandasOrganizerDetail(s);



                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
        organizerTandaViewModel.requestParticipantes(sharedPrefs);

        recyclerView = root.findViewById(
                R.id.frag_org_tanda_recyclerview_participantes);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }
}
