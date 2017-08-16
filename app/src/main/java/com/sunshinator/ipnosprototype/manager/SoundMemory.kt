package com.sunshinator.ipnosprototype.manager

import android.content.Context
import com.sunshinator.ipnosprototype.entity.Sound


/**
 * Manages saving currently or last played sounds
 *
 * Created by The Sunshinator on 14/08/2017.
 */

val PREF_FILE_NAME = "sounds_save_file"
val KEY_SOUND_SET = "SOUND_SET"

fun saveSounds(context: Context, sounds: Set<Sound>) {

    val enumNames = HashSet<String>()
    sounds.mapTo(enumNames) { it.toString() }

    val sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

    editor.putStringSet(KEY_SOUND_SET, enumNames)

    editor.apply()
}

fun getSavedSounds(context: Context): HashSet<Sound> {
    val sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    val enumNames = sharedPref.getStringSet(KEY_SOUND_SET, HashSet<String>())

    val enums = HashSet<Sound>()
    enumNames.mapTo(enums) { Sound.valueOf(it) }

    return enums
}