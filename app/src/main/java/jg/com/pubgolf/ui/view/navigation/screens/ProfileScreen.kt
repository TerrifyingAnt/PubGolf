package jg.com.pubgolf.ui.view.navigation.screens

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.core.content.ContextCompat.startActivity
import jg.com.pubgolf.R
import jg.com.pubgolf.ui.view.AboutUsActivity
import jg.com.pubgolf.ui.view.DetailProfileActivity
import jg.com.pubgolf.ui.view.FriendsActivity
import jg.com.pubgolf.ui.view.LoginActivity
import jg.com.pubgolf.utils.SharedPreferencesManager


@Composable
fun ProfileScreen() {

    val context = LocalContext.current

    val activity = LocalContext.current as Activity

    val sharedPreferencesManager = SharedPreferencesManager(context)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary),
        ) {
            Row(
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    //СЮДА ПОДРУБАЕМ ФОТКУ ПОЛЬЗОВАТЕЛЯ
                    //Надо наверное будет сделать фото по дефолту, но мб потом
                    painter = painterResource(id = R.drawable.shrek),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = 17.dp)
                        .size(100.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .clickable {
                            //СМЕНА ФОТКИ
                        }
                )
                Column(
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = sharedPreferencesManager.getVal("username")!!, //СЮДА ПОДРУБАЕМ НИК ПОЛЬЗОВАТЕЛЯ
                        style = MaterialTheme.typography.h1,
                        fontSize = 26.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "ID: " + sharedPreferencesManager.getVal("id")!!, //СЮДА ПОДРУБАЕМ ID ПОЛЬЗОВАТЕЛЯ
                        style = MaterialTheme.typography.h1,
                        fontSize = 26.sp
                    )
                }
                Icon(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 12.dp)
                        .size(28.dp)
                        .clickable {
                            //СМЕНА НИКА, МБ ДЛЯ ПРОСТОТЫ ПРОСТО ОТКРЫВАТЬ ДИАЛОГОВОЕ ОКНО
                            //ИЛИ КАК ОНО ТАМ НАЗЫВАЕТСЯ
                        },
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "",
                    tint = MaterialTheme.colors.primaryVariant
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuCard(iconId = R.drawable.baseline_manage_accounts_24, title = "Профиль") {
                    //Активити с настройками профиля и более подробной инфой
                    context.startActivity(Intent(context, DetailProfileActivity::class.java))
                    //activity.finish()
                }
                MenuCard(iconId = R.drawable.baseline_people_24, title = "Друзья") {
                    context.startActivity(Intent(context, FriendsActivity::class.java))
                    //Активити с друзьями
                }
                //RIP 22.05.2023 КНОПКА С ДОСТИЖЕНИЯМИ
                MenuCard(iconId = R.drawable.baseline_info_24, title = "О нас") {
                    context.startActivity(Intent(context, AboutUsActivity::class.java))
                    //Активити "О нас"
                }
                Button(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    onClick = {
                        sharedPreferencesManager.saveVal("token", "")
                        activity.startActivity(Intent(context, LoginActivity::class.java))
                        activity.finish()
                    },
                    colors = ButtonDefaults
                        .buttonColors(
                            backgroundColor = MaterialTheme.colors.primary
                        ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "ВЫЙТИ",
                        style = MaterialTheme.typography.h1,
                        color = Color.White,
                        fontSize = 28.sp
                    )
                }
            }
        }
    }
}
@Composable
fun MenuCard(iconId: Int, title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(8.dp)
            .background(MaterialTheme.colors.background),
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable
                {
                    onClick()
                }
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                //horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    painter = painterResource(id = iconId),
                    contentDescription = "",
                    tint = MaterialTheme.colors.primaryVariant
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = title,
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.primaryVariant,
                    fontSize = 32.sp
                )
            }
        }
    }
}