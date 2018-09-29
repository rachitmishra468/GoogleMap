package com.media_mosaic.httpwww.doubloons.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.Countray_model;
import com.media_mosaic.httpwww.doubloons.Data_Model.Invite_Model;
import com.media_mosaic.httpwww.doubloons.Data_Model.MyDoubloons_model;
import com.media_mosaic.httpwww.doubloons.Fragments.Create_Multi_doubllon_Sub;
import com.media_mosaic.httpwww.doubloons.Fragments.Doubloon_detail_fragment;
import com.media_mosaic.httpwww.doubloons.Fragments.Mydoubloons;
import com.media_mosaic.httpwww.doubloons.Fragments.Update_doubloon_info;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;

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
 * Created by Rachit on 2/5/2018.
 */

public class MyDoubloon_Adapter extends RecyclerView.Adapter<MyDoubloon_Adapter.ProductViewHolder> {

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    private Context mCtx;
    ReadPref readPref;
    String DateToStr;
    boolean flag=true;
    Dialog mBottomSheetDialog;
    ListView listView;
    EditText editTextView,editpassword;
    ArrayList<Invite_Model> ItemModelList;
    Invite_CustomAdapter customAdapter;
    private List<MyDoubloons_model> myDoubloons_models;
    String invitation_mode="";
    Mydoubloons fragment=new Mydoubloons();
    public MyDoubloon_Adapter(Context mCtx, List<MyDoubloons_model> productList) {
        this.mCtx = mCtx;
        this.myDoubloons_models = productList;
    }

