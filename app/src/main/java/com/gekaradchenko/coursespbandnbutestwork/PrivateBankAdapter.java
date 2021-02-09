package com.gekaradchenko.coursespbandnbutestwork;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PrivateBankAdapter extends RecyclerView.Adapter<PrivateBankAdapter.PrivateBankViewHolder> {

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    private ArrayList<PrivateBank> privateBankArrayList = new ArrayList<>();

    public void setPrivateBankArrayList(ArrayList<PrivateBank> privateBankArrayList) {
        this.privateBankArrayList = privateBankArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrivateBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_for_private_bank, parent, false);
        PrivateBankViewHolder privateBankViewHolder = new PrivateBankViewHolder(view, mListener);
        return privateBankViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PrivateBankViewHolder holder, int position) {
        PrivateBank privateBank =privateBankArrayList.get(position);
        holder.nameValPrivateBankTextView.setText(privateBank.getVal());
        holder.buyValPrivateBankTextView.setText(privateBank.getBuy());
        holder.saleValPrivateBankTextView.setText(privateBank.getSale());

    }

    @Override
    public int getItemCount() {
        return privateBankArrayList.size();
    }

    public class PrivateBankViewHolder extends RecyclerView.ViewHolder {

        private TextView nameValPrivateBankTextView;
        private TextView buyValPrivateBankTextView;
        private TextView saleValPrivateBankTextView;

        public PrivateBankViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameValPrivateBankTextView = itemView.findViewById(R.id.nameValPrivateBankTextView);
            buyValPrivateBankTextView = itemView.findViewById(R.id.buyValPrivateBankTextView);
            saleValPrivateBankTextView = itemView.findViewById(R.id.saleValPrivateBankTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
