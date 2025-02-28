package top.cha01.geofence

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import top.cha01.geofence.data.database.AppDatabase
import top.cha01.geofence.ui.theme.GeoFenceTheme
import top.cha01.geofence.ui.viewmodels.GeoFenceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-db").build()
        val geoFenceViewModel = GeoFenceViewModel(db.geoFenceDao())

        setContent {
            val navController = rememberNavController()
            AppNavHost(navController, geoFenceViewModel)
        }
        setContent {
            GeoFenceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeoFenceTheme {
        Greeting("Android")
    }
}