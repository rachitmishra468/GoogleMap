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
import com.media_mosaic.httpwww.doubloons.Adapters.Progressive_adapter;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.progressive_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Progressive_doubloons extends Fragment {
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    List<progressive_model> itemList;
    private RecyclerView progressive__recycler_view;
    private ProgressDialog pDialog;
    Progressive_adapter progressive_adapter;
    SwipeRefreshLayout progressive_swifeRefresh;
    ReadPref readPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_multi_daily_doubloons, container, false);
        progressive__recycler_view=(RecyclerView)view.findViewById(R.id.progressive__recycler_view);
        progressive_swifeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.progressive_swifeRefresh);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        GetResponse();
        HashMap<String,String> params = new HashMap<>();
        params.put("lat", String.valueOf(MapsActivity.latitude));
        params.put("lng",String.valueOf(MapsActivity.longitude));
        params.put("category","progressive");
        params.put("user_id",readPref.getuserId());
        Log.d("TAG@123",params.toString());
        groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/categoryDoubloons",params,"item");
        progressive_swifeRefresh.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.white);
        progressive_swifeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                GetResponse();
                HashMap<String,String> params = new HashMap<>();
                params.put("lat", String.valueOf(MapsActivity.latitude));
                params.put("lng",String.valueOf(MapsActivity.longitude));
                params.put("category","progressive");
                params.put("user_id",readPref.getuserId());
                Log.d("TAG@123",params.toString());
                groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
                groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/categoryDoubloons",params,"item");
                progressive_swifeRefresh.setRefreshing(false);
            }
        });
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void GetResponse()
    {

        groceryAppResponse= new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {
                Log.d("TAG@123",response);
                String dhjdj=response.toString();
                pDialog.dismiss();
                itemList= new ArrayList<>();
                try {
                    JSONObject jsonObjectdata=new JSONObject(response);
                    JSONArray jsonArray= jsonObjectdata.getJSONArray("Doubloon_app");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        progressive_model progressive_model=new progressive_model();
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        progressive_model.setStarted(jsonObject.getString("started"));
                        progressive_model.setFinished(jsonObject.getString("finished"));
                        progressive_model.setId(jsonObject.getString("id"));
                        progressive_model.setName(jsonObject.getString("name"));
                        progressive_model.setAddress(jsonObject.getString("Address"));
                        progressive_model.setDiscount(jsonObject.getString("discount"));
                        progressive_model.setGroup_type(jsonObject.getString("group_type"));
                        progressive_model.setLat(jsonObject.getString("lat"));
                        progressive_model.setLongi(jsonObject.getString("long"));
                        progressive_model.setPost_type(jsonObject.getString("post_type"));
                        progressive_model.setRunningCount(jsonObject.getString("runningCount"));
                        progressive_model.setSuccessCount(jsonObject.getString("successCount"));
                        progressive_model.setTime_limit(jsonObject.getString("time_limit"));
                        itemList.add(progressive_model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TAG@123","Item list size:"+itemList.size());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                progressive__recycler_view.setLayoutManager(mLayoutManager);
                progressive__recycler_view.setItemAnimator(new DefaultItemAnimator());
                progressive_adapter= new Progressive_adapter(getActivity(),itemList);
                progressive__recycler_view.setAdapter(progressive_adapter);
            }
            @Override
            public void onError(String error, String responseType) {


            }
        };



    }


}
