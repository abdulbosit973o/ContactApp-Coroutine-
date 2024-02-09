package com.example.contactapp.presentation.screens.viewModules

import androidx.lifecycle.LiveData
import com.example.contactapp.data.remote.request.ContactNumberRequest
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse

interface RegisterPhoneViewModel {
    val messageLiveData:LiveData<String>
    val openHomeScreen:LiveData<ContactSuccessRegisterResponse>

    fun checkUserPhoneNumberCode(contactNumberRequest: ContactNumberRequest)
}