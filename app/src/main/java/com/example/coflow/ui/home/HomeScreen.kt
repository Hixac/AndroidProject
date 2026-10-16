
package com.example.coflow.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF245BDB)
private val PageBackground = Color(0xFFF7F8FA)
private val SecondaryText = Color(0xFF667085)
private val CardBorder = Color(0xFFEAECF0)

@Composable
fun HomeScreen(
    onNotificationsClick: () -> Unit = {},
    onCreateTaskClick: () -> Unit = {},
    onTasksClick: () -> Unit = {},
    onProjectsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    MaterialTheme {
        Scaffold(
            containerColor = PageBackground,
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    val labels = listOf(
                        "Главная", "Мои задачи", "Проекты", "Профиль"
                    )
                    val symbols = listOf("⌂", "☑", "▱", "♙")

                    labels.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                                when (index) {
                                    1 -> onTasksClick()
                                    2 -> onProjectsClick()
                                    3 -> onProfileClick()
                                }
                            },
                            icon = {
                                Text(
                                    text = symbols[index],
                                    fontSize = 20.sp
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                // Приветствие и переход к уведомлениям
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Пятница, 16 октября",
                            fontSize = 11.sp,
                            color = SecondaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Привет, Алексей!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF101828)
                        )
                    }

                    Surface(
                        onClick = onNotificationsClick,
                        shape = CircleShape,
                        color = Color(0xFFEAF1FF),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("♧", color = Blue, fontSize = 20.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Статистика: подписи оставлены, цифры временно скрыты
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "ТВОЯ НЕДЕЛЯ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SecondaryText
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            StatItem(
                                label = "На сегодня",
                                modifier = Modifier.weight(1f)
                            )
                            StatItem(
                                label = "Просрочено",
                                modifier = Modifier.weight(1f)
                            )
                            StatItem(
                                label = "Выполнено",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Заголовок раздела задач
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "На сегодня",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF101828)
                    )
                    Text(
                        "Все задачи →",
                        fontSize = 11.sp,
                        color = Blue
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Карточки задач без названий и сроков
                TaskCard(urgent = true)

                Spacer(modifier = Modifier.height(6.dp))

                TaskCard()

                Spacer(modifier = Modifier.height(16.dp))

                // Последние события без текстовых описаний
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Последние события",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF101828)
                    )
                    Text(
                        "→",
                        fontSize = 17.sp,
                        color = Blue
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        ActivityItem(symbol = "♙")

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = CardBorder
                        )

                        ActivityItem(symbol = "◯")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Кнопка создания задачи
                Button(
                    onClick = onCreateTaskClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                ) {
                    Text(
                        "+  Создать задачу",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        // Место для будущего динамического значения статистики
        Spacer(modifier = Modifier.height(25.dp))

        Text(
            label,
            fontSize = 9.sp,
            color = SecondaryText,
            maxLines = 1
        )
    }
}

@Composable
private fun TaskCard(
    urgent: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Индикатор выполнения задачи
            Text(
                "○",
                color = Color(0xFF98A2B3),
                fontSize = 19.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Единственная временная подпись — срочность
            if (urgent) {
                Surface(
                    color = Color(0xFFFEE4E2),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Срочно",
                        modifier = Modifier.padding(
                            horizontal = 6.dp,
                            vertical = 4.dp
                        ),
                        fontSize = 9.sp,
                        color = Color(0xFFB42318)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityItem(
    symbol: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(30.dp),
            shape = CircleShape,
            color = Color(0xFFEAF1FF)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    symbol,
                    fontSize = 15.sp,
                    color = Blue
                )
            }
        }
    }
}
