package com.iteso.tanderomobile.fragments.organizer.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.adapters.AdapterTandasOrganizer;
import com.iteso.tanderomobile.adapters.AdapterTandasOrganizerDetail;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;

import java.util.List;

public class OrganizerTandaFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CustomProgressDialog progressDialog;
    private OrganizerTandaViewModel organizerTandaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        organizerTandaViewModel = ViewModelProviders.of(this).get(OrganizerTandaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_organizer_tanda, container, false);
        progressDialog = new CustomProgressDialog(getActivity());

        organizerTandaViewModel.getParticipantes().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> s) {
                Log.v("participantes", s.toString());
                mAdapter = new AdapterTandasOrganizerDetail(s);



                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
        organizerTandaViewModel.requestParticipantes();

        recyclerView = root.findViewById(R.id.frag_org_tanda_recyclerview_participantes);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }
}
