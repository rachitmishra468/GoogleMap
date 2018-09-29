package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.Adapters.Sponsored_adapter;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.sponsored_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sponsored_doubloon extends Fragment {
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    List<sponsored_model> itemList;
    private RecyclerView sponser__recycler_view;
    private ProgressDialog pDialog;
    Sponsored_adapter sponsored_adapter;
    SwipeRefreshLayout sponser_swifeRefresh;
    ReadPref readPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_sponsored_doubloon, container, false);
        sponser__recycler_view = (RecyclerView) view.findViewById(R.id.sponser__recycler_view);
        sponser_swifeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sponser_swifeRefresh);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        GetResponse();
        HashMap<String,String> params = new HashMap<>();
        params.put("lat", String.valueOf(MapsActivity.latitude));
        params.put("lng",String.valueOf(MapsActivity.longitude));
        params.put("category","sponsored");
        params.put("user_id",readPref.getuserId());
        Log.d("TAG@123",params.toString());
        groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/categoryDoubloons", params, "item");
        sponser_swifeRefresh.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.white);
        sponser_swifeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                GetResponse();
                HashMap<String, String> params = new HashMap<>();
                params.put("lat", String.valueOf(MapsActivity.latitude));
                params.put("lng",String.valueOf(MapsActivity.longitude));
                params.put("category","sponsored");
                params.put("user_id",readPref.getuserId());
                Log.d("TAG@123",params.toString());
                groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
                groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/categoryDoubloons", params, "item");
                sponser_swifeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    private void GetResponse() {

        groceryAppResponse = new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {
                Log.d("TAG@123", response);
                String dhjdj = response.toString();
                pDialog.dismiss();
                itemList = new ArrayList<>();
                try {
                    JSONObject jsonObjectdata = new JSONObject(response);
                    JSONArray jsonArray = jsonObjectdata.getJSONArray("Doubloon_app");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        sponsored_model sponsored_model = new sponsored_model();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        sponsored_model.setId(jsonObject.getString("id"));

                        sponsored_model.setName(jsonObject.getString("name"));
                        sponsored_model.setAddress(jsonObject.getString("Address"));
                        sponsored_model.setDiscount(jsonObject.getString("discount"));
                        sponsored_model.setGroup_type(jsonObject.getString("group_type"));
                        sponsored_model.setLat(jsonObject.getString("lat"));
                        sponsored_model.setLongi(jsonObject.getString("long"));
                        sponsored_model.setPost_type(jsonObject.getString("post_type"));
                        sponsored_model.setRunningCount(jsonObject.getString("runningCount"));
                        sponsored_model.setSuccessCount(jsonObject.getString("successCount"));
                        sponsored_model.setTime_limit(jsonObject.getString("time_limit"));
                        if (!jsonObject.isNull("sponsor_name")) {
                            sponsored_model.setSponsor_name(jsonObject.getString("sponsor_name"));
                        }
                        else {
                            sponsored_model.setSponsor_name("");
                        }
                        sponsored_model.setStarted(jsonObject.getString("started"));
                        sponsored_model.setFinished(jsonObject.getString("finished"));
                        itemList.add(sponsored_model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TAG@123", "Item list size:" + itemList.size());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                sponser__recycler_view.setLayoutManager(mLayoutManager);
                sponser__recycler_view.setItemAnimator(new DefaultItemAnimator());
                sponsored_adapter = new Sponsored_adapter(getActivity(), itemList);
                sponser__recycler_view.setAdapter(sponsored_adapter);

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
