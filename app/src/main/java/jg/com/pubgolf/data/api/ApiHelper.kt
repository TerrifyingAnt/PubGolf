package jg.com.pubgolf.data.api

import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.data.model.RegisterationModels.RegistrationRequest

class ApiHelper(private val apiService: ApiService) {

    // авторизация пользователя
    suspend fun auth(request: AuthRequest) = apiService.auth(request)

    // регистрация пользователя
    suspend fun register(request: RegistrationRequest) = apiService.register(request)
}