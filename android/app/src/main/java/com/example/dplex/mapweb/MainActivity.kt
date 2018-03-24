package com.example.dplex.mapweb

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    val client by lazy {
        GeoApi.create()
    }

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener(View.OnClickListener {
            post()
        })

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


}
