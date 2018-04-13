package com.pracainz20.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pracainz20.Model.Mapper.UserParameterMapper;
import com.pracainz20.Model.Meal;
import com.pracainz20.Model.Product;
import com.pracainz20.Model.UserParameter;
import com.pracainz20.R;
import com.pracainz20.Util.CalendarManagement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pracainz20.Model.Mapper.UserParameterMapper.mapper;

/**
 * Created by Grzechu on 10.04.2018.
 */

public class ChooseProductAdapter extends RecyclerView.Adapter<ChooseProductAdapter.ViewHolder> implements Filterable, ListAdapter{

    private Context context;
    private List<Product> products;
    private List<Product> productsFiltered;
    private Activity activity;

    public ChooseProductAdapter(Context context, List<Product> products, Activity activity) {
        this.context = context;
        this.products = products;
        this.productsFiltered = products;
        this.activity = activity;
    }

    @Override
    public ChooseProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_choose_product, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ChooseProductAdapter.ViewHolder holder, int position) {

        Product product = productsFiltered.get(position);

        holder.productName.setText(product.getName());
        holder.fatVal.setText(product.getLipid().toString());
        holder.protVal.setText(product.getProtein().toString());
        holder.carbVal.setText(product.getCarb().toString());
        holder.kalVal.setText(product.getKcal().toString());

    }

    @Override
    public int getItemCount() {
        return productsFiltered.size();
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

                }

                if (charString.isEmpty()) {

                    productsFiltered = products;
                } else {

                    List<Product> filteredList = new ArrayList<>();

                    for (Product p : products) {

                        if (p.getName().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(p);
                        }
                    }

                    productsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productsFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;
        private TextView protVal;
        private TextView carbVal;
        private TextView fatVal;
        private TextView kalVal;
        //// dialog value
        private TextView prot_val1;
        private TextView carb_val1;
        private TextView fat_val1;
        private TextView kcal_val1;


        public ViewHolder(View itemView, Context ctx) {
            super(itemView);

            context =ctx;

            productName = (TextView) itemView.findViewById(R.id.choose_product_name);
            protVal = (TextView) itemView.findViewById(R.id.choose_product_prot);
            carbVal = (TextView) itemView.findViewById(R.id.choose_product_carb);
            fatVal = (TextView) itemView.findViewById(R.id.choose_product_fat);
            kalVal = (TextView) itemView.findViewById(R.id.choose_product_kal);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
                    final View mView = layoutInflaterAndroid.inflate(R.layout.dialog_product, null);
                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
                    alertDialogBuilderUserInput.setView(mView);



                    TextView product_name1 = (TextView) mView.findViewById(R.id.dialog_product_name);
                    product_name1.setText(productName.getText().toString().trim());

                    final EditText portion_val = (EditText) mView.findViewById(R.id.dialog_amount_portion_val);
                    portion_val.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Log.d("refresh","execute");
                            Float portion;
                            if(portion_val.getText().toString().isEmpty()){
                               portion=0f;
                            }else{
                                portion = Float.parseFloat(String.valueOf(portion_val.getText()));

                            }

                            prot_val1 = (TextView) mView.findViewById(R.id.dialog_prot_val);
                            Double prot = Double.parseDouble(protVal.getText().toString().trim());
                            prot_val1.setText(String.valueOf(new DecimalFormat("##.#").format(portion/100*prot)));

                            carb_val1 = (TextView) mView.findViewById(R.id.dialog_carb_val);
                            Double carb = Double.parseDouble(carbVal.getText().toString().trim());
                            carb_val1.setText(String.valueOf(new DecimalFormat("##.#").format(portion/100*carb)));


                            fat_val1 = (TextView) mView.findViewById(R.id.dialog_fat_val);
                            Double fat = Double.parseDouble(fatVal.getText().toString().trim());
                            fat_val1.setText(String.valueOf(new DecimalFormat("##.#").format(portion/100*fat)));

                            kcal_val1 = (TextView) mView.findViewById(R.id.dialog_kcal_val);
                            Double kcal = Double.parseDouble(kalVal.getText().toString().trim());
                            kcal_val1.setText(String.valueOf(new DecimalFormat("##.#").format(portion/100*kcal)));
                        }
                    });






                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("Wstaw", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {



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
            });

        }
    }
}
