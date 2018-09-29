package com.media_mosaic.httpwww.doubloons.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.DB.SavePref;
import com.media_mosaic.httpwww.doubloons.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserTypeActivity extends AppCompatActivity {
    @BindView(R.id.player)
    Button Player;
    @BindView(R.id.partner)
    Button Partner;
    SavePref savepref;
    ReadPref readPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        savepref=new SavePref();
        readPref = new ReadPref(UserTypeActivity.this);
    }
    @OnClick(R.id.partner)
     public  void patrner_signup(View view) {

        if (readPref.getpartnerLoggedIn().equalsIgnoreCase("No")||readPref.getpartnerLoggedIn().equalsIgnoreCase("")) {
            savepref.saveusertype(UserTypeActivity.this,"patrner");
            Intent i = new Intent(UserTypeActivity.this, Host_login.class);
            startActivity(i);
        }
        else {
            savepref.saveusertype(UserTypeActivity.this,"patrner");
            Intent i = new Intent(UserTypeActivity.this, MapsActivity.class);
            startActivity(i);
        }

    }

    @OnClick(R.id.player)
    public  void Player_signup(View view) {
        if (readPref.getLoggedIn().equalsIgnoreCase("No")||readPref.getLoggedIn().equalsIgnoreCase("")) {

        savepref.saveusertype(UserTypeActivity.this,"player");
        Intent i = new Intent(UserTypeActivity.this, Player_login.class);
        startActivity(i);
        }
        else {
            savepref.saveusertype(UserTypeActivity.this,"player");
            Intent i = new Intent(UserTypeActivity.this, MapsActivity.class);
            startActivity(i);
        }
    }
}
