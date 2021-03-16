package uz.fintech.uzbankcard.navui.twoui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.twoui.home.HomeDB
import uz.fintech.uzbankcard.utils.PreferenceManager

interface IReportVM{
    val loadCard:LiveData<CardModel>
    fun loadItemRt(cardnumber:String)
}
class ReportViewModel (app:Application):AndroidViewModel(app),
        IReportVM, ValueEventListener {

    private val db by lazyFast {
        BuilderDB.instanse(app)
    }
    private val preferense by lazyFast {
        PreferenceManager.instanse(app)
    }

    private val firebaseDb by lazyFast {
        FirebaseDatabase.getInstance().reference.child("card") }
    override val loadCard= MutableLiveData<CardModel>()
    override fun loadItemRt(cardnumber: String) {
        val query=firebaseDb.orderByChild("cardnum").startAt(cardnumber).endAt(cardnumber+"\uf8ff")
        query.addValueEventListener(this)
    }

    override fun onCancelled(error: DatabaseError) {

    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val d=snapshot.children
                .forEach {
                    val maps= hashMapOf<String,String>()
                    maps.put("cardname","Asaka bank")
                    firebaseDb.child( it.ref.key!!).updateChildren(maps as Map<String, Any>)
                }



    }



}

