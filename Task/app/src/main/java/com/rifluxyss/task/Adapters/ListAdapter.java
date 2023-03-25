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
import com.google.gson.reflect.TypeToken;
import com.rifluxyss.task.Activities.MainActivity;
import com.rifluxyss.task.Model.RepData;
import com.rifluxyss.task.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.DashboardRecentViewHolder> {
    Context context;
    List<RepData> userList = new ArrayList<>();
    List<RepData> fields = new ArrayList<>();

    public ListAdapter(MainActivity mainActivity, ArrayList<RepData> reps) {
        this.context = mainActivity;
        this.userList = reps;
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
        final RepData DataList = userList.get(position);

        holder.nameTxt.setText(DataList.getName());
        Glide.with(context).load(DataList.getProfilePic()).into(holder.profileImage);
        holder.likedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (holder.likedImg.isSelected()) {
                    holder.likedImg.setSelected(false);
                    Glide.with(context).load(R.drawable.ic_heart).into(holder.likedImg);
                    RepData repData = new RepData();
                    repData.setProfilePic(DataList.getProfilePic());
                    repData.setValue(DataList.getValue());
                    repData.setName(DataList.getName());
                    fields.remove(repData);
                    saveData();

                } else {
                    RepData repData = new RepData();
                    repData.setProfilePic(DataList.getProfilePic());
                    repData.setValue(DataList.getValue());
                    repData.setName(DataList.getName());
                    fields.add(repData);
                    saveData();
                    finishBtn.setEnabled(true);
                    Glide.with(context).load(R.drawable.ic_heart_red).into(holder.likedImg);
                    holder.likedImg.setSelected(true);
                }
            }
        });
    }

    private void saveData() {

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preference", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(fields);

        editor.putString("like", json);

        editor.apply();
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
