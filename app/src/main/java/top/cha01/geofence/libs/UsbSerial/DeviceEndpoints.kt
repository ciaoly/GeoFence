package top.cha01.geofence.libs.UsbSerial

import de.kai_morich.simple_usb_terminal.SerialListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import top.cha01.geofence.protobuf.Request
import top.cha01.geofence.protobuf.Response

class DeviceEndpoints {
    private val _readEndpoint = MutableSharedFlow<Response>()
    private val _writeEndpoint = MutableSharedFlow<Request>()

    val readEndpoint: SharedFlow<Response> = _readEndpoint.asSharedFlow()
    val writeEndpoint: SharedFlow<Request> = _writeEndpoint.asSharedFlow()

    suspend fun writeData(request: Request) {
        _writeEndpoint.emit(request)
    }

    suspend fun onSerialRead(response: Response) {
        _readEndpoint.emit(response)
    }
}