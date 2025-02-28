package top.cha01.geofence.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import top.cha01.geofence.data.database.entities.GeoFence


@Dao
interface GeoFenceDao {
    @Query("SELECT * FROM geo_fence")
    fun getAll(): Flow<List<GeoFence>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(geoFence: GeoFence)

    @Delete
    suspend fun delete(geoFence: GeoFence)
}

