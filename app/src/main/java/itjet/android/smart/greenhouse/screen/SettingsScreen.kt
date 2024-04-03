package itjet.android.smart.greenhouse.screen

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itjet.android.smart.greenhouse.model.Settings
import itjet.android.smart.http.Webclient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class SettingsScreen(private val application: Application) {

    val client = Webclient.getInstance().getRetrofitInstance()
    private var humidity70 = false
    private var humidity60 = false
    private var humidity50 = false
    private var voltage = false
    private var mode = false
    private var heating = false
    var value = 0F

    private fun updateSettings(settings: Settings) {
        client.putSettings(settings).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("retrofit", "api call performed")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.v("retrofit", "call failed")
            }
        })
    }

    @Composable
    fun Settings(settingsState: Settings) {

        if (settingsState.voltage) {
            voltage = true
        } else if (settingsState.auto) {
            mode = true
        }
        if (settingsState.minHumidity.equals(50F)) {
            humidity50 = true
        } else if (settingsState.minHumidity.equals(60F)) {
            humidity60 = true
        } else if (settingsState.minHumidity.equals(70F)) {
            humidity70 = true
        }
        if (settingsState.heating) {
            heating = true;
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3B568D),
                                Color(0xFF151A3F),
                                Color(0xFF1B3157),
                            )
                        )
                    )
            ) {
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 150.dp, 0.dp, 0.dp)
        ) {
            val metric = LocalContext.current.getResources().getDisplayMetrics()
            val height = metric.heightPixels.toFloat()
            val width = metric.widthPixels.toFloat()
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy((width * 0.3).dp),
            ) {

                Text(text = "Tryb pracy (manual/auto)", fontSize = 25.sp, color = Color.White)
            }
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy((width * 0.3).dp),
            ) {
                Text(text = "Napięcie (wył/wł)", fontSize = 25.sp, color = Color.White)
            }
            Row(
                modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy((width * 0.3).dp),
            ) {
                Text(text = "Nawadnianie/Ogrzewanie", fontSize = 25.sp, color = Color.White)
            }

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 150.dp, 0.dp, 0.dp)
        ) {
            val metric = LocalContext.current.getResources().getDisplayMetrics()
            Row(modifier = Modifier.padding(10.dp)) {
                Spacer(Modifier.weight(1f))
                SwitchMode(settingsState)
            }
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Spacer(Modifier.weight(1f))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(150.dp, 350.dp, 0.dp, 0.dp)
        ) {
            val metric = LocalContext.current.getResources().getDisplayMetrics()
            val width = metric.widthPixels.toFloat()
            Row(
                horizontalArrangement = Arrangement.spacedBy((width * 0.3).dp)

            ) {
                Text(text = "Wilgotność", fontSize = 25.sp, color = Color.White)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 400.dp, 0.dp, 0.dp)
        ) {
            Row(
                Modifier.padding(10.dp)
            ) {
                Text(text = "50%-60%", fontSize = 25.sp, color = Color.White)
            }
            Row(
                Modifier.padding(10.dp)
            ) {
                Text(text = "60%-70%", fontSize = 25.sp, color = Color.White)
            }
            Row(
                Modifier.padding(10.dp)
            ) {
                Text(text = "70%-85%", fontSize = 25.sp, color = Color.White)
            }

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 400.dp, 0.dp, 0.dp)
        ) {
            Row(
                Modifier.padding(10.dp)
            ) {
                Spacer(Modifier.weight(1f))
                SwitchHumidity50(settingsState)
            }

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 600.dp, 0.dp, 0.dp)
        ) {
            Row(
            ) {
                MinimalTemperature(settingsState)
            }

        }


    }

    @Composable
    fun MinimalTemperature(settingsState: Settings) {
        var sliderPosition by remember { mutableFloatStateOf(settingsState.minTemperature.toFloat()) }
        Column() {
            Row(Modifier.padding(100.dp, 0.dp, 0.dp, 0.dp)) {
                Text(text = "Temperatura minimalna", color = Color.White, fontSize = 20.sp)
            }
            Row() {
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        value = it
                    },
                    onValueChangeFinished = {
                        settingsState.minTemperature = value.toInt()
                        updateSettings(settingsState)
                    },
                    valueRange = 0f..20f
                )
            }
            Row(Modifier.padding(200.dp, 0.dp, 0.dp, 0.dp)) {
                Text(
                    text = sliderPosition.toInt().toString() + "°C",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }

    @Composable
    fun SwitchHumidity50(settingsState: Settings) {
        var humidity5 by remember { mutableStateOf(humidity50) }
        var humidity6 by remember { mutableStateOf(humidity60) }
        var humidity7 by remember { mutableStateOf(humidity70) }
        Column {

            Row() {
                Switch(
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Green,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Red,
                    ),
                    checked = humidity5,
                    onCheckedChange = {
                        humidity5 = it
                        humidity50 = it
                        humidity60 = false
                        humidity70 = false
                        humidity6 = false
                        humidity7 = false
                        settingsState.minHumidity = 50F
                        settingsState.maxHumidity = 60F
                        updateSettings(settingsState)
                    }
                )
            }
            Row() {
                Switch(
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Green,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Red,
                    ),
                    checked = humidity6,
                    onCheckedChange = {
                        humidity6 = it
                        humidity60 = it
                        humidity7 = false
                        humidity70 = false
                        humidity5 = false
                        humidity50 = false
                        settingsState.minHumidity = 60F
                        settingsState.maxHumidity = 70F
                        updateSettings(settingsState)
                    }
                )
            }
            Row() {
                Switch(
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Green,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Red,
                    ),
                    checked = humidity7,
                    onCheckedChange = {
                        humidity7 = it
                        humidity70 = it
                        humidity50 = false
                        humidity60 = false
                        humidity5 = false
                        humidity6 = false
                        settingsState.minHumidity = 70F
                        settingsState.maxHumidity = 85F
                        updateSettings(settingsState)
                    }
                )
            }
        }
    }

    @Composable
    fun SwitchMode(settingsState: Settings) {
        var volt by remember { mutableStateOf(settingsState.voltage) }
        var mod by remember { mutableStateOf(settingsState.auto) }
        var heat by remember { mutableStateOf(heating) }
        Column() {
            Switch(
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Green,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Red,
                ),
                checked = mod,
                onCheckedChange = {
                    mod = it
                    settingsState.auto = it
                    if (it) {
                        settingsState.voltage = false
                        volt = false
                        voltage = false
                    }
                    updateSettings(settingsState)
                }
            )
            Switch(
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Red,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Green,
                ),
                checked = volt,
                enabled = !mod,
                onCheckedChange = {
                    volt = it
                    settingsState.voltage = it
                    updateSettings(settingsState)
                }
            )
            Switch(
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Red,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Blue,
                ),
                checked = heat,
                onCheckedChange = {
                    heat = it
                    settingsState.heating = it
                    updateSettings(settingsState)
                }
            )
        }
    }
}