package jg.com.pubgolf.classes

data class Game(
    val id: Int,
    val price: Double,
    val player_count: Int,
    val startTime: String?,
    val endTime: String?,
    val complexityLevel: String,
    val budgetLevel: String,
    val status: String
)