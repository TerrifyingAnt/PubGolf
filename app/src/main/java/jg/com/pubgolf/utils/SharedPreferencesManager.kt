package jg.com.pubgolf.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jg.com.pubgolf.R
import jg.com.pubgolf.data.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesManager @Inject constructor(@ApplicationContext val context: Context) {

    private val appContext = context.applicationContext

    private val preferences: SharedPreferences
        get() = appContext.getSharedPreferences(appContext.resources.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    fun saveVal(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }
    fun getVal(key: String): String? {
        return preferences.getString(key, null)
    }
}

