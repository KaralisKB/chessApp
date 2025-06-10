package com.example.chess.utils

import android.annotation.SuppressLint
import com.example.chess.ui.board.GameTimer.startTimeMillis
import java.text.SimpleDateFormat
import java.time.LocalDate

import java.util.Date

@SuppressLint("DefaultLocale", "SimpleDateFormat")
 fun convertLongToDateTime(timeMillis: Long?, short: Boolean): String? {
    val seconds = (timeMillis!! % 60000) / 1000
    val minutes = (timeMillis / 60000)


     if(short) {
         return String.format("%02d:%02d", minutes, seconds)
     } else {
         val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
         val dateString = simpleDateFormat.format(timeMillis)
         return String.format("%s", dateString)
     }

}
