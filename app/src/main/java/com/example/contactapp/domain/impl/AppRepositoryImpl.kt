package com.example.contactapp.domain.impl

import com.example.contactapp.data.local.dao.ContactDao
import com.example.contactapp.data.local.entity.ContactEntity
import com.example.contactapp.data.local.sharedPref.SharedPreferences
import com.example.contactapp.data.mapper.ContactMapper.toUIData
import com.example.contactapp.data.model.ContactUIData
import com.example.contactapp.data.model.StatusEnum
import com.example.contactapp.data.model.toStatusEnum
import com.example.contactapp.data.remote.api.ContactApi
import com.example.contactapp.data.remote.request.ContactAddData
import com.example.contactapp.data.remote.request.ContactEditData
import com.example.contactapp.data.remote.request.ContactLoginRequest
import com.example.contactapp.data.remote.request.ContactNumberRequest
import com.example.contactapp.data.remote.request.ContactUserDataRequest
import com.example.contactapp.data.remote.response.ContactErrorResponse
import com.example.contactapp.data.remote.response.ContactResponse
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse
import com.example.contactapp.data.sealedData.ResultData
import com.example.contactapp.domain.AppRepository
import com.example.contactapp.utils.NetworkStatusValidator
import com.example.contactapp.utils.flowResponse
import com.example.contactapp.utils.toResultData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao,
    private val gson: Gson,
    private val api: ContactApi,
    private val networkStatusValidator: NetworkStatusValidator,
) : AppRepository {
    private val shared = SharedPreferences.getInstance()

    override fun checkUserNumberCode(contactNumberRequest: ContactNumberRequest): Flow<ResultData<ContactSuccessRegisterResponse>> = flowResponse {
        val response = api.registerUserNumber(contactNumberRequest)
        emit(response.toResultData { it })
    }

    override fun checkUserData(contactUserDataRequest: ContactUserDataRequest): Flow<ResultData<ContactErrorResponse>> = flowResponse {
        val response = api.registerUser(contactUserDataRequest)
        shared.saveUserNumber(contactUserDataRequest.phone)
        emit(response.toResultData { it })
    }

    override fun checkUserLoginData(contactLoginRequest: ContactLoginRequest): Flow<ResultData<ContactSuccessRegisterResponse>> = flowResponse {
        val response = api.loginUserProfile(contactLoginRequest)
        emit(response.toResultData { it })
    }

    override fun loadData(): Flow<ResultData<List<ContactUIData>>> = flowResponse {
        val remoteList = api.getAllContacts(shared.getUserToken())
        val list = mergeData(remoteList.body() ?: emptyList(), contactDao.getAllContacts())
        emit(remoteList.toResultData { list })
    }

    override fun addContact(contactAddData: ContactAddData): Flow<ResultData<Unit>> = flowResponse {
        if (networkStatusValidator.hasNetwork) {
            val response = api.addContact(shared.getUserToken(), contactAddData)
            emit(response.toResultData { })
        } else {
            contactDao.addContact(ContactEntity(0, 0, contactAddData.firstName, contactAddData.lastName, contactAddData.phone, StatusEnum.ADD.statusCode))
            emit(ResultData.success(Unit))
        }
    }

    override fun deleteContact(contactResponse: ContactResponse): Flow<ResultData<Unit>> = flowResponse {
        if (networkStatusValidator.hasNetwork) {
            val response = api.deleteContact(shared.getUserToken(), contactResponse.id)
            emit(response.toResultData { })
        } else {
            contactDao.addContact(ContactEntity(0, contactResponse.id, contactResponse.firstName, contactResponse.lastName, contactResponse.phone, StatusEnum.DELETE.statusCode))
            emit(ResultData.success(Unit))
        }
    }

    override fun editContact(contactEditData: ContactEditData): Flow<ResultData<Unit>> = flowResponse {
        if (networkStatusValidator.hasNetwork) {
            val response = api.editContact(shared.getUserToken(), contactEditData)
            emit(response.toResultData { })
        } else {
            contactDao.addContact(ContactEntity(0, contactEditData.id, contactEditData.firstName, contactEditData.lastName, contactEditData.phone, StatusEnum.EDIT.statusCode))
            (ResultData.success(Unit))
        }
    }

    override fun syncWithServer(): Flow<ResultData<Unit>> = flowResponse {
        val list = contactDao.getAllContacts()
        list.forEach { entity ->
            when (entity.statusCode.toStatusEnum()) {
                StatusEnum.ADD -> {
                    val response = api.addContact(shared.getUserToken(), ContactAddData(entity.firstName, entity.lastName, entity.phone))
                    emit(response.toResultData { })
                    contactDao.deleteContact(entity)
                }

                StatusEnum.DELETE -> {
                    val response = api.deleteContact(shared.getUserToken(), entity.remoteID)
                    emit(response.toResultData { })
                    contactDao.deleteContact(entity)
                }

                StatusEnum.EDIT -> {
                    val response = api.editContact(shared.getUserToken(), ContactEditData(entity.remoteID, entity.firstName, entity.lastName, entity.phone))
                    emit(response.toResultData { })
                    contactDao.deleteContact(entity)
                }

                else -> {}
            }

        }
    }
}

private fun mergeData(remoteList: List<ContactResponse>, localList: List<ContactEntity>): List<ContactUIData> {
    val result = ArrayList<ContactUIData>()
    result.addAll(remoteList.map { it.toUIData() })

    var index = remoteList.lastOrNull()?.id ?: 0      // face
    localList.forEach { entity ->
        when (entity.statusCode.toStatusEnum()) {
            StatusEnum.ADD -> {
                result.add(entity.toUIData(++index))
            }

            StatusEnum.EDIT -> {
                val findData = result.find { it.id == entity.remoteID }
                if (findData != null) {
                    val findIndex = result.indexOf(findData)
                    val newData = entity.toUIData(findData.id)
                    result[findIndex] = newData
                }
            }
            StatusEnum.DELETE -> {
                val findData = result.find { it.id == entity.remoteID }
                if (findData != null) {
                    val findIndex = result.indexOf(findData)
                    val newData = entity.toUIData(findData.id)
                    result[findIndex] = newData
                }
            }

            StatusEnum.SYNC -> {}
        }
    }

    return result
}