package jg.com.pubgolf.ui.view

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
import jg.com.pubgolf.ui.view.navigation.screens.DetailProfileScreen
import jg.com.pubgolf.utils.SharedPreferencesManager
import jg.com.pubgolf.viewModel.AuthViewModel
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory
import jg.com.pubgolf.viewModel.state.FriendState
import jg.com.pubgolf.viewModel.state.MeState

class DetailProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PubGolfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    DetailProfileScreen()
                }
            }
        }
    }
}