package com.example.instilostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reusable activity for veiwing my posts -> kept in the code for future reference
 */
public class MyPosts extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("FoundData");
        Query queryRef = myRef.orderByChild("FoundData").equalTo("gooner");
        final List<Map<String, String>> of = new ArrayList<Map<String, String>>();
        final List<Map<String, String>> ol = new ArrayList<Map<String, String>>();
        Map<String, String> of1 = new HashMap<String, String>();
        of1.put("key", "123");
        of1.put("category","electronics");
        of1.put("name","Phone");
        of1.put("description","Lenovo Z+. Black colour.");
        of1.put("date","28th Oct 2019");
        of1.put("type","found");
        of1.put("place","CC103");
        of.add(of1);

        Map<String, String> of2 = new HashMap<String, String>();
        of2.put("key", "456");
        of2.put("category","money");
        of2.put("name","wallet");
        of2.put("description","green colour baggit");
        of2.put("date","28th Oct 2019");
        of2.put("type","lost");
        of2.put("place","sic201");
        ol.add(of2);




        final Intent intent = getIntent();
        ListView listView = (ListView) findViewById(R.id.listviewlost);
        if(intent.getStringExtra("type").equals("lost")){

            String ol_names[] = new String[ol.size()];
            for(int i=0; i<ol.size();i++){
                ol_names[i] = ol.get(i).get("name");
            }

            ArrayAdapter<String> adaptor = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, ol_names);
            listView.setAdapter(adaptor);
        }
        else if(intent.getStringExtra("type").equals("found")){

            String of_names[] = new String[of.size()];
            for(int i=0; i<of.size();i++){
                of_names[i] = of.get(i).get("name");
            }

            ArrayAdapter<String> adaptor = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, of_names);
            listView.setAdapter(adaptor);
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(MyPosts.this, ItemDetails.class);
                if(intent.getStringExtra("type").equals(("found"))){

                    intent1.putExtra("category", "Electronics");
                    intent1.putExtra("name", of.get(position).get("name"));
                    intent1.putExtra("place", of.get(position).get("place"));
                    intent1.putExtra("description", of.get(position).get("description"));
                    intent1.putExtra("date", of.get(position).get("date"));
                    intent1.putExtra("key", of.get(position).get("key"));
                    intent1.putExtra("type", of.get(position).get("type"));
                }

                else if(intent.getStringExtra("type").equals(("lost"))){

                    intent1.putExtra("category", "Electronics");
                    intent1.putExtra("name", ol.get(position).get("name"));
                    intent1.putExtra("place", ol.get(position).get("place"));
                    intent1.putExtra("description", ol.get(position).get("description"));
                    intent1.putExtra("date", ol.get(position).get("date"));
                    intent1.putExtra("key", ol.get(position).get("key"));
                    intent1.putExtra("type", ol.get(position).get("type"));
                }

                startActivity(intent1);
            }
        });
    }
}

