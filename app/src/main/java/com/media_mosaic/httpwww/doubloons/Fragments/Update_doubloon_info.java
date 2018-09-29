package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Update_doubloon_info extends Fragment implements View.OnClickListener {

    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    View view;
    ReadPref readPref;
    EditText state_text, country_text, sponser_name, doubloon_name, flat_no, locality_text, city_text, price_text, number_person;
    private ProgressDialog pDialog;
    Button update_button;
    TextView add_img, add_video, upload, upload_img_video, title_text, steps_coun, expity_date, description_text, clues_text;
    private int mYear, mMonth, mDay;
    String post_type, Group_type, id = "";
    LinearLayout sponsored_lay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_doubloon_info, container, false);
        doubloon_name = (EditText) view.findViewById(R.id.doubloon_name);
        sponser_name = (EditText) view.findViewById(R.id.sponser_name);
        price_text = (EditText) view.findViewById(R.id.price_text);
        number_person = (EditText) view.findViewById(R.id.number_person);
        update_button = (Button) view.findViewById(R.id.update_button);
        expity_date = (TextView) view.findViewById(R.id.expity_date);
        clues_text = (TextView) view.findViewById(R.id.clues_text);
        description_text = (TextView) view.findViewById(R.id.description_text);
        sponsored_lay = (LinearLayout) view.findViewById(R.id.sponsored_lay);
        sponsored_lay.setVisibility(View.GONE);
        expity_date.setOnClickListener(this);
        clues_text.setOnClickListener(this);
        description_text.setOnClickListener(this);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        GetResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("post_id", getArguments().getString("post_id"));
        Log.d("TAG@123", params.toString());
        groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/postDetails", params, "item");
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                GetResponse();
                HashMap<String, String> params = new HashMap<>();
                params.put("id",id);
               // params.put("house_no", flat_no.getText().toString().trim());
              //  params.put("locality", locality_text.getText().toString().trim());
              //  params.put("city", city_text.getText().toString().trim());
             //   params.put("state", state_text.getText().toString().trim());
             //   params.put("country", country_text.getText().toString().trim());
                params.put("doubloons_name", doubloon_name.getText().toString().trim());
                params.put("expiry_date", expity_date.getText().toString().trim());
                params.put("description", description_text.getText().toString().trim());
                params.put("clues", clues_text.getText().toString().trim());
                params.put("price", price_text.getText().toString().trim());
                params.put("no_of_people", number_person.getText().toString().trim());
                params.put("post_type", post_type);
                params.put("group_type",Group_type);
                params.put("sponsor_name", sponser_name.getText().toString().trim());
                Log.d("TAG@123", params.toString());
                groceryAPICALL = new DataAPICALL(getActivity(), groceryAppResponse);
                groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/updatePost", params, "updatePost");
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void GetResponse() {
        groceryAppResponse = new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {
                pDialog.dismiss();
                if(responseType.equals("updatePost")){
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        settoast(jsonObject.getString("res_msg"));
                        Mydoubloons fragment = new Mydoubloons();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                    } catch (JSONException r) {
                    }
                }

                else {
                Log.d("TAG@123", response);
                String dhjdj = response.toString();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("Doubloon_app");
                    id= jsonObject1.getString("id");
                    doubloon_name.setText(jsonObject1.getString("name"));
                    price_text.setText(jsonObject1.getString("discount"));
                    number_person.setText(jsonObject1.getString("quantity"));
                    expity_date.setText(jsonObject1.getString("time_limit"));
                    clues_text.setText(jsonObject1.getString("clue"));
                    description_text.setText(jsonObject1.getString("description"));
                    post_type = jsonObject1.getString("post_type");
                    Group_type = jsonObject1.getString("group_type");
                    if (Group_type.equals("Sponsored")) {
                        sponsored_lay.setVisibility(View.VISIBLE);
                        sponser_name.setText(jsonObject1.getString("sponsor_name"));
                    }
                } catch (JSONException e) {

                }}

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

}
