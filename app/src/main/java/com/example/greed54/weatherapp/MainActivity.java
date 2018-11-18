package com.example.greed54.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.greed54.weatherapp.adapter.SliderAdapter;
import com.example.greed54.weatherapp.common.Common;
import com.example.greed54.weatherapp.model.sugarEntities.DaoSession;
import com.example.greed54.weatherapp.model.sugarEntities.PlaceEntity;
import com.example.greed54.weatherapp.fragment.TodayWeatherFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private List<TextView> dots = new ArrayList<>();

    private DaoSession daoSession;

    private ImageButton addCityButton;

    private ImageButton removeCityButton;

    private ViewPager slideViewPager;

    private SliderAdapter sliderAdapter;

    private LinearLayout dotsLayout;

    private RelativeLayout relativeLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDb();

        setContentView(R.layout.activity_main);

        relativeLayout = (RelativeLayout) findViewById(R.id.root_view);


        //Request permissions
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            buildLocationRequest();
                            buildLocationCallback();
                        }
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Snackbar.make(relativeLayout, "Permissions Denied", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }).check();
    }


    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Common.CURRENT_LOCATION = locationResult.getLastLocation();

                initTab();
                initCitiesOnStart();
                initDotsIndicator(0);
                addCiyButtonListener();
                removeCityButtonListener();
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    private void initTab() {
        slideViewPager = (ViewPager) findViewById(R.id.slider_view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.dots_layout);

        sliderAdapter = new SliderAdapter(getSupportFragmentManager());

        addNewFragment(new PlaceEntity(), TodayWeatherFragment.CURRENT_LOCATION_MODE);

        slideViewPager.setAdapter(sliderAdapter);
        slideViewPager.addOnPageChangeListener(viewListener);
    }

    private void addNewFragment(PlaceEntity place, String mode){
        sliderAdapter.addFragment(TodayWeatherFragment.getInstance(place, mode));
        dots.add(new TextView(this));

        sliderAdapter.notifyDataSetChanged();
    }

    private void initDotsIndicator(int position) {
        dotsLayout.removeAllViews();

        for (TextView t : dots) {
            t.setText(Html.fromHtml("&#8226"));

            t.setTextSize(35);
            t.setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            dotsLayout.addView(t);
        }

        if (dots.size() > 0) {
            dots.get(position).setTextColor(getResources().getColor(R.color.light_font));
        }
    }

    private void addCiyButtonListener() {
        addCityButton = (ImageButton) findViewById(R.id.addCityButton);

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    private void removeCityButtonListener(){
        removeCityButton = (ImageButton) findViewById(R.id.removeCityButton);

        removeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlace();
                initDotsIndicator(0);
            }
        });
    }

    private void initCitiesOnStart(){
        List<PlaceEntity> placeEntities = daoSession.getPlaceEntityDao().loadAll();

        if (!placeEntities.isEmpty()){
            for (PlaceEntity place : placeEntities){
                addNewFragment(place, TodayWeatherFragment.OTHER_LOCATION_MODE);
            }
        }
    }

    private void addNewPlace(Place place){
        PlaceEntity placeEntity = new PlaceEntity(place.getName().toString(), place.getLatLng().longitude, place.getLatLng().latitude);
        daoSession.getPlaceEntityDao().insert(placeEntity);

        addNewFragment(placeEntity, TodayWeatherFragment.OTHER_LOCATION_MODE);
        slideViewPager.setCurrentItem(sliderAdapter.getCount());
    }

    private void removePlace(){
        TodayWeatherFragment todayWeatherFragment = (TodayWeatherFragment) sliderAdapter.getItem(slideViewPager.getCurrentItem());
        daoSession.getPlaceEntityDao().delete(todayWeatherFragment.getPlace());

        dots.remove(slideViewPager.getCurrentItem());

        sliderAdapter.removeFragment(slideViewPager.getCurrentItem());
        sliderAdapter.notifyDataSetChanged();

        //slideViewPager.setCurrentItem(0);
    }

    private void showOrHideRemoveButton(int position){

        if (position == 0){
            removeCityButton.setVisibility(View.GONE);
        } else {
            removeCityButton.setVisibility(View.VISIBLE);
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            initDotsIndicator(position);
            showOrHideRemoveButton(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("Pl", "Place: " + place.getName());

                addNewPlace(place);
                initDotsIndicator(sliderAdapter.getCount()-1);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Pl", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void initDb(){
        daoSession = ((WeatherApp)getApplication()).getDaoSession();
    }
}
