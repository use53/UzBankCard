package uz.fintech.uzbankcard.utils

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TextPreference (
private val preferences:SharedPreferences,
private val key:String,
private val value:String=""
):ReadWriteProperty<Any,String>{
    override fun getValue(thisRef: Any, property: KProperty<*>): String =
        preferences.getString(key,"").toString()

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
    preferences.edit().putString(key,value).apply()
    }
}