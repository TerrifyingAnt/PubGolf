package jg.com.pubgolf

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import jg.com.pubgolf.classes.User
import jg.com.pubgolf.classes.UserResponse
import jg.com.pubgolf.debugutils.Constants.LOGIN_KEY
import jg.com.pubgolf.debugutils.Constants.NAME_KEY
import jg.com.pubgolf.debugutils.Constants.PASSWORD_KEY
import jg.com.pubgolf.debugutils.Constants.PREFS_NAME
import jg.com.pubgolf.debugutils.Constants.apiService
import jg.com.pubgolf.debugutils.Constants.disposable
import jg.com.pubgolf.debugutils.Constants.enteredUser
import jg.com.pubgolf.debugutils.Debug
import jg.com.pubgolf.ui.theme.*
import retrofit2.HttpException


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PubGolfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    val context: Context = LocalContext.current
                    val user: User = loadCredentials(context)
                    if(user.password != "" && user.email != "" && user.name != ""){
                        enterUser(user.name, user.email, user.password, context)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ result ->
                                println("8============D " + result.name)
                                Toast.makeText(context, "Добро пожаловать!", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, GamesActivity::class.java))
                                finish()
                            }, { error ->
                                Toast.makeText(context, "Ошибка: " + error.message, Toast.LENGTH_LONG).show()
                            })
                            .addTo(disposable)

                    }
                    StartScreen()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}


// ЛУЧШЕ НЕ ТРОГАТЬ, ТАМ ВСЕ ПРО ДИЗАЙН
@Composable
fun StartScreen() {
    var showDialogRegister by remember { mutableStateOf(false) }
    var showDialogEnter by remember { mutableStateOf(false) }

    // ЛОГИКА ОТСЛЕЖИВАНИЯ ВКЛЮЧЕНИЯ/ВЫКЛЮЧЕНИЯ ДИАЛОГОВ
    if (showDialogRegister) {
        RegisterDialog(onRegister = { user ->
            enteredUser = user
        }, onDismiss = {showDialogRegister = false}, LocalContext.current)
    }
    else {
        if (showDialogEnter) {
            EnterDialog(onEnter = { user ->
                enteredUser = user
            }, onDismiss = {showDialogEnter = false}, LocalContext.current)
        }
    }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

        // ЛОГО ПАБГОЛЬФА С ТЕКСТОМ
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight(0.5f)) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.ic_cheers),
                    contentDescription = "logo",
                    modifier = Modifier.size(120.dp)
                )
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Pub",
                        color = Purple700,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.gotham_bold)),
                    )
                    Text(
                        text = "Golf",
                        color = Purple200,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.gotham_bold)),
                    )
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ПАБГОЛЬФ ТЕКСТ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Pub",
                color = Purple700,
                fontSize = 60.sp,
                fontFamily = FontFamily(Font(R.font.gotham_bold)),
            )
            Text(
                text = "Golf",
                color = Purple200,
                fontSize = 60.sp,
                fontFamily = FontFamily(Font(R.font.gotham_bold)),
            )
        }
        // НЕ ПОТРЕБЛЯЙ, А ДЕГУСТИРУЙ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Не потребляй, а дегустируй!",
                color = Purple700,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // КНОПКА СОЗДАТЬ АККАУНТ
        OutlinedButton(onClick = {
            showDialogRegister = true
        },
            colors = ButtonDefaults.buttonColors(backgroundColor = Teal200),
            border = BorderStroke(2.dp, Gray),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            // КНОПКА СОЗДАТЬ АККАУНТ
            Text(
                text = "Создать аккаунт",
                color = Purple700,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                textDecoration = TextDecoration.None,
                textAlign = Center
            )
        }

        // КНОПКА ВОЙТИ
        OutlinedButton(
            onClick = {  showDialogEnter = true },
            colors = ButtonDefaults.buttonColors(backgroundColor = Teal200),
            border = BorderStroke(2.dp, Gray),
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            // КНОПКА ВОЙТИ
            Text(
                text = "Войти",
                color = Purple700,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                textDecoration = TextDecoration.None,
                textAlign = Center
            )
        }
        Spacer(modifier = Modifier.height(120.dp))
    }



}

//ДИАЛОГ РЕГИСТРАЦИИ
@Composable
fun RegisterDialog(
    onRegister: (User) -> Unit,
    onDismiss: () -> Unit,
    context: Context
) {

    val dialogState = remember { mutableStateOf(true) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    if (dialogState.value) {
        val registerEnabled: Boolean = name.isNotEmpty() &&
                email.isNotEmpty() &&
                password == repeatPassword &&
                password.length >= 8
        AlertDialog(
            onDismissRequest = { dialogState.value = false; onDismiss() },
            title = {
                Text(
                    "Регистрация",
                    fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = {
                            Text(
                                "Отображаемое имя",
                                fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                            )
                        }
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                "Почта",
                                fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email
                        )
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            if(password == "") {
                                Text(
                                    "Пароль",
                                    fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                                )
                            }
                            else
                            if(password.length < 8) {
                                Text(
                                    "Минимум 8 символов",
                                    fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                                    color = Red500
                                )
                            }
                            else {
                                Text(
                                    "Пароль хороший",
                                    fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                                    color = Green500
                                )
                            }
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        )
                    )
                    OutlinedTextField(
                        value = repeatPassword,
                        onValueChange = { repeatPassword = it },
                        label = {
                            if(repeatPassword == "") {
                                Text(
                                    "Повторите пароль",
                                    fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                                )
                            }
                            else
                            if(repeatPassword != password) {
                                Text(
                                    "Пароли не совпадают",
                                    fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                                    color = Red500
                                )
                            }
                            else {
                                Text(
                                    "Пароли совпадают",
                                    fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                                    color = Green500
                                )
                            }
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        )
                    )
                }
            },

            confirmButton = {
                TextButton(
                    enabled = registerEnabled,
                    onClick = {
                        authUser(::registerUser,
                            User(name, email, password),
                            context,
                            onSuccess = { user ->
                                Toast.makeText(context, context.getString(R.string.welcome),
                                    Toast.LENGTH_LONG).show()
                                context.startActivity(Intent(context, GamesActivity::class.java))
                                (context as Activity).finish()
                            },
                            onError = { error ->
                                Toast.makeText(context, "Ошибка", Toast.LENGTH_LONG).show()
                            })
                    }
                ) {
                    Text(
                        "Зарегистрироваться",
                        fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogState.value = false
                        disposable.dispose()
                        onDismiss()
                    }
                ) {
                    Text(
                        "Отмена",
                        fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                    )
                }
            }
        )
    }
}

