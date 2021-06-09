package com.example.instilostandfound;


import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Fragment to select location of found item from a google map
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    GoogleMap mMap;
    MapView mMapView;
    public View mView;
    public Marker my_marker;
    Double lat=19.131482, lon=72.916129;



    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Defines the layout of fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return mview
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_location, container, false);
        return mView;
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
        mView.findViewById(R.id.select_location).setOnClickListener(this);


    }

    /**
     * Populates all the necessary current info which the map should contain.
     * Adds marker and sets latitute and longitude of marker
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Bundle bundle = getArguments();
        if(bundle.containsKey("Lat")){
            lat = bundle.getDouble("Lat");
        }
        if(bundle.containsKey("Lon")){
            lon = bundle.getDouble("Lon");
            //Toast.makeText(getActivity(), "!!!!!"+lon.toString(), Toast.LENGTH_SHORT).show();

        }
        LatLng kresit = new LatLng(lat, lon);
        //Toast.makeText(getActivity(), "Kresit!!"+kresit.longitude, Toast.LENGTH_SHORT).show();

        my_marker = mMap.addMarker(new MarkerOptions().position(kresit).draggable(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kresit,16f));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }

    /**
     * Define what should happen once Select Button is clicked
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_location:
                lat = my_marker.getPosition().latitude;
                lon = my_marker.getPosition().longitude;
                //Toast.makeText(getActivity(), lat.toString(), Toast.LENGTH_SHORT).show();
                ((FoundItem)getActivity()).place.setText(lat.toString()+","+lon.toString());
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

                break;
        }

    }

    /**
     * Defines what happens when marker is clicked
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng my_marker_location = marker.getPosition();
        //Toast.makeText(getActivity(), my_marker_location.toString(), Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    /**
     * Method to define what happens when marker is dragged accross the map
     * @param marker
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        my_marker.setPosition(marker.getPosition());
        //Toast.makeText(getActivity(), my_marker.getPosition().toString(), Toast.LENGTH_SHORT).show();


    }
}
