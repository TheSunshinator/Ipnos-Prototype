package com.sunshinator.ipnosprototype.display

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.Guideline
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunshinator.ipnosprototype.R
import com.sunshinator.ipnosprototype.entity.Sound
import com.sunshinator.ipnosprototype.manager.SoundSelectionEvent
import com.sunshinator.ipnosprototype.manager.addSoundSelectionListener
import com.sunshinator.ipnosprototype.manager.player.SoundStatusChangeEvent
import com.sunshinator.ipnosprototype.manager.player.addSoundStatusChangeListener
import com.sunshinator.ipnosprototype.manager.player.removeSoundStatusChangeListener
import com.sunshinator.ipnosprototype.manager.removeSoundSelectionListener
import com.sunshinator.ipnosprototype.view.ToggleButton

/**
 * Adapter that shows button to trigger sounds to create a melody
 *
 * Created by The Sunshinator on 12/08/2017.
 */
class SoundAdapter(private var _context: Context?, private val _clickListener: (Sound) -> Unit)
    : RecyclerView.Adapter<SoundAdapter.ViewHolder>(),
        SoundSelectionEvent.Listener, SoundStatusChangeEvent.Listener {

    private val _items = Sound.values()
    private val _selectedSounds = HashSet<Sound>()
    private val _playingSounds = HashSet<Sound>()
    private val _soundsToViewMap = HashMap<Sound, ToggleButton?>()

    init {
        addSoundSelectionListener(this)
        addSoundStatusChangeListener(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(_context).inflate(R.layout.item_sound_row, parent, false)
        return ViewHolder(view, _clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        var index = position * 3 // Three items per column
        holder?.setTo(
                _items[index++],
                if (index < _items.size) _items[index++] else null,
                if (index < _items.size) _items[index] else null)
    }

    override fun getItemCount(): Int = Math.ceil(_items.size / 3.0).toInt() // Three items per column

    override fun onSoundSelection(sound: Sound, isSelected: Boolean) {
        if (isSelected) {
            _selectedSounds.add(sound)
        } else {
            _selectedSounds.remove(sound)
        }

        _soundsToViewMap[sound]?.isChecked = isSelected
    }

    override fun onSoundPlaybackTriggered(sound: Sound, isPlaying: Boolean) {
        if (isPlaying) {
            _playingSounds.add(sound)
            _soundsToViewMap[sound]?.startResourceAnimation( R.anim.sound_playing )
        } else {
            _playingSounds.remove(sound)
            _soundsToViewMap[sound]?.stopResourceAnimation()
        }
    }

    fun onStop() {
        _context = null

        removeSoundSelectionListener(this)
        removeSoundStatusChangeListener(this)
    }

    inner class ViewHolder(view: View, private val _listener: ((Sound) -> Unit))
        : RecyclerView.ViewHolder(view) {

        private val _viewSound1: ToggleButton = view.findViewById(R.id.element1)
        private val _viewSound2: ToggleButton = view.findViewById(R.id.element2)
        private val _viewSound3: ToggleButton = view.findViewById(R.id.element3)
        private val _guidelineRope: Guideline = view.findViewById(R.id.guideline_rope)
        private val _guidelineSound1: Guideline = view.findViewById(R.id.guideline1)
        private val _guidelineSound2: Guideline = view.findViewById(R.id.guideline2)
        private val _guidelineSound3: Guideline = view.findViewById(R.id.guideline3)

        init {
            _viewSound1.setTextColor(ContextCompat.getColor(_context, R.color.text_btn_sound))
            _viewSound2.setTextColor(ContextCompat.getColor(_context, R.color.text_btn_sound))
            _viewSound3.setTextColor(ContextCompat.getColor(_context, R.color.text_btn_sound))
        }

        private var sound1: Sound = Sound.BIRDS
            set(value) {
                setSoundInView(value, _viewSound1)
            }

        private var sound2: Sound? = Sound.FLUTE
            set(value) {
                setSoundInView(value, _viewSound2)

                adjustRopePosition(
                        when {
                            value != null -> _guidelineSound3
                            sound2 != null -> _guidelineSound2
                            else -> _guidelineSound1
                        })
            }

        private var sound3: Sound? = Sound.LOUNGE
            set(value) {
                setSoundInView(value, _viewSound3)

                adjustRopePosition(
                        when {
                            value != null -> _guidelineSound3
                            sound2 != null -> _guidelineSound2
                            else -> _guidelineSound1
                        })
            }

        fun setTo(s1: Sound, s2: Sound?, s3: Sound?) {
            _soundsToViewMap.put(s1, null)
            if (s2 != null) _soundsToViewMap.put(s2, null)
            if (s3 != null) _soundsToViewMap.put(s3, null)

            sound1 = s1
            sound2 = s2
            sound3 = s3
        }

        private fun setSoundInView(sound: Sound?, view: ToggleButton) {
            if (sound != null) {
                view.setText(sound.resourceText)
                view.setIcon(sound.resourceBackground)
                view.visibility = View.VISIBLE
                _soundsToViewMap.put(sound, view)
            } else
                view.visibility = View.GONE

            view.isChecked = sound in _selectedSounds
            view.setOnClickListener { if (sound != null) _listener.invoke(sound) }

            if (sound in _playingSounds)
                view.startResourceAnimation( R.anim.sound_playing )
            else
                view.stopResourceAnimation()
        }

        private fun adjustRopePosition(view: View) {
            val paramsSound: ConstraintLayout.LayoutParams = view.layoutParams
                    as ConstraintLayout.LayoutParams
            val paramsRope: ConstraintLayout.LayoutParams = _guidelineRope.layoutParams
                    as ConstraintLayout.LayoutParams

            // Add 10% so that the rope ends behind the drawable
            paramsRope.guidePercent = paramsSound.guidePercent + 0.1f

            _guidelineRope.layoutParams = paramsRope
        }
    }
}