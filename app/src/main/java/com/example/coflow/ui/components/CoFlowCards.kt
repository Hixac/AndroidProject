package com.example.coflow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowGlassStyles
import com.example.coflow.ui.theme.CoFlowShapes

// 1. Универсальная стеклянная карточка (для списков, групп задач)
@Composable
fun CoFlowGlassCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CoFlowGlassStyles.CardShape,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Column(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = shape, ambientColor = CoFlowGlassStyles.Shadow, spotColor = CoFlowGlassStyles.Shadow)
            .clip(shape)
            .background(CoFlowGlassStyles.Surface)
            .border(1.dp, CoFlowGlassStyles.BlueBorder, shape)
            .padding(horizontal = 12.dp, vertical = 9.dp)
            .then(clickModifier)
    ) {
        content()
    }
}

// 2. Карточка авторизации (специфичный полупрозрачный фон)
@Composable
fun CoFlowAuthCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = CoFlowShapes.Card,
        color = CoFlowColors.CardBackground,
        border = BorderStroke(1.dp, CoFlowColors.BlueBorder.copy(alpha = 0.75f)),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp)) {
            content()
        }
    }
}