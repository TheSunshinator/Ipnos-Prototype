package com.sunshinator.ipnosprototype.entity

import com.sunshinator.ipnosprototype.R

/**
 * Enumeration of every available sound
 *
 * Created by The Sunshinator on 12/08/2017.
 */
enum class Sound(val resourceBackground: Int, val resourceText: Int, val resourceSound: Int) {

    BIRDS(R.drawable.toggle_birds, R.string.sound_birds, R.raw.birds),
    FLUTE(R.drawable.toggle_flute, R.string.sound_flute, R.raw.flute),
    LOUNGE(R.drawable.toggle_lounge, R.string.sound_lounge, R.raw.lounge),
    MUSIC_BOX(R.drawable.toggle_musicbox, R.string.sound_musicbox, R.raw.musicbox),
    OCEAN(R.drawable.toggle_ocean, R.string.sound_ocean, R.raw.ocean),
    ORCHESTRAL(R.drawable.toggle_orchestral, R.string.sound_orchestral, R.raw.orchestral),
    PIANO(R.drawable.toggle_piano, R.string.sound_piano, R.raw.piano),
    RAIN(R.drawable.toggle_rain, R.string.sound_rain, R.raw.rain),
    WIND(R.drawable.toggle_wind, R.string.sound_wind, R.raw.wind);

}