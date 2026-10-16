package com.example.coflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.coflow.ui.theme.CoFlowTheme
import com.example.coflow.ui.auth.AuthRoute
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.coflow.ui.home.HomeScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CoFlowTheme {
                // TODO: после успешного входа перейти на главный экран
                // изменила у Лизы, добавим сразу переход на главный экран ддля проверки

                // Новое: состояние для переключения между авторизацией и главным экраном.
                // Для обычного запуска оставляем авторизацию первым экраном.
                var isAuthenticated by remember {
                    mutableStateOf(true)
                }

                if (isAuthenticated) {
                    // Новое: показываем главный экран после успешного входа.
                    HomeScreen()
                } else {
                    // Существующий экран авторизации не изменяем.
                    AuthRoute(
                        onAuthSuccess = {
                            // Новое: переключаемся на главный экран.
                            isAuthenticated = true
                        }
                    )
                }
            }
        }
    }

}
