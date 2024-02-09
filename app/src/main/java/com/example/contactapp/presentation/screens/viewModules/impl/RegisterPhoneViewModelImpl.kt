package com.example.contactapp.presentation.screens.viewModules.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.remote.request.ContactNumberRequest
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse
import com.example.contactapp.domain.AppRepository
import com.example.contactapp.presentation.screens.viewModules.RegisterPhoneViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterPhoneViewModelImpl @Inject constructor(
    private val repository: AppRepository,
) : ViewModel(), RegisterPhoneViewModel {
    override val messageLiveData = MutableLiveData<String>()
    override val openHomeScreen = MutableLiveData<ContactSuccessRegisterResponse>()


    override fun checkUserPhoneNumberCode(contactNumberRequest: ContactNumberRequest) {
        repository.checkUserNumberCode(contactNumberRequest).onEach {
            it.onSuccess { success ->
                openHomeScreen.value = success
            }
                .onFailure { message ->
                    messageLiveData.value = message
                }
        }
            .launchIn(viewModelScope)
    }
}