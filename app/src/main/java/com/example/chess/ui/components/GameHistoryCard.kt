package com.example.chess.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.chess.R
import com.example.chess.local.model.GameType
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
            //Header with date,
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

            // Middle row with winners crown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(){
                    ChessLottie(modifier = Modifier.size(50.dp), R.raw.crown, 1f)
                }
                Box(){

                }
                Box(){

                }
            }
            //Main row with names, game type, winner and time remaining
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box() {
                    Text(
                        text = "Player 1",
                        color = Color(0xFFF2EFE7)
                    )
                }
                Box() {
                    Text(
                        text = "5",
                        color = Color(0xFFF2EFE7)
                    )
                }
                Box() {
                    Text(
                        text = "Player 2",
                        color = Color(0xFFF2EFE7)
                    )
                }
            }
        }
    }


}