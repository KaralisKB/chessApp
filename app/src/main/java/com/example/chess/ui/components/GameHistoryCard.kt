package com.example.chess.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.dp

@Composable
fun GameHistoryCard(

) {
    Box(
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(120.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF48A6A7)),
            border = BorderStroke(4.dp, Color(0xFF9ACBD0))
        ) {

        }
    }


}