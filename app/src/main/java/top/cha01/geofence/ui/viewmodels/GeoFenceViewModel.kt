package top.cha01.geofence.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.cha01.geofence.data.database.daos.GeoFenceDao
import top.cha01.geofence.data.database.entities.GeoFence

class GeoFenceViewModel(private val dao: GeoFenceDao) : ViewModel() {
    val geoFences: StateFlow<List<GeoFence>> = dao.getAll().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    fun addGeoFence(geoFence: GeoFence) {
        viewModelScope.launch {
            dao.insert(geoFence)
        }
    }

    fun deleteGeoFence(geoFence: GeoFence) {
        viewModelScope.launch {
            dao.delete(geoFence)
        }
    }
}
