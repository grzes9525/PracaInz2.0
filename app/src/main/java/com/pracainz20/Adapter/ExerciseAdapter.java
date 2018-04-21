package com.pracainz20.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pracainz20.Model.Exercise;
import com.pracainz20.R;

import java.util.List;

/**
 * Created by Grzechu on 20.04.2018.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> implements Filterable {

    private List<Exercise> exerciseList;
    private Context context;

    public ExerciseAdapter(List<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseAdapter.ViewHolder holder, int position) {

        Exercise exercise = exerciseList.get(position);

        holder.exerciseName.setText(exercise.getName());
        holder.numberRepetitions.setText(exercise.getRepetitions().toString());
        holder.numberSeries.setText(exercise.getSerires().toString());
        holder.exerciseVolume.setText(exercise.getVolume().toString());

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView exerciseName;
        private TextView numberSeries;
        private TextView numberRepetitions;
        private TextView exerciseVolume;

        public ViewHolder(View itemView) {

            super(itemView);
            exerciseName = (TextView) itemView.findViewById(R.id.exercise_name);
            numberSeries = (TextView) itemView.findViewById(R.id.number_series_exercise);
            numberRepetitions = (TextView) itemView.findViewById(R.id.number_repetitions_exercise);
            exerciseVolume = (TextView) itemView.findViewById(R.id.volume_exercise);

        }


    }
}
