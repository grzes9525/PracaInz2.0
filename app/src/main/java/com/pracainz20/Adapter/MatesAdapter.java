package com.pracainz20.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pracainz20.Activity.MateProfileActivity;
import com.pracainz20.Activity.MatesActivity;
import com.pracainz20.Activity.UserProfileActivity;
import com.pracainz20.Model.Invitation;
import com.pracainz20.Model.Mate;
import com.pracainz20.Model.User;
import com.pracainz20.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Grzechu on 12.02.2018.
 */

public class MatesAdapter extends RecyclerView.Adapter<MatesAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Invitation> invitations;
    private Activity activity;
    private DatabaseReference databaseReference;


    public MatesAdapter(Context context, List<Invitation> invitations, Activity activity, DatabaseReference databaseReference) {
        this.context = context;
        this.invitations = invitations;
        this.activity = activity;
        this.databaseReference = databaseReference;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mate_card_view, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Invitation invitation = invitations.get(position);

        holder.userName.setText(invitation.getUserName());
        holder.showProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserProfileActivity.class);
                intent.putExtra("USER_ID", invitation.getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });

        holder.rejectInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("rejectButton","execute");
               databaseReference.child(invitation.getKeyId()).removeValue();
                Intent intent = new Intent(activity, MatesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });

        holder.acceptInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Log.d("rejectButton","execute");
                databaseReference.child(invitation.getKeyId()).removeValue();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Mate mate1 = new Mate();
                mate1.setUserId(invitation.getUserId());
                mate1.setCreateDt(new Date());
                //dodanie uzytkownika do swoich znajomych
                reference.child("Mates").child(myID).push().setValue(mate1);
                //dodanie pozwolenia na dostęp do swoich danych
                reference.child("Permissions").child(myID).child("permissionForUser").push().setValue(invitation.getUserId());
                //dodanie siebie do jego znjamoych
                Mate mate2 = new Mate();
                mate2.setUserId(myID);
                mate2.setCreateDt(new Date());
                reference.child("Mates").child(invitation.getUserId()).push().setValue(mate2);
                Intent intent = new Intent(activity, MatesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });


        Glide.with(context)
                .load(invitation.getProfileImage())
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private ImageView image;
        private Button showProfile;
        private Button rejectInvitation;
        private Button acceptInvitation;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            userName = (TextView) view.findViewById(R.id.inivitation_user_name);
            image = (ImageView) view.findViewById(R.id.imageProfile);
            showProfile = (Button) view.findViewById(R.id.show_profile);
            rejectInvitation = (Button) view.findViewById(R.id.reject_invitation);
            acceptInvitation= (Button) view.findViewById(R.id.accept_invitation);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // we can go to the next activity...

                }
            });

        }
    }
}