package com.media_mosaic.httpwww.doubloons.Fragments;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.media_mosaic.httpwww.doubloons.Adapters.MyDoubloon_Adapter;
import com.media_mosaic.httpwww.doubloons.Adapters.multi_sub_adapter;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.MyDoubloons_model;
import com.media_mosaic.httpwww.doubloons.Data_Model.multi_sub_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.media_mosaic.httpwww.doubloons.Fragments.Mydoubloons.textAsBitmap;

public class Create_Multi_doubllon_Sub extends Fragment {
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    List<multi_sub_model> itemList;
    private RecyclerView myDoubloonRecyclerview;
    private ProgressDialog pDialog;
    multi_sub_adapter multi_sub_adapter;
    FloatingActionButton add_new_Doubloon;
    SwipeRefreshLayout swifeRefresh;
    ReadPref readPref;
    View   view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_create__multi_doubllon__sub, container, false);


        myDoubloonRecyclerview=(RecyclerView)view.findViewById(R.id.my_doublloon_recycler_view);
        swifeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.swifeRefresh);
        String data=getArguments().getString("post_id");
        swifeRefresh.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.white);
        swifeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    GetResponse();
                    HashMap<String,String> params = new HashMap<>();
                    params.put("post_id", getArguments().getString("post_id"));
                    Log.d("TAG@123",params.toString());
                    groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
                    groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/getChildPosts",params,"item");

                swifeRefresh.setRefreshing(false);
            }
        });


            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            GetResponse();
            HashMap<String,String> params = new HashMap<>();
            params.put("post_id", getArguments().getString("post_id"));
            Log.d("TAG@123",params.toString());
            groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/getChildPosts",params,"item");

return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event

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
                String msg="";
                pDialog.dismiss();
                itemList= new ArrayList<>();
                try {
                    JSONObject jsonObjectdata=new JSONObject(response);

                    msg=jsonObjectdata.getString("res_msg");
                    settoast(jsonObjectdata.getString("res_msg"));
                    JSONArray jsonArray= jsonObjectdata.getJSONArray("Doubloon_app");
                    if(readPref.getusertype().equals("player")){
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            multi_sub_model myDoubloons_model=new multi_sub_model();
                            JSONObject jsonObject= jsonArray.getJSONObject(i);
                            myDoubloons_model.setId(jsonObject.getString("id"));
                            myDoubloons_model.setName(jsonObject.getString("doubloon_name"));
                            myDoubloons_model.setPrev_finished(jsonObject.getString("prev_finished"));
                            myDoubloons_model.setPrev_started(jsonObject.getString("prev_started"));
                            myDoubloons_model.setAddress("");
                            myDoubloons_model.setClue("");
                            myDoubloons_model.setDiscount(jsonObject.getString("discount"));
                            myDoubloons_model.setGroup_type("");
                            myDoubloons_model.setLat("");
                            myDoubloons_model.setLongi("");
                            myDoubloons_model.setPost_type("");
                            myDoubloons_model.setRunningCount(jsonObject.getString("runningCount"));
                            myDoubloons_model.setSponsor_name("");
                            myDoubloons_model.setStatus("");
                            myDoubloons_model.setSuccessCount(jsonObject.getString("successCount"));
                            myDoubloons_model.setTime_limit(jsonObject.getString("expiry_date"));
                         //   myDoubloons_model.setDoubloon_status(jsonObject.getString("doubloon_status"));
                            itemList.add(myDoubloons_model);
                        }
                    }
                    else {
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            multi_sub_model myDoubloons_model=new multi_sub_model();
                            JSONObject jsonObject= jsonArray.getJSONObject(i);
                            myDoubloons_model.setId(jsonObject.getString("id"));
                            myDoubloons_model.setName(jsonObject.getString("name"));
                            myDoubloons_model.setAddress(jsonObject.getString("Address"));
                            myDoubloons_model.setClue("");
                            myDoubloons_model.setDiscount(jsonObject.getString("discount"));
                            myDoubloons_model.setGroup_type(jsonObject.getString("group_type"));
                            myDoubloons_model.setLat(jsonObject.getString("lat"));
                            myDoubloons_model.setLongi(jsonObject.getString("long"));
                            myDoubloons_model.setPost_type(jsonObject.getString("post_type"));
                            myDoubloons_model.setRunningCount(jsonObject.getString("runningCount"));
                            myDoubloons_model.setSponsor_name("");
                            myDoubloons_model.setStatus(jsonObject.getString("status"));
                            myDoubloons_model.setSuccessCount(jsonObject.getString("successCount"));
                            myDoubloons_model.setTime_limit(jsonObject.getString("time_limit"));
                            itemList.add(myDoubloons_model);
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                myDoubloonRecyclerview.setLayoutManager(mLayoutManager);
                myDoubloonRecyclerview.setItemAnimator(new DefaultItemAnimator());
                multi_sub_adapter= new multi_sub_adapter(getActivity(),itemList);
                myDoubloonRecyclerview.setAdapter(multi_sub_adapter);

                if(itemList.size()==0){
                    Doubloon_detail_fragment fragment = new Doubloon_detail_fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("post_id",  getArguments().getString("post_id"));
                    bundle.putString("flag","true");
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).commit();
                }

            }

            @Override
            public void onError(String error, String responseType) {


            }
        };



    }
    public  void popup(String data){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.app_name);
        builder.setCancelable(false);
        builder.setMessage(data);
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("Ok", null);
        builder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
    }



    public void settoast(String title) {
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text_toast);
        text.setText(title);
        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }

}
