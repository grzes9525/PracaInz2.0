package com.pracainz20.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pracainz20.Activity.PersonalActivity;
import com.pracainz20.Model.WelcomeData;
import com.pracainz20.R;

import java.util.ArrayList;

/**
 * Created by Grzechu on 24.10.2017.
 */

public class PersonalDetailAdapter extends RecyclerView.Adapter<PersonalDetailAdapter.MyViewHolder> {

    private ArrayList<WelcomeData> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewValue;
        TextView unity_CardView_textView;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            this.textViewValue = (TextView) itemView.findViewById(R.id.textViewValue);
            this.unity_CardView_textView = (TextView) itemView.findViewById(R.id.unity_cardView_textView);

        }
    }

    public PersonalDetailAdapter(ArrayList<WelcomeData> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_welcome, parent, false);

        view.setOnClickListener(PersonalActivity.getMyOnClickListener());

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewTitle = holder.textViewTitle;
        TextView textViewValue = holder.textViewValue;
        TextView unity_cardView_textView = holder.unity_CardView_textView;


        textViewTitle.setText(dataSet.get(listPosition).getTitle());
        textViewValue.setText(dataSet.get(listPosition).getValue());
        unity_cardView_textView.setText(dataSet.get(listPosition).getUnity());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}