package com.example.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    val countDownFlow = flow<Int> {
        val startingValue = 10
        var currentValue = startingValue
        emit(startingValue)

        while (currentValue > 0) {
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    init {
        collectFlow()
    }

    private fun collectFlow() {
        // simple
        viewModelScope.launch {
            countDownFlow
                .filter { time ->
                    time % 2 == 0
                }
                .map { time ->
                    time * time
                }
                .onEach { time ->
                    println("The current time is $time")
                }
                .collect { time ->
                delay(1500L)
                println("The current time is $time")
            }

            countDownFlow.collectLatest { time ->
                delay(1500L)
                println("The current time is $time")
            }
        }

        // terminal operators
        // count
        viewModelScope.launch {
            val count = countDownFlow
                .filter { time ->
                    time % 2 == 0
                }
                .map { time ->
                    time * time
                }
                .onEach { time ->
                    println("The current time is $time")
                }
                .count {
                     it % 2 == 0
                }

            println("The count is $count")
        }

        // reduce
        viewModelScope.launch {
            val reduceResult = countDownFlow
                .reduce { accumulator, value ->
                    accumulator + value
                }

            println("The count is $reduceResult")
        }

        // fold
        viewModelScope.launch {
            val foldResult = countDownFlow
                .fold(100) { accumulator, value ->
                    accumulator + value
                }

            println("The count is $foldResult")
        }

        val flow1 = flow {
            emit(1)
            delay(500L)
            emit(2)
        }

//        flow1.flatMapLatest {  }

        viewModelScope.launch {
            flow1.flatMapConcat { value ->
                flow {
                    emit(value + 1)
                    delay(500L)
                    emit(value + 2)
                }
            }.collect { value ->
                println("The value is $value")
            }
        }
    }

    private fun collectFlow2() {
        val flow = flow {
            delay(250L)
            emit("Appetizer")
            delay(1000L)
            emit("Main Dish")
            delay(100L)
            emit("Dessert")
        }

        // buffer
        viewModelScope.launch {
            flow.onEach {
                println("FLOW: $it is delivered")
            }
                .buffer() // vs conflate == collectlatest
            .collect {
                println("FLOW: Now eating $it")
                delay(1500L)
                println("FLOW: Finished eating $it" )
            }
        }
    }
}