// ДИАЛОГ ВХОДА
@Composable
fun EnterDialog(
    onEnter: (User) -> Unit,
    onDismiss: () -> Unit,
    context: Context
) {

    val dialogState = remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (dialogState.value) {
        val registerEnabled: Boolean = email.isNotEmpty() && password.isNotEmpty()
        AlertDialog(
            onDismissRequest = { dialogState.value = false; onDismiss() },
            title = {
                Text(
                    "Вход в аккаунт",
                    fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                "Почта",
                                fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email
                        )
                    )
                    // ПАРОЛЬ
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                "Пароль",
                                fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium)),
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        )
                    )
                }
            },
            // КНОПКА ВХОДА
            confirmButton = {
                TextButton(
                    enabled = registerEnabled,
                    onClick = {
                        authUser(::enterUser,
                            User("", email, password),
                            context,
                            onSuccess = { user ->
                                Toast.makeText(context, context.getString(R.string.welcome),
                                Toast.LENGTH_LONG).show()
                                context.startActivity(Intent(context, GamesActivity::class.java))
                                (context as Activity).finish()
                            },
                            onError = { error ->
                                Toast.makeText(context, "Ошибка", Toast.LENGTH_LONG).show()
                            })
                    }
                ) {
                    Text(
                        "Войти",
                        fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                    )
                }
            },
            // КНОПКА ОТМЕНЫ
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogState.value = false
                        disposable.dispose()
                        onDismiss()
                    }
                ) {
                    Text(
                        "Отмена",
                        fontFamily = FontFamily(Font(R.font.gothamxnarrow_medium))
                    )
                }
            }
        )
    }
}


// регистрация пользователя
// опробовать оптимизировать в будущем, но хз как
// не нравится повторяющаяся часть кода с обзерверами что здесь, что в enterUser
fun registerUser(name: String, email: String, password: String, context: Context): Single<UserResponse> {

    val user = User(name, email, password)
    return apiService.registerUser(user)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext { throwable: Throwable ->
            showToastErrors(context, context.getString(R.string.emailExistsError), throwable)
            Single.error(throwable)
        }
}


// вход в приложение
fun enterUser(name: String, email: String, password: String, context: Context): Single<UserResponse> {
    val user = User(name ,email, password)
    return apiService.enterUser(user)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext { throwable: Throwable ->
            showToastErrors(context, context.getString(R.string.loginAndPasswordError), throwable)
            Single.error(throwable)
        }
}


// сохранение sharedPreferences
fun saveCredentials(name: String, email:String, password:String, context: Context) {
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        prefs.edit {
            putString(LOGIN_KEY, email)
            putString(PASSWORD_KEY, password)
            putString(NAME_KEY, password)
        }
}


// выгрузка данных из sharedPreferences
fun loadCredentials(context: Context): User {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val email = prefs.getString(LOGIN_KEY, "") ?: ""
    val password = prefs.getString(PASSWORD_KEY, "") ?: ""
    val name = prefs.getString(NAME_KEY, "") ?: ""
    return User(name, email, password)
}


// функция для вывода ошибок в тосте
fun showToastErrors(context: Context, errorString: String, throwable: Throwable) {
    val errorMessage: String = if (throwable is HttpException && throwable.code() == HttpStatus.SC_BAD_REQUEST) {
        errorString
    } else {
        if(Debug.state) {
            "Ошибка: ${throwable.message}"
        } else {
            context.getString(R.string.retryLaterError)
        }
    }
    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
}



/**
 * Смэрть
 *
 * авторизация пользователей
 * вот это есть смысл расписать
 * @param authFunc - функция enterUser или registerUser
 * @param user - пользователь, данные которого передаются в authFunc
 *
 */
fun authUser(
    authFunc: (String, String, String, Context) -> Single<UserResponse>,
    user: User,
    context: Context,
    onSuccess: (UserResponse) -> Unit,
    onError: (Throwable) -> Unit
): Disposable {

    authFunc(user.name, user.email, user.password, context)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ result ->
            saveCredentials(result.name, user.email, user.password, context)
            onSuccess(result)
        }, { error ->
            onError(error)
        })
        .addTo(disposable)

    return disposable
}