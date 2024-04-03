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
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SensorLive(private val application: Application) : AndroidViewModel(application) {

    private val _screenState = MutableStateFlow(Sensor())
    private lateinit var response: Unit

    @Volatile
    var screenState = _screenState.asStateFlow()
    @Volatile
    var toContinue = true

    override fun onCleared() {
        super.onCleared()
        toContinue = false
    }

    init {
        viewModelScope.launch {
            while(toContinue) {
                refreshIndicators()
                delay(500)
            }
        }
    }

    fun refreshIndicators() {
        response = Webclient.getInstance().getRetrofitInstance().getSensorData().enqueue(object : Callback<Sensor> {
            override fun onResponse(call: Call<Sensor>, response: Response<Sensor>) {
                val sensor = response.body()
                if(sensor !=null){
                    _screenState.update {
                        Sensor(
                            sensor.groundTemperature,
                            sensor.airTemperature,
                            sensor.airHumidity,
                            sensor.groundHumidity1,
                            sensor.groundHumidity2,
                            sensor.groundHumidity3
                        )
                    }
                }

            }
            override fun onFailure(call: Call<Sensor>, t: Throwable) {
                Log.v("retrofit", "call failed" +  t.cause)
            }
        })
    }

}