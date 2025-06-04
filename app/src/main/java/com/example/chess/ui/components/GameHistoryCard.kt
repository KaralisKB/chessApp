package com.example.chess.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.chess.R
import com.example.chess.local.model.GameType
import com.example.chess.ui.theme.blurEffect
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import java.util.Date

@Composable
fun GameHistoryCard(
    player1name: String?,
    player2name: String?,
    winner: String?,
    player1time: Long?,
    player2time: Long?,
    gameType: GameType?,
    date: Long?
) {


    val hazeState = remember { HazeState() }
    Box(
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .hazeChild(hazeState, blurEffect(Color(0xFFF2EFE7))),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(4.dp, Color(0xFF9ACBD0))
        ) {
            //Top row with date of match
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "12-04-2022",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 5.dp),
                    color = Color(0xFF9ACBD0)
                )
            }

            // Middle bigger row with three columns, player, game type, player
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row() {
                        ChessLottie(modifier = Modifier.size(50.dp), R.raw.crown, 1f)
                    }

                    Row() {
                        Text(
                            text = "Player 1",
                            color = Color(0xFFF2EFE7)
                        )
                    }

                    Row() {
                        Text(
                            text = "01:33",
                            color = Color(0xFFF2EFE7)
                        )
                    }

                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row() {

                        Text(
                            text = "Type\n5s",
                            color = Color(0xFFF2EFE7),
                            textAlign = TextAlign.Center
                        )

                    }

                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row() {
                        Box(modifier = Modifier.size(50.dp)) {}
                    }

                    Row() {
                        Text(
                            text = "Player 2",
                            color = Color(0xFFF2EFE7)
                        )
                    }

                    Row() {
                        Text(
                            text = "00:00",
                            color = Color(0xFFF2EFE7)
                        )
                    }
                }

            }
        }
    }


}