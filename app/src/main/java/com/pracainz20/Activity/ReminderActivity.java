package com.pracainz20.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pracainz20.Adapter.ReminderAdapter;
import com.pracainz20.Model.Reminder;
import com.pracainz20.Model.User;
import com.pracainz20.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzechu on 18.03.2018.
 */

public class ReminderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private Reminder reminder;
    private int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("reminder","4");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Log.d("reminder","5");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d("reminder","6");

        Log.d("reminder","1");
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_reminder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<Reminder> reminders = new ArrayList<>();
        i=0;
        Log.d("reminder","2");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("MReminders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                reminder = new Reminder();
                reminder = dataSnapshot.getValue(Reminder.class);
                reminders.add(reminder);
                Log.d("reminderData",dataSnapshot.getValue(Reminder.class).getAuthor_id());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Query query = reference.child("MUsersPublic")
                        .child(dataSnapshot.getValue(Reminder.class).getAuthor_id());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        Log.d("datasnapshot1",dataSnapshot1.getValue(User.class).getFirstName()+" "+dataSnapshot1.getValue(User.class).getLastName());
                        reminders.get(i).setAuthor_name(dataSnapshot1.getValue(User.class).getFirstName()+" "+dataSnapshot1.getValue(User.class).getLastName());
                        reminders.get(i).setProfileImage(dataSnapshot1.getValue(User.class).getProfileImage());
                        i++;
                        reminderAdapter = new ReminderAdapter(getApplicationContext(),reminders, ReminderActivity.this);
                        recyclerView.setAdapter(reminderAdapter);
                        reminderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("child", "chand");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("child", "remove");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("child", "moved");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("child", "cancled");

            }
        });

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
