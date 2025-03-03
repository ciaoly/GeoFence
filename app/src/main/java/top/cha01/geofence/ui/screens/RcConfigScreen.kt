package top.cha01.geofence.ui.screens

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RcConfigScreen(dao: RcConfigDao) {
    val viewModel = RcConfigViewModel(dao)
    val listScreen = RcConfigListScreen(viewModel)
    val detailScreen = RcConfigEditScreen(viewModel)

    ListDetailScreen(listScreen.ComposableScreen, detailScreen.ComposableScreen)
}
