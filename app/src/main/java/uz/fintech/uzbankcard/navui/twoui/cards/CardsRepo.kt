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
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.network.NetworkStatus
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
    private val cardsStatus=MutableLiveData<CardsStatus>()
    private val searchList=MutableLiveData<MutableList<PaymentHistory>>()

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
        if (preference.isCardModelSave.length>3){
        preference.isCardSaveBoolean=true
        preference.isCardModelSave=cardModel.cardnum.toString()
            cardsStatus.postValue(CardsStatus.SaveCardSuccess)
        }else{
            //
           cardsStatus.postValue(CardsStatus.SaveCardError)
        }

    }

    fun cardDelete(cardModel: CardModel){
        if (preference.isCardModelSave==cardModel.cardnum){
            preference.isCardModelSave=""
            preference.isCardSaveBoolean=false


        }
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

        //
        cardsStatus.postValue(CardsStatus.DeleteCardSuccess)
    }
    fun ldStatus(): MutableLiveData<CardsStatus> {
        return cardsStatus
    }

    fun historyPaymentAllList(paymentHistory: String){
        db.paymentdao().paymentListAll(paymentHistory)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{

            }
            .doOnSuccess {
                searchList.postValue(it)
            }
            .subscribe()

    }
    fun listSearch(): MutableLiveData<MutableList<PaymentHistory>> {
        return searchList
    }


}

sealed class CardsStatus{
object DeleteCardSuccess:CardsStatus()
object SaveCardSuccess:CardsStatus()
object SaveCardError:CardsStatus()
}