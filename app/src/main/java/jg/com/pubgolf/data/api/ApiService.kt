package jg.com.pubgolf.data.api

import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.data.model.AuthModels.AuthResponse
import jg.com.pubgolf.data.model.RegisterationModels.RegistrationRequest
import jg.com.pubgolf.data.model.RegisterationModels.UserInfoResponse
import retrofit2.http.*


interface ApiService {

    @POST("auth/token/login/")
    suspend fun auth(@Body request: AuthRequest): AuthResponse

    @POST("users/")
    suspend fun register(@Body request: RegistrationRequest): UserInfoResponse

}