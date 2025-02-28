package top.cha01.geofence.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import top.cha01.geofence.data.database.entities.GeoFence
import top.cha01.geofence.ui.viewmodels.GeoFenceViewModel

@Composable
fun GeoFenceEditScreen(navController: NavController, viewModel: GeoFenceViewModel, id: Int?) {
    val geoFence = viewModel.geoFences.value.find { it.Id == id }
        ?: GeoFence(Id = 0, Rid = null, Name = "", Latitude = 0.0, Longitude = 0.0)

    var name by remember { mutableStateOf(geoFence.Name) }

    Column {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("名称") }
        )

        Button(onClick = {
            viewModel.addGeoFence(geoFence.copy(Name = name))
            navController.popBackStack()
        }) {
            Text("保存")
        }
    }
}