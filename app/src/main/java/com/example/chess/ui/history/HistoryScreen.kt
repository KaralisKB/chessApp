package com.example.chess.ui.history

// import dev.chrisbanes.haze.hazeChild // Uncomment if you use this
// import dev.chrisbanes.haze.BlurEffect // Uncomment if you use this
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chess.ui.components.GameHistoryCard
import com.example.chess.ui.navigation.Screen
import com.example.chess.ui.theme.Cream
import com.example.chess.ui.theme.LightMain
import com.example.chess.ui.theme.MediumMain
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

private val ITEM_HEIGHT_DP = 110.dp
private val SPACER_HEIGHT_DP = 180.dp

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val hazeState = remember { HazeState() }
    val listState = rememberLazyListState()

    var topNavVisible by rememberSaveable { mutableStateOf(false) }

    val density = LocalDensity.current

    val tempGamesList by viewModel.gamesList.collectAsState()
    val gamesList = tempGamesList.reversed()

    // TODO: hook up db data to the cards
    // TODO: make the cards look nicer with the data
    // TODO: maybe find some icons for the game type in middle

    val itemHeightPx = with(density) { ITEM_HEIGHT_DP.toPx() }
    val spacerHeightPx = with(density) { SPACER_HEIGHT_DP.toPx() }

    val totalScrolledPixels by remember {
        derivedStateOf {
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset

            var cumulativeScroll = 0

            if (firstVisibleItemIndex > 0) {
                cumulativeScroll += spacerHeightPx.toInt()
            }

            if (firstVisibleItemIndex > 1) {
                cumulativeScroll += (firstVisibleItemIndex - 1) * itemHeightPx.toInt()
            }

            cumulativeScroll + firstVisibleItemScrollOffset
        }
    }


    val scrollThresholdPx = with(density) { 120.dp.toPx() }
    val scrollProgress = (totalScrolledPixels / scrollThresholdPx).coerceIn(0f, 1f)


    val initialHeight = 200.dp
    val targetHeight = 60.dp
    val animatedHeaderHeight by animateDpAsState(
        targetValue = initialHeight - ((initialHeight - targetHeight) * scrollProgress),
        animationSpec = tween(durationMillis = 0),
        label = "AnimatedHeaderHeight"
    )


    val initialGradientStartYPx = with(density) { 80.dp.toPx() }
    val targetGradientStartYPx = 0f
    val animatedGradientStartY by animateFloatAsState(
        targetValue = initialGradientStartYPx - ((initialGradientStartYPx - targetGradientStartYPx) * scrollProgress),
        animationSpec = tween(durationMillis = 0),
        label = "AnimatedGradientStartY"
    )

    val startColor = MediumMain
    val endColor = LightMain
    val animatedEndColor by animateColorAsState(
        targetValue = endColor.copy(alpha = 0.0f + (0.8f * scrollProgress)),
        animationSpec = tween(durationMillis = 0),
        label = "AnimatedStartColor"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .haze(hazeState),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(SPACER_HEIGHT_DP))
            }
            items(gamesList.size) { index ->
                GameHistoryCard(
                    gamesList[index].whiteName,
                    gamesList[index].blackName,
                    gamesList[index].winnerName,
                    gamesList[index].whiteTimeRemaining,
                    gamesList[index].blackTimeRemaining,
                    gamesList[index].gameType,
                    gamesList[index].date
                )
            }
        }

        LaunchedEffect(Unit) {
            topNavVisible = true
        }

        AnimatedVisibility(
            visible = topNavVisible,
            enter = fadeIn(animationSpec = tween(1800)),
            exit = fadeOut(animationSpec = tween(100)),

            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(startColor, animatedEndColor),
                            startY = animatedGradientStartY,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
                    .height(animatedHeaderHeight)
                    .zIndex(1f),
            ) {
                Text(
                    text = "History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Cream,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 18.dp)
                )
                IconButton(
                    onClick = {
                        navController.navigate(Screen.MainMenu)
                        topNavVisible = false
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Cream,
                        modifier = Modifier.size(40.dp)
                    )
                }
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
