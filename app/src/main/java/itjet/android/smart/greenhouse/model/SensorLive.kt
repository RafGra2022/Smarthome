package itjet.android.smart.greenhouse.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import itjet.android.smart.http.Webclient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorLive(private val application: Application) : AndroidViewModel(application) {

    private val _screenState = MutableStateFlow(SensorData(Sensor(), SystemStatus(DeviceStatus())))

    private lateinit var response: Unit

    @Volatile
    var screenState = _screenState.asStateFlow()

    @Volatile
    var toContinue = true

    override fun onCleared() {
        super.onCleared()
        Log.i("greenhouse-refresh","refreshing stop!!")
        toContinue = false
    }

    init {
        viewModelScope.launch {
            while (toContinue) {
                refreshIndicators()
                delay(2000)
            }
        }
    }

    fun refreshIndicators() {
        Log.i("greenhouse-refresh","refreshing data")
        response = Webclient.getInstance().getRetrofitInstance(application).getSensorData()
            .enqueue(object : Callback<SensorData> {
                override fun onResponse(call: Call<SensorData>, response: Response<SensorData>) {
                    val data = response.body()
                    if (data != null) {
                        _screenState.update {
                            SensorData(
                                Sensor(
                                    data.sensors.groundTemperature,
                                    data.sensors.airTemperature,
                                    data.sensors.airHumidity,
                                    data.sensors.groundHumidity1,
                                    data.sensors.groundHumidity2,
                                    data.sensors.groundHumidity3
                                ), SystemStatus(
                                    DeviceStatus(
                                        data.systemStatus.deviceStatus.status,
                                        data.systemStatus.deviceStatus.voltage
                                    )
                                )
                            )
                        }
                    }

                }

                override fun onFailure(call: Call<SensorData>, t: Throwable) {
                    Log.v("retrofit", "call failed" + t.cause)
                    _screenState.update {
                        SensorData(
                            Sensor(
                            ), SystemStatus(
                                DeviceStatus("SERVER-DOWN")
                            )
                        )
                    }
                }
            })
    }

}