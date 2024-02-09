package com.example.contactapp.presentation.screens.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.contactapp.R
import com.example.contactapp.data.local.sharedPref.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment(R.layout.fragment_splash) {
    private val shared = SharedPreferences.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!shared.userLogOut() && shared.getUserToken() != "")   {
            lifecycleScope.launch {
                delay(1000)
                findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignIn())
            }
        }
        else if (shared.userLogOut() || shared.getUserToken() != "") {
            lifecycleScope.launch {
                delay(1000)
                findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignIn())
            }
        }
        else {
            lifecycleScope.launch {
                delay(1000)
                findNavController().navigate(SplashScreenDirections.actionSplashScreenToSignUp())
            }
        }


    }
}