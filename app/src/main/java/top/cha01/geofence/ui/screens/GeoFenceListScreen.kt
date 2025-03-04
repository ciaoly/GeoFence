package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow
import top.cha01.geofence.data.database.daos.GeoFenceDao
import top.cha01.geofence.data.database.entities.GeoFence
import top.cha01.geofence.ui.components.GeoFenceListItem
import top.cha01.geofence.ui.viewmodels.GeoFenceViewModel

class GeoFenceListScreen(viewModel: GeoFenceViewModel): BaseComposableListContent<GeoFence, GeoFenceViewModel>(viewModel) {

    constructor(dao: GeoFenceDao): this(GeoFenceViewModel(dao))

    @Composable
    override fun listDataFlowFromViewModal(): StateFlow<List<GeoFence>> {
        return viewModel.geoFences
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun buildItemContent(
        itemData: GeoFence,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isListVisible: Boolean,
        isSelected: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {
        GeoFenceListItem(itemData, isSelected)
    }
}
