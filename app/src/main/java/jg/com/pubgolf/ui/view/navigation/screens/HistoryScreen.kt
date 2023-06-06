package jg.com.pubgolf.ui.view.navigation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import jg.com.pubgolf.data.model.GameModels.GameResponse
import jg.com.pubgolf.ui.theme.Purple500
import jg.com.pubgolf.ui.view.GameActivity
import jg.com.pubgolf.utils.convertDateTime
import jg.com.pubgolf.viewModel.GameViewModel
import jg.com.pubgolf.viewModel.state.GameState
import jg.com.pubgolf.viewModel.state.StartGameState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(gameViewModel: GameViewModel) {

    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val gamesState by gameViewModel.allGamesState.collectAsState(initial = GameState.Loading)
    val startGameState by gameViewModel.startGameState.collectAsState(initial = StartGameState.Loading)
    val loading = remember { mutableStateOf(false) }
    val selectedGame = remember { mutableStateOf("") }
    val intent = Intent(context, GameActivity::class.java)
    //gameViewModel.getGames() Пофикси фигню с тем, что каждый раз вызывается эта фигня, т.к.
    // перерисовывается экран миллиард раз, я просто не придумал как сделать логику
    //так, чтобы один раз ток вызвалась


    LaunchedEffect(gamesState, startGameState) {
        when (gamesState) {
            is GameState.Loading -> {
                gameViewModel.getGames() // Обновляет один раз, если создать игру не обновит
                loading.value = false
            }
            is GameState.Success -> {
                loading.value = true
            }
            is GameState.Error -> {
                gameViewModel.getGames()
                loading.value = false
                Toast.makeText(context, "Ошибка получения игр!", Toast.LENGTH_LONG).show()
            }
        }
        when (startGameState) {
            is StartGameState.Loading -> {

            }
            is StartGameState.Success -> {
                intent.putExtra("game_info", Gson().toJson(gameViewModel.startGameInfo))
                intent.putExtra("game", selectedGame.value)
                context.startActivity(intent)
            }
            is StartGameState.Error -> {
                Toast.makeText(context, "Ошибка старта игры!", Toast.LENGTH_LONG).show()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading.value) {
            items(gameViewModel.allGamesList.size) { item ->
                GameCard(gameViewModel.allGamesList[item], intent, gameViewModel, selectedGame)
            }
        }
    }
    Spacer(modifier = Modifier.height(100.dp))
}

@Composable
fun GameCard(
    game: GameResponse,
    intent: Intent,
    gameViewModel: GameViewModel,
    selectedGame: MutableState<String>
) {
    val cardColor = remember { mutableStateOf(Color(0x332F3273)) }
    val gameEndStatus = remember { mutableStateOf("") }
    val gameStartStatus = remember { mutableStateOf("Статус: Создана") }

    when (game.status) {
        "started" -> {
            cardColor.value = Color(0x33BFFF09)
            gameStartStatus.value = "Старт: " + convertDateTime(game.start_time)
        }
        "finished" -> {
            cardColor.value = Color(0x33F20202)
            gameStartStatus.value = "Старт: " + convertDateTime(game.start_time)
            gameEndStatus.value = "Завершена: " + convertDateTime(game.finish_time)
        }
        else -> {

        }
    }

    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, cardColor.value),
        backgroundColor = cardColor.value,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(150.dp)
            .padding(10.dp)
            .clickable {
                //Прокидываем инфу о игре
                if (game.status == "created") {
                    selectedGame.value = Gson().toJson(game)
                    gameViewModel.startGame(game.id)
                }
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(

            ) {
                //НУ ДА, ГОВОНОКОД И ЧО ТЫ МНЕ СДЕЛАЕШЬ?? Я В ДРУГОМ ГОРОДЕ
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 20.sp
                )
                Text(
                    text = "Сложность: " + game.difficulty_level,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 16.sp
                )
                Text(
                    text = "Бюджет: " + game.budget_level,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 16.sp
                )
                Text(
                    text = gameStartStatus.value,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 16.sp
                )
                Text(
                    text = gameEndStatus.value,
                    style = MaterialTheme.typography.h1,
                    color = Purple500,
                    fontSize = 16.sp
                )
            }
            //Можно впиндюрить кнопку какую-нибудь
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val currentTime = Date()
    return dateFormat.format(currentTime)
}