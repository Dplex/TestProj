package com.example.dplex.mapweb

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

/**
 * Created by dplex on 2018-03-19.
 */
interface GeoApi {
    @POST("geo/")
    @Headers("Content-Type: application/json;charset=utf-8")
    fun post(@Body geo: GeoModel): Observable<GeoModel>

    companion object {
        fun create(): GeoApi {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://59.12.69.183:8000/")
                    .build()
            return retrofit.create(GeoApi::class.java)
        }

    }

}