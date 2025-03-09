package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import top.cha01.geofence.data.database.entities.RcConfig
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

class RcConfigEditScreen(viewModel: RcConfigViewModel): BaseComposableDetailContent<RcConfig, RcConfigViewModel>(viewModel) {

    @Composable
    override fun itemDataFromViewModal(): RcConfig {
        val rcConfig = if (viewModel.configId >= 0) viewModel.toRcConfig() else RcConfig(Name = "", Code = "", RevertCode = "")
        return rcConfig
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun buildItemContent(
        itemData: RcConfig,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isDetailVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope?,
        animatedVisibilityScope: AnimatedVisibilityScope?
    ) {
        var name by remember { mutableStateOf(itemData.Name) }
        Column {
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("名称") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.memo,
                onValueChange = { viewModel.memo = it },
                label = { Text("备注") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.createDate,
                onValueChange = { viewModel.createDate = it },
                label = { Text("创建日期") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            OutlinedTextField(
                value = viewModel.pulseLength.toString(),
                onValueChange = { viewModel.pulseLength = it.toIntOrNull() ?: 350 },
                label = { Text("脉冲长度") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = viewModel.syncBitHigh.toString(),
                onValueChange = { viewModel.syncBitHigh = it.toIntOrNull() ?: 1 },
                label = { Text("同步位高") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = viewModel.syncBitLow.toString(),
                onValueChange = { viewModel.syncBitLow = it.toIntOrNull() ?: 31 },
                label = { Text("同步位低") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            // ... (其他 OutlinedTextField 字段)
            OutlinedTextField(
                value = viewModel.code,
                onValueChange = { viewModel.code = it },
                label = { Text("Code") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = viewModel.revertCode,
                onValueChange = { viewModel.revertCode = it },
                label = { Text("RevertCode") },
                modifier = Modifier.fillMaxWidth(),
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = viewModel.invertedSignal, onCheckedChange = { viewModel.invertedSignal = it })
                Text("信号反转")
            }
        }
    }
}
