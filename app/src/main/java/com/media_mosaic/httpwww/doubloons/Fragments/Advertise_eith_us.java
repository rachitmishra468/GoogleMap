package com.media_mosaic.httpwww.doubloons.Fragments;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.Age_model;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
public class Advertise_eith_us extends Fragment {
    View view;
    EditText url_text, ciyt_text, age_text, interest_text, country_text, state_text, adverties_us_edit, target_edit, suggested_edit;
    Spinner country, state, age, interest;
    private ProgressDialog pDialog;
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    ArrayList<String> interests_array;
    Button add_advertise_us;
    TextView upload_img, add_img;
    ReadPref readPref;
    ImageView mAvatarImage;
    boolean upload_logo = true;
    private int PICK_IMAGE_REQUEST = 1;
    String filePath = "";
    String img_name="";
    String Age_value="";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int SELECT_VIDEO = 3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_advertise_eith_us, container, false);
        requestStoragePermission();
        adverties_us_edit = (EditText) view.findViewById(R.id.adverties_us_edit);
        mAvatarImage=(ImageView)view.findViewById(R.id.mAvatarImage);
        upload_img = (TextView) view.findViewById(R.id.upload_img);
        add_img = (TextView) view.findViewById(R.id.add_img);
        ciyt_text = (EditText) view.findViewById(R.id.ciyt_text);
        url_text = (EditText) view.findViewById(R.id.url_text);
        age_text = (EditText) view.findViewById(R.id.age_text);
        interest_text = (EditText) view.findViewById(R.id.interest_text);
        country_text = (EditText) view.findViewById(R.id.country_text);
        state_text = (EditText) view.findViewById(R.id.state_text);
        target_edit = (EditText) view.findViewById(R.id.target_edit);
        suggested_edit = (EditText) view.findViewById(R.id.suggested_edit);
        country = (Spinner) view.findViewById(R.id.country);
        state = (Spinner) view.findViewById(R.id.state);
        age = (Spinner) view.findViewById(R.id.age);
        interest = (Spinner) view.findViewById(R.id.interest);
        add_advertise_us = (Button) view.findViewById(R.id.add_advertise_us);
        interests_array = new ArrayList<>();
        interests_array.add("Interest *");
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Countray_model myCountry;
                if (!(country.getSelectedItem() == null)) {
                    myCountry = (Countray_model) country.getSelectedItem();
                    String t = myCountry.getId();
                    country_text.setText(country.getSelectedItem().toString());
                    getstatedata(myCountry.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Age_model age_model;
                if (!(age.getSelectedItem() == null)) {
                    age_model = (Age_model) age.getSelectedItem();
                    Age_value= age_model.getAge();
                    age_text.setText(age.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Countray_model myCountry;
                state_text.setText(state.getSelectedItem().toString());
                if (!(state.getSelectedItem() == null)) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                interest_text.setText(interest.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        getcountrydata();
        getagedata();
        getinterestdata();
        add_advertise_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataOK()) {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    GetResponse();
                    HashMap<String, String> params = new HashMap<>();
                    params.put("advertise_with_us", adverties_us_edit.getText().toString().trim());
                    params.put("target_audience", target_edit.getText().toString().trim());
                    params.put("suggested_doubloons", suggested_edit.getText().toString().trim());
                    params.put("interest", interest_text.getText().toString().trim());
                    params.put("age", Age_value);
                    params.put("country", country_text.getText().toString().trim());
                    params.put("state", state_text.getText().toString().trim());
                    params.put("city", ciyt_text.getText().toString().trim());
                    params.put("url", url_text.getText().toString().trim());
                    params.put("image", img_name);
                    params.put("user_id", readPref.getuserId());
                    Log.d("TAG@123", params.toString());
                    groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
                    groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/advertiseWithUs", params, "advertiseWithUs");
                }
            }
        });

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                uploadlogo();
            }
        });

        return view;
    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                try {
                    upload_logo = false;
                    Uri picUri = data.getData();
                    filePath = picUri.toString();
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    mAvatarImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    String err = e.getMessage().toString();
                }
            }
        } catch (Exception e) {
            String err = e.getMessage().toString();
        }
    }

    public boolean DataOK() {
        boolean isOk;
        if (adverties_us_edit.getText().toString().trim().equals("")) {
            adverties_us_edit.setError("Please Enter");
            isOk = false;
        } else {
            if (target_edit.getText().toString().trim().equals("")) {
                target_edit.setError("Please Enter");
                isOk = false;
            } else {
                if (suggested_edit.getText().toString().trim().equals("")) {
                    suggested_edit.setError("Please Enter");
                    isOk = false;
                } else {
                    if (age_text.getText().toString().trim().equals("")) {
                        age_text.setError("Please Enter");
                        isOk = false;
                    } else {
                        if (interest_text.getText().toString().trim().equals("Interest *")) {
                            interest_text.setError("Please Enter");
                            isOk = false;
                        } else {
                            if (country_text.getText().toString().trim().equals("")) {
                                country_text.setError("Please Enter");

                                isOk = false;
                            } else {
                                if (state_text.getText().toString().trim().equals("")) {
                                    state_text.setError("Please Enter");
                                    isOk = false;
                                } else {
                                    if (ciyt_text.getText().toString().trim().equals("")) {
                                        ciyt_text.setError("Please Enter");
                                        isOk = false;
                                    } else {
                                        if (url_text.getText().toString().trim().equals("")) {
                                            url_text.setError("Please Enter");
                                            isOk = false;
                                        } else {
                                            if (!upload_logo) {
                                                Toast.makeText(getActivity(),"Upload Image",Toast.LENGTH_SHORT).show();
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
        return isOk;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void getcountrydata() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        GetResponse();
        groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.GET, "http://www.doubloon.media-mosaic.in/apis/get_country_name", null, "country_name");
    }

    public void getstatedata(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("country_id", id);
        Log.d("TAG@123", params.toString());
        GetResponse();
        groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/get_state_name", params, "get_state_name");
    }

    public void getagedata() {
        GetResponse();
        groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.GET, "http://www.doubloon.media-mosaic.in/apis/agerange", null, "agerange");
    }

    public void getinterestdata() {
        GetResponse();
        groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.GET, "http://www.doubloon.media-mosaic.in/apis/interests", null, "interests");
    }

    private void GetResponse() {
        groceryAppResponse = new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Log.d("TAG@123", response);
                if (responseType.equals("country_name")) {
                    try {
                        Log.d("TAG@123", response.toString());
                        ArrayList<Countray_model> myCountries = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(response.toString()).getJSONObject("Doubloon_app").getJSONObject("country_name");
                        for (int i = 1; i < jsonObject.length(); i++) {
                            try {
                                Countray_model countray_model = new Countray_model();
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

                    } catch (JSONException e) {
                        pDialog.dismiss();
                    }
                } else if (responseType.equals("get_state_name")) {
                    {
                        Log.d("TAG@123", response.toString());
                        try {
                            ArrayList<State_model> myState = new ArrayList<>();
                            String jsonObject = new JSONObject(response.toString()).getJSONObject("Doubloon_app").getJSONObject("state_name").toString().replace("{}", "");
                            String[] arrOfStr = jsonObject.split(",");
                            for (String a : arrOfStr) {
                                System.out.println(a);
                                try {
                                    String[] arr = a.split(":");
                                    State_model state_model = new State_model();
                                    state_model.setState_name(arr[1].replaceAll("\"", "").toString());
                                    state_model.setId(arr[0]);
                                    myState.add(state_model);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            ArrayAdapter<State_model> myAdapter = new ArrayAdapter<State_model>(getActivity(), R.layout.doubloon_text, myState);
                            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            state.setAdapter(myAdapter);
                        } catch (JSONException e) {

                        }
                    }
                } else if (responseType.equals("agerange")) {
                    try {

                        ArrayList<Age_model> Age_list = new ArrayList<>();
                        JSONObject resobj = new JSONObject(response.toString()).getJSONObject("range");
                        Iterator<String> iter = resobj.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {
                                Object value = resobj.get(key);
                                Age_model age_model=new Age_model();
                                age_model.setAge(key);
                                age_model.setAge_range(value.toString());
                                Age_list.add(age_model);
                            } catch (JSONException e) {
                                // Something went wrong!
                            }
                        }
                        ArrayAdapter<Age_model> myAdapter = new ArrayAdapter<Age_model>(getActivity(), R.layout.doubloon_text, Age_list);
                        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        age.setAdapter(myAdapter);
                    }
                    catch (Exception e){
                        String key = e.getMessage().toString();
                    }
                } else if (responseType.equals("interests")) {
                    try {
                        Log.d("TAG@123", response.toString());
                        JSONObject jsonObject = new JSONObject(response.toString()).getJSONObject("interests");
                        for (int i = 1; i < jsonObject.length(); i++) {
                            try {
                                interests_array.add(jsonObject.getString("" + i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(), R.layout.interests_text, interests_array);
                        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        interest.setAdapter(myAdapter);

                    } catch (JSONException e) {

                    }
                } else if (responseType.equals("advertiseWithUs")) {

                    Log.d("advertiseWithUs", response);
                    try {
                        Log.d("TAG@123", response.toString());
                        JSONObject jsonObject = new JSONObject(response.toString());
                        Toast.makeText(getActivity(), jsonObject.getString("res_msg"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {

                    }
                }


            }

            @Override
            public void onError(String error, String responseType) {


            }
        };


    }
    private void uploadlogo() {

        String url = "http://www.doubloon.media-mosaic.in/apis/uploadAdvertise";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                upload_logo=true;
                pDialog.dismiss();
                String resultResponse = new String(response.data);
                Log.d("TAG@123", resultResponse.toString());
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    Toast.makeText(getActivity(),result.getString("res_msg"),Toast.LENGTH_SHORT).show();
                    img_name=result.getString("image_name");
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

}
