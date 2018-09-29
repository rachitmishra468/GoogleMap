package com.media_mosaic.httpwww.doubloons.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.media_mosaic.httpwww.doubloons.DB.SavePref;
import com.media_mosaic.httpwww.doubloons.Fragments.ForgotFragment;
import com.media_mosaic.httpwww.doubloons.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Host_login extends AppCompatActivity {
    @BindView(R.id.partner_login)
    Button partner_login;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.txtForgotPassword)
    TextView txtForgotPassword;
    @BindView(R.id.partner_signup)
            Button partner_signup;
    EditText partner_email,partner_password;
    ProgressDialog pd;
    SavePref savepref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_hadder);
        ButterKnife.bind(this);
        title.setText(R.string.partner_login);
        txtForgotPassword.setPaintFlags(txtForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        savepref=new SavePref();
        ButterKnife.bind(this);
        partner_email=(EditText)findViewById(R.id.partner_email);
        partner_password=(EditText)findViewById(R.id.partner_password);
    }

    @OnClick(R.id.txtForgotPassword)
    public  void  forgotpassword(View view){
        title.setText("FORGOT PASSWORD");
        ForgotFragment fragment = new ForgotFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.partner_login_main, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


    }
    @OnClick(R.id.partner_signup)
    public void gosignup(View view){
        Intent i = new Intent(Host_login.this, Host_SignUp.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
    @OnClick(R.id.partner_login)
    public  void log_in(View view){
        if(LoginDataOK()){

            if (!isNetworkAvailable()){
                Snackbar.make(view, "Network Not Available", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else {
                partnerLogin();
            }

        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(title.getText().toString().equals("FORGOT PASSWORD")){
            title.setText(R.string.partner_login);
        }

    }

    public boolean LoginDataOK() {
        boolean isOk;
        if (partner_email.getText().toString().trim().equals("") || partner_email.getText().toString().equals("Email")) {
            partner_email.setError("Please Enter Email");
            isOk = false;
        } else {
            if (partner_password.getText().toString().trim().equals("") ) {
                partner_password.setError("Please Enter Password");
                isOk = false;
            } else {
                if (!isEmailValid(partner_email.getText().toString().trim())) {
                    partner_email.setError(getString(R.string.error_invalid_email));
                    settoast("This Email  is invalid ");
                    isOk = false;
                } else {

                    isOk = true;

                }

            }
        }
        return isOk;
    }
    private void partnerLogin(){
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/partnerLogin",
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
                                savepref.saveuserId(Host_login.this,emp.getJSONObject("Doubloon_app").getString("user_id"));
                                savepref.saveLoggedemail(Host_login.this,emp.getJSONObject("Doubloon_app").getString("email"));
                                savepref.saveLoggedname(Host_login.this,emp.getJSONObject("Doubloon_app").getString("username"));
                                savepref.savepartnerLoggedIn(Host_login.this,"Yes");
                                savepref.saveusertype(Host_login.this,"patrner");
                                Intent i = new Intent(Host_login.this, MapsActivity.class);
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
                params.put("username",partner_email.getText().toString().trim());
                params.put("password",partner_password.getText().toString());
                System.out.println("params - "+ params.toString());
                Log.d("TAG@123", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
    public  boolean isEmailValid(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
