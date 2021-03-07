package uz.fintech.uzbankcard.navui.twoui.addcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.fintech.uzbankcard.model.CardModel

interface IAddCard{
    val liveAddCard:LiveData<CardModel>
    fun liveCard(cardModel: CardModel)
}

class AddCardViewModel(app:Application) :AndroidViewModel(app),IAddCard{
    override val liveAddCard= MutableLiveData<CardModel>()

    override fun liveCard(cardModel: CardModel) {

    }


}