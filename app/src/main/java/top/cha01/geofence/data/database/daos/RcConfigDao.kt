package top.cha01.geofence.data.database.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import top.cha01.geofence.data.database.entities.RcConfig

@Dao
interface RcConfigDao {
    @Query("SELECT * FROM rc_config")
    fun getAllRcConfigs(): Flow<List<RcConfig>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rcConfig: RcConfig)

    @Delete
    suspend fun delete(rcConfig: RcConfig)
}
