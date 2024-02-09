package com.example.contactapp.data.local.sharedPref

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Singleton

@Singleton
class SharedPreferences {
    companion object{
        private lateinit var shared: SharedPreferences

        fun init(context: Context) {
            if (!(::shared.isInitialized)) {
                shared = context.getSharedPreferences("ContactData", Context.MODE_PRIVATE)
            }
        }

        fun saveUserNumber(string: String) {
            shared.edit().putString("Number", string).apply()
        }

        fun getUserNumber(): String {
            return shared.getString("Number", "").toString()
        }
        fun getUserToken(): String {
            return shared.getString("Token", "").toString()
        }
        fun saveUserToken(token: String) {
            shared.edit().putString("Token", token).apply()
        }
        fun userIsLogOut(string: Boolean) {
            shared.edit().putBoolean("isLog", string).apply()
        }
        fun userLogOut() : Boolean {
            return shared.getBoolean("isLog", false)
        }

        fun getInstance() = this


    }


}