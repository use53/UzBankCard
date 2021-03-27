package uz.fintech.uzbankcard.navui.twoui.report

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
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.PreferenceManager

class ReportRepo(ctx:Context){

    companion object {
        private var instanse: ReportRepo? = null
        fun ReportRepoInstanse(ctx: Context): ReportRepo {
            if (instanse == null) {
                instanse = ReportRepo(ctx)
            } else {
                instanse
            }
            return instanse!!
        }
    }

    private val preferense by lazyFast {
        PreferenceManager.instanse(ctx)
    }

    private val firebaseDb by lazyFast {
        FirebaseDatabase.getInstance().reference.child("card")

    }

    private val builderDb by lazyFast {
        BuilderDB.instanse(ctx)
    }

    private val ldcardModel=MutableLiveData<CardModel>()
    private val status=MutableLiveData<NetworkStatus>()

    fun loadItemRt(cardnumber: String){
        val searchNumber = firebaseDb.
        orderByChild("cardnum").startAt(cardnumber).endAt(cardnumber + "\uf8ff")
        searchNumber.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                status.postValue(NetworkStatus.Error(error.code))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val snp=it.getValue(CardModel::class.java)
                    ldcardModel.postValue(snp!!)
                }
            }

        })

    }

    fun ldCardModel(): MutableLiveData<CardModel> {
        return ldcardModel
    }

    fun updateMoneyRepo(money: String,oldmoney: String){
        status.postValue(NetworkStatus.Loading)
        val saveNumber = preferense.isCardModelSave
        val  query=firebaseDb.orderByChild("cardnum").
        startAt(saveNumber).endAt(saveNumber+"\uf8ff")
        val queryPlus=firebaseDb.orderByChild("cardnum")
            .startAt(oldmoney).endAt(oldmoney+"\uf8ff")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
              status.postValue(NetworkStatus.Error(error.code))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val snp=it.getValue(CardModel::class.java)
                    if (snp!=null && snp.cardnum==preferense.isCardModelSave){
                        val minusM=snp.money
                        val moneyMin=(minusM-money.toLong())
                        val maps = hashMapOf<String, Long>()
                        maps.put("money",moneyMin)
                        firebaseDb.child(it.ref.key!!).updateChildren(maps as Map<String, Any>)
                        status.postValue(NetworkStatus.Success)
                        builderDb.paymentdao()
                            .insertPt(
                                PaymentHistory(snp.cardname.toString()
                                ,money.toLong(),"2020-12-01")
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()
                    }
                }
            }
        })
        queryPlus.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                status.postValue(NetworkStatus.Error(error.code))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val snp=it.getValue(CardModel::class.java)
                    if (snp!=null && snp.cardnum==oldmoney){
                        val plus=snp.money
                        val moniyMax=(plus+money.toLong())
                        val maps = hashMapOf<String, Long>()
                        maps.put("money",moniyMax)
                        firebaseDb.child(it.ref.key!!).updateChildren(maps as Map<String, Any>)

                    }
                }
            }
        })
    }
    fun ldStatus(): MutableLiveData<NetworkStatus> {
        return status
    }
}