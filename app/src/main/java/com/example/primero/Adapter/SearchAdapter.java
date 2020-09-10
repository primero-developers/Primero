package com.example.primero.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.primero.Models.TurfModel;
import com.example.primero.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    LiveData<List<TurfModel>> searchedList;

    public SearchAdapter(LiveData<List<TurfModel>> searchedList) {
        this.searchedList = searchedList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.turf_list,parent,false);
        SearchAdapter.SearchViewHolder holder = new SearchAdapter.SearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.turfTitle.setText(searchedList.getValue().get(position).getTurfName());
        holder.turfLocation.setText(searchedList.getValue().get(position).getTurfLocation());
        holder.turfSize.setText(searchedList.getValue().get(position).getTurfSize());

    }

    @Override
    public int getItemCount() {
        return Math.min(searchedList.getValue().size(), 3);
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView turfTitle,turfLocation,turfSize;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            turfTitle = itemView.findViewById(R.id.turf_title);
            turfLocation = itemView.findViewById(R.id.turf_location);
            turfSize = itemView.findViewById(R.id.turf_size);
        }
    }
}
