package com.akarshseggemu.idosmapplication;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DisplayMetrics dm = this.getResources().getDisplayMetrics();

        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.main);
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Adding copyright
        map.getOverlays().add(new CopyrightOverlay(this));

        // Zoom and Touch controls
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // Setting min and max zoom levels
        map.setMinZoomLevel(3);
        map.setMaxZoomLevel(21);

        // setting default center
         IMapController mapController = map.getController();
         mapController.setZoom(3);

        // Location
        GpsMyLocationProvider mNewGPSLocation = new GpsMyLocationProvider(this.getBaseContext());
        // Set the minimum distance for location updates
        // parameter is in meters
        mNewGPSLocation.setLocationUpdateMinDistance(100);
        // Set the minimum time interval for location updates
        // parameter is in milliseconds
        mNewGPSLocation.setLocationUpdateMinTime(10000);
        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(mNewGPSLocation, map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);

        // Compass
        CompassOverlay mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

        // Rotation
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(map);
        mRotationGestureOverlay.setEnabled(false);
        map.getOverlays().add(mRotationGestureOverlay);

        // Scale Bar
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);

        // Minimap
        MinimapOverlay mMinimapOverlay = new MinimapOverlay(this, map.getTileRequestCompleteHandler());
        mMinimapOverlay.setWidth(dm.widthPixels / 5);
        mMinimapOverlay.setHeight(dm.heightPixels / 5);
        map.getOverlays().add(mMinimapOverlay);


    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }


}
