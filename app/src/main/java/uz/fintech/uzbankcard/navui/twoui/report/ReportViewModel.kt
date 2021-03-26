package uz.fintech.uzbankcard.navui.twoui.report

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.utils.PreferenceManager

interface IReportVM {
    val loadCard: LiveData<CardModel>
    fun loadItemRt(cardnumber: String)
    fun updateMoney(money:String,oldmoney:String)
}

class ReportViewModel(app: Application) : AndroidViewModel(app),
        IReportVM {

    private val preferense by lazyFast {
        PreferenceManager.instanse(app)
    }

    private val firebaseDb by lazyFast {
        FirebaseDatabase.getInstance().reference.child("card")

    }
    private val builderDb by lazyFast {
        BuilderDB.instanse(app)
    }

    override val loadCard = MutableLiveData<CardModel>()

    override fun loadItemRt(cardnumber: String) {
        val searchNumber = firebaseDb.orderByChild("cardnum").startAt(cardnumber).endAt(cardnumber + "\uf8ff")
        searchNumber.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val snp=it.getValue(CardModel::class.java)
                    loadCard.postValue(snp)
                }
            }

        })
}
    override fun updateMoney(money: String,oldmoney: String) {
        val saveNumber = preferense.isCardModelSave
        val  query=firebaseDb.orderByChild("cardnum").
        startAt(saveNumber).endAt(saveNumber+"\uf8ff")
          val queryPlus=firebaseDb.orderByChild("cardnum")
              .startAt(oldmoney).endAt(oldmoney+"\uf8ff")
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

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
                       builderDb.paymentdao()
                           .insertPt(PaymentHistory(snp.cardname.toString()
                           ,money.toLong(),"2020-12-01"))
                           .subscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread())
                           .subscribe()
                   }
               }
            }
        })
        queryPlus.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

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


}

