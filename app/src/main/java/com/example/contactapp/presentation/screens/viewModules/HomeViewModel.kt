package com.example.contactapp.presentation.screens.viewModules

import androidx.lifecycle.LiveData
import com.example.contactapp.data.model.ContactUIData
import com.example.contactapp.data.remote.request.ContactAddData
import com.example.contactapp.data.remote.request.ContactEditData

interface HomeViewModel {
    val refreshLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val listFromServer: LiveData<List<ContactUIData>>
    val emptyListLiveData: LiveData<Boolean>
    fun addContact(contactAddData: ContactAddData)
    fun editContact(contactEditData: ContactEditData)
    fun deleteContact(contactUIData: ContactUIData)
    fun loadContacts()

}