    @Override
    public MyDoubloon_Adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        readPref = new ReadPref(mCtx);
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateToStr = format.format(curDate);
        View view = inflater.inflate(R.layout.mydoubloons_items, null);
        return new MyDoubloon_Adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyDoubloon_Adapter.ProductViewHolder holder, int position) {
        imageLoader = MyApplication.getInstance().getImageLoader();
        final MyDoubloons_model product = myDoubloons_models.get(position);
        holder.doubloons_name.setText(product.getName());
        holder.address_text.setText(product.getAddress());
        holder.date_time.setText(product.getTime_limit());
        holder.runing_text.setText(product.getRunningCount());
        holder.success_text.setText(product.getSuccessCount());
        holder.price_doubloon.setText(product.getDiscount());
        if(product.getGroup_type().equals("Close Group")){
            holder.logo_img.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.featured_icon));
        }
        else if(product.getGroup_type().equals("Sponsored")){
            holder.logo_img.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.sponsored_marker_icon));
        }
        if(product.getRunningCount().equals("0")){
            holder.runing_text_hading.setTextColor(mCtx.getResources().getColor(R.color.text_code));
            holder.runing_text.setTextColor(mCtx.getResources().getColor(R.color.text_code));
        }
        else {
            holder.runing_text_hading.setTextColor(mCtx.getResources().getColor(R.color.running));
            holder.runing_text.setTextColor(mCtx.getResources().getColor(R.color.running));
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        if(readPref.getusertype().equals("player")){
          holder.invite_text.setVisibility(View.INVISIBLE);
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
                holder.day_left_text.setTextColor(mCtx.getResources().getColor(R.color.red));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }}
        else {
            if(product.getStatus().equals("1")){
                holder.edit_layout.setVisibility(View.INVISIBLE);
                holder.invite_text.setVisibility(View.INVISIBLE);
                holder.public_layout.setVisibility(View.GONE);
                holder.days_left.setVisibility(View.VISIBLE);
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
                        holder.dayleft_text.setTextColor(mCtx.getResources().getColor(R.color.black));
                    }
                    else {
                        holder.inactive_text.setVisibility(View.VISIBLE);
                        holder.day_left_text.setText("Expired");
                        holder.active_text.setVisibility(View.GONE);
                        holder.dayleft_text.setText("");
                        holder.day_left_text.setTextColor(mCtx.getResources().getColor(R.color.red));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                holder.edit_layout.setVisibility(View.VISIBLE);
                holder.public_layout.setVisibility(View.VISIBLE);
                holder.days_left.setVisibility(View.GONE);
                holder.inactive_text.setVisibility(View.VISIBLE);
                holder.active_text.setVisibility(View.GONE);
                if(product.getGroup_type().equals("Close Group")){
                    holder.invite_text.setVisibility(View.VISIBLE);
                } else {
                    holder.invite_text.setVisibility(View.INVISIBLE);
                }

            }






        }

        holder.edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update_doubloon_info doubloon_detail_fragment = new Update_doubloon_info();
                MapsActivity myActivity = (MapsActivity)mCtx;
                Bundle bundle = new Bundle();
                bundle.putString("post_id",  product.getId());
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        holder.info_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment doubloon_detail_fragment;
                if(product.getPost_type().equals("Multi")){
                    doubloon_detail_fragment = new Create_Multi_doubllon_Sub();
                }
                else {
                    doubloon_detail_fragment = new Doubloon_detail_fragment();
                }
                MapsActivity myActivity = (MapsActivity)mCtx;
                Bundle bundle = new Bundle();
                doubloon_detail_fragment = new Doubloon_detail_fragment();
                bundle.putString("post_id",product.getId());
                bundle.putString("flag","true");
                bundle.putString("doubloon_status",product.getDoubloon_status());
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        holder.doubloons_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Fragment doubloon_detail_fragment;
                if(product.getPost_type().equals("Multi")){

                    doubloon_detail_fragment = new Create_Multi_doubllon_Sub();
                }
                else {
                    doubloon_detail_fragment = new Doubloon_detail_fragment();
                }
                MapsActivity myActivity = (MapsActivity)mCtx;
                Bundle bundle = new Bundle();
                bundle.putString("post_id",  product.getId());
                bundle.putString("flag","true");
                bundle.putString("doubloon_status",product.getDoubloon_status());
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        holder.logo_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment doubloon_detail_fragment;
                if(product.getPost_type().equals("Multi")){

                    doubloon_detail_fragment = new Create_Multi_doubllon_Sub();
                }
                else {
                    doubloon_detail_fragment = new Doubloon_detail_fragment();
                }
                MapsActivity myActivity = (MapsActivity)mCtx;
                Bundle bundle = new Bundle();
                bundle.putString("post_id",  product.getId());
                bundle.putString("flag","true");
                bundle.putString("doubloon_status",product.getDoubloon_status());
                doubloon_detail_fragment.setArguments(bundle);
                myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.map, doubloon_detail_fragment, doubloon_detail_fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });







      holder.publish_text.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              publis_doubloon_post(mCtx,product.getId(),view);
          }
      });

        holder.delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Deletd_doubloon(mCtx,product.getId(),view);
            }
        });


        holder.invite_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(product.getId() );
            }
        });




    }


    @Override
    public int getItemCount() {
        int i = myDoubloons_models.size();
        return myDoubloons_models.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        LinearLayout edit_layout,price_layout, address_layout, public_layout, days_left;
        TextView runing_text_hading,delete_text,publish_text,invite_text,price_doubloon, doubloons_name, address_text, date_time, runing_text, success_text, dayleft_text, inactive_text, active_text, day_left_text;
        ImageView logo_img,info_img;
        public ProductViewHolder(View itemView) {
            super(itemView);
            edit_layout= itemView.findViewById(R.id.edit_layout);
            runing_text_hading= itemView.findViewById(R.id.runing_text_hading);
            doubloons_name = itemView.findViewById(R.id.doubloons_name);
            address_text = itemView.findViewById(R.id.address_text);
            date_time = itemView.findViewById(R.id.date_time);
            runing_text = itemView.findViewById(R.id.runing_text);
            success_text = itemView.findViewById(R.id.success_text);
            dayleft_text = itemView.findViewById(R.id.dayleft_text);
            price_layout = itemView.findViewById(R.id.price_layout);
            address_layout = itemView.findViewById(R.id.address_layout);
            inactive_text = itemView.findViewById(R.id.inactive_text);
            active_text = itemView.findViewById(R.id.active_text);
            day_left_text = itemView.findViewById(R.id.day_left_text);
            price_doubloon = itemView.findViewById(R.id.price_doubloon);
            public_layout = itemView.findViewById(R.id.public_layout);
            days_left = itemView.findViewById(R.id.days_left);
            logo_img= itemView.findViewById(R.id.logo_img);
            info_img= itemView.findViewById(R.id.info_img);
            invite_text= itemView.findViewById(R.id.invite_text);
            delete_text=itemView.findViewById(R.id.delete_text);
            publish_text=itemView.findViewById(R.id.publish_text);
            if (readPref.getusertype().equals("player")) {
                address_layout.setVisibility(View.GONE);
                price_layout.setVisibility(View.VISIBLE);
                public_layout.setVisibility(View.GONE);
                days_left.setVisibility(View.VISIBLE);
                invite_text.setVisibility(View.INVISIBLE);
                edit_layout.setVisibility(View.INVISIBLE);
            } else {
                flag=false;
                days_left.setVisibility(View.GONE);
                address_layout.setVisibility(View.VISIBLE);
                public_layout.setVisibility(View.VISIBLE);
                price_layout.setVisibility(View.GONE);
                edit_layout.setVisibility(View.VISIBLE);
            }

        }
    }


    private void publis_doubloon( final  Context context,final  String id,final  View v){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Please wait..");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/checkDoubloons",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        if (response != null) {
                            Log.d("TAG@123",response.toString());
                            try {
                                JSONObject emp=new JSONObject(response);
                                String res_msg = emp.getString("res_msg");
                                Toast.makeText(context,res_msg,Toast.LENGTH_SHORT).show();
                                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                Mydoubloons myFragment = new Mydoubloons();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout, myFragment).addToBackStack(null).commit();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(context,"Anything happened Wrong Please try Again.",Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        String message="Anything happened Wrong Please try Again.";
                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("partner_id",id);
                Log.d("TAG@123", params.toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void publis_doubloon_post( final  Context context,final  String id,final  View v){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Please wait..");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/publishPost",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        if (response != null) {
                            Log.d("TAG@123",response.toString());
                            try {
                                JSONObject emp=new JSONObject(response);
                                String res_msg = emp.getString("res_msg");
                                Toast.makeText(context,res_msg,Toast.LENGTH_SHORT).show();
                                fragment.changedata();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(context,"Anything happened Wrong Please try Again.",Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        String message="Anything happened Wrong Please try Again.";
                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                Log.d("TAG@123", params.toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void Deletd_doubloon( final  Context context,final  String id,final  View v){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Please wait..");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/deletePost",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        if (response != null) {
                            Log.d("TAG@123",response.toString());
                            try {
                                JSONObject emp=new JSONObject(response);
                                String res_msg = emp.getString("res_msg");
                                Toast.makeText(context,res_msg,Toast.LENGTH_SHORT).show();
                                fragment.changedata();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(context,"Anything happened Wrong Please try Again.",Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        String message="Anything happened Wrong Please try Again.";
                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                Log.d("TAG@123", params.toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public  void show(final String post_id ){
        LayoutInflater inflater = (LayoutInflater)mCtx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.invite_layout, null);
        listView = (ListView)view.findViewById(R.id.listview);
        final Spinner spin = (Spinner)view.findViewById(R.id.invitation_mode);
        ArrayAdapter<CharSequence> invitationadapter = ArrayAdapter.createFromResource(mCtx, R.array.invitation_mode_arr, R.layout.player_invitation_spinner);
        invitationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(invitationadapter);
        Button imgViewAdd=(Button)view.findViewById(R.id.imgViewAdd);
        Button cancle_button=(Button)view.findViewById(R.id.cancle_button);
        Button send_Invitaion_button=(Button)view.findViewById(R.id.send_Invitaion_button);
        editTextView = (EditText)view.findViewById(R.id.editTextView);
        editpassword= (EditText)view.findViewById(R.id.passTextView);
        ItemModelList = new ArrayList<Invite_Model>();
        customAdapter = new Invite_CustomAdapter(getApplicationContext(), ItemModelList);
        listView.setEmptyView(view.findViewById(R.id.listview));
        listView.setAdapter(customAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                if (spin.getSelectedItem().toString().trim().equals("Atomuatic")) {
                    invitation_mode="atomuatic";
                    editpassword.setVisibility(View.GONE);
                } else {
                    invitation_mode="custom";
                    editpassword.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }

        });
        send_Invitaion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spin.getSelectedItem().toString().trim().equals("Invitation Mode")){

                    Toast.makeText(mCtx,"Please Select Invitation Mode",Toast.LENGTH_LONG).show();

                }
                else {
                    if(ItemModelList.size()>0){
                        send_Invitation(mCtx,view,post_id ,getemail_ids(), getInvitation_mode() ,getpassword());
                    }else {
                        Toast.makeText(mCtx,"Please Add Email ",Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        imgViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spin.getSelectedItem().toString().trim().equals("Invitation Mode")){
                    Toast.makeText(mCtx,"Please Select Invitation Mode",Toast.LENGTH_LONG).show();
                }else {
                    String name = editTextView.getText().toString();
                    String Password = editpassword.getText().toString();
                    if (!isEmailValid(editTextView.getText().toString().trim())) {
                        editTextView.setError("This email address is invalid");
                    } else {
                        Invite_Model md = new Invite_Model(name,Password,invitation_mode);
                        ItemModelList.add(md);
                        customAdapter.notifyDataSetChanged();
                        editpassword.setText("");
                        editTextView.setText("");

                    }
                }

            }
        });

        mBottomSheetDialog = new Dialog(mCtx, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.CENTER);
        mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        mBottomSheetDialog.show();
        cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });





    }
public  String getemail_ids(){
    String completeString = new String();
    for(int i=0;i<ItemModelList.size();i++){
        if (completeString.equals("")) {
            completeString = completeString + ItemModelList.get(i).getName();
        } else {
            completeString = completeString + "," + ItemModelList.get(i).getName();
        }

    }


        return completeString;}

    public  String getpassword(){
        String completeString = new String();
        for(int i=0;i<ItemModelList.size();i++){
            if (completeString.equals("")) {
                completeString = completeString + ItemModelList.get(i).getPassword();
            } else {
                completeString = completeString + "," + ItemModelList.get(i).getPassword();
            }

        }


        return completeString;}

    public  String getInvitation_mode(){
        String completeString = new String();
        for(int i=0;i<ItemModelList.size();i++){
            if (completeString.equals("")) {
                completeString = completeString + ItemModelList.get(i).getInvitation_mode();
            } else {
                completeString = completeString + "," + ItemModelList.get(i).getInvitation_mode();
            }

        }


        return completeString;
    }
    public  boolean isEmailValid(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void send_Invitation( final  Context context,final  View v,final String post_id,final String email_ids,final String invitation_mode,final String password){
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Please wait..");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/sendPlayerInvitation",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();

                        if (response != null) {
                            Log.d("TAG@123",response.toString());
                            try {
                                JSONObject emp=new JSONObject(response);
                                String res_msg = emp.getString("res_msg");
                                Toast.makeText(context,res_msg,Toast.LENGTH_SHORT).show();
                                if( mBottomSheetDialog.isShowing()){
                                    mBottomSheetDialog.dismiss();
                                }
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(context,"Anything happened Wrong Please try Again.",Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        String message="Anything happened Wrong Please try Again.";
                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(" post_id",post_id);
                params.put("email_ids",email_ids);
                params.put("invitation_mode",invitation_mode);
                params.put("password",password);
                Log.d("TAG@123", params.toString());
                                                  return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}