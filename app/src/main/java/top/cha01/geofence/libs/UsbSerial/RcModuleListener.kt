package top.cha01.geofence.libs.UsbSerial


interface RcModuleListener {
    fun onSerialConnect()
    fun onSerialConnectError(e: Exception?)
    fun onSerialRead(data: String) // socket -> service
    fun onSerialIoError(e: Exception?)
}
