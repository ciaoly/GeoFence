package top.cha01.geofence.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.data.database.entities.RcConfig
import top.cha01.geofence.libs.UsbSerial.DeviceEndpoints
import top.cha01.geofence.protobuf.HighLow
import top.cha01.geofence.protobuf.Protocol
import top.cha01.geofence.protobuf.RepeatTimes
import top.cha01.geofence.protobuf.Request
import top.cha01.geofence.protobuf.Response
import top.cha01.geofence.protobuf.SendIntegration

class RcConfigViewModel(private val dao: RcConfigDao, private val endpoints: DeviceEndpoints) : ViewModel() {
    val serialData: SharedFlow<Response?> = endpoints.readEndpoint.stateIn(
        viewModelScope, SharingStarted.Lazily, null
    )

    val rcConfigList: StateFlow<List<RcConfig>> = dao.getAllRcConfigs().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    private var selectedConfig: RcConfig? = null

    var configId by mutableStateOf(selectedConfig?.Id ?: -1)
    var name by mutableStateOf(selectedConfig?.Name ?: "")
    var memo by mutableStateOf(selectedConfig?.Memo ?: "")
    var createDate by mutableStateOf(selectedConfig?.CreateDate ?: "")
    var pulseLength by mutableStateOf(selectedConfig?.PulseLength ?: 350)
    var syncBitHigh by mutableStateOf(selectedConfig?.SyncBitHigh ?: 1)
    var syncBitLow by mutableStateOf(selectedConfig?.SyncBitLow ?: 31)
    var zeroBitHigh by mutableStateOf(selectedConfig?.ZeroBitHigh ?: 1)
    var zeroBitLow by mutableStateOf(selectedConfig?.ZeroBitLow ?: 3)
    var oneBitHigh by mutableStateOf(selectedConfig?.OneBitHigh ?: 3)
    var oneBitLow by mutableStateOf(selectedConfig?.OneBitLow ?: 1)
    var invertedSignal by mutableStateOf(selectedConfig?.InvertedSignal ?: false)
    var repeatTimes by mutableStateOf(selectedConfig?.RepeatTimes ?: 3)
    var repeatWaitTime by mutableStateOf(selectedConfig?.RepeatWaitTime ?: 0)
    var code by mutableStateOf(selectedConfig?.Code ?: "")
    var revertCode by mutableStateOf(selectedConfig?.RevertCode ?: "")

    var nameError by mutableStateOf<String?>(null)
    var codeError by mutableStateOf<String?>(null)

    fun toRcConfig(): RcConfig {
        return RcConfig(
            Name = name,
            Memo = memo,
            CreateDate = createDate,
            PulseLength = pulseLength,
            SyncBitHigh = syncBitHigh,
            SyncBitLow = syncBitLow,
            ZeroBitHigh = zeroBitHigh,
            ZeroBitLow = zeroBitLow,
            OneBitHigh = oneBitHigh,
            OneBitLow = oneBitLow,
            InvertedSignal = invertedSignal,
            RepeatTimes = repeatTimes,
            RepeatWaitTime = repeatWaitTime,
            Code = code,
            RevertCode = revertCode
        )
    }

    fun selectConfig(id: Int) {
        val config = rcConfigList.value.find { it.Id == id }
        selectConfig(config ?: return)
    }

    fun selectConfig(config: RcConfig) {
        selectedConfig = config
        configId = selectedConfig?.Id ?: -1
        name = selectedConfig?.Name ?: ""
        memo = selectedConfig?.Memo ?: ""
        createDate = selectedConfig?.CreateDate ?: ""
        pulseLength = selectedConfig?.PulseLength ?: 350
        syncBitHigh = selectedConfig?.SyncBitHigh ?: 1
        syncBitLow = selectedConfig?.SyncBitLow ?: 31
        zeroBitHigh = selectedConfig?.ZeroBitHigh ?: 1
        zeroBitLow = selectedConfig?.ZeroBitLow ?: 3
        oneBitHigh = selectedConfig?.OneBitHigh ?: 3
        oneBitLow = selectedConfig?.OneBitLow ?: 1
        invertedSignal = selectedConfig?.InvertedSignal ?: false
        repeatTimes = selectedConfig?.RepeatTimes ?: 3
        repeatWaitTime = selectedConfig?.RepeatWaitTime ?: 0
        code = selectedConfig?.Code ?: ""
        revertCode = selectedConfig?.RevertCode ?: ""
    }

