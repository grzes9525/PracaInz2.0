package com.pracainz20.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.pracainz20.Activity.ChooseExerciseDetailActivity;
import com.pracainz20.Activity.OwnProfileActivity;
import com.pracainz20.Model.Exercise;
import com.pracainz20.Model.Product;
import com.pracainz20.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzechu on 17.04.2018.
 */

public class ChooseExerciseAdapter  extends RecyclerView.Adapter<ChooseExerciseAdapter.ViewHolder> implements Filterable, ListAdapter {

    private Context context;
    private List<Exercise> exerciseList;
    private  List<Exercise> exerciseListFiltered;
    private Activity activity;
    private int dayInDb;


    public ChooseExerciseAdapter(Context context, List<Exercise> exerciseList, Activity activity, int dayInDb) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.exerciseListFiltered = exerciseList;
        this.activity = activity;
        this.dayInDb = dayInDb;

    }

    @Override
    public ChooseExerciseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_choose_exercise, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ChooseExerciseAdapter.ViewHolder holder, int position) {

        Exercise exercise = exerciseListFiltered.get(position);


        holder.exercise = exercise;
        holder.exerciseName.setText(exercise.getName());
        holder.exerciseKind.setText(exercise.getKindOfExercise());
    }

    @Override
    public int getItemCount() {
        return exerciseListFiltered.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString ="";
                if(charSequence!=null)
                {
                    charString = charSequence.toString();
                    Log.d("char",charString);

                }

                if (charString.isEmpty() || charString.length()==0) {

                    exerciseListFiltered = exerciseList;
                } else {

                    List<Exercise> filteredList = new ArrayList<>();

                    for (Exercise e : exerciseList) {

                        if (e.getName().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(e);
                        }
                    }

                    exerciseListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = exerciseListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                exerciseListFiltered = (ArrayList<Exercise>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView exerciseName;
        private TextView exerciseKind;
        private Exercise exercise;

        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;


            exercise = new Exercise();
            exerciseName = (TextView) view.findViewById(R.id.name_exercise);
            exerciseKind = (TextView) view.findViewById(R.id.kind_exercise);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChooseExerciseDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXERCISE_DATE", dayInDb);
                    intent.putExtra("EXERCISE", exercise);
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}
