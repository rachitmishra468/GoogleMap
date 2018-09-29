package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Doubloons_clues_fragment extends Fragment {
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    NetworkImageView add_detail_first,add_detail_sceound;
    View view;
    String url="";
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    private ProgressDialog pDialog;
    ReadPref readPref;
    TextView clues_text_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_doubloons_clues_fragment, container, false);
        add_detail_first=(NetworkImageView)view.findViewById(R.id.add_detail_first);
        add_detail_sceound=(NetworkImageView)view.findViewById(R.id.add_detail_sceound);
        clues_text_view=(TextView)view.findViewById(R.id.clues_text_view);
        clues_text_view.setText(getArguments().getString("clues"));
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        if(readPref.getusertype().equals("player")){url=  "http://www.doubloon.media-mosaic.in/apis/getPlayerAdvertise";}
        else {url=  "http://www.doubloon.media-mosaic.in/apis/getPartnerAdvertise";}
        GetResponse();
        HashMap<String,String> paramsadd = new HashMap<>();
        paramsadd.put("user_id",readPref.getuserId());
        paramsadd.put("screen_type","Clue Details");
        Log.d("TAG@123",paramsadd.toString());
        groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, url,paramsadd,"Add_img");
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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
                Log.d("TAG@123", "Item Response:" + response);
                pDialog.dismiss();
                if(responseType.equals("hunt_doubloon")){
                }
                else if(responseType.equals("Add_img")){
                    imageLoader = MyApplication.getInstance().getImageLoader();
                    add_detail_first.setImageUrl("http://doubloon.media-mosaic.in/images/advertise/img25a1006668a853.png", imageLoader);
                    add_detail_sceound.setImageUrl("http://doubloon.media-mosaic.in/images/advertise/img25a1006668a853.png", imageLoader);

                }
                else {

                }


            }

            @Override
            public void onError(String error, String responseType) {


            }
        };



    }

}
