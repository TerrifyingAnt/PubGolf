package jg.com.pubgolf.ui.view.navigation.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jg.com.pubgolf.R


@Composable
fun FriendsScreen(loading: Boolean) {

    val activity = LocalContext.current as Activity

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        topBar = {
            TopAppBar(
                title = { Text("Друзья") },
                navigationIcon = {
                    IconButton(onClick = {
                        activity.finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                })
        }
    ) {
        if (!loading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = it)
            ) {
                // TODO
                // на вход приходит список из класса FriendResponse
                //Сюда надо lazycolumn будет впиндюрить
                FriendCard(image = R.drawable.shrek, name = "Негр1")
                FriendCard(image = R.drawable.shrek, name = "Негр2")
                FriendCard(image = R.drawable.shrek, name = "Негр3")
            }
        }
        else {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

//Хз как там фотку передвать, пока айдишником
@Composable
fun FriendCard(image: Int, name: String) {
    //Добавить карточку для друзей
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(4.dp)
        ,
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Row(
            modifier = Modifier.clickable {

            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(10.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = image),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 35.dp),
                text = name,
                style = MaterialTheme.typography.h1,
                fontSize = 28.sp,
                color = Color.White
            )
        }
    }
}