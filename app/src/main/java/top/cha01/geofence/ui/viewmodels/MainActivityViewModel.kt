package top.cha01.geofence.ui.viewmodels

import android.content.BroadcastReceiver
import com.hoho.android.usbserial.driver.UsbSerialPort
import top.cha01.geofence.MainActivity.Connected
import top.cha01.geofence.libs.UsbSerial.DeviceEndpoints
import top.cha01.geofence.services.SerialService

class MainActivityViewModel {
   var service: SerialService? = null
   private val endpoints = DeviceEndpoints()

   private var broadcastReceiver: BroadcastReceiver? = null
   private var deviceId = 0
   private var portNum = 0
   private var baudRate = 0
   private var usbSerialPort: UsbSerialPort? = null
   private var service: SerialService? = null


   private var connected = Connected.False
   private var initialStart = true
   private val hexEnabled = false
}