package com.example.coflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.coflow.ui.theme.CoFlowGlassStyles

@Composable
fun CoFlowGlassCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CoFlowGlassStyles.CardShape,
    content: @Composable ColumnScope.() -> Unit // <-- Изменено на ColumnScope
) {
    // ИСПРАВЛЕНИЕ: Используем Column вместо Box, чтобы элементы выстраивались
    // вертикально друг за другом, а не накладывались слоями друг на друга.
    Column(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = shape, ambientColor = CoFlowGlassStyles.Shadow, spotColor = CoFlowGlassStyles.Shadow)
            .clip(shape)
            .background(CoFlowGlassStyles.Surface)
            .border(1.dp, CoFlowGlassStyles.BlueBorder, shape)
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        content()
    }
}

@Composable
fun CoFlowGlassControl(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(13.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        androidx.compose.ui.graphics.Color.White.copy(alpha = 0.30f),
                        androidx.compose.ui.graphics.Color(0xFFDDEAFF).copy(alpha = 0.16f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        androidx.compose.ui.graphics.Color.White.copy(alpha = 0.92f),
                        com.example.coflow.ui.theme.CoFlowColors.Blue.copy(alpha = 0.14f)
                    )
                ),
                shape = shape
            )
            .padding(horizontal = 9.dp, vertical = 7.dp)
    ) {
        content()
    }
}