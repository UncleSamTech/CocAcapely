package com.unclesamtech.cocacapely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RhemaYoutubeDisplayAdapter extends RecyclerView.Adapter<RhemaYoutubeDisplayAdapter.RhemaHiveYoutubeDisplayViewHolder> implements Filterable {

        ArrayList<RhemaHiveYoutubeVideosModel> rhemaHiveYoutList;
        RhemaHiveYoutubeVideosModel rhemaHiveYoutubeVideosModel;
        ArrayList<RhemaHiveYoutubeVideosModel> rhemaHiveYouFilteredList;
private View v;

public RhemaYoutubeDisplayAdapter(ArrayList<RhemaHiveYoutubeVideosModel> rhemaHiveYoutList){
        this.rhemaHiveYoutList=rhemaHiveYoutList;
        this.rhemaHiveYouFilteredList=rhemaHiveYoutList;
        }

@NonNull
@Override
public RhemaHiveYoutubeDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.rhema_video_display_lay,parent,false);
        return new RhemaHiveYoutubeDisplayViewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull RhemaHiveYoutubeDisplayViewHolder holder,int position){
        rhemaHiveYoutubeVideosModel=rhemaHiveYouFilteredList.get(position);

        if(rhemaHiveYoutubeVideosModel.getVideoTitle()!=null&&rhemaHiveYoutubeVideosModel.getVideoTitle().length()>0&&!rhemaHiveYoutubeVideosModel.getVideoTitle().equals("")){
        holder.youtubeTitle.setText(rhemaHiveYoutubeVideosModel.getVideoTitle());

        }
        else{
        holder.youtubeTitle.setText(CocAcapelyConstants.EMPTY_VAL);
        }

        /*if(rhemaHiveYoutubeVideosModel.getVideoDate() != null && rhemaHiveYoutubeVideosModel.getVideoDate().length() > 0 && !rhemaHiveYoutubeVideosModel.getVideoDate().equals("")){
            holder.youtubeDate.setText(rhemaHiveYoutubeVideosModel.getVideoDate());

        }
        else{
            holder.youtubeDate.setText(RhemaHiveClassReferenceConstants.EMPTY_VAL);
        }
*/
        if(rhemaHiveYoutubeVideosModel.getVideoId()!=null&&rhemaHiveYoutubeVideosModel.getVideoId().length()>0&&!rhemaHiveYoutubeVideosModel.getVideoId().equals("")){
        holder.youtubeVideoId.setText(rhemaHiveYoutubeVideosModel.getVideoId());

        }
        else{
        holder.youtubeVideoId.setText(CocAcapelyConstants.EMPTY_VAL);
        }

        /*if(rhemaHiveYoutubeVideosModel.getViewsCount() != null && rhemaHiveYoutubeVideosModel.getViewsCount().length() > 0 && !rhemaHiveYoutubeVideosModel.getViewsCount().equals("")){

            holder.youtubeCount.setText(rhemaHiveYoutubeVideosModel.getViewsCount());

        }
        else{
            holder.youtubeCount.setText(RhemaHiveClassReferenceConstants.EMPTY_VAL);
        }*/

        if(rhemaHiveYoutubeVideosModel.getVideoThumbnail()!=null&&rhemaHiveYoutubeVideosModel.getVideoThumbnail().length()>0&&!rhemaHiveYoutubeVideosModel.getVideoThumbnail().equals("")){
       // GlideApp.with(v.getContext()).load(rhemaHiveYoutubeVideosModel.getVideoThumbnail()).into(holder.youtubeThumbnail);

        }
        else{
        holder.youtubeThumbnail.setImageResource(R.drawable.acapely);
        }

        }

@Override
public int getItemCount(){
        return rhemaHiveYoutList.size();
        }

@Override
public Filter getFilter(){
        return filteredYoutubeContent;
        }


public Filter filteredYoutubeContent=new Filter(){
@Override
protected FilterResults performFiltering(CharSequence charSequence)

        {


        String value=charSequence.toString();
        if(value.isEmpty()){
        rhemaHiveYouFilteredList=rhemaHiveYoutList;
        }else{
        ArrayList<RhemaHiveYoutubeVideosModel> filterVideoList=new ArrayList<>();
        for(RhemaHiveYoutubeVideosModel videoModelClass:rhemaHiveYoutList){
        if(videoModelClass.getVideoTitle().toLowerCase().contains(value.toLowerCase())){
        filterVideoList.add(videoModelClass);
        }
        }
        rhemaHiveYouFilteredList=filterVideoList;
        }

        FilterResults results=new FilterResults();
        results.values=rhemaHiveYouFilteredList;
        return results;
        }

@Override
protected void publishResults(CharSequence charSequence,FilterResults filterResults){

        rhemaHiveYoutList=(ArrayList<RhemaHiveYoutubeVideosModel>)filterResults.values;
        notifyDataSetChanged();

        }


        };


class RhemaHiveYoutubeDisplayViewHolder extends RecyclerView.ViewHolder {
    TextView youtubeTitle, youtubeDate, youtubeCount, youtubeVideoId;
    ImageView youtubeThumbnail;

    public RhemaHiveYoutubeDisplayViewHolder(@NonNull View itemView) {
        super(itemView);
        youtubeTitle = itemView.findViewById(R.id.rhem_youtube_title);
        //youtubeCount = itemView.findViewById(R.id.rhem_youtube_count);
        //youtubeDate = itemView.findViewById(R.id.rhem_youtube_time);
        youtubeVideoId = itemView.findViewById(R.id.rhem_youtube_video_id);
        youtubeThumbnail = itemView.findViewById(R.id.rhem_youtube_thumb);


    }

}



}