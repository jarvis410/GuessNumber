package com.pm.guessnumber

enum class Level(val guesses: Int) {
    Easy(6), Medium(5), Hard(3)
}

const val LEVEL = "Level"
const val RESULT = "Result"
const val THEME = "Theme"

const val THEME_LIGHT = 0
const val THEME_DARK = 1