package com.example.tickerapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun TimerScreen(
    timerViewModel: TimerViewModel = hiltViewModel()
) {

    val state: String by timerViewModel.state.collectAsStateWithLifecycle()

    val localLifecycleOwner = LocalLifecycleOwner.current
    
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { timerViewModel.startTicker(30L, owner = localLifecycleOwner) }) {
            Text(text = "Start Ticker", textAlign = TextAlign.Center)
        }
        Button(onClick = { timerViewModel.pauseTicker() }) {
            Text(text = "Stop Ticker", textAlign = TextAlign.Center)
        }
        Text(text = state, textAlign = TextAlign.Center)

    }
}