package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.data.database.entities.RcConfig
import top.cha01.geofence.libs.UsbSerial.DeviceEndpoints
import top.cha01.geofence.ui.components.RcConfigListItem
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

class RcConfigListScreen(viewModel: RcConfigViewModel): BaseComposableListContent<RcConfig, RcConfigViewModel>(viewModel) {

    constructor(dao: RcConfigDao, endpoints: DeviceEndpoints): this(RcConfigViewModel(dao, endpoints)) {    }

    @Composable
    override fun listDataFlowFromViewModal(): StateFlow<List<RcConfig>> {
        return viewModel.rcConfigList
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun buildItemContent(
        itemData: RcConfig,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isListVisible: Boolean,
        isSelected: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {
        RcConfigListItem(
            itemData,
            { viewModel.barrierUp(itemData) },
            { viewModel.barrierDown(itemData)},
            isSelected
        )
    }

}
