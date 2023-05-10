package jg.com.pubgolf.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private val PREFS_NAME = "pubgolf_user"
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // TODO сохранение пользователя
    fun saveUser() {

    }

    // TODO сохранение токенов
    fun saveToken() {

    }

}