package top.cha01.geofence.data.database.repositories

import kotlinx.coroutines.flow.Flow
import top.cha01.geofence.data.database.daos.GeoFenceDao
import top.cha01.geofence.data.database.entities.GeoFence

class GeoFenceRepository(private val geoFenceDao: GeoFenceDao) {

    val allGeoFences: Flow<List<GeoFence>> = geoFenceDao.getAll()

    suspend fun insert(geoFence: GeoFence) {
        geoFenceDao.insert(geoFence)
    }

    suspend fun delete(geoFence: GeoFence) {
        geoFenceDao.delete(geoFence)
    }
}
