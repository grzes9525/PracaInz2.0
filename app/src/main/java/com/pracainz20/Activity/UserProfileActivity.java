package com.pracainz20.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pracainz20.Model.Mapper.UserParameterMapper;
import com.pracainz20.Model.Reminder;
import com.pracainz20.Model.User;
import com.pracainz20.Model.UserParameter;
import com.pracainz20.R;
import com.pracainz20.Util.CalendarManagement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.pracainz20.Model.Mapper.UserParameterMapper.mapper;

/**
 * Created by Grzechu on 06.03.2018.
 */

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private TextView userName;
    private TextView dialogUserName;

    private ImageView profilePic;
    private Button sendInvitation;
    private Context c = this ;
    private User[] user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("MUsersPublic").child(getIntent().getStringExtra("USER_ID"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("userActivitySnapshot", dataSnapshot.getValue(User.class).getFirstName());
                    user = new User[1];
                    user[0] = dataSnapshot.getValue(User.class);
                    Uri uri = Uri.parse(user[0].getProfileImage());
                    Log.d("userUri",user[0].getProfileImage());
                    userName = (TextView) findViewById(R.id.user_name);
                    profilePic = (ImageView) findViewById(R.id.imageView_profile_user);
                    userName.setText(user[0].getFirstName()+" "+user[0].getLastName());
                    StorageReference storage = FirebaseStorage.getInstance()
                            .getReferenceFromUrl(String.valueOf(uri));
                    Glide.with(c)
                            .using(new FirebaseImageLoader())
                            .load(storage)
                            .into(profilePic);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sendInvitation = (Button) findViewById(R.id.button_send_invitation);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sendInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitation();
            }
        });

    }

    public void sendInvitation(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_send_invitation, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        dialogUserName = (TextView) mView.findViewById(R.id.dialog_user_name);


        Log.d("sendInvitaionValue",user[0].getFirstName()+" "+user[0].getLastName());
        dialogUserName.setText(user[0].getFirstName()+" "+user[0].getLastName());

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Wyślij", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        String usrId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference.child("Permissions").child(usrId)
                            .child("accessToUser").push().setValue(getIntent().getStringExtra("USER_ID"));
                        Reminder reminder = new Reminder();
                        reminder.setAuthor_id(usrId);
                        reminder.setCreate_dt(new Date());
                        //reminder.setAuthor_name();
                        reminder.setType_reminder("requestToAddToFriends");
                    reference.child("MReminders").child(getIntent().getStringExtra("USER_ID")).push().setValue(reminder);

                    }
                })

                .setNegativeButton("Anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_personal_details) {
            startActivity(new Intent(getApplicationContext(), PersonalDetailActivity.class));
            finish();
        }
        if (id == R.id.nav_profile) {
            startActivity(new Intent(getApplicationContext(), PersonalActivity.class));
            finish();
        }
        if (id == R.id.nav_mates) {
            startActivity(new Intent(getApplicationContext(), MatesActivity.class));
            finish();
        }if (id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            if (firebaseUser != null && firebaseAuth != null) {
                firebaseAuth.signOut();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        }

        return super.onOptionsItemSelected(item);
    }
}
