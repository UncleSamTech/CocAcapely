package com.unclesamtech.cocacapely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;

public class AcapelyTerms extends AppCompatActivity {
    private NetworkInfo networkInfo;
    private ConnectivityManager connMgr;
    private FirebaseAuth fAuth;
    private Context c = AcapelyTerms.this;
    CheckBox radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acapely_terms);
try{
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //the NetworkInfo class gets the current state of the device network connection
        networkInfo = connMgr.getActiveNetworkInfo();


        fAuth = FirebaseAuth.getInstance();

        getBtn(R.id.btn_click_cont).setVisibility(CocAcapelyConstants.GONE);
        radioButton = getRad(R.id.rhema_tc_radio);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton.isChecked()) {

                    getBtn(R.id.btn_click_cont).setVisibility(CocAcapelyConstants.VISIBLE);

                } else {
                    getBtn(R.id.btn_click_cont).setVisibility(CocAcapelyConstants.GONE);
                }

            }
        });
        getBtn(R.id.btn_click_cont).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting()) {
                    getAuto().getToast(c, " Congrats Hiver !", CocAcapelyConstants.TOAST_SHORT_LEN).show();

                        startActivity(new Intent(c, AcapelyLogin.class));


                }

                else{
                    getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS+ networkInfo.getExtraInfo(), CocAcapelyConstants.TOAST_LONG_LEN).show();
                }
            }
        });


        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isConnectedOrConnecting()) {
            loadTerms();
        } else {
            getAuto().getToast(c, "Oops ! No internet on Phone", CocAcapelyConstants.TOAST_SHORT_LEN).show();
        }
    } catch (Exception e) {
        getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
    }

}


    public TextView getTv(int id) {

        return findViewById(id);
    }

    private void loadTerms() {
        getTv(R.id.tc_tv_read_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1f4mf18xMhum_6zwpYt9P3NbHuElA7tyU0cgr1eEdqPA/edit?usp=sharing"));
                startActivity(browserIntent);
            }
        });
    }


    public Button getBtn(int id) {

        return findViewById(id);
    }

    public CheckBox getRad(int id) {
        return  findViewById(id);

    }
    
    private AcapaleyAutoUtils getAuto(){
    return new AcapaleyAutoUtils();
    }

}
