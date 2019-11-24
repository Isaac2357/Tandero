package com.iteso.tanderomobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.iteso.tanderomobile.R;
import java.util.List;
/**Adapter for an specific batch.*/
public class AdapterTandasOrganizerDetail extends
        RecyclerView.Adapter<AdapterTandasOrganizerDetail.MyViewHolder> {
    /**Dataset for the batch.*/
    private List<String> mDataset;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        /**TextView for the user item. */
        private TextView usuario;
        MyViewHolder(final View v) {
            super(v);
            usuario = v.findViewById(R.id.item_usuario);
        }
    }

    /**Adapter tandas organizer detail constructor.
     * @param myDataSet the data set received.*/
    public AdapterTandasOrganizerDetail(final List<String> myDataSet) {
        this.mDataset = myDataSet;
    }
    /**The view holder on create method.
     * @param parent The view group.
     * @param viewType The view type.
     * @return The view holder.
     * */
    @NonNull
    public AdapterTandasOrganizerDetail.MyViewHolder onCreateViewHolder(
            final ViewGroup parent,
            final int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);

        AdapterTandasOrganizerDetail.MyViewHolder vh =
                new AdapterTandasOrganizerDetail.MyViewHolder(v);
        return vh;
    }
    /**Binds with the position of the dataset.
     * @param holder The View Holder.
     * @param position the position of the list of the dataset.*/
    @Override
    public void onBindViewHolder(
            final AdapterTandasOrganizerDetail.MyViewHolder holder,
            final int position) {
        holder.usuario.setText(mDataset.get(position));

    }
    /**Returns the items inside the DataSet.
     * @return size of the DataSet.*/
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
