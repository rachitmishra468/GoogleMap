package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.media_mosaic.httpwww.doubloons.Adapters.FAQaddapter;
import com.media_mosaic.httpwww.doubloons.Data_Model.FAQ_model;
import com.media_mosaic.httpwww.doubloons.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FaqFragment extends Fragment {
    List<FAQ_model> productList;
    public static RecyclerView faq_recycle;
    ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_faq,container,false);

        faq_recycle = (RecyclerView)view.findViewById(R.id.faq_recycle);
        faq_recycle.setHasFixedSize(true);
        faq_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        getfaqdata();
        return view;

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


   public void  getfaqdata(){
       pd = new ProgressDialog(getActivity());
       pd.setMessage("Loading..");
        pd.show();
       RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
               Request.Method.GET, "http://www.doubloon.media-mosaic.in/apis/getFaqs",
               null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       if (response == null) {
                             pd.dismiss();
                           Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();
                       } else {

                             pd.dismiss();
                           try{
                               JSONArray jsonArray=new JSONArray();
                               JSONObject jsonObject=new JSONObject(response.toString());
                               jsonArray=jsonObject.getJSONArray("Doubloon_app");
                               productList = new ArrayList<>();
                               for(int i=0;i<jsonArray.length();i++){
                                   try {
                                       JSONObject json=jsonArray.getJSONObject(i);
                                       productList.add(new FAQ_model(1,json.getString("question"), json.getString("answer"), 4.3, 60000, R.drawable.logo));

                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }



                               }
                               FAQaddapter adapter = new FAQaddapter(getActivity(), productList);
                               faq_recycle.setAdapter(adapter);

                           }
                           catch (JSONException e){

                           }}
                   }
               },
               new Response.ErrorListener(){
                   @Override
                   public void onErrorResponse(VolleyError error){
                        pd.dismiss();

                   }
               }
       );


       requestQueue.add(jsonObjectRequest);
   }




    @Override
    public void onDetach() {
        super.onDetach();

    }



}
