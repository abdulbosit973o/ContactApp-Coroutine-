package com.example.contactapp.presentation.screens.viewModules.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.model.ContactUIData
import com.example.contactapp.data.remote.request.ContactAddData
import com.example.contactapp.data.remote.request.ContactEditData
import com.example.contactapp.data.remote.response.ContactResponse
import com.example.contactapp.domain.AppRepository
import com.example.contactapp.presentation.screens.viewModules.HomeViewModel
import com.example.contactapp.utils.MyEventBus
import com.example.contactapp.utils.NetworkStatusValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    private val networkStatusValidator: NetworkStatusValidator
) : ViewModel(), HomeViewModel {
    override val refreshLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val listFromServer = MutableLiveData<List<ContactUIData>>()
    override val emptyListLiveData = MutableLiveData<Boolean>()

    init {
        MyEventBus.reloadEvent = {
            loadContacts()
        }
    }


    override fun addContact(contactAddData: ContactAddData) {
        refreshLiveData.value = true
        repository.addContact(contactAddData).onEach {
            refreshLiveData.value = false
            it.onSuccess {
                val message = if (networkStatusValidator.hasNetwork) "Success saved" else "Saved In local"
                messageLiveData.value = message
                MyEventBus.reloadEvent?.invoke()
            }.onFailure { message ->
                messageLiveData.value = message
            }
        }.launchIn(viewModelScope)
    }

    override fun editContact(contactEditData: ContactEditData) {
        refreshLiveData.value = true
        repository.editContact(contactEditData)
            .onEach {
                refreshLiveData.value = false
                it.onSuccess {
                    val message = if (networkStatusValidator.hasNetwork) "Success edited" else "Success edited. We are refresh back Online"
                    messageLiveData.value = message
                    MyEventBus.reloadEvent?.invoke()
                }
                    .onFailure { message ->
                        messageLiveData.value = message
                    }
            }
            .launchIn(viewModelScope)
    }

    override fun deleteContact(contactUIData: ContactUIData) {
        refreshLiveData.value = true
        repository.deleteContact(
            ContactResponse(contactUIData.id, contactUIData.firstName, contactUIData.lastName, contactUIData.phone)
        ).onEach {
            refreshLiveData.value = false
            it.onSuccess {
                MyEventBus.reloadEvent?.invoke()
            }
                .onFailure { message ->
                    messageLiveData.value = message
                }
        }.launchIn(viewModelScope)
    }


    override fun loadContacts() {
        refreshLiveData.value = true
        repository.loadData().onEach {
            refreshLiveData.value = false
            it.onSuccess { list ->
                listFromServer.value = list
            }
            it.onFailure { message ->
                messageLiveData.value = message
            }
        }.launchIn(viewModelScope)
    }
}
