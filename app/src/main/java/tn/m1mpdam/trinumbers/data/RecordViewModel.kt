package tn.m1mpdam.trinumbers.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Record>>
    private val repository: RecordRepository

    init {
        val recordDao = RecordDatabase.getDatabase(application).recordDAO()
        repository = RecordRepository(recordDao)
        readAllData = repository.readAllData
    }

    fun addRecord(record: Record){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecord(record)
        }
    }
}