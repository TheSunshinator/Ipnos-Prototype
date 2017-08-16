package com.sunshinator.ipnosprototype.manager.player

import com.sunshinator.ipnosprototype.entity.Sound

/**
 * Event triggered when an individual sound is started or stopped
 *
 * Created by The Sunshinator on 15/08/2017.
 */
class SoundStatusChangeEvent private constructor() {

    // Singleton
    private object Holder {
        val INSTANCE = SoundStatusChangeEvent()
    }

    companion object {
        val instance: SoundStatusChangeEvent by lazy { Holder.INSTANCE }
    }

    internal val _listeners: MutableSet<Listener> = HashSet()

    internal fun trigger(sound: Sound, isPlaying: Boolean) {
        for (listener in _listeners) listener.onSoundPlaybackTriggered(sound, isPlaying)
    }

    /**
     * Trigger event for all registered listeners.
     * The event is triggered by {@link SoundPlayer)
     */
    interface Listener {
        fun onSoundPlaybackTriggered(sound: Sound, isPlaying: Boolean)
    }
}

fun addSoundStatusChangeListener(listener: SoundStatusChangeEvent.Listener) {
    SoundStatusChangeEvent.instance._listeners.add(listener)
}

fun removeSoundStatusChangeListener(listener: SoundStatusChangeEvent.Listener) {
    SoundStatusChangeEvent.instance._listeners.remove(listener)
}