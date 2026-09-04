package com.example.himarka.core.localization

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String
) {
    ENGLISH("en", "English", "English"),
    ASSAMESE("as", "Assamese", "অসমীয়া"),
    BENGALI("bn", "Bengali", "বাংলা"),
    HINDI("hi", "Hindi", "हिन्दी"),
    NEPALI("ne", "Nepali", "नेपाली"),
    MANIPURI("mni", "Manipuri", "মৈতৈলোন্"),
    BODO("brx", "Bodo", "বর'"),
    MIZO("lus", "Mizo", "Mizo ṭawng"),
    KHASI("kha", "Khasi", "Ka Ktien Khasi");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
