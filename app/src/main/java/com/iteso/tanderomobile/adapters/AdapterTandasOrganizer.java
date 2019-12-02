package com.iteso.tanderomobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.iteso.tanderomobile.R;
import com.iteso.tanderomobile.fragments.admin.OnTandaClickListener;

import java.util.List;

/**Adapter for the organizer batches view.*/
public class AdapterTandasOrganizer extends
        RecyclerView.Adapter<AdapterTandasOrganizer.MyViewHolder>
        implements View.OnClickListener {
    /**data set of batches.*/
    private List<String> mDataset;
    /**On click listener.*/
    private View.OnClickListener listener;
    /**Tanda listener.*/
    private OnTandaClickListener tandaListener;
    /**Variable that helps hide the buttons.*/
    private boolean isAdminOrUser;
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
        /**Delete button.*/
        private ImageButton deleteButton;
        /**Edit button.*/
        private ImageButton editButton;

        /**Constructor for the view holder.
         * @param view the view.
         * @param isAdminOrUser Depending on its value, hides the buttons.
         * */
        MyViewHolder(final View view, final boolean isAdminOrUser) {
            super(view);
            tanda = view.findViewById(R.id.item_tanda);
            deleteButton = view.findViewById(R.id.item_tanda_delete_button);
            editButton =  view.findViewById(R.id.item_tanda_edit_button);
            if (!isAdminOrUser) {
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
            }
        }
    }
    /**Constructor for the adapter.
     * @param myDataSet received data set.
     * @param newIsAdminOrUser Tells if its the admin fragment or not.
     * */
    public AdapterTandasOrganizer(final List<String> myDataSet,
                                  final boolean newIsAdminOrUser) {
        mDataset = myDataSet;
        this.isAdminOrUser = newIsAdminOrUser;
    }
    /** Set the listener for the buttons.
    * @param newListener Listener for the buttons.
    */
    public void setTandaButtonsListener(
            final OnTandaClickListener newListener) {
        this.tandaListener = newListener;
    }
    /**View holder for the batches organizer.
     * @param parent ViewGroup parent.
     * @param viewType the view type.
     * @return A view holder for the adapter.*/
    @NonNull
    public AdapterTandasOrganizer.MyViewHolder  onCreateViewHolder(
            final ViewGroup parent,
            final int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tanda, parent, false);

        v.setOnClickListener(this);
        return new MyViewHolder(v, this.isAdminOrUser);
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
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                tandaListener.onDeleteButtonClick(mDataset.get(position));
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                tandaListener.onEditButtonClick(mDataset.get(position));
            }
        });
    }
    /**The item count.
     * @return The item count.*/
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
