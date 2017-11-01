package com.pracainz20;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Grzechu on 24.10.2017.
 */

public class WelcomeAdapter extends RecyclerView.Adapter<WelcomeAdapter.MyViewHolder> {

    private ArrayList<WelcomeData> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewValue;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            this.textViewValue = (TextView) itemView.findViewById(R.id.textViewValue);

        }
    }

    public WelcomeAdapter(ArrayList<WelcomeData> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_welcome, parent, false);

        view.setOnClickListener(WelcomeActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewTitle = holder.textViewTitle;
        TextView textViewValue = holder.textViewValue;


        textViewTitle.setText(dataSet.get(listPosition).getTitle());
        textViewValue.setText(dataSet.get(listPosition).getValue());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
