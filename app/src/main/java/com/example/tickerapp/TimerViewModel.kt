package com.example.tickerapp

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tickerlibrary.Ticker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val ticker: Ticker
) : ViewModel() {

    private var _state = MutableStateFlow("00:00")
    var state: StateFlow<String> = _state.asStateFlow()

    fun startTicker(startValue: Long, owner: LifecycleOwner) = viewModelScope.launch {
        ticker.startTick(startValue, CoroutineScope(Dispatchers.Main), owner, onTick = { it ->
            viewModelScope.launch {
                _state.emit(it.toString())
            }
        }, onComplete = {
            viewModelScope.launch {
                _state.emit("Complete")
            }
        })
    }

    fun pauseTicker() = ticker.onTickerPause()
}