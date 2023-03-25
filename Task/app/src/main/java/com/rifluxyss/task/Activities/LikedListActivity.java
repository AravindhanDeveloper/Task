package com.rifluxyss.task.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rifluxyss.task.Adapters.LikedListAdapter;
import com.rifluxyss.task.Adapters.ListAdapter;
import com.rifluxyss.task.Model.RepData;
import com.rifluxyss.task.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LikedListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<RepData> fields = new ArrayList();
ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_list);
        recyclerView = findViewById(R.id.like_recyclerview);
        backBtn = findViewById(R.id.back_img);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("like", null);
        Type type = new TypeToken<ArrayList<RepData>>() {
        }.getType();
        fields = gson.fromJson(json, type);
        LikedListAdapter RecentActivityAdapter = new LikedListAdapter(LikedListActivity.this, fields);
        recyclerView.setAdapter(RecentActivityAdapter);
        RecentActivityAdapter.notifyDataSetChanged();
        if (fields == null) {
            fields = new ArrayList<>();

        }
    }

    public void onBackPressed() {
        finish();
    }

}