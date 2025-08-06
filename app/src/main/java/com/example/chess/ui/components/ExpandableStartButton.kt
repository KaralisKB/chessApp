package com.example.chess.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.local.model.GameType
import com.example.chess.ui.theme.Cream
import com.example.chess.ui.theme.DarkMain

@Composable
fun ExpandableStartButton(
    onAction: (GameType, String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedGameType by remember { mutableStateOf<GameType?>(null)}
    var whiteName by remember { mutableStateOf("") }
    var blackName by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = { expanded = !expanded },
            shape = RoundedCornerShape(
                topStart = if (expanded) 21.dp else 50.dp,
                topEnd = if (expanded) 21.dp else 50.dp,
                bottomStart = if (expanded) 0.dp else 50.dp,
                bottomEnd = if (expanded) 0.dp else 50.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Cream,
                contentColor = DarkMain
            ),
            border = BorderStroke(1.dp, DarkMain)
        ) {
            Text("Start Game")
        }

        Box(
            modifier = Modifier
                .offset(y = (-8).dp)
                .fillMaxWidth(0.8f)
                .animateContentSize(
                    animationSpec = SpringSpec(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .background(Cream, RoundedCornerShape(
                    topStart = 30.dp, topEnd = 30.dp, bottomStart = 30.dp, bottomEnd = 30.dp))
                .border(1.dp, DarkMain, shape = RoundedCornerShape(
                    topStart = 30.dp, topEnd = 30.dp, bottomStart = 30.dp, bottomEnd = 30.dp
                ))
        ) {
            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = whiteName,
                        onValueChange = { whiteName = it },
                        label = { Text("White Player Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = DarkMain,
                            unfocusedLabelColor = DarkMain,
                            focusedPlaceholderColor = DarkMain,
                            unfocusedPlaceholderColor = DarkMain,
                            unfocusedBorderColor = DarkMain,
                            focusedBorderColor = DarkMain,
                            focusedTextColor = DarkMain
                        )
                    )

                    OutlinedTextField(
                        value = blackName,
                        onValueChange = { blackName = it },
                        label = { Text("Black Player Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = DarkMain,
                            unfocusedLabelColor = DarkMain,
                            focusedPlaceholderColor = DarkMain,
                            unfocusedPlaceholderColor = DarkMain,
                            unfocusedBorderColor = DarkMain,
                            focusedBorderColor = DarkMain,
                            focusedTextColor = DarkMain
                        )
                    )

                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        GameOption(Modifier.weight(1f), "60m") {
                            onAction(GameType.SIXTY, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "30m") {
                            onAction(GameType.THIRTY, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "15m") {
                            onAction(GameType.FIFTEEN, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                    }
                    Spacer(modifier = Modifier.size(3.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        GameOption(Modifier.weight(1f), "10m") {
                            onAction(GameType.TEN, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "5m + 3s") {
                            onAction(GameType.FIVE_THREE, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "5m") {
                            onAction(GameType.FIVE, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                    }
                    Spacer(modifier = Modifier.size(3.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        GameOption(Modifier.weight(1f), "3m + 2s") {
                            onAction(GameType.THREE_TWO, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "3m") {
                            onAction(GameType.THREE, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "1m") {
                            onAction(GameType.ONE, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun GameOption(modifier: Modifier, label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .border(1.dp, DarkMain)
            .border(8.dp, Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            style = TextStyle(fontSize = 18.sp, color = DarkMain, textAlign = TextAlign.Center)
        )
    }

}

