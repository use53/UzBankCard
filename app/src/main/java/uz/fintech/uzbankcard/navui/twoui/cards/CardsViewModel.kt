package uz.fintech.uzbankcard.navui.twoui.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardColorModel
import uz.fintech.uzbankcard.model.CardModel

class CardsViewModel(app:Application) :AndroidViewModel(app){

   private val cardsRepo by lazyFast { CardsRepo.homeRepoInstanse(app.applicationContext) }
    private var ldColorVM=MutableLiveData<MutableList<CardColorModel>>()
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


}