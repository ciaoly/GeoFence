package top.cha01.geofence

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import top.cha01.geofence.data.database.AppDatabase
import top.cha01.geofence.ui.router.Screen
import top.cha01.geofence.ui.router.appNavigation
import top.cha01.geofence.ui.router.navigationList
import top.cha01.geofence.ui.theme.GeoFenceTheme
import de.kai_morich.simple_usb_terminal.SerialListener
import java.lang.Exception
import java.util.ArrayDeque

class MainActivity : ComponentActivity(), ServiceConnection, SerialListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-db").build()

        setContent {

            GeoFenceTheme {
                val navController = rememberNavController()
                // 获取当前选中的路由
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val windowSize = with(LocalDensity.current) {
                    currentWindowSize().toSize().toDpSize()
                }
                val layoutType = if (windowSize.width >= 1200.dp) {
                    NavigationSuiteType.NavigationDrawer
                } else {
                    NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                        currentWindowAdaptiveInfo()
                    )
                }

                NavigationSuiteScaffold(
                    layoutType = layoutType,
                    navigationSuiteItems = {
                        navigationList.forEach {
                            item(
                                icon = { Icon(it.icon, contentDescription = null) },
                                label = { Text(it.title) },
                                selected = currentRoute == it.screen.route, // 根据当前路由判断是否选中
                                onClick = {
                                    navController.navigate(it.screen.route) {
                                        // 避免重复导航到同一个目的地
                                        launchSingleTop = true
                                    } },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                ) {
                    NavHost(navController, startDestination = Screen.RcConfig.route) {
                        appNavigation(db)
                    }
                }
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }

    override fun onSerialConnect() {
        TODO("Not yet implemented")
    }

    override fun onSerialConnectError(e: Exception?) {
        TODO("Not yet implemented")
    }

    override fun onSerialRead(data: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun onSerialRead(datas: ArrayDeque<ByteArray>?) {
        TODO("Not yet implemented")
    }

    override fun onSerialIoError(e: Exception?) {
        TODO("Not yet implemented")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeoFenceTheme {
    }
}