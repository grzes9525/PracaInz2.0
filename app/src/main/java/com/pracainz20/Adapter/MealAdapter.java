package com.pracainz20.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pracainz20.Activity.ChooseProductActivity;
import com.pracainz20.Activity.DiaryActivity;
import com.pracainz20.Activity.UserProfileActivity;
import com.pracainz20.Model.Invitation;
import com.pracainz20.Model.Meal;
import com.pracainz20.Model.Product;
import com.pracainz20.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grzechu on 04.04.2018.
 */

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> implements Filterable{

    private Context context;
    private List<Meal> meals;
    private Activity activity;

    public MealAdapter(Context context, List<Meal> meals, Activity activity) {
        this.context = context;
        this.meals = meals;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_meal, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Meal meal = meals.get(position);

        holder.mealName.setText(meal.getName());

        holder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChooseProductActivity.class);
                intent.putExtra("MEAL_ID", meal.getPushId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
            }
        });

        holder.products.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.products.setLayoutManager(layoutManager);
        holder.products.setItemAnimator(new DefaultItemAnimator());


        RecyclerView.Adapter adapter = new MealProductAdapter(meal.getProducts());
        adapter.notifyDataSetChanged();
        holder.products.setAdapter(adapter);


    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mealName;
        private Button addProduct;
        private RecyclerView products;

        public ViewHolder(View itemView, Context ctx) {

            super(itemView);

            context = ctx;

            mealName = (TextView) itemView.findViewById(R.id.meal_name);
            addProduct = (Button) itemView.findViewById(R.id.add_product);
            products = (RecyclerView) itemView.findViewById(R.id.my_meal_recycle_view_in_card_view);
        }
    }
}