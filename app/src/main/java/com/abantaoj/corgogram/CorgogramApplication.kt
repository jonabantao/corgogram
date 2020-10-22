package com.abantaoj.corgogram

import android.app.Application
import com.abantaoj.corgogram.models.Post
import com.parse.Parse
import com.parse.ParseObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CorgogramApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Post::class.java)
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG)

        val builder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        builder.networkInterceptors().add(httpLoggingInterceptor)

        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId(BuildConfig.APP_ID)
            .clientKey(BuildConfig.CLIENT_KEY)
            .server(BuildConfig.SERVER_URL)
            .build()
        )
    }
}