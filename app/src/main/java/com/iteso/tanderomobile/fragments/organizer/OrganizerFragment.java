package com.iteso.tanderomobile.fragments.organizer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.adapters.AdapterTandasOrganizer;
import com.iteso.tanderomobile.utils.CustomProgressDialog;

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

import java.util.List;

public class OrganizerFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private OrganizerViewModel organizerViewModel;
    private FloatingActionButton floatingButton;
    private CustomProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        organizerViewModel = ViewModelProviders.of(this).get(OrganizerViewModel.class);

        View root = inflater.inflate(R.layout.fragment_organizer, container, false);
        progressDialog = new CustomProgressDialog(getActivity());
        organizerViewModel.getTandas().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> s) {
                Log.v("tandas", s.toString());
                mAdapter = new AdapterTandasOrganizer(s);
                ((AdapterTandasOrganizer) mAdapter).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Toast.makeText(getContext(),
                                "selección: " + s.get(recyclerView.getChildAdapterPosition(view)).toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

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
                CreateTandaDialogFragment dialog = new CreateTandaDialogFragment();
                dialog.show(getFragmentManager(), "Create tanda");
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // mAdapter = new AdapterTandasOrganizer(myDataset);
        final FloatingActionButton floatingButton = root.findViewById(R.id.fragment_organizer_floatingbutton);

        return root;
    }
}
