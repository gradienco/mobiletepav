package id.co.gradien.tepav.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Database(
    entities = [
        DeviceModel::class
            ],
    version = 1
)


abstract class AppDatabase:RoomDatabase() {

    abstract fun deviceDao() : DeviceDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "tepav.db"

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device ORDER BY name ASC")
    fun getAll(): LiveData<List<DeviceModel>>?

    @Query("SELECT * FROM device WHERE mac = :mac LIMIT 1")
    fun getById(mac: String): LiveData<DeviceModel>?

    @Query("DELETE FROM device")
    fun clear()
}