package com.media_mosaic.httpwww.doubloons.Fragments;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.Uitl.UrlConstant;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import static com.facebook.FacebookSdk.getApplicationContext;
public class Doubloon_detail_fragment extends Fragment implements View.OnClickListener {
    View view;
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    private ProgressDialog pDialog;
    ReadPref readPref;
    ImageView img_gallery;
    boolean flag=true;
    boolean Expired=false;
    Button hunt_doubloon,try_outher_doubloon,submit_location_doubloon;
    TextView date_time,price,clues_text_read,description_text_read,readmoredescription,readmoreclue;
    NetworkImageView add_detail_first,add_detail_sceound;
    String img="";
    String group_type="";
    String description,clues="";
    Dialog mBottomSheetDialog;
    boolean hunt=false;
    String DateToStr;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateToStr = format.format(curDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doubloon_detail_fragment, container, false);
        clues_text_read=(TextView)view.findViewById(R.id.clues_text_read);
        date_time=(TextView)view.findViewById(R.id.date_time);
        price=(TextView)view.findViewById(R.id.price);
        description_text_read=(TextView)view.findViewById(R.id.description_text_read);
        readmoredescription=(TextView)view.findViewById(R.id.readmoredescription);
        readmoreclue=(TextView)view.findViewById(R.id.readmoreclue);
        try_outher_doubloon=(Button)view.findViewById(R.id.try_outher_doubloon);
        hunt_doubloon=(Button)view.findViewById(R.id.hunt_doubloon);
        submit_location_doubloon=(Button)view.findViewById(R.id.submit_location_doubloon);
        add_detail_first=(NetworkImageView)view.findViewById(R.id.add_detail_first);
        add_detail_sceound=(NetworkImageView)view.findViewById(R.id.add_detail_sceound);
        img_gallery=(ImageView)view.findViewById(R.id.img_gallery);
        readmoreclue.setPaintFlags(readmoreclue.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        readmoredescription.setPaintFlags(readmoreclue.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        clues_text_read.setOnClickListener(this);
        description_text_read.setOnClickListener(this);
        hunt_doubloon.setOnClickListener(this);
        submit_location_doubloon.setOnClickListener(this);
        readmoreclue.setOnClickListener(this);
        try_outher_doubloon.setOnClickListener(this);
        readmoredescription.setOnClickListener(this);
        pDialog = new ProgressDialog(getActivity());
        String data=getArguments().getString("post_id");
        if(getArguments().getString("flag").equals("true")){

            submit_location_doubloon.setVisibility(View.VISIBLE);
            hunt_doubloon.setVisibility(View.GONE);
            if(getArguments().getString("doubloon_status").equals("3")){
                hunt=true;
            }
        }
        else {
            hunt_doubloon.setVisibility(View.VISIBLE);
            submit_location_doubloon.setVisibility(View.GONE);
        }
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        GetResponse();
        HashMap<String,String> params = new HashMap<>();
        params.put("post_id", getArguments().getString("post_id"));
        Log.d("TAG@123",params.toString());
        groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/postDetails",params,"detail");
        if(readPref.getusertype().equals("player")){
            GetResponse();
            HashMap<String,String> paramsadd = new HashMap<>();
            paramsadd.put("user_id",readPref.getuserId());
            paramsadd.put("screen_type","Doubloon details");
            Log.d("TAG@123",paramsadd.toString());
            groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/getPlayerAdvertise",paramsadd,"Add_img");
        }
        else {
            hunt_doubloon.setVisibility(View.GONE);
            submit_location_doubloon.setVisibility(View.GONE);
            GetResponse();
            HashMap<String,String> paramsadd = new HashMap<>();
            paramsadd.put("user_id",readPref.getuserId());
            paramsadd.put("screen_type","Doubloon details");
            Log.d("TAG@123",paramsadd.toString());
            groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/postDetails",paramsadd,"Add_img");
        }
        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(img.equals("")){
                    settoast("Record Not Found");
                }
                else {
                    GalleryFragment fragment = new GalleryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("img",  img);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                }

            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        Bundle bundle = new Bundle();
      switch (view.getId()){
          case R.id.readmoreclue:
              fragment = new Doubloons_clues_fragment();
              bundle.putString("clues",  clues);
              fragment.setArguments(bundle);
              getActivity().getSupportFragmentManager().beginTransaction()
                      .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
              break;
          case R.id.readmoredescription:
              fragment = new Doubloons_description_Fragment();
              bundle.putString("description",  description);
              fragment.setArguments(bundle);
              getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
              break;
          case R.id.clues_text_read:
              fragment = new Doubloons_clues_fragment();
              bundle.putString("clues",  clues);
              fragment.setArguments(bundle);
              getActivity().getSupportFragmentManager().beginTransaction()
                      .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
              break;
          case R.id.description_text_read:
             fragment = new Doubloons_description_Fragment();
              bundle.putString("description",  description);
              fragment.setArguments(bundle);
              getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
              break;
          case R.id.hunt_doubloon:
              if(Expired){
                  settoast("Expired");
          } else {
              if(UrlConstant.started.equals("1")){
                  settoast("Already Playing ");
              }
              else {
                  if (flag) {

                      if (group_type.equals("Close Group")) {
                          show();
                      } else {
                          pDialog.setMessage("Loading...");
                          pDialog.setCancelable(false);
                          pDialog.show();
                          GetResponse();
                          HashMap<String, String> params = new HashMap<>();
                          params.put("post_id", getArguments().getString("post_id"));
                          params.put("user_id", readPref.getuserId());
                          Log.d("TAG@123", params.toString());
                          groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
                          groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/startGame", params, "hunt_doubloon");
                      }
                  } else {
                      flag = true;
                      pDialog.setMessage("Loading...");
                      pDialog.setCancelable(false);
                      pDialog.show();
                      GetResponse();
                      HashMap<String, String> params = new HashMap<>();
                      params.put("post_id", getArguments().getString("post_id"));
                      params.put("user_id", readPref.getuserId());
                      Log.d("TAG@123", params.toString());
                      groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
                      groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/startGame", params, "hunt_doubloon");
                  }
              }}
              break;
          case R.id.try_outher_doubloon:
              FragmentManager fm = getActivity().getSupportFragmentManager();
              fm.popBackStack();
              break;
          case R.id.submit_location_doubloon:

              if(Expired){
                  settoast("Expired");
              } else {
                  if (!hunt) {
                      settoast("Found Successfully");
                  } else {
                      pDialog.setMessage("Loading...");
                      pDialog.setCancelable(false);
                      pDialog.show();
                      GetResponse();
                      HashMap<String, String> params = new HashMap<>();
                      params.put("post_id", getArguments().getString("post_id"));
                      params.put("user_id", readPref.getuserId());
                      params.put("last_lat", String.valueOf(MapsActivity.latitude));
                      params.put("last_lng", String.valueOf(MapsActivity.longitude));
                      Log.d("TAG@123", params.toString());
                      groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
                      groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/checkReached", params, "checkReached");
                  }
              } break;

      }
    }
    public  void settoast(String title){
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.toast,(ViewGroup)view.findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text_toast);
        text.setText(title);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();

    }

