package top.cha01.geofence.ui.screens

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.libs.UsbSerial.DeviceEndpoints
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RcConfigScreen(dao: RcConfigDao, endpoints: DeviceEndpoints) {
    val viewModel = RcConfigViewModel(dao, endpoints)
    val listScreen = RcConfigListScreen(viewModel)
    val detailScreen = RcConfigEditScreen(viewModel)
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val list: listContentType = {
        modifier,
        onIndexClick,
        isListAndDetailVisible,
        isListVisible,
        sharedTransitionScope,
        animatedVisibilityScope
        ->
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.selectConfig(-1)
//                    onIndexClick(-1, -1)
                    showSheet = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "添加")
                }
            }
        ) { paddingValues ->
            listScreen.ComposableScreen(
                modifier = modifier.fillMaxWidth().padding(paddingValues),
                onIndexClick = { index, itemId ->
                    viewModel.selectConfig(itemId)
                    onIndexClick(index, itemId)
                },
                isListAndDetailVisible = isListAndDetailVisible,
                isListVisible = isListVisible,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }

    val detail: detailContentType = {
        modifier,
        isListAndDetailVisible,
        isDetailVisible,
        sharedTransitionScope,
        animatedVisibilityScope
        ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(viewModel.name)
                    },
                    navigationIcon = {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "返回")
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.saveRcConfig()
                        }) {
                            Icon(Icons.Filled.Save, contentDescription = "保存")
                        }
                    },
                )
            },
        ) { paddingValues ->
            detailScreen.ComposableScreen(
                modifier.padding(paddingValues),
                isListAndDetailVisible,
                isDetailVisible,
                sharedTransitionScope,
                animatedVisibilityScope
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                detailScreen.ComposableScreen(
                    Modifier,
                    true,
                    false,
                    sharedTransitionScope = null,
                    animatedVisibilityScope = null
                )
                Row {
                    FilledTonalButton(
                        onClick = {
                            viewModel.saveRcConfig()
                            showSheet = false
                        }
                    ) {
                        Text("保存")
                    }
                    OutlinedButton(
                        onClick = {
                            showSheet = false
                        }
                    ) {
                        Text("取消")
                    }
                }

            }
        }
    }

    ListDetailScreen(
        listContent = list,
        detailContent = detail
    )
}
