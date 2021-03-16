package uz.fintech.uzbankcard.navui.twoui.addcard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.database.HistoryDB
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.PreferenceManager
import java.net.UnknownHostException
import kotlin.system.exitProcess

interface IAddCard{
    val liveAddCard:LiveData<CardModel>
    fun liveCard(cardModel: String)
    val livestatus:LiveData<NetworkStatus>
    fun saveRoom(saveCard:String)

}


class AddCardViewModel(app:Application) :
        AndroidViewModel(app),IAddCard, ValueEventListener {

    private val database by lazyFast { FirebaseDatabase.getInstance() }
    private val lD by lazyFast { BuilderDB.instanse(app.applicationContext) }
    private val preferens by lazyFast { PreferenceManager.instanse(app.applicationContext) }
    override val liveAddCard= MutableLiveData<CardModel>()
    override val livestatus= MutableLiveData<NetworkStatus>()
    private val cd=CompositeDisposable()



    override fun liveCard(cardModel: String) {
        livestatus.postValue(NetworkStatus.Loading)
        val d=database.reference.child("card")
                .orderByChild("cardnum").
        startAt(cardModel).endAt(cardModel+"\uf8ff")
        d.addValueEventListener(this)
    }


    override fun onCancelled(error: DatabaseError) {
          if (error is UnknownHostException){
                 livestatus.postValue(NetworkStatus.Offline())
          }else{
              livestatus.postValue(NetworkStatus.Error(error.message.hashCode()))
          }
    }

    override fun onDataChange(snapshot: DataSnapshot) {
      snapshot.children.forEach {

      val spt=it.getValue(CardModel::class.java)
          livestatus.postValue(NetworkStatus.Success)
          liveAddCard.postValue(spt)

      }
    }

    override fun saveRoom(saveCard: String) {
        if (!preferens.isCardSaveBoolean){
            preferens.isCardModelSave=saveCard
            preferens.isCardSaveBoolean=true
        }
        val historyDB=HistoryDB(cardnumdb = saveCard)
        try {
            val disposable=lD.getCardDao().insert(historyDB)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            cd.add(disposable)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}