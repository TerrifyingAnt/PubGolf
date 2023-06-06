package jg.com.pubgolf.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.model.GameModels.*
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.state.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    val apiHelper: ApiHelper,
    val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    //Список всех игр
    private val _allGamesState = MutableStateFlow<GameState>(GameState.Loading)
    val allGamesState: StateFlow<GameState> = _allGamesState
    private var _allGamesList = mutableStateOf<List<GameResponse>>(emptyList())
    val allGamesList: List<GameResponse>
        get() = _allGamesList.value

    private val _newGameState = MutableStateFlow<NewGameState>(NewGameState.Loading)
    val newGameState: StateFlow<NewGameState> = _newGameState

    private val _finishGameState = MutableStateFlow<FinishGameState>(FinishGameState.Loading)
    val finishGameState: StateFlow<FinishGameState> = _finishGameState

    private val _startGameState = MutableStateFlow<StartGameState>(StartGameState.Loading)
    val startGameState: StateFlow<StartGameState> = _startGameState
    private val _startGameInfo = MutableStateFlow(StartGameResponse(0, emptyList()))
    val startGameInfo: StartGameResponse
        get() = _startGameInfo.value

    // получить все игры
    fun getGames() {
        viewModelScope.launch {
            _allGamesState.value = GameState.Loading
            try {
                _allGamesList.value = apiHelper.getAllGames().toMutableList()
                _allGamesState.value = GameState.Success(allGamesList)
            } catch (e: Exception) {
                _allGamesState.value = GameState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }

    fun createNewGame(request: NewGameRequest) {
        viewModelScope.launch {
            _newGameState.value = NewGameState.Loading
            try {
                val newGameRequest = apiHelper.createGame(request)
                _newGameState.value = NewGameState.Success(newGameRequest)
            } catch (e: Exception) {
                _newGameState.value = NewGameState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }

    fun startGame(gameId: Int){
        viewModelScope.launch {
            _startGameState.value = StartGameState.Loading
            try {
                _startGameInfo.value = apiHelper.startGame(gameId)
                _startGameState.value = StartGameState.Success(startGameInfo)
            } catch (e: Exception) {
                _startGameState.value = StartGameState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }

    fun finishGame(request: List<User>, gameId: Int) {
        viewModelScope.launch {
            _finishGameState.value = FinishGameState.Loading
            try {
                val finishGameRequest = apiHelper.finishGame(request, gameId)
                _finishGameState.value = FinishGameState.Success(finishGameRequest)
            } catch (e: Exception) {
                _finishGameState.value = FinishGameState.Error(e.message ?: "Возникла ошибка")
            }
        }
    }
}