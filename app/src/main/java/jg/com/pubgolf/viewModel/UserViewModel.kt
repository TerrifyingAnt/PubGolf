package jg.com.pubgolf.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.state.AuthState
import jg.com.pubgolf.viewModel.state.FriendState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val apiHelper: ApiHelper, val sharedPreferencesManager: SharedPreferencesManager) : ViewModel()  {

    private val _friendState = MutableStateFlow<FriendState>(FriendState.Loading)
    val friendState: StateFlow<FriendState> = _friendState

    fun getFriends() {
        viewModelScope.launch {
            _friendState.value = FriendState.Loading
            try {
                val friendList = apiHelper.getFriends()
                _friendState.value = FriendState.Success(friendList)
            } catch (e: Exception) {
                _friendState.value = FriendState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }
}
