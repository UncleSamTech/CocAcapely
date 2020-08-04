package com.unclesamtech.cocacapely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
int status = 0;
private Context c = MainActivity.this;
    private NetworkInfo networkInfo;
    private ConnectivityManager connMgr;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //the NetworkInfo class gets the current state of the device network connection
        networkInfo = connMgr.getActiveNetworkInfo();


        fAuth = FirebaseAuth.getInstance();
        executeAcapelyDelayThread(c);

    }

    /**
     * This method is used to run a delayed thread on any activity it is called on
     * @param context
     *
     * @return
     */
    public void executeAcapelyDelayThread(final Context context){

        new Thread(new Runnable() {
            public void run() {
                while (status < 100) {
                    status += 5;

                    try {

                        Thread.sleep(200);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(fAuth.getCurrentUser() != null)startActivity(new Intent(context, AcapelyLogin.class));
                else startActivity(new Intent(context, AcapelyTerms.class));


            }
        }).start();


    }

/*
    public String printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    CocAcapelyConstants.PACKAGE_NAME,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);

            }
        } catch (PackageManager.NameNotFoundException e) {
            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        } catch (NoSuchAlgorithmException e) {
            getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(),CocAcapelyConstants.TOAST_LONG_LEN).show();
        }

        return keyHash;
    }

    String keyHash;*/


    public void pushHash( String value,String key , int mode, String prefkey){
        SharedPreferences shPref = getSharedPreferences(prefkey,mode);
        SharedPreferences.Editor edt = shPref.edit();
        edt.putString(key,value);
        edt.apply();
    }

    private AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }

}
