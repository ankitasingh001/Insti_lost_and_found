package com.example.instilostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * Home activity to navigate news feed and other activity screens
 */
public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private String username = null;
    private DrawerLayout drawerLayout;

    /**
     * Method for implementing drawer layout for navigation
     * @param savedInstanceState current instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        username = getIntent().getStringExtra("username");

        findViewById(R.id.lost_button_newsfeed).setOnClickListener(this);
        findViewById(R.id.found_button_newsfeed).setOnClickListener(this);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        TextView ldapname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        ldapname.setText(username);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle navigation_toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(navigation_toggle);
        navigation_toggle.syncState();

    }

    /**
     * Close and open navigation drawer on back pressed
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    /**
     * Method to migrate to other activities based on selection of menu
     * @param menuItem the list of menu items on navigation drawer
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch ((menuItem.getItemId())) {
            case R.id.post_found:
                Intent intent = new Intent(Navigation.this, FoundItem.class);
                intent.putExtra("username", username);
                intent.putExtra("CallingClass","Navigation");
                startActivity(intent);
                break;
            case R.id.post_lost:
                intent = new Intent(Navigation.this, LostItem.class);
                intent.putExtra("username", username);
                intent.putExtra("CallingClass","Navigation");
                startActivity(intent);
                break;
            case R.id.home:
                finish();
                startActivity(getIntent());
                break;
            case R.id.my_posts:
                intent = new Intent(this, RedirectMyPosts.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            case R.id.logout:
                finish();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * On click methods for news feed for lost and found items
     * @param v view layout
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lost_button_newsfeed:
                Intent intent = new Intent(Navigation.this, NewsFeedLost.class);
                intent.putExtra("type", "lost");
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            case R.id.found_button_newsfeed:
                intent = new Intent(Navigation.this, NewsFeedFound.class);
                intent.putExtra("username", username);
                intent.putExtra("type", "found");
                startActivity(intent);
                break;
        }
    }




}
