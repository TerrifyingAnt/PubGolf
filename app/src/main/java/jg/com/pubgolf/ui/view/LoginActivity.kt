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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import jg.com.pubgolf.data.api.ApiHelper
import jg.com.pubgolf.data.api.RetrofitBuilder
import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.ui.theme.*
import jg.com.pubgolf.viewModel.AuthViewModel
import jg.com.pubgolf.viewModel.ViewModelFactory
import jg.com.pubgolf.viewModel.state.AuthState


class LoginActivity : ComponentActivity() {

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
                    LoginScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: AuthViewModel) {

    // хранение логина
    val loginText = remember{mutableStateOf("")}

    // хранение пароля
    val passwordText = remember{mutableStateOf("")}

    // хранение контекста
    val context = LocalContext.current

    val activity = LocalContext.current as Activity

    // состояние ответа в вьюмоделе
    val state by viewModel.authState.collectAsState(initial = AuthState.Loading)

    // состояние пароля
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Запуск активити, если пришел норм результат
    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        if (state is AuthState.Error) {
            Toast.makeText(context, "Проверьте логин и пароль", Toast.LENGTH_LONG).show()
        }
    }

    // TODO Доделать картинку пабгольфа
    // TODO Просто дизайн прикольнее сделай пжлст
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Добро пожаловть!")
        }
        Spacer(modifier = Modifier.height(5.dp))
        // Поле ввода логина
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = loginText.value,
                onValueChange = { login -> loginText.value = login },
                placeholder = {Text("Введите логин")})
        }
        Spacer(modifier = Modifier.height(5.dp))

        // Поле ввода пароля
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = passwordText.value,
                onValueChange = { password -> passwordText.value = password },
                singleLine = true,
                placeholder = {Text("Введите пароль")},
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff


                    val description = if (passwordVisible) "Скрыть пароль" else "показать пароль"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                })
        }

        // Кнопка входа
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                if (loginText.value != "" && passwordText.value != "") {
                    val authRequest = AuthRequest(loginText.value, passwordText.value)
                    viewModel.authenticateUser(authRequest)
                }
            }) {
                Text("Войти")
            }
        }

        // Кнопка регистрации
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                activity.startActivity(Intent(context, RegistrationActivity::class.java))
            }) {
                Text("Зарегистрироваться")
            }
        }
    }
}





