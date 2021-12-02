package com.finalproject.queerCalc.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.queerCalc.R;
import com.finalproject.queerCalc.Secret;
import com.finalproject.queerCalc.SecretDao;

import java.util.List;

public class SecretsAdapter extends RecyclerView.Adapter<SecretsAdapter.ViewHolder> {

    private int secretItemLayout;
    private List<Secret> secretList;

    public SecretsAdapter(int layoutId){secretItemLayout = layoutId;}

    private void setSecretList(List<Secret> secrets){
        secretList = secrets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(secretItemLayout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView secretTitle = holder.tv_secretTitle;
        TextView secretPin = holder.tv_secretPin;
        ImageView imageView = holder.img_menu;

        secretTitle.setText(secretList.get(position).getSecretTitle());
        secretPin.setText(secretList.get(position).getSecretPin());

        //set the imageview to inflate the menu onclick


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_secretTitle;
        TextView tv_secretPin;
        ImageView img_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_secretTitle = itemView.findViewById(R.id.temp_title);
            tv_secretPin = itemView.findViewById(R.id.temp_number);
            img_menu = itemView.findViewById(R.id.menu_button);
        }
    }


}
