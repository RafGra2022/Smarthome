package itjet.android.smart.greenhouse

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import itjet.android.smart.R
import itjet.android.smart.greenhouse.model.SettingsLive
import itjet.android.smart.greenhouse.screen.IndicatorScreen
import itjet.android.smart.greenhouse.screen.SettingsScreen

sealed class Destination(val route: String) {
    object IndicatorScreen : Destination("Indicator")
    object Settings : Destination("Settings")
}

class GreenhouseActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this
        val indi: IndicatorScreen = IndicatorScreen(activity)
        setContent() {
            val settings = SettingsScreen(application)
            val vm = viewModel<SettingsLive>()
            val screenState by vm.settingsState.collectAsState()
            val settingsState = screenState
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Destination.IndicatorScreen.route
            ) {
                composable(Destination.IndicatorScreen.route) { indi.Indicators(navController = navController) }
                composable(Destination.Settings.route) { settings.Settings(settingsState) }
            }
        }

    }


    private fun WiFiName(context: Context): String {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(networkInfo)
        if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
//            val wifiInfo = capabilities.transportInfo as WifiInfo
        }
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.connectionInfo.ssid
    }

}
