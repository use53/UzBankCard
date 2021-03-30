package uz.fintech.uzbankcard.navui.twoui.addcard

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardColorModel
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.database.HistoryDB
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.PreferenceManager

class AddCardRepo(ctx: Context) {

    companion object {
        private var instanse: AddCardRepo? = null
        fun homeRepoInstanse(ctx: Context): AddCardRepo {
            if (instanse == null) {
                instanse = AddCardRepo(ctx)
            } else {
                instanse
            }
            return instanse!!
        }
    }

    private val lD by lazyFast { BuilderDB.instanse(ctx) }
    private val firebaseDB by lazyFast { FirebaseDatabase.getInstance() }
    private val ldFirebase = MutableLiveData<CardModel>()
    private var text: String? = null
    private val preferens by lazyFast { PreferenceManager.instanse(ctx) }
    private var livestatus = MutableLiveData<NetworkStatus>()

    fun searchRepo(text: String) {
        livestatus.postValue(NetworkStatus.Loading)
        this.text = text
        firebaseDB.reference.child("card")
            .orderByChild("cardnum").startAt(text).endAt(text + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                  livestatus.postValue(NetworkStatus.Error(error.code))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val spt = it.getValue(CardModel::class.java)
                        if (spt != null) {
                            ldFirebase.postValue(spt!!)
                            livestatus.postValue(NetworkStatus.Success)
                        } else {
                       livestatus.postValue(NetworkStatus.Offline())
                        }

                    }
                }
            })
    }

    fun ldsearch():MutableLiveData<CardModel>{
        return ldFirebase

    }

    fun localDBsave(){
        if (!preferens.isCardSaveBoolean){
            preferens.isCardModelSave=text!!
            preferens.isCardSaveBoolean=true
        }
        val historyDB= HistoryDB(cardnumdb = text!!)
        try {
            val disposable=lD.getCardDao().insert(historyDB)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun ldStatus():MutableLiveData<NetworkStatus>{
        return livestatus
    }

    fun ColorRepo(cardModel:CardModel,cardColorModel: CardColorModel){
        firebaseDB.reference.child("card")
            .orderByChild("cardnum").startAt(cardModel.cardnum)
            .endAt(cardModel.cardnum+"\uf8ff")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val map = hashMapOf<String, Long>()
                        map.put("color",cardColorModel.colors.toLong())
                        firebaseDB.reference.child("card").child(it.ref.key!!)
                            .updateChildren(map as Map<String, Any>)
                    }
                }
            })
    }
}