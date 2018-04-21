package com.pracainz20.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pracainz20.Adapter.ExerciseAdapter;
import com.pracainz20.Adapter.MealAdapter;
import com.pracainz20.Adapter.PersonalDetailAdapter;
import com.pracainz20.Model.Exercise;
import com.pracainz20.Model.Mapper.UserParameterMapper;
import com.pracainz20.Model.Meal;
import com.pracainz20.Model.Product;
import com.pracainz20.Model.UserParameter;
import com.pracainz20.Model.WelcomeData;
import com.pracainz20.R;
import com.pracainz20.Util.CalendarManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pracainz20.Model.Mapper.UserParameterMapper.mapper;

/**
 * Created by Grzechu on 23.03.2018.
 */

public class DiaryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton nextDate;
    private ImageButton previousDate;
    private int dayInDB = 0;
    private TextView dateConfirmation;
    private Context c=this;
    private RecyclerView.LayoutManager layoutManagerMeal;
    private RecyclerView.LayoutManager layoutManagerExercise;
    private RecyclerView recyclerViewMeal;
    private RecyclerView recyclerViewExercise;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        TabHost tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setup();

        final TabHost.TabSpec spec = tabHost.newTabSpec("Posiłki");
        spec.setContent(R.id.tab_meal);
        spec.setIndicator("Posiłki");
        tabHost.addTab(spec);

        TabHost.TabSpec spec1 = tabHost.newTabSpec("Trening");
        spec1.setContent(R.id.tab_exercise);
        spec1.setIndicator("Trening");
        tabHost.addTab(spec1);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_meal);

        layoutManagerMeal = new LinearLayoutManager(this);
        layoutManagerExercise = new LinearLayoutManager(this);

        nextDate = (ImageButton) findViewById(R.id.right_date_diary_button);
        previousDate = (ImageButton) findViewById(R.id.left_date_diary_button);
        dateConfirmation = (TextView) findViewById(R.id.date_diary_textView);
        dateConfirmation.setText(CalendarManagement.getDate(dayInDB));

        nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNextDay();
            }
        });

        previousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePreviousDay();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(recyclerViewMeal.isShown())
                {
                    addMeal();

                }else {
                    addExercise();
                }

            }
        });

    }

    private void addExercise(){
        Intent intent = new Intent(getApplicationContext(), ChooseExerciseActivity.class);
        intent.putExtra("DAY_IN_DB",dayInDB);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void addMeal(){

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_meal, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        //String[] meals = new String[]{"śniadanie", "obiad", "lunch","kolacja","podwieczorek"};
        List<String> meals = new ArrayList<>();
        meals.add("sniadanie");
        meals.add("obiad");
        meals.add("kolacja");
        meals.add("lunch");
        meals.add("podwieczorek");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.select_dialog_singlechoice,meals );
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) mView.findViewById(R.id.dialog_input_name_meal);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Wstaw", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Log.d("actionbar","execute");

                        Meal meal = new Meal();
                        meal.setDate(CalendarManagement.getDateToSaveDatabase(dayInDB));
                        meal.setName(autoCompleteTextView.getText().toString().trim());
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                .child("Diaries")
                                .child("meals")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .push();
                        meal.setPushId(ref.getKey());

                        ref.setValue(meal);
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
    protected void onStart() {
        super.onStart();
        dateConfirmation.setText(CalendarManagement.getDate(dayInDB));



        final RecyclerView.Adapter[] adapter = new RecyclerView.Adapter[1];

        final List<Meal> meals = new ArrayList<>();

        ////
        final List<Exercise> exercises = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference()
                .child("Diaries")
                .child("trenings")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("date")
                .equalTo(CalendarManagement.getDateToSaveDatabase(dayInDB)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                if(dataSnapshot.getValue()!=null){
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exercises.add(exercise);
                }

                adapter[0] = new ExerciseAdapter(exercises, getApplicationContext());
                recyclerViewExercise = (RecyclerView) findViewById(R.id.my_recycler_view_exercise);
                recyclerViewExercise.setHasFixedSize(true);

                recyclerViewExercise.setLayoutManager(layoutManagerExercise);
                recyclerViewExercise.setItemAnimator(new DefaultItemAnimator());

                recyclerViewExercise.setAdapter(adapter[0]);




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
        adapter[0] = new ExerciseAdapter(exercises, getApplicationContext());
        recyclerViewExercise = (RecyclerView) findViewById(R.id.my_recycler_view_exercise);
        recyclerViewExercise.setHasFixedSize(true);

        recyclerViewExercise.setLayoutManager(layoutManagerExercise);
        recyclerViewExercise.setItemAnimator(new DefaultItemAnimator());

        recyclerViewExercise.setAdapter(adapter[0]);
        /////

        FirebaseDatabase.getInstance().getReference()
                .child("Diaries")
                .child("meals")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("date")
                .equalTo(CalendarManagement.getDateToSaveDatabase(dayInDB)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                    if(dataSnapshot.getValue()!=null){
                        Meal meal = dataSnapshot.getValue(Meal.class);
                        if(meal.getProducts()==null){
                            Product product = new Product();
                            product.setName("");
                            product.setKcal(0d);
                            List<Product> list = new ArrayList<>();
                            meal.setProducts(list);
                        }
                        meals.add(meal);
                    }




                    Log.d("meals",meals.get(0).getName());

                    adapter[0] = new MealAdapter(c,meals,DiaryActivity.this);
                    recyclerViewMeal = (RecyclerView) findViewById(R.id.my_recycler_view_meal);
                    recyclerViewMeal.setHasFixedSize(true);

                    recyclerViewMeal.setLayoutManager(layoutManagerMeal);
                    recyclerViewMeal.setItemAnimator(new DefaultItemAnimator());

                    recyclerViewMeal.setAdapter(adapter[0]);




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
        adapter[0] = new MealAdapter(c,meals,DiaryActivity.this);
        recyclerViewMeal = (RecyclerView) findViewById(R.id.my_recycler_view_meal);
        recyclerViewMeal.setHasFixedSize(true);

        recyclerViewMeal.setLayoutManager(layoutManagerMeal);
        recyclerViewMeal.setItemAnimator(new DefaultItemAnimator());

        recyclerViewMeal.setAdapter(adapter[0]);


    }

    private void updateNextDay() {
        dayInDB = dayInDB + 1;
        onStart();
    }

    private void updatePreviousDay() {
        dayInDB = dayInDB - 1;
        onStart();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
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
        }if (id == R.id.nav_reminder) {
            startActivity(new Intent(getApplicationContext(), ReminderActivity.class));
            finish();
        }if (id == R.id.nav_diary) {
            startActivity(new Intent(getApplicationContext(), DiaryActivity.class));
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
