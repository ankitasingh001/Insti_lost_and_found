package com.example.instilostandfound;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

/**
 * Activity to display object details
 */
public class ItemDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        CreateFoundObject objectfound = (CreateFoundObject)getIntent().getSerializableExtra("FoundObject");
        Log.v("desc",objectfound.getmDescription());

        ((TextView)findViewById(R.id.categorycontent)).setText(objectfound.getmCategory());
        ((TextView)findViewById(R.id.namecontent)).setText(objectfound.getmTitle());
        ((TextView) findViewById(R.id.placecontent)).setText(objectfound.getmLocation());
        ((TextView) findViewById(R.id.descriptioncontent)).setText(objectfound.getmDescription());
        ((TextView) findViewById(R.id.datecontent)).setText(objectfound.getDate());
        ImageView camera = findViewById(R.id.imagecam);
        Picasso.with(ItemDetails.this).load(objectfound.getImageUrl())
                .fit()
                .centerCrop()
                .into(camera);

    }

}
