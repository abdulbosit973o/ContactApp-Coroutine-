package com.example.contactapp.domain

import com.example.contactapp.data.model.ContactUIData
import com.example.contactapp.data.remote.request.ContactAddData
import com.example.contactapp.data.remote.request.ContactEditData
import com.example.contactapp.data.remote.request.ContactLoginRequest
import com.example.contactapp.data.remote.request.ContactNumberRequest
import com.example.contactapp.data.remote.request.ContactUserDataRequest
import com.example.contactapp.data.remote.response.ContactErrorResponse
import com.example.contactapp.data.remote.response.ContactResponse
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse
import com.example.contactapp.data.sealedData.ResultData
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun checkUserNumberCode(contactNumberRequest: ContactNumberRequest) : Flow<ResultData<ContactSuccessRegisterResponse>>
    fun checkUserData(contactUserDataRequest: ContactUserDataRequest) : Flow<ResultData<ContactErrorResponse>>
    fun checkUserLoginData(contactLoginRequest: ContactLoginRequest) : Flow<ResultData<ContactSuccessRegisterResponse>>
    fun loadData() :Flow<ResultData<List<ContactUIData>>>
    fun addContact(contactAddData: ContactAddData):Flow<ResultData<Unit>>
    fun deleteContact(contactResponse: ContactResponse):Flow<ResultData<Unit>>
    fun editContact(contactEditData: ContactEditData):Flow<ResultData<Unit>>
    fun syncWithServer() :Flow<ResultData<Unit>>
}