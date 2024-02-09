package com.example.contactapp.presentation.screens.auth.register_user_phone

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contactapp.R
import com.example.contactapp.data.local.sharedPref.SharedPreferences
import com.example.contactapp.data.remote.request.ContactNumberRequest
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse
import com.example.contactapp.databinding.ScreenAuthCheckNumberBinding
import com.example.contactapp.presentation.screens.viewModules.RegisterPhoneViewModel
import com.example.contactapp.presentation.screens.viewModules.impl.RegisterPhoneViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterPhone :Fragment(R.layout.screen_auth_check_number) {
private val binding by viewBinding(ScreenAuthCheckNumberBinding::bind)
    private val viewModel: RegisterPhoneViewModel by viewModels<RegisterPhoneViewModelImpl>()
    private val shared = SharedPreferences
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edittext.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.continueInput.isEnabled = s.toString().length == 6
            }
            override fun afterTextChanged(s: Editable?) {}

        })

        binding.continueInput.setOnClickListener {
            viewModel.checkUserPhoneNumberCode(
                ContactNumberRequest(
                    shared.getUserNumber(),
                    binding.edittext.text.toString(),
                ),
            )
        }

        viewModel.messageLiveData.observe(this, messageObserver)
        viewModel.openHomeScreen.observe(this, openHomeScreenObServer)


    }
    private val messageObserver = Observer<String> {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private val openHomeScreenObServer = Observer<ContactSuccessRegisterResponse> {
        shared.saveUserNumber(it.phone)
        shared.saveUserToken(it.token)
        navController.navigate(RegisterPhoneDirections.actionRegisterPhoneToSignIn())
    }
}