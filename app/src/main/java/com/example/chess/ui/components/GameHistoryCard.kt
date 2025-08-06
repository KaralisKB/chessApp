package com.example.chess.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.R
import com.example.chess.local.model.GameType
import com.example.chess.ui.theme.Cream
import com.example.chess.ui.theme.MediumMain
import com.example.chess.utils.convertLongToDateTime

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
                .clickable { },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(2.dp, Cream),
            colors = CardDefaults.cardColors(containerColor = MediumMain)
        ) {
            //Top row with date of match
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = convertLongToDateTime(date!!, false) ?: "00:00:00",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 5.dp),
                    color = Cream
                )
            }

            // Middle bigger row with three columns, player, game type, player
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                    .background(Color(0xFFA0B2C9), shape = RoundedCornerShape(6.dp))
                    .border(1.dp, Cream, shape = RoundedCornerShape(6.dp)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                    Row() {
                        if (winner == player1name) {
                            ChessLottie(modifier = Modifier.size(50.dp), R.raw.crown, 1f)
                        } else {
                            Box(modifier = Modifier.size(50.dp)) {}
                        }
                    }

                    Row() {
                        Text(
                            text = player1name ?: "Player 1",
                            color = Cream
                        )
                    }

                    Row() {
                        Text(
                            text = convertLongToDateTime(player1time!!, true) ?: "00:00:00",
                            color = Cream
                        )
                    }

                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row() {

                        Text(
                            text = "Type\n" +
                                    gameType.toString(),
                            color = Cream,
                            textAlign = TextAlign.Center
                        )

                    }

                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row() {
                        if (winner == player2name) {
                            ChessLottie(modifier = Modifier.size(50.dp), R.raw.crown, 1f)
                        } else {
                            Box(modifier = Modifier.size(50.dp)) {}
                        }
                    }

                    Row() {
                        Text(
                            text = player2name ?: "Player 2",
                            color = Cream
                        )
                    }

                    Row() {
                        Text(
                            text = convertLongToDateTime(player2time!!, true) ?: "00:00:00",
                            color = Cream
                        )
                    }
                }
            }
        }
    }


}