package uz.fintech.uzbankcard.navui.twoui.home
/*

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.database.HistoryDB
import java.util.logging.Handler as Handler1

interface IHomeVH {
    val loadHome: LiveData<MutableList<CardModel>>
    fun readHistory()
}

class HomeViewModel(app: Application) : AndroidViewModel(app)
        , IHomeVH{
    private val builderDB by lazyFast { BuilderDB.instanse(app.applicationContext) }
    override val loadHome = MutableLiveData<MutableList<CardModel>>()
    private val list by lazyFast { mutableListOf<CardModel>() }
    private val firebaseDB by lazy {
         FirebaseDatabase.getInstance().reference.child("card")

     }
    override fun readHistory() {
        builderDB.getCardDao().loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { historylist(null) }
                .doOnSuccess {
                    historylist(it) }
                .subscribe()

    }


    private fun historylist(snp: List<HistoryDB>?) {
        val  newlist= mutableListOf<CardModel>()
       firebaseDB.addValueEventListener(object :ValueEventListener{
           override fun onCancelled(error: DatabaseError) {

           }

           override fun onDataChange(snapshot: DataSnapshot) {
               snapshot.children.forEach {
                   val options=it.getValue(CardModel::class.java)
                      snp!!.forEach {
                          if (it.cardnumdb==options!!.cardnum){
                              list.add(options)
                          }
                      }
                   list.add(options!!)
               }
               if (snp!=null){
               snp.forEach {snk->
                   list.forEach {
                       if(snk.cardnumdb==it.cardnum){
                           newlist.add(it)
                       }
                   }
               }
               }else{
                   newlist.add(CardModel())
               }
               loadHome.postValue(list)
           }
       })

    }
}
*/
