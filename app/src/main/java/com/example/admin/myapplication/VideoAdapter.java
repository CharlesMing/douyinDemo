package com.example.admin.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerItemNormalHolder> {
    private List<VideoModel> mData;

    public void setData(List<VideoModel> data) {
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerItemNormalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_video_item_normal, parent, false);
        final RecyclerItemNormalHolder holder = new RecyclerItemNormalHolder(parent.getContext(), v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerItemNormalHolder holder, int position) {
        holder.setRecyclerBaseAdapter(this);
        holder.onBind(position, mData.get(position));
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


}
