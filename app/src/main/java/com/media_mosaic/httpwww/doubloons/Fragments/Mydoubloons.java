package com.media_mosaic.httpwww.doubloons.Fragments;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.Adapters.MyDoubloon_Adapter;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.MyDoubloons_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class Mydoubloons extends Fragment {
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    List<MyDoubloons_model> itemList;
    private RecyclerView myDoubloonRecyclerview;
    private ProgressDialog pDialog;
    MyDoubloon_Adapter myDoubloon_adapter;
    FloatingActionButton add_new_Doubloon;
    SwipeRefreshLayout swifeRefresh;
    ReadPref readPref;
    LinearLayout add_new_doubloon_layour;
    View   view;
    ImageView add_doubloon_img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           view=inflater.inflate(R.layout.fragment_mydoubloons, container, false);
        myDoubloonRecyclerview=(RecyclerView)view.findViewById(R.id.my_doublloon_recycler_view);
        add_new_Doubloon=(FloatingActionButton)view.findViewById(R.id.add_new_Doubloon);
        swifeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.swifeRefresh);
        add_new_doubloon_layour=(LinearLayout)view.findViewById(R.id.add_new_doubloon_layour);
        add_doubloon_img=(ImageView)view.findViewById(R.id.add_doubloon_img);
        add_new_Doubloon.setImageBitmap(textAsBitmap("ADD", 40, Color.BLACK));
        add_new_Doubloon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_new_Doubloons fragment = new Create_new_Doubloons();
                 getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }
        });

        swifeRefresh.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.white);
        swifeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(readPref.getusertype().equals("player")){
                            add_new_Doubloon.setVisibility(View.GONE);
                            pDialog = new ProgressDialog(getActivity());
                            pDialog.setMessage("Loading...");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            GetResponse();
                            HashMap<String,String> params = new HashMap<>();
                            params.put("user_id",readPref.getuserId());
                            Log.d("TAG@123",params.toString());
                            //params.put("user_id",readPref.getuserId());
                            groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
                            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/userPlayedDoubloons",params,"item");
                        }else {
                            add_new_Doubloon.setVisibility(View.VISIBLE);
                            pDialog = new ProgressDialog(getActivity());
                            pDialog.setMessage("Loading...");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            GetResponse();
                            HashMap<String,String> params = new HashMap<>();
                            params.put("partner_id",readPref.getuserId());
                            Log.d("TAG@123",params.toString());
                            groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
                            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/myPostList",params,"item");
                        }
                swifeRefresh.setRefreshing(false);
            }
        });
        if(readPref.getusertype().equals("player")){
            add_new_Doubloon.setVisibility(View.GONE);
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            GetResponse();
            HashMap<String,String> params = new HashMap<>();
            params.put("user_id",readPref.getuserId());
            Log.d("TAG@123",params.toString());
            groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/userPlayedDoubloons",params,"item");
        }else {
            add_new_Doubloon.setVisibility(View.VISIBLE);
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            GetResponse();
            HashMap<String,String> params = new HashMap<>();
            params.put("partner_id",readPref.getuserId());
            Log.d("TAG@123",params.toString());
            groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/myPostList",params,"item");
        }


        add_doubloon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(readPref.getusertype().equals("player")){

                    Intent intent=new Intent(getActivity(), MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);

                }
                else {
                    Create_new_Doubloons fragment = new Create_new_Doubloons();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                }
            }
        });












        return view;
    }


    public  void changedata() {


        {
            if(readPref.getusertype().equals("player")){
                add_new_Doubloon.setVisibility(View.GONE);
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                GetResponse();
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",readPref.getuserId());
                Log.d("TAG@123",params.toString());
                //params.put("user_id",readPref.getuserId());
                groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
                groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/userPlayedDoubloons",params,"item");
            }else {
                add_new_Doubloon.setVisibility(View.VISIBLE);
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                GetResponse();
                HashMap<String,String> params = new HashMap<>();
                params.put("partner_id",readPref.getuserId());
                Log.d("TAG@123",params.toString());
                groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
                groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/myPostList",params,"item");
            }
            swifeRefresh.setRefreshing(false);
        }
    }




    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
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
                    settoast(jsonObjectdata.getString("res_msg"));
                    JSONArray jsonArray= jsonObjectdata.getJSONArray("Doubloon_app");
                    if(readPref.getusertype().equals("player")){
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            MyDoubloons_model myDoubloons_model=new MyDoubloons_model();
                            JSONObject jsonObject= jsonArray.getJSONObject(i);
                            myDoubloons_model.setId(jsonObject.getString("post_id"));
                            myDoubloons_model.setPost_id(jsonObject.getString("post_id"));
                            myDoubloons_model.setName(jsonObject.getString("doubloon_name"));
                            myDoubloons_model.setAddress("");
                            myDoubloons_model.setClue("");
                            myDoubloons_model.setDoubloon_status(jsonObject.getString("doubloon_status"));
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
                            itemList.add(myDoubloons_model);
                        }
                    }
                    else {
                        for(int i=0;i<jsonArray.length();i++)
                    {
                        MyDoubloons_model myDoubloons_model=new MyDoubloons_model();
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        myDoubloons_model.setId(jsonObject.getString("id"));
                        myDoubloons_model.setPost_id("");
                        myDoubloons_model.setName(jsonObject.getString("name"));
                        myDoubloons_model.setAddress(jsonObject.getString("Address"));
                        myDoubloons_model.setClue(jsonObject.getString("clue"));
                        myDoubloons_model.setDiscount(jsonObject.getString("discount"));
                        myDoubloons_model.setGroup_type(jsonObject.getString("group_type"));
                        myDoubloons_model.setLat(jsonObject.getString("lat"));
                        myDoubloons_model.setLongi(jsonObject.getString("long"));
                        myDoubloons_model.setPost_type(jsonObject.getString("post_type"));
                        myDoubloons_model.setRunningCount(jsonObject.getString("runningCount"));
                        myDoubloons_model.setSponsor_name(jsonObject.getString("sponsor_name"));
                        myDoubloons_model.setStatus(jsonObject.getString("status"));
                        myDoubloons_model.setSuccessCount(jsonObject.getString("successCount"));
                        myDoubloons_model.setTime_limit(jsonObject.getString("time_limit"));
                        myDoubloons_model.setDoubloon_status("");
                        itemList.add(myDoubloons_model);
                    }}

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TAG@123","Item list size:"+itemList.size());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                myDoubloonRecyclerview.setLayoutManager(mLayoutManager);
                myDoubloonRecyclerview.setItemAnimator(new DefaultItemAnimator());
                myDoubloon_adapter= new MyDoubloon_Adapter(getActivity(),itemList);
                myDoubloonRecyclerview.setAdapter(myDoubloon_adapter);

                if(itemList.size()==0){
                    swifeRefresh.setVisibility(View.GONE);
                    add_new_doubloon_layour.setVisibility(View.VISIBLE);
                    if(readPref.getusertype().equals("player")){
                        add_doubloon_img.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.checked_doubloon));

                    }
                }



            }

            @Override
            public void onError(String error, String responseType) {


            }
        };



    }
    public  void popup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.app_name);
        builder.setCancelable(false);
        builder.setMessage("");
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("Yes", null);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
        builder.setNegativeButton("No",null);
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
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
