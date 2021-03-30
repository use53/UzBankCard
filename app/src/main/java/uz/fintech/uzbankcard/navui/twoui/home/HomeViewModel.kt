package uz.fintech.uzbankcard.navui.twoui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.network.NetworkStatus


interface IHomeVH {
    val loadHomeApi: LiveData<MutableList<BarEntry>>
    val loadRead: LiveData<MutableList<PaymentHistory>>
    fun readApiSerVM()
    fun historyRead()
}

class HomeViewModel(val app: Application) : AndroidViewModel(app), IHomeVH {

    override val loadHomeApi = MutableLiveData<MutableList<BarEntry>>()
    override var loadRead = MutableLiveData<MutableList<PaymentHistory>>()
    private val homeRepo by lazyFast { HomeRepo.homeRepoInstanse(app.applicationContext) }
    private var dbroomld = MutableLiveData<MutableList<CardModel>>()
    private var apilivedata = MutableLiveData<BarData>()
    private var statusVH = MutableLiveData<NetworkStatus>()

    override fun readApiSerVM() {
    // homeRepo.restApi()
        apilivedata = homeRepo.apibardata
    }


    fun statusVM() {
        statusVH = homeRepo.status()
    }

    fun statusLiveData(): LiveData<NetworkStatus> {
        return statusVH
    }

    fun responsApi(): LiveData<BarData> {
        return apilivedata
    }

    override fun historyRead() {
        homeRepo.paymentDb()
        loadRead = homeRepo.paymentLiveData()
    }

    fun paymentLoad(): LiveData<MutableList<PaymentHistory>> {
        return loadRead
    }

    fun dbReadVM() {
        homeRepo.dBread()
        dbroomld = homeRepo.historyLivedata()
    }

    fun dbList(): LiveData<MutableList<CardModel>> {
        return dbroomld
    }

}
