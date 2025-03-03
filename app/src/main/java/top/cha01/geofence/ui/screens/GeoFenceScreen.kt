package top.cha01.geofence.ui.screens

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import top.cha01.geofence.data.database.daos.GeoFenceDao
import top.cha01.geofence.ui.viewmodels.GeoFenceViewModel
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GeoFenceScreen(dao: GeoFenceDao) {
    val viewModel = GeoFenceViewModel(dao)
    val listScreen = GeoFenceListScreen(viewModel)
    val detailScreen = GeoFenceEditScreen(viewModel)

    ListDetailScreen(listScreen.ComposableScreen, detailScreen.ComposableScreen)
}
