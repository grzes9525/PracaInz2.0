package com.pracainz20.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pracainz20.Model.Client;
import com.pracainz20.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PersonalDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText firstName;
    private EditText lastName;
    private EditText phone_number;
    private EditText height;
    private EditText age;
    private Button createAccountBtn;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference currenUserDb;
    private FirebaseDatabase mDatabase;
    private StorageReference mFirebaseStorage;
    private ProgressDialog mProgressDialog;
    private ImageButton profilePic;
    private Uri resultUri = null;
    private final static int GALLERY_CODE = 1;

    private DataSnapshot dataSnapshot;



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

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");

        currenUserDb = mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MBlog_Profile_Pics");



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

    }

    private void createNewAccount() {
        Client client = new Client();

        client.setFirstName(firstName.getText().toString().trim());
        client.setLastName(lastName.getText().toString().trim());
        client.setPhoneNumber(phone_number.getText().toString().trim());
        client.setHeight(height.getText().toString().trim());
        client.setAge(age.getText().toString().trim());

        // update the user profile information in Firebase database.
        if(TextUtils.isEmpty(client.getFirstName()) || TextUtils.isEmpty(client.getLastName()) ||
                TextUtils.isEmpty(client.getPhoneNumber())
                || TextUtils.isEmpty(client.getHeight()) || TextUtils.isEmpty(client.getAge())){
            Toast.makeText(getApplicationContext(), "Wszystkie pola musza byc wypełnione.", Toast.LENGTH_LONG).show();
        }
        mProgressDialog.setMessage("Creating Account...");
        mProgressDialog.show();

        currenUserDb.setValue(client);
        currenUserDb.child("image").setValue(resultUri.toString());

        mProgressDialog.dismiss();

        //send users to postList
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        currenUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //test

                Client client = dataSnapshot.getValue(Client.class);

                firstName.setText(client.getFirstName(), TextView.BufferType.EDITABLE);
                lastName.setText(client.getLastName(), TextView.BufferType.EDITABLE);
                phone_number.setText(client.getPhoneNumber(), TextView.BufferType.EDITABLE);
                height.setText(client.getHeight(), TextView.BufferType.EDITABLE);
                age.setText(client.getAge(), TextView.BufferType.EDITABLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

                profilePic.setImageURI(resultUri);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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

}
