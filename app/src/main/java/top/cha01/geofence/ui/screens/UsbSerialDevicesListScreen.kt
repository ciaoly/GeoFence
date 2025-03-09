package top.cha01.geofence.ui.screens

import android.hardware.usb.UsbManager
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow
import top.cha01.geofence.data.UsbSerialDevice
import top.cha01.geofence.libs.UsbSerial.DeviceEndpoints
import top.cha01.geofence.ui.viewmodels.UsbSerialDevicesViewModel

class UsbSerialDevicesListScreen(viewModel: UsbSerialDevicesViewModel): BaseComposableListContent<UsbSerialDevice, UsbSerialDevicesViewModel>(viewModel) {


    @Composable
    override fun listDataFlowFromViewModal(): StateFlow<List<UsbSerialDevice>> {
        return viewModel.usbSerialDevices
    }

    @ExperimentalSharedTransitionApi
    @Composable
    override fun buildItemContent(
        itemData: UsbSerialDevice,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isListVisible: Boolean,
        isSelected: Boolean,
        sharedTransitionScope: SharedTransitionScope?,
        animatedVisibilityScope: AnimatedVisibilityScope?
    ) {
        TODO("Not yet implemented")
    }

}

@Composable
fun UsbSerialScreen(usbManager: UsbManager, endpoints: DeviceEndpoints) {
    val viewModel = UsbSerialDevicesViewModel(usbManager, endpoints)
    val actionButtonIcon = { @Composable { Icon(Icons.Filled.Settings, contentDescription = "串口设置") } }


}