package com.media_mosaic.httpwww.doubloons.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.MyAvailableDoubloonsModel;
import com.media_mosaic.httpwww.doubloons.Data_Model.sponsored_model;
import com.media_mosaic.httpwww.doubloons.Fragments.Doubloon_detail_fragment;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by akash on 2/27/2018.
 */

public class MyAvailableDoubloonsAdapter extends RecyclerView.Adapter<MyAvailableDoubloonsAdapter.ProductViewHolder> {

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    private Context mCtx;
    ReadPref readPref;
    String DateToStr;
    private List<MyAvailableDoubloonsModel> myDoubloons_models;

    public MyAvailableDoubloonsAdapter(Context mCtx, List<MyAvailableDoubloonsModel> productList) {
        this.mCtx = mCtx;
        this.myDoubloons_models = productList;
    }

    @Override
    public MyAvailableDoubloonsAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        readPref = new ReadPref(mCtx);
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateToStr = format.format(curDate);
        View view = inflater.inflate(R.layout.myavailabledoubloons, null);
        return new MyAvailableDoubloonsAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAvailableDoubloonsAdapter.ProductViewHolder holder,final int position) {
        imageLoader = MyApplication.getInstance().getImageLoader();
        final MyAvailableDoubloonsModel product = myDoubloons_models.get(position);
        holder.dounloons_text.setText(product.getName());
        holder.doubloons_check.setChecked(product.isSelected());

        holder.doubloons_check.setTag(myDoubloons_models.get(position));
        holder.doubloons_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                MyAvailableDoubloonsModel contact = (MyAvailableDoubloonsModel) cb.getTag();

                contact.setSelected(cb.isChecked());
                myDoubloons_models.get(position).setSelected(cb.isChecked());

            }
        });
    }

    @Override
    public int getItemCount() {

        return myDoubloons_models.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView dounloons_text;
        CheckBox doubloons_check;

        public ProductViewHolder(View itemView) {
            super(itemView);
            dounloons_text = itemView.findViewById(R.id.dounloons_text);
            doubloons_check=itemView.findViewById(R.id.doubloons_check);
        }
    }
    // method to access in activity after updating selection
    public List<MyAvailableDoubloonsModel> getStudentist() {
        return myDoubloons_models;
    }
}