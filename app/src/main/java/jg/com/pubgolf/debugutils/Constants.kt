package jg.com.pubgolf.debugutils

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import io.reactivex.rxjava3.disposables.CompositeDisposable
import jg.com.pubgolf.classes.User
import jg.com.pubgolf.interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Constants{
        //ссылки
        const val phoneTest: String = "192.168.0.146"
        const val emulatorTest: String = "192.168.56.1"
        const val base: String = "http://$emulatorTest:8081/api/"
        const val registerLink: String = "register"
        const val enterLink: String = "enter"

        // переменные
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(base)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()

        val apiService = retrofit.create(ApiService::class.java)

        val disposable = CompositeDisposable()

        var enteredUser: User = User("", "", "")

        val PREFS_NAME = "USER"
        val LOGIN_KEY = "login"
        val PASSWORD_KEY = "password"
        val NAME_KEY = "name"


}