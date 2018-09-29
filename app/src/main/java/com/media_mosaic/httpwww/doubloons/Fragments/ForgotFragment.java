package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.media_mosaic.httpwww.doubloons.Activitys.UserTypeActivity;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotFragment extends Fragment {
ReadPref readPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_forgot, container, false);
        Button Forgot_submit=(Button)v.findViewById(R.id.Forgotbtn_login);
        final EditText ForgotEmail=(EditText)v.findViewById(R.id.Forgot_input_email);
        Forgot_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmailValid(ForgotEmail.getText().toString().trim())){
                    ForgotEmail.setError(getString(R.string.error_invalid_email));
                    Toast.makeText(getActivity(), R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
                }
                else {
                    forgotpassword(ForgotEmail.getText().toString().trim());
                }
            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
      //  ((Home)getActivity()).show_hide_title();;
    }

    public  boolean isEmailValid(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public  void popup(String mess){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.app_name);
        builder.setCancelable(false);
        builder.setMessage(mess);
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("OK", null);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });

        builder.show();
    }

    private void forgotpassword(final String username){
      final ProgressDialog pd;
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait..");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/forgotPassword",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        System.out.println(response);
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        if (response != null) {
                            String log=response.toString();
                            Log.d("TAG@123",log);

                            try {
                                JSONObject emp=new JSONObject(response);
                                popup(emp.getJSONObject("Doubloon_app").getString("res_msg"));
                            }
                            catch (JSONException e)
                            {



                                Toast.makeText(getActivity(),"Anything happened Wrong Please try Again.",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email",username);
              //  params.put("user_type",readPref.getusertype());
                Log.d("TAG@123", params.toString());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