    fun validate(): Boolean {
        var isValid = true

        if (name.isBlank()) {
            nameError = "名称不能为空"
            isValid = false
        } else {
            nameError = null
        }

        if (code.isBlank()) {
            codeError = "代码不能为空"
            isValid = false
        } else if (!code.matches(Regex("^[01]+$"))) {
            codeError = "代码只能包含 0 和 1"
            isValid = false
        } else {
            codeError = null
        }

        return isValid
    }

    fun addRcConfig(rcConfig: RcConfig) {
        viewModelScope.launch {
            dao.insert(rcConfig)
        }
    }

    fun deleteRcConfig(rcConfig: RcConfig) {
        viewModelScope.launch {
            dao.delete(rcConfig)
        }
    }

    fun barrierUp() {
        val config = selectedConfig ?: return
        barrierUp(config)
    }
    fun barrierUp(id: Int) {
        val config = rcConfigList.value.find { it.Id == id }
        barrierUp(config ?: return)
    }
    fun barrierUp(rcConfig: RcConfig) {
        selectConfig(rcConfig)
        val proto = Protocol.newBuilder()
            .setPulselength(rcConfig.PulseLength)
            .setSyncFactor(HighLow.newBuilder().setHigh(rcConfig.SyncBitHigh).setLow(rcConfig.SyncBitLow).build())
            .setOne(HighLow.newBuilder().setHigh(rcConfig.OneBitHigh).setLow(rcConfig.OneBitLow).build())
            .setZero(HighLow.newBuilder().setHigh(rcConfig.ZeroBitHigh).setLow(rcConfig.ZeroBitLow).build())
            .setInvertedSignal(rcConfig.InvertedSignal)
            .build()
        val sendIntegration = SendIntegration.newBuilder()
            .setCode(rcConfig.Code)
            .setProtocol(proto)
            .setRepeatTimes(RepeatTimes.newBuilder().setTimes(rcConfig.RepeatTimes).build())
            .build()
        val request = Request.newBuilder()
            .setMethod(Request.Method.SEND_INTEGRATION)
            .setSendIntegration(sendIntegration)
            .build()
        writeSerial(request)
    }

    fun barrierDown() {
        val config = selectedConfig ?: return
        barrierDown(config)
    }
    fun barrierDown(id: Int) {
        val config = rcConfigList.value.find { it.Id == id }
        barrierDown(config ?: return)
    }
    fun barrierDown(rcConfig: RcConfig) {
        selectConfig(rcConfig)
        val proto = Protocol.newBuilder()
            .setPulselength(rcConfig.PulseLength)
            .setSyncFactor(HighLow.newBuilder().setHigh(rcConfig.SyncBitHigh).setLow(rcConfig.SyncBitLow).build())
            .setOne(HighLow.newBuilder().setHigh(rcConfig.OneBitHigh).setLow(rcConfig.OneBitLow).build())
            .setZero(HighLow.newBuilder().setHigh(rcConfig.ZeroBitHigh).setLow(rcConfig.ZeroBitLow).build())
            .setInvertedSignal(rcConfig.InvertedSignal)
            .build()
        val sendIntegration = SendIntegration.newBuilder()
            .setCode(rcConfig.RevertCode)
            .setProtocol(proto)
            .setRepeatTimes(RepeatTimes.newBuilder().setTimes(rcConfig.RepeatTimes).build())
            .build()
        val request = Request.newBuilder()
            .setMethod(Request.Method.SEND_INTEGRATION)
            .setSendIntegration(sendIntegration)
            .build()
        writeSerial(request)
    }

    fun writeSerial(request: Request) {
        viewModelScope.launch {
            endpoints.writeData(request)
        }
    }
}
