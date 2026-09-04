package com.example.himarka.data.model

import com.example.himarka.R

enum class StoragePreset(
    val id: Int,
    val titleResId: Int,
    val rangeResId: Int,
    val descResId: Int,
    val targetTempMin: Float,
    val targetTempMax: Float
) {
    MODE_1(
        id = 1,
        titleResId = R.string.mode_1_title,
        rangeResId = R.string.mode_1_range,
        descResId = R.string.mode_1_desc,
        targetTempMin = 0f,
        targetTempMax = 2f
    ),
    MODE_2(
        id = 2,
        titleResId = R.string.mode_2_title,
        rangeResId = R.string.mode_2_range,
        descResId = R.string.mode_2_desc,
        targetTempMin = 5f,
        targetTempMax = 7f
    ),
    MODE_3(
        id = 3,
        titleResId = R.string.mode_3_title,
        rangeResId = R.string.mode_3_range,
        descResId = R.string.mode_3_desc,
        targetTempMin = 10f,
        targetTempMax = 13f
    );

    fun formatTempRange(): String = "${targetTempMin.toInt()}°C – ${targetTempMax.toInt()}°C"
}
