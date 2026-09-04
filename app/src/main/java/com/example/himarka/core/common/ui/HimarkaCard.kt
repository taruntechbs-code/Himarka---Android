package com.example.himarka.core.common.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.himarka.core.theme.HimarkaCardBackground
import com.example.himarka.core.theme.HimarkaCardBorder
import com.example.himarka.core.theme.HimarkaShapes

@Composable
fun HimarkaCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = HimarkaCardBackground,
    borderColor: Color = HimarkaCardBorder,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp,
    shape: Shape = HimarkaShapes.medium,
    contentPadding: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val animatedElevation by animateDpAsState(
        targetValue = elevation,
        animationSpec = tween(durationMillis = 300),
        label = "card_elevation"
    )

    Card(
        modifier = modifier.shadow(
            elevation = animatedElevation,
            shape = shape,
            clip = false,
            ambientColor = Color(0x1F7C3AED),
            spotColor = Color(0x297C3AED)
        ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(borderWidth, borderColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp // Shadow applied via modifier for custom tint
        )
    ) {
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            content()
        }
    }
}
