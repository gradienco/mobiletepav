package id.co.gradien.tepav.data

data class DeviceModel (
    var id: String? = null,
    val name: String? = null,
    val mac: String? = null,
    val user: String? = null,
    val duration: String = "0"
)