package uz.fintech.uzbankcard.navui.twoui.addcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardColorModel
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.PreferenceManager

interface IAddCard {
    fun searchFirebase(cardModel: String)
    fun saveLocalDB()

}


class AddCardViewModel(app: Application) :
    AndroidViewModel(app), IAddCard {


    private var ldsearchVM = MutableLiveData<CardModel>()
    private var livestatusVM = MutableLiveData<NetworkStatus>()
    private val cd = CompositeDisposable()
    private val addCardRepo by lazyFast { AddCardRepo.homeRepoInstanse(app.applicationContext) }


    override fun searchFirebase(cardModel: String) {
        addCardRepo.searchRepo(cardModel)
        ldsearchVM = addCardRepo.ldsearch()
    }

    fun ldSearch(): LiveData<CardModel> {
        return ldsearchVM
    }


    override fun saveLocalDB() {
        addCardRepo.localDBsave()
    }
    fun statusVM():LiveData<NetworkStatus>{
        livestatusVM=addCardRepo.ldStatus()
        return livestatusVM
    }

    fun colorVM(cardModel:CardModel, cardColorModel: CardColorModel){
        addCardRepo.ColorRepo(cardModel,cardColorModel)
    }
}