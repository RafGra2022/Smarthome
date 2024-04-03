package itjet.android.smart.greenhouse.model

import java.time.LocalDate
import java.time.LocalTime

data class WeatherData(
    val date: LocalDate = LocalDate.now(),
    val highTemperature: Int = 9,
    val lowTemperature: Int = -2,
    val precipValue: Float = 0F,
    val fenomen: String = "pochmurno",
    val sunrise: LocalTime = LocalTime.now(),
    val sunset: LocalTime = LocalTime.now()
)
