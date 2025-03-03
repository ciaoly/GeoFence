package top.cha01.geofence.data.database.entities

import androidx.room.PrimaryKey
import top.cha01.geofence.ui.screens.BaseListItemType

abstract class BaseEntity: BaseListItemType {
    @PrimaryKey(autoGenerate = true)
    override var Id: Int = 0
}