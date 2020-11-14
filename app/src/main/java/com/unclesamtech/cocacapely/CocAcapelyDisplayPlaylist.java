package com.unclesamtech.cocacapely;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class CocAcapelyDisplayPlaylist extends AppCompatActivity {
    private NetworkInfo networkInfo;
    private ConnectivityManager connMgr;
    private ProgressDialog pBar;
    private RecyclerView recyclerView;
    private Context c = CocAcapelyDisplayPlaylist.this;
    private RequestQueue requestQueue;
    private String playlist_id;
    private ArrayList<RhemaHiveYoutubeVideosModel> videoList;
    private RhemaYoutubeDisplayAdapter videoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coc_acapely_display_playlist);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //the NetworkInfo class gets the current state of the device network connection
        networkInfo = connMgr.getActiveNetworkInfo();
        playlist_id = retrPlayId("AcapelyPlayId",MODE_PRIVATE,"AcapelyPlayPrefId");
        //String API_URL  = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=";
        String API_URL  = "";
       // String YOUTUBE_API_KEY = "AIzaSyB3hpjfjylcprSwLoi-LZZ33EBF7hTSiC0";
        String YOUTUBE_API_KEY = "";
        String API_OTHERS = "&key=" + YOUTUBE_API_KEY + "&maxResults=50";
        String COMP_API_URL = API_URL + playlist_id + API_OTHERS;
        pBar = new ProgressDialog(c);
        requestQueue = Volley.newRequestQueue(c);
        videoList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_video_playing_display);
        videoAdapter = new RhemaYoutubeDisplayAdapter( videoList);

        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new AcapelyItemDecor2(c,LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(videoAdapter);
        recyclerView.addOnItemTouchListener(new AcapelyItemTouchListener(c, recyclerView, new AcapelyItemTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RhemaHiveYoutubeVideosModel rModel = videoList.get(position);
                // getAuto().getToast(getContext(),"position : " + position,RhemaHiveClassReferenceConstants.TOAST_SHORT_LEN).show();
                //intent.putExtra("RhemYoutube", videoList.get(position));
                pushVideoId( rModel.getVideoId(),"rhem_video_id",MODE_PRIVATE,"rhem_vid_id");
                startActivity(new Intent(c,AcapelyVideoPlayingActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        try {
            getUrl(COMP_API_URL);
        } catch (IOException e) {
            getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }

    }

    public void getUrl(String url) throws IOException {
        if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {
            pBar.show();
            pBar.setMessage("Getting your playlist ready.....");

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // JSONObject rootObject  = response.getJSONObject("data");
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject videoData = jsonArray.getJSONObject(i);

                            JSONObject video = videoData.getJSONObject("snippet");
                            String playlistid = video.getString("playlistId");//1
                            String videoId = video.getJSONObject("resourceId").getString("videoId");//2
                            String createdDate = video.getString("publishedAt");//3
                            String videoTitle = video.getString("title");//4
                            JSONObject thumbnail = video.getJSONObject("thumbnails");
                            JSONObject defaultUrl = thumbnail.getJSONObject("default");
                            String videoIcon = defaultUrl.getString("url");//5
                            String position = video.getString("position");
                            //int age = employee.getInt("age");
                            pBar.dismiss();
                            //getAuto().getToast(c," Details  : " + playlistid + "videoid" + videoId + " date " + createdDate + "video title " + videoTitle  + " icon " + videoIcon , RhemaHiveClassReferenceConstants.TOAST_LONG_LEN).show();
                            videoList.add(new RhemaHiveYoutubeVideosModel(videoTitle, videoIcon, videoId));
                            videoAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + error.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
                }
            });

            requestQueue.add(request);

        } else {
            getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + networkInfo.getExtraInfo(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }
    }

    private AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }

    public String retrPlayId(String key,  int mode,String prefKey){
        SharedPreferences sharedPreferences = getSharedPreferences(prefKey,mode);
        //if(sharedPreferences.contains("user_church")){}
        return sharedPreferences.getString(key,null);
    }

    public void pushVideoId( String value,String key , int mode, String prefkey){
        SharedPreferences shPref = getSharedPreferences(prefkey,mode);
        SharedPreferences.Editor edt = shPref.edit();
        edt.putString(key,value);
        edt.apply();
    }
}
