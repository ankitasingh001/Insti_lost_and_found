package com.example.instilostandfound;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


/**
 * Activity to post the details of the lost item -> includes image
 */
public class LostItem extends AppCompatActivity implements View.OnClickListener, Serializable {

    private static final String TAG = LostItem.class.getSimpleName(); ;
    EditText title;
    TextView place;
    EditText desc;
    TextView date;
    Spinner category;
    String username;
    ImageView camera;
    // various item categries defined here
    String[] items = new String[]{"Others","Electronics", "Documents", "Clothes","Furniture","Accessories"};
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private DatabaseReference mDatabase;
    public Uri mImageUri;
    public Uri uploadSessionUri;
    int PICK_IMAGE_REQUEST = 111;
    public String callingclass;
    ProgressDialog pd;
    private StorageTask mUploadTask;
    private boolean updatedata;
    public String tempUri;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("LostData");
    StorageReference mStorage  = FirebaseStorage.getInstance().getReference("Images");
    FirebaseStorage mStorageRef =  FirebaseStorage.getInstance();

    DatePickerDialog.OnDateSetListener dateSetListener;
    /**
     * On create method for lost item activity
     * @param savedInstanceState default param takes input the details of the object lost
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username = getIntent().getStringExtra("username");
        callingclass = getIntent().getStringExtra("CallingClass");
        title = findViewById(R.id.title);
        place = findViewById(R.id.place);
        desc = findViewById(R.id.desc);
        date = findViewById(R.id.date);
        category = findViewById(R.id.category);
        camera = findViewById(R.id.camera);
        place.setOnClickListener(this);
        camera.setOnClickListener(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        date.setOnClickListener(this);


        if(callingclass.equals("MyPosts"))
        {
            CreateFoundObject objectfound = (CreateFoundObject)getIntent().getSerializableExtra("FoundObject");
            Log.v("desc",objectfound.getmDescription());
            title.setText(objectfound.getmTitle());
            place.setText(objectfound.getmLocation());
            desc.setText(objectfound.getmDescription());
            date.setText(objectfound.getmDateFound());
            Picasso.with(LostItem.this).load(objectfound.getImageUrl())
                    .fit()
                    .centerCrop()
                    .into(camera);
        }


        findViewById(R.id.submit).setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        category.setAdapter(adapter);
    }

    /**
     * Stores the data in firebase db
     */
    private void SubmitLost() {
        final String Title = title.getText().toString().trim();
        final String Place = place.getText().toString().trim();
        final String Desc = desc.getText().toString().trim();
       final String Date = date.getText().toString().trim();
        final String Category =  category.getSelectedItem().toString();
        updatedata = false;
        if (title.getText().toString().trim().isEmpty()) {
            title.setError("Please enter title");
            title.requestFocus();
            return;
        }

        if (place.toString().trim().isEmpty()) {
            place.setError("Please enter location");
            place.requestFocus();
            return;
        }


        if(callingclass.equals("MyPosts"))
        {
            final CreateFoundObject objectfound = (CreateFoundObject)getIntent().getSerializableExtra("LostObject");
            if(objectfound.getImageUrl().equals("NO-IMAGE"))
            {
                myRef.child(objectfound.getKey()).removeValue();
                Toast.makeText(LostItem.this, "Item Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LostItem.this, MyPostsFoundRV.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
            else
            {
                StorageReference imageRef = mStorageRef.getReferenceFromUrl(objectfound.getImageUrl());
                tempUri = objectfound.getImageUrl();
                updatedata = true;
                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myRef.child(objectfound.getKey()).removeValue();
                        Toast.makeText(LostItem.this, "Item Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LostItem.this, MyPostsFoundRV.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }

        if (mImageUri != null ) {
            pd.show();
            final StorageReference fileReference = mStorage.child(System.currentTimeMillis()
                    + "." + getExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);

                            mImageUri = null;
                            Toast.makeText(LostItem.this, "Upload successful", Toast.LENGTH_LONG).show();

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uploadSessionUri = uri;
                                    CreateFoundObject upload;
                                    java.util.Date datef = new Date();
                                    if(updatedata)
                                    {
                                        upload = new CreateFoundObject(username,Title ,tempUri,Place, Desc,
                                                Category, Date,"lost");
                                    }
                                    else {
                                        upload = new CreateFoundObject(username, Title, uri.toString(), Place, Desc,
                                                Category,Date, "lost");
                                    }
                                    String uploadId = myRef.push().getKey();
                                    Log.v("UploadID", uploadId);
                                    myRef.child(uploadId).setValue(upload);
                                    title.setText("");
                                    place.setText("");
                                    desc.setText("");
                                    date.setText("");
                                    pd.dismiss();
                                    Toast.makeText(LostItem.this,"SUBMIT SUCCESSFUL",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LostItem.this, MyPostsLostRV.class);
                                    intent.putExtra("username", username);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();


                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LostItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LostItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {

            Date datef = new Date();
            Toast.makeText(LostItem.this, "No file selected", Toast.LENGTH_SHORT).show();
            CreateFoundObject upload = new CreateFoundObject(username,Title ,"NO-IMAGE",Place, Desc,
                    Category, Date,"lost");
            String uploadId = myRef.push().getKey();
            Log.v("UploadID", uploadId);
            myRef.child(uploadId).setValue(upload);
            title.setText("");
            place.setText("");
            desc.setText("");
            date.setText("");
            Toast.makeText(LostItem.this,"SUBMIT SUCCESSFUL -NO IMAGE UPLOADED!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LostItem.this, MyPostsLostRV.class);
            intent.putExtra("username", username);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }




    }

    /**Method to get extension of type of image uploaded
     *
     * @param uri image url
     * @return extension name
     */
    private  String getExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    /**
     * On click for views and buttons of activity
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.submit:
                SubmitLost();
                break;
            case R.id.camera:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                break;

            case R.id.place:
                Fragment lostItemLocationFragment = new LostItemLocationFragment();
                Bundle bundle = new Bundle();
                if(place.getText().toString().trim().isEmpty()){

                    bundle.putDouble("Lat", 19.131482);
                    bundle.putDouble("Lon", 72.916129);

                }
                else{
                    String  coordinates;
                    Double lat=0.0,lon=0.0;
                    coordinates = place.getText().toString().trim();
                    Log.v("coordinatesbefore: ", coordinates);
                    Log.v("coordinates: ", coordinates);
                    lat = Double.parseDouble(coordinates.split(",")[0]);
                    lon = Double.parseDouble(coordinates.split(",")[1]);
                    bundle.putDouble("Lat", lat);
                    bundle.putDouble("Lon", lon);
                    bundle.putString("type", "Lost");
                }
                lostItemLocationFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.map_container,
                        lostItemLocationFragment).commit();
                break;


            case R.id.date:
                Calendar cal= Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(LostItem.this
                        ,android.R.style.Theme_Black,
                        dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month +1;
                        Log.d(TAG,"onDateSet: mm/dd/yyy: "+ month + "/" + day +"/" +year);
                        String dt= month+"/"+day+"/"+year;
                        date.setText(dt);

                    }
                };
                break;
        }
    }

    /**
     * Executed after image is selected
     * @param requestCode default param
     * @param resultCode default param
     * @param data default param
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            try {
                Picasso.with(LostItem.this).load(mImageUri)
                        .fit()
                        .centerCrop()
                        .into(camera);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
