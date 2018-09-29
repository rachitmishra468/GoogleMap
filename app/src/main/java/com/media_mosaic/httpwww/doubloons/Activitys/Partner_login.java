package com.media_mosaic.httpwww.doubloons.Activitys;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.media_mosaic.httpwww.doubloons.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Partner_login extends AppCompatActivity {
@BindView(R.id.partner_login)
    Button partner_login;
    @BindView(R.id.title)
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_hadder);
        ButterKnife.bind(this);
        title.setText(R.string.partner_login);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.partner_login)
    public  void log_in(View view){
        Intent i = new Intent(Partner_login.this, Create_Doubloon.class);
        startActivity(i);

    }
}
