package com.example.chess.ui.components

import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import com.example.chess.local.model.Game
import com.example.chess.local.model.GameType
import com.example.chess.ui.theme.Jade
import java.lang.StackWalker.Option
import kotlin.reflect.KProperty

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
                containerColor = Color(0xFFF2EFE7),
                contentColor = Color(0xFF006A71)
            ),
            border = BorderStroke(1.dp, Color(0xFF006A71))
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
                .background(Color(0xFFF2EFE7), RoundedCornerShape(
                    topStart = 30.dp, topEnd = 30.dp, bottomStart = 30.dp, bottomEnd = 30.dp))
                .border(1.dp, Color(0xFF006A71), shape = RoundedCornerShape(
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
                            focusedLabelColor = Color(0xFF006A71),
                            unfocusedLabelColor = Color(0xFF006A71),
                            focusedPlaceholderColor = Color(0xFF006A71),
                            unfocusedPlaceholderColor = Color(0xFF006A71),
                            unfocusedBorderColor = Color(0xFF006A71),
                            focusedBorderColor = Color(0xFF006A71)
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
                            focusedLabelColor = Color(0xFF006A71),
                            unfocusedLabelColor = Color(0xFF006A71),
                            focusedPlaceholderColor = Color(0xFF006A71),
                            unfocusedPlaceholderColor = Color(0xFF006A71),
                            unfocusedBorderColor = Color(0xFF006A71),
                            focusedBorderColor = Color(0xFF006A71)
                        )
                    )

                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        GameOption(Modifier.weight(1f), "60") {
                            onAction(GameType.SIXTY, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "30") {
                            onAction(GameType.THIRTY, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "15") {
                            onAction(GameType.FIFTEEN, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                    }
                    Spacer(modifier = Modifier.size(3.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        GameOption(Modifier.weight(1f), "10") {
                            onAction(GameType.TEN, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "5 + 3") {
                            onAction(GameType.FIVE_THREE, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "5") {
                            onAction(GameType.FIVE, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                    }
                    Spacer(modifier = Modifier.size(3.dp))
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        GameOption(Modifier.weight(1f), "3 + 2") {
                            onAction(GameType.THREE_TWO, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "3") {
                            onAction(GameType.THREE, whiteName.ifBlank { "White" }, blackName.ifBlank { "Black" })
                            expanded = false
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        GameOption(Modifier.weight(1f), "1") {
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
            .border(1.dp, Color(0xFF006A71))
            .border(8.dp, Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            style = TextStyle(fontSize = 24.sp, color = Color(0xFF006A71), textAlign = TextAlign.Center)
        )
    }

}

