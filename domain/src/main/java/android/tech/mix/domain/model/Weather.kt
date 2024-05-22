package android.tech.mix.domain.model

data class CityInfo(
    val cityName: String,
    val cityKey: String,
    val cityRank: Int,
    val countryName: String,
    val stateName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var isFavorite: Boolean = false
) {
    fun isValidCity(): Boolean {
        return latitude != null && longitude != null
    }

    fun getFormattedCityName(): String {
        return "$cityName, $countryName"
    }
}

data class WeatherForecastInfo(
    val headlines: String,
    val weatherForecasts: List<WeatherInfo>
)

data class WeatherInfo(
    val date: Long,
    val probability: String,
    val lowTemperature: Double,
    val highTemperature: Double,
    val dayIcon: String,
    val nightIcon: String,
    val description: String,
    val title: String? = null,
    val alerts: List<Notification>? = null,
    val feelsLikeTemperature: Double = 0.0,
    val windSpeed: Double = 0.0,
    val humidity: Int = 0,
    val uvIndex: Double = 0.0,
    val pressure: Double = 0.0,
    val visibility: Double = 0.0,
    val dewPoint: Double = 0.0,
    val precipitation: Double = 0.0,
)

data class WeatherTypeData(
    val type: WeatherDataType,
    val weatherData: List<WeatherInfo>
)
