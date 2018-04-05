package com.pracainz20.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pracainz20.Model.Product;
import com.pracainz20.R;

import java.util.List;

/**
 * Created by Grzechu on 05.04.2018.
 */

public class MealProductAdapter extends RecyclerView.Adapter<MealProductAdapter.ViewHolder> implements Filterable {

    private List<Product> products;


    public MealProductAdapter(List<Product> products) {
        this.products = products;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_product, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealProductAdapter.ViewHolder holder, int position) {

        Product product = products.get(position);

        holder.productName.setText(product.getName());
        holder.kcalVal.setText(product.getKcal().toString());

    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView kcalVal;

        public ViewHolder(View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.product_name);
            kcalVal = (TextView) itemView.findViewById(R.id.kcl_val);
        }
    }
}
