package com.iteso.tanderomobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iteso.tanderomobile.R;

import java.util.List;

public class AdapterTandasOrganizerDetail extends RecyclerView.Adapter<AdapterTandasOrganizerDetail.MyViewHolder> {
    private List<String> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView usuario;
        public MyViewHolder(View v) {
            super(v);
            usuario = v.findViewById(R.id.item_usuario);
        }



    }
    public AdapterTandasOrganizerDetail(List<String> myDataSet){
        this.mDataset = myDataSet;
    }
    public AdapterTandasOrganizerDetail.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);

        AdapterTandasOrganizerDetail.MyViewHolder vh = new AdapterTandasOrganizerDetail.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterTandasOrganizerDetail.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.usuario.setText(mDataset.get(position));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
