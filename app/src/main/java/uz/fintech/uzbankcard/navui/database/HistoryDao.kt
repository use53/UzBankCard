package uz.fintech.uzbankcard.navui.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(it:HistoryDB): Completable

    @Query("select * from HistoryDB")
    fun loadAll(): Single<List<HistoryDB>>

}