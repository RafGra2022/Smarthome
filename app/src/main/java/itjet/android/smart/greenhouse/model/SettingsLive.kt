package itjet.android.smart.greenhouse.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import itjet.android.smart.http.Webclient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsLive(private val application: Application) : AndroidViewModel(application) {

    private val _settingsState = MutableStateFlow(Settings())

    @Volatile
    var settingsState = _settingsState.asStateFlow()

    init {
        viewModelScope.launch {
            refreshSettings()
        }
    }

    fun refreshSettings() {
        Webclient.getInstance().getRetrofitInstance(application).getSettings().enqueue(object : Callback<Settings> {
            override fun onResponse(call: Call<Settings>, response: Response<Settings>) {
                val settings = response.body()
                if(settings !=null){
                    settings.updated = true
                    _settingsState.update {
                        Settings(
                            settings.auto,
                            settings.voltage,
                            settings.heating,
                            settings.minHumidity,
                            settings.maxHumidity,
                            settings.minTemperature,
                            settings.updated
                        )
                    }
                }

            }
            override fun onFailure(call: Call<Settings>, t: Throwable) {
                Log.v("retrofit", "call failed")
            }
        })
    }

}