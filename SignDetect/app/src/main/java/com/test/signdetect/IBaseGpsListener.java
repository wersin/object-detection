package com.test.signdetect;

/**
 * Created by bley on 25.07.2017.
 */

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public interface IBaseGpsListener extends LocationListener {

    public void onLocationChanged(Location location);

    public void onProviderDisabled(String provider);

    public void onProviderEnabled(String provider);

    public void onStatusChanged(String provider, int status, Bundle extras);

}
