package jg.com.pubgolf.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val apiHelper: ApiHelper, val sharedPreferencesManager: SharedPreferencesManager) : ViewModel()  {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    fun authenticateUser(request: AuthRequest) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val authResponse = apiHelper.auth(request)
                _authState.value = AuthState.Success(authResponse)
                sharedPreferencesManager.saveVal("token", authResponse.auth_token)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }
}
