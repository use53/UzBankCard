package uz.fintech.uzbankcard.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SplashViewModel(val app:Application):
    AndroidViewModel(app){

    private var splashRepo:SplashRepo?=null
   var isSplash=MutableLiveData<Boolean>()

    fun  splashRepo(){
           splashRepo= SplashRepo.splashRepoInstanse()
       val livedate= splashRepo?.splashFile(app.applicationContext)
        isSplash=livedate!!
    }

    fun  getSplash():LiveData<Boolean>{
        return isSplash
    }

}