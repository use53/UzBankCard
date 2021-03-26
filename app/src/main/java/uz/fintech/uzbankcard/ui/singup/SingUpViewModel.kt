package uz.fintech.uzbankcard.ui.singup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.utils.IPreferences
import uz.fintech.uzbankcard.utils.PreferenceManager

class SingUpViewModel (app:Application):AndroidViewModel(app){

    private val preference: IPreferences by lazyFast {
        PreferenceManager.instanse(app)
    }


    fun singUpSave(callNum:String,name:String,password:String){
        preference.isSingupName = name
        preference.isSingupPassword = password
        preference.isCallNumber=callNum
    }

}