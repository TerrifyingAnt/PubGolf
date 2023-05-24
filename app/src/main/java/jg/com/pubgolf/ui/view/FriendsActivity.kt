package jg.com.pubgolf.ui.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.api.RetrofitBuilder
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.ui.view.navigation.screens.FriendsScreen
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.AuthViewModel
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory
import jg.com.pubgolf.viewModel.state.AuthState
import jg.com.pubgolf.viewModel.state.FriendState

class FriendsActivity : ComponentActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    // viewModel пользователя
    private val viewModel: UserViewModel by viewModels {
        ViewModelFactory(
            ApiHelper(
                RetrofitBuilder.provideApiService(RetrofitBuilder.provideRetrofit()),
                sharedPreferencesManager
            )
        )
    }

    var loading: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PubGolfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val context = LocalContext.current
                    sharedPreferencesManager = SharedPreferencesManager(context)
                    // состояние ответа в вьюмоделе
                    val state by viewModel.friendState.collectAsState(initial = FriendState.Loading)

                    LaunchedEffect(state) {
                        viewModel.getFriends()
                        if (state is FriendState.Success) {
                            loading = false
                        }
                        if (state is FriendState.Error) {
                            Toast.makeText(context, "Ошибка в получении друзей", Toast.LENGTH_LONG).show()
                        }
                    }
                    FriendsScreen(loading)
                }
            }
        }
    }
}