package uz.fintech.uzbankcard.navui.twoui.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardColorModel
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.network.NetworkStatus

class CardsViewModel(app:Application) :AndroidViewModel(app){

   private val cardsRepo by lazyFast { CardsRepo.homeRepoInstanse(app.applicationContext) }
    private var ldColorVM=MutableLiveData<MutableList<CardColorModel>>()
    private var cardsStatusVM =MutableLiveData<CardsStatus>()
    private var searchListPayment=MutableLiveData<MutableList<PaymentHistory>>()

    fun colorListVM(){
        cardsRepo.listColor()
        ldColorVM=cardsRepo.ldColor()
    }

    fun ldColorVM():LiveData<MutableList<CardColorModel>>{
        return ldColorVM
    }

    fun updateCardVM(cardModel: CardModel){
        cardsRepo.MainUpdateCard(cardModel)
    }

    fun cardDeleteVM(cardModel: CardModel){
        cardsRepo.cardDelete(cardModel)
    }

   fun ldStatusVM(){
       cardsStatusVM=cardsRepo.ldStatus()

   }

    fun ldStatusCards():LiveData<CardsStatus>{
        return cardsStatusVM
    }

    fun historyListPayment(paymentHistory: String){
        cardsRepo.historyPaymentAllList(paymentHistory)
        searchListPayment=cardsRepo.listSearch()
    }
    fun listSearchVM(): LiveData<MutableList<PaymentHistory>> {
        return searchListPayment
    }

}