package jg.com.pubgolf.viewModel.state

import jg.com.pubgolf.data.model.GameModels.User

sealed class FinishGameState {
    object Loading : FinishGameState()
    data class Success(val finishGameResponse: List<User>) : FinishGameState()
    data class Error(val message: String) : FinishGameState()
}