package jg.com.pubgolf.interfaces

import jg.com.pubgolf.classes.Player
import io.reactivex.rxjava3.core.Single
import jg.com.pubgolf.classes.Room
import jg.com.pubgolf.debugutils.Constants
import jg.com.pubgolf.classes.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // метод для регистрации пользователя
    @POST(Constants.base + Constants.registerLink)
    fun registerUser(@Body player: Player): Single<UserResponse>

    // метод для авторизации пользователя
    @POST(Constants.base + Constants.enterLink)
    fun loginUser(@Body player: Player): Single<UserResponse>

    // метод для получения истории игр
    @POST(Constants.base + Constants.getGamesLink)
    fun getGames(@Body player: Player): Single<List<Room>>
}