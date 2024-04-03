package itjet.android.smart.greenhouse.model

data class Settings(
    var auto: Boolean = false,
    var voltage: Boolean = false,
    var heating: Boolean = false,
    var minHumidity: Float = 10.0F,
    var maxHumidity: Float = 10.0F,
    var minTemperature: Int = 10,
    var updated: Boolean = false
)
