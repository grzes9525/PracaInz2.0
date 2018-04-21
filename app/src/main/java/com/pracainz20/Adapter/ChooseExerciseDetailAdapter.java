package com.pracainz20.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.pracainz20.Model.Exercise;
import com.pracainz20.Model.Serie;
import com.pracainz20.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzechu on 19.04.2018.
 */

public class  ChooseExerciseDetailAdapter  extends RecyclerView.Adapter<ChooseExerciseDetailAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Serie> series;
    private List<Serie> changeSeries;

    public ChooseExerciseDetailAdapter(Context context, List<Serie> series) {
        this.context = context;
        this.series = series;
        changeSeries = new ArrayList<>();
        for(Serie s : series){
            s = new Serie();
            s.setWeight(0.0);
            s.setRepetitions(0);
            changeSeries.add(s);
        }
    }

    @Override
    public ChooseExerciseDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_exercise_detail, parent, false);
        return new ViewHolder(view,context);    }

    @Override
    public void onBindViewHolder(ChooseExerciseDetailAdapter.ViewHolder holder, final int position) {

        final Serie serie = series.get(position);

        holder.numberSeriesDetail.setText(String.valueOf(serie.getIdSerie()));
        holder.numberRepetitionDetail.setText(String.valueOf(serie.getRepetitions()));
        changeSeries.get(position).setRepetitions(serie.getRepetitions());

        holder.numberRepetitionDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty() && s!=null){
                    Log.d("lista", String.valueOf(changeSeries.size()));

                    changeSeries.get(position).setRepetitions(Integer.valueOf(s.toString()));
                    Log.d("seria", String.valueOf(changeSeries.get(position).getRepetitions()));
                }
            }
        });
        holder.weightDetail.setText(String.valueOf(serie.getWeight()));
        changeSeries.get(position).setWeight(serie.getWeight());

        holder.weightDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty() && s!=null) {

                    changeSeries.get(position).setWeight(Double.valueOf(s.toString()));
                    Log.d("seria", String.valueOf(changeSeries.get(position).getWeight()));

                }
            }
        });

    }

    public List<Serie> getChangeSeries(){
        return changeSeries;
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    public Serie getItem(int position){
        return series.get(position);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView numberSeriesDetail;
        private EditText numberRepetitionDetail;
        private EditText weightDetail;
        public ViewHolder(View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            numberSeriesDetail = (TextView) itemView.findViewById(R.id.number_series_detail);
            numberRepetitionDetail = (EditText) itemView.findViewById(R.id.number_repetition_detail_val);
            weightDetail = (EditText) itemView.findViewById(R.id.weight_detail_val);
        }
    }


}
