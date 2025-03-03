package top.cha01.geofence.data


interface RcModuleListener {
    fun onSerialConnect()
    fun onSerialConnectError(e: Exception?)
    fun onSerialRead(data: String) // socket -> service
    fun onSerialIoError(e: Exception?)
}
