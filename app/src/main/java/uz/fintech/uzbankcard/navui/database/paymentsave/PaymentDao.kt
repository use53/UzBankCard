package uz.fintech.uzbankcard.navui.database.paymentsave

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PaymentDao{

    @Insert
    fun insertPt(payment:PaymentHistory):Completable

    @Query("select * from PaymentHistory")
    fun loadAllPt():Single<MutableList<PaymentHistory>>
}