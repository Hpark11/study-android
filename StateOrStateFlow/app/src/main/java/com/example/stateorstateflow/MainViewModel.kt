package com.example.stateorstateflow

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class MainViewModel(
    private val stateHandle: SavedStateHandle
): ViewModel() {

    val color = stateHandle.getStateFlow("color", 0xffffffff)

//    val redValue = color.map {
//        return@map it
//    }.stateIn(viewModelScope)

    var composeColor by mutableStateOf(
        stateHandle.get<Long>("color") ?: 0xffffffff
    )
        private set

    fun generateNewColor() {
        val color = Random.nextLong(0xFFFFFFFF)
        stateHandle["color"] = color
        composeColor = color
    }


}