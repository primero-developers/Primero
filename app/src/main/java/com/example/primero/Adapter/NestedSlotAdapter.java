package com.example.primero.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.primero.Models.SlotModel;
import com.example.primero.R;

import java.util.List;

public class NestedSlotAdapter extends RecyclerView.Adapter<NestedSlotAdapter.NestedSlotViewHolder> {

    List<SlotModel> slotList;
    Context context;

    public NestedSlotAdapter(Context context, List<SlotModel> slotList) {
        this.slotList = slotList;
        this.context = context;
    }

    @NonNull
    @Override
    public NestedSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_slot_layout,parent,false);
        return new NestedSlotViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NestedSlotViewHolder holder, int position) {
        String[] time= slotList.get(position).getTime().split("to");
        String timeStart = time[0].trim();
        String timeEnd = time[1].trim();
        String price = context.getResources().getString(R.string.rupees) + " " + slotList.get(position).getPrice();
        holder.slotStartTime.setText(timeStart);
        holder.slotEndTime.setText(timeEnd);
        holder.slotPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public class NestedSlotViewHolder extends RecyclerView.ViewHolder{

        TextView slotStartTime,slotEndTime, slotPrice;

        public NestedSlotViewHolder(@NonNull View itemView) {
            super(itemView);

            slotStartTime = itemView.findViewById(R.id.slot_time_start);
            slotEndTime = itemView.findViewById(R.id.slot_time_end);
            slotPrice = itemView.findViewById(R.id.nested_slot_price);
        }
    }
}
