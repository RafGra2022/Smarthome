package itjet.android.smart.http

import itjet.android.smart.greenhouse.model.Sensor
import itjet.android.smart.greenhouse.model.Settings
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IGreenhouse {

    @GET("/greenhouse/sensor")
    fun getSensorData(): Call<Sensor>

    @GET("/greenhouse/settings")
    fun getSettings(): Call<Settings>

    @POST("/greenhouse/settings")
    fun putSettings(@Body settings: Settings) : Call<String>
}