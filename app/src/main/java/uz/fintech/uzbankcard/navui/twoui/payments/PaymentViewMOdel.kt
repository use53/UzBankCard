package uz.fintech.uzbankcard.navui.twoui.payments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.PaymentModel

interface IPaymentVH{

    val loadPayment:LiveData<MutableList<PaymentModel>>
    fun ItemPayment()
}


class PaymentViewMOdel(app:Application):AndroidViewModel(app),IPaymentVH, ValueEventListener {
    override val loadPayment =MutableLiveData<MutableList<PaymentModel>>()
   private val firebaseDB by lazyFast { FirebaseDatabase
       .getInstance()
       .reference
       .child("payment")}
    override fun ItemPayment() {
        firebaseDB.addValueEventListener(this)
    }

    override fun onCancelled(error: DatabaseError) {

    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val list= mutableListOf<PaymentModel>()
        snapshot.children.forEach {
            val snp=it.getValue(PaymentModel::class.java)
            list.add(snp!!)
         }
        loadPayment.postValue(list)
    }


}