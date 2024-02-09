package com.example.contactapp.presentation.screens.viewModules

import androidx.lifecycle.LiveData
import com.example.contactapp.data.remote.request.ContactLoginRequest
import com.example.contactapp.data.remote.response.ContactErrorResponse
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse

interface SignInViewModel {
    val openHomeScreen: LiveData<ContactSuccessRegisterResponse>
    val messageLiveData: LiveData<String>

    fun checkUserByLogin(contactLoginRequest: ContactLoginRequest)
}