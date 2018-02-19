package com.pracainz20.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pracainz20.Adapter.PersonalDetailAdapter;
import com.pracainz20.Model.Mapper.UserParameterMapper;
import com.pracainz20.Model.UserParameter;
import com.pracainz20.Model.WelcomeData;
import com.pracainz20.R;
import com.pracainz20.Util.CalendarManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.pracainz20.Model.Mapper.UserParameterMapper.mapper;

public class PersonalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<WelcomeData> data;
    static View.OnClickListener myOnClickListener;
    final Context c = this;
    private TextView dateConfirmation;
    private ImageButton nextDate;
    private ImageButton previousDate;
    private EditText userInputDialogEditText;
    private TextView userInputDialogTextViewTitle;
    private TextView textViewUnit;
    private String currentValue;
    private Integer current_id;
    private TextView textViewValue;
    RecyclerView.ViewHolder viewHolder;
    private ProgressDialog mProgressDialog;
    private String[] values = {"", "", "", "", "", "", "", ""};
    private String[] titles = {"Waga", "Obwód szyi", "Obwód bioder", "Obwód tali", " Obwód klatki piersiowej", "Obwód lewego bicepsa", "Obwód prawego bicepsa", "Zdjęcie sylwetki"};
    private String[] units = {"kg", "cm", "cm", "cm", "cm", "cm", "cm", ""};
    private Integer[] id_ = {0, 1, 2, 3, 4, 5, 6, 7};
    private Integer dayInDB = 0;
    private int counter_entries = 0;


    //FIREBASE
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference currenUserDb;
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mój profil");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dateConfirmation = (TextView) findViewById(R.id.date_personal_textView);
        nextDate = (ImageButton) findViewById(R.id.right_date_personal_button);
        previousDate = (ImageButton) findViewById(R.id.left_date_personal_button);

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_personal);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        userid = mUser.getUid();
        currenUserDb = mDatabaseReference.child("MUsersParameters").child(userid);
        mProgressDialog = new ProgressDialog(this);
        dateConfirmation.setText(CalendarManagement.getDate(dayInDB));


        Log.d("DAYINDB", getDayInDB().toString());
        data = new ArrayList<WelcomeData>();
        for (int i = 0; i < 8; i++) {
            data.add(new WelcomeData(
                    getTitles()[i],
                    getValues()[i],
                    getUnits()[i]
            ));
        }

        adapter = new PersonalDetailAdapter(data);

        recyclerView.setAdapter(adapter);

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
    }

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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

            userInputDialogTextViewTitle.setText(titles[getValue(v)]);
            textViewUnit.setText(units[getValue(v)]);
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
                            values[current_id] = currentValue;
                            userInputDialogEditText.setText(values[current_id]);
                            textViewValue.setText(values[current_id]);

                            Log.d("wartosc z methodMapper", values[current_id]);

                            currenUserDb.child(CalendarManagement.getDateToSaveDatabase(dayInDB)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, Object> dataToSave = new HashMap<>();

                                    UserParameter userParameter = new UserParameter();
                                    UserParameterMapper.methodsMapper(current_id, values[current_id], userParameter);

                                    dataToSave.put(UserParameterMapper.mapper(current_id), UserParameterMapper.methodsGetMapper(current_id, userParameter));
                                    currenUserDb.child(CalendarManagement.getDateToSaveDatabase(dayInDB)).child(mapper(current_id)).setValue(dataToSave);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("The read failed: " + databaseError.getCode());
                                }
                            });

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

        private Integer getValue(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewTitle
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewTitle);
            String selectedTitle = (String) textViewTitle.getText();
            int selectedItemId = -1;
            for (int i = 0; i < 8; i++) {
                if (selectedTitle.equals(titles[i])) {
                    selectedItemId = getId_()[i];
                }
            }
            Map<String, String> welcomeData = new HashMap<>();
            welcomeData.put(titles[selectedItemId], values[selectedItemId]);

            return selectedItemId;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("DAYINDBinONstart", getDayInDB().toString());
        dateConfirmation.setText(CalendarManagement.getDate(dayInDB));

        Log.d("CAELNDARDAYINDB", CalendarManagement.getDateToSaveDatabase(dayInDB));

        Log.d("PATH", currenUserDb.toString());

        Log.d("licznik_pocz ", String.valueOf(counter_entries));



        currenUserDb.child(CalendarManagement.getDateToSaveDatabase(dayInDB)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.d("Child_ADDED", "wjescie do metody childAded");
                if (!((Activity) c).isFinishing()) {
                    mProgressDialog.show();
                }
                Log.d("licznikkkk przed", String.valueOf(counter_entries));
                if (dataSnapshot.getValue() != null) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    for (int i = 0; i < 8; i++) {
                        //values[i]=dataToSave.get(mapper(i));
                        if (map.keySet().contains(UserParameterMapper.mapper(i))) {
                            Log.d("Child_ADDED", "wjescie do mapera kluczy");
                            values[i] = String.valueOf(map.get(UserParameterMapper.mapper(i)));

                        }

                    }

                } else {
                    Log.d("NULL_PARA_chang", "null w para");
                }


                data = new ArrayList<WelcomeData>();
                for (int i = 0; i < 8; i++) {

                    data.add(new WelcomeData(
                            getTitles()[i],
                            getValues()[i],
                            getUnits()[i]
                    ));
                }


                adapter = new PersonalDetailAdapter(data);
                recyclerView.setAdapter(adapter);

                counter_entries = counter_entries + 1;


                if (!((Activity) c).isFinishing()) {
                    mProgressDialog.dismiss();
                }
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
        if (counter_entries == 0) {
            data = new ArrayList<WelcomeData>();
            for (int i = 0; i < 8; i++) {

                values[i] = "";


                data.add(new WelcomeData(
                        getTitles()[i],
                        getValues()[i],
                        getUnits()[i]
                ));
            }


            adapter = new PersonalDetailAdapter(data);
            recyclerView.setAdapter(adapter);
        }
        Log.d("licznik na końcu ", String.valueOf(counter_entries));

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateNextDay() {
        dayInDB = dayInDB + 1;
        counter_entries = 0;
        onStart();
    }

    private void updatePreviousDay() {
        dayInDB = dayInDB - 1;
        counter_entries = 0;
        onStart();

    }

    public String[] getValues() {
        return values;
    }

    public Integer[] getId_() {
        return id_;
    }

    public static ArrayList<WelcomeData> getData() {
        return data;
    }

    public static void setData(ArrayList<WelcomeData> data) {
        PersonalActivity.data = data;
    }

    public static View.OnClickListener getMyOnClickListener() {
        return myOnClickListener;
    }

    public String[] getTitles() {
        return titles;
    }

    public String[] getUnits() {
        return units;
    }

    public Integer getDayInDB() {
        return dayInDB;
    }


}