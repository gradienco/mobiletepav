package id.co.gradien.tepav.data

data class PacketModel (
        var id: String? = null,
        val device: String? = null,
        val receiveTime: String? = null,
        val cleaningTime: String? = null,
        val sterilizedTime: String? = null,
        val status: String? = null
)