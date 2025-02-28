package top.cha01.geofence.data.database.repositories

import kotlinx.coroutines.flow.Flow
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.data.database.entities.RcConfig

class RcConfigRepository(private val rcConfigDao: RcConfigDao) {

    val allRcConfigs: Flow<List<RcConfig>> = rcConfigDao.getAllRcConfigs()

    suspend fun insert(rcConfig: RcConfig) {
        rcConfigDao.insert(rcConfig)
    }

    suspend fun delete(rcConfig: RcConfig) {
        rcConfigDao.delete(rcConfig)
    }
}
