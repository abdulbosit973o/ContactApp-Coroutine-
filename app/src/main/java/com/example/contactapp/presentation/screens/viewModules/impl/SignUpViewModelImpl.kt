package com.example.contactapp.presentation.screens.viewModules.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.remote.request.ContactUserDataRequest
import com.example.contactapp.data.remote.response.ContactErrorResponse
import com.example.contactapp.domain.AppRepository
import com.example.contactapp.presentation.screens.viewModules.SignUpViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelImpl @Inject constructor(
    private val repository: AppRepository,
) : ViewModel(), SignUpViewModel {
    override val openRegisterNumberScreen = MutableLiveData<ContactErrorResponse>()
    override val messageLiveData = MutableLiveData<String>()

    override fun checkUserData(contactUserDataRequest: ContactUserDataRequest) {
        repository.checkUserData(contactUserDataRequest).onEach {
            it.onSuccess { errorResponse ->
                openRegisterNumberScreen.value = errorResponse
            }
                .onFailure { message ->
                    messageLiveData.value = message
                }
        }.launchIn(viewModelScope)
    }


}