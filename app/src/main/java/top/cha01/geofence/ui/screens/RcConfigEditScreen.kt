package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.data.database.entities.RcConfig
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

class RcConfigEditScreen(viewModel: RcConfigViewModel): BaseComposableDetailContent<RcConfig, RcConfigViewModel>(viewModel) {

    constructor(dao: RcConfigDao): this(RcConfigViewModel(dao))

    @Composable
    override fun itemDataFromViewModal(id: Int): RcConfig {
        val rcConfig = viewModel.rcConfigList.value.find { it.Id == id }
            ?: RcConfig(Name = "", Code = "", RevertCode = "")
        return rcConfig
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun buildItemContent(
        itemData: RcConfig,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isDetailVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {
        var name by remember { mutableStateOf(itemData.Name) }
        Column {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("名称") }
            )

            Button(onClick = {
                viewModel.addRcConfig(itemData.copy(Name = name))
            }) {
                Text("保存")
            }
        }
    }
}
