package com.sunshinator.ipnosprototype.manager

import android.content.Context
import com.sunshinator.ipnosprototype.entity.Sound
import com.sunshinator.ipnosprototype.manager.player.SoundPlayer

/**
 * Manages playing, stopping, saving sounds
 *
 * Created by The Sunshinator on 12/08/2017.
 */
private val MAX_SIMULT_SOUNDS = 3

class SoundSelectionManager(context: Context) {

    private val _selectedSounds: HashSet<Sound> = HashSet()
    private val _mediaPlayer: SoundPlayer = SoundPlayer()
    private var _context: Context? = context
    var errorListener: ErrorListener? = null

    fun onTriggered(sound: Sound) {

        when {
            isSelected(sound) -> {
                _mediaPlayer.stop(sound)
                _selectedSounds.remove(sound)

                // Trigger related events
                SoundSelectionEvent.instance.trigger(sound, false)
            }

            _selectedSounds.size >= MAX_SIMULT_SOUNDS ->
                errorListener?.onError(Error.MAX_SIMULT_SOUNDS)

            else -> {
                _selectedSounds.add(sound)

                for (s in _selectedSounds) _mediaPlayer.play(_context!!, s)

                // Trigger related events
                SoundSelectionEvent.instance.trigger(sound, true)
            }
        }
    }

    fun onClear() {
        _mediaPlayer.stopAll()

        for (sound in _selectedSounds)
            SoundSelectionEvent.instance.trigger(sound, false)

        _selectedSounds.clear()
    }

    fun onPlayPause() {
        if (_mediaPlayer.isPlaying()) _mediaPlayer.stopAll()
        else for (s in _selectedSounds) _mediaPlayer.play(_context!!, s)
    }

    fun onStop() {
        saveSounds(_context!!, _selectedSounds)
        onClear()
        _context = null
    }

    fun isSelected(s: Sound): Boolean = _selectedSounds.contains(s)

    fun loadSavedSounds() {
        onClear()
        _selectedSounds.addAll(getSavedSounds(_context!!))
        for (sound in _selectedSounds)
            SoundSelectionEvent.instance.trigger(sound, true)
    }

    interface ErrorListener {
        fun onError(error: Error)
    }

    enum class Error {
        MAX_SIMULT_SOUNDS, OTHER
    }
}