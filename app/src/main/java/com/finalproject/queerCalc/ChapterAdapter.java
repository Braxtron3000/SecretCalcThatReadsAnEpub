package com.finalproject.queerCalc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    private ArrayList<String> chapters;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_display,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(chapters.get(position));
        //holder.getPositionTextView().setText(""+position);
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        //private final TextView positionTextView;

        public ViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.textView);
            //positionTextView = (TextView) view.findViewById(R.id.tv_position);
        }

        public TextView getTextView() {
            return textView;
        }

        /*public TextView getPositionTextView() {
            return positionTextView;
        }*/
    }

    public ChapterAdapter(ArrayList<String>chapters){
        this.chapters=chapters;
    }
}
