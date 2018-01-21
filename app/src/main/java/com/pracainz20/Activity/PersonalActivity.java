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
import android.widget.Button;
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
import com.google.firebase.storage.StorageReference;
import com.pracainz20.Adapter.WelcomeAdapter;
import com.pracainz20.Model.UserParameter;
import com.pracainz20.Model.WelcomeData;
import com.pracainz20.R;
import com.pracainz20.Util.CalendarManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Button confirmationDiaryButton;
    private EditText userInputDialogEditText;
    private TextView userInputDialogTextViewTitle;
    private TextView textViewUnit;
    private String currentValue;
    private Integer current_id;
    private TextView textViewValue;
    RecyclerView.ViewHolder viewHolder;
    private ProgressDialog mProgressDialog;
    private String[] values={"", "", "", "","","","",""};
    private String[] titles = {"Waga", "Obwód szyi", "Obwód bioder", "Obwód tali" ," Obwód klatki piersiowej","Obwód lewego bicepsa","Obwód prawego bicepsa","Zdjęcie sylwetki"};
    private String[] units = {"kg", "cm", "cm", "cm","cm","cm","cm",""};
    private Integer[] id_ = {0, 1, 2, 3,4,5,6,7};
    private Integer dayInDB=0;
    private int counter_entries;
    private CalendarManagement calendarManagement;
    private Map<String,String> dataToSave;


    //FIREBASE
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private StorageReference mFirebaseStorage;
    private DataSnapshot dataSnapshot;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference currenUserDb;
    private String userid;
    private UserParameter userParameter;
    private List<UserParameter> userParameters;

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

        confirmationDiaryButton = (Button) findViewById(R.id.buttonConfirmationPersonal);

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
        calendarManagement = new CalendarManagement();
        dateConfirmation.setText(calendarManagement.getDate(dayInDB));


        Log.d("DAYINDB",getDayInDB().toString());
        data = new ArrayList<WelcomeData>();
        for (int i = 0; i < 8; i++) {
            data.add(new WelcomeData(
                    getTitles()[i],
                    getValues()[i],
                    getUnits()[i]
            ));
        }

        adapter = new WelcomeAdapter(data);
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
                            values[current_id]=currentValue;
                            userInputDialogEditText.setText(values[current_id]);
                            textViewValue.setText( values[current_id]);

                            Log.d("wartosc z methodMapper",values[current_id]);

                            currenUserDb.child(calendarManagement.getDateToSaveDatabase(dayInDB)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                        Map<String,Object> dataToSave = new HashMap<>();
                                    try{

                                        UserParameter userParameter = dataSnapshot.getValue(UserParameter.class);
                                        methodsMapper(current_id,values[current_id],userParameter);
                                        userParameter.setKey(dataSnapshot.getKey());

                                        currenUserDb.child(calendarManagement.getDateToSaveDatabase(dayInDB)).setValue( dataToSave.put(mapper(current_id),methodsGetMapper(current_id,userParameter)));
                                        Log.d("Klucz: ",userParameter.getKey());
                                    }catch (Throwable e){
                                        Log.d("NULL_update", "null przy update");
                                        UserParameter userParameter = new UserParameter();
                                        methodsMapper(current_id,values[current_id],userParameter);
                                        Log.d("wartosc_mappera",mapper(current_id));
                                       // Log.d("wartosc: ", methodsGetMapper(current_id, userParameter).toString());
                                        //Log.d("wartosc_mapy:",(dataToSave.put("weight","1")).toString());
                                        dataToSave.put(mapper(current_id),methodsGetMapper(current_id,userParameter));
                                        currenUserDb.child(calendarManagement.getDateToSaveDatabase(dayInDB)).child(mapper(current_id)).setValue(dataToSave);
                                    }


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

        private Integer getValue(View v){
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
            Map<String,String> welcomeData = new HashMap<>();
            welcomeData.put(titles[selectedItemId],values[selectedItemId]);

            return selectedItemId;
        }


    }

    private String mapper(Integer selectedItemId){
        String columnName=null;
        if(selectedItemId==0){columnName="weight";}
        else if(selectedItemId==1){columnName="neckCircuit";}
        else if(selectedItemId==2){columnName="hipCircuit";}
        else if(selectedItemId==3){columnName="waistCircuit";}
        else if(selectedItemId==4){columnName="chestCircuit";}
        else if(selectedItemId==5){columnName="leftBicepsCircuit";}
        else if(selectedItemId==6){columnName="rightBicepsCircuit";}
        else if(selectedItemId==7){columnName="imageOfProfile";}
        return columnName;
    }

    private void methodsMapper(Integer selectedItemId, String currentValue, UserParameter userParameter){


        switch(selectedItemId) {
            case 0:
                userParameter.setWeight(currentValue);
                break;
            case 1:
                userParameter.setNeckCircuit(currentValue);
                break;
            case 2:
                userParameter.setHipCircuit(currentValue);
                break;
            case 3:
                userParameter.setWaistCircuit(currentValue);
                break;
            case 4:
                userParameter.setChestCircuit(currentValue);
                break;
            case 5:
                userParameter.setLeftBicepsCircuit(currentValue);
                break;
            case 6:
                userParameter.setRightBicepsCircuit(currentValue);
                break;
            case 7:
                userParameter.setImageOfProfile(currentValue);
                break;
        }
    }

    private String methodsGetMapper(Integer selectedItemId,UserParameter userParameter){

        String result = null;
        if(selectedItemId==0)
            result=userParameter.getWeight();

        else if(selectedItemId==1)
            result= userParameter.getNeckCircuit();

        else if(selectedItemId==2)
            result=userParameter.getHipCircuit();

        else if(selectedItemId==3)
            result=userParameter.getWaistCircuit();

        else if(selectedItemId==4)
            result=userParameter.getChestCircuit();

        else if(selectedItemId==5)
            result=userParameter.getLeftBicepsCircuit();

        else if(selectedItemId==6)
            result=userParameter.getRightBicepsCircuit();

        else if(selectedItemId==7)
            result=userParameter.getImageOfProfile();

        return result;
    }





    @Override
    protected void onStart() {
        super.onStart();
        Log.d("DAYINDBinONstart",getDayInDB().toString());
        CalendarManagement calendarManagement = new CalendarManagement();
        dateConfirmation.setText(calendarManagement.getDate(dayInDB));

        userParameters = new ArrayList<>();


        Log.d("CAELNDARDAYINDB", calendarManagement.getDateToSaveDatabase(dayInDB));
        Log.d("REFdoDaty",  currenUserDb.child(calendarManagement.getDateToSaveDatabase(dayInDB)).toString());

        Log.d("licznik_pocz ", String.valueOf(counter_entries));
        currenUserDb.child(calendarManagement.getDateToSaveDatabase(dayInDB)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Log.d("Child_ADDED","wjescie do metody childAded");
                if(!((Activity) c).isFinishing())
                {
                    mProgressDialog.show();
                }
                Map<String, String> dataToSave = new HashMap<>();


                try{


                    /*
                    dataToSave.put("neckCircuit",dataSnapshot.child("neckCircuit").getValue(String.class));

                    dataToSave.put("weight",dataSnapshot.child("weight").getValue(String.class));
                    Log.d("waga",dataToSave.get("weight"));
                    dataToSave.put("hipCircuit",dataSnapshot.child("hipCircuit").getValue(String.class));
                    dataToSave.put("waistCircuit",dataSnapshot.child("waistCircuit").getValue(String.class));
                    Log.d("obwodTali",dataToSave.get("waistCircuit"));
                    dataToSave.put("chestCircuit",dataSnapshot.child("chestCircuit").getValue(String.class));
                    dataToSave.put("leftBicepsCircuit",dataSnapshot.child("leftBicepsCircuit").getValue(String.class));
                    dataToSave.put("rightBicepsCircuit",dataSnapshot.child("rightBicepsCircuit").getValue(String.class));
                    dataToSave.put("imageOfProfile",dataSnapshot.child("imageOfProfile").getValue(String.class));

                    */
                    //userParameter = dataSnapshot.getValue(UserParameter.class);
                    /*
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    for(DataSnapshot dataSnapshot1 : dataSnapshots){
                        int i =0;
                        values[i]=dataSnapshot1.getValue(String.class);
                        Log.d("loop_snap",values[i].toString());
                        i++;

                    }
                    */
                    values[counter_entries]=dataSnapshot.child(mapper(counter_entries)).getValue(String.class);


                    counter_entries=counter_entries+1;
                    Log.d("dzieci", String.valueOf(dataSnapshot.getChildrenCount()));
                    /*
                    values[0]=dataSnapshot.child("weight").getValue(String.class);
                    Log.d("WAGA:",values[0]);

                    values[1]=dataSnapshot.child("neckCircuit").getValue(String.class);
                    Log.d("obwod_szyi:",values[1]);
                    values[2]=dataSnapshot.child("hipCircuit").getValue(String.class);
                    values[3]=dataSnapshot.child("waistCircuit").getValue(String.class);
                    values[4]=dataSnapshot.child("chestCircuit").getValue(String.class);
                    values[5]=dataSnapshot.child("leftBicepsCircuit").getValue(String.class);
                    values[6]=dataSnapshot.child("rightBicepsCircuit").getValue(String.class);
                    values[7]=dataSnapshot.child("imageOfProfile").getValue(String.class);
                    */
                }catch (Throwable e){
                    Log.d("NULL_PARA_chang","null w para");

                }


                data = new ArrayList<WelcomeData>();
                for (int i = 0; i < 8; i++) {
                    //values[i]=dataToSave.get(mapper(i));
                    data.add(new WelcomeData(
                            getTitles()[i],
                            getValues()[i],
                            getUnits()[i]
                    ));
                }


                adapter = new WelcomeAdapter(data);
                recyclerView.setAdapter(adapter);


                if(!((Activity) c).isFinishing())
                {
                    mProgressDialog.dismiss();
                }

                Log.d("wart", String.valueOf(values[counter_entries]));
                Log.d("licznikkkk ", String.valueOf(counter_entries));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("child","chand");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("child","remove");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("child","moved");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("child","cancled");

            }
        });
        if(counter_entries==0){
            data = new ArrayList<WelcomeData>();
            for (int i = 0; i < 8; i++) {

                    values[i]="";


                data.add(new WelcomeData(
                        getTitles()[i],
                        getValues()[i],
                        getUnits()[i]
                ));
            }


            adapter = new WelcomeAdapter(data);
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

    private void updateNextDay(){
        dayInDB = dayInDB +1;
        counter_entries=0;
        onStart();
    }

    private void updatePreviousDay(){
        dayInDB = dayInDB-1;
        counter_entries=0;
        onStart();

    }
    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public Integer[] getId_() {
        return id_;
    }

    public void setId_(Integer[] id_) {
        this.id_ = id_;
    }
    public static RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public static void setAdapter(RecyclerView.Adapter adapter) {
        PersonalActivity.adapter = adapter;
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
        PersonalActivity.recyclerView = recyclerView;
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

    public static void setMyOnClickListener(View.OnClickListener myOnClickListener) {
        PersonalActivity.myOnClickListener = myOnClickListener;
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

    public ProgressDialog getmProgressDialog() {
        return mProgressDialog;
    }

    public void setmProgressDialog(ProgressDialog mProgressDialog) {
        this.mProgressDialog = mProgressDialog;
    }

    public DatabaseReference getmDatabaseReference() {
        return mDatabaseReference;
    }

    public void setmDatabaseReference(DatabaseReference mDatabaseReference) {
        this.mDatabaseReference = mDatabaseReference;
    }

    public FirebaseDatabase getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public StorageReference getmFirebaseStorage() {
        return mFirebaseStorage;
    }

    public void setmFirebaseStorage(StorageReference mFirebaseStorage) {
        this.mFirebaseStorage = mFirebaseStorage;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    public FirebaseUser getmUser() {
        return mUser;
    }

    public void setmUser(FirebaseUser mUser) {
        this.mUser = mUser;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public DatabaseReference getCurrenUserDb() {
        return currenUserDb;
    }

    public void setCurrenUserDb(DatabaseReference currenUserDb) {
        this.currenUserDb = currenUserDb;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



    public UserParameter getUserParameter() {
        return userParameter;
    }

    public void setUserParameter(UserParameter userParameter) {
        this.userParameter = userParameter;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public String[] getUnits() {
        return units;
    }

    public void setUnits(String[] units) {
        this.units = units;
    }
    public Integer getDayInDB() {
        return dayInDB;
    }

    public void setDayInDB(Integer dayInDB) {
        this.dayInDB = dayInDB;
    }

}