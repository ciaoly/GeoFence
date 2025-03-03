package top.cha01.geofence.data.database.entities
import androidx.room.*

@Entity(
    tableName = "geo_fence",
    foreignKeys = [ForeignKey(
        entity = RcConfig::class,
        parentColumns = ["Id"],
        childColumns = ["Rid"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["Rid"])]
)

data class GeoFence(
    val Rid: Int?,
    val Name: String,
    val Memo: String = "",
    val CreateDate: String = "",
    val OrderNo: Int = 0,
    val Radius: Float = 3f,
    val Latitude: Double,
    val Longitude: Double
): BaseEntity()
