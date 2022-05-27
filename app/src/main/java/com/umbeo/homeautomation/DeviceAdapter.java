package com.umbeo.homeautomation;

import static com.umbeo.homeautomation.HomeActivity.sq;
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
    String relayState = "";

    // RecyclerView recyclerView;

    public DeviceAdapter(Context context, List<DeviceModel> listdata) {
        this.listdata = listdata;
        this.context = context;
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
                        sq.put(new OperateData("stop:",null));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.connect.setChecked(true);
                    i = 1;
                    holder.device_status.setTextColor(Color.parseColor("#00AD39"));
                    holder.device_status.setText("Connected");
                    try {
                        sq.put(new OperateData("start:" + (String) listdata.get(position).getDevice_name(), new DeviceState() {
                            @Override
                            public void relayState(String a) {
                                relayState = a;
                                Toast.makeText(context, "Interface value: "+a+" Stored: "+relayState, Toast.LENGTH_SHORT).show();
                            }
                        }));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                if(listdata.get(position).getDevice_status()==1) {
                    Intent i = new Intent(context, RelaysActivity.class);
                    i.putExtra("title", listdata.get(position).getNew_name() + "");
                    i.putExtra("relays", relayState+"");
                    context.startActivity(i);
                }
                else {
                }
            }
        });

    }

    private void Show(int posi) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Enter New Name");
        alertDialog.setMessage(" ");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_baseline_done_24);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString();
                        updateName(posi, newName);
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
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
        ImageButton edit_name;
        CardView deviceCard;

        ViewHolder(View itemView) {
            super(itemView);
            device_name = (TextView) itemView.findViewById(R.id.device_name);
            edit_name = (ImageButton) itemView.findViewById(R.id.edit_name);
            newName = (TextInputEditText) itemView.findViewById(R.id.newName);
            device_status = (TextView) itemView.findViewById(R.id.device_status);
            connect = (Switch) itemView.findViewById(R.id.connect);
            deviceCard = (CardView) itemView.findViewById(R.id.deviceCard);
        }

    }
}