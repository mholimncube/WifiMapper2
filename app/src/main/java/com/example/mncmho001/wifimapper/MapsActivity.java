package com.example.mncmho001.wifimapper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    private Button button;
    WifiManager wifiManager;
    WifiInfo connection;
    String wifiDtls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        button = (Button) findViewById(R.id.scanBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                connection = wifiManager.getConnectionInfo();
                wifiDtls += "SSID: "+connection.getSSID();
                        // "RSSi: "+connection.getRssi();
                                //+ "\n"+"MAC Address: "
                        //+connection.getMacAddress()
                //"IP Address: " + connection.getIpAddress();
                //debugging purposes
                System.out.println("SSID: "+connection.getSSID());
                System.out.println("RSSi: "+connection.getRssi());
                System.out.println("MAC Address: "+connection.getMacAddress());
                System.out.println("IP Address: " + connection.getIpAddress());
                System.out.println(connection.getNetworkId());
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //check if the network provider is enabled
        if(locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    //get latitude
                    double latitude = location.getLatitude();
                    //get longitude
                    double longitude =  location.getLongitude();

                    //instantiate the class, LatLng
                    LatLng latLng = new LatLng(latitude,longitude);
                    //Instantiate the class, Geocoder
                    Geocoder geocoder=  new Geocoder(getApplicationContext());

                   try {
                       List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                       //String str = addressList.get(0).getLocality()+ " , ";
                       //str += addressList.get(0).getCountryName()+ " ";
                       mMap.addMarker(new MarkerOptions().position(latLng).title(wifiDtls));
                       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20.0f));
                   } catch(IOException e){
                       e.printStackTrace();
                   }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
        else if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //scan wifihot stops on this location
                    scanWifi();
                    //get latitude
                    double latitude = location.getLatitude();
                    //get longitude
                    double longitude =  location.getLongitude();

                    //instantiate the class, LatLng
                    LatLng latLng = new LatLng(latitude,longitude);
                    //Instantiate the class, Geocoder
                    Geocoder geocoder=  new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        //String str = addressList.get(0).getLocality()+ " , ";
                        //str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(wifiDtls));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20.0f));
                    } catch(IOException e){
                        e.printStackTrace();
                    }

                }
                //method to scan wifi store hotspots in an array/database with location
                public void scanWifi() {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
            //LatLng uct = new LatLng(-33.957637, 18.461023);
        //mMap.addMarker(new MarkerOptions().position(uct).title("Marker in UCT"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uct,20.0f));

    }


}
