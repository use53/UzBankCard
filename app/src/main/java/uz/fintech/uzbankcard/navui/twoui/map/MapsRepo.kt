package uz.fintech.uzbankcard.navui.twoui.map

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.MapModel
import uz.fintech.uzbankcard.navui.twoui.payments.PaymentRepo

class MapsRepo (ctx:Context): ValueEventListener {

    companion object {
        private var instanse: MapsRepo? = null
        fun paymentRepoInstanse(ctx: Context): MapsRepo {
            if (instanse == null) {
                instanse = MapsRepo(ctx)
            } else {
                instanse
            }
            return instanse!!
        }
    }

     private var isEnableMap=false
    private val firebaseDB by lazyFast { FirebaseDatabase.getInstance().reference }
    private val listItem= MutableLiveData<MutableList<MapModel>>()
    private val list by lazyFast { mutableListOf<MapModel>() }

    fun loadMapItem(){
        if (!isEnableMap){
            val db= firebaseDB.child("maps")
            db.addListenerForSingleValueEvent(this)
            isEnableMap=true
        }
    }

    override fun onCancelled(error: DatabaseError) {

    }

    override fun onDataChange(snapshot: DataSnapshot) {
        snapshot.children.forEach {

            val snp=it.getValue(MapModel::class.java)
            list.add(snp!!)
        }
        listItem.postValue(list)
    }
    fun ldListMap(): MutableLiveData<MutableList<MapModel>> {
        return listItem
    }
}