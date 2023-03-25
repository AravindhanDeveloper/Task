package com.rifluxyss.task.Adapters;

import static android.content.Context.MODE_PRIVATE;
import static com.rifluxyss.task.Activities.MainActivity.finishBtn;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.rifluxyss.task.Activities.LikedListActivity;
import com.rifluxyss.task.Activities.MainActivity;
import com.rifluxyss.task.Model.RepData;
import com.rifluxyss.task.R;

import java.util.ArrayList;
import java.util.List;




public class LikedListAdapter extends RecyclerView.Adapter<LikedListAdapter.DashboardRecentViewHolder> {
    Context context;
    List<RepData> likeList = new ArrayList<>();
    List<RepData> fields = new ArrayList<>();

    public LikedListAdapter(LikedListActivity Activity, ArrayList<RepData> reps) {
        this.context = Activity;
        this.likeList = reps;
    }

    @NonNull
    @Override
    public DashboardRecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter, parent, false);
        return new DashboardRecentViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull DashboardRecentViewHolder holder, int position) {
        final RepData DataList = likeList.get(position);
        holder.nameTxt.setText(DataList.getName());
        Glide.with(context).load(DataList.getProfilePic()).into(holder.profileImage);
        holder.likedImg.setVisibility(View.GONE);
    }



    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public static class DashboardRecentViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView nameTxt;
        ImageView profileImage, likedImg;

        public DashboardRecentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.name_txt);
            profileImage = itemView.findViewById(R.id.profile_image);
            likedImg = itemView.findViewById(R.id.liked_img);


        }
    }
}
