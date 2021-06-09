package com.example.instilostandfound;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.squareup.picasso.Picasso;

/**
 * Activity to see details and claim the item
 */
public class ItemClaim extends AppCompatActivity  implements View.OnClickListener{
    private String username =null;
    CreateFoundObject objectfound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_claim);
        findViewById(R.id.buttonClaim).setOnClickListener(this);
        findViewById(R.id.placeclaim).setOnClickListener(this);

        username = getIntent().getStringExtra("username");

        objectfound = (CreateFoundObject)getIntent().getSerializableExtra("FoundObject");
        Log.v("desc",objectfound.getmDescription());

        ((TextView)findViewById(R.id.categorycontentclaim)).setText(objectfound.getmCategory());
        ((TextView)findViewById(R.id.namecontentclaim)).setText(objectfound.getmTitle());
        ((TextView) findViewById(R.id.placecontentclaim)).setText(objectfound.getmLocation());
        ((TextView) findViewById(R.id.descriptioncontentclaim)).setText(objectfound.getmDescription());
        ((TextView) findViewById(R.id.datecontentclaim)).setText(objectfound.getDate());
        ImageView camera = findViewById(R.id.imagecamclaim);
        Picasso.with(ItemClaim.this).load(objectfound.getImageUrl())
                .fit()
                .centerCrop()
                .into(camera);
    }

    /**
     * Executed on click of claim button
     * @param v claim button
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonClaim:
                Fragment emailFragment = new emailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("recievername", objectfound.getLDAP());
                bundle.putString("itemname",objectfound.getmTitle() );

                emailFragment.setArguments(bundle);

                ((FragmentActivity) this)
                        .getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frameLayout, emailFragment)
                        .addToBackStack(null).commit();
                break;

            case R.id.placeclaim:
                Fragment seeLocationFragment = new SeeLocationFragment();
                Bundle bundle1 = new Bundle();
                if(objectfound.getmLocation().trim().isEmpty()){

                    break;
                }
                else{
                    String  coordinates;
                    Double lat=0.0,lon=0.0;
                    coordinates = objectfound.getmLocation().trim();
                    Log.v("coordinatesbefore: ", coordinates);
                    Log.v("coordinates: ", coordinates);
                    lat = Double.parseDouble(coordinates.split(",")[0]);
                    lon = Double.parseDouble(coordinates.split(",")[1]);
                    bundle1.putDouble("Lat", lat);
                    bundle1.putDouble("Lon", lon);
                }
                seeLocationFragment.setArguments(bundle1);
                getSupportFragmentManager().beginTransaction().replace(R.id.map_container,
                        seeLocationFragment).commit();
                break;


        }
    }
}
