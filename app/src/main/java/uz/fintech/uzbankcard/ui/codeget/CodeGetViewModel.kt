package uz.fintech.uzbankcard.ui.codeget

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.fintech.uzbankcard.network.NetworkStatus


class CodeGetViewModel(app:Application):
    AndroidViewModel(app){
     private var codeAdd=MutableLiveData<Int>()
    private val codeGetRepo by lazy { CodeGetRepo.codeGetInstanse() }
    private var networkStatus=MutableLiveData<NetworkStatus>()
    fun codeGetClearVM(){
        codeGetRepo?.codeClear()
    }
    fun CodeGetAddVM(position:Int){
        val poscodeAdd=codeGetRepo?.codeGetAdd(position)
        codeAdd=poscodeAdd!!
    }
    fun repitAdd():LiveData<Int>{
        return codeAdd
    }

    fun firebaseSearch(ctx:Context){
        val net=codeGetRepo?.onAddFirebase(ctx)
       networkStatus=net!!
    }
    fun statusNet():LiveData<NetworkStatus>{
        return networkStatus
    }

    override fun onCleared() {
        codeGetRepo!!.codeClear()
        super.onCleared()

    }
}