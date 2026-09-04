package com.example.himarka.core.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.himarka.core.theme.HimarkaShapes
import com.example.himarka.core.theme.HimarkaStatusCritical
import com.example.himarka.core.theme.HimarkaStatusDemo
import com.example.himarka.core.theme.HimarkaStatusOptimal
import com.example.himarka.core.theme.HimarkaStatusWarning

enum class StatusLevel {
    OPTIMAL,
    WARNING,
    CRITICAL,
    DEMO,
    NEUTRAL
}

@Composable
fun StatusChip(
    text: String,
    level: StatusLevel,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, dotColor) = when (level) {
        StatusLevel.OPTIMAL -> Triple(HimarkaStatusOptimal.copy(alpha = 0.12f), HimarkaStatusOptimal, HimarkaStatusOptimal)
        StatusLevel.WARNING -> Triple(HimarkaStatusWarning.copy(alpha = 0.12f), HimarkaStatusWarning, HimarkaStatusWarning)
        StatusLevel.CRITICAL -> Triple(HimarkaStatusCritical.copy(alpha = 0.12f), HimarkaStatusCritical, HimarkaStatusCritical)
        StatusLevel.DEMO -> Triple(HimarkaStatusDemo.copy(alpha = 0.12f), HimarkaStatusDemo, HimarkaStatusDemo)
        StatusLevel.NEUTRAL -> Triple(Color.Gray.copy(alpha = 0.12f), Color.DarkGray, Color.Gray)
    }

    Row(
        modifier = modifier
            .clip(HimarkaShapes.small)
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
