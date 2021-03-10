package uz.fintech.uzbankcard.navui.twoui.addcard

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
import uz.fintech.uzbankcard.network.NetworkStatus
import java.net.UnknownHostException

interface IAddCard{
    val liveAddCard:LiveData<CardModel>
    fun liveCard(cardModel: String)
    val livestatus:LiveData<NetworkStatus>
}


class AddCardViewModel(app:Application) :
        AndroidViewModel(app),IAddCard, ValueEventListener {

    val database by lazyFast { FirebaseDatabase.getInstance() }
    override val liveAddCard= MutableLiveData<CardModel>()
    override val livestatus= MutableLiveData<NetworkStatus>()


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


}