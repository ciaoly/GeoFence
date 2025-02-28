package top.cha01.geofence.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.data.database.entities.RcConfig

class RcConfigViewModel(private val dao: RcConfigDao) : ViewModel() {
    val geoFences: StateFlow<List<RcConfig>> = dao.getAllRcConfigs().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    fun addGeoFence(rcConfig: RcConfig) {
        viewModelScope.launch {
            dao.insert(rcConfig)
        }
    }

    fun deleteGeoFence(rcConfig: RcConfig) {
        viewModelScope.launch {
            dao.delete(rcConfig)
        }
    }
}
