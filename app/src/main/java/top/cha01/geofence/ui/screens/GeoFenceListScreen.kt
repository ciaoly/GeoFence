package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.cha01.geofence.data.database.daos.GeoFenceDao
import top.cha01.geofence.data.database.entities.GeoFence
import top.cha01.geofence.ui.viewmodels.GeoFenceViewModel

class GeoFenceListScreen(viewModel: GeoFenceViewModel): BaseComposableListContent<GeoFence, GeoFenceViewModel>(viewModel) {

    constructor(dao: GeoFenceDao): this(GeoFenceViewModel(dao))

    @Composable
    override fun itemDataFromViewModal(): List<GeoFence> {
        val geoFences by viewModel.geoFences.collectAsState(initial = emptyList())
        return geoFences
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun buildItemContent(
        itemData: GeoFence,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isListVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {
        ListItem(
            headlineContent = { Text(itemData.Name) },
            supportingContent = { Text("半径: ${itemData.Radius}m") },
            modifier = modifier
        )
    }
}
