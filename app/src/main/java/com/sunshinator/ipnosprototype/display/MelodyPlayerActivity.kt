package com.sunshinator.ipnosprototype.display

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.sunshinator.ipnosprototype.R
import com.sunshinator.ipnosprototype.manager.SoundSelectionManager
import com.sunshinator.ipnosprototype.manager.player.PlaybackStatusChangeEvent
import com.sunshinator.ipnosprototype.manager.player.addPlaybackStatusChangeListener
import com.sunshinator.ipnosprototype.manager.player.removePlaybackStatusChangeListener
import com.sunshinator.ipnosprototype.view.ToggleButton

class MelodyPlayerActivity : AppCompatActivity(),
        SoundSelectionManager.ErrorListener, PlaybackStatusChangeEvent.Listener {

    private var _manager: SoundSelectionManager? = null
    private var _grid: RecyclerView? = null
    private var _playBtn: ToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_melody_player)

        _grid = findViewById(R.id.grid)
        _playBtn = findViewById(R.id.play)
    }

    override fun onResume() {
        super.onResume()

        _manager = SoundSelectionManager(this)
        _grid?.adapter = SoundAdapter(this, { sound -> _manager?.onTriggered(sound) })

        startComponents()
    }

    override fun onPause() {
        super.onPause()

        stopComponents()
    }

    override fun onError(error: SoundSelectionManager.Error) {
        when (error) {
            SoundSelectionManager.Error.MAX_SIMULT_SOUNDS -> {
                // AlertDialogs could have been used as well
                Toast.makeText(this, R.string.error_max_playing_sounds, Toast.LENGTH_LONG).show()
            }

            SoundSelectionManager.Error.OTHER -> {
                Toast.makeText(this, R.string.error_media_player, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPlaybackTriggered(isPlaying: Boolean) {
        // Drawable is adjusted via selector drawable
        _playBtn?.isChecked = isPlaying
    }

    /**
     * Loads saved sounds and start event listeners
     */
    private fun startComponents() {

        _manager?.loadSavedSounds()

        _manager?.errorListener = this

        addPlaybackStatusChangeListener(this)
    }

    /**
     * Saved playing sounds and stop event listeners
     */
    private fun stopComponents() {
        (_grid?.adapter as SoundAdapter?)?.onStop()
        _grid?.adapter = null

        _manager?.errorListener = null

        removePlaybackStatusChangeListener(this)

        _manager?.onStop()
        _manager = null
    }

    fun onClear(@Suppress("UNUSED_PARAMETER") v: View) {
        _manager?.onClear()
    }

    fun onPlayOrPause(@Suppress("UNUSED_PARAMETER") v: View) {
        _manager?.onPlayPause()
    }
}
