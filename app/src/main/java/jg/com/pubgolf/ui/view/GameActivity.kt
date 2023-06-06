package jg.com.pubgolf.ui.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.api.RetrofitBuilder
import jg.com.pubgolf.data.model.GameModels.GameResponse
import jg.com.pubgolf.data.model.GameModels.StartGameResponse
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.ui.view.navigation.screens.GameScreen
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.GameViewModel
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory

class GameActivity : ComponentActivity()  {
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    // viewModel пользователя
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory(
            ApiHelper(
                RetrofitBuilder.provideApiService(RetrofitBuilder.provideRetrofit()),
                sharedPreferencesManager
            )
        )
    }

    private val gameViewModel: GameViewModel by viewModels {
        ViewModelFactory(
            ApiHelper(
                RetrofitBuilder.provideApiService(RetrofitBuilder.provideRetrofit()),
                sharedPreferencesManager
            )
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            sharedPreferencesManager = SharedPreferencesManager(context)
            PubGolfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val gameJson = intent.getStringExtra("game")
                    val game = Gson().fromJson(gameJson, GameResponse::class.java)

                    val gameInfoJson = intent.getStringExtra("game_info")
                    val gameInfo = Gson().fromJson(gameInfoJson, StartGameResponse::class.java)

                    GameScreen(game, gameInfo, userViewModel, gameViewModel)
                }
            }
        }
    }
}