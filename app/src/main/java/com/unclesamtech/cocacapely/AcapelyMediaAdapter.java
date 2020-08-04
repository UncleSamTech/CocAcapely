package com.unclesamtech.cocacapely;

import android.text.TextUtils;
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

public class AcapelyMediaAdapter extends RecyclerView.Adapter<AcapelyMediaAdapter.AcapelyMediaViewHolder> implements Filterable {
    private ArrayList<AcapelyMediaModel> createPlayList ;
    private ArrayList<AcapelyMediaModel> filteredPlayList;
    private View v;
    private AcapelyMediaModel playListModel;

    public AcapelyMediaAdapter(ArrayList<AcapelyMediaModel> createPlayList) {
        this.createPlayList = createPlayList;
        this.filteredPlayList = new ArrayList<>(createPlayList);
    }

    @NonNull
    @Override
    public AcapelyMediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_media_list,parent,false);
        return new AcapelyMediaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AcapelyMediaViewHolder holder, int position) {
        playListModel = createPlayList.get(position);
        if(!TextUtils.isEmpty(playListModel.getPlaylist_title()) && playListModel.getPlaylist_title() != null && playListModel.getPlaylist_title().length() > 0){
            holder.playlistTitle.setText(playListModel.getPlaylist_title());
        }
        else{
            holder.playlistTitle.setText(CocAcapelyConstants.EMPTY_VAL);
        }

        if(!TextUtils.isEmpty(playListModel.getPlaylist_poster()) && playListModel.getPlaylist_poster() != null && playListModel.getPlaylist_poster().length() > 0){
            holder.playlistPoster.setText(playListModel.getPlaylist_poster());
        }
        else{
            holder.playlistPoster.setText(CocAcapelyConstants.EMPTY_VAL);
        }


        if(!TextUtils.isEmpty(playListModel.getPlaylistTime()) && playListModel.getPlaylistTime() != null && playListModel.getPlaylistTime().length() > 0){
            holder.playlistTime.setText(playListModel.getPlaylistTime());
        }
        else{
            holder.playlistTime.setText(CocAcapelyConstants.EMPTY_VAL);
        }

        if(!TextUtils.isEmpty(playListModel.getPlaylist_icon()) && playListModel.getPlaylist_icon().length() >0 && playListModel.getPlaylist_icon() != null){


                //GlideApp.with(v).load(playListModel.getPlaylist_icon()).into(holder.playlistIcon);
        }

        else{
            holder.playlistIcon.setImageResource(R.drawable.acapely);
        }

        if(!TextUtils.isEmpty(playListModel.getPlaylist_id()) && playListModel.getPlaylist_id().length() >0 && playListModel.getPlaylist_id()  != null){
            holder.playlistId.setText(playListModel.getPlaylist_id());
        }
        else{
            holder.playlistId.setText(CocAcapelyConstants.EMPTY_VAL);
        }

    }

    @Override
    public int getItemCount() {
        return createPlayList.size();
    }

    @Override
    public Filter getFilter() {
        return acapelyFilter;
    }


    public Filter acapelyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
           /* ArrayList<RhemaHiveUserMessageModelClass> rhemaModelList = new ArrayList<>();
            if(charSequence.length() == 0 || charSequence == null ){
               rhemaModelList.addAll(filteredMessage);

            }
            else{

                String filteredEntry  = charSequence.toString().toLowerCase().trim();
                for(RhemaHiveUserMessageModelClass rModel : filteredMessage){
                    if(rModel.getReceiverName().toLowerCase().contains(filteredEntry)){
                        rhemaModelList.add(rModel);
                    }
                }

                filteredMessage = rhemaModelList;
            }
*/

            String value = charSequence.toString();
            if (value.isEmpty()) {
                filteredPlayList = createPlayList;
            } else {
                ArrayList<AcapelyMediaModel> filterPlayList = new ArrayList<>();
                for (AcapelyMediaModel playListModelClass : createPlayList) {
                    if (playListModelClass.getPlaylist_title().toLowerCase().contains(value.toLowerCase())) {
                        filterPlayList.add(playListModelClass);
                    }
                }
                filteredPlayList = filterPlayList;
            }

            FilterResults results = new FilterResults();
            results.values = filteredPlayList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            createPlayList = (ArrayList<AcapelyMediaModel>) filterResults.values;
            notifyDataSetChanged();
        }

    };




    public class AcapelyMediaViewHolder extends RecyclerView.ViewHolder{
    TextView playlistTitle, playlistPoster, playlistTime,playlistId;
    ImageView playlistIcon;

    public AcapelyMediaViewHolder(@NonNull View itemView) {
        super(itemView);
        playlistTitle = itemView.findViewById(R.id.church_playlist_title);
        playlistPoster = itemView.findViewById(R.id.playlist_church);
        playlistTime = itemView.findViewById(R.id.playlist_time);
        playlistIcon = itemView.findViewById(R.id.playlist_loggo);
        playlistId = itemView.findViewById(R.id.playlist_id);
    }


}

}
