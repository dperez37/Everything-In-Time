package com.everything.time.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Event, Action>(
    state: State,
) : ViewModel() {
    protected val mutableStateFlow: MutableStateFlow<State> = MutableStateFlow(value = state)
    private val eventChannel: Channel<Event> = Channel(capacity = Channel.UNLIMITED)
    private val internalActionChannel: Channel<Action> = Channel(capacity = Channel.UNLIMITED)

    val stateFlow: StateFlow<State> = mutableStateFlow.asStateFlow()
    val eventFlow: Flow<Event> = eventChannel.receiveAsFlow()

    val actionChannel: SendChannel<Action> = internalActionChannel

    init {
        internalActionChannel
            .consumeAsFlow()
            .onEach(::handleAction)
            .launchIn(viewModelScope)
    }

    protected abstract fun handleAction(action: Action)

    protected fun sendEvent(event: Event) {
        viewModelScope.launch { eventChannel.send(event) }
    }

    fun sendAction(action: Action) {
        actionChannel.trySend(action)
    }
}