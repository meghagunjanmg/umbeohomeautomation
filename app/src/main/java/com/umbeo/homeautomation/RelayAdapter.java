package com.umbeo.homeautomation;
import static com.umbeo.homeautomation.RelaysActivity.updateName;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class RelayAdapter extends RecyclerView.Adapter<RelayAdapter.ViewHolder>{
    List<RelayModel> models ;
    Context context;
    public RelayAdapter(List<RelayModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public RelayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_relay, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RelayAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.relay_name.setText(models.get(position).getRelay_name());
        if(models.get(position).getRelayState().equals("0")){
            holder.connect.setChecked(false);
        }else holder.connect.setChecked(true);

        holder.edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show(position);
            }
        });

        holder.connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        int rs = position + 1;
                        String ip = models.get(position).getDeviceip();
                        HomeActivity.connector.sq.put(new HomeAutomationOperator(ip, "R:" + rs + 1, null));
                    } else{
                        int rs = position + 1;
                        String ip = models.get(position).getDeviceip();
                        HomeActivity.connector.sq.put(new HomeAutomationOperator(ip, "R:" + rs + 0, null));
                    }
                    System.out.println(compoundButton);
                    System.out.println(b);
                }
                catch(Exception e)
                {
                    Log.e("TEST_RELAY_STATE",e.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView relay_name;
        Switch connect;
        ImageView edit_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edit_name = itemView.findViewById(R.id.edit_name);
            connect = itemView.findViewById(R.id.connect);
            relay_name = itemView.findViewById(R.id.relay_name);
        }
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

}
