package com.example.coflow.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowShapes

@Composable
fun CoFlowPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = CoFlowShapes.Button,
    height: Dp = 52.dp,
    fontSize: TextUnit = 15.sp,
    containerColor: Color = CoFlowColors.Blue,
    contentColor: Color = Color.White,
    disabledContainerColor: Color = CoFlowColors.Blue.copy(alpha = 0.45f),
    disabledContentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(height),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(text = text, fontSize = fontSize, fontWeight = FontWeight.SemiBold)
    }
}