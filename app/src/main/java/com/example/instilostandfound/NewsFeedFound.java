package com.example.instilostandfound;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity displaying the news feed of found items
 */
public class NewsFeedFound extends AppCompatActivity implements NewsfeedAdaptor.OnItemClickListener, Serializable {
    private RecyclerView mrecyclerView;
    private NewsfeedAdaptor mAdaptor;
    LinearLayoutManager mLayoutManager; //for sorting
    SharedPreferences mSharedPref; //for saving sort settings
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private List<CreateFoundObject> mPosts;
    private String username =null;
    /**
     * Displays the found items based on applied category filter
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_found);

        username = getIntent().getStringExtra("username");
        mrecyclerView = findViewById(R.id.my_posts_rv);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPosts = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("FoundData");
        mStorage = FirebaseStorage.getInstance();

        //Actionbar
        ActionBar actionBar = getSupportActionBar();
        //set title
        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "Others");//where if no settingsis selected newest will be default
        //if (mSorting.equals("newest")) {
        mDatabaseRef.orderByChild("mCategory").equalTo(mSorting).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {

                    CreateFoundObject post = postSnapShot.getValue(CreateFoundObject.class);
                    post.setKey(postSnapShot.getKey());
                    mPosts.add(post);
                }

                mAdaptor = new NewsfeedAdaptor(NewsFeedFound.this, mPosts);
                mrecyclerView.setAdapter(mAdaptor);
                mAdaptor.setOnItemClickListener(NewsFeedFound.this);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(NewsFeedFound.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

    }

    /**
     * Displays the details of the item and the claim button
     * @param position position of the item on the screen
     */
    @Override
    public void onItemClick(int position) {
        Log.v("position =",String.valueOf(position));
        CreateFoundObject selectedItem = mPosts.get(position);
        final String selectedKey = selectedItem.getKey();
        Intent intent = new Intent(NewsFeedFound.this, ItemClaim.class);
        intent.putExtra("FoundObject", selectedItem);
        intent.putExtra("CallingClass","MyPosts");
        intent.putExtra("username",username);
        startActivity(intent);
    }


    /**
     * Create filter and search options for the user
     * @param menu menu items
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it present
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter as you type
                //firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //handle other action bar item clicks here
        if (id == R.id.action_sort) {
            //display alert dialog to choose sorting
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Displays the categories via which user can sort items
     */
    private void showSortDialog() {
        //options to display in dialog
        final String[] sortOptions = {"Others","Electronics", "Documents", "Clothes","Furniture","Accessories"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by") //set title
                .setIcon(R.drawable.camera) //set icon
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        // 0 means "Newest" and 1 means "oldest"
                        if (which == 0) {
                            //sort by newest
                            //Edit our shared preferences
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort", sortOptions[which]); //where 'Sort' is key & 'newest' is value
                            editor.apply(); // apply/save the value in our shared preferences
                            recreate(); //restart activity to take effect
                        } else  {
                            {
                                //sort by oldest
                                //Edit our shared preferences
                                SharedPreferences.Editor editor = mSharedPref.edit();
                                editor.putString("Sort",  sortOptions[which]); //where 'Sort' is key & 'oldest' is value
                                editor.apply(); // apply/save the value in our shared preferences
                                recreate(); //restart activity to take effect
                            }
                        }
                    }
                });
        builder.show();
    }

}


