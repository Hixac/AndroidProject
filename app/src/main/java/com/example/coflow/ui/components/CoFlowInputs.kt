package com.example.coflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowGlassStyles
import com.example.coflow.ui.theme.CoFlowShapes

@Composable
fun CoFlowGlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null, // Если null, рендерится как поисковая строка
    placeholder: String = "",
    leadingIcon: String? = null,
    showTrailingArrow: Boolean = false,
    onClick: (() -> Unit)? = null, // Для полей-селекторов (дата, проект)
    singleLine: Boolean = true,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = if (singleLine) ImeAction.Next else ImeAction.Default
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val shape = CoFlowShapes.Input
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CoFlowColors.TextPrimary
            )
        }

        val fieldModifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = shape, ambientColor = CoFlowGlassStyles.Shadow, spotColor = CoFlowGlassStyles.Shadow)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(alpha = 0.72f), Color(0xFFF5F8FF).copy(alpha = 0.58f))
                )
            )
            .border(width = 1.dp, color = CoFlowGlassStyles.BlueBorder, shape = shape)

        BasicTextField(
            value = value,
            onValueChange = { onValueChange(if (maxLength != null) it.take(maxLength) else it) },
            modifier = fieldModifier,
            textStyle = TextStyle(color = CoFlowColors.TextPrimary, fontSize = 14.sp, lineHeight = 20.sp),
            cursorBrush = SolidColor(CoFlowColors.Blue),
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = if (!singleLine && keyboardOptions.imeAction == ImeAction.Next) {
                KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
            } else {
                keyboardActions
            },
            decorationBox = { innerTextField ->
                if (!singleLine) {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp)) {
                        Box(modifier = Modifier.fillMaxWidth().heightIn(min = 88.dp), contentAlignment = Alignment.TopStart) {
                            if (value.isEmpty()) Text(text = placeholder, color = CoFlowColors.TextSecondary, fontSize = 14.sp, lineHeight = 20.sp)
                            innerTextField()
                        }
                        if (maxLength != null) {
                            Text(
                                text = "${value.length}/$maxLength",
                                modifier = Modifier.align(Alignment.End).padding(top = 6.dp),
                                fontSize = 11.sp, color = CoFlowColors.TextSecondary
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (leadingIcon != null) {
                            CoFlowIcon(name = leadingIcon, modifier = Modifier.size(21.dp), color = CoFlowColors.TextPrimary)
                            Spacer(Modifier.width(10.dp))
                        }

                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                            if (value.isEmpty()) Text(text = placeholder, color = CoFlowColors.TextSecondary, fontSize = 14.sp)
                            innerTextField()
                        }

                        if (showTrailingArrow) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(Color.White.copy(alpha = 0.65f), CircleShape)
                                    .border(1.dp, CoFlowGlassStyles.BlueBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                CoFlowIcon(name = "chevron", modifier = Modifier.size(14.dp), color = CoFlowColors.Blue)
                            }
                        }
                    }
                }
            }
        )
    }
}