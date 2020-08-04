package com.unclesamtech.cocacapely;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AcapelyDisplayMusicPlay extends AppCompatActivity {
    private String playId;
    private String videoId;
    private String completeUrl;
    Context c = AcapelyDisplayMusicPlay.this;
    private RecyclerView recyclerView;

    private NetworkInfo networkInfo;
    private ConnectivityManager connMgr;
    private FirebaseAuth fAuth;
    private CollectionReference rhemaCollRef;
    private FirebaseFirestore fStore;
    private ProgressDialog pBar;
    private ArrayList<RhemaHiveYoutubeVideosModel> videoList;
    private RhemaYoutubeDisplayAdapter videoAdapter;
    private RequestQueue requestQueue;
    private String API_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acapely_display_music_play);
        try {


            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            requestQueue = Volley.newRequestQueue(c);

            // getAuto().getToast(c, "Playid : " + playId, RhemaHiveClassReferenceConstants.TOAST_LONG_LEN).show();

            //API_URL = RhemaHiveClassReferenceConstants.URL_WITHOUT_PLAYID + playId + RhemaHiveClassReferenceConstants.PLAY_KEY_AREA;



            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //the NetworkInfo class gets the current state of the device network connection
            networkInfo = connMgr.getActiveNetworkInfo();

            pBar = new ProgressDialog(c);
            //String church = retrChurch("user_church",MODE_PRIVATE,"church_name");


        } catch (Exception e) {
            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(),CocAcapelyConstants.TOAST_LONG_LEN).show();
        }

    }

    private AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }
}
