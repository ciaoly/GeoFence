package top.cha01.geofence.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavController
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

@Composable
fun RcConfigListScreen(navController: NavController, viewModel: RcConfigViewModel) {
    val geoFences by viewModel.geoFences.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("edit_geo_fence") }) {
                Icon(Icons.Default.Add, contentDescription = "添加")
            }
        }
    ) {
            padding -> LazyColumn(
        modifier = Modifier.fillMaxHeight().padding(padding),
        contentPadding = PaddingValues(6.dp)) {
        items(geoFences) { geoFence ->
            ListItem(
                headlineContent = { Text(geoFence.Name) },
                modifier = Modifier.clickable {
                    navController.navigate("edit_geo_fence/${geoFence.Id}")
                }
            )
        }
    }
    }
}