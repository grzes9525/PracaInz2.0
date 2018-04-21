package com.pracainz20.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pracainz20.Adapter.ChooseExerciseAdapter;
import com.pracainz20.Model.Exercise;
import com.pracainz20.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzechu on 16.04.2018.
 */

public class ChooseExerciseActivity extends AppCompatActivity{

    private RecyclerView.LayoutManager layoutManager;
    private AutoCompleteTextView acTextView;
    private int counter;
    private List<Exercise> exercises;
    private Context c=this;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exercise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Cwiczenia");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        setSupportActionBar(toolbar);

        layoutManager = new LinearLayoutManager(this);
        counter=0;

        exercises = new ArrayList<>();

        acTextView = (AutoCompleteTextView) findViewById(R.id.editText_search_exercise);



        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Exercises").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exercises.add(exercise);
                    counter++;
                    Log.d("exercise", exercise.getName()+" "+counter);
                    if(counter>=152 ){
                        Log.d("pList", String.valueOf(exercises.size()));
                        //RecyclerView.Adapter adapter = new ChooseProductAdapter(c,products,ChooseProductActivity.this);
                        Log.d("day", String.valueOf(getIntent().getIntExtra("DAY_IN_DB",0)));
                        ChooseExerciseAdapter adapter = new ChooseExerciseAdapter(c
                                ,exercises
                                ,ChooseExerciseActivity.this
                                ,getIntent().getIntExtra("DAY_IN_DB",0));

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_choose_exercise);
                        recyclerView.setHasFixedSize(true);

                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        acTextView.setThreshold(1);
                        acTextView.setAdapter(adapter);

                        recyclerView.setAdapter(adapter);
                        reference.child("Exercises").removeEventListener(this);

                    }



                }
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

    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        if (id == android.R.id.home) {
            if (firebaseUser != null && firebaseAuth != null) {


                startActivity(new Intent(getApplicationContext(), DiaryActivity.class));
                finish();

            }
        }

        return super.onOptionsItemSelected(item);
    }

}
