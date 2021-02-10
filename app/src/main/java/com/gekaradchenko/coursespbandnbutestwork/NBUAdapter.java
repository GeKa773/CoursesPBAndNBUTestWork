package com.gekaradchenko.coursespbandnbutestwork;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NBUAdapter extends RecyclerView.Adapter<NBUAdapter.NBUViewHolder> {


    private ArrayList<NBU> nbuArrayList = new ArrayList<>();


    public void setNbuArrayList(ArrayList<NBU> nbuArrayList) {
        this.nbuArrayList = nbuArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NBUViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item_for_nbu, parent, false);
        NBUViewHolder nbuViewHolder = new NBUViewHolder(view);
        return nbuViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NBUViewHolder holder, int position) {
        NBU nbu = nbuArrayList.get(position);
        holder.valNBUTextView.setText(nbu.getName());
        holder.rateValNBUTextView.setText(nbu.getRate());
        holder.nameValNBUTextView.setText(nbu.getVal());
        if (position%2 !=0){
            holder.itemView.setBackgroundResource(R.color.color_for_second_recycler_view_item);
        }
        if (nbu.isSelect()){
            holder.itemView.setBackgroundResource(R.color.color_for_selected_recycler_view_item);
        }

    }

    @Override
    public int getItemCount() {
        return nbuArrayList.size();
    }

    public class NBUViewHolder extends RecyclerView.ViewHolder {
        private TextView nameValNBUTextView;
        private TextView rateValNBUTextView;
        private TextView valNBUTextView;

        public NBUViewHolder(@NonNull View itemView) {
            super(itemView);
            nameValNBUTextView = itemView.findViewById(R.id.nameValNBUTextView);
            rateValNBUTextView = itemView.findViewById(R.id.rateValNBUTextView);
            valNBUTextView = itemView.findViewById(R.id.valNBUTextView);
        }
    }
}
