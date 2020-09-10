package com.example.primero.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.primero.Models.TurfModel;
import com.example.primero.R;
import com.example.primero.Utils.TurfClick;

import java.util.List;

public class TurfListAdapter extends RecyclerView.Adapter<TurfListAdapter.TurfListViewHolder> {

    private static final String TAG = "TurfListAdapter";

    LiveData<List<TurfModel>> turfList;
    TurfClick listner;

    public TurfListAdapter(LiveData<List<TurfModel>> turfList, TurfClick listner) {
        this.turfList = turfList;
        this.listner = listner;
    }

    @NonNull
    @Override
    public TurfListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.turf_list,parent,false);
        TurfListViewHolder holder = new TurfListViewHolder(view,listner);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull TurfListViewHolder holder, int position) {
        holder.turfTitle.setText(turfList.getValue().get(position).getTurfName());
        holder.turfLocation.setText(turfList.getValue().get(position).getTurfLocation());
        holder.turfSize.setText(turfList.getValue().get(position).getTurfSize());



    }

    @Override
    public int getItemCount() {
        return turfList.getValue().size();
    }

    public class TurfListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView turfTitle, turfLocation, turfSize;
        RelativeLayout parent;
        TurfClick listner;

        public TurfListViewHolder(@NonNull View itemView, TurfClick listner) {
            super(itemView);

            this.listner = listner;
            turfTitle = itemView.findViewById(R.id.turf_title);
            turfLocation = itemView.findViewById(R.id.turf_location);
            turfSize = itemView.findViewById(R.id.turf_size);
            parent = itemView.findViewById(R.id.turf_list_parent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: CLICK LISTNER inside view holder ");
            listner.onTurfClick(getAdapterPosition());
        }
    }

}
