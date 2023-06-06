package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.GameModels.StartGameResponse

sealed class StartGameState{
    object Loading : StartGameState()
    data class Success(val startedGame: StartGameResponse) : StartGameState()
    data class Error(val message: String) : StartGameState()
}
