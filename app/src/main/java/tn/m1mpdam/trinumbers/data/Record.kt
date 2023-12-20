package tn.m1mpdam.trinumbers.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record_table")
data class Record(
    @PrimaryKey(autoGenerate = true) val recordID: Int,
    @ColumnInfo(name = "recordDate") val date: String?,
    @ColumnInfo(name = "recordTime") val time: String?,

)
