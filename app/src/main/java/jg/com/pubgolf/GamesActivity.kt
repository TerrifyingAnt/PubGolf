package jg.com.pubgolf

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import jg.com.pubgolf.classes.Room
import jg.com.pubgolf.classes.Player
import jg.com.pubgolf.debugutils.Constants.apiService
import jg.com.pubgolf.ui.theme.Gray
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.ui.theme.Purple200
import jg.com.pubgolf.ui.theme.Purple700

class GamesActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PubGolfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                )
                {
                    ScaffoldMain()
                }
            }
        }
    }
}


// ui вместе с верхней панелькой и нижней навигацией
@Composable
fun ScaffoldMain() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.background(Gray),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(65.dp),
                content = {
                    Column(Modifier.fillMaxHeight()) {
                        Row(verticalAlignment = Alignment.Top) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Pub",
                                fontFamily = FontFamily(Font(R.font.gotham_bold)),
                                fontSize = 18.sp,
                                color = Purple700,
                            )
                            Text(
                                text = "Golf",
                                fontFamily = FontFamily(Font(R.font.gotham_bold)),
                                fontSize = 18.sp,
                                color = Purple200
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "Игры",
                                Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.gotham_bold)),
                                fontSize = 25.sp,
                                color = Purple700
                            )
                        }
                    }
                },
                backgroundColor = Gray,
                elevation = 0.dp
            )
        },
        content = { NavHost(navController, startDestination = "gamesScreen") {
            composable("gamesScreen") { MainScreen(LocalContext.current, navController = navController) }
            composable("userProfileScreen") { UserScreen(navController = navController) }
            composable("settingsScreen") { SettingsScreen(navController = navController) }
        } }, bottomBar = {
            BottomNavigation(
                backgroundColor = Gray,
                elevation = 0.dp
            ) {
                BottomNavigationItem(
                    selected = navController.currentDestination?.route == "gamesScreen",
                    onClick = { navController.navigate("gamesScreen",
                        navOptions = NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo("gamesScreen", true)
                            .build() )},
                    icon = { Image(painter = painterResource(id = R.drawable.ic_cheers), contentDescription = "Home") },
                )
                BottomNavigationItem(
                    selected = navController.currentDestination?.route == "userProfileScreen",
                    onClick = { navController.navigate("userProfileScreen",
                        navOptions = NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo("userProfileScreen", true)
                            .build()
                    ) },
                    icon = { Image(painter = painterResource(id = R.drawable.ic_user), contentDescription = "User") },
                )
                BottomNavigationItem(
                    selected = navController.currentDestination?.route == "settingsScreen",
                    onClick = { navController.navigate("settingsScreen",
                        navOptions = NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo("settingsScreen", true)
                            .build()
                    )},
                    icon = { Image(painter = painterResource(id = R.drawable.ic_settings), contentDescription = "Settings") },
                )
            }
        }
    )
}

@Composable
fun UserScreen(navController: NavController) {
    Text(text = "Профиль пользователя скоро будет!")
}

@Composable
fun SettingsScreen(navController: NavController) {
    Text(text = "Настройки скоро будут!")
}


@Composable
fun MainScreen(context: Context, navController: NavController) {
    val roomState = remember { mutableStateOf(emptyList<Room>()) }

    DisposableEffect(Unit) {
        val disposable = getGamesList(context)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                roomState.value = result
            }, { error ->
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                Log.e("MyTag", "Error: ${error.message}", error)
            })

        onDispose {
            disposable.dispose()
        }
    }

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item() {
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Spacer(modifier = Modifier.fillMaxWidth(0.09f))
                Surface(
                    modifier = Modifier.fillMaxWidth(0.9f).clickable(onClick = {
                        context.startActivity(Intent(context, CreateRoomActivity::class.java))
                        (context as Activity).finish()
                    }),
                    color = Gray,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = "+",
                        fontFamily = FontFamily(Font(R.font.gotham_bold)),
                        textAlign = TextAlign.Center,
                        color = Purple700,
                        fontSize = 80.sp
                    )
                }
            }
        }
        items(roomState.value) { room ->
            // Display the myObject data here
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Spacer(modifier = Modifier.fillMaxWidth(0.09f))
                Surface(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    color = Gray,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // НАЗВАНИЕ КОМНАТЫ
                        Text(text = room.about, color = Purple700,
                            fontFamily = FontFamily(Font(R.font.gotham_bold)),
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp)

                        // СЛОЖНОСТЬ ИГРЫ
                        Text(text = "Сложность: " + room.game.complexityLevel,
                            color = Purple700,
                            fontFamily = FontFamily(Font(R.font.gotham_bold)),
                            textAlign = TextAlign.Center)

                        // СТОИМОСТЬ ИГРЫ
                        Text(text = "Бюджет: " + room.game.budgetLevel, color = Purple700,
                            fontFamily = FontFamily(Font(R.font.gotham_bold)),
                            textAlign = TextAlign.Center)

                        // КОЛИЧЕСТВО ИГРОКОВ
                        Text(
                            text = "Количество игроков: " + room.game.player_count.toString(),
                            color = Purple700,
                            fontFamily = FontFamily(Font(R.font.gotham_bold)),
                            textAlign = TextAlign.Center
                        )

                        // СТАТУС ИГРЫ
                        Text(text = "Статус: " + room.game.status,
                            color = Purple700,
                            fontFamily = FontFamily(Font(R.font.gotham_bold)),
                            textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

// метод получения игр
fun getGamesList(context: Context): Single<List<Room>> {
    return apiService.getGames(Player("", loadCredentials(context).email, ""))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext { throwable: Throwable ->
            showToastErrors(context, context.getString(R.string.error), throwable)
            Single.error(throwable)
        }
}



