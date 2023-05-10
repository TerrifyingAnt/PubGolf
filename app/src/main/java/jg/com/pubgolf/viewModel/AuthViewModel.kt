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
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody




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

    // TODO: так делать нельзя и ссылки нужно хотя бы куда-нибудь вынести
    fun registerUser(request: RegistrationRequest) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            try {
                val registrationResponse = apiHelper.register(request)
                _registrationState.value = RegistrationState.Success(registrationResponse)
                print("8===========D" + registrationResponse)

            } catch (e: Exception) {
                print(e.message)
                val thread = Thread {
                    try {
                        val client = OkHttpClient()
                        val requestBuilder: Request.Builder = Request.Builder()
                        val JSON: MediaType = MediaType.get("application/json; charset=utf-8")
                        val body: RequestBody = RequestBody.create(JSON, request.toString())
                        requestBuilder.post(body)
                        requestBuilder.url("http://10.0.2.2:8000/api/v1/users/")
                        val response = client.newCall(requestBuilder.build()).execute()
                        _registrationState.value = RegistrationState.Error(response)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                thread.start()
            }
        }
    }


}