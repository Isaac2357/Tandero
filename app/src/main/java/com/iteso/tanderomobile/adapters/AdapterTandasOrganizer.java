package com.iteso.tanderomobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iteso.tanderomobile.R;

import java.util.List;

public class AdapterTandasOrganizer extends RecyclerView.Adapter<AdapterTandasOrganizer.MyViewHolder> {
    private List<String> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tanda;
        public MyViewHolder(View v) {
            super(v);
            tanda = v.findViewById(R.id.item_tanda);
        }
    }

    public AdapterTandasOrganizer(List<String> myDataSet){
        mDataset = myDataSet;
    }

    public AdapterTandasOrganizer.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tanda, parent, false);


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tanda.setText(mDataset.get(position));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
