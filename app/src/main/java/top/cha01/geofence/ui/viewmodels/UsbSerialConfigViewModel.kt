package top.cha01.geofence.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UsbSerialConfigViewModel : ViewModel() {
    var baudRate by mutableStateOf(9600)

    var dataBits by mutableStateOf(8)

    var parity by mutableStateOf("None")

    var stopBits by mutableStateOf(1)

    var flowControl by mutableStateOf("None")

    fun saveConfig() {
        // 在这里实现保存配置的逻辑，例如保存到 SharedPreferences 或数据库
        println("保存的配置：$baudRate, $dataBits, $parity, $stopBits, $flowControl")
    }
}