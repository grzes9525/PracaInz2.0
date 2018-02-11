package com.pracainz20.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pracainz20.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText firstName;
    private EditText lastName;
    private EditText phone_number;
    private EditText height;
    private EditText age;
    private Button createAccountBtn;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference currenUserDb;
    private DatabaseReference currenUserDbProfileImage;
    private FirebaseDatabase mDatabase;
    private StorageReference mFirebaseStorage;
    private ProgressDialog mProgressDialog;
    private ImageButton profilePic;
    private Uri resultUri = null;
    private Uri mImageUri;
    private final static int GALLERY_CODE = 1;
    private DataSnapshot dataSnapshot;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private String userid;
    private int counter;
    private Context c = this ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dane personalne");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userid = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");
        mDatabaseReference.keepSynced(true);
        Log.i("INFO_MREFERENCEDB", mDatabaseReference.toString());

        currenUserDb = mDatabaseReference.child(userid);
        currenUserDbProfileImage = mDatabaseReference.child("ProfileImages").child(userid);

        Log.d("ID clienta",FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFirebaseStorage = FirebaseStorage.getInstance().getReference();

        mProgressDialog = new ProgressDialog(this);

        firstName = (EditText) findViewById(R.id.personal_firstNameAct);
        lastName = (EditText) findViewById(R.id.personal_lastNameAct);
        phone_number = (EditText) findViewById(R.id.personal_phone_number);
        height = (EditText) findViewById(R.id.personal_height);
        age = (EditText) findViewById(R.id.personal_age);
        profilePic = (ImageButton) findViewById(R.id.personal_profilePic);
        createAccountBtn = (Button) findViewById(R.id.personal_createAccoutAct);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);

            }
        });

        counter = 0;

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        //Dałem w onCreate baza nadpisywała cropa
        final String[] profileImage = new String[1];

            mProgressDialog.show();

        currenUserDbProfileImage.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Child_ADDED","wjescie do metody childAded");



                if (dataSnapshot.getValue() != null) {
                    profileImage[0] = (String) dataSnapshot.getValue();
                    Log.d("wartosc:", profileImage[0]);
                }




                counter=counter+1;

                Log.d("counet", String.valueOf(counter));


                Log.d("lista:", profileImage[0]);

                Log.d("profileImage", profileImage[0]);
                Log.d("get(5)","nie jest null");
                Uri uri = Uri.parse(profileImage[0]);

                StorageReference storage = FirebaseStorage.getInstance()
                        .getReferenceFromUrl(String.valueOf(uri));
                Glide.with(c)
                        .using(new FirebaseImageLoader())
                        .load(storage)
                        .into(profilePic);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


            mProgressDialog.dismiss();



    }

    private void createNewAccount()  {

        final Map<String, Object> dataToSave = new HashMap<>();
        final Map<String, Object> dataToSaveWithImage = new HashMap<>();
        dataToSave.put("firstName",firstName.getText().toString().trim());
        dataToSave.put("lastName",lastName.getText().toString().trim());
        dataToSave.put("age",age.getText().toString().trim());
        dataToSave.put("height",height.getText().toString().trim());
        dataToSave.put("phoneNumber",phone_number.getText().toString().trim());

        // update the user profile information in Firebase database.
        if(resultUri==null || TextUtils.isEmpty((CharSequence) dataToSave.get("firstName")) || TextUtils.isEmpty((CharSequence) dataToSave.get("lastName")) ||
                TextUtils.isEmpty((CharSequence) dataToSave.get("age"))
                || TextUtils.isEmpty((CharSequence) dataToSave.get("height")) || TextUtils.isEmpty((CharSequence) dataToSave.get("phoneNumber"))){
            Toast.makeText(getApplicationContext(), "Niektóre pola zostały nie zmienione", Toast.LENGTH_LONG).show();
        }
        mProgressDialog.setMessage("Creating Account...");
        mProgressDialog.show();


        if(resultUri!=null){
            Log.d("creat account","wejscie de result uri");

            StorageReference imagePath = mFirebaseStorage.child("M_Profile_Pics")
                    .child(resultUri.getLastPathSegment());
            imagePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();

                    dataToSaveWithImage.put("profileImage",downloadurl.toString());
                    currenUserDbProfileImage.setValue(dataToSaveWithImage);
                    Log.d("resultUri",resultUri.toString());
                    Log.d("downloadUri",downloadurl.toString());
                }
            });
        }
        Log.d("maapa", String.valueOf(dataToSave.values()));
        currenUserDb.setValue(dataToSave);
        mProgressDialog.dismiss();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }



    @Override
    protected void onStart() {
        super.onStart();

        final List<String> values = new ArrayList<>();

            mProgressDialog.show();


        counter=0;
        currenUserDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Child_ADDED","wjescie do metody childAded");



                if (dataSnapshot.getValue() != null) {
                    String value = (String) dataSnapshot.getValue();
                    Log.d("wartosc:", value);
                    values.add(value);

                }




                counter=counter+1;
                if(values.size()==5){
                    firstName.setText(values.get(1), TextView.BufferType.EDITABLE);
                    lastName.setText(values.get(3), TextView.BufferType.EDITABLE);
                    phone_number.setText(values.get(4), TextView.BufferType.EDITABLE);
                    height.setText(values.get(2), TextView.BufferType.EDITABLE);
                    age.setText(values.get(0), TextView.BufferType.EDITABLE);
                }
                Log.d("counet", String.valueOf(counter));


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


            mProgressDialog.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            Uri mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);



        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.d("Crop", String.valueOf(resultUri));
                profilePic.setImageURI(resultUri);
                Log.d("Crop2:","wywolanie crop2");

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            if (mUser != null && mAuth != null) {
                mAuth.signOut();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        }

        return super.onOptionsItemSelected(item);
    }

    public EditText getFirstName() {
        return firstName;
    }

    public void setFirstName(EditText firstName) {
        this.firstName = firstName;
    }

    public EditText getLastName() {
        return lastName;
    }

    public void setLastName(EditText lastName) {
        this.lastName = lastName;
    }

    public EditText getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(EditText phone_number) {
        this.phone_number = phone_number;
    }

    public EditText getHeight() {
        return height;
    }

    public void setHeight(EditText height) {
        this.height = height;
    }

    public EditText getAge() {
        return age;
    }

    public void setAge(EditText age) {
        this.age = age;
    }

    public Button getCreateAccountBtn() {
        return createAccountBtn;
    }

    public void setCreateAccountBtn(Button createAccountBtn) {
        this.createAccountBtn = createAccountBtn;
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

    public ProgressDialog getmProgressDialog() {
        return mProgressDialog;
    }

    public void setmProgressDialog(ProgressDialog mProgressDialog) {
        this.mProgressDialog = mProgressDialog;
    }

    public ImageButton getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(ImageButton profilePic) {
        this.profilePic = profilePic;
    }

    public Uri getResultUri() {
        return resultUri;
    }

    public void setResultUri(Uri resultUri) {
        this.resultUri = resultUri;
    }

    public static int getGalleryCode() {
        return GALLERY_CODE;
    }


    public DatabaseReference getCurrenUserDb() {
        return currenUserDb;
    }

    public void setCurrenUserDb(DatabaseReference currenUserDb) {
        this.currenUserDb = currenUserDb;
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


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}