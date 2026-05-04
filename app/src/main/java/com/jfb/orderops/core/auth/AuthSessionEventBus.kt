package com.jfb.orderops.core.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AuthSessionEventBus {

    private val _events = MutableSharedFlow<AuthSessionEvent>(
        extraBufferCapacity = 1
    )

    val events = _events.asSharedFlow()

    fun notifySessionExpired() {
        _events.tryEmit(AuthSessionEvent.SessionExpired)
    }
}

sealed interface AuthSessionEvent {
    data object SessionExpired : AuthSessionEvent
}