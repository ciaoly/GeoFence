package top.cha01.geofence.data.database.entities
import androidx.room.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "rc_config")
data class RcConfig(
    val Name: String,
    val Memo: String = "",
    val CreateDate: String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
    val PulseLength: Int = 350,
    val SyncBitHigh: Int = 1,
    val SyncBitLow: Int = 31,
    val ZeroBitHigh: Int = 1,
    val ZeroBitLow: Int = 3,
    val OneBitHigh: Int = 3,
    val OneBitLow: Int = 1,
    val InvertedSignal: Boolean = false,
    val RepeatTimes: Int = 3,
    val RepeatWaitTime: Int = 0,
    val Code: String,
    val RevertCode: String = ""
): BaseEntity()
