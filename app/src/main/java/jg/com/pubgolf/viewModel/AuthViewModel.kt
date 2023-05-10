package jg.com.pubgolf.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.data.model.RegisterationModels.RegistrationRequest
import jg.com.pubgolf.viewModel.state.AuthState
import jg.com.pubgolf.viewModel.state.RegistrationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import retrofit2.HttpException


class AuthViewModel(private val apiHelper: ApiHelper) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Loading)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun authenticateUser(request: AuthRequest) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val authResponse = apiHelper.auth(request)
                _authState.value = AuthState.Success(authResponse)
                // TODO:
                // запись в шейред преференсес
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }

    fun registerUser(request: RegistrationRequest) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            try {
                val registrationResponse = apiHelper.register(request)
                _registrationState.value = RegistrationState.Success(registrationResponse)

            } catch (e: HttpException) {
                try {
                    val errorBody = e.response()?.errorBody()?.string() ?: ""
                    val errorResponse = parseErrorResponse(errorBody)
                    _registrationState.value = RegistrationState.Error(errorResponse)
                } catch (ex: Exception) {
                    _registrationState.value = RegistrationState.Error("Что-то пошло не так")
                }
            }
        }
    }

    fun parseErrorResponse(errorBody: String): String {
        val jsonObject = JSONObject(errorBody)
        val errorMessages = mutableListOf<String>()

        // Iterate through each field and its error messages
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val errorArray = jsonObject.getJSONArray(key)
            for (i in 0 until errorArray.length()) {
                val errorMessage = errorArray.getString(i)
                errorMessages.add("$key: $errorMessage")
            }
        }

        return errorMessages.joinToString("\n")
    }

}