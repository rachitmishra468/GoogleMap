package com.media_mosaic.httpwww.doubloons.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.Data_Model.BuyPackages_model;
import com.media_mosaic.httpwww.doubloons.Fragments.Packages_Detail_fragment;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;

import java.util.List;


public class BuyPackages_adapter  extends RecyclerView.Adapter<BuyPackages_adapter.ProductViewHolder> {

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    private Context mCtx;
    private Fragment fragment;
    private List<BuyPackages_model> buyPackages_models;
    public BuyPackages_adapter(Context mCtx, List<BuyPackages_model> productList,Fragment fragment) {
        this.mCtx = mCtx;
        this.buyPackages_models = productList;
        this.fragment=fragment;
    }

    @Override
    public BuyPackages_adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.buypackages_items, null);
        return new BuyPackages_adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BuyPackages_adapter.ProductViewHolder holder, int position) {
        imageLoader = MyApplication.getInstance().getImageLoader();
        final BuyPackages_model product = buyPackages_models.get(position);
        holder.package_name.setText(product.getPackage_name()+" Package ");
        holder.package_detail.setText(product.getDetail());
        holder.no_post.setText("Number of post: "+product.getAllowed_doubloons());
        holder.cost.setText("$ "+product.getCost());
        holder.discount.setText(product.getDiscount()+"% Off");
        holder.buy_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity myActivity = (MapsActivity)mCtx;
                Bundle bundle=new Bundle();
                bundle.putString("package_name",product.getPackage_name());
                bundle.putString("cost",product.getCost());
                bundle.putString("allowed_doubloons",product.getAllowed_doubloons());
                Packages_Detail_fragment packages_detail_fragment = new Packages_Detail_fragment();
                packages_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, packages_detail_fragment, packages_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        int i=buyPackages_models.size();
        return buyPackages_models.size();
    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        Button buy_package;
        TextView package_name,package_detail,no_post,cost,discount;
        public ProductViewHolder(View itemView) {
            super(itemView);
            package_name=itemView.findViewById(R.id.package_name);
            package_detail=itemView.findViewById(R.id.package_detail);
            no_post=itemView.findViewById(R.id.no_post);
            cost=itemView.findViewById(R.id.cost);
            discount=itemView.findViewById(R.id.discount);
            buy_package=itemView.findViewById(R.id.buy_package);
        }
    }
}