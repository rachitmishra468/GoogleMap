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

public class Partner_SignUp extends AppCompatActivity {
@BindView(R.id.partner_sign)
    Button Partner_sign;
@BindView(R.id.title)
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner__sign_up);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_hadder);
        ButterKnife.bind(this);
        title.setText(R.string.partner_sign_up);
    }
    @OnClick(R.id.partner_sign)
    public void Partner_sign(View view){
        Intent i = new Intent(Partner_SignUp.this, Partner_login.class);
        startActivity(i);
    }

}
