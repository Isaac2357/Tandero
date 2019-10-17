package com.iteso.tanderomobile.fragments.organizer.user;

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
import com.iteso.tanderomobile.fragments.organizer.admin.AdminOrganizerViewModel;
import com.iteso.tanderomobile.utils.ui.CreateTandaDialogFragment;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;
import com.iteso.tanderomobile.utils.ui.JoinTandaDialog;

import java.util.List;

public class UserOrganizerFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private UserOrganizerViewModel organizerViewModel;
    private FloatingActionButton floatingButton;
    private CustomProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        organizerViewModel = ViewModelProviders.of(this).get(UserOrganizerViewModel.class);

        View root = inflater.inflate(R.layout.fragment_organizer, container, false);
        progressDialog = new CustomProgressDialog(getActivity());

        organizerViewModel.getTandas().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> s) {
                Log.v("Tandas", s.toString());
                mAdapter = new AdapterTandasOrganizer(s);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });

        progressDialog.show();
        organizerViewModel.requestTandas();

        recyclerView = root.findViewById(R.id.fragment_organizer_tandas_rv);
        recyclerView.setHasFixedSize(true);

        floatingButton = root.findViewById(R.id.fragment_organizer_floatingbutton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinTandaDialog dialog = new JoinTandaDialog();
                dialog.show(getFragmentManager(), "Create tanda");
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }
}
