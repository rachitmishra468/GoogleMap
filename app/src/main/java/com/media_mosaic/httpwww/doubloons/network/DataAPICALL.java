package com.media_mosaic.httpwww.doubloons.network;
import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.media_mosaic.httpwww.doubloons.MyApplication;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Rachit on 31-10-2017.
 */

public class DataAPICALL {

    private static final String TAG= DataAPICALL.class.getSimpleName();
    private DataAppResponse groceryAppResponse;
    private Context context;


    public DataAPICALL(DataAppResponse groceryAppResponse) {
        this.groceryAppResponse = groceryAppResponse;
    }


    public DataAPICALL(Context context, DataAppResponse groceryAppResponse) {
        this.context = context;
        this.groceryAppResponse = groceryAppResponse;

    }

    public void sendData(int requestMehod, final String url, final HashMap<String, String> data, final String requestType)
    {StringRequest stringRequest = new StringRequest(requestMehod, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"*****"+requestType+"******************"+data+"*********"+url+"********************************************************");
                Log.d(TAG, response);
                groceryAppResponse.onResponse(response, requestType);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"##################Error##########Error#####s#######  "+requestType+"  ############# "+data+"  "+url+" ############################### ## ######");
                Log.d(TAG,"Error->"+error.toString());
                groceryAppResponse.onError(error.toString(),requestType);
            }
        }


        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG,""+data);
                return data;
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*30,(DefaultRetryPolicy.DEFAULT_MAX_RETRIES+4),
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy (retryPolicy);
            }
        };


        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }







}



