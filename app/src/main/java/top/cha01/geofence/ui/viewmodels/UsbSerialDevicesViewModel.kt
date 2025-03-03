package top.cha01.geofence.ui.viewmodels

import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoho.android.usbserial.driver.UsbSerialProber
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.cha01.geofence.data.RcModuleListener
import top.cha01.geofence.data.UsbSerialDevice
import top.cha01.geofence.libs.UsbSerial.CustomProber
import top.cha01.geofence.protobuf.Request

class UsbSerialDevicesViewModel(private val usbManager: UsbManager) : ViewModel(), RcModuleListener {
    private val _usbSerialDevices = MutableStateFlow<List<UsbSerialDevice>>(emptyList())
    val usbSerialDevices: StateFlow<List<UsbSerialDevice>> = _usbSerialDevices.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    private val _serialData = MutableSharedFlow<String>()
    val serialData: StateFlow<String> = _serialData.stateIn(
        viewModelScope, SharingStarted.Lazily, ""
    )

    init {
        viewModelScope.launch {
            _usbSerialDevices.value = scanUsbDevices()
        }
    }

    fun refreshUsbDevices() {
        viewModelScope.launch {
            _usbSerialDevices.value = emptyList()
            _usbSerialDevices.value = scanUsbDevices()
        }
    }

    private fun scanUsbDevices(): List<UsbSerialDevice> {
        val listItems = emptyList<UsbSerialDevice>().toMutableList()
        val usbDefaultProber = UsbSerialProber.getDefaultProber()
        val usbCustomProber = CustomProber.customProber
        for (device in usbManager.deviceList.values) {
            var driver = usbDefaultProber.probeDevice(device)
            if (driver == null) {
                driver = usbCustomProber.probeDevice(device)
            }
            if (driver != null) {
                for (port in driver.ports.indices) listItems.add(
                    UsbSerialDevice(
                        device,
                        port,
                        driver
                    )
                )
            }
        }
        return listItems
    }

    override fun onSerialConnect() {
        TODO("Not yet implemented")
    }

    override fun onSerialConnectError(e: Exception?) {
        TODO("Not yet implemented")
    }

    override fun onSerialRead(data: String) {
        viewModelScope.launch {
            _serialData.emit(data)
        }
    }

    override fun onSerialIoError(e: Exception?) {
        TODO("Not yet implemented")
    }
}
