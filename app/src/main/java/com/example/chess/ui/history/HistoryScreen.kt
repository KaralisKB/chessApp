package com.example.chess.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chess.ui.components.GameHistoryCard
import com.example.chess.ui.navigation.Screen

@Composable
fun HistoryScreen(
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color(0xFFF2EFE7))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp, bottom = 10.dp, start = 5.dp, end = 5.dp)
                .background(Color(0xFFF2EFE7))

        ) {
            Text(
                text = "History",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF006A71)
            )
            Button(onClick = { navController.navigate(Screen.MainMenu) }) {
                Text("Main Menu")
            }
        }

        LazyColumn(
            modifier = Modifier.background(Color(0xFFF2EFE7))
        ) {
            item {
                GameHistoryCard(null, null, null, null, null, null, null)
            }
            item {
                GameHistoryCard(null, null, null, null, null, null, null)
            }
            item {
                GameHistoryCard(null, null, null, null, null, null, null)
            }
            item {
                GameHistoryCard(null, null, null, null, null, null, null)
            }
            item {
                GameHistoryCard(null, null, null, null, null, null, null)
            }
            item {
                GameHistoryCard(null, null, null, null, null, null, null)
            }
            item {
                GameHistoryCard(null, null, null, null, null, null, null)
            }
        }
    }
}