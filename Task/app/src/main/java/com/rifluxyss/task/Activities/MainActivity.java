package com.rifluxyss.task.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rifluxyss.task.Adapters.ListAdapter;
import com.rifluxyss.task.Model.RepData;
import com.rifluxyss.task.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    public static MaterialButton addButton, finishBtn;
    RecyclerView recyclerView;
    ArrayList<RepData> fields = new ArrayList();
    CircleImageView profileImage;
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list_recyclerview);
        finishBtn = findViewById(R.id.finish_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LikedListActivity.class);
                startActivity(i);
            }
        });
//        if (fields != null) {
            try {
                BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.rep_data)));
                StringBuilder jsonBuilder = new StringBuilder();
                for (String line = null; (line = jsonReader.readLine()) != null; ) {
                    jsonBuilder.append(line).append("\n");
                }

                JSONTokener tokener = new JSONTokener(jsonBuilder.toString());
                JSONArray jsonArray = new JSONArray(tokener);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    RepData repdata = new RepData();
                    repdata.setName(jsonObject.getString("name"));
                    repdata.setValue(jsonObject.getString("value"));
                    repdata.setProfilePic(jsonObject.getString("profile_pic"));
                    fields.add(repdata);
                    saveData();
                    ListAdapter Adapter = new ListAdapter(MainActivity.this, fields);
                    recyclerView.setAdapter(Adapter);
                    Adapter.notifyDataSetChanged();
                }

            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            } catch (JSONException e) {
            }
//        }
        loadData();

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBottomSheet();
            }
        });

    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("detail", null);
        Type type = new TypeToken<ArrayList<RepData>>() {
        }.getType();
        fields = gson.fromJson(json, type);
        ListAdapter RecentActivityAdapter = new ListAdapter(MainActivity.this, fields);
        recyclerView.setAdapter(RecentActivityAdapter);
        RecentActivityAdapter.notifyDataSetChanged();
        if (fields == null) {
            fields = new ArrayList<>();

        }
    }

    private void saveData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(fields);

        editor.putString("detail", json);

        editor.apply();
    }

    private void addBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.CustomBottomSheetDialog);
        bottomSheetDialog.setContentView(R.layout.add_list_bottomsheet);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        ImageView close = bottomSheetDialog.findViewById(R.id.close);
        profileImage = bottomSheetDialog.findViewById(R.id.profile_image);
        MaterialButton submit = bottomSheetDialog.findViewById(R.id.submit);
        TextInputEditText nameTxt = bottomSheetDialog.findViewById(R.id.name_txt);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepData repData = new RepData();
                repData.setProfilePic(String.valueOf(selectedImageUri));
                repData.setValue("12");
                repData.setName(nameTxt.getText().toString());
                fields.add(repData);
                saveData();
                loadData();

                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.show();
    }

    private void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    profileImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
