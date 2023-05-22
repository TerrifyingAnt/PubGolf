package jg.com.pubgolf.ui.view.navigation.screens

import jg.com.pubgolf.R
import android.app.Activity
import androidx.compose.foundation.*
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

@Composable
fun DetailProfileScreen() {

    val activity = LocalContext.current as Activity

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
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
                .padding(paddingValues = it)
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .height(165.dp)
                    .padding(15.dp)
                    .clip(RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    //СЮДА ПОДРУБАЕМ ФОТКУ ПОЛЬЗОВАТЕЛЯ
                    //Надо наверное будет сделать фото по дефолту, но мб потом
                    painter = painterResource(id = R.drawable.shrek),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .clickable {
                            //СМЕНА ФОТКИ
                        }
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "ID:228", //СЮДА ПОДРУБАЕМ ID ПОЛЬЗОВАТЕЛЯ
                    style = MaterialTheme.typography.h1,
                    fontSize = 26.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            InfoCard(
                iconId = R.drawable.outline_account_circle_24,
                hint = "Логин",
                title = "Shrek" //Сюда логин
            )
            InfoCard(
                iconId = R.drawable.baseline_alternate_email_24,
                hint = "Эл. почта",
                title = "Shrek@boloto.com" //Сюда почту
            )
            InfoCard(
                iconId = R.drawable.outline_phone_24,
                hint = "Номер телефона",
                title = "+7(933)-231-32-23" //Сюда номер телефона
            )
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp),
                onClick = {
                    //Изменить инфу об аккаунте
                },
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Изменить",
                    style = MaterialTheme.typography.h1,
                    color = Color.White,
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
fun InfoCard(iconId: Int, hint: String, title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(10.dp),
        elevation = 0.dp
    ) {
        Row() {
            Card(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .size(60.dp),
                elevation = 2.dp,
                shape = RoundedCornerShape(90.dp)
            ) {
                Image(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(id = iconId), contentDescription = ""
                )
            }
            Column(

            ) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.h1,
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.h1,
                    fontSize = 26.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
                Divider(
                    modifier = Modifier.padding(end = 40.dp),
                    color = MaterialTheme.colors.primary,
                    thickness = 1.dp
                )
            }
        }
    }
}