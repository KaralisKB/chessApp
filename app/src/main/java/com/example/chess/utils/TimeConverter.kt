package com.example.chess.utils

import java.time.Instant
import java.time.ZoneId

 fun convertLongToDateTime(timeMillis: Long?, format: String?): String? {

    val instant = timeMillis?.let { Instant.ofEpochMilli(it) }

    val localDateTime = instant?.atZone(ZoneId.systemDefault())?.toLocalDateTime()

    val formatter = if (format == "short") {
        java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
    } else {
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
    }

    return localDateTime?.format(formatter)
}