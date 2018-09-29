package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import com.media_mosaic.httpwww.doubloons.Adapters.Notification_adapter;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.BuyPackages_model;
import com.media_mosaic.httpwww.doubloons.Data_Model.Notification_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.Uitl.UrlConstant;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification_fragment extends Fragment {
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    List<Notification_model> itemList;
    private RecyclerView my_notification_recycler_view;
    private ProgressDialog pDialog;
    Notification_adapter notification_adapter;
    ShimmerFrameLayout container_view;
    CardView card_view;
    LinearLayout inflateview;
    ReadPref readPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View   view=inflater.inflate(R.layout.fragment_notification_fragment, container, false);
        my_notification_recycler_view=(RecyclerView)view.findViewById(R.id.my_notification_recycler_view);
        container_view = (ShimmerFrameLayout)view.findViewById(R.id.shimmer_view_container1);
        card_view=(CardView)view.findViewById(R.id.card_view);
        inflateview=(LinearLayout)view.findViewById(R.id.inflateview);
        container_view.startShimmerAnimation();
        GetResponse();
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id","42");
        params.put("type","partner");
        groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/getNotifications",params,"item");
        return view;

    }
    private void GetResponse()
    {

        groceryAppResponse= new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {


                Log.d("TAG@123","Item Response:"+response);
               // pDialog.dismiss();
                inflateview.setVisibility(View.VISIBLE);
                card_view.setVisibility(View.GONE);
                itemList= new ArrayList<>();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("Doubloon_app");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        Notification_model notification_model=new Notification_model();
                        JSONObject jsonObjectdata= jsonArray.getJSONObject(i);
                        notification_model.setId(jsonObjectdata.getString("id"));
                        notification_model.setCreated(jsonObjectdata.getString("created"));
                        notification_model.setDescription(jsonObjectdata.getString("description"));
                        notification_model.setStatus(jsonObjectdata.getString("status"));
                        notification_model.setTitle(jsonObjectdata.getString("title"));

                        itemList.add(notification_model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TAG@123","Item list size:"+itemList.size());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                my_notification_recycler_view.setLayoutManager(mLayoutManager);
                my_notification_recycler_view.setItemAnimator(new DefaultItemAnimator());
                notification_adapter= new Notification_adapter(getActivity(),itemList);
                my_notification_recycler_view.setAdapter(notification_adapter);

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
