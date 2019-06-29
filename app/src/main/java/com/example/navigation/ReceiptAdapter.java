package com.example.navigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.ArrayList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
public class ReceiptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "ReceiptAdapter";

    List<RecipetModel> list;
    private Context mContext;

    public ReceiptAdapter(List<RecipetModel> list, Context context) {
         this.list = list;

//        this.mStepIndex = mStepIndex;
//        this.mStepInner = mStepInner;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        RecipetModel model = list.get(position);
        if (model != null){
            return model.getType();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case RecipetModel.METAR_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_metarlist,parent,false);
                return new MetaViewHolder(view);

            case RecipetModel.STEP_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_steplist,parent,false);
                return new StepViewHolder(view);

            case RecipetModel.IMGNAME_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_imagename,parent,false);
                return new ImageNameViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");
        RecipetModel model = list.get(position);
        switch(model.getType()){
            case RecipetModel.STEP_TYPE:
                ((StepViewHolder)holder).stepindex.setText(model.getmStepIndex());
                ((StepViewHolder)holder).stepinner.setText(model.getmStepInner());
                ((StepViewHolder)holder).steplayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"onclick: step clicked on:" );
                    }
                });
                break;
            case RecipetModel.METAR_TYPE:
                ((MetaViewHolder)holder).metarname.setText(model.getmMetaName());
                ((MetaViewHolder)holder).metarquantity.setText(model.getmMetaQuantity());
                ((MetaViewHolder)holder).metarlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"onclick: metar clicked on:" );
                    }
                });
                break;

            case RecipetModel.IMGNAME_TYPE:
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.pic);

                //load recipe image
                GlideApp.with(this.mContext)
                        .load(model.getmRecipeImgurl())
                        //.placeholder(R.drawable.pic)
                        .into(((ImageNameViewHolder) holder).recipeimage);

                ((ImageNameViewHolder)holder).recipename.setText(model.getmRecipeName());
                ((ImageNameViewHolder)holder).metatitle.setText(model.getmMetaTitle());
                ((ImageNameViewHolder)holder).imgnamelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"onclick: image clicked on:" );
                    }
                });
                //buylist onclick responsor
                ((ImageNameViewHolder)holder).buylistbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // addList();
                    }
                });
                //add favourite respnsor
                ((ImageNameViewHolder)holder).addfavorbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // addFavorite();
                    }
                });
                break;

        };
        }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder{
        ImageView image;

        TextView stepindex;
        TextView stepinner;
        RelativeLayout steplayout;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepindex = itemView.findViewById(R.id.stepindex);
            stepinner = itemView.findViewById(R.id.stepinner);
            steplayout = itemView.findViewById(R.id.stepparent_layout);
        }
    }

    class MetaViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView metarname;
        TextView metarquantity;
        RelativeLayout metarlayout;

        public MetaViewHolder(@NonNull View itemView){
            super(itemView);
            metarname = itemView.findViewById(R.id.metarname);
            metarquantity = itemView.findViewById(R.id.metarquantity);
            metarlayout = itemView.findViewById(R.id.metarparent_layout);
        }
    }

    public class ImageNameViewHolder extends RecyclerView.ViewHolder{
        ImageView recipeimage;
        TextView recipename;
        TextView metatitle;
        ImageButton buylistbutton;
        RelativeLayout imgnamelayout;
        ImageButton addfavorbutton;

        public ImageNameViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeimage = itemView.findViewById(R.id.recipeimage);
            recipename = itemView.findViewById(R.id.recipename);
            metatitle = itemView.findViewById(R.id.metartitle);
            buylistbutton = itemView.findViewById(R.id.buylistbutton);
            imgnamelayout = itemView.findViewById(R.id.imgnamelayout);
            addfavorbutton = itemView.findViewById(R.id.favoritebutton);
        }
    }



}
