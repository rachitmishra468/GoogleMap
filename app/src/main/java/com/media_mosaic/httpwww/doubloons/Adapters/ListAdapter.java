package com.media_mosaic.httpwww.doubloons.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.List_model;
import com.media_mosaic.httpwww.doubloons.Fragments.Doubloon_detail_fragment;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Rachit on 2/22/2018.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ProductViewHolder> {
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    private Context mCtx;
    private List<List_model> featured_models;
    ReadPref readPref;
    String DateToStr;
    public ListAdapter(Context mCtx, List<List_model> productList) {
        this.mCtx = mCtx;
        this.featured_models = productList;
    }

    @Override
    public ListAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        readPref = new ReadPref(mCtx);
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateToStr = format.format(curDate);
        View view = inflater.inflate(R.layout.listadapter_item, null);
        return new ListAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ProductViewHolder holder, int position) {
        imageLoader = MyApplication.getInstance().getImageLoader();
        final List_model product = featured_models.get(position);
        holder.doubloons_name.setText(product.getName());
        holder.address_text.setText(product.getAddress());
        holder.date_time.setText(product.getTime_limit());
        holder.runing_text.setText(product.getRunningCount());
        holder.success_text.setText(product.getSuccessCount());
        holder.price_doubloon.setText(product.getDiscount());
        if(product.getRunningCount().equals("0")){
            holder.runing_text_hading.setTextColor(mCtx.getResources().getColor(R.color.text_code));
            holder.runing_text.setTextColor(mCtx.getResources().getColor(R.color.text_code));
        }
        else {
            holder.runing_text_hading.setTextColor(mCtx.getResources().getColor(R.color.running));
            holder.runing_text.setTextColor(mCtx.getResources().getColor(R.color.running));
        }
        if(product.getGroup_type().equals("Close Group")){
            holder.main_logo_img.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.featured_icon));
        }
        else if(product.getGroup_type().equals("Sponsored")){
            holder.main_logo_img.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.sponsored_marker_icon));
        }
        else {
            holder.main_logo_img.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.green_nav_lag));

        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(DateToStr);
            d2 = format.parse(product.getTime_limit());
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffDays>0.0){
                holder.inactive_text.setVisibility(View.GONE);
                holder.day_left_text.setText("Days Left");
                holder.active_text.setVisibility(View.VISIBLE);
                holder.dayleft_text.setText(diffDays+" days");
                holder.day_left_text.setTextColor(mCtx.getResources().getColor(R.color.black));
            }
            else {
                holder.inactive_text.setVisibility(View.VISIBLE);
                holder.day_left_text.setText("Expired");
                holder.active_text.setVisibility(View.GONE);
                holder.dayleft_text.setText("");
                //holder.main_logo_img.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.red_nav_larg));
                holder.day_left_text.setTextColor(mCtx.getResources().getColor(R.color.red));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.main_logo_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity myActivity = (MapsActivity)mCtx;
                Bundle bundle = new Bundle();
                Doubloon_detail_fragment doubloon_detail_fragment = new Doubloon_detail_fragment();
                bundle.putString("post_id",  product.getId());
                bundle.putString("flag","false");
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        holder.doubloons_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity myActivity = (MapsActivity)mCtx;
                Bundle bundle = new Bundle();
                Doubloon_detail_fragment doubloon_detail_fragment = new Doubloon_detail_fragment();
                bundle.putString("post_id",  product.getId());
                bundle.putString("flag","false");
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        holder.info_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity myActivity = (MapsActivity)mCtx;
                Bundle bundle = new Bundle();
                Doubloon_detail_fragment doubloon_detail_fragment = new Doubloon_detail_fragment();
                bundle.putString("post_id",  product.getId());
                bundle.putString("flag","false");
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });



    }
    @Override
    public int getItemCount() {
        return featured_models.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout price_layout,address_layout;
        ImageView main_logo_img,info_img;
        TextView runing_text_hading,price_doubloon,doubloons_name,address_text,date_time,runing_text,success_text,dayleft_text,inactive_text,active_text,day_left_text;
        public ProductViewHolder(View itemView) {
            super(itemView);
            info_img= itemView.findViewById(R.id.info_img);
            runing_text_hading= itemView.findViewById(R.id.runing_text_hading);
            doubloons_name=itemView.findViewById(R.id.doubloons_name);
            address_text=itemView.findViewById(R.id.address_text);
            date_time=itemView.findViewById(R.id.date_time);
            runing_text=itemView.findViewById(R.id.runing_text);
            success_text=itemView.findViewById(R.id.success_text);
            dayleft_text=itemView.findViewById(R.id.dayleft_text);
            price_layout=itemView.findViewById(R.id.price_layout);
            address_layout=itemView.findViewById(R.id.address_layout);
            inactive_text=itemView.findViewById(R.id.inactive_text);
            active_text=itemView.findViewById(R.id.active_text);
            day_left_text=itemView.findViewById(R.id.day_left_text);
            price_doubloon=itemView.findViewById(R.id.price_doubloon);
            main_logo_img=itemView.findViewById(R.id.main_logo_img);
            if(readPref.getusertype().equals("player")){
                address_layout.setVisibility(View.GONE);
                price_layout.setVisibility(View.VISIBLE);
            }else {
                address_layout.setVisibility(View.VISIBLE);
                price_layout.setVisibility(View.GONE);
            }

        }
    }
}