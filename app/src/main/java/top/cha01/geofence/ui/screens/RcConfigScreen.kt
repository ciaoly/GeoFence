package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.libs.UsbSerial.DeviceEndpoints
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RcConfigScreen(dao: RcConfigDao, endpoints: DeviceEndpoints) {
    val viewModel = RcConfigViewModel(dao, endpoints)
    val listScreen = RcConfigListScreen(viewModel)
    val detailScreen = RcConfigEditScreen(viewModel)

    val list: listContentType = {
        modifier,
        onIndexClick,
        isListAndDetailVisible,
        isListVisible,
        sharedTransitionScope,
        animatedVisibilityScope
        ->
            listScreen.ComposableScreen(
                onAdd = {},
                modifier = modifier,
                onItemClick = { index, itemId ->
                    viewModel.selectConfig(itemId)
                },
                isListAndDetailVisible = isListAndDetailVisible,
                isListVisible = isListVisible,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
    }

    val detail: detailContentType = {
        modifier,
        isListAndDetailVisible,
        isDetailVisible,
        sharedTransitionScope,
        animatedVisibilityScope
        ->
            detailScreen.ComposableScreen(
                selectedId = viewModel.configId,
                modifier,
                isListAndDetailVisible,
                isDetailVisible,
                sharedTransitionScope,
                animatedVisibilityScope
            )
    }

    ListDetailScreen(
        listContent = list,
        detailContent = detail
    )
}
