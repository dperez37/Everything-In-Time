package com.everything.time.ui.screen.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor() : BaseViewModel<Unit, RootEvent, RootAction>(
    state = Unit,
) {
    override fun handleAction(action: RootAction) {
        when (action) {
            RootAction.JavaTabClick -> handleJavaTabClick()
            RootAction.KotlinTabClick -> handleKotlinTabClick()
        }
    }

    private fun handleJavaTabClick() {
        sendEvent(RootEvent.NavigateToJavaTab)
    }

    private fun handleKotlinTabClick() {
        sendEvent(RootEvent.NavigateToKotlinTab)
    }
}

sealed class RootAction {
    data object JavaTabClick : RootAction()
    data object KotlinTabClick : RootAction()
}

sealed class RootEvent {
    data object NavigateToJavaTab : RootEvent()
    data object NavigateToKotlinTab : RootEvent()
}
