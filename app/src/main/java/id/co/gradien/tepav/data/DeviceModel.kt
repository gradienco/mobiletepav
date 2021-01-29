package id.co.gradien.tepav.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class DeviceModel (
    @PrimaryKey val mac: String = "0",
    val name: String? = null,
    val user: String? = null,
    val duration: Long = 0
)