package com.media_mosaic.httpwww.doubloons.Activitys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.media_mosaic.httpwww.doubloons.DB.SavePref;
import com.media_mosaic.httpwww.doubloons.Fragments.ForgotFragment;
import com.media_mosaic.httpwww.doubloons.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Player_login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
@BindView(R.id.player_login)
    Button player_login;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.txtForgotPassword)
            TextView txtForgotPassword;
    Button btn_sign_out;
    @BindView(R.id.player_signup)
            Button player_signup;
    EditText player_email,player_password;
    private static final int SIGN_IN_CODE = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount account;
    Dialog mBottomSheetDialog;
    ProgressDialog pd;
    SavePref savepref;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;

    JSONObject response, profile_pic_data, profile_pic_url;
    CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_hadder);
        ButterKnife.bind(this);
        savepref=new SavePref();
        title.setText(R.string.player_login);
        txtForgotPassword.setPaintFlags(txtForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btn_sign_out=(Button)findViewById(R.id.btn_sign_out);
        player_email=(EditText)findViewById(R.id.player_email);
        player_password=(EditText)findViewById(R.id.player_password);
        ButterKnife.bind(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        btn_sign_out.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions( "public_profile email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(AccessToken.getCurrentAccessToken() != null){
                    getUserDetails(loginResult);
                }
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @OnClick(R.id.txtForgotPassword)
    public  void forgotpass(View view){
        title.setText("FORGOT PASSWORD");
        ForgotFragment fragment = new ForgotFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_main, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


    }
    @OnClick(R.id.player_signup)
    public  void gosignplayer(View view ){


        Intent i = new Intent(Player_login.this, Player_Signup.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.btn_sign_out:
                break;
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, SIGN_IN_CODE);
    }
    @OnClick(R.id.player_login)
    public  void Player_login(View view){
        if(LoginDataOK()){

            if (!isNetworkAvailable()){
                Snackbar.make(view, "Network Not Available", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else {
                playerLogin();
            }

        }



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       if(title.getText().toString().equals("FORGOT PASSWORD")){
           title.setText(R.string.player_login);
       }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG@123", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e("TAG@123", "display name: " + acct.getDisplayName());
            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String id="";
            Log.d("TAG@123", "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            show(email,personName,personPhotoUrl,id);

        } else {

            Log.d("TAG@123", "Signed out, show unauthenticated UI." + result.isSuccess());
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.d("TAG@123", "Status:" + status);
                        }


                    });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> optPenRes = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (optPenRes.isDone()) {
            Log.e("TAG@123", "Yayy!");
            GoogleSignInResult result = optPenRes.get();
            handleSignInResult(result);
        } else {
            optPenRes.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    public boolean LoginDataOK() {
        boolean isOk;
        if (player_email.getText().toString().trim().equals("") || player_email.getText().toString().equals("Email")) {
            player_email.setError("Please Enter Email");
            isOk = false;
        } else {
            if (player_password.getText().toString().trim().equals("") ) {
                player_password.setError("Please Enter Password");
                isOk = false;
            } else {
                if (!isEmailValid(player_email.getText().toString().trim())) {
                    player_email.setError(getString(R.string.error_invalid_email));
                    settoast("This Email  is invalid ");
                    isOk = false;
                } else {

                    isOk = true;

                }

            }
        }
        return isOk;
    }

    public  boolean isEmailValid(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    JSONObject json = response.getJSONObject();
                    System.out.println("Json data :"+json);
                    Log.d("TAG@123",json.toString());
                    if(json != null){
                        try {
                            profile_pic_data = new JSONObject(json.get("picture").toString());
                            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        show(json.get("email").toString(),json.get("name").toString(),profile_pic_url.getString("url"),json.get("id").toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture.width(120).height(120)");
        request.setParameters(parameters);
        request.executeAsync();


    }

    private void playerLogin(){
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/userLogin",
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
                                savepref.saveuserId(Player_login.this,emp.getJSONObject("Doubloon_app").getString("user_id"));
                                savepref.saveLoggedemail(Player_login.this,emp.getJSONObject("Doubloon_app").getString("email"));
                                savepref.saveLoggedname(Player_login.this,emp.getJSONObject("Doubloon_app").getString("username"));
                                savepref.saveLoggedIn(Player_login.this,"Yes");
                                savepref.saveusertype(Player_login.this,"player");
                                Intent i = new Intent(Player_login.this, MapsActivity.class);
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
                params.put("username",player_email.getText().toString().trim());
                params.put("password",player_password.getText().toString());
                System.out.println("params - "+ params.toString());
                Log.d("TAG@123", params.toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public  void show(final String email,final String name,String img,final String id ){

        {
            {
                pd = new ProgressDialog(Player_login.this);
                pd.setMessage("loading");
                pd.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/socialLogin",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.dismiss();
                                Log.d("TAG@123",response);
                                System.out.println(response);
                                if (response != null) {
                                    try {
                                        JSONObject emp=new JSONObject(response);
                                        settoast(emp.getString("res_msg"));
                                        savepref.saveuserId(Player_login.this,emp.getJSONObject("Doubloon_app").getString("user_id"));
                                        savepref.saveLoggedemail(Player_login.this,emp.getJSONObject("Doubloon_app").getString("email"));
                                        savepref.saveLoggedname(Player_login.this,emp.getJSONObject("Doubloon_app").getString("username"));
                                        savepref.saveLoggedIn(Player_login.this,"Yes");
                                        savepref.saveusertype(Player_login.this,"player");
                                        Intent i = new Intent(Player_login.this, MapsActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(i);

                                    }catch (JSONException e)
                                    {settoast("Something happened Wrong Please try Again.");}
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
                        params.put("email",email);
                        params.put("social_token_id",id);
                        params.put("name",name);
                        params.put("device_type","Android");
                        System.out.println("params - "+ params.toString());
                        Log.d("TAG@123", params.toString());
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(Player_login.this);
                requestQueue.add(stringRequest);
            }




        }
    }/*{
        LayoutInflater inflater = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.info_facebook, null);
        TextView player_name=(TextView)view.findViewById(R.id.player_name);
        TextView player_email=(TextView)view.findViewById(R.id.player_email);
        ImageView player_img=(ImageView)view.findViewById(R.id.player_img);
        Button partner_login_facebook=(Button)view.findViewById(R.id.partner_login_facebook);
        player_email.setText(email);
        player_name.setText(name);
        Picasso.with(this).load(img)
                .into(player_img);
        partner_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {mBottomSheetDialog.dismiss();
                    pd = new ProgressDialog(Player_login.this);
                    pd.setMessage("loading");
                    pd.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/socialLogin",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pd.dismiss();
                                    Log.d("TAG@123",response);
                                    System.out.println(response);
                                    if (response != null) {
                                        try {
                                            JSONObject emp=new JSONObject(response);
                                            settoast(emp.getString("res_msg"));
                                            savepref.saveuserId(Player_login.this,emp.getJSONObject("Doubloon_app").getString("user_id"));
                                            savepref.saveLoggedemail(Player_login.this,emp.getJSONObject("Doubloon_app").getString("email"));
                                            savepref.saveLoggedname(Player_login.this,emp.getJSONObject("Doubloon_app").getString("username"));
                                            savepref.saveLoggedIn(Player_login.this,"Yes");
                                            savepref.saveusertype(Player_login.this,"player");
                                            Intent i = new Intent(Player_login.this, MapsActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            startActivity(i);

                                        }catch (JSONException e)
                                        {settoast("Something happened Wrong Please try Again.");}
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
                            params.put("email",email);
                            params.put("social_token_id",id);
                            params.put("name",name);
                            params.put("device_type","Android");
                            System.out.println("params - "+ params.toString());
                            Log.d("TAG@123", params.toString());
                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Player_login.this);
                    requestQueue.add(stringRequest);
                }




            }
        });
        mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.CENTER);
        //mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        mBottomSheetDialog.show();






    }*/


}
