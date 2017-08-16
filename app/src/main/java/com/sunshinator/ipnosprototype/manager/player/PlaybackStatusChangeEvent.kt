package com.sunshinator.ipnosprototype.manager.player

/**
 * Event triggered when the SoundPlayer starts or stops
 *
 * Created by The Sunshinator on 15/08/2017.
 */
class PlaybackStatusChangeEvent private constructor() {

    // Singleton
    private object Holder {
        val INSTANCE = PlaybackStatusChangeEvent()
    }

    companion object {
        val instance: PlaybackStatusChangeEvent by lazy { Holder.INSTANCE }
    }

    internal val _listeners: MutableSet<Listener> = HashSet()

    /**
     * Trigger event for all registered listeners.
     * The event is triggered by {@link SoundPlayer}
     */
    internal fun trigger(isPlaying: Boolean) {
        for (listener in _listeners) listener.onPlaybackTriggered(isPlaying)
    }

    interface Listener {
        fun onPlaybackTriggered(isPlaying: Boolean)
    }
}

fun addPlaybackStatusChangeListener(listener: PlaybackStatusChangeEvent.Listener) {
    PlaybackStatusChangeEvent.instance._listeners.add(listener)
}

fun removePlaybackStatusChangeListener(listener: PlaybackStatusChangeEvent.Listener) {
    PlaybackStatusChangeEvent.instance._listeners.remove(listener)
}