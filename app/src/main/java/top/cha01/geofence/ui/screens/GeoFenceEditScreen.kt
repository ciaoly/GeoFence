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
import androidx.navigation.NavController
import top.cha01.geofence.data.database.daos.GeoFenceDao
import top.cha01.geofence.data.database.entities.GeoFence
import top.cha01.geofence.ui.viewmodels.GeoFenceViewModel

class GeoFenceEditScreen(viewModel: GeoFenceViewModel): BaseComposableDetailContent<GeoFence, GeoFenceViewModel>(viewModel) {

    constructor(dao: GeoFenceDao): this(GeoFenceViewModel(dao))

    @Composable
    override fun itemDataFromViewModal(id: Int): GeoFence {
        val geoFence = viewModel.geoFences.value.find { it.Id == id }
            ?: GeoFence(Rid = null, Name = "", Latitude = 0.0, Longitude = 0.0)
        return geoFence
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun buildItemContent(
        itemData: GeoFence,
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
                viewModel.addGeoFence(itemData.copy(Name = name))
            }) {
                Text("保存")
            }
        }
    }

}