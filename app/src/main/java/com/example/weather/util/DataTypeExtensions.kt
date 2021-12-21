package com.example.weather.util

fun Double.speed() =
    this.specialString("m/s")

fun Double.celsius(): String =
    this.toInt().
    specialString("℃")

fun Int.percent() =
    this.specialString("%")

fun Int.hpaInHg() =
    (this * 0.75).toInt()
        .specialString("mm/hg")

fun <P : Number> P.specialString(units: String): String =
    this.let {
        "$this $units"
    }
