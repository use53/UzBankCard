package uz.fintech.uzbankcard.utils

import android.content.Context
import android.content.SharedPreferences

import androidx.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TextNumPreference (
    private val preference: SharedPreferences,
    private val key:String,
    private val value:Boolean=false
):ReadWriteProperty<Any,Boolean>{
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        preference.getBoolean(key,false)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
      preference.edit().putBoolean(key,value).apply()
    }

}