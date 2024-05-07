package itjet.android.smart.greenhouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import itjet.android.smart.greenhouse.model.SettingsLive
import itjet.android.smart.greenhouse.screen.IndicatorScreen
import itjet.android.smart.greenhouse.screen.PowerGraphScreen
import itjet.android.smart.greenhouse.screen.SettingsScreen

sealed class Destination(val route: String) {
    object IndicatorScreen : Destination("Indicator")
    object Settings : Destination("Settings")
    object PowerGraphScreen : Destination("Power")
}

class GreenhouseActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this
        val indicators = IndicatorScreen(activity)
        setContent() {
            val settings = SettingsScreen(application)
            val powerScreen = PowerGraphScreen(applicationContext)
            val vm = viewModel<SettingsLive>()
            val screenState by vm.settingsState.collectAsState()
            val settingsState = screenState
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Destination.IndicatorScreen.route
            ) {
                composable(Destination.IndicatorScreen.route) { indicators.Indicators(navController = navController) }
                composable(Destination.Settings.route) { settings.Settings(settingsState) }
                composable(Destination.PowerGraphScreen.route) { powerScreen.Screen() }
            }
        }

    }

}
