package net.izzatiliyana.project2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.izzatiliyana.project2.databinding.ActivityMapsBinding;

import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    MarkerOptions marker;
    LatLng centerlocation;

    Vector<MarkerOptions> markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        centerlocation = new LatLng(5.335361, 100.354725);

        markerOptions = new Vector<>();

        markerOptions.add( new MarkerOptions().title("Bliss Pharmacy")
                .position(new LatLng(5.27895, 100.49218))
                .snippet("Health")
        );
        markerOptions.add( new MarkerOptions().title("Sg.Bakap Hospital")
                .position(new LatLng(5.21953, 100.49722))
                .snippet("Hospital")
        );
        markerOptions.add( new MarkerOptions().title("Bandar Tasek Mutiara Health Clinic")
                .position(new LatLng(5.28194, 100.49184))
                .snippet("Clinic")
        );
        markerOptions.add( new MarkerOptions().title("Bukit Mertajam Hospital")
                .position(new LatLng(5.35957, 100.46411))
                .snippet("Hospital")
        );
        markerOptions.add( new MarkerOptions().title("Seberang Jaya Hospital")
                .position(new LatLng(5.39439, 100.40778))
                .snippet("Hospital")
        );


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        for (MarkerOptions mark : markerOptions) {
            mMap.addMarker(mark);

        }

        enableMyLocation();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerlocation,8));
        mMap.getUiSettings().setZoomControlsEnabled(true);



    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            String perms[] =  {"android.permission.ACCESS_FINE_LOCATION"};
            ActivityCompat.requestPermissions(this,perms, 200);
        }
    }
}