package uz.fintech.uzbankcard.ui.splash

import android.content.Context
import androidx.lifecycle.MutableLiveData
import uz.fintech.uzbankcard.utils.PreferenceManager

@Suppress("DEPRECATION")
class SplashRepo {
    companion object{
       private var instanse:SplashRepo?=null
        fun splashRepoInstanse(): SplashRepo? {
            if (instanse==null){
                instanse=SplashRepo()
            }else{
                instanse
            }
            return instanse
        }
    }

    fun splashFile(ctx:Context):MutableLiveData<Boolean>{
        val preference = PreferenceManager.instanse(ctx)
        val livedata=MutableLiveData<Boolean>()
        livedata.postValue(preference.isCodenSave)
       return livedata
    }

}