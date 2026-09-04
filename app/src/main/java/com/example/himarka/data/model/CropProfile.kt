package com.example.himarka.data.model

data class CropProfile(
    val id: String,
    val name: String,
    val scientificReferenceTemp: String,
    val recommendedPreset: StoragePreset,
    val isVerified: Boolean = true,
    val category: String = "Vegetable"
)

object CropCatalog {
    val allCrops: List<CropProfile> = listOf(
        CropProfile("tomato", "Tomato", "13–15°C", StoragePreset.MODE_3),
        CropProfile("cabbage", "Cabbage", "0–1°C", StoragePreset.MODE_1),
        CropProfile("cauliflower", "Cauliflower", "0–1°C", StoragePreset.MODE_1),
        CropProfile("broccoli", "Broccoli", "0–1°C", StoragePreset.MODE_1),
        CropProfile("carrot", "Carrot", "0–1°C", StoragePreset.MODE_1),
        CropProfile("radish", "Radish", "0–1°C", StoragePreset.MODE_1),
        CropProfile("beetroot", "Beetroot", "0°C", StoragePreset.MODE_1),
        CropProfile("green_peas", "Green Peas", "0–1°C", StoragePreset.MODE_1),
        CropProfile("french_beans", "French Beans", "4–7°C", StoragePreset.MODE_2),
        CropProfile("capsicum", "Capsicum", "7–10°C", StoragePreset.MODE_3),
        CropProfile("green_chilli", "Green Chilli", "0–5°C", StoragePreset.MODE_1),
        CropProfile("brinjal", "Brinjal", "10–12°C", StoragePreset.MODE_3),
        CropProfile("okra", "Okra", "7–10°C", StoragePreset.MODE_3),
        CropProfile("cucumber", "Cucumber", "10–13°C", StoragePreset.MODE_3),
        CropProfile("spinach", "Spinach", "0°C", StoragePreset.MODE_1),
        CropProfile("lettuce", "Lettuce", "0°C", StoragePreset.MODE_1),
        CropProfile("onion_dry", "Onion (Dry)", "0–2°C", StoragePreset.MODE_1),
        CropProfile("garlic", "Garlic", "Unverified", StoragePreset.MODE_1, isVerified = false),
        CropProfile("potato", "Potato", "4–7°C", StoragePreset.MODE_2),
        CropProfile("ginger", "Ginger", "13–15°C", StoragePreset.MODE_3),
        CropProfile("turmeric", "Turmeric", "13–15°C", StoragePreset.MODE_3),
        CropProfile("mustard_greens", "Mustard Greens", "0–1°C", StoragePreset.MODE_1),
        CropProfile("celery", "Celery", "0–1°C", StoragePreset.MODE_1),
        CropProfile("spring_onion", "Spring Onion", "0–1°C", StoragePreset.MODE_1)
    )
}
