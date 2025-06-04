package com.example.chess.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.haze

@Composable
fun HistoryScreen(
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFFF2EFE7))
            .padding(top = 20.dp)
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
            IconButton(
                onClick = { navController.navigate(Screen.MainMenu) },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF006A71),
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.background(Color(0xFFF2EFE7))
        ) {
            items(7) {
                GameHistoryCard(null, null, null, null, null, null, null)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryScreenPreview() {
    val navController = rememberNavController()
    HistoryScreen(navController = navController)
}