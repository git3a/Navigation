package com.example.navigation;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "StaggeredRecyclerViewAd";

    private ArrayList<String> Materials = new ArrayList<>();
    private ArrayList<String> mnums = new ArrayList<>();
    //private ArrayList<Integer> mIds;
    private Context mContext;

    public ListRecyclerViewAdapter(Context mContext,ArrayList<String> mNames, ArrayList<String> mImageUrls, ArrayList<Integer> mIds) {
        this.Materials = mNames;
        this.mnums = mImageUrls;
        //this.mIds = mIds;
        this.mContext = mContext;
    }

    @Override
    public ListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layour_grid_list, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder Holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Holder.material.setText(Materials.get(position));
        Holder.num.setText(mnums.get(position));

    }

    @Override
    public int getItemCount() {
        return Materials.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView material;
        TextView num;


//        public ViewHolder(@NonNull View itemView, TextView material, TextView num) {
//            super(itemView);
//            this.material = material;
//            this.num = num;
//        }
        public ViewHolder(View itemView){
            super(itemView);
            this.material = itemView.findViewById(R.id.material);
            this.num = itemView.findViewById(R.id.num);
        }
    }
}