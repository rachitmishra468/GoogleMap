package com.media_mosaic.httpwww.doubloons.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.media_mosaic.httpwww.doubloons.Activitys.Host_login;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.Invite_Model;
import com.media_mosaic.httpwww.doubloons.Data_Model.MyAvailableDoubloonsModel;
import com.media_mosaic.httpwww.doubloons.Data_Model.MyDoubloons_model;
import com.media_mosaic.httpwww.doubloons.Data_Model.sponsored_model;
import com.media_mosaic.httpwww.doubloons.Fragments.Doubloon_detail_fragment;
import com.media_mosaic.httpwww.doubloons.Fragments.MyAvailableDoubloonsFragment;
import com.media_mosaic.httpwww.doubloons.Fragments.Packages_Detail_fragment;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.Uitl.UrlConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by rachit on 2/13/2018.
 */

public class Sponsored_adapter extends RecyclerView.Adapter<Sponsored_adapter.ProductViewHolder> {

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    private Context mCtx;
    ReadPref readPref;
    String DateToStr;
    ProgressDialog pd;
    String main_id="";
    Dialog mBottomSheetDialog;
    List<MyAvailableDoubloonsModel> itemList;
    private List<sponsored_model> myDoubloons_models;

    public Sponsored_adapter(Context mCtx, List<sponsored_model> productList) {
        this.mCtx = mCtx;
        this.myDoubloons_models = productList;
    }

