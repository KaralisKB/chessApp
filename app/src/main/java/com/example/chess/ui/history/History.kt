package com.example.chess.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import java.lang.reflect.Modifier

@Composable
fun HistoryScreen(
    navController: NavController
) {
    Column() {
        Text(text = "History")
    }
}