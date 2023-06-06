package jg.com.pubgolf.ui.view.navigation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import jg.com.pubgolf.R
import jg.com.pubgolf.data.model.FriendModels.FriendResponse
import jg.com.pubgolf.data.model.GameModels.*
import jg.com.pubgolf.viewModel.GameViewModel
import jg.com.pubgolf.viewModel.UserViewModel
import jg.com.pubgolf.viewModel.state.FinishGameState
import jg.com.pubgolf.viewModel.state.UserState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GameScreen(
    game: GameResponse,
    gameInfo: StartGameResponse,
    userViewModel: UserViewModel,
    gameViewModel: GameViewModel
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    val usersScore = remember { mutableMapOf<Int, Int>() }
    val usersState by userViewModel.allUsersState.collectAsState(initial = UserState.Loading)
    val finishGameState by gameViewModel.finishGameState.collectAsState(initial = FinishGameState.Loading)
    val users = remember { mutableStateOf<List<FriendResponse>>(emptyList()) }
    val userListReady = remember { mutableStateOf(false) } // КОСТЫЛЬ :D

    LaunchedEffect(usersState, finishGameState) {
        when (usersState) {
            is UserState.Loading -> {
                userViewModel.getUsers()
            }
            is UserState.Success -> {
                users.value = userViewModel.allUsersList
                userListReady.value = true
            }
            is UserState.Error -> {

            }
        }
        when (finishGameState) {
            is FinishGameState.Loading -> {

            }
            is FinishGameState.Success -> {
                activity.finish()
            }
            is FinishGameState.Error -> {

            }
        }
    }
    val firstRedraw = remember { mutableStateOf(false) } // КОСТЫЛЬ2 :D

    if (!firstRedraw.value) {
        for (stat in game.stats) {
            usersScore[stat.user] = 0
        }
        firstRedraw.value = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(game.name) },
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = {
                        activity.finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userListReady.value) {
                LazyColumn(
                    modifier = Modifier
                        .padding(15.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0x332F3273))
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(text = "Игроки")
                        for (user in usersScore) {
                            Row(
                                modifier = Modifier.width(230.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(text = "ID: " + user.key.toString())
                                Text(text = "  |  Имя: " + userViewModel.allUsersList.find { it.id == user.key }!!.username)
                            }
                        }
                    }
                }
            }
            GameCard(usersScore, gameInfo.stages)
            Button(
                onClick = {
                    val winner = usersScore.minByOrNull { it.value }
                    usersScore.remove(winner!!.key)
                    val userList = mutableListOf<User>()
                    userList.add(User(winner.key, "won"))
                    for (user in usersScore) {
                        if (user.value == winner.value)
                            userList.add(User(user.key, "won"))
                        else
                            userList.add(User(user.key, "lost"))
                    }
                    gameViewModel.finishGame(userList, game.id)
                }) {
                Text(text = "Завершить игру")
            }
            LazyColumn(
                modifier = Modifier
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0x332F3273))
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(text = "Адреса баров/пабов")
                    for (stage in gameInfo.stages) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(text = "Название: " + stage.pub.name)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(text = "Адрес: " + stage.pub.pub_address)
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable//ЭТО ПИЗДЕЦ, БРАТ
fun GameCard(usersScore: MutableMap<Int, Int>, stages: List<Stage>) {
    val usersId = usersScore.keys
    val spacer = remember { mutableStateOf(20) }


    LazyRow() {
        item {
            LazyColumn(
                modifier = Modifier
                    .padding(1.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0x332F3273))
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(stages) { stage ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (stage.pub.name.length < spacer.value)
                            stage.pub.name =
                                stage.pub.name + "  ".repeat(spacer.value - stage.pub.name.length)

                        Text(text = stage.pub.name)
                        for (user in usersId) {
                            Text(text = "           $user    ")
                        }
                    }
                    for (drink in stage.drinks) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (drink.alcohol_name.length < spacer.value)
                                drink.alcohol_name =
                                    drink.alcohol_name + "  ".repeat(spacer.value - drink.alcohol_name.length)

                            Text(text = drink.alcohol_name)
                            for (user in usersId) {
                                Row() {
                                    val counter = remember { mutableStateOf(0) }
                                    val userId = remember { mutableStateOf(user) }
                                    Icon(
                                        modifier = Modifier.clickable {
                                            if (counter.value > 0) {
                                                counter.value--
                                                val score = usersScore[userId.value]
                                                usersScore[userId.value] = score!! - 1
                                            }
                                        },
                                        painter = painterResource(id = R.drawable.baseline_remove_24),
                                        contentDescription = "remove"
                                    )
                                    Text(text = "" + counter.value.toString() + "")
                                    Icon(
                                        modifier = Modifier.clickable {
                                            counter.value++
                                            val score = usersScore[userId.value]
                                            usersScore[userId.value] = score!! + 1
                                        },
                                        painter = painterResource(id = R.drawable.baseline_add_24),
                                        contentDescription = "add"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}