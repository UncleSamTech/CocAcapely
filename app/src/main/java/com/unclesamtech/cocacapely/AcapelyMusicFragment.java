package com.unclesamtech.cocacapely;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcapelyMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcapelyMusicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;

    private NetworkInfo networkInfo;
    private ConnectivityManager connMgr;
    private FirebaseAuth fAuth;
    private CollectionReference rhemaCollRef;
    private FirebaseFirestore fStore;
    private View v;
    private AcapelyMediaModel acapelyModel;
    private AcapelyMediaAdapter playListAdapter;
    private ArrayList<AcapelyMediaModel> rhemaPlayList;
    private ProgressDialog pBar;
    private String posterName;


    public AcapelyMusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcapelyMusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcapelyMusicFragment newInstance(String param1, String param2) {
        AcapelyMusicFragment fragment = new AcapelyMusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            v = inflater.inflate(R.layout.fragment_acapely_music, container, false);
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            posterName = retrEmail("acapely_email_key",MODE_PRIVATE,"acapely_email_pref");

            //requestQueue = Volley.newRequestQueue(getContext());

            connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            //the NetworkInfo class gets the current state of the device network connection
            networkInfo = connMgr.getActiveNetworkInfo();
            setHasOptionsMenu(true);
            pBar = new ProgressDialog(getContext());


            rhemaPlayList = new ArrayList<>();
            recyclerView = v.findViewById(R.id.rv_music_acap);


            playListAdapter = new AcapelyMediaAdapter(rhemaPlayList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new AcapelyItemDecoration(2, dpToPx(), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(playListAdapter);
            recyclerView.addOnItemTouchListener(new AcapelyItemTouchListener(getContext(), recyclerView, new AcapelyItemTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    acapelyModel = rhemaPlayList.get(position);
                    //pushPlayListId(rhemaHiveCreatePlayListModel.getPlaylist_id(),"RhemYoutubePlayId",MODE_PRIVATE,"RhemaPlayListId");
                    //getAuto().getToast(getContext(), "You clicked position " + position, RhemaHiveClassReferenceConstants.TOAST_LONG_LEN).show();
                    startActivity(new Intent(getContext(),CocAcapelyDisplayPlaylist.class));
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
            popVid();
            retrMusicPlayList();
            getFab(R.id.fab_create_music_playlist).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushPoster(posterName,"acap_post_name_key",MODE_PRIVATE,"acap_post_name_pref_key");
                    startActivity(new Intent(getContext(), AcapelyCreateMusicPlaylist.class));
                }
            });
        } catch (Exception e) {
            getAuto().getToast(getContext(),CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }

        return v;

    }

    private void popVid(){
        rhemaPlayList.add(new AcapelyMediaModel("Media Title","Samuel","2:34","","juggt56vhjjj"));
        rhemaPlayList.add(new AcapelyMediaModel("Music Title","Ekene","3:34","","juggt56vhjjj"));
        rhemaPlayList.add(new AcapelyMediaModel("Video Title","Okon","8:34","","juggt56vhjjj"));
        rhemaPlayList.add(new AcapelyMediaModel("Acapella Title","Joel","9:34","","juggt56vhjjj"));
        playListAdapter.notifyDataSetChanged();
    }

    private int dpToPx() {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }

    public void pushPoster( String value,String key , int mode, String prefkey){
        SharedPreferences shPref = getContext().getSharedPreferences(prefkey,mode);
        SharedPreferences.Editor edt = shPref.edit();
        edt.putString(key,value);
        edt.apply();
    }

    private AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }

    private FloatingActionButton getFab(int id){
        return v.findViewById(id);
    }

    private TextView getTv(int ids){
        return v.findViewById(ids);
    }

    private void retrMusicPlayList(){
        if(networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {
            rhemaCollRef = fStore.collection("acapely_collection");
            rhemaCollRef.whereEqualTo("playlistType","Music Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.getDocuments().isEmpty()){
                        for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                            if(d.exists()){
                                rhemaPlayList.add(new AcapelyMediaModel(d.getString("playlist_title"),d.getString("playlist_id"),d.getString("playlist_poster"),d.getString("playlist_icon"),d.getString("playlistTime")));
                                playListAdapter.notifyDataSetChanged();
                            }

                            else{
                                getAuto().getToast(getContext(), " Oops..we will upload some playlist soon ", CocAcapelyConstants.TOAST_LONG_LEN).show();
                            }
                        }
                    }
                    else{
                        getAuto().getToast(getContext(), " Oops..we will upload some playlist soon ", CocAcapelyConstants.TOAST_LONG_LEN).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getAuto().getToast(getContext(), e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
                }
            });
            //rhemaCollRef.document("music_playlist").get().addOnSuccessListener(new O)
        }

        else{
            getAuto().getToast(getContext(), CocAcapelyConstants.ERROR_MESS + networkInfo.getExtraInfo(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }
    }

    public String retrEmail(String key,  int mode,String prefKey){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(prefKey,mode);
        //if(sharedPreferences.contains("user_church")){}
        return sharedPreferences.getString(key,null);
    }

}
