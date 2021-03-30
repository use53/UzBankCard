package uz.fintech.uzbankcard.navui.twoui.cards

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardColorModel
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.database.HistoryDB
import uz.fintech.uzbankcard.utils.PreferenceManager

class CardsRepo (ctx: Context){

    companion object {
        private var instanse: CardsRepo? = null
        fun homeRepoInstanse(ctx: Context): CardsRepo {
            if (instanse == null) {
                instanse = CardsRepo(ctx)
            } else {
                instanse
            }
            return instanse!!
        }
    }
    private val preference by lazyFast { PreferenceManager.instanse(ctx) }
    private val db by lazyFast { BuilderDB.instanse(ctx) }

    private val ldColor=MutableLiveData<MutableList<CardColorModel>>()
    fun listColor(){
           val list = mutableListOf<CardColorModel>()
        list.add(CardColorModel(Color.GRAY,"kulrang"))
        list.add(CardColorModel(Color.BLUE,"ko'k"))
        list.add(CardColorModel(Color.GREEN,"yashil"))
        list.add(CardColorModel(Color.RED,"qizil"))
        list.add(CardColorModel(Color.WHITE,"oq"))
      ldColor.postValue(list)
    }
    fun ldColor(): MutableLiveData<MutableList<CardColorModel>> {
        return ldColor
    }

    fun MainUpdateCard(cardModel: CardModel){
        preference.isCardSaveBoolean=true
        preference.isCardModelSave=cardModel.cardnum.toString()
    }

    fun cardDelete(cardModel: CardModel){
        db.getCardDao().loadFirst(cardModel.cardnum.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {

            }
            .doOnSuccess {
                historydelete(it)
            }
            .subscribe()
    }

    private fun historydelete(it: HistoryDB) {
        db.getCardDao().delete(it)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}