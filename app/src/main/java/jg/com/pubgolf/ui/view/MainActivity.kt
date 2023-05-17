package jg.com.pubgolf.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.ui.view.navigation.BottomMenuItem
import jg.com.pubgolf.ui.view.navigation.MainNavigation
import jg.com.pubgolf.utils.SharedPreferencesManager

import java.util.*
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesManager = SharedPreferencesManager(this)

        setContent {
            PubGolfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    MainNavigation()
                    println("из активити" + sharedPreferencesManager.getVal("token")!!)
                }
            }
        }
    }
}




