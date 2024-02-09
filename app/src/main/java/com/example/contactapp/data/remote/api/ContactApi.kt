package com.example.contactapp.data.remote.api


import com.example.contactapp.data.remote.request.ContactAddData
import com.example.contactapp.data.remote.request.ContactEditData
import com.example.contactapp.data.remote.request.ContactLoginRequest
import com.example.contactapp.data.remote.request.ContactNumberRequest
import com.example.contactapp.data.remote.request.ContactUserDataRequest
import com.example.contactapp.data.remote.response.ContactErrorResponse
import com.example.contactapp.data.remote.response.ContactResponse
import com.example.contactapp.data.remote.response.ContactSuccessRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface ContactApi {
    @GET("api/v1/contact")
    suspend fun getAllContacts(@Header("token") token:String): Response<List<ContactResponse>>

    @POST("api/v1/contact")
    suspend fun addContact(@Header("token") token:String, @Body contactAddData: ContactAddData): Response<ContactResponse>

    @POST("api/v1/register")
    suspend fun registerUser(@Body userDataRequest: ContactUserDataRequest) : Response<ContactErrorResponse>

    @POST("api/v1/register/verify")
    suspend fun registerUserNumber(@Body userNumberRequest: ContactNumberRequest) : Response<ContactSuccessRegisterResponse>

    @POST("api/v1/login")
    suspend fun loginUserProfile(@Body userLoginRequest: ContactLoginRequest) : Response<ContactSuccessRegisterResponse>


    @DELETE("api/v1/contact")
    suspend fun deleteContact(@Header("token") token: String, @Query("id") id: Int): Response<ContactResponse>

    @PUT("api/v1/contact")
    suspend fun editContact(@Header("token") token :String, @Body contactEditData: ContactEditData): Response<ContactResponse>
}