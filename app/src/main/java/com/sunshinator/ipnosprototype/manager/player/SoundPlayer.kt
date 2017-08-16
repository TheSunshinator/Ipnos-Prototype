package com.sunshinator.ipnosprototype.manager.player

import android.content.Context
import android.media.MediaPlayer
import com.sunshinator.ipnosprototype.entity.Sound

/**
 * Manages currently playing sounds
 *
 * Created by The Sunshinator on 14/08/2017.
 */
class SoundPlayer {

    private val _playingSounds: HashMap<Sound, MediaPlayer> = HashMap()

    fun play(context: Context, sound: Sound) {
        val wasPlaying = isPlaying()

        if (sound !in _playingSounds) {

            val player: MediaPlayer = MediaPlayer.create(context, sound.resourceSound)
            player.isLooping = true
            player.start()

            _playingSounds.put(sound, player)

            // Trigger related events
            if (!wasPlaying) PlaybackStatusChangeEvent.instance.trigger(true)
            SoundStatusChangeEvent.instance.trigger(sound, true)

        }
    }

    fun stop(sound: Sound) {

        if (sound in _playingSounds) {

            val player = _playingSounds[sound]
            player?.stop()
            player?.release()

            _playingSounds.remove(sound)

            // Trigger related events
            if (_playingSounds.isEmpty()) PlaybackStatusChangeEvent.instance.trigger(false)
            SoundStatusChangeEvent.instance.trigger(sound, false)

        }
    }

    fun stopAll() {
        val wasPlaying = isPlaying()

        for (sound in _playingSounds.keys) {
            _playingSounds[sound]?.stop()
            _playingSounds[sound]?.release()

            SoundStatusChangeEvent.instance.trigger(sound, false)
        }

        _playingSounds.clear()

        // Trigger related events
        if (wasPlaying) PlaybackStatusChangeEvent.instance.trigger(false)
    }

    fun isPlaying(): Boolean = !_playingSounds.isEmpty()

}