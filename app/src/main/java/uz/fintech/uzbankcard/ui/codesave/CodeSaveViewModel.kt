package uz.fintech.uzbankcard.ui.codesave

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.network.NetworkStatus

class CodeSaveViewModel(app:Application) :AndroidViewModel(app){
   private val codeSaveRepo by lazyFast { CodeSaveRepo.codeSaveRP() }

    private var ldAddList=MutableLiveData<Int>()
    private var firebaseLd=MutableLiveData<NetworkStatus>()
    fun codeClear(){
        codeSaveRepo.listClear()
    }

    fun codeAddListVM(position:Int):LiveData<Int>{
        ldAddList=codeSaveRepo.listAdd(position)
        return ldAddList
    }

    fun firebseSaveVM(ctx:Context):LiveData<NetworkStatus>{
        val lives=codeSaveRepo.firebaseSave(ctx)
        return lives
    }
}