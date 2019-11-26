package com.iteso.tanderomobile.fragments.admin;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.activities.base.ActivityBase;
import com.iteso.tanderomobile.adapters.AdapterTandasOrganizer;
import com.iteso.tanderomobile.utils.Parameters;
import com.iteso.tanderomobile.utils.ui.CreateTandaDialogFragment;
import com.iteso.tanderomobile.utils.ui.CustomProgressDialog;
import android.os.Bundle;
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
import java.util.List;
/**This class holds the AdminOrganizerFragment.
 * It is responsible for showing the batches the current user is organizing.
 * As well as the register form for another new batch.
 * */
public class AdminOrganizerFragment extends Fragment {
    /**Recycler view to show the batches.*/
    private RecyclerView recyclerView;
    /**Adapter for the batches.*/
    private RecyclerView.Adapter mAdapter;
    /**Progress dialog that shows in the screen whenever the batches have not
     * yet being loaded.*/
    private CustomProgressDialog progressDialog;
    /**This method creates the view for this fragment.
     * @param inflater The Layout inflater.
     * @param container The ViewGroup container.
     * @param savedInstanceState The Saved instance bundle.
     * @return A view for the fragment.
     * .*/
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        AdminOrganizerViewModel organizerViewModel =
                ViewModelProviders.of(this).get(AdminOrganizerViewModel.class);
        final View root = inflater.inflate(
                R.layout.fragment_organizer, container, false);
        progressDialog = new CustomProgressDialog(getActivity());

        organizerViewModel.getTandas().observe(
                this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> s) {
                //Log.v("AdminOrgFragment", s.toString());
                mAdapter = new AdapterTandasOrganizer(s);
                ((AdapterTandasOrganizer) mAdapter).setOnClickListener(
                        new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        Parameters.CURRENT_TANDA = s.get(
                                recyclerView.getChildAdapterPosition(view)
                        ).toString();

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
        organizerViewModel.requestTandas();
        recyclerView = root.findViewById(R.id.fragment_organizer_tandas_rv);
        recyclerView.setHasFixedSize(true);

        FloatingActionButton floatingButton =
                root.findViewById(R.id.fragment_organizer_floatingbutton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                CreateTandaDialogFragment dialog =
                        new CreateTandaDialogFragment();
                dialog.show(getFragmentManager(), "Create tanda");
            }
        });

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // mAdapter = new AdapterTandasOrganizer(myDataset);
        return root;
    }
}
