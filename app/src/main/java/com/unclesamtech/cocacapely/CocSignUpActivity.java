package com.unclesamtech.cocacapely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CocSignUpActivity extends AppCompatActivity {
    private NetworkInfo networkInfo;
    private ConnectivityManager connMgr;

    private String uuid;
    private Button btn;
    private AppCompatEditText emailEdt;
    private AppCompatEditText passEdt;
    private TextView textView;
    private int responseCode;
    private FirebaseUser user;
    private FirebaseAuth fAuth;
    private ProgressDialog pBar;
    private Context c = CocSignUpActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coc_sign_up);
        try {
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //the NetworkInfo class gets the current state of the device network connection
            networkInfo = connMgr.getActiveNetworkInfo();
            fAuth = FirebaseAuth.getInstance();
            pBar = new ProgressDialog(c);
            btn = getButton(R.id.sign_upreg_btn);
            textView = getTv(R.id.sign_inreg_lab);
            emailEdt = getAppEdit(R.id.email_sup_app_edt);
            passEdt = getAppEdit(R.id.pass_sup_app_edt);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(c,AcapelyLogin.class));
                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        signUpUser(emailEdt.getText().toString().trim(), passEdt.getText().toString());
                    } catch (NullPointerException np) {
                        getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + np.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                    }
                }
            });
        } catch (Exception e) {
            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }
    }









    private void signOutUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            getAuto().getToast(c, "We are about to sign you out", CocAcapelyConstants.TOAST_SHORT_LEN).show();
            try {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(c, "You have being sucessfully signed out", CocAcapelyConstants.TOAST_SHORT_LEN).show();
            } catch (NullPointerException fb) {
                Toast.makeText(c, "Unable to sign out as a result of " + fb.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(c, "You are already signed out", CocAcapelyConstants.TOAST_SHORT_LEN).show();
        }
    }

    /**
     * This method is used to get the user id
     * @return
     * @throws NullPointerException
     */
    public String getUserId() throws NullPointerException{


        if(fAuth != null){

            uuid = fAuth.getCurrentUser().getUid();

        }

        else{
            getAuto().getToast(c," Oops there is no id associated with this user " , CocAcapelyConstants.TOAST_SHORT_LEN).show();
        }

        return uuid;
    }



    private AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }



    private AppCompatEditText getAppEdit(int id) {
        AppCompatEditText appEdit = findViewById(id);
        return appEdit;
    }

    private Button getButton(int id) {
        Button btn = findViewById(id);
        return btn;
    }



    private void signUpUser(String email, String password) throws NullPointerException {
        if (checkFilledData(email, password) == 1) {


            if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {

                pBar.show();
                pBar.setMessage("Please wait while we process your request");
                getButton(R.id.sign_upreg_btn).setClickable(false);
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



                            getAuto().getToast(c,"Account Created Successfully", CocAcapelyConstants.TOAST_SHORT_LEN).show();

                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        getButton(R.id.sign_upreg_btn).setClickable(true);
                                        pBar.dismiss();
                                        getAuto().getToast(c, " Please verify email to continue.. Email have been sent to : " + fAuth.getCurrentUser().getEmail(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                        startActivity(new Intent(c,AcapelyLogin.class));
                                    } else {
                                        pBar.dismiss();
                                        getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + task.getException().getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                    }
                                }
                            });
                        } else {
                            pBar.dismiss();
                            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + task.getException().getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                        }
                    }
                });


            } else {
                pBar.dismiss();
                getAuto().getToast(c, "Oops ! ! No Internet Seen on Phone ", CocAcapelyConstants.TOAST_SHORT_LEN).show();
            }

        } else {
            pBar.dismiss();
            getAuto().getToast(c, "Oops ! !..Sorry you need to fill data properly", CocAcapelyConstants.TOAST_SHORT_LEN).show();
        }
    }


    private int checkFilledData(String email, String password) {
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            responseCode = -1;
            getAuto().getToast(c, " Oops ! !..Email  and Password are empty",CocAcapelyConstants.TOAST_SHORT_LEN).show();
        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            responseCode = 0;
            getAuto().getToast(c, " Oops ! !..Email  and Password are empty", CocAcapelyConstants.TOAST_SHORT_LEN).show();
        } else {
            responseCode = 1;
        }

        return responseCode;
    }


    private TextView getTv(int id){
        return  findViewById(id);

    }





}
