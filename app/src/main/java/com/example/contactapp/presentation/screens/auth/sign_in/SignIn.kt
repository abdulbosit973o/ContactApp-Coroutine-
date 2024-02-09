package com.example.contactapp.presentation.screens.auth.sign_in

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactapp.R
import com.example.contactapp.data.local.sharedPref.SharedPreferences
import com.example.contactapp.data.remote.request.ContactLoginRequest
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse
import com.example.contactapp.databinding.ScreenAuthSigninBinding
import com.example.contactapp.presentation.screens.viewModules.RegisterPhoneViewModel
import com.example.contactapp.presentation.screens.viewModules.SignInViewModel
import com.example.contactapp.presentation.screens.viewModules.impl.RegisterPhoneViewModelImpl
import com.example.contactapp.presentation.screens.viewModules.impl.SignInViewModelImpl
import com.example.contactapp.utils.MyEventBus
import com.example.contactapp.utils.NetworkStatusValidator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignIn : Fragment(R.layout.screen_auth_signin) {
    private val viewModel: SignInViewModel by viewModels<SignInViewModelImpl>()
    private val binding by viewBinding(ScreenAuthSigninBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private val shared = SharedPreferences
    private lateinit var dialog:Dialog


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireContext())
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.setCancelable(false)
//
//        if (!NetworkStatusValidator.hasNetwork) {
//            showBottomSheetDialog()
//        }

        MyEventBus.reloadEvent = {
            dialog.dismiss()
        }
        MyEventBus.checkNetwork={
            showBottomSheetDialog()
        }

        binding.creat.setOnClickListener { navController.navigate(SignInDirections.actionSignInToSignUp()) }

        binding.login.addTextChangedListener {
            binding.btnSignIn.isEnabled = it.toString().length == 13
        }
        binding.pasword.addTextChangedListener {
            binding.btnSignIn.isEnabled = it.toString().length >= 8
        }


            binding.btnSignIn.setOnClickListener {
                viewModel.checkUserByLogin(
                    ContactLoginRequest(
                        binding.login.text.toString(),
                        binding.pasword.text.toString()
                    )
                )
            }


        viewModel.messageLiveData.observe(this, messageObserver)
        viewModel.openHomeScreen.observe(this, openHomeScreenObServer)

    }
    private val messageObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }
    private val openHomeScreenObServer = Observer<ContactSuccessRegisterResponse>{
        shared.saveUserNumber(it.phone)
        shared.saveUserToken(it.token)
        shared.userIsLogOut(false)
        Toast.makeText(requireContext(), "${shared.getUserNumber()} ${shared.getUserToken()}", Toast.LENGTH_SHORT).show()
      navController.navigate(SignInDirections.actionSignInToHomeScreen())
    }


    private fun showBottomSheetDialog() {
        dialog.setContentView(R.layout.dialog_not_connection)


        dialog.findViewById<FrameLayout>(R.id.retry_button).setOnClickListener {
//            if (NetworkStatusValidator.hasNetwork)
                   dialog.dismiss()
        }



        dialog.show()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
}