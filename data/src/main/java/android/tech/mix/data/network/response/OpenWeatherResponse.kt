package android.tech.mix.data.network.response

import android.tech.mix.data.network.OpenWeatherApiService
import android.tech.mix.domain.model.CityInfo
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class WeatherAlert(
    @SerializedName("sender_name")
    val senderName: String?,
    @SerializedName("event")
    val event: String?,
    @SerializedName("start")
    val start: Long?,
    @SerializedName("end")
    val end: Long?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("tags")
    val tags: List<String>?,
)

data class WeatherCity(
    @SerializedName("name")
    val name: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double,
    @SerializedName("state")
    val state: String?,
    @SerializedName("country")
    val country: String
) {
    fun toCityModel(): CityInfo {
        return CityInfo(
            name,
            "",
            1,
            country,
            state ?: "",
            latitude,
            longitude
        )
    }
}

data class WeatherImage(
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
) {
    fun getDayThemeImageUrl(): String {
        return String.format(OpenWeatherApiService.BASE_IMAGE_URL, icon)
    }

    fun getNightThemeImageUrl(): String {
        return String.format(OpenWeatherApiService.BASE_IMAGE_URL, icon)
    }
}


data class WeatherTemperature(
    @SerializedName("day")
    val day: Double?,
    @SerializedName("min")
    val min: Double?,
    @SerializedName("max")
    val max: Double?,
    @SerializedName("night")
    val night: Double?,
    @SerializedName("eve")
    val eve: Double?,
    @SerializedName("morn")
    val morn: Double?
)

data class HourlyWeatherData(
    @SerializedName("dt")
    val date: Long,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long,
    @SerializedName("moonrise")
    val moonrise: Long,
    @SerializedName("moonset")
    val moonset: Long,
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("feels_like")
    val feelsLikeTemperature: Double,
    @SerializedName("weather")
    val weatherImages: List<WeatherImage>,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_deg")
    val windDegree: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("uvi")
    val uvIndex: Double,
    @SerializedName("dew_point")
    val dewPoint: Double,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("clouds")
    val clouds: Int,
    @SerializedName("rain")
    val rain: JsonObject? = null,
    @SerializedName("snow")
    val snow: JsonObject? = null,
    @SerializedName("pop")
    val pop: Double
)

data class DailyWeatherData(
    @SerializedName("dt")
    val date: Long,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long,
    @SerializedName("moonrise")
    val moonrise: Long,
    @SerializedName("moonset")
    val moonset: Long,
    @SerializedName("temp")
    val temperature: WeatherTemperature,
    @SerializedName("feels_like")
    val feelsLikeTemperature: WeatherTemperature,
    @SerializedName("weather")
    val weatherImages: List<WeatherImage>,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_deg")
    val windDegree: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("pop")
    val pop: Double
)

data class OpenWeatherApiResponse(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double,
    @SerializedName("current")
    val currentWeather: HourlyWeatherData,
    @SerializedName("hourly")
    val hourlyWeather: List<HourlyWeatherData>? = null,
    @SerializedName("daily")
    val dailyWeather: List<DailyWeatherData>? = null,
    @SerializedName("alerts")
    val alerts: List<WeatherAlert>? = null
)