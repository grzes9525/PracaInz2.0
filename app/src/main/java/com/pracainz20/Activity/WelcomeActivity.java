package com.pracainz20.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pracainz20.Adapter.WelcomeAdapter;
import com.pracainz20.Data.WelcomeDB;
import com.pracainz20.Model.WelcomeData;
import com.pracainz20.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<WelcomeData> data;
    static View.OnClickListener myOnClickListener;
    final Context c = this;
    private Button confirmationDiaryButton;
    private EditText userInputDialogEditText;
    private TextView userInputDialogTextViewTitle;
    private TextView textViewUnit;
    private String currentValue;
    private Integer current_id;
    private TextView textViewValue;
    RecyclerView.ViewHolder viewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mój profil");
        setSupportActionBar(toolbar);

        confirmationDiaryButton = (Button) findViewById(R.id.buttonConfirmationDiary);

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_welcome);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<WelcomeData>();
        for (int i = 0; i < WelcomeDB.getTitles().length; i++) {
            data.add(new WelcomeData(
                    WelcomeDB.getTitles()[i],
                    WelcomeDB.getValues()[i],
                    WelcomeDB.getId_()[i],
                    WelcomeDB.getUnits()[i]

            ));
        }


        adapter = new WelcomeAdapter(data);
        recyclerView.setAdapter(adapter);
    }



    public class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }


        @Override
        public void onClick(View v) {


            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_diary, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
            alertDialogBuilderUserInput.setView(mView);

            userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
            userInputDialogTextViewTitle = (TextView) mView.findViewById(R.id.dialogTitle);
            textViewUnit = (TextView) mView.findViewById(R.id.dialogUnit);


            //String dialogTitle= getValue(v).keySet().toString();
            //String dialog = dialogTitle.substring(1,dialogTitle.length()-1);
            userInputDialogTextViewTitle.setText(WelcomeDB.getTitles()[getValue(v)]);
            textViewUnit.setText(WelcomeDB.getUnits()[getValue(v)]);
            //userInputDialogEditText.setText(WelcomeDB.values[getValue(v)]);
            current_id = getValue(v);
            viewHolder
                    = recyclerView.findViewHolderForPosition(current_id);
            textViewValue
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewValue);

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Wstaw", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {


                            currentValue = String.valueOf(userInputDialogEditText.getText());
                            ////welcome DB
                            WelcomeDB.getValues()[current_id]=currentValue;
                            userInputDialogEditText.setText(WelcomeDB.getValues()[current_id]);
                            textViewValue.setText(WelcomeDB.getValues()[current_id]);

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

        private Integer getValue(View v){
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewTitle
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewTitle);
            String selectedTitle = (String) textViewTitle.getText();
            int selectedItemId = -1;
            for (int i = 0; i < WelcomeDB.getTitles().length; i++) {
                if (selectedTitle.equals(WelcomeDB.getTitles()[i])) {
                    selectedItemId = WelcomeDB.getId_()[i];
                }
            }
            Map<String,String> welcomeData = new HashMap<>();
            welcomeData.put(WelcomeDB.getTitles()[selectedItemId],WelcomeDB.getValues()[selectedItemId]);

            return selectedItemId;
        }


    }


    public static RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(RecyclerView.Adapter adapter) {
        WelcomeActivity.adapter = adapter;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public static RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public static void setRecyclerView(RecyclerView recyclerView) {
        WelcomeActivity.recyclerView = recyclerView;
    }

    public static ArrayList<WelcomeData> getData() {
        return data;
    }

    public static void setData(ArrayList<WelcomeData> data) {
        WelcomeActivity.data = data;
    }

    public static View.OnClickListener getMyOnClickListener() {
        return myOnClickListener;
    }

    public static void setMyOnClickListener(View.OnClickListener myOnClickListener) {
        WelcomeActivity.myOnClickListener = myOnClickListener;
    }

    public Context getC() {
        return c;
    }

    public Button getConfirmationDiaryButton() {
        return confirmationDiaryButton;
    }

    public void setConfirmationDiaryButton(Button confirmationDiaryButton) {
        this.confirmationDiaryButton = confirmationDiaryButton;
    }

    public EditText getUserInputDialogEditText() {
        return userInputDialogEditText;
    }

    public void setUserInputDialogEditText(EditText userInputDialogEditText) {
        this.userInputDialogEditText = userInputDialogEditText;
    }

    public TextView getUserInputDialogTextViewTitle() {
        return userInputDialogTextViewTitle;
    }

    public void setUserInputDialogTextViewTitle(TextView userInputDialogTextViewTitle) {
        this.userInputDialogTextViewTitle = userInputDialogTextViewTitle;
    }

    public TextView getTextViewUnit() {
        return textViewUnit;
    }

    public void setTextViewUnit(TextView textViewUnit) {
        this.textViewUnit = textViewUnit;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getCurrent_id() {
        return current_id;
    }

    public void setCurrent_id(Integer current_id) {
        this.current_id = current_id;
    }

    public TextView getTextViewValue() {
        return textViewValue;
    }

    public void setTextViewValue(TextView textViewValue) {
        this.textViewValue = textViewValue;
    }

    public RecyclerView.ViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(RecyclerView.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }
}
