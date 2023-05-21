package jg.com.pubgolf.ui.view.navigation.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jg.com.pubgolf.R
import jg.com.pubgolf.ui.view.AboutUsActivity
import jg.com.pubgolf.ui.view.AchievementActivity
import jg.com.pubgolf.ui.view.DetailProfileActivity
import jg.com.pubgolf.ui.view.FriendsActivity


@Composable
fun ProfileScreen() {

    val context = LocalContext.current

    val activity = LocalContext.current as Activity

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
                        .clickable {
                            //СМЕНА ФОТКИ
                        }
                        .clip(shape = RoundedCornerShape(10.dp))
                )
                Column(
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = "SHREK", //СЮДА ПОДРУБАЕМ НИК ПОЛЬЗОВАТЕЛЯ
                        style = MaterialTheme.typography.h1,
                        fontSize = 26.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "ID:228", //СЮДА ПОДРУБАЕМ ID ПОЛЬЗОВАТЕЛЯ
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
                MenuCard(
                    iconId = R.drawable.baseline_workspace_achievement_24,
                    title = "Достижения"
                ) {
                    context.startActivity(Intent(context, AchievementActivity::class.java))
                    //Активити с достижениями
                }
                MenuCard(iconId = R.drawable.baseline_info_24, title = "О нас") {
                    context.startActivity(Intent(context, AboutUsActivity::class.java))
                    //Активити "О нас"
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp),
                    onClick = {
                        //Выход из аккаунта
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
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 32.sp
                    )
                }
            }
        }
    }
    /* TODO:
    делать тут не мало на самом деле
    попробуй сделать ui пока
    я не вывез
    я пока подключил шарды умер
    главная просьба наверн по ui
    любой переход на другую страницу делай активностью, а не навграфами
    так будет логичнее
    а так, что тут за ui будет - думай сам
    в плане
    по идее, тут должен быть список кнопок по типу
    профиль -> открывает активити с инфой о пользователе, которую можно поменять
    друзья -> открывает активити с инфой о друзьях (просто список, оставь его пустым, я вью модель потом напишу)
    хз, кнопка о нас -> оставь пока пустой
    и кнопка выйти, тоже оставь пока пустой


    еще мем
    когда будешь переходить на другие активити, закрывай предыдущие через finish()

    а, и постарайся сделать его больше модульным - то есть логически разбей на @composable функци, чтобы не было все в одном месте
    в идеале даже на файлики разбить, чтобы каждая активность была в своем файлике
    но тут как сам посчитаешь нужным
     */
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