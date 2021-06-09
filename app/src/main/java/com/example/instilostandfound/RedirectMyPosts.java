package com.example.instilostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that redireccts user's posts to lost or found item activity
 */
public class RedirectMyPosts extends AppCompatActivity implements View.OnClickListener {
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect_my_posts);

        Intent i = getIntent();
        username = i.getStringExtra("username");
        findViewById(R.id.lost_button).setOnClickListener(this);
        findViewById(R.id.found_button).setOnClickListener(this);
    }

    /**
     * Redirection to appropriate activities based on button click
     * @param v lost/found buttons
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lost_button:
                    Intent intent = new Intent(RedirectMyPosts.this, MyPostsLostRV.class);
                    intent.putExtra("type", "lost");
                    intent.putExtra("username", username);
                    startActivity(intent);
                break;
            case R.id.found_button:
                intent = new Intent(RedirectMyPosts.this, MyPostsFoundRV.class);
                intent.putExtra("username", username);
                intent.putExtra("type", "found");
                startActivity(intent);
                break;
        }
    }
}
