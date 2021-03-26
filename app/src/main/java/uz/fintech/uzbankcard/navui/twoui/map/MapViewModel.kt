package uz.fintech.uzbankcard.navui.twoui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.MapModel

interface IMapVH{
    val listItem:LiveData<MutableList<MapModel>>
    fun loadMapItem()
}

class MapViewModel(app:Application)
    :AndroidViewModel(app),IMapVH, ValueEventListener {
    private var isEnableMap=false
    private val firebaseDB by lazyFast { FirebaseDatabase.getInstance().reference }
    override val listItem= MutableLiveData<MutableList<MapModel>>()
     private val list by lazyFast { mutableListOf<MapModel>() }
    override fun loadMapItem() {
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


}