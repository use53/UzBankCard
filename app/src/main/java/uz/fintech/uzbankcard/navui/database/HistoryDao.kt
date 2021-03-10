package uz.fintech.uzbankcard.navui.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface HistoryDao {

  /*  @Insert
    fun insert(model: HistoryDB)*/

    @Query("select * from HistoryDB")
    fun loadAll(): Single<List<HistoryDB>>

}