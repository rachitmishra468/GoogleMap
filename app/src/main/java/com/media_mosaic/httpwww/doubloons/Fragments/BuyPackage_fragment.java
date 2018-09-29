package com.media_mosaic.httpwww.doubloons.Fragments;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.facebook.shimmer.ShimmerFrameLayout;

import com.media_mosaic.httpwww.doubloons.Adapters.BuyPackages_adapter;
import com.media_mosaic.httpwww.doubloons.Adapters.MyDoubloon_Adapter;
import com.media_mosaic.httpwww.doubloons.Data_Model.BuyPackages_model;
import com.media_mosaic.httpwww.doubloons.Data_Model.MyDoubloons_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.Uitl.UrlConstant;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuyPackage_fragment extends Fragment {
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    List<BuyPackages_model> itemList;
    private RecyclerView buypackages_recyclerview;

    BuyPackages_adapter buyPackages_adapter;
    ShimmerFrameLayout container_view;
    CardView card_view;
    LinearLayout inflateview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View   view=inflater.inflate(R.layout.fragment_buy_package_fragment, container, false);
        buypackages_recyclerview=(RecyclerView)view.findViewById(R.id.my_doublloon_recycler_view);
        container_view = (ShimmerFrameLayout)view.findViewById(R.id.shimmer_view_container1);
        card_view=(CardView)view.findViewById(R.id.card_view);
        inflateview=(LinearLayout)view.findViewById(R.id.inflateview);

        container_view.startShimmerAnimation();
       /* pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();*/
        GetResponse();
        groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.GET, "http://www.doubloon.media-mosaic.in/apis/getPackages",null,"item");
        return view;

    }

    private void GetResponse()
    {

        groceryAppResponse= new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {

               // container.stopShimmerAnimation();
                inflateview.setVisibility(View.VISIBLE);
                card_view.setVisibility(View.GONE);
                Log.d("TAG@123","Item Response:"+response);
              //  pDialog.dismiss();
                itemList= new ArrayList<>();

                try {

                    JSONObject jsonObjectdata=new JSONObject(response);
                    JSONArray jsonArray= jsonObjectdata.getJSONArray("Doubloon_app");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        BuyPackages_model BuyPackages_model=new BuyPackages_model();
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        BuyPackages_model.setId(jsonObject.getString("id"));
                        BuyPackages_model.setAllowed_doubloons(jsonObject.getString("allowed_doubloons"));
                        BuyPackages_model.setCost(jsonObject.getString("cost"));
                        BuyPackages_model.setDetail(jsonObject.getString("detail"));
                        BuyPackages_model.setDiscount(jsonObject.getString("discount"));
                        BuyPackages_model.setMrp(jsonObject.getString("mrp"));
                        BuyPackages_model.setPackage_name(jsonObject.getString("package_name"));
                        BuyPackages_model.setType(jsonObject.getString("type"));
                        itemList.add(BuyPackages_model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TAG@123","Item list size:"+itemList.size());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                buypackages_recyclerview.setLayoutManager(mLayoutManager);
                buypackages_recyclerview.setItemAnimator(new DefaultItemAnimator());
                buyPackages_adapter= new BuyPackages_adapter(getActivity(),itemList,BuyPackage_fragment.this);
                buypackages_recyclerview.setAdapter(buyPackages_adapter);

            }

            @Override
            public void onError(String error, String responseType) {


            }
        };



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
