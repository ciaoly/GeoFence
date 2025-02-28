package top.cha01.geofence.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import top.cha01.geofence.data.database.daos.GeoFenceDao
import top.cha01.geofence.data.database.daos.RcConfigDao
import top.cha01.geofence.data.database.entities.GeoFence
import top.cha01.geofence.data.database.entities.RcConfig

@Database(entities = [GeoFence::class, RcConfig::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun geoFenceDao(): GeoFenceDao
    abstract fun rcConfigDao(): RcConfigDao
}