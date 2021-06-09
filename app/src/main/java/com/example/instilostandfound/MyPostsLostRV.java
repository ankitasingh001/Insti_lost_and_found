package com.example.instilostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Activity that displays all lost items for the current user
 */

public class MyPostsLostRV extends AppCompatActivity implements MyPostsAdaptor.OnItemClickListener,Serializable {
    private RecyclerView mrecyclerView;
    private MyPostsAdaptor mAdaptor;

    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private List<CreateFoundObject> mPosts;
    private String username =null;


    /**
     * Populates the view based on filter values via shared preferences
     * @param savedInstanceState Current state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts_lost_rv);

        username = getIntent().getStringExtra("username");
        mrecyclerView = findViewById(R.id.my_posts_rv);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPosts = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("LostData");
        mStorage = FirebaseStorage.getInstance();
        Query usernamequery = mDatabaseRef.orderByChild("ldap").equalTo((username+"@iitb.ac.in").toLowerCase());
        Log.v("query",usernamequery.toString());

        usernamequery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){

                    CreateFoundObject post = postSnapShot.getValue(CreateFoundObject.class);
                    post.setKey(postSnapShot.getKey());
                    mPosts.add(post);
                }

                mAdaptor = new MyPostsAdaptor(MyPostsLostRV.this, mPosts);
                mrecyclerView.setAdapter(mAdaptor);
                mAdaptor.setOnItemClickListener(MyPostsLostRV.this);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MyPostsLostRV.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Method to open the item details activity
     * @param position Position of the item on the screen
     */
    @Override
    public void onItemClick(int position) {
        Log.v("position =",String.valueOf(position));
        CreateFoundObject selectedItem = mPosts.get(position);
        final String selectedKey = selectedItem.getKey();
        Intent intent = new Intent(MyPostsLostRV.this, ItemDetails.class);
        intent.putExtra("FoundObject", selectedItem);
        intent.putExtra("CallingClass","MyPosts");
        intent.putExtra("username",username);
        startActivity(intent);
    }
    /**
     * Method to open item edit activity
     * @param position Position of the item on the screen
     */
    @Override
    public void oneditClick(int position) {
        Log.v("position =",String.valueOf(position));
        CreateFoundObject selectedItem = mPosts.get(position);
        final String selectedKey = selectedItem.getKey();
        Intent intent = new Intent(MyPostsLostRV.this, LostItem.class);
        intent.putExtra("FoundObject", selectedItem);
        intent.putExtra("CallingClass","MyPosts");
        intent.putExtra("username",username);
        startActivity(intent);
    }
    /**
     * Method to delete item
     * @param position Position of the item on the screen
     */
    @Override
    public void onDeleteClick(int position) {
        Log.v("position =",String.valueOf(position));
        CreateFoundObject selectedItem = mPosts.get(position);
        final String selectedKey = selectedItem.getKey();
        Log.v("selected key =",selectedKey);
        Log.v("url of image =",selectedItem.getImageUrl());
        if(selectedItem.getImageUrl()=="NO-IMAGE") {
            StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Toast.makeText(MyPostsLostRV.this, "Item deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyPostsLostRV.this, MyPostsLostRV.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else
        {
            mDatabaseRef.child(selectedKey).removeValue();
            Toast.makeText(MyPostsLostRV.this, "Item deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyPostsLostRV.this, MyPostsLostRV.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }
    }
}
