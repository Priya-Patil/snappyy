package com.m90.zero.home.pickupdrop;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.m90.zero.R;
import com.m90.zero.home.GpsTracker;
import com.m90.zero.utils.PrefManager;
import com.m90.zero.utils.Utilities;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DropSearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LocationListener {

    String TAG = "Dropplaceautocomplete";
    ImageView iv_mylocation;


    private GpsTracker gpsTracker;

    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    String latitude, longitude;

    Activity activity;
    PrefManager prefManager;

    String picDropType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activity = DropSearchActivity.this;
        iv_mylocation = findViewById(R.id.iv_mylocation);
        prefManager =  new PrefManager(activity);

        // picDropType = getIntent().getStringExtra("pickup");
        Places.initialize(getApplicationContext(), "AIzaSyAjJ2_qYW0Isgm3tjsgJeJyZhc_jp9kM28");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                //txtView.setText(place.getName()+","+place.getId());
                try {
                    Log.d(TAG, "Place: " + place.getName() + " ," + ", " + place.getId());
                    String pickupAddres = getAddress(place.getLatLng().latitude, place.getLatLng().longitude);
                    //prefManager.setDrop(pickupAddres);
                    // NewPickDropActivity.binding.tvAddressPickup.setText(pickupAddres);


                    if(getIntent().getStringExtra("drop").equals("drop"))
                    {
                        prefManager.setDrop(pickupAddres);
                        Utilities.launchActivity(DropSearchActivity.this,NewPickDropActivity.class,true);
                        Log.e(TAG, "onCreate: 2211111111" );

                    }

                    if(getIntent().getStringExtra("drop").equals("drop1"))
                    {
                        prefManager.setDrop(pickupAddres);
                        Utilities.launchActivity(DropSearchActivity.this,NewPickDropActivity.class,true);
                        Log.e(TAG, "onCreate: 2211111111" );

                    }

                    Log.e(TAG, "onActivityResultPickup: " + pickupAddres + " " + place.getLatLng().latitude + " " + place.getLatLng().longitude);
                    Utilities.launchActivity(DropSearchActivity.this,NewPickDropActivity.class,true);

                }catch (Exception e)
                {
                    Log.e(TAG, "onPlaceSelected: "+e.getMessage() );

                }

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        iv_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
                getLocationa();
            }
        });

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.e( "","onLocationChanged: Current Location: " + location.getLatitude() + ", " + location.getLongitude());

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void getLocationa(){
        gpsTracker = new GpsTracker(DropSearchActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            String addressHome = getAddress(latitude,longitude);
            Log.e("TAG", "getLocation: "+latitude+" "+longitude+" "+addressHome);
            prefManager.setDrop(addressHome);


            try {


                if (getIntent().getStringExtra("drop").equals("drop")) {
                    prefManager.setDrop(addressHome);
                    Utilities.launchActivity(DropSearchActivity.this, NewPickDropActivity.class, true);

                }

                if (getIntent().getStringExtra("drop").equals("drop1")) {
                    prefManager.setDrop(addressHome);
                    Utilities.launchActivity(DropSearchActivity.this, NewShopActivity.class, true);

                }
            }
            catch (Exception e)
            {

            }


            //txt_address.setText(addressHome);
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0));


            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

}

