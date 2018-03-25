package com.example.dplex.mapweb

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat

class MainActivity : Activity() {

    var boolean_permission: Boolean = false

    val client by lazy {
        GeoApi.create()
    }

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(applicationContext, LocationTrackingService::class.java)
        button.setOnClickListener(View.OnClickListener {
            if (boolean_permission) {
                startService(intent)
            }
//            registerR
//            post()
        })
        button2.setOnClickListener(View.OnClickListener {
            stopService(intent)
        })
        fn_permission()
    }

    private fun fn_permission() {
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {


            } else {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSIONS)

            }
        } else {
            boolean_permission = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if (grantResults!!.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true
                }
            }

        }

    }

    private fun post() {
        val geo= GeoModel(2.4f, 3.5f)

        disposable = client.post(geo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> Log.v("posted", geo.toString()) },
                        { error -> Log.e("error", error.message + "!!!")}
                )
    }

    companion object {
        private val REQUEST_PERMISSIONS = 100
    }

}
