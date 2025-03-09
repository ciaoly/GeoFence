package top.cha01.geofence.ui.router

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import top.cha01.geofence.data.database.AppDatabase
import top.cha01.geofence.libs.UsbSerial.DeviceEndpoints
import top.cha01.geofence.ui.screens.GeoFenceScreen
import top.cha01.geofence.ui.screens.RcConfigScreen

sealed class Screen(val route: String) {
    object GeoFence : Screen("geo-fence")
    object RcConfig : Screen("rc-config")
    object Settings : Screen("settings")
}

val navigationList = listOf(
    NavItem(
        title = "遥控器",
        icon = Icons.Default.Podcasts,
        Screen.RcConfig
    ),
    NavItem(
        title = "地理围栏",
        icon = Icons.Default.LocationOn,
        Screen.GeoFence
    ),
    NavItem(
        title = "设置",
        icon = Icons.Default.Settings,
        Screen.Settings
    )
)

fun NavGraphBuilder.appNavigation(db: AppDatabase, endpoints: DeviceEndpoints) {
    composable(Screen.RcConfig.route) {
        RcConfigScreen(db.rcConfigDao(), endpoints)
    }
    composable(Screen.GeoFence.route) {
        GeoFenceScreen(db.geoFenceDao())
    }
    composable(Screen.Settings.route) {

    }
}
