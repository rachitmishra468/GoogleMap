package com.media_mosaic.httpwww.doubloons.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.media_mosaic.httpwww.doubloons.DB.SavePref;
import com.media_mosaic.httpwww.doubloons.Data_Model.Countray_model;
import com.media_mosaic.httpwww.doubloons.Data_Model.State_model;
import com.media_mosaic.httpwww.doubloons.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Host_SignUp extends AppCompatActivity {
    @BindView(R.id.partner_sign)
    Button Partner_sign;
    @BindView(R.id.title)
    TextView title;
    ProgressDialog pd;
    EditText owner_name,owner_busines_name,email_partner,password_partner,confirm_password_partner,partner_country,partner_state,partner_city;
    Spinner country,state;
    SavePref savepref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner__sign_up);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_hadder);
        ButterKnife.bind(this);
        title.setText(R.string.partner_sign_up);
        savepref=new SavePref();
        init();
        getcountrydata();
    }
    public  void init(){
        owner_name=(EditText)findViewById(R.id.owner_name);
        owner_busines_name=(EditText)findViewById(R.id.owner_busines_name);
        email_partner=(EditText)findViewById(R.id.email_partner);
        password_partner=(EditText)findViewById(R.id.password_partner);
        confirm_password_partner=(EditText)findViewById(R.id.confirm_password_partner);
        partner_country=(EditText)findViewById(R.id.partner_country);
        partner_state=(EditText)findViewById(R.id.partner_state);
        partner_city=(EditText)findViewById(R.id.partner_city);
        state= (Spinner) findViewById(R.id.state);
        country = (Spinner) findViewById(R.id.country);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                Countray_model myCountry;
                if(!(country.getSelectedItem() == null))
                {
                    myCountry = (Countray_model)country.getSelectedItem();
                    String t=myCountry.getId();
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
                if(!(state.getSelectedItem() == null))
                {

                   /* myCountry = (Countray_model)country.getSelectedItem();
                    player_country.setText(country.getSelectedItem().toString());
                    getstatedata(myCountry.getId());*/

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }

        });


    }
    @OnClick(R.id.partner_sign)
    public void Partner_sign(View view){
        if(isRegistrationDataOK()){

            if (!isNetworkAvailable()){
                Snackbar.make(view, "Network Not Available", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else {
              /*  Intent i = new Intent(Player_Signup.this, Player_login.class);
                startActivity(i);*/
                 registerUser();
            }
    }}

    public void getcountrydata (){
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();
        RequestQueue requestQueue = Volley.newRequestQueue(Host_SignUp.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://www.doubloon.media-mosaic.in/apis/get_country_name",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            pd.dismiss();
                            Toast.makeText(Host_SignUp.this, "No data found", Toast.LENGTH_LONG).show();
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
                                ArrayAdapter<Countray_model> myAdapter = new ArrayAdapter<Countray_model>(Host_SignUp.this, R.layout.country_text, myCountries);
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
                                ArrayAdapter<State_model> myAdapter = new ArrayAdapter<State_model>(Host_SignUp.this, R.layout.state_text, myState);
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    public boolean isRegistrationDataOK() {
        boolean isOk;
        if (owner_name.getText().toString().trim().equals("") || owner_name.getText().toString().equals("Name")) {
            owner_name.setError("Please Enter Name");
            isOk = false;
        } else {
            if (email_partner.getText().toString().trim().equals("") || email_partner.getText().toString().equals("Email")) {
                email_partner.setError("Please Enter Email");
                isOk = false;
            } else {

                if (country.getSelectedItem().toString().trim().equals("") ||country.getSelectedItem().toString().trim().equals("Country")) {
                    partner_country.setError("Please Enter Country");
                    settoast("Please Enter Country");
                    isOk = false;
                } else {
                    if (state.getSelectedItem().toString().trim().equals("") || state.getSelectedItem().toString().trim().equals("State")) {
                        partner_state.setError("Please Enter State");
                        settoast("Please Enter State");
                        isOk = false;
                    } else {

                        if (partner_city.getText().toString().trim().equals("") || partner_city.getText().toString().equals("City")) {
                            partner_city.setError("Please Enter city");
                            isOk = false;
                        } else {

                            if (confirm_password_partner.getText().toString().trim().equals("") || confirm_password_partner.getText().toString().equals("Password")) {
                                confirm_password_partner.setError("Please Enter Password");
                                isOk = false;
                            } else {
                                if (!confirm_password_partner.getText().toString().trim().equals(password_partner.getText().toString().trim())) {
                                    confirm_password_partner.setError("Password Not Matched");
                                    password_partner.setText("");
                                    confirm_password_partner.setText("");
                                    settoast("Password Not Matched .");

                                    isOk = false;
                                } else {
                                    if (!isPasswordValid(confirm_password_partner.getText().toString().trim())) {
                                        confirm_password_partner.setError("This password is too short");
                                        confirm_password_partner.setText("");
                                        password_partner.setText("");
                                        settoast("This password is too short ");
                                        isOk = false;
                                    } else {
                                        if (!isEmailValid(email_partner.getText().toString().trim())) {
                                            email_partner.setError(getString(R.string.error_invalid_email));
                                            settoast("This email address is invalid");
                                            isOk = false;
                                        } else {
                                            if(owner_busines_name.getText().toString().trim().equals("") || owner_busines_name.getText().toString().equals("Business Name")) {
                                                owner_busines_name.setError("Please Enter Business Name");
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
    public  boolean isEmailValid(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public  void settoast(String title){
        LayoutInflater li = getLayoutInflater();
        View layout = li.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text_toast);
        text.setText(title);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }


    private void registerUser(){
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/partnerSignup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Log.d("TAG@123",response);
                        System.out.println(response);
                        if (response != null) {
                            try {
                                JSONObject emp=new JSONObject(response);
                                settoast(emp.getJSONObject("Doubloon_app").getString("res_msg"));
                                savepref.saveuserId(Host_SignUp.this,emp.getJSONObject("Doubloon_app").getString("user_id"));
                                Intent i = new Intent(Host_SignUp.this, Host_login.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(i);

                            }catch (JSONException e)
                            {

                                settoast("Something happened Wrong Please try Again.");

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        String message="Anything happened Wrong Please try Again.";
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
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }
                        settoast(message);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("owner_name",owner_name.getText().toString().trim());
                params.put("business_name",owner_busines_name.getText().toString().trim());
                params.put("email",email_partner.getText().toString().trim());
                params.put("password",confirm_password_partner.getText().toString().trim());
                params.put("country_name",country.getSelectedItem().toString().trim());
                params.put("state_name",state.getSelectedItem().toString().trim());
                params.put("city_name",partner_city.getText().toString().trim());
                params.put("device_type","Android");
                System.out.println("params - "+ params.toString());
                Log.d("TAG@123", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
