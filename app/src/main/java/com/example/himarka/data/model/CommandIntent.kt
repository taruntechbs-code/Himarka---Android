package com.example.himarka.data.model

sealed class CommandIntent {
    data class SetStoragePresetCommand(
        val targetPreset: StoragePreset,
        val timestamp: Long = System.currentTimeMillis()
    ) : CommandIntent()
}

enum class CommandStatus {
    SUBMITTED,
    VALIDATED,
    EXECUTED,
    REJECTED_SAFETY_LIMIT
}

data class CommandResult(
    val command: CommandIntent,
    val status: CommandStatus,
    val message: String
)