    @Override
    public Sponsored_adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        readPref = new ReadPref(mCtx);
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateToStr = format.format(curDate);
        View view = inflater.inflate(R.layout.sponsored_item, null);
        return new Sponsored_adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Sponsored_adapter.ProductViewHolder holder, int position) {
        imageLoader = MyApplication.getInstance().getImageLoader();
        final sponsored_model product = myDoubloons_models.get(position);
        holder.doubloons_name.setText(product.getName());
        holder.sponsored_by_doubloon.setText(product.getSponsor_name());
        holder.date_time.setText(product.getTime_limit());
        holder.runing_text.setText(product.getRunningCount());
        holder.success_text.setText(product.getSuccessCount());
        holder.price_doubloon.setText(product.getDiscount());
        if (product.getRunningCount().equals("0")) {
            holder.runing_text_hading.setTextColor(mCtx.getResources().getColor(R.color.text_code));
            holder.runing_text.setTextColor(mCtx.getResources().getColor(R.color.text_code));
        } else {
            holder.runing_text_hading.setTextColor(mCtx.getResources().getColor(R.color.running));
            holder.runing_text.setTextColor(mCtx.getResources().getColor(R.color.running));
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(DateToStr);
            d2 = format.parse(product.getTime_limit());
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffDays > 0.0) {
                holder.inactive_text.setVisibility(View.GONE);
                holder.day_left_text.setText("Days Left");
                holder.active_text.setVisibility(View.VISIBLE);
                holder.dayleft_text.setText(diffDays + " days");
                holder.day_left_text.setTextColor(mCtx.getResources().getColor(R.color.black));
            } else {
                holder.inactive_text.setVisibility(View.VISIBLE);
                holder.day_left_text.setText("Expired");
                holder.active_text.setVisibility(View.GONE);
                holder.dayleft_text.setText("");
                holder.day_left_text.setTextColor(mCtx.getResources().getColor(R.color.red));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_id=new String("");
                main_id=product.getId();
                AddEvent();

            }
        });
        holder.main_logo_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlConstant.started = new String("");
                UrlConstant.finished = new String("");
                UrlConstant.started = product.getStarted();
                UrlConstant.finished = product.getFinished();
                MapsActivity myActivity = (MapsActivity) mCtx;
                Bundle bundle = new Bundle();
                Doubloon_detail_fragment doubloon_detail_fragment = new Doubloon_detail_fragment();
                bundle.putString("post_id", product.getId());
                bundle.putString("flag", "false");
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        holder.doubloons_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlConstant.started = new String("");
                UrlConstant.finished = new String("");
                UrlConstant.started = product.getStarted();
                UrlConstant.finished = product.getFinished();
                MapsActivity myActivity = (MapsActivity) mCtx;
                Bundle bundle = new Bundle();
                Doubloon_detail_fragment doubloon_detail_fragment = new Doubloon_detail_fragment();
                bundle.putString("post_id", product.getId());
                bundle.putString("flag", "false");
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        holder.info_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlConstant.started = new String("");
                UrlConstant.finished = new String("");
                UrlConstant.started = product.getStarted();
                UrlConstant.finished = product.getFinished();
                MapsActivity myActivity = (MapsActivity) mCtx;
                Bundle bundle = new Bundle();
                Doubloon_detail_fragment doubloon_detail_fragment = new Doubloon_detail_fragment();
                bundle.putString("post_id", product.getId());
                bundle.putString("flag", "false");
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        holder.add_detail_sceound.setImageUrl("http://doubloon.media-mosaic.in/images/advertise/img25a1006668a853.png", imageLoader);
        if (position % 2 == 0)
            holder.add_detail_sceound.setVisibility(View.VISIBLE);
        else
            holder.add_detail_sceound.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {

        return myDoubloons_models.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout price_layout, sponsored_by_layout;
        Button add_event;
        ImageView main_logo_img, info_img;
        NetworkImageView add_detail_sceound;
        TextView runing_text_hading, price_doubloon, doubloons_name, sponsored_by_doubloon, date_time, runing_text, success_text, dayleft_text, inactive_text, active_text, day_left_text;

        public ProductViewHolder(View itemView) {
            super(itemView);
            info_img = itemView.findViewById(R.id.info_img);
            runing_text_hading = itemView.findViewById(R.id.runing_text_hading);
            add_detail_sceound = itemView.findViewById(R.id.add_detail_sceound);
            doubloons_name = itemView.findViewById(R.id.doubloons_name);
            sponsored_by_doubloon = itemView.findViewById(R.id.sponsored_by_doubloon);
            date_time = itemView.findViewById(R.id.date_time);
            runing_text = itemView.findViewById(R.id.runing_text);
            success_text = itemView.findViewById(R.id.success_text);
            dayleft_text = itemView.findViewById(R.id.dayleft_text);
            price_layout = itemView.findViewById(R.id.price_layout);
            sponsored_by_layout = itemView.findViewById(R.id.sponsored_by_layout);
            inactive_text = itemView.findViewById(R.id.inactive_text);
            active_text = itemView.findViewById(R.id.active_text);
            day_left_text = itemView.findViewById(R.id.day_left_text);
            price_doubloon = itemView.findViewById(R.id.price_doubloon);
            add_event = itemView.findViewById(R.id.add_event);
            main_logo_img = itemView.findViewById(R.id.main_logo_img);
            if (readPref.getusertype().equals("player")) {
                add_event.setVisibility(View.GONE);
            } else {
                add_event.setVisibility(View.VISIBLE);
            }

        }
    }


    private void AddEvent() {
        pd = new ProgressDialog(mCtx);
        pd.setMessage("loading");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/getMyAvailableDoubloons",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.d("TAG@123", response);
                        System.out.println(response);
                        if (response != null) {
                            itemList = new ArrayList<>();
                            try {
                                JSONObject jsonObjectdata = new JSONObject(response);
                                Toast.makeText(mCtx, jsonObjectdata.getString("res_msg"), Toast.LENGTH_LONG).show();
                                JSONArray jsonArray = jsonObjectdata.getJSONArray("Doubloon_app");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MyAvailableDoubloonsModel myAvailableDoubloonsModel = new MyAvailableDoubloonsModel();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    myAvailableDoubloonsModel.setId(jsonObject.getString("id"));
                                    myAvailableDoubloonsModel.setName(jsonObject.getString("name"));
                                    myAvailableDoubloonsModel.setStatus(jsonObject.getString("status"));
                                    myAvailableDoubloonsModel.setSelected(false);
                                    itemList.add(myAvailableDoubloonsModel);
                                }


                                ShowmyDoubloon();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        String message = "Anything happened Wrong Please try Again.";
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(mCtx, message, Toast.LENGTH_LONG).show();
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("partner_id", readPref.getuserId());
                System.out.println("params - " + params.toString());
                Log.d("TAG@123", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }


    public void ShowmyDoubloon() {
        LayoutInflater inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_my_available_doubloons, null);
        RecyclerView sponser__recycler_view = (RecyclerView) view.findViewById(R.id.mydoubloons__recycler_view);
        Button add_doubloon_but = (Button) view.findViewById(R.id.add_doubloon_but);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mCtx);
        sponser__recycler_view.setLayoutManager(mLayoutManager);
        sponser__recycler_view.setItemAnimator(new DefaultItemAnimator());
      final   MyAvailableDoubloonsAdapter myAvailableDoubloonsAdapter = new MyAvailableDoubloonsAdapter(mCtx, itemList);
        sponser__recycler_view.setAdapter(myAvailableDoubloonsAdapter);
        add_doubloon_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = "";
                List<MyAvailableDoubloonsModel> stList = ((MyAvailableDoubloonsAdapter)myAvailableDoubloonsAdapter)
                        .getStudentist();

                for (int i = 0; i < stList.size(); i++) {
                    MyAvailableDoubloonsModel singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == true) {
                        if(data.equals("")){
                            data = singleStudent.getId().toString();
                        }
                        else {
                            data = data + "," + singleStudent.getId().toString();
                        }

                    }

                }
                mBottomSheetDialog.dismiss();
                saveRelatedDoubloons(main_id,data);
            }
        });



        mBottomSheetDialog = new Dialog(mCtx, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        // mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        mBottomSheetDialog.show();


    }


    public void saveRelatedDoubloons(final  String main_post_id,final String related_post_id){
        pd = new ProgressDialog(mCtx);
        pd.setMessage("loading");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/saveRelatedDoubloons",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.d("TAG@123", response);
                        System.out.println(response);
                        if (response != null) {

                            try {
                                JSONObject jsonObjectdata = new JSONObject(response);
                                Toast.makeText(mCtx, jsonObjectdata.getString("res_msg"), Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        String message = "Anything happened Wrong Please try Again.";
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(mCtx, message, Toast.LENGTH_LONG).show();
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("main_post_id", main_post_id);
                params.put("related_post_id", related_post_id);
                Log.d("TAG@123", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }
}