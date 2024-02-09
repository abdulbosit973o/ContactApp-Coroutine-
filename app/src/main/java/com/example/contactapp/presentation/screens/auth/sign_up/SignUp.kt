package com.example.contactapp.presentation.screens.auth.sign_up

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.example.contactapp.data.remote.request.ContactUserDataRequest
import com.example.contactapp.data.remote.response.ContactErrorResponse
import com.example.contactapp.databinding.ScreenAuthRegisterBinding
import com.example.contactapp.presentation.screens.viewModules.SignUpViewModel
import com.example.contactapp.presentation.screens.viewModules.impl.SignUpViewModelImpl
import com.example.contactapp.utils.MyEventBus
import com.example.contactapp.utils.NetworkStatusValidator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : Fragment(R.layout.screen_auth_register) {
    private val viewModel: SignUpViewModel by viewModels<SignUpViewModelImpl>()
    private val binding by viewBinding(ScreenAuthRegisterBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private lateinit var dialog: Dialog


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireContext())
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.setCancelable(false)
//        if (!NetworkStatusValidator.hasNetwork) {
//            showBottomSheetDialog()
//        }

        MyEventBus.reloadEvent = {
            dialog.dismiss()
        }
        MyEventBus.checkNetwork={
            showBottomSheetDialog()
        }
        binding.backSignin.setOnClickListener {
            navController.navigate(SignUpDirections.actionSignUpToSignIn())
        }

        val nextBtn = binding.btnNext

        binding.firstName.addTextChangedListener {
            MyEventBus.reloadEvent?.invoke()
           nextBtn.isEnabled = it.toString().length > 3
        }
        binding.lastName.addTextChangedListener {
            nextBtn.isEnabled = it.toString().length > 3
        }
        binding.phone.addTextChangedListener {
            nextBtn.isEnabled = it.toString().length == 13
        }
        binding.password.addTextChangedListener {
            nextBtn.isEnabled = it.toString().length >= 8
        }


        nextBtn.setOnClickListener {
            MyEventBus.reloadEvent?.invoke()
            viewModel.checkUserData(ContactUserDataRequest(
                firstName = binding.firstName.text.toString(),
                lastName = binding.lastName.text.toString(),
                password = binding.password.text.toString(),
                phone = binding.phone.text.toString(),
            ))
        }

        viewModel.messageLiveData.observe(this, messageObserver)
        viewModel.openRegisterNumberScreen.observe(this, openRegisterNumberScreenObServer)
    }
    private val messageObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }
    private val openRegisterNumberScreenObServer = Observer<ContactErrorResponse> {
        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        navController.navigate(SignUpDirections.actionSignUpToRegisterPhone())
    }
    private fun showBottomSheetDialog() {
        dialog.setContentView(R.layout.dialog_not_connection)

        dialog.findViewById<FrameLayout>(R.id.retry_button).setOnClickListener {
//            if (NetworkStatusValidator.hasNetwork) {
                dialog.dismiss()
//            }
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