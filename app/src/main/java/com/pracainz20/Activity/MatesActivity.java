package com.pracainz20.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pracainz20.Adapter.MatesAdapter;
import com.pracainz20.Adapter.ReminderAdapter;
import com.pracainz20.Model.Invitation;
import com.pracainz20.Model.Mate;
import com.pracainz20.Model.Reminder;
import com.pracainz20.Model.User;
import com.pracainz20.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Grzechu on 09.02.2018.
 */

public class MatesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private AutoCompleteTextView acTextView;
    private ArrayAdapter<String> adapter;
    private Invitation invitation;
    private RecyclerView recyclerView;
    private MatesAdapter matesAdapter;
    private int i;
    private int k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Znajomi");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_mate_invitation);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Mates");
        mDatabaseReference.keepSynced(true);

        acTextView = (AutoCompleteTextView) findViewById(R.id.editText_search_button);

        k=0;
        final List<Invitation> invitations = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference reference1=reference.child("Mates").child("invitations").child(mUser.getUid());
        reference.child("Mates").child("invitations").child(mUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                Log.d("invitation", "execute");
                invitation = new Invitation();
                invitation = dataSnapshot.getValue(Invitation.class);
                invitation.setKeyId(dataSnapshot.getKey());
                invitations.add(invitation);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Query query = reference.child("MUsersPublic")
                        .child(dataSnapshot.getValue(Invitation.class).getUserId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        Log.d("datasnapshot1",dataSnapshot1.getValue(User.class).getFirstName()+" "+dataSnapshot1.getValue(User.class).getLastName()+""+k);
                        invitations.get(k).setUserName(dataSnapshot1.getValue(User.class).getFirstName()+" "+dataSnapshot1.getValue(User.class).getLastName());

                        invitations.get(k).setProfileImage(dataSnapshot1.getValue(User.class).getProfileImage());
                        k++;
                        matesAdapter = new MatesAdapter(getApplicationContext(),invitations, MatesActivity.this, reference1);
                        recyclerView.setAdapter(matesAdapter);
                        matesAdapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            if (mUser != null && mAuth != null) {
                mAuth.signOut();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        final List<String> matesList = new ArrayList();
        final Map<Integer, String> matesMap = new HashMap<>();


        i=0;
        Log.d("onStart","wywołanie onStart");
        mDatabaseReference.child(mUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("poAddChild","po wywolaniu metody addChild "+i);
                Mate mate = dataSnapshot.getValue(Mate.class);
                Log.d("user_wart",mate.getFirstName()+" "+mate.getLastName());
                matesMap.put(i,mate.getUserId());
                Log.d("mateId",mate.getUserId());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                Query query = reference.child("MUsersPublic")
                        .child(mate.getUserId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        Log.d("mate1",dataSnapshot1.getValue(Mate.class).getFirstName());
                    Mate mate1 = dataSnapshot1.getValue(Mate.class);
                    matesList.add( mate1.getFirstName()+" "+mate1.getLastName());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Log.d("key",dataSnapshot.getKey());
                i++;
                Log.d("matesMap", String.valueOf(matesMap.size()));
                adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.select_dialog_singlechoice,matesList );
                acTextView.setThreshold(1);
                acTextView.setAdapter( adapter);
                acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selected = (String) parent.getItemAtPosition(position);
                        final int pos = matesList.indexOf(selected);
                        Log.d("pos", String.valueOf(pos));
                        Log.d("userIDMAte",matesMap.get(pos));
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("Mates")
                                .child(mUser.getUid())
                                .orderByChild("userId")
                                .equalTo(matesMap.get(pos));
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Log.d("DataSnapShot",dataSnapshot.getValue(String.class));
                                //Log.d("mateId ",dataSnapshot.getValue(String.class));
                                if (dataSnapshot.exists()) {
                                    Log.d("warunek ","if");
                                    Intent intent = new Intent(getApplicationContext(), MateProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("MATE_ID", matesMap.get(pos));
                                    startActivity(intent);

                                }else if(matesMap.get(pos).equals(mUser.getUid())){
                                    Log.d("warunek ","elseif");
                                    Intent intent = new Intent(getApplicationContext(), OwnProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("OWN_ID", matesMap.get(pos));
                                    startActivity(intent);
                                }else {
                                    Log.d("warunek ","else");
                                    Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("USER_ID", matesMap.get(pos));
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




    }
}
