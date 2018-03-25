package com.example.dplex.mapweb

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by dplex on 2018-03-25.
 */

class LocationTrackingService : Service() {

    var locationManager : LocationManager? = null

    override fun onBind(p0: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        Log.i("mapweb", "onCreate")
        if (locationManager == null) {
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        try {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL, DISTANCE, locationListeners[1])
        } catch (e: Exception){
            Log.e("mapweb", "Fail", e)
        }

        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, DISTANCE, locationListeners[0])
        } catch (e: Exception) {
            Log.e("mapweb", "Fail", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null)
            for (i in 0..locationListeners.size) {
                try {
                    locationManager?.removeUpdates(locationListeners[i])
                } catch (e:Exception) {
                    Log.w("mapweb", "Fail")
                }
            }
    }

    companion object {
        val INTERVAL = 1000.toLong()
        val DISTANCE = 10.toFloat()
        val client by lazy {
            GeoApi.create()
        }

        var disposable: Disposable? = null

        val locationListeners = arrayOf(
                LTRLocationListener(LocationManager.GPS_PROVIDER),
                LTRLocationListener(LocationManager.NETWORK_PROVIDER)
        )

        class LTRLocationListener(provider: String) : android.location.LocationListener {

            val lastLocation = Location(provider)

            override fun onLocationChanged(location: Location?) {
                Log.i("mapweb", location.toString())
                val geo = GeoModel(location!!.latitude.toFloat(), location!!.longitude.toFloat())

                disposable = client.post(geo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result -> Log.v("posted", geo.toString()) },
                                { error -> Log.e("error", error.message + "!!!")}
                        )
                lastLocation.set(location)
                // TODO: Do something here
            }

            override fun onProviderDisabled(provider: String?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

        }
    }
}