    public  void show(){
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.check_player_password, null);
      final   EditText invitation_password=(EditText)view.findViewById(R.id.invitation_passTextView);
        Button password_submit=(Button)view.findViewById(R.id.password_submit);
        password_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(invitation_password.getText().toString().equals("")){
                }else {
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    mBottomSheetDialog.dismiss();
                    GetResponse();
                    HashMap<String,String> params = new HashMap<>();
                    params.put("post_id", getArguments().getString("post_id"));
                    params.put("password", invitation_password.getText().toString().trim());
                    Log.d("TAG@123",params.toString());
                    groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
                    groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/verifyInvitationPassword",params,"invitation_password");



                }
            }
        });
        mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.CENTER);
        mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        mBottomSheetDialog.show();
    }
    public  void congrats(String mess){
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.congrats_screen, null);
        TextView mess_text=(TextView)view.findViewById(R.id.mess_text);
        mess_text.setText(mess);
        Button button=(Button)view.findViewById(R.id.outher_doubloon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.CENTER);
        mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        mBottomSheetDialog.show();
    }
    private void GetResponse()
    {

        groceryAppResponse= new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {
                Log.d("TAG@123",  response);
                pDialog.dismiss();
               if(responseType.equals("hunt_doubloon")){

                   Log.d("hunt_doubloon", "Item Response:" + response);
                   try {
                       JSONObject jsonObjectdata = new JSONObject(response);
                       settoast(jsonObjectdata.getString("res_msg"));
                       if(jsonObjectdata.getString("res_code").equals("1")){
                           hunt_doubloon.setVisibility(View.GONE);
                           hunt=true;
                           submit_location_doubloon.setVisibility(View.VISIBLE);
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
               else if(responseType.equals("checkReached")){

                   try {
                       JSONObject jsonObjectdata = new JSONObject(response);
                       settoast(jsonObjectdata.getString("res_msg"));
                       if(jsonObjectdata.getString("res_code").equals("1")){
                           congrats(jsonObjectdata.getString("res_msg"));
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }
               else if(responseType.equals("Add_img")){
                   imageLoader = MyApplication.getInstance().getImageLoader();
                   add_detail_first.setImageUrl("http://doubloon.media-mosaic.in/images/advertise/img25a1006668a853.png", imageLoader);
                   add_detail_sceound.setImageUrl("http://doubloon.media-mosaic.in/images/advertise/img25a1006668a853.png", imageLoader);
               }
               else if(responseType.equals("invitation_password")){
                   try {
                       JSONObject jsonObjectdata = new JSONObject(response);
                       settoast(jsonObjectdata.getString("res_msg"));
                     if(jsonObjectdata.getString("res_code").equals("1")){
                           flag=false;
                           // hunt();
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               }
               else {
                   try {
                       JSONObject jsonObjectdata = new JSONObject(response);
                       JSONObject jsonArray = jsonObjectdata.getJSONObject("Doubloon_app");
                       group_type=jsonArray.getString("group_type");
                       date_time.setText(jsonArray.getString("time_limit"));
                       price.setText(jsonArray.getString("discount"));
                       clues=jsonArray.getString("clue");
                       description=jsonArray.getString("description");
                       clues_text_read.setText(jsonArray.getString("clue"));
                       description_text_read.setText(jsonArray.getString("description"));

                       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                       Date d1 = null;
                       Date d2 = null;
                       try {
                           d1 = format.parse(DateToStr);
                           d2 = format.parse(jsonArray.getString("time_limit"));
                           long diff = d2.getTime() - d1.getTime();
                           long diffDays = diff / (24 * 60 * 60 * 1000);
                           if (diffDays > 0.0) {
                               Expired=false;
                           } else {
                               Expired=true;
                           }

                       } catch (Exception e) {
                           e.printStackTrace();
                       }

                       img=jsonArray.getJSONArray("images").toString();
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
            }
            @Override
            public void onError(String error, String responseType) {


            }
        };



    }

public  void hunt(){
    pDialog.setMessage("Loading...");
    pDialog.setCancelable(false);
    pDialog.show();
    GetResponse();
    HashMap<String,String> params = new HashMap<>();
    params.put("post_id", getArguments().getString("post_id"));
    params.put("user_id", readPref.getuserId());
    Log.d("TAG@123",params.toString());
    groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
    groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/startGame",params,"hunt_doubloon");
}
}
