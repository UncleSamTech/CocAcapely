package com.unclesamtech.cocacapely;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AcapelyLogin extends AppCompatActivity implements AcapelyForgotPasswordAdapter.RhemaHiveForgotPasswordListener  {

    private Context c = AcapelyLogin.this;
    int responseCode;
    private NetworkInfo networkInfo;
    private ConnectivityManager connMgr;
    private List<AuthUI.IdpConfig> providers;
    private AuthUI.IdpConfig fbUi;
    private String uuid;
    private AuthUI.IdpConfig twitUi;
    private AuthUI.IdpConfig emailUi;
    private AuthUI.IdpConfig gogUi;
    private FirebaseAuth fAuth;
    private CallbackManager callbackManager;
    ImageButton twitButton;
    ImageButton fbImg;
    private Bundle ui_bund;
    private TextView forPassTv;
    private Bundle church_bund;
    private ProgressDialog progressDialog;
    ImageButton googBut;
    private LoginButton loginButton;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acapely_login);
        try {
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //the NetworkInfo class gets the current state of the device network connection
            networkInfo = connMgr.getActiveNetworkInfo();

            callbackManager = CallbackManager.Factory.create();
            fAuth = FirebaseAuth.getInstance();
            progressDialog = new ProgressDialog(c);

            loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList(EMAIL));

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    getAuto().getToast(c, "Result  " + loginResult.toString(), CocAcapelyConstants.TOAST_LONG_LEN).show();
                }

                @Override
                public void onCancel() {
                    // App code
                    getAuto().getToast(c," login cancelled ", CocAcapelyConstants.TOAST_LONG_LEN).show();
                }

                @Override
                public void onError(FacebookException exception) {
                 getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + exception.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
                }
            });

            if(checkLoginSess()){
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            }

            getImgBut(R.id.twit_ui).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadTwitAuth();


                }
            });

            getImgBut(R.id.goog_ui).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadGoogAuth();
                }
            });

            getTv(R.id.sign_up_lab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(c, CocSignUpActivity.class));
                }
            });

            forPassTv = getTv(R.id.forget_pass_label);
            forPassTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });

            getBut(R.id.sign_up_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInUser(getEdt(R.id.email_app_edt_log).getText().toString().trim(),getEdt(R.id.pass_app_edt_log).getText().toString().trim());
                }
            });

        } catch (Exception e) {
            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }




    }

    public void showDialog(){
        DialogFragment dialogFragment = new AcapelyForgotPasswordAdapter();
        dialogFragment.show(getSupportFragmentManager(),"Forgot Password Dialog");

    }



    private void signInUser(String email, String pass) throws  NullPointerException {
        if (checkFilledData(email, pass) == 1) {
            if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {


                progressDialog.show();
                progressDialog.setMessage("Please wait while we verify your details");
                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (fAuth.getCurrentUser() != null && fAuth.getCurrentUser().isEmailVerified()) {
                                getAuto().getToast(c, "Welcome Hiver ! ! Logged in with email " + fAuth.getCurrentUser().getEmail(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                String email = fAuth.getCurrentUser().getEmail();
                                loginUsers(email);
                                //authenticateUser(returnUid(),email,retrPhone);

                                //startActivity(getAuto().newActivityStarter(c,RhemaHiveDecisionActivity.class));
                                progressDialog.dismiss();




                            }
                            else {
                                getAuto().getToast(c, "Oops ! !  Sorry your email havent been verified", CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                progressDialog.dismiss();
                            }

                        } else {
                            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + task.getException().getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
            else {
                getAuto().getToast(c, "Oops ! ! No Internet seen on Phone", CocAcapelyConstants.TOAST_SHORT_LEN).show();
            }
        }
    }


    /**
     * This method is used to send a forgot password email
     * @param email
     */
    public void forgotPassword(final String email){




        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            getAuto().getToast(c,"Password reset link has been sent to : " + email,CocAcapelyConstants.TOAST_SHORT_LEN).show();
                        }
                    }
                });



    }



    private int checkFilledData(String email, String password) {
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            responseCode = -1;
            getAuto().getToast(c, " Oops ! !..Email  and Password are empty", CocAcapelyConstants.TOAST_SHORT_LEN).show();
        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            responseCode = 0;
            getAuto().getToast(c, " Oops ! !..Email  and Password are empty", CocAcapelyConstants.TOAST_SHORT_LEN).show();
        } else {
            responseCode = 1;
        }

        return responseCode;
    }



    /**
     * This method is used to return facebook UI
     *
     * @return
     */
    public List<AuthUI.IdpConfig> getFbProvider() {
        providers = Collections.singletonList(getFbUi());
        return providers;
    }

    /**
     * This method is used to return facebook UI
     *
     * @return
     */
    public List<AuthUI.IdpConfig> getEmailProvider() {
        providers = Collections.singletonList(getEmailUi());
        return providers;
    }

    /**
     * This method is used to get facebook Ui
     *
     * @return
     */
    public List<AuthUI.IdpConfig> getTwitterProvider() {
        providers = Collections.singletonList(getTwitUi());
        return providers;
    }

    /**
     * This method is used to get Google provider
     *
     * @return
     */
    public List<AuthUI.IdpConfig> getGoogProvider() {
        providers = Collections.singletonList(getGoogUi());
        return providers;
    }

    /**
     * This method is used to run and return AuthUI.IdpConfig providers which is all providers
     *
     * @return
     */
    public List<AuthUI.IdpConfig> getProviders() {

        providers = Arrays.asList(
                //getEmailUi(),
                getGoogUi(),
                //getFbUi(),
                getTwitUi());


        return providers;
    }


    public void loadFbAuth() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(getFbProvider()).setLogo(R.drawable.acapely_loggo).setTheme(R.style.AppTheme_NoActionBar2).build(), CocAcapelyConstants.RC_SIGN_IN_ID);
    }


    /**
     * this method loads twitter authentication
     */
    public void loadTwitAuth() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(getTwitterProvider()).setLogo(R.drawable.acapely_loggo).setTheme(R.style.AppTheme_NoActionBar2).build(), CocAcapelyConstants.RC_SIGN_IN_ID);
        //authenticateTwitter();
        decideTwittLoginToUse();

    }


    public void decideTwittLoginToUse(){
        // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(fAuth.getCurrentUser() != null){
            authTwitterWithExistingProvider();
        }
        else{
              authenticateTwitter();
        }
    }





    public void loadGoogAuth() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(getGoogProvider()).setLogo(R.drawable.acapely_loggo).setTheme(R.style.AppTheme_NoActionBar2).build(), CocAcapelyConstants.RC_SIGN_IN_ID);
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


    public AuthUI.IdpConfig getFbUi() {
        fbUi = new AuthUI.IdpConfig.FacebookBuilder().build();
        return fbUi;
    }


    public AuthUI.IdpConfig getTwitUi() {
        twitUi = new AuthUI.IdpConfig.TwitterBuilder().build();
        return twitUi;
    }

    public AuthUI.IdpConfig getEmailUi() {
        emailUi = new AuthUI.IdpConfig.EmailBuilder().build();
        return emailUi;
    }

    public AuthUI.IdpConfig getGoogUi() {
        gogUi = new AuthUI.IdpConfig.GoogleBuilder().build();
        return gogUi;
    }




    private TextInputEditText getEdt(int id){
        return findViewById(id);
    }

    private TextView getTv(int id){
        return findViewById(id);

    }

    private Button getBut(int ids){
        return findViewById(ids);
    }

    private ImageButton getImgBut(int id){
        return findViewById(id);
    }

    private AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }

    public String retrHash(String key,  int mode,String prefKey){
        SharedPreferences sharedPreferences = getSharedPreferences(prefKey,mode);
        //if(sharedPreferences.contains("user_church")){}
        return sharedPreferences.getString(key,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {

            if (requestCode ==CocAcapelyConstants.RC_SIGN_IN_ID) {
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    try{
                        String email = response.getEmail();
                        if(!TextUtils.isEmpty(email)){
                        loginUsers(email);}
                        else{
                            loginUsers("example@domain.com");
                            getAuto().getToast(c, " email missing ",CocAcapelyConstants.TOAST_LONG_LEN).show();
                        }

                    }catch(NullPointerException np){
                        getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + np.getLocalizedMessage(),CocAcapelyConstants.TOAST_LONG_LEN).show();
                    }
                }
            }
        }
    }


    public void pushEmail( String value,String key , int mode, String prefkey){
        SharedPreferences shPref = getSharedPreferences(prefkey,mode);
        SharedPreferences.Editor edt = shPref.edit();
        edt.putString(key,value);
        edt.apply();
    }

    private void loginUsers(String email){
        if(!TextUtils.isEmpty(email)){
        getAuto().getToast(c, "login successful for user " + email, CocAcapelyConstants.TOAST_LONG_LEN).show();
        pushEmail(email,"acapely_email_key",MODE_PRIVATE,"acapely_email_pref");
        startActivity(new Intent(c,AcapelyDashboard.class));}
        else{
            getAuto().getToast(c, "login successful for user " + email, CocAcapelyConstants.TOAST_LONG_LEN).show();
            pushEmail("example@domain.com","acapely_email_key",MODE_PRIVATE,"acapely_email_pref");
            startActivity(new Intent(c,AcapelyDashboard.class));

    }

    }




    public void authTwitterWithExistingProvider(){
        if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {
            final OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
            // Target specific email with login hint.
            //provider.addCustomParameter("lang", "fr");

            Task<AuthResult> pendingResultTask = fAuth.getPendingAuthResult();
            if (pendingResultTask != null) {
                pendingResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        try {
                            String twit_uname = authResult.getAdditionalUserInfo().getUsername();
                            // String uid = getUserId();
                            // ui_bund = new Bundle();
                            //ui_bund.putString("uuid",uid);
                            //Intent intent  = new Intent(c, RhemaHiveDecisionActivity.class);
                            //intent.putExtras(intent);
                            //startActivity(getAuto().newActivityStarter());
                            getAuto().getToast(c, "User is already signed in as : " + twit_uname, CocAcapelyConstants.TOAST_SHORT_LEN).show();
                            String email = authResult.getUser().getDisplayName();
                            if(!TextUtils.isEmpty(email)){

                                getAuto().getToast(c, " Authentication Sucessful Welcome Acapeler ! ", CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                //startActivity(RhemaHiveInstanceManagerClass.getRhemaHiveAutoUtilsClass().newActivityStarter(c, RhemaHiveDecisionActivity.class));
                                loginUsers(email);
                            }

                            else{
                                loginUsers("example@domain.com");
                                getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + " null values ", CocAcapelyConstants.TOAST_LONG_LEN).show();
                            }
                            //startActivity(getAuto().newActivityStarter(c , RhemaHiveDecisionActivity.class));
                        } catch (NullPointerException np) {
                            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + np.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                    }
                });
            } else {
                final FirebaseUser userFb = fAuth.getCurrentUser();
                try {
                    //userFb.s
                    userFb.startActivityForLinkWithProvider(/* activity= */ this, provider.build())
                            .addOnSuccessListener(
                                    new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            //getAuto().getToast(c, "User : " + userFb.getDisplayName() + " is signed in on twitter as : " + authResult.getAdditionalUserInfo().getUsername().toString(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                            //startActivity(getAuto().newActivityStarter(c, RhemaHiveDecisionActivity.class));
                                            String email = authResult.getUser().getDisplayName();
                                            if(!TextUtils.isEmpty(email) ){

                                                getAuto().getToast(c, " Authentication Sucessful Welcome Acapeler ! ", CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                                //startActivity(RhemaHiveInstanceManagerClass.getRhemaHiveAutoUtilsClass().newActivityStarter(c, RhemaHiveDecisionActivity.class));
                                                loginUsers(email);
                                            }

                                            else{
                                                loginUsers("example@domain.com");
                                                getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + " null values ", CocAcapelyConstants.TOAST_LONG_LEN).show();
                                            }

                                            // Twitter credential is linked to the current user.
                                            // IdP data available in
                                            // authResult.getAdditionalUserInfo().getProfile().
                                            // The OAuth access token can also be retrieved:
                                            // authResult.getCredential().getAccessToken().
                                            // The OAuth secret can be retrieved by calling:
                                            // authResult.getCredential().getSecret().
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle failure.
                                            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                        }
                                    });

                } catch (NullPointerException np) {
                    getAuto().getToast(c, "error as a result of :  " + np.getLocalizedMessage().toString(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                }
            }


        }
        else{
            getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + networkInfo.getReason(),CocAcapelyConstants.TOAST_SHORT_LEN).show();
        }

    }


    public void authenticateTwitter(){
        if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {
            final OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
            // Target specific email with login hint.
            //provider.addCustomParameter("lang", "fr");

            final Task<AuthResult> pendingResultTask = fAuth.getPendingAuthResult();
            if (pendingResultTask != null) {
                pendingResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        try {
                            // String twit_uname = authResult.getAdditionalUserInfo().getUsername();
                            //startActivity(getAuto().newActivityStarter(c,RhemaHiveDecisionActivity.class));
                            //getAuto().getToast(c,"Welcome Hiver ! ",CocAcapelyConstants.TOAST_SHORT_LEN).show();
                            String email = pendingResultTask.getResult().getUser().getDisplayName();
                            if(!TextUtils.isEmpty(email)){

                                getAuto().getToast(c, " Authentication Sucessful Welcome Acapeler ! ", CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                //startActivity(RhemaHiveInstanceManagerClass.getRhemaHiveAutoUtilsClass().newActivityStarter(c, RhemaHiveDecisionActivity.class));
                               // authenticateUser(returnUid(),email,retrPhone);
                                loginUsers(email);
                            }

                            else{
                                loginUsers("example@domain.com");
                                getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + " null values ", CocAcapelyConstants.TOAST_LONG_LEN).show();
                            }

                        } catch (NullPointerException np) {
                            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + np.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                    }
                });
            } else {

                fAuth
                        .startActivityForSignInWithProvider(/* activity= */ this, provider.build())
                        .addOnSuccessListener(
                                new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        try {


                                            //startActivity(getAuto().newActivityStarter(c,RhemaHiveDecisionActivity.class));
                                            String email = authResult.getUser().getDisplayName();
                                            if(!TextUtils.isEmpty(email)){

                                                getAuto().getToast(c, " Authentication Sucessful Welcome Hiver ! ", CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                                //startActivity(RhemaHiveInstanceManagerClass.getRhemaHiveAutoUtilsClass().newActivityStarter(c, RhemaHiveDecisionActivity.class));
                                                loginUsers(email);
                                            }

                                            else{
                                                loginUsers(email);
                                                getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + " null values ", CocAcapelyConstants.TOAST_LONG_LEN).show();
                                            }
                                            //getAuto().getToast(c, "Start Registration ", CocAcapelyConstants.TOAST_SHORT_LEN).show();

                                        } catch (NullPointerException np) {

                                            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + np.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();


                                        }
                                        // User is signed in.
                                        // IdP data available in
                                        // authResult.getAdditionalUserInfo().getProfile().
                                        // The OAuth access token can also be retrieved:
                                        // authResult.getCredential().getAccessToken().
                                        // The OAuth secret can be retrieved by calling:
                                        // authResult.getCredential().getSecret().
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure.
                                        getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage().toString(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                    }
                                });
            }

        }

        else{
            getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + networkInfo.getReason(),CocAcapelyConstants.TOAST_SHORT_LEN).show();
        }
    }

    private boolean checkLoginSess(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        return isLoggedIn;
    }


    @Override
    public void onDialogPositiveClick(String email) {
        if(!TextUtils.isEmpty(email)){
            forgotPassword(email);
        }
        else{
            getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + " no email seen ", CocAcapelyConstants.TOAST_LONG_LEN).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
            dialogFragment.dismiss();
    }
}
