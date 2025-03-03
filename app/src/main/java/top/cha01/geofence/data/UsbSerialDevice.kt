package top.cha01.geofence.data

import android.hardware.usb.UsbDevice
import com.hoho.android.usbserial.driver.UsbSerialDriver
import top.cha01.geofence.ui.screens.BaseListItemType

class UsbSerialDevice(
    var device: UsbDevice,
    var port: Int,
    var driver: UsbSerialDriver,
    override var Id: Int = port
): BaseListItemType