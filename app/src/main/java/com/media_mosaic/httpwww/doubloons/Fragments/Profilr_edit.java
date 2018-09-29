package com.media_mosaic.httpwww.doubloons.Fragments;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.Countray_model;
import com.media_mosaic.httpwww.doubloons.Data_Model.State_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Profilr_edit extends Fragment {
    Spinner country, state, intrest_spinner;
    View view;
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    Button Uplate_info;
    ProgressDialog pd;
    ReadPref readPref;
    private int mYear, mMonth, mDay;
    private ProgressDialog pDialog;
    ArrayList<String> interests_array;
    EditText name_text, email_text, phone_no_text, dob_text, old_password, new_password, address_text, city_text;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profilr_edit, container, false);
        state = (Spinner) view.findViewById(R.id.state);
        Uplate_info=(Button)view.findViewById(R.id.Uplate_info);
        country = (Spinner) view.findViewById(R.id.country);
        intrest_spinner = (Spinner) view.findViewById(R.id.intrest_spinner);
        name_text = (EditText) view.findViewById(R.id.name_text);
        email_text = (EditText) view.findViewById(R.id.email_text);
        email_text.setEnabled(false);
        phone_no_text = (EditText) view.findViewById(R.id.phone_no_text);
        dob_text = (EditText) view.findViewById(R.id.dob_text);
        old_password = (EditText) view.findViewById(R.id.old_password);
        new_password = (EditText) view.findViewById(R.id.new_password);
        address_text = (EditText) view.findViewById(R.id.address_text_profile);
        city_text = (EditText) view.findViewById(R.id.city_text);
        String info = getArguments().getString("Update_info");
        try {

            if(readPref.getusertype().equals("player")){
                JSONObject jsonObject = new JSONObject(getArguments().getString("Update_info"));
                name_text.setText(jsonObject.getString("name"));
                email_text.setText(jsonObject.getString("email"));

            }
            else {
                JSONObject jsonObject = new JSONObject(getArguments().getString("Update_info")).getJSONObject("Doubloon_app");
                name_text.setText(jsonObject.getString("owner_name"));
                email_text.setText(jsonObject.getString("email"));

            }

        } catch (JSONException d) {
        }
        interests_array = new ArrayList<>();
        interests_array.add("Interest *");
        getinterests();
        getcountrydata();
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Countray_model myCountry;
                if (!(country.getSelectedItem() == null)) {
                    myCountry = (Countray_model) country.getSelectedItem();
                    String t = myCountry.getId();
                    getstatedata(myCountry.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        dob_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dob_text.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Countray_model myCountry;
                if (!(state.getSelectedItem() == null)) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
Uplate_info.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            GetResponse();
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", readPref.getuserId());
            params.put("name", name_text.getText().toString().trim());
            params.put("city", city_text.getText().toString().trim());
            params.put("state", state.getSelectedItem().toString().trim());
            params.put("country", country.getSelectedItem().toString().trim());
            params.put("dob", dob_text.getText().toString().trim());
            params.put("mobile_no", phone_no_text.getText().toString().trim());
            params.put("interest", intrest_spinner.getSelectedItem().toString().trim());
            params.put("old_password", old_password.getText().toString().trim());
            params.put("new_password", new_password.getText().toString().trim());
            Log.d("TAG@123", params.toString());
            groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
            groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/userInfoUpdate", params, "item");


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
                pDialog.dismiss();

                Log.d("TAG@123", response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    settoast(jsonObject.getString("res_msg"));
                }catch (JSONException f){

                }



            }

            @Override
            public void onError(String error, String responseType) {

                pDialog.dismiss();
            }
        };



    }

    public void getcountrydata() {
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

                                ArrayAdapter<Countray_model> myAdapter = new ArrayAdapter<Countray_model>(getActivity(), R.layout.country_text, myCountries);
                                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                country.setAdapter(myAdapter);

                            } catch (JSONException e) {
                                pd.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // pd.dismiss();

                    }
                }
        );


        requestQueue.add(jsonObjectRequest);
    }
    public void getinterests() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://www.doubloon.media-mosaic.in/apis/interests",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {

                            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();
                        } else {

                            try {
                                Log.d("TAG@123", response.toString());
                                JSONObject jsonObject = new JSONObject(response.toString()).getJSONObject("interests");
                                for (int i = 1; i < jsonObject.length(); i++) {
                                    try {

                                        interests_array.add(jsonObject.getString(""+i));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(), R.layout.interests_text, interests_array);
                                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                intrest_spinner.setAdapter(myAdapter);

                            } catch (JSONException e) {

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // pd.dismiss();

                    }
                }
        );


        requestQueue.add(jsonObjectRequest);
    }

    public void getstatedata(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/get_state_name",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String dd = response.toString();
                        System.out.println(response);
                        Log.d("TAG@123", response.toString());
                        if (response != null) {
                            Log.d("TAG@123", response.toString());
                            try {
                                settoast(new JSONObject(response.toString()).getString("res_msg"));
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
                                ArrayAdapter<State_model> myAdapter = new ArrayAdapter<State_model>(getActivity(), R.layout.state_text, myState);
                                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                state.setAdapter(myAdapter);
                            } catch (JSONException e) {

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "Anything happened Wrong Please try Again.";
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("country_id", id);
                Log.d("TAG@123", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

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
