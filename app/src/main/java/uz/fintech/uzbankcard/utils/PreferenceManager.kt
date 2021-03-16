package uz.fintech.uzbankcard.utils

import android.content.Context
import android.content.SharedPreferences
import uz.fintech.uzbankcard.model.CardModel

private const val KEY_SINGUP_NAME="KEY_SINGUP_NAME"
private const val KEY_SINGUP_PASSWORD="KEY_SINGUP_PASSWORD"
private const val KEY_SINGUP_CALLNUM="KEY_SINGUP_CALLNUMBER"
private const val KEY_HOME_SAVE_BOOLEAN="KEY_HOME_SAVE_BOOLEAN"
private const val KEY_HOME_CARDMODEL_SAVE="KEY_HOME_CARDMODEL_SAVE"
class PreferenceManager( preferences:SharedPreferences):IPreferences{

    override var isSingupName: String by
    TextPreference(preferences,KEY_SINGUP_NAME)

    override var isSingupPassword: String by
    TextPreference(preferences, KEY_SINGUP_PASSWORD)

    override var isCallNumber: Boolean by
    TextNumPreference(preferences,KEY_SINGUP_CALLNUM)
    override var isCardSaveBoolean: Boolean by
    TextNumPreference(preferences, KEY_HOME_SAVE_BOOLEAN)

    override var isCardModelSave: String by
    TextPreference(preferences, KEY_HOME_CARDMODEL_SAVE)

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