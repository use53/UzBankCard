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
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.PreferenceManager

interface IReportVM {

    fun loadItemRt(cardnumber: String)
    fun updateMoney(money: String, oldmoney: String)
}

class ReportViewModel(app: Application) : AndroidViewModel(app),
    IReportVM {


    private val reportRepo by lazyFast {
        ReportRepo.ReportRepoInstanse(app.applicationContext)
    }
    private var loadCardVM = MutableLiveData<CardModel>()
    private var ldStatusVM = MutableLiveData<NetworkStatus>()

    override fun loadItemRt(cardnumber: String) {
        reportRepo.loadItemRt(cardnumber)
        loadCardVM = reportRepo.ldCardModel()
    }

    fun ldcardModelVM(): LiveData<CardModel> {
        return loadCardVM
    }

    override fun updateMoney(money: String, oldmoney: String) {
        reportRepo.updateMoneyRepo(money, oldmoney)
    }

    fun ldStatus(): LiveData<NetworkStatus> {
        ldStatusVM = reportRepo.ldStatus()
        return ldStatusVM
    }


}

