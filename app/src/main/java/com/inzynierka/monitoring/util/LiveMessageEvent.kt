package com.inzynierka.monitoring.util

import androidx.lifecycle.LifecycleOwner

class LiveMessageEvent<T> : SingleLiveEvent<(T.() -> Unit)?>() {

    fun setEventReceiver(owner: LifecycleOwner, receiver: T) {
        observe(owner, { event ->
            if (event != null) {
                receiver.event()
            }
        })
    }

    fun sendEvent(event: (T.() -> Unit)?) {
        value = event
    }
}