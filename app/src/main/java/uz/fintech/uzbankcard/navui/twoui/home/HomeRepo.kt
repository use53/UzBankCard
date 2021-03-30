package uz.fintech.uzbankcard.navui.twoui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.fintech.uzbankcard.api.ICurrencieApi
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.model.currencie.CurrencieModel
import uz.fintech.uzbankcard.navui.database.BuilderDB
import uz.fintech.uzbankcard.navui.database.HistoryDB
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.network.NetworkStatus
import java.net.UnknownHostException

@Suppress("DEPRECATION")
class HomeRepo(ctx: Context) {

    companion object {
        private var instanse: HomeRepo? = null
        fun homeRepoInstanse(ctx: Context): HomeRepo {
            if (instanse == null) {
                instanse = HomeRepo(ctx)
            } else {
                instanse
            }
            return instanse!!
        }
    }

    private val db by lazyFast { BuilderDB.instanse(ctx) }
    val listReduser = MutableLiveData<MutableList<CardModel>>()
    private val firebaseDB by lazyFast { FirebaseDatabase.getInstance().reference }
    private val currencies by lazyFast { ICurrencieApi.instanse() }
     val apibardata = MutableLiveData<BarData>()
    private val networkstatus=MutableLiveData<NetworkStatus>()
   private val paymentLivedata=MutableLiveData<MutableList<PaymentHistory>>()
    fun dBread() {
       Handler().postDelayed(Runnable {
           networkstatus.postValue(NetworkStatus.Loading)
           db.getCardDao().loadAll()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .doOnError {
                   networkstatus.postValue(NetworkStatus.Error(it.message!!.length))
               }
               .doOnSuccess {

                   readHistory(it)
               }
               .subscribe()
       },2_000)

    }

    fun paymentDb(){
        val list= mutableListOf<PaymentHistory>()
        db.paymentdao().loadAllPt()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                networkstatus.postValue(NetworkStatus.Error(it.message!!.length))
            }
            .doOnSuccess {
                if (it.isNotEmpty())
                paymentLivedata.postValue(it)
                else{
                    list.add(PaymentHistory("Malumotlar Mavjud Emas",0L,""))
                    paymentLivedata.postValue(list)
                }
            }
            .subscribe()
    }

    fun status():MutableLiveData<NetworkStatus>{
        return  networkstatus
    }

    fun  paymentLiveData():MutableLiveData<MutableList<PaymentHistory>>{
        return paymentLivedata
    }

    fun readHistory(historyList: List<HistoryDB>?) {
        networkstatus.postValue(NetworkStatus.Loading)
        val roomList = mutableListOf<CardModel>()
        if (historyList != null) {

                firebaseDB.child("card")
                    .addValueEventListener(
                        object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                            networkstatus.postValue(NetworkStatus.Error(error.code))
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                //
                                networkstatus.postValue(NetworkStatus.Success)
                                if (!historyList.isEmpty()){
                                snapshot.children.forEach {
                                    val snp = it.getValue(CardModel::class.java)
                                   // if (!historyList.isEmpty()){
                                    historyList.forEach {
                                        if (it.cardnumdb == snp!!.cardnum) {
                                            roomList.add(snp)
                                            networkstatus.postValue(NetworkStatus.Success)
                                        }
                                    }

                                }}
                                else{
                                    roomList.add(CardModel(cardnum = "####################"))
                                }
                                listReduser.postValue(roomList)

                            }

                        }
                    )

        }

    }

    fun historyLivedata(): MutableLiveData<MutableList<CardModel>> {
        return listReduser
    }

    @SuppressLint("CheckResult")
    fun restApi() {
        currencies!!.loadCours()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responsApi(it) }, {
                if (it is UnknownHostException){
                    networkstatus.postValue(NetworkStatus.Offline())
                }else{
                    networkstatus.postValue(NetworkStatus.Error(it.hashCode()))
                }
            })

    }

    private fun responsApi(it: CurrencieModel?) {
        val chartlist = mutableListOf<BarEntry>()
        if (it != null) {
            chartlist.add(BarEntry(1F, it.quotes.uSDEUR.toFloat()))
            chartlist.add(BarEntry(2F, it.quotes.uSDKZT.toFloat()))
            chartlist.add(BarEntry(3F, it.quotes.uSDRUB.toFloat()))
            chartlist.add(BarEntry(4F, it.quotes.uSDUZS.toFloat()))
            chartlist.add(BarEntry(5F, it.quotes.uSDTJS.toFloat()))
            chartlist.add(BarEntry(6F, it.quotes.uSDTMT.toFloat()))
        }else{
            networkstatus.postValue(NetworkStatus.Offline())
        }
        val barDataSet = BarDataSet(chartlist, "valyuta")
        val bardata = BarData()
        bardata.addDataSet(barDataSet)
        apibardata.postValue(bardata)
    }




}