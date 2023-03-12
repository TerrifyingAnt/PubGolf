package jg.com.pubgolf.classes

data class Room(
    val id: Int,
    val about: String,
    val player: Player,
    val game: Game
)