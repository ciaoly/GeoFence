package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import top.cha01.geofence.data.UsbSerialDevice
import top.cha01.geofence.ui.viewmodels.UsbSerialDevicesViewModel

class UsbSerialDevicesListScreen(viewModel: UsbSerialDevicesViewModel): BaseComposableListContent<UsbSerialDevice, UsbSerialDevicesViewModel>(viewModel) {
    @Composable
    override fun itemDataFromViewModal(): List<UsbSerialDevice> {
        val usbSerialDevices by viewModel.usbSerialDevices.collectAsState(initial = emptyList())
        return usbSerialDevices
    }

    @ExperimentalSharedTransitionApi
    @Composable
    override fun buildItemContent(
        itemId: UsbSerialDevice,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isListVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {

    }

}