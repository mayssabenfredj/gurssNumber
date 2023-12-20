package tn.m1mpdam.trinumbers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordDatabase: RoomDatabase() {
    abstract fun recordDAO(): recordDAO

    companion object{
        @Volatile
        private var INSTANCE: RecordDatabase? = null

        fun getDatabase(context: Context): RecordDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
           synchronized(this){
               val instance = Room.databaseBuilder(
                   context.applicationContext,RecordDatabase::class.java,
                   "record_table"
               ).build()
               INSTANCE = instance
               return instance
           }
        }
    }
}