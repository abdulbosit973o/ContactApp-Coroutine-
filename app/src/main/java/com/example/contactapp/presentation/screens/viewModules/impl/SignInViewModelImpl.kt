package com.example.contactapp.presentation.screens.viewModules.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.remote.request.ContactLoginRequest
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse
import com.example.contactapp.domain.AppRepository
import com.example.contactapp.presentation.screens.viewModules.SignInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignInViewModelImpl @Inject constructor(
    private val repository: AppRepository,
) : ViewModel(), SignInViewModel {
    override val openHomeScreen = MutableLiveData<ContactSuccessRegisterResponse>()
    override val messageLiveData = MutableLiveData<String>()

    override fun checkUserByLogin(contactLoginRequest: ContactLoginRequest) {
        repository.checkUserLoginData(contactLoginRequest).onEach {
            it.onSuccess { success ->
                openHomeScreen.value = success
            }
                .onFailure {message ->
                    messageLiveData.value =message
                }

        }
            .launchIn(viewModelScope)
    }
}