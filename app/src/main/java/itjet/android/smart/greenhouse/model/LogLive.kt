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

class LogLive(private val application: Application) : AndroidViewModel(application) {

    private val _screenState = MutableStateFlow(PowerUsage())

    @Volatile
    var screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            logs()
        }
    }


    fun logs(){
        val client = Webclient()
        client.getRetrofitInstance(application).getLogs().enqueue(object :
            Callback<PowerUsage> {
            override fun onResponse(
                call: Call<PowerUsage>,
                response: Response<PowerUsage>
            ) {
                val data = response.body()
                if (data != null) {
                    //body
                    _screenState.update {
                        PowerUsage(
                            data.powerUsage
                        )
                    }

                }
            }

            override fun onFailure(call: Call<PowerUsage>, t: Throwable) {
                Log.v("retrofit", "call failed" + t.cause)
            }
        })
    }
}