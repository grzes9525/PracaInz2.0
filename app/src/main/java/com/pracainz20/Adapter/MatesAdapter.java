package com.pracainz20.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pracainz20.Model.User;
import com.pracainz20.R;

import java.util.List;

/**
 * Created by Grzechu on 12.02.2018.
 */

public class MatesAdapter extends RecyclerView.Adapter<MatesAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<User> userList;
    private List<String> listMate;


    public MatesAdapter(Context context, List<String> listMate) {
        this.context = context;
        this.listMate = listMate;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mate_card_view, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        User user = userList.get(position);
        String imageUrl = null;

        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());

        imageUrl = user.getProfileImage();

        Glide.with(context)
                .load(imageUrl)
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstName;
        public TextView lastName;
        public ImageView image;
        String userid;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            firstName = (TextView) view.findViewById(R.id.textViewFirstName);
            lastName = (TextView) view.findViewById(R.id.textViewLastName);
            image = (ImageView) view.findViewById(R.id.imageProfile);

            userid = null;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // we can go to the next activity...

                }
            });

        }
    }
}