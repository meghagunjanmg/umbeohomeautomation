package com.umbeo.homeautomation;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.ColorSpace;
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
import java.util.concurrent.SynchronousQueue;

public class RelaysActivity extends AppCompatActivity {

    TextView title;
    static AppDatabase db;
    String tit,relays,newName;
    List<RelayModel> relayModels = new ArrayList<>();
    static List<RelayModel> relayModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    RelayAdapter adapter;
    static SynchronousQueue<String> dstate = new SynchronousQueue<>(true);
    static Thread stateprocess = new Thread()
    {
        public void run()
        {
            String str_state,str_first=null,str_title=null;
            boolean flg = false;
            long smp=0;
            while (true){
                try{
                    String cmd = dstate.poll();
                    if(cmd!=null){
                        str_title=cmd;
                        flg=true;
                    }
                    if(flg)
                    {
                        str_first = HomeActivity.relaystate.get(str_title);
                        flg=false;
                    }
                    if((System.currentTimeMillis()-smp)>=500){
                        str_state = HomeActivity.relaystate.get(str_title);
                        int len = str_state.length();
                        for(int i=0; i<len; i++){
                            if(str_first.charAt(i)!=str_state.charAt(i)){
                                updateState(i,String.valueOf(str_state.charAt(i)));
                            }
                        }
                        str_first=str_state;
                        smp=System.currentTimeMillis();
                    }
                }
                catch (Exception e)
                {

                }
        }
        }
    };
    static {stateprocess.start();}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relays);
        title = findViewById(R.id.title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (db == null) {
            db = AppDatabase.getInstance(getApplicationContext());
        }
        tit = getIntent().getStringExtra("title");
        newName = getIntent().getStringExtra("newName");
        title.setText(newName);

        try {
            dstate.put(tit);
        }
        catch (Exception e){

        }
        relays = getIntent().getStringExtra("relays");

        relays = HomeActivity.relaystate.get(tit);

        int size = 0;
        try {
            size = relays.length();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e("TEST",tit);
        for(int i = 0;i<size;i++)
        {
            relayModels.add(new RelayModel(i,"Relay "+i+1, String.valueOf(relays.charAt(i)),tit,relays));
            relayModelArrayList.add(new RelayModel(i,"Relay "+i+1, String.valueOf(relays.charAt(i)),tit,relays));
        }

        adapter = new RelayAdapter(relayModels, RelaysActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(RelaysActivity.this,2));
        recyclerView.setAdapter(adapter);

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

    static void updateState(int i , String n){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    db.realysDao().UpdateState(relayModelArrayList.get(i).getPid(),n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}