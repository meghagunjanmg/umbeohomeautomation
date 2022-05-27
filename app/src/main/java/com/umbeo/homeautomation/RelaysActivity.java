package com.umbeo.homeautomation;

import static com.umbeo.homeautomation.HomeActivity.sq;
import static com.umbeo.homeautomation.HomeActivity.updateName;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class RelaysActivity extends AppCompatActivity {

    TextView title;
    static AppDatabase db;
    String tit,relays;
    List<RelayModel> relayModels = new ArrayList<>();
    static List<RelayModel> relayModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    RelayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relays);
        title = findViewById(R.id.title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        tit = getIntent().getStringExtra("title");
        relays = getIntent().getStringExtra("relays");
        title.setText(tit);

        int size = relays.length();
        for(int i = 0;i<size;i++)
        {
            relayModels.add(new RelayModel(i,"Relay "+i+1, String.valueOf(relays.charAt(i))));
            relayModelArrayList.add(new RelayModel(i,"Relay "+i+1, String.valueOf(relays.charAt(i))));
        }

        adapter = new RelayAdapter(relayModels, RelaysActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(RelaysActivity.this,2));
        recyclerView.setAdapter(adapter);

        if (db == null) {
            db = AppDatabase.getInstance(getApplicationContext());
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.realysDao().nukeTable();
            }
        });

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.realysDao().insertAll(relayModels);
            }
        });


        db.realysDao().getAll().observe(this, new Observer<List<RelayModel>>() {
            @Override
            public void onChanged(List<RelayModel> entities) {
                adapter = new RelayAdapter(entities, RelaysActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(RelaysActivity.this,2));
                recyclerView.setAdapter(adapter);
            }
        });


    }
    static void updateName(int i , String n){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    db.realysDao().UpdateOne(relayModelArrayList.get(i).getPid(),n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}