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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.Uitl.AppHelper;
import com.media_mosaic.httpwww.doubloons.Uitl.VolleyMultipartRequest;
import com.media_mosaic.httpwww.doubloons.Uitl.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class Create_new_Doubloons extends Fragment {
    private int PICK_IMAGE_REQUEST = 1;
    @BindView(R.id.title)
    TextView title;
    Button continue_button;
    EditText doubloons_name, logo_text;
    TextView upload,select_logo;
    String filePath="";
    Spinner post_type, group_type, steps_type;
    LinearLayout sponser_layout, step_layout;
    private static final int STORAGE_PERMISSION_CODE = 123;
    View view;
    ImageView mAvatarImage;
    private ProgressDialog pDialog;
    String logo_name="";
    boolean upload_logo=true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_new__doubloons, container, false);
        ButterKnife.bind(getActivity());
        requestStoragePermission();
        mAvatarImage=(ImageView)view.findViewById(R.id.mAvatarImage);
        continue_button = (Button) view.findViewById(R.id.continue_button);
        sponser_layout = (LinearLayout) view.findViewById(R.id.sponser_layout);
        step_layout = (LinearLayout) view.findViewById(R.id.step_layout);
        sponser_layout.setVisibility(View.GONE);
        doubloons_name = (EditText) view.findViewById(R.id.doubloons_name);
        logo_text = (EditText) view.findViewById(R.id.logo_text);
        upload=(TextView) view.findViewById(R.id.upload);
        select_logo= (TextView) view.findViewById(R.id.select_logo);
        group_type = (Spinner) view.findViewById(R.id.group_type);
        post_type = (Spinner) view.findViewById(R.id.post_type);
        steps_type = (Spinner) view.findViewById(R.id.steps_type);
        ArrayAdapter<CharSequence> stepadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.step_type, R.layout.spinner_item);
        steps_type.setAdapter(stepadapter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.planets_array, R.layout.spinner_item);
        post_type.setAdapter(adapter);
        ArrayAdapter<CharSequence> group_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.group_type, R.layout.spinner_item);
        group_type.setAdapter(group_adapter);
        group_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (group_type.getSelectedItem().toString().trim().equals("Sponsored")) {
                    sponser_layout.setVisibility(View.VISIBLE);
                    upload_logo=false;

                } else {
                    upload_logo=true;
                    sponser_layout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        post_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (post_type.getSelectedItem().toString().trim().equals("Multi")) {
                    step_layout.setVisibility(View.VISIBLE);
                } else {
                    step_layout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(upload_logo){
                    if (DataOK()) {
                        Create_Single_doubllon_fragment fragment = new Create_Single_doubllon_fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Post_type", post_type.getSelectedItem().toString().trim());
                        bundle.putString("Doubloons_name", doubloons_name.getText().toString().trim());
                        bundle.putString("Doubloons_step", steps_type.getSelectedItem().toString().trim());
                        bundle.putString("group_type", group_type.getSelectedItem().toString().trim());
                        bundle.putString("logo",logo_name);
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                    }
                }else {
                    settoast("Please Upload Logo");
                }

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                uploadlogo();
            }
        });

        select_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });
        return view;
    }

    public boolean DataOK() {
        boolean isOk;
        if (doubloons_name.getText().toString().trim().equals("") || doubloons_name.getText().toString().equals("Email")) {
            doubloons_name.setError("Please Enter  Doubloons Name");
            isOk = false;
        } else {
            if (post_type.getSelectedItem().toString().trim().equals("Group Type")) {
                Toast.makeText(getActivity(), "Please Select Group Type", Toast.LENGTH_SHORT).show();
                isOk = false;
            } else {
                if (group_type.getSelectedItem().toString().trim().equals("Post Type")) {
                    Toast.makeText(getActivity(), "Please Select Post Type", Toast.LENGTH_SHORT).show();
                    isOk = false;
                } else {
                    if (post_type.getSelectedItem().toString().trim().equals("Multi")) {
                        if (steps_type.getSelectedItem().toString().trim().equals("Steps")) {
                            Toast.makeText(getActivity(), "Please Select Doubloons Steps", Toast.LENGTH_SHORT).show();
                            isOk = false;
                        } else {
                            isOk = true;
                        }
                    } else {
                        isOk = true;
                    }
                }

            }
        }
        return isOk;
    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    @OnClick(R.id.continue_button)
    public void Continue(View view) {
        Create_Single_doubllon_fragment fragment = new Create_Single_doubllon_fragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.map, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = picUri.toString();
            logo_text.setText(filePath);
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                mAvatarImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
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
