package com.example.himarka.domain.usecase

import com.example.himarka.R
import com.example.himarka.core.common.ui.StatusLevel
import com.example.himarka.data.model.StoragePreset
import com.example.himarka.data.model.Telemetry
import org.junit.Assert.assertEquals
import org.junit.Test

class EvaluateStorageHealthUseCaseTest {

    private val useCase = EvaluateStorageHealthUseCase()

    @Test
    fun `when temperature is within preset range returns OPTIMAL`() {
        val telemetry = Telemetry(temperatureC = 1.0f)
        val result = useCase(telemetry, StoragePreset.MODE_1)

        assertEquals(StatusLevel.OPTIMAL, result.level)
        assertEquals(R.string.health_optimal, result.statusMsgResId)
    }

    @Test
    fun `when temperature exceeds preset range returns WARNING or CRITICAL`() {
        val telemetry = Telemetry(temperatureC = 8.0f)
        val result = useCase(telemetry, StoragePreset.MODE_1)

        assertEquals(StatusLevel.CRITICAL, result.level)
        assertEquals(R.string.health_above_target, result.statusMsgResId)
    }

    @Test
    fun `when device is offline returns CRITICAL`() {
        val telemetry = Telemetry(isDeviceOnline = false)
        val result = useCase(telemetry, StoragePreset.MODE_1)

        assertEquals(StatusLevel.CRITICAL, result.level)
        assertEquals(R.string.health_offline, result.statusMsgResId)
    }
}
