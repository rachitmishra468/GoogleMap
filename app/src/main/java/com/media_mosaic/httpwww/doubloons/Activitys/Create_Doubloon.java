package com.media_mosaic.httpwww.doubloons.Activitys;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.media_mosaic.httpwww.doubloons.Data_Model.Countray_model;
import com.media_mosaic.httpwww.doubloons.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Create_Doubloon extends AppCompatActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.continue_button)
    Button continue_button;
    EditText doubloons_name,logo_text;
    TextView upload;
    Spinner post_type,group_type;
    LinearLayout sponser_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__doubloon);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_hadder);
        ButterKnife.bind(this);
      //  title.setText(R.string.create_doubloon);
       /* sponser_layout=(LinearLayout)findViewById(R.id.sponser_layout);
        sponser_layout.setVisibility(View.GONE);
                doubloons_name=(EditText)findViewById(R.id.doubloons_name);
        logo_text=(EditText)findViewById(R.id.logo_text);
        upload=(TextView)findViewById(R.id.upload);
        group_type= (Spinner) findViewById(R.id.group_type);
         post_type = (Spinner) findViewById(R.id.post_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, R.layout.spinner_item);
        post_type.setAdapter(adapter);
        ArrayAdapter<CharSequence> group_adapter = ArrayAdapter.createFromResource(this,
                R.array.group_type, R.layout.spinner_item);
        group_type.setAdapter(group_adapter);
        post_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
               if(post_type.getSelectedItem().toString().trim().equals("Sponsored")){
                   sponser_layout.setVisibility(View.VISIBLE);
               }
               else {
                   sponser_layout.setVisibility(View.GONE);
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }

        });*/


    }
   /* @OnClick(R.id.continue_button)
    public  void Continue(View view){
        Intent i = new Intent(Create_Doubloon.this, MapsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);

    }*/
}
