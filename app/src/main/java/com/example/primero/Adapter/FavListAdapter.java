package com.example.primero.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.primero.Models.FavModel;
import com.example.primero.Models.TurfModel;
import com.example.primero.R;

import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.FavListViewHolder> {

    LiveData<List<FavModel>> favList;
    Context context;
    public FavListAdapter(Context context, LiveData<List<FavModel>> favList) {
        this.favList = favList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_list_item,parent,false);
        FavListViewHolder holder = new FavListViewHolder(view);
        return holder;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull FavListViewHolder holder, int position) {

        holder.turfName.setText(favList.getValue().get(position).getTurfName());
        holder.turfLocation.setText(favList.getValue().get(position).getTurfLocation());
        holder.turfSize.setText(favList.getValue().get(position).getTurfSize());
        Glide.with(context)
                .load(favList.getValue().get(position).getImgUrl())
                .placeholder(context.getResources().getDrawable(R.drawable.turf_2))
                .into(holder.turfImage);


    }

    @Override
    public int getItemCount() {
        return favList.getValue().size();
    }

    public class FavListViewHolder extends RecyclerView.ViewHolder{

        TextView turfName,turfLocation,turfSize;
        ImageView turfImage;

        public FavListViewHolder(@NonNull View itemView) {
            super(itemView);
            turfName = itemView.findViewById(R.id.fav_turf_title);
            turfLocation = itemView.findViewById(R.id.fav_turf_location);
            turfSize = itemView.findViewById(R.id.fav_turf_size);
            turfImage = itemView.findViewById(R.id.fav_turf_img);
        }
    }
}
