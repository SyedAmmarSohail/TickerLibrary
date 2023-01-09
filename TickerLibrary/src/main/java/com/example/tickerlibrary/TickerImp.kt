package com.example.tickerlibrary

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

interface Ticker {
    fun startTick(
        secondValueInFuture: Long,
        coroutineScope: CoroutineScope,
        owner: LifecycleOwner,
        onTick: (number: Long) -> Unit,
        onComplete: () -> Unit
    )

    fun onTickerPause()
}

object TickerImp : Ticker, LifecycleEventObserver {

    private var lastValue = 0L
    private var ONE_MILLI_SECOND = 1000L

    lateinit var scope: CoroutineScope

    override fun startTick(
        secondValueInFuture: Long,
        coroutineScope: CoroutineScope,
        owner: LifecycleOwner,
        onTick: (number: Long) -> Unit,
        onComplete: () -> Unit
    ) {
        owner.lifecycle.addObserver(this)
        scope = coroutineScope

        val startValue = getStartValue(secondValueInFuture)

        tickerFlow(startValue)
            .onEach {
                lastValue = it
                if (it == 0L) {
                    onComplete()
                } else {
                    onTick(it)
                }
            }
            .launchIn(coroutineScope)
    }

    override fun onTickerPause() {
        if (scope.isActive) {
            scope.cancel()
        }
    }

    private fun getStartValue(secondValueInFuture: Long): Long {
        return if (lastValue != 0L) lastValue else secondValueInFuture
    }

    private fun tickerFlow(
        startValue: Long,
    ) =
        flow {
            for (i in startValue downTo 0) {
                emit(i)
                delay(ONE_MILLI_SECOND)
            }
        }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                print("Ticker: onResume")
            }
            Lifecycle.Event.ON_PAUSE -> {
                print("Ticker: onPause")
                if (scope.isActive)
                    scope.cancel()
            }
            else -> Unit
        }
    }
}
