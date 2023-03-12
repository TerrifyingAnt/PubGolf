package jg.com.pubgolf.debugutils

import io.reactivex.rxjava3.disposables.CompositeDisposable
import jg.com.pubgolf.classes.Player
import jg.com.pubgolf.interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Constants{
        // ССЫЛКИ
        const val phoneTest: String = "192.168.0.146" // если тестить через отладку по телефону, то юзать это
        const val emulatorTest: String = "192.168.56.1" // если тестить через эмулятор - указать через ipconfig свой ip
        const val base: String = "http://$emulatorTest:8081/api/" // по умолчанию тестим через эмулятор
        const val registerLink: String = "register" // ссылка для регистрации
        const val enterLink: String = "login" // ссылка для входа
        const val getGamesLink: String = "get-all-games" // ссылка для получения истории игр

        // переменные
        // юзается везде, где нужно тыкать сервер
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(base)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

        // юзается там же, где и переменная выше
        val apiService = retrofit.create(ApiService::class.java)

        val disposable = CompositeDisposable()

        // пользователь, который зашел (на самом деле не очень нужна, потом удалить нужно)
        // TODO
        var enteredPlayer: Player = Player("", "", "")

        // переменные для shared preferences
        val PREFS_NAME = "USER"
        val LOGIN_KEY = "login"
        val PASSWORD_KEY = "password"
        val NAME_KEY = "name"

}