package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.Countray_model;
import com.media_mosaic.httpwww.doubloons.Data_Model.State_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.Uitl.AppHelper;
import com.media_mosaic.httpwww.doubloons.Uitl.VolleyMultipartRequest;
import com.media_mosaic.httpwww.doubloons.Uitl.VolleySingleton;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Create_Single_doubllon_fragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener {
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    LinearLayout sponsored_lay, step_layout;
    EditText state_text,country_text,sponser_name,doubloon_name,  flat_no, locality_text, city_text, price_text, number_person;
    TextView title_text, steps_coun,expity_date,description_text,clues_text;
    Button continue_button;
    private int mYear, mMonth, mDay;
    public static String parent_id = "";
    public static String count = "";
    public static String number_person_win = "";
    View view;
    Spinner country,state;
    ImageView mAvatarImage;
    ReadPref readPref;
    private static final int SELECT_VIDEO = 3;
    TextView add_img,add_video,upload,upload_img_video;
    private ProgressDialog pDialog;
    String Sponsored_logo="";
    private int PICK_IMAGE_REQUEST = 1;
    String filePath="";
    String logo_name="";
    boolean upload_logo=true;
    ArrayList<String> al;
    public  int k=0;
    ProgressDialog pd;
    private static final String[] numNames = {"First", "Second", "Third", " fourth", " fiveth", " six", " seventh", " eighth", " nineth", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_create_doubllon_fragment, container, false);
        continue_button = (Button) view.findViewById(R.id.continue_button);
        upload_img_video=(TextView) view.findViewById(R.id.upload_img_video);
        add_img=(TextView) view.findViewById(R.id.add_img);
        mAvatarImage=(ImageView)view.findViewById(R.id.mAvatarImage);
        add_video=(TextView) view.findViewById(R.id.add_video);
        doubloon_name = (EditText) view.findViewById(R.id.doubloon_name);
        steps_coun = (TextView) view.findViewById(R.id.steps_coun);
        title_text = (TextView) view.findViewById(R.id.title_text);
        expity_date = (TextView) view.findViewById(R.id.expity_date);
        description_text = (TextView) view.findViewById(R.id.description_text);
        clues_text = (TextView) view.findViewById(R.id.clues_text);
        flat_no = (EditText) view.findViewById(R.id.flat_no);
        locality_text = (EditText) view.findViewById(R.id.locality_text);
        city_text = (EditText) view.findViewById(R.id.city_text);
        price_text = (EditText) view.findViewById(R.id.price_text);
        number_person = (EditText) view.findViewById(R.id.number_person);
        sponser_name= (EditText) view.findViewById(R.id.sponser_name);
        state_text=(EditText) view.findViewById(R.id.state_text);
        country_text=(EditText) view.findViewById(R.id.country_text);
        step_layout = (LinearLayout) view.findViewById(R.id.step_layout);
        sponsored_lay = (LinearLayout) view.findViewById(R.id.sponsored_lay);
        state= (Spinner)view.findViewById(R.id.state);
        country = (Spinner)view.findViewById(R.id.country);
        sponsored_lay.setVisibility(View.GONE);
        title_text.setText(getArguments().getString("Doubloons_name"));
        Sponsored_logo=getArguments().getString("logo");
        expity_date.setOnClickListener(this);
        clues_text.setOnClickListener(this);
        continue_button.setOnClickListener(this);
        description_text.setOnClickListener(this);
        add_img.setOnClickListener(this);
        add_video.setOnClickListener(this);
        parent_id=new String("");
        upload_img_video.setOnClickListener(this);
        sponser_name.setText("");
        if (getArguments().getString("Post_type").equals("Multi")) {
            count = new String("");
            count = getArguments().getString("Doubloons_step");
            String count_number=numNames[k];
            String hini="Enter "+count_number+ " doubloons name ";
            if(k==0){
                doubloon_name.setText(getArguments().getString("Doubloons_name"));
            }else {
                doubloon_name.setHint(hini);
            }
        }
        else {
            title_text.setVisibility(View.GONE);
            doubloon_name.setHint("Doubloon Name");
            doubloon_name.setText(getArguments().getString("Doubloons_name"));
        }
        if (getArguments().getString("group_type").equals("Sponsored")) {
            sponser_name.setHint("Sponsor Name");
            sponsored_lay.setVisibility(View.VISIBLE);
        }
        al=new ArrayList<>();
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                Countray_model myCountry;
                if(!(country.getSelectedItem() == null))
                {   myCountry = (Countray_model)country.getSelectedItem();
                    String t=myCountry.getId();
                    country_text.setText(country.getSelectedItem().toString());
                    getstatedata(myCountry.getId());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }

        });
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                Countray_model myCountry;
                state_text.setText(state.getSelectedItem().toString());
                if(!(state.getSelectedItem() == null))
                {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }

        });

        getcountrydata();
        return view;
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch(v.getId()){
            case R.id.expity_date:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        expity_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.clues_text:
                show("Clues", clues_text.getText().toString());
                break;
            case R.id.description_text:
                show("description_text", description_text.getText().toString());
                break;
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_img_video:

                if(filePath.equals("")){
                    settoast("Please Select Picture .");
                }else {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    uploadlogo();
                }
                break;
            case R.id.add_img:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
            case R.id.add_video:
                chooseVideo();
                break;

            case R.id.expity_date:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        expity_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
            case R.id.clues_text:
                show("Clues", clues_text.getText().toString());
                break;
            case R.id.description_text:
                show("description_text", description_text.getText().toString());
                break;
            case R.id.continue_button:
                if(upload_logo){
                if (DataOK()) {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    GetResponse();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("house_no", flat_no.getText().toString().trim());
                    params.put("locality", locality_text.getText().toString().trim());
                    params.put("city", city_text.getText().toString().trim());
                    params.put("state", state_text.getText().toString().trim());
                    params.put("country", country_text.getText().toString().trim());
                    params.put("doubloons_name", doubloon_name.getText().toString().trim());
                    params.put("expiry_date", expity_date.getText().toString().trim());
                    params.put("description", description_text.getText().toString().trim());
                    params.put("clues", clues_text.getText().toString().trim());
                    params.put("price", price_text.getText().toString().trim());
                    params.put("no_of_people", number_person.getText().toString().trim());
                    params.put("partner_id", readPref.getuserId());
                    params.put("parent_id", parent_id);
                    params.put("post_type", getArguments().getString("Post_type"));
                    params.put("group_type", getArguments().getString("group_type"));
                    params.put("sponsor_name", sponser_name.getText().toString().trim());
                    params.put("sponsor_logo", Sponsored_logo);
                    params.put("images", getpostimg());
                    params.put("videos", locality_text.getText().toString().trim());
                    Log.d("TAG@123", params.toString());
                    groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
                    groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/addPost", params, "item");
                }}
                else {
                    settoast("Please Upload images");
                }
                break;

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                upload_logo=false;
            Uri picUri = data.getData();
            filePath = picUri.toString();

            Uri imageUri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                mAvatarImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                String err=e.getMessage().toString();
            }
        }}
        catch (Exception e){
            String err=e.getMessage().toString();
        }
    }




    public  String getpostimg(){
        String completeString = new String();

if(al.size()>0){
    for(int i=0;i<al.size();i++){
        if (completeString.equals("")) {
            completeString = completeString + al.get(i);
        } else {
            completeString = completeString + "," + al.get(i);
        }

    }
}
        return completeString;
    }
    private void uploadlogo() {

        String url = "http://www.doubloon.media-mosaic.in/apis/uploadClue";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                upload_logo=true;
                pDialog.dismiss();
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    settoast(result.getString("res_msg"));
                    logo_name= result.getString("res_name");
                    al.add(logo_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getActivity(), mAvatarImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void show(final String type, String data) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.more_data, null);
        dialogBuilder.setView(dialogView);
        final EditText more_data_edit = (EditText) dialogView.findViewById(R.id.more_data_edit);
        if (type.equals("Clues")) {
            dialogBuilder.setTitle("Enter Clues");
        } else {
            dialogBuilder.setTitle("Enter Description");

        }
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (type.equals("Clues")) {
                    clues_text.setText(more_data_edit.getText().toString());
                } else {
                    description_text.setText(more_data_edit.getText().toString());
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();


    }

    public boolean DataOK() {
        boolean isOk;
        if (expity_date.getText().toString().trim().equals("") || expity_date.getText().toString().equals("Email")) {
            expity_date.setError("Please Enter");
            isOk = false;
        } else {
            if (description_text.getText().toString().trim().equals("")) {
                description_text.setError("Please Enter");
                isOk = false;
            } else {
                if (clues_text.getText().toString().trim().equals("")) {
                    clues_text.setError("Please Enter");
                    isOk = false;
                } else {
                    if (flat_no.getText().toString().trim().equals("")) {
                        flat_no.setError("Please Enter");
                        isOk = false;
                    } else {
                        if (locality_text.getText().toString().trim().equals("")) {
                            locality_text.setError("Please Enter");
                            isOk = false;
                        } else {
                            if (country_text.getText().toString().trim().equals("")) {
                                country_text.setError("Please Enter");
                                settoast("Please Select country");
                                isOk = false;
                            } else {
                                if (state_text.getText().toString().trim().equals("")) {
                                    settoast("Please Select state");
                                    isOk = false;
                                } else {
                                    if (city_text.getText().toString().trim().equals("")) {
                                        city_text.setError("Please Enter");
                                        isOk = false;
                                    } else {
                                        if (price_text.getText().toString().trim().equals("")) {
                                            price_text.setError("Please Enter");
                                            isOk = false;
                                        } else {
                                            if (number_person.getText().toString().trim().equals("")) {
                                                number_person.setError("Please Enter");
                                                isOk = false;
                                            } else {
                                                if (number_person_win.equals("")) {
                                                    isOk = true;
                                                } else {
                                                    if (Integer.parseInt(number_person.getText().toString())>Integer.parseInt(number_person_win)) {
                                                        number_person.setError("Please Enter");
                                                        settoast("Please Change ");
                                                        isOk = false;
                                                    } else {
                                                        isOk = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return isOk;
    }

    private void GetResponse() {
        groceryAppResponse = new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {
                pDialog.dismiss();
                try {
                     JSONObject jsonObj= new JSONObject(response.toString());
                     settoast(jsonObj.getString("res_msg"));
                     if(jsonObj.getString("res_msg").equals("Please Enter Proper Address!")){
                         settoast(jsonObj.getString("res_msg"));
                     }
else {
                         Log.d("TAG@123", "Item Response:" + response);

                         number_person_win = new String("");
                         number_person_win = number_person.getText().toString().trim();
                         if (getArguments().getString("Post_type").equals("Multi")) {
                             if (count.equals("1")) {
                                 k++;
                                 steps_coun.setText("Last");
                                 try {
                                     JSONObject jsonObject = new JSONObject(response.toString());
                                     settoast(jsonObject.getString("res_msg"));
                                     Mydoubloons fragment = new Mydoubloons();
                                     getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                 } catch (JSONException r) {
                                 }
                             } else {
                                 count = String.valueOf(Integer.parseInt(count) - 1);
                                 k++;
                                 steps_coun.setText(count);
                                 expity_date.setText("");
                                 description_text.setText("");
                                 clues_text.setText("");
                                 flat_no.setText("");
                                 locality_text.setText("");
                                 city_text.setText("");
                                 price_text.setText("");
                                 number_person.setText("");
                                 doubloon_name.setText("");
                                 String count_number = numNames[k];
                                 String hini = "Enter " + count_number + " doubloons name ";
                                 if (k == 0) {
                                     doubloon_name.setText(getArguments().getString("Doubloons_name"));
                                 } else {
                                     doubloon_name.setHint(hini);
                                 }
                                 try {
                                     JSONObject jsonObject = new JSONObject(response.toString());
                                     settoast(jsonObject.getString("res_msg"));
                                     if (parent_id.equals("")) {
                                         parent_id = jsonObject.getString("last_post_id");
                                     }
                                 } catch (JSONException r) {

                                 }
                             }
                         } else {
                             try {
                                 JSONObject jsonObject = new JSONObject(response.toString());
                                 settoast(jsonObject.getString("res_msg"));
                                 Mydoubloons fragment = new Mydoubloons();
                                 getActivity().getSupportFragmentManager().beginTransaction()
                                         .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                             } catch (JSONException r) {
                             }
                         }


                     }  }catch (JSONException r){


                }


            }
            @Override
            public void onError(String error, String responseType) {


            }
        };


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


    public void getcountrydata (){
        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://www.doubloon.media-mosaic.in/apis/get_country_name",
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
                                Log.d("TAG@123",response.toString());
                                ArrayList<Countray_model> myCountries = new ArrayList<>();
                                JSONObject jsonObject=new JSONObject(response.toString()).getJSONObject("Doubloon_app").getJSONObject("country_name");
                                for(int i=1;i<jsonObject.length();i++){
                                    try {
                                        Countray_model countray_model=new Countray_model();
                                        countray_model.setCountry_name(jsonObject.getString(String.valueOf(i)));
                                        countray_model.setId(String.valueOf(i));
                                        myCountries.add(countray_model);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ArrayAdapter<Countray_model> myAdapter = new ArrayAdapter<Countray_model>(getActivity(), R.layout.doubloon_text, myCountries);
                                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                country.setAdapter(myAdapter);

                            }
                            catch (JSONException e){
                                pd.dismiss();
                            }}
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // pd.dismiss();

                    }
                }
        );


        requestQueue.add(jsonObjectRequest);
    }
    public  void getstatedata( final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/get_state_name",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String dd=response.toString();
                        System.out.println(response);
                        Log.d("TAG@123", response.toString());
                        if (response != null) {

                            Log.d("TAG@123",response.toString());
                            try{
                                settoast(new JSONObject(response.toString()).getString("res_msg"));
                                ArrayList<State_model> myState = new ArrayList<>();
                                String jsonObject=new JSONObject(response.toString()).getJSONObject("Doubloon_app").getJSONObject("state_name").toString().replace("{}","");
                                String [] arrOfStr = jsonObject.split(",");
                                for (String a : arrOfStr){
                                    System.out.println(a);
                                    try {
                                        String [] arr = a.split(":");
                                        State_model state_model=new State_model();
                                        state_model.setState_name(arr[1].replaceAll("\"","").toString());
                                        state_model.setId(arr[0]);
                                        myState.add(state_model);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                ArrayAdapter<State_model> myAdapter = new ArrayAdapter<State_model>(getActivity(), R.layout.doubloon_text, myState);
                                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                state.setAdapter(myAdapter);
                            }
                            catch (JSONException e){
 /*   for(int i=0;i<jsonObject.length();i++){
                                    try {
                                        JSONObject json=(new JSONObject(response.toString())).getJSONObject(String.valueOf(i));
                                        State_model state_model=new State_model();
                                        state_model.setState_name(json.getString("state_name"));
                                        state_model.setId(json.getString("id"));
                                        myState.add(state_model);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }*/

                            }}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message="Anything happened Wrong Please try Again.";
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("country_id",id);
                Log.d("TAG@123", params.toString());
                return params;}

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}
