package com.unclesamtech.cocacapely;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AcapelyCreateVideoPlaylist extends AppCompatActivity {
    private View v;
    private FirebaseFirestore fStore;
    private ConnectivityManager connMgr;
    private CollectionReference rhemaCollRef;
    private String posterName;
    private NetworkInfo networkInfo;
    private ProgressDialog pBar;
    private FirebaseAuth fAuth;
    public String senderId;
    private Task<Uri> urlTask;
    private Uri imgUri;
    private String imgPath;

    private UploadTask uploadTask;
    Context c = AcapelyCreateVideoPlaylist.this;
    private String downloadUrl;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acapely_create_video_playlist);
        try{

            actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setTitle("Video Playlist");
            }
            else{
                getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + "null action bar",CocAcapelyConstants.TOAST_LONG_LEN).show();
            }

            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //the NetworkInfo class gets the current state of the device network connection
            networkInfo = connMgr.getActiveNetworkInfo();
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            pBar = new ProgressDialog(c);
            posterName = retrPosterName("acap_post_name_key",MODE_PRIVATE,"acap_post_name_pref_key");
            getBut(R.id.btn_create_play_main).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //regPlayList(getUserId(),churchName,getEdt(R.id.edt_playlist_title).getText().toString().trim(),getEdt(R.id.edt_playlist_id).getText().toString().trim(),retSelectedPlayType(),"",churchTime);
                    if (!TextUtils.isEmpty(posterName) && !TextUtils.isEmpty(getEdt(R.id.edt_playlist_title).getText().toString().trim()) && !TextUtils.isEmpty(getEdt(R.id.edt_playlist_id).getText().toString().trim()) && !TextUtils.isEmpty("time")) {
                        try {

                            uploadImg(get_fire_storage(), posterName, getEdt(R.id.edt_playlist_title).getText().toString().trim(), getEdt(R.id.edt_playlist_id).getText().toString().trim(), getDateTime());


                        } catch (StorageException e) {
                            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
                        }
                    } else {
                        getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + "some values are missing", CocAcapelyConstants.TOAST_LONG_LEN).show();
                    }
                }
            });

            getImg(R.id.img_playlist_icon_create).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getLocalImgPath();
                }
            });

        }
        catch(NullPointerException np){
            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + np.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }
    }

    private AcapaleyAutoUtils getAuto(){
        return new AcapaleyAutoUtils();
    }

    public void regPlayList(String playlist_title, String playlist_id, String playlist_poster, String playlist_icon, String user_uid, String playlistTime, String playlistType){
        if(networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {
            rhemaCollRef = fStore.collection("acapely_collection");
            rhemaCollRef.document(playlistTime).set(new AcapelyMediaModel( playlist_title,playlist_id,playlist_poster,playlist_icon,user_uid,playlistTime,playlistType)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    getAuto().getToast(c,  " Playlist set successfully ", CocAcapelyConstants.TOAST_LONG_LEN).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_LONG_LEN).show();
                }
            });
        }
        else{
            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + networkInfo.getExtraInfo(), CocAcapelyConstants.TOAST_LONG_LEN).show();
        }

    }

    private Button getBut(int id){
        return findViewById(id);
    }

    public String getUserId(){
        if(fAuth != null){
            senderId = fAuth.getCurrentUser().getUid();
        }
        else{
            getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + " not valid ", CocAcapelyConstants.TOAST_LONG_LEN).show();
        }

        return senderId;
    }

    private TextInputEditText getEdt(int id){
        TextInputEditText editText = findViewById(id);
        return editText;
    }


    /**
     * This is used to get and instance of Firebase Storage
     * @return
     */
    public FirebaseStorage get_fire_storage(){
        FirebaseStorage fStorage = FirebaseStorage.getInstance();
        return fStorage;}



    public void getLocalImgPath() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, CocAcapelyConstants.GALLERY_REQUEST_CODE);

            //imgPath = galleryIntent.getData().getSchemeSpecificPart();
        } catch (NullPointerException np) {
            getAuto().getToast(c, "Error as a result of : " + np.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
        }
        // getAuto().getToast(c, "Selected Image path is : " + imgPath, CocAcapelyConstants.TOAST_SHORT_LEN).show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CocAcapelyConstants.GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                imgUri = data.getData();
                imgPath = data.getData().getSchemeSpecificPart();

                getImg(R.id.img_playlist_icon_create).setImageURI(imgUri);
                getAuto().getToast(c, "Path is : " + imgPath, CocAcapelyConstants.TOAST_LONG_LEN).show();
            } catch (NullPointerException np) {
                getAuto().getToast(c, "Error as a result of : " + np.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
            }
        }
    }

    public Uri getImgUri(){
        Uri upUri = imgUri;

        return upUri;
    }

    public String getChurchImgPath() {
        // getAuto().getToast(c, "Image Path for this is : " + imgPath, CocAcapelyConstants.TOAST_SHORT_LEN).show();
        return imgPath;
    }


    private ImageView getImg(int id){
        ImageView img = findViewById(id);
        return img;
    }


    public String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    /**
     * this is used to upload image
     *
     * @return
     */
    public String uploadImg(FirebaseStorage storage, final String poster_name, final String playlistTitle, final String playlistId,  final String createdTime) throws NullPointerException, StorageException {

        if(networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isConnected()) {

            pBar.show();
            pBar.setMessage("Please wait while we process your request");


            final StorageReference storeRef = storage.getReference(poster_name + "/").child(System.currentTimeMillis() + "." + getFileExtension(getImgUri()));


            uploadTask = storeRef.putFile(getImgUri());
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    try{
                        double progress = (100.0 * taskSnapshot.getBytesTransferred());
                        getAuto().getToast(c, "Upload is " + progress + "% done ", CocAcapelyConstants.TOAST_SHORT_LEN).show();
                    }
                    catch(NullPointerException np){
                        getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS +  np.getLocalizedMessage(),CocAcapelyConstants.TOAST_SHORT_LEN).show();
                    }
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    try{
                        getAuto().getToast(c, "Uplpoad is paused as a result of : " + taskSnapshot.getError().getLocalizedMessage(),CocAcapelyConstants.TOAST_SHORT_LEN).show();}
                    catch(NullPointerException np){
                        getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS,CocAcapelyConstants.TOAST_SHORT_LEN).show();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                getAuto().getToast(c, "Image Uploaded Successfully", CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                try{
                                    if(!TextUtils.isEmpty(getUserId()) && !TextUtils.isEmpty(poster_name) && !TextUtils.isEmpty(playlistTitle) && !TextUtils.isEmpty(downloadUrl) && !TextUtils.isEmpty(createdTime) && !TextUtils.isEmpty(playlistId)){
                                        regPlayList(playlistTitle,playlistId,poster_name,downloadUrl,getUserId(),createdTime,"Video Playlist");
                                        getEdt(R.id.edt_playlist_title).setText("");
                                        getEdt(R.id.edt_playlist_id).setText("");
                                        getImg(R.id.img_playlist_icon_create).setImageResource(R.drawable.ic_camera);



                                    }

                                    else{
                                        getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + " Unable to complete registration as a result of missing values",CocAcapelyConstants.TOAST_LONG_LEN).show();
                                    }

                                }

                                catch(NullPointerException np){
                                    getAuto().getToast(c,CocAcapelyConstants.ERROR_MESS + np.getLocalizedMessage(),CocAcapelyConstants.TOAST_SHORT_LEN).show();
                                }
                                pBar.dismiss();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(),CocAcapelyConstants.TOAST_LONG_LEN).show();
                            }
                        });

                    } else {
                        getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + task.getException().getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + e.getLocalizedMessage(), CocAcapelyConstants.TOAST_SHORT_LEN).show();
                }
            });


        }
        else{
            getAuto().getToast(c, CocAcapelyConstants.ERROR_MESS + networkInfo.getExtraInfo(),CocAcapelyConstants.TOAST_SHORT_LEN).show();
        }
        return downloadUrl;
    }

    public String getDateTime(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss", Locale.getDefault());


        return  sdf.format(new Date());

    }

    public String retrPosterName(String key,  int mode,String prefKey){
        SharedPreferences sharedPreferences = getSharedPreferences(prefKey,mode);
        //if(sharedPreferences.contains("user_church")){}
        return sharedPreferences.getString(key,null);
    }


}
