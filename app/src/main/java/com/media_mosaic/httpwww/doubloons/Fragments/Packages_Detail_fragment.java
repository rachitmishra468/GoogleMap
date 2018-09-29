package com.media_mosaic.httpwww.doubloons.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.Uitl.PayPalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

public class Packages_Detail_fragment extends Fragment implements View.OnClickListener{


    View view;
    Button button;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            .acceptCreditCards(false)
            .merchantName("TEAM SPOTS");
    TextView total_cost_text,package_name_text,number_of_post,cost_text;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_packages__detail_fragment, container, false);
        total_cost_text=(TextView)view.findViewById(R.id.total_cost_text);
        package_name_text=(TextView)view.findViewById(R.id.package_name_text);
        number_of_post=(TextView)view.findViewById(R.id.number_of_post);
        cost_text=(TextView)view.findViewById(R.id.cost_text);
        String data=getArguments().getString("post_id");
        button=(Button)view.findViewById(R.id.partner_pay);
        button.setOnClickListener(this);
        total_cost_text.setText("$ "+getArguments().getString("cost"));
        package_name_text.setText(getArguments().getString("package_name"));
        number_of_post.setText(getArguments().getString("allowed_doubloons"));
        cost_text.setText("$ "+getArguments().getString("cost"));
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.partner_pay:
                getPayment();;
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    private void getPayment() {
        PayPalPayment payment = new PayPalPayment((new BigDecimal(getArguments().getString("cost"))), "GBP", "Daily Doubloons",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


}
