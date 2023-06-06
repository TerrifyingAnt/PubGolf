package jg.com.pubgolf.data.model.GameModels

data class StartGameResponse(
    val id: Int,
    val stages: List<Stage>
)

data class Stage(
    val id: Int,
    val pub: Pub,
    val drinks: List<Drink>
)

data class Pub(
    var name: String,
    val pub_address: String
)

data class Drink(
    val id: Int,
    var alcohol_name: String,
    val alcohol_percent: Int,
    val cost: Int
)