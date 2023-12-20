package tn.m1mpdam.trinumbers.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface recordDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecord(record: Record)

    @Query("SELECT * FROM record_table")
    fun getAll(): LiveData<List<Record>>
}