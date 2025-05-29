package com.example.chess.ui.menu

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chess.R
import com.example.chess.local.model.GameType
import com.example.chess.ui.components.ChessLottie
import com.example.chess.ui.components.ExpandableStartButton
import com.example.chess.ui.navigation.Screen
import com.example.chess.ui.theme.Jade
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun MainMenuScreen(
    navController: NavController,
    navigateToGame: (String, String, GameType, Long) -> Unit = { _, _, _, _ -> },
    viewModel: MenuViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF9ACBD0), Color(0xFFF2EFE7)),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ChessLottie(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(250.dp),
                id = R.raw.chess_knight,
                speed = 1f
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).offset(y = (-30).dp),
                text = stringResource(R.string.app_title),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0xFF006A71),
                        blurRadius = 10f
                    )
                )
            )

            val scope = rememberCoroutineScope()

            ExpandableStartButton { gameType, whiteName, blackName ->
                scope.launch {
                    val gameId = viewModel.createGame(whiteName, blackName, gameType)
                    navigateToGame(whiteName, blackName, gameType, gameId)
                }
            }

            Button(
                onClick = { navController.navigate(Screen.GameHistory) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF48A6A7),
                    contentColor = Color(0xFFF2EFE7)
                ),
                border = BorderStroke(1.dp, Color(0xFF006A71))
                ) {
                Text("Game History")
            }
        }
    }
}
