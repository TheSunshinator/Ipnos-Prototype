package com.sunshinator.ipnosprototype.manager

import com.sunshinator.ipnosprototype.entity.Sound

/**
 * Event triggered when a sound is selected by the manager
 *
 * Created by The Sunshinator on 15/08/2017.
 */
class SoundSelectionEvent private constructor() {

    // Singleton
    private object Holder {
        val INSTANCE = SoundSelectionEvent()
    }

    companion object {
        val instance: SoundSelectionEvent by lazy { Holder.INSTANCE }
    }

    internal val _listeners: MutableSet<Listener> = HashSet()

    /**
     * Trigger event for all registered listeners.
     * The event is triggered by {@link SoundSelectionManager}
     */
    internal fun trigger(sound: Sound, isSelected: Boolean) {
        for (listener in _listeners) listener.onSoundSelection(sound, isSelected)
    }

    interface Listener {
        fun onSoundSelection(sound: Sound, isSelected: Boolean)
    }
}

fun addSoundSelectionListener(listener: SoundSelectionEvent.Listener) {
    SoundSelectionEvent.instance._listeners.add(listener)
}

fun removeSoundSelectionListener(listener: SoundSelectionEvent.Listener) {
    SoundSelectionEvent.instance._listeners.remove(listener)
}