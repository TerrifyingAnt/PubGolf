package jg.com.pubgolf.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.api.RetrofitBuilder
import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.data.model.RegisterationModels.RegistrationRequest
import jg.com.pubgolf.ui.theme.PubGolfTheme
import jg.com.pubgolf.viewModel.AuthViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory
import jg.com.pubgolf.viewModel.state.AuthState
import jg.com.pubgolf.viewModel.state.RegistrationState

class RegistrationActivity: ComponentActivity() {

    // viewModel авторизации
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(
            ApiHelper(
                RetrofitBuilder.apiService,
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PubGolfTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    RegistrationScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun RegistrationScreen(viewModel: AuthViewModel) {


    // хранение почты
    val emailText = remember{ mutableStateOf("") }

    // хранение пароля
    val passwordText = remember{ mutableStateOf("") }

    // хранение повторно введенного пароля
    val rePasswordText = remember{ mutableStateOf("") }

    // хранение имени
    val usernameText = remember{ mutableStateOf("") }


    // хранение телефона
    val phoneText = remember{ mutableStateOf("") }

    // хранение контекста
    val context = LocalContext.current

    val activity = LocalContext.current as Activity

    // состояние ответа в вьюмоделе
    val state by viewModel.registrationState.collectAsState(initial = RegistrationState.Loading)

    // Запуск активити, если пришел норм результат
    LaunchedEffect(state) {
        if (state is RegistrationState.Success) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        if (state is RegistrationState.Error) {
            Toast.makeText(context, (state as RegistrationState.Error).response, Toast.LENGTH_LONG).show()
        }
    }


    /*
    TODO сделай проверку на количество символов, правильность введенного телефона
        там в доке расписано как это все происходит
     */
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Добро пожаловть!")
        }
        Spacer(modifier = Modifier.height(5.dp))

        // Поле ввода данных пользователя
        defaultText(text = usernameText, placeholder = "Никнейм")
        defaultText(text = emailText, placeholder = "Почта")
        defaultText(text = phoneText, placeholder = "Телефон")


        // Поле ввода пароля
        hidedText(text = passwordText, placeholder = "Пароль")

        // Повторное поле ввода пароля
        hidedText(text = rePasswordText, placeholder = "Повторите пароль")

        // Кнопка входа
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                if (emailText.value != "" &&
                    usernameText.value != "" &&
                    passwordText.value != "" &&
                        rePasswordText.value == passwordText.value &&
                        passwordText.value.length > 7 &&
                        phoneText.value != "") {
                    val registrationRequest = RegistrationRequest(usernameText.value,
                        emailText.value,
                        passwordText.value,
                        "player",
                        "qwer",
                        "qwer",
                        phoneText.value,
                        rePasswordText.value
                    )
                    viewModel.registerUser(registrationRequest)
                }
            }) {
                Text("Зарегистрироваться")
            }
        }

        // Кнопка регистрации
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                activity.startActivity(Intent(context, LoginActivity::class.java))
                activity.finish()
            }) {
                Text("У меня уже есть аккаунт")
            }
        }
    }
}

@Composable
fun defaultText(text: MutableState<String>, placeholder: String) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text.value,
            onValueChange = { changedText -> text.value = changedText },
            placeholder = { Text(placeholder) })
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun hidedText(text: MutableState<String>, placeholder: String) {
    // состояние текст
    var textVisible by rememberSaveable { mutableStateOf(false) }
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text.value,
            onValueChange = { changedText -> text.value = changedText },
            singleLine = true,
            placeholder = { Text(placeholder) },
            visualTransformation = if (textVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (textVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff


                val description = if (textVisible) "Скрыть" else "Показать"

                IconButton(onClick = {textVisible = !textVisible}){
                    Icon(imageVector  = image, description)
                }
            })
    }
    Spacer(modifier = Modifier.height(5.dp))
}

