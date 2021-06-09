package com.example.instilostandfound;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeeLocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    GoogleMap mMap;
    MapView mMapView;
    public View mView;
    Marker my_marker;
    Double lat = 19.131482, lon = 72.916129;



    public SeeLocationFragment() {
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
        mView = inflater.inflate(R.layout.fragment_see_location, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
        mView.findViewById(R.id.done).setOnClickListener(this);

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
        lat = bundle.getDouble("Lat");
        lon = bundle.getDouble("Lon");
        LatLng location = new LatLng(lat, lon);
        my_marker = mMap.addMarker(new MarkerOptions().position(location).draggable(false));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,16f));
        mMap.setOnMarkerClickListener(this);
    }
    /**
     * Define what should happen once Done Button is clicked
     * The fragment closes and goes back to parent
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done:
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
        return false;
    }



}
