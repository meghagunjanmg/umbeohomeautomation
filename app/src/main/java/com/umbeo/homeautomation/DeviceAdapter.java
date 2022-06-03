package com.umbeo.homeautomation;
import static com.umbeo.homeautomation.HomeActivity.updateName;
import static com.umbeo.homeautomation.HomeActivity.updateStatus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;


public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private List<DeviceModel> listdata;
    int count = 0;
    private Context context;
    int i = -1;
    HomeAutomationConnector hc;

    // RecyclerView recyclerView;

    public DeviceAdapter(Context context, List<DeviceModel> listdata) {
        this.listdata = listdata;
        this.context = context;
        hc = new HomeAutomationConnector();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_device, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final DeviceModel myListData = listdata.get(position);
        holder.device_name.setText(listdata.get(position).getNew_name());
        holder.newName.setText(listdata.get(position).getNew_name());
        Log.e("ROOMDB", String.valueOf(listdata.get(position).getDevice_status()));

        if (listdata.get(position).getDevice_status() == 1) {
            holder.connect.setChecked(true);
            holder.device_status.setTextColor(Color.parseColor("#00AD39"));
            holder.device_status.setText("Connected");
        } else {
            holder.connect.setChecked(false);
            holder.device_status.setTextColor(Color.parseColor("#F44336"));
            holder.device_status.setText("Not Connected");
        }


        holder.connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    holder.device_status.setTextColor(Color.parseColor("#F44336"));
                    holder.device_status.setText("Not Connected");
                    holder.connect.setChecked(false);
                    i = 0;
                    try {
                        hc.sq.put(new HomeAutomationOperator(listdata.get(position).getDevice_name(),"stop:",null));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.connect.setChecked(true);
                    i = 1;
                    holder.device_status.setTextColor(Color.parseColor("#00AD39"));
                    holder.device_status.setText("Connected");
                    try
                    {
                        hc.sq.put(new HomeAutomationOperator(listdata.get(position).getDevice_name(), "start", new HomeAutomationListener() {
                            public void homeAutomationState(String a) {
                                if (a != null) {
                                    HomeActivity.relaystate.put(listdata.get(i).getDevice_name(), a);
                                }
                            }
                        }
                        ));
                    }
                    catch(Exception e)
                    {
                    }
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateStatus(position, i);
                    }
                }, 1000);
            }
        });

        holder.edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show(position);

            }
        });
        holder.deviceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listdata.get(position).getDevice_status()==1 ) {

                    Intent i = new Intent(context, RelaysActivity.class);
                    i.putExtra("title", listdata.get(position).getDevice_name() + "");
                    i.putExtra("newName", listdata.get(position).getNew_name() + "");
                    i.putExtra("relays", HomeActivity.relaystate.get(listdata.get(position).getDevice_name()));
                    context.startActivity(i);
                }
                else {
                }
            }
        });

    }

    private void Show(int posi) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialoglayout = inflater.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(dialoglayout);
        TextInputEditText input = dialoglayout.findViewById(R.id.input);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newName = input.getText().toString();
                if(newName.length()>0) updateName(posi, newName);
                else Toast.makeText(context, "Empty String", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertDialog.show();
}


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView device_name, device_status;
        Switch connect;
        TextInputEditText newName;
        ImageView edit_name;
        CardView deviceCard;

        ViewHolder(View itemView) {
            super(itemView);
            device_name = (TextView) itemView.findViewById(R.id.device_name);  //ip name
            edit_name = (ImageView) itemView.findViewById(R.id.edit_name);
            newName = (TextInputEditText) itemView.findViewById(R.id.newName);
            device_status = (TextView) itemView.findViewById(R.id.device_status);
            connect = (Switch) itemView.findViewById(R.id.connect); // toggle
            deviceCard = (CardView) itemView.findViewById(R.id.deviceCard);
        }

    }
}