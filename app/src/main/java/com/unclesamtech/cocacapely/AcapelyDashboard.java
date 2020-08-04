package com.unclesamtech.cocacapely;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;


import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.unclesamtech.cocacapely.ui.main.SectionsPagerAdapter;

public class AcapelyDashboard extends AppCompatActivity {
    private Context c = AcapelyDashboard.this;
    private String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acapely_dashboard);
        userEmail = retrEmail("acapely_email_key",MODE_PRIVATE,"acapely_email_pref");
        getTv(R.id.title).setText(getString(R.string.welc) + " " + userEmail);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, " Acapely ");
                    String shareMessage= "\n Acapely inspiring millions with rhythm..Download using this link..\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share Acapely via "));
                } catch (Exception e) {
                    getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(),CocAcapelyConstants.TOAST_SHORT_LEN).show();
                }
            }

        });

    }

    private AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }

    public String retrEmail(String key,  int mode,String prefKey){
        SharedPreferences sharedPreferences = getSharedPreferences(prefKey,mode);
        //if(sharedPreferences.contains("user_church")){}
        return sharedPreferences.getString(key,null);
    }

    private TextView getTv(int ids){
        return findViewById(ids);
    }

}