package jg.com.pubgolf.data.api

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.com.pubgolf.R
import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.data.model.RegisterationModels.RegistrationRequest
import jg.com.pubgolf.utils.SharedPreferencesManager
import retrofit2.http.Header
import javax.inject.Inject


class ApiHelper @Inject constructor(val apiService: ApiService, val sharedPreferencesManager: SharedPreferencesManager) {

    // авторизация пользователя
    suspend fun auth(request: AuthRequest) = apiService.auth(request)

    // регистрация пользователя
    suspend fun register(request: RegistrationRequest) = apiService.register(request)

    // получение списка пользователей
    suspend fun getFriends() = apiService.getFriends("Token " + sharedPreferencesManager.getVal("token"))

    // получение информации о самом себе
    suspend fun getMe() = apiService.getMe("Token " + sharedPreferencesManager.getVal("token"))
}

