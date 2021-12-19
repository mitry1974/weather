package com.example.weather.util

//fun Double?.celsiusString(): String =
//    this?.let {
//        "${this.toInt()}\u2103"
//    } ?: ""

//fun Int?.hpaInHgInt(): String =
//    this?.let {
//        "${(this / 10 * 7.5).toInt()}mmhg"
//    } ?: ""

// fun Double?.speedString(): String = specialString("m/s")

//fun Int?.percentString(): String =
//    this?.let {
//        "$this %"
//    } ?: ""

fun <P: Double> P.celsius(): String =
    this.toInt().
    specialString("℃")

fun <P: Int> P.hpaInHg() =
    (this * 0.75).toInt()
    .specialString("mm/hg")

fun <P: Double> P.speed() =
    this.specialString("m/s")

fun <P: Int> P.percent() =
    this.specialString("%")

fun <P : Number> P.specialString(units: String): String =
    this.let {
        "$this $units"
    }
