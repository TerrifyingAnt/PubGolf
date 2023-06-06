package jg.com.pubgolf.data.model.GameModels

data class FinishGameResponse(
    val users: List<User>
)

data class User(
    val user: Int,
    val player_status: String
)