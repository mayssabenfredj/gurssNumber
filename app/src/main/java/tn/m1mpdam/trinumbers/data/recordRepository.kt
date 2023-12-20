package tn.m1mpdam.trinumbers.data

import androidx.lifecycle.LiveData

class RecordRepository(private val recordDao: recordDAO) {
    val readAllData: LiveData<List<Record>> = recordDao.getAll()

    suspend fun addRecord(record: Record){
        recordDao.addRecord(record)
    }
}