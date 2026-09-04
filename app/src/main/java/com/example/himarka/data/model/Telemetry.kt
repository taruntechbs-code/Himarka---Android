package com.example.himarka.data.model

data class Telemetry(
    val deviceId: String = "HIMARKA-NER-01",
    val timestamp: Long = System.currentTimeMillis(),
    val temperatureC: Float = 2.6f,
    val humidityPercent: Float = 89.0f,
    val gasPpm: Float = 145.0f,
    val solarVoltageV: Float = 19.4f,
    val solarGenerationW: Float = 50.0f,
    val batteryVoltageV: Float = 26.8f,
    val batteryPercent: Int = 94,
    val coolingPowerW: Float = 38.5f,
    val isCoolingActive: Boolean = true,
    val isDoorOpen: Boolean = false,
    val isDemoMode: Boolean = true,
    val isDeviceOnline: Boolean = true
)
