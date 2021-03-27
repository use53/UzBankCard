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

    fun loadMapItemVM()
}

class MapViewModel(app:Application)
    :AndroidViewModel(app),IMapVH {
    private val mapsRepo by lazyFast { MapsRepo.paymentRepoInstanse(app.applicationContext)
    }

    private var listItemVM=MutableLiveData<MutableList<MapModel>>()
    override fun loadMapItemVM() {
    mapsRepo.loadMapItem()
        listItemVM=mapsRepo.ldListMap()
    }

    fun listItemVM():LiveData<MutableList<MapModel>>{
        return listItemVM
    }



}