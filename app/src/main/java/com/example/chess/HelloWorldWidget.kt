package com.example.chess

import android.content.Context
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.text.Text
import com.example.chess.ui.main.MainActivity

class HelloWorldWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                Scaffold(
                    backgroundColor = GlanceTheme.colors.background,
                    titleBar = {
                        TitleBar(
                            startIcon = ImageProvider(R.drawable.chess_nlt60),
                            title = "Chess"
                        )
                    }
                ) {
                    Column {
                        Text(
                            text = "Hello, World!",
                        )
                        Button(
                            text = "Open Test Activity",
                            onClick = actionStartActivity<MainActivity>()
                        )
                    }
                }
            }
        }
    }
}