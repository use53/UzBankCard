package uz.fintech.uzbankcard.navui.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import uz.fintech.uzbankcard.model.CardModel

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(it:HistoryDB): Completable

    @Query("select * from HistoryDB")
    fun loadAll(): Single<List<HistoryDB>>

    @Delete()
    fun delete(user:HistoryDB):Completable

    @Query("select * from HistoryDB where cardnumdb=:cardnum")
    fun loadFirst(cardnum: String): Single<HistoryDB>

}