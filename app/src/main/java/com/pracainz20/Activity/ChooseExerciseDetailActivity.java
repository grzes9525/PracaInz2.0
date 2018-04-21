package com.pracainz20.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pracainz20.Adapter.ChooseExerciseDetailAdapter;
import com.pracainz20.Model.Exercise;
import com.pracainz20.Model.Serie;
import com.pracainz20.R;
import com.pracainz20.Util.CalendarManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzechu on 18.04.2018.
 */

public class ChooseExerciseDetailActivity extends AppCompatActivity {

    private EditText editTextNumberSeries;
    private EditText editTextNumberRepetition;
    private EditText editTextWeight;
    private Button addExercise;
    private List<Serie> series;
    private Exercise exercise;
    private ChooseExerciseDetailAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exercise_detail);

        exercise = (Exercise) getIntent().getSerializableExtra("EXERCISE");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(exercise.getName());
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        setSupportActionBar(toolbar);


        addExercise = (Button) findViewById(R.id.add_exercise_info);
        editTextWeight = (EditText) findViewById(R.id.weight_val);
        editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("serie",editTextNumberSeries.getText().toString());
                Double weight = null;
                Integer numberSeries = null;
                Integer repetitions = null;
                if(editTextNumberSeries.getText()!=null
                        && !editTextNumberSeries.getText().toString().trim().isEmpty()
                        ){
                    numberSeries = Integer.valueOf(editTextNumberSeries.getText().toString().trim());

                }else{
                    numberSeries = 0;

                }
                if(editTextWeight.getText()!=null
                        && !editTextWeight.getText().toString().trim().isEmpty()
                        ){
                    weight = Double.valueOf(editTextWeight.getText().toString().trim());

                }else{
                    weight =0.0;

                }
                if(editTextNumberRepetition.getText()!=null
                        && !editTextNumberRepetition.getText().toString().trim().isEmpty()
                        ){
                    repetitions = Integer.valueOf(editTextNumberRepetition.getText().toString().trim());

                }else{
                    repetitions =0;

                }
                series = new ArrayList<>();
                for(int i = 1; i<=numberSeries;i++){
                    Serie serie = new Serie();
                    serie.setRepetitions(repetitions);
                    serie.setWeight(weight);
                    serie.setIdSerie(i);
                    series.add(serie);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView_exercise_detail);
                recyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                adapter = new ChooseExerciseDetailAdapter(getApplicationContext(),series);
                recyclerView.setAdapter(adapter);
            }
        });
        editTextNumberRepetition = (EditText) findViewById(R.id.number_repetition_val);
        editTextNumberRepetition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("serie",editTextNumberSeries.getText().toString());
                Double weight = null;
                Integer numberSeries = null;
                Integer repetitions = null;
                if(editTextNumberSeries.getText()!=null
                        && !editTextNumberSeries.getText().toString().trim().isEmpty()
                        ){
                    numberSeries = Integer.valueOf(editTextNumberSeries.getText().toString().trim());

                }else{
                    numberSeries = 0;

                }
                if(editTextWeight.getText()!=null
                        && !editTextWeight.getText().toString().trim().isEmpty()
                        ){
                    weight = Double.valueOf(editTextWeight.getText().toString().trim());

                }else{
                    weight =0.0;

                }
                if(editTextNumberRepetition.getText()!=null
                        && !editTextNumberRepetition.getText().toString().trim().isEmpty()){
                    repetitions = Integer.valueOf(editTextNumberRepetition.getText().toString().trim());

                }else{
                    repetitions =0;

                }
                series = new ArrayList<>();
                for(int i = 1; i<=numberSeries;i++){
                    Serie serie = new Serie();
                    serie.setRepetitions(repetitions);
                    serie.setWeight(weight);
                    serie.setIdSerie(i);
                    series.add(serie);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView_exercise_detail);
                recyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                adapter = new ChooseExerciseDetailAdapter(getApplicationContext(),series);
                recyclerView.setAdapter(adapter);
            }
        });

        editTextNumberSeries = (EditText) findViewById(R.id.number_series_val);
        editTextNumberSeries.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("serie",editTextNumberSeries.getText().toString());
                Double weight = null;
                Integer numberSeries = null;
                Integer repetitions = null;
                if(editTextNumberSeries.getText()!=null
                        && !editTextNumberSeries.getText().toString().trim().isEmpty()
                        ){
                    numberSeries = Integer.valueOf(editTextNumberSeries.getText().toString().trim());

                }else{
                    numberSeries = 0;

                }
                if(editTextWeight.getText()!=null
                        && !editTextWeight.getText().toString().trim().isEmpty()
                        ){
                    weight = Double.valueOf(editTextWeight.getText().toString().trim());

                }else{
                    weight =0.0;

                }
                if(editTextNumberRepetition.getText()!=null
                        && !editTextNumberRepetition.getText().toString().trim().isEmpty()
                        ){
                    repetitions = Integer.valueOf(editTextNumberRepetition.getText().toString().trim());

                }else{
                    repetitions =0;

                }
                series = new ArrayList<>();

                for(int i = 1; i<=numberSeries;i++){
                    Serie serie = new Serie();
                    serie.setRepetitions(repetitions);
                    serie.setWeight(weight);
                    serie.setIdSerie(i);
                    series.add(serie);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView_exercise_detail);
                recyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                adapter = new ChooseExerciseDetailAdapter(getApplicationContext(),series);
                recyclerView.setAdapter(adapter);
            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise.setSeries(adapter.getChangeSeries());
                Log.d("nSeries", String.valueOf(adapter.getChangeSeries().size()));
                Log.d("nSeries", String.valueOf(adapter.getChangeSeries().get(0).getWeight()));

                exercise.setSerires(Integer.valueOf(editTextNumberSeries.getText().toString().trim()));
                exercise.setWeight(Double.valueOf(editTextWeight.getText().toString().trim()));
                exercise.setRepetitions(Integer.valueOf(editTextNumberRepetition.getText().toString().trim()));
                exercise.setVolume(calculateVolume(adapter.getChangeSeries(), exercise));
                exercise.setDate(CalendarManagement.getDateToSaveDatabase(getIntent().getIntExtra("EXERCISE_DATE",0)));


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("Diaries")
                        .child("trenings")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .push();
                exercise.setPushId(ref.getKey());
                ref.setValue(exercise);
                startActivity(new Intent(getApplicationContext(), ChooseExerciseActivity.class));
            }
        });

    }

    private Double calculateVolume(List<Serie> series, Exercise exercise){
        Double volume = 0.0;
        Integer repetitions =0;


        for(Serie s : series)
        {
            Log.d("volume",s.getWeight().toString());
            volume = volume + s.getWeight()*s.getRepetitions();
            repetitions = repetitions + s.getRepetitions();

        }
        exercise.setRepetitions(repetitions);
        return volume;
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


                startActivity(new Intent(getApplicationContext(), ChooseExerciseActivity.class));
                finish();

            }
        }

        return super.onOptionsItemSelected(item);
    }
}
