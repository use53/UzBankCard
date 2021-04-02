package uz.fintech.uzbankcard.navui.twoui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import uz.fintech.uzbankcard.adapter.SavePaymentAdapter
import uz.fintech.uzbankcard.api.ICurrencieApi
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardColorModel
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
    private val networkstatus = MutableLiveData<NetworkStatus>()
    private val touchcallbac= MutableLiveData<ItemTouchHelper>()
    private val paymentLivedata = MutableLiveData<MutableList<PaymentHistory>>()
    fun dBread() {
        Handler().postDelayed({
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
        }, 1_000)

    }

    fun paymentDb() {
        val list = mutableListOf<PaymentHistory>()
        db.paymentdao().loadAllPt()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                networkstatus.postValue(NetworkStatus.Error(it.message!!.length))
            }
            .doOnSuccess {
                if (it.isNotEmpty())
                    paymentLivedata.postValue(it)
                else {
                    list.add(PaymentHistory("Malumotlar", 0L, ""))
                    paymentLivedata.postValue(list)
                }
            }
            .subscribe()
    }

    fun status(): MutableLiveData<NetworkStatus> {
        return networkstatus
    }

    fun paymentLiveData(): MutableLiveData<MutableList<PaymentHistory>> {
        return paymentLivedata
    }

    fun readHistory(historyList: List<HistoryDB>?) {
        val roomList = mutableListOf<CardModel>()
        if (historyList != null) {
            networkstatus.postValue(NetworkStatus.Loading)

            firebaseDB.child("card")
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            networkstatus.postValue(NetworkStatus.Error(error.code))
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!historyList.isEmpty()) {
                                networkstatus.postValue(NetworkStatus.Success)
                                snapshot.children.forEach {
                                    val snp = it.getValue(CardModel::class.java)
                                    historyList.forEach {
                                        if (it.cardnumdb == snp!!.cardnum) {
                                         roomList.add(snp)
                                        }
                                    }

                                }
                            } else {
                                networkstatus.postValue(NetworkStatus.Success)
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
            .subscribe({ responsApi(it) }, { networkstatus.postValue(NetworkStatus.Error(it.hashCode())) })

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
        }
        val barDataSet = BarDataSet(chartlist, "valyuta")
        val bardata = BarData()
        bardata.addDataSet(barDataSet)
        apibardata.postValue(bardata)
    }


    @SuppressLint("CheckResult")
    fun onClickCardColor(cardModel: CardModel,
                         cardColorModel:
                         CardColorModel){


        firebaseDB.child("card")
            .orderByChild("cardnum")
            .startAt(cardModel.cardnum)
            .endAt(cardModel.cardnum+"\uf8ff")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val map = hashMapOf<String, Long>()
                        map.put("color",cardColorModel.colors.toLong())
                        firebaseDB.child("card").child(it.ref.key!!)
                            .updateChildren(map as Map<String, Any>)
                    }
                }
            })
    }

    fun onRecyclerviewCallback(adapter:SavePaymentAdapter,icon:Drawable){
        val callback=object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                  direction: Int) {
               val pos=viewHolder.adapterPosition
                adapter.updateList(pos)
                val deleteItem = adapter.getWordAtPosition(pos)
                if (pos.equals(0)) {
                    historyItemDelete(deleteItem)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                 val correntView=viewHolder.itemView
                val iconMagin=(correntView.height-icon.intrinsicHeight)/2
                val iconTop=correntView.top-iconMagin
                val iconBottom=iconTop+icon.intrinsicHeight
                if (dX>0){
                    val iconLeft=correntView.left+iconMagin
                    val iconRight=iconLeft+icon.intrinsicHeight
                    icon.setBounds(iconLeft,iconTop,iconRight,iconBottom)
                }else if (dX<0){

                }
                else{
                    icon.setBounds(0,0,0,0)
                }
                icon.draw(c)
            }
        }
        val helper=ItemTouchHelper(callback)
        touchcallbac.postValue(helper)
    }

    fun ldtouchHelper(): MutableLiveData<ItemTouchHelper> {
        return touchcallbac
    }

   private fun historyItemDelete(paymentHistory: PaymentHistory) {
         db.paymentdao().deleteItem(paymentHistory)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

}



