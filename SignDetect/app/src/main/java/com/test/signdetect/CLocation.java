package com.test.signdetect;

/**
 * Created by bley on 25.07.2017.
 * helper class for our actual location
 */
import android.location.Location;

public class CLocation extends Location {

    public CLocation(Location location)
    {
        // TODO Auto-generated constructor stub
        super(location);
    }

    @Override
    public float distanceTo(Location dest) {
        // TODO Auto-generated method stub
        float nDistance = super.distanceTo(dest);
        return nDistance;
    }

    @Override
    public float getAccuracy() {
        // TODO Auto-generated method stub
        float nAccuracy = super.getAccuracy();
        return nAccuracy;
    }

    @Override
    public double getAltitude() {
        // TODO Auto-generated method stub
        double nAltitude = super.getAltitude();
        return nAltitude;
    }

    @Override
    public float getSpeed() {
        // TODO Auto-generated method stub
        float nSpeed = super.getSpeed() * 3.6f;
        return nSpeed;
    }



}