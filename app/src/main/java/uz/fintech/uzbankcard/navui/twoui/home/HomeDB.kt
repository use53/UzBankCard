package uz.fintech.uzbankcard.navui.twoui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.database.HistoryDB


class HomeDB (ctx:Context){

    private val db by lazyFast { BuilderDB.instanse(ctx) }
     val itemList=MutableLiveData<MutableList<CardModel>>()
    private val firebaseDB by lazyFast { FirebaseDatabase.getInstance().reference.child("card") }
      fun loadDb() {
        db.getCardDao().loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    loadHistory(null)
                }
                .doOnSuccess {
                    loadHistory(it)
                }
                .subscribe()
    }

      fun loadHistory(historyDB: List<HistoryDB>?){
        val list= mutableListOf<CardModel>()
        firebaseDB.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                 val snp=it.getValue(CardModel::class.java)
                   if (historyDB!=null){
                       historyDB.forEach {
                           if (it.cardnumdb==snp!!.cardnum){
                               list.add(snp)
                           }
                       }
                   }else{
                       list.add(CardModel("","",""))
                   }
                }
                itemList.postValue(list)

            }
        })

    }





}