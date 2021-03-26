package uz.fintech.uzbankcard.navui.twoui.payments

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.PaymentModel
import uz.fintech.uzbankcard.network.StatusLoading

interface IPaymentVH {
    fun ItemPayment()
}


class PaymentViewMOdel(app: Application) :
    AndroidViewModel(app), IPaymentVH {
  private var loadPaymentVM = MutableLiveData<MutableList<PaymentModel>>()
   private val paymentRepo by lazyFast { PaymentRepo.paymentRepoInstanse(app.applicationContext) }
    private var statusVM = MutableLiveData<StatusLoading>()

    override fun ItemPayment() {
       paymentRepo.paymentItim()
        loadPaymentVM=paymentRepo.ldPayment()
    }
    fun ldpaymentVM():LiveData<MutableList<PaymentModel>>{
   return loadPaymentVM
    }

  fun paymentMoney(money:String,name:String){
      paymentRepo.paymentUpdate(money,name)
  }

    fun statusVM():LiveData<StatusLoading>{
        statusVM=paymentRepo.statusRepo()
        return  statusVM
    }
}