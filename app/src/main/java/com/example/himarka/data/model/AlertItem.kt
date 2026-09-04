package com.example.himarka.data.model

enum class AlertSeverity {
    INFO,
    WARNING,
    CRITICAL
}

data class AlertItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val severity: AlertSeverity,
    val isResolved: Boolean = false,
    val isAcknowledged: Boolean = false
)
