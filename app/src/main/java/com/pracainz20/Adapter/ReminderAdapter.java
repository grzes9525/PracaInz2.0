package com.pracainz20.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pracainz20.Activity.MatesActivity;
import com.pracainz20.Activity.OwnProfileActivity;
import com.pracainz20.Model.Reminder;
import com.pracainz20.R;
import com.pracainz20.Util.CalendarManagement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Grzechu on 19.03.2018.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{

    private Context context;
    private List<Reminder> remindersList;
    private Reminder reminder;
    private Activity activity;

    public ReminderAdapter(Context context, List<Reminder> remindersList, Activity activity) {
        this.context = context;
        this.remindersList = remindersList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_reminder, parent, false);


        return new ReminderAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        reminder = remindersList.get(position);
        holder.reminderAuthorName.setText(reminder.getAuthor_name());
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
        holder.create_dt.setText(dateFormat.format(reminder.getCreate_dt()));

        Glide.with(context)
                .load(reminder.getProfileImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView reminderAuthorName;
        private ImageView image;
        private TextView create_dt;



        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;
            reminderAuthorName = (TextView) view.findViewById(R.id.reminder_author);
            create_dt = (TextView) view.findViewById(R.id.reminder_date);
            image = (ImageView) view.findViewById(R.id.reminder_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("remiderAdapter","execute");
                    Log.d("remiderAdapter",reminder.getType_reminder());

                    if(reminder.getType_reminder().equals("requestToAddToFriends")){
                        Log.d("remiderAdapter","executeIF");

                        activity.startActivity(new Intent(activity, MatesActivity.class));
                    }

                }
            });
        }
    }
}
