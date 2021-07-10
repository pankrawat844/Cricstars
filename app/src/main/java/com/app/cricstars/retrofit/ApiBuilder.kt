package com.app.cricstars.retrofit

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiBuilder {
    private var retrofit: Retrofit? = null
    private var BASE_URL: String? = "https://t-10.in/cricstars/"

    fun getRetrofitInstance(ctx: Context): Retrofit? {
        val gson = GsonBuilder()
            .setLenient()
            .create()
       val  httpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.interceptors().add(interceptor)
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()
        }
        return retrofit
    }


}