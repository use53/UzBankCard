package uz.fintech.uzbankcard.utils

import android.content.Context
import android.content.SharedPreferences
private const val KEY_SINGUP_NAME="KEY_SINGUP_NAME"
private const val KEY_SINGUP_PASSWORD="KEY_SINGUP_PASSWORD"
private const val KEY_SINGUP_CALLNUM="KEY_SINGUP_CALLNUMBER"

class PreferenceManager( preferences:SharedPreferences):IPreferences{

    override var isSingupName: String by
    TextPreference(preferences,KEY_SINGUP_NAME)

    override var isSingupPassword: String by
    TextPreference(preferences, KEY_SINGUP_PASSWORD)

    override var isCallNumber: Boolean by
    TextNumPreference(preferences,KEY_SINGUP_CALLNUM)

    companion object{
        private var instanse:IPreferences?=null
        fun instanse(ctx: Context): IPreferences {
            if (instanse==null){
                val preference= androidx.preference.PreferenceManager.getDefaultSharedPreferences(ctx)
                instanse=PreferenceManager(preference)
            }
            return instanse!!
        }

    }
}