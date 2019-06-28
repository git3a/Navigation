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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "ListRecyclerViewAd";

    private ArrayList<String> Materials = new ArrayList<>();
    private ArrayList<String> mnums = new ArrayList<>();
    List<ListModel> list;

    private Context mContext;

    public ListRecyclerViewAdapter(Context mContext,List<ListModel> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case ListModel.METAR_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layour_grid_list,parent,false);
                return new MetaViewHolder(view);

            case ListModel.RECIPE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_recipename,parent,false);
                return new RecipeViewHolder(view);

        }
        return null;
    }

    public int getItemViewType(int position) {
        ListModel model = list.get(position);
        if (model != null){
            return model.getType();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Log.d(TAG,"onBindViewHolder: called");
        ListModel model = list.get(i);
        switch(model.getType()){
            case ListModel.METAR_TYPE:
                ((MetaViewHolder)viewHolder).metarname.setText(model.getmMetaName());
                ((MetaViewHolder)viewHolder).metarquantity.setText(model.getmMetaQuantity());
                ((MetaViewHolder)viewHolder).metarlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"onclick: metar clicked on:" );
                    }
                });
                break;

            case ListModel.RECIPE_TYPE:

                ((RecipeViewHolder)viewHolder).recipename.setText(model.getmRecipeName());
                ((RecipeViewHolder)viewHolder).recipelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"onclick: recipename clicked on:" );
                    }
                });

                break;

        };
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MetaViewHolder extends RecyclerView.ViewHolder{
        //ImageView image;
        TextView metarname;
        TextView metarquantity;
        RelativeLayout metarlayout;

        public MetaViewHolder(@NonNull View itemView){
            super(itemView);
            metarname = itemView.findViewById(R.id.material);
            metarquantity = itemView.findViewById(R.id.num);
            metarlayout = itemView.findViewById(R.id.Cailiaolayout);
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView recipename;
        RelativeLayout recipelayout;

        public RecipeViewHolder(View itemView){
            super(itemView);
            recipename = itemView.findViewById(R.id.recipename);
            recipelayout = itemView.findViewById(R.id.Recipenamelayout);
        }
    }
}