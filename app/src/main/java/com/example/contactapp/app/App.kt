package com.example.contactapp.app

import android.app.Application
import com.example.contactapp.data.local.sharedPref.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application() {
//    companion object {
//        @SuppressLint("StaticFieldLeak")
//        private lateinit var context: Context
//        fun getContext() = context
//        fun setContext(context: Context) {
//            this.context = context
//        }
//    }
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    SharedPreferences.init(this)
    }

}