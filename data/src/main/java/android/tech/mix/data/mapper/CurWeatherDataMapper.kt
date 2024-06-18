package android.tech.mix.data.mapper

import android.tech.mix.data.network.response.OpenWeatherApiResponse
import android.tech.mix.domain.mapper.Transformer
import android.tech.mix.domain.model.Notification
import android.tech.mix.domain.model.WeatherInfo
import javax.inject.Inject

class CurWeatherDataMapper @Inject constructor() :
    Transformer<OpenWeatherApiResponse, List<WeatherInfo>> {

    override fun mapObject(data: OpenWeatherApiResponse): List<WeatherInfo> {

        val currentWeather = data.currentWeather

        return listOf(
            WeatherInfo(
                date = currentWeather.date,
                probability = "${currentWeather.pop * 100} %",
                lowTemperature = currentWeather.temperature,
                highTemperature = currentWeather.temperature,
                feelsLikeTemperature = currentWeather.feelsLikeTemperature,
                dayIcon = currentWeather.weatherImages[0].getDayThemeImageUrl(),
                nightIcon = currentWeather.weatherImages[0].getNightThemeImageUrl(),
                description = currentWeather.weatherImages[0].description,
                title = currentWeather.weatherImages[0].main,
                visibility = currentWeather.visibility.toDouble(),
                dewPoint = currentWeather.dewPoint,
                uvIndex = currentWeather.uvIndex,
                pressure = currentWeather.pressure.toDouble(),
                humidity = currentWeather.humidity,
                windSpeed = currentWeather.windSpeed,
                precipitation = currentWeather.rain?.get("1h")?.asDouble ?: 0.0,
                alerts = data.alerts?.map {
                    Notification(
                        sender = it.senderName ?: "",
                        startTime = it.start ?: 0,
                        endTime = it.end ?: 0,
                        eventType = it.event ?: "",
                        details = it.description,
                        tagsList = it.tags,
                    )
                }
            )
        )
    }
}