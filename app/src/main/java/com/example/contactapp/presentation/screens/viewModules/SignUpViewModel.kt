package com.example.contactapp.presentation.screens.viewModules

import androidx.lifecycle.LiveData
import com.example.contactapp.data.remote.request.ContactUserDataRequest
import com.example.contactapp.data.remote.response.ContactErrorResponse

interface SignUpViewModel {
    fun checkUserData(contactUserDataRequest: ContactUserDataRequest)
    val openRegisterNumberScreen: LiveData<ContactErrorResponse>
    val messageLiveData: LiveData<String>
}