package com.example.mrpeter1.hikerwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

               // Log.i("Location", location.toString()); // jika lokasinya berubah ubah
                    updateLocationInfo(location);
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
        };
        // jika lokasi di atas belum ditemukan. atau perintah diatas belum di jalakan maka harus meminta permision dulu
        // meminta permisi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else { // if we have permission
            locationManager. requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener); // untuk meminta lokasi update
            Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastknownLocation != null){
                 updateLocationInfo(lastknownLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
             startlistening();
        }
    }

    public void startlistening() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }

    public  void updateLocationInfo(Location location){ // update semua info

       // Log.i("location", location.toString());
        TextView textViewlat, textViewlong, textViewacc, textViewatt, textViewadd;

        textViewlat = findViewById(R.id.textViewlat);
        textViewlong = findViewById(R.id.textViewlong);
        textViewacc = findViewById(R.id.textViewacc);
        textViewatt = findViewById(R.id.textViewatt);
        textViewadd = findViewById(R.id.textViewadd);

        textViewlat.setText("Latitude : " + Double.toString(location.getLatitude()));
        textViewlong.setText("Longtitude : " + Double.toString(location.getLongitude()));
        textViewacc.setText("Accuracy : "+ Double.toString(location.getAccuracy()));
        textViewatt.setText("altitude : "+ Double.toString(location.getAltitude()));

        String address = "Could not fing address :(";

        // untuk alamat kita harus menggunakan geocoder
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {

            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddresses != null && listAddresses.size() > 0 ){
                address = "Address \n";
                if (listAddresses.get(0).getThoroughfare() != null){
                    address += listAddresses.get(0).getThoroughfare() + "\n";
                }

                if (listAddresses.get(0).getLocality() != null){
                    address += listAddresses.get(0).getLocality() + " ";
                }
                if (listAddresses.get(0).getPostalCode() != null){
                    address += listAddresses.get(0).getPostalCode() + " ";
                }

                if (listAddresses.get(0).getAdminArea() != null){
                    address += listAddresses.get(0).getAdminArea();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        textViewadd.setText(address);



    }
}
