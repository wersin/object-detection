package com.test.signdetect;

/**
 * Created by bley on 23.07.2017.
 * This Activity gets the current Speed based on the time elapsed of our old and new location
 * we already get our current Speed thanks to the CLocation class so here we just display it
 * The idea was to detect a Sign and measure our velocity, the check whether we are currently
 * driving too fast for the permitted speed and alert the Driver.
 *
 * Since we are just using the SpeedDreams Simulation this is no longer needed
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Clob;
import java.util.Formatter;
import java.util.Locale;

import android.Manifest;
import android.location.Location;
import android.location.LocationManager;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class Speed extends AppCompatActivity implements IBaseGpsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);

    }

    public void finish()
    {
        super.finish();
        System.exit(0);
    }

    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        float nCurrentSpeed = 0;

        if(location != null)
        {
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.GERMANY, "%5.2f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        String strUnits = "meters/second";

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if(location != null)
        {
            CLocation myLocation = new CLocation(location);
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
