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
 * Fragment for lost item location
 * A simple {@link Fragment} subclass.
 */
public class LostItemLocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    GoogleMap mMap;
    MapView mMapView;
    public View mView;
    Marker my_marker;
    Double lat = 19.131482, lon = 72.916129;



    public LostItemLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_lost_item_location, container, false);
        return mView;
    }

    /**
     * Sets map's view
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
     * Populatesmap with required info
     * Adds marker and sets its location
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
        LatLng kresit = new LatLng(lat, lon);
        my_marker = mMap.addMarker(new MarkerOptions().position(kresit).draggable(true));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kresit,16f));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }

    /**
     * implemented clickable event, select to select location of lost item
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_location:
                Double lat, lon;
                lat = my_marker.getPosition().latitude;
                lon = my_marker.getPosition().longitude;
                Bundle bundle = getArguments();
                ((LostItem)getActivity()).place.setText(lat.toString()+","+lon.toString());
                break;
        }

       getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    /**
     * defines what happens when marker is clicked
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng my_marker_location = marker.getPosition();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    /**
     * Defines what happens when the marker is dragged
     * @param marker
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        my_marker = marker;

    }
}
