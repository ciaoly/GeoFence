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
import top.cha01.geofence.data.database.entities.RcConfig
import top.cha01.geofence.ui.viewmodels.RcConfigViewModel

@Composable
fun RcConfigEditScreen(navController: NavController, viewModel: RcConfigViewModel, id: Int?) {
    val rcConfig = viewModel.geoFences.value.find { it.Id == id }
        ?: RcConfig(Id = 0, Name = "")

    var name by remember { mutableStateOf(rcConfig.Name) }

    Column {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("名称") }
        )

        Button(onClick = {
            viewModel.addGeoFence(rcConfig.copy(Name = name))
            navController.popBackStack()
        }) {
            Text("保存")
        }
    }
}
