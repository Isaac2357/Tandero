package com.iteso.tanderomobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.iteso.tanderomobile.R;
import java.util.List;

/**Adapter for the organizer batches view.*/
public class AdapterTandasOrganizer extends
        RecyclerView.Adapter<AdapterTandasOrganizer.MyViewHolder>
        implements View.OnClickListener {
    /**data set of batches.*/
    private List<String> mDataset;
    /**On click listener.*/
    private View.OnClickListener listener;
    /**sets the click listener.
     * @param rListener the click listener.
     * */
    public void setOnClickListener(final View.OnClickListener rListener) {
        this.listener = rListener;
    }

    /**Method to perform the onClick on the listener.
     * @param view the current view.
     * */
    @Override
    public void onClick(final View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }
    /**View holder for the batch item.*/
    static class MyViewHolder extends RecyclerView.ViewHolder {
        /**The name of the batch shown in the view.*/
        private TextView tanda;
        /**Constructor for the view holder.
         * @param view the view.
         * */
        MyViewHolder(final View view) {
            super(view);
            tanda = view.findViewById(R.id.item_tanda);
        }
    }
    /**Constructor for the adapter.
     * @param myDataSet received data set.
     * */
    public AdapterTandasOrganizer(final List<String> myDataSet) {
        mDataset = myDataSet;
    }
    /**View holder for the batches organizer.
     * @param parent ViewGroup parent.
     * @param viewType the view type.
     * @return A view holder for the adapter.*/
    public AdapterTandasOrganizer.MyViewHolder  onCreateViewHolder(
            final ViewGroup parent,
            final int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tanda, parent, false);

        v.setOnClickListener(this);
        return new MyViewHolder(v);
    }
    /** Replace the contents of a view (invoked by the layout manager).
     * @param holder MyViewHolder to bind.
     * @param position the position of the dataset.*/
    @Override
    public void onBindViewHolder(
            final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tanda.setText(mDataset.get(position));

    }
    /**The item count.
     * @return The item count.*/
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
