package uz.fintech.uzbankcard.navui.twoui.payments

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.model.PaymentModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.network.StatusLoading
import uz.fintech.uzbankcard.utils.PreferenceManager

class PaymentRepo(ctx: Context) {

    companion object {
        private var instanse: PaymentRepo? = null
        fun paymentRepoInstanse(ctx: Context): PaymentRepo {
            if (instanse == null) {
                instanse = PaymentRepo(ctx)
            } else {
                instanse
            }
            return instanse!!
        }
    }

    private var selectBoolean = true
    private val loadPayment = MutableLiveData<MutableList<PaymentModel>>()
    private val firebaseDB by lazyFast {
        FirebaseDatabase
            .getInstance()
            .reference

    }
    private val builderDB by lazyFast { BuilderDB.instanse(ctx) }
    private val preference by lazyFast { PreferenceManager.instanse(ctx) }
    private val statusLoading = MutableLiveData<StatusLoading>()


    fun paymentItim() {
        if (selectBoolean) {
            statusLoading.postValue(StatusLoading.LoadingBigin)
            firebaseDB.child("payment").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    statusLoading.postValue(StatusLoading.Error(error.code))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<PaymentModel>()
                    snapshot.children.forEach {
                        val snp = it.getValue(PaymentModel::class.java)
                        list.add(snp!!)
                        statusLoading.postValue(StatusLoading.Success)
                    }
                    loadPayment.postValue(list)
                }
            })
            selectBoolean = false
       }
    }

    fun ldPayment(): MutableLiveData<MutableList<PaymentModel>> {
        return loadPayment
    }

    fun paymentUpdate(money: String, name: String) {
        if (preference.isCardSaveBoolean){
        statusLoading.postValue(StatusLoading.Loading)
        val query = firebaseDB.child("card").orderByChild("cardnum")
            .startAt(preference.isCardModelSave)
            .endAt(preference.isCardModelSave + "\uf8ff")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
               statusLoading.postValue(StatusLoading.Error(error.code))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val snp = it.getValue(CardModel::class.java)
                    updateMoney(snp, money, name, it)
                }
            }
        })}else{
            statusLoading.postValue(StatusLoading.DeleteCard)
        }
    }

    fun statusRepo(): MutableLiveData<StatusLoading> {
        return statusLoading
    }

    private fun updateMoney(
        snp: CardModel?,
        money: String,
        name: String,
        it: DataSnapshot
    ) {
        if (snp != null) {
            val minus = (snp.money - money.toLong())
            if (minus>0){
            val maps = hashMapOf<String, Long>()
            maps.put("money", minus)
            firebaseDB.child("card").child(it.ref.key!!).updateChildren(maps as Map<String, Any>)
            val querymoney = firebaseDB.child("payment").orderByChild("payname")
                .startAt(name).endAt(name + "\uf8ff")
            querymoney.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                   statusLoading.postValue(StatusLoading.Error(errorMsg =error.code))
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { snp ->
                        val map = hashMapOf<String, Long>()
                        map.put("money", money.toLong())
                        firebaseDB.child("payment").child(snp.ref.key!!)
                            .updateChildren(map as Map<String, Any>)

                         statusLoading.postValue(StatusLoading.SuccessUpdate)

                        val paymentHistory = PaymentHistory(name, money.toLong(), "")
                        builderDB.paymentdao().insertPt(paymentHistory)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()

                    }
                }
            })}else{
                statusLoading.postValue(StatusLoading.SuccessUpdate)
            }
            //
        }
    }
